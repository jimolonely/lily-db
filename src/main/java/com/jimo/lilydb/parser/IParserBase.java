package com.jimo.lilydb.parser;

import java.util.function.Function;

/**
 * @author jimo
 * @date 2020/8/24 下午9:09
 */
public abstract class IParserBase extends IParser {

    class IncreaseDepthTag {
    }

    public static boolean wrapParseImpl(Pos pos, Function<Void, Boolean> f) {
        Pos begin = pos;
        Boolean res = f.apply(null);
        if (!res) {
            pos = begin;
        }
        return res;
    }

    public static boolean wrapParseImpl(Pos pos, IncreaseDepthTag tag, Function<Void, Boolean> f) {
        Pos begin = pos;
        pos.increaseDepth();
        Boolean res = f.apply(null);
        pos.decreaseDepth();
        if (!res) {
            pos = begin;
        }
        return res;
    }

    @Override
    boolean parse(Pos pos, IAST node, Expected expected) {
        expected.add(pos, getName());

        return wrapParseImpl(pos, new IncreaseDepthTag(), (v) -> {
            boolean res = parseImpl(pos, node, expected);
            if (!res) {
                // FIXME
            }
            return res;
        });
    }

    protected abstract boolean parseImpl(Pos pos, IAST node, Expected expected);
}
