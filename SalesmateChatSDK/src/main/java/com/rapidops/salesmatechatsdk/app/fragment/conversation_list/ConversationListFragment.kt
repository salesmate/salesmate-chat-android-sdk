package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.adapter.ConversationAdapter
import com.rapidops.salesmatechatsdk.app.interfaces.EndlessScrollListener
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.foregroundColor
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.getDrawableForBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.setTintFromBackground
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil.updateActionTint
import com.rapidops.salesmatechatsdk.databinding.FConversationListBinding
import com.rapidops.salesmatechatsdk.domain.models.ConversationDetailItem
import java.util.*


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
                ?.getDrawableForBackground()
        binding.toolbar.setNavigationOnClickListener {
            getBaseActivity().popBackStack()
        }
        binding.root.setBackgroundColor(ColorUtil.backGroundColor)

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
            txtStartNewChat.updateActionTint()
            txtStartNewChat.compoundDrawablesRelative.forEach {
                it?.setTint(ColorUtil.actionColor.foregroundColor())
            }
            rvConversation.addOnScrollListener(endlessScrollListener)
            rvConversation.layoutManager = layoutManager
            rvConversation.adapter = conversationAdapter
            txtStartNewChat.isVisible = viewModel.pingRes.canVisitorOrContactStartNewConversation
        }

        observeViewModel()
        viewModel.subscribe()
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
            val list = conversationAdapter.getItems()
            val indexOfFirst =
                list.indexOfFirst { it.conversations?.id == conversationDetailItem.conversations?.id }
            if (indexOfFirst != -1) {
                list[indexOfFirst] = conversationDetailItem
                Collections.swap(list, indexOfFirst, 0)
                conversationAdapter.notifyItemMoved(indexOfFirst, 0)
            } else {
                list.add(0, conversationDetailItem)
                conversationAdapter.notifyItemInserted(0)
            }
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