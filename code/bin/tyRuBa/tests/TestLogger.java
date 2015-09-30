/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import junit.framework.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TestLogger
/*     */ {
/*     */   public static final boolean logging = false;
/*  14 */   private int logtime = 0;
/*     */   PrintWriter logFile;
/*  16 */   PrintStream console = System.err;
/*     */   
/*     */   public TestLogger(PrintWriter writer) {
/*  19 */     this.logFile = writer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   TestLogger.LogEntry current = new TestLogger.LogEntry(this, null, "BIGBANG", 0);
/*     */   
/*     */   public TestLogger.LogEntry enter(String kind) {
/* 115 */     this.current = new TestLogger.LogEntry(this, this.current, kind, ++this.logtime);
/* 116 */     return this.current;
/*     */   }
/*     */   
/*     */   public TestLogger.LogEntry enter(String kind, String params) {
/* 120 */     this.current = new TestLogger.LogEntry(this, this.current, kind, ++this.logtime);
/* 121 */     this.current.addInfo(params);
/* 122 */     return this.current;
/*     */   }
/*     */   
/*     */   public TestLogger.LogEntry enter(String kind, int info) {
/* 126 */     return enter(kind, info);
/*     */   }
/*     */   
/*     */   public void logNow(String kind, String params) {
/* 130 */     TestLogger.LogEntry enter = enter(kind, params);
/* 131 */     enter.exit();
/*     */   }
/*     */   
/*     */   void close() {
/* 135 */     while (this.current != null)
/* 136 */       this.current.exit();
/* 137 */     if (this.logFile != null)
/* 138 */       this.logFile.close();
/*     */   }
/*     */   
/* 141 */   private static boolean loading = false;
/*     */   
/* 143 */   public synchronized TestLogger.LogEntry enterLoad(String path) { Assert.assertFalse("Reentrant load should not happen", loading);
/* 144 */     Assert.assertFalse("Load inside storeAll", this.current.kind.equals("storeAll"));
/* 145 */     loading = true;
/* 146 */     return enter("load", "\"" + path + "\"");
/*     */   }
/*     */   
/*     */   public synchronized void exitLoad(TestLogger.LogEntry entry) {
/* 150 */     Assert.assertTrue("Must enter load before exit load", loading);
/* 151 */     entry.exit();
/* 152 */     loading = false;
/*     */   }
/*     */   
/*     */   public void assertTrue(String msg, boolean b) {
/* 156 */     if (!b) {
/* 157 */       logNow("assertionFailed", "\"" + msg + "\"");
/* 158 */       Assert.fail(msg);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/TestLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */