package com.jimo.lilydb.common;

/**
 * string utils
 *
 * @author jimo
 * @date 2020/8/23 下午9:54
 */
public class StringUtils {

    /**
     * 空白字符
     */
    public static boolean isWhitespaceASCII(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }

    /**
     * number
     */
    public static boolean isNumericASCII(char c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * 十六进制字符
     */
    public static boolean isHexDigit(char c) {
        return isNumericASCII(c)
                || (c >= 'a' && c <= 'f')
                || (c >= 'A' && c <= 'F');
    }

    public static boolean isWordCharASCII(char c) {
        return isAlphaNumericASCII(c)
                || c == '_';
    }

    private static boolean isAlphaNumericASCII(char c) {
        return isAlphaASCII(c)
                || isNumericASCII(c);
    }

    private static boolean isAlphaASCII(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z');
    }
}
