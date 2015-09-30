/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import tyRuBa.engine.factbase.FactBase;
/*     */ import tyRuBa.engine.factbase.FactLibraryManager;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModedRuleBaseCollection
/*     */ {
/*     */   private QueryEngine engine;
/*  25 */   ArrayList modedRBs = new ArrayList();
/*     */   
/*  27 */   FactBase facts = null;
/*     */   
/*     */ 
/*     */   private PredicateIdentifier predId;
/*     */   
/*     */ 
/*     */   private FactLibraryManager factLibraryManager;
/*     */   
/*     */ 
/*  36 */   ArrayList unconvertedRules = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModedRuleBaseCollection(QueryEngine qe, PredInfo p, String identifier)
/*     */   {
/*  62 */     this.engine = qe;
/*  63 */     this.facts = p.getFactBase();
/*  64 */     this.predId = p.getPredId();
/*  65 */     this.factLibraryManager = this.engine.frontend().getFactLibraryManager();
/*     */     
/*  67 */     for (int i = 0; i < p.getNumPredicateMode(); i++) {
/*  68 */       PredicateMode pm = p.getPredicateModeAt(i);
/*  69 */       this.modedRBs.add(makeEmptyModedRuleBase(pm, this.facts));
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
/*     */   private RuleBase newModedRuleBase(PredicateMode pm, FactBase facts)
/*     */   {
/*  82 */     ModedRuleBase result = makeEmptyModedRuleBase(pm, facts);
/*  83 */     this.modedRBs.add(result);
/*     */     
/*  85 */     for (Iterator iter = this.unconvertedRules.iterator(); iter.hasNext();) {
/*  86 */       ModedRuleBaseCollection.InsertionInfo insertion = (ModedRuleBaseCollection.InsertionInfo)iter.next();
/*  87 */       if (!insertion.isValid()) {
/*  88 */         iter.remove();
/*     */       } else {
/*     */         try {
/*  91 */           result.insert(
/*  92 */             insertion.rule, insertion.conversionContext, insertion.resultTypes);
/*     */         } catch (TypeModeError e) {
/*  94 */           e.printStackTrace();
/*  95 */           throw new Error("Cannot happen because all the rules have already been inserted before");
/*     */         }
/*     */       }
/*     */     }
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   private ModedRuleBase makeEmptyModedRuleBase(PredicateMode pm, FactBase facts) {
/* 103 */     ModedRuleBase result = new ModedRuleBase(this.engine, pm, facts, this.factLibraryManager, this.predId);
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   public int HashCode() {
/* 108 */     return this.modedRBs.hashCode() * 17 + 4986;
/*     */   }
/*     */   
/*     */   public void insertInEach(RBComponent r, ModedRuleBaseIndex rulebases, TupleType resultTypes) throws TypeModeError
/*     */   {
/* 113 */     if (r.isGroundFact()) {
/* 114 */       this.facts.insert(r);
/*     */     }
/* 116 */     else if (!this.facts.isPersistent()) {
/* 117 */       this.unconvertedRules.add(new ModedRuleBaseCollection.InsertionInfo(this, r, rulebases, resultTypes));
/* 118 */       int size = this.modedRBs.size();
/* 119 */       for (int i = 0; i < size; i++) {
/* 120 */         ((RuleBase)this.modedRBs.get(i)).insert(r, rulebases, resultTypes);
/*     */       }
/*     */     } else {
/* 123 */       throw new Error("Rules cannot be added to persistent factbases");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public RuleBase getBest(BindingList bindings)
/*     */   {
/* 131 */     RuleBase result = null;
/* 132 */     for (int i = 0; i < this.modedRBs.size(); i++) {
/* 133 */       RuleBase currRulebase = (RuleBase)this.modedRBs.get(i);
/* 134 */       BindingList currBindings = currRulebase.getParamModes();
/* 135 */       if (currBindings.equals(bindings))
/*     */       {
/* 137 */         return currRulebase; }
/* 138 */       if (bindings.satisfyBinding(currBindings)) {
/* 139 */         if (result == null) {
/* 140 */           result = currRulebase;
/* 141 */         } else if (currRulebase.isBetterThan(result)) {
/* 142 */           result = currRulebase;
/*     */         }
/*     */       }
/*     */     }
/* 146 */     if ((result == null) || (result.getParamModes().equals(bindings))) {
/* 147 */       return result;
/*     */     }
/* 149 */     if (bindings.hasFree())
/*     */     {
/* 151 */       result = newModedRuleBase(
/* 152 */         Factory.makePredicateMode(bindings, result.getMode().moreBound(), false), 
/* 153 */         this.facts);
/* 154 */       return result;
/*     */     }
/*     */     
/* 157 */     result = newModedRuleBase(
/* 158 */       Factory.makePredicateMode(bindings, Mode.makeSemidet(), false), 
/* 159 */       this.facts);
/* 160 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public void dumpFacts(PrintStream out)
/*     */   {
/* 166 */     out.print(this.facts);
/*     */   }
/*     */   
/*     */   public void backup() {
/* 170 */     this.facts.backup();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/ModedRuleBaseCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */