/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.Action;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SemiDetCompiled
/*    */   extends Compiled
/*    */ {
/*    */   public SemiDetCompiled(Mode mode)
/*    */   {
/* 19 */     super(mode);
/*    */   }
/*    */   
/*    */   public SemiDetCompiled() {
/* 23 */     super(Mode.makeSemidet());
/*    */   }
/*    */   
/*    */   public ElementSource run(ElementSource inputs, final RBContext context) {
/* 27 */     inputs.map(new Action() {
/*    */       public Object compute(Object arg) {
/* 29 */         return SemiDetCompiled.this.runSemiDet(arg, context);
/*    */       }
/*    */     });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract Frame runSemiDet(Object paramObject, RBContext paramRBContext);
/*    */   
/*    */ 
/*    */ 
/*    */   public ElementSource runNonDet(Object input, RBContext context)
/*    */   {
/* 42 */     Frame result = runSemiDet(input, context);
/* 43 */     if (result == null) {
/* 44 */       return ElementSource.theEmpty;
/*    */     }
/*    */     
/* 47 */     return ElementSource.singleton(result);
/*    */   }
/*    */   
/*    */   public Compiled conjoin(Compiled other)
/*    */   {
/* 52 */     if ((other instanceof SemiDetCompiled)) {
/* 53 */       return new CompiledConjunction_SemiDet_SemiDet(
/* 54 */         this, (SemiDetCompiled)other);
/*    */     }
/* 56 */     return new CompiledConjunction_SemiDet_NonDet(this, other);
/*    */   }
/*    */   
/*    */   public Compiled disjoin(Compiled other) {
/* 60 */     if (other.equals(fail))
/* 61 */       return this;
/* 62 */     if ((other instanceof SemiDetCompiled)) {
/* 63 */       return new CompiledDisjunction_SemiDet_SemiDet(
/* 64 */         this, (SemiDetCompiled)other);
/*    */     }
/* 66 */     return new CompiledDisjunction_SemiDet_NonDet(this, other);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/SemiDetCompiled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */