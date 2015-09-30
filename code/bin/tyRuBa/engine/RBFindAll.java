/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.CompiledFindAll;
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
/*     */ public class RBFindAll extends RBExpression
/*     */ {
/*     */   private RBExpression query;
/*     */   private RBTerm extract;
/*     */   private RBTerm result;
/*     */   
/*     */   public RBFindAll(RBExpression q, RBTerm e, RBTerm r)
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
/*  38 */   public RBTerm getResult() { return this.result; }
/*     */   
/*     */   public String toString()
/*     */   {
/*  42 */     return 
/*  43 */       "FINDALL(" + getQuery() + "," + getExtract() + "," + getResult() + ")";
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  47 */     return new CompiledFindAll(getQuery().compile(c), getExtract(), getResult());
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*     */     try {
/*  52 */       TypeEnv afterQueryEnv = getQuery().typecheck(predinfo, startEnv);
/*  53 */       Type extractType = getExtract().getType(afterQueryEnv);
/*  54 */       Type inferredResultType = Factory.makeListType(extractType);
/*  55 */       TypeEnv resultTypeEnv = Factory.makeTypeEnv();
/*  56 */       Type resultType = getResult().getType(resultTypeEnv);
/*  57 */       resultType.checkEqualTypes(inferredResultType);
/*     */       
/*  59 */       return afterQueryEnv.intersect(resultTypeEnv);
/*     */     } catch (TypeModeError e) {
/*  61 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError {
/*  66 */     Collection freevars = this.query.getFreeVariables(context);
/*  67 */     Collection extractedVars = getExtract().getVariables();
/*  68 */     freevars.removeAll(extractedVars);
/*     */     
/*  70 */     if (!freevars.isEmpty()) {
/*  71 */       return Factory.makeModedExpression(
/*  72 */         this, 
/*  73 */         new ErrorMode("Variables improperly left unbound in FINDALL: " + 
/*  74 */         freevars), 
/*  75 */         context);
/*     */     }
/*  77 */     RBExpression convQuery = this.query.convertToMode(context, rearrange);
/*  78 */     Mode convertedMode = convQuery.getMode();
/*  79 */     if ((convertedMode instanceof ErrorMode)) {
/*  80 */       return Factory.makeModedExpression(this, convQuery.getMode(), 
/*  81 */         convQuery.getNewContext());
/*     */     }
/*  83 */     ModeCheckContext newContext = (ModeCheckContext)context.clone();
/*  84 */     this.result.makeAllBound(newContext);
/*  85 */     return Factory.makeModedExpression(
/*  86 */       new RBFindAll(convQuery, getExtract(), this.result), 
/*  87 */       convertedMode.findAll(), newContext);
/*     */   }
/*     */   
/*     */ 
/*     */   public RBExpression convertToNormalForm(boolean negate)
/*     */   {
/*  93 */     RBExpression result = 
/*  94 */       new RBFindAll(getQuery().convertToNormalForm(false), 
/*  95 */       getExtract(), getResult());
/*  96 */     if (negate) {
/*  97 */       return new RBNotFilter(result);
/*     */     }
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 104 */     return v.visit(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBFindAll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */