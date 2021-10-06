package com.rapidops.salesmatechatsdk.app.utils

import java.util.*

/**
 * Created by admin on 12/8/15.
 */
class ColorGenerator private constructor(private val mColors: List<Int>) {
    companion object {
        var DEFAULT: ColorGenerator
        var MATERIAL: ColorGenerator
        var IOS8: ColorGenerator
        var MATERIAL2: ColorGenerator
        var MATERIAL3: ColorGenerator
        var MATERIAL4: ColorGenerator
        fun create(colorList: List<Int>): ColorGenerator {
            return ColorGenerator(colorList)
        }

        init {
            DEFAULT = create(
                listOf(
                    -0xe9c9c,
                    -0xa7aa7,
                    -0x65bc2,
                    -0x1b39d2,
                    -0x98408c,
                    -0xa65d42,
                    -0xdf6c33,
                    -0x529d59,
                    -0x7fa87f
                )
            )
            MATERIAL = create(
                listOf(
                    -0x1a8c8d,
                    -0xf9d6e,
                    -0x459738,
                    -0x6a8a33,
                    -0x867935,
                    -0x9b4a0a,
                    -0xb03c09,
                    -0xb22f1f,
                    -0xb24954,
                    -0x7e387c,
                    -0x512a7f,
                    -0x759b,
                    -0x2b1ea9,
                    -0x2ab1,
                    -0x48b3,
                    -0x5e7781,
                    -0x6f5b52
                )
            )
            MATERIAL2 = create(
                listOf(
                    -0x106566,
                    -0xb704f,
                    -0x316c28,
                    -0x4c6225,
                    -0x605726,
                    -0x6f3507,
                    -0x7e2b06,
                    -0x7f2116,
                    -0x7f343c,
                    -0x5a2959,
                    -0x3a1e5b,
                    -0x3380,
                    -0x48b3,
                    -0x546f,
                    -0x43555c,
                    -0x4f413b
                )
            )
            MATERIAL3 = create(
                listOf(
                    -0x759b,
                    -0x7e387c,
                    -0x48b3,
                    -0x6a8a33,
                    -0x9b4a0a,
                    -0xf9d6e,
                    -0x867935,
                    -0xb03c09,
                    -0xb22f1f,
                    -0xb24954,
                    -0x3223c7,
                    -0x35d8,
                    -0xb22f1f
                )
            )
            MATERIAL4 = create(
                listOf(
                    -0xa9de,
                    -0x7fa801,
                    -0xb27701,
                    -0xbe97,
                    -0x98c549,
                    -0xfc560c,
                    -0xd93a26,
                    -0xff5384,
                    -0x3f35cd,
                    -0x4dff,
                    -0xff3378
                )
            )
            IOS8 = create(
                listOf(
                    -0x1b4810,
                    -0xa43501,
                    -0x7e0c03,
                    -0x282829,
                    -0x5b187a,
                    -0xa4c9,
                    -0x39bc04,
                    -0xd598
                )
            )
        }
    }

    private val mRandom: Random = Random(System.currentTimeMillis())
    val randomColor: Int
        get() = mColors[mRandom.nextInt(mColors.size)]

    fun getColor(key: Any): Int {
        return mColors[Math.abs(key.hashCode()) % mColors.size]
    }

}