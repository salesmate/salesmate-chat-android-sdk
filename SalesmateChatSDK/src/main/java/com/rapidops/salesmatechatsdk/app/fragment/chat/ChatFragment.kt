package com.rapidops.salesmatechatsdk.app.fragment.chat

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.extension.loadImage
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ChatTopBarUserAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ToolbarUserAdapter
import com.rapidops.salesmatechatsdk.app.interfaces.EndlessScrollListener
import com.rapidops.salesmatechatsdk.app.interfaces.MessageAdapterListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setSendButtonColorStateList
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.databinding.FChatBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.User
import com.rapidops.salesmatechatsdk.domain.models.message.MessageItem
import kotlin.math.abs

internal class ChatFragment : BaseFragment<ChatViewModel>() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: FChatBinding
    private lateinit var endlessScrollListener: EndlessScrollListener


    companion object {
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
            conversationDetailItem?.isConversationRead ?: true
        )
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
    }

    private fun attachListener() {
        binding.edtMessage.addTextChangedListener {
            binding.txtSend.isEnabled = getTypedMessage().isNotEmpty()
        }

        binding.txtSend.setOnClickListener {
            viewModel.sendTextMessage(getTypedMessage())
            clearTypedMessage()
        }

        binding.imgAttachment.setOnClickListener {

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
                txtStatus.text = it.status
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

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                getBaseActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val messageAdapterListener = object : MessageAdapterListener {
        override fun onRetryClick(messageItem: MessageItem) {
            viewModel.onRetrySendMessage(messageItem)
        }

    }

}