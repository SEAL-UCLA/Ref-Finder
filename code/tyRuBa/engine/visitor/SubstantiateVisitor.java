/*    */ package tyRuBa.engine.visitor;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBCompoundTerm;
/*    */ import tyRuBa.engine.RBPair;
/*    */ import tyRuBa.engine.RBQuoted;
/*    */ import tyRuBa.engine.RBTemplateVar;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ import tyRuBa.modes.ConstructorType;
/*    */ 
/*    */ public class SubstantiateVisitor implements TermVisitor
/*    */ {
/*    */   Frame subst;
/*    */   Frame inst;
/*    */   
/*    */   public SubstantiateVisitor(Frame subst, Frame inst)
/*    */   {
/* 20 */     this.subst = subst;
/* 21 */     this.inst = inst;
/*    */   }
/*    */   
/*    */   public Object visit(RBCompoundTerm compoundTerm) {
/* 25 */     ConstructorType typeConst = compoundTerm.getConstructorType();
/* 26 */     return typeConst.apply(
/* 27 */       (RBTerm)compoundTerm.getArg().accept(this));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object visit(RBTuple tuple)
/*    */   {
/* 34 */     RBTerm[] subterms = new RBTerm[tuple.getNumSubterms()];
/* 35 */     for (int i = 0; i < subterms.length; i++) {
/* 36 */       subterms[i] = ((RBTerm)tuple.getSubterm(i).accept(this));
/*    */     }
/* 38 */     return RBTuple.make(subterms);
/*    */   }
/*    */   
/*    */   public Object visit(RBPair pair) {
/* 42 */     RBPair head = new RBPair((RBTerm)pair.getCar().accept(this));
/*    */     
/*    */ 
/* 45 */     RBPair prev = head;
/*    */     
/* 47 */     RBTerm cdr = pair.getCdr();
/*    */     
/* 49 */     while ((cdr instanceof RBPair)) {
/* 50 */       pair = (RBPair)cdr;
/* 51 */       RBPair next = new RBPair((RBTerm)pair.getCar().accept(this));
/* 52 */       prev.setCdr(next);
/* 53 */       prev = next;
/* 54 */       cdr = pair.getCdr();
/*    */     }
/*    */     
/* 57 */     prev.setCdr((RBTerm)cdr.accept(this));
/*    */     
/* 59 */     return head;
/*    */   }
/*    */   
/*    */   public Object visit(RBQuoted quoted) {
/* 63 */     return new RBQuoted(
/* 64 */       (RBTerm)quoted.getQuotedParts().accept(this));
/*    */   }
/*    */   
/*    */   public Object visit(RBVariable var) {
/* 68 */     RBTerm val = this.subst.get(var);
/* 69 */     if (val == null) {
/* 70 */       return (RBTerm)var.accept(new InstantiateVisitor(this.inst));
/*    */     }
/* 72 */     return (RBTerm)val.accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(tyRuBa.engine.RBIgnoredVariable ignoredVar)
/*    */   {
/* 77 */     return ignoredVar;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object visit(RBTemplateVar templVar)
/*    */   {
/* 83 */     throw new Error("Unsupported operation");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/SubstantiateVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */