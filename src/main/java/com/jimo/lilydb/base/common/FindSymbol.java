package com.jimo.lilydb.base.common;

/**
 * @author jimo
 * @date 2020/8/23 下午9:47
 */
public class FindSymbol {

    public static int findFirstSymbols(String text, char symbol, int start, int end) {
        for (int i = start; i < end; i++) {
            if (text.charAt(i) == symbol) {
                return i;
            }
        }
        return -1;
    }
}
