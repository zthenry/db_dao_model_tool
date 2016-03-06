package com.henry.metadata;

import java.util.List;

/**
 * 实体类
 */
public class EntityDefination {

    /** 实体类路径 */
    private String packPath;
    /** 实体类 */
    private String tableCode;
    /** 实体类名称 */
    private String tableName;
    /** 实体字段集合 */
    private List<FieldDefination> fieldList;
    
    /*主键列表*/
    private List<String> keyList;
    
    
   /** 过滤字段集合 */
    private List filterList;
    
    public String getPackPath() {
        return packPath;
    }
    public void setPackPath(String packPath) {
        this.packPath = packPath;
    }
    public String getTableCode() {
        return tableCode;
    }
    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public List<FieldDefination> getFieldList() {
        return fieldList;
    }
    public void setFieldList(List<FieldDefination> fieldList) {
        this.fieldList = fieldList;
    }
	public List<String> getKeyList() {
		return keyList;
	}
	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	public List getFilterList() {
		return filterList;
	}
	public void setFilterList(List filterList) {
		this.filterList = filterList;
	}
	
    
}
