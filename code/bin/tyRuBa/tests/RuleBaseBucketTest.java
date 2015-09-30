/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.SimpleRuleBaseBucket;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ public class RuleBaseBucketTest extends TyrubaTest
/*     */ {
/*     */   SimpleRuleBaseBucket bucket;
/*     */   SimpleRuleBaseBucket otherBucket;
/*     */   
/*     */   public void setUp() throws Exception
/*     */   {
/*  14 */     tyRuBa.engine.RuleBase.silent = true;
/*  15 */     super.setUp();
/*  16 */     this.bucket = new SimpleRuleBaseBucket(this.frontend);
/*  17 */     this.otherBucket = new SimpleRuleBaseBucket(this.frontend);
/*     */   }
/*     */   
/*     */   public void testOutdateSemiDetPersistentFact() throws tyRuBa.parser.ParseException, TypeModeError {
/*  21 */     this.frontend.parse("foo :: String, String \nPERSISTENT MODES (B,F) IS SEMIDET END \n");
/*     */     
/*  23 */     this.bucket.addStuff("foo(bucket,buck).");
/*  24 */     this.bucket.addStuff("foo(target,targ).");
/*  25 */     test_resultcount("foo(bucket,?x)", 1);
/*  26 */     test_resultcount("foo(bucket,buck)", 1);
/*     */     
/*  28 */     this.bucket.setOutdated();
/*     */     
/*  30 */     test_resultcount("foo(bucket,?x)", 1);
/*  31 */     test_resultcount("foo(bucket,buck)", 1);
/*     */   }
/*     */   
/*     */   public void testOutdateSemiDetPersistentFact2() throws tyRuBa.parser.ParseException, TypeModeError {
/*  35 */     this.frontend.parse("foo :: String, String \nPERSISTENT MODES (B,F) IS SEMIDET END \n");
/*     */     
/*  37 */     this.bucket.addStuff("foo(bucket,buck).");
/*  38 */     this.bucket.addStuff("foo(target,targ).");
/*  39 */     test_resultcount("foo(bucket,?x)", 1);
/*  40 */     test_resultcount("foo(bucket,buck)", 1);
/*     */     
/*  42 */     this.bucket.clearStuff();
/*  43 */     this.bucket.addStuff("foo(bucket,buck2).");
/*  44 */     this.bucket.addStuff("foo(target,targ2).");
/*  45 */     test_resultcount("foo(bucket,?x)", 1);
/*  46 */     test_resultcount("foo(bucket,buck2)", 1);
/*     */   }
/*     */   
/*     */   public void testOutdateSemiDetPersistentFact3() throws tyRuBa.parser.ParseException, TypeModeError
/*     */   {
/*  51 */     this.frontend.parse("foo :: String, String \nPERSISTENT MODES (B,F) IS SEMIDET END \n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     this.bucket.addStuff("foo(a#bucket,buck).");
/*  57 */     this.bucket.addStuff("foo(a#target,targ).");
/*  58 */     test_resultcount("foo(a#bucket,?x)", 1);
/*  59 */     test_resultcount("foo(a#bucket,buck)", 1);
/*     */     
/*  61 */     this.bucket.clearStuff();
/*  62 */     this.bucket.addStuff("foo(b#bucket,buck).");
/*  63 */     this.bucket.addStuff("foo(b#target,targ).");
/*     */     
/*  65 */     test_resultcount("foo(b#bucket,?x)", 1);
/*  66 */     test_resultcount("foo(b#bucket,buck)", 1);
/*     */     
/*  68 */     this.bucket.clearStuff();
/*  69 */     this.bucket.addStuff("foo(a#bucket,buck1).");
/*  70 */     this.bucket.addStuff("foo(a#target,targ1).");
/*     */     
/*  72 */     test_resultcount("foo(a#bucket,?x)", 1);
/*  73 */     test_resultcount("foo(a#bucket,buck1)", 1);
/*     */   }
/*     */   
/*     */   public void testGlobalFact()
/*     */     throws tyRuBa.parser.ParseException, TypeModeError
/*     */   {
/*  79 */     this.frontend.parse("foo :: String \nMODES (F) IS NONDET END \n");
/*     */     
/*  81 */     this.bucket.addStuff("foo(bucket).");
/*  82 */     this.frontend.parse("foo(frontend).");
/*     */     
/*  84 */     test_must_succeed("foo(bucket)");
/*  85 */     test_must_succeed("foo(frontend)");
/*  86 */     test_must_succeed("foo(bucket)", this.bucket);
/*  87 */     test_must_succeed("foo(frontend)", this.bucket);
/*  88 */     test_must_fail("foo(bad)");
/*     */     
/*  90 */     this.bucket.clearStuff();
/*     */     
/*  92 */     test_must_fail("foo(bucket)");
/*  93 */     test_must_fail("foo(bucket)", this.bucket);
/*  94 */     test_must_succeed("foo(frontend)");
/*  95 */     test_must_succeed("foo(frontend)", this.bucket);
/*  96 */     test_must_fail("foo(c)");
/*     */   }
/*     */   
/*     */   public void testAllCleared() throws tyRuBa.parser.ParseException, TypeModeError {
/* 100 */     this.frontend.parse("foo :: String \nMODES (F) IS NONDET END \n");
/*     */     
/*     */ 
/* 103 */     this.bucket.addStuff("foo(bucket).");
/* 104 */     this.bucket.addStuff("foo(bucket2).");
/* 105 */     this.bucket.addStuff("foo(bucket3).");
/*     */     
/* 107 */     test_must_succeed("foo(bucket).");
/*     */     
/* 109 */     this.bucket.clearStuff();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 114 */     test_must_fail("foo(?x).");
/*     */   }
/*     */   
/*     */   public void testLocalFact() throws tyRuBa.parser.ParseException, TypeModeError {
/* 118 */     this.bucket.addStuff("bar :: String");
/* 119 */     this.bucket.addStuff("bar(bucket).");
/* 120 */     test_must_succeed("bar(bucket)", this.bucket);
/*     */     try {
/* 122 */       test_must_fail("bar(bucket)");
/* 123 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to the frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */     
/*     */     try
/*     */     {
/* 129 */       this.frontend.parse("bar(frontend).");
/* 130 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to the frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */ 
/* 135 */     this.bucket.clearStuff();
/*     */     try {
/* 137 */       test_must_fail("bar(bucket).", this.bucket);
/* 138 */       fail("This should have thrown a TypeModeError because bucket has been cleared and the predicate bar is no longer declared in bucket.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */   }
/*     */   
/*     */   public void testDuplicate() throws tyRuBa.parser.ParseException, TypeModeError
/*     */   {
/* 145 */     this.frontend.parse("foobar :: String");
/*     */     try {
/* 147 */       this.bucket.parse("foobar :: String");
/* 148 */       fail("This should have thrown a TypeModeError since foobar is declared in the frontend, bucket cannot declare it again.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testGlobalRule() throws tyRuBa.parser.ParseException, TypeModeError
/*     */   {
/* 155 */     this.frontend.parse("foo :: Object");
/* 156 */     this.bucket.addStuff("foo(?x) :- equals(?x, bucket).");
/* 157 */     this.frontend.parse("foo(?x) :- equals(?x, 1).");
/*     */     
/* 159 */     test_must_succeed("foo(bucket)");
/* 160 */     test_must_succeed("foo(1)");
/* 161 */     test_must_succeed("foo(bucket)", this.bucket);
/* 162 */     test_must_succeed("foo(1)", this.bucket);
/* 163 */     test_must_fail("foo(bad)");
/*     */     
/* 165 */     this.bucket.clearStuff();
/*     */     
/* 167 */     test_must_fail("foo(bucket)");
/* 168 */     test_must_fail("foo(bucket)", this.bucket);
/* 169 */     test_must_succeed("foo(1)");
/* 170 */     test_must_succeed("foo(1)", this.bucket);
/* 171 */     test_must_fail("foo(c)");
/*     */   }
/*     */   
/*     */   public void testLocalRule() throws tyRuBa.parser.ParseException, TypeModeError {
/* 175 */     this.bucket.addStuff("bar :: String");
/* 176 */     this.bucket.addStuff("bar(?x) :- equals(?x, bucket).");
/* 177 */     test_must_succeed("bar(bucket)", this.bucket);
/*     */     try {
/* 179 */       test_must_fail("bar(bucket)");
/* 180 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */     try
/*     */     {
/* 186 */       this.frontend.parse("bar(?x) :- equals(?x, frontend).");
/* 187 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */     
/*     */ 
/* 192 */     this.bucket.clearStuff();
/*     */     try {
/* 194 */       test_must_fail("bar(bucket).", this.bucket);
/* 195 */       fail("This should have thrown a TypeModeError because bucket has been cleared and the predicate bar is no longer declared in bucket.");
/*     */     } catch (TypeModeError e) {
/* 197 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void testGlobalFactWithMultipleBuckets() throws tyRuBa.parser.ParseException, TypeModeError {
/* 202 */     this.frontend.parse("foo :: String");
/* 203 */     this.frontend.parse("foo(frontend).");
/* 204 */     this.otherBucket.addStuff("foo(otherBucket).");
/* 205 */     this.bucket.addStuff("foo(bucket).");
/*     */     
/* 207 */     test_must_succeed("foo(frontend)");
/* 208 */     test_must_succeed("foo(frontend)", this.otherBucket);
/* 209 */     test_must_succeed("foo(frontend)", this.bucket);
/*     */     
/* 211 */     test_must_succeed("foo(otherBucket)");
/* 212 */     test_must_succeed("foo(otherBucket)", this.otherBucket);
/* 213 */     test_must_succeed("foo(otherBucket)", this.bucket);
/*     */     
/* 215 */     test_must_succeed("foo(bucket)");
/* 216 */     test_must_succeed("foo(bucket)", this.otherBucket);
/* 217 */     test_must_succeed("foo(bucket)", this.bucket);
/*     */     
/* 219 */     this.otherBucket.clearStuff();
/*     */     
/* 221 */     test_must_succeed("foo(frontend)");
/* 222 */     test_must_succeed("foo(frontend)", this.otherBucket);
/* 223 */     test_must_succeed("foo(frontend)", this.bucket);
/*     */     
/* 225 */     test_must_fail("foo(otherBucket)");
/* 226 */     test_must_fail("foo(otherBucket)", this.otherBucket);
/* 227 */     test_must_fail("foo(otherBucket)", this.bucket);
/*     */     
/* 229 */     test_must_succeed("foo(bucket)");
/* 230 */     test_must_succeed("foo(bucket)", this.otherBucket);
/* 231 */     test_must_succeed("foo(bucket)", this.bucket);
/*     */   }
/*     */   
/*     */   public void testLocalFactWithMultipleBuckets() throws tyRuBa.parser.ParseException, TypeModeError {
/* 235 */     this.bucket.addStuff("bar :: String");
/* 236 */     this.bucket.addStuff("bar(bucket).");
/* 237 */     test_must_succeed("bar(bucket)", this.bucket);
/*     */     try
/*     */     {
/* 240 */       test_must_fail("bar(bucket)");
/* 241 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */     
/*     */     try
/*     */     {
/* 247 */       test_must_fail("bar(bucket)", this.otherBucket);
/* 248 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to otherBucket.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */ 
/* 253 */     this.otherBucket.addStuff("bar :: String");
/* 254 */     this.otherBucket.addStuff("bar(otherBucket).");
/* 255 */     test_must_succeed("bar(otherBucket)", this.otherBucket);
/* 256 */     test_must_fail("bar(otherBucket)", this.bucket);
/*     */     try
/*     */     {
/* 259 */       test_must_fail("bar(otherBucket)");
/* 260 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */     
/*     */ 
/* 265 */     test_must_fail("bar(bucket)", this.otherBucket);
/*     */   }
/*     */   
/*     */   public void testGlobalRuleWithMultipleBuckets() throws tyRuBa.parser.ParseException, TypeModeError {
/* 269 */     this.frontend.parse("foo :: Object");
/* 270 */     this.frontend.parse("foo(?x) :- equals(?x,1).");
/* 271 */     this.otherBucket.addStuff("foo(?x) :- equals(?x,10.1).");
/* 272 */     this.bucket.addStuff("foo(?x) :- equals(?x,bucket).");
/*     */     
/* 274 */     test_must_succeed("foo(1)");
/* 275 */     test_must_succeed("foo(1)", this.otherBucket);
/* 276 */     test_must_succeed("foo(1)", this.bucket);
/*     */     
/* 278 */     test_must_succeed("foo(10.1)");
/* 279 */     test_must_succeed("foo(10.1)", this.otherBucket);
/* 280 */     test_must_succeed("foo(10.1)", this.bucket);
/*     */     
/* 282 */     test_must_succeed("foo(bucket)");
/* 283 */     test_must_succeed("foo(bucket)", this.otherBucket);
/* 284 */     test_must_succeed("foo(bucket)", this.bucket);
/*     */     
/* 286 */     this.otherBucket.clearStuff();
/*     */     
/* 288 */     test_must_succeed("foo(1)");
/* 289 */     test_must_succeed("foo(1)", this.otherBucket);
/* 290 */     test_must_succeed("foo(1)", this.bucket);
/*     */     
/* 292 */     test_must_fail("foo(10.1)");
/* 293 */     test_must_fail("foo(10.1)", this.otherBucket);
/* 294 */     test_must_fail("foo(10.1)", this.bucket);
/*     */     
/* 296 */     test_must_succeed("foo(bucket)");
/* 297 */     test_must_succeed("foo(bucket)", this.otherBucket);
/* 298 */     test_must_succeed("foo(bucket)", this.bucket);
/*     */   }
/*     */   
/*     */   public void testLocalRuleWithMultipleBuckets() throws tyRuBa.parser.ParseException, TypeModeError {
/* 302 */     this.bucket.addStuff("bar :: String");
/* 303 */     this.bucket.addStuff("bar(?x) :- equals(?x,bucket).");
/* 304 */     test_must_succeed("bar(bucket)", this.bucket);
/*     */     try
/*     */     {
/* 307 */       test_must_fail("bar(bucket)");
/* 308 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */     
/*     */     try
/*     */     {
/* 314 */       test_must_fail("bar(bucket)", this.otherBucket);
/* 315 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to otherBucket.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */ 
/* 320 */     this.otherBucket.addStuff("bar :: String");
/* 321 */     this.otherBucket.addStuff("bar(?x) :- equals(?x,otherBucket).");
/* 322 */     test_must_succeed("bar(otherBucket)", this.otherBucket);
/* 323 */     test_must_fail("bar(otherBucket)", this.bucket);
/*     */     try
/*     */     {
/* 326 */       test_must_fail("bar(otherBucket)");
/* 327 */       fail("This should have thrown a TypeModeError because the predicate bar is unknown to frontend.");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */     
/*     */ 
/* 332 */     test_must_fail("bar(bucket)", this.otherBucket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void testRuleBaseCollectionBug()
/*     */     throws tyRuBa.parser.ParseException, TypeModeError
/*     */   {
/* 344 */     this.frontend.parse("bar :: String,String MODES (F,F) IS NONDET END");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 349 */     this.bucket.addStuff("bar(?x,?z) :- bar(?x,?y), bar(?y,?z).");
/* 350 */     this.frontend.updateBuckets();
/* 351 */     this.bucket.setOutdated();
/* 352 */     this.bucket.addStuff("bar(?x,?z) :- bar(?x,?y), bar(?y,?z), bar(?x,?z).");
/* 353 */     this.frontend.updateBuckets();
/* 354 */     this.bucket.setOutdated();
/* 355 */     this.frontend.updateBuckets();
/*     */   }
/*     */   
/*     */   public void testRuleBaseDestroy() throws tyRuBa.parser.ParseException, TypeModeError {
/* 359 */     this.frontend.parse("factFrom :: String MODES (F) IS NONDET END");
/*     */     
/* 361 */     this.bucket.addStuff("factFrom(bucket).");
/* 362 */     this.otherBucket.addStuff("factFrom(otherBucket).");
/* 363 */     test_resultcount("factFrom(?buck)", 2);
/* 364 */     this.otherBucket.destroy();
/* 365 */     test_resultcount("factFrom(?buck)", 1);
/* 366 */     this.bucket.destroy();
/* 367 */     test_resultcount("factFrom(?buck)", 0);
/*     */   }
/*     */   
/*     */   public RuleBaseBucketTest(String arg0) {
/* 371 */     super(arg0);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/RuleBaseBucketTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */