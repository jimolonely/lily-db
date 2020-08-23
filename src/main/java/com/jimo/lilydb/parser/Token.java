package com.jimo.lilydb.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Token
 *
 * @author jimo
 * @date 2020/8/23 下午8:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private TokenType type;
    private int begin;
    private int end;

    public int size() {
        return end - begin;
    }

    boolean isSignificant() {
        return type != TokenType.Whitespace && type != TokenType.Comment;
    }

    boolean isError() {
        return type.compareTo(TokenType.EndOfStream) > 0;
    }

    boolean isEnd() {
        return type == TokenType.EndOfStream;
    }
}
