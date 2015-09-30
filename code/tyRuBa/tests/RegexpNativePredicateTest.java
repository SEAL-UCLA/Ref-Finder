/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class RegexpNativePredicateTest extends TyrubaTest
/*    */ {
/*    */   public RegexpNativePredicateTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     TyrubaTest.initfile = true;
/* 14 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testRegexp() throws ParseException, tyRuBa.modes.TypeModeError {
/* 18 */     test_must_succeed("RegExp(/c*/)");
/* 19 */     test_must_fail("list_ref(0,[1,/c*/],?x), RegExp(?x)");
/* 20 */     test_must_fail("list_ref(0,[abc,/c*/],?x), RegExp(?x)");
/*    */   }
/*    */   
/*    */   public void testReMatch() throws ParseException, tyRuBa.modes.TypeModeError {
/* 24 */     test_must_succeed("re_match(/c*/,ccc)");
/* 25 */     test_must_fail("re_match(/a/,ccc)");
/* 26 */     test_must_succeed("re_match(/b+/,abc)");
/*    */   }
/*    */   
/*    */   public void testBadUseOfRegexp() throws ParseException, tyRuBa.modes.TypeModeError {
/* 30 */     this.frontend.parse(
/* 31 */       "TYPE Element AS String name :: Element, String MODES (B,F) IS SEMIDET (F,F) IS NONDET (F,B) IS NONDET END");
/*    */     
/*    */ 
/* 34 */     test_must_fail("name(foo::Element,/bar/)");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/RegexpNativePredicateTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */