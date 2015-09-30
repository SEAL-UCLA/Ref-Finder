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
/*    */ public class RemoveControlFlag
/*    */   implements Rule
/*    */ {
/*    */   private static final String CONDITION = "?condition";
/*    */   private static final String EXPRESSION = "?expression";
/*    */   private static final String IDENTIFIER = "?identifier";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private String name_;
/*    */   
/*    */   public RemoveControlFlag()
/*    */   {
/* 23 */     this.name_ = "remove_control_flag";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 28 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 33 */     return getName() + "(" + "?identifier" + "," + "?mFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 38 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 42 */     return "deleted_localvar(?mFullName,\"boolean\",?identifier,?expression),deleted_conditional(?condition,?,?,?mFullName),NOT(added_conditional(?condition,?,?,?mFullName))";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 50 */     String condition = rs.getString("?condition");
/* 51 */     String identifier = rs.getString("?identifier");
/*    */     
/* 53 */     if (!condition.contains(identifier)) {
/* 54 */       return null;
/*    */     }
/* 56 */     return 
/* 57 */       getName() + "(\"" + identifier + "\",\"" + rs.getString("?mFullName") + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/RemoveControlFlag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */