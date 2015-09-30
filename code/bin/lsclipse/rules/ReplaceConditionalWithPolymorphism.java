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
/*    */ public class ReplaceConditionalWithPolymorphism
/*    */   implements Rule
/*    */ {
/*    */   private static final String SUBM_FULL_NAME = "?newmFullName";
/*    */   private static final String SUBT_FULL_NAME = "?subtFullName";
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   private static final String M_SHORT_NAME = "?mShortName";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private static final String CONDITION = "?condition";
/*    */   private static final String NEWM_BODY = "?newmBody";
/*    */   private static final String IF_PART = "?ifPart";
/*    */   private static final String F_FULL_NAME = "?fFullName";
/*    */   private static final String TYPET_FULL_NAME = "?typeTFullName";
/*    */   private static final String TYPEM_FULL_NAME = "?typeMFullName";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceConditionalWithPolymorphism()
/*    */   {
/* 31 */     this.name_ = "replace_conditional_with_polymorphism";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 36 */     return this.name_;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getRefactoringString()
/*    */   {
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 47 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 51 */     return "deleted_conditional(?condition,?ifPart,?,?mFullName),before_method(?mFullName,?mShortName,?tFullName),(after_subtype(?tFullName,?subtFullName);(after_field(?fFullName,?,?tFullName),after_fieldoftype(?fFullName,?typeTFullName),after_subtype(?typeTFullName,?subtFullName),added_method(?typeMFullName,?mShortName,?typeTFullName),added_calls(?mFullName,?typeMFullName))),added_method(?newmFullName,?mShortName,?subtFullName),added_methodbody(?newmFullName,?newmBody)";
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
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 68 */     String cond = rs.getString("?condition").toLowerCase();
/*    */     
/* 70 */     if ((!cond.toLowerCase().contains("type")) && 
/* 71 */       (!cond.contains("instanceof"))) {
/* 72 */       return null;
/*    */     }
/* 74 */     String newmBody_str = rs.getString("?newmBody");
/* 75 */     String ifPart_str = rs.getString("?ifPart");
/*    */     
/* 77 */     if ((newmBody_str.length() > 1) && 
/* 78 */       (CodeCompare.compare(newmBody_str, ifPart_str)))
/*    */     {
/* 80 */       return 
/* 81 */         getName() + "(\"" + rs.getString("?mFullName") + "\",\"" + rs.getString("?subtFullName") + "\")";
/*    */     }
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceConditionalWithPolymorphism.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */