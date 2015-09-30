/*    */ package lsclipse.rules;
/*    */ 
/*    */ import lsclipse.RefactoringQuery;
/*    */ import tyRuBa.tdbc.ResultSet;
/*    */ import tyRuBa.tdbc.TyrubaException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreserveWholeObject
/*    */   implements Rule
/*    */ {
/*    */   private static final String OLD_PARAM_NAME = "?oldParamName";
/*    */   private static final String OBJ_PARAM_NAME = "?objParamName";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private static final String OBJT_FULL_NAME = "?objtFullName";
/*    */   private static final String OBJM_FULL_NAME = "?objmFullName";
/*    */   private static final String CLIENTM_FULL_NAME = "?clientmFullName";
/*    */   private String name_;
/*    */   
/*    */   public PreserveWholeObject()
/*    */   {
/* 25 */     this.name_ = "preserve_whole_object";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 30 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 35 */     return getName() + "(" + "?mFullName" + "," + "?tParamShortName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 40 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 44 */     return "deleted_calls(?clientmFullName,?objmFullName),after_method(?objmFullName,?,?objtFullName),before_calls(?clientmFullName,?mFullName),after_calls(?clientmFullName,?mFullName),added_calls(?mFullName,?objmFullName),added_parameter(?mFullName,?,?objParamName),deleted_parameter(?mFullName,?,?oldParamName)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 56 */     String objParamName = rs.getString("?objParamName");
/* 57 */     String oldParamName = rs.getString("?oldParamName");
/* 58 */     String objType = rs.getString("?objtFullName");
/*    */     
/* 60 */     String objParamShortType = objParamName.substring(0, objParamName.indexOf(':'));
/* 61 */     String oldParamShortType = oldParamName.substring(0, oldParamName.indexOf(':'));
/*    */     
/*    */ 
/* 64 */     if ((objType.contains(oldParamShortType)) || (!objType.contains(objParamShortType))) {
/* 65 */       return null;
/*    */     }
/* 67 */     return getName() + "(\"" + rs.getString("?mFullName") + "\",\"" + objParamShortType + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/PreserveWholeObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */