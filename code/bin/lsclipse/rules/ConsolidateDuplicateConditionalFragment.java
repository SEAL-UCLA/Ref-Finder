/*    */ package lsclipse.rules;
/*    */ 
/*    */ import lsclipse.LCS;
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
/*    */ public class ConsolidateDuplicateConditionalFragment
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public ConsolidateDuplicateConditionalFragment()
/*    */   {
/* 22 */     this.name_ = "consolidate_duplicate_cond_fragments";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 27 */     String old_elsePart = rs.getString("?old_elsePart");
/* 28 */     String new_elsePart = rs.getString("?new_elsePart");
/* 29 */     String old_ifPart = rs.getString("?old_ifPart");
/* 30 */     String new_ifPart = rs.getString("?new_ifPart");
/* 31 */     String body = rs.getString("?mbody");
/*    */     
/* 33 */     if (old_elsePart.equals("")) return null;
/* 34 */     if ((similar_fragments(old_elsePart, new_elsePart, body)) && 
/* 35 */       (similar_fragments(old_ifPart, new_ifPart, body))) {
/* 36 */       String writeTo = getName() + "(\"" + rs.getString("?mFullName") + 
/* 37 */         "\")";
/* 38 */       return writeTo;
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 44 */     return "deleted_conditional(?cond, ?old_ifPart, ?old_elsePart, ?mFullName), added_conditional(?cond, ?new_ifPart, ?new_elsePart, ?mFullName), after_methodbody(?mFullName, ?mbody)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean similar_fragments(String old, String news, String body)
/*    */   {
/* 52 */     String lcs = LCS.getLCS(old, news);
/*    */     
/* 54 */     int index = old.indexOf(lcs);
/*    */     
/* 56 */     if (index == -1) { return false;
/*    */     }
/* 58 */     String prefix = old.substring(0, index);
/* 59 */     String suffix = old.substring(index + lcs.length());
/*    */     
/* 61 */     if ((body.contains(prefix)) && (body.contains(suffix))) {
/* 62 */       return true;
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 69 */     return this.name_;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 74 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 75 */       getQueryString());
/* 76 */     return repl;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 81 */     return getName() + "(?mFullName)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ConsolidateDuplicateConditionalFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */