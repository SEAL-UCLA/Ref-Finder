/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.SimpleRuleBaseBucket;
/*    */ import tyRuBa.modes.TypeModeError;
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
/* 20 */   ProgressMonitorTest.MyProgressMonitor mon = new ProgressMonitorTest.MyProgressMonitor();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ProgressMonitorTest(String arg0)
/*    */   {
/* 55 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void testProgressMonitor() throws ParseException, TypeModeError
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


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/ProgressMonitorTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */