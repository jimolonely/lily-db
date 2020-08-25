package com.jimo.lilydb.parser;

import com.jimo.lilydb.parser.lexer.Token;
import com.jimo.lilydb.parser.lexer.TokenType;

/**
 * @author jimo
 * @date 2020/8/24 下午9:14
 */
public class TokenIterator {

    private Tokens tokens;
    private int index = 0;

    public TokenIterator(Tokens tokens) {
        this.tokens = tokens;
    }

    public Token get() {
        return tokens.get(index);
    }

    public TokenIterator add() {
        ++index;
        return this;
    }

    public TokenIterator minus() {
        --index;
        return this;
    }

    public boolean lessThan(TokenIterator rhs) {
        return index < rhs.index;
    }

    public boolean lessEqualThan(TokenIterator rhs) {
        return index <= rhs.index;
    }

    public boolean equal(TokenIterator rhs) {
        return index == rhs.index;
    }

    public boolean notEqual(TokenIterator rhs) {
        return index != rhs.index;
    }

    public boolean isValid() {
        // <
        return get().getType().compareTo(TokenType.EndOfStream) < 0;
    }

    public Token max() {
        return tokens.max();
    }
}
