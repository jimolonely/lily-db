package com.jimo.lilydb.parser;

/**
 * 常量
 *
 * @author jimo
 * @date 2020/8/24 下午9:34
 */
public class ParserConst {

    public enum ErrorCodes {
        TOO_DEEP_RECURSIVE,
        LOCAL_ERROR,
        CANNOT_PTHREAP_ATTR,

        BAD_TYPE_OF_FIELD,
        BAD_GET,
        NOT_IMPLEMENTED,
        LOGICAL_ERROR,
        ILLEGAL_TYPE_OF_ARGUMENT
    }
}
