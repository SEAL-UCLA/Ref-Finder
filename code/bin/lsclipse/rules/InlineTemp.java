/*    */ package lsclipse.rules;
/*    */ 
/*    */ import java.util.regex.Pattern;
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
/*    */ public class InlineTemp
/*    */   implements Rule
/*    */ {
/*    */   private static final String NEWM_BODY = "?newmBody";
/*    */   private static final String M_BODY = "?mBody";
/*    */   private static final String EXPRESSION = "?expression";
/*    */   private static final String IDENTIFIER = "?identifier";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private String name_;
/*    */   
/*    */   public InlineTemp()
/*    */   {
/* 26 */     this.name_ = "inline_temp";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 31 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 36 */     return 
/* 37 */       getName() + "(" + "?identifier" + "," + "?expression" + "," + "?mFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 42 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 46 */     return "deleted_localvar(?mFullName,?,?identifier,?expression),NOT(added_localvar(?mFullName,?,?identifier,?)),NOT(added_localvar(?mFullName,?,?,?expression)),deleted_methodbody(?mFullName,?mBody),added_methodbody(?mFullName,?newmBody)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 56 */     String expression = rs.getString("?expression");
/* 57 */     String mBody = rs.getString("?mBody");
/* 58 */     String newmBody = rs.getString("?newmBody");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 64 */     int oldCount = Pattern.compile(expression, 16).split(
/* 65 */       mBody, -1).length - 1;
/* 66 */     int newCount = Pattern.compile(expression, 16).split(
/* 67 */       newmBody, -1).length - 1;
/*    */     
/* 69 */     if (newCount < oldCount) {
/* 70 */       return null;
/*    */     }
/*    */     
/* 73 */     if (newCount < 1) {
/* 74 */       return null;
/*    */     }
/* 76 */     return 
/* 77 */       getName() + "(\"" + rs.getString("?identifier") + "\",\"" + expression + "\",\"" + rs.getString("?mFullName") + "\")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/InlineTemp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */