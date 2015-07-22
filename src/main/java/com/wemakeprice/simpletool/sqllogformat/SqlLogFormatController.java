package com.wemakeprice.simpletool.sqllogformat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SqlLogFormatController {

    @Autowired
    private SqlLogFormatService sqlLogFormatService;

    @RequestMapping("/sqllogformat/input")
    public String input(ModelAndView modelAndView) {
        return "sqllogformat/input";
    }

    @RequestMapping("/sqllogformat/result")
    public String result(@RequestParam(value = "sqlLog") String sqlLog, Model model) {
        FormattedWithBindSql result = sqlLogFormatService.formatting(sqlLog);
        model.addAttribute("formattedSql", result);
        return "sqllogformat/result";
    }
}
