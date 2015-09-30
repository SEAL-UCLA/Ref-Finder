/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class CompiledDisjunction extends Compiled
/*    */ {
/*    */   private Compiled right;
/*    */   private Compiled left;
/*    */   
/*    */   public CompiledDisjunction(Compiled left, Compiled right)
/*    */   {
/* 12 */     super(left.getMode().add(right.getMode()));
/* 13 */     this.left = left;
/* 14 */     this.right = right;
/*    */   }
/*    */   
/*    */   public tyRuBa.util.ElementSource runNonDet(Object input, RBContext context) {
/* 18 */     return this.left.runNonDet(input, context).append(this.right.runNonDet(input, context));
/*    */   }
/*    */   
/*    */   public Compiled negate() {
/* 22 */     return this.left.negate().conjoin(this.right.negate());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 26 */     return "(" + this.right + " + " + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledDisjunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */