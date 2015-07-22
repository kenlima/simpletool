package com.wemakeprice.simpletool.loganalysis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogAnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(LogAnalysisController.class);

    @Autowired
    private LogAnalysisService logAnalysisService;

    @RequestMapping(value = "/groupByUser", method = RequestMethod.GET)
    public List<GroupByUserDto> pageCountPerJikmoo() throws Exception {

        List<GroupByUserDto> result = logAnalysisService.groupByUser();

        return result;
    }

}
