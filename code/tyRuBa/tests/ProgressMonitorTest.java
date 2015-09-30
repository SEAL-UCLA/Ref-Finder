/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.ProgressMonitor;
/*    */ import tyRuBa.engine.SimpleRuleBaseBucket;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class ProgressMonitorTest extends TyrubaTest
/*    */ {
/*    */   SimpleRuleBaseBucket bucket;
/*    */   SimpleRuleBaseBucket otherBucket;
/*    */   
/*    */   public void setUp() throws Exception
/*    */   {
/* 15 */     super.setUp(this.mon);
/* 16 */     this.bucket = new SimpleRuleBaseBucket(this.frontend);
/* 17 */     this.otherBucket = new SimpleRuleBaseBucket(this.frontend);
/*    */   }
/*    */   
/* 20 */   MyProgressMonitor mon = new MyProgressMonitor();
/*    */   
/*    */   static class MyProgressMonitor implements ProgressMonitor
/*    */   {
/* 24 */     private boolean isDone = true;
/*    */     
/* 26 */     int updates = -99;
/*    */     int expectedWork;
/*    */     
/*    */     public void beginTask(String name, int totalWork) {
/* 30 */       this.updates = 0;
/* 31 */       this.expectedWork = totalWork;
/* 32 */       if (!this.isDone)
/* 33 */         ProgressMonitorTest.fail("No multi tasking/progressing!");
/* 34 */       this.isDone = false;
/* 35 */       ProgressMonitorTest.assertTrue(totalWork > 0);
/*    */     }
/*    */     
/*    */     public void worked(int units) {
/* 39 */       this.updates += units;
/*    */     }
/*    */     
/*    */     public void done() {
/* 43 */       this.isDone = true;
/*    */     }
/*    */     
/*    */     public int workDone() {
/* 47 */       if (!this.isDone)
/* 48 */         ProgressMonitorTest.fail("Hey... the work is not done!");
/* 49 */       return this.updates;
/*    */     }
/*    */   }
/*    */   
/*    */   public ProgressMonitorTest(String arg0)
/*    */   {
/* 55 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void testProgressMonitor() throws ParseException, tyRuBa.modes.TypeModeError
/*    */   {
/* 60 */     tyRuBa.engine.RuleBase.autoUpdate = true;
/*    */     
/* 62 */     this.frontend.parse("foo :: String");
/*    */     
/* 64 */     this.frontend.parse("foo(frontend).");
/* 65 */     this.otherBucket.addStuff("foo(otherBucket).");
/* 66 */     this.bucket.addStuff("foo(bucket).");
/*    */     
/* 68 */     test_must_succeed("foo(frontend)");
/*    */     
/* 70 */     assertEquals(this.mon.expectedWork, this.mon.workDone());
/*    */     
/* 72 */     test_must_succeed("foo(frontend)", this.otherBucket);
/*    */     
/* 74 */     this.otherBucket.clearStuff();
/*    */     
/* 76 */     test_must_succeed("foo(frontend)");
/*    */     
/* 78 */     assertEquals(this.mon.expectedWork, this.mon.workDone());
/*    */     
/* 80 */     this.mon.updates = 64537;
/*    */     
/* 82 */     test_must_succeed("foo(frontend)", this.otherBucket);
/*    */     
/* 84 */     assertEquals(64537, this.mon.workDone());
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/ProgressMonitorTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */