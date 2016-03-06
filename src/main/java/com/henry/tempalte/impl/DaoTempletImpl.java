package com.henry.tempalte.impl;


import com.henry.metadata.EntityDefination;
import com.henry.metadata.FieldDefination;
import com.henry.tempalte.ISysCharset;
import com.henry.tempalte.ITemplet;
import com.henry.util.Director;
import com.henry.util.MessageUtil;
import com.henry.util.StrOper;

import java.util.ArrayList;
import java.util.List;

public class DaoTempletImpl implements ITemplet,ISysCharset {
	private String view ; 
	private String tableName;
	private String aliasName;
    private String fields;
    private String values;
    private String batchValues;

    private String dbWriteSource;
    private String dbReadSource;

    private String inserBatchMethodName = "insertBatch";
	private String queryConditions;
	/*主键查询条件 开头没有and*/
	private String keyConditions;
	
	
	
	
    private EntityDefination entityDefination;
    private String ClassNameBl = null;
    private String ClassName = null;
    private static final String PRE = "obj";
    private final String INSERT_NAME = "insert";
    private final String INSERT_BATCH_NAME = "insertBatch";
    public DaoTempletImpl(EntityDefination entityDefination){
        this.entityDefination = entityDefination;
        this.ClassNameBl = StrOper.delLine(entityDefination.getTableCode());
        this.ClassName = StrOper.upShouAndDelLine(entityDefination.getTableCode());
    }
    /**
     * 获取实体类的字符串描述
     * @return
     */
    public String templetContent(){
        return createContent();
    }
    
    /**
     * 整个类的字符串描述
     * @return
     */
    private String createContent(){
        dbWriteSource = MessageUtil.getMessage("tools").getProperty("dbWriteSource");
        dbReadSource = MessageUtil.getMessage("tools").getProperty("dbReadSource");
        StringBuffer entityCon = new StringBuffer();
        entityCon.append("package "+entityDefination.getPackPath()+"."+PACK_DAO+";"+TWO_HC);
        entityCon.append("import java.util.*;"+TWO_HC);
        entityCon.append("import com.sankuai.meituan.waimai.datasource.multi.annotation.DataSource;"+ONE_HC);
        entityCon.append("import org.apache.ibatis.annotations.*;"+ONE_HC);
        entityCon.append("import org.springframework.stereotype.Component;"+ONE_HC);
        entityCon.append("import java.text.MessageFormat;"+TWO_HC);
        entityCon.append("import "+entityDefination.getPackPath()+"."+PACK_ENTITY+"."+ClassName+";"+TWO_HC);
        entityCon.append("/** ClassName ("+notic+") */"+ONE_HC);
        entityCon.append("@Component"+ONE_HC);
        entityCon.append("@DataSource(\""+dbWriteSource+"\")"+ONE_HC);
        entityCon.append("public interface "+ClassName+PACK_DAO_CLASS_POSTFIX+" {"+TWO_HC);
       
        //获取表名
        tableName = entityDefination.getTableCode();

        //获取别名
        aliasName = createAlias(tableName);

        //获取view视图
        view = createView(entityDefination.getFieldList());

        fields = createFields(entityDefination.getFieldList());
        values = createValues(entityDefination.getFieldList());

        batchValues = createBatchValues(entityDefination.getFieldList());

        //主键查询条件
        keyConditions = getIdCondtions(entityDefination.getFieldList());
        
        
        
        //创建静态属性
        entityCon.append(ONE_TAB+"public static final String ALIGNNAME =\""+aliasName+"\";"+TWO_HC);
        entityCon.append(ONE_TAB+"public static final String VIEW =\""+view+"\";"+TWO_HC);
        entityCon.append(ONE_TAB+"public static final String TABLENAME =\""+tableName+"\";"+TWO_HC);
        entityCon.append(ONE_TAB+"public static final String FIELDS =\""+fields+"\";"+TWO_HC);
        entityCon.append(ONE_TAB+"public static final String VALUES =\""+values+"\";"+TWO_HC);
        entityCon.append(ONE_TAB+"public static final String BATCHVALUES =\""+batchValues+"\";"+TWO_HC);

        //创建查询条件的常量
//        entityCon.append(createConditions(entityDefination.getFieldList()));
        
        //添加方法
        entityCon.append(createInsert(entityDefination.getFieldList())+TWO_HC);

        //添加方法
        entityCon.append(createInsertBatch(entityDefination.getFieldList())+TWO_HC);



        //创建删除方法
        entityCon.append(createDelete(entityDefination.getFieldList())+TWO_HC); 
        //如果全部都是主键，则不提供更新的方法
//        if(!isAllKey(entityDefination.getFieldList()))
//        {
//        	 //按实例更新方法
//            entityCon.append(createUpdate(entityDefination.getFieldList())+TWO_HC); //创建修改方法
//            //按属性更新方法
//            entityCon.append(createUpdateByProperty(entityDefination.getFieldList())+TWO_HC);
//        }
        
        //按ID查询
        entityCon.append(createFindById(entityDefination.getFieldList())+TWO_HC);
//        //按对象查询
//        entityCon.append(createFindByExample(entityDefination.getFieldList())+TWO_HC);
//        //按对象和属性集合模糊查询
//        entityCon.append(createFindByExampleAndColletion(entityDefination.getFieldList())+TWO_HC);
//        //按对象模糊查询
//        entityCon.append(createFindFuzzzyByExample(entityDefination.getFieldList())+TWO_HC);
//        //按对象和属性集合模糊查询
//        entityCon.append(createFindFuzzzyByExampleAndColletion(entityDefination.getFieldList())+TWO_HC);
//
//        //按属性属性查询
//        entityCon.append(createFindByProperty(entityDefination.getFieldList())+TWO_HC);
//        //按属性模糊查询
//        entityCon.append(createFindFuzzyByProperty(entityDefination.getFieldList())+TWO_HC);
//
//
//        if(Director.supportPage)
//        {
//        	//创建分页查询的方法
//        	entityCon.append(createFindPage(entityDefination.getFieldList())+TWO_HC);
//        	entityCon.append(createCountPage(entityDefination.getFieldList())+TWO_HC);
//
//        	//创建分页查询的方法
//        	entityCon.append(createFindFuzzyPage(entityDefination.getFieldList())+TWO_HC);
//        	entityCon.append(createCountFuzzyPage(entityDefination.getFieldList())+TWO_HC);
//
//        	//创建按属性集合查询分页查询
//        	entityCon.append(createFindPageByColletion(entityDefination.getFieldList())+TWO_HC);
//        	entityCon.append(countFindPageByColletion(entityDefination.getFieldList())+TWO_HC);
//
//        	//创建按属性集合模糊查询查询分页查询
//        	entityCon.append(createFuzzyFindPageByColletion(entityDefination.getFieldList())+TWO_HC);
//        	entityCon.append(countFuzzyFindPageByColletion(entityDefination.getFieldList())+TWO_HC);
//
//        }
//        //自己拼sql
//        entityCon.append(createAssembling(entityDefination.getFieldList())+TWO_HC);

        entityCon.append(createInsertBatchProviderClass());

        entityCon.append("}"+ONE_HC);
        return entityCon.toString();
    }
    private String createView(List<FieldDefination>  fieldList){
    	return createOneAllFilesCon(fieldList);
    }

    private String createFields(List<FieldDefination>  fieldList){

        StringBuffer strView = new StringBuffer();
        String table_field_name = null;
        for(FieldDefination fieldDefination : fieldList) {
            table_field_name = fieldDefination.getFieldCode();
            strView.append("`"+table_field_name+"`, ");
        }
        return strView.substring(0, strView.length()-2);

    }

    private String createValues(List<FieldDefination>  fieldList){

        StringBuffer strView = new StringBuffer();
        String table_field_name = null;
        String classFieldName = null;
        for(FieldDefination fieldDefination : fieldList) {
            table_field_name = fieldDefination.getFieldCode();
            classFieldName = StrOper.delLine(table_field_name);
            strView.append("#{"+classFieldName+"}, ");
        }
        return strView.substring(0, strView.length()-2);

    }

