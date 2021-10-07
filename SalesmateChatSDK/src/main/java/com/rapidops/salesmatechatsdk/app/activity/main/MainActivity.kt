package com.rapidops.salesmatechatsdk.app.activity.main

import android.os.Bundle
import android.view.View
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseActivity
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.app.fragment.recent_list.RecentChatFragment
import com.rapidops.salesmatechatsdk.app.interfaces.IFragmentSupport
import com.rapidops.salesmatechatsdk.databinding.AMainBinding


internal class MainActivity : BaseActivity<MainViewModel>(), IFragmentSupport {

    private lateinit var binding: AMainBinding

    override fun getContainerLayoutId(): Int {
        return R.id.a_main_fl_fragment_container
    }

    override fun initializeViewModel(): MainViewModel {
        return obtainViewModel(MainViewModel::class.java)
    }

    override fun getLayoutView(): View {
        binding = AMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun setUpUI(savedInstanceState: Bundle?) {

        observeViewModel()

        viewModel.subscribe()

    }

    private fun observeViewModel() {
        viewModel.showRecentChatList.observe(this, {
            addFragment(RecentChatFragment.newInstance())
        })
    }

}
