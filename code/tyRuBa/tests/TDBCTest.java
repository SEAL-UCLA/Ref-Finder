/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import junit.framework.TestCase;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.tdbc.Connection;
/*     */ import tyRuBa.tdbc.Insert;
/*     */ import tyRuBa.tdbc.PreparedInsert;
/*     */ import tyRuBa.tdbc.PreparedQuery;
/*     */ import tyRuBa.tdbc.Query;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TDBCTest
/*     */   extends TestCase
/*     */ {
/*     */   Connection conn;
/*     */   
/*     */   protected void setUp()
/*     */     throws Exception
/*     */   {
/*  27 */     super.setUp();
/*     */     
/*  29 */     FrontEnd fe = new FrontEnd(true);
/*     */     
/*  31 */     fe.parse("TYPE Method  AS String");
/*  32 */     fe.parse("TYPE Field   AS String");
/*  33 */     fe.parse("TYPE Member = Method | Field");
/*  34 */     fe.parse("foo :: Method,String \nMODES (F,F) IS NONDET END");
/*     */     
/*  36 */     fe.parse("foo(booh::Method,booh).");
/*     */     
/*  38 */     fe.parse("fooMem :: Member,String \nMODES (F,F) IS NONDET END");
/*     */     
/*  40 */     fe.parse("fooMem(f_booh::Field,f_booh).");
/*  41 */     fe.parse("fooMem(m_booh::Method,m_booh).");
/*     */     
/*  43 */     this.conn = new Connection(fe);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void testQuery()
/*     */     throws Exception
/*     */   {
/*  52 */     Query stat = this.conn.createQuery();
/*  53 */     ResultSet results = stat.executeQuery("string_append(?x,?y,abcde)");
/*  54 */     int count = 0;
/*  55 */     while (results.next()) {
/*  56 */       count++;
/*  57 */       String x = results.getString("?x");
/*  58 */       String y = results.getString("?y");
/*  59 */       assertEquals(x + y, "abcde");
/*     */     }
/*  61 */     assertEquals(count, 6);
/*     */   }
/*     */   
/*     */   public void testNoColsQuery() throws Exception {
/*  65 */     Query stat = this.conn.createQuery();
/*  66 */     ResultSet results = stat.executeQuery("string_append(ab,cde,abcde)");
/*  67 */     assertTrue(results.next());
/*  68 */     assertFalse(results.next());
/*     */     
/*  70 */     results = stat.executeQuery("string_append(ab,cd,abcde)");
/*  71 */     assertFalse(results.next());
/*     */   }
/*     */   
/*     */   public void testPreparedQuery() throws TyrubaException {
/*  75 */     PreparedQuery stat = this.conn.prepareQuery("string_append(!x,!y,?xy)");
/*  76 */     String x = "a b c";
/*  77 */     String y = " d e";
/*  78 */     stat.put("!x", x);
/*  79 */     stat.put("!y", y);
/*  80 */     ResultSet results = stat.executeQuery();
/*  81 */     int count = 0;
/*  82 */     while (results.next()) {
/*  83 */       count++;
/*  84 */       String xy = results.getString("?xy");
/*  85 */       assertEquals(x + y, xy);
/*     */     }
/*  87 */     assertEquals(count, 1);
/*     */   }
/*     */   
/*     */   public void testPreparedQueryMissingVar() throws TyrubaException {
/*  91 */     PreparedQuery stat = this.conn.prepareQuery("string_append(!x,!y,?xy)");
/*  92 */     String x = "a b c";
/*     */     
/*  94 */     stat.put("!x", x);
/*     */     try {
/*  96 */       stat.executeQuery();
/*  97 */       fail("Should have detected the problem that !y has not been put.");
/*     */     }
/*     */     catch (TyrubaException localTyrubaException) {}
/*     */   }
/*     */   
/*     */   public void testPreparedQueryBadType()
/*     */     throws TyrubaException
/*     */   {
/* 105 */     PreparedQuery stat = this.conn.prepareQuery("string_append(!x,!y,?xy)");
/*     */     try {
/* 107 */       stat.put("!x", 123);
/* 108 */       fail("This should have thrown an exception. !m MUST be a string");
/*     */     } catch (TyrubaException e) {
/* 110 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testPreparedQueryBadVar() throws TyrubaException {
/* 115 */     PreparedQuery stat = this.conn.prepareQuery("string_append(!x,!y,?xy)");
/*     */     try {
/* 117 */       stat.put("!m", "abc");
/* 118 */       fail("This should have thrown an exception. !m is not defined");
/*     */     } catch (TyrubaException e) {
/* 120 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testPreparedQueryBadType2() throws TyrubaException {
/* 125 */     PreparedQuery stat = this.conn.prepareQuery("foo(!m::Method,?n)");
/*     */     try {
/* 127 */       stat.put("!m", 123);
/* 128 */       fail("This should have thrown an exception. !m MUST be a string");
/*     */     }
/*     */     catch (TyrubaException localTyrubaException) {}
/*     */   }
/*     */   
/*     */   public void testPreparedQueryUDTypeOut() throws TyrubaException
/*     */   {
/* 135 */     PreparedQuery stat = this.conn.prepareQuery("foo(?m,!n)");
/* 136 */     String n = "booh";
/* 137 */     stat.put("!n", n);
/* 138 */     ResultSet results = stat.executeQuery();
/* 139 */     int count = 0;
/* 140 */     while (results.next()) {
/* 141 */       count++;
/* 142 */       String m = results.getString("?m");
/* 143 */       assertEquals(m, n);
/*     */     }
/* 145 */     assertEquals(count, 1);
/*     */   }
/*     */   
/*     */   public void testPreparedQueryUDTypeOut2() throws TyrubaException {
/* 149 */     PreparedQuery stat = this.conn.prepareQuery("fooMem(?m,!n)");
/* 150 */     String n = "m_booh";
/* 151 */     stat.put("!n", n);
/* 152 */     ResultSet results = stat.executeQuery();
/* 153 */     int count = 0;
/* 154 */     while (results.next()) {
/* 155 */       count++;
/* 156 */       String m = results.getString("?m");
/* 157 */       assertEquals(m, n);
/*     */     }
/* 159 */     assertEquals(count, 1);
/*     */   }
/*     */   
/*     */   public void testPreparedQueryUDTypeIn() throws TyrubaException {
/* 163 */     PreparedQuery stat = this.conn.prepareQuery("foo(!m::Method,?n)");
/* 164 */     String m = "booh";
/* 165 */     stat.put("!m", m);
/* 166 */     ResultSet results = stat.executeQuery();
/* 167 */     int count = 0;
/* 168 */     while (results.next()) {
/* 169 */       count++;
/* 170 */       String n = results.getString("?n");
/* 171 */       assertEquals(m, n);
/*     */     }
/* 173 */     assertEquals(count, 1);
/*     */   }
/*     */   
/*     */   public void testInsert() throws Exception {
/* 177 */     Insert ins = this.conn.createInsert();
/* 178 */     ins.executeInsert("foo(bih::Method,bah).");
/*     */     
/* 180 */     Query q = this.conn.createQuery();
/* 181 */     ResultSet results = q.executeQuery("foo(bih::Method,?bah).");
/*     */     
/* 183 */     int count = 0;
/* 184 */     while (results.next()) {
/* 185 */       count++;
/* 186 */       String bah = results.getString("?bah");
/* 187 */       assertEquals(bah, "bah");
/*     */     }
/* 189 */     assertEquals(count, 1);
/*     */   }
/*     */   
/*     */   public void testPreparedInsert() throws Exception {
/* 193 */     PreparedInsert ins = this.conn.prepareInsert("foo(clock::Method,!duh).");
/*     */     
/* 195 */     ins.put("!duh", "bim");
/* 196 */     ins.executeInsert();
/*     */     
/* 198 */     ins.put("!duh", "bam");
/* 199 */     ins.executeInsert();
/*     */     
/* 201 */     ins.put("!duh", "bom");
/* 202 */     ins.executeInsert();
/*     */     
/* 204 */     Query q = this.conn.createQuery();
/* 205 */     ResultSet results = q.executeQuery("foo(clock::Method,?sound).");
/*     */     
/* 207 */     int count = 0;
/* 208 */     while (results.next()) {
/* 209 */       count++;
/* 210 */       String sound = results.getString("?sound");
/* 211 */       assertTrue((sound.length() == 3) && (sound.startsWith("b")) && (sound.endsWith("m")));
/*     */     }
/* 213 */     assertEquals(count, 3);
/*     */   }
/*     */   
/*     */   public void testPreparedInsertMissingVar() throws Exception {
/* 217 */     PreparedInsert ins = this.conn.prepareInsert("foo(!dah::Method,!duh).");
/*     */     
/* 219 */     ins.put("!duh", "abc");
/*     */     try {
/* 221 */       ins.executeInsert();
/* 222 */       fail("Should have made an error: the variable !dah has not been put");
/*     */     } catch (TyrubaException e) {
/* 224 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testPreparedInsertBadType() throws Exception {
/* 229 */     PreparedInsert ins = this.conn.prepareInsert("foo(clock::Method,!duh).");
/*     */     try
/*     */     {
/* 232 */       ins.put("!duh", 1);
/* 233 */       fail("Should have made an error: he variable !duh should be a string.");
/*     */     } catch (TyrubaException e) {
/* 235 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testPreparedInsertBadVar() throws Exception {
/* 240 */     PreparedInsert ins = this.conn.prepareInsert("foo(clock::Method,!duh).");
/*     */     try
/*     */     {
/* 243 */       ins.put("!dah", "abc");
/* 244 */       fail("Should have made an error: the variable !dah is unknown");
/*     */     } catch (TyrubaException e) {
/* 246 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testPreparedInsertUDType() throws Exception {
/* 251 */     PreparedInsert ins = this.conn.prepareInsert("foo(!dah::Method,!duh).");
/*     */     
/* 253 */     ins.put("!dah", "abc");
/* 254 */     ins.put("!duh", "abc");
/*     */     
/* 256 */     ins.executeInsert();
/*     */     
/* 258 */     Query q = this.conn.createQuery();
/* 259 */     ResultSet results = q.executeQuery("foo(?out,abc).");
/*     */     
/* 261 */     int count = 0;
/* 262 */     while (results.next()) {
/* 263 */       count++;
/* 264 */       String out = results.getString("?out");
/* 265 */       assertEquals(out, "abc");
/*     */     }
/* 267 */     assertEquals(count, 1);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/TDBCTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */