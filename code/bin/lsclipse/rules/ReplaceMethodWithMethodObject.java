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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReplaceMethodWithMethodObject
/*    */   implements Rule
/*    */ {
/*    */   private static final String CALLINGT_FULL_NAME = "?callingtFullName";
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   public static final String NEWM_BODY = "?newmBody";
/*    */   public static final String M_BODY = "?mBody";
/*    */   public static final String M_FULL_NAME = "?mFullName";
/*    */   public static final String NEWM_FULL_NAME = "?newmFullName";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceMethodWithMethodObject()
/*    */   {
/* 29 */     this.name_ = "replace_method_with_method_object";
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 34 */     return getName() + "(" + "?mFullName" + "," + "?tFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 39 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private static String getQueryString()
/*    */   {
/* 44 */     return "added_type(?tFullName, ?tShortName, ?), added_method(?newmFullName, ?, ?tFullName),added_calls(?mFullName, ?newmFullName),after_method(?mFullName, ?, ?callingtFullName), added_methodbody(?newmFullName,?newmBody), deleted_methodbody(?mFullName,?mBody)";
/*    */   }
/*    */   
/*    */ 
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
/* 58 */     String newmBody_str = rs.getString("?newmBody");
/* 59 */     String mBody_str = rs.getString("?mBody");
/*    */     
/* 61 */     if ((newmBody_str.length() > 1) && 
/* 62 */       (CodeCompare.compare(newmBody_str, mBody_str)))
/*    */     {
/* 64 */       String writeTo = getName() + "(\"" + rs.getString("?mFullName") + 
/* 65 */         "\",\"" + rs.getString("?tFullName") + "\")";
/*    */       
/* 67 */       return writeTo;
/*    */     }
/* 69 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 75 */     return this.name_;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceMethodWithMethodObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */