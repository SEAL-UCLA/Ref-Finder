/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*    */ import tyRuBa.modes.ErrorMode;
/*    */ import tyRuBa.modes.Factory;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ import tyRuBa.modes.PredInfoProvider;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ public class RBNotFilter extends RBExpression
/*    */ {
/*    */   private RBExpression negated_q;
/*    */   
/*    */   public RBNotFilter(RBExpression not_q)
/*    */   {
/* 20 */     this.negated_q = not_q;
/*    */   }
/*    */   
/*    */   public RBExpression getNegatedQuery() {
/* 24 */     return this.negated_q;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return "NOT(" + getNegatedQuery() + ")";
/*    */   }
/*    */   
/*    */   public Compiled compile(CompilationContext c) {
/* 32 */     return getNegatedQuery().compile(c).negate();
/*    */   }
/*    */   
/*    */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError
/*    */   {
/*    */     try {
/* 38 */       getNegatedQuery().typecheck(predinfo, startEnv);
/* 39 */       return startEnv;
/*    */     } catch (TypeModeError e) {
/* 41 */       throw new TypeModeError(e, this);
/*    */     }
/*    */   }
/*    */   
/*    */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*    */   {
/* 47 */     Collection vars = this.negated_q.getFreeVariables(context);
/*    */     
/* 49 */     if (vars.isEmpty()) {
/* 50 */       RBExpression converted = this.negated_q.convertToMode(context, rearrange);
/* 51 */       return Factory.makeModedExpression(
/* 52 */         new RBNotFilter(converted), 
/* 53 */         converted.getMode().negate(), 
/* 54 */         context);
/*    */     }
/* 56 */     return Factory.makeModedExpression(
/* 57 */       this, 
/* 58 */       new ErrorMode("Variables improperly left unbound in NOT: " + vars), 
/* 59 */       context);
/*    */   }
/*    */   
/*    */   public RBExpression convertToNormalForm(boolean negate)
/*    */   {
/* 64 */     if (negate) {
/* 65 */       return getNegatedQuery().convertToNormalForm(false);
/*    */     }
/* 67 */     return getNegatedQuery().convertToNormalForm(true);
/*    */   }
/*    */   
/*    */   public Object accept(ExpressionVisitor v)
/*    */   {
/* 72 */     return v.visit(this);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBNotFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */