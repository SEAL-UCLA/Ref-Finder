/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledFirst extends SemiDetCompiled
/*    */ {
/*    */   Compiled compiled;
/*    */   
/*    */   public CompiledFirst(Compiled compiled)
/*    */   {
/* 12 */     super(compiled.getMode().first());
/* 13 */     this.compiled = compiled;
/*    */   }
/*    */   
/*    */   public tyRuBa.engine.Frame runSemiDet(Object input, RBContext context) {
/* 17 */     ElementSource result = this.compiled.runNonDet(input, context);
/* 18 */     if (result.hasMoreElements()) {
/* 19 */       return (tyRuBa.engine.Frame)result.nextElement();
/*    */     }
/*    */     
/* 22 */     return null;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 26 */     return "FIRST(" + this.compiled + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledFirst.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */