/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ public class RBDisjunction
/*     */   extends RBCompoundExpression
/*     */ {
/*     */   public RBDisjunction() {}
/*     */   
/*     */   public RBDisjunction(Vector exps)
/*     */   {
/*  22 */     super(exps);
/*     */   }
/*     */   
/*     */   public RBDisjunction(Object[] exps) {
/*  26 */     super(exps);
/*     */   }
/*     */   
/*     */   public RBDisjunction(RBExpression e1, RBExpression e2) {
/*  30 */     super(e1, e2);
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  34 */     Compiled result = Compiled.fail;
/*  35 */     for (int i = 0; i < getNumSubexps(); i++) {
/*  36 */       result = result.disjoin(getSubexp(i).compile(c));
/*     */     }
/*  38 */     return result;
/*     */   }
/*     */   
/*     */   protected String separator() {
/*  42 */     return ";";
/*     */   }
/*     */   
/*     */   public RBExpression crossMultiplyDisjunction(RBDisjunction other) {
/*  46 */     int numExps = getNumSubexps();
/*  47 */     int otherNumExps = other.getNumSubexps();
/*  48 */     RBDisjunction result = new RBDisjunction();
/*  49 */     for (int pos = 0; pos < numExps; pos++) {
/*  50 */       for (int otherPos = 0; otherPos < otherNumExps; otherPos++)
/*  51 */         result.addSubexp(
/*  52 */           FrontEnd.makeAnd(getSubexp(pos), other.getSubexp(otherPos)));
/*     */     }
/*  54 */     return result;
/*     */   }
/*     */   
/*     */   public RBExpression crossMultiply(RBExpression other) {
/*  58 */     if ((other instanceof RBDisjunction))
/*  59 */       return crossMultiplyDisjunction((RBDisjunction)other);
/*  60 */     int numExps = getNumSubexps();
/*  61 */     RBDisjunction result = new RBDisjunction();
/*  62 */     for (int pos = 0; pos < numExps; pos++) {
/*  63 */       result.addSubexp(FrontEnd.makeAnd(getSubexp(pos), other));
/*     */     }
/*  65 */     return result;
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*  69 */     TypeEnv resultEnv = null;
/*     */     try {
/*  71 */       for (int i = 0; i < getNumSubexps(); i++) {
/*  72 */         TypeEnv currEnv = getSubexp(i).typecheck(predinfo, startEnv);
/*  73 */         if (resultEnv == null) {
/*  74 */           resultEnv = currEnv;
/*     */         } else {
/*  76 */           resultEnv = resultEnv.union(currEnv);
/*     */         }
/*     */       }
/*     */     } catch (TypeModeError e) {
/*  80 */       throw new TypeModeError(e, this);
/*     */     }
/*  82 */     return resultEnv;
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*     */   {
/*  87 */     RBDisjunction result = new RBDisjunction();
/*  88 */     Mode resultMode = Mode.makeFail();
/*  89 */     ModeCheckContext resultContext = null;
/*  90 */     for (int i = 0; i < getNumSubexps(); i++) {
/*  91 */       RBExpression converted = 
/*  92 */         getSubexp(i).convertToMode(context, rearrange);
/*  93 */       if (resultContext == null) {
/*  94 */         resultContext = converted.getNewContext();
/*     */       } else {
/*  96 */         resultContext = 
/*  97 */           resultContext.intersection(converted.getNewContext());
/*     */       }
/*  99 */       result.addSubexp(converted);
/* 100 */       resultMode = resultMode.add(converted.getMode());
/*     */     }
/* 102 */     return Factory.makeModedExpression(result, resultMode, resultContext);
/*     */   }
/*     */   
/*     */   public RBExpression convertToNormalForm(boolean negate) {
/* 106 */     if (negate) {
/* 107 */       RBExpression result = null;
/* 108 */       for (int i = 0; i < getNumSubexps(); i++) {
/* 109 */         RBExpression converted = 
/* 110 */           getSubexp(i).convertToNormalForm(true);
/* 111 */         if (result == null) {
/* 112 */           result = converted;
/*     */         } else {
/* 114 */           result = result.crossMultiply(converted);
/*     */         }
/*     */       }
/* 117 */       return result;
/*     */     }
/* 119 */     RBDisjunction result = new RBDisjunction();
/* 120 */     for (int i = 0; i < getNumSubexps(); i++) {
/* 121 */       result.addSubexp(getSubexp(i).convertToNormalForm(false));
/*     */     }
/* 123 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 128 */   public Object accept(ExpressionVisitor v) { return v.visit(this); }
/*     */   
/*     */   public RBExpression addExistsQuantifier(RBVariable[] newVars, boolean negate) {
/*     */     RBCompoundExpression result;
/*     */     RBCompoundExpression result;
/* 133 */     if (negate) {
/* 134 */       result = new RBConjunction();
/*     */     } else {
/* 136 */       result = new RBDisjunction();
/*     */     }
/* 138 */     for (int i = 0; i < getNumSubexps(); i++) {
/* 139 */       result.addSubexp(getSubexp(i).addExistsQuantifier(newVars, negate));
/*     */     }
/* 141 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBDisjunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */