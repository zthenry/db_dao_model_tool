package com.henry.util;


import com.henry.metadata.EntityDefination;
import com.henry.metadata.FieldDefination;
import com.henry.tempalte.IEntityWriter;
import com.henry.tempalte.impl.EntityWriterImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author zhangtao13
 */
public class Director {

	//将数据库表的字段类型与Java中的类型对应起来，并加载到map中，生成对应实体类时用
	public static final Properties colMapper = MessageUtil.getMessage("mapping");

	public static final String PAGECONDITIONS = " #if(:page.orderBy){ order by ##(:page.orderBy) ##(:page.ascDesc) }  limit :page.startIndex , :page.countEachPage ";

	public static List<EntityDefination> fetchEntityDefination() throws SQLException {
		//配置文件加载到Map
		Set<String> tables = MessageUtil.getMessage("tableName").stringPropertyNames();
		String packagePath = MessageUtil.getMessage("tools").getProperty("packagePath");

		
//		RoseAppContext rose = new RoseAppContext();
		//数据库的数据源加载
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");

		DatabaseMetaData metadata = null;
		Connection connection = dataSource.getConnection();
		metadata = connection.getMetaData();
		
		List<EntityDefination> list = null;
		if ( null != metadata ) {
			list = new ArrayList<EntityDefination>();
			//遍历要生成DAO与Model的表
			for ( String table : tables ){
				ResultSet cloumnResultSet = metadata.getColumns(null, null,table, null);
				ResultSet keySet = metadata.getIndexInfo(null, null, table, false, false);
				List<String> keyList = new ArrayList<String>();
				while(keySet.next())
				{
					//如果是非主键，rs.getString("COLUMN_NAME")与rs.getString("INDEX_NAME")相同
					String indexNameValue = keySet.getString("INDEX_NAME");
					String PRIMARY = "PRIMARY";
					String columnNameValue = keySet.getString("COLUMN_NAME");

	                if(indexNameValue.equalsIgnoreCase(PRIMARY) && !columnNameValue.equalsIgnoreCase(indexNameValue)){
	                	keyList.add(columnNameValue);
	                }
				}
				EntityDefination entityDefination = new EntityDefination();
				entityDefination.setKeyList(keyList);
				entityDefination.setTableCode(table);
				entityDefination.setPackPath(packagePath);
				wrapBean(cloumnResultSet,keyList ,entityDefination);
				
				list.add(entityDefination);
				 
			}
		}

		connection.close();
		
		return list;
		
	}
	public static Enumeration<String> fetchTableName(String param){
		ResourceBundle rb = ResourceBundle.getBundle(param);
		return rb.getKeys();
	}
	
	public static String fetch(String param, String key){
		ResourceBundle rb = ResourceBundle.getBundle(param);
		return rb.getString(key);
	}

	/**
	 *
	 * @param cloumnResultSet
	 * @param keyList  主键列表
	 * @param entityDefination  实体类定义:包路径,主键,类所对应的表名
	 * @throws SQLException
	 */
	public static void wrapBean(ResultSet cloumnResultSet,List<String> keyList,EntityDefination entityDefination) throws SQLException{

		List<FieldDefination> list = new ArrayList<FieldDefination>();
		//遍历表的所有定义字段
		while (cloumnResultSet.next()) {
			FieldDefination fieldDefination = new FieldDefination();
      //字段Comment
			String remarks = cloumnResultSet.getString("REMARKS");
			String name = cloumnResultSet.getString("COLUMN_NAME");
			fieldDefination.setAutoIncreate(cloumnResultSet.getBoolean("IS_AUTOINCREMENT"));

			//驼峰命名方式
			fieldDefination.setFieldName(StrOper.delLine(name));

			//保存与数据表表字段一致
			fieldDefination.setFieldCode(name);

			String filedType = getFieldType( cloumnResultSet );
			fieldDefination.setFieldType(filedType);
			fieldDefination.setIsKey(false);
			fieldDefination.setFieldComment(remarks);
			for (String key : keyList) {
				//如果当前字段是Key
				if(key.equals(name))
				{
					fieldDefination.setIsKey(true);
				}
			}
			list.add(fieldDefination);
		}
		entityDefination.setFieldList(list);
	}
	
	private static String getFieldType( ResultSet cloumnResultSet ) throws SQLException {
		
		String typeName = cloumnResultSet.getString("TYPE_NAME");
		String type = (String)colMapper.get(typeName);
		
		if ( null == type ) {
			int size = cloumnResultSet.getInt("COLUMN_SIZE");
			int digits = cloumnResultSet.getInt("DECIMAL_DIGITS");
			
			typeName = typeName + "(" + size + "," + digits + ")";
			type = (String)colMapper.get(typeName);
		}
		
		return type;
	}

	/**
	 * 执行该类的main方法，生成model与DAO
	 * @param args
	 */
	public static void main( String[] args ) {
		List<EntityDefination> list = null;
		try {
			//连接数据库,读取配置在tableName.properties的表定义，生成对应的实体类定义
			list = fetchEntityDefination();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!CollectionUtils.isEmpty(list)) {
			String exportPath = fetch("tools", "exportPath");
			IEntityWriter iew = new EntityWriterImpl(list,exportPath);
			iew.initEntityWriter();
		}
		
	}
	
}
