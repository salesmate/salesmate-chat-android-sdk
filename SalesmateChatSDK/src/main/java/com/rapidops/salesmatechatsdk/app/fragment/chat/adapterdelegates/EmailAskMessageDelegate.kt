package com.rapidops.salesmatechatsdk.app.fragment.chat.adapterdelegates

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.extension.isValidEmail
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageViewHolder
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateBackgroundTintAction
import com.rapidops.salesmatechatsdk.databinding.REmailAskedMessageBinding
import com.rapidops.salesmatechatsdk.databinding.VEmailAskedBinding
import com.rapidops.salesmatechatsdk.domain.models.ContactData
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import com.rapidops.salesmatechatsdk.domain.models.message.MessageType


internal class EmailAskMessageDelegate(
    private val activity: Activity,
    private val messageAdapterListener: MessageAdapterListener,
    private val getContactDetail: () -> ContactData?,
) :
    BaseMessageAdapterDelegate(activity, messageAdapterListener) {


    override fun onCreateMessageHolder(parent: ViewGroup): MessageViewHolder {
        val view = REmailAskedMessageBinding.inflate(inflater, parent, false).root
        return EmailAskedMessageViewHolder(view)
    }

    override fun onBindMessageViewHolder(
        items: List<MessageItem>,
        position: Int,
        holder: MessageViewHolder
    ) {
        val viewHolder = holder as EmailAskedMessageViewHolder
        val bind = viewHolder.bind
        val messageItem = items[position]

        val isAlreadySubmitted = getContactId().isNotEmpty() || messageItem.isEmailSubmitted

        bind.lblGetNotified.isGone = isAlreadySubmitted
        bind.lblNotifiedInfo.isVisible = isAlreadySubmitted

        bind.incEmailAsked.apply {
            txtSubmit.updateBackgroundTintAction()

            val email = (getContactEmail().takeIf { it.isNotEmpty() }
                ?: messageItem.contactEmail)

            edtEmail.tag = messageItem.contactEmail
            edtEmail.setText(email)
            edtEmail.tag = null

            val name = (getContactName().takeIf { it.isNotEmpty() } ?: messageItem.contactName)
            edtName.tag = messageItem.contactName
            edtName.setText(name)
            edtName.tag = null

            edtEmail.addTextChangedListener {
                if (edtEmail.tag == null) {
                    messageItem.contactEmail = it.toString()
                }
                setSubmitButtonState(this)
            }

            edtName.addTextChangedListener {
                if (edtName.tag == null) {
                    messageItem.contactName = it.toString()
                }
                setSubmitButtonState(this)
            }

            setSubmitButtonState(this)

            txtSubmit.setOnClickListener {
                if (edtEmail.text.toString().trim().isValidEmail()) {
                    txtEmailError.isVisible = false
                    messageAdapterListener.submitContact(
                        edtName.text.toString().trim(),
                        edtEmail.text.toString()
                    )
                } else {
                    txtEmailError.isVisible = true
                }
            }

            txtSubmit.isGone = isAlreadySubmitted

            enableEditText(edtName, isAlreadySubmitted.not())
            enableEditText(edtEmail, isAlreadySubmitted.not())

            if (isAlreadySubmitted) {
                edtName.background = null
                edtEmail.background = null
                edtName.setPadding(0, 0, 0, 0)
                edtEmail.setPadding(0, 0, 0, 0)
            } else {
                val padding: Int =
                    holder.itemView.resources.getDimensionPixelOffset(R.dimen.padding_small2)
                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.drw_edit_back_corner_stroke
                )
                edtName.background = drawable
                edtEmail.background = drawable
                edtName.setPadding(padding, padding, padding, padding)
                edtEmail.setPadding(padding, padding, padding, padding)
            }
        }
    }


    private fun enableEditText(editText: AppCompatEditText, enable: Boolean) {
        editText.isClickable = enable
        editText.isFocusable = enable
        editText.isFocusableInTouchMode = enable
    }

    private fun setSubmitButtonState(bind: VEmailAskedBinding) {
        bind.apply {
            val isFilled =
                edtName.editableText.isNotEmpty() && edtEmail.editableText.isNotEmpty()
            txtSubmit.isEnabled = isFilled
            txtSubmit.alpha = if (isFilled) 1f else 0.5f
        }
    }

    override fun isForViewType(item: MessageItem, position: Int): Boolean {
        return item.messageType == MessageType.EMAIL_ASKED.value
    }

    internal inner class EmailAskedMessageViewHolder(itemView: View) :
        MessageViewHolder(itemView) {
        val bind = REmailAskedMessageBinding.bind(itemView)
    }

    private fun getContactId(): String {
        return getContactDetail()?.id ?: ""
    }

    private fun getContactName(): String {
        return getContactDetail()?.name ?: ""
    }

    private fun getContactEmail(): String {
        return getContactDetail()?.email ?: ""
    }
}