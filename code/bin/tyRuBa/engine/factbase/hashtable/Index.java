/*     */ package tyRuBa.engine.factbase.hashtable;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBTuple;
/*     */ import tyRuBa.engine.factbase.NamePersistenceManager;
/*     */ import tyRuBa.engine.factbase.ValidatorManager;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.pager.Location;
/*     */ import tyRuBa.util.pager.Pager;
/*     */ import tyRuBa.util.pager.Pager.ResourceId;
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
/*     */ public final class Index
/*     */ {
/*     */   private final int[] freePlaces;
/*     */   private final int[] boundPlaces;
/*     */   private String predicateName;
/*     */   private boolean checkDet;
/*     */   private QueryEngine engine;
/*     */   private ValidatorManager validatorManager;
/*     */   private NamePersistenceManager nameManager;
/*     */   private Location storageLocation;
/*     */   
/*     */   public String toString()
/*     */   {
/* 110 */     String result = "Index(" + this.predicateName + " ";
/* 111 */     int arity = this.boundPlaces.length + this.freePlaces.length;
/* 112 */     char[] boundMap = new char[arity];
/* 113 */     for (int i = 0; i < boundMap.length; i++) {
/* 114 */       boundMap[i] = 'F';
/*     */     }
/* 116 */     for (int i = 0; i < this.boundPlaces.length; i++) {
/* 117 */       boundMap[this.boundPlaces[i]] = 'B';
/*     */     }
/* 119 */     return result + new String(boundMap) + ")";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Index(PredicateMode mode, Location storageLocation, QueryEngine engine, String predicateName)
/*     */   {
/* 130 */     this.validatorManager = engine.getFrontEndValidatorManager();
/* 131 */     this.engine = engine;
/* 132 */     this.storageLocation = storageLocation;
/* 133 */     this.predicateName = predicateName;
/* 134 */     this.nameManager = engine.getFrontendNamePersistenceManager();
/* 135 */     this.checkDet = ((mode.getMode().isDet()) || (mode.getMode().isSemiDet()));
/* 136 */     BindingList bl = mode.getParamModes();
/* 137 */     this.boundPlaces = new int[bl.getNumBound()];
/* 138 */     int boundPos = 0;
/* 139 */     this.freePlaces = new int[bl.getNumFree()];
/* 140 */     int freePos = 0;
/* 141 */     for (int i = 0; i < bl.size(); i++) {
/* 142 */       if (bl.get(i).isBound()) {
/* 143 */         this.boundPlaces[(boundPos++)] = i;
/*     */       } else {
/* 145 */         this.freePlaces[(freePos++)] = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Index(PredicateMode mode, Location storageLocation, QueryEngine engine, String predicateName, NamePersistenceManager nameManager, ValidatorManager validatorManager)
/*     */   {
/* 156 */     this(mode, storageLocation, engine, predicateName);
/* 157 */     this.nameManager = nameManager;
/* 158 */     this.validatorManager = validatorManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Pager getPager()
/*     */   {
/* 165 */     return this.engine.getFrontEndPager();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private RBTuple extract(int[] toExtract, RBTuple from)
/*     */   {
/* 174 */     RBTerm[] extracted = new RBTerm[toExtract.length];
/* 175 */     for (int i = 0; i < extracted.length; i++) {
/* 176 */       extracted[i] = from.getSubterm(toExtract[i]);
/*     */     }
/* 178 */     return FrontEnd.makeTuple(extracted);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RBTuple extractBound(RBTuple goal)
/*     */   {
/* 186 */     return extract(this.boundPlaces, goal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RBTuple extractFree(RBTuple goal)
/*     */   {
/* 194 */     return extract(this.freePlaces, goal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addFact(IndexValue fact)
/*     */   {
/* 202 */     RBTuple parts = fact.getParts();
/* 203 */     RBTuple whole_key = extractBound(parts);
/* 204 */     RBTuple free = extractFree(parts);
/*     */     
/*     */ 
/* 207 */     if (whole_key == RBTuple.theEmpty) {
/* 208 */       whole_key = free;
/*     */     }
/* 210 */     Object key = whole_key.getSecond();
/* 211 */     String topLevelKey = whole_key.getFirst();
/* 212 */     IndexValue value = IndexValue.make(fact.getValidatorHandle(), free);
/*     */     
/* 214 */     getPager().asynchDoTask(getResourceFromKey(whole_key), new Index.1(this, true, key, value));
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
/* 263 */     getPager().asynchDoTask(this.storageLocation.getResourceID("keys.data"), new Index.2(this, true, topLevelKey));
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
/*     */   private Pager.ResourceId getResourceFromKey(RBTuple whole_key)
/*     */   {
/* 280 */     return this.storageLocation.getResourceID(this.nameManager.getPersistentName(whole_key.getFirst()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ElementSource getMatchElementSource(RBTuple inputPars)
/*     */   {
/* 287 */     if (inputPars == RBTuple.theEmpty) {
/* 288 */       return convertIndexValuesToRBTuples(values());
/*     */     }
/*     */     
/* 291 */     Object key = inputPars.getSecond();
/*     */     
/* 293 */     return (ElementSource)getPager().synchDoTask(getResourceFromKey(inputPars), new Index.3(this, false, key));
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
/*     */   public RBTuple getMatchSingle(RBTuple inputPars)
/*     */   {
/* 309 */     return (RBTuple)getMatchElementSource(inputPars).firstElementOrNull();
/*     */   }
/*     */   
/*     */   public ElementSource values()
/*     */   {
/* 314 */     return 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */       getTopLevelKeys().map(new Index.4(this)).flatten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElementSource getTopLevelKeys()
/*     */   {
/* 329 */     Index.HashSetResource topLevelKeys = (Index.HashSetResource)getPager().synchDoTask(
/* 330 */       this.storageLocation.getResourceID("keys.data"), new Index.5(this, false));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 336 */     return ElementSource.with(topLevelKeys.iterator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElementSource getTopKeyValues(String topkey)
/*     */   {
/* 344 */     ElementSource valid_values = (ElementSource)getPager().synchDoTask(
/* 345 */       this.storageLocation.getResourceID(this.nameManager.getPersistentName(topkey)), new Index.6(this, false));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 355 */     return valid_values;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSource convertIndexValuesToRBTuples(ElementSource source)
/*     */   {
/* 363 */     return source.map(new Index.7(this));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSource removeInvalids(Object values)
/*     */   {
/* 375 */     if (values == null)
/* 376 */       return ElementSource.theEmpty;
/* 377 */     if ((values instanceof ArrayList)) {
/* 378 */       return ElementSource.with((ArrayList)values).map(new Index.8(this));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 387 */     if (((IndexValue)values).isValid(this.validatorManager)) {
/* 388 */       return ElementSource.singleton(values);
/*     */     }
/* 390 */     return ElementSource.theEmpty;
/*     */   }
/*     */   
/*     */   public void backup() {}
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/hashtable/Index.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */