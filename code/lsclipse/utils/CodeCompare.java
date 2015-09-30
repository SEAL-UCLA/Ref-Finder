/*    */ package lsclipse.utils;
/*    */ 
/*    */ import lsclipse.LCS;
/*    */ 
/*    */ public class CodeCompare
/*    */ {
/*    */   public static final double SIMILARITY_THRESHOLD = 0.85D;
/*    */   public static final double DIFFERNCE_THRESHOLD = 0.75D;
/*    */   
/*    */   public static boolean compare(String left, String right)
/*    */   {
/* 12 */     String shorter = getShorterString(left, right);
/* 13 */     String lcs = LCS.getLCS(left, right);
/* 14 */     double similarity = lcs.length() / shorter.length();
/* 15 */     if (similarity >= 0.85D) {
/* 16 */       return true;
/*    */     }
/* 18 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public static boolean contrast(String left, String right)
/*    */   {
/* 24 */     String longer = getLongerString(left, right);
/* 25 */     String lcs = LCS.getLCS(left, right);
/* 26 */     double similarity = lcs.length() / longer.length();
/* 27 */     if (similarity <= 0.75D) {
/* 28 */       return true;
/*    */     }
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   private static String getShorterString(String left, String right) {
/* 34 */     if (left.length() < right.length())
/* 35 */       return left;
/* 36 */     return right;
/*    */   }
/*    */   
/*    */   private static String getLongerString(String left, String right) {
/* 40 */     if (left.length() > right.length())
/* 41 */       return left;
/* 42 */     return right;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/utils/CodeCompare.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */