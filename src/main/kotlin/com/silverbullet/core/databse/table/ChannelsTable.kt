package com.silverbullet.core.databse.table

import com.silverbullet.core.databse.utils.ChannelType
import org.jetbrains.exposed.dao.id.IntIdTable

object ChannelsTable: IntIdTable("channels") {

    val name = varchar("name",64)
        .nullable()
    val type = enumeration("type",ChannelType::class)

}