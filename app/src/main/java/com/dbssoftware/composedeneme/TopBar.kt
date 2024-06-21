package com.dbssoftware.composedeneme

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TopBar(
    title: String,
    navigationIcon: ImageVector? = null,
    navigationIconClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = if (navigationIcon != null && navigationIconClick != null) {
            {
                IconButton(onClick = navigationIconClick) {
                    Icon(navigationIcon, contentDescription = "Back")
                }
            }
        } else null
    )
}
