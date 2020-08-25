package com.jimo.lilydb.parser;


import com.jimo.lilydb.parser.lexer.Lexer;
import com.jimo.lilydb.parser.lexer.Token;
import com.jimo.lilydb.parser.lexer.TokenType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jimo
 * @date 2020/8/23 下午8:37
 */
public class LexerTest {

    private Map<TokenType, String> highLight = getHighLight();

    private Map<TokenType, String> getHighLight() {
        return new HashMap<>(8);
    }

    @Test
    public void test() {
        String query = " SELECT 18446744073709551615, f(1), '\\\\', [a, b, c], (a, b, c), 1 + 2 * -3, a = b OR c > d.1 + 2 * -g[0] AND NOT e < f * (x + y)" +
                " FROM default.hits" +
                " WHERE CounterID = 101500 AND UniqID % 3 = 0" +
                " GROUP BY UniqID" +
                " HAVING SUM(Refresh) > 100" +
                " ORDER BY Visits, PageViews" +
                " LIMIT LENGTH('STRING OF 20 SYMBOLS') - 20 + 1000, 10.05 / 5.025 * 5" +
                " INTO OUTFILE 'test.out'" +
                " FORMAT TabSeparated";
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
        System.out.println(token);
    }

    private void writeChar(char c) {
        System.out.print(c);
    }
}