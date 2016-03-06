package com.henry.metadata;

/**
 * 实体类属性
 */
public class FieldDefination {

  /** 字段 注释 */
  private String fieldComment;

  /** 字段 */
  private String fieldCode;

  /** 字段名称 */
  private String fieldName;

  /** 字段类型 */
  private String fieldType;

  /* 是否自增长 */
  private Boolean autoIncreate;

  /* 是否是主键 */
  private Boolean isKey;

  public String getFieldCode() {
    return fieldCode;
  }

  public void setFieldCode(String fieldCode) {
    this.fieldCode = fieldCode;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public Boolean getAutoIncreate() {
    return autoIncreate;
  }

  public void setAutoIncreate(Boolean autoIncreate) {
    this.autoIncreate = autoIncreate;
  }

  public Boolean getIsKey() {
    return isKey;
  }

  public void setIsKey(Boolean isKey) {
    this.isKey = isKey;
  }

  public String getFieldComment() {
    return fieldComment;
  }

  public void setFieldComment(String fieldComment) {
    this.fieldComment = fieldComment;
  }

}
