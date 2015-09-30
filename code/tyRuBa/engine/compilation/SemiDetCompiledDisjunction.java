/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class SemiDetCompiledDisjunction extends SemiDetCompiled
/*    */ {
/*    */   private SemiDetCompiled right;
/*    */   private SemiDetCompiled left;
/*    */   
/*    */   public SemiDetCompiledDisjunction(SemiDetCompiled left, SemiDetCompiled right)
/*    */   {
/* 12 */     this.left = left;
/* 13 */     this.right = right;
/*    */   }
/*    */   
/*    */   public tyRuBa.engine.Frame runSemiDet(Object input, RBContext context) {
/* 17 */     tyRuBa.engine.Frame result = this.left.runSemiDet(input, context);
/* 18 */     if (result == null) {
/* 19 */       return this.right.runSemiDet(input, context);
/*    */     }
/* 21 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return "SEMIDET(" + this.left + " + " + this.right + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/SemiDetCompiledDisjunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */