package com.senyor_o.firebasechat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senyor_o.firebasechat.domain.model.DrawerItem

@Composable
fun DrawerMenuItem(
    item: DrawerItem,
    isSelected: Boolean = false,
    activeHighlightColor: Color = MaterialTheme.colors.onBackground,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = MaterialTheme.colors.onBackground,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) activeHighlightColor else Color.Transparent)
            .clickable {
                onItemClick(item.id)
            }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (isSelected) activeTextColor else inactiveTextColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            color = if(isSelected) activeTextColor else inactiveTextColor
        )
    }

}