/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class CompiledConjunction extends Compiled
/*    */ {
/*    */   private Compiled right;
/*    */   private Compiled left;
/*    */   
/*    */   public CompiledConjunction(Compiled left, Compiled right)
/*    */   {
/* 12 */     super(left.getMode().multiply(right.getMode()));
/* 13 */     this.left = left;
/* 14 */     this.right = right;
/*    */   }
/*    */   
/*    */   public tyRuBa.util.ElementSource run(tyRuBa.util.ElementSource inputs, RBContext context) {
/* 18 */     return this.right.run(this.left.run(inputs, context), context);
/*    */   }
/*    */   
/*    */   public tyRuBa.util.ElementSource runNonDet(Object input, RBContext context) {
/* 22 */     return this.right.run(this.left.runNonDet(input, context), context);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 26 */     return "(" + this.right + "==>" + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledConjunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */