/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import junit.framework.Assert;
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
/*     */   public class LogEntry
/*     */   {
/*     */     LogEntry parent;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String kind;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */     ArrayList info = new ArrayList();
/*     */     
/*  51 */     int enter = 0;
/*  52 */     int exit = 0;
/*     */     
/*     */     LogEntry(LogEntry parent, String kind, int creationTime) {
/*  55 */       this.parent = parent;
/*  56 */       this.kind = kind;
/*  57 */       this.enter = creationTime;
/*     */     }
/*     */     
/*     */     public void addInfo(Object infoArg) {
/*  61 */       this.info.add(infoArg.toString());
/*     */     }
/*     */     
/*     */     public String toString() {
/*  65 */       return 
/*     */       
/*     */ 
/*     */ 
/*  69 */         this.kind + "(" + this.enter + "," + this.exit + "," + parentID() + infoString() + ").";
/*     */     }
/*     */     
/*     */     private int parentID() {
/*  73 */       return this.parent == null ? 0 : this.parent.enter;
/*     */     }
/*     */     
/*     */     private String infoString() {
/*  77 */       String result = "";
/*  78 */       for (Iterator iter = this.info.iterator(); iter.hasNext();) {
/*  79 */         String element = (String)iter.next();
/*  80 */         result = result + "," + element;
/*     */       }
/*  82 */       return result;
/*     */     }
/*     */     
/*     */     public void exit() {
/*  86 */       this.exit = (++TestLogger.this.logtime);
/*  87 */       String msg = toString();
/*  88 */       println(msg);
/*  89 */       Assert.assertEquals(TestLogger.this.current, this);
/*  90 */       if ((this.parent != null) && (!this.parent.inProgress()))
/*  91 */         println("*** problem with parent entry " + this.parent);
/*  92 */       TestLogger.this.current = this.parent;
/*     */     }
/*     */     
/*     */     private void println(String msg) {
/*  96 */       if (TestLogger.this.console != null)
/*  97 */         TestLogger.this.console.println(msg);
/*  98 */       if (TestLogger.this.logFile != null)
/*  99 */         TestLogger.this.logFile.println(msg);
/*     */     }
/*     */     
/*     */     private boolean inProgress() {
/* 103 */       return this.exit == 0;
/*     */     }
/*     */     
/*     */     public void exit(String info) {
/* 107 */       addInfo(info);
/* 108 */       exit();
/*     */     }
/*     */   }
/*     */   
/* 112 */   LogEntry current = new LogEntry(null, "BIGBANG", 0);
/*     */   
/*     */   public LogEntry enter(String kind) {
/* 115 */     this.current = new LogEntry(this.current, kind, ++this.logtime);
/* 116 */     return this.current;
/*     */   }
/*     */   
/*     */   public LogEntry enter(String kind, String params) {
/* 120 */     this.current = new LogEntry(this.current, kind, ++this.logtime);
/* 121 */     this.current.addInfo(params);
/* 122 */     return this.current;
/*     */   }
/*     */   
/*     */   public LogEntry enter(String kind, int info) {
/* 126 */     return enter(kind, info);
/*     */   }
/*     */   
/*     */   public void logNow(String kind, String params) {
/* 130 */     LogEntry enter = enter(kind, params);
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
/* 143 */   public synchronized LogEntry enterLoad(String path) { Assert.assertFalse("Reentrant load should not happen", loading);
/* 144 */     Assert.assertFalse("Load inside storeAll", this.current.kind.equals("storeAll"));
/* 145 */     loading = true;
/* 146 */     return enter("load", "\"" + path + "\"");
/*     */   }
/*     */   
/*     */   public synchronized void exitLoad(LogEntry entry) {
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


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/TestLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */