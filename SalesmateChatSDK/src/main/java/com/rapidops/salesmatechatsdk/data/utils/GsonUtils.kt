package com.rapidops.salesmatechatsdk.data.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rapidops.salesmatechatsdk.data.ds.*
import com.rapidops.salesmatechatsdk.data.resmodels.*
import com.rapidops.salesmatechatsdk.domain.models.*
import com.rapidops.salesmatechatsdk.domain.models.events.ChatNewMessage
import com.rapidops.salesmatechatsdk.domain.models.message.*

internal object GsonUtils {

    val gson: Gson by lazy {
        val gsonBuilder = GsonBuilder()
        registerTypeAdapters(gsonBuilder)
        gsonBuilder.create()
    }

    private fun registerTypeAdapters(gsonBuilder: GsonBuilder) {
        gsonBuilder.registerTypeAdapter(Error::class.java, ErrorDs())

        //Ping
        gsonBuilder.registerTypeAdapter(PingRes::class.java, PingDs())
        gsonBuilder.registerTypeAdapter(Availability::class.java, AvailabilityDs())
        gsonBuilder.registerTypeAdapter(OfficeHour::class.java, OfficeHoursDs())
        gsonBuilder.registerTypeAdapter(LookAndFeel::class.java, LookAndFeelDs())
        gsonBuilder.registerTypeAdapter(Misc::class.java, MiscDs())
        gsonBuilder.registerTypeAdapter(
            ConversationsSettings::class.java,
            ConversationsSettingsDs()
        )
        gsonBuilder.registerTypeAdapter(WelcomeMessage::class.java, WelcomeMessagesDs())
        gsonBuilder.registerTypeAdapter(SecuritySettings::class.java, SecuritySettingsDs())
        gsonBuilder.registerTypeAdapter(WorkspaceData::class.java, WorkspaceDataDs())
        gsonBuilder.registerTypeAdapter(User::class.java, UserDs())
        gsonBuilder.registerTypeAdapter(Location::class.java, LocationDs())
        gsonBuilder.registerTypeAdapter(EmojiMapping::class.java, EmojiMappingDs())
        gsonBuilder.registerTypeAdapter(GenerateTokenRes::class.java, GenerateTokenResDs())
        gsonBuilder.registerTypeAdapter(Channel::class.java, ChannelDs())
        gsonBuilder.registerTypeAdapter(ChannelsItem::class.java, ChannelsItemDs())
        gsonBuilder.registerTypeAdapter(Events::class.java, EventsDs())

        //Conversation
        gsonBuilder.registerTypeAdapter(ConversationRes::class.java, ConversationResDs())
        gsonBuilder.registerTypeAdapter(Conversations::class.java, ConversationDs())
        gsonBuilder.registerTypeAdapter(LastMessageData::class.java, LastMessageDataDs())
        gsonBuilder.registerTypeAdapter(ContactData::class.java, ContactDataDs())
        gsonBuilder.registerTypeAdapter(Owner::class.java, OwnerDs())
        gsonBuilder.registerTypeAdapter(UserAvailability::class.java, UserAvailabilityDs())
        gsonBuilder.registerTypeAdapter(
            ConversationDetailRes::class.java,
            ConversationDetailResDs()
        )

        //Messages
        gsonBuilder.registerTypeAdapter(MessageListRes::class.java, MessageListResDs())
        gsonBuilder.registerTypeAdapter(SourceMeta::class.java, SourceMetaDs())
        gsonBuilder.registerTypeAdapter(MessageItem::class.java, MessageItemDs())
        gsonBuilder.registerTypeAdapter(BlockDataItem::class.java, BlockDataItemDs())
        gsonBuilder.registerTypeAdapter(FileAttachmentData::class.java, FileAttachmentDataDs())
        gsonBuilder.registerTypeAdapter(TextBlockDataItem::class.java, TextBlockDataItemDs())
        gsonBuilder.registerTypeAdapter(FileBlockDataItem::class.java, FileBlockDataItemDs())
        gsonBuilder.registerTypeAdapter(ImageBlockDataItem::class.java, ImageBlockDataItemDs())
        gsonBuilder.registerTypeAdapter(ReferenceTeam::class.java, ReferenceTeamDs())
        gsonBuilder.registerTypeAdapter(ReferenceUser::class.java, ReferenceUserDs())

        gsonBuilder.registerTypeAdapter(SendMessageRes::class.java, SendMessageResDs())


        //Events
        gsonBuilder.registerTypeAdapter(ChatNewMessage::class.java, ChatNewMessageDs())
    }
}
