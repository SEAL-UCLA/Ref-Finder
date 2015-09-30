/*    */ package tyRuBa.engine.visitor;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.engine.RBTemplateVar;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBUniqueQuantifier;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstantiateVisitor
/*    */   extends SubstituteOrInstantiateVisitor
/*    */ {
/*    */   public InstantiateVisitor(Frame frame)
/*    */   {
/* 19 */     super(frame);
/*    */   }
/*    */   
/*    */   public Object visit(RBUniqueQuantifier unique) {
/* 23 */     RBExpression exp = (RBExpression)unique.getExp().accept(this);
/* 24 */     Collection vars = new HashSet();
/* 25 */     for (int i = 0; i < unique.getNumVars(); i++) {
/* 26 */       vars.add(unique.getVarAt(i).accept(this));
/*    */     }
/* 28 */     return new RBUniqueQuantifier(vars, exp);
/*    */   }
/*    */   
/*    */   public Object visit(RBVariable var) {
/* 32 */     RBTerm val = getFrame().get(var);
/* 33 */     if (val == null) {
/* 34 */       val = (RBVariable)var.clone();
/* 35 */       getFrame().put(var, val);
/* 36 */       return val;
/*    */     }
/* 38 */     return val;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object visit(RBTemplateVar templVar)
/*    */   {
/* 45 */     throw new Error("Unsupported operation");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/visitor/InstantiateVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */