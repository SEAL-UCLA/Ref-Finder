/*    */ package tyRuBa.engine.visitor;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import tyRuBa.engine.RBCompoundTerm;
/*    */ import tyRuBa.engine.RBConjunction;
/*    */ import tyRuBa.engine.RBModeSwitchExpression;
/*    */ import tyRuBa.engine.RBPair;
/*    */ import tyRuBa.engine.RBPredicateExpression;
/*    */ import tyRuBa.engine.RBQuoted;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ 
/*    */ public abstract class AbstractCollectVarsVisitor implements ExpressionVisitor, TermVisitor
/*    */ {
/*    */   Collection vars;
/*    */   protected ModeCheckContext context;
/*    */   
/*    */   public AbstractCollectVarsVisitor(Collection vars, ModeCheckContext context)
/*    */   {
/* 21 */     this.vars = vars;
/* 22 */     this.context = context;
/*    */   }
/*    */   
/*    */   public Collection getVars() {
/* 26 */     return this.vars;
/*    */   }
/*    */   
/*    */   public Object visit(RBConjunction conjunction) {
/* 30 */     for (int i = 0; i < conjunction.getNumSubexps(); i++) {
/* 31 */       conjunction.getSubexp(i).accept(this);
/*    */     }
/* 33 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBModeSwitchExpression modeSwitch) {
/* 37 */     for (int i = 0; i < modeSwitch.getNumModeCases(); i++) {
/* 38 */       modeSwitch.getModeCaseAt(i).getExp().accept(this);
/*    */     }
/* 40 */     if (modeSwitch.hasDefaultExp()) {
/* 41 */       modeSwitch.getDefaultExp().accept(this);
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBPredicateExpression predExp) {
/* 47 */     return predExp.getArgs().accept(this);
/*    */   }
/*    */   
/*    */   public Object visit(RBCompoundTerm compoundTerm) {
/* 51 */     compoundTerm.getArg().accept(this);
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBTuple tuple) {
/* 56 */     for (int i = 0; i < tuple.getNumSubterms(); i++) {
/* 57 */       tuple.getSubterm(i).accept(this);
/*    */     }
/* 59 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBPair pair) {
/* 63 */     pair.getCar().accept(this);
/*    */     
/* 65 */     RBTerm cdr = pair.getCdr();
/*    */     
/* 67 */     while ((cdr instanceof RBPair)) {
/* 68 */       pair = (RBPair)cdr;
/* 69 */       pair.getCar().accept(this);
/* 70 */       cdr = pair.getCdr();
/*    */     }
/*    */     
/* 73 */     cdr.accept(this);
/*    */     
/* 75 */     return null;
/*    */   }
/*    */   
/*    */   public Object visit(RBQuoted quoted) {
/* 79 */     return quoted.getQuotedParts().accept(this);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/AbstractCollectVarsVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */