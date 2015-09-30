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
/*    */ public class SeparateQueryFromModifier
/*    */   implements Rule
/*    */ {
/*    */   private static final String F2_FULL_NAME = "?f2FullName";
/*    */   private static final String F1_FULL_NAME = "?f1FullName";
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   private static final String M2_SHORT_NAME = "?m2ShortName";
/*    */   private static final String M1_SHORT_NAME = "?m1ShortName";
/*    */   private static final String OLDM_SHORT_NAME = "?oldmShortName";
/*    */   private static final String M2_FULL_NAME = "?m2FullName";
/*    */   private static final String M1_FULL_NAME = "?m1FullName";
/*    */   private static final String OLDM_FULL_NAME = "?oldmFullName";
/*    */   private String name_;
/*    */   
/*    */   public SeparateQueryFromModifier()
/*    */   {
/* 29 */     this.name_ = "separate_query_from_modifier";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 34 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 39 */     return 
/* 40 */       getName() + "(" + "?oldmFullName" + "," + "?m1FullName" + "," + "?m2FullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 45 */     RefactoringQuery sep_qm = new RefactoringQuery(getName(), 
/* 46 */       getQueryString());
/* 47 */     return sep_qm;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 51 */     return "before_field(?f1FullName,?,?tFullName),after_field(?f1FullName,?,?tFullName),before_field(?f2FullName,?,?tFullName),after_field(?f2FullName,?,?tFullName),deleted_method(?oldmFullName,?oldmShortName,?tFullName),deleted_accesses(?f1FullName,?oldmFullName),deleted_accesses(?f2FullName,?oldmFullName),added_method(?m1FullName,?m1ShortName,?tFullName),added_accesses(?f1FullName,?m1FullName),added_method(?m2FullName,?m2ShortName,?tFullName),added_accesses(?f2FullName,?m2FullName),NOT(equals(?m1FullName,?m2FullName))";
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
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 70 */     String oldmShortName = rs.getString("?oldmShortName");
/* 71 */     String m1ShortName = rs.getString("?m1ShortName");
/* 72 */     String m2ShortName = rs.getString("?m2ShortName");
/*    */     
/* 74 */     if ((!CodeCompare.compare(oldmShortName, m1ShortName)) || 
/* 75 */       (!CodeCompare.compare(oldmShortName, m2ShortName))) {
/* 76 */       return null;
/*    */     }
/* 78 */     if (m1ShortName.compareTo(m2ShortName) < 0) {
/* 79 */       return 
/*    */       
/* 81 */         getName() + "(\"" + rs.getString("?oldmFullName") + "\",\"" + rs.getString("?m1FullName") + "\",\"" + rs.getString("?m2FullName") + "\")";
/*    */     }
/* 83 */     return 
/*    */     
/* 85 */       getName() + "(\"" + rs.getString("?oldmFullName") + "\",\"" + rs.getString("?m2FullName") + "\",\"" + rs.getString("?m1FullName") + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/SeparateQueryFromModifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */