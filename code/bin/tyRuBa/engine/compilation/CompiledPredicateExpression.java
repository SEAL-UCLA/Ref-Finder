/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.engine.RuleBase;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledPredicateExpression extends Compiled
/*    */ {
/*    */   private final RuleBase rules;
/*    */   private final RBTuple args;
/*    */   
/*    */   public CompiledPredicateExpression(Mode mode, RuleBase rules, RBTuple args)
/*    */   {
/* 17 */     super(mode);
/* 18 */     this.rules = rules;
/* 19 */     this.args = args;
/*    */   }
/*    */   
/*    */   public final ElementSource runNonDet(Object input, RBContext context) {
/* 23 */     RBTuple goal = (RBTuple)this.args.substitute((Frame)input);
/* 24 */     ElementSource result = compiledRules().runNonDet(goal, context);
/* 25 */     if (((Frame)input).isEmpty())
/*    */     {
/* 27 */       return result;
/*    */     }
/* 29 */     return result.map(new CompiledPredicateExpression.1(this, input));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Compiled compiledRules()
/*    */   {
/* 41 */     return this.rules.getCompiled();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 45 */     return "PRED(" + this.args + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledPredicateExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */