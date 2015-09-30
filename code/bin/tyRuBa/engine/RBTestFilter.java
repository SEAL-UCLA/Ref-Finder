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
/*    */ public class RBTestFilter extends RBExpression
/*    */ {
/*    */   private RBExpression test_q;
/*    */   
/*    */   public RBTestFilter(RBExpression test_query)
/*    */   {
/* 20 */     this.test_q = test_query;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 24 */     return "TEST(" + getQuery() + ")";
/*    */   }
/*    */   
/*    */   public Compiled compile(CompilationContext c) {
/* 28 */     return this.test_q.compile(c).test();
/*    */   }
/*    */   
/*    */   public RBExpression getQuery() {
/* 32 */     return this.test_q;
/*    */   }
/*    */   
/*    */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError {
/*    */     try {
/* 37 */       return getQuery().typecheck(predinfo, startEnv);
/*    */     } catch (TypeModeError e) {
/* 39 */       throw new TypeModeError(e, this);
/*    */     }
/*    */   }
/*    */   
/*    */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*    */   {
/* 45 */     Collection vars = this.test_q.getFreeVariables(context);
/*    */     
/* 47 */     if (vars.isEmpty()) {
/* 48 */       RBExpression converted = this.test_q.convertToMode(context, rearrange);
/* 49 */       return Factory.makeModedExpression(
/* 50 */         new RBTestFilter(converted), 
/* 51 */         converted.getMode().first(), 
/* 52 */         context);
/*    */     }
/* 54 */     return Factory.makeModedExpression(this, 
/* 55 */       new ErrorMode("Variables improperly left unbound in TEST: " + vars), 
/* 56 */       context);
/*    */   }
/*    */   
/*    */   public RBExpression convertToNormalForm(boolean negate)
/*    */   {
/* 61 */     if (negate) {
/* 62 */       return getQuery().convertToNormalForm(true);
/*    */     }
/* 64 */     return new RBTestFilter(getQuery().convertToNormalForm(false));
/*    */   }
/*    */   
/*    */   public Object accept(ExpressionVisitor v)
/*    */   {
/* 69 */     return v.visit(this);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBTestFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */