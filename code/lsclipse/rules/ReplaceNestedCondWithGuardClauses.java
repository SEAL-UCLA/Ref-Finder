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
/*    */ public class ReplaceNestedCondWithGuardClauses
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public ReplaceNestedCondWithGuardClauses()
/*    */   {
/* 22 */     this.name_ = "replace_nested_cond_guard_clauses";
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 26 */     return "deleted_conditional(?old_cond1, ?old_ifPart1, ?old_elsePart1, ?mFullName), added_conditional(?new_cond1, ?new_ifPart1, \"\", ?mFullName), added_conditional(?new_cond2, ?new_ifPart2, \"\", ?mFullName)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 36 */     String old_cond1 = rs.getString("?old_cond1");
/* 37 */     String new_cond1 = rs.getString("?new_cond1");
/* 38 */     String new_cond2 = rs.getString("?new_cond2");
/* 39 */     String old_ifPart1 = rs.getString("?old_ifPart1");
/* 40 */     String new_ifPart1 = rs.getString("?new_ifPart1");
/* 41 */     String new_ifPart2 = rs.getString("?new_ifPart2");
/* 42 */     String old_elsePart1 = rs.getString("?old_elsePart1");
/*    */     
/* 44 */     if ((CodeCompare.compare(old_cond1, new_cond1)) && 
/* 45 */       (CodeCompare.compare(old_ifPart1, new_ifPart1)) && 
/* 46 */       (CodeCompare.compare(new_cond2, old_elsePart1)) && 
/* 47 */       (CodeCompare.compare(new_ifPart2, old_elsePart1)))
/*    */     {
/* 49 */       String writeTo = getName() + "(\"" + rs.getString("?mFullName") + 
/* 50 */         "\")";
/*    */       
/* 52 */       return writeTo;
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 59 */     return this.name_;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 64 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 65 */       getQueryString());
/* 66 */     return repl;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 71 */     return getName() + "(?mFullName)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/ReplaceNestedCondWithGuardClauses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */