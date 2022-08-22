package com.senyor_o.firebasechat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    textLabel: String,
    maxChar: Int? = null,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType,
    keyboardActions: KeyboardActions,
    onValueChanged: (String) -> Unit,
    imeAction: ImeAction,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = textFieldValue.take(maxChar ?: 40),
        onValueChange = { onValueChanged(it) },
        label = {
            Text(
                text = textLabel,
                style = MaterialTheme.typography.subtitle1
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun TransparentTextFieldPreview() {
    TransparentTextField(
        textFieldValue = "passwordValue",
        textLabel = "Password",
        keyboardType = KeyboardType.Password,
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        imeAction = ImeAction.Done,
        trailingIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "toggle Password Icon")
            }
        },
        onValueChanged = {},
        visualTransformation = VisualTransformation.None
    )
}