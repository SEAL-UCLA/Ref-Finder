/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledDisjunction_SemiDet_NonDet extends Compiled
/*    */ {
/*    */   SemiDetCompiled left;
/*    */   Compiled right;
/*    */   
/*    */   public CompiledDisjunction_SemiDet_NonDet(SemiDetCompiled left, Compiled right)
/*    */   {
/* 14 */     super(left.getMode().add(right.getMode()));
/* 15 */     this.left = left;
/* 16 */     this.right = right;
/*    */   }
/*    */   
/*    */   public ElementSource runNonDet(Object input, RBContext context) {
/* 20 */     tyRuBa.engine.Frame leftResult = this.left.runSemiDet(input, context);
/* 21 */     ElementSource rightResult = this.right.runNonDet(input, context);
/* 22 */     if (leftResult == null) {
/* 23 */       return rightResult;
/*    */     }
/*    */     
/* 26 */     return ElementSource.singleton(leftResult).append(rightResult);
/*    */   }
/*    */   
/*    */ 
/*    */   public SemiDetCompiled first()
/*    */   {
/* 32 */     return new SemiDetCompiledDisjunction(this.left, this.right.first());
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return "(" + this.left + " + " + this.right + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledDisjunction_SemiDet_NonDet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */