package com.example.richtextapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.richtextapplication.ui.theme.RichTextApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      RichTextApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(12.dp),
          color = MaterialTheme.colors.background
        ) {
          Column {
            ImprintText(data = test1)
            Divider(Modifier.padding(0.dp, 32.dp))
            ImprintText(data = test2)
            Divider(Modifier.padding(0.dp, 32.dp))
            ImprintText(data = test3)
          }
        }
      }
    }
  }
}

val test1 = ImprintTextData(
  string = "Hello {%0}\nThis is a {%1}",
  background = 0x220000FF,
  layoutWidth = 200,
  margin = 16,
  padding = 8,
  args = listOf(
    ImprintTextData(
      string = "Imprint",
      color = 0xFF0000FF,
      fontSize = 20,
      link = "https://www.imprint.co/"
    ),
    ImprintTextData(
      string = "Apple",
      fontSize = 26,
      bold = true,
      color = 0xFFFF0000,
      link = "https://www.apple.com/"
    )
  )
)

val test2 = ImprintTextData(
  string = "Hello, this is a plain text, with only string, and there is no any other configuration",
)

val test3 = ImprintTextData(
  string = "{%0}\n\n{%1}{%2}.",
  textAlign = 3,
  args = listOf(
    ImprintTextData("Title", fontSize = 20, bold = true),
    ImprintTextData(
      string = "This is description, This is description, This is description, ",
      fontSize = 16,
      color = 0xFF888888,
    ),
    ImprintTextData(
      "more info",
      fontSize = 16,
      color = 0xFF888888,
      underline = true,
      link = "https://www.imprint.co/",
    )
  )
)
