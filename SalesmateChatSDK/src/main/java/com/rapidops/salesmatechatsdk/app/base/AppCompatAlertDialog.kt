package com.rapidops.salesmatechatsdk.app.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.Serializable


internal class AppCompatAlertDialog : DialogFragment() {
    private var builder: Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        builder = requireArguments().getSerializable(EXTRA_BUILDER) as Builder
        var alertDialogBuilder = AlertDialog.Builder(requireContext())
        if (!builder!!.isCancellable()) {
            alertDialogBuilder = alertDialogBuilder.setCancelable(builder!!.isCancellable())
            this.isCancelable = builder!!.isCancellable()
        }
        if (builder!!.titleResId != -1) {
            alertDialogBuilder = alertDialogBuilder.setTitle(builder!!.titleResId)
        }
        if (builder!!.messageResId != -1) {
            alertDialogBuilder = alertDialogBuilder.setMessage(builder!!.messageResId)
        }
        if (builder!!.getMessage() != "") {
            alertDialogBuilder = alertDialogBuilder.setMessage(builder!!.getMessage())
        }
        if (builder!!.negativeTextResId != -1) {
            alertDialogBuilder =
                alertDialogBuilder.setNegativeButton(builder!!.negativeTextResId) { dialogInterface, which ->
                    builder!!.mNegativeButtonListener?.onClick(dialogInterface, which)
                    dialogInterface.cancel()
                }
        }
        if (builder!!.positiveTextResId != -1) {
            alertDialogBuilder =
                alertDialogBuilder.setPositiveButton(builder!!.positiveTextResId) { dialogInterface, which ->
                    builder!!.mPositiveButtonListener?.onClick(dialogInterface, which)
                    dialogInterface.dismiss()
                }
        }
        return alertDialogBuilder.create()
    }

    class Builder() : Serializable {
        @StringRes
        var titleResId = -1
            private set

        @StringRes
        var messageResId = -1
            private set

        @StringRes
        var positiveTextResId = -1
            private set

        @StringRes
        var negativeTextResId = -1
            private set

        private var requestKey = ""

        private var title = ""
        private var message = ""
        private var cancellable: Boolean = false

        var mPositiveButtonListener: DialogInterface.OnClickListener? = null
        var mNegativeButtonListener: DialogInterface.OnClickListener? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setTitle(@StringRes titleResId: Int): Builder {
            this.titleResId = titleResId
            return this
        }

        fun setMessage(@StringRes messageResId: Int): Builder {
            this.messageResId = messageResId
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message.trim { it <= ' ' }
            return this
        }

        fun setPositiveButton(@StringRes textResId: Int): Builder {
            this.positiveTextResId = textResId
            return this
        }

        fun setPositiveButton(
            @StringRes textResId: Int,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.positiveTextResId = textResId
            this.mPositiveButtonListener = listener
            return this
        }

        fun setNegativeButton(@StringRes textResId: Int): Builder {
            this.negativeTextResId = textResId
            return this
        }

        fun setNegativeButton(
            @StringRes textResId: Int,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.negativeTextResId = textResId
            this.mNegativeButtonListener = listener
            return this
        }

        fun setCancellable(isCancellable: Boolean): Builder {
            this.cancellable = isCancellable
            return this
        }

        fun isCancellable(): Boolean {
            return cancellable
        }

        fun setRequestKey(setRequestKey: String): Builder {
            this.requestKey = setRequestKey.trim()
            return this
        }

        fun getRequestKey(): String {
            return requestKey
        }

        fun getMessage(): String {
            return message
        }
    }

    companion object {
        val EXTRA_BUILDER = "EXTRA_BUILDER"

        fun newInstance(builder: Builder): AppCompatAlertDialog {
            val fragment = AppCompatAlertDialog()
            val args = Bundle()
            args.putSerializable(EXTRA_BUILDER, builder)
            fragment.arguments = args
            return fragment
        }
    }
}