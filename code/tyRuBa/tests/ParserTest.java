/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class ParserTest extends TyrubaTest
/*    */ {
/*    */   public ParserTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void testParseExpression() throws ParseException, tyRuBa.modes.TypeModeError {
/*    */     try {
/* 14 */       test_must_fail("string_append(?x,?y,abc) string_append(?y,?z,abc)");
/* 15 */       fail("Should throw a parse exception");
/*    */     }
/*    */     catch (ParseException localParseException) {}
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/ParserTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */