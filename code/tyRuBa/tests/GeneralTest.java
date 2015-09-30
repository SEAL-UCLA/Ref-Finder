/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ import tyRuBa.util.ElementSource;
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
/*    */ public class GeneralTest
/*    */   extends TyrubaTest
/*    */ {
/*    */   public GeneralTest(String arg0)
/*    */   {
/* 29 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 33 */     TyrubaTest.initfile = true;
/* 34 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testAskForMoreAgain() throws ParseException, TypeModeError {
/* 38 */     ElementSource result = this.frontend.frameQuery("string_append(abc,def,?x)");
/* 39 */     int ctr = 0;
/* 40 */     while (result.hasMoreElements()) {
/* 41 */       ctr++;
/* 42 */       result.nextElement();
/*    */     }
/* 44 */     assertEquals(1, ctr);
/* 45 */     assertFalse(result.hasMoreElements());
/*    */   }
/*    */   
/*    */   public void testPersistentRBQuotedInFact() throws ParseException, TypeModeError {
/* 49 */     this.frontend.parse("test :: String PERSISTENT MODES (F) IS NONDET END");
/* 50 */     this.frontend.parse("test({Hola Pola!}).");
/* 51 */     test_must_succeed("test({Hola Pola!})");
/* 52 */     test_must_succeed("test(\"Hola Pola!\")");
/*    */   }
/*    */   
/*    */   public void testGetVars() throws ParseException, TypeModeError {
/* 56 */     RBExpression exp = this.frontend.makeExpression("string_append(?x,?a,abc);string_append(?x,?b,def).");
/* 57 */     Collection vars = exp.getVariables();
/* 58 */     assertEquals(vars.size(), 1);
/* 59 */     assertEquals(vars.toString(), "[?x]");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/GeneralTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */