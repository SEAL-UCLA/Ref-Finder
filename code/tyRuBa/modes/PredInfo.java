/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import tyRuBa.engine.PredicateIdentifier;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.factbase.FactBase;
/*     */ import tyRuBa.engine.factbase.SimpleArrayListFactBase;
/*     */ import tyRuBa.engine.factbase.hashtable.HashTableFactBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredInfo
/*     */ {
/*     */   private QueryEngine engine;
/*     */   private PredicateIdentifier predId;
/*     */   private TupleType tList;
/*     */   private ArrayList predModes;
/*     */   private FactBase factbase;
/*     */   private boolean isPersistent;
/*     */   
/*     */   public PredInfo(QueryEngine qe, String predName, TupleType tList, ArrayList predModes, boolean isPersistent)
/*     */   {
/*  34 */     this.engine = qe;
/*  35 */     this.predId = new PredicateIdentifier(predName, tList.size());
/*  36 */     this.tList = tList;
/*  37 */     this.predModes = predModes;
/*  38 */     this.isPersistent = isPersistent;
/*     */   }
/*     */   
/*     */   public PredInfo(QueryEngine qe, String predName, TupleType tList, ArrayList predModes) {
/*  42 */     this(qe, predName, tList, predModes, false);
/*     */   }
/*     */   
/*     */   public PredInfo(QueryEngine qe, String predName, TupleType tList) {
/*  46 */     this(qe, predName, tList, new ArrayList());
/*     */   }
/*     */   
/*     */   public void addPredicateMode(PredicateMode pm) {
/*  50 */     this.predModes.add(pm);
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId() {
/*  54 */     return this.predId;
/*     */   }
/*     */   
/*     */   public TupleType getTypeList() {
/*  58 */     return (TupleType)this.tList.clone(new HashMap());
/*     */   }
/*     */   
/*     */   public int getNumPredicateMode() {
/*  62 */     return this.predModes.size();
/*     */   }
/*     */   
/*     */   public PredicateMode getPredicateModeAt(int pos) {
/*  66 */     return (PredicateMode)this.predModes.get(pos);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  70 */     return this.predId.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  74 */     StringBuffer result = new StringBuffer(
/*  75 */       this.predId.toString() + this.tList + "\nMODES\n");
/*     */     
/*  77 */     int size = this.predModes.size();
/*  78 */     for (int i = 0; i < size; i++) {
/*  79 */       result.append(this.predModes.get(i) + "\n");
/*     */     }
/*  81 */     return result.toString();
/*     */   }
/*     */   
/*     */   public FactBase getFactBase() {
/*  85 */     if ((this.factbase == null) && ((this.predId.getArity() == 0) || (this.engine == null)))
/*     */     {
/*  87 */       this.factbase = new SimpleArrayListFactBase(this);
/*  88 */     } else if (this.factbase == null) {
/*  89 */       if (this.isPersistent) {
/*  90 */         this.factbase = new HashTableFactBase(this);
/*     */       } else {
/*  92 */         this.factbase = new SimpleArrayListFactBase(this);
/*     */       }
/*     */     }
/*  95 */     return this.factbase;
/*     */   }
/*     */   
/*     */   public QueryEngine getQueryEngine() {
/*  99 */     return this.engine;
/*     */   }
/*     */   
/*     */   public boolean isPersistent() {
/* 103 */     return this.isPersistent;
/*     */   }
/*     */   
/*     */   public int getArity() {
/* 107 */     return getPredId().getArity();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/PredInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */