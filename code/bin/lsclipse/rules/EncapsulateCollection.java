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
/*    */ public class EncapsulateCollection
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   
/*    */   public EncapsulateCollection()
/*    */   {
/* 19 */     this.name_ = "encapsulate_collection";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 24 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 29 */     return getName() + "(" + "?fFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 34 */     RefactoringQuery encap_coll = new RefactoringQuery(getName(), 
/* 35 */       getQueryString());
/* 36 */     return encap_coll;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 40 */     return "before_field(?fFullName, ?, ?tFullName),after_field(?fFullName, ?, ?tFullName),before_accesses(?fFullName, ?getmFullName),after_accesses(?fFullName, ?getmFullName),added_calls(?getmFullName, ?unmodmFullName),after_method(?getmFullName, ?getmShortName, ?tFullName),added_method(?m1FullName, ?m1ShortName, ?tFullName),added_accesses(?fFullName, ?m1FullName),added_method(?m2FullName, ?m2ShortName, ?tFullName),added_accesses(?fFullName, ?m2FullName),NOT(equals(?m1FullName, ?m2FullName)),deleted_method(?setmFullName, ?setmShortName, ?tFullName),deleted_accesses(?fFullName, ?setmFullName)";
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 63 */     String getmShortName = rs.getString("?getmShortName");
/* 64 */     if (!getmShortName.startsWith("get")) {
/* 65 */       return null;
/*    */     }
/*    */     
/* 68 */     String setmShortName = rs.getString("?setmShortName");
/* 69 */     if (!setmShortName.startsWith("set")) {
/* 70 */       return null;
/*    */     }
/*    */     
/* 73 */     String m1ShortName = rs.getString("?m1ShortName");
/* 74 */     String m2ShortName = rs.getString("?m2ShortName");
/* 75 */     if (((!m1ShortName.startsWith("add")) || 
/* 76 */       (!m2ShortName.startsWith("remove"))) && ((!m2ShortName.startsWith("add")) || 
/* 77 */       (!m1ShortName.startsWith("remove")))) {
/* 78 */       return null;
/*    */     }
/*    */     
/* 81 */     String unmodmFullName = rs.getString("?unmodmFullName");
/* 82 */     if (!unmodmFullName.startsWith("java.util%.Collections#unmodifiable")) {
/* 83 */       return null;
/*    */     }
/* 85 */     return getName() + "(\"" + rs.getString("?fFullName") + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/EncapsulateCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */