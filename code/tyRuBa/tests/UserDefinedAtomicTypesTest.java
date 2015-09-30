/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserDefinedAtomicTypesTest
/*    */   extends TyrubaTest
/*    */ {
/*    */   public UserDefinedAtomicTypesTest(String arg0)
/*    */   {
/* 15 */     super(arg0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void testUnification()
/*    */     throws ParseException, TypeModeError
/*    */   {
/* 23 */     this.frontend.parse("TYPE Foo AS String");
/* 24 */     this.frontend.parse(
/* 25 */       "foo2string :: Foo, String \nMODES (B,F) IS DET (F,B) IS DET END");
/*    */     
/*    */ 
/* 28 */     this.frontend.parse("foo2string(?s::Foo,?s).");
/*    */     
/* 30 */     test_must_succeed("foo2string(abc::Foo,abc)");
/* 31 */     test_must_fail("foo2string(abc::Foo,ab)");
/* 32 */     test_must_equal("foo2string(abc::Foo,?x)", "?x", "abc");
/* 33 */     test_must_equal("foo2string(?x::Foo,abc)", "?x", "abc");
/* 34 */     test_must_equal("foo2string(?x,abc)", "?x", "abc::Foo");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/UserDefinedAtomicTypesTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */