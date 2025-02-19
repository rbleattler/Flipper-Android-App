package com.flipperdevices.bridge.connection.screens.nopermission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import flipperapp.components.bridge.connection.sample.shared.generated.resources.Res
import flipperapp.components.bridge.connection.sample.shared.generated.resources.connection_test_require_permission
import org.jetbrains.compose.resources.stringResource

class ConnectionNoPermissionDecomposeComponent(
    componentContext: ComponentContext
) : ScreenDecomposeComponent(componentContext) {
    @Composable
    override fun Render() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(Res.string.connection_test_require_permission))
        }
    }
}
