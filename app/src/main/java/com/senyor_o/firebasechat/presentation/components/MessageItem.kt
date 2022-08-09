package com.senyor_o.firebasechat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest

private val ForeignOwnMessage = RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
private val AuthorOwnMessage = RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)

@Composable
fun MessageItem(
    text: String,
    isCurrentUser: Boolean,
    userName: String?,
    imageUrl: String?,
    time: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            if(!isCurrentUser) {
                AvatarImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .size(32.dp)
                )
            }
            Spacer(modifier = Modifier
                .width(
                    8.dp
                )
            )
            Column(
                modifier = Modifier
                    .background(
                        if (isCurrentUser) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.LightGray
                        },
                        shape = if (isCurrentUser) {
                            AuthorOwnMessage
                        } else {
                            ForeignOwnMessage
                        }
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if(!isCurrentUser && !userName.isNullOrBlank()) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                        ,
                        fontWeight = FontWeight.Bold,
                        text = userName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                ConstraintLayout {
                    val (contentRef, timeRef) = createRefs()
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .constrainAs(contentRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            },
                        text = text,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(timeRef) {
                                top.linkTo(contentRef.bottom)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                        text = time,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun MessageItemPreview() {
    MessageItem(
        "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!",
        false,
        "Perry",
        null,
        "00:20",
    )
}