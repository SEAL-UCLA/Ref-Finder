/*    */ package tyRuBa.engine.visitor;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.engine.RBTemplateVar;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBUniqueQuantifier;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ 
/*    */ 
/*    */ public class SubstituteVisitor
/*    */   extends SubstituteOrInstantiateVisitor
/*    */ {
/*    */   public SubstituteVisitor(Frame frame)
/*    */   {
/* 16 */     super(frame);
/*    */   }
/*    */   
/*    */   public Object visit(RBUniqueQuantifier unique) {
/* 20 */     Frame f = getFrame();
/* 21 */     for (int i = 0; i < unique.getNumVars(); i++) {
/* 22 */       f.remove(unique.getVarAt(i));
/*    */     }
/* 24 */     RBExpression exp = (RBExpression)unique.getExp().accept(this);
/* 25 */     return new RBUniqueQuantifier(unique.getQuantifiedVars(), exp);
/*    */   }
/*    */   
/*    */   public Object visit(RBVariable var) {
/* 29 */     RBTerm val = getFrame().get(var);
/* 30 */     if (val == null) {
/* 31 */       return var;
/*    */     }
/* 33 */     return val.accept(this);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object visit(RBTemplateVar var)
/*    */   {
/* 39 */     RBTerm val = getFrame().get(var);
/* 40 */     if (val == null) {
/* 41 */       return var;
/*    */     }
/* 43 */     return val.accept(this);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/SubstituteVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */