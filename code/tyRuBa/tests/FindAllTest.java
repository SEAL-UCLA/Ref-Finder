/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ public class FindAllTest extends TyrubaTest
/*    */ {
/*    */   public FindAllTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     TyrubaTest.initfile = true;
/* 14 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testFindAll() throws tyRuBa.parser.ParseException, TypeModeError {
/* 18 */     this.frontend.parse("testje :: [[Integer]]\nMODES (F) IS DET END");
/*    */     
/* 20 */     this.frontend.parse("testje(?re) :- FINDALL(append(?x,?,[1,2,3]),?x,?re).");
/*    */     
/* 22 */     test_must_succeed("testje([[],[1],[1,2],[1,2,3]])");
/* 23 */     test_must_equal("FINDALL((EXISTS ?y : append(?x,?y,[1,2,3])),?x,?re)", 
/* 24 */       "?re", "[[],[1],[1,2],[1,2,3]]");
/* 25 */     test_must_succeed("member([1],?re), FINDALL(append(?x,?,[1,2,3]),?x,?re)");
/* 26 */     test_must_succeed("FINDALL(append(?x,?,?lst),?x,?re), append([1],[2,3],?lst)");
/*    */     try
/*    */     {
/* 29 */       test_must_fail("FINDALL(append(?x,?y,[1,2,3]),?x,?re)");
/* 30 */       fail("Should have thrown a TypeModeError because ?y is not bound.");
/*    */     }
/*    */     catch (TypeModeError localTypeModeError) {}
/*    */   }
/*    */   
/*    */   public void testCountAll() throws tyRuBa.parser.ParseException, TypeModeError
/*    */   {
/* 37 */     test_must_succeed("COUNTALL((EXISTS ?y:append(?x,?y,[1,2,3])),?x,4).");
/* 38 */     test_must_fail("COUNTALL((EXISTS ?y:append(?x,?y,[1,2,3])),?x,2).");
/* 39 */     test_must_equal("COUNTALL((EXISTS ?y: append(?x,?y,[1,2,3])),?x,?n)", 
/* 40 */       "?n", "4");
/*    */     try
/*    */     {
/* 43 */       test_must_fail("COUNTALL(append(?x,?y,[1,2,3]),?x,?re)");
/* 44 */       fail("Should have thrown a TypeModeError because ?y is not bound.");
/*    */     }
/*    */     catch (TypeModeError localTypeModeError) {}
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/FindAllTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */