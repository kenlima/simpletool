package com.wemakeprice.simpletool.sqlmapformatter;

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
public class SqlMapFormatterService {
    public SqlMappingResult mappingSql(String inputSql) {
        SqlMappingResult result = new SqlMappingResult();
        String data = inputSql;
        // 15:58:46,515 INFOD  [stdout] (default task-11)
        data = data.replaceAll(
                "[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} [A-Z]{4,5}  \\[stdout\\] \\(default task-[0-9]+\\)", "");
        List<String> parameters = new ArrayList<>();
        String inputQuery = "";

        int idx = 0;

        if ((idx = data.indexOf("Preparing:")) > -1) {

            inputQuery = data.substring(idx + 10);
            idx = inputQuery.indexOf("\n");
            if (idx > -1) {
                inputQuery = inputQuery.substring(0, idx);
            } else {
                idx = inputQuery.indexOf("\r\n");
                if (idx > -1) {
                    inputQuery = inputQuery.substring(0, idx);
                }
            }

        }

        if ((idx = data.indexOf("Parameters:")) > -1) {
            int begin = idx + 11;
            String param = data.substring(begin);
            result.setParameters(param);

            /*
            Pattern script = Pattern.compile("\\([a-zA-Z]*\\)");
            Matcher mat = script.matcher(param);
            String result = mat.replaceAll("");
            */
            parameters = Arrays.asList(param.split("[,]"));
            //parameters.replaceAll(String::trim);

            //parameters.stream().map(s -> replaceTypeValue(s));
            parameters.replaceAll(SqlMapFormatterService::replaceTypeValue);

        }
        inputQuery = parseQuery(inputQuery);
        System.out.println("Parameters : " + String.join(", ", parameters));

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
        result.setSql(mappedQuerySb.toString());

        //result = parseQuery(result);

        return result;
    }

    private String parseQuery(String result) {
        System.out.println("extracted sql : " + result);
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
        sqlparser.setSqltext(result);
        int ret = sqlparser.parse();
        if (ret == 0) {
            GFmtOpt option = GFmtOptFactory.newInstance();
            option.outputFmt = GOutputFmt.ofhtml;
            result = FormatterFactory.pp(sqlparser, option);
            System.out.println(result);
        }
        System.out.println("parsed sql : " + result);
        return result;
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
