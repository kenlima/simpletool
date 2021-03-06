package com.wemakeprice.simpletool.loganalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wemakeprice.simpletool.DateUtil;

@Service
public class ContentsLogAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(ContentsLogAnalysisService.class);

    @Autowired
    public UserService userService;;

    @Value("${log1.path}")
    private String logPath;

    private Comparator<Entry<String, List<Log>>> sortByCount = (entry1, entry2) -> new Integer(entry1.getValue().size())
            .compareTo(new Integer(entry2.getValue().size()));

    public static List<String> getLines(File file) {
        try {
            return Files.lines(file.toPath()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getPageRankGroupByJikmoo(String fromDate, String toDate) throws Exception {

        Map<String, List<Log>> groupByJikmoo = groupByJikmoo(fromDate, toDate);
        //byJikmoo.entrySet().stream().map(entry -> entry).collect(Collectors.toMap(keyMapper, valueMapper))

        List<Map<String, String>> result = sortAndCollect(groupByJikmoo, sortByCount);

        return result;
    }

    public List<Map<String, String>> groupByUrl(String fromDate, String toDate, String searchKeyword) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Log> allLines = getAllLogs(fromDate, toDate, searchKeyword);
        stopWatch.split();
        logger.info("File read time:" + stopWatch.toSplitString());

        Map<String, List<Log>> groupByUrl = allLines.stream().collect(Collectors.groupingBy(Log::getUrl));
        stopWatch.split();
        logger.info("Group by time:" + stopWatch.toSplitString());

        List<Map<String, String>> result = groupByUrl.entrySet().stream().sorted(sortByCount.reversed())
                .map(entry -> getMapForUrl(entry)).collect(Collectors.toList());
        stopWatch.stop();

        logger.info("Mapping time:" + stopWatch);
        return result;
    }

    private Map<String, List<Log>> groupByJikmoo(String fromDate, String toDate) {

        List<Log> allLines = getAllLogs(fromDate, toDate, null);

        Map<String, List<Log>> groupByJikmoo = allLines.stream().collect(Collectors.groupingBy(Log::getJikmooCd));
        return groupByJikmoo;
    }

    private Map<String, List<Log>> groupByUser(String fromDate, String toDate) {

        List<Log> allLines = getAllLogs(fromDate, toDate, null);

        Map<String, List<Log>> groupByUser = allLines.stream().collect(Collectors.groupingBy(Log::getUserCd));
        return groupByUser;
    }

    private List<Log> getAllLogs(String fromDate, String toDate, String searchKeyword) {

        List<File> files = getFiles(fromDate, toDate);

        Stream<List<String>> lineStream = files.stream().map(ContentsLogAnalysisService::getLines);
        List<Log> allLines = lineStream.flatMap(perLines -> perLines.stream()).filter(lineFilter(searchKeyword))
                .map(line -> parseLine(line)).collect(Collectors.toList());

        return allLines;
    }

    private static Predicate<? super String> lineFilter(String searchKeyword) {

        Predicate<String> keyword1 = line -> line.contains("RequestInfo:");
        //Predicate<String> keyword2 = line -> !line.contains(".json");
        Predicate<String> keyword3 = line -> !line.contains("/contents/getContentsListFirst.wmp");
        Predicate<String> keyword4 = line -> !line.contains("/contents/common/getProductCategoryList");
        Predicate<String> keyword5 = line -> !line.contains("/index.wmp");
        Predicate<String> keyword6 = line -> !line.contains("/contents/saveMailSender.wmp");
        //        Predicate<? super String> result = keyword1.and(keyword2).and(keyword3).and(keyword4).and(keyword5).and(keyword6); 
        Predicate<String> result = keyword1.and(keyword3).and(keyword4).and(keyword5);
        if (searchKeyword != null) {
            result = result.and(line -> line.contains(searchKeyword));
        }
        return result;
    }

    public Log parseLine(String line) {
        String date = line.substring(1, line.indexOf("]"));
        line = line.substring(line.indexOf("RequestInfo") + 12);
        String[] logArr = line.split(",");
        String[] userArr = logArr[3].split(":");
        String[] urlArr = logArr[0].split(":");

        Log log = new Log();
        log.setDate(date);
        log.setUserCd(userArr[1].trim());
        log.setJikmooCd(userService.getUser(log.getUserCd()).getJikmooCd());
        log.setJikchakCd(userService.getUser(log.getUserCd()).getJikchakCd());
        log.setUrlName(MyCommonData.getUrl(urlArr[0]));
        log.setUrl(urlArr[0]);

        return log;

    }

    private static Predicate<File> fileFilter() {
        Predicate<File> hidden = p -> !p.isHidden();
        Predicate<File> directory = p -> !p.isDirectory();

        //Predicate<File> ranged = p -> isRangedFile(p, fromDate, toDate);

        return hidden.and(directory);
    }

    private static Predicate<File> rangedFilter(String fromDate, String toDate) {
        return p -> isRangedFile(p, fromDate, toDate);
    }

    private static boolean isRangedFile(File p, String fromDate, String toDate) {

        String[] tmp = p.getName().split("[.]");
        String fileDay = null;
        if (tmp.length == 2) {
            fileDay = DateUtil.getCurrentDate();
        } else {
            fileDay = p.getName().split("[.]")[2];
        }

        LocalDate fileLocalDate = parseLocalDate(fileDay);
        LocalDate fromLocalDate = parseLocalDate(fromDate);
        LocalDate toLocalDate = parseLocalDate(toDate);

        if (fileLocalDate.isEqual(fromLocalDate) || fileLocalDate.isEqual(toLocalDate)) {
            return true;
        }
        if (fileLocalDate.isAfter(fromLocalDate) && fileLocalDate.isBefore(toLocalDate)) {
            return true;
        }

        return false;
    }

    private static LocalDate parseLocalDate(String fileDay) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate fld = LocalDate.parse(fileDay, dtf);
        return fld;
    }

    private static List<Map<String, String>> sortAndCollect(Map<String, List<Log>> groupByData,
            Comparator<Entry<String, List<Log>>> urlComparator) {
        List<Map<String, String>> result = groupByData.entrySet().stream().sorted(urlComparator.reversed())
                .map(entry -> getMapForJikmoo(entry)).collect(Collectors.toList());
        return result;
    }

    private List<UserCount> sortAndCollectUser(Map<String, List<Log>> groupByData,
            Comparator<Entry<String, List<Log>>> urlComparator) {
        List<UserCount> result = groupByData.entrySet().stream().sorted(urlComparator.reversed())
                .map(entry -> getUserCount(entry)).collect(Collectors.toList());
        return result;
    }

    public static Map<String, String> getMapForJikmoo(Entry<String, List<Log>> entry) {
        Map<String, String> map = new HashMap<>();
        map.put("jikmooCd", entry.getKey());
        map.put("jikmoo", MyCommonData.getJikmoo(entry.getKey()));
        map.put("cnt", String.valueOf(entry.getValue().size()));
        return map;
    }

    public UserCount getUserCount(Entry<String, List<Log>> entry) {
        UserCount groupByUser = new UserCount();
        groupByUser.setUser(userService.getUser(entry.getKey()));
        groupByUser.setCount(String.valueOf(entry.getValue().size()));
        return groupByUser;
    }

    public static Map<String, String> getMapForUrl(Entry<String, List<Log>> entry) {
        Map<String, String> map = new HashMap<>();
        map.put("rank", "1");
        map.put("urlName", MyCommonData.getUrl(entry.getKey()));
        map.put("url", entry.getKey());
        map.put("cnt", String.valueOf(entry.getValue().size()));
        return map;
    }

    public static Map<String, String> getMapForJikchak(Entry<String, List<Log>> entry) {
        Map<String, String> map = new HashMap<>();
        map.put("jikchakCd", entry.getKey());
        map.put("jikchak", MyCommonData.getJikchak(entry.getKey()));
        map.put("cnt", String.valueOf(entry.getValue().size()));
        return map;
    }

    public List<Map<String, String>> getPageRankGroupByUrlPerJikmoo(String jikmooCd, String fromDate, String toDate) {
        Map<String, List<Log>> groupByJikmoo = groupByJikmoo(fromDate, toDate);

        //        List<Log> aa = groupByJikmoo.entrySet().stream().filter(entry -> entry.getKey().equals(jikmooCd))
        //                .flatMap(e -> e.getValue().stream()).collect(Collectors.toList());

        Map<String, List<Log>> aa = groupByJikmoo.entrySet().stream().filter(entry -> entry.getKey().equals(jikmooCd))
                .flatMap(e -> e.getValue().stream()).collect(Collectors.groupingBy(Log::getUrl));

        List<Map<String, String>> result = aa.entrySet().stream().sorted(sortByCount.reversed())
                .map(entry -> getMapForUrl(entry)).collect(Collectors.toList());

        return result;
    }

    public List<UserCount> groupByUserPerUrl(String url, String fromDate, String toDate) {
        List<Log> allLines = getAllLogs(fromDate, toDate, null);
        Map<String, List<Log>> groupByUrl = allLines.stream().filter(log -> log.getUrl().equals(url))
                .collect(Collectors.groupingBy(Log::getUserCd));

        List<UserCount> result = groupByUrl.entrySet().stream().sorted(sortByCount.reversed())
                .map(entry -> getUserCount(entry)).collect(Collectors.toList());

        return result;
    }

    public List<Map<String, String>> getRequestCountPerJikchak(String jikmooCd, String fromDate, String toDate) {
        Map<String, List<Log>> groupByJikmoo = groupByJikmoo(fromDate, toDate);

        //        List<Log> aa = groupByJikmoo.entrySet().stream().filter(entry -> entry.getKey().equals(jikmooCd))
        //                .flatMap(e -> e.getValue().stream()).collect(Collectors.toList());

        Map<String, List<Log>> aa = groupByJikmoo.entrySet().stream().filter(entry -> entry.getKey().equals(jikmooCd))
                .flatMap(e -> e.getValue().stream()).collect(Collectors.groupingBy(Log::getJikchakCd));

        List<Map<String, String>> result = aa.entrySet().stream().sorted(sortByCount.reversed())
                .map(entry -> getMapForJikchak(entry)).collect(Collectors.toList());

        return result;
    }

    public List<UserCount> getPageRankGroupByUser(String fromDate, String toDate) {

        Map<String, List<Log>> groupByUser = groupByUser(fromDate, toDate);
        List<UserCount> result = sortAndCollectUser(groupByUser, sortByCount);
        return result;
    }

    public List<Log> getLogsOfUser(String userCd, String fromDate, String toDate) {
        Comparator<Log> comparator = (a, b) -> a.getDate().compareTo(b.getDate());

        List<File> files = getFiles(fromDate, toDate);

        Stream<List<String>> lineStream = files.stream().map(ContentsLogAnalysisService::getLines);
        List<Log> logs = lineStream.flatMap(perLines -> perLines.stream()).filter(lineFilter(null))
                .map(line -> parseLine(line)).filter(log -> log.getUserCd().equals(userCd)).sorted(comparator)
                .collect(Collectors.toList());
        return logs;
    }

    private List<File> getFiles(String fromDate, String toDate) {
        List<File> files = Stream.of(new File(this.logPath).listFiles())
                .flatMap(file -> file.listFiles() == null ? Stream.of(file) : Stream.of(file.listFiles()))
                .filter(fileFilter()).filter(rangedFilter(fromDate, toDate)).collect(Collectors.toList());
        return files;
    }
}
