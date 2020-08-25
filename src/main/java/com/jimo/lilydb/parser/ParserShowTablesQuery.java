package com.jimo.lilydb.parser;

/**
 * show tables
 *
 * @author jimo
 * @date 2020/8/25 上午6:55
 */
public class ParserShowTablesQuery extends IParserBase {
    @Override
    String getName() {
        return "SHOW [TEMPORARY] TABLES|DATABASES|CLUSTERS|CLUSTER 'name' [[NOT] [I]LIKE 'str'] [LIMIT expr]";
    }

    @Override
    protected boolean parseImpl(Pos pos, IAST node, Expected expected) {

        return false;
    }

}
