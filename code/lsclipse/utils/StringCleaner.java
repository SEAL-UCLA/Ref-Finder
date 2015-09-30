/*    */ package lsclipse.utils;
/*    */ 
/*    */ public class StringCleaner {
/*    */   public static String cleanupString(String s) {
/*  5 */     s = s.trim();
/*  6 */     s = s.replace("\n", "");
/*  7 */     s = s.replace("\"", "");
/*  8 */     s = s.replace(";", "");
/*  9 */     s = s.replace(" ", "");
/* 10 */     s = s.replace("{", "");
/* 11 */     s = s.replace("}", "");
/* 12 */     s = s.replace("\\", "");
/* 13 */     s = s.replace("return", "");
/* 14 */     return s;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/utils/StringCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */