package com.coplanin.terrainfo.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun copyAssetToFile(context: Context, assetName: String): File {
    val outFile = File(context.filesDir, assetName)
    if (!outFile.exists()) {
        context.assets.open(assetName).use { input ->
            FileOutputStream(outFile).use { output -> input.copyTo(output) }
        }
    }
    return outFile
}