/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ 
/*    */ public class ExistQuantifierAndNotFilterTest extends TyrubaTest
/*    */ {
/*    */   public ExistQuantifierAndNotFilterTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     TyrubaTest.initfile = false;
/* 14 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testSimpleNot() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 18 */     this.frontend.parse("planet :: String\nMODES (F) IS MULTI END\n");
/*    */     
/*    */ 
/* 21 */     this.frontend.parse("orbits :: String, String\nMODES\n(B,F) IS SEMIDET\n(F,F)  IS MULTI\n(F,B) IS NONDET\nEND\n");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 28 */     this.frontend.parse("no_moon :: String\nMODES (F) IS NONDET END\n");
/*    */     
/*    */ 
/* 31 */     this.frontend.parse("no_moon(?x) :- NOT( EXISTS ?m : orbits(?m,?x) ), planet(?x).");
/*    */   }
/*    */   
/*    */   public void testBadNot() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError
/*    */   {
/* 36 */     this.frontend.parse("planet :: String\nMODES (F) IS MULTI END\n");
/*    */     
/*    */ 
/* 39 */     this.frontend.parse("orbits :: String, String\nMODES\n(B,F) IS SEMIDET\n(F,F)  IS MULTI\n(F,B) IS NONDET\nEND\n");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */     this.frontend.parse("no_moon :: String\nMODES (F) IS NONDET END\n");
/*    */     try
/*    */     {
/* 49 */       this.frontend.parse("no_moon(?x) :- NOT(orbits(?m,?x)), planet(?x).");
/* 50 */       fail("This should have thrown a TypeModeError because ?m is not bound before entering NOT");
/*    */     }
/*    */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*    */   }
/*    */   
/*    */   public void testBadExist() throws Exception
/*    */   {
/* 57 */     TyrubaTest.initfile = true;
/* 58 */     super.setUp();
/*    */     
/* 60 */     this.frontend.parse("foo :: ?t\nMODES (F) IS NONDET END");
/*    */     
/*    */     try
/*    */     {
/* 64 */       this.frontend.parse("foo(?x) :- EXISTS ?x : member(?x,[bar]).");
/* 65 */       fail("This should have thrown a TypeModeError because ?x does not become bound after EXISTS");
/*    */     }
/*    */     catch (tyRuBa.modes.TypeModeError e) {
/* 68 */       System.err.println(e.getMessage());
/*    */     }
/*    */     
/* 71 */     this.frontend.parse("foo1 :: String\nMODES (F) IS NONDET END");
/*    */     
/* 73 */     this.frontend.parse("foo1(?x) :- member(?x,[bar,foo]), (EXISTS ?x : equals(?x,bar)).");
/*    */     
/* 75 */     test_must_succeed("foo1(bar)");
/* 76 */     test_must_succeed("foo1(foo)");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/ExistQuantifierAndNotFilterTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */