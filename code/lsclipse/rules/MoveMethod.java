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
/*    */ public class MoveMethod
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   private static final String PACKAGE = "?package";
/*    */   private static final String T2_FULL_NAME = "?t2FullName";
/*    */   private static final String T1_FULL_NAME = "?t1FullName";
/*    */   private static final String M_SHORT_NAME = "?mShortName";
/*    */   public static final String M2_BODY = "?m2Body";
/*    */   public static final String M1_BODY = "?m1Body";
/*    */   public static final String M1_FULL_NAME = "?m1FullName";
/*    */   public static final String M2_FULL_NAME = "?m2FullName";
/*    */   
/*    */   public MoveMethod()
/*    */   {
/* 31 */     this.name_ = "move_method";
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 36 */     return 
/* 37 */       getName() + "(" + "?mShortName" + "," + "?t1FullName" + "," + "?t2FullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 42 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private static String getQueryString() {
/* 46 */     return "deleted_method(?m1FullName, ?mShortName, ?t1FullName), added_method(?m2FullName, ?mShortName, ?t2FullName), before_type(?t1FullName, ?, ?package), after_type(?t2FullName, ?, ?package), NOT(equals(?t1FullName, ?t2FullName)), added_methodbody(?m2FullName,?m2Body), deleted_methodbody(?m1FullName,?m1Body),NOT(equals(?m2FullName,?m1FullName))";
/*    */   }
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
/* 59 */     String mShortName = rs.getString("?mShortName");
/* 60 */     if (mShortName.equals("<init>()")) {
/* 61 */       return null;
/*    */     }
/* 63 */     String newmBody_str = rs.getString("?m2Body");
/* 64 */     String mBody_str = rs.getString("?m1Body");
/*    */     
/* 66 */     if ((newmBody_str.length() > 1) && 
/* 67 */       (CodeCompare.compare(newmBody_str, mBody_str)))
/*    */     {
/* 69 */       String writeTo = getName() + "(\"" + rs.getString("?mShortName") + 
/* 70 */         "\",\"" + rs.getString("?t1FullName") + "\",\"" + 
/* 71 */         rs.getString("?t2FullName") + "\")";
/*    */       
/* 73 */       return writeTo;
/*    */     }
/* 75 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 81 */     return this.name_;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/MoveMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */