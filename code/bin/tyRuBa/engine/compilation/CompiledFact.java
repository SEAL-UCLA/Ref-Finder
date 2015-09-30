/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ 
/*    */ public class CompiledFact extends SemiDetCompiled
/*    */ {
/*    */   RBTuple args;
/*    */   
/*    */   public CompiledFact(RBTuple args)
/*    */   {
/* 14 */     super(tyRuBa.modes.Mode.makeSemidet());
/* 15 */     this.args = args;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 19 */     RBTerm goal = (RBTerm)input;
/*    */     
/*    */ 
/* 22 */     Frame callFrame = new Frame();
/*    */     
/* 24 */     goal = (RBTuple)goal.instantiate(callFrame);
/*    */     
/*    */ 
/* 27 */     Frame fc = goal.unify(this.args, new Frame());
/* 28 */     if (fc == null) {
/* 29 */       return null;
/*    */     }
/*    */     
/* 32 */     Frame result = callFrame.callResult(fc);
/*    */     
/*    */ 
/* 35 */     return result;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 40 */     return "FACT(" + this.args + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledFact.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */