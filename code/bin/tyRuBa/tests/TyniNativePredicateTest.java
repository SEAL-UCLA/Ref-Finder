/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class TyniNativePredicateTest extends TyrubaTest
/*    */ {
/*    */   public TyniNativePredicateTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     TyrubaTest.initfile = true;
/* 14 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testString() throws ParseException, tyRuBa.modes.TypeModeError {
/* 18 */     test_must_succeed("String(abcd)");
/* 19 */     test_must_fail("equals([?x|?],[1,a]), String(?x)");
/* 20 */     test_resultcount("member(?x,[1,a,2,b]), String(?x)", 2);
/*    */   }
/*    */   
/*    */   public void testInteger()
/*    */     throws ParseException, tyRuBa.modes.TypeModeError
/*    */   {
/* 26 */     test_must_succeed("Integer(123)");
/* 27 */     test_must_fail("equals([?x|?],[a,1]), Integer(?x)");
/* 28 */     test_resultcount("member(?x,[1,a,2,b]), Integer(?x)", 2);
/*    */   }
/*    */   
/*    */   public void testRange()
/*    */     throws ParseException, tyRuBa.modes.TypeModeError
/*    */   {
/* 34 */     test_must_findall("range(23,27,?x)", "?x", 
/* 35 */       new String[] { "23", "24", "25", "26" });
/*    */   }
/*    */   
/*    */   public void testGreater() throws ParseException, tyRuBa.modes.TypeModeError {
/* 39 */     test_must_succeed("greater(10,5)");
/* 40 */     test_must_fail("greater(5,10)");
/*    */   }
/*    */   
/*    */   public void testSum() throws ParseException, tyRuBa.modes.TypeModeError {
/* 44 */     test_must_succeed("sum(1,2,3)");
/* 45 */     test_must_equal("sum(1,2,?x)", "?x", "3");
/* 46 */     test_must_equal("sum(1,?x,3)", "?x", "2");
/* 47 */     test_must_equal("sum(?x,2,3)", "?x", "1");
/*    */   }
/*    */   
/*    */   public void testMul() throws ParseException, tyRuBa.modes.TypeModeError {
/* 51 */     test_must_succeed("mul(2,3,6)");
/* 52 */     test_must_fail("mul(2,3,7)");
/* 53 */     test_must_equal("mul(2,3,?x)", "?x", "6");
/*    */   }
/*    */   
/*    */   public void testHashValue() throws ParseException, tyRuBa.modes.TypeModeError {
/* 57 */     test_must_succeed("hash_value(foobar,?v)");
/*    */   }
/*    */   
/*    */   public void testLength() throws ParseException, tyRuBa.modes.TypeModeError {
/* 61 */     test_must_equal("length([a,b,c],?x)", "?x", "3");
/* 62 */     test_must_succeed("length([a,b,c],3)");
/* 63 */     test_must_fail("length([a,b,c],2)");
/*    */   }
/*    */   
/*    */   public void testDebugPrint() throws ParseException, tyRuBa.modes.TypeModeError {
/* 67 */     test_must_succeed("equals(?x,partner), debug_print({Howdy ?x, how ya doin??})");
/*    */   }
/*    */   
/*    */   public void testWriteOutput() throws ParseException, tyRuBa.modes.TypeModeError {
/* 71 */     test_must_succeed("equals(?x,partner), write_output({Howdy ?x, how ya doin??})");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/TyniNativePredicateTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */