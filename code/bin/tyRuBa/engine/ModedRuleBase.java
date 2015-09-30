/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.factbase.FactBase;
/*     */ import tyRuBa.engine.factbase.FactLibraryManager;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ public class ModedRuleBase
/*     */   extends RuleBase
/*     */ {
/*  21 */   private RBComponentVector rules = null;
/*     */   private FactBase facts;
/*  23 */   private Mode currMode = null;
/*  24 */   private Vector[] currTypes = null;
/*     */   
/*     */   private PredicateIdentifier predId;
/*     */   
/*     */   private FactLibraryManager libraryManager;
/*     */   
/*     */ 
/*     */   public ModedRuleBase(QueryEngine engine, PredicateMode predMode, FactBase allTheFacts, FactLibraryManager libraryManager, PredicateIdentifier predId)
/*     */   {
/*  33 */     super(engine, predMode, allTheFacts.isPersistent());
/*  34 */     this.facts = allTheFacts;
/*  35 */     this.predId = predId;
/*  36 */     this.libraryManager = libraryManager;
/*  37 */     ensureRuleBase();
/*  38 */     this.currTypes = new Vector[predMode.getParamModes().getNumBound()];
/*  39 */     for (int i = 0; i < this.currTypes.length; i++) {
/*  40 */       this.currTypes[i] = new Vector();
/*     */     }
/*     */   }
/*     */   
/*     */   public void insert(RBComponent r, ModedRuleBaseIndex insertedFrom, TupleType inferredTypes) throws TypeModeError
/*     */   {
/*     */     try {
/*  47 */       PredicateMode thisRBMode = getPredMode();
/*  48 */       RBComponent converted = r.convertToMode(thisRBMode, 
/*  49 */         Factory.makeModeCheckContext(insertedFrom));
/*     */       
/*     */ 
/*     */ 
/*  53 */       if (getPredMode().toBeCheck()) {
/*  54 */         BindingList bindings = thisRBMode.getParamModes();
/*  55 */         TupleType boundTypes = Factory.makeTupleType();
/*     */         
/*     */ 
/*  58 */         for (int i = 0; i < bindings.size(); i++) {
/*  59 */           if (bindings.get(i).isBound()) {
/*  60 */             boundTypes.add(inferredTypes.get(i));
/*     */           }
/*     */         }
/*     */         
/*  64 */         if (this.currMode == null) {
/*  65 */           this.currMode = converted.getMode();
/*  66 */           for (int i = 0; i < this.currTypes.length; i++) {
/*  67 */             this.currTypes[i].add(boundTypes.get(i));
/*     */           }
/*  69 */         } else if (this.currTypes.length == 0)
/*     */         {
/*     */ 
/*     */ 
/*  73 */           this.currMode = this.currMode.add(converted.getMode());
/*     */         } else {
/*  75 */           boolean hasOverlap = true;
/*  76 */           for (int i = 0; i < this.currTypes.length; i++) {
/*  77 */             hasOverlap = 
/*  78 */               boundTypes.get(i).hasOverlapWith(this.currTypes[i], hasOverlap);
/*     */           }
/*  80 */           if ((hasOverlap) && (this.currTypes.length > 0)) {
/*  81 */             this.currMode = this.currMode.add(converted.getMode());
/*     */           } else {
/*  83 */             this.currMode = this.currMode.noOverlapWith(converted.getMode());
/*     */           }
/*     */         }
/*     */         
/*  87 */         if (this.currMode.compatibleWith(getMode())) {
/*  88 */           privateInsert(converted, insertedFrom);
/*     */         } else {
/*  90 */           throw new TypeModeError(
/*  91 */             "Inferred mode exceeds declared mode in " + 
/*  92 */             converted.getPredName() + "\n" + 
/*  93 */             "inferred mode: " + this.currMode + "\tdeclared mode: " + getMode());
/*     */         }
/*     */       } else {
/*  96 */         privateInsert(converted, insertedFrom);
/*     */       }
/*     */     } catch (TypeModeError e) {
/*  99 */       throw new TypeModeError("while converting " + r + " to mode: " + 
/* 100 */         getPredMode() + "\n" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void privateInsert(RBComponent converted, ModedRuleBaseIndex insertedFrom)
/*     */     throws TypeModeError
/*     */   {
/* 109 */     ensureRuleBase();
/* 110 */     this.rules.insert(converted);
/*     */   }
/*     */   
/*     */   private void ensureRuleBase() {
/* 114 */     if (this.rules == null) {
/* 115 */       this.rules = new RBComponentVector();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 120 */     return 
/*     */     
/*     */ 
/*     */ 
/* 124 */       "/******** BEGIN ModedRuleBase ***********************/\nPredicate mode: " + getPredMode() + "\n" + "Inferred mode: " + this.currMode + "\n" + this.rules + "\n" + "/******** END ModedRuleBase *************************/";
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 128 */     throw new Error("That's strange... who wants to know my hashcode??");
/*     */   }
/*     */   
/*     */   protected Compiled compile(CompilationContext context) {
/* 132 */     if (this.rules != null) {
/* 133 */       if (isPersistent()) {
/* 134 */         return this.facts.compile(getPredMode(), context).disjoin(this.libraryManager.compile(getPredMode(), this.predId, context)).disjoin(this.rules.compile(context));
/*     */       }
/* 136 */       return this.facts.compile(getPredMode(), context).disjoin(this.rules.compile(context));
/*     */     }
/*     */     
/*     */ 
/* 140 */     if (isPersistent()) {
/* 141 */       return this.facts.compile(getPredMode(), context).disjoin(this.libraryManager.compile(getPredMode(), this.predId, context));
/*     */     }
/* 143 */     return this.facts.compile(getPredMode(), context);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/ModedRuleBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */