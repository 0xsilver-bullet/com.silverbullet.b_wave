package com.silverbullet.feature_profile.route

import com.silverbullet.core.utils.exceptions.InvalidRequestBody
import com.silverbullet.core.utils.exceptions.UnexpectedServiceError
import com.silverbullet.feature_profile.ProfileController
import com.silverbullet.feature_profile.request.EditProfileRequest
import com.silverbullet.feature_profile.response.EditProfileResponse
import com.silverbullet.utils.userId
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.editProfileRoute(controller: ProfileController) {

    authenticate {

        post("edit") {

            val userId = call.userId ?: throw UnexpectedServiceError()

            val multipartData = call.receiveMultipart()
            lateinit var editProfileRequest: EditProfileRequest
            var extension: String? = null
            var fileBytes: ByteArray? = null

            // This is some explanation for the code above.
            // you might wonder why editProfileRequest is just a late init var and (extension, fileBytes)
            // has to be nullable and not just a late init var.
            // The point is after receiving the request and parsing it's inputs, we need to process this data,
            // and we will not reach the point of processing this  data if editProfileRequest is initialized and this
            // is guaranteed.
            // on the other side we might reach the point of processing without initializing extension,file Bytes,
            // because the user might only change the name and not upload new pic.
            // in this case they will be null.
            // I just assigned editProfilePic as late init to avoid null check.

            multipartData.forEachPart { part ->

                when (part) {

                    is PartData.FormItem -> {
                        if (part.name == null || part.name != "form")
                            throw InvalidRequestBody()
                        try {
                            editProfileRequest = Json.decodeFromString(part.value)
                        } catch (e: Exception) {
                            throw InvalidRequestBody()
                        }
                    }

                    is PartData.FileItem -> {
                        extension = part.originalFileName?.takeLastWhile { it != '.' } ?: throw InvalidRequestBody()
                        fileBytes = part.streamProvider().readBytes()
                    }

                    else -> Unit
                }
                part.dispose()
            }

            val newUserInfo = controller
                .processEditProfileRequest(
                    userId = userId,
                    request = editProfileRequest,
                    profilePicData = if (extension != null && fileBytes != null)
                        Pair(extension!!, fileBytes!!)
                    else
                        null
                )
            val response = EditProfileResponse(newUserInfo)
            call.respond(response)
        }

    }
}