package com.senyor_o.firebasechat.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val id: Int,
    val title: String,
    val icon: ImageVector,
    val onClick: (Int) -> Unit
)
