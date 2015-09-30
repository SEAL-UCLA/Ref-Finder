/*     */ package tyRuBa.engine.compilation;
/*     */ 
/*     */ import tyRuBa.engine.CachedRuleBase;
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.RBContext;
/*     */ import tyRuBa.engine.SemiDetCachedRuleBase;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.util.ElementSource;
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
/*     */ public abstract class Compiled
/*     */ {
/*     */   private final Mode mode;
/*     */   
/*     */   public Compiled(Mode mode)
/*     */   {
/*  29 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*  33 */     return this.mode;
/*     */   }
/*     */   
/*  36 */   public static Compiled succeed = new Compiled.1(Mode.makeDet());
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
/*  58 */   public static Compiled fail = new Compiled.2(Mode.makeFail());
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
/*     */   public ElementSource run(ElementSource inputs, RBContext context)
/*     */   {
/*  84 */     return 
/*     */     
/*     */ 
/*     */ 
/*  88 */       inputs.map(new Compiled.3(this, context)).flatten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ElementSource runNonDet(Object paramObject, RBContext paramRBContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSource start(Frame putMap)
/*     */   {
/* 102 */     return runNonDet(putMap, new RBContext());
/*     */   }
/*     */   
/*     */   public Compiled conjoin(Compiled other)
/*     */   {
/* 107 */     if ((other instanceof SemiDetCompiled)) {
/* 108 */       return new CompiledConjunction_Nondet_Semidet(
/* 109 */         this, (SemiDetCompiled)other);
/*     */     }
/* 111 */     return new CompiledConjunction(this, other);
/*     */   }
/*     */   
/*     */ 
/*     */   public Compiled disjoin(Compiled other)
/*     */   {
/* 117 */     if (other.equals(fail))
/* 118 */       return this;
/* 119 */     if ((other instanceof SemiDetCompiled)) {
/* 120 */       return other.disjoin(this);
/*     */     }
/* 122 */     return new CompiledDisjunction(this, other);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SemiDetCompiled first()
/*     */   {
/* 132 */     return new CompiledFirst(this);
/*     */   }
/*     */   
/*     */   public Compiled negate()
/*     */   {
/* 137 */     return new CompiledNot(this);
/*     */   }
/*     */   
/*     */   public Compiled test() {
/* 141 */     return new CompiledTest(this);
/*     */   }
/*     */   
/*     */   public static Compiled makeCachedRuleBase(Compiled compiledRules) {
/* 145 */     if (compiledRules.getMode().hi.compareTo(Multiplicity.one) <= 0)
/*     */     {
/* 147 */       return new SemiDetCachedRuleBase((SemiDetCompiled)compiledRules);
/*     */     }
/*     */     
/* 150 */     return new CachedRuleBase(compiledRules);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/Compiled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */