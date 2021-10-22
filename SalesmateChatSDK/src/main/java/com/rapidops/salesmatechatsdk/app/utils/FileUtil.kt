package com.rapidops.salesmatechatsdk.app.utils

import java.io.File

object FileUtil {

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
}
