package com.wemakeprice.simpletool.sqllogformat;

import lombok.Data;

public @Data class FormattedWithBindSql {
    private String sql;
    private String parameters;

}