    private String createBatchValues(List<FieldDefination>  fieldList){

        StringBuffer strView = new StringBuffer();
        String table_field_name = null;
        String classFieldName = null;
        for(FieldDefination fieldDefination : fieldList) {
            table_field_name = fieldDefination.getFieldCode();
            classFieldName = StrOper.delLine(table_field_name);
            strView.append("#'{'list[{0}]."+classFieldName+"}, ");
        }
        return strView.substring(0, strView.length()-2);

    }

    public static String createAlias(String table){
        String[] subStrArry = table.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append("alias_");
        for (int i = 0; i <subStrArry.length ; i++) {
            sb.append(subStrArry[i].substring(0,1));
        }
        return sb.toString();
    }

    /**
     * 是否全部都是主键
     */
    private boolean isAllKey(List<FieldDefination>  list){
    	boolean flag = true;
    	for (FieldDefination fieldDefination : list) {
			flag = fieldDefination.getIsKey();
			if(!flag)
				return flag;
		}
    	return true;
    }



    /** 创建添加方法 */
    private String createInsert(List<FieldDefination> fieldList){
    	 
    	Boolean returnFlag = false;
    	List<FieldDefination> keyList = findKey(fieldList);
    	String returnType = "void";
        String keyProperty = "id";
        if((!keyList.isEmpty())&&keyList.size()==1&&keyList.get(0).getAutoIncreate())
        {
            returnFlag = true;
        	returnType = keyList.get(0).getFieldType();
            keyProperty = keyList.get(0).getFieldCode();
        }
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** insert "+ClassName+" */"+ONE_HC);
        strResult.append(ONE_TAB+"@DataSource(\""+dbWriteSource+"\")"+ONE_HC);
//        if(returnFlag)
//        strResult.append(ONE_TAB+"@Insert"+ONE_HC);

        strResult.append(ONE_TAB+"@Insert("+'"'+"insert into \"+TABLENAME+\" (\"+FIELDS+\") values (\"+VALUES+\")\")"+ONE_HC);
        strResult.append(ONE_TAB+"@Options(useGeneratedKeys = true, keyColumn = \""+keyProperty+"\", keyProperty = \""+keyProperty+"\", useCache = false, flushCache = true)"+ONE_HC);

        strResult.append(ONE_TAB+"public "+returnType+" "+INSERT_NAME+"("+ClassName+" "+ClassNameBl+");");
        return strResult.toString();
    }


    /** 创建添加方法 */
    private String createInsertBatch(List<FieldDefination> fieldList){

        Boolean returnFlag = false;
        List<FieldDefination> keyList = findKey(fieldList);
        String returnType = "void";
        String keyProperty = "id";
        if((!keyList.isEmpty())&&keyList.size()==1&&keyList.get(0).getAutoIncreate())
        {
            returnFlag = true;
            returnType = keyList.get(0).getFieldType();
            keyProperty = keyList.get(0).getFieldCode();
        }
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** insert "+ClassName+" */"+ONE_HC);
        strResult.append(ONE_TAB+"@DataSource(\""+dbWriteSource+"\")"+ONE_HC);
//        if(returnFlag)
//        strResult.append(ONE_TAB+"@Insert"+ONE_HC);

        strResult.append(ONE_TAB+"@InsertProvider(type = "+ClassName+PACK_DAO_CLASS_POSTFIX+DAO_PROVIDER_CLASS_POSTFIX+".class, method = \""+inserBatchMethodName+"\")"+ONE_HC);

        strResult.append(ONE_TAB+"public "+returnType+" "+INSERT_BATCH_NAME+"(List<"+ClassName+"> "+ClassNameBl+"List);");
        return strResult.toString();
    }

