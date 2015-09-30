/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class IgnoredVariableTest extends TyrubaTest
/*    */ {
/*    */   public IgnoredVariableTest(String arg0)
/*    */   {
/* 10 */     super(arg0);
/*    */   }
/*    */   
/*    */   protected void setUp() throws Exception {
/* 14 */     TyrubaTest.initfile = false;
/* 15 */     tyRuBa.engine.RuleBase.useCache = false;
/* 16 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testIgnoredVars1() throws ParseException, tyRuBa.modes.TypeModeError {
/* 20 */     this.frontend.parse("test :: ( ) \nMODES () IS DET END\n");
/*    */     
/* 22 */     this.frontend.parse("foobar :: String, Integer\nMODES (F,F) IS MULTI END\n");
/*    */     
/* 24 */     this.frontend.parse("test() :- foobar(?,?).");
/*    */   }
/*    */   
/*    */   public void testIgnoredVars2() throws ParseException, tyRuBa.modes.TypeModeError {
/* 28 */     this.frontend.parse("foo :: String, String\nMODES (F,F) IS NONDET END");
/*    */     
/* 30 */     this.frontend.parse("foo(a,b).");
/* 31 */     this.frontend.parse("foo(aa,bb).");
/* 32 */     test_must_findall("foo(?x,?)", "?x", new String[] { "a", "aa" });
/* 33 */     test_must_findall("foo(?,?x)", "?x", new String[] { "b", "bb" });
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/IgnoredVariableTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */