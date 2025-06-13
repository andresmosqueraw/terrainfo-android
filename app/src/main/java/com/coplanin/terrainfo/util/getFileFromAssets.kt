package com.coplanin.terrainfo.util

import android.content.Context
import java.io.File

/**
 * Devuelve un `File` real en el `cacheDir` copiado desde assets solo la 1.ª vez.
 *
 * ```
 * val mbtiles = getFileFromAssets(context, "planet2.mbtiles")
 * ```
 */
fun getFileFromAssets(context: Context, fileName: String): File =
    File(context.cacheDir, fileName).also { outFile ->
        if (!outFile.exists()) {
            context.assets.open(fileName).use { input ->
                outFile.outputStream().use { output -> input.copyTo(output) }
            }
        }
    }
