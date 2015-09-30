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
/*    */ 
/*    */ 
/*    */ public class ConsolidateConditionalExpression
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public ConsolidateConditionalExpression()
/*    */   {
/* 21 */     this.name_ = "consolidate_cond_expression";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 26 */     String old_cond1 = rs.getString("?old_cond1");
/* 27 */     String old_cond2 = rs.getString("?old_cond2");
/* 28 */     String new_cond = rs.getString("?new_cond");
/* 29 */     String extMthdBody = "";
/* 30 */     boolean foundExtract = true;
/*    */     try {
/* 32 */       extMthdBody = rs.getString("?extMthdBody");
/*    */     }
/*    */     catch (NullPointerException localNullPointerException) {
/* 35 */       foundExtract = false;
/*    */     }
/*    */     
/* 38 */     if (foundExtract) {
/* 39 */       if ((!extMthdBody.contains(old_cond1)) || 
/* 40 */         (!extMthdBody.contains(old_cond2))) return null;
/*    */     }
/* 42 */     else if ((!new_cond.contains(old_cond1)) || 
/* 43 */       (!new_cond.contains(old_cond2))) { return null;
/*    */     }
/* 45 */     String writeTo = getName() + "(\"" + rs.getString("?mFullName") + 
/* 46 */       "\")";
/*    */     
/* 48 */     return writeTo;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 52 */     return "(deleted_conditional(?old_cond1, ?ifPart, ?elsePart, ?mFullName), deleted_conditional(?old_cond2, ?ifPart, ?elsePart, ?mFullName), added_conditional(?new_cond, ?ifPart, ?elsePart, ?mFullName), extract_method(?mFullName, ?, ?extMthdBody, ?)); (deleted_conditional(?old_cond1, ?ifPart, ?elsePart, ?mFullName), deleted_conditional(?old_cond2, ?ifPart, ?elsePart, ?mFullName), added_conditional(?new_cond, ?ifPart, ?elsePart, ?mFullName))";
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 70 */     return this.name_;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 75 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 76 */       getQueryString());
/* 77 */     return repl;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 82 */     return getName() + "(?mFullName)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/ConsolidateConditionalExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */