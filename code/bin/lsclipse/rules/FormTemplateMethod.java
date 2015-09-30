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
/*     */ public class FormTemplateMethod
/*     */   implements Rule
/*     */ {
/*     */   private String name_;
/*     */   
/*     */   public FormTemplateMethod()
/*     */   {
/*  22 */     this.name_ = "form_template_method";
/*     */   }
/*     */   
/*     */   private String getQueryString() {
/*  26 */     return "added_methodbody(?calleeMFullName1, ?new_mbody1), deleted_methodbody(?mFullName1, ?mbody1), NOT(equals(?calleeMFullName1, ?mFullName1)), added_methodbody(?calleeMFullName2, ?new_mbody2), deleted_methodbody(?mFullName2, ?mbody2), NOT(equals(?calleeMFullName2, ?mFullName2)), NOT(equals(?mFullName1, ?mFullName2)),NOT(equals(?sub_tFullName1, ?sub_tFullName2)),added_method(?mFullName, ?mShortName, ?super_tFullName), deleted_method(?mFullName1, ?mShortName, ?sub_tFullName1), deleted_method(?mFullName2, ?mShortName, ?sub_tFullName2), added_calls(?mFullName, ?calleeMFullName), added_inheritedmethod(?calleemShortName, ?super_tFullName, ?sub_tFullName1), added_inheritedmethod(?calleemShortName, ?super_tFullName, ?sub_tFullName2), added_method(?calleeMFullName1, ?calleemShortName, ?sub_tFullName1), added_method(?calleeMFullName2, ?calleemShortName, ?sub_tFullName2), after_method(?calleeMFullName, ?calleemShortName, ?super_tFullName), after_subtype(?super_tFullName, ?sub_tFullName1), after_subtype(?super_tFullName, ?sub_tFullName2) ";
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
/*     */ 
/*     */ 
/*     */   public String checkAdherence(ResultSet rs)
/*     */     throws TyrubaException
/*     */   {
/*  62 */     String sub1 = rs.getString("?sub_tFullName1");
/*  63 */     String sub2 = rs.getString("?sub_tFullName2");
/*  64 */     String new_mbody1 = rs.getString("?new_mbody1");
/*  65 */     String new_mbody2 = rs.getString("?new_mbody2");
/*  66 */     String mbody1 = rs.getString("?mbody1");
/*  67 */     String mbody2 = rs.getString("?mbody2");
/*     */     
/*     */ 
/*  70 */     if ((CodeCompare.compare(new_mbody1, mbody1)) && 
/*  71 */       (CodeCompare.compare(new_mbody2, mbody2))) { String second;
/*     */       String first;
/*  73 */       String second; if (sub1.compareTo(sub2) < 0) {
/*  74 */         String first = sub1;
/*  75 */         second = sub2;
/*     */       } else {
/*  77 */         first = sub2;
/*  78 */         second = sub1;
/*     */       }
/*     */       
/*  81 */       String writeTo = getName() + "(\"" + 
/*  82 */         rs.getString("?super_tFullName") + "\",\"" + first + 
/*  83 */         "\",\"" + second + "\",\"" + rs.getString("?mFullName") + 
/*  84 */         "\")";
/*  85 */       return writeTo;
/*     */     }
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  93 */     return this.name_;
/*     */   }
/*     */   
/*     */   public RefactoringQuery getRefactoringQuery()
/*     */   {
/*  98 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/*  99 */       getQueryString());
/* 100 */     return repl;
/*     */   }
/*     */   
/*     */   public String getRefactoringString()
/*     */   {
/* 105 */     return 
/* 106 */       getName() + "(?super_tFullName, ?sub_tFullName1, ?sub_tFullName2, ?mFullName)";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/FormTemplateMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */