/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class CacheRuleBaseTest extends TyrubaTest
/*    */ {
/*    */   public void setUp() throws Exception
/*    */   {
/* 10 */     tyRuBa.engine.RuleBase.useCache = true;
/* 11 */     TyrubaTest.initfile = false;
/* 12 */     super.setUp();
/*    */   }
/*    */   
/*    */   public CacheRuleBaseTest(String arg0) {
/* 16 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void test() throws ParseException, tyRuBa.modes.TypeModeError {
/* 20 */     this.frontend.parse("foo, bar, goo :: String\nMODES (F) IS NONDET END");
/*    */     
/*    */ 
/* 23 */     test_must_fail("foo(?x)");
/* 24 */     this.frontend.parse("foo(?x) :- bar(?x).");
/* 25 */     test_must_fail("foo(?x)");
/* 26 */     this.frontend.parse("bar(bar).");
/* 27 */     test_must_succeed("foo(bar)");
/*    */     
/* 29 */     this.frontend.parse("goo(goo).");
/* 30 */     this.frontend.parse("bar(?x) :- goo(?x).");
/* 31 */     test_must_succeed("foo(goo)");
/* 32 */     test_resultcount("foo(?x)", 2);
/*    */   }
/*    */   
/*    */   public void testMinnieBug() throws ParseException, tyRuBa.modes.TypeModeError {
/* 36 */     this.frontend.parse("married :: String, String\nMODES (F,F) IS NONDET END\n");
/*    */     
/*    */ 
/* 39 */     this.frontend.parse("married(Minnie,Mickey).");
/* 40 */     this.frontend.parse("married(?x,?y) :- married(?y,?x).");
/*    */     
/* 42 */     test_resultcount("married(?a,?b)", 2);
/* 43 */     test_must_succeed("married(Minnie,Mickey)");
/* 44 */     test_must_equal("married(Minnie,?x)", "?x", "Mickey");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/CacheRuleBaseTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */