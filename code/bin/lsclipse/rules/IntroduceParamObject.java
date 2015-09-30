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
/*    */ public class IntroduceParamObject
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public IntroduceParamObject()
/*    */   {
/* 20 */     this.name_ = "introduce_parameter_object";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 25 */     String new_param = getTypeofFieldfromParam(rs.getString("?new_param"));
/* 26 */     String old_param1 = getTypeofFieldfromParam(rs.getString("?old_param1"));
/* 27 */     String old_param2 = getTypeofFieldfromParam(rs.getString("?old_param2"));
/* 28 */     String fType1 = getTypeofFieldfromFOT(rs.getString("?fType1"));
/* 29 */     String fType2 = getTypeofFieldfromFOT(rs.getString("?fType2"));
/* 30 */     String tShortName = rs.getString("?tShortName");
/* 31 */     if (((old_param1.equals(fType1)) && 
/* 32 */       (old_param2.equals(fType2))) || (
/* 33 */       (old_param1.equals(fType2)) && 
/* 34 */       (old_param2.equals(fType1)) && 
/* 35 */       (new_param.equals(tShortName)))) {
/* 36 */       String writeTo = getName() + "(\"" + rs.getString("?mFullName") + 
/* 37 */         "\",\"" + rs.getString("?tFullName") + "\")";
/* 38 */       return writeTo;
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   private static String getTypeofFieldfromParam(String arg) {
/* 44 */     return arg.substring(0, arg.indexOf(':'));
/*    */   }
/*    */   
/*    */   private static String getTypeofFieldfromFOT(String arg) {
/* 48 */     if (arg.indexOf('.') == -1) {
/* 49 */       return arg;
/*    */     }
/* 51 */     return arg.substring(arg.indexOf('.') + 1);
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 56 */     return this.name_;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 61 */     RefactoringQuery repl = new RefactoringQuery(getName(), 
/* 62 */       getQueryString());
/* 63 */     return repl;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 67 */     return "deleted_parameter(?mFullName,?, ?old_param1), deleted_parameter(?mFullName,?, ?old_param2), NOT(equals(?old_param1, ?old_param2)),added_parameter(?mFullName,?, ?new_param), added_type(?tFullName, ?tShortName, ?), added_field(?fFullName1, ?, ?tFullName), added_fieldoftype(?fFullName1, ?fType1), added_field(?fFullName2, ?, ?tFullName), added_fieldoftype(?fFullName2, ?fType2), NOT(equals(?fFullName1, ?fFullName2))";
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
/*    */   public String getRefactoringString()
/*    */   {
/* 81 */     return getName() + "(?mFullName, ?tFullName)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/IntroduceParamObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */