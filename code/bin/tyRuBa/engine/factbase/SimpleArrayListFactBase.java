/*    */ package tyRuBa.engine.factbase;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import tyRuBa.engine.RBComponent;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.Multiplicity;
/*    */ import tyRuBa.modes.PredInfo;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleArrayListFactBase
/*    */   extends FactBase
/*    */ {
/* 31 */   ArrayList facts = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleArrayListFactBase(PredInfo info) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEmpty()
/*    */   {
/* 43 */     return this.facts.isEmpty();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isPersistent()
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void insert(RBComponent f)
/*    */   {
/* 57 */     this.facts.add(f);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Compiled basicCompile(PredicateMode mode, CompilationContext context)
/*    */   {
/* 65 */     if (mode.getMode().hi.compareTo(Multiplicity.one) <= 0) {
/* 66 */       return new SimpleArrayListFactBase.1(this, mode.getMode());
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 83 */     return new SimpleArrayListFactBase.2(this, mode.getMode());
/*    */   }
/*    */   
/*    */   public void backup() {}
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/SimpleArrayListFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */