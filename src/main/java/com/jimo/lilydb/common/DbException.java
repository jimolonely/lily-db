package com.jimo.lilydb.common;

import com.jimo.lilydb.parser.ParserConst;

/**
 * @author jimo
 * @date 2020/8/24 下午9:37
 */
public class DbException extends RuntimeException {

    private ParserConst.ErrorCodes code;

    public DbException(String message, ParserConst.ErrorCodes code) {
        super(message);
        this.code = code;
    }
}
