package com.wemakeprice.wmputilweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SqlMappingController {

    @Autowired
    private SqlMappingService sqlMappingService;

    @RequestMapping(value = "/sqlmappingform", method = RequestMethod.GET)
    public String sqlMappingForm() {
        return "sqlmappingform";
    }

    @RequestMapping(value = "/sqlmappingresult", method = RequestMethod.POST)
    public String sqlMappingResult(final InputSqlForm inputSqlForm, Model model) {

        System.out.println("test23");
        SqlMappingResult result = sqlMappingService.mappingSql(inputSqlForm);
        model.addAttribute("result", result);
        return "sqlmappingresult";
    }
}
