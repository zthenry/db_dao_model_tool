package com.henry.tempalte.impl;


import com.henry.metadata.EntityDefination;
import com.henry.tempalte.IEntityWriter;
import com.henry.tempalte.ISysCharset;
import com.henry.tempalte.ITemplet;
import com.henry.util.StrOper;
import com.henry.util.XmlCreater;

import java.util.List;

public class EntityWriterImpl implements IEntityWriter,ISysCharset {

    /**
     * @param entityList 实体类数据
     * @param exportPath 输入路径
     */
    public EntityWriterImpl(List<EntityDefination> entityList, String exportPath){
        this.entityList = entityList;
        this.exportPath = exportPath;
    }
    
    private String exportPath;
    private List<EntityDefination> entityList;
    
    /**
     * 创建实体类
     */
    public void initEntityWriter(){
        for (EntityDefination entityDefination : entityList) {
            ITemplet bean = new EntityTempletImpl(entityDefination);
            ITemplet dao = new DaoTempletImpl(entityDefination);

            //创建对应的.java文件
            createEntity(bean.templetContent(), entityDefination.getTableCode(), PACK_ENTITY_CLASS_POSTFIX, PACK_ENTITY);
            createEntity(dao.templetContent(), entityDefination.getTableCode(), PACK_DAO_CLASS_POSTFIX, PACK_DAO);
        }
    }
    
    /**
     * 创建类
     * @param entityContent 类文件的具体代码:package,import,注释,字段,方法
     * @param
     * @param
     */
    public void createEntity(String entityContent, String tableCode, String postfix, String packPath){

        String javaFileName = StrOper.upShouAndDelLine(tableCode)+postfix+".java";
        //文件存放路径
        String javaFilePath = exportPath + "/" + packPath;
        XmlCreater.CreatFile(javaFilePath, javaFileName);
        
        String exPath = javaFilePath + "/" +javaFileName;
        XmlCreater.appendMethodA(exPath, StrOper.changeContentChar(entityContent, oldChar, newChar));
    }
    
    public void createSQL(String entityCon, String tableCode, String folderPath) {
    	String fileName = StrOper.delLine(tableCode)+".sql";
        String filePath = exportPath + "/" + folderPath;
        XmlCreater.CreatFile(filePath, fileName);
        String exPath = filePath + "/" +fileName;
        XmlCreater.appendMethodA(exPath, StrOper.changeContentChar(entityCon, oldChar, newChar));
    }
}
