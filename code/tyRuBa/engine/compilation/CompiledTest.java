/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ 
/*    */ public class CompiledTest extends SemiDetCompiled
/*    */ {
/*    */   private Compiled tested;
/*    */   
/*    */   public CompiledTest(Compiled tested)
/*    */   {
/* 11 */     super(tested.getMode().first());
/* 12 */     this.tested = tested;
/*    */   }
/*    */   
/*    */   public tyRuBa.engine.Frame runSemiDet(Object input, RBContext context) {
/* 16 */     if (this.tested.runNonDet(input, context).hasMoreElements()) {
/* 17 */       return (tyRuBa.engine.Frame)input;
/*    */     }
/* 19 */     return null;
/*    */   }
/*    */   
/*    */   public Compiled negate() {
/* 23 */     return new CompiledNot(this.tested);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 27 */     return "TEST(" + this.tested + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */