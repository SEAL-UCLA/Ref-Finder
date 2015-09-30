/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class UniqueQuantifierTest extends TyrubaTest
/*    */ {
/*    */   public UniqueQuantifierTest(String arg0)
/*    */   {
/* 10 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 14 */     TyrubaTest.initfile = true;
/* 15 */     tyRuBa.engine.RuleBase.silent = false;
/* 16 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testSimpleUnique() throws ParseException, tyRuBa.modes.TypeModeError {
/* 20 */     this.frontend.parse("unique_member :: ?t, [?t]\nMODES (F,B) IS SEMIDET END");
/*    */     
/* 22 */     this.frontend.parse("unique_member(?x,?lst) :- UNIQUE ?x : member(?x,?lst).");
/*    */     
/* 24 */     test_must_succeed("unique_member(1,[1])");
/* 25 */     test_must_fail("unique_member(?x,[1,2])");
/* 26 */     test_must_fail("unique_member(?x,[])");
/* 27 */     test_must_equal("unique_member(?x,[hello])", "?x", "hello");
/*    */   }
/*    */   
/*    */   public void testUniqueWithVariableBoundBefore() throws ParseException, tyRuBa.modes.TypeModeError {
/* 31 */     this.frontend.parse("foo :: String, [Object]\nMODES (F,B) IS SEMIDET END");
/*    */     
/* 33 */     this.frontend.parse("foo(?x,?lst) :- equals(?x,bar), (UNIQUE ?x : member(?x,?lst)).");
/*    */     
/*    */ 
/* 36 */     test_must_succeed("foo(?x,[bar])");
/* 37 */     test_must_succeed("foo(?x,[bar,bar])");
/* 38 */     test_must_fail("foo(?x,[foo])");
/* 39 */     test_must_fail("foo(?x,[foo,bar])");
/* 40 */     test_must_fail("foo(?x,[])");
/*    */   }
/*    */   
/*    */   public void testUniqueWithVariableUsedAfter() throws ParseException, tyRuBa.modes.TypeModeError {
/* 44 */     this.frontend.parse("foo :: String, [Object]\nMODES (F,B) IS SEMIDET END");
/*    */     
/* 46 */     this.frontend.parse("foo(?x,?lst) :- (UNIQUE ?x : member(?x,?lst)), equals(?x,foo).");
/*    */     
/*    */ 
/* 49 */     test_must_succeed("foo(?x,[foo])");
/* 50 */     test_must_succeed("foo(?x,[foo,foo])");
/* 51 */     test_must_fail("foo(?x,[bar])");
/* 52 */     test_must_fail("foo(?x,[foo,bar])");
/* 53 */     test_must_fail("foo(?x,[])");
/*    */   }
/*    */   
/*    */   public void testUniqueWithMultipleQuantifiedVariables() throws ParseException, tyRuBa.modes.TypeModeError {
/* 57 */     test_must_succeed("UNIQUE ?x,?y: append(?x,?y,[])");
/* 58 */     test_must_fail("UNIQUE ?x,?y: equals(?x,1), member(?y,[])");
/* 59 */     test_must_fail("UNIQUE ?x,?y: member(?x,[]), equals(?y,a)");
/* 60 */     test_must_succeed("UNIQUE ?x,?y: member(?x,[1]), member(?y,[a])");
/* 61 */     test_must_succeed("UNIQUE ?x,?y: member(?x,[1,1]), member(?y,[a,a])");
/* 62 */     test_must_fail("UNIQUE ?x,?y: equals(?x,1), member(?y,[a,b])");
/* 63 */     test_must_fail("UNIQUE ?x,?y: member(?x,[1,2]), equals(?y,a)");
/* 64 */     test_must_fail("UNIQUE ?x,?y: append(?x,?y,[1,2,3])");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/UniqueQuantifierTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */