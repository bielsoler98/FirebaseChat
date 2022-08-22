package com.senyor_o.firebasechat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.firestore.FieldValue
import com.senyor_o.firebasechat.domain.model.Message
import com.senyor_o.firebasechat.domain.model.User
import com.senyor_o.firebasechat.utils.Constants.IMAGE_TYPE
import com.senyor_o.firebasechat.utils.Constants.TEXT_TYPE

private val ForeignOwnMessage = RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
private val AuthorOwnMessage = RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)

@Composable
fun MessageItem(
    message: Message,
    user: User,
    isCurrentUser: Boolean,
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
                    imageUrl = user.ppf,
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
                            MaterialTheme.colors.primary
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
                if(!isCurrentUser && user.displayName.isNotBlank()) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                        ,
                        fontWeight = FontWeight.Bold,
                        text = user.displayName,
                        style = MaterialTheme.typography.subtitle2
                    )
                }
                ConstraintLayout {
                    val (contentRef, timeRef) = createRefs()
                    if (message.contentType == TEXT_TYPE && message.content.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .constrainAs(contentRef) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                            text = message.content,
                            style = MaterialTheme.typography.subtitle1
                        )
                    } else if(message.contentType == IMAGE_TYPE && message.content.isNotEmpty()) {
                        AsyncImage(
                            modifier = Modifier
                                .size(450.dp)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .constrainAs(contentRef) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                },
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(message.content)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                    Text(
                        modifier = Modifier
                            .constrainAs(timeRef) {
                                top.linkTo(contentRef.bottom)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                        text = message.sentDate.toString(),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun MessageItemPreview() {
//    MessageItem(
//        message = Message(
//            TEXT_TYPE,
//            "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!",
//            25L,
//            "Perry",
//        ),
//        user = User(
//
//        ),
//        isCurrentUser = false
//    )
//}