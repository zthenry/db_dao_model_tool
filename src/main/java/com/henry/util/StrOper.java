package com.henry.util;

public class StrOper {

    /**
     * 首字母大写
     * @param attr
     * @return
     */
    public static String upShouzm(String attr){
        return upPosZm(attr, 0);
    }
    
    private static String upPosZm(String attr,int pos){
        return attr.substring(0,pos) + attr.substring(pos, pos+1).toUpperCase()+attr.substring(pos+1);
    }
    
    /**
     * 去下划线 驼峰变量
     * @param attr
     * @return
     */
    public static String delLine(String attr){
    	attr = attr.toLowerCase();
        while(attr.contains("_")){
            int pos = attr.indexOf("_");
            attr = upPosZm(attr, pos+1);
            attr = attr.replaceFirst("_", "");
        }
        return attr;
    }
    
    /**
     * 首字母大写+去下划线 驼峰变量
     * @param attr
     * @return
     */
    public static String upShouAndDelLine(String attr){
        return upShouzm(delLine(attr));
    }
    
    /**
     * 改变编码
     * @param content
     * @param oldChar
     * @param newChar
     * @return
     */
    public static String changeContentChar(String content, String oldChar, String newChar)  {
        try {
            return new String(content.getBytes(oldChar), newChar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(StrOper.delLine("abc_def_ghi_asdf"));
    }
}
