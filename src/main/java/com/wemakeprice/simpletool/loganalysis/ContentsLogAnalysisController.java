package com.wemakeprice.simpletool.loganalysis;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wemakeprice.simpletool.sqlmapformatter.SqlMappingResult;

@RestController
@RequestMapping("/rest")
public class ContentsLogAnalysisController {
    private static final Logger logger = LoggerFactory.getLogger(ContentsLogAnalysisController.class);

    @Autowired
    private ContentsLogAnalysisService logReportService;

    /**
     * 직무별
     * 
     * @param fromDate
     * @param toDate
     * @return
     * @throws Exception
     */
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
    public List<UserCount> pageRankGroupByUser(@RequestParam(value = "fromDate") String fromDate, @RequestParam(
            value = "toDate") String toDate) throws Exception {

        List<UserCount> result = logReportService.getPageRankGroupByUser(fromDate, toDate);
        return result;
    }

    @RequestMapping("/groupByUrl")
    public List<Map<String, String>> groupByUrl(@RequestParam(value = "fromDate") String fromDate, @RequestParam(
            value = "toDate") String toDate,
            @RequestParam(value = "searchUrlKeyword", required = false) String searchUrlKeyword) throws Exception {

        List<Map<String, String>> result = logReportService.groupByUrl(fromDate, toDate, searchUrlKeyword);
        return result;
    }

    @RequestMapping("/groupByUserPerUrl")
    public List<UserCount> groupByUserPerUrl(@RequestParam String url,
            @RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate)
            throws Exception {

        List<UserCount> result = logReportService.groupByUserPerUrl(url, fromDate, toDate);
        return result;
    }

    @RequestMapping("/userLog")
    public List<Log> logsOfUser(@RequestParam String userCd, @RequestParam(value = "fromDate") String fromDate,
            @RequestParam(value = "toDate") String toDate) throws Exception {
        List<Log> result = logReportService.getLogsOfUser(userCd, fromDate, toDate);
        return result;
    }
}
