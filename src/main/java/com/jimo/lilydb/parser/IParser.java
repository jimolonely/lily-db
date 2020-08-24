package com.jimo.lilydb.parser;

import com.jimo.lilydb.common.DbException;

import static com.jimo.lilydb.parser.ParserConst.ErrorCodes.LOGICAL_ERROR;
import static com.jimo.lilydb.parser.ParserConst.ErrorCodes.TOO_DEEP_RECURSIVE;

/**
 * @author jimo
 * @date 2020/8/24 下午9:10
 */
public abstract class IParser {

    class Pos extends TokenIterator {

        int depth = 0;
        int maxDepth = 0;

        public Pos(Tokens tokens, int maxDepth) {
            super(tokens);
            this.maxDepth = maxDepth;
        }

        void increaseDepth() {
            ++depth;
            if (maxDepth > 0 && depth > maxDepth) {
                throw new DbException("Maximum parse depth (" + maxDepth + ") exceeded. " +
                        " Consider rising max_parser_depth parameter.", TOO_DEEP_RECURSIVE);
            }
        }

        void decreaseDepth() {
            if (depth == 0) {
                throw new DbException("Logical error in parser: incorrect calculation of parse depth", LOGICAL_ERROR);
            }
            --depth;
        }
    }

    /**
     * 获取解析器解析的文本
     */
    abstract String getName();

    abstract boolean parse(Pos pos, IAST node, Expected expected);

    public boolean ignore(Pos pos, Expected expected) {
        // FIXME
        IAST ignoreNode = null;
        return parse(pos, ignoreNode, expected);
    }

    boolean check(Pos pos, Expected expected) {
        Pos begin = pos;
        IAST node = null;
        if (!parse(pos, node, expected)) {
            pos = begin;
            return false;
        }
        return true;
    }

    // TODO
}
