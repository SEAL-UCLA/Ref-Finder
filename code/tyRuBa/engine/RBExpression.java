/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import junit.framework.Assert;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.visitor.CollectFreeVarsVisitor;
/*     */ import tyRuBa.engine.visitor.CollectVarsVisitor;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.engine.visitor.SubstituteVisitor;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.tdbc.PreparedQuery;
/*     */ 
/*     */ public abstract class RBExpression
/*     */   implements Cloneable
/*     */ {
/*  24 */   private Mode mode = null;
/*  25 */   private ModeCheckContext newContext = null;
/*     */   
/*     */   public abstract Compiled compile(CompilationContext paramCompilationContext);
/*     */   
/*     */   PreparedQuery prepareForRunning(QueryEngine engine) throws TypeModeError
/*     */   {
/*  31 */     RBExpression converted = convertToNormalForm();
/*  32 */     TypeEnv resultEnv = converted.typecheck(engine.rulebase(), Factory.makeTypeEnv());
/*  33 */     RBExpression result = 
/*  34 */       converted.convertToMode(Factory.makeModeCheckContext(engine.rulebase()));
/*  35 */     if ((result.getMode() instanceof ErrorMode))
/*  36 */       throw new TypeModeError(
/*  37 */         this + " cannot be converted to any declared mode\n" + 
/*  38 */         "   " + result.getMode());
/*  39 */     if (!RuleBase.silent) {
/*  40 */       System.err.println("inferred types: " + resultEnv);
/*  41 */       System.err.println("converted to Mode: " + result);
/*     */     }
/*     */     
/*  44 */     return new PreparedQuery(engine, result, resultEnv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Collection getVariables()
/*     */   {
/*  51 */     CollectVarsVisitor visitor = new CollectVarsVisitor();
/*  52 */     accept(visitor);
/*  53 */     Collection vars = visitor.getVars();
/*  54 */     vars.remove(RBIgnoredVariable.the);
/*  55 */     return vars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Collection getFreeVariables(ModeCheckContext context)
/*     */   {
/*  63 */     CollectFreeVarsVisitor visitor = new CollectFreeVarsVisitor(context);
/*  64 */     accept(visitor);
/*  65 */     return visitor.getVars();
/*     */   }
/*     */   
/*     */   public abstract TypeEnv typecheck(PredInfoProvider paramPredInfoProvider, TypeEnv paramTypeEnv)
/*     */     throws TypeModeError;
/*     */   
/*     */   public final RBExpression convertToMode(ModeCheckContext context) throws TypeModeError
/*     */   {
/*  73 */     return convertToMode(context, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract RBExpression convertToMode(ModeCheckContext paramModeCheckContext, boolean paramBoolean)
/*     */     throws TypeModeError;
/*     */   
/*     */ 
/*  81 */   public RBExpression convertToNormalForm() { return convertToNormalForm(false); }
/*     */   
/*     */   public RBExpression convertToNormalForm(boolean negate) {
/*     */     RBExpression result;
/*     */     RBExpression result;
/*  86 */     if (negate) {
/*  87 */       result = new RBNotFilter(this);
/*     */     } else {
/*  89 */       result = this;
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   public RBExpression crossMultiply(RBExpression other) {
/*  95 */     if ((other instanceof RBCompoundExpression))
/*  96 */       return other.crossMultiply(this);
/*  97 */     return FrontEnd.makeAnd(this, other);
/*     */   }
/*     */   
/*     */   public abstract Object accept(ExpressionVisitor paramExpressionVisitor);
/*     */   
/*     */   public RBExpression substitute(Frame frame) {
/* 103 */     SubstituteVisitor visitor = new SubstituteVisitor(frame);
/* 104 */     return (RBExpression)accept(visitor);
/*     */   }
/*     */   
/*     */   public RBExpression addExistsQuantifier(RBVariable[] newVars, boolean negate) {
/* 108 */     RBExistsQuantifier exists = new RBExistsQuantifier(newVars, this);
/* 109 */     if (negate) {
/* 110 */       return new RBNotFilter(exists);
/*     */     }
/* 112 */     return exists;
/*     */   }
/*     */   
/*     */   public RBExpression makeModed(Mode mode, ModeCheckContext context)
/*     */   {
/*     */     try {
/* 118 */       RBExpression clone = (RBExpression)clone();
/* 119 */       clone.setMode(mode, context);
/* 120 */       return clone;
/*     */     } catch (CloneNotSupportedException e) {
/* 122 */       e.printStackTrace();
/* 123 */       throw new Error("Should not happen");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setMode(Mode mode, ModeCheckContext context)
/*     */   {
/* 132 */     this.mode = mode;
/* 133 */     this.newContext = context;
/*     */   }
/*     */   
/*     */   public boolean isBetterThan(RBExpression other)
/*     */   {
/* 138 */     return getMode().isBetterThan(other.getMode());
/*     */   }
/*     */   
/*     */   protected Mode getMode() {
/* 142 */     return this.mode;
/*     */   }
/*     */   
/*     */   public ModeCheckContext getNewContext() {
/* 146 */     Assert.assertNotNull(this.newContext);
/* 147 */     return this.newContext;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */