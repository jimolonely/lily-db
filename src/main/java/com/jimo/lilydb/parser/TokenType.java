package com.jimo.lilydb.parser;

/**
 * @author jimo
 * @date 2020/8/23 下午8:49
 */
public enum TokenType {

    Whitespace,
    Comment,

    ErrorMaxQuerySizeExceeded,
    ErrorSingleQuoteIsNotClosed,
    ErrorDoubleQuoteIsNotClosed,
    ErrorBackQuoteIsNotClosed,

    Dot,

    ErrorWrongNumber,
    Number,
    StringLiteral,
    QuotedIdentifier,

    OpeningRoundBracket,
    ClosingRoundBracket,
    OpeningSquareBracket,
    ClosingSquareBracket,
    OpeningCurlyBrace,
    ClosingCurlyBrace,
    Comma,
    Semicolon,

    BaseWord,
    Plus,
    Arrow,
    Minus,
    Asterisk,

    EndOfStream
}
