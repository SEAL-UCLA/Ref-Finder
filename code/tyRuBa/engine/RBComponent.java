/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ import tyRuBa.modes.PredInfoProvider;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ import tyRuBa.modes.TupleType;
/*    */ import tyRuBa.modes.TypeModeError;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RBComponent
/*    */ {
/*    */   public abstract Mode getMode();
/*    */   
/*    */   public abstract TupleType typecheck(PredInfoProvider paramPredInfoProvider)
/*    */     throws TypeModeError;
/*    */   
/*    */   public abstract RBComponent convertToMode(PredicateMode paramPredicateMode, ModeCheckContext paramModeCheckContext)
/*    */     throws TypeModeError;
/*    */   
/*    */   public abstract PredicateIdentifier getPredId();
/*    */   
/*    */   public String getPredName()
/*    */   {
/* 55 */     return getPredId().getName();
/*    */   }
/*    */   
/*    */   public abstract RBTuple getArgs();
/*    */   
/*    */   public RBComponent convertToNormalForm() {
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isGroundFact() {
/* 65 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isValid()
/*    */   {
/* 70 */     return true;
/*    */   }
/*    */   
/*    */   public abstract Compiled compile(CompilationContext paramCompilationContext);
/*    */   
/*    */   public Validator getValidator() {
/* 76 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */