/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
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
/*    */   public void testBucketUpdateWithDependencies()
/*    */     throws ParseException, TypeModeError
/*    */   {
/* 68 */     new BucketDependenciesTest.TestBucket(this, this.frontend, 0);
/* 69 */     test_resultcount("bucket(?id)", 128);
/*    */   }
/*    */   
/*    */   public void testBucketUpdateWithDependenciesDouble() throws ParseException, TypeModeError {
/* 73 */     new BucketDependenciesTest.TestBucket(this, this.frontend, 1);
/* 74 */     new BucketDependenciesTest.TestBucket(this, this.frontend, 2);
/* 75 */     test_resultcount("bucket(?id)", 127);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/BucketDependenciesTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */