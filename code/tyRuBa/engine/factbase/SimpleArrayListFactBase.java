/*    */ package tyRuBa.engine.factbase;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBComponent;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.Multiplicity;
/*    */ import tyRuBa.modes.PredInfo;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ import tyRuBa.util.Action;
/*    */ import tyRuBa.util.ArrayListSource;
/*    */ import tyRuBa.util.ElementSource;
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
/* 66 */       new SemiDetCompiled(mode.getMode())
/*    */       {
/*    */         public Frame runSemiDet(Object input, RBContext context) {
/* 69 */           RBTuple goal = (RBTuple)input;
/* 70 */           Frame result = null;
/* 71 */           for (Iterator iter = SimpleArrayListFactBase.this.facts.iterator(); (result == null) && (iter.hasNext());) {
/* 72 */             RBComponent fact = (RBComponent)iter.next();
/* 73 */             if (!fact.isValid()) {
/* 74 */               iter.remove();
/*    */             } else
/* 76 */               result = goal.unify(fact.getArgs(), new Frame());
/*    */           }
/* 78 */           return result;
/*    */         }
/*    */       };
/*    */     }
/*    */     
/* 83 */     new Compiled(mode.getMode())
/*    */     {
/*    */       public ElementSource runNonDet(Object input, RBContext context) {
/* 86 */         final RBTuple goal = (RBTuple)input;
/* 87 */         new ArrayListSource(SimpleArrayListFactBase.this.facts).map(new Action()
/*    */         {
/*    */           public Object compute(Object arg) {
/* 90 */             RBComponent fact = (RBComponent)arg;
/* 91 */             if (!fact.isValid())
/* 92 */               return null;
/* 93 */             return goal.unify(fact.getArgs(), new Frame());
/*    */           }
/*    */         });
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   public void backup() {}
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/SimpleArrayListFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */