/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ 
/*    */ public class TestFilterTest extends TyrubaTest
/*    */ {
/*    */   public TestFilterTest(String arg0)
/*    */   {
/*  9 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void setUp() throws Exception {
/* 13 */     TyrubaTest.initfile = true;
/* 14 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testTestFilter() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 18 */     this.frontend.parse("test_append_ffb :: [?t]\nMODES (B) IS DET END\n");
/*    */     
/* 20 */     this.frontend.parse("test_append_ffb(?z) :- TEST(EXISTS ?x,?y : append(?x, ?y, ?z)).");
/*    */     
/*    */ 
/* 23 */     test_must_succeed("test_append_ffb([1,2,3])");
/*    */     
/* 25 */     this.frontend.parse("test_append_bbf :: [?t],[?t]\nMODES (B,B) IS DET END\n");
/*    */     
/* 27 */     this.frontend.parse("test_append_bbf(?x, ?y) :- TEST(EXISTS ?z : append(?x,?y,?z)).");
/*    */     
/*    */ 
/* 30 */     test_must_succeed("test_append_bbf([1],[2])");
/*    */     
/* 32 */     this.frontend.parse("test_list_ref_bbf :: =Integer, [?t]\n");
/* 33 */     this.frontend.parse("test_list_ref_bbf(?x,?y) :- TEST(EXISTS ?z : list_ref(?x,?y,?z)).");
/*    */     
/*    */ 
/* 36 */     test_must_succeed("test_list_ref_bbf(0,[1,2])");
/* 37 */     test_must_fail("test_list_ref_bbf(4,[1])");
/*    */     
/* 39 */     this.frontend.parse("test_list_ref_fbb :: [?t], ?t\n");
/* 40 */     this.frontend.parse("test_list_ref_fbb(?y,?z) :- TEST(EXISTS ?x : list_ref(?x,?y,?z)).");
/*    */     
/*    */ 
/* 43 */     test_must_succeed("test_list_ref_fbb([1,2,3],2)");
/* 44 */     test_must_fail("test_list_ref_fbb([1],2)");
/*    */     
/* 46 */     test_must_succeed("TEST(append(?,?,[1,2,3]))");
/* 47 */     test_must_fail("TEST(member(?,[]))");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/TestFilterTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */