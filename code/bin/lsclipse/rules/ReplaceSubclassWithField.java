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
/*    */ public class ReplaceSubclassWithField
/*    */   implements Rule
/*    */ {
/*    */   private static final String F_SHORT_NAME = "?fShortName";
/*    */   private static final String SUBT_FULL_NAME = "?subtFullName";
/*    */   private static final String SUPERT_FULL_NAME = "?supertFullName";
/*    */   private static final String M_FACT_FULL_NAME = "?mFactFullName";
/*    */   private static final String SUBT_SHORT_NAME = "?subtShortName";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceSubclassWithField()
/*    */   {
/* 25 */     this.name_ = "replace_subclass_with_field";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 30 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 35 */     return 
/* 36 */       getName() + "(" + "?supertFullName" + "," + "?subtShortName" + "," + "?fShortName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 41 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 45 */     return "replace_constructor_with_factory_method(?, ?mFactFullName),after_method(?mFactFullName, ?, ?supertFullName),deleted_subtype(?supertFullName, ?subtFullName),added_field(?, ?fShortName, ?supertFullName),before_type(?subtFullName,?subtShortName,?)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 55 */     String fName = rs.getString("?fShortName");
/* 56 */     String subtName = rs.getString("?subtShortName");
/*    */     
/* 58 */     if (!CodeCompare.compare(fName, subtName)) {
/* 59 */       return null;
/*    */     }
/* 61 */     return 
/* 62 */       getName() + "(\"" + rs.getString("?supertFullName") + "\",\"" + subtName + "\",\"" + fName + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceSubclassWithField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */