package com.rapidops.salesmatechatsdk.app.fragment.chat

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.isValidEmail
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.extension.loadImage
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ChatTopBarUserAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ToolbarUserAdapter
import com.rapidops.salesmatechatsdk.app.fragment.upload_attachment.UploadAttachmentDialogFragment
import com.rapidops.salesmatechatsdk.app.fragment.upload_attachment.UploadAttachmentDialogFragmentListener
import com.rapidops.salesmatechatsdk.app.interfaces.EndlessScrollListener
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setSendButtonColorStateList
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateBackgroundTintAction
import com.rapidops.salesmatechatsdk.app.utils.FileUtil
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.databinding.FChatBinding
import com.rapidops.salesmatechatsdk.domain.models.AvailabilityStatus
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.User
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import java.io.File
import kotlin.math.abs


internal class ChatFragment : BaseFragment<ChatViewModel>() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: FChatBinding
    private lateinit var endlessScrollListener: EndlessScrollListener

    companion object {
        private var TAG = ChatFragment::class.java.simpleName
        private const val EXTRA_CONVERSATION_DETAIL = "EXTRA_CONVERSATION_DETAIL"

        fun newInstance(conversationDetailItem: ConversationDetailItem? = null): ChatFragment {
            val bundle = Bundle()
            bundle.putSerializable(EXTRA_CONVERSATION_DETAIL, conversationDetailItem)
            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle
            return chatFragment
        }
    }

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FChatBinding.inflate(inflater)
        return binding.root
    }

    override fun initializeViewModel(): ChatViewModel {
        return obtainViewModel(ChatViewModel::class.java)
    }

    override fun setUpUI() {
        setHasOptionsMenu(true)
        getBaseActivity().setSupportActionBar(binding.toolbar)
        getBaseActivity().supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        binding.toolbar.navigationIcon?.setTintFromBackground()
        binding.toolbar.setNavigationOnClickListener {
            getBaseActivity().popBackStack()
        }

        val conversationDetailItem =
            arguments?.getSerializable(EXTRA_CONVERSATION_DETAIL) as ConversationDetailItem?
        setUpTopBar(conversationDetailItem)

        binding.incChatToolbarView.rvUser.addItemDecoration(OverlapDecoration())

        binding.txtSend.setSendButtonColorStateList()


        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        endlessScrollListener = object : EndlessScrollListener(layoutManager, 1) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                viewModel.loadMoreMessageList(totalItemsCount)
            }
        }
        binding.rvMessage.addOnScrollListener(endlessScrollListener)
        binding.rvMessage.layoutManager = layoutManager
        binding.rvMessage.itemAnimator?.changeDuration = 0
        binding.rvMessage.itemAnimator?.removeDuration = 0
        binding.rvMessage.itemAnimator?.addDuration = 0
        binding.rvMessage.itemAnimator?.moveDuration = 0
        messageAdapter = MessageAdapter(requireActivity(), messageAdapterListener)
        binding.rvMessage.adapter = messageAdapter


        observeViewModel()
        attachListener()
        viewModel.subscribe(
            conversationDetailItem?.conversations?.id,
            conversationDetailItem?.isContactHasRead ?: true
        )

        binding.imgAttachment.isVisible = viewModel.canUploadAttachment
    }

    private fun observeViewModel() {
        viewModel.showConversationDetail.observe(this, {
            setUpTopBar(it)
        })

        viewModel.showMessageList.observe(this, {
            if (messageAdapter.items.isNullOrEmpty()) {
                messageAdapter.setItemList(it.toMutableList())
            } else {
                messageAdapter.addItems(it.toMutableList())
            }
            viewModel.updateAdapterList(messageAdapter.items)
        })

        viewModel.showNewMessage.observe(this, {
            if (messageAdapter.items.isNullOrEmpty()) {
                messageAdapter.setItemList(it.toMutableList())
            } else {
                messageAdapter.addNewItems(it.toMutableList())
            }
            viewModel.updateAdapterList(messageAdapter.items)
        })

        viewModel.updateMessage.observe(this, {
            messageAdapter.updateMessage(it)
            viewModel.updateAdapterList(messageAdapter.items)
        })

        viewModel.showOverLimitFileMessageDialog.observe(this, {
            showOverLimitFileMessageDialog()
        })

        viewModel.updateRatingMessage.observe(this, {
            messageAdapter.updateRatingMessage()
        })

        viewModel.updateAskEmailMessage.observe(this, {
            messageAdapter.updateAskEmailMessage()
        })

        viewModel.showTypingMessageView.observe(this, {
            showTypingMessage(it)
        })

        viewModel.hideTypingMessageView.observe(this, {
            binding.incTypingMessage.loadingDotsTyping.stopAnimation()
            binding.incTypingMessage.root.isVisible = false
        })

        viewModel.showAskEmailView.observe(this, {
            showEmailAskView(it)
        })

        viewModel.showExportedChatFile.observe(this, {
            showExportedChat(it)
        })

        viewModel.updateConversationReadStatus.observe(this, {
            messageAdapter.updateMessages()
        })

        viewModel.isConversationOpenForMessage.observe(this, {
            setUpConversationByStatus(it)
        })
    }

    private fun attachListener() {
        binding.edtMessage.doOnTextChanged { text, start, before, count ->
            if (count > before) {
                viewModel.sendTypingEvent()
            }
            binding.txtSend.isEnabled = getTypedMessage().isNotEmpty()
        }

        binding.txtSend.setOnClickListener {
            viewModel.sendTextMessage(getTypedMessage())
            clearTypedMessage()
        }

        binding.imgAttachment.setOnClickListener {
            showFilePickerWithPermissionCheck()
        }

        binding.edtMessage.setOnFocusChangeListener { view, isFocus ->
            if (isFocus) {
                binding.appBar.setExpanded(false, true)
            }
        }

        messageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0) {
                    binding.rvMessage.scrollToPosition(0)
                }
            }
        })
    }

    private fun getTypedMessage(): String {
        return binding.edtMessage.text.toString().trim()
    }

    private fun clearTypedMessage() {
        binding.edtMessage.text?.clear()
    }

    private fun setUpTopBar(conversationDetailItem: ConversationDetailItem?) {
        conversationDetailItem?.user?.id?.let {
            showToolbarWithUserDetail(conversationDetailItem.user)
        } ?: run {
            showToolbarWithoutUserDetail()
            if (conversationDetailItem == null) {
                binding.appBar.setExpanded(true)
            }
        }
        getBaseActivity().invalidateOptionsMenu()
    }

    private fun showToolbarWithoutUserDetail() {
        viewModel.pingRes.apply {
            bindToolbarWithLogo(this)
            bindToolbarWithoutLogo(this)

            if (lookAndFeel.logoUrl.isEmpty()) {
                binding.incChatTopLogoView.root.isVisible = false
                binding.incChatToolbarView.root.isVisible = true
            } else {
                binding.incChatTopLogoView.root.isInvisible = true
            }
        }
    }

    private fun bindToolbarWithLogo(pingRes: PingRes) {
        pingRes.apply {
            binding.appBar.addOnOffsetChangedListener(appBarOnOffsetChangedListener)
            binding.incChatUserToolbarView.root.isVisible = false
            binding.incChatTopLogoView.apply {
                imgLogo.loadImage(lookAndFeel.logoUrl)
                txtTeamIntro.text = welcomeMessages.first().teamIntro
                txtReplyTime.text =
                    getString(R.string.lbl_we_reply, availability?.replyTime)

                val availableUseList = getAvailableUserList()
                val chatTopBarUserAdapter = ChatTopBarUserAdapter(availableUseList.size)
                chatTopBarUserAdapter.setItems(availableUseList.take(4).toMutableList())
                rvUser.adapter = chatTopBarUserAdapter
            }
        }
    }

    private fun bindToolbarWithoutLogo(pingRes: PingRes) {
        pingRes.apply {
            binding.incChatUserToolbarView.root.isVisible = false
            binding.incChatToolbarView.apply {
                txtReplyTime.text = getString(R.string.lbl_we_reply, availability?.replyTime)
                txtWorkspaceName.text = workspaceData?.name
                val availableUserList = getAvailableUserList()
                val toolbarUserAdapter = ToolbarUserAdapter(availableUserList.size)
                toolbarUserAdapter.setItems(availableUserList.take(3).toMutableList())
                rvUser.adapter = toolbarUserAdapter
            }
        }
    }

    private fun showToolbarWithUserDetail(user: User?) {
        user?.let {
            binding.appBar.removeOnOffsetChangedListener(appBarOnOffsetChangedListener)
            binding.incChatTopLogoView.root.isVisible = false
            binding.incChatToolbarView.root.isVisible = false
            binding.incChatUserToolbarView.apply {
                root.isVisible = true
                imgUser.loadCircleProfileImage(
                    it.profileUrl,
                    it.firstName
                )
                txtUserName.text = String.format("%s %s", it.firstName, it.lastName)
                txtStatus.text = it.status.replaceFirstChar { it.uppercaseChar() }

                val statusYellowColor = if (it.status == AvailabilityStatus.AVAILABLE.value)
                    R.color.light_green
                else
                    R.color.status_yellow_color

                imgStatus.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        statusYellowColor
                    )
                )
                imgStatus.background.setTintBackground()
            }
        }
    }

    private val appBarOnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percentage =
                abs(verticalOffset).toFloat() / appBarLayout!!.totalScrollRange
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                //  Collapsed
                binding.incChatTopLogoView.root.animate().alpha(0f)
                binding.incChatToolbarView.root.isVisible = true
            } else if (verticalOffset == 0) {
                //Expanded
                binding.incChatToolbarView.root.animate().alpha(0f)
                binding.incChatTopLogoView.root.isVisible = true
            } else {
                //In Between
                binding.incChatToolbarView.root.animate().alpha(percentage)
                binding.incChatTopLogoView.root.animate().alpha(1 - percentage)
            }

        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_conversation_list, menu)
        menu.findItem(R.id.action_close).icon.setTintFromBackground()
        menu.findItem(R.id.action_download).icon.setTintFromBackground()
        menu.findItem(R.id.action_download).isVisible =
            viewModel.showConversationDetail.value?.user?.id.isNullOrEmpty().not()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                getBaseActivity().finish()
            }

            R.id.action_download -> {
                viewModel.downloadTranscript()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val messageAdapterListener = object : MessageAdapterListener {
        override fun onInfoClick(messageItem: MessageItem) {
            showFailedInfoDialog(messageItem)
        }

        override fun getConversationDetail(): ConversationDetailItem? {
            return viewModel.showConversationDetail.value
        }

        override fun submitRemark(remark: String) {
            viewModel.submitRemark(remark)
        }

        override fun submitRating(rating: String) {
            viewModel.submitRating(rating)
        }

        override fun submitContact(name: String, email: String) {
            viewModel.submitContactDetail(name, email)
        }

        override fun isUserHasRead(): Boolean {
            return viewModel.isUserHasRead
        }
    }

    private fun showFailedInfoDialog(messageItem: MessageItem) {
        showAlertDialog(
            titleId = R.string.lbl_failed,
            messageId = R.string.msg_send_failed_message,
            positiveButtonId = R.string.lbl_try_again,
            negativeButtonId = R.string.dialog_cancel,
            positive = {
                viewModel.onRetrySendMessage(messageItem)
            },
            negative = {}
        ).show()
    }

    private fun showOverLimitFileMessageDialog() {
        showAlertDialog(
            titleId = R.string.lbl_failed,
            messageId = R.string.msg_support_file_sizes_upto_25mb,
            positiveButtonId = R.string.dialog_ok,
        ).show()
    }

    private fun showFilePickerWithPermissionCheck() {
        getBaseActivity().requestForStoragePermission {
            if (it) {
                showAttachmentDialog()
            }
        }
    }

    private fun showAttachmentDialog() {
        val uploadAttachmentDialogFragment = UploadAttachmentDialogFragment()
        uploadAttachmentDialogFragment.show(
            childFragmentManager,
            UploadAttachmentDialogFragment::class.java.name
        )
        uploadAttachmentDialogFragment.listener = object : UploadAttachmentDialogFragmentListener {
            override fun onFilePicked(uri: Uri) {
                viewModel.sendAttachment(requireContext(), uri)
            }
        }
    }

    private fun showTypingMessage(user: User) {
        if (binding.incTypingMessage.root.isVisible) return
        binding.incTypingMessage.root.isVisible = true
        binding.incTypingMessage.apply {
            loadingDotsTyping.stopAnimation()
            loadingDotsTyping.dotsColor = ColorUtil.actionColor
            root.isVisible = true
            loadingDotsTyping.startAnimation()
            imgUser.loadCircleProfileImage(
                user.profileUrl,
                user.firstName
            )
        }
    }

    private fun showEmailAskView(show: Boolean) {
        if (show) {
            binding.llMessage.isVisible = false
            binding.llAskEmail.isVisible = true
            binding.incEmailAsked.apply {
                txtSubmit.updateBackgroundTintAction()
                edtName.addTextChangedListener(afterTextChanged = nameAndEmailChange)
                edtEmail.addTextChangedListener(afterTextChanged = nameAndEmailChange)

                txtSubmit.setOnClickListener {
                    if (edtEmail.text.toString().trim().isValidEmail()) {
                        txtEmailError.isVisible = false
                        viewModel.submitContactDetail(
                            edtName.text.toString().trim(),
                            edtEmail.text.toString().trim()
                        )
                    } else {
                        txtEmailError.isVisible = true
                    }
                }

                val onFocusChangeLister = View.OnFocusChangeListener { _, isFocus ->
                    if (isFocus) {
                        binding.appBar.setExpanded(false, true)
                    }
                }
                edtName.onFocusChangeListener = onFocusChangeLister
                edtEmail.onFocusChangeListener = onFocusChangeLister
            }
        } else {
            binding.llAskEmail.isVisible = false
            binding.llMessage.isVisible = true
        }
    }

    private val nameAndEmailChange: (text: Editable?) -> Unit = {
        binding.incEmailAsked.apply {
            val isFilled =
                edtName.editableText.isNotEmpty() && edtEmail.editableText.isNotEmpty()
            txtSubmit.isEnabled = isFilled
            txtSubmit.alpha = if (isFilled) 1f else 0.5f
        }
    }

    private fun showExportedChat(file: File) {
        val intent =
            Intent(Intent.ACTION_VIEW, FileUtil.getUriFromFileProvider(requireContext(), file))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun setUpConversationByStatus(isOpen: Boolean) {
        if (isOpen) {
            binding.llMessage.isVisible = true
            binding.llAskEmail.isVisible = false
            binding.llConversationClosed.isVisible = false
        } else {
            binding.llMessage.isVisible = false
            binding.llAskEmail.isVisible = false
            binding.llConversationClosed.isVisible = true
            binding.txtStartNewChat.updateBackgroundTintAction()
            binding.txtStartNewChat.compoundDrawablesRelative.forEach {
                it?.setTint(ColorUtil.actionColor.foregroundColor())
            }

            binding.txtStartNewChat.setOnClickListener {

            }
        }
    }

}