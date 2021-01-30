package com.commsens.tollgatetest;

import androidx.annotation.NonNull;

public class RouteCodeItem {
    private final String routeCode;
    private final String routeName;

    public RouteCodeItem(@NonNull String routeCode, @NonNull String routeName) {
        this.routeCode = routeCode;
        this.routeName = routeName;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

}
