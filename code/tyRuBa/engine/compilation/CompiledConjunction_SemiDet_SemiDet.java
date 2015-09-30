/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class CompiledConjunction_SemiDet_SemiDet extends SemiDetCompiled
/*    */ {
/*    */   private final SemiDetCompiled left;
/*    */   private final SemiDetCompiled right;
/*    */   
/*    */   public CompiledConjunction_SemiDet_SemiDet(SemiDetCompiled left, SemiDetCompiled right)
/*    */   {
/* 12 */     super(left.getMode().multiply(right.getMode()));
/* 13 */     this.left = left;
/* 14 */     this.right = right;
/*    */   }
/*    */   
/*    */   public tyRuBa.engine.Frame runSemiDet(Object input, RBContext context) {
/* 18 */     tyRuBa.engine.Frame leftResult = this.left.runSemiDet(input, context);
/* 19 */     if (leftResult == null) {
/* 20 */       return null;
/*    */     }
/* 22 */     return this.right.runSemiDet(leftResult, context);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 26 */     return "(" + this.right + " ==> " + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledConjunction_SemiDet_SemiDet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */