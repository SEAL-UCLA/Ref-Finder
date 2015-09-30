/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import junit.framework.TestCase;
/*    */ import junit.framework.TestSuite;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.Multiplicity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModeTest
/*    */   extends TestCase
/*    */ {
/*    */   public ModeTest(String arg0)
/*    */   {
/* 21 */     super(arg0);
/*    */   }
/*    */   
/*    */ 
/*    */   protected void setUp()
/*    */     throws Exception
/*    */   {
/* 28 */     super.setUp();
/*    */   }
/*    */   
/*    */   public void testMultiply() {
/* 32 */     for (int ilo = 0; ilo <= 1; ilo++) {
/* 33 */       Multiplicity ilom = Multiplicity.fromInt(ilo);
/* 34 */       for (int ihi = ilo; ihi <= 2; ihi++) {
/* 35 */         Multiplicity ihim = Multiplicity.fromInt(ihi);
/*    */         
/* 37 */         Mode imode = new Mode(ilom, ihim);
/*    */         
/* 39 */         for (int jlo = 0; jlo <= 1; jlo++) {
/* 40 */           Multiplicity jlom = Multiplicity.fromInt(jlo);
/* 41 */           for (int jhi = jlo; jhi <= 2; jhi++) {
/* 42 */             Multiplicity jhim = Multiplicity.fromInt(jhi);
/* 43 */             Mode jmode = new Mode(jlom, jhim);
/* 44 */             Mode result = imode.multiply(jmode);
/* 45 */             assertEquals(
/* 46 */               imode + " * " + jmode, 
/* 47 */               result, 
/* 48 */               new Mode(
/* 49 */               Multiplicity.fromInt(ilo * jlo), 
/* 50 */               Multiplicity.fromInt(ihi * jhi)));
/* 51 */             assertTrue(
/* 52 */               "Ordercheck for " + 
/* 53 */               imode + 
/* 54 */               " * " + 
/* 55 */               jmode + 
/* 56 */               " = " + 
/* 57 */               result, 
/* 58 */               result.lo.compareTo(result.hi) <= 0);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static TestSuite suite() {
/* 66 */     TestSuite suite = new TestSuite();
/* 67 */     suite.addTestSuite(MultiplicityTest.class);
/* 68 */     suite.addTestSuite(ModeTest.class);
/* 69 */     return suite;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/ModeTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */