package com.rapidops.salesmatechatsdk.app.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.utils.getJsonObject
import com.rapidops.salesmatechatsdk.data.utils.getString
import com.rapidops.salesmatechatsdk.data.utils.hasProperty
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import com.rapidops.salesmatechatsdk.domain.models.PublishType
import com.rapidops.salesmatechatsdk.domain.models.UserAvailability
import com.rapidops.salesmatechatsdk.domain.models.events.ChatNewMessage
import com.rapidops.salesmatechatsdk.domain.models.events.TypingMessage
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import io.github.sac.BasicListener
import io.github.sac.ReconnectStrategy
import io.github.sac.Socket
import org.json.JSONObject
import javax.inject.Inject

internal class SocketController @Inject constructor(
    private val appSettingsDataSource: IAppSettingsDataSource,
) {
    private val TAG = "SocketController"
    private var _socket: Socket? = null
    private val eventBus = EventBus
    private val gson: Gson = GsonUtils.gson
    fun connect() {
        if (appSettingsDataSource.linkName.isNotEmpty()) {
            Log.v(TAG, "LinkName " + appSettingsDataSource.linkName)
            if (_socket != null && _socket?.isconnected()?.not() == true) {
                _socket?.disconnect()
                _socket?.connectAsync()
            } else if (_socket == null) {
                val socketClusterUrl =
                    "wss://" + appSettingsDataSource.salesMateChatSetting.tenantId + "/socketcluster/"
                _socket = Socket(socketClusterUrl)
                _socket?.seturl(socketClusterUrl)
                _socket?.setReconnection(ReconnectStrategy().setDelay(30000).setMaxAttempts(100))
                _socket?.setAuthToken(appSettingsDataSource.accessToken)
                _socket?.connectAsync()
                if (BuildConfig.DEBUG) {
                    _socket?.disableLogging()
                }
                _socket?.setListener(object : BasicListener {
                    override fun onConnected(socket: Socket, headers: Map<String, List<String>>) {
                        Log.v(TAG, "Connected to endpoint")
                    }

                    override fun onDisconnected(
                        socket: Socket,
                        serverCloseFrame: WebSocketFrame,
                        clientCloseFrame: WebSocketFrame,
                        closedByServer: Boolean
                    ) {
                        Log.v(TAG, "Disconnected from end-point")
                        /*_socket=null;
                        connect();*/
                    }

                    override fun onConnectError(socket: Socket, exception: WebSocketException) {
                        Log.v(TAG, "Got connect error $exception")
                    }

                    override fun onAuthentication(socket: Socket, status: Boolean) {
                        if (status) {
                            subscribeContactUnVerifiedChannel()
                            subScribeTenantSpecificChannel()
                        }
                        if (status) {
                            Log.v(TAG, "socket is authenticated")
                        } else {
                            Log.v(TAG, "Authentication is required (optional)")
                        }
                    }

                    override fun onSetAuthToken(token: String, socket: Socket) {
                        Log.v(TAG, "Token is $token")
                    }
                })
            }
        } else {
            Log.v(TAG, "LinkName empty")
        }
    }

    private val isSocketConnected: Boolean
        get() = _socket != null && _socket?.isconnected() == true

    fun subscribeContactUnVerifiedChannel() {
        if (isSocketConnected) {
            val contactUnVerifiedChannelName =
                appSettingsDataSource.channel.channels?.contactUnVerifiedChannelName
            var contactUnVerifiedChannel = _socket?.channels?.get(contactUnVerifiedChannelName)
            if (contactUnVerifiedChannel == null) {
                contactUnVerifiedChannel = _socket?.createChannel(contactUnVerifiedChannelName)
            } else {
                contactUnVerifiedChannel.unsubscribe()
            }
            contactUnVerifiedChannel?.subscribe { name, error, data ->
                Log.v(TAG, "Channel contactUnVerified subscribed ---> $name Success fully")
            }
            contactUnVerifiedChannel?.onMessage { name, data ->
                Log.v(TAG, "Channel contactUnVerified message ---> $name")
                Log.v(TAG, "Channel contactUnVerified data ---> $data")
                val jsonObject = JsonParser.parseString(data.toString()).asJsonObject
                if (jsonObject.hasProperty("type")) {
                    val type = jsonObject.getString("type") ?: ""
                    val publishType = PublishType.findEnumFromValue(type)
                    when (publishType) {
                        PublishType.NEW_MESSAGE -> {
                            /*{"type":"NEW_MESSAGE","data":{
                                        "conversationId":"846c7b72-3169-4c3a-8e46-07decd30bc3f","createdDate":"2021-10-12T08:34:40.393Z",
                                        "messageId":"46f843f8-4832-49a2-95cc-4d7d4a0dd166","blocks":[]}}*/
                            jsonObject.getJsonObject("data")?.let {
                                val chatNewMessage: ChatNewMessage =
                                    gson.fromJson(it, ChatNewMessage::class.java)
                                eventBus.fireEvent(AppEvent.NewMessageEvent(chatNewMessage))
                            }
                        }
                        PublishType.UPDATE_CONVERSATIONS_LIST -> {
                            /*{"type":"UPDATE_CONVERSATIONS_LIST","data":{"conversationId":"31f588e5-5495-4c08-930c-6829e405eb80","isInbound":true}}*/
                            jsonObject.getJsonObject("data")?.let {
                                it.getString("conversationId")?.let { conversationId ->
                                    eventBus.fireEvent(
                                        AppEvent.UpdateConversationListEvent(conversationId)
                                    )
                                }

                            }
                        }
                        PublishType.MESSAGE_DELETED -> {
                            /*{"type":"MESSAGE_DELETED","data":{"messageData":{"id":"a3c0f489-5389-4d06-86a0-0286b1265e93","conversation_id":"92e67f42-adb7-4811-aef8-2b2e5ce73418","user_id":"10","message_summary":"This message was deleted.","is_internal_message":false,"deleted_by":"10","deleted_date":"2021-10-20T09:43:49.686Z","unique_id":null,"verified_id":null,"created_date":"2021-10-20T09:38:51.591Z"}}}*/
                            jsonObject.getJsonObject("data")?.getJsonObject("messageData")?.let {
                                val chatNewMessage: MessageItem =
                                    gson.fromJson(it, MessageItem::class.java)
                                eventBus.fireEvent(AppEvent.DeleteMessageEvent(chatNewMessage))
                            }
                        }
                        PublishType.CONVERSATION_RATING_CHANGED -> {
                            /*{"type":"CONVERSATION_RATING_CHANGED","data":{"conversationId":"b31e02fe-af65-4109-850c-a292976390da","rating":"1"}}*/
                            jsonObject.getJsonObject("data")?.let {
                                val conversationID = it.getString("conversationId") ?: ""
                                val rating = it.getString("rating") ?: ""
                                eventBus.fireEvent(
                                    AppEvent.ConversationRatingChangeEvent(
                                        conversationID,
                                        rating
                                    )
                                )
                            }
                        }

                        PublishType.CONVERSATION_REMARK_ADDED -> {
                            /*{"type":"CONVERSATION_REMARK_ADDED","data":{"conversationId":"b31e02fe-af65-4109-850c-a292976390da","remark":"Hello"}}*/
                            jsonObject.getJsonObject("data")?.let {
                                val conversationID = it.getString("conversationId") ?: ""
                                val remark = it.getString("remark") ?: ""
                                eventBus.fireEvent(
                                    AppEvent.ConversationRemarkChangeEvent(
                                        conversationID,
                                        remark
                                    )
                                )
                            }
                        }

                    }
                } else {
                    val typingMessage = TypingMessage().apply {
                        conversationId = jsonObject.getString("conversationId") ?: ""
                        userId = jsonObject.getString("userId") ?: ""
                        workspaceId = jsonObject.getString("workspaceId") ?: ""
                        messageType = jsonObject.getString("messageType") ?: ""
                    }
                    eventBus.fireEvent(AppEvent.TypingMessageEvent(typingMessage))
                }
            }
        } else {
            Log.v(TAG, "Your socket is not connected yet")
        }
    }

    fun subScribeTenantSpecificChannel() {
        if (isSocketConnected) {
            val tenantSpecificChannelName =
                appSettingsDataSource.channel.channels?.tenantSpecificChannelNameForWidget
            var tenantSpecificChannel = _socket?.channels?.get(tenantSpecificChannelName)
            if (tenantSpecificChannel == null) {
                tenantSpecificChannel = _socket?.createChannel(tenantSpecificChannelName)
            } else {
                tenantSpecificChannel.unsubscribe()
            }
            tenantSpecificChannel?.subscribe { name, error, data ->
                Log.v(TAG, "Channel tenantSpecific subscribed ---> $name Success fully")
            }
            tenantSpecificChannel?.onMessage { name, data ->
                val jsonObject = JsonParser.parseString(data.toString()).asJsonObject
                if (jsonObject.hasProperty("type")) {
                    val type = jsonObject.getString("type") ?: ""
                    val publishType = PublishType.findEnumFromValue(type)
                    if (publishType == PublishType.USER_AVAILABILITY_STATUS_UPDATED) {
                        /*{"type":"USER_AVAILABILITY_STATUS_UPDATED","data":{"status":"away","userIds":["1","4","7","13"]}}*/
                        jsonObject.getJsonObject("data")?.let {
                            val userAvailability: UserAvailability =
                                gson.fromJson(it, UserAvailability::class.java)
                            eventBus.fireEvent(AppEvent.UserAvailabilityEvent(userAvailability))
                        }
                    }
                }
                Log.v(TAG, "Channel tenantSpecific message ---> $name")
                Log.v(TAG, "Channel tenantSpecific data ---> $data")
            }
        } else {
            Log.v(TAG, "Your socket is not connected yet")
        }
    }

    fun subScribeWorkspaceChannel() {
        if (isSocketConnected) {
            val channelWorkSpaceName = "";
            var channelWorkSpace = _socket?.channels?.get(channelWorkSpaceName)
            if (channelWorkSpace == null) {
                channelWorkSpace = _socket?.createChannel(channelWorkSpaceName)
            } else {
                channelWorkSpace.unsubscribe()
            }
            channelWorkSpace!!.subscribe { name, error, data ->
                Log.v(
                    TAG,
                    "Channel subscribed ---> $name Success fully"
                )
            }
            channelWorkSpace.onMessage { name, data ->
                val jsonObject = JsonParser.parseString(data.toString()).asJsonObject
                /*if (JsonUtil.hasProperty(jsonObject, "type")) {
                    val type = jsonObject["type"].asString
                    val payLoadType: PayLoadType = PayLoadType.findEnumFromValue(type)
                    if (payLoadType === PayLoadType.NEW_MESSAGE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val chatNewMessage: ChatNewMessage =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, ChatNewMessage::class.java)
                            val newMessageEvent = NewMessageEvent()
                            newMessageEvent.setChatMessage(chatNewMessage)
                            eventBus.post(newMessageEvent)
                        }
                    } else if (payLoadType === PayLoadType.CONVERSATION_HAS_READ) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val conversationHasRead = ConversationHasRead()
                            if (JsonUtil.hasProperty(dataJsonObject, "conversationId")) {
                                val conversationId = dataJsonObject["conversationId"].asString
                                conversationHasRead.setConversationId(conversationId)
                            }
                            if (JsonUtil.hasProperty(dataJsonObject, "contactHasRead")) {
                                val contactHasRead = dataJsonObject["contactHasRead"].asBoolean
                                conversationHasRead.setContactHasRead(contactHasRead)
                            }
                            val conversationHasReadEvent = ConversationHasReadEvent()
                            conversationHasReadEvent.setConversationHasRead(conversationHasRead)
                            eventBus.post(conversationHasReadEvent)
                        }
                    } else if (payLoadType === PayLoadType.ASSIGNMENT_ON_NEW_MESSAGE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val chatNewMessage: ChatNewMessage =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, ChatNewMessage::class.java)
                            val newMessageEvent = NewMessageEvent()
                            newMessageEvent.setChatMessage(chatNewMessage)
                            eventBus.post(newMessageEvent)
                        }
                    } else if (payLoadType === PayLoadType.CHANGE_ASSIGNEE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val assigneeChanged: AssigneeChanged =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, AssigneeChanged::class.java)
                            val assigneeChangedEvent = AssigneeChangedEvent()
                            assigneeChangedEvent.setAssigneeChanged(assigneeChanged)
                            eventBus.post(assigneeChangedEvent)
                        }
                    } else if (payLoadType === PayLoadType.CHANGE_ASSIGNEE_AND_REOPEN) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val assigneeChanged: AssigneeChanged =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, AssigneeChanged::class.java)
                            val assigneeChangedEvent = AssigneeChangedEvent()
                            assigneeChangedEvent.setAssigneeChanged(assigneeChanged)
                            eventBus.post(assigneeChangedEvent)
                        }
                    } else if (payLoadType === PayLoadType.UPDATE_CONVERSATIONS_LIST) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val chatConversation: ChatConversation =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, ChatConversation::class.java)
                            val updateConversationEvent = UpdateConversationEvent()
                            updateConversationEvent.setChatConversation(chatConversation)
                            eventBus.post(chatConversation)
                        }
                    } else if (payLoadType === PayLoadType.CONVERSATION_STATUS_UPDATE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val updateConversation: UpdateConversation =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, UpdateConversation::class.java)
                            val event = UpdateConversationStatusEvent()
                            event.setUpdateConversation(updateConversation)
                            eventBus.post(event)
                        }
                    } else if (payLoadType === PayLoadType.MESSAGE_DELETED) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            if (JsonUtil.hasProperty(dataJsonObject, "messageData")) {
                                val messageDataJsonObject =
                                    dataJsonObject["messageData"].asJsonObject
                                val chatMessage: ChatMessage = GsonInterface.getInstance().getGson()
                                    .fromJson(messageDataJsonObject, ChatMessage::class.java)
                                val deleteMessageEvent = DeleteMessageEvent()
                                deleteMessageEvent.setChatMessage(chatMessage)
                                eventBus.post(deleteMessageEvent)
                            }
                        }
                    } else if (payLoadType === PayLoadType.TITLE_CHANGE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val titleChanged: TitleChanged = GsonInterface.getInstance().getGson()
                                .fromJson(dataJsonObject, TitleChanged::class.java)
                            val event = TitleChangedEvent()
                            event.setTitleChanged(titleChanged)
                            eventBus.post(event)
                        }
                    } else if (payLoadType === PayLoadType.MENTION_USER) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            val chatConversation: ChatConversation =
                                GsonInterface.getInstance().getGson()
                                    .fromJson(dataJsonObject, ChatConversation::class.java)
                            val updateConversationEvent = MentionUserEvent()
                            updateConversationEvent.setChatConversation(chatConversation)
                            eventBus.post(updateConversationEvent)
                        }
                    } else if (payLoadType === PayLoadType.PRIORITY_CHANGE) {
                        if (JsonUtil.hasProperty(jsonObject, "data")) {
                            val priorityChangedEvent = PriorityChangedEvent()
                            val dataJsonObject = jsonObject["data"].asJsonObject
                            if (JsonUtil.hasProperty(dataJsonObject, "conversationId")) {
                                val conversationId = dataJsonObject["conversationId"].asString
                                priorityChangedEvent.setConversationId(conversationId)
                            }
                            eventBus.post(priorityChangedEvent)
                        }
                    }
                } else {
                    val chatMessageTyping = ChatMessageTyping()
                    if (JsonUtil.hasProperty(jsonObject, "conversationId")) {
                        val conversationId = jsonObject["conversationId"].asString
                        chatMessageTyping.setConversationId(conversationId)
                    }
                    if (JsonUtil.hasProperty(jsonObject, "userId")) {
                        val userId = jsonObject["userId"].asString
                        chatMessageTyping.setUserId(userId)
                    }
                    if (JsonUtil.hasProperty(jsonObject, "workspaceId")) {
                        val workspaceId = jsonObject["workspaceId"].asString
                        chatMessageTyping.setWorkspaceId(workspaceId)
                    }
                    if (JsonUtil.hasProperty(jsonObject, "messageType")) {
                        val messageType = jsonObject["messageType"].asString
                        chatMessageTyping.setMessageType(messageType)
                    }
                    if (JsonUtil.hasProperty(jsonObject, "visitorName")) {
                        val visitorName = jsonObject["visitorName"].asString
                        chatMessageTyping.setVisitorName(visitorName)
                    }
                    val event = ChatMessageTypingEvent()
                    event.setMessageTyping(chatMessageTyping)
                    eventBus.post(event)
                }*/
                Log.v(TAG, "Channel default workspace message ---> $name")
                Log.v(TAG, "Channel default workspace data ---> $data")
            }
        } else {
            Log.v(TAG, "Your socket is not connected yet")
        }
    }

    /*fun sendTyping(conversation: ChatConversation, messageType: MessageType) {
        *//*JsonObject dataJsonObject=new JsonObject();
            JsonObject ids=new JsonObject();
            ids.addProperty("uniqueId",conversation.getUniqueId());
            ids.add("verifiedId",null);
            dataJsonObject.add("ids",ids);

            dataJsonObject.addProperty("conversationId",conversation.getId());
            dataJsonObject.addProperty("userId", Integer.parseInt(AppSettings.getInstance().getUserId()));
            dataJsonObject.addProperty("workspaceId",AppSettings.getInstance().getMessengerDefaultWorkspaceId());
            dataJsonObject.addProperty("messageType","comment");
            System.out.println("Prepared Json for typing  --> "+dataJsonObject.toString());*//*
        try {
            val dataJsonObject = JSONObject()
            val ids = JSONObject()
            ids.put("uniqueId", conversation.getUniqueId())
            //ids.put("verifiedId",null);
            dataJsonObject.put("ids", ids)
            dataJsonObject.put("conversationId", conversation.getId())
            dataJsonObject.put("userId", AppSettings.getInstance().getUserId().toInt())
            dataJsonObject.put("workspaceId", conversation.getWorkspace_id())
            dataJsonObject.put("messageType", messageType.value)
            println("Prepared Json for typing  --> $dataJsonObject")
            if (isSocketConnected) {
                _socket?.emit("tenant-user-is-typing", dataJsonObject)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }*/

    fun sendTypingEvent(conversation: Conversations) {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("conversationId", conversation.id)
            jsonObject.put("visitorName", appSettingsDataSource.contactName)
            if (isSocketConnected) {
                _socket?.emit("visitor-is-typing", jsonObject)
                Log.w(TAG, "sendTypingEvent: $jsonObject")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}