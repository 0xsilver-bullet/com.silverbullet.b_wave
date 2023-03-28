package com.silverbullet.feature_profile

import com.silverbullet.core.databse.dao.UserDao
import com.silverbullet.core.mapper.toUserInfo
import com.silverbullet.core.model.UserInfo
import com.silverbullet.feature_auth.exception.UserNotFound
import com.silverbullet.feature_profile.request.EditProfileRequest
import java.io.File
import java.util.UUID

class ProfileController(private val userDao: UserDao) {

    suspend fun processEditProfileRequest(
        userId: Int,
        request: EditProfileRequest,
        profilePicData: Pair<String, ByteArray>? // (extension , bytes)
    ): UserInfo {
        val user = userDao.getUserById(userId).data ?: throw UserNotFound()
        if (profilePicData == null) {
            // user just want to change basic information.
            return userDao
                .updateUser(
                    userId = userId,
                    name = request.name,
                    profilePicUrl = null,
                    profilePicLocalPath = null
                )
                .data!! // it will not be null because we have just queried using the same id
                .toUserInfo()
        } else {
            // Then user has wants to upload an image
            val (filePath, newUrl) = saveProfilePic(profilePicData)

            user.profilePicLocalPath?.let { profilePicLocalPath ->
                // then user have a previous image, and we need to delete it.
                File(profilePicLocalPath).apply {
                    if (exists()) {
                        delete()
                    }
                }
            }

            return userDao
                .updateUser(
                    userId = userId,
                    name = request.name,
                    profilePicUrl = newUrl,
                    profilePicLocalPath = filePath
                )
                .data!! // it will not be null because we have just queried using the same id
                .toUserInfo()
        }
    }

    /**
     * @param profilePicData a pair of image extension (JPG, PNG, ..) and image byte array
     * @return pair of (new profile pic local path, the new url)
     */
    private fun saveProfilePic(profilePicData: Pair<String, ByteArray>): Pair<String, String> {
        val extension = profilePicData.first
        val fileBytes = profilePicData.second

        val fileName = "${UUID.randomUUID()}.$extension"
        val filePath = "uploads/$fileName"
        val newProfilePicUrl = "http://192.168.1.2:8080/$fileName"

        // save the image file
        File(filePath).writeBytes(fileBytes)

        return Pair(filePath, newProfilePicUrl)
    }
}