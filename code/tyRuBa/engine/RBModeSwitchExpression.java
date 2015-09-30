/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Vector;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCase;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBModeSwitchExpression
/*     */   extends RBExpression
/*     */ {
/*  26 */   Vector modeCases = new Vector();
/*  27 */   RBExpression defaultExp = null;
/*     */   
/*     */   public RBModeSwitchExpression(ModeCase mc) {
/*  30 */     this.modeCases.add(mc);
/*     */   }
/*     */   
/*     */   public void addModeCase(ModeCase mc) {
/*  34 */     this.modeCases.add(mc);
/*     */   }
/*     */   
/*     */   public void addDefaultCase(RBExpression exp) {
/*  38 */     this.defaultExp = exp;
/*     */   }
/*     */   
/*     */   public ModeCase getModeCaseAt(int pos) {
/*  42 */     return (ModeCase)this.modeCases.elementAt(pos);
/*     */   }
/*     */   
/*     */   public int getNumModeCases() {
/*  46 */     return this.modeCases.size();
/*     */   }
/*     */   
/*     */   public boolean hasDefaultExp() {
/*  50 */     return this.defaultExp != null;
/*     */   }
/*     */   
/*     */   public RBExpression getDefaultExp() {
/*  54 */     return this.defaultExp;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  58 */     StringBuffer result = new StringBuffer();
/*  59 */     for (int i = 0; i < getNumModeCases(); i++) {
/*  60 */       if (i > 0) {
/*  61 */         result.append("\n| ");
/*     */       }
/*  63 */       result.append(getModeCaseAt(i).toString());
/*     */     }
/*  65 */     if (this.defaultExp != null) {
/*  66 */       result.append("\n| DEFAULT: " + this.defaultExp);
/*     */     }
/*  68 */     return result.toString();
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  72 */     throw new Error("Should not happen: a mode case should have been selected before any compilation is performed");
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError
/*     */   {
/*     */     try {
/*  78 */       TypeEnv resultEnv = null;
/*  79 */       for (int i = 0; i < getNumModeCases(); i++) {
/*  80 */         RBExpression currExp = getModeCaseAt(i).getExp();
/*  81 */         TypeEnv currEnv = currExp.typecheck(predinfo, startEnv);
/*     */         
/*  83 */         if (resultEnv == null) {
/*  84 */           resultEnv = currEnv;
/*     */         } else {
/*  86 */           resultEnv = resultEnv.union(currEnv);
/*     */         }
/*     */       }
/*     */       TypeEnv currEnv;
/*  90 */       if (hasDefaultExp()) {
/*  91 */         currEnv = getDefaultExp().typecheck(predinfo, startEnv);
/*  92 */         if (resultEnv == null)
/*  93 */           resultEnv = currEnv;
/*     */       }
/*  95 */       return resultEnv.union(currEnv);
/*     */ 
/*     */     }
/*     */     catch (TypeModeError e)
/*     */     {
/*     */ 
/* 101 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*     */   {
/* 107 */     for (int i = 0; i < getNumModeCases(); i++) {
/* 108 */       ModeCase currModeCase = getModeCaseAt(i);
/* 109 */       Collection boundVars = currModeCase.getBoundVars();
/* 110 */       if (context.checkIfAllBound(boundVars)) {
/* 111 */         RBExpression converted = 
/* 112 */           currModeCase.getExp().convertToMode(context, false);
/* 113 */         return Factory.makeModedExpression(converted, 
/* 114 */           converted.getMode(), converted.getNewContext());
/*     */       }
/*     */     }
/* 117 */     if (hasDefaultExp()) {
/* 118 */       RBExpression converted = 
/* 119 */         getDefaultExp().convertToMode(context, rearrange);
/* 120 */       return Factory.makeModedExpression(
/* 121 */         converted, converted.getMode(), converted.getNewContext());
/*     */     }
/* 123 */     return Factory.makeModedExpression(
/* 124 */       this, 
/* 125 */       new ErrorMode("There is a missing mode case in " + this), 
/* 126 */       context);
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 131 */     return v.visit(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBModeSwitchExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */