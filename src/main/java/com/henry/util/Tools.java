package com.henry.util;

import java.util.HashMap;
import java.util.Map;

public class Tools {

    public static Map<String, String> dateTypes;
    static{
        dateTypes = new HashMap<String, String>();
        dateTypes.put("TINYINT", "int");
        dateTypes.put("INT", "int");
        dateTypes.put("BIGINT", "long");
        dateTypes.put("FLOAT", "double");
        dateTypes.put("VARCHAR", "String");
        dateTypes.put("CHAR", "String");
        dateTypes.put("TEXT", "String");
        dateTypes.put("DATE", "Date");
        dateTypes.put("DATETIME", "Date");
    }
    
    
}
