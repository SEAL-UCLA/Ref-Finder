/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import junit.framework.TestCase;
/*    */ import tyRuBa.modes.Multiplicity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultiplicityTest
/*    */   extends TestCase
/*    */ {
/* 14 */   Multiplicity zero = Multiplicity.zero;
/* 15 */   Multiplicity one = Multiplicity.one;
/* 16 */   Multiplicity many = Multiplicity.many;
/* 17 */   Multiplicity infinite = Multiplicity.infinite;
/*    */   
/*    */   public MultiplicityTest(String arg0) {
/* 20 */     super(arg0);
/*    */   }
/*    */   
/*    */   public void testObjects()
/*    */   {
/* 25 */     assertFalse(this.zero.equals(this.one));
/* 26 */     assertFalse(this.one.equals(this.zero));
/*    */     
/* 28 */     assertFalse(this.zero.equals(this.many));
/* 29 */     assertFalse(this.many.equals(this.zero));
/*    */     
/* 31 */     assertFalse(this.one.equals(this.many));
/* 32 */     assertFalse(this.many.equals(this.one));
/*    */     
/* 34 */     assertTrue(this.zero.equals(this.zero));
/* 35 */     assertTrue(this.one.equals(this.one));
/* 36 */     assertTrue(this.many.equals(this.many));
/* 37 */     assertTrue(this.infinite.equals(this.infinite));
/*    */   }
/*    */   
/*    */   public void testMultiply() {
/* 41 */     for (int i = 0; i < 5; i++) {
/* 42 */       Multiplicity im = Multiplicity.fromInt(i);
/* 43 */       for (int j = 0; j < 5; j++) {
/* 44 */         Multiplicity jm = Multiplicity.fromInt(j);
/* 45 */         Multiplicity result = im.multiply(jm);
/* 46 */         assertEquals(result, Multiplicity.fromInt(i * j));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void testInfMultiply() {
/* 52 */     assertEquals(this.zero, this.zero.multiply(this.infinite));
/* 53 */     assertEquals(this.zero, this.infinite.multiply(this.zero));
/* 54 */     for (int i = 1; i < 5; i++) {
/* 55 */       Multiplicity im = Multiplicity.fromInt(i);
/* 56 */       assertEquals(this.infinite, im.multiply(this.infinite));
/* 57 */       assertEquals(this.infinite, this.infinite.multiply(im));
/*    */     }
/* 59 */     assertEquals(this.infinite, this.infinite.multiply(this.infinite));
/*    */   }
/*    */   
/*    */   public void testAdd() {
/* 63 */     for (int i = 0; i < 5; i++) {
/* 64 */       Multiplicity im = Multiplicity.fromInt(i);
/* 65 */       for (int j = 0; j < 5; j++) {
/* 66 */         Multiplicity jm = Multiplicity.fromInt(j);
/* 67 */         Multiplicity result = im.add(jm);
/* 68 */         assertEquals(result, Multiplicity.fromInt(i + j));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void testInfAdd() {
/* 74 */     for (int i = 0; i < 5; i++) {
/* 75 */       Multiplicity im = Multiplicity.fromInt(i);
/* 76 */       assertEquals(this.infinite, im.add(this.infinite));
/* 77 */       assertEquals(this.infinite, this.infinite.add(im));
/*    */     }
/* 79 */     assertEquals(this.infinite, this.infinite.add(this.infinite));
/*    */   }
/*    */   
/*    */   public void testCompare() {
/* 83 */     for (int i = 0; i <= 2; i++) {
/* 84 */       Multiplicity im = Multiplicity.fromInt(i);
/* 85 */       assertEquals(im.compareTo(this.infinite), -1);
/* 86 */       assertEquals(this.infinite.compareTo(im), 1);
/* 87 */       for (int j = 0; j <= 2; j++) {
/* 88 */         Multiplicity jm = Multiplicity.fromInt(j);
/* 89 */         if (i == j)
/* 90 */           assertEquals(im.compareTo(jm), 0);
/* 91 */         if (i < j)
/* 92 */           assertEquals(im.compareTo(jm), -1);
/* 93 */         if (i > j)
/* 94 */           assertEquals(im.compareTo(jm), 1);
/*    */       }
/*    */     }
/* 97 */     assertEquals(this.infinite.compareTo(this.infinite), 0);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/MultiplicityTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */