package com.wemakeprice.simpletool.sqllogformat;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.para.GOutputFmt;
import gudusoft.gsqlparser.pp.stmtformatter.FormatterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
public class SqlLogFormatService {
    public FormattedWithBindSql formatting(String sqlLog) {
        // 15:58:46,515 INFO  [stdout] (default task-11) 추출
        String tmpSqlLog = sqlLog.replaceAll(
                "[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} [A-Z]{4,5}  \\[stdout\\] \\(default task-[0-9]+\\)", "");

        int readIdx = 0;
        String extractedSql = "";
        if ((readIdx = tmpSqlLog.indexOf("Preparing:")) > -1) {
            extractedSql = extractSql(tmpSqlLog, readIdx);

        }
        String formattedSql = formattingSql(extractedSql);

        List<String> parameters = new ArrayList<>();
        if ((readIdx = tmpSqlLog.indexOf("Parameters:")) > -1) {
            parameters = extractParameter(tmpSqlLog, readIdx);

        }
        String bindSql = bindingParameter(parameters, formattedSql);

        // set result data.
        FormattedWithBindSql result = new FormattedWithBindSql();
        result.setParameters(String.join(", ", parameters));
        result.setSql(bindSql);

        return result;
    }

    private List<String> extractParameter(String tmpSqlLog, int idx) {
        List<String> parameters;
        int begin = idx + 11;
        String param = tmpSqlLog.substring(begin);

        parameters = Arrays.asList(param.split("[,]"));
        //parameters.replaceAll(String::trim);

        //parameters.stream().map(s -> replaceTypeValue(s));
        parameters.replaceAll(SqlLogFormatService::replaceTypeValue);
        return parameters;
    }

    private String extractSql(String tmpSqlLog, int idx) {
        String extractedSql;
        extractedSql = tmpSqlLog.substring(idx + 10);
        idx = extractedSql.indexOf("\n");
        if (idx > -1) {
            extractedSql = extractedSql.substring(0, idx);
        } else {
            idx = extractedSql.indexOf("\r\n");
            if (idx > -1) {
                extractedSql = extractedSql.substring(0, idx);
            }
        }
        return extractedSql;
    }

    private String bindingParameter(List<String> parameters, String inputQuery) {
        StringBuffer mappedQuerySb = new StringBuffer();
        int q1 = 0;
        int q2 = 0;
        int parameterIdx = 0;
        while (true) {
            q2 = inputQuery.indexOf("?", q1);
            if (q2 < 0) {
                break;
            }
            mappedQuerySb.append(inputQuery.substring(q1, q2));

            mappedQuerySb.append("<span style='font-weight:bold;color:black'>" + parameters.get(parameterIdx).trim()
                    + "</span>");
            q1 = q2 + 1;
            parameterIdx++;
        }
        mappedQuerySb.append(inputQuery.substring(q1));

        return mappedQuerySb.toString();
    }

    private String formattingSql(String result) {
        System.out.println("extracted sql : " + result);
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
        sqlparser.setSqltext(result);
        int ret = sqlparser.parse();
        String result2 = null;
        if (ret == 0) {
            GFmtOpt option = GFmtOptFactory.newInstance();
            option.outputFmt = GOutputFmt.ofhtml;
            result2 = FormatterFactory.pp(sqlparser, option);
            System.out.println(result2);
        }
        System.out.println("parsed sql : " + result2);

        return result2;
    }

    private static String replaceTypeValue(String name) {
        name = name.trim();
        int idx = 0;
        if ((idx = name.indexOf("String")) > -1) {
            name = "'" + name.substring(0, idx - 1) + "'";
        } else if ((idx = name.indexOf("Integer")) > -1) {
            name = name.substring(0, idx - 1);
        } else if ((idx = name.indexOf("Long")) > -1) {
            name = name.substring(0, idx - 1);
        }
        //name = name.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        name = StringEscapeUtils.escapeHtml(name);
        return name;
    }
}
