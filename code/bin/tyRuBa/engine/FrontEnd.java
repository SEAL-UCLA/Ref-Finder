/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import tyRuBa.engine.factbase.FactLibraryManager;
/*     */ import tyRuBa.engine.factbase.FileBasedValidatorManager;
/*     */ import tyRuBa.engine.factbase.NamePersistenceManager;
/*     */ import tyRuBa.engine.factbase.ValidatorManager;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.util.Aurelizer;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.SynchPolicy;
/*     */ import tyRuBa.util.SynchResource;
/*     */ import tyRuBa.util.pager.Pager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrontEnd
/*     */   extends QueryEngine
/*     */   implements SynchResource
/*     */ {
/*  36 */   public long updateCounter = 0L;
/*     */   
/*     */ 
/*  39 */   boolean someOutdated = false;
/*     */   
/*  41 */   ProgressMonitor progressMonitor = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private BasicModedRuleBaseIndex rules;
/*     */   
/*     */ 
/*  48 */   PrintStream os = System.out;
/*     */   
/*  50 */   private Collection holdingPen = makeBucketCollection();
/*     */   
/*  52 */   private Collection myBuckets = makeBucketCollection();
/*     */   
/*  54 */   private int progressBar = 0;
/*  55 */   private int updatedBuckets = 0;
/*     */   
/*     */   private static Pager pager;
/*     */   
/*     */   private ValidatorManager validatorManager;
/*     */   
/*     */   private NamePersistenceManager namePersistenceManager;
/*     */   
/*     */   private FactLibraryManager factLibraryManager;
/*     */   
/*     */   private File path;
/*     */   
/*     */   private String identifier;
/*     */   
/*     */   public static final int defaultPagerCacheSize = 5000;
/*     */   private static final int defaultPagerQueueSize = 1000;
/*     */   private long lastBackupTime;
/*     */   
/*     */   public FrontEnd(boolean loadInitFile, File path, boolean persistent, ProgressMonitor mon, boolean clean, boolean enableBackgroundCleaning)
/*     */   {
/*  75 */     this.progressMonitor = mon;
/*  76 */     this.path = path;
/*     */     
/*  78 */     if (pager != null) {
/*  79 */       pager.shutdown();
/*     */     }
/*     */     
/*  82 */     if (!checkAndFixConsistency()) {
/*  83 */       clean = true;
/*     */     }
/*     */     
/*  86 */     if (clean) {
/*  87 */       deleteDirectory(path);
/*     */     }
/*  89 */     pager = new Pager(5000, 1000, this.lastBackupTime, enableBackgroundCleaning);
/*     */     
/*  91 */     if (!path.exists()) {
/*  92 */       path.mkdirs();
/*     */     }
/*     */     try
/*     */     {
/*  96 */       new File(path.getPath() + "/running.data").createNewFile();
/*     */     } catch (IOException e) {
/*  98 */       throw new Error("Could not create running \"lock\" file");
/*     */     }
/*     */     
/* 101 */     this.validatorManager = new FileBasedValidatorManager(path.getPath());
/* 102 */     this.namePersistenceManager = new NamePersistenceManager(path.getPath());
/* 103 */     this.factLibraryManager = new FactLibraryManager(this);
/*     */     
/* 105 */     this.identifier = "**frontend**";
/*     */     
/* 107 */     if (persistent) {
/* 108 */       this.rules = RuleBase.make(this, "GLOBAL", false);
/*     */     } else
/* 110 */       this.rules = RuleBase.make(this);
/* 111 */     RuleBase.silent = true;
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
/*     */     try
/*     */     {
/* 124 */       System.err.println("Loading metabase decls");
/* 125 */       parse(MetaBase.declarations);
/* 126 */       MetaBase.addTypeMappings(this);
/* 127 */       System.err.println("DONE Loading metabase decls");
/*     */       
/* 129 */       if (loadInitFile) {
/* 130 */         boolean silent = RuleBase.silent;
/* 131 */         RuleBase.silent = true;
/* 132 */         NativePredicate.defineNativePredicates(this);
/* 133 */         URL initfile = 
/* 134 */           getClass().getClassLoader().getResource(
/* 135 */           "lib/initfile.rub");
/* 136 */         load(initfile);
/* 137 */         RuleBase.silent = silent;
/*     */       }
/*     */     } catch (ParseException e) {
/* 140 */       System.err.println(e.getMessage());
/*     */     } catch (IOException e) {
/* 142 */       System.err.println(e.getMessage());
/*     */     } catch (TypeModeError e) {
/* 144 */       System.err.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private static Collection makeBucketCollection() {
/* 149 */     return new LinkedHashSet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FrontEnd(boolean loadInitFile)
/*     */   {
/* 156 */     this(loadInitFile, new File("./fdb/"), false, null, false, false);
/*     */   }
/*     */   
/*     */   public FrontEnd(boolean loadInitFile, ProgressMonitor mon) {
/* 160 */     this(loadInitFile, new File("./fdb/"), false, mon, false, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FrontEnd(boolean initfile, boolean clean)
/*     */   {
/* 168 */     this(initfile, new File("./fdb/"), false, null, clean, false);
/*     */   }
/*     */   
/*     */   public void setCacheSize(int cacheSize) {
/* 172 */     pager.setCacheSize(cacheSize);
/*     */   }
/*     */   
/*     */   public int getCacheSize() {
/* 176 */     return pager.getCacheSize();
/*     */   }
/*     */   
/*     */   private boolean checkAndFixConsistency() {
/* 180 */     boolean result = true;
/* 181 */     String stPath = this.path.getPath();
/* 182 */     File crashed = new File(stPath + "/running.data");
/* 183 */     File checkFile = new File(stPath + "/lastBackup.data");
/* 184 */     if (checkFile.exists()) {
/* 185 */       this.lastBackupTime = checkFile.lastModified();
/*     */     } else {
/* 187 */       this.lastBackupTime = -1L;
/*     */     }
/* 189 */     System.err.println("Checking consistency...");
/* 190 */     if (crashed.exists()) {
/* 191 */       System.err.println("System was running.");
/*     */       
/* 193 */       if (!checkFile.exists())
/*     */       {
/* 195 */         System.err.println("We hadn't backed up before....");
/* 196 */         System.err.println("Data is in an inconsistent state, must delete everything.");
/* 197 */         deleteDirectory(this.path);
/* 198 */         this.path.mkdirs();
/*     */       } else {
/* 200 */         long backupTime = checkFile.lastModified();
/*     */         
/* 202 */         System.err.println("Trying to restore backup files.");
/* 203 */         if (!restoreBackups(this.path, backupTime)) {
/* 204 */           System.err.println("Data is in an inconsistent state, must delete everything.");
/* 205 */           deleteDirectory(this.path);
/* 206 */           this.path.mkdirs();
/* 207 */           result = false;
/*     */         } else {
/* 209 */           System.err.println("Restoring backup file successfull!");
/*     */           
/*     */           try
/*     */           {
/* 213 */             FileWriter fw = new FileWriter(checkFile, false);
/* 214 */             fw.write("Nothing to see here... move along. ;)");
/* 215 */             fw.close();
/*     */           } catch (IOException e) {
/* 217 */             throw new Error("Could not create backup file");
/*     */           }
/* 219 */           this.lastBackupTime = checkFile.lastModified();
/*     */         }
/*     */       }
/* 222 */       crashed.delete();
/*     */     }
/* 224 */     return result;
/*     */   }
/*     */   
/*     */   public long getLastBackupTime() {
/* 228 */     return this.lastBackupTime;
/*     */   }
/*     */   
/*     */   private boolean restoreBackups(File f, long backupTime) {
/* 232 */     if (f.isDirectory()) {
/* 233 */       boolean success = true;
/* 234 */       File[] files = f.listFiles();
/* 235 */       for (int i = 0; i < files.length; i++) {
/* 236 */         success &= restoreBackups(files[i], backupTime);
/*     */       }
/* 238 */       return success;
/*     */     }
/* 240 */     String name = f.getName();
/* 241 */     if ((!name.endsWith(".bup")) && (!name.endsWith(".data"))) {
/* 242 */       long fileModifiedTime = f.lastModified();
/* 243 */       if (fileModifiedTime > backupTime) {
/* 244 */         File backup = new File(f.getAbsolutePath() + ".bup");
/* 245 */         if (backup.exists()) {
/* 246 */           f.delete();
/* 247 */           backup.renameTo(f);
/* 248 */           return true;
/*     */         }
/*     */         
/* 251 */         System.err.println(f.getAbsolutePath() + ": " + backupTime + " ::: " + fileModifiedTime);
/* 252 */         return false;
/*     */       }
/*     */       
/* 255 */       return true;
/*     */     }
/*     */     
/* 258 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private void deleteDirectory(File dir)
/*     */   {
/* 264 */     if (dir.isDirectory()) {
/* 265 */       File[] children = dir.listFiles();
/* 266 */       for (int i = 0; i < children.length; i++) {
/* 267 */         deleteDirectory(children[i]);
/*     */       }
/*     */     }
/* 270 */     dir.delete();
/*     */   }
/*     */   
/*     */   Validator obtainGroupValidator(Object identifier, boolean temporary) {
/* 274 */     if (!(identifier instanceof String)) throw new Error("[ERROR] - obtainGroupValidator - ID needs to be a String");
/* 275 */     Validator result = this.validatorManager.get((String)identifier);
/*     */     
/* 277 */     if ((result != null) && 
/* 278 */       (!result.isValid())) {
/* 279 */       this.validatorManager.remove((String)identifier);
/* 280 */       result = null;
/*     */     }
/*     */     
/*     */ 
/* 284 */     if (result == null) {
/* 285 */       result = new Validator();
/* 286 */       this.validatorManager.add(result, (String)identifier);
/*     */     }
/*     */     
/* 289 */     return result;
/*     */   }
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
/*     */   public boolean fastBackupFactBase()
/*     */     throws BackupFailedException
/*     */   {
/* 306 */     if (pager.isDirty()) {
/* 307 */       return false;
/*     */     }
/* 309 */     backupFactBase();
/* 310 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void backupFactBase() throws BackupFailedException
/*     */   {
/* 315 */     System.err.println("[DEBUG] - backupFactBase - Entering Backup Method");
/* 316 */     getSynchPolicy().stopSources();
/*     */     try {
/* 318 */       System.err.println("[DEBUG] - backupFactBase - Backup: Actually Backing Up");
/*     */       
/*     */ 
/* 321 */       this.rules.backup();
/* 322 */       Object[] buckets = getBuckets().toArray();
/* 323 */       for (int i = 0; i < buckets.length; i++) {
/* 324 */         RuleBaseBucket bucket = (RuleBaseBucket)buckets[i];
/* 325 */         bucket.backup();
/*     */       }
/*     */       
/* 328 */       pager.backup();
/* 329 */       this.validatorManager.backup();
/* 330 */       this.namePersistenceManager.backup();
/*     */       
/*     */ 
/* 333 */       File lastBackup = new File(this.path.getPath() + "/lastBackup.data");
/* 334 */       if (lastBackup.exists()) {
/* 335 */         lastBackup.delete();
/*     */       }
/*     */       try
/*     */       {
/* 339 */         FileWriter fw = new FileWriter(lastBackup, false);
/* 340 */         fw.write("Nothing to see here... move along. ;)");
/* 341 */         fw.close();
/*     */       } catch (IOException e) {
/* 343 */         throw new Error("Could not create backup file");
/*     */       }
/*     */     }
/*     */     finally {
/* 347 */       getSynchPolicy().allowSources(); } getSynchPolicy().allowSources();
/*     */     
/* 349 */     System.err.println("[DEBUG] - backupFactBase - Done Backup Method");
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized void shutdown()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 188	java/lang/System:err	Ljava/io/PrintStream;
/*     */     //   3: ldc_w 461
/*     */     //   6: invokevirtual 193	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   9: aload_0
/*     */     //   10: aconst_null
/*     */     //   11: invokevirtual 463	tyRuBa/engine/FrontEnd:setLogger	(LtyRuBa/util/QueryLogger;)V
/*     */     //   14: aload_0
/*     */     //   15: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   18: invokevirtual 422	tyRuBa/util/SynchPolicy:stopSources	()V
/*     */     //   21: aload_0
/*     */     //   22: getfield 180	tyRuBa/engine/FrontEnd:rules	LtyRuBa/engine/BasicModedRuleBaseIndex;
/*     */     //   25: invokevirtual 429	tyRuBa/engine/BasicModedRuleBaseIndex:backup	()V
/*     */     //   28: aload_0
/*     */     //   29: invokevirtual 433	tyRuBa/engine/FrontEnd:getBuckets	()Ljava/util/Collection;
/*     */     //   32: invokeinterface 467 1 0
/*     */     //   37: ifne +50 -> 87
/*     */     //   40: aload_0
/*     */     //   41: invokevirtual 433	tyRuBa/engine/FrontEnd:getBuckets	()Ljava/util/Collection;
/*     */     //   44: invokeinterface 436 1 0
/*     */     //   49: astore_1
/*     */     //   50: iconst_0
/*     */     //   51: istore_2
/*     */     //   52: goto +29 -> 81
/*     */     //   55: aload_1
/*     */     //   56: iload_2
/*     */     //   57: aaload
/*     */     //   58: checkcast 442	tyRuBa/engine/RuleBaseBucket
/*     */     //   61: astore_3
/*     */     //   62: aload_3
/*     */     //   63: aconst_null
/*     */     //   64: invokevirtual 470	tyRuBa/engine/RuleBaseBucket:setLogger	(LtyRuBa/util/QueryLogger;)V
/*     */     //   67: aload_3
/*     */     //   68: invokevirtual 471	tyRuBa/engine/RuleBaseBucket:isTemporary	()Z
/*     */     //   71: ifeq +7 -> 78
/*     */     //   74: aload_3
/*     */     //   75: invokevirtual 474	tyRuBa/engine/RuleBaseBucket:destroy	()V
/*     */     //   78: iinc 2 1
/*     */     //   81: iload_2
/*     */     //   82: aload_1
/*     */     //   83: arraylength
/*     */     //   84: if_icmplt -29 -> 55
/*     */     //   87: getstatic 90	tyRuBa/engine/FrontEnd:pager	LtyRuBa/util/pager/Pager;
/*     */     //   90: invokevirtual 445	tyRuBa/util/pager/Pager:backup	()V
/*     */     //   93: aload_0
/*     */     //   94: getfield 154	tyRuBa/engine/FrontEnd:validatorManager	LtyRuBa/engine/factbase/ValidatorManager;
/*     */     //   97: invokeinterface 446 1 0
/*     */     //   102: aload_0
/*     */     //   103: getfield 159	tyRuBa/engine/FrontEnd:namePersistenceManager	LtyRuBa/engine/factbase/NamePersistenceManager;
/*     */     //   106: invokevirtual 447	tyRuBa/engine/factbase/NamePersistenceManager:backup	()V
/*     */     //   109: new 111	java/io/File
/*     */     //   112: dup
/*     */     //   113: new 118	java/lang/StringBuilder
/*     */     //   116: dup
/*     */     //   117: aload_0
/*     */     //   118: getfield 88	tyRuBa/engine/FrontEnd:path	Ljava/io/File;
/*     */     //   121: invokevirtual 120	java/io/File:getPath	()Ljava/lang/String;
/*     */     //   124: invokestatic 124	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   127: invokespecial 130	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   130: ldc -123
/*     */     //   132: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   135: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   138: invokespecial 142	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   141: invokevirtual 326	java/io/File:delete	()Z
/*     */     //   144: pop
/*     */     //   145: goto +15 -> 160
/*     */     //   148: astore 4
/*     */     //   150: aload_0
/*     */     //   151: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   154: invokevirtual 448	tyRuBa/util/SynchPolicy:allowSources	()V
/*     */     //   157: aload 4
/*     */     //   159: athrow
/*     */     //   160: aload_0
/*     */     //   161: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   164: invokevirtual 448	tyRuBa/util/SynchPolicy:allowSources	()V
/*     */     //   167: getstatic 188	java/lang/System:err	Ljava/io/PrintStream;
/*     */     //   170: ldc_w 477
/*     */     //   173: invokevirtual 193	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   176: return
/*     */     // Line number table:
/*     */     //   Java source line #353	-> byte code offset #0
/*     */     //   Java source line #354	-> byte code offset #9
/*     */     //   Java source line #355	-> byte code offset #14
/*     */     //   Java source line #357	-> byte code offset #21
/*     */     //   Java source line #358	-> byte code offset #28
/*     */     //   Java source line #360	-> byte code offset #40
/*     */     //   Java source line #361	-> byte code offset #50
/*     */     //   Java source line #362	-> byte code offset #55
/*     */     //   Java source line #363	-> byte code offset #62
/*     */     //   Java source line #364	-> byte code offset #67
/*     */     //   Java source line #365	-> byte code offset #74
/*     */     //   Java source line #361	-> byte code offset #78
/*     */     //   Java source line #369	-> byte code offset #87
/*     */     //   Java source line #370	-> byte code offset #93
/*     */     //   Java source line #371	-> byte code offset #102
/*     */     //   Java source line #374	-> byte code offset #109
/*     */     //   Java source line #375	-> byte code offset #148
/*     */     //   Java source line #376	-> byte code offset #150
/*     */     //   Java source line #377	-> byte code offset #157
/*     */     //   Java source line #376	-> byte code offset #160
/*     */     //   Java source line #378	-> byte code offset #167
/*     */     //   Java source line #379	-> byte code offset #176
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	177	0	this	FrontEnd
/*     */     //   49	34	1	buckets	Object[]
/*     */     //   51	31	2	i	int
/*     */     //   61	14	3	bucket	RuleBaseBucket
/*     */     //   148	10	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   21	148	148	finally
/*     */   }
/*     */   
/*     */   public void redirectOutput(PrintStream output)
/*     */   {
/* 382 */     closeOutput();
/* 383 */     this.os = output;
/*     */   }
/*     */   
/*     */   public void flush()
/*     */   {
/* 388 */     this.updateCounter += 1L;
/*     */   }
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
/*     */   public String toString()
/*     */   {
/* 405 */     return "FrontEnd: " + this.rules;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RBTerm makeCompound(ConstructorType cons, RBTerm[] args)
/*     */   {
/* 413 */     return cons.apply(makeTuple(args));
/*     */   }
/*     */   
/*     */   public static RBVariable makeVar(String name) {
/* 417 */     return RBVariable.make(name);
/*     */   }
/*     */   
/*     */   public static RBTerm makeTemplateVar(String name) {
/* 421 */     return new RBTemplateVar(name);
/*     */   }
/*     */   
/*     */   public static RBVariable makeUniqueVar(String name) {
/* 425 */     return RBVariable.makeUnique(name);
/*     */   }
/*     */   
/*     */   public static RBVariable makeIgnoredVar() {
/* 429 */     return RBIgnoredVariable.the;
/*     */   }
/*     */   
/*     */   public static RBTerm makeName(String n) {
/* 433 */     return RBCompoundTerm.makeJava(n);
/*     */   }
/*     */   
/*     */   public static RBTerm makeInteger(String n) {
/* 437 */     return RBCompoundTerm.makeJava(new Integer(n));
/*     */   }
/*     */   
/* 440 */   public static RBTerm makeInteger(int n) { return RBCompoundTerm.makeJava(new Integer(n)); }
/*     */   
/*     */   public static RBTerm makeReal(String n)
/*     */   {
/* 444 */     return RBCompoundTerm.makeJava(new Float(n));
/*     */   }
/*     */   
/*     */   public static RBConjunction makeAnd(RBExpression e1, RBExpression e2) {
/* 448 */     return new RBConjunction(e1, e2);
/*     */   }
/*     */   
/*     */   public static RBPredicateExpression makePredicateExpression(String pred, RBTerm[] terms) {
/* 452 */     return new RBPredicateExpression(pred, terms);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RBTerm makeList(ElementSource els)
/*     */   {
/* 460 */     if (els.hasMoreElements()) {
/* 461 */       RBTerm first = (RBTerm)els.nextElement();
/* 462 */       return new RBPair(first, makeList(els));
/*     */     }
/* 464 */     return theEmptyList;
/*     */   }
/*     */   
/*     */   public static RBTerm makeList(RBTerm[] elements) {
/* 468 */     return RBPair.make(elements);
/*     */   }
/*     */   
/*     */   public static RBTuple makeTuple(RBTerm[] elements) {
/* 472 */     return RBTuple.make(elements);
/*     */   }
/*     */   
/*     */   public static RBTuple makeTuple(ArrayList args) {
/* 476 */     return RBTuple.make(args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 481 */   public static final RBTerm theEmptyList = RBJavaObjectCompoundTerm.theEmptyList;
/*     */   private static final int PROGRESS_BAR_LEN = 100;
/*     */   
/*     */   public void finalize() throws Throwable
/*     */   {
/*     */     try {
/* 487 */       closeOutput();
/* 488 */       super.finalize();
/*     */     } catch (Throwable e) {
/* 490 */       e.printStackTrace();
/* 491 */       if (Aurelizer.debug_sounds != null)
/* 492 */         Aurelizer.debug_sounds.enter("error");
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeOutput() {
/* 497 */     if ((this.os != null) && (this.os != System.out) && (this.os != System.err)) {
/* 498 */       this.os.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FrontEnd frontend()
/*     */   {
/* 505 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   synchronized void addBucket(RuleBaseBucket bucket)
/*     */   {
/* 511 */     if (this.holdingPen == null)
/* 512 */       this.holdingPen = makeBucketCollection();
/* 513 */     this.holdingPen.add(bucket);
/* 514 */     this.someOutdated |= bucket.isOutdated();
/*     */   }
/*     */   
/*     */ 
/*     */   synchronized void removeBucket(RuleBaseBucket bucket)
/*     */   {
/* 520 */     if (!getBuckets().remove(bucket))
/* 521 */       throw new Error("Attempted to remove bucket which is not present");
/* 522 */     Map predMap = bucket.rulebase.localRuleBase.typeInfoBase.predicateMap;
/* 523 */     Iterator it = predMap.values().iterator();
/* 524 */     while (it.hasNext()) {
/* 525 */       PredInfo localPredInfo = (PredInfo)it.next();
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection getBuckets() {
/* 530 */     if (this.holdingPen != null) {
/* 531 */       this.myBuckets.addAll(this.holdingPen);
/* 532 */       this.holdingPen = null;
/*     */     }
/* 534 */     return this.myBuckets;
/*     */   }
/*     */   
/*     */   private synchronized int bucketCount() {
/* 538 */     return this.myBuckets.size() + (this.holdingPen == null ? 0 : this.holdingPen.size());
/*     */   }
/*     */   
/*     */   ModedRuleBaseIndex rulebase() {
/* 542 */     return this.rules;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public synchronized void updateBuckets()
/*     */     throws TypeModeError, ParseException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: lconst_0
/*     */     //   1: lstore_1
/*     */     //   2: aload_0
/*     */     //   3: iconst_0
/*     */     //   4: putfield 82	tyRuBa/engine/FrontEnd:progressBar	I
/*     */     //   7: aload_0
/*     */     //   8: iconst_0
/*     */     //   9: putfield 84	tyRuBa/engine/FrontEnd:updatedBuckets	I
/*     */     //   12: aload_0
/*     */     //   13: getfield 63	tyRuBa/engine/FrontEnd:someOutdated	Z
/*     */     //   16: ifeq +216 -> 232
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   23: invokevirtual 422	tyRuBa/util/SynchPolicy:stopSources	()V
/*     */     //   26: invokestatic 689	java/lang/System:currentTimeMillis	()J
/*     */     //   29: lstore_1
/*     */     //   30: getstatic 188	java/lang/System:err	Ljava/io/PrintStream;
/*     */     //   33: ldc_w 692
/*     */     //   36: invokevirtual 193	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   39: aload_0
/*     */     //   40: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   43: ifnull +17 -> 60
/*     */     //   46: aload_0
/*     */     //   47: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   50: ldc_w 694
/*     */     //   53: bipush 100
/*     */     //   55: invokeinterface 696 3 0
/*     */     //   60: aload_0
/*     */     //   61: invokevirtual 433	tyRuBa/engine/FrontEnd:getBuckets	()Ljava/util/Collection;
/*     */     //   64: astore_3
/*     */     //   65: aload_0
/*     */     //   66: aload_3
/*     */     //   67: invokespecial 700	tyRuBa/engine/FrontEnd:updateSomeBuckets	(Ljava/util/Collection;)V
/*     */     //   70: goto +18 -> 88
/*     */     //   73: aload_0
/*     */     //   74: getfield 78	tyRuBa/engine/FrontEnd:holdingPen	Ljava/util/Collection;
/*     */     //   77: astore_3
/*     */     //   78: aload_0
/*     */     //   79: invokevirtual 433	tyRuBa/engine/FrontEnd:getBuckets	()Ljava/util/Collection;
/*     */     //   82: pop
/*     */     //   83: aload_0
/*     */     //   84: aload_3
/*     */     //   85: invokespecial 700	tyRuBa/engine/FrontEnd:updateSomeBuckets	(Ljava/util/Collection;)V
/*     */     //   88: aload_0
/*     */     //   89: getfield 78	tyRuBa/engine/FrontEnd:holdingPen	Ljava/util/Collection;
/*     */     //   92: ifnonnull -19 -> 73
/*     */     //   95: aload_0
/*     */     //   96: iconst_0
/*     */     //   97: putfield 63	tyRuBa/engine/FrontEnd:someOutdated	Z
/*     */     //   100: goto +70 -> 170
/*     */     //   103: astore 4
/*     */     //   105: aload_0
/*     */     //   106: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   109: invokevirtual 448	tyRuBa/util/SynchPolicy:allowSources	()V
/*     */     //   112: aload_0
/*     */     //   113: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   116: ifnull +12 -> 128
/*     */     //   119: aload_0
/*     */     //   120: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   123: invokeinterface 704 1 0
/*     */     //   128: getstatic 188	java/lang/System:err	Ljava/io/PrintStream;
/*     */     //   131: new 118	java/lang/StringBuilder
/*     */     //   134: dup
/*     */     //   135: ldc_w 707
/*     */     //   138: invokespecial 130	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   141: invokestatic 689	java/lang/System:currentTimeMillis	()J
/*     */     //   144: lload_1
/*     */     //   145: lsub
/*     */     //   146: invokevirtual 364	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
/*     */     //   149: ldc_w 709
/*     */     //   152: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   155: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   158: invokevirtual 193	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   161: getstatic 90	tyRuBa/engine/FrontEnd:pager	LtyRuBa/util/pager/Pager;
/*     */     //   164: invokevirtual 711	tyRuBa/util/pager/Pager:printStats	()V
/*     */     //   167: aload 4
/*     */     //   169: athrow
/*     */     //   170: aload_0
/*     */     //   171: invokevirtual 418	tyRuBa/engine/FrontEnd:getSynchPolicy	()LtyRuBa/util/SynchPolicy;
/*     */     //   174: invokevirtual 448	tyRuBa/util/SynchPolicy:allowSources	()V
/*     */     //   177: aload_0
/*     */     //   178: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   181: ifnull +12 -> 193
/*     */     //   184: aload_0
/*     */     //   185: getfield 65	tyRuBa/engine/FrontEnd:progressMonitor	LtyRuBa/engine/ProgressMonitor;
/*     */     //   188: invokeinterface 704 1 0
/*     */     //   193: getstatic 188	java/lang/System:err	Ljava/io/PrintStream;
/*     */     //   196: new 118	java/lang/StringBuilder
/*     */     //   199: dup
/*     */     //   200: ldc_w 707
/*     */     //   203: invokespecial 130	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   206: invokestatic 689	java/lang/System:currentTimeMillis	()J
/*     */     //   209: lload_1
/*     */     //   210: lsub
/*     */     //   211: invokevirtual 364	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
/*     */     //   214: ldc_w 709
/*     */     //   217: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   220: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   223: invokevirtual 193	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   226: getstatic 90	tyRuBa/engine/FrontEnd:pager	LtyRuBa/util/pager/Pager;
/*     */     //   229: invokevirtual 711	tyRuBa/util/pager/Pager:printStats	()V
/*     */     //   232: return
/*     */     // Line number table:
/*     */     //   Java source line #547	-> byte code offset #0
/*     */     //   Java source line #548	-> byte code offset #2
/*     */     //   Java source line #549	-> byte code offset #12
/*     */     //   Java source line #550	-> byte code offset #19
/*     */     //   Java source line #552	-> byte code offset #26
/*     */     //   Java source line #553	-> byte code offset #30
/*     */     //   Java source line #554	-> byte code offset #39
/*     */     //   Java source line #555	-> byte code offset #46
/*     */     //   Java source line #556	-> byte code offset #60
/*     */     //   Java source line #557	-> byte code offset #65
/*     */     //   Java source line #558	-> byte code offset #70
/*     */     //   Java source line #560	-> byte code offset #73
/*     */     //   Java source line #561	-> byte code offset #78
/*     */     //   Java source line #562	-> byte code offset #83
/*     */     //   Java source line #558	-> byte code offset #88
/*     */     //   Java source line #564	-> byte code offset #95
/*     */     //   Java source line #566	-> byte code offset #103
/*     */     //   Java source line #567	-> byte code offset #105
/*     */     //   Java source line #568	-> byte code offset #112
/*     */     //   Java source line #569	-> byte code offset #119
/*     */     //   Java source line #570	-> byte code offset #128
/*     */     //   Java source line #571	-> byte code offset #161
/*     */     //   Java source line #572	-> byte code offset #167
/*     */     //   Java source line #567	-> byte code offset #170
/*     */     //   Java source line #568	-> byte code offset #177
/*     */     //   Java source line #569	-> byte code offset #184
/*     */     //   Java source line #570	-> byte code offset #193
/*     */     //   Java source line #571	-> byte code offset #226
/*     */     //   Java source line #574	-> byte code offset #232
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	233	0	this	FrontEnd
/*     */     //   1	209	1	startTime	long
/*     */     //   64	21	3	bucketColl	Collection
/*     */     //   103	65	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   26	103	103	finally
/*     */   }
/*     */   
/*     */   private void updateSomeBuckets(Collection bucketColl)
/*     */     throws TypeModeError, ParseException
/*     */   {
/* 581 */     for (Iterator buckets = bucketColl.iterator(); buckets.hasNext();) {
/* 582 */       RuleBaseBucket bucket = (RuleBaseBucket)buckets.next();
/* 583 */       if (bucket.isOutdated()) {
/* 584 */         bucket.clear();
/*     */       }
/*     */     }
/* 587 */     for (Iterator buckets = bucketColl.iterator(); buckets.hasNext();) {
/* 588 */       RuleBaseBucket bucket = (RuleBaseBucket)buckets.next();
/* 589 */       if (bucket.isOutdated()) {
/* 590 */         bucket.doUpdate();
/*     */       }
/* 592 */       if (this.progressMonitor != null) {
/* 593 */         this.updatedBuckets += 1;
/* 594 */         int newProgressBar = this.updatedBuckets * 100 / bucketCount();
/* 595 */         if (newProgressBar > this.progressBar) {
/* 596 */           this.progressMonitor.worked(newProgressBar - this.progressBar);
/* 597 */           this.progressBar = newProgressBar;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void autoUpdateBuckets() throws TypeModeError, ParseException {
/* 604 */     if (RuleBase.autoUpdate) {
/* 605 */       updateBuckets();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 611 */   private SynchPolicy synchPol = null;
/*     */   
/*     */   public SynchPolicy getSynchPolicy() {
/* 614 */     if (this.synchPol == null)
/* 615 */       this.synchPol = new SynchPolicy(this);
/* 616 */     return this.synchPol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getStoragePath()
/*     */   {
/* 623 */     return this.path.getPath();
/*     */   }
/*     */   
/*     */   public ValidatorManager getValidatorManager() {
/* 627 */     return this.validatorManager;
/*     */   }
/*     */   
/*     */   public NamePersistenceManager getNamePersistenceManager() {
/* 631 */     return this.namePersistenceManager;
/*     */   }
/*     */   
/*     */   public FactLibraryManager getFactLibraryManager() {
/* 635 */     return this.factLibraryManager;
/*     */   }
/*     */   
/*     */   public Pager getPager() {
/* 639 */     return pager;
/*     */   }
/*     */   
/*     */   public String getIdentifier() {
/* 643 */     return this.identifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void crash()
/*     */   {
/* 653 */     pager.crash();
/* 654 */     pager = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableMetaData()
/*     */   {
/* 665 */     this.rules.enableMetaData();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/FrontEnd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */