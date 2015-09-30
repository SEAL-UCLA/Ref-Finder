/*     */ package tyRuBa.engine.factbase.hashtable;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import junit.framework.Assert;
/*     */ import tyRuBa.engine.PredicateIdentifier;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBComponent;
/*     */ import tyRuBa.engine.Validator;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.factbase.FactBase;
/*     */ import tyRuBa.engine.factbase.NamePersistenceManager;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.pager.FileLocation;
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
/*     */ public class HashTableFactBase
/*     */   extends FactBase
/*     */ {
/*     */   private Map indexes;
/*     */   private int arity;
/*     */   private String name;
/*     */   private Index allFreeIndex;
/*     */   private QueryEngine engine;
/*  63 */   private boolean isEmpty = true;
/*     */   
/*     */   private String storageLocation;
/*     */   
/*     */ 
/*     */   public HashTableFactBase(PredInfo info)
/*     */   {
/*  70 */     this.arity = info.getArity();
/*  71 */     this.name = info.getPredId().getName();
/*  72 */     this.engine = info.getQueryEngine();
/*  73 */     this.storageLocation = (this.engine.getStoragePath() + "/" + this.engine.getFrontendNamePersistenceManager().getPersistentName(this.name) + "/" + this.arity + "/");
/*  74 */     initIndexes(info);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initIndexes(PredInfo info)
/*     */   {
/*  84 */     this.indexes = new HashMap();
/*     */     
/*     */ 
/*  87 */     BindingList allFree = Factory.makeBindingList(this.arity, Factory.makeFree());
/*  88 */     PredicateMode freeMode = new PredicateMode(allFree, new Mode(Multiplicity.zero, Multiplicity.many), false);
/*  89 */     this.allFreeIndex = new Index(freeMode, new FileLocation(this.storageLocation + "/" + 
/*  90 */       freeMode.getParamModes().getBFString() + "/"), this.engine, this.name + "/" + this.arity);
/*  91 */     this.indexes.put(freeMode.getParamModes(), this.allFreeIndex);
/*     */     
/*     */ 
/*     */ 
/*  95 */     BindingList allBound = Factory.makeBindingList(this.arity, Factory.makeBound());
/*  96 */     PredicateMode boundMode = new PredicateMode(allBound, new Mode(Multiplicity.zero, Multiplicity.one), false);
/*  97 */     this.indexes.put(boundMode.getParamModes(), this.allFreeIndex);
/*     */     
/*     */ 
/* 100 */     for (int i = 0; i < info.getNumPredicateMode(); i++) {
/* 101 */       PredicateMode pm = info.getPredicateModeAt(i);
/* 102 */       BindingList paramModes = pm.getParamModes();
/* 103 */       if (new File(this.storageLocation + "/" + paramModes.getBFString()).exists()) {
/* 104 */         this.isEmpty = false;
/*     */       }
/* 106 */       if ((paramModes.getNumFree() != this.arity) && (paramModes.getNumBound() != this.arity)) {
/* 107 */         this.indexes.put(pm.getParamModes(), new Index(pm, new FileLocation(this.storageLocation + "/" + 
/* 108 */           pm.getParamModes().getBFString() + "/"), this.engine, this.name + "/" + this.arity));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 113 */     int numIndexes = (int)Math.pow(2.0D, this.arity);
/* 114 */     for (int i = 0; i < numIndexes; i++) {
/* 115 */       BindingList blist = Factory.makeBindingList();
/* 116 */       int checkNum = 1;
/* 117 */       for (int j = 0; j < this.arity; j++) {
/* 118 */         if ((i & checkNum) == 0) {
/* 119 */           blist.add(Factory.makeBound());
/*     */         } else {
/* 121 */           blist.add(Factory.makeFree());
/*     */         }
/* 123 */         checkNum *= 2;
/*     */       }
/*     */       
/* 126 */       if ((blist.getNumBound() != 0) && (blist.getNumFree() != 0) && (!this.indexes.containsKey(blist)) && 
/* 127 */         (new File(this.storageLocation + "/" + blist.getBFString()).exists())) {
/* 128 */         this.isEmpty = false;
/* 129 */         PredicateMode mode = new PredicateMode(blist, new Mode(Multiplicity.zero, Multiplicity.many), false);
/* 130 */         Index idx = new Index(mode, new FileLocation(this.storageLocation + "/" + 
/* 131 */           mode.getParamModes().getBFString() + "/"), this.engine, this.name + "/" + this.arity);
/* 132 */         this.indexes.put(mode.getParamModes(), idx);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Index getIndex(PredicateMode mode)
/*     */   {
/* 144 */     Index index = (Index)this.indexes.get(mode.getParamModes());
/* 145 */     if (index == null) {
/* 146 */       index = makeIndex(mode);
/* 147 */       this.indexes.put(mode.getParamModes(), index);
/*     */     }
/*     */     
/* 150 */     return index;
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
/*     */   private Index makeIndex(PredicateMode mode)
/*     */   {
/* 166 */     Index index = new Index(mode, new FileLocation(this.storageLocation + "/" + mode.getParamModes().getBFString() + "/"), 
/* 167 */       this.engine, this.name + "/" + this.arity);
/* 168 */     for (ElementSource iter = this.allFreeIndex.values(); iter.hasMoreElements();) {
/* 169 */       IndexValue fact = (IndexValue)iter.nextElement();
/* 170 */       index.addFact(fact);
/*     */     }
/* 172 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 179 */     return this.isEmpty;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isPersistent()
/*     */   {
/* 186 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void insert(RBComponent f)
/*     */   {
/* 193 */     Assert.assertTrue("Only ground facts should be insterted in to FactBases", f.isGroundFact());
/* 194 */     this.isEmpty = false;
/*     */     
/* 196 */     Validator v = f.getValidator();
/*     */     
/* 198 */     for (Iterator iter = this.indexes.entrySet().iterator(); iter.hasNext();) {
/* 199 */       Map.Entry entry = (Map.Entry)iter.next();
/* 200 */       BindingList key = (BindingList)entry.getKey();
/* 201 */       if (key.getNumFree() != 0) {
/* 202 */         Index index = (Index)entry.getValue();
/* 203 */         index.addFact(IndexValue.make(v, f.getArgs()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Compiled basicCompile(PredicateMode mode, CompilationContext context)
/*     */   {
/* 213 */     Index index = getIndex(mode);
/*     */     
/* 215 */     if (mode.getMode().hi.compareTo(Multiplicity.one) <= 0) {
/* 216 */       if (mode.getParamModes().getNumFree() != 0)
/*     */       {
/* 218 */         return new HashTableFactBase.1(this, mode.getMode(), index);
/*     */       }
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
/* 236 */       return new HashTableFactBase.2(this, mode.getMode(), index);
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
/*     */ 
/*     */ 
/*     */ 
/* 250 */     if (mode.getParamModes().getNumFree() != 0)
/*     */     {
/* 252 */       return new HashTableFactBase.3(this, mode.getMode(), index);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */     throw new Error("This case should not happen");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void backup()
/*     */   {
/* 282 */     for (Iterator iter = this.indexes.entrySet().iterator(); iter.hasNext();) {
/* 283 */       Map.Entry entry = (Map.Entry)iter.next();
/* 284 */       BindingList key = (BindingList)entry.getKey();
/* 285 */       if (key.getNumFree() != 0) {
/* 286 */         Index idx = (Index)entry.getValue();
/* 287 */         idx.backup();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/hashtable/HashTableFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */