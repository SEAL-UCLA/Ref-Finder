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
/*    */ public class InlineMethod
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   private String old_method_body_;
/*    */   private String method_body_;
/*    */   private String method_full_name_;
/*    */   private String old_method_full_name_;
/*    */   private String type_full_name_;
/*    */   private String old_method_short_name_;
/*    */   
/*    */   public InlineMethod()
/*    */   {
/* 29 */     this.name_ = "inline_method";
/*    */     
/* 31 */     this.old_method_body_ = "?oldmBody";
/* 32 */     this.method_body_ = "?mBody";
/* 33 */     this.method_full_name_ = "?mFullName";
/* 34 */     this.old_method_full_name_ = "?oldmFullName";
/* 35 */     this.type_full_name_ = "?tFullName";
/* 36 */     this.old_method_short_name_ = "?oldmShortName";
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 41 */     return 
/*    */     
/* 43 */       getName() + "(" + this.method_full_name_ + "," + this.old_method_full_name_ + "," + this.old_method_body_ + "," + this.type_full_name_ + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 48 */     RefactoringQuery inline_method = new RefactoringQuery(getName(), 
/* 49 */       getQueryString());
/* 50 */     return inline_method;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 54 */     return 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */       "deleted_method(" + this.old_method_full_name_ + ", " + this.old_method_short_name_ + ", " + this.type_full_name_ + "), " + "before_method(" + this.method_full_name_ + ", ?, " + this.type_full_name_ + "), " + "before_calls(" + this.method_full_name_ + ", " + this.old_method_full_name_ + "), added_methodbody(" + this.method_full_name_ + "," + this.method_body_ + "), deleted_methodbody(" + this.old_method_full_name_ + "," + this.old_method_body_ + ")," + "NOT(equals(" + this.method_full_name_ + "," + this.old_method_full_name_ + "))";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 67 */     String oldmBody_str = rs.getString(this.old_method_body_);
/* 68 */     String mBody_str = rs.getString(this.method_body_);
/*    */     
/* 70 */     if ((oldmBody_str.length() > 1) && 
/* 71 */       (CodeCompare.compare(oldmBody_str, mBody_str)) && 
/* 72 */       (CodeCompare.contrast(oldmBody_str, mBody_str)))
/*    */     {
/* 74 */       String writeTo = getName() + "(" + "\"" + 
/* 75 */         rs.getString(this.method_full_name_) + "\"" + "," + "\"" + 
/* 76 */         rs.getString(this.old_method_full_name_) + "\"" + "," + "\"" + 
/* 77 */         oldmBody_str + "\"" + "," + "\"" + 
/* 78 */         rs.getString(this.type_full_name_) + "\"" + ")";
/*    */       
/* 80 */       return writeTo;
/*    */     }
/* 82 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 88 */     return this.name_;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/InlineMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */