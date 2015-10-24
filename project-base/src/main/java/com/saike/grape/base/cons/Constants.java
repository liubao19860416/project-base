package com.saike.grape.base.cons;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量定义类
 * 
 */
public final class Constants {

    //私有的构造方法（因为有枚举，否则可以使用接口）
    private Constants() {
    }

    //默认的时间戳格式
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static final int DAYS_MIN = 0;                    // 最小正整数
    public static final int DAYS_MAX = Integer.MAX_VALUE;    // 最大整型数据
    public static final float FLOAT_MIN = 0.0f;              // 最小正浮点数
    public static final float FLOAT_MAX = Float.MAX_VALUE;   // 最大浮点数
    public static final String FIT_TO_ALL_CODE = "*";        // 编号通配符,适用所有范围

    //存储Enum枚举类型map
    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> mapEnumTypes = new HashMap<Class<? extends Enum<?>>, Enum<?>[]>();

    //初始化Enum数据到静态map对象中去
    static {
        mapEnumTypes.put(MaintenancePeriodUnit.class,
                MaintenancePeriodUnit.class.getEnumConstants());

        mapEnumTypes.put(SparePartGroupedSelectMode.class,
                SparePartGroupedSelectMode.class.getEnumConstants());
    }

    // 保养周期单位
    public static enum MaintenancePeriodUnit {
        NONE("-"),  // 非周期性
        KM("km"),   // 按公里数
        DAY("day"); // 按日期（天数）

        private String unit;

        private MaintenancePeriodUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return this.unit;
        }
    }

    // 配件选择模式
    public static enum SparePartGroupedSelectMode {
        MUST("must"),           // 必选
        CHECKED("checked"),     // 选中状态（可选）
        UNCHECKED("unchecked"); // 非选中状态（可选）

        private String selectMode;

        private SparePartGroupedSelectMode(String selectMode) {
            this.selectMode = selectMode;
        }

        @Override
        public String toString() {
            return this.selectMode;
        }
    }

    // 车挡类型
    public static enum Gear {
        AUTO("A"),              // 自动
        MANUAL("M"),            // 手动
        MANUAL_PLUS_AUTO("MA"), // 手自一体
        NA("-");                // N/A 未知

        private String type;

        private Gear(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    /**
     * 根据value值，获取对应的Enum枚举类型对象
     */
    public static Enum<?> getEnumByValue(Class<? extends Enum<?>> enumClass,
            String value) {

        if (enumClass == null || !enumClass.isEnum()) {
            throw new IllegalArgumentException("Argument enumClass is null or is not Enum type!!");
        }

        if (value == null || "".equals(value)) {
            return null;
        }

        for (Class<? extends Enum<?>> eclass : mapEnumTypes.keySet()) {
            if (eclass.equals(enumClass)) {
                for (Enum<?> e : mapEnumTypes.get(eclass)) {
                    if (e.toString().equals(value)) {
                        return e;
                    }
                }
            }
        }

        throw new IllegalArgumentException("Enum class \""
                + enumClass.getName() + "\"" + " with value \"" + value + "\""
                + " is not supported!!");
    }

}
