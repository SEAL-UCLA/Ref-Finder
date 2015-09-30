/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class ConvertToTest extends TyrubaTest
/*    */ {
/*    */   public ConvertToTest(String arg0)
/*    */   {
/* 12 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 16 */     TyrubaTest.initfile = true;
/* 17 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testConvertToType1() throws ParseException, TypeModeError {
/* 21 */     test_must_succeed("convertToInteger(1,1)");
/* 22 */     test_must_equal("convertToInteger(1,?x)", "?x", "1");
/* 23 */     test_must_equal("convertToInteger(?x,1)", "?x", "1");
/* 24 */     test_must_fail("convertToInteger(1,2)");
/*    */     
/* 26 */     test_must_succeed("convertToString(abc,abc)");
/* 27 */     test_must_equal("convertToString(abc,?x)", "?x", "abc");
/* 28 */     test_must_equal("convertToString(?x,abc)", "?x", "abc");
/*    */     
/* 30 */     this.frontend.parse("TYPE Sno AS String");
/* 31 */     this.frontend.parse("TYPE Bol AS String");
/* 32 */     this.frontend.parse("TYPE Snobol = Sno | Bol");
/* 33 */     test_must_succeed("convertToSno(sno::Sno,sno::Sno)");
/* 34 */     test_must_succeed("convertToSnobol(?x,sno::Sno)");
/* 35 */     test_must_succeed("convertToSno(?x,?y), member(?x,[sno::Sno,bol::Bol])");
/* 36 */     test_must_equal("convertToSno(?x,?y), member(?x,[sno::Sno,bol::Bol])", "?y", "sno::Sno");
/* 37 */     test_resultcount("convertToSno(?x,?y), member(?x,[sno::Sno,bol::Bol])", 1);
/*    */   }
/*    */   
/*    */   public void testConvertToType2() throws ParseException, TypeModeError {
/* 41 */     this.frontend.parse("foo :: =Integer, =Integer\nMODES (F,F) IS NONDET END\n");
/*    */     
/*    */ 
/* 44 */     this.frontend.parse("foo(?x,?y) :- member(?x1,?lst), sum(?x,?x,?y), append([1,2],[a,b],?lst),convertToInteger(?x1,?x).");
/*    */     
/*    */ 
/*    */ 
/* 48 */     test_must_succeed("foo(1,2)");
/* 49 */     test_must_succeed("foo(2,4)");
/*    */     
/* 51 */     this.frontend.parse("bar :: =Integer\nMODES (F) IS NONDET END");
/*    */     try
/*    */     {
/* 54 */       this.frontend.parse("bar(?x) :- member(?x1,[1,2,3]), convertToString(?x1,?x).");
/* 55 */       fail("This should have thrown a TypeModeError because a string cannot be converted from an integer");
/*    */     }
/*    */     catch (TypeModeError e) {
/* 58 */       System.err.println(e.getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/ConvertToTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */