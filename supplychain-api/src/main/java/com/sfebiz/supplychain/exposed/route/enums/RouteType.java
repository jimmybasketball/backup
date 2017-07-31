package com.sfebiz.supplychain.exposed.route.enums;

/**
 * 路由信息类型
 * @author liujc [liujunchi@ifunq.com]
 * @date  2017/7/28 11:42
 */
public enum RouteType {

    /* 枚举的声明顺序不要变，读取用户路由信息时(AbstractRouteOperation.arrangeUserViewRoute) 依赖这里的声明顺序 */
    INTERNAL("INTERNAL", "国内段物流"),
    CLEARANCE("CLEARANCE", "清关段物流"),
    TRANSIT("TRANSIT", "集货(转运)段物流"),
    INTERNATIONAL("INTERNATIONAL", "国际段物流"),
    USERDEFINED("USERDEFINED", "企业自定义");


    private String type;
    private String description;

    RouteType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static RouteType getByType(String type) {
        for (RouteType routeType : RouteType.values()) {
            if (routeType.getType().equalsIgnoreCase(type)) {
                return routeType;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (RouteType type : RouteType.values()) {
            System.out.println(type);
        }
    }
}
