/*    */ package tyRuBa.engine.factbase;
/*    */ 
/*    */ import tyRuBa.engine.RBComponent;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FactBase
/*    */ {
/*    */   public abstract boolean isPersistent();
/*    */   
/*    */   public abstract void backup();
/*    */   
/*    */   public abstract void insert(RBComponent paramRBComponent);
/*    */   
/*    */   public abstract boolean isEmpty();
/*    */   
/*    */   public final Compiled compile(PredicateMode mode, CompilationContext context)
/*    */   {
/* 46 */     if (isEmpty()) {
/* 47 */       return Compiled.fail;
/*    */     }
/* 49 */     return basicCompile(mode, context);
/*    */   }
/*    */   
/*    */   public abstract Compiled basicCompile(PredicateMode paramPredicateMode, CompilationContext paramCompilationContext);
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/FactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */