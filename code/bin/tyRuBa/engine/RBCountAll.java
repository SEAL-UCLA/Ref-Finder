/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.CompiledCount;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ public class RBCountAll extends RBExpression
/*     */ {
/*     */   private RBExpression query;
/*     */   private RBTerm extract;
/*     */   private RBTerm result;
/*     */   
/*     */   public RBCountAll(RBExpression q, RBTerm e, RBTerm r)
/*     */   {
/*  25 */     this.query = q;
/*  26 */     this.extract = e;
/*  27 */     this.result = r;
/*     */   }
/*     */   
/*     */   public RBExpression getQuery() {
/*  31 */     return this.query;
/*     */   }
/*     */   
/*     */   public RBTerm getExtract() {
/*  35 */     return this.extract;
/*     */   }
/*     */   
/*     */   public RBTerm getResult() {
/*  39 */     return this.result;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  43 */     return "COUNTALL(" + getQuery() + "," + getExtract() + "," + getResult() + ")";
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  47 */     return new CompiledCount(getQuery().compile(c), getExtract(), getResult());
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*     */     try {
/*  52 */       TypeEnv afterQueryEnv = getQuery().typecheck(predinfo, startEnv);
/*  53 */       TypeEnv resultTypeEnv = Factory.makeTypeEnv();
/*  54 */       Type resultType = getResult().getType(resultTypeEnv);
/*  55 */       resultType.checkEqualTypes(Factory.makeAtomicType(
/*  56 */         Factory.makeTypeConstructor(Integer.class)));
/*     */       
/*  58 */       return afterQueryEnv.intersect(resultTypeEnv);
/*     */     } catch (TypeModeError e) {
/*  60 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError {
/*  65 */     Collection freevars = this.query.getFreeVariables(context);
/*  66 */     Collection extractedVars = getExtract().getVariables();
/*  67 */     freevars.removeAll(extractedVars);
/*     */     
/*  69 */     if (!freevars.isEmpty()) {
/*  70 */       return Factory.makeModedExpression(
/*  71 */         this, 
/*  72 */         new ErrorMode("Variables improperly left unbound in COUNT: " + 
/*  73 */         freevars), 
/*  74 */         context);
/*     */     }
/*  76 */     RBExpression convQuery = this.query.convertToMode(context, rearrange);
/*  77 */     Mode convertedMode = convQuery.getMode();
/*  78 */     if ((convertedMode instanceof ErrorMode)) {
/*  79 */       return Factory.makeModedExpression(this, convQuery.getMode(), 
/*  80 */         convQuery.getNewContext());
/*     */     }
/*  82 */     ModeCheckContext newContext = (ModeCheckContext)context.clone();
/*  83 */     this.result.makeAllBound(newContext);
/*  84 */     return Factory.makeModedExpression(
/*  85 */       new RBCountAll(convQuery, getExtract(), getResult()), 
/*  86 */       convertedMode.findAll(), newContext);
/*     */   }
/*     */   
/*     */ 
/*     */   public RBExpression convertToNormalForm(boolean negate)
/*     */   {
/*  92 */     RBExpression result = 
/*  93 */       new RBCountAll(getQuery().convertToNormalForm(false), 
/*  94 */       getExtract(), getResult());
/*  95 */     if (negate) {
/*  96 */       return new RBNotFilter(result);
/*     */     }
/*  98 */     return result;
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 103 */     return v.visit(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBCountAll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */