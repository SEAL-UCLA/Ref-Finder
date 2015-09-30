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
/*    */ public class IntroduceNullObject
/*    */   implements Rule
/*    */ {
/*    */   private static final String NULLT_FULL_NAME = "?nulltFullName";
/*    */   private static final String SERVERT_FULL_NAME = "?servertFullName";
/*    */   private static final String SERVERM_FULL_NAME = "?servermFullName";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private static final String NULL_COND = "?nullCond";
/*    */   private String name_;
/*    */   
/*    */   public IntroduceNullObject()
/*    */   {
/* 24 */     this.name_ = "introduce_null_object";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 29 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 34 */     return 
/* 35 */       getName() + "(" + "?nulltFullName" + "," + "?servertFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 40 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 44 */     return "deleted_conditional(?nullCond, ?, ?, ?mFullName),NOT(added_conditional(?nullCond, ?, ?, ?mFullName)),before_calls(?mFullName, ?servermFullName),after_calls(?mFullName, ?servermFullName),after_method(?servermFullName, ?, ?servertFullName),added_type(?nulltFullName, ?, ?),added_subtype(?servertFullName, ?nulltFullName)";
/*    */   }
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
/* 57 */     if (!rs.getString("?nullCond").contains("==null"))
/* 58 */       return null;
/* 59 */     return 
/* 60 */       getName() + "(\"" + rs.getString("?nulltFullName") + "\",\"" + rs.getString("?servertFullName") + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/IntroduceNullObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */