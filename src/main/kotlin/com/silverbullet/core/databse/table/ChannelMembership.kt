package com.silverbullet.core.databse.table

import com.silverbullet.core.databse.utils.ChannelType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction

object ChannelMembership : Table("channels_memberships") {

    val channelId = integer("channel_id")
        .references(
            ChannelsTable.id,
            onDelete = ReferenceOption.CASCADE
        )

    val memberId = integer("member_id")
        .references(
            UsersTable.id,
            onDelete = ReferenceOption.CASCADE
        )

    override val primaryKey = PrimaryKey(channelId, memberId)

    // this trigger is used to ensure that DM Channel has only 2 memberships and if not throws error.
    private val functionQuery = """
            CREATE FUNCTION check_max_dm_membership()
                RETURNS TRIGGER
                LANGUAGE PLPGSQL
            AS $$
            BEGIN
                IF (SELECT type FROM channels WHERE id = NEW.channel_id)  = ${ChannelType.DmChannel.ordinal} THEN
                    IF (SELECT COUNT(*) FROM channels_memberships WHERE channel_id = NEW.channel_id) >= 2 THEN
                        RAISE EXCEPTION 'Maximum membership limit reached';
                    END IF;
                END IF;
                RETURN NEW;
            END
            $$
        """.trimIndent()

    private val triggerQuery = """ 
            CREATE TRIGGER max_dm_membership BEFORE INSERT ON channels_memberships
            FOR EACH ROW
            EXECUTE FUNCTION check_max_dm_membership();
        """.trimIndent()

    fun Transaction.initializeChannelsMembershipsTableTrigger(){
        exec(functionQuery)
        exec(triggerQuery)
    }
}