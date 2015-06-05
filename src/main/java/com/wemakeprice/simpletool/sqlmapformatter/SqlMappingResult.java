package com.wemakeprice.simpletool.sqlmapformatter;

import lombok.Data;

public @Data class SqlMappingResult {
    private String sql;
    private String parameters;

}
