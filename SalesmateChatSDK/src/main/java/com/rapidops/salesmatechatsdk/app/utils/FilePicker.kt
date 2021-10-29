package com.rapidops.salesmatechatsdk.app.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AlertDialog
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseActivity
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File

internal class FilePicker(private val context: Context) {

    private var destinationUri: Uri? = null
    var filePickerListener: FilePickerListener? = null
    private var requestCode = -1

    companion object {
        const val TAG = "FilePicker"
        const val REQ_CAMERA = 101
        const val REQ_GALLERY = 102
    }

    private fun getCaptureImageIntent(launch: (intent: Intent) -> Unit) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        destinationUri = createTempFileUri()
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, destinationUri)
        launch(cameraIntent)
        requestCode = REQ_CAMERA
    }

    private fun getGalleryIntent(launch: (intent: Intent) -> Unit) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        launch(intent)
        requestCode = REQ_GALLERY
    }

    private fun createTempFileUri(): Uri {
        val timeStamp = DateTimeFormat.forPattern("yyyyMMdd_HHmmss").print(DateTime.now())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image: File = File.createTempFile(imageFileName, ".jpg", storageDir)
        return FileUtil.getUriFromFileProvider(context, image)
    }

    fun onActivityResult(activityResult: ActivityResult) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_GALLERY) {
                activityResult.data?.data?.let {
                    filePickerListener?.onFilePicked(it)
                }
            } else if (requestCode == REQ_CAMERA) {
                destinationUri?.let {
                    filePickerListener?.onFilePicked(it)
                }
            }
        }
        requestCode = -1
        destinationUri = null
    }

    fun showGalleryPickerWithPermissionCheck(launch: (intent: Intent) -> Unit) {
        EasyPermissions.requestPermissions(
            context,
            BaseActivity.REQ_STORAGE_PERMISSION,
            object : EasyPermissions.PermissionCallbacks {
                override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
                    Log.w(TAG, "Granted Storage Permission")
                    getGalleryIntent {
                        launch(it)
                    }
                }

                override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
                    Log.w(TAG, "Denied Storage Permission")
                }

                override fun onPermissionsPermanentlyDeclined(
                    requestCode: Int,
                    perms: List<String>
                ) {
                    AlertDialog
                        .Builder(context)
                        .setCancelable(false)
                        .setTitle(R.string.title_permission_denied)
                        .setMessage(R.string.msg_storage_permission)
                        .setNegativeButton(R.string.lbl_goto_settings) { _, _ -> EasyPermissions.startSetting() }
                        .setPositiveButton(R.string.dialog_ok) { _, _ -> }.show()
                }

            },
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    fun showCameraPickerWithPermissionCheck(launch: (intent: Intent) -> Unit) {
        EasyPermissions.requestPermissions(
            context,
            BaseActivity.REQ_CAMERA_PERMISSION,
            object : EasyPermissions.PermissionCallbacks {
                override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
                    Log.w(TAG, "Granted Camera Permission")
                    getCaptureImageIntent {
                        launch(it)
                    }
                }

                override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
                    Log.w(TAG, "Denied Camera Permission")
                }

                override fun onPermissionsPermanentlyDeclined(
                    requestCode: Int,
                    perms: List<String>
                ) {
                    AlertDialog
                        .Builder(context)
                        .setCancelable(false)
                        .setTitle(R.string.title_permission_denied)
                        .setMessage(R.string.msg_camera_permission)
                        .setNegativeButton(R.string.lbl_goto_settings) { _, _ -> EasyPermissions.startSetting() }
                        .setPositiveButton(R.string.dialog_ok) { _, _ -> }.show()
                }

            },
            Manifest.permission.CAMERA
        )
    }

}


internal interface FilePickerListener {
    fun onFilePicked(uri: Uri)
}