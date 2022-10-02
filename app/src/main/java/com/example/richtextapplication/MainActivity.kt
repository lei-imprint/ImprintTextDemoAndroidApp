package com.example.richtextapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.richtextapplication.ui.theme.RichTextApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      RichTextApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          RichText(
            data = RichTextData(
              string = "hello {%s0}, this is a {%s1}",
              color = 0xFFFFFF00,
              args = listOf(
                RichTextData("world", 0xFFCCCCCC),
                RichTextData(
                  "link",
                  fontSize = 26,
                  bold = true,
                  color = 0xFFFF0000,
                  link = "https://www.google.com/"
                )
              )
            )
          )
        }
      }
    }
  }
}

data class RichTextData(
  val string: String,
  val color: Long = 0xFF000000,
  val bold: Boolean = false,
  val fontSize: Int = 14,
  val link: String? = null,
  val args: List<RichTextData> = emptyList(),
)

@Composable
fun RichText(data: RichTextData) {

  fun buildStyledSpan(data: RichTextData) =
    SpanStyle(
      fontSize = data.fontSize.sp,
      color = Color(data.color),
      fontWeight = if (data.bold) FontWeight.Bold else FontWeight.Normal
    )

  fun buildString() = buildAnnotatedString {
    val str = data.string
    val countArgs = str.count { c -> c == '%' }
    if (countArgs != data.args.size) {
      print("Args Not match!")
      return AnnotatedString("")
    }
    var last = 0
    data.args.forEachIndexed { index, child ->
      val placeholder = "{%s$index}"
      var p1 = str.indexOf(placeholder)
      withStyle(buildStyledSpan(data)) { append(str.substring(last, p1)) }
      withStyle(buildStyledSpan(child)) { append(child.string) }
      last = p1 + placeholder.length
    }
    withStyle(buildStyledSpan(data)) { append(str.substring(last)) }
  }

  Text(buildString())
}
