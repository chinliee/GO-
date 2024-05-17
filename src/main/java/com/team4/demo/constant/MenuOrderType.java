package com.team4.demo.constant;

public enum MenuOrderType {

    DELIVERY,

    TAKEOUT,

    ;

public static MenuOrderType getEnum(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        for (MenuOrderType menuOrderType : values()) {
            if (menuOrderType.name().equalsIgnoreCase(value)) {
                return menuOrderType;
            }
        }
        return null;
    }

}
