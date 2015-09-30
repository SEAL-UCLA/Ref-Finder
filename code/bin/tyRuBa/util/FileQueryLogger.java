/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileQueryLogger
/*    */   extends QueryLogger
/*    */ {
/*    */   PrintWriter writer;
/*    */   
/*    */   public void close()
/*    */   {
/* 18 */     this.writer.close();
/*    */   }
/*    */   
/*    */   public FileQueryLogger(File logFile, boolean append) throws IOException {
/* 22 */     this.writer = new PrintWriter(new FileOutputStream(logFile, append));
/* 23 */     this.writer.println("//SCENARIO");
/*    */   }
/*    */   
/*    */   public void logQuery(RBExpression query) {
/* 27 */     this.writer.println(query.toString());
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/FileQueryLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */