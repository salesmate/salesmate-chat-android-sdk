package com.rapidops.salesmatechatsdk.app.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
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
        val name = fileName.substringBeforeLast(".")
        val extension = fileName.substringAfterLast(".")
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
        return this.toDouble().sizeInMb <= FileUtil.MAX_FILE_SIZE_SUPPORT_IN_MB
    }


    fun DocumentFile.isImageFile(context: Context): Boolean {
        val imageMimeType = "image/"
        val mimeType: String =
            type ?: getMimeType(this.uri.toString()) ?: getMimeType(context, this.uri) ?: ""
        return mimeType.startsWith(imageMimeType)
    }

    fun String.isImageType(): Boolean {
        return this.startsWith("image/")
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
                    MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(uri.toFile()).toString())
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
}
