package com.commsens.tollgatetest;

import java.util.ArrayList;

public class RouteCodeList {

    private final ArrayList<RouteCodeItem> routeCodeItemArrayList = new ArrayList<>();

    public RouteCodeList() {
        String[] routeCodes = {
                "001", "120", "253", "010", "102",
                "104", "030", "300", "065", "012",
                "600", "060", "100", "151", "015",
                "027", "050", "020", "110", "045",
                "451", "055", "551", "035", "040",
                "014", "025", "251"
        };
        String[] routeNames = {
                "경부선", "경인선", "고창담양선", "남해선", "남해제1지선",
                "남해제2지선", "당진영덕선", "대전남부순환선", "동해·울산포항선", "무안광주·광주대구선",
                "부산외곽순환선", "서울양양선", "수도권제1순환선", "서천공주선", "서해안선",
                "순천완주선", "영동선", "익산포항선", "제2경인선", "중부내륙선",
                "중선내륙선의지선", "중앙선", "중앙선의지선", "통영대전·중부선", "평택제천선",
                "함양울산선", "호남선", "호남선의지선"
        };

        for (int i = 0; i < routeCodes.length; i++) {
            routeCodeItemArrayList.add(new RouteCodeItem(routeCodes[i], routeNames[i]));
        }
    }

    public ArrayList<RouteCodeItem> getRouteCodeItemArrayList() {
        return routeCodeItemArrayList;
    }
}
