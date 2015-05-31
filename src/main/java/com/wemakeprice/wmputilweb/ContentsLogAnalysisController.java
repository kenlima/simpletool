package com.wemakeprice.wmputilweb;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class ContentsLogAnalysisController {
    private static final Logger logger = LoggerFactory.getLogger(ContentsLogAnalysisController.class);

    @Autowired
    private LogReportService logReportService;

    @RequestMapping(value = "/groupByJikmoo", method = RequestMethod.GET)
    public List<Map<String, String>> pageCountPerJikmoo(@RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate) throws Exception {

        List<Map<String, String>> result = logReportService.getPageRankGroupByJikmoo(fromDate, toDate);

        return result;
    }

    @RequestMapping("/pageRankGroupByUrlPerJikmoo")
    public List<Map<String, String>> pageRankPerJikmoo(@RequestParam String jikmooCd,
            @RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate)
            throws Exception {
        List<Map<String, String>> result = logReportService.getPageRankGroupByUrlPerJikmoo(jikmooCd, fromDate, toDate);
        return result;
    }

    @RequestMapping("/requestCountPerJikchak")
    public List<Map<String, String>> requestCountPerJikchak(@RequestParam String jikmooCd, @RequestParam(
            value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate) throws Exception {
        List<Map<String, String>> result = logReportService.getRequestCountPerJikchak(jikmooCd, fromDate, toDate);
        return result;
    }

    @RequestMapping("/groupByUser")
    public List<GroupByUser> pageRankGroupByUser(@RequestParam(value = "fromDate") String fromDate, @RequestParam(
            value = "toDate") String toDate) throws Exception {

        List<GroupByUser> result = logReportService.getPageRankGroupByUser(fromDate, toDate);
        return result;
    }

    @RequestMapping("/userLog")
    public List<Log> logsOfUser(@RequestParam String userCd, @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate) throws Exception {
        List<Log> result = logReportService.getLogsOfUser(userCd, fromDate, toDate);
        return result;
    }
}
