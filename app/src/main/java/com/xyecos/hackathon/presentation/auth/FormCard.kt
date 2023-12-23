package com.xyecos.hackathon.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.ui.theme.bold
import com.xyecos.hackathon.ui.theme.medium

@SuppressLint("UnrememberedMutableState")
@Composable
fun FormCard(
    modifier: Modifier = Modifier,
    emailError: String? = null,
    passwordError: String? = null,
) {
    val emailValue = mutableStateOf("")
    val password = mutableStateOf("")

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                top = 40.dp,
                start = 30.dp,
                end = 30.dp,
                bottom = 30.dp
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Авторизация",
                style = TextStyle(
                    fontFamily = bold,
                    fontSize = 24.sp
                ),
            )

            LoginFormInputField(
                onTextChanged = {it -> emailValue.value = it},
                modifier = Modifier.padding(top = 16.dp),
                text = emailValue.value,
                inputType = InputType.EMAIL,
                hint = "E-mail"
            )
            Text(
                modifier = Modifier.alpha(if (emailError.isNullOrBlank()) 0f else 1f),
                text = emailError.orEmpty(),
                style = TextStyle(
                        fontFamily = medium,
                        fontSize = 14.sp)
            )

            LoginFormInputField(
                text = password.value,
                onTextChanged = {password.value = it},
                modifier = Modifier.padding(top = 10.dp),
                inputType = InputType.PASSWORD,
                hint = "Пароль"
            )

            Text(
                modifier = Modifier.alpha(if (passwordError.isNullOrBlank()) 0f else 1f),
                text = passwordError.orEmpty(),
                style = TextStyle(
                    fontFamily = medium,
                    fontSize = 14.sp
                )
            )
            LoginFormFilledButton(
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    val emailsList = listOf("")
                }
            )
        }
    }
}