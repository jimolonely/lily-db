package com.jimo.lilydb.parser;

import com.jimo.lilydb.common.DbException;
import com.jimo.lilydb.parser.lexer.TokenType;

/**
 * Parse specified keyword such as SELECT or compound keyword such as ORDER BY.
 * All case insensitive. Requires word boundary.
 * For compound keywords, any whitespace characters and comments could be in the middle.
 *
 * @author jimo
 * @date 2020/8/25 上午6:57
 */
/// Example: ORDER/* Hello */ BY
public class ParserKeyword extends IParserBase {

    private char[] s;

    public ParserKeyword(char[] s) {
        this.s = s;
    }

    @Override
    protected boolean parseImpl(Pos pos, IAST node, Expected expected) {
        if (pos.get().getType() != TokenType.BaseWord) {
            return false;
        }
        int current = 0;
        int sLen = s.length;
        if (sLen <= 0) {
            throw new DbException("Logical error: keyword cannot be empty string", ParserConst.ErrorCodes.LOGICAL_ERROR);
        }
        int end = current + sLen;

        while (true) {
            expected.add(pos, s[current] + "");

            if (pos.get().getType() != TokenType.BaseWord) {
                return false;
            }
        }
    }

    @Override
    String getName() {
        return new String(s);
    }
}
