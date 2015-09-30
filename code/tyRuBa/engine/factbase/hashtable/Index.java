/*     */ package tyRuBa.engine.factbase.hashtable;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ import tyRuBa.util.Action;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.pager.Location;
/*     */ import tyRuBa.util.pager.Pager;
/*     */ import tyRuBa.util.pager.Pager.Resource;
/*     */ import tyRuBa.util.pager.Pager.ResourceId;
/*     */ import tyRuBa.util.pager.Pager.Task;
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
/*     */   static class HashMapResource extends HashMap implements Pager.Resource
/*     */   {
/*  41 */     private long myLastCleanTime = System.currentTimeMillis();
/*     */     
/*     */     public boolean isClean(ValidatorManager vm) {
/*  44 */       long lastDirty = vm.getLastInvalidatedTime();
/*  45 */       return this.myLastCleanTime > lastDirty;
/*     */     }
/*     */     
/*     */     public void clean(ValidatorManager vm) {
/*  49 */       this.myLastCleanTime = System.currentTimeMillis();
/*  50 */       for (Iterator iter = entrySet().iterator(); iter.hasNext();) {
/*  51 */         Map.Entry entry = (Map.Entry)iter.next();
/*  52 */         Object whatIsThere = entry.getValue();
/*  53 */         if ((whatIsThere instanceof ArrayList)) {
/*  54 */           ArrayList lstWhatIsThere = (ArrayList)whatIsThere;
/*  55 */           for (Iterator iterator = lstWhatIsThere.iterator(); iterator.hasNext();) {
/*  56 */             IndexValue element = (IndexValue)iterator.next();
/*  57 */             if (!element.isValid(vm)) {
/*  58 */               iterator.remove();
/*     */             }
/*     */           }
/*     */           
/*  62 */           int size = lstWhatIsThere.size();
/*  63 */           if (size == 0) {
/*  64 */             iter.remove();
/*  65 */           } else if (size == 1) {
/*  66 */             entry.setValue(lstWhatIsThere.get(0));
/*     */           }
/*     */         }
/*     */         else {
/*  70 */           IndexValue idxWhatIsThere = (IndexValue)whatIsThere;
/*  71 */           if (!idxWhatIsThere.isValid(vm)) {
/*  72 */             iter.remove();
/*     */           }
/*     */         }
/*     */       }
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
/* 210 */     final Object key = whole_key.getSecond();
/* 211 */     final String topLevelKey = whole_key.getFirst();
/* 212 */     final IndexValue value = IndexValue.make(fact.getValidatorHandle(), free);
/*     */     
/* 214 */     getPager().asynchDoTask(getResourceFromKey(whole_key), new Pager.Task(true)
/*     */     {
/*     */       public Object doIt(Pager.Resource map_rsrc) {
/* 217 */         Index.HashMapResource map = (Index.HashMapResource)map_rsrc;
/*     */         
/* 219 */         if ((map != null) && (!map.isClean(Index.this.validatorManager))) {
/* 220 */           map.clean(Index.this.validatorManager);
/*     */         }
/*     */         
/* 223 */         if (map == null) {
/* 224 */           map = new Index.HashMapResource();
/*     */         }
/*     */         
/* 227 */         Object whatIsThere = map.get(key);
/* 228 */         if (whatIsThere == null) {
/* 229 */           map.put(key, value);
/* 230 */         } else if ((whatIsThere instanceof ArrayList)) {
/* 231 */           ArrayList lstWhatIsThere = (ArrayList)whatIsThere;
/* 232 */           if (Index.this.checkDet) {
/* 233 */             for (Iterator iter = lstWhatIsThere.iterator(); iter.hasNext();) {
/* 234 */               IndexValue element = (IndexValue)iter.next();
/* 235 */               if (!element.getParts().equals(value.getParts())) {
/* 236 */                 throw new Error(
/* 237 */                   "OOPS!! More than one fact has been inserted into a Det/SemiDet predicate (" + Index.this.predicateName + ") present = " + 
/* 238 */                   element.getParts() + " ||| new = " + value.getParts() + key);
/*     */               }
/*     */             }
/*     */           }
/* 242 */           lstWhatIsThere.add(value);
/*     */         } else {
/* 244 */           IndexValue idxWhatIsThere = (IndexValue)whatIsThere;
/* 245 */           if ((Index.this.checkDet) && 
/* 246 */             (!idxWhatIsThere.getParts().equals(value.getParts()))) {
/* 247 */             throw new Error(
/* 248 */               "OOPS!! More than one fact has been inserted into a Det/SemiDet predicate (" + Index.this.predicateName + ") present = " + 
/* 249 */               idxWhatIsThere.getParts() + " ||| new = " + value.getParts() + key);
/*     */           }
/*     */           
/* 252 */           ArrayList lstWhatIsThere = new ArrayList(2);
/* 253 */           lstWhatIsThere.add(whatIsThere);
/* 254 */           lstWhatIsThere.add(value);
/* 255 */           map.put(key, lstWhatIsThere);
/*     */         }
/*     */         
/* 258 */         changedResource(map);
/* 259 */         return null;
/*     */       }
/*     */       
/* 262 */     });
/* 263 */     getPager().asynchDoTask(this.storageLocation.getResourceID("keys.data"), new Pager.Task(true)
/*     */     {
/*     */       public Object doIt(Pager.Resource rsrc) {
/* 266 */         Index.HashSetResource toplevelKeys = (Index.HashSetResource)rsrc;
/* 267 */         if (toplevelKeys == null)
/* 268 */           toplevelKeys = new Index.HashSetResource();
/* 269 */         if (toplevelKeys.add(topLevelKey))
/* 270 */           changedResource(toplevelKeys);
/* 271 */         return null;
/*     */       }
/*     */     });
/*     */   }
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
/* 291 */     final Object key = inputPars.getSecond();
/*     */     
/* 293 */     (ElementSource)getPager().synchDoTask(getResourceFromKey(inputPars), new Pager.Task(false)
/*     */     {
/*     */       public Object doIt(Pager.Resource rsrc) {
/* 296 */         Index.HashMapResource map_resource = (Index.HashMapResource)rsrc;
/* 297 */         if (map_resource == null) {
/* 298 */           return ElementSource.theEmpty;
/*     */         }
/* 300 */         return Index.this.convertIndexValuesToRBTuples(Index.this.removeInvalids(map_resource.get(key)));
/*     */       }
/*     */     });
/*     */   }
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
/* 314 */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */       getTopLevelKeys().map(new Action()
/*     */       {
/*     */         public Object compute(Object arg)
/*     */         {
/* 317 */           String topkey = (String)arg;
/* 318 */           return Index.this.getTopKeyValues(topkey);
/*     */         }
/*     */       }).flatten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElementSource getTopLevelKeys()
/*     */   {
/* 329 */     HashSetResource topLevelKeys = (HashSetResource)getPager().synchDoTask(
/* 330 */       this.storageLocation.getResourceID("keys.data"), new Pager.Task(false) {
/*     */         public Object doIt(Pager.Resource rsrc) {
/* 332 */           Index.HashSetResource toplevelKeys = (Index.HashSetResource)rsrc;
/* 333 */           return toplevelKeys;
/*     */         }
/* 335 */       });
/* 336 */     return ElementSource.with(topLevelKeys.iterator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElementSource getTopKeyValues(String topkey)
/*     */   {
/* 344 */     ElementSource valid_values = (ElementSource)getPager().synchDoTask(
/* 345 */       this.storageLocation.getResourceID(this.nameManager.getPersistentName(topkey)), new Pager.Task(false) {
/*     */         public Object doIt(Pager.Resource rsrc) {
/* 347 */           Index.HashMapResource map = (Index.HashMapResource)rsrc;
/* 348 */           ElementSource.with(map.values().iterator()).map(new Action() {
/*     */             public Object compute(Object arg) {
/* 350 */               return Index.this.removeInvalids(arg);
/*     */             }
/*     */           })
/*     */           
/*     */ 
/*     */ 
/* 352 */             .flatten();
/*     */         }
/* 354 */       });
/* 355 */     return valid_values;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSource convertIndexValuesToRBTuples(ElementSource source)
/*     */   {
/* 363 */     source.map(new Action() {
/*     */       public Object compute(Object arg) {
/* 365 */         return ((IndexValue)arg).getParts();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSource removeInvalids(Object values)
/*     */   {
/* 375 */     if (values == null)
/* 376 */       return ElementSource.theEmpty;
/* 377 */     if ((values instanceof ArrayList)) {
/* 378 */       ElementSource.with((ArrayList)values).map(new Action() {
/*     */         public Object compute(Object arg) {
/* 380 */           if (((IndexValue)arg).isValid(Index.this.validatorManager)) {
/* 381 */             return arg;
/*     */           }
/* 383 */           return null;
/*     */         }
/*     */       });
/*     */     }
/* 387 */     if (((IndexValue)values).isValid(this.validatorManager)) {
/* 388 */       return ElementSource.singleton(values);
/*     */     }
/* 390 */     return ElementSource.theEmpty;
/*     */   }
/*     */   
/*     */   public void backup() {}
/*     */   
/*     */   static class HashSetResource
/*     */     extends HashSet
/*     */     implements Pager.Resource
/*     */   {}
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/hashtable/Index.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */