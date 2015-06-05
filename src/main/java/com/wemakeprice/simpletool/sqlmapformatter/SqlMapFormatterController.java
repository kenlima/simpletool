package com.wemakeprice.simpletool.sqlmapformatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class SqlMapFormatterController {

    @Autowired
    private SqlMapFormatterService sqlMapFormatterService;

    @RequestMapping(value = "/formattedSql", method = RequestMethod.POST)
    public SqlMappingResult formattedSql(@RequestParam(value = "inputSql") String inputSql) throws Exception {
        SqlMappingResult result = sqlMapFormatterService.mappingSql(inputSql);
        return result;
    }
}
