package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.EmojiAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.IItemListener
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setSendButtonColorStateList
import com.rapidops.salesmatechatsdk.app.view.SpacesItemDecoration
import com.rapidops.salesmatechatsdk.databinding.RRatingAskedMessageBinding
import com.rapidops.salesmatechatsdk.domain.models.EmojiMapping
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType


internal class RatingAskMessageDelegate(
    private val activity: Activity,
    private val messageAdapterListener: MessageAdapterListener,
    private val emojiMappingList: List<EmojiMapping>
) :
    BaseMessageAdapterDelegate(activity) {

    private var newRemark: String = ""

    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = RRatingAskedMessageBinding.inflate(inflater, parent, false).root
        return RatingAskedMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as RatingAskedMessageViewHolder
        val bind = viewHolder.bind
        val messageItem = items[position]

        val rateId = getRating()
        val existingRemark = getRemark()

        val remark = existingRemark.takeIf { it.isNotEmpty() } ?: newRemark

        viewHolder.emojiAdapter.setSelectedRateId(rateId, existingRemark.isEmpty())
        viewHolder.emojiAdapter.setItems(emojiMappingList.toMutableList())
        bind.edtFeedback.tag = remark
        bind.edtFeedback.setText(remark)
        bind.edtFeedback.tag = null


        bind.txtRateLabel.isInvisible = rateId.isEmpty()
        bind.imgSend.isEnabled = remark.isNotEmpty()
        bind.txtRateLabel.text =
            emojiMappingList.find { rateId == it.id }?.label ?: ""
        bind.imgSend.setSendButtonColorStateList()
        bind.edtFeedback.addTextChangedListener {
            if (bind.edtFeedback.tag == null) {
                newRemark = it.toString()
            }
            bind.imgSend.isEnabled = newRemark.isNotEmpty()
        }

        bind.llFeedback.isVisible = rateId.isNotEmpty()
        bind.imgSend.isVisible = existingRemark.isEmpty()
        enableEditText(bind.edtFeedback, existingRemark.isEmpty())
        bind.imgSend.isVisible = existingRemark.isEmpty()

        holder.emojiAdapter.clickListener = object : IItemListener<EmojiMapping> {
            override fun onItemClick(position: Int, item: EmojiMapping) {
                messageAdapterListener.getConversationDetail()?.conversations?.rating = item.id
                bind.txtRateLabel.text = item.label
                bind.llFeedback.isVisible = true
                bind.txtRateLabel.isVisible = true
                messageAdapterListener.submitRating(item.id)
            }
        }

        bind.imgSend.setOnClickListener {
            messageAdapterListener.submitRemark(newRemark)
            messageAdapterListener.getConversationDetail()?.conversations?.remark = newRemark
            enableEditText(bind.edtFeedback, false)
            bind.imgSend.isVisible = false
        }
    }

    private fun enableEditText(editText: AppCompatEditText, enable: Boolean) {
        editText.isClickable = enable
        editText.isFocusable = enable
        editText.isFocusableInTouchMode = enable
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.messageType == MessageType.RATING_ASKED.value
    }

    internal inner class RatingAskedMessageViewHolder(itemView: View) :
        MessageViewHolder(itemView) {
        val bind = RRatingAskedMessageBinding.bind(itemView)
        val emojiAdapter = EmojiAdapter()

        init {
            bind.rvEmoji.addItemDecoration(SpacesItemDecoration(15))
            bind.rvEmoji.adapter = emojiAdapter
        }
    }

    private fun getRating(): String {
        return messageAdapterListener.getConversationDetail()?.conversations?.rating ?: ""
    }

    private fun getRemark(): String {
        return messageAdapterListener.getConversationDetail()?.conversations?.remark ?: ""
    }
}