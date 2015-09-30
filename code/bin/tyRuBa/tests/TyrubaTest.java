/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import junit.framework.TestCase;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.ProgressMonitor;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.util.ElementSource;
/*     */ 
/*     */ public abstract class TyrubaTest
/*     */   extends TestCase
/*     */ {
/*     */   FrontEnd frontend;
/*  19 */   public static boolean initfile = true;
/*     */   
/*     */   public TyrubaTest(String arg0) {
/*  22 */     super(arg0);
/*     */   }
/*     */   
/*     */   protected void setUp() throws Exception {
/*  26 */     super.setUp();
/*  27 */     this.frontend = new FrontEnd(initfile, true);
/*     */   }
/*     */   
/*     */   protected void setUpNoFrontend() throws Exception {
/*  31 */     super.setUp();
/*     */   }
/*     */   
/*     */   protected void setUp(ProgressMonitor mon) throws Exception {
/*  35 */     super.setUp();
/*  36 */     this.frontend = new FrontEnd(initfile, mon);
/*     */   }
/*     */   
/*     */   void test_must_succeed(String query) throws ParseException, TypeModeError {
/*  40 */     ElementSource result = this.frontend.frameQuery(query);
/*  41 */     assertTrue(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_succeed(String query, QueryEngine qe) throws ParseException, TypeModeError
/*     */   {
/*  46 */     ElementSource result = qe.frameQuery(query);
/*  47 */     assertTrue(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_equal(String query, String var, String expected) throws ParseException, TypeModeError {
/*  51 */     ElementSource result = this.frontend.varQuery(query, var);
/*  52 */     assertEquals(this.frontend.makeTermFromString(expected), result.nextElement());
/*  53 */     assertFalse(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_equal(String query, String var, Object expected) throws ParseException, TypeModeError {
/*  57 */     ElementSource result = this.frontend.varQuery(query, var);
/*  58 */     assertEquals(expected, ((RBTerm)result.nextElement()).up());
/*  59 */     assertFalse(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_equal(String query, String[] vars, String[] expected) throws ParseException, TypeModeError {
/*  63 */     for (int i = 0; i < vars.length; i++) {
/*  64 */       ElementSource result = this.frontend.varQuery(query, vars[i]);
/*  65 */       assertEquals(this.frontend.makeTermFromString(expected[i]), result.nextElement());
/*  66 */       assertFalse(result.hasMoreElements());
/*     */     }
/*     */   }
/*     */   
/*     */   void test_must_fail(String query) throws ParseException, TypeModeError {
/*  71 */     ElementSource result = this.frontend.frameQuery(query);
/*  72 */     assertFalse(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_fail(String query, QueryEngine qe) throws ParseException, TypeModeError
/*     */   {
/*  77 */     ElementSource result = qe.frameQuery(query);
/*  78 */     assertFalse(result.hasMoreElements());
/*     */   }
/*     */   
/*     */   void test_must_findall(String query, String var, String[] expected) throws ParseException, TypeModeError {
/*  82 */     Set expectedSet = new HashSet();
/*  83 */     for (int i = 0; i < expected.length; i++) {
/*  84 */       expectedSet.add(this.frontend.makeTermFromString(expected[i]));
/*     */     }
/*  86 */     ElementSource result = this.frontend.varQuery(query, var);
/*  87 */     while (result.hasMoreElements()) {
/*  88 */       Object res = result.nextElement();
/*  89 */       boolean ok = expectedSet.remove(res);
/*  90 */       assertTrue("Unexpected result: " + res, ok);
/*     */     }
/*  92 */     assertTrue("Expected results not found: " + expectedSet, expectedSet.isEmpty());
/*     */   }
/*     */   
/*     */   void test_must_findall(String query, String[] vars, String[][] expected) throws ParseException, TypeModeError
/*     */   {
/*  97 */     for (int i = 0; i < vars.length; i++) {
/*  98 */       ElementSource result = this.frontend.varQuery(query, vars[i]);
/*  99 */       for (int j = 0; j < expected.length; j++) {
/* 100 */         assertTrue(result.hasMoreElements());
/* 101 */         assertEquals(this.frontend.makeTermFromString(expected[j][i]), 
/* 102 */           result.nextElement());
/*     */       }
/* 104 */       assertFalse(result.hasMoreElements());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void test_resultcount(String query, int numresults) throws ParseException, TypeModeError {
/* 109 */     int counter = get_resultcount(query);
/* 110 */     assertEquals("Expected number of results:", numresults, counter);
/*     */   }
/*     */   
/*     */   protected int get_resultcount(String query) throws ParseException, TypeModeError
/*     */   {
/* 115 */     ElementSource result = this.frontend.frameQuery(query);
/* 116 */     int counter = 0;
/* 117 */     while (result.hasMoreElements()) {
/* 118 */       counter++;
/* 119 */       result.nextElement();
/*     */     }
/* 121 */     return counter;
/*     */   }
/*     */   
/*     */   protected void deleteDirectory(File dir) {
/* 125 */     if (dir.isDirectory()) {
/* 126 */       File[] children = dir.listFiles();
/* 127 */       for (int i = 0; i < children.length; i++) {
/* 128 */         deleteDirectory(children[i]);
/*     */       }
/*     */     }
/* 131 */     dir.delete();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/TyrubaTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */