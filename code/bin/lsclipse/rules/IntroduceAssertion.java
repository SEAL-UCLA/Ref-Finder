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
/*    */ public class IntroduceAssertion
/*    */   implements Rule
/*    */ {
/*    */   private static final String NEWM_BODY = "?newmBody";
/*    */   private static final String OLDM_BODY = "?oldmBody";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private String name_;
/*    */   
/*    */   public IntroduceAssertion()
/*    */   {
/* 22 */     this.name_ = "introduce_assertion";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 27 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 32 */     return getName() + "(" + "?mFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 37 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 41 */     return "deleted_methodbody(?mFullName,?oldmBody),added_methodbody(?mFullName,?newmBody)";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 47 */     String oldmBody = rs.getString("?oldmBody");
/* 48 */     String newmBody = rs.getString("?newmBody");
/* 49 */     int oldNumAsserts = oldmBody.split("assert").length - 1;
/* 50 */     int newNumAsserts = newmBody.split("assert").length - 1;
/*    */     
/* 52 */     if (newNumAsserts > oldNumAsserts) {
/* 53 */       return getName() + "(\"" + rs.getString("?mFullName") + "\")";
/*    */     }
/*    */     
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/IntroduceAssertion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */