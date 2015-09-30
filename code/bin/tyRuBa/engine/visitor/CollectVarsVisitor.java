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
/*    */ 
/*    */ 
/*    */ public class CollectVarsVisitor
/*    */   extends AbstractCollectVarsVisitor
/*    */ {
/*    */   public CollectVarsVisitor(Collection vars)
/*    */   {
/* 24 */     super(vars, null);
/*    */   }
/*    */   
/*    */   public CollectVarsVisitor() {
/* 28 */     super(new HashSet(), null);
/*    */   }
/*    */   
/*    */   public Object visit(RBDisjunction disjunction) {
/* 32 */     Collection oldVars = getVars();
/* 33 */     Collection intersection = null;
/* 34 */     for (int i = 0; i < disjunction.getNumSubexps(); i++) {
/* 35 */       Collection next = disjunction.getSubexp(i).getVariables();
/* 36 */       if (intersection == null) {
/* 37 */         intersection = next;
/*    */       } else
/* 39 */         intersection.retainAll(next);
/*    */     }
/* 41 */     if (intersection != null) {
/* 42 */       oldVars.addAll(intersection);
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBExistsQuantifier exists) {
/* 48 */     return exists.getExp().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBFindAll findAll) {
/* 52 */     return findAll.getResult().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBCountAll count) {
/* 56 */     return count.getResult().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBNotFilter notFilter) {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBTestFilter testFilter) {
/* 64 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBUniqueQuantifier unique) {
/* 68 */     return unique.getExp().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBVariable var) {
/* 72 */     getVars().add(var);
/* 73 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBIgnoredVariable ignoredVar) {
/* 77 */     getVars().add(ignoredVar);
/* 78 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBTemplateVar templVar) {
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/visitor/CollectVarsVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */