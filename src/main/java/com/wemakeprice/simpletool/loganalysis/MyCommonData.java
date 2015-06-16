package com.wemakeprice.simpletool.loganalysis;

import java.util.HashMap;
import java.util.Map;

public class MyCommonData {
    public static Map<String, String> jikmooMap = new HashMap<>();
    public static Map<String, String> jikchakMap = new HashMap<>();

    public static Map<String, String> urlMap = new HashMap<>();
    private static Map<String, String> jikgubMap = new HashMap<>();

    static {
        jikmooMap.put("1", "AMD");
        jikmooMap.put("2", "AP");
        jikmooMap.put("3", "CS");
        jikmooMap.put("4", "DBA");
        jikmooMap.put("5", "MC");
        jikmooMap.put("6", "MD");
        jikmooMap.put("7", "QA");
        jikmooMap.put("8", "SCM");
        jikmooMap.put("9", "SE");
        jikmooMap.put("10", "UI개발");
        jikmooMap.put("11", "경영지원");
        jikmooMap.put("12", "기획운영");
        jikmooMap.put("13", "디자이너");
        jikmooMap.put("14", "마케팅");
        jikmooMap.put("15", "법무");
        jikmooMap.put("16", "보안");
        jikmooMap.put("17", "신사업");
        jikmooMap.put("18", "영상");
        jikmooMap.put("19", "인사");
        jikmooMap.put("20", "임원");
        jikmooMap.put("21", "재무");
        jikmooMap.put("22", "전략기획");
        jikmooMap.put("23", "전술");
        jikmooMap.put("24", "통계분석");
        jikmooMap.put("25", "포토그래퍼");
        jikmooMap.put("26", "프로그래머");
        jikmooMap.put("27", "홍보");
        jikmooMap.put("28", "사무보조");

        jikchakMap.put("1", "대표이사");
        jikchakMap.put("2", "디렉터");
        jikchakMap.put("3", "사업부장");
        jikchakMap.put("4", "팀장");
        jikchakMap.put("5", "팀장(대행)");
        jikchakMap.put("6", "센터장");
        jikchakMap.put("7", "부문장");

        jikgubMap.put("1", "대표이사");
        jikgubMap.put("2", "부사장");
        jikgubMap.put("3", "부장");
        jikgubMap.put("4", "차장");
        jikgubMap.put("5", "과장");
        jikgubMap.put("6", "대리");
        jikgubMap.put("7", "사원");
        jikgubMap.put("8", "SMC");
        jikgubMap.put("9", "WMC");
        jikgubMap.put("10", "MMC");
        jikgubMap.put("11", "IMC");
        jikgubMap.put("12", "IMC1");
        jikgubMap.put("13", "IMC2");

        urlMap.put("/contents/getContentsList.wmp", "제작진행/완료 페이지");
        urlMap.put("/contents/getContentsDetail.wmp", "제작요청상세 페이지");
        urlMap.put("/contents/createContentsMake.wmp", "제작요청 페이지");
        urlMap.put("/contents/common/getContentsPoint.pop", "컨텐츠포인트현황 팝업");
        urlMap.put("/contents/contentsUpdateMake.wmp", "컨텐츠제작수정 페이지");
        urlMap.put("/contents/contentsRequestProduction.wmp", "컨텐츠제작요청현황(포토) 페이지");
        urlMap.put("/contents/contentsRequestProductionDesign.wmp", "컨텐츠제작요청현황(디자이너) 페이지");
        urlMap.put("/contents/currentProductDesign.wmp", "컨텐츠제작현황(디자이너) 페이지");
        urlMap.put("/contents/requestProductionDesignExcel.down", "컨텐츠제작 요청현황(디자이너) 엑셀다운로드");
        urlMap.put("/contents/getStaffPointStatus.wmp", "개별포인트 페이지");
        urlMap.put("/contents/getContentsPointStatusList.wmp", "부서포인트 페이지");
        urlMap.put("/contents/staffPointStateListExcel.down", "개별포인트 엑셀다운로드 ");
        urlMap.put("/contents/scheduler.wmp", "컨텐츠제작일정 페이지");
        urlMap.put("/contents/scheduler.pop", "컨텐츠제작일정 팝업");
        urlMap.put("/contents/preview.pop", "대용량파일미리보기 팝업");
        urlMap.put("/contents/getContentsModifySettingList.wmp", "수정현황>컨텐츠수정담당자지정");
        urlMap.put("/contents/contentsExcelUpload", "요청엑셀업로드");
        urlMap.put("/contents/addDataInExcel", "요청샘플 엑셀다운로드");
        urlMap.put("/contents/refFileDownLoad.down", "제작요청파일다운로드");
        urlMap.put("/contents/getContentsModifyList.wmp", "컨텐츠수정리스트");
        urlMap.put("/contents/contentsRating.wmp", "컨텐츠 딜평가표");
        urlMap.put("/contents/currentModifyDesign.wmp", "컨텐츠수정현황(디자이너)");
        urlMap.put("/contents/getDesignPrice.down", "주간보고용 디자인단가 엑셀다운로드");
        urlMap.put("/contents/getDesignerPoint.down", "주간보고용 디자인팀 엑셀다운로드");
        urlMap.put("/contents/getContentsModifyPop.pop", "수정요청 수정 팝업");
        urlMap.put("/contents/setDealratingList.wmp", "딜평가표 관리");
        urlMap.put("/contents/createProcessAlarm.wmp", "업무알림 관리 설정");
        urlMap.put("/contents/requestModifyInfo.pop", "업무알림 관리 설정");
        urlMap.put("/contents/requestModifyInfo.pop", "수정요청 팝업");
        urlMap.put("/mypage/myPageList.wmp", "비밀번호변경");
        urlMap.put("/contents/setDesignPriceList.wmp", "컨텐츠제작단가 관리설정");
    }

    public static String getJikchak(String cd) {
        if (jikchakMap.containsKey(cd)) {
            return jikchakMap.get(cd);
        }
        return "팀원";
    }

    public static String getJikmoo(String cd) {
        if (jikmooMap.containsKey(cd)) {
            return jikmooMap.get(cd);
        }
        return cd;
    }

    public static String getUrl(String cd) {
        if (urlMap.containsKey(cd)) {
            return urlMap.get(cd);
        }
        return "";
    }

    public static String getJikgub(String cd) {
        if (jikgubMap.containsKey(cd)) {
            return jikgubMap.get(cd);
        }
        return cd;
    }

}
