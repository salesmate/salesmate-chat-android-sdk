package com.rapidops.salesmatechatsdk.data.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rapidops.salesmatechatsdk.data.ds.*
import com.rapidops.salesmatechatsdk.data.resmodels.ConversationRes
import com.rapidops.salesmatechatsdk.data.resmodels.GenerateTokenRes
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.domain.models.*

internal object GsonUtils {

    val gson: Gson by lazy {
        val gsonBuilder = GsonBuilder()
        registerTypeAdapters(gsonBuilder)
        gsonBuilder.create()
    }

    private fun registerTypeAdapters(gsonBuilder: GsonBuilder) {
        gsonBuilder.registerTypeAdapter(Error::class.java, ErrorDs())
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
        gsonBuilder.registerTypeAdapter(ConversationRes::class.java, ConversationResDs())
        gsonBuilder.registerTypeAdapter(Conversations::class.java, ConversationDs())
        gsonBuilder.registerTypeAdapter(LastMessageData::class.java, EventsDs())

    }
}
