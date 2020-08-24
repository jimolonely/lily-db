package com.jimo.lilydb.base.common;

/**
 * @author jimo
 * @date 2020/8/23 下午9:47
 */
public class FindSymbol {

    public static int findFirstSymbols(String text, int start, int end, char... symbols) {
        String search = text.substring(start, end);
        for (char symbol : symbols) {
            int i = search.indexOf(symbol);
            if (i >= 0) {
                return i + start;
            }
        }
        return end;
    }
}
