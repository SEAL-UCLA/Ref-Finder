/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBRule;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.Multiplicity;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledRule extends Compiled
/*    */ {
/*    */   RBTuple args;
/*    */   Compiled compiledCond;
/*    */   RBRule rule;
/*    */   
/*    */   public CompiledRule(RBRule rule, RBTuple args, Compiled compiledCond)
/*    */   {
/* 21 */     super(rule.getMode());
/* 22 */     this.args = args;
/* 23 */     this.compiledCond = compiledCond;
/* 24 */     this.rule = rule;
/*    */   }
/*    */   
/*    */   private static void debugInfo(RBRule r, RBTerm goal, RBTerm callGoal, Frame callFrame, Frame result, Frame callResult)
/*    */   {
/* 29 */     System.err.println("Rule invoked: " + r);
/* 30 */     System.err.println("For goal: " + goal);
/* 31 */     System.err.println("Call goal: " + callGoal);
/* 32 */     System.err.println("CallFrame: " + callFrame);
/* 33 */     System.err.println("Result: " + result);
/* 34 */     System.err.println("CallResult: " + callResult);
/*    */   }
/*    */   
/*    */   public ElementSource runNonDet(Object input, RBContext context) {
/* 38 */     RBTerm goaL = (RBTerm)input;
/*    */     
/*    */ 
/* 41 */     Frame callFrame = new Frame();
/*    */     
/* 43 */     RBTuple goal = (RBTuple)goaL.instantiate(callFrame);
/*    */     
/*    */ 
/* 46 */     Frame fc = goal.unify(this.args, new Frame());
/* 47 */     if (fc == null) {
/* 48 */       return ElementSource.theEmpty;
/*    */     }
/*    */     
/* 51 */     RBRule r = this.rule.substitute(fc);
/* 52 */     context = new tyRuBa.engine.RBAvoidRecursion(context, r);
/* 53 */     return this.compiledCond.runNonDet(fc, context).map(new CompiledRule.1(this, callFrame));
/*    */   }
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
/*    */   public static Compiled make(RBRule rule, RBTuple args, Compiled compiledCond)
/*    */   {
/* 67 */     Mode mode = rule.getMode();
/* 68 */     if (mode.hi.compareTo(Multiplicity.one) <= 0) {
/* 69 */       return new SemiDetCompiledRule(rule, args, 
/* 70 */         (SemiDetCompiled)compiledCond);
/*    */     }
/* 72 */     return new CompiledRule(rule, args, compiledCond);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 76 */     return "RULE(" + this.args + " :- " + this.compiledCond + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */