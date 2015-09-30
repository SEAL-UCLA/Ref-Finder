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
/*    */ public class ParameterizeMethod
/*    */   implements Rule
/*    */ {
/*    */   private static final String M2_FULL_NAME = "?m2FullName";
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   private static final String M1_FULL_NAME = "?m1FullName";
/*    */   private static final String NEWPARAMS = "?newparams";
/*    */   private static final String PARAMS2 = "?params2";
/*    */   private static final String PARAMS1 = "?params1";
/*    */   private static final String NEWM_SHORT_NAME = "?newmShortName";
/*    */   private static final String M2_SHORT_NAME = "?m2ShortName";
/*    */   private static final String M1_SHORT_NAME = "?m1ShortName";
/*    */   private static final String NEWM_FULL_NAME = "?newmFullName";
/*    */   private String name_;
/*    */   
/*    */   public ParameterizeMethod()
/*    */   {
/* 30 */     this.name_ = "parameterize_method";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 35 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 40 */     return getName() + "(" + "?newmFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 45 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 49 */     return "deleted_method(?m1FullName,?m1ShortName,?tFullName),before_parameter(?m1FullName,?params1,?),deleted_method(?m2FullName,?m2ShortName,?tFullName),before_parameter(?m2FullName,?params2,?),NOT(equals(?m1ShortName,?m2ShortName)),added_method(?newmFullName,?newmShortName,?tFullName),after_parameter(?newmFullName,?newparams,?)";
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
/* 62 */     String m1ShortName = rs.getString("?m1ShortName");
/* 63 */     String m2ShortName = rs.getString("?m2ShortName");
/* 64 */     String newmShortName = rs.getString("?newmShortName");
/* 65 */     if ((!CodeCompare.compare(m1ShortName, m2ShortName)) || 
/* 66 */       (!CodeCompare.compare(m1ShortName, newmShortName)) || 
/* 67 */       (!CodeCompare.compare(m2ShortName, newmShortName))) {
/* 68 */       return null;
/*    */     }
/* 70 */     String[] params1 = rs.getString("?params1").split(", ");
/* 71 */     String[] params2 = rs.getString("?params2").split(", ");
/* 72 */     String[] newParams = rs.getString("?newparams").split(", ");
/*    */     
/*    */ 
/* 75 */     int newLen = numParams(newParams);
/* 76 */     int len1 = numParams(params1);
/* 77 */     int len2 = numParams(params2);
/*    */     
/* 79 */     if ((len1 != len2) || (len1 >= newLen)) {
/* 80 */       return null;
/*    */     }
/* 82 */     return getName() + "(\"" + rs.getString("?newmFullName") + "\")";
/*    */   }
/*    */   
/*    */   private int numParams(String[] params)
/*    */   {
/* 87 */     if (params.length == 0)
/* 88 */       return 0;
/* 89 */     if (params[0] == "") {
/* 90 */       return 0;
/*    */     }
/* 92 */     return params.length;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/ParameterizeMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */