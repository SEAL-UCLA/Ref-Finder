/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledConjunction_SemiDet_NonDet extends Compiled
/*    */ {
/*    */   private final SemiDetCompiled left;
/*    */   private final Compiled right;
/*    */   
/*    */   public CompiledConjunction_SemiDet_NonDet(SemiDetCompiled left, Compiled right)
/*    */   {
/* 13 */     super(left.getMode().multiply(right.getMode()));
/* 14 */     this.left = left;
/* 15 */     this.right = right;
/*    */   }
/*    */   
/*    */   public ElementSource runNonDet(Object input, RBContext context) {
/* 19 */     tyRuBa.engine.Frame leftResult = this.left.runSemiDet(input, context);
/* 20 */     if (leftResult == null) {
/* 21 */       return ElementSource.theEmpty;
/*    */     }
/* 23 */     return this.right.runNonDet(leftResult, context);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 27 */     return "(" + this.right + "==>" + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledConjunction_SemiDet_NonDet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */