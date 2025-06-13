package com.coplanin.terrainfo.ui.map

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView

/**
 * Call once from any composable to obtain a MapView that automatically
 * follows the host lifecycle.
 */
@Composable
fun rememberMapViewWithLifecycle(
    context: Context = LocalContext.current,
    savedInstanceState: Bundle? = null
): MapView {
    // Initialise Mapbox ONE time for the whole process.
    Mapbox.getInstance(context.applicationContext)

    val mapView = remember {
        MapView(context).apply { onCreate(savedInstanceState) }
    }

    // Hook MapView to lifecycle
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner)  = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner)  = mapView.onPause()
            override fun onStop(owner: LifecycleOwner)   = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner)= mapView.onDestroy()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    return mapView
}