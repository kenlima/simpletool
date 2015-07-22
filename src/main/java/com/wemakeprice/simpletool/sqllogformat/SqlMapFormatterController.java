package com.wemakeprice.simpletool.sqllogformat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class SqlMapFormatterController {

    @Autowired
    private SqlLogFormatService sqlMapFormatterService;

    @RequestMapping(value = "/formattedSql", method = RequestMethod.POST)
    public FormattedWithBindSql formattedSql(@RequestParam(value = "inputSql") String inputSql) throws Exception {
        FormattedWithBindSql result = sqlMapFormatterService.formatting(inputSql);
        return result;
    }
}
