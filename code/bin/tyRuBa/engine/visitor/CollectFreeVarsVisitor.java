/*    */ package tyRuBa.engine.visitor;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import tyRuBa.engine.RBCountAll;
/*    */ import tyRuBa.engine.RBDisjunction;
/*    */ import tyRuBa.engine.RBExistsQuantifier;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.engine.RBFindAll;
/*    */ import tyRuBa.engine.RBIgnoredVariable;
/*    */ import tyRuBa.engine.RBNotFilter;
/*    */ import tyRuBa.engine.RBTemplateVar;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTestFilter;
/*    */ import tyRuBa.engine.RBUniqueQuantifier;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ import tyRuBa.modes.BindingMode;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CollectFreeVarsVisitor
/*    */   extends AbstractCollectVarsVisitor
/*    */ {
/*    */   public CollectFreeVarsVisitor(ModeCheckContext context)
/*    */   {
/* 28 */     super(new HashSet(), context);
/*    */   }
/*    */   
/*    */   public Object visit(RBDisjunction disjunction) {
/* 32 */     for (int i = 0; i < disjunction.getNumSubexps(); i++) {
/* 33 */       disjunction.getSubexp(i).accept(this);
/*    */     }
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBExistsQuantifier exists) {
/* 39 */     exists.getExp().accept(this);
/* 40 */     for (int i = 0; i < exists.getNumVars(); i++) {
/* 41 */       this.vars.remove(exists.getVarAt(i));
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBFindAll findAll) {
/* 47 */     findAll.getQuery().accept(this);
/* 48 */     findAll.getResult().accept(this);
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBCountAll count) {
/* 53 */     count.getQuery().accept(this);
/* 54 */     count.getResult().accept(this);
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBNotFilter notFilter) {
/* 59 */     return notFilter.getNegatedQuery().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBTestFilter testFilter) {
/* 63 */     return testFilter.getQuery().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBUniqueQuantifier unique) {
/* 67 */     unique.getExp().accept(this);
/* 68 */     for (int i = 0; i < unique.getNumVars(); i++) {
/* 69 */       this.vars.remove(unique.getVarAt(i));
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBVariable var) {
/* 75 */     if (!var.getBindingMode(this.context).isBound()) {
/* 76 */       this.vars.add(var);
/*    */     }
/* 78 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBIgnoredVariable ignoredVar) {
/* 82 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBTemplateVar ignoredVar) {
/* 86 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/visitor/CollectFreeVarsVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */