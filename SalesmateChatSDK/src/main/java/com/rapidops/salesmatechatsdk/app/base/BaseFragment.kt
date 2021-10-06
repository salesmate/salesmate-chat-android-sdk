package com.rapidops.salesmatechatsdk.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.interfaces.IBackPress
import com.rapidops.salesmatechatsdk.databinding.FBaseLayoutBinding
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateChatException


internal abstract class BaseFragment<VM : BaseViewModel> : Fragment(), IBackPress {

    protected abstract fun getLayoutView(inflater: LayoutInflater): View

    protected abstract fun initializeViewModel(): VM

    protected lateinit var viewModel: VM

    abstract fun setUpUI()

    private var fBaseBinding: FBaseLayoutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initializeViewModel()

    }

    private fun observeBaseViewModel() {
        viewModel.progress.observe(this.viewLifecycleOwner, { show ->
            if (show) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        viewModel.dataProgress.observe(this.viewLifecycleOwner, { show ->
            if (show) {
                showDataProgress()
            } else {
                hideDataProgress()
            }
        })

        viewModel.salesMateChatException.observe(this.viewLifecycleOwner, {
            showSoftError(it)
        })
    }

    protected open fun getProgressView(): View? {
        return fBaseBinding?.fContentLoadProgress
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fBaseBinding = FBaseLayoutBinding.inflate(layoutInflater, container, false)
        val layoutView = getLayoutView(inflater)
        fBaseBinding?.fBaseLayoutContent?.addView(layoutView)

        observeBaseViewModel()

        return fBaseBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    fun hideSoftKeyboard() {
        getBaseActivity().hideSoftKeyboard()
    }

    private fun showProgress() {
        getProgressView()?.isVisible = true
    }

    private fun hideProgress() {
        if (childFragmentManager.isStateSaved) return
        getProgressView()?.isVisible = false
    }

    private fun showDataProgress() {
        getProgressView()?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        getProgressView()?.isVisible = true
    }

    private fun hideDataProgress() {
        getProgressView()?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        getProgressView()?.isVisible = false
    }

    fun showAlertMessage(message: String, title: String? = null) {
        val builder = AppCompatAlertDialog.Builder()
        title?.let { builder.setTitle(it) }
        builder.setMessage(message)
        builder.setPositiveButton(R.string.dialog_ok)
        AppCompatAlertDialog.newInstance(builder)
            .show(childFragmentManager, AppCompatAlertDialog::class.java.simpleName)
    }

    fun showUnknownErrorAlertMessage() {
        val builder = AppCompatAlertDialog.Builder()
        builder.setTitle(R.string.error)
        builder.setMessage(R.string.unknown_error_message)
        builder.setNegativeButton(R.string.dialog_close)
        AppCompatAlertDialog.newInstance(builder)
            .show(childFragmentManager, AppCompatAlertDialog::class.java.simpleName)
    }


    fun showSoftError(refreshException: SalesmateChatException) {
        refreshException.printStackTrace()
        when (refreshException.kind) {
            SalesmateChatException.Kind.UNEXPECTED -> showUnknownErrorAlertMessage()
            SalesmateChatException.Kind.NETWORK -> showAlertMessage(getString(R.string.df_no_network_connection_tv_msg))
            SalesmateChatException.Kind.REST_API -> {
                refreshException.error?.let {
                    showAlertMessage(it.message)
                }
            }
        }
    }


    fun getBaseActivity(): BaseActivity<*> {
        return activity as BaseActivity<*>
    }

    fun getParentBaseFragment(): BaseFragment<*> {
        return parentFragment as BaseFragment<*>
    }

    open fun getChildContainerLayoutId(): Int = -1

    fun addChildFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if (childFragmentManager.isStateSaved) return
        val tag = fragment.javaClass.name
        val transaction = childFragmentManager.beginTransaction()

        val currentFragment = getChildCurrentFragment()
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        transaction.add(getChildContainerLayoutId(), fragment, tag).show(fragment)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }

    fun getChildCurrentFragment() =
        childFragmentManager.findFragmentById(getChildContainerLayoutId())

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        hideSoftKeyboard()
        /*if (!hidden && isAdded) {

        }*/
    }

    override fun onDestroyView() {
        hideProgress()
        super.onDestroyView()
    }

    override fun onBackPressed(): Boolean {

        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()
            return true
        } else if (parentFragment != null && requireParentFragment().childFragmentManager.backStackEntryCount > 1) {
            requireParentFragment().childFragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun clearBackStackUpto(
        removeFragmentUptoFragmentId: String,
        isIncludeThisFragment: Boolean = false
    ) {
        val flag = if (isIncludeThisFragment) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
        childFragmentManager.popBackStackImmediate(removeFragmentUptoFragmentId, flag)
    }

}