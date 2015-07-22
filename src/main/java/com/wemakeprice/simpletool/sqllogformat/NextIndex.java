package com.wemakeprice.simpletool.sqllogformat;

import lombok.Data;

public @Data class NextIndex {
    private int index;
    private int nextIndex;

    public void scan(String document, String str) throws Exception {
        int idx = document.indexOf(str, index);
        if (idx < 0) {
            throw new Exception(str + " is not found.");
        }
        index = nextIndex;
        nextIndex = idx + str.length();
    }

    public void scanNewLine(String tmpSqlLog) {
        int idx = tmpSqlLog.indexOf("\n", nextIndex);
        if (idx > -1) {
            index = nextIndex;
            nextIndex = idx;
        } else {
            idx = tmpSqlLog.indexOf("\r\n", nextIndex);
            if (idx > -1) {
                index = nextIndex;
                nextIndex = idx;
            }
        }
    }

    public String read(String tmpSqlLog) {
        return tmpSqlLog.substring(index, nextIndex);
    }
}
