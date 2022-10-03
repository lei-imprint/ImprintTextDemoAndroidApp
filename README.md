All-in-one infra that can hold versatile texts in a single block, with custom styles at different
places; such as:
one block of text with title + n paragraphs of descriptions;
one snippet text with n clickable links;
or any of the above combined;

The goal is to make data totally controlled by server side, and client side can parse and render
the data without ZERO code change;

This is not exclusive set of configurations of text, instead, it is more of a working demo to
show the solution how to remotely define and control complex string and strings. Once added into
codebase, the code quality and Unit Test should be addressed; Also, DarkTheme need to be handled
properly;

For completed configuration of text level (not layout level), can refer to params in [SpanStyle]

Usage steps:
1. sever side define and pass down imprint_text_data;
2. client side parse imprint_text_data into [ImprintTextData];
3. client side bind [ImprintTextData] with [ImprintText];

![Screenshot_20221003_092659](https://user-images.githubusercontent.com/111802981/193630063-3a61308f-439a-40d8-bf6b-161d21564df4.png)
