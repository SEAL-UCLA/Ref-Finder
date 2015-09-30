/*     */ package lsclipse.rules;
/*     */ 
/*     */ import lsclipse.RefactoringQuery;
/*     */ import lsclipse.utils.CodeCompare;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReplaceTypeCodeWithState
/*     */   implements Rule
/*     */ {
/*     */   private String name_;
/*     */   
/*     */   public ReplaceTypeCodeWithState()
/*     */   {
/*  22 */     this.name_ = "replace_type_code_with_state";
/*     */   }
/*     */   
/*     */   public String checkAdherence(ResultSet rs)
/*     */     throws TyrubaException
/*     */   {
/*  28 */     String fShortName1 = rs.getString("?fShortName1");
/*  29 */     String fShortName2 = rs.getString("?fShortName2");
/*  30 */     String tCodeShortName1 = rs.getString("?tCodeShortName1");
/*  31 */     String tCodeShortName2 = rs.getString("?tCodeShortName2");
/*  32 */     String tCodeShortName = rs.getString("?tCodeShortName");
/*  33 */     String tShortName = rs.getString("?tShortName");
/*     */     
/*  35 */     fShortName1 = fShortName1.toLowerCase();
/*  36 */     fShortName2 = fShortName2.toLowerCase();
/*  37 */     tCodeShortName1 = tCodeShortName1.toLowerCase();
/*  38 */     tCodeShortName2 = tCodeShortName2.toLowerCase();
/*  39 */     tCodeShortName = tCodeShortName.toLowerCase();
/*  40 */     tShortName = tShortName.toLowerCase();
/*     */     
/*  42 */     if (((CodeCompare.compare(fShortName1, tCodeShortName1)) && 
/*  43 */       (CodeCompare.compare(fShortName2, tCodeShortName2))) || (
/*  44 */       (CodeCompare.compare(fShortName1, tCodeShortName2)) && 
/*  45 */       (CodeCompare.compare(fShortName2, tCodeShortName1)) && 
/*  46 */       (CodeCompare.compare(tCodeShortName, tShortName)))) {
/*  47 */       String writeTo = getName() + "(\"" + rs.getString("?tFullName") + 
/*  48 */         "\",\"" + rs.getString("?tCodeFullName") + "\")";
/*  49 */       return writeTo;
/*     */     }
/*     */     
/*     */ 
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   private String getQueryString() {
/*  57 */     return "deleted_field(?old_fFullName1, ?fShortName1, ?tFullName), deleted_field(?old_fFullName2, ?fShortName2, ?tFullName), NOT(equals(?fShortName1, ?fShortName2)), before_fieldmodifier(?old_fFullName1, \"static\"), before_fieldmodifier(?old_fFullName2, \"static\"), before_fieldmodifier(?old_fFullName1, \"final\"), before_fieldmodifier(?old_fFullName2, \"final\"), modified_type(?tFullName, ?tShortName, ?), added_field(?new_fFullName1, ?fShortName1, ?tCodeFullName), added_field(?new_fFullName2, ?fShortName2, ?tCodeFullName), after_fieldmodifier(?new_fFullName1, \"static\"), after_fieldmodifier(?new_fFullName2, \"static\"), after_fieldmodifier(?new_fFullName1, \"final\"), after_fieldmodifier(?new_fFullName2, \"final\"), deleted_fieldoftype(?tCodefieldFullName, ?), added_fieldoftype(?tCodefieldFullName, ?tCodeFullName), added_type(?tCodeFullName, ?tCodeShortName, ?), added_type(?tCodeFullName1, ?tCodeShortName1, ?), added_type(?tCodeFullName2, ?tCodeShortName2, ?), added_subtype(?tCodeFullName, ?tCodeFullName1), added_subtype(?tCodeFullName, ?tCodeFullName2),NOT(equals(?tCodeShortName1, ?tCodeShortName2))";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  90 */     return this.name_;
/*     */   }
/*     */   
/*     */   public RefactoringQuery getRefactoringQuery()
/*     */   {
/*  95 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/*  96 */       getQueryString());
/*  97 */     return repl;
/*     */   }
/*     */   
/*     */   public String getRefactoringString()
/*     */   {
/* 102 */     return getName() + "(?tFullName, ?tCodeFullName)";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceTypeCodeWithState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */