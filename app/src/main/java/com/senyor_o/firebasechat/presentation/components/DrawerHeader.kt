package com.senyor_o.firebasechat.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerHeader(
    text: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Column(
       modifier = Modifier
           .fillMaxWidth()
           .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .size(128.dp),
            onClick = {
                onClick()
            }
        ) {
            AvatarImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .size(128.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.h2
        )
    }
}