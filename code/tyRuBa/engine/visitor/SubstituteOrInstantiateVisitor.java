/*     */ package tyRuBa.engine.visitor;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.RBCompoundTerm;
/*     */ import tyRuBa.engine.RBConjunction;
/*     */ import tyRuBa.engine.RBCountAll;
/*     */ import tyRuBa.engine.RBDisjunction;
/*     */ import tyRuBa.engine.RBExistsQuantifier;
/*     */ import tyRuBa.engine.RBExpression;
/*     */ import tyRuBa.engine.RBFindAll;
/*     */ import tyRuBa.engine.RBIgnoredVariable;
/*     */ import tyRuBa.engine.RBModeSwitchExpression;
/*     */ import tyRuBa.engine.RBNotFilter;
/*     */ import tyRuBa.engine.RBPair;
/*     */ import tyRuBa.engine.RBPredicateExpression;
/*     */ import tyRuBa.engine.RBQuoted;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBTestFilter;
/*     */ import tyRuBa.engine.RBTuple;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ 
/*     */ public abstract class SubstituteOrInstantiateVisitor implements ExpressionVisitor, TermVisitor
/*     */ {
/*     */   Frame frame;
/*     */   
/*     */   public SubstituteOrInstantiateVisitor(Frame frame)
/*     */   {
/*  30 */     this.frame = frame;
/*     */   }
/*     */   
/*     */   public Frame getFrame() {
/*  34 */     return this.frame;
/*     */   }
/*     */   
/*     */   public Object visit(RBConjunction conjunction) {
/*  38 */     RBConjunction result = new RBConjunction();
/*  39 */     for (int i = 0; i < conjunction.getNumSubexps(); i++) {
/*  40 */       result.addSubexp(
/*  41 */         (RBExpression)conjunction.getSubexp(i).accept(this));
/*     */     }
/*  43 */     return result;
/*     */   }
/*     */   
/*     */   public Object visit(RBDisjunction disjunction) {
/*  47 */     RBDisjunction result = new RBDisjunction();
/*  48 */     for (int i = 0; i < disjunction.getNumSubexps(); i++) {
/*  49 */       result.addSubexp(
/*  50 */         (RBExpression)disjunction.getSubexp(i).accept(this));
/*     */     }
/*  52 */     return result;
/*     */   }
/*     */   
/*     */   public Object visit(RBExistsQuantifier exists) {
/*  56 */     RBExpression exp = (RBExpression)exists.getExp().accept(this);
/*  57 */     Collection vars = new HashSet();
/*  58 */     for (int i = 0; i < exists.getNumVars(); i++) {
/*  59 */       vars.add(exists.getVarAt(i).accept(this));
/*     */     }
/*  61 */     return new RBExistsQuantifier(vars, exp);
/*     */   }
/*     */   
/*     */   public Object visit(RBFindAll findAll) {
/*  65 */     RBExpression query = (RBExpression)findAll.getQuery().accept(this);
/*  66 */     RBTerm extract = (RBTerm)findAll.getExtract().accept(this);
/*  67 */     RBTerm result = (RBTerm)findAll.getResult().accept(this);
/*  68 */     return new RBFindAll(query, extract, result);
/*     */   }
/*     */   
/*     */   public Object visit(RBCountAll count) {
/*  72 */     RBExpression query = (RBExpression)count.getQuery().accept(this);
/*  73 */     RBTerm extract = (RBTerm)count.getExtract().accept(this);
/*  74 */     RBTerm result = (RBTerm)count.getResult().accept(this);
/*  75 */     return new RBCountAll(query, extract, result);
/*     */   }
/*     */   
/*     */   public Object visit(RBModeSwitchExpression modeSwitch) {
/*  79 */     throw new Error("Should not happen: a mode case should have been selected before any substitution or instantiation is performed");
/*     */   }
/*     */   
/*     */   public Object visit(RBNotFilter notFilter)
/*     */   {
/*  84 */     RBExpression negatedQuery = 
/*  85 */       (RBExpression)notFilter.getNegatedQuery().accept(this);
/*  86 */     return new RBNotFilter(negatedQuery);
/*     */   }
/*     */   
/*     */   public Object visit(RBPredicateExpression predExp) {
/*  90 */     return predExp.withNewArgs((RBTuple)predExp.getArgs().accept(this));
/*     */   }
/*     */   
/*     */   public Object visit(RBTestFilter testFilter) {
/*  94 */     RBExpression testQuery = 
/*  95 */       (RBExpression)testFilter.getQuery().accept(this);
/*  96 */     return new RBTestFilter(testQuery);
/*     */   }
/*     */   
/*     */   public Object visit(RBCompoundTerm compoundTerm) {
/* 100 */     ConstructorType typeConst = compoundTerm.getConstructorType();
/* 101 */     return typeConst.apply(
/* 102 */       (RBTerm)compoundTerm.getArg().accept(this));
/*     */   }
/*     */   
/*     */ 
/*     */   public Object visit(RBTuple tuple)
/*     */   {
/* 108 */     RBTerm[] subterms = new RBTerm[tuple.getNumSubterms()];
/* 109 */     for (int i = 0; i < subterms.length; i++) {
/* 110 */       subterms[i] = ((RBTerm)tuple.getSubterm(i).accept(this));
/*     */     }
/* 112 */     return RBTuple.make(subterms);
/*     */   }
/*     */   
/*     */   public Object visit(RBIgnoredVariable ignoredVar) {
/* 116 */     return ignoredVar;
/*     */   }
/*     */   
/*     */   public Object visit(RBPair pair) {
/* 120 */     RBPair head = new RBPair((RBTerm)pair.getCar().accept(this));
/*     */     
/*     */ 
/* 123 */     RBPair prev = head;
/*     */     
/* 125 */     RBTerm cdr = pair.getCdr();
/*     */     
/* 127 */     while ((cdr instanceof RBPair)) {
/* 128 */       pair = (RBPair)cdr;
/* 129 */       RBPair next = new RBPair((RBTerm)pair.getCar().accept(this));
/* 130 */       prev.setCdr(next);
/* 131 */       prev = next;
/* 132 */       cdr = pair.getCdr();
/*     */     }
/*     */     
/* 135 */     prev.setCdr((RBTerm)cdr.accept(this));
/*     */     
/* 137 */     return head;
/*     */   }
/*     */   
/*     */   public Object visit(RBQuoted quoted) {
/* 141 */     return new RBQuoted(
/* 142 */       (RBTerm)quoted.getQuotedParts().accept(this));
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/SubstituteOrInstantiateVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */