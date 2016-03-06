package com.henry.tempalte.impl;


import com.henry.metadata.EntityDefination;
import com.henry.metadata.FieldDefination;
import com.henry.tempalte.ISysCharset;
import com.henry.tempalte.ITemplet;

import java.util.List;

public class SQLTempletImpl implements ITemplet, ISysCharset {
	
	private EntityDefination entityDefination;
	
	public SQLTempletImpl( EntityDefination entityDefination ) {
		if ( null == entityDefination
				|| null == entityDefination.getFieldList()
				|| entityDefination.getFieldList().isEmpty() ) {
			throw new IllegalArgumentException();
		}
		this.entityDefination = entityDefination;
	}
	
	@Override
	public String templetContent() {
		return createContent();
	}
	
	private String createContent(){
        StringBuffer entityCon = new StringBuffer();
        entityCon.append(createAdd(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createUpdate(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createDelete(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createSelectOne(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createSelectSome(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createSelectSomePage(entityDefination.getFieldList())+TWO_HC);
        entityCon.append(createSelectSomePageCount(entityDefination.getFieldList())+TWO_HC);
        return entityCon.toString();
    }
	
    /** 创建添加方法 */
    private String createAdd(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("INSERT INTO ")
        			.append(entityDefination.getTableCode()).append(" (")
        			.append(createOneAllFilesCon(fieldList)).append(") VALUES (")
        			.append(createAddSomeCon(fieldList))
        			.append(")").append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建修改方法 */
    private String createUpdate(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("UPDATE ")
        			.append(entityDefination.getTableCode()).append(" SET ")
        			.append(createUpdateSomeCon(fieldList))
        			.append(" WHERE ID=?").append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建删除方法 */
    private String createDelete(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("DELETE FROM ")
        			.append(entityDefination.getTableCode())
        			.append(" WHERE ID=?").append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建单条查询 */
    private String createSelectOne(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("SELECT ")
        			.append(createOneAllFilesCon(fieldList))
        			.append(" FROM ").append(entityDefination.getTableCode())
        			.append(" WHERE ID=?").append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建多条查询 */
    private String createSelectSome(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("SELECT ")
        		.append(createOneAllFilesCon(fieldList))
        		.append(" FROM ")
        		.append(entityDefination.getTableCode())
        		.append(createFilterClause(fieldList)).append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建多条分页查询 */
    private String createSelectSomePage(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("SELECT ")
        		.append(createOneAllFilesCon(fieldList))
        		.append(" FROM ").append(entityDefination.getTableCode())
        		.append(createFilterClause(fieldList))
        		.append(" LIMIT ?, ?").append(ONE_HC);
        return strResult.toString();
    }
    
    /** 创建多条分页需要的count查询 */
    private String createSelectSomePageCount(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append("SELECT COUNT(*) FROM ")
        		.append(entityDefination.getTableCode())
        		.append(createFilterClause(fieldList)).append(ONE_HC);
        return strResult.toString();
    }
    
    private String createOneAllFilesCon(List<FieldDefination> fieldList){
        StringBuffer strView = new StringBuffer();
        String theZd = null;
        for(FieldDefination fieldDefination : fieldList) {
            theZd = fieldDefination.getFieldCode();
            strView.append(theZd+", ");
        }
        return strView.substring(0, strView.length()-2);
    }
    
    private String createAddSomeCon(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        for ( int i=fieldList.size(); i>0; i-- ) {
        	strResult.append("?").append(", ");
        }
        return strResult.substring(0, strResult.length()-2);
    }
    
    private String createUpdateSomeCon(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        String theZd = null;
        for(FieldDefination fieldDefination : fieldList) {
            theZd = fieldDefination.getFieldCode();
            strResult.append(theZd+"=?, ");
        }
        return strResult.substring(0, strResult.length()-2);
    }
    
    private String createFilterClause(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer(" WHERE ");
        String theZd = null;
        for(FieldDefination fieldDefination : fieldList) {
            theZd = fieldDefination.getFieldCode();
            strResult.append(theZd+"=? and ");
        }
        return strResult.substring(0, strResult.length()-5);
    }

}
