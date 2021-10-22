package com.rapidops.salesmatechatsdk.app.fragment.upload_attachment

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.rapidops.salesmatechatsdk.app.base.BaseBottomSheetDialogFragment
import com.rapidops.salesmatechatsdk.app.utils.ColorUtil
import com.rapidops.salesmatechatsdk.app.utils.FilePicker
import com.rapidops.salesmatechatsdk.app.utils.FilePickerListener
import com.rapidops.salesmatechatsdk.databinding.FUploadAttachmentBinding

internal class UploadAttachmentDialogFragment :
    BaseBottomSheetDialogFragment() {
    private lateinit var binding: FUploadAttachmentBinding

    var listener: UploadAttachmentDialogFragmentListener? = null

    override fun getLayoutView(inflater: LayoutInflater): View {
        binding = FUploadAttachmentBinding.inflate(inflater)
        return binding.root
    }

    private var filePicker: FilePicker? = null
    override fun setUpUI() {
        filePicker = FilePicker(requireContext())
        binding.txtUploadFromCamera.compoundDrawablesRelative.forEach {
            it?.setTint(ColorUtil.actionColor)
        }

        binding.txtUploadFromGallery.compoundDrawablesRelative.forEach {
            it?.setTint(ColorUtil.actionColor)
        }

        binding.txtUploadFromGallery.setOnClickListener {
            filePicker?.showGalleryPickerWithPermissionCheck {
                getAttachmentContent.launch(it)
            }
        }

        binding.txtUploadFromCamera.setOnClickListener {
            filePicker?.showCameraPickerWithPermissionCheck {
                getAttachmentContent.launch(it)
            }
        }

        filePicker?.filePickerListener = object : FilePickerListener {
            override fun onFilePicked(uri: Uri) {
                dismiss()
                listener?.onFilePicked(uri)
            }

        }
    }

    private val getAttachmentContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            filePicker?.onActivityResult(it)
        }
}

internal interface UploadAttachmentDialogFragmentListener {
    fun onFilePicked(uri: Uri)
}