/*     */ package lsclipse;
/*     */ 
/*     */ import changetypes.AtomicChange;
/*     */ import changetypes.AtomicChange.ChangeTypes;
/*     */ import changetypes.ChangeSet;
/*     */ import changetypes.Fact;
/*     */ import changetypes.Fact.FactTypes;
/*     */ import changetypes.FactBase;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import lsclipse.dialogs.ProgressBarDialog;
/*     */ import lsclipse.utils.StringCleaner;
/*     */ import lsd.facts.LSDRuleEnumerator;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDPredicate;
/*     */ import lsd.rule.LSDRule;
/*     */ import metapackage.MetaInfo;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ import org.eclipse.jdt.core.IJavaProject;
/*     */ import org.eclipse.jdt.core.IPackageFragment;
/*     */ import org.eclipse.jdt.core.JavaCore;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LSDiffRunner
/*     */ {
/*     */   private static final int NUM_THREADS = 4;
/*     */   private static final long TIMEOUT = 60L;
/*  59 */   private static Map<String, IJavaElement> oldTypeToFileMap_ = new ConcurrentHashMap();
/*     */   
/*     */   public static Map<String, IJavaElement> getOldTypeToFileMap() {
/*  62 */     return Collections.unmodifiableMap(oldTypeToFileMap_);
/*     */   }
/*     */   
/*  65 */   private static Map<String, IJavaElement> newTypeToFileMap_ = new ConcurrentHashMap();
/*     */   
/*     */   public static Map<String, IJavaElement> getNewTypeToFileMap() {
/*  68 */     return Collections.unmodifiableMap(newTypeToFileMap_);
/*     */   }
/*     */   
/*     */   public boolean doFactExtractionForRefFinder(String proj1, String proj2, ProgressBarDialog progbar) {
/*  72 */     if (!doFactExtraction(proj1, proj2, progbar))
/*  73 */       return false;
/*  74 */     installLSDiff();
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public List<LSDResult> doLSDiff(String proj1, String proj2, ProgressBarDialog progbar) {
/*  79 */     if (!doFactExtraction(proj1, proj2, progbar)) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     progbar.setMessage("Invoking LSDiff... ");
/*  84 */     BufferedWriter output = null;
/*  85 */     LSDRuleEnumerator enumerator = null;
/*  86 */     List<LSDRule> rules = null;
/*     */     File resultsFile;
/*  88 */     try { installLSDiff();
/*     */       
/*  90 */       File winnowingRulesFile = new File(MetaInfo.winnowings);
/*  91 */       File typeLevelWinnowingRulesFile = new File(MetaInfo.modifiedWinnowings);
/*  92 */       resultsFile = new File(MetaInfo.resultsFile);
/*  93 */       File twoKBFile = new File(MetaInfo.lsclipse2KB);
/*  94 */       File deltaKBFile = new File(MetaInfo.lsclipseDelta);
/*  95 */       enumerator = new LSDRuleEnumerator(twoKBFile, deltaKBFile, winnowingRulesFile, resultsFile, 
/*  96 */         3, 0.75D, 1, 100, 10, typeLevelWinnowingRulesFile, output);
/*  97 */       rules = enumerator.levelIncrementLearning(System.out);
/*     */     } catch (Exception e) {
/*  99 */       progbar.appendError("Unable to do LSDiff analysis");
/* 100 */       progbar.dispose();
/* 101 */       return null;
/*     */     }
/* 103 */     if (rules == null) {
/* 104 */       progbar.appendError("Unable to derive any rules!");
/* 105 */       progbar.dispose();
/* 106 */       return null;
/*     */     }
/* 108 */     progbar.appendLog("OK\n");
/* 109 */     progbar.appendLog("Found " + rules.size() + " rules\n");
/*     */     
/*     */ 
/* 112 */     List<LSDResult> res = new ArrayList();
/* 113 */     for (LSDRule r : rules) {
/* 114 */       LSDResult result = new LSDResult();
/* 115 */       result.num_matches = enumerator.countMatches(r);
/* 116 */       result.num_counter = enumerator.countExceptions(r);
/* 117 */       result.desc = r.toString();
/* 118 */       result.examples = enumerator.getRelevantFacts(r);
/* 119 */       result.exceptions = enumerator.getExceptions(r);
/*     */       
/* 121 */       res.add(result);
/*     */     }
/*     */     
/* 124 */     progbar.setStep(5);
/* 125 */     progbar.setMessage("Cleaning up... ");
/* 126 */     progbar.appendLog("OK\n");
/*     */     
/* 128 */     progbar.dispose();
/*     */     
/*     */ 
/* 131 */     return res;
/*     */   }
/*     */   
/*     */   private boolean doFactExtraction(String proj1, String proj2, ProgressBarDialog progbar)
/*     */   {
/* 136 */     Set<ICompilationUnit> allFiles = null;
/*     */     
/*     */ 
/* 139 */     progbar.setStep(1);
/* 140 */     progbar.setMessage("Retrieving facts for FB1... \n");
/* 141 */     FactBase fb1 = new FactBase();
/* 142 */     long beforefacts1 = System.currentTimeMillis();
/*     */     try {
/* 144 */       allFiles = getFiles(proj1);
/*     */     } catch (Exception localException) {}
/* 146 */     if (allFiles == null)
/* 147 */       return false;
/* 148 */     progbar.appendLog("Scanning " + allFiles.size() + " files...");
/* 149 */     Iterator<ICompilationUnit> iter = allFiles.iterator();
/* 150 */     ExecutorService execService = Executors.newFixedThreadPool(4);
/* 151 */     List<Future<FactBase>> futures = new LinkedList();
/* 152 */     LSDiffRunner.FactGetter fg; while (iter.hasNext()) {
/* 153 */       ICompilationUnit file = (ICompilationUnit)iter.next();
/* 154 */       fg = new LSDiffRunner.FactGetter(file, oldTypeToFileMap_);
/* 155 */       futures.add(execService.submit(fg));
/*     */     }
/* 157 */     execService.shutdown();
/*     */     try {
/* 159 */       execService.awaitTermination(60L, TimeUnit.MINUTES);
/*     */     }
/*     */     catch (InterruptedException e1) {
/* 162 */       e1.printStackTrace();
/*     */     }
/*     */     
/* 165 */     for (Future<FactBase> f : futures) {
/*     */       try {
/* 167 */         fb1.addAll((Collection)f.get());
/*     */       }
/*     */       catch (InterruptedException e) {
/* 170 */         e.printStackTrace();
/*     */       }
/*     */       catch (ExecutionException e) {
/* 173 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 176 */     int numFacts1 = fb1.size();
/* 177 */     progbar.appendLog("Extraction OK! Extracted " + numFacts1 + " facts for FB1\n");
/*     */     
/* 179 */     long afterfacts1 = System.currentTimeMillis();
/*     */     
/* 181 */     progbar.setMessage("Adding derived facts for FB1... \n");
/* 182 */     fb1.deriveFacts();
/* 183 */     progbar.appendLog("Derivation OK! Added " + (fb1.size() - numFacts1) + " facts to FB1\n");
/* 184 */     progbar.appendLog("All done! FB1 contains " + fb1.size() + " facts\n");
/*     */     
/* 186 */     long afterderivedfacts1 = System.currentTimeMillis();
/*     */     
/* 188 */     progbar.setStep(2);
/* 189 */     progbar.setMessage("Retrieving facts for FB2... \n");
/* 190 */     FactBase fb2 = new FactBase();
/* 191 */     long beforefacts2 = System.currentTimeMillis();
/*     */     try {
/* 193 */       allFiles = getFiles(proj2);
/*     */     } catch (Exception localException1) {}
/* 195 */     if (allFiles == null)
/* 196 */       return false;
/* 197 */     progbar.appendLog("Scanning " + allFiles.size() + " files...");
/* 198 */     iter = allFiles.iterator();
/* 199 */     execService = Executors.newFixedThreadPool(4);
/* 200 */     futures.clear();
/* 201 */     LSDiffRunner.FactGetter fg; while (iter.hasNext()) {
/* 202 */       ICompilationUnit file = (ICompilationUnit)iter.next();
/* 203 */       fg = new LSDiffRunner.FactGetter(file, newTypeToFileMap_);
/* 204 */       futures.add(execService.submit(fg));
/*     */     }
/* 206 */     execService.shutdown();
/*     */     try {
/* 208 */       execService.awaitTermination(60L, TimeUnit.MINUTES);
/*     */     }
/*     */     catch (InterruptedException e1) {
/* 211 */       e1.printStackTrace();
/*     */     }
/*     */     
/* 214 */     for (Future<FactBase> f : futures) {
/*     */       try {
/* 216 */         fb2.addAll((Collection)f.get());
/*     */       }
/*     */       catch (InterruptedException e) {
/* 219 */         e.printStackTrace();
/*     */       }
/*     */       catch (ExecutionException e) {
/* 222 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 225 */     int numFacts2 = fb2.size();
/* 226 */     progbar.appendLog("Extraction OK! Extracted " + numFacts2 + " facts for FB2\n");
/*     */     
/* 228 */     long afterfacts2 = System.currentTimeMillis();
/*     */     
/* 230 */     progbar.setMessage("Adding derived facts for FB2... \n");
/* 231 */     fb2.deriveFacts();
/* 232 */     progbar.appendLog("Derivation OK! Added " + (fb2.size() - numFacts2) + " facts to FB2\n");
/* 233 */     progbar.appendLog("All done! FB2 contains " + fb2.size() + " facts\n");
/* 234 */     long afterderivedfacts2 = System.currentTimeMillis();
/*     */     
/* 236 */     progbar.setMessage("Doing post processing for FB2... ");
/* 237 */     progbar.appendLog("OK\n");
/*     */     
/*     */ 
/* 240 */     progbar.setStep(3);
/* 241 */     progbar.setMessage("Computing factbase differences... ");
/* 242 */     long beforediff = System.currentTimeMillis();
/* 243 */     ChangeSet cs = fb2.diff(fb1);
/* 244 */     progbar.appendLog("All done! " + cs.size() + " changes found\n");
/*     */     
/* 246 */     long afterdiff = System.currentTimeMillis();
/*     */     
/* 248 */     progbar.setStep(4);
/*     */     
/* 250 */     progbar.setMessage("Preparing to run LSDiff...\n");
/* 251 */     progbar.appendLog("Converting atomic change to LSDiff changes... ");
/* 252 */     long beforeconversion = System.currentTimeMillis();
/* 253 */     ArrayList<LSDFact> input2kbFacts = new ArrayList();
/* 254 */     ArrayList<LSDFact> inputDeltaFacts = new ArrayList();
/*     */     
/* 256 */     for (Fact f : fb1) {
/* 257 */       input2kbFacts.add(makeLSDFact(f, "before"));
/*     */     }
/* 259 */     System.out.println("***************************************");
/* 260 */     for (Fact f : fb2) {
/* 261 */       input2kbFacts.add(makeLSDFact(f, "after"));
/*     */     }
/*     */     
/*     */ 
/* 265 */     for (AtomicChange ac : cs) {
/* 266 */       LSDFact f = makeLSDFact(ac);
/* 267 */       if (f != null) {
/* 268 */         inputDeltaFacts.add(f);
/*     */       }
/*     */     }
/* 271 */     progbar.appendLog("OK\n");
/* 272 */     long afterconversion = System.currentTimeMillis();
/*     */     
/*     */ 
/* 275 */     progbar.appendLog("Writing to LSDiff input files... \n");
/* 276 */     BufferedWriter lsd2kbfile = null;
/* 277 */     long beforeoutput = System.currentTimeMillis();
/*     */     int counter;
/*     */     try {
/* 280 */       File f2KBfile = new File(MetaInfo.lsclipse2KB);
/* 281 */       File dir = f2KBfile.getParentFile();
/* 282 */       if (!dir.exists()) {
/* 283 */         dir.mkdirs();
/*     */       }
/*     */       
/* 286 */       progbar.appendLog("  Writing 2KB to " + MetaInfo.lsclipse2KB + "\n");
/* 287 */       lsd2kbfile = new BufferedWriter(new FileWriter(MetaInfo.lsclipse2KB));
/*     */       
/* 289 */       counter = 0;
/* 290 */       for (LSDFact f : input2kbFacts) {
/* 291 */         counter++;
/*     */         
/* 293 */         if (f != null)
/* 294 */           lsd2kbfile.append(f.toString() + ".\n");
/*     */       }
/* 296 */       lsd2kbfile.close();
/*     */     } catch (IOException e) {
/* 298 */       progbar.appendError("Unable to create 2KB input file! Exiting...");
/* 299 */       progbar.dispose();
/* 300 */       return false;
/*     */     }
/* 302 */     BufferedWriter lsddeltafile = null;
/*     */     try {
/* 304 */       progbar.appendLog("  Writing deltas to " + MetaInfo.lsclipseDelta + "\n");
/* 305 */       lsddeltafile = new BufferedWriter(new FileWriter(MetaInfo.lsclipseDelta));
/* 306 */       lsddeltafile.close();
/* 307 */       for (LSDFact f : inputDeltaFacts) {
/* 308 */         lsddeltafile = new BufferedWriter(new FileWriter(MetaInfo.lsclipseDelta, true));
/* 309 */         lsddeltafile.append(f.toString() + ".\n");
/* 310 */         lsddeltafile.close();
/*     */       }
/*     */     } catch (IOException e) {
/* 313 */       progbar.appendError("Unable to create delta KB input file! Exiting...");
/* 314 */       progbar.dispose();
/* 315 */       return false;
/*     */     }
/* 317 */     progbar.appendLog("OK\n");
/* 318 */     long afteroutput = System.currentTimeMillis();
/* 319 */     progbar.appendLog("\nTotal time for fb1 extraction(ms): " + (afterderivedfacts1 - beforefacts1));
/* 320 */     progbar.appendLog("\nTotal time for fb2 extraction(ms): " + (afterderivedfacts2 - beforefacts2));
/* 321 */     progbar.appendLog("\nTotal time for diff(ms): " + (afterdiff - beforediff));
/* 322 */     progbar.appendLog("\nTotal time for conversion to LSD(ms): " + (afterconversion - beforeconversion));
/* 323 */     progbar.appendLog("\nTotal time for write to file(ms): " + (afteroutput - beforeoutput));
/*     */     
/* 325 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void installLSDiff()
/*     */   {
/* 331 */     File srcfile = MetaInfo.srcDir;
/* 332 */     srcfile.mkdirs();
/* 333 */     File resfile = MetaInfo.resDir;
/* 334 */     resfile.mkdirs();
/* 335 */     File fdbfile = MetaInfo.fdbDir;
/* 336 */     fdbfile.mkdirs();
/*     */     
/* 338 */     File included2KBFile = MetaInfo.included2kb;
/* 339 */     if (!included2KBFile.exists()) {
/* 340 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + included2KBFile.getName());
/* 341 */       writeStreamToFile(is, included2KBFile);
/*     */     }
/*     */     
/* 344 */     File includedDeltaKBFile = MetaInfo.includedDelta;
/* 345 */     if (!includedDeltaKBFile.exists()) {
/* 346 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + includedDeltaKBFile.getName());
/* 347 */       writeStreamToFile(is, includedDeltaKBFile);
/*     */     }
/*     */     
/* 350 */     File winnowingRulesFile = new File(MetaInfo.winnowings);
/* 351 */     if (!winnowingRulesFile.exists()) {
/* 352 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + winnowingRulesFile.getName());
/* 353 */       writeStreamToFile(is, winnowingRulesFile);
/*     */     }
/*     */     
/* 356 */     File typeLevelWinnowingRulesFile = new File(MetaInfo.winnowings);
/* 357 */     if (!typeLevelWinnowingRulesFile.exists()) {
/* 358 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + typeLevelWinnowingRulesFile.getName());
/* 359 */       writeStreamToFile(is, typeLevelWinnowingRulesFile);
/*     */     }
/*     */     
/* 362 */     File includedPrimedDeltaKBFile = new File(MetaInfo.lsclipseRefactorDeltaPrimed);
/* 363 */     if (!includedPrimedDeltaKBFile.exists()) {
/* 364 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + includedPrimedDeltaKBFile.getName());
/* 365 */       writeStreamToFile(is, includedPrimedDeltaKBFile);
/*     */     }
/*     */     
/* 368 */     File includedPred1File = new File(MetaInfo.lsclipseRefactorPred);
/* 369 */     if (!includedPred1File.exists()) {
/* 370 */       InputStream is = LSclipse.getDefault().getClass().getResourceAsStream("/lib/" + includedPred1File.getName());
/* 371 */       writeStreamToFile(is, includedPred1File);
/*     */     }
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
/*     */   private static LSDPredicate makeLSDPredicate(Fact.FactTypes type, String modifier)
/*     */   {
/* 418 */     switch (type) {
/* 419 */     case ACCESSES:  return LSDPredicate.getPredicate(modifier + "_" + "package");
/* 420 */     case CALLS:  return LSDPredicate.getPredicate(modifier + "_" + "type");
/* 421 */     case CONDITIONAL:  return LSDPredicate.getPredicate(modifier + "_" + "field");
/* 422 */     case CAST:  return LSDPredicate.getPredicate(modifier + "_" + "method");
/*     */     case EXTENDS: 
/* 424 */       return LSDPredicate.getPredicate(modifier + "_" + "return");
/* 425 */     case GETTER:  return LSDPredicate.getPredicate(modifier + "_" + "subtype");
/*     */     case FIELDMODIFIER: 
/* 427 */       return LSDPredicate.getPredicate(modifier + "_" + "accesses");
/* 428 */     case FIELDOFTYPE:  return LSDPredicate.getPredicate(modifier + "_" + "calls");
/* 429 */     case INHERITEDMETHOD:  return LSDPredicate.getPredicate(modifier + "_" + "inheritedfield");
/* 430 */     case LOCALVAR:  return LSDPredicate.getPredicate(modifier + "_" + "inheritedmethod");
/* 431 */     case FIELD:  return LSDPredicate.getPredicate(modifier + "_" + "fieldoftype");
/* 432 */     case METHOD:  return LSDPredicate.getPredicate(modifier + "_" + "typeintype");
/*     */     case IMPLEMENTS: 
/* 434 */       return LSDPredicate.getPredicate(modifier + "_" + "extends");
/* 435 */     case INHERITEDFIELD:  return LSDPredicate.getPredicate(modifier + "_" + "implements");
/* 436 */     case METHODSIGNATURE:  return LSDPredicate.getPredicate(modifier + "_" + "conditional");
/* 437 */     case METHODBODY:  return LSDPredicate.getPredicate(modifier + "_" + "methodbody");
/* 438 */     case PACKAGE:  return LSDPredicate.getPredicate(modifier + "_" + "parameter");
/* 439 */     case PARAMETER:  return LSDPredicate.getPredicate(modifier + "_" + "methodmodifier");
/* 440 */     case RETURN:  return LSDPredicate.getPredicate(modifier + "_" + "fieldmodifier");
/*     */     
/*     */     case SETTER: 
/* 443 */       return LSDPredicate.getPredicate(modifier + "_" + "cast");
/* 444 */     case SUBTYPE:  return LSDPredicate.getPredicate(modifier + "_" + "trycatch");
/* 445 */     case THROWN:  return LSDPredicate.getPredicate(modifier + "_" + "throws");
/* 446 */     case TRYCATCH:  return LSDPredicate.getPredicate(modifier + "_" + "getter");
/* 447 */     case TYPE:  return LSDPredicate.getPredicate(modifier + "_" + "setter");
/* 448 */     case TYPEINTYPE:  return LSDPredicate.getPredicate(modifier + "_" + "localvar");
/*     */     }
/* 450 */     return null;
/*     */   }
/*     */   
/*     */   private static LSDFact makeLSDFact(Fact f, String modifier) {
/* 454 */     LSDPredicate pred = makeLSDPredicate(f.type, modifier);
/* 455 */     List<String> constants = new ArrayList();
/* 456 */     int numparams = f.params.size();
/* 457 */     if ((f.type == Fact.FactTypes.METHOD) || (f.type == Fact.FactTypes.FIELD) || (f.type == Fact.FactTypes.TYPE))
/* 458 */       numparams--;
/* 459 */     for (int i = 0; i < numparams; i++) {
/* 460 */       constants.add(StringCleaner.cleanupString((String)f.params.get(i)));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 471 */     return LSDFact.createLSDFact(pred, constants, true);
/*     */   }
/*     */   
/* 474 */   private static LSDPredicate makeLSDPredicate(AtomicChange ac) { String modifier = "";
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
/* 487 */     if ((ac.type.ordinal() >= AtomicChange.ChangeTypes.ADD_PACKAGE.ordinal()) && 
/* 488 */       (ac.type.ordinal() <= AtomicChange.ChangeTypes.ADD_FIELDMODIFIER.ordinal())) {
/* 489 */       modifier = "added";
/* 490 */     } else if ((ac.type.ordinal() >= AtomicChange.ChangeTypes.DEL_PACKAGE.ordinal()) && 
/* 491 */       (ac.type.ordinal() <= AtomicChange.ChangeTypes.DEL_FIELDMODIFIER.ordinal())) {
/* 492 */       modifier = "deleted";
/*     */     } else {
/* 494 */       modifier = "modified";
/*     */     }
/*     */     
/* 497 */     switch (ac.type) {
/*     */     case ADD_ACCESSES: case CHANGE_METHODSIGNATURE: 
/*     */     case DEL_TYPEINTYPE: 
/* 500 */       return LSDPredicate.getPredicate(modifier + "_" + "package");
/*     */     case ADD_CALLS: case DEL_ACCESSES: 
/*     */     case MOD_FIELD: 
/* 503 */       return LSDPredicate.getPredicate(modifier + "_" + "type");
/*     */     case ADD_CONDITIONAL: case DEL_CAST: 
/*     */     case MOD_PACKAGE: 
/* 506 */       return LSDPredicate.getPredicate(modifier + "_" + "field");
/*     */     case ADD_CAST: case DEL_CALLS: 
/*     */     case MOD_METHOD: 
/* 509 */       return LSDPredicate.getPredicate(modifier + "_" + "method");
/*     */     case ADD_EXTENDS: 
/*     */     case DEL_CONDITIONAL: 
/* 512 */       return LSDPredicate.getPredicate(modifier + "_" + "return");
/*     */     case ADD_GETTER: case DEL_FIELDOFTYPE: 
/* 514 */       return LSDPredicate.getPredicate(modifier + "_" + "subtype");
/*     */     case ADD_FIELDMODIFIER: 
/*     */     case DEL_FIELD: 
/* 517 */       return LSDPredicate.getPredicate(modifier + "_" + "accesses");
/*     */     case ADD_FIELDOFTYPE: case DEL_FIELDMODIFIER: 
/* 519 */       return LSDPredicate.getPredicate(modifier + "_" + "calls");
/*     */     case ADD_INHERITEDMETHOD: case DEL_INHERITEDFIELD: 
/* 521 */       return LSDPredicate.getPredicate(modifier + "_" + "inheritedfield");
/*     */     case ADD_LOCALVAR: case DEL_INHERITEDMETHOD: 
/* 523 */       return LSDPredicate.getPredicate(modifier + "_" + "inheritedmethod");
/*     */     case ADD_FIELD: case DEL_EXTENDS: 
/* 525 */       return LSDPredicate.getPredicate(modifier + "_" + "fieldoftype");
/*     */     case ADD_METHOD: case DEL_LOCALVAR: 
/* 527 */       return LSDPredicate.getPredicate(modifier + "_" + "typeintype");
/*     */     case ADD_IMPLEMENTS: 
/*     */     case DEL_GETTER: 
/* 530 */       return LSDPredicate.getPredicate(modifier + "_" + "extends");
/*     */     case ADD_INHERITEDFIELD: case DEL_IMPLEMENTS: 
/* 532 */       return LSDPredicate.getPredicate(modifier + "_" + "implements");
/*     */     
/*     */     case ADD_THROWN: 
/*     */     case DEL_SUBTYPE: 
/* 536 */       return LSDPredicate.getPredicate(modifier + "_" + "conditional");
/*     */     
/*     */     case ADD_SUBTYPE: 
/*     */     case DEL_SETTER: 
/* 540 */       return LSDPredicate.getPredicate(modifier + "_" + "methodbody");
/*     */     
/*     */     case ADD_TRYCATCH: 
/*     */     case DEL_THROWN: 
/* 544 */       return LSDPredicate.getPredicate(modifier + "_" + "parameter");
/*     */     
/*     */     case ADD_TYPE: 
/*     */     case DEL_TRYCATCH: 
/* 548 */       return LSDPredicate.getPredicate(modifier + "_" + "methodmodifier");
/*     */     
/*     */     case ADD_TYPEINTYPE: 
/*     */     case DEL_TYPE: 
/* 552 */       return LSDPredicate.getPredicate(modifier + "_" + "fieldmodifier");
/*     */     
/*     */     case ADD_METHODBODY: 
/*     */     case DEL_METHOD: 
/* 556 */       return LSDPredicate.getPredicate(modifier + "_" + "cast");
/*     */     case ADD_METHODMODIFIER: 
/*     */     case DEL_METHODBODY: 
/* 559 */       return LSDPredicate.getPredicate(modifier + "_" + "trycatch");
/*     */     case ADD_PACKAGE: 
/*     */     case DEL_METHODMODIFIER: 
/* 562 */       return LSDPredicate.getPredicate(modifier + "_" + "throws");
/*     */     case ADD_PARAMETER: 
/*     */     case DEL_PACKAGE: 
/* 565 */       return LSDPredicate.getPredicate(modifier + "_" + "getter");
/*     */     case ADD_RETURN: 
/*     */     case DEL_PARAMETER: 
/* 568 */       return LSDPredicate.getPredicate(modifier + "_" + "setter");
/*     */     case ADD_SETTER: 
/*     */     case DEL_RETURN: 
/* 571 */       return LSDPredicate.getPredicate(modifier + "_" + "localvar"); }
/* 572 */     return null;
/*     */   }
/*     */   
/*     */   private static LSDFact makeLSDFact(AtomicChange ac)
/*     */   {
/* 577 */     LSDPredicate pred = makeLSDPredicate(ac);
/* 578 */     List<String> constants = new ArrayList();
/* 579 */     for (String s : ac.params) {
/* 580 */       constants.add(StringCleaner.cleanupString(s));
/*     */     }
/*     */     
/* 583 */     return LSDFact.createLSDFact(pred, constants, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Set<ICompilationUnit> getFiles(String projname)
/*     */     throws CoreException
/*     */   {
/* 607 */     IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
/* 608 */     IProject proj = ws.getProject(projname);
/* 609 */     IJavaProject javaProject = JavaCore.create(proj);
/* 610 */     Set<ICompilationUnit> files = new HashSet();
/* 611 */     javaProject.open(new NullProgressMonitor());
/* 612 */     IPackageFragment[] arrayOfIPackageFragment; int j = (arrayOfIPackageFragment = javaProject.getPackageFragments()).length; for (int i = 0; i < j; i++) { IPackageFragment packFrag = arrayOfIPackageFragment[i];
/* 613 */       ICompilationUnit[] arrayOfICompilationUnit; int m = (arrayOfICompilationUnit = packFrag.getCompilationUnits()).length; for (int k = 0; k < m; k++) { ICompilationUnit icu = arrayOfICompilationUnit[k];
/* 614 */         files.add(icu);
/*     */       }
/*     */     }
/* 617 */     javaProject.close();
/* 618 */     return files;
/*     */   }
/*     */   
/*     */   private static void writeStreamToFile(InputStream is, File file) {
/*     */     try {
/* 623 */       OutputStream out = new FileOutputStream(file);
/* 624 */       byte[] buf = new byte['Ѐ'];
/*     */       int len;
/* 626 */       while ((len = is.read(buf)) > 0) { int len;
/* 627 */         out.write(buf, 0, len); }
/* 628 */       out.close();
/* 629 */       is.close();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/LSDiffRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */