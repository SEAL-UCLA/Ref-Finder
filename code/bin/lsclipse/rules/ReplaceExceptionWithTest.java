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
/*    */ public class ReplaceExceptionWithTest
/*    */   implements Rule
/*    */ {
/*    */   private static final String M_BODY = "?mBody";
/*    */   private static final String CONDITION = "?condition";
/*    */   private static final String CATCH_BLOCKS = "?catchBlocks";
/*    */   private static final String ELSE_BLOCK = "?elseBlock";
/*    */   private static final String M_FULL_NAME = "?mFullName";
/*    */   private static final String IF_BLOCK = "?ifBlock";
/*    */   private static final String TRY_BLOCK = "?tryBlock";
/*    */   private String name_;
/*    */   
/*    */   public ReplaceExceptionWithTest()
/*    */   {
/* 27 */     this.name_ = "replace_exception_with_test";
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 32 */     return this.name_;
/*    */   }
/*    */   
/*    */   public String getRefactoringString()
/*    */   {
/* 37 */     return 
/* 38 */       getName() + "(?catchStatement," + "?condition" + "," + "?mFullName" + ")";
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefactoringQuery()
/*    */   {
/* 43 */     return new RefactoringQuery(getName(), getQueryString());
/*    */   }
/*    */   
/*    */   private String getQueryString() {
/* 47 */     return "deleted_trycatch(?tryBlock,?catchBlocks,?,?mFullName),added_conditional(?condition,?ifBlock,?elseBlock,?mFullName),NOT(before_conditional(?condition, ?, ?, ?mFullName)), added_methodbody(?mFullName,?mBody)";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String checkAdherence(ResultSet rs)
/*    */     throws TyrubaException
/*    */   {
/* 57 */     String tryBlock = rs.getString("?tryBlock");
/* 58 */     String condition = rs.getString("?condition");
/* 59 */     String ifBlock = rs.getString("?ifBlock");
/* 60 */     String elseBlock = rs.getString("?elseBlock");
/* 61 */     if (elseBlock.equals("")) {
/* 62 */       String mBody = rs.getString("?mBody");
/* 63 */       String firstPart = condition + ")" + ifBlock;
/*    */       
/* 65 */       elseBlock = mBody.substring(mBody.indexOf(firstPart) + 
/* 66 */         firstPart.length());
/*    */     }
/* 68 */     String compareToCatch = null;
/*    */     
/* 70 */     if (CodeCompare.compare(tryBlock, ifBlock)) {
/* 71 */       compareToCatch = elseBlock;
/* 72 */     } else if (CodeCompare.compare(tryBlock, elseBlock)) {
/* 73 */       compareToCatch = ifBlock;
/*    */     } else {
/* 75 */       return null;
/*    */     }
/* 77 */     assert (compareToCatch != null);
/*    */     
/* 79 */     String catchString = rs.getString("?catchBlocks");
/* 80 */     String[] catchBlocks = catchString.split(",");
/*    */     String[] arrayOfString1;
/* 82 */     int j = (arrayOfString1 = catchBlocks).length; for (int i = 0; i < j; i++) { String catchBlock = arrayOfString1[i];
/* 83 */       if (catchBlock.length() != 0)
/*    */       {
/* 85 */         String exception = catchBlock.substring(0, catchBlock.indexOf(':'));
/* 86 */         String catchBody = catchBlock
/* 87 */           .substring(catchBlock.indexOf(':') + 1);
/* 88 */         if ((catchBody.length() > 0) && 
/* 89 */           (CodeCompare.compare(compareToCatch, catchBody))) {
/* 90 */           return 
/* 91 */             getName() + "(\"" + exception + "\",\"" + condition + "\",\"" + rs.getString("?mFullName") + "\")";
/*    */         }
/*    */       }
/*    */     }
/* 95 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/ReplaceExceptionWithTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */