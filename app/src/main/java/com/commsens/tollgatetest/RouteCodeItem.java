package com.commsens.tollgatetest;

import androidx.annotation.NonNull;

public class RouteCodeItem {
    private String routeCode;
    private String routeName;

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
