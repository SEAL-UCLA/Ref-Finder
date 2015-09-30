/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Random;
/*     */ import junit.framework.Assert;
/*     */ import tyRuBa.engine.BackupFailedException;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.RuleBaseBucket;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FactBaseTest
/*     */   extends TyrubaTest
/*     */ {
/* 156 */   static String test_space = "test_space";
/*     */   
/*     */ 
/*     */   private static final boolean regenrubs = true;
/*     */   
/* 161 */   private static int init_numatoms = 10;
/*     */   
/*     */ 
/*     */ 
/* 165 */   private static int maxarity = 4;
/*     */   
/*     */   private int bucketLoads;
/*     */   
/* 169 */   File factstore = new File(test_space, "fact_store");
/*     */   
/*     */ 
/* 172 */   String declarations_fle = test_space + "/declarations.rub";
/*     */   
/*     */   String[] bucket_fle;
/*     */   
/*     */   private RuleBaseBucket[] buckets;
/*     */   
/* 178 */   String[] test_preds = {
/* 179 */     "foo", "bar", "zor", "snol", "brol", "wols" };
/*     */   
/*     */   int[] pred_arity;
/*     */   
/*     */   String[] test_atoms;
/*     */   
/*     */   String[] initial_test_atoms;
/*     */   
/*     */ 
/*     */   public FactBaseTest(String arg0)
/*     */   {
/* 190 */     super(arg0);
/*     */   }
/*     */   
/*     */   protected void setUp(boolean reconnect) throws Exception
/*     */   {
/* 195 */     FrontEnd old_frontend = this.frontend;
/* 196 */     if ((reconnect) && (this.frontend != null)) {
/*     */       try
/*     */       {
/* 199 */         this.frontend.backupFactBase();
/*     */ 
/*     */       }
/*     */       catch (BackupFailedException e)
/*     */       {
/* 204 */         super.setUpNoFrontend();
/* 205 */         this.frontend = old_frontend;
/* 206 */         return;
/*     */       }
/*     */     }
/*     */     
/* 210 */     super.setUpNoFrontend();
/*     */     
/* 212 */     if (!reconnect) {
/* 213 */       File test_dir = new File(test_space);
/*     */       
/* 215 */       makeEmptyDir(test_dir);
/* 216 */       makeEmptyDir(this.factstore);
/* 217 */       this.frontend = new FrontEnd(true, this.factstore, true, null, true, false);
/*     */     } else {
/* 219 */       this.frontend = new FrontEnd(true, this.factstore, true, null, false, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */     if (!reconnect) {
/* 228 */       generateRubFiles();
/*     */     }
/*     */     
/*     */ 
/* 232 */     this.frontend.load(this.declarations_fle);
/*     */     
/*     */ 
/* 235 */     this.buckets = new RuleBaseBucket[init_numatoms];
/* 236 */     for (int i = 0; i < this.bucket_fle.length; i++) {
/* 237 */       System.err.println("Making bucket: " + this.bucket_fle[i]);
/* 238 */       this.buckets[i] = new FactBaseTest.RubFileBucket(this, this.frontend, this.bucket_fle[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void generateRubFiles()
/*     */     throws IOException
/*     */   {
/* 245 */     int countFacts = 0;
/* 246 */     PrintWriter declarations = makeFile(this.declarations_fle);
/*     */     
/* 248 */     for (int i = 0; i < this.test_preds.length; i++) {
/* 249 */       declarations.print(this.test_preds[i] + " :: ");
/*     */       
/* 251 */       for (int j = 0; j < this.pred_arity[i]; j++) {
/* 252 */         if (j > 0)
/* 253 */           declarations.print(", ");
/* 254 */         declarations.print("String");
/*     */       }
/* 256 */       declarations.println();
/*     */       
/* 258 */       declarations.print("PERSISTENT MODES (");
/* 259 */       for (int j = 0; j < this.pred_arity[i]; j++) {
/* 260 */         if (j > 0)
/* 261 */           declarations.print(", ");
/* 262 */         declarations.print("F");
/*     */       }
/* 264 */       declarations.println(") IS NONDET END");
/*     */     }
/* 266 */     declarations.close();
/*     */     
/* 268 */     PrintWriter[] bucket = new PrintWriter[this.bucket_fle.length];
/* 269 */     for (int i = 0; i < bucket.length; i++) {
/* 270 */       bucket[i] = makeFile(this.bucket_fle[i]);
/*     */     }
/*     */     
/* 273 */     for (int currPred = 0; currPred < this.test_preds.length; currPred++) {
/* 274 */       int[] currAtom = new int[this.pred_arity[currPred]];
/* 275 */       for (int i = 0; i < currAtom.length; i++) {
/* 276 */         currAtom[i] = 0;
/*     */       }
/* 278 */       boolean stop = false;
/* 279 */       while (!stop) {
/* 280 */         int currBucket = currAtom[(currAtom.length - 1)];
/* 281 */         bucket[currBucket].print(this.test_preds[currPred] + "(");
/* 282 */         for (int i = 0; i < currAtom.length; i++) {
/* 283 */           if (i > 0)
/* 284 */             bucket[currBucket].print(",");
/* 285 */           bucket[currBucket].print(this.test_atoms[currAtom[i]]);
/*     */         }
/* 287 */         bucket[currBucket].println(").");
/*     */         
/* 289 */         countFacts++;
/*     */         
/* 291 */         stop = nextParamList(0, currAtom);
/*     */       }
/*     */     }
/*     */     
/* 295 */     for (int i = 0; i < bucket.length; i++) {
/* 296 */       bucket[i].close();
/*     */     }
/* 298 */     System.err.println("========= generated " + countFacts + " test facts =======");
/*     */   }
/*     */   
/*     */   protected void setUp() throws Exception
/*     */   {
/* 303 */     this.bucketLoads = 0;
/*     */     
/*     */ 
/* 306 */     this.test_atoms = new String[init_numatoms];
/* 307 */     this.initial_test_atoms = this.test_atoms;
/* 308 */     for (int i = 0; i < this.test_atoms.length; i++) {
/* 309 */       this.test_atoms[i] = (this.test_preds[(i % this.test_preds.length)] + i);
/*     */     }
/*     */     
/* 312 */     this.pred_arity = new int[this.test_preds.length];
/* 313 */     for (int i = 0; i < this.test_preds.length; i++) {
/* 314 */       int arity = i % maxarity + 1;
/* 315 */       this.pred_arity[i] = arity;
/*     */     }
/*     */     
/*     */ 
/* 319 */     this.bucket_fle = new String[this.test_atoms.length];
/* 320 */     for (int i = 0; i < this.test_atoms.length; i++) {
/* 321 */       this.bucket_fle[i] = (test_space + "/" + this.test_atoms[i] + ".rub");
/*     */     }
/*     */     
/* 324 */     setUp(false);
/*     */   }
/*     */   
/*     */   protected void tearDown() throws Exception {
/* 328 */     super.tearDown();
/*     */   }
/*     */   
/*     */   private void makeEmptyDir(File dir) {
/* 332 */     if (dir.exists())
/* 333 */       deleteDir(dir);
/* 334 */     dir.mkdir();
/*     */   }
/*     */   
/*     */   private boolean deleteDir(File dir) {
/* 338 */     if (dir.isDirectory()) {
/* 339 */       String[] children = dir.list();
/* 340 */       for (int i = 0; i < children.length; i++) {
/* 341 */         boolean success = deleteDir(new File(dir, children[i]));
/* 342 */         if (!success) {
/* 343 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 349 */     return dir.delete();
/*     */   }
/*     */   
/*     */   public void testRandomConcurrency() throws Throwable {
/* 353 */     int workLoadSize = 
/*     */     
/* 355 */       40;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 366 */     doRandomQueries(1);
/*     */     
/* 368 */     FactBaseTest.TesterThread[] thread = new FactBaseTest.TesterThread[10];
/* 369 */     for (int i = 0; i < thread.length; i++) {
/* 370 */       thread[i] = new FactBaseTest.DoRandomThingsThread(this, i, 40);
/* 371 */       thread[i].start();
/* 372 */       System.out.println("Thread " + i + " started");
/*     */     }
/*     */     
/* 375 */     for (int i = 0; i < thread.length; i++) {
/* 376 */       thread[i].join();
/* 377 */       if (thread[i].crash != null)
/* 378 */         throw thread[i].crash;
/* 379 */       System.out.println("Thread " + i + " ended");
/*     */     }
/*     */   }
/*     */   
/*     */   public void testConcurrentKilling() throws Throwable {
/* 384 */     System.err.println("====== TEST: testConcurrentKilling ===");
/* 385 */     int workLoadSize = 20;
/* 386 */     int numThreads = 20;
/*     */     
/*     */ 
/* 389 */     doRandomQueries(1);
/*     */     
/*     */ 
/* 392 */     FactBaseTest.TesterThread[] thread = new FactBaseTest.TesterThread[20];
/* 393 */     for (int i = 0; i < thread.length - 1; i++) {
/* 394 */       thread[i] = new FactBaseTest.RandomQueriesThread(this, i, 20);
/* 395 */       thread[i].start();
/* 396 */       System.out.println("Thread " + i + " started");
/*     */     }
/*     */     
/* 399 */     thread[(thread.length - 1)] = new FactBaseTest.KillingThread(this, "killing");
/* 400 */     thread[(thread.length - 1)].start();
/* 401 */     System.out.println("Killing thread started");
/*     */     
/* 403 */     for (int i = 0; i < thread.length; i++) {
/* 404 */       thread[i].join();
/* 405 */       if (thread[i].crash != null)
/* 406 */         throw thread[i].crash;
/* 407 */       System.out.println("Thread " + i + " ended");
/*     */     }
/*     */   }
/*     */   
/*     */   public void testReconnecting() throws Exception
/*     */   {
/* 413 */     int expectedBucketLoads = numbuckets();
/*     */     
/* 415 */     doSomeQueries();
/* 416 */     setUp(true);
/* 417 */     doSomeQueries();
/* 418 */     for (int i = 2; i <= 5; i++) {
/* 419 */       expectedBucketLoads += outdateSomebuckets(0, i);
/*     */       
/* 421 */       setUp(true);
/* 422 */       System.err.println(i);
/* 423 */       doSomeQueries();
/*     */     }
/*     */     
/* 426 */     assertEquals("Number of buckets loaded", expectedBucketLoads, this.bucketLoads);
/*     */     
/* 428 */     this.frontend.backupFactBase();
/*     */     
/*     */ 
/* 431 */     doSomeQueries();
/* 432 */     this.frontend.parse(this.test_preds[0] + "(ThisIsNewAfterSave).");
/* 433 */     test_must_succeed(this.test_preds[0] + "(ThisIsNewAfterSave)");
/*     */     
/* 435 */     this.frontend.crash();
/* 436 */     this.frontend = null;
/*     */     
/* 438 */     setUp(true);
/* 439 */     doSomeQueries();
/* 440 */     test_must_fail(this.test_preds[0] + "(ThisIsNewAfterSave)");
/*     */   }
/*     */   
/*     */   public void testBackupTestsCase0()
/*     */     throws Exception
/*     */   {
/* 446 */     this.bucketLoads = 0;
/* 447 */     doSomeQueries();
/* 448 */     assertEquals(numbuckets(), this.bucketLoads);
/* 449 */     this.frontend.backupFactBase();
/* 450 */     this.frontend.shutdown();
/* 451 */     this.frontend = null;
/* 452 */     setUp(true);
/* 453 */     this.bucketLoads = 0;
/* 454 */     doSomeQueries();
/* 455 */     assertEquals(0, this.bucketLoads);
/*     */   }
/*     */   
/*     */   public void testBackupTestsCase1() throws Exception
/*     */   {
/* 460 */     this.bucketLoads = 0;
/* 461 */     doSomeQueries();
/* 462 */     assertEquals(numbuckets(), this.bucketLoads);
/* 463 */     this.frontend.crash();
/* 464 */     this.frontend = null;
/* 465 */     setUp(true);
/* 466 */     this.bucketLoads = 0;
/* 467 */     doSomeQueries();
/* 468 */     assertEquals(numbuckets(), this.bucketLoads);
/*     */   }
/*     */   
/*     */   public void testBackupTestsCase2() throws Exception
/*     */   {
/* 473 */     this.bucketLoads = 0;
/* 474 */     doSomeQueries();
/* 475 */     assertEquals(numbuckets(), this.bucketLoads);
/* 476 */     this.frontend.backupFactBase();
/* 477 */     this.frontend.crash();
/* 478 */     this.frontend = null;
/* 479 */     setUp(true);
/* 480 */     this.bucketLoads = 0;
/* 481 */     doSomeQueries();
/* 482 */     assertEquals(0, this.bucketLoads);
/*     */   }
/*     */   
/*     */   public void testBackupTestsCase3() throws Exception
/*     */   {
/* 487 */     this.bucketLoads = 0;
/* 488 */     doSomeQueries();
/* 489 */     assertEquals(numbuckets(), this.bucketLoads);
/* 490 */     this.frontend.backupFactBase();
/* 491 */     this.buckets[0].parse(this.test_preds[0] + "(newnewnew).");
/* 492 */     test_must_succeed(this.test_preds[0] + "(newnewnew)");
/* 493 */     this.frontend.crash();
/* 494 */     this.frontend = null;
/* 495 */     setUp(true);
/* 496 */     this.bucketLoads = 0;
/* 497 */     doSomeQueries();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 502 */     assertTrue((this.bucketLoads == 10) || (this.bucketLoads == 0));
/* 503 */     test_must_fail(this.test_preds[0] + "(newnewnew)");
/*     */   }
/*     */   
/*     */   public void testStress() throws ParseException, TypeModeError {
/* 507 */     int expectedBucketLoads = 0;
/* 508 */     for (int space = 5; space <= 1000; space += 50) {
/* 509 */       System.out.println("Run with cache size target = " + space);
/*     */       
/* 511 */       expectedBucketLoads += outdateSomebuckets(0, 1);
/* 512 */       doSomeQueries();
/*     */     }
/*     */     
/* 515 */     assertEquals("Number of buckets loaded ", expectedBucketLoads, this.bucketLoads);
/*     */   }
/*     */   
/*     */   public void testJustSomeQueries() throws ParseException, TypeModeError {
/* 519 */     System.err.println("==== TEST: JustSomeQueries ===");
/* 520 */     doSomeQueries();
/* 521 */     assertEquals("Number of buckets loaded ", numbuckets(), this.bucketLoads);
/*     */   }
/*     */   
/*     */   public void testBucketKilling() throws ParseException, TypeModeError {
/* 525 */     System.err.println("==== TEST: BucketKilling ===");
/* 526 */     doSomeQueries();
/*     */     
/* 528 */     while (this.test_atoms.length > 1) {
/* 529 */       shrinkAtoms();
/* 530 */       doSomeQueries();
/*     */     }
/*     */   }
/*     */   
/*     */   public void testBucketKillingMany() throws ParseException, TypeModeError
/*     */   {
/* 536 */     System.err.println("==== TEST: BucketKillingMany ===");
/* 537 */     doSomeQueries();
/*     */     
/* 539 */     while (this.test_atoms.length > 1) {
/* 540 */       shrinkAtoms();
/*     */     }
/*     */     
/* 543 */     doSomeQueries();
/*     */   }
/*     */   
/*     */   private void shrinkAtoms()
/*     */   {
/* 548 */     RuleBaseBucket toDestroy = this.buckets[(this.test_atoms.length - 1)];
/* 549 */     synchronized (this.frontend) {
/* 550 */       toDestroy.destroy();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 555 */       System.err.println("Destroying bucket: " + toDestroy);
/* 556 */       String[] old = this.test_atoms;
/* 557 */       this.test_atoms = new String[old.length - 1];
/* 558 */       System.arraycopy(old, 0, this.test_atoms, 0, this.test_atoms.length);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSomeQueries() throws ParseException, TypeModeError
/*     */   {
/* 564 */     for (int i = 0; i < this.test_preds.length; i++) {
/* 565 */       int predictCount = 1;
/* 566 */       String query = this.test_preds[i] + "(";
/* 567 */       for (int j = 0; j < this.pred_arity[i]; j++) {
/* 568 */         if (j > 0)
/* 569 */           query = query + ",";
/* 570 */         query = query + "?x" + j;
/* 571 */         if (j + 1 == this.pred_arity[i])
/*     */         {
/* 573 */           predictCount *= this.test_atoms.length;
/*     */         } else
/* 575 */           predictCount *= init_numatoms;
/*     */       }
/* 577 */       query = query + ")";
/* 578 */       System.err.println(query);
/* 579 */       if (predictCount > 0) {
/* 580 */         test_must_succeed(query);
/*     */       }
/* 582 */       test_resultcount(query, predictCount);
/*     */     }
/*     */     
/*     */ 
/* 586 */     for (int i = 0; i < this.test_preds.length; i++) {
/* 587 */       String query = this.test_preds[i] + "(?x";
/* 588 */       for (int j = 1; j < this.pred_arity[i]; j++) {
/* 589 */         query = query + "," + this.test_atoms[(j * 113 % this.test_atoms.length)];
/*     */       }
/* 591 */       query = query + ")";
/* 592 */       System.out.println(query);
/* 593 */       if (this.pred_arity[i] > 1) {
/* 594 */         test_must_findall(query, "?x", this.initial_test_atoms);
/*     */       }
/*     */       else {
/* 597 */         test_must_findall(query, "?x", this.test_atoms);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void testBucketOutdating() throws ParseException, TypeModeError {
/* 603 */     int expectedBucketLoads = numbuckets();
/*     */     
/* 605 */     doSomeQueries();
/*     */     
/* 607 */     expectedBucketLoads += outdateSomebuckets(0, 1);
/* 608 */     doSomeQueries();
/*     */     
/* 610 */     expectedBucketLoads += outdateSomebuckets(1, 2);
/* 611 */     doSomeQueries();
/*     */     
/* 613 */     expectedBucketLoads += outdateSomebuckets(0, 2);
/* 614 */     doSomeQueries();
/*     */     
/* 616 */     expectedBucketLoads += outdateSomebuckets(1, 4);
/* 617 */     doSomeQueries();
/*     */     
/* 619 */     assertEquals("Number of buckets loaded ", expectedBucketLoads, this.bucketLoads);
/*     */   }
/*     */   
/*     */   private int numbuckets() {
/* 623 */     return this.test_atoms.length;
/*     */   }
/*     */   
/*     */   void doRandomQueries(int howmany) throws ParseException, TypeModeError {
/* 627 */     Random rnd = new Random();
/* 628 */     for (int i = 0; i < howmany; i++) {
/* 629 */       doRandomQuery(rnd);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doRandomQuery(Random rnd) throws ParseException, TypeModeError {
/* 634 */     int prednum = rnd.nextInt(this.test_preds.length);
/* 635 */     String query = this.test_preds[prednum] + "(";
/* 636 */     int predicted_results = 1;
/* 637 */     boolean var = false;
/* 638 */     int atom = -1;
/*     */     
/* 640 */     synchronized (this.frontend) {
/* 641 */       int atoms_at_start = this.test_atoms.length;
/* 642 */       for (int argNum = 0; argNum < this.pred_arity[prednum]; argNum++) {
/* 643 */         if (argNum > 0)
/* 644 */           query = query + ",";
/* 645 */         var = rnd.nextBoolean();
/* 646 */         atom = rnd.nextInt(this.test_atoms.length);
/* 647 */         if (var) {
/* 648 */           query = query + "?x" + argNum;
/* 649 */           predicted_results *= init_numatoms;
/*     */         } else {
/* 651 */           query = query + this.test_atoms[atom];
/*     */         } }
/* 653 */       query = query + ")";
/*     */     }
/*     */     int atoms_at_start;
/* 656 */     System.err.println("Doing Query");
/* 657 */     if (var) {
/* 658 */       test_must_succeed(query);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 665 */     int results = get_resultcount(query);
/* 666 */     synchronized (this.frontend) {
/* 667 */       int kill_adjusted = kill_adjust_predicted(predicted_results, atoms_at_start, var, atom);
/*     */       
/* 669 */       if (results != kill_adjusted)
/* 670 */         System.err.println("Q = " + query + " R = " + results + " P = " + kill_adjusted + " A = " + atoms_at_start);
/* 671 */       while ((results != kill_adjusted) && (atoms_at_start > this.test_atoms.length)) {
/* 672 */         kill_adjusted = 
/* 673 */           kill_adjust_predicted(predicted_results, --atoms_at_start, var, atom);
/* 674 */         System.err.println("retry Q = " + query + " R = " + results + " P = " + kill_adjusted + " A = " + atoms_at_start);
/* 675 */         if (results == kill_adjusted)
/* 676 */           return;
/*     */       }
/* 678 */       Assert.assertEquals("Result count wrong for " + query, kill_adjusted, results);
/*     */     }
/* 680 */     System.err.println("Done Query");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int kill_adjust_predicted(int predicted_results, int numatoms, boolean var, int atom)
/*     */   {
/* 689 */     int kill_adjusted = 
/* 690 */       var ? predicted_results / init_numatoms * numatoms : 
/* 691 */       predicted_results;
/* 692 */     if ((!var) && (atom >= numatoms))
/* 693 */       kill_adjusted = 0;
/* 694 */     return kill_adjusted;
/*     */   }
/*     */   
/*     */   public void testConcurrentQueries()
/*     */     throws Throwable
/*     */   {
/* 700 */     doRandomQueries(1);
/*     */     
/* 702 */     FactBaseTest.RandomQueriesThread[] thread = new FactBaseTest.RandomQueriesThread[10];
/* 703 */     for (int i = 0; i < thread.length; i++) {
/* 704 */       thread[i] = new FactBaseTest.RandomQueriesThread(this, i, 10);
/* 705 */       thread[i].start();
/* 706 */       System.out.println("Thread " + i + " started");
/*     */     }
/* 708 */     for (int i = 0; i < thread.length; i++) {
/* 709 */       thread[i].join();
/* 710 */       if (thread[i].crash != null)
/* 711 */         throw thread[i].crash;
/* 712 */       System.out.println("Thread " + i + " ended");
/*     */     }
/*     */   }
/*     */   
/*     */   public void testConcurrentOutdating() throws Throwable {
/* 717 */     int workLoadSize = 20;
/*     */     
/*     */ 
/* 720 */     doRandomQueries(1);
/*     */     
/*     */ 
/* 723 */     FactBaseTest.TesterThread[] thread = new FactBaseTest.TesterThread[11];
/* 724 */     for (int i = 0; i < thread.length - 1; i++) {
/* 725 */       thread[i] = new FactBaseTest.RandomQueriesThread(this, i, 20);
/* 726 */       thread[i].start();
/* 727 */       System.out.println("Thread " + i + " started");
/*     */     }
/*     */     
/* 730 */     thread[(thread.length - 1)] = new FactBaseTest.OutdatingThread(this, "outdating", 20);
/* 731 */     thread[(thread.length - 1)].start();
/* 732 */     System.out.println("Outdating thread started");
/*     */     
/* 734 */     for (int i = 0; i < thread.length; i++) {
/* 735 */       thread[i].join();
/* 736 */       if (thread[i].crash != null)
/* 737 */         throw thread[i].crash;
/* 738 */       System.out.println("Thread " + i + " ended");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int outdateSomebuckets(int ofset, int mod)
/*     */   {
/* 746 */     int count = 0;
/* 747 */     for (int i = ofset; i < this.buckets.length; i += mod)
/*     */     {
/* 749 */       this.buckets[i].setOutdated();
/* 750 */       count++;
/*     */     }
/* 752 */     return count;
/*     */   }
/*     */   
/*     */   private boolean nextParamList(int i, int[] currAtom) {
/* 756 */     if (i >= currAtom.length) {
/* 757 */       return true;
/*     */     }
/* 759 */     currAtom[i] = ((currAtom[i] + 1) % this.test_atoms.length);
/* 760 */     if (currAtom[i] == 0) {
/* 761 */       return nextParamList(i + 1, currAtom);
/*     */     }
/* 763 */     return false;
/*     */   }
/*     */   
/*     */   private static PrintWriter makeFile(String name)
/*     */   {
/* 768 */     File path = new File(name);
/*     */     try {
/* 770 */       return new PrintWriter(new FileWriter(path));
/*     */     } catch (IOException e) {
/* 772 */       throw new Error("Error making logfile: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/FactBaseTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */