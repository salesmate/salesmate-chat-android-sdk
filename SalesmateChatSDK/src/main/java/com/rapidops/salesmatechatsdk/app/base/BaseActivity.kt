package com.rapidops.salesmatechatsdk.app.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.interfaces.IFragmentSupport
import com.rapidops.salesmatechatsdk.app.utils.ConnectivityLiveData
import com.rapidops.salesmatechatsdk.databinding.ABaseLayoutBinding
import com.rapidops.salesmatechatsdk.domain.exception.SalesmateChatException


internal abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {


    private lateinit var baseBinding: ABaseLayoutBinding

    protected abstract fun getLayoutView(): View

    protected abstract fun setUpUI(savedInstanceState: Bundle?)

    protected abstract fun initializeViewModel(): VM

    protected lateinit var viewModel: VM

    open fun onInternetConnected() {}

    open fun onInternetDisconnected() {}

    open fun getToolbar(): Toolbar {
        return Toolbar(this)
    }

    protected open fun getProgressView(): View {
        return baseBinding.vContentLoadProgress
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ABaseLayoutBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)

        baseBinding.aBaseLayoutContent.addView(getLayoutView())

        viewModel = initializeViewModel()


        observeBaseViewModel()

        addConnectivityListener()

        setUpUI(savedInstanceState)

    }

    private fun observeBaseViewModel() {
        viewModel.progress.observe(this, { show ->
            if (show) {
                showProgress()
            } else {
                hideProgress()
            }
        })

        viewModel.dataProgress.observe(this, { show ->
            if (show) {
                showDataProgress()
            } else {
                hideDataProgress()
            }
        })

        viewModel.salesMateChatException.observe(this, {
            showSoftError(it)
        })
    }

    private fun showProgress() {
        getProgressView().isVisible = true
    }

    private fun hideProgress() {
        if (!isDestroyed) getProgressView().isVisible = false
    }

    private fun showDataProgress() {
        getProgressView().setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        getProgressView().isVisible = true
    }

    private fun hideDataProgress() {
        getProgressView().setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        getProgressView().isVisible = false
    }

    fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        isAnimate: Boolean = false
    ) {
        val tag = fragment.javaClass.name
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
//        if (isAnimate) transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout)
        transaction.replace(getFragmentContainerLayoutId(), fragment, tag)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }

    private fun getFragmentContainerLayoutId(): Int {
        return if (this is IFragmentSupport) {
            this.getContainerLayoutId()
        } else {
            R.id.a_base_layout_content
        }
    }


    fun addFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
        showAnimation: Boolean = true
    ) {
        if (supportFragmentManager.isStateSaved) return
        val tag = fragment.javaClass.name
        val transaction = supportFragmentManager.beginTransaction()
        if (showAnimation) {
            transaction
                .setCustomAnimations(R.anim.enter_fragment, R.anim.exit_fragment, R.anim.pop_enter_fragment, R.anim.pop_exit_fragment)
        }
        val currentFragment = getCurrentFragment()
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        transaction.add(getFragmentContainerLayoutId(), fragment, tag).show(fragment)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
    }

    private fun getCurrentFragment() = supportFragmentManager.findFragmentById(getFragmentContainerLayoutId())

    fun popBackStackUp(fragmentName: String): Boolean {
        if (supportFragmentManager.isStateSaved) {
            finish()
            return false
        }
        return supportFragmentManager.popBackStackImmediate(fragmentName, 0)
    }

    fun popBackStack() {
        if (!supportFragmentManager.isStateSaved) {
            supportFragmentManager.popBackStack()
        }
    }

    fun showAlertMessage(message: String, title: String? = null) {
        val builder = AppCompatAlertDialog.Builder()
        builder.setMessage(message)
        title?.let { builder.setTitle(it) }
        builder.setPositiveButton(R.string.dialog_ok)
        AppCompatAlertDialog.newInstance(builder)
            .show(supportFragmentManager, AppCompatAlertDialog::class.java.simpleName)
    }

    fun showUnknownErrorAlertMessage() {
        val builder = AppCompatAlertDialog.Builder()
        builder.setTitle(R.string.error)
        builder.setMessage(R.string.unknown_error_message)
        builder.setNegativeButton(R.string.dialog_close)
        AppCompatAlertDialog.newInstance(builder)
            .show(supportFragmentManager, AppCompatAlertDialog::class.java.simpleName)
    }

    fun showSoftError(kpsException: SalesmateChatException) {
        when (kpsException.kind) {
            SalesmateChatException.Kind.UNEXPECTED -> showUnknownErrorAlertMessage()
            SalesmateChatException.Kind.NETWORK -> showAlertMessage(getString(R.string.df_no_network_connection_tv_msg))
            SalesmateChatException.Kind.REST_API -> {
                kpsException.error?.let {
                    showAlertMessage(it.message)
                }
            }
        }
    }


    fun hideSoftKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun showSoftKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) finish()
        else super.onBackPressed()
    }

    private fun addConnectivityListener() {
        ConnectivityLiveData(application).observe(this, { isConnected ->
            if (isConnected) {
                viewModel.connectSocket()
            }
        })
    }
}
