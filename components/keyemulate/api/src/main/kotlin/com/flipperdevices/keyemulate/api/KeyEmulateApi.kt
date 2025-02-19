package com.flipperdevices.keyemulate.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bridge.connection.feature.emulate.api.model.EmulateConfig

interface KeyEmulateApi {
    @Composable
    @Suppress("NonSkippableComposable")
    fun ComposableEmulateButton(
        modifier: Modifier,
        emulateConfig: EmulateConfig,
        isSynchronized: Boolean,
        componentContext: ComponentContext
    )
}
