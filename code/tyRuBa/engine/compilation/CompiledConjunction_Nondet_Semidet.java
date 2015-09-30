/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledConjunction_Nondet_Semidet extends Compiled
/*    */ {
/*    */   Compiled left;
/*    */   SemiDetCompiled right;
/*    */   
/*    */   public CompiledConjunction_Nondet_Semidet(Compiled left, SemiDetCompiled right)
/*    */   {
/* 14 */     super(left.getMode().multiply(right.getMode()));
/* 15 */     this.left = left;
/* 16 */     this.right = right;
/*    */   }
/*    */   
/*    */   public ElementSource runNonDet(Object input, final RBContext context) {
/* 20 */     this.left.runNonDet(input, context).map(new tyRuBa.util.Action() {
/*    */       public Object compute(Object arg) {
/* 22 */         return CompiledConjunction_Nondet_Semidet.this.right.runSemiDet(arg, context);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return "(" + this.right + " ==> " + this.left + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledConjunction_Nondet_Semidet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */