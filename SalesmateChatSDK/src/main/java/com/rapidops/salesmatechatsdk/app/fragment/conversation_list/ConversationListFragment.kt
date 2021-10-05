package com.rapidops.salesmatechatsdk.app.fragment.conversation_list

import android.view.LayoutInflater
import android.view.View
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.databinding.FHomeBinding

internal class ConversationListFragment : BaseFragment<ConversationListViewModel>() {

    private lateinit var binding: FHomeBinding

    companion object {
        fun newInstance(): ConversationListFragment {
            return ConversationListFragment()
        }
    }

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun initializeViewModel(): ConversationListViewModel {
        return obtainViewModel(ConversationListViewModel::class.java)
    }

    override fun setUpUI() {

    }
}