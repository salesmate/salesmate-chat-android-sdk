package com.rapidops.salesmatechatsdk.app.fragment.recent_list

import android.view.*
import androidx.core.view.isVisible
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.loadImage
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.chat.ChatFragment
import com.rapidops.salesmatechatsdk.app.fragment.conversation_list.ConversationListFragment
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter.ConversationAdapter
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter.LetsChatUserAdapter
import com.rapidops.salesmatechatsdk.app.interfaces.IItemListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateBackgroundTintAction
import com.rapidops.salesmatechatsdk.app.utils.OverlapDecoration
import com.rapidops.salesmatechatsdk.databinding.FRecentChatListBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem


internal class RecentChatFragment : BaseFragment<RecentChatViewModel>() {

    private lateinit var binding: FRecentChatListBinding
    private val conversationAdapter = ConversationAdapter()
    companion object {
        fun newInstance(): RecentChatFragment {
            return RecentChatFragment()
        }
    }

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FRecentChatListBinding.inflate(inflater)
        return binding.root
    }

    override fun initializeViewModel(): RecentChatViewModel {
        return obtainViewModel(RecentChatViewModel::class.java)
    }

    override fun setUpUI() {
        setHasOptionsMenu(true)
        getBaseActivity().setSupportActionBar(binding.toolbar)

        viewModel.pingRes.apply {
            if (lookAndFeel.logoUrl.isNotEmpty()) {
                binding.imgLogo.loadImage(lookAndFeel.logoUrl)
            } else {
                binding.imgLogo.isVisible = false
            }
            binding.txtGreeting.text = welcomeMessages.first().greetingMessage
            binding.txtTeamIntro.text = welcomeMessages.first().teamIntro

            binding.llPoweredBy.isVisible = lookAndFeel.showPoweredBy
        }

        observeViewModel()
        attachListener()
        viewModel.subscribe()
    }

    private fun attachListener() {
        conversationAdapter.clickListener = object : IItemListener<ConversationDetailItem> {
            override fun onItemClick(position: Int, item: ConversationDetailItem) {
                getBaseActivity().addFragment(ChatFragment.newInstance(item))
            }
        }

        binding.incLetsChat.txtStartNewChat.setOnClickListener { startNewChat() }
        binding.incRecentChat.txtStartNewChatList.setOnClickListener { startNewChat() }
    }

    private fun startNewChat() {
        if (conversationAdapter.getItems().isEmpty()) {
            viewModel.sendTrackEvent()
        }
        getBaseActivity().addFragment(ChatFragment.newInstance())
    }

    private fun observeViewModel() {
        viewModel.showConversationList.observe(this, {
            if (it.isEmpty()) {
                showLetsChatView()
            } else {
                showRecentChatView(it)
            }
        })

        viewModel.recentViewProgress.observe(this, {
            showProgressView(it)
        })
    }

    private fun showLetsChatView() {
        binding.flProgress.isVisible = false
        binding.incRecentChat.llRecentChats.isVisible = false
        binding.incLetsChat.llLetsChat.isVisible = true
        viewModel.pingRes.apply {

            binding.incLetsChat.apply {
                txtReplyTime.text =
                    getString(R.string.lbl_the_team_typically_replies_in, availability?.replyTime)
                txtStartNewChat.updateBackgroundTintAction()
                txtStartNewChat.compoundDrawablesRelative.forEach {
                    it?.setTint(ColorUtil.actionColor.foregroundColor())
                }

                rvUser.addItemDecoration(OverlapDecoration())

                val availableUseList = getAvailableUserList()
                val userAdapter = LetsChatUserAdapter(availableUseList.size)
                userAdapter.setItems(availableUseList.take(4).toMutableList())
                rvUser.adapter = userAdapter

                txtStartNewChat.isVisible = canVisitorOrContactStartNewConversation
            }

        }
    }

    private fun showRecentChatView(list: List<ConversationDetailItem>) {
        binding.flProgress.isVisible = false
        binding.incLetsChat.llLetsChat.isVisible = false
        binding.incRecentChat.llRecentChats.isVisible = true
        viewModel.pingRes.apply {
            binding.incRecentChat.apply {
                txtStartNewChatList.updateBackgroundTintAction()
                txtStartNewChatList.compoundDrawablesRelative.forEach {
                    it?.setTint(ColorUtil.actionColor.foregroundColor())
                }
                txtStartNewChatList.isVisible = canVisitorOrContactStartNewConversation

                conversationAdapter.setItems(list.take(2).toMutableList())
                rvRecentConversation.adapter = conversationAdapter

                txtViewAll.isVisible = list.size >= 3

            }
        }

        binding.incRecentChat.txtViewAll.setOnClickListener {
            getBaseActivity().addFragment(ConversationListFragment.newInstance())
        }
    }

    private fun showProgressView(show: Boolean) {
        binding.flProgress.isVisible = show
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