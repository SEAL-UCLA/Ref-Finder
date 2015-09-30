/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBExistsQuantifier
/*     */   extends RBExpression
/*     */ {
/*     */   private RBExpression exp;
/*     */   private RBVariable[] vars;
/*     */   
/*     */   public RBExistsQuantifier(Collection variables, RBExpression exp)
/*     */   {
/*  26 */     this.exp = exp;
/*  27 */     this.vars = ((RBVariable[])variables.toArray(new RBVariable[variables.size()]));
/*     */   }
/*     */   
/*     */   RBExistsQuantifier(RBVariable[] vars, RBExpression exp) {
/*  31 */     this.exp = exp;
/*  32 */     this.vars = vars;
/*     */   }
/*     */   
/*     */   public RBExpression getExp() {
/*  36 */     return this.exp;
/*     */   }
/*     */   
/*     */   public int getNumVars() {
/*  40 */     return this.vars.length;
/*     */   }
/*     */   
/*     */   public RBVariable getVarAt(int pos) {
/*  44 */     return this.vars[pos];
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*     */     try {
/*  49 */       return getExp().typecheck(predinfo, startEnv);
/*     */     } catch (TypeModeError e) {
/*  51 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*     */   {
/*  57 */     ModeCheckContext resultContext = (ModeCheckContext)context.clone();
/*  58 */     Collection boundedVars = getVariables();
/*     */     
/*  60 */     for (int i = 0; i < getNumVars(); i++) {
/*  61 */       RBVariable currVar = getVarAt(i);
/*  62 */       if (!boundedVars.contains(currVar)) {
/*  63 */         return Factory.makeModedExpression(
/*  64 */           this, 
/*  65 */           new ErrorMode("Existentially quantified variable " + currVar + 
/*  66 */           " must become bound in " + getExp()), 
/*  67 */           context);
/*     */       }
/*     */     }
/*     */     
/*  71 */     RBExpression converted = getExp().convertToMode(context, rearrange);
/*  72 */     return Factory.makeModedExpression(
/*  73 */       new RBExistsQuantifier(this.vars, converted), 
/*  74 */       converted.getMode(), resultContext);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  78 */     StringBuffer result = new StringBuffer("(EXISTS ");
/*  79 */     for (int i = 0; i < this.vars.length; i++) {
/*  80 */       if (i > 0)
/*  81 */         result.append(",");
/*  82 */       result.append(this.vars[i].toString());
/*     */     }
/*  84 */     result.append(" : " + getExp() + ")");
/*  85 */     return result.toString();
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  89 */     return getExp().compile(c);
/*     */   }
/*     */   
/*     */   public RBExpression convertToNormalForm(boolean negate) {
/*  93 */     Frame varRenaming = new Frame();
/*  94 */     RBVariable[] newVars = new RBVariable[this.vars.length];
/*  95 */     for (int i = 0; i < this.vars.length; i++) {
/*  96 */       newVars[i] = ((RBVariable)this.vars[i].instantiate(varRenaming));
/*     */     }
/*  98 */     RBExpression convertedExp = this.exp.substitute(varRenaming).convertToNormalForm(false);
/*  99 */     return convertedExp.addExistsQuantifier(newVars, negate);
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 104 */     return v.visit(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBExistsQuantifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */