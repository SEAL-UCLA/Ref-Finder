/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.RuleBaseBucket;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
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
/*    */ public class BucketDependenciesTest
/*    */   extends TyrubaTest
/*    */ {
/*    */   public BucketDependenciesTest(String arg0)
/*    */   {
/* 24 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 28 */     tyRuBa.engine.RuleBase.silent = true;
/* 29 */     super.setUp();
/* 30 */     this.frontend.parse(
/* 31 */       "bucket :: Integer MODES (F) IS NONDET END ");
/*    */     
/* 33 */     this.frontend.parse(
/* 34 */       "child :: Integer, Integer MODES     (B,F) IS NONDET     (F,B) IS SEMIDET     (F,F) IS NONDET END ");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private class TestBucket
/*    */     extends RuleBaseBucket
/*    */   {
/*    */     static final int num_buckets = 128;
/*    */     
/*    */     private int myID;
/*    */     
/*    */ 
/*    */     public TestBucket(FrontEnd frontend, int bucketID)
/*    */     {
/* 49 */       super(bucketID);
/* 50 */       this.myID = bucketID;
/*    */     }
/*    */     
/*    */     protected void update() throws TypeModeError, ParseException {
/* 54 */       parse("bucket(" + this.myID + ").");
/* 55 */       assertChild(2 * this.myID + 1);
/* 56 */       assertChild(2 * this.myID + 2);
/*    */     }
/*    */     
/*    */     private void assertChild(int childID) throws ParseException, TypeModeError {
/* 60 */       if (childID < 128) {
/* 61 */         new TestBucket(BucketDependenciesTest.this, frontend(), childID);
/* 62 */         parse("child(" + this.myID + "," + childID + ").");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void testBucketUpdateWithDependencies() throws ParseException, TypeModeError {
/* 68 */     new TestBucket(this.frontend, 0);
/* 69 */     test_resultcount("bucket(?id)", 128);
/*    */   }
/*    */   
/*    */   public void testBucketUpdateWithDependenciesDouble() throws ParseException, TypeModeError {
/* 73 */     new TestBucket(this.frontend, 1);
/* 74 */     new TestBucket(this.frontend, 2);
/* 75 */     test_resultcount("bucket(?id)", 127);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/BucketDependenciesTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */