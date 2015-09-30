/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class CompiledNot extends SemiDetCompiled
/*    */ {
/*    */   private Compiled negated;
/*    */   
/*    */   public CompiledNot(Compiled negated)
/*    */   {
/* 12 */     super(negated.getMode().negate());
/* 13 */     this.negated = negated;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 17 */     if (this.negated.runNonDet(input, context).hasMoreElements()) {
/* 18 */       return null;
/*    */     }
/* 20 */     return (Frame)input;
/*    */   }
/*    */   
/*    */   public Compiled negate()
/*    */   {
/* 25 */     return new CompiledTest(this.negated);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     return "NOT(" + this.negated + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledNot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */