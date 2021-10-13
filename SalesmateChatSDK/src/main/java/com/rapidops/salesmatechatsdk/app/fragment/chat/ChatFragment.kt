package com.rapidops.salesmatechatsdk.app.fragment.chat

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.loadCircleProfileImage
import com.rapidops.salesmatechatsdk.app.extension.loadImage
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ChatTopBarUserAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.MessageAdapter
import com.rapidops.salesmatechatsdk.app.fragment.chat.adapter.ToolbarUserAdapter
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setSendButtonColorStateList
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.data.resmodels.PingRes
import com.rapidops.salesmatechatsdk.databinding.FChatBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import com.rapidops.salesmatechatsdk.domain.models.User

internal class ChatFragment : BaseFragment<ChatViewModel>() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: FChatBinding


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


        binding.rvMessage.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        messageAdapter = MessageAdapter(requireActivity())
        binding.rvMessage.adapter = messageAdapter


        observeViewModel()
        attachListener()
        viewModel.subscribe(conversationDetailItem?.conversations?.id)
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
        })
    }

    private fun attachListener() {
        binding.edtMessage.addTextChangedListener {
            binding.txtSend.isEnabled = getTypedMessage().isNotEmpty()
        }

        binding.txtSend.setOnClickListener {

        }

        binding.imgAttachment.setOnClickListener {

        }
    }

    private fun getTypedMessage(): String {
        return binding.edtMessage.text.toString().trim()
    }

    private fun setUpTopBar(conversationDetailItem: ConversationDetailItem?) {
        conversationDetailItem?.user?.id?.let {
            showToolbarWithUserDetail(conversationDetailItem.user)
        } ?: run {
            showToolbarWithoutConversationDetail()
        }

    }

    private fun showToolbarWithoutConversationDetail() {
        viewModel.pingRes.apply {
            if (lookAndFeel.logoUrl.isNotEmpty()) {
                bindToolbarWithLogo(this)
            } else {
                bindToolbarWithoutLogo(this)
            }
        }
    }

    private fun bindToolbarWithLogo(pingRes: PingRes) {
        pingRes.apply {
            binding.incChatToolbarView.root.isVisible = false
            binding.incChatUserToolbarView.root.isVisible = false
            binding.incChatTopLogoView.apply {
                root.isVisible = true
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
            binding.incChatTopLogoView.root.isVisible = false
            binding.incChatUserToolbarView.root.isVisible = false
            binding.incChatToolbarView.apply {
                root.isVisible = true
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
}