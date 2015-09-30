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
/*    */ public class ReplaceArrayWithObject
/*    */   implements Rule
/*    */ {
/*    */   private static final String NEWT_FULL_NAME = "?newtFullName";
/*    */   private static final String F_FULL_NAME = "?fFullName";
/*    */   private static final String OLDT_FULL_NAME = "?oldtFullName";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceArrayWithObject()
/*    */   {
/* 22 */     this.name_ = "replace_array_with_object";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 27 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 32 */     return getName() + "(" + "?fFullName" + "," + "?newtFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 37 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 38 */       getQueryString());
/* 39 */     return repl;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 43 */     return "deleted_fieldoftype(?fFullName, ?oldtFullName),added_fieldoftype(?fFullName, ?newtFullName),added_type(?newtFullName, ?, ?)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 51 */     String oldType = rs.getString("?oldtFullName");
/* 52 */     if (oldType.endsWith("[]")) {
/* 53 */       String writeTo = getName() + "(\"" + rs.getString("?fFullName") + 
/* 54 */         "\",\"" + rs.getString("?newtFullName") + "\")";
/*    */       
/* 56 */       return writeTo;
/*    */     }
/* 58 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceArrayWithObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */