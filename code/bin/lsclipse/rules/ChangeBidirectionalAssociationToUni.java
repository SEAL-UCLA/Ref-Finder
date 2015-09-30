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
/*    */ public class ChangeBidirectionalAssociationToUni
/*    */   implements Rule
/*    */ {
/*    */   private static final String T2P_FULL_NAME = "?t2pFullName";
/*    */   private static final String TP_FULL_NAME = "?tpFullName";
/*    */   private static final String T2_FULL_NAME = "?t2FullName";
/*    */   private static final String T_FULL_NAME = "?tFullName";
/*    */   private String name_;
/*    */   
/*    */   public ChangeBidirectionalAssociationToUni()
/*    */   {
/* 24 */     this.name_ = "change_bi_to_uni";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 29 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 34 */     return getName() + "(" + "?tFullName" + "," + "?t2FullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 39 */     RefactoringQuery changeunitobi = new RefactoringQuery(getName(), 
/* 40 */       getQueryString());
/* 41 */     return changeunitobi;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 45 */     return "before_field(?fFullName, ?, ?t2FullName),after_field(?fFullName, ?, ?t2FullName),before_fieldoftype(?fFullName, ?tpFullName),after_fieldoftype(?fFullName, ?tpFullName),deleted_field(?f2FullName, ?, ?tFullName),deleted_fieldoftype(?f2FullName, ?t2pFullName),NOT(equals(?tFullName, ?t2FullName))";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 56 */     String tFullName = rs.getString("?tFullName");
/* 57 */     String t2FullName = rs.getString("?t2FullName");
/* 58 */     String tpFullName = rs.getString("?tpFullName");
/* 59 */     String t2pFullName = rs.getString("?t2pFullName");
/*    */     
/* 61 */     if (((tFullName.equals(tpFullName)) && (t2pFullName.contains(t2FullName))) || (
/* 62 */       (t2FullName.equals(t2pFullName)) && 
/* 63 */       (tpFullName.contains(tFullName)))) {
/* 64 */       String result = getName() + "(\"" + tFullName + "\",\"" + 
/* 65 */         t2FullName + "\")";
/* 66 */       return result;
/*    */     }
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ChangeBidirectionalAssociationToUni.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */