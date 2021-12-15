package com.rapidops.salesmatechatsdk.app.utils

import android.Manifest
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.rapidops.salesmatechatsdk.R
import com.rapidops.salesmatechatsdk.app.base.BaseActivity
import com.rapidops.salesmatechatsdk.core.SalesmateChat
import java.io.*


object FileUtil {

    private const val fileProviderAuthority: String = "com.rapidops.salesmatechatsdk.fileprovider"

    const val MAX_FILE_SIZE_SUPPORT_IN_MB = 25

    private const val EOF = -1

    val Double.sizeInKb get() = this / 1024
    val Double.sizeInMb get() = sizeInKb / 1024
    val Double.sizeInGb get() = sizeInMb / 1024
    val Double.sizeInTb get() = sizeInGb / 1024

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
    val File.sizeInMb get() = sizeInKb / 1024
    val File.sizeInGb get() = sizeInMb / 1024
    val File.sizeInTb get() = sizeInGb / 1024

    fun File.sizeStr(): String = size.toString()
    fun File.sizeStrInKb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInKb)
    fun File.sizeStrInMb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInMb)
    fun File.sizeStrInGb(decimals: Int = 0): String = "%.${decimals}f".format(sizeInGb)

    fun File.sizeStrWithBytes(): String = sizeStr() + "b"
    fun File.sizeStrWithKb(decimals: Int = 0): String = sizeStrInKb(decimals) + "Kb"
    fun File.sizeStrWithMb(decimals: Int = 0): String = sizeStrInMb(decimals) + "Mb"
    fun File.sizeStrWithGb(decimals: Int = 0): String = sizeStrInGb(decimals) + "Gb"

    val UNSUPPORTED_FILE = listOf(
        ".exe",
        ".cmd",
        ".msi",
        ".com",
        ".hta",
        ".html",
        ".htm",
        ".js",
        ".jar",
        ".vbs",
        ".vb",
        ".sfx",
        ".bat",
        ".ps1",
        ".war",
        ".sh",
        ".bash",
        ".command"
    )

    fun Uri.getFile(context: Context): File {
        val inputStream = context.contentResolver.openInputStream(this)
        val fileName: String = getFileName(context, this)
        val splitName: Array<String> = splitFileName(fileName)
        var tempFile = File.createTempFile(splitName[0], splitName[1])
        tempFile = rename(tempFile, fileName)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            copy(inputStream, out)
            inputStream.close()
        }
        out?.close()
        return tempFile
    }

    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf(File.separator)
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    private fun rename(file: File, newName: String): File {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    private fun copy(input: InputStream, output: OutputStream?): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output?.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }

    fun Long.isValidFileSize(): Boolean {
        return this.toDouble().sizeInMb <= MAX_FILE_SIZE_SUPPORT_IN_MB
    }

    fun DocumentFile.isGifFile(context: Context): Boolean {
        val gifMimeType = "image/gif"
        val mimeType: String = getMimeType(context)
        return mimeType == gifMimeType
    }


    fun DocumentFile.isImageFile(context: Context): Boolean {
        val imageMimeType = "image/"
        val mimeType: String = getMimeType(context)
        return mimeType.startsWith(imageMimeType)
    }

    private fun DocumentFile.getMimeType(context: Context): String {
        return type ?: getMimeType(uri.toString()) ?: getMimeType(context, uri) ?: ""
    }

    fun String.isImageType(): Boolean {
        return this.startsWith("image/")
    }

    fun String.isImageUrl(): Boolean {
        return this.lowercase().endsWith(".jpg") || this.lowercase()
            .endsWith(".jpeg") || this.lowercase().endsWith(".png")
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        var mimeType: String?
        mimeType =
            if (uri.scheme != null && uri.scheme!! == ContentResolver.SCHEME_CONTENT) { //If scheme is a content
                context.contentResolver.getType(uri)
            } else { //If scheme is a File
                val extension =
                    MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }

        if (mimeType.isNullOrEmpty()) {
            mimeType = uri.getQueryParameter("mimeType")
        }
        return mimeType
    }


    private fun getExtension(context: Context, uri: Uri): String? {
        val mimeType = getMimeType(context, uri)
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(mimeType)
    }

    fun getExtension(filePath: String?): String {
        if (filePath == null) return ""
        val dot = filePath.lastIndexOf(".")
        return if (dot >= 0) {
            filePath.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    private fun getExtensionFromMimeType(mimeType: String): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

    fun createTextFile(name: String, data: String): File {
        val file = File.createTempFile(name, ".txt")
        try {
            val writer = FileWriter(file)
            writer.append(data)
            writer.flush()
            writer.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun getUriFromFileProvider(context:Context,file: File): Uri {
        val contentUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                fileProviderAuthority,
                file
            )
        } else {
            Uri.fromFile(file)
        }
        return contentUri
    }

    fun String?.isInValidFile(): Boolean {
        val extension = getExtension(this)
        return extension.isEmpty() || UNSUPPORTED_FILE.contains(extension.lowercase())
    }

    fun directDownloadFileWithDownloadManager(
        context: Context,
        fileName: String,
        url: String,
        mimeType: String
    ) {
        EasyPermissions.requestPermissions(
            context,
            BaseActivity.REQ_STORAGE_PERMISSION,
            object : EasyPermissions.PermissionCallbacks {
                override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
                    downloadFileWithDownloadManager(context, url, fileName, mimeType)
                }

                override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
                    Log.w(FilePicker.TAG, "Denied Storage Permission")
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
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun downloadFileWithDownloadManager(
        context: Context,
        url: String,
        fileName: String,
        mimeType: String
    ) {
        val appSettingsDataSource = SalesmateChat.daggerDataComponent.getAppSettingsDataSource()
        Toast.makeText(
            context,
            context.getString(R.string.file_download_message, fileName),
            Toast.LENGTH_SHORT
        ).show()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var request = DownloadManager.Request(Uri.parse(url))
            .addRequestHeader("x-contact-id", appSettingsDataSource.contactData?.id ?: "")
            .addRequestHeader("x-unique-id", appSettingsDataSource.androidUniqueId)
            .addRequestHeader("x-linkname", appSettingsDataSource.salesMateChatSetting.tenantId)
            .addRequestHeader("x-workspace-id", appSettingsDataSource.salesMateChatSetting.workspaceId)
            .addRequestHeader("x-verified-id", appSettingsDataSource.verifiedId)
        request =
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request =
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request = request.setTitle(fileName)
        request = request.setDescription("Downloading a file")
        if (mimeType.isNotEmpty()) {
            request = request.setMimeType(mimeType)
        }
        downloadManager.enqueue(request)
    }

}
