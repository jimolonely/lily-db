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

    /// https://en.wikipedia.org/wiki/Whitespace_character
    /// with some adjustments.
    /// Code points: 0085 00A0 180E 2000..200A 2028..2029 200B..200D 202F 205F 2060 3000 FEFF
    /// The corresponding UTF-8 is: C285 C2A0 E1A08E E28080..E2808A E280A8..E280A9 E2808B..E2808D E280AF E2819F E281A0 E38080 EFBBBF
    /// We check for these bytes directly in UTF8 for simplicity reasons.

    /**
     * C2
     * 85
     * A0
     * E1 A0 8E
     * E2
     * 80
     * 80..8A
     * A8..A9
     * 8B..8D
     * AF
     * 81
     * 9F
     * A0
     * E3 80 80
     * EF BB BF
     */
    public static int skipWhitespacesUTF8(String text, int pos, int end) {
        while (pos < end) {
            if (isWhitespaceASCII(text.charAt(pos))) {
                ++pos;
            } else {
                if (pos + 1 < end && text.charAt(pos) == 0xC2
                        && (text.charAt(pos + 1) == 0x85 || text.charAt(pos + 1) == 0xA0)) {
                    pos += 2;
                } else if (
                        pos + 2 < end
                                && (text.charAt(pos) == 0xE1 && text.charAt(pos + 1) == 0xA0 && text.charAt(pos + 2) == 0x8E)
                    // TODO 还没写完
                ) {
                    pos += 3;
                } else {
                    break;
                }
            }
        }
        return pos;
    }
}
