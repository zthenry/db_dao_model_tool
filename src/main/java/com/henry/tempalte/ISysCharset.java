package com.henry.tempalte;


public interface ISysCharset {

    public static String notic = "此类代码自动生成,请继承该DAO,防止代码需要重新生成的时候覆盖自定义的方法";
    
    public static String oldChar = "UTF-8";
    public static String newChar = "ISO8859-1";
    
    public static String ONE_HC = "\n";
    public static String TWO_HC = ONE_HC+ONE_HC;
    public static String ONE_TAB = "\t";
    public static String TWO_TAB = ONE_TAB+ONE_TAB;
    
    /** bean包名 */
    public static String PACK_ENTITY = "model";
    /** bean类名后缀 */
    public static String PACK_ENTITY_CLASS_POSTFIX = "";
    /** dao包名 */
    public static String PACK_DAO = "dao";
    /** dao包名 */
    public static String FOLDER_SQL = "sql";
    /** dao类名后缀 */
    public static String PACK_DAO_CLASS_POSTFIX = "DAO";

    public static String DAO_PROVIDER_CLASS_POSTFIX = "Provider";

    /** biz包名 */
    public static String PACK_BIZ = "biz";
    /** biz类名后缀 */
    public static String PACK_BIZ_CLASS_POSTFIX = "Biz";
    /** bizImpl包名 */
    public static String PACK_BIZIMPL = "bizImpl";
    /** bizImpl类名后缀 */
    public static String PACK_BIZIMPL_CLASS_POSTFIX = "BizImpl";
    /** service包名 */
    public static String PACK_SERVICE = "service";
    /** service类名后缀 */
    public static String PACK_SERVICE_CLASS_POSTFIX = "Service";
    /** bizImpl包名 */
    public static String PACK_SERVICEIMPL = "serviceImpl";
    /** bizImpl类名后缀 */
    public static String PACK_SERVICEIMPL_CLASS_POSTFIX = "ServiceImpl";
    
}
