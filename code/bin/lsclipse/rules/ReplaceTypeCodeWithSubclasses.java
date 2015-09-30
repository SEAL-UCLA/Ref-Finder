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
/*    */ public class ReplaceTypeCodeWithSubclasses
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public ReplaceTypeCodeWithSubclasses()
/*    */   {
/* 22 */     this.name_ = "replace_type_code_with_subclasses";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 27 */     String fShortName1 = rs.getString("?fShortName1");
/* 28 */     String fShortName2 = rs.getString("?fShortName2");
/* 29 */     String tCodeShortName1 = rs.getString("?tCodeShortName1");
/* 30 */     String tCodeShortName2 = rs.getString("?tCodeShortName2");
/*    */     
/* 32 */     fShortName1 = fShortName1.toLowerCase();
/* 33 */     fShortName2 = fShortName2.toLowerCase();
/* 34 */     tCodeShortName1 = tCodeShortName1.toLowerCase();
/* 35 */     tCodeShortName2 = tCodeShortName2.toLowerCase();
/*    */     
/* 37 */     if (((CodeCompare.compare(fShortName1, tCodeShortName1)) && 
/* 38 */       (CodeCompare.compare(fShortName2, tCodeShortName2))) || (
/* 39 */       (CodeCompare.compare(fShortName1, tCodeShortName2)) && 
/* 40 */       (CodeCompare.compare(fShortName2, tCodeShortName1)))) {
/* 41 */       String writeTo = getName() + "(\"" + rs.getString("?tFullName") + 
/* 42 */         "\")";
/* 43 */       return writeTo;
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 49 */     return "before_field(?fFullName1, ?fShortName1, ?tFullName), before_field(?fFullName2, ?fShortName2, ?tFullName), NOT(equals(?fFullName1, ?fFullName2)), before_fieldmodifier(?fFullName1, \"static\"), before_fieldmodifier(?fFullName2, \"static\"), before_fieldmodifier(?fFullName1, \"final\"), before_fieldmodifier(?fFullName2, \"final\"), deleted_field(?tCodefFullName, ?, ?tFullName), added_type(?tCodeFullName1, ?tCodeShortName1, ?), added_type(?tCodeFullName2, ?tCodeShortName2, ?), NOT(equals(?tCodeFullName1, ?tCodeFullName2)),added_subtype(?tFullName, ?tCodeFullName1), added_subtype(?tFullName, ?tCodeFullName2)";
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
/*    */   public String getName()
/*    */   {
/* 66 */     return this.name_;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 71 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 72 */       getQueryString());
/* 73 */     return repl;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 78 */     return getName() + "(?tFullName)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceTypeCodeWithSubclasses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */