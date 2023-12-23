package com.xyecos.hackathon.presentation.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.R
import com.xyecos.hackathon.ui.theme.mainBlue
import com.xyecos.hackathon.ui.theme.medium
import com.xyecos.hackathon.ui.theme.separator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormInputField(
    modifier: Modifier = Modifier,
    inputType: InputType,
    text: String = "",
    onTextChanged: (String) -> Unit = {},
    hint: String = "",
    isError: Boolean = false
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        singleLine = true,
        value = text,
        textStyle = TextStyle(
            fontFamily = medium,
            fontSize = 14.sp
        ),
        onValueChange = { newText ->
            onTextChanged(newText)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        label = {
            Text(
                text = hint,
                style = TextStyle(
                    fontFamily = medium,
                    fontSize = 14.sp
                )
            )
        },
        visualTransformation = if (passwordVisible || inputType != InputType.PASSWORD) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = when (inputType) {
            InputType.PASSWORD -> KeyboardOptions(keyboardType = KeyboardType.Password)
            InputType.EMAIL -> KeyboardOptions(keyboardType = KeyboardType.Email)
            else -> KeyboardOptions(keyboardType = KeyboardType.Text)
        },
        trailingIcon = {
            if (inputType == InputType.PASSWORD) {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.eye)
                } else {
                    painterResource(id = R.drawable.eye_slash)
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null, tint = Color.Black)
                }
            }
        },
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(

            focusedBorderColor = mainBlue,
            unfocusedBorderColor = separator,
            cursorColor = mainBlue,
            focusedLabelColor = mainBlue,
            unfocusedLabelColor = separator,
            containerColor = White
        ),
        isError = isError
    )

}

enum class InputType {
    PASSWORD, EMAIL, OTHER
}

@Preview
@Composable
fun LoginFormInputFieldPreview() {
    LoginFormInputField(
        inputType = InputType.PASSWORD,
        hint = "E-mail",
    )
}