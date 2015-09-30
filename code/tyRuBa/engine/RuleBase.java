/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.Multiplicity;
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
/*     */ 
/*     */ public abstract class RuleBase
/*     */ {
/*     */   private QueryEngine engine;
/*  24 */   long upToDateWith = -1L;
/*     */   
/*     */   private boolean uptodateCheck() {
/*  27 */     if (this.upToDateWith < this.engine.frontend().updateCounter) {
/*  28 */       forceRecompilation();
/*  29 */       return false;
/*     */     }
/*  31 */     return true;
/*     */   }
/*     */   
/*     */   private void forceRecompilation()
/*     */   {
/*  36 */     this.compiledRules = null;
/*  37 */     this.semidetCompiledRules = null;
/*     */   }
/*     */   
/*  40 */   private Compiled compiledRules = null;
/*  41 */   private SemiDetCompiled semidetCompiledRules = null;
/*     */   
/*     */ 
/*  44 */   public static boolean useCache = true;
/*     */   
/*     */ 
/*     */ 
/*  48 */   public static boolean softCache = true;
/*     */   
/*     */ 
/*  51 */   public static boolean silent = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   public static boolean autoUpdate = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean debug_tracing = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final boolean debug_checking = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PredicateMode predMode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isPersistent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RuleBase(QueryEngine engine, PredicateMode predMode, boolean isSQLAble)
/*     */   {
/*  98 */     this.engine = engine;
/*  99 */     this.predMode = predMode;
/* 100 */     this.isPersistent = isSQLAble;
/*     */   }
/*     */   
/*     */   public PredicateMode getPredMode()
/*     */   {
/* 105 */     return this.predMode;
/*     */   }
/*     */   
/*     */   public BindingList getParamModes()
/*     */   {
/* 110 */     return getPredMode().getParamModes();
/*     */   }
/*     */   
/*     */   public Mode getMode()
/*     */   {
/* 115 */     return getPredMode().getMode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isBetterThan(RuleBase other)
/*     */   {
/* 122 */     return getMode().isBetterThan(other.getMode());
/*     */   }
/*     */   
/*     */   public static BasicModedRuleBaseIndex make(FrontEnd frontEnd) {
/* 126 */     return new BasicModedRuleBaseIndex(frontEnd, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicModedRuleBaseIndex make(FrontEnd frontEnd, String identifier, boolean temporary)
/*     */   {
/* 134 */     return new BasicModedRuleBaseIndex(frontEnd, identifier);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void insert(RBComponent paramRBComponent, ModedRuleBaseIndex paramModedRuleBaseIndex, TupleType paramTupleType)
/*     */     throws TypeModeError;
/*     */   
/*     */ 
/*     */   public void retract(RBFact f)
/*     */   {
/* 145 */     throw new Error("Unsupported operation RETRACT");
/*     */   }
/*     */   
/*     */   public RuleBase addCondition(RBExpression e)
/*     */   {
/* 150 */     throw new Error("Operation not implemented");
/*     */   }
/*     */   
/*     */   public void dumpFacts(PrintStream out) {}
/*     */   
/*     */   public Compiled getCompiled()
/*     */   {
/* 157 */     uptodateCheck();
/* 158 */     if (this.compiledRules == null) {
/* 159 */       this.compiledRules = compile(new CompilationContext());
/* 160 */       if (useCache)
/* 161 */         this.compiledRules = Compiled.makeCachedRuleBase(this.compiledRules);
/* 162 */       this.upToDateWith = this.engine.frontend().updateCounter;
/*     */     }
/* 164 */     return this.compiledRules;
/*     */   }
/*     */   
/*     */   public SemiDetCompiled getSemiDetCompiledRules() {
/* 168 */     uptodateCheck();
/* 169 */     if (this.semidetCompiledRules == null) {
/* 170 */       Compiled compiled = getCompiled();
/* 171 */       if (compiled.getMode().hi.compareTo(Multiplicity.one) > 0) {
/* 172 */         this.semidetCompiledRules = compiled.first();
/*     */       } else
/* 174 */         this.semidetCompiledRules = ((SemiDetCompiled)compiled);
/* 175 */       this.upToDateWith = this.engine.frontend().updateCounter;
/*     */     }
/* 177 */     return this.semidetCompiledRules;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract Compiled compile(CompilationContext paramCompilationContext);
/*     */   
/*     */ 
/*     */   public boolean isPersistent()
/*     */   {
/* 186 */     return this.isPersistent;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RuleBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */