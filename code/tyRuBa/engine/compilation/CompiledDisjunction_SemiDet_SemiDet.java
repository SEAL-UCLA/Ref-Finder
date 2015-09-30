/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledDisjunction_SemiDet_SemiDet extends Compiled
/*    */ {
/*    */   private final SemiDetCompiled left;
/*    */   private final SemiDetCompiled right;
/*    */   
/*    */   public CompiledDisjunction_SemiDet_SemiDet(SemiDetCompiled left, SemiDetCompiled right)
/*    */   {
/* 14 */     super(left.getMode().add(right.getMode()));
/* 15 */     this.left = left;
/* 16 */     this.right = right;
/*    */   }
/*    */   
/*    */   public ElementSource runNonDet(Object input, RBContext context) {
/* 20 */     Frame leftResult = this.left.runSemiDet(input, context);
/* 21 */     Frame rightResult = this.right.runSemiDet(input, context);
/* 22 */     if ((leftResult == null) && (rightResult == null)) {
/* 23 */       return ElementSource.theEmpty;
/*    */     }
/* 25 */     if (leftResult == null)
/*    */     {
/* 27 */       return ElementSource.singleton(rightResult); }
/* 28 */     if (rightResult == null)
/*    */     {
/* 30 */       return ElementSource.singleton(leftResult);
/*    */     }
/* 32 */     return ElementSource.with(new Object[] { leftResult, rightResult });
/*    */   }
/*    */   
/*    */ 
/*    */   public SemiDetCompiled first()
/*    */   {
/* 38 */     return new SemiDetCompiledDisjunction(this.left, this.right);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 42 */     return "(" + this.right + " + " + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledDisjunction_SemiDet_SemiDet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */