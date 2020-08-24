package com.jimo.lilydb.parser;

import java.util.List;

/**
 * @author jimo
 * @date 2020/8/24 下午9:14
 */
public class Tokens {

    private List<Token> data;
    private Lexer lexer;

    public Tokens(int begin, int end, int maxQuerySize) {
//        lexer = new Lexer()
        // FIXME
    }

    public Token get(int index) {
        while (true) {
            if (index < data.size()) {
                return data.get(index);
            }
            if (!data.isEmpty() && data.get(data.size() - 1).isEnd()) {
                return data.get(data.size() - 1);
            }
            Token token = lexer.nextToken();
            if (token.isSignificant()) {
                data.add(token);
            }
        }
    }

    public Token max() {
        if (data.isEmpty()) {
            // FIXME
            return new Token();
        }
        return data.get(data.size() - 1);
    }
}
