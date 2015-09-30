/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ 
/*    */ public class ModeSwitchExpressionTest extends TyrubaTest
/*    */ {
/*    */   public ModeSwitchExpressionTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testWithoutDefault() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 17 */     this.frontend.parse("foo :: =Integer, =Integer\nMODES\n(F,B) IS DET\n(B,F) IS DET\n END");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 22 */     this.frontend.parse("foo(?x,?y) :- BOUND ?x : sum(?x,1,?y)| BOUND ?y: sum(?y,1,?x).");
/*    */     
/*    */ 
/* 25 */     test_must_equal("foo(1,?y)", "?y", "2");
/* 26 */     test_must_equal("foo(?x,1)", "?x", "2");
/*    */   }
/*    */   
/*    */   public void testWithDefault() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 30 */     this.frontend.parse("foo :: =Integer, =Integer\nMODES\n(F,B) IS DET\n(B,F) IS DET\nEND");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 35 */     this.frontend.parse("foo(?x,?y) :- BOUND ?x : sum(?x,1,?y)| DEFAULT: sum(?y,1,?x).");
/*    */     
/*    */ 
/* 38 */     test_must_equal("foo(1,?y)", "?y", "2");
/* 39 */     test_must_equal("foo(?x,1)", "?x", "2");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/ModeSwitchExpressionTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */