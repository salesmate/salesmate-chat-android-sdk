package com.rapidops.salesmatechatsdk.app.fragment

import android.view.LayoutInflater
import android.view.View
import com.rapidops.salesmatechatsdk.app.base.BaseFragment
import com.rapidops.salesmatechatsdk.app.extension.obtainViewModel
import com.rapidops.salesmatechatsdk.databinding.FHomeBinding

class HomeFragment : BaseFragment<HomeViewModel>() {

    private lateinit var binding: FHomeBinding

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun initializeViewModel(): HomeViewModel {
        return obtainViewModel(HomeViewModel::class.java)
    }

    override fun setUpUI() {

    }
}