    private String createInsertBatchProviderClass(){
        StringBuilder sb = new StringBuilder();
        String varName = ClassNameBl+"List";
        sb.append(ONE_TAB+"static class "+ClassName+PACK_DAO_CLASS_POSTFIX+DAO_PROVIDER_CLASS_POSTFIX+" {"+ONE_HC);
        sb.append(TWO_TAB+"public String "+inserBatchMethodName+"(Map map) {"+ONE_HC);
        sb.append(ONE_TAB + TWO_TAB + "List<" + ClassName + "> " + varName + " = (List<" + ClassName + ">)map.get(\"list\");" + ONE_HC);
        sb.append(ONE_TAB+TWO_TAB+"StringBuilder sb = new StringBuilder();"+ONE_HC);

        sb.append(ONE_TAB+TWO_TAB+"sb.append(\"INSERT INTO \"+TABLENAME+\" (\"+FIELDS+\") VALUES \");"+ONE_HC);

        sb.append(ONE_TAB+TWO_TAB+"MessageFormat mf = new MessageFormat(\"(\"+BATCHVALUES+\")\");"+ONE_HC);
        sb.append(ONE_TAB+TWO_TAB+"for (int i = 0; i < "+varName+".size(); i++) {"+ONE_HC);
        sb.append(TWO_TAB+TWO_TAB+"sb.append(mf.format(new Object[]{i}));"+ONE_HC);
        sb.append(TWO_TAB+TWO_TAB+"if (i < "+varName+".size() - 1) {"+ONE_HC);
        sb.append(TWO_TAB+TWO_TAB+ONE_TAB+"sb.append(\",\");"+ONE_HC);

        sb.append(TWO_TAB+TWO_TAB+"}"+ONE_HC);

        sb.append(ONE_TAB+TWO_TAB+"}"+ONE_HC);

        sb.append(ONE_TAB+TWO_TAB+"return sb.toString();"+ONE_HC);
        sb.append(TWO_TAB+"}"+ONE_HC);
        sb.append(ONE_TAB+"}"+ONE_HC);

        return sb.toString();
    }
    /** 添加方法的 一些代码 */
  private String createInsertdSomeCon(List<FieldDefination> fieldList){
      StringBuffer strResult = new StringBuffer();
      String theField = null;
      for(FieldDefination fieldDefination : fieldList) {
      	//如果是自增长的列
      	if(fieldDefination.getAutoIncreate())
      	{
      		
      		theField = addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName(),":"+PRE+"."+StrOper.delLine(fieldDefination.getFieldCode())+", ");
      		strResult.append(theField);
      	}
      	else{
      		theField = StrOper.delLine(fieldDefination.getFieldCode());
              strResult.append(":"+PRE+"."+theField+", ");
      	}
          
      }
      return strResult.substring(0, strResult.length()-2);
  }
    /** 创建insert的字段 */
  private String createInsertAllFilesCon(List<FieldDefination> fieldList){
      StringBuffer strView = new StringBuffer();
      String theZd = null;
      for(FieldDefination fieldDefination : fieldList) {
      	//如果是自增加的
      	if(fieldDefination.getAutoIncreate())
      	{
      		theZd = addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName(),fieldDefination.getFieldCode()+", ");
//      		 #if(:obj.warnMessageId!=null) { warn_message_id,  }
      		strView.append(theZd);
      	}
      	else{
      		theZd = fieldDefination.getFieldCode();
      		strView.append("`"+theZd+"`, ");
      	}
          
      }
      return strView.substring(0, strView.length()-2);
  }
  private String addIf(String type,String column,String target){
  	StringBuffer sb = new StringBuffer();
  	if("String".equals(type))
  	{
  		sb.append(" #if(:"+PRE+"."+column+") { {_target_} }");
  	}
  	else{
  		sb.append(" #if(:"+PRE+"."+column+"!=null) { {_target_} }");
  	}
  	return sb.toString().replace("{_target_}", target);
  }
  /** 创建删除方法 */
	private String createDelete(List<FieldDefination> fieldList){
		List<FieldDefination> keyList = new ArrayList<FieldDefination>();
		for (FieldDefination fieldDefination : fieldList) {
			if(fieldDefination.getIsKey())
			{
				keyList.add(fieldDefination);
			}
		}
	    StringBuffer strResult = new StringBuffer();
	    strResult.append(ONE_TAB+"/** delete "+ClassName+"*/"+ONE_HC);
	    strResult.append(ONE_TAB+"@DataSource(\""+dbWriteSource+"\")"+ONE_HC);
	    strResult.append(ONE_TAB+"@Delete("+'"'+"delete from \"+TABLENAME+\" where "+getDeleteConditions(keyList)+" \")"+ONE_HC);
	    strResult.append(ONE_TAB+"public void delete"+getDeleteParam(keyList)+";");
	    return strResult.toString();
	}
	/**
	 * 获ID的查询条件
	 * @param
	 * @return
	 */
	private String getIdCondtions(List<FieldDefination> fieldList){
		StringBuffer sb = new StringBuffer(); 
		int flag = 0;
		List<FieldDefination>  keyList = findKey(fieldList);
		for (FieldDefination fieldDefination : keyList) {
			if(flag !=0)
			{
				sb.append(" and ");
			}
			sb.append(" "+aliasName+"."+ fieldDefination.getFieldCode()+"=#{"+fieldDefination.getFieldName()+"}");
			flag++;
		}
		return sb.toString();
	}
	private String getDeleteConditions(List<FieldDefination> keyList){
		StringBuffer sb = new StringBuffer(); 
		int flag = 0;
		for (FieldDefination fieldDefination : keyList) {
			if(flag !=0)
			{
				sb.append(" and ");
			}
			sb.append(" "+ fieldDefination.getFieldCode()+"=#{"+fieldDefination.getFieldName()+"}");
			flag++;
		}
		return sb.toString();
	}
	private String getDeleteParam(List<FieldDefination> keyList ){
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			int flag =0;
			for (FieldDefination fieldDefination : keyList) {
				if(flag !=0 )
				{
					sb.append(",");
				}
				sb.append("@Param(\""+fieldDefination.getFieldName()+"\") "+fieldDefination.getFieldType()+" "+fieldDefination.getFieldName());
				flag++;
			}
			sb.append(")");
			return sb.toString();
	}
	
	private String createUpdateByProperty(List<FieldDefination> fieldList){
		List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** update "+ClassName+" by property */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"update "+tableName+" set ");
        strResult.append(" ##(:propertyName)=:propertyValue ");
        strResult.append(" where "+getUpdateConditions(keyList)+" \")"+ONE_HC);
        strResult.append(ONE_TAB+"public void update(@SQLParam("+'"'+PRE+'"'+") "+ClassName+" "+ClassNameBl+",@SQLParam("+'"'+"propertyName"+'"'+") String propertyName," +
        		"@SQLParam("+'"'+"propertyValue"+'"'+") Object propertyValue);");
        return strResult.toString();
	}
    /** 创建修改方法 */
    private String createUpdate(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** update "+ClassName+" by entity */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"update "+tableName+" set ");
        strResult.append(createUpdateSomeCon(fieldList));
        strResult.append(" where  "+getUpdateConditions(keyList)+" \")"+ONE_HC);
        strResult.append(ONE_TAB+"public void update(@SQLParam("+'"'+"obj"+'"'+") "+ClassName+" "+ClassNameBl+");");
        return strResult.toString();
    }
    private List<FieldDefination> findKey(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = new ArrayList<FieldDefination>();
		for (FieldDefination fieldDefination : fieldList) {
			if(fieldDefination.getIsKey())
			{
				keyList.add(fieldDefination);
			}
		}
		return keyList;
    }
    private String getUpdateConditions(List<FieldDefination> keyList){
    	StringBuffer sb = new StringBuffer(); 
    	int flag = 0; 
		for (FieldDefination fieldDefination : keyList) {
			if(flag !=0)
			{
				sb.append(" and ");
			}
			sb.append(" "+ fieldDefination.getFieldCode()+"=:"+PRE+"."+fieldDefination.getFieldName());
			flag++;
		}
		return sb.toString();
    	
    }
    /** 修改方法的 一些代码 */
    private String createUpdateSomeCon(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        String theZd = null;
        String theField = null;
        for(FieldDefination fieldDefination : fieldList) {
        	//如果不是主键
        	if(!fieldDefination.getIsKey())
        	{
        		theZd = fieldDefination.getFieldCode();
                theField = StrOper.delLine(fieldDefination.getFieldCode());
                strResult.append("`"+theZd+"`"+"=:obj."+theField+", ");
        	}
        }
        
        return strResult.substring(0, strResult.length()-2);
    }
    
    /** 按ID查询 */
    private String createFindById(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" find by ID , */"+ONE_HC);
        strResult.append(ONE_TAB+"@DataSource(\""+dbReadSource+"\")"+ONE_HC);
        strResult.append(ONE_TAB+"@Select("+'"'+"select ");
        strResult.append("\"+VIEW+\"");
        strResult.append(" from \"+TABLENAME+\" \"+ALIGNNAME+\" where  "+ keyConditions +"\")"+ONE_HC);
        strResult.append(ONE_TAB+"public "+ClassName+" findById"+getDeleteParam(keyList)+";");
        return strResult.toString();
    }
    
    /** 按对象查询 */
    private String createFindByExample(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" find by example , */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" "+createFilterClause(fieldList)+"\")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findByExample(@SQLParam("+'"'+"obj"+'"'+") "+ClassName+" "+ClassNameBl+");");
        return strResult.toString();
    }
    
    /** 按对象和属性集合查询 */
    private String createFindByExampleAndColletion(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" 按对象和属性集合查询 */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" "+createFilterClause(fieldList)+"  and ##(:property) in (:list)  \")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findByExample(@SQLParam("+'"'+"obj"+'"'+") "+ClassName+" "+ClassNameBl+" ,@SQLParam(\"property\") String propertyName,@SQLParam(\"list\") List list );");
        return strResult.toString();
    }
    
    /** 按对象和集合模糊查询 */
    private String createFindFuzzzyByExampleAndColletion(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" 按对象模糊查询，只会对属性是字符的属性才模糊查询 */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" "+createFuzzyFilterClause(fieldList)+"  and ##(:property) in (:list)  \")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findFuzzyByExample(@SQLParam("+'"'+"obj"+'"'+") "+ClassName+" "+ClassNameBl+" ,@SQLParam(\"property\") String propertyName,@SQLParam(\"list\") List list );");
        return strResult.toString();
    }
    
    /** 
     * 按对象模糊查询
     *  */
    private String createFindFuzzzyByExample(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" 按对象模糊查询 , */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" "+createFuzzyFilterClause(fieldList)+"\")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findFuzzyByExample(@SQLParam("+'"'+"obj"+'"'+") "+ClassName+" "+ClassNameBl+");");
        return strResult.toString();
    }
    
    /** 按对象查询 */
    private String createFindFuzzyByProperty(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" find by property fuzzy , */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" where "+aliasName+".##(:propertyName) like CONCAT('%',CONCAT(:propertyValue,'%'))  \")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findFuzzyByProperty(@SQLParam("+'"'+"propertyName"+'"'+") " +
        		"String propertyName," + "@SQLParam("+'"'+"propertyValue"+'"'+") String propertyValue);");
        return strResult.toString();
    }
    
    
    /**
     *  按属性查询查询 
     **/
    private String createFindByProperty(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" find by property , */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" where "+aliasName+".##(:propertyName)=:propertyValue \")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findByProperty(@SQLParam("+'"'+"propertyName"+'"'+") " +
        		"String propertyName," + "@SQLParam("+'"'+"propertyValue"+'"'+") Object propertyValue);");
        return strResult.toString();
    }
    
   /** 查询、添加用到的字段 */
    private String createOneAllFilesCon(List<FieldDefination> fieldList){
        StringBuffer strView = new StringBuffer();
        String table_field_name = null;
        String classFieldName = null;
        for(FieldDefination fieldDefination : fieldList) {
            table_field_name = fieldDefination.getFieldCode();
            classFieldName = StrOper.delLine(fieldDefination.getFieldCode());
            strView.append(aliasName+".`"+table_field_name+"` as "+classFieldName+", ");
        }
        return strView.substring(0, strView.length()-2);
    }
    /** where 子句  */
    private String createFuzzyFilterClause(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer(" WHERE 1=1 ");
        String theZd = null;
        String theField = null;
        for(FieldDefination fieldDefination : fieldList) {
        	 if("String".equals(fieldDefination.getFieldType()))
        	 {
        		 theZd = fieldDefination.getFieldCode();
                 theField = StrOper.delLine(fieldDefination.getFieldCode());
                 String condition = addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName()," and "+aliasName+".`" + theZd+"` like CONCAT('%',CONCAT(:obj."+theField+",'%')) ");
                 strResult.append(condition);
        	 }
        	 else if("Date".equalsIgnoreCase(fieldDefination.getFieldType())){
        		 theZd = fieldDefination.getFieldCode();
                 theField = StrOper.delLine(fieldDefination.getFieldCode());
        		 String ifStr = " and " +
        		 		"to_days("+aliasName+".`"+ fieldDefination.getFieldCode()+"`) = to_days(:obj."+StrOper.delLine(fieldDefination.getFieldCode())+")"; 
//        		 		"TIMESTAMPDIFF(DAY,"+aliasName+".`"+ fieldDefination.getFieldCode()+"`,:obj."+StrOper.delLine(fieldDefination.getFieldCode())+")=0  ";
        		 String condition = addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName(),ifStr);
                 strResult.append(condition);
        	 }
        	 else{
        		 theZd = fieldDefination.getFieldCode();
                 theField = StrOper.delLine(fieldDefination.getFieldCode());
                 String condition = addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName()," and " +aliasName+".`"+ theZd+"`=:obj."+theField);
                 strResult.append(condition);
        	 }
        }
        return strResult.toString();
    }
    
    /** where 子句  */
    private String createFilterClause(List<FieldDefination> fieldList){
        StringBuffer strResult = new StringBuffer(" WHERE 1=1 ");
        for(FieldDefination fieldDefination : fieldList) {
        	strResult.append(" \"+ "+ClassName+PACK_DAO_CLASS_POSTFIX+"."+getColumnWhereName(fieldDefination.getFieldName())+"+\"");
        }
        return strResult.toString();
    }
    /** 按对象查询 */
    private String createAssembling(List<FieldDefination> fieldList){
    	List<FieldDefination> keyList = findKey(fieldList);
        StringBuffer strResult = new StringBuffer();
        strResult.append(ONE_TAB+"/** "+ClassName+" find by 自己定义的sql语句 , */"+ONE_HC);
        strResult.append(ONE_TAB+"@SQL("+'"'+"select ");
        strResult.append(view);
        strResult.append(" from "+tableName+" "+aliasName+" where 1=1 and  ##(:where) \")"+ONE_HC);
        strResult.append(ONE_TAB+"public List<"+ClassName+"> findCustomWhere(@SQLParam("+'"'+"where"+'"'+") String where);");
        return strResult.toString();
    }
    public String createConditions(List<FieldDefination> fieldList){
    	StringBuffer sb = new StringBuffer();
    	for (FieldDefination fieldDefination : fieldList) {
    		if("Date".equalsIgnoreCase(fieldDefination.getFieldType()))
    		{
    			String ifStr =" and to_days("+aliasName+".`"+ fieldDefination.getFieldCode()+"`) = to_days(:obj."+StrOper.delLine(fieldDefination.getFieldCode())+")"; 
//    				" and TIMESTAMPDIFF(DAY,"+aliasName+".`"+ fieldDefination.getFieldCode()+"`,:obj."+StrOper.delLine(fieldDefination.getFieldCode())+")=0  ";
    			sb.append("public static final String "+getColumnWhereName(fieldDefination.getFieldName()) +"=\""+addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName(),ifStr)+"\";"+TWO_HC);
    		}
    		else{
    			sb.append("public static final String "+getColumnWhereName(fieldDefination.getFieldName()) +"=\""+addIf(fieldDefination.getFieldType(),fieldDefination.getFieldName()," and " +aliasName+".`"+ fieldDefination.getFieldCode()+"`=:obj."+StrOper.delLine(fieldDefination.getFieldCode()))+"\";"+TWO_HC);
    		}
		}
    	return sb.toString();
    }
    public String getColumnWhereName(String fileName){
    	return "WHERE_"+fileName.toUpperCase();
    }
    
}
