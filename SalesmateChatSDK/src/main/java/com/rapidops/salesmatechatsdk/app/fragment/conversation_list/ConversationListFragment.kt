package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.chat.ChatFragment
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter.ConversationAdapter
import com.rapidops.salesmatechatsdk.app.interfaces.EndlessScrollListener
import com.rapidops.salesmatechatsdk.app.interfaces.IItemListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateBackgroundTintAction
import com.rapidops.salesmatechatsdk.databinding.FConversationListBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem


internal class ConversationListFragment : BaseFragment<ConversationListViewModel>() {

    private val conversationAdapter = ConversationAdapter()
    private lateinit var binding: FConversationListBinding
    private lateinit var endlessScrollListener: EndlessScrollListener

    companion object {
        fun newInstance(): ConversationListFragment {
            return ConversationListFragment()
        }
    }

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FConversationListBinding.inflate(inflater)
        return binding.root
    }

    override fun initializeViewModel(): ConversationListViewModel {
        return obtainViewModel(ConversationListViewModel::class.java)
    }

    override fun setUpUI() {
        setHasOptionsMenu(true)
        getBaseActivity().setSupportActionBar(binding.toolbar)
        getBaseActivity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        binding.toolbar.navigationIcon?.setTintFromBackground()

        binding.toolbar.setNavigationOnClickListener {
            getBaseActivity().popBackStack()
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        endlessScrollListener = object : EndlessScrollListener(layoutManager, 1) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                viewModel.loadConversationList(totalItemsCount)
            }
        }
        binding.apply {
            txtStartNewChat.updateBackgroundTintAction()
            txtStartNewChat.compoundDrawablesRelative.forEach {
                it?.setTint(ColorUtil.actionColor.foregroundColor())
            }
            rvConversation.addOnScrollListener(endlessScrollListener)
            rvConversation.layoutManager = layoutManager
            rvConversation.adapter = conversationAdapter
            txtStartNewChat.isVisible = viewModel.pingRes.canVisitorOrContactStartNewConversation
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
        binding.txtStartNewChat.setOnClickListener {
            getBaseActivity().addFragment(ChatFragment.newInstance())
        }
    }

    private fun observeViewModel() {
        viewModel.showConversationList.observe(this, {
            if (conversationAdapter.itemCount == 0) {
                conversationAdapter.setItems(it.toMutableList())
            } else {
                conversationAdapter.addItems(it.toMutableList())
            }
        })

        viewModel.showLoadMore.observe(this, {
            conversationAdapter.showLoadMore(it)
        })

        viewModel.updateConversationItem.observe(this, { conversationDetailItem ->
            conversationAdapter.updateConversation(conversationDetailItem)
        })

        viewModel.updateConversationItemMessage.observe(this, { messageItem ->
            conversationAdapter.updateConversationMessage(messageItem)
        })

        viewModel.updateReadStatus.observe(this, { conversationHasReadEvent ->
            conversationAdapter.updateReadStatus(conversationHasReadEvent)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_conversation_list, menu)
        menu.findItem(R.id.action_close).icon.setTintFromBackground()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                getBaseActivity().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}