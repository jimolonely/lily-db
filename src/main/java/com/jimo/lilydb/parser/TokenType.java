package com.jimo.lilydb.parser;

/**
 * @author jimo
 * @date 2020/8/23 下午8:49
 */
public enum TokenType {

    Whitespace,
    Comment,

    BaseWord,

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
    Dot,

    Asterisk,

    Plus,
    Minus,
    Slash,
    Percent,
    Arrow,
    QuestionMark,
    Colon,
    Equals,
    NotEquals,
    Less,
    Greater,
    LessOrEqual,
    GreaterOrEquals,
    Concatenation,

    At,
    DoubleAt,

    EndOfStream,

    Error,
    ErrorMultilineCommentInfoNotClosed,
    ErrorSingleQuoteIsNotClosed,
    ErrorDoubleQuoteIsNotClosed,
    ErrorBackQuoteIsNotClosed,
    ErrorSingleExclamationMark,
    ErrorSinglePipeMark,
    ErrorWrongNumber,
    ErrorMaxQuerySizeExceeded,
}
