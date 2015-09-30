/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ public class RBConjunction
/*     */   extends RBCompoundExpression
/*     */ {
/*     */   public RBConjunction() {}
/*     */   
/*     */   public RBConjunction(Vector exps)
/*     */   {
/*  24 */     super(exps);
/*     */   }
/*     */   
/*     */   public RBConjunction(Object[] exps) {
/*  28 */     super(exps);
/*     */   }
/*     */   
/*     */   public RBConjunction(RBExpression e1, RBExpression e2) {
/*  32 */     super(e1, e2);
/*     */   }
/*     */   
/*     */   public final Compiled compile(CompilationContext c)
/*     */   {
/*  37 */     Compiled res = Compiled.succeed;
/*  38 */     for (int i = 0; i < getNumSubexps(); i++) {
/*  39 */       res = res.conjoin(getSubexp(i).compile(c));
/*     */     }
/*  41 */     return res;
/*     */   }
/*     */   
/*     */   protected String separator() {
/*  45 */     return ",";
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError
/*     */   {
/*  50 */     TypeEnv resultEnv = startEnv;
/*  51 */     for (int i = 0; i < getNumSubexps(); i++) {
/*     */       try {
/*  53 */         resultEnv = resultEnv.intersect(
/*  54 */           getSubexp(i).typecheck(predinfo, resultEnv));
/*     */       } catch (TypeModeError e) {
/*  56 */         throw new TypeModeError(e, this);
/*     */       }
/*     */     }
/*  59 */     return resultEnv;
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrangeAllowed) throws TypeModeError
/*     */   {
/*  64 */     RBConjunction result = new RBConjunction();
/*  65 */     Mode resultMode = Mode.makeDet();
/*     */     
/*  67 */     ArrayList toBeChecked = getSubexpsArrayList();
/*     */     
/*  69 */     while (!toBeChecked.isEmpty())
/*     */     {
/*  71 */       int bestPos = 0;
/*  72 */       RBExpression best; if (rearrangeAllowed) {
/*  73 */         RBExpression best = ((RBExpression)toBeChecked.get(0))
/*  74 */           .convertToMode(context, true);
/*  75 */         for (int i = 1; i < toBeChecked.size(); i++) {
/*  76 */           RBExpression exp = (RBExpression)toBeChecked.get(i);
/*  77 */           RBExpression converted = exp.convertToMode(context, true);
/*  78 */           if (converted.isBetterThan(best)) {
/*  79 */             best = converted;
/*  80 */             bestPos = i;
/*     */           }
/*     */         }
/*  83 */         if ((best.getMode() instanceof ErrorMode)) {
/*  84 */           return Factory.makeModedExpression(
/*  85 */             this, 
/*  86 */             best.getMode(), 
/*  87 */             context);
/*     */         }
/*     */       } else {
/*  90 */         best = 
/*  91 */           ((RBExpression)toBeChecked.get(0)).convertToMode(context, false);
/*     */       }
/*  93 */       result.addSubexp(best);
/*  94 */       toBeChecked.remove(bestPos);
/*  95 */       resultMode = resultMode.multiply(best.getMode());
/*  96 */       context = best.getNewContext();
/*     */     }
/*     */     
/*  99 */     return Factory.makeModedExpression(result, resultMode, context);
/*     */   }
/*     */   
/*     */   public RBExpression convertToNormalForm(boolean negate) {
/* 103 */     if (negate) {
/* 104 */       RBDisjunction result = new RBDisjunction();
/* 105 */       for (int i = 0; i < getNumSubexps(); i++) {
/* 106 */         result.addSubexp(getSubexp(i).convertToNormalForm(true));
/*     */       }
/* 108 */       return result;
/*     */     }
/*     */     
/* 111 */     RBExpression result = null;
/* 112 */     for (int i = 0; i < getNumSubexps(); i++) {
/* 113 */       RBExpression converted = 
/* 114 */         getSubexp(i).convertToNormalForm(false);
/* 115 */       if (result == null) {
/* 116 */         result = converted;
/*     */       } else {
/* 118 */         result = result.crossMultiply(converted);
/*     */       }
/*     */     }
/* 121 */     return result;
/*     */   }
/*     */   
/*     */   public RBExpression crossMultiply(RBExpression other)
/*     */   {
/* 126 */     if ((other instanceof RBDisjunction)) {
/* 127 */       return other.crossMultiply(this);
/*     */     }
/* 129 */     RBConjunction result = (RBConjunction)clone();
/* 130 */     if ((other instanceof RBConjunction)) {
/* 131 */       RBConjunction cother = (RBConjunction)other;
/* 132 */       for (int i = 0; i < cother.getNumSubexps(); i++)
/* 133 */         result.addSubexp(cother.getSubexp(i));
/*     */     } else {
/* 135 */       result.addSubexp(other); }
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v) {
/* 140 */     return v.visit(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBConjunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */