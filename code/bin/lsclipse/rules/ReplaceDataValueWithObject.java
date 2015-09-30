/*    */ package lsclipse.rules;
/*    */ 
/*    */ import lsclipse.RefactoringQuery;
/*    */ import lsclipse.utils.CodeCompare;
/*    */ import tyRuBa.tdbc.ResultSet;
/*    */ import tyRuBa.tdbc.TyrubaException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReplaceDataValueWithObject
/*    */   implements Rule
/*    */ {
/*    */   private static final String NEWT_FULL_NAME = "?newtFullName";
/*    */   private static final String F_FULL_NAME = "?fFullName";
/*    */   private static final String NEWT_SHORT_NAME = "?newtShortName";
/*    */   private static final String F_SHORT_NAME = "?fShortName";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceDataValueWithObject()
/*    */   {
/* 24 */     this.name_ = "replace_data_with_object";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 29 */     return this.name_;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 33 */     return "before_field(?fFullName, ?fShortName, ?tFullName),deleted_fieldoftype(?fFullName,?),added_type(?newtFullName, ?newtShortName, ?),after_field(?newfFullName, ?, ?tFullName),added_fieldoftype(?newfFullName, ?newtFullName)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getRefactoringString()
/*    */   {
/* 43 */     return getName() + "(" + "?fFullName" + ", " + "?newtFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 48 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 49 */       getQueryString());
/* 50 */     return repl;
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 55 */     String fieldName = rs.getString("?fShortName");
/* 56 */     String typeName = rs.getString("?newtShortName");
/*    */     
/*    */ 
/* 59 */     if (CodeCompare.compare(fieldName, typeName))
/*    */     {
/* 61 */       String writeTo = getName() + "(\"" + rs.getString("?fFullName") + 
/* 62 */         "\",\"" + rs.getString("?newtFullName") + "\")";
/*    */       
/* 64 */       return writeTo;
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceDataValueWithObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */