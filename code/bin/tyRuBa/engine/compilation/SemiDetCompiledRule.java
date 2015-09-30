/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBAvoidRecursion;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBRule;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ 
/*    */ public class SemiDetCompiledRule extends SemiDetCompiled
/*    */ {
/*    */   RBTuple args;
/*    */   SemiDetCompiled compiledCond;
/*    */   RBRule rule;
/*    */   
/*    */   public SemiDetCompiledRule(RBRule rule, RBTuple args, SemiDetCompiled compiledCond)
/*    */   {
/* 18 */     super(rule.getMode());
/* 19 */     this.args = args;
/* 20 */     this.compiledCond = compiledCond;
/* 21 */     this.rule = rule;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 25 */     RBTerm goaL = (RBTerm)input;
/*    */     
/*    */ 
/* 28 */     Frame callFrame = new Frame();
/*    */     
/* 30 */     RBTuple goal = (RBTuple)goaL.instantiate(callFrame);
/*    */     
/*    */ 
/* 33 */     Frame fc = goal.unify(this.args, new Frame());
/* 34 */     if (fc == null) {
/* 35 */       return null;
/*    */     }
/*    */     
/* 38 */     RBRule r = this.rule.substitute(fc);
/* 39 */     context = new RBAvoidRecursion(context, r);
/* 40 */     Frame result = this.compiledCond.runSemiDet(fc, context);
/* 41 */     if (result == null) {
/* 42 */       return null;
/*    */     }
/* 44 */     return callFrame.callResult(result);
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 50 */     return "RULE(" + this.args + " :- " + this.compiledCond + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/SemiDetCompiledRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */