/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ 
/*     */ public class PrologNativePredicateTest extends TyrubaTest
/*     */ {
/*     */   public PrologNativePredicateTest(String arg0)
/*     */   {
/*  10 */     super(arg0);
/*     */   }
/*     */   
/*     */   public void setUp() throws Exception {
/*  14 */     TyrubaTest.initfile = true;
/*  15 */     tyRuBa.engine.RuleBase.useCache = true;
/*  16 */     tyRuBa.engine.RuleBase.silent = true;
/*  17 */     super.setUp();
/*     */   }
/*     */   
/*     */   public void testListRef() throws ParseException, TypeModeError {
/*  21 */     test_must_equal("list_ref(?n,[a,b,c],b)", "?n", "1");
/*  22 */     test_must_equal("list_ref(2,[a,b,c],?x)", "?x", "c");
/*  23 */     test_must_findall("list_ref(?n,[a,b,a],a)", "?n", 
/*  24 */       new String[] { "0", "2" });
/*     */   }
/*     */   
/*     */   public void testAppend() throws ParseException, TypeModeError {
/*  28 */     test_must_succeed("append([1,2,3],[4,5],[1,2,3,4,5])");
/*  29 */     test_must_equal("append(?x,[4,5],[1,2,3,4,5])", "?x", "[1,2,3]");
/*  30 */     test_must_fail("append(?x,[3,5],[1,2,3,4,5])");
/*  31 */     test_must_findall("append(?x,?y,[1,2,3,4])", 
/*  32 */       new String[] { "?x", "?y" }, 
/*  33 */       new String[][] {
/*  34 */       { "[]", "[1,2,3,4]" }, 
/*  35 */       { "[1]", "[2,3,4]" }, 
/*  36 */       { "[1,2]", "[3,4]" }, 
/*  37 */       { "[1,2,3]", "[4]" }, 
/*  38 */       { "[1,2,3,4]", "[]" } });
/*     */   }
/*     */   
/*     */   public void testMember() throws ParseException, TypeModeError
/*     */   {
/*  43 */     test_must_findall("member(?x,[1,2,3,4])", "?x", 
/*  44 */       new String[] { "1", "2", "3", "4" });
/*     */   }
/*     */   
/*     */   public void testPermutation() throws ParseException, TypeModeError {
/*  48 */     test_must_succeed("permutation([1,2,3],[1,2,3])");
/*  49 */     test_must_succeed("permutation([1,2,3],[1,3,2])");
/*  50 */     test_must_succeed("permutation([1,2,3],[2,1,3])");
/*  51 */     test_must_succeed("permutation([1,2,3],[2,3,1])");
/*  52 */     test_must_succeed("permutation([1,2,3],[3,1,2])");
/*  53 */     test_must_succeed("permutation([1,2,3],[3,2,1])");
/*  54 */     test_must_fail("permutation([1,2,3],[1,2,4])");
/*  55 */     test_must_succeed("permutation([1,2,3], ?x)");
/*  56 */     test_must_succeed("permutation(?x, [1,2,3])");
/*     */   }
/*     */   
/*     */   public void testReverse() throws ParseException, TypeModeError {
/*  60 */     test_must_equal("reverse([1,2,3],?x)", "?x", "[3,2,1]");
/*  61 */     test_must_equal("reverse(?x,[1,2,3])", "?x", "[3,2,1]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void testEqualOrUnify()
/*     */     throws ParseException, TypeModeError
/*     */   {
/*  72 */     this.frontend.parse("TYPE foo<?x> AS <String>");
/*  73 */     test_must_succeed("equals([1,2,3],[1,2,3])");
/*  74 */     test_must_fail("equals(a,b)");
/*  75 */     test_must_succeed("equals(foo<abc>,foo<abc>)");
/*  76 */     test_must_equal("equals(foo<abc>,?x)", "?x", "foo<abc>");
/*  77 */     test_must_equal("equals(a,?x)", "?x", "a");
/*  78 */     test_must_equal("equals(?x,a)", "?x", "a");
/*     */   }
/*     */   
/*     */   public void testZip() throws ParseException, TypeModeError {
/*  82 */     test_must_succeed(
/*  83 */       "zip([1,2,3],[a,b,c],?x), equals(?x,[<1,a>,<2,b>,<3,c>])");
/*  84 */     test_must_succeed("zip([1,2,3],[a,b,c],[<1,a>,<2,b>,<3,c>])");
/*  85 */     test_must_fail("zip([1,2],[a,b,c],[<1,a>,<2,b>,<3,c>])");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void testSumList()
/*     */     throws ParseException, TypeModeError
/*     */   {
/*  96 */     test_must_equal("sumList([1,2,3],?x)", "?x", "6");
/*     */   }
/*     */   
/*     */   public void testTrueFalse() throws ParseException, TypeModeError {
/* 100 */     test_must_fail("false()");
/* 101 */     test_must_succeed("true()");
/* 102 */     test_must_succeed("true();false()");
/* 103 */     test_must_succeed("false();true()");
/* 104 */     test_must_fail("true(),false()");
/* 105 */     test_must_fail("false(),true()");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/PrologNativePredicateTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */