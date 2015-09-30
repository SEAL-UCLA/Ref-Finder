/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.CompiledUnique;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBUniqueQuantifier
/*     */   extends RBExpression
/*     */ {
/*     */   private RBExpression exp;
/*     */   private RBVariable[] vars;
/*     */   
/*     */   public RBUniqueQuantifier(Collection variables, RBExpression exp)
/*     */   {
/*  31 */     this.exp = exp;
/*  32 */     this.vars = ((RBVariable[])variables.toArray(new RBVariable[variables.size()]));
/*     */   }
/*     */   
/*     */   public RBUniqueQuantifier(RBVariable[] vars, RBExpression exp) {
/*  36 */     this.exp = exp;
/*  37 */     this.vars = vars;
/*     */   }
/*     */   
/*     */   public RBExpression getExp() {
/*  41 */     return this.exp;
/*     */   }
/*     */   
/*     */   public int getNumVars() {
/*  45 */     return this.vars.length;
/*     */   }
/*     */   
/*     */   public RBVariable getVarAt(int pos) {
/*  49 */     return this.vars[pos];
/*     */   }
/*     */   
/*     */   public RBVariable[] getQuantifiedVars() {
/*  53 */     return this.vars;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  57 */     StringBuffer result = new StringBuffer("(UNIQUE ");
/*  58 */     for (int i = 0; i < this.vars.length; i++) {
/*  59 */       if (i > 0)
/*  60 */         result.append(",");
/*  61 */       result.append(this.vars[i].toString());
/*     */     }
/*  63 */     result.append(" : " + getExp() + ")");
/*  64 */     return result.toString();
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*     */     try {
/*  69 */       return getExp().typecheck(predinfo, startEnv);
/*     */     } catch (TypeModeError e) {
/*  71 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError {
/*  76 */     ModeCheckContext resultContext = (ModeCheckContext)context.clone();
/*  77 */     Collection boundedVars = this.exp.getVariables();
/*  78 */     Collection vars = new HashSet();
/*     */     
/*  80 */     for (int i = 0; i < getNumVars(); i++) {
/*  81 */       RBVariable currVar = getVarAt(i);
/*  82 */       if (!boundedVars.contains(currVar)) {
/*  83 */         return Factory.makeModedExpression(
/*  84 */           this, 
/*  85 */           new ErrorMode("UNIQUE variable " + currVar + 
/*  86 */           " must become bound in " + this.exp), 
/*  87 */           context);
/*     */       }
/*  89 */       vars.add(currVar);
/*     */     }
/*     */     
/*     */ 
/*  93 */     Collection freeVars = getFreeVariables(resultContext);
/*     */     
/*  95 */     if (freeVars.isEmpty()) {
/*  96 */       RBExpression converted = this.exp.convertToMode(context, rearrange);
/*  97 */       return Factory.makeModedExpression(
/*  98 */         new RBUniqueQuantifier(vars, converted), 
/*  99 */         converted.getMode().unique(), converted.getNewContext());
/*     */     }
/* 101 */     return Factory.makeModedExpression(
/* 102 */       this, 
/* 103 */       new ErrorMode("Variables improperly left unbound in UNIQUE: " + 
/* 104 */       freeVars), 
/* 105 */       resultContext);
/*     */   }
/*     */   
/*     */   public RBExpression convertToNormalForm(boolean negate)
/*     */   {
/* 110 */     RBExpression result = new RBUniqueQuantifier(
/* 111 */       this.vars, this.exp.convertToNormalForm(false));
/* 112 */     if (negate) {
/* 113 */       return new RBNotFilter(result);
/*     */     }
/* 115 */     return result;
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 120 */     return v.visit(this);
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/* 124 */     return new CompiledUnique(this.vars, this.exp.compile(c));
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBUniqueQuantifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */