package com.jimo.lilydb.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author jimo
 * @date 2020/8/23 下午8:37
 */
class LexerTest {

    private Map<TokenType, String> highLight = getHighLight();

    private Map<TokenType, String> getHighLight() {
        return null;
    }

    @Test
    void test() {
        String query = "";
        Lexer lexer = new Lexer(query);

        while (true) {
            Token token = lexer.nextToken();

            if (token.isEnd()) {
                break;
            }

            writeChar(' ');

            String it = highLight.get(token.getType());
            if (it != null) {
                writeString(it);
            }

            writeString(token.toString());

            writeChar(' ');

            if (token.isError()) {
                return;
            }
        }

        writeChar('\n');
    }

    private void writeString(String token) {

    }

    private void writeChar(char c) {
        System.out.println(c);
    }
}