/*     */ package lsd.facts;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import lsd.rule.LSDBinding;
/*     */ import lsd.rule.LSDConst;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDPredicate;
/*     */ 
/*     */ public class LSdiffHierarchialDeltaKB
/*     */ {
/*  18 */   public String ADDED = "ADD";
/*  19 */   public String DELETED = "DELETE";
/*  20 */   public String MODIFIED = "MODIFY";
/*     */   
/*     */   public static final int PACKAGE_LEVEL = 0;
/*     */   
/*     */   public static final int TYPE_LEVEL = 1;
/*     */   public static final int TYPE_DEPENDENCY_LEVEL = 2;
/*     */   public static final int METHOD_LEVEL = 3;
/*     */   public static final int FIELD_LEVEL = 4;
/*     */   public static final int BODY_LEVEL = 5;
/*  29 */   HashMap<String, TreeSet<LSDFact>> packageLevel = new HashMap();
/*  30 */   HashMap<String, TreeSet<LSDFact>> typeLevel = new HashMap();
/*  31 */   HashMap<String, TreeSet<LSDFact>> methodLevel = new HashMap();
/*  32 */   HashMap<String, TreeSet<LSDFact>> fieldLevel = new HashMap();
/*     */   
/*  34 */   private LSdiffFilter filter = new LSdiffFilter(true, true, true, true, true);
/*     */   private ArrayList<LSDFact> originalDeltaKB;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  39 */     File deltaKBFile = new File("input/jfreechart/1.0.12_1.0.13delta.rub");
/*  40 */     ArrayList<LSDFact> deltaKB = new lsd.io.LSDTyrubaFactReader(deltaKBFile).getFacts();
/*  41 */     LSdiffHierarchialDeltaKB modifiedFB = new LSdiffHierarchialDeltaKB(deltaKB);
/*  42 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/*  43 */     TreeSet<LSDFact> ontheflyDeltaKB2 = new TreeSet();
/*     */     
/*  45 */     File temp = new File("temp-fileterdDelta");
/*  46 */     File temp2 = new File("temp-hFilteredDelta");
/*     */     
/*     */     try
/*     */     {
/*  50 */       PrintStream p = new PrintStream(temp);
/*  51 */       PrintStream p2 = new PrintStream(temp2);
/*     */       
/*  53 */       modifiedFB.filterFacts(p, ontheflyDeltaKB);
/*  54 */       modifiedFB.topDownTraversal2(p2, ontheflyDeltaKB2);
/*     */     } catch (java.io.FileNotFoundException e) {
/*  56 */       e.printStackTrace();
/*     */     }
/*  58 */     boolean result1 = modifiedFB.checkEquivalence(deltaKB, ontheflyDeltaKB);
/*     */     
/*  60 */     boolean result2 = modifiedFB.checkEquivalence(deltaKB, ontheflyDeltaKB2);
/*  61 */     System.err.println(result1);
/*  62 */     System.err.println(result2);
/*     */   }
/*     */   
/*  65 */   public LSdiffHierarchialDeltaKB(ArrayList<LSDFact> deltaKB) { this.originalDeltaKB = deltaKB;
/*  66 */     constructFieldLevel();
/*  67 */     constructMethodLevel();
/*  68 */     constructTypeLevel();
/*  69 */     constructPackageLevel();
/*     */   }
/*     */   
/*     */   public TreeSet<LSDFact> expandCluster(List<LSDFact> cluster, int level) {
/*  73 */     switch (level) {
/*     */     case 0: 
/*  75 */       return getPackageLevelFacts(null);
/*     */     case 1: 
/*  77 */       return expandPackageLevelCluster2TypeElements(null, cluster);
/*     */     case 2: 
/*  79 */       return expandTypeLevelCluster2TypeDependencies(null, cluster);
/*     */     case 3: 
/*  81 */       return expandTypeLevelCluster2Methods(null, cluster);
/*     */     case 4: 
/*  83 */       return expandTypeLevelCluster2Fields(null, cluster);
/*     */     case 5: 
/*  85 */       return expandMethodLevelCluster2Bodies(null, cluster);
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */   
/*  90 */   private boolean checkEquivalence(ArrayList<LSDFact> deltaKB, TreeSet<LSDFact> ontheflyDeltaKB) { ArrayList<LSDFact> tempOriginal = new ArrayList(deltaKB);
/*  91 */     System.out.println("original:\t" + tempOriginal.size());
/*  92 */     tempOriginal.removeAll(ontheflyDeltaKB);
/*  93 */     ArrayList<LSDFact> tempOntheFly = new ArrayList();
/*     */     
/*  95 */     for (LSDFact f : ontheflyDeltaKB) {
/*  96 */       if (f.getPredicate().getName().indexOf("modified_") < 0) {
/*  97 */         tempOntheFly.add(f);
/*     */       }
/*     */     }
/* 100 */     System.out.println("onthefly:\t" + tempOntheFly.size());
/* 101 */     tempOntheFly.removeAll(deltaKB);
/*     */     
/* 103 */     System.out.println("onthefly - original:\t" + tempOntheFly.size());
/*     */     
/* 105 */     for (LSDFact f : tempOntheFly) {
/* 106 */       System.out.println(f);
/*     */     }
/* 108 */     System.out.println("original - onthefly:\t" + tempOriginal.size());
/* 109 */     for (LSDFact f : tempOriginal) {
/* 110 */       System.out.println(f);
/*     */     }
/* 112 */     return (tempOriginal.size() == 0) && (tempOntheFly.size() == 0);
/*     */   }
/*     */   
/*     */ 
/*     */   private void filterFacts(PrintStream p, TreeSet<LSDFact> output)
/*     */   {
/* 118 */     output.addAll(getPackageLevelFacts(p));
/* 119 */     output.addAll(expandPackageLevelCluster2TypeElements(p, null));
/* 120 */     output.addAll(expandTypeLevelCluster2TypeDependencies(p, null));
/* 121 */     output.addAll(expandTypeLevelCluster2Methods(p, null));
/* 122 */     output.addAll(expandTypeLevelCluster2Fields(p, null));
/* 123 */     output.addAll(expandMethodLevelCluster2Bodies(p, null));
/*     */   }
/*     */   
/*     */   public void filterFacts(PrintStream p, TreeSet<LSDFact> output, LSdiffFilter filter) {
/* 127 */     assert (filter != null);
/* 128 */     if (filter.packageLevel) output.addAll(getPackageLevelFacts(p));
/* 129 */     if (filter.typeLevel) output.addAll(expandPackageLevelCluster2TypeElements(p, null));
/* 130 */     if (filter.typeLevel) output.addAll(expandTypeLevelCluster2TypeDependencies(p, null));
/* 131 */     if (filter.methodLevel) output.addAll(expandTypeLevelCluster2Methods(p, null));
/* 132 */     if (filter.fieldLevel) output.addAll(expandTypeLevelCluster2Fields(p, null));
/* 133 */     if (filter.bodyLevel) output.addAll(expandMethodLevelCluster2Bodies(p, null));
/*     */   }
/*     */   
/*     */   private TreeSet<LSDFact> getPackageLevelFacts(PrintStream p) {
/* 137 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 138 */     Iterator localIterator2; for (Iterator localIterator1 = this.packageLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 139 */         localIterator2.hasNext())
/*     */     {
/* 138 */       String kind = (String)localIterator1.next();
/* 139 */       localIterator2 = ((TreeSet)this.packageLevel.get(kind)).iterator(); continue;LSDFact packageF = (LSDFact)localIterator2.next();
/* 140 */       if (p != null) p.println(packageF);
/* 141 */       ontheflyDeltaKB.add(packageF);
/*     */     }
/*     */     
/* 144 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/*     */   private TreeSet<LSDFact> expandPackageLevelCluster2TypeElements(PrintStream p, List<LSDFact> packageLevelCluster) {
/* 148 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 149 */     TreeSet<String> packageConstants = null;
/*     */     
/* 151 */     if (packageLevelCluster != null) {
/* 152 */       packageConstants = new TreeSet();
/* 153 */       for (LSDFact packageF : packageLevelCluster) {
/* 154 */         packageConstants.add(((LSDBinding)packageF.getBindings().get(0)).getGroundConst());
/*     */       }
/*     */     }
/*     */     Iterator localIterator2;
/* 158 */     for (??? = this.typeLevel.keySet().iterator(); ???.hasNext(); 
/* 159 */         localIterator2.hasNext())
/*     */     {
/* 158 */       String kind = (String)???.next();
/* 159 */       localIterator2 = ((TreeSet)this.typeLevel.get(kind)).iterator(); continue;LSDFact typeF = (LSDFact)localIterator2.next();
/* 160 */       String containerPackage = ((LSDBinding)typeF.getBindings().get(2)).getGroundConst();
/* 161 */       if ((packageConstants == null) || (packageConstants.contains(containerPackage))) {
/* 162 */         if (p != null) p.println("\t" + typeF);
/* 163 */         ontheflyDeltaKB.add(typeF);
/*     */       }
/*     */     }
/*     */     
/* 167 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TreeSet<LSDFact> expandTypeLevelCluster2TypeDependencies(PrintStream p, List<LSDFact> typeLevelCluster)
/*     */   {
/* 177 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 178 */     TreeSet<String> typeConstants = null;
/* 179 */     if (typeLevelCluster != null) {
/* 180 */       typeConstants = new TreeSet();
/* 181 */       for (LSDFact typeF : typeLevelCluster) {
/* 182 */         typeConstants.add(((LSDBinding)typeF.getBindings().get(0)).getGroundConst());
/*     */       }
/*     */     }
/* 185 */     for (LSDFact fact : this.originalDeltaKB) {
/* 186 */       String involvedType = null;
/* 187 */       if (fact.getPredicate().getName().indexOf("_typeintype") > 0) {
/* 188 */         involvedType = ((LSDBinding)fact.getBindings().get(1)).getGroundConst();
/*     */ 
/*     */       }
/* 191 */       else if (fact.getPredicate().getName().indexOf("_extends") > 0) {
/* 192 */         involvedType = ((LSDBinding)fact.getBindings().get(1)).getGroundConst();
/*     */       }
/* 194 */       else if (fact.getPredicate().getName().indexOf("_implements") > 0) {
/* 195 */         involvedType = ((LSDBinding)fact.getBindings().get(1)).getGroundConst();
/*     */       }
/* 197 */       else if (fact.getPredicate().getName().indexOf("_inheritedfield") > 0) {
/* 198 */         involvedType = ((LSDBinding)fact.getBindings().get(2)).getGroundConst();
/* 199 */       } else if (fact.getPredicate().getName().indexOf("_inheritedmethod") > 0) {
/* 200 */         involvedType = ((LSDBinding)fact.getBindings().get(2)).getGroundConst();
/*     */       }
/* 202 */       if ((involvedType != null) && ((typeConstants == null) || (typeConstants.contains(involvedType)))) {
/* 203 */         if (p != null) p.println("\t\t\t" + fact);
/* 204 */         ontheflyDeltaKB.add(fact);
/*     */       }
/*     */     }
/* 207 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/*     */   private TreeSet<LSDFact> expandTypeLevelCluster2Methods(PrintStream p, List<LSDFact> typeLevelCluster) {
/* 211 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 212 */     TreeSet<String> typeConstants = null;
/* 213 */     TreeSet<String> methodConstants = new TreeSet();
/* 214 */     if (typeLevelCluster != null) {
/* 215 */       typeConstants = new TreeSet();
/* 216 */       for (LSDFact typeF : typeLevelCluster)
/* 217 */         typeConstants.add(((LSDBinding)typeF.getBindings().get(0)).getGroundConst());
/*     */     }
/*     */     Iterator localIterator2;
/* 220 */     for (??? = this.methodLevel.keySet().iterator(); ???.hasNext(); 
/* 221 */         localIterator2.hasNext())
/*     */     {
/* 220 */       String kind = (String)???.next();
/* 221 */       localIterator2 = ((TreeSet)this.methodLevel.get(kind)).iterator(); continue;LSDFact methodF = (LSDFact)localIterator2.next();
/* 222 */       String containerType = ((LSDBinding)methodF.getBindings().get(2)).getGroundConst();
/* 223 */       if ((typeConstants == null) || (typeConstants.contains(containerType))) {
/* 224 */         if (p != null) p.println("\t\t" + methodF);
/* 225 */         ontheflyDeltaKB.add(methodF);
/* 226 */         methodConstants.add(((LSDBinding)methodF.getBindings().get(0)).getGroundConst());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 231 */     for (LSDFact fact : this.originalDeltaKB) {
/* 232 */       if (fact.getPredicate().getName().indexOf("_return") > 0) {
/* 233 */         String involvedMethod = ((LSDBinding)fact.getBindings().get(0)).getGroundConst();
/* 234 */         if (methodConstants.contains(involvedMethod)) {
/* 235 */           ontheflyDeltaKB.add(fact);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 240 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/*     */   private TreeSet<LSDFact> expandTypeLevelCluster2Fields(PrintStream p, List<LSDFact> typeLevelCluster) {
/* 244 */     TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 245 */     TreeSet<String> typeConstants = null;
/* 246 */     TreeSet<String> fieldConstants = new TreeSet();
/* 247 */     if (typeLevelCluster != null) {
/* 248 */       typeConstants = new TreeSet();
/* 249 */       for (LSDFact typeF : typeLevelCluster)
/* 250 */         typeConstants.add(((LSDBinding)typeF.getBindings().get(0)).getGroundConst());
/*     */     }
/*     */     Iterator localIterator2;
/* 253 */     for (??? = this.fieldLevel.keySet().iterator(); ???.hasNext(); 
/* 254 */         localIterator2.hasNext())
/*     */     {
/* 253 */       String kind = (String)???.next();
/* 254 */       localIterator2 = ((TreeSet)this.fieldLevel.get(kind)).iterator(); continue;LSDFact fieldF = (LSDFact)localIterator2.next();
/* 255 */       String containerType = ((LSDBinding)fieldF.getBindings().get(2))
/* 256 */         .getGroundConst();
/*     */       
/* 258 */       if ((typeConstants == null) || 
/* 259 */         (typeConstants.contains(containerType))) {
/* 260 */         if (p != null)
/* 261 */           p.println("\t\t" + fieldF);
/* 262 */         ontheflyDeltaKB.add(fieldF);
/* 263 */         fieldConstants.add(((LSDBinding)fieldF.getBindings().get(0))
/* 264 */           .getGroundConst());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 269 */     for (LSDFact fact : this.originalDeltaKB) {
/* 270 */       if (fact.getPredicate().getName().indexOf("_fieldoftype") > 0) {
/* 271 */         String involvedField = ((LSDBinding)fact.getBindings().get(0)).getGroundConst();
/* 272 */         if (fieldConstants.contains(involvedField)) {
/* 273 */           ontheflyDeltaKB.add(fact);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 278 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/* 281 */   private TreeSet<LSDFact> expandMethodLevelCluster2Bodies(PrintStream p, List<LSDFact> methodLevelCluster) { TreeSet<LSDFact> ontheflyDeltaKB = new TreeSet();
/* 282 */     TreeSet<String> methodConstants = null;
/* 283 */     if (methodLevelCluster != null) {
/* 284 */       methodConstants = new TreeSet();
/* 285 */       for (LSDFact methodF : methodLevelCluster) {
/* 286 */         methodConstants.add(((LSDBinding)methodF.getBindings().get(0)).getGroundConst());
/*     */       }
/*     */     }
/*     */     
/* 290 */     for (LSDFact fact : this.originalDeltaKB) {
/* 291 */       String involvedMethod = null;
/* 292 */       if (fact.getPredicate().getName().indexOf("_calls") > 0) {
/* 293 */         involvedMethod = ((LSDBinding)fact.getBindings().get(0)).getGroundConst();
/*     */       }
/* 295 */       else if (fact.getPredicate().getName().indexOf("_accesses") > 0) {
/* 296 */         involvedMethod = ((LSDBinding)fact.getBindings().get(1)).getGroundConst();
/*     */       }
/* 298 */       if ((involvedMethod != null) && ((methodConstants == null) || (methodConstants.contains(involvedMethod)))) {
/* 299 */         if (p != null) p.println("\t\t\t" + fact);
/* 300 */         ontheflyDeltaKB.add(fact);
/*     */       }
/*     */     }
/* 303 */     return ontheflyDeltaKB;
/*     */   }
/*     */   
/*     */   private void printPakcageLevelFactStat(PrintStream p)
/*     */   {
/* 308 */     if (p != null) p.println("# added_package:\t" + ((TreeSet)this.packageLevel.get(this.ADDED)).size());
/* 309 */     if (p != null) p.println("# deleted_package:\t" + ((TreeSet)this.packageLevel.get(this.DELETED)).size());
/* 310 */     if (p != null) p.println("# modified_package:\t" + ((TreeSet)this.packageLevel.get(this.MODIFIED)).size());
/*     */   }
/*     */   
/*     */   private void printTypeLevelFactStat(PrintStream p) {
/* 314 */     if (p != null) p.println("# added_type:\t" + ((TreeSet)this.typeLevel.get(this.ADDED)).size());
/* 315 */     if (p != null) p.println("# deleted_type:\t" + ((TreeSet)this.typeLevel.get(this.DELETED)).size());
/* 316 */     if (p != null) p.println("# modified_type:\t" + ((TreeSet)this.typeLevel.get(this.MODIFIED)).size());
/*     */   }
/*     */   
/* 319 */   private void printMethodLevelFactStat(PrintStream p) { if (p != null) p.println("# added_method:\t" + ((TreeSet)this.methodLevel.get(this.ADDED)).size());
/* 320 */     if (p != null) p.println("# deleted_method:\t" + ((TreeSet)this.methodLevel.get(this.DELETED)).size());
/* 321 */     if (p != null) p.println("# modified_method:\t" + ((TreeSet)this.methodLevel.get(this.MODIFIED)).size());
/*     */   }
/*     */   
/* 324 */   private void printFieldLevelFactStat(PrintStream p) { if (p != null) p.println("# added_field:\t" + ((TreeSet)this.fieldLevel.get(this.ADDED)).size());
/* 325 */     if (p != null) p.println("# deleted_field:\t" + ((TreeSet)this.fieldLevel.get(this.DELETED)).size());
/* 326 */     if (p != null) { p.println("# modified_field:\t" + ((TreeSet)this.fieldLevel.get(this.MODIFIED)).size());
/*     */     }
/*     */   }
/*     */   
/*     */   private void constructFieldLevel()
/*     */   {
/* 332 */     TreeSet<LSDFact> addedField = new TreeSet();
/* 333 */     TreeSet<LSDFact> deletedField = new TreeSet();
/* 334 */     TreeSet<LSDFact> modifiedField = new TreeSet();
/* 335 */     String predName; for (LSDFact fact : this.originalDeltaKB) {
/* 336 */       predName = fact.getPredicate().getName();
/* 337 */       if (predName.equals("added_field"))
/*     */       {
/*     */ 
/* 340 */         addedField.add(fact);
/* 341 */       } else if (predName.equals("deleted_field"))
/*     */       {
/*     */ 
/* 344 */         deletedField.add(fact);
/*     */       }
/*     */     }
/* 347 */     int counter = 0;
/* 348 */     for (LSDFact fact : this.originalDeltaKB) {
/* 349 */       String predName = fact.getPredicate().getName();
/* 350 */       counter++;
/* 351 */       System.out.println(counter + ". \"" + predName + "\":" + fact);
/* 352 */       if ((predName.equals("added_fieldoftype")) || 
/* 353 */         (predName.equals("deleted_fieldoftype")))
/*     */       {
/*     */ 
/* 356 */         List<LSDBinding> bindings = fact.getBindings();
/* 357 */         LSDBinding firstBinding = (LSDBinding)bindings.get(0);
/* 358 */         LSDFact mfield = LSDConst.createModifiedField(firstBinding
/* 359 */           .getGroundConst());
/* 360 */         if (!containsTheSameFact(addedField, deletedField, mfield)) {
/* 361 */           modifiedField.add(mfield);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 366 */     this.fieldLevel.put(this.ADDED, addedField);
/* 367 */     this.fieldLevel.put(this.DELETED, deletedField);
/* 368 */     this.fieldLevel.put(this.MODIFIED, modifiedField);
/*     */   }
/*     */   
/*     */   private void constructMethodLevel() {
/* 372 */     TreeSet<LSDFact> addedMethod = new TreeSet();
/* 373 */     TreeSet<LSDFact> deletedMethod = new TreeSet();
/* 374 */     TreeSet<LSDFact> modifiedMethod = new TreeSet();
/*     */     
/* 376 */     for (LSDFact fact : this.originalDeltaKB) {
/* 377 */       String predName = fact.getPredicate().getName();
/* 378 */       if (predName.equals("added_method"))
/*     */       {
/* 380 */         addedMethod.add(fact);
/* 381 */       } else if (predName.equals("deleted_method"))
/*     */       {
/* 383 */         deletedMethod.add(fact);
/*     */       }
/*     */     }
/* 386 */     for (LSDFact fact : this.originalDeltaKB) {
/* 387 */       String predName = fact.getPredicate().getName();
/* 388 */       if ((predName.equals("added_return")) || 
/* 389 */         (predName.equals("deleted_return"))) {
/* 390 */         List<LSDBinding> bindings = fact.getBindings();
/* 391 */         LSDBinding firstBinding = (LSDBinding)bindings.get(0);
/* 392 */         LSDFact mmethod = LSDConst.createModifiedMethod(firstBinding
/* 393 */           .getGroundConst());
/* 394 */         if (!containsTheSameFact(addedMethod, deletedMethod, mmethod)) {
/* 395 */           modifiedMethod.add(mmethod);
/*     */         }
/* 397 */       } else if ((predName.equals("deleted_calls")) || 
/* 398 */         (predName.equals("added_calls")))
/*     */       {
/*     */ 
/* 401 */         List<LSDBinding> bindings = fact.getBindings();
/* 402 */         LSDBinding firstBinding = (LSDBinding)bindings.get(0);
/* 403 */         LSDFact mmethod = LSDConst.createModifiedMethod(firstBinding
/* 404 */           .getGroundConst());
/* 405 */         if (!containsTheSameFact(addedMethod, deletedMethod, mmethod))
/* 406 */           modifiedMethod.add(mmethod);
/* 407 */       } else if ((predName.equals("added_accesses")) || 
/* 408 */         (predName.equals("deleted_accesses")))
/*     */       {
/*     */ 
/*     */ 
/* 412 */         List<LSDBinding> bindings = fact.getBindings();
/* 413 */         LSDBinding secondBinding = (LSDBinding)bindings.get(1);
/* 414 */         LSDFact mmethod = LSDConst.createModifiedMethod(secondBinding
/* 415 */           .getGroundConst());
/* 416 */         if (!containsTheSameFact(addedMethod, deletedMethod, mmethod))
/* 417 */           modifiedMethod.add(mmethod);
/*     */       }
/*     */     }
/* 420 */     this.methodLevel.put(this.ADDED, addedMethod);
/* 421 */     this.methodLevel.put(this.DELETED, deletedMethod);
/* 422 */     this.methodLevel.put(this.MODIFIED, modifiedMethod);
/*     */   }
/*     */   
/*     */   private void constructTypeLevel() {
/* 426 */     TreeSet<LSDFact> addedType = new TreeSet();
/* 427 */     TreeSet<LSDFact> deletedType = new TreeSet();
/* 428 */     TreeSet<LSDFact> modifiedType = new TreeSet();
/*     */     
/* 430 */     for (LSDFact fact : this.originalDeltaKB) {
/* 431 */       String predName = fact.getPredicate().getName();
/* 432 */       if (predName.equals("added_type"))
/*     */       {
/* 434 */         addedType.add(fact);
/* 435 */       } else if (predName.equals("deleted_type"))
/*     */       {
/* 437 */         deletedType.add(fact);
/*     */       }
/*     */     }
/*     */     List<LSDBinding> bindings;
/* 441 */     for (LSDFact fact : this.originalDeltaKB) {
/* 442 */       String predName = fact.getPredicate().getName();
/* 443 */       if (predName.endsWith("_typeintype"))
/*     */       {
/* 445 */         List<LSDBinding> bindings = fact.getBindings();
/* 446 */         LSDBinding secondBinding = (LSDBinding)bindings.get(1);
/* 447 */         LSDFact mtype = LSDConst.createModifiedType(secondBinding.getGroundConst());
/* 448 */         if (!containsTheSameFact(addedType, deletedType, mtype)) modifiedType.add(mtype);
/* 449 */       } else if ((predName.endsWith("_extends")) || (predName.endsWith("_implements")))
/*     */       {
/* 451 */         List<LSDBinding> bindings = fact.getBindings();
/* 452 */         LSDBinding secondBinding = (LSDBinding)bindings.get(1);
/* 453 */         LSDFact mtype = LSDConst.createModifiedType(secondBinding.getGroundConst());
/* 454 */         if (!containsTheSameFact(addedType, deletedType, mtype)) modifiedType.add(mtype);
/*     */       }
/* 456 */       else if (predName.endsWith("_inheritedmethod"))
/*     */       {
/* 458 */         List<LSDBinding> bindings = fact.getBindings();
/* 459 */         LSDBinding secondBinding = (LSDBinding)bindings.get(1);
/* 460 */         LSDFact mtype = LSDConst.createModifiedType(secondBinding.getGroundConst());
/* 461 */         modifiedType.add(mtype);
/*     */         
/*     */ 
/* 464 */         LSDBinding thirdBinding = (LSDBinding)bindings.get(2);
/* 465 */         LSDFact m2type = LSDConst.createModifiedType(thirdBinding.getGroundConst());
/* 466 */         if (!containsTheSameFact(addedType, deletedType, m2type)) modifiedType.add(m2type);
/*     */       }
/* 468 */       else if (predName.endsWith("_inheritedfield"))
/*     */       {
/*     */ 
/*     */ 
/* 472 */         bindings = fact.getBindings();
/* 473 */         LSDBinding secondBinding = (LSDBinding)bindings.get(1);
/* 474 */         LSDFact mtype = LSDConst.createModifiedType(secondBinding.getGroundConst());
/* 475 */         if (!containsTheSameFact(addedType, deletedType, mtype)) { modifiedType.add(mtype);
/*     */         }
/*     */         
/* 478 */         LSDBinding thirdBinding = (LSDBinding)bindings.get(2);
/* 479 */         LSDFact m2type = LSDConst.createModifiedType(thirdBinding.getGroundConst());
/* 480 */         if (!containsTheSameFact(addedType, deletedType, m2type)) { modifiedType.add(m2type);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 485 */     for (??? = this.methodLevel.keySet().iterator(); ???.hasNext(); 
/* 486 */         bindings.hasNext())
/*     */     {
/* 485 */       String kind = (String)???.next();
/* 486 */       bindings = ((TreeSet)this.methodLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)bindings.next();
/*     */       
/* 488 */       List<LSDBinding> bindings = fact.getBindings();
/* 489 */       LSDBinding thirdBinding = (LSDBinding)bindings.get(2);
/* 490 */       LSDFact mtype = LSDConst.createModifiedType(thirdBinding.getGroundConst());
/* 491 */       if (!containsTheSameFact(addedType, deletedType, mtype)) { modifiedType.add(mtype);
/*     */       }
/*     */     }
/*     */     
/* 495 */     for (??? = this.fieldLevel.keySet().iterator(); ???.hasNext(); 
/* 496 */         bindings.hasNext())
/*     */     {
/* 495 */       String kind = (String)???.next();
/* 496 */       bindings = ((TreeSet)this.fieldLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)bindings.next();
/*     */       
/* 498 */       List<LSDBinding> bindings = fact.getBindings();
/* 499 */       LSDBinding thirdBinding = (LSDBinding)bindings.get(2);
/* 500 */       LSDFact mtype = LSDConst.createModifiedType(thirdBinding
/* 501 */         .getGroundConst());
/* 502 */       if (!containsTheSameFact(addedType, deletedType, mtype)) { modifiedType.add(mtype);
/*     */       }
/*     */     }
/* 505 */     this.typeLevel.put(this.ADDED, addedType);
/* 506 */     this.typeLevel.put(this.DELETED, deletedType);
/* 507 */     this.typeLevel.put(this.MODIFIED, modifiedType);
/*     */   }
/*     */   
/*     */   private void constructPackageLevel() {
/* 511 */     TreeSet<LSDFact> addedPackage = new TreeSet();
/* 512 */     TreeSet<LSDFact> deletedPackage = new TreeSet();
/* 513 */     TreeSet<LSDFact> modifiedPackage = new TreeSet();
/*     */     
/* 515 */     for (LSDFact fact : this.originalDeltaKB) {
/* 516 */       String predName = fact.getPredicate().getName();
/* 517 */       if (predName.equals("added_package"))
/*     */       {
/* 519 */         addedPackage.add(fact);
/* 520 */       } else if (predName.equals("deleted_package"))
/*     */       {
/* 522 */         deletedPackage.add(fact);
/*     */       }
/*     */     }
/*     */     Iterator localIterator2;
/* 526 */     for (??? = this.typeLevel.keySet().iterator(); ???.hasNext(); 
/* 527 */         localIterator2.hasNext())
/*     */     {
/* 526 */       String kind = (String)???.next();
/* 527 */       localIterator2 = ((TreeSet)this.typeLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)localIterator2.next();
/*     */       
/* 529 */       List<LSDBinding> bindings = fact.getBindings();
/* 530 */       LSDBinding thirdBinding = (LSDBinding)bindings.get(2);
/* 531 */       LSDFact mpackage = LSDConst.createModifiedPackage(thirdBinding
/* 532 */         .getGroundConst());
/*     */       
/* 534 */       if (!containsTheSameFact(addedPackage, deletedPackage, mpackage)) { modifiedPackage.add(mpackage);
/*     */       }
/*     */     }
/* 537 */     this.packageLevel.put(this.ADDED, addedPackage);
/* 538 */     this.packageLevel.put(this.DELETED, deletedPackage);
/* 539 */     this.packageLevel.put(this.MODIFIED, modifiedPackage);
/*     */   }
/*     */   
/*     */   private boolean containsTheSameFact(TreeSet<LSDFact> addSet, TreeSet<LSDFact> deletedSet, LSDFact mf) {
/* 543 */     LSDFact add = LSDConst.convertModifiedToAdded(mf);
/* 544 */     LSDFact del = LSDConst.convertModifiedToDeleted(mf);
/* 545 */     return (addSet.contains(add)) || (deletedSet.contains(del));
/*     */   }
/*     */   
/*     */   private void filterFacts2(PrintStream p, TreeSet<LSDFact> output, LSdiffFilter filter)
/*     */   {
/* 550 */     if (filter == null) return;
/* 551 */     this.filter = filter;
/* 552 */     topDownTraversal(p, output);
/*     */   }
/*     */   
/*     */   private void filterPerType(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF)
/*     */   {
/* 557 */     printSubtypes(p, ontheflyDeltaKB, typeF);
/*     */     
/* 559 */     printInnerTypes(p, ontheflyDeltaKB, typeF);
/*     */     
/* 561 */     printInheritedMethods(p, ontheflyDeltaKB, typeF);
/*     */     
/* 563 */     printInheritedFields(p, ontheflyDeltaKB, typeF);
/* 564 */     printMethodsInType(p, ontheflyDeltaKB, typeF);
/* 565 */     printFieldsInType(p, ontheflyDeltaKB, typeF);
/*     */   }
/*     */   
/*     */   private void filterPerMethod(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact methodF) {
/* 569 */     printCallsInMethod(p, ontheflyDeltaKB, methodF);
/* 570 */     printAccessesInMethod(p, ontheflyDeltaKB, methodF);
/* 571 */     printReturnInMethod(p, ontheflyDeltaKB, methodF);
/*     */   }
/*     */   
/* 574 */   private void filterPerField(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact fieldF) { printFieldOfType(p, ontheflyDeltaKB, fieldF); }
/*     */   
/*     */ 
/*     */   private void topDownTraversal(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB)
/*     */   {
/*     */     Iterator localIterator2;
/* 580 */     for (Iterator localIterator1 = this.packageLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 581 */         localIterator2.hasNext())
/*     */     {
/* 580 */       String kind = (String)localIterator1.next();
/* 581 */       localIterator2 = ((TreeSet)this.packageLevel.get(kind)).iterator(); continue;LSDFact packageF = (LSDFact)localIterator2.next();
/* 582 */       if ((this.filter.packageLevel) && (p != null)) p.println(packageF);
/* 583 */       if (this.filter.packageLevel) ontheflyDeltaKB.add(packageF);
/* 584 */       filterPerPackage(p, ontheflyDeltaKB, packageF);
/*     */     }
/*     */   }
/*     */   
/*     */   public void filterPerPackage(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact packageF) {
/*     */     Iterator localIterator2;
/* 590 */     for (Iterator localIterator1 = this.typeLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 591 */         localIterator2.hasNext())
/*     */     {
/* 590 */       String kind = (String)localIterator1.next();
/* 591 */       localIterator2 = ((TreeSet)this.typeLevel.get(kind)).iterator(); continue;LSDFact typeF = (LSDFact)localIterator2.next();
/* 592 */       if (((LSDBinding)typeF.getBindings().get(2)).getGroundConst().equals(
/* 593 */         ((LSDBinding)packageF.getBindings().get(0)).getGroundConst())) {
/* 594 */         if ((this.filter.typeLevel) && (p != null)) p.println("\t" + typeF);
/* 595 */         if (this.filter.typeLevel) ontheflyDeltaKB.add(typeF);
/* 596 */         filterPerType(p, ontheflyDeltaKB, typeF);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void printMethodsInType(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF)
/*     */   {
/*     */     Iterator localIterator2;
/* 604 */     for (Iterator localIterator1 = this.methodLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 605 */         localIterator2.hasNext())
/*     */     {
/* 604 */       String kind = (String)localIterator1.next();
/* 605 */       localIterator2 = ((TreeSet)this.methodLevel.get(kind)).iterator(); continue;LSDFact methodF = (LSDFact)localIterator2.next();
/* 606 */       if (((LSDBinding)methodF.getBindings().get(2)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst())) {
/* 607 */         if ((this.filter.methodLevel) && (p != null)) p.println("\t\t" + methodF);
/* 608 */         if (this.filter.methodLevel) ontheflyDeltaKB.add(methodF);
/* 609 */         filterPerMethod(p, ontheflyDeltaKB, methodF);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void printFieldsInType(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF) {
/*     */     Iterator localIterator2;
/* 616 */     for (Iterator localIterator1 = this.fieldLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 617 */         localIterator2.hasNext())
/*     */     {
/* 616 */       String kind = (String)localIterator1.next();
/* 617 */       localIterator2 = ((TreeSet)this.fieldLevel.get(kind)).iterator(); continue;LSDFact fieldF = (LSDFact)localIterator2.next();
/* 618 */       if (((LSDBinding)fieldF.getBindings().get(2)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst())) {
/* 619 */         if ((this.filter.fieldLevel) && (p != null)) p.println("\t\t" + fieldF);
/* 620 */         if (this.filter.fieldLevel) ontheflyDeltaKB.add(fieldF);
/* 621 */         filterPerField(p, ontheflyDeltaKB, fieldF);
/*     */       }
/*     */     } }
/*     */   
/*     */   private void printInnerTypes(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF) { LSDFact fact;
/*     */     label133:
/* 627 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 630 */         p.println("\t\t\t" + fact))
/*     */     {
/* 627 */       fact = (LSDFact)localIterator.next();
/* 628 */       if ((fact.getPredicate().getName().indexOf("_typeintype") <= 0) || (!((LSDBinding)fact.getBindings().get(1)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst()))) break label133;
/* 629 */       if (this.filter.typeLevel) ontheflyDeltaKB.add(fact);
/* 630 */       if ((!this.filter.typeLevel) || (p == null))
/*     */         break label133;
/*     */     }
/*     */   }
/*     */   
/* 635 */   private void printSubtypes(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF) { for (LSDFact fact : this.originalDeltaKB)
/*     */     {
/* 637 */       if ((fact.getPredicate().getName().indexOf("_extends") > 0) && (((LSDBinding)fact.getBindings().get(1)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst()))) {
/* 638 */         if (this.filter.typeLevel) ontheflyDeltaKB.add(fact);
/* 639 */         if ((this.filter.typeLevel) && (p != null)) p.println("\t\t\t" + fact);
/*     */       }
/* 641 */       else if ((fact.getPredicate().getName().indexOf("_implements") > 0) && (((LSDBinding)fact.getBindings().get(1)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst()))) {
/* 642 */         if (this.filter.typeLevel) ontheflyDeltaKB.add(fact);
/* 643 */         if ((this.filter.typeLevel) && (p != null)) p.println("\t\t\t" + fact);
/*     */       } }
/*     */   }
/*     */   
/*     */   private void printCallsInMethod(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact methodF) { LSDFact fact;
/*     */     label133:
/* 649 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 652 */         p.println("\t\t\t" + fact))
/*     */     {
/* 649 */       fact = (LSDFact)localIterator.next();
/* 650 */       if ((fact.getPredicate().getName().indexOf("_calls") <= 0) || (!((LSDBinding)fact.getBindings().get(0)).getGroundConst().equals(((LSDBinding)methodF.getBindings().get(0)).getGroundConst()))) break label133;
/* 651 */       if (this.filter.bodyLevel) ontheflyDeltaKB.add(fact);
/* 652 */       if ((!this.filter.bodyLevel) || (p == null)) break label133;
/*     */     } }
/*     */   
/*     */   private void printAccessesInMethod(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact methodF) { LSDFact fact;
/*     */     label133:
/* 657 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 660 */         p.println("\t\t\t" + fact))
/*     */     {
/* 657 */       fact = (LSDFact)localIterator.next();
/* 658 */       if ((fact.getPredicate().getName().indexOf("_accesses") <= 0) || (!((LSDBinding)fact.getBindings().get(1)).getGroundConst().equals(((LSDBinding)methodF.getBindings().get(0)).getGroundConst()))) break label133;
/* 659 */       if (this.filter.bodyLevel) ontheflyDeltaKB.add(fact);
/* 660 */       if ((!this.filter.bodyLevel) || (p == null)) break label133;
/*     */     } }
/*     */   
/*     */   private void printReturnInMethod(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact methodF) { LSDFact fact;
/*     */     label133:
/* 665 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 668 */         p.println("\t\t\t" + fact))
/*     */     {
/* 665 */       fact = (LSDFact)localIterator.next();
/* 666 */       if ((fact.getPredicate().getName().indexOf("_return") <= 0) || (!((LSDBinding)fact.getBindings().get(0)).getGroundConst().equals(((LSDBinding)methodF.getBindings().get(0)).getGroundConst()))) break label133;
/* 667 */       if (this.filter.methodLevel) ontheflyDeltaKB.add(fact);
/* 668 */       if ((!this.filter.methodLevel) || (p == null)) break label133;
/*     */     }
/*     */   }
/*     */   
/*     */   private void printInheritedFields(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF) { LSDFact fact;
/*     */     label133:
/* 674 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 677 */         p.println("\t\t\t" + fact))
/*     */     {
/* 674 */       fact = (LSDFact)localIterator.next();
/* 675 */       if ((fact.getPredicate().getName().indexOf("_inheritedfield") <= 0) || (!((LSDBinding)fact.getBindings().get(2)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst()))) break label133;
/* 676 */       if (this.filter.fieldLevel) ontheflyDeltaKB.add(fact);
/* 677 */       if ((!this.filter.fieldLevel) || (p == null)) break label133;
/*     */     } }
/*     */   
/*     */   private void printInheritedMethods(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact typeF) { LSDFact fact;
/*     */     label133:
/* 682 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 685 */         p.println("\t\t\t" + fact))
/*     */     {
/* 682 */       fact = (LSDFact)localIterator.next();
/* 683 */       if ((fact.getPredicate().getName().indexOf("_inheritedmethod") <= 0) || (!((LSDBinding)fact.getBindings().get(2)).getGroundConst().equals(((LSDBinding)typeF.getBindings().get(0)).getGroundConst()))) break label133;
/* 684 */       if (this.filter.methodLevel) ontheflyDeltaKB.add(fact);
/* 685 */       if ((!this.filter.methodLevel) || (p == null)) break label133;
/*     */     }
/*     */   }
/*     */   
/*     */   private void printFieldOfType(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB, LSDFact fieldF) { LSDFact fact;
/*     */     label133:
/* 691 */     for (Iterator localIterator = this.originalDeltaKB.iterator(); localIterator.hasNext(); 
/*     */         
/*     */ 
/* 694 */         p.println("\t\t\t" + fact))
/*     */     {
/* 691 */       fact = (LSDFact)localIterator.next();
/* 692 */       if ((fact.getPredicate().getName().indexOf("_fieldoftype") <= 0) || (!((LSDBinding)fact.getBindings().get(0)).getGroundConst().equals(((LSDBinding)fieldF.getBindings().get(0)).getGroundConst()))) break label133;
/* 693 */       if (this.filter.fieldLevel) ontheflyDeltaKB.add(fact);
/* 694 */       if ((!this.filter.fieldLevel) || (p == null)) {
/*     */         break label133;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void topDownTraversal2(PrintStream p, TreeSet<LSDFact> ontheflyDeltaKB) {
/*     */     Iterator localIterator2;
/* 702 */     for (Iterator localIterator1 = this.packageLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 703 */         localIterator2.hasNext())
/*     */     {
/* 702 */       String kind = (String)localIterator1.next();
/* 703 */       localIterator2 = ((TreeSet)this.packageLevel.get(kind)).iterator(); continue;LSDFact packageF = (LSDFact)localIterator2.next();
/* 704 */       if ((this.filter.packageLevel) && (p != null)) p.println(packageF);
/* 705 */       if (this.filter.packageLevel) ontheflyDeltaKB.add(packageF);
/* 706 */       filterPerPackage(p, ontheflyDeltaKB, packageF);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/facts/LSdiffHierarchialDeltaKB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */