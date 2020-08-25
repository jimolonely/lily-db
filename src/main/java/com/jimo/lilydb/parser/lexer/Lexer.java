package com.jimo.lilydb.parser.lexer;

import static com.jimo.lilydb.base.common.FindSymbol.findFirstSymbols;
import static com.jimo.lilydb.common.StringUtils.*;

/**
 * 词法解析
 *
 * @author jimo
 * @date 2020/8/23 下午8:35
 */

public class Lexer {

    private int pos;
    private int begin;
    private int end;
    private int maxQuerySize;
    private String query;
    private TokenType prevSignificantTokenType = TokenType.Whitespace;

    public Lexer(String query) {
        this.query = query;
        this.begin = 0;
        this.end = query.length();
    }

    public Token nextToken() {

        Token res = nextTokenImpl();
        if (res.getType() != TokenType.EndOfStream && maxQuerySize > 0 && res.getEnd() > begin + maxQuerySize) {
            res.setType(TokenType.ErrorMaxQuerySizeExceeded);
        }
        if (res.isSignificant()) {
            prevSignificantTokenType = res.getType();
        }
        return res;
    }

    private Token nextTokenImpl() {
        if (pos >= end) {
            return new Token(TokenType.EndOfStream, end, end);
        }

        int tokenBegin = pos;

        switch (query.charAt(pos)) {
            case ' ':
            case '\t':
            case '\n':
            case '\r':
            case '\f':
//            case '\v':
            {
                ++pos;
                while (pos < end && isWhitespaceASCII(query.charAt(pos))) {
                    ++pos;
                }
                return new Token(TokenType.Whitespace, tokenBegin, pos);
            }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                if (prevSignificantTokenType == TokenType.Dot) {
                    ++pos;
                    while (pos < end && isNumericASCII(query.charAt(pos))) {
                        ++pos;
                    }
                } else {
                    /// 0x,0b
                    boolean hex = false;
                    if (pos + 2 < end && query.charAt(pos) == '0'
                            && (query.charAt(pos + 1) == 'x' || query.charAt(pos + 1) == 'b'
                            || query.charAt(pos + 1) == 'X' || query.charAt(pos + 1) == 'B')) {
                        if (query.charAt(pos + 1) == 'x' || query.charAt(pos + 1) == 'X') {
                            hex = true;
                        }
                        pos += 2;
                    } else {
                        ++pos;
                    }

                    while (pos < end && (hex ? isHexDigit(query.charAt(pos)) : isNumericASCII(query.charAt(pos)))) {
                        ++pos;
                    }

                    /// decimal point
                    if (pos < end && query.charAt(pos) == '.') {
                        ++pos;
                        while (pos < end && (hex ? isHexDigit(query.charAt(pos)) : isNumericASCII(query.charAt(pos)))) {
                            ++pos;
                        }
                    }

                    /// exponentiation (base 10 or base 2)
                    if (pos + 1 < end && (hex ? (query.charAt(pos) == 'p' || query.charAt(pos) == 'P') : (query.charAt(pos) == 'e' || query.charAt(pos) == 'E'))) {
                        ++pos;
                        /// sign of exponent, It is always decimal
                        if (pos + 1 < end && (query.charAt(pos) == '-' || query.charAt(pos) == '+')) {
                            ++pos;
                        }
                        while (pos < end && isNumericASCII(query.charAt(pos))) {
                            ++pos;
                        }
                    }
                }
                /// word character cannot go just after number (SELECT 123FROM)
                if (pos < end && isWordCharASCII(query.charAt(pos))) {
                    ++pos;
                    while (pos < end && isWordCharASCII(query.charAt(pos))) {
                        ++pos;
                    }
                    return new Token(TokenType.ErrorWrongNumber, tokenBegin, pos);
                }

                return new Token(TokenType.Number, tokenBegin, pos);
            }
            case '\'':
                return quotedString('\'', TokenType.StringLiteral, TokenType.ErrorSingleQuoteIsNotClosed, tokenBegin);
            case '"':
                return quotedString('"', TokenType.QuotedIdentifier, TokenType.ErrorDoubleQuoteIsNotClosed, tokenBegin);
            case '`':
                return quotedString('`', TokenType.QuotedIdentifier, TokenType.ErrorBackQuoteIsNotClosed, tokenBegin);
            case '(':
                return new Token(TokenType.OpeningRoundBracket, tokenBegin, ++pos);
            case ')':
                return new Token(TokenType.ClosingRoundBracket, tokenBegin, ++pos);
            case '[':
                return new Token(TokenType.OpeningSquareBracket, tokenBegin, ++pos);
            case ']':
                return new Token(TokenType.ClosingSquareBracket, tokenBegin, ++pos);
            case '{':
                return new Token(TokenType.OpeningCurlyBrace, tokenBegin, ++pos);
            case '}':
                return new Token(TokenType.ClosingCurlyBrace, tokenBegin, ++pos);
            case ',':
                return new Token(TokenType.Comma, tokenBegin, ++pos);
            case ';':
                return new Token(TokenType.Semicolon, tokenBegin, ++pos);
            case '.':
                /// qualifier, tuple access operator or start of floating point number
            {
                /// just after identifier or complex expression or number(for chained tuple access like x.1.1 to work properly)
                if (pos > begin
                        && (!(pos + 1 < end && isNumericASCII(query.charAt(pos)))
                        || prevSignificantTokenType == TokenType.ClosingRoundBracket
                        || prevSignificantTokenType == TokenType.ClosingSquareBracket
                        || prevSignificantTokenType == TokenType.BaseWord
                        || prevSignificantTokenType == TokenType.QuotedIdentifier
                        || prevSignificantTokenType == TokenType.Number)) {
                    return new Token(TokenType.Dot, tokenBegin, ++pos);
                }
                ++pos;
                while (pos < end && isNumericASCII(query.charAt(pos))) {
                    ++pos;
                }

                /// exponentiation
                if (pos + 1 < end && (query.charAt(pos) == 'e' || query.charAt(pos) == 'E')) {
                    ++pos;
                    /// sign of exponent
                    if (pos + 1 < end && (query.charAt(pos) == '-' || query.charAt(pos) == '+')) {
                        ++pos;
                    }
                    while (pos < end && isNumericASCII(query.charAt(pos))) {
                        ++pos;
                    }
                }
                return new Token(TokenType.Number, tokenBegin, pos);
            }
            case '+':
                return new Token(TokenType.Plus, tokenBegin, ++pos);
            case '-':
                /// minus(-), arrow(->) or start of comment(--)
            {
                ++pos;
                if (pos < end && query.charAt(pos) == '>') {
                    return new Token(TokenType.Arrow, tokenBegin, ++pos);
                }
                if (pos < end && query.charAt(pos) == '-') {
                    ++pos;
                    return commentUntilEndOfLine(tokenBegin);
                }
                return new Token(TokenType.Minus, tokenBegin, pos);
            }
            case '*':
                return new Token(TokenType.Asterisk, tokenBegin, ++pos);
            case '/':
                /// division(/) or start of comment (//, /*)
            {
                ++pos;
                if (pos < end && (query.charAt(pos) == '/' || query.charAt(pos) == '*')) {
                    if (query.charAt(pos) == '/') {
                        ++pos;
                        return commentUntilEndOfLine(tokenBegin);
                    } else {
                        ++pos;
                        while (pos + 2 <= end) {
                            if (query.charAt(pos) == '*' && query.charAt(pos + 1) == '/') {
                                pos += 2;
                                return new Token(TokenType.Comment, tokenBegin, pos);
                            }
                            ++pos;
                        }
                        return new Token(TokenType.ErrorMultilineCommentInfoNotClosed, tokenBegin, pos);
                    }
                }
                return new Token(TokenType.Slash, tokenBegin, pos);
            }
            case '%':
                return new Token(TokenType.Percent, tokenBegin, ++pos);
            case '=':
                /// =,==
            {
                ++pos;
                if (pos < end && query.charAt(pos) == '=') {
                    ++pos;
                }
                return new Token(TokenType.Equals, tokenBegin, pos);
            }
            case '!':
                /// !=
            {
                ++pos;
                if (pos < end && query.charAt(pos) == '=') {
                    return new Token(TokenType.NotEquals, tokenBegin, ++pos);
                }
                return new Token(TokenType.ErrorSingleExclamationMark, tokenBegin, pos);
            }
            case '<':
                /// <,<=,<>
            {
                ++pos;
                if (pos < end && query.charAt(pos) == '=') {
                    return new Token(TokenType.LessOrEqual, tokenBegin, ++pos);
                }
                if (pos < end && query.charAt(pos) == '>') {
                    return new Token(TokenType.NotEquals, tokenBegin, ++pos);
                }
                return new Token(TokenType.Less, tokenBegin, pos);
            }
            case '>':
                /// >,>=
            {
                ++pos;
                if (pos < end && query.charAt(pos) == '=') {
                    return new Token(TokenType.GreaterOrEquals, tokenBegin, ++pos);
                }
                return new Token(TokenType.Greater, tokenBegin, pos);
            }
            case '?':
                return new Token(TokenType.QuestionMark, tokenBegin, ++pos);
            case ':':
                return new Token(TokenType.Colon, tokenBegin, ++pos);
            case '|': {
                ++pos;
                if (pos < end && query.charAt(pos) == '|') {
                    return new Token(TokenType.Concatenation, tokenBegin, ++pos);
                }
                return new Token(TokenType.ErrorSinglePipeMark, tokenBegin, pos);
            }
            case '@': {
                ++pos;
                if (pos < end && query.charAt(pos) == '@') {
                    return new Token(TokenType.DoubleAt, tokenBegin, ++pos);
                }
                return new Token(TokenType.At, tokenBegin, pos);
            }
            default:
                if (isWordCharASCII(query.charAt(pos))) {
                    ++pos;
                    while (pos < end && isWordCharASCII(query.charAt(pos))) {
                        ++pos;
                    }
                    return new Token(TokenType.BaseWord, tokenBegin, pos);
                } else {
                    /// We will also skip unicode whitespaces in UTF-8 to support for queries copy-pasted from MS Word and similar.
                    pos = skipWhitespacesUTF8(query, pos, end);
                    if (pos > tokenBegin) {
                        return new Token(TokenType.Whitespace, tokenBegin, pos);
                    }
                    return new Token(TokenType.Error, tokenBegin, ++pos);
                }
        }
    }

    private Token quotedString(char quote, TokenType successToken, TokenType errorToken, int tokenBegin) {
        ++pos;
        while (true) {
            pos = findFirstSymbols(query, pos, end, quote, '\\');
            if (pos >= end) {
                return new Token(errorToken, tokenBegin, end);
            }
            if (query.charAt(pos) == quote) {
                ++pos;
                if (pos < end && query.charAt(pos) == quote) {
                    ++pos;
                    continue;
                }
                return new Token(successToken, tokenBegin, pos);
            }

            if (query.charAt(pos) == '\\') {
                ++pos;
                if (pos >= end) {
                    return new Token(errorToken, tokenBegin, end);
                }
                ++pos;
                continue;
            }
            // 这里应该永远到不了
            return null;
        }
    }

    private Token commentUntilEndOfLine(int tokenBegin) {
        pos = findFirstSymbols(query, pos, end, '\n');
        return new Token(TokenType.Comment, tokenBegin, pos);
    }
}
