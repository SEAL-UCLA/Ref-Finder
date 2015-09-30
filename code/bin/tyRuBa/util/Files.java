/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class Files
/*    */ {
/*    */   public static void deleteDirectory(File dir) {
/*  8 */     if (dir.exists()) {
/*  9 */       if (dir.isDirectory()) {
/* 10 */         File[] children = dir.listFiles();
/* 11 */         for (int i = 0; i < children.length; i++) {
/* 12 */           deleteDirectory(children[i]);
/*    */         }
/*    */       }
/* 15 */       dir.delete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/Files.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */