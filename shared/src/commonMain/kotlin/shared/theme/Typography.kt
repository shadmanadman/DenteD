package shared.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class ApplicationTypography(
    val body12: TextStyle,
    val body13: TextStyle,
    val body14: TextStyle,
    val title15: TextStyle,
    val title16: TextStyle,
    val title17: TextStyle,
    val title18: TextStyle,
    val headline19: TextStyle,
    val headline20: TextStyle,
    val headline21: TextStyle,
    val headline22: TextStyle,
    val headline23: TextStyle,
    val headline26: TextStyle,
    val headline30: TextStyle

)

@Composable
fun appTypography(): ApplicationTypography {

    return ApplicationTypography(
        body12 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ),
        body13 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        body14 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        title15 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp
        ),
        title16 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        title17 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 17.sp
        ),
        title18 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        ),
        headline19 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 19.sp
        ),
        headline20 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        headline21 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 21.sp
        ),
        headline22 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        ),
        headline23 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 23.sp
        ),
        headline26 = TextStyle(

            fontWeight = FontWeight.Normal,
            fontSize = 26.sp
        ),
        headline30 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp
        )
    )
}