/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.engine.RuleBase;
/*    */ 
/*    */ public class SemiDetCompiledPredicateExpression extends SemiDetCompiled
/*    */ {
/*    */   private final RuleBase rules;
/*    */   private final RBTuple args;
/*    */   
/*    */   public SemiDetCompiledPredicateExpression(tyRuBa.modes.Mode mode, RuleBase rules, RBTuple args)
/*    */   {
/* 15 */     super(mode);
/* 16 */     this.rules = rules;
/* 17 */     this.args = args;
/*    */   }
/*    */   
/*    */   public final Frame runSemiDet(Object input, RBContext context) {
/* 21 */     RBTuple goal = (RBTuple)this.args.substitute((Frame)input);
/* 22 */     Frame result = compiledRules().runSemiDet(goal, context);
/* 23 */     if (((Frame)input).isEmpty())
/*    */     {
/* 25 */       return result; }
/* 26 */     if (result == null) {
/* 27 */       return null;
/*    */     }
/* 29 */     return ((Frame)input).append(result);
/*    */   }
/*    */   
/*    */   private SemiDetCompiled compiledRules()
/*    */   {
/* 34 */     return this.rules.getSemiDetCompiledRules();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return "SEMIDET PRED(" + this.args + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/SemiDetCompiledPredicateExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */