package com.example.richtextapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.annotations.SerializedName


/**
 * All-in-one infra that can hold versatile texts in a single block, with custom styles at different
 * places; such as:
 * one block of text with title + n paragraphs of descriptions;
 * one snippet text with n clickable links;
 * or any of the above combined;
 *
 * The goal is to make data totally controlled by server side, and client side can parse and render
 * the data without ZERO code change;
 *
 * This is not exclusive set of configurations of text, instead, it is more of a working demo to
 * show the solution how to remotely define and control complex string and strings. Once added into
 * codebase, the code quality and Unit Test should be addressed; Also, DarkTheme need to be handled
 * properly;
 *
 * For completed configuration of text level (not layout level), can refer to params in [SpanStyle]
 * For some layout level configuration (text alignment), can refer to params in [TextStyle],
 * consumed by [ClickableText]
 *
 * Usage steps:
 * 1. sever side define and pass down imprint_text_data;
 * 2. client side parse imprint_text_data into [ImprintTextData];
 * 3. client side bind [ImprintTextData] with [ImprintText];
 * */

@Composable
fun ImprintText(data: ImprintTextData) {

  fun getMap() = data.args.filter { it.link != null }.associate { it.string to it.link }

  fun buildStyledSpan(data: ImprintTextData) =
    SpanStyle(
      fontSize = data.fontSize.sp,
      color = Color(data.color),
      fontWeight = if (data.bold) FontWeight.Bold else null,
      textDecoration = if (data.underline) TextDecoration.Underline else null,
    )

  fun buildText() = buildAnnotatedString {
    val str = data.string
    val countArgs = str.count { c -> c == '%' }
    if (countArgs != data.args.size) {
      print("Args Not match!")
      return AnnotatedString("")
    }
    var last = 0
    data.args.forEachIndexed { index, child ->
      val placeholder = "{%s$index}"
      val p1 = str.indexOf(placeholder)
      withStyle(buildStyledSpan(data)) { append(str.substring(last, p1)) }
      if (child.link != null) {
        pushStringAnnotation(
          tag = child.string,
          annotation = child.link,
        )
        withStyle(buildStyledSpan(child)) { append(child.string) }
        pop()
      } else {
        withStyle(buildStyledSpan(child)) { append(child.string) }
      }
      last = p1 + placeholder.length
    }
    withStyle(buildStyledSpan(data)) { append(str.substring(last)) }
  }

  val annotatedText = buildText()
  val uriHandler = LocalUriHandler.current
  var modifier = Modifier
    .padding(data.margin.dp)
    .background(color = Color(data.background))
    .padding(data.padding.dp)
  if (data.layoutWidth == -1) {
    modifier = modifier.fillMaxWidth()
  } else if (data.layoutWidth > 0) {
    modifier = modifier.width(data.layoutWidth.dp)
  }

  val textAlign = when (data.textAlign) {
    3 -> TextAlign.Center
    6 -> TextAlign.End
    else -> TextAlign.Start
  }

  ClickableText(
    text = annotatedText,
    modifier = modifier,
    style = TextStyle(textAlign = textAlign),
    onClick = { offset ->
      for (tag in getMap().keys) {
        annotatedText
          .getStringAnnotations(tag = tag, start = offset, end = offset)
          .firstOrNull()?.let { uriHandler.openUri(getMap()[tag]!!) }
      }
    }
  )
}

data class ImprintTextData(
  @SerializedName("string") val string: String = "",
  @SerializedName("font_size") val fontSize: Int = 14,
  @SerializedName("color") val color: Long = 0xFF000000,
  @SerializedName("bold") val bold: Boolean = false,
  @SerializedName("underline") val underline: Boolean = false,
  // 3 -> Center; 6 -> End; else -> Start, same as in [TextAlign]
  @SerializedName("text_align") val textAlign: Int = 1,
  @SerializedName("link") val link: String? = null,
  @SerializedName("background") val background: Long = 0x00000000, // only applied at top level
  // 0 = wrap content; -1L = match parent; > 0 means custom width; only applied at top level
  @SerializedName("layout_width") val layoutWidth: Int = 0,
  @SerializedName("margin") val margin: Int = 0, // only applied at top level
  @SerializedName("padding") val padding: Int = 0, // only applied at top level
  // can have 2nd level embedded texts with different styles, can be used such as: title, link
  @SerializedName("args") val args: List<ImprintTextData> = emptyList(),
)
