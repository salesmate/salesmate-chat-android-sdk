package com.rapidops.salesmatechatsdk.app.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.utils.AppEvent
import com.rapidops.salesmatechatsdk.app.utils.EventBus
import com.rapidops.salesmatechatsdk.data.utils.*
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import com.rapidops.salesmatechatsdk.domain.models.Conversations
import com.rapidops.salesmatechatsdk.domain.models.PublishType
import com.rapidops.salesmatechatsdk.domain.models.UserAvailability
import com.rapidops.salesmatechatsdk.domain.models.events.ChatNewMessage
import com.rapidops.salesmatechatsdk.domain.models.events.TypingMessage
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import io.github.sac.BasicListener
import io.github.sac.Emitter
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
                            subscribeContactChannel()
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

    fun subscribeContactChannel() {
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

            val contactVerifiedChannelName =
                appSettingsDataSource.channel.channels?.contactVerifiedChannelName
            var contactVerifiedChannel = _socket?.channels?.get(contactVerifiedChannelName)
            if (contactVerifiedChannelName?.isNotEmpty() == true) {
                if (contactVerifiedChannel == null) {
                    contactVerifiedChannel = _socket?.createChannel(contactVerifiedChannelName)
                } else {
                    contactVerifiedChannel.unsubscribe()
                }
                contactVerifiedChannel?.subscribe { name, error, data ->
                    Log.v(TAG, "Channel contactVerified subscribed ---> $name Success fully")
                }
            }

            val value = Emitter.Listener { name, data ->
                Log.v(TAG, "Channel contact message ---> $name")
                Log.v(TAG, "Channel contact data ---> $data")
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

                        PublishType.CONTACT_CREATED -> {
                            jsonObject.getJsonObject("data")?.let {
                                val uniqueId = it.getString("uniqueId") ?: ""
                                val contactId = it.getString("contactId") ?: ""
                                val email = it.getString("email") ?: ""
                                val contactName = it.getString("name") ?: ""
                                appSettingsDataSource.saveContactDetail(
                                    contactId,
                                    email,
                                    contactName
                                )
                                eventBus.fireEvent(AppEvent.ContactCreateEvent)
                            }
                        }

                        PublishType.CONVERSATION_HAS_READ -> {
                            jsonObject.getJsonObject("data")?.let {
                                val conversationId = it.getString("conversationId") ?: ""
                                val userHashRead = it.getBoolean("userHasRead")
                                val contactHasRead = it.getBoolean("contactHasRead")
                                eventBus.fireEvent(
                                    AppEvent.ConversationHasReadEvent(
                                        conversationId,
                                        userHashRead,
                                        contactHasRead
                                    )
                                )
                            }
                        }

                        PublishType.CONVERSATION_STATUS_UPDATE -> {
                            jsonObject.getJsonObject("data")?.let {
                                val conversationId = it.getString("conversationId") ?: ""
                                val status = it.getString("status") ?: ""
                                eventBus.fireEvent(
                                    AppEvent.ConversationStatusUpdateEvent(
                                        conversationId,
                                        status
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
            contactUnVerifiedChannel?.onMessage(value)
            contactVerifiedChannel?.onMessage(value)
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
                            appSettingsDataSource.pingRes.users.forEach { user ->
                                if (userAvailability.userIds.contains(user.id)) {
                                    user.status = userAvailability.status
                                }
                            }
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

    fun resetSocketAndConnect() {
        _socket?.disconnect()
        _socket = null
        connect()
    }

    fun resetSocket() {
        _socket?.disconnect()
        _socket = null
    }
}