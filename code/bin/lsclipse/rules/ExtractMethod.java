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
/*    */ public class ExtractMethod
/*    */   implements Rule
/*    */ {
/*    */   private String name_;
/*    */   private String new_method_body_;
/*    */   private String method_body_;
/*    */   private String method_full_name_;
/*    */   private String new_method_full_name_;
/*    */   private String type_full_name_;
/*    */   
/*    */   public ExtractMethod()
/*    */   {
/* 28 */     this.name_ = "extract_method";
/*    */     
/* 30 */     this.new_method_body_ = "?newmBody";
/* 31 */     this.method_body_ = "?mBody";
/* 32 */     this.method_full_name_ = "?mFullName";
/* 33 */     this.new_method_full_name_ = "?newmFullName";
/* 34 */     this.type_full_name_ = "?tFullName";
/*    */   }
/*    */   
/*    */ 
/*    */   public ExtractMethod(String method_full_name, String new_method_full_name, String new_method_body, String type_full_name)
/*    */   {
/* 40 */     this.name_ = "extract_method";
/*    */     
/* 42 */     this.new_method_body_ = new_method_body;
/* 43 */     this.method_body_ = "?mBody";
/* 44 */     this.method_full_name_ = method_full_name;
/* 45 */     this.new_method_full_name_ = new_method_full_name;
/* 46 */     this.type_full_name_ = type_full_name;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 51 */     return 
/*    */     
/* 53 */       getName() + "(" + this.method_full_name_ + "," + this.new_method_full_name_ + "," + this.new_method_body_ + "," + this.type_full_name_ + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 58 */     RefactoringQuery extract_method = new RefactoringQuery(getName(), 
/* 59 */       getQueryString());
/* 60 */     return extract_method;
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 64 */     return 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 71 */       "added_method(" + this.new_method_full_name_ + ",?," + this.type_full_name_ + "), " + "after_method(" + this.method_full_name_ + ",?," + this.type_full_name_ + ")," + "after_calls(" + this.method_full_name_ + ", " + this.new_method_full_name_ + "), added_methodbody(" + this.new_method_full_name_ + "," + this.new_method_body_ + "), deleted_methodbody(" + this.method_full_name_ + "," + this.method_body_ + ")," + "NOT(equals(" + this.new_method_full_name_ + "," + this.method_full_name_ + "))";
/*    */   }
/*    */   
/*    */   public String checkAdherence(ResultSet rs) throws TyrubaException
/*    */   {
/* 76 */     String newmBody_str = rs.getString(this.new_method_body_);
/* 77 */     String mBody_str = rs.getString(this.method_body_);
/*    */     
/* 79 */     if ((newmBody_str.length() > 1) && 
/* 80 */       (CodeCompare.compare(newmBody_str, mBody_str)) && 
/* 81 */       (CodeCompare.contrast(newmBody_str, mBody_str)))
/*    */     {
/* 83 */       String writeTo = getName() + "(" + "\"" + 
/* 84 */         rs.getString(this.method_full_name_) + "\"" + "," + "\"" + 
/* 85 */         rs.getString(this.new_method_full_name_) + "\"" + "," + "\"" + 
/* 86 */         newmBody_str + "\"" + "," + "\"" + 
/* 87 */         rs.getString(this.type_full_name_) + "\"" + ")";
/*    */       
/* 89 */       return writeTo;
/*    */     }
/* 91 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 97 */     return this.name_;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ExtractMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */