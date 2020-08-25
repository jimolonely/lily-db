package com.jimo.lilydb.parser.lexer;

import lombok.*;

/**
 * Token
 *
 * @author jimo
 * @date 2020/8/23 下午8:49
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private TokenType type;
    private int begin;
    private int end;

    public int size() {
        return end - begin;
    }

    public boolean isSignificant() {
        return type != TokenType.Whitespace && type != TokenType.Comment;
    }

    public boolean isError() {
        return type.compareTo(TokenType.EndOfStream) > 0;
    }

    public boolean isEnd() {
        return type == TokenType.EndOfStream;
    }
}
