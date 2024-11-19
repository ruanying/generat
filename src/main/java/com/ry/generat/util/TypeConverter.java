package com.ry.generat.util;

import java.util.HashMap;
import java.util.Map;

public class TypeConverter {

    private static Map<String, String> types;

    private static void init() {
        types = new HashMap<>();

        types.put("CHAR", "String");
        types.put("VARCHAR", "String");
        types.put("LONGVARCHAR", "String");

        types.put("NUMERIC", "BigDecimal");
        types.put("DECIMAL", "BigDecimal");

        types.put("BIT", "Boolean");
        types.put("BOOLEAN", "Boolean");

        types.put("TINYINT", "Integer");// byte
        types.put("SMALLINT", "Integer");// short
        types.put("INTEGER", "Integer");
        types.put("BIGINT", "Long");
        // types.put("REAL", "Float");
        types.put("FLOAT", "Float");// Double
        types.put("DOUBLE", "Double");

        types.put("BINARY", "byte[]");
        types.put("VARBINARY", "byte[]");
        types.put("LONGVARBINARY", "byte[]");

        types.put("DATE", "Date");// java.sql.Date
        types.put("TIME", "Date");// java.sql.Time
        types.put("TIMESTAMP", "Date");// java.sql.Timestamp

        types.put("CLOB", "byte[]");// Clob
        types.put("BLOB", "byte[]");// Blob
        types.put("JSON", "String");
        // types.put("ARRAY", "Array");
        // types.put("DISTINCT", "");//mapping of underlying type
        // types.put("STRUCT", "Struct");
        // types.put("REF", "Ref");
        // types.put("DATALINK", "Ref");//java.net.URL[color=red][/color]

    }

    public static String getType(String key) {
        if (types == null) {
            init();
        }
        return types.get(key);
    }

    public static Map<String, String> getTypes() {
        if (types == null) {
            init();
        }
        return types;
    }
}
