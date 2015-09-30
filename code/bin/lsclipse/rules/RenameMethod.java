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
/*    */ 
/*    */ public class RenameMethod
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   private static final String M1_SHORT_NAME = "?m1ShortName";
/*    */   private static final String M2_SHORT_NAME = "?m2ShortName";
/*    */   public static final String M2_BODY = "?m2Body";
/*    */   public static final String M1_BODY = "?m1Body";
/*    */   public static final String M1_FULL_NAME = "?m1FullName";
/*    */   public static final String M2_FULL_NAME = "?m2FullName";
/*    */   
/*    */   public RenameMethod()
/*    */   {
/* 30 */     this.name_ = "rename_method";
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 35 */     return 
/* 36 */       getName() + "(" + "?m1FullName" + "," + "?m2FullName" + "," + "?tFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 41 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private static String getQueryString() {
/* 45 */     return "deleted_method(?m1FullName, ?m1ShortName, ?tFullName), added_method(?m2FullName, ?m2ShortName, ?tFullName), NOT(equals(?m1ShortName, ?m2ShortName)), added_methodbody(?m2FullName,?m2Body), deleted_methodbody(?m1FullName,?m1Body)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 55 */     String m1Body_str = rs.getString("?m1Body");
/* 56 */     String m2Body_str = rs.getString("?m2Body");
/*    */     
/* 58 */     if ((m2Body_str.length() > 1) && 
/* 59 */       (CodeCompare.compare(m1Body_str, m2Body_str)))
/*    */     {
/* 61 */       String writeTo = getName() + "(\"" + rs.getString("?m1FullName") + 
/* 62 */         "\",\"" + rs.getString("?m2FullName") + "\",\"" + 
/* 63 */         rs.getString("?tFullName") + "\")";
/*    */       
/* 65 */       return writeTo;
/*    */     }
/* 67 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 73 */     return this.name_;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/RenameMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */