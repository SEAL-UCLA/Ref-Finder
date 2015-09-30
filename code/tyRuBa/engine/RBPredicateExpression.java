/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import junit.framework.Assert;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.CompiledPredicateExpression;
/*     */ import tyRuBa.engine.compilation.SemiDetCompiledPredicateExpression;
/*     */ import tyRuBa.engine.visitor.ExpressionVisitor;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBPredicateExpression
/*     */   extends RBExpression
/*     */ {
/*     */   protected PredicateIdentifier pred;
/*     */   private RBTuple args;
/*  36 */   private RuleBase rules = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public RBPredicateExpression(String predName, ArrayList argTerms)
/*     */   {
/*  42 */     this(predName, RBTuple.make(argTerms));
/*     */   }
/*     */   
/*     */   public RBPredicateExpression withNewArgs(RBTuple newArgs) {
/*  46 */     return new RBPredicateExpression(this.pred, newArgs);
/*     */   }
/*     */   
/*     */   public RBPredicateExpression(String predName, RBTuple args) {
/*  50 */     this.pred = new PredicateIdentifier(predName, args.getNumSubterms());
/*  51 */     this.args = args;
/*     */   }
/*     */   
/*     */   public RBPredicateExpression(PredicateIdentifier pred, RBTuple args) {
/*  55 */     this.pred = pred;
/*  56 */     this.args = args;
/*     */   }
/*     */   
/*     */   RBPredicateExpression(String predName, RBTerm[] argTerms)
/*     */   {
/*  61 */     this(predName, RBTuple.make(argTerms));
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  65 */     Assert.assertNotNull("Must be mode checked first!", getMode());
/*  66 */     if (getMode().hi.compareTo(Multiplicity.one) <= 0) {
/*  67 */       return new SemiDetCompiledPredicateExpression(getMode(), this.rules, getArgs());
/*     */     }
/*     */     
/*  70 */     return new CompiledPredicateExpression(getMode(), this.rules, getArgs());
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId()
/*     */   {
/*  75 */     return this.pred;
/*     */   }
/*     */   
/*     */   public String getPredName() {
/*  79 */     return this.pred.name;
/*     */   }
/*     */   
/*     */   public RBTuple getArgs() {
/*  83 */     return this.args;
/*     */   }
/*     */   
/*     */   public int getNumArgs() {
/*  87 */     return this.args.getNumSubterms();
/*     */   }
/*     */   
/*     */   public RBTerm getArgAt(int pos) {
/*  91 */     return this.args.getSubterm(pos);
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     try {
/*  96 */       RBPredicateExpression cl = (RBPredicateExpression)super.clone();
/*  97 */       cl.args = ((RBTuple)this.args.clone());
/*  98 */       return cl;
/*     */     } catch (CloneNotSupportedException e) {
/* 100 */       e.printStackTrace();
/* 101 */       throw new Error("This should not happen");
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 106 */     StringBuffer result = new StringBuffer(getPredName());
/* 107 */     int numSubterms = this.args.getNumSubterms();
/* 108 */     result.append("(");
/* 109 */     for (int i = 0; i < numSubterms; i++) {
/* 110 */       if (i > 0) result.append(",");
/* 111 */       result.append(this.args.getSubterm(i));
/*     */     }
/* 113 */     result.append(")");
/*     */     
/* 115 */     if (getMode() != null) {
/* 116 */       result.append(" {");
/* 117 */       if (this.rules == null) {
/* 118 */         result.append("MODE ERROR");
/*     */       } else {
/* 120 */         result.append(this.rules.getPredMode());
/*     */       }
/* 122 */       result.append("}");
/*     */     }
/* 124 */     return result.toString();
/*     */   }
/*     */   
/*     */   public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv) throws TypeModeError
/*     */   {
/*     */     try {
/* 130 */       TypeEnv myEnv = Factory.makeTypeEnv();
/* 131 */       PredicateIdentifier pred = getPredId();
/* 132 */       PredInfo pInfo = predinfo.getPredInfo(pred);
/* 133 */       if (pInfo == null) {
/* 134 */         throw new TypeModeError("Unknown predicate " + pred);
/*     */       }
/* 136 */       TupleType predType = pInfo.getTypeList();
/*     */       
/* 138 */       for (int i = 0; i < getNumArgs(); i++) {
/* 139 */         Type argType = getArgAt(i).getType(myEnv);
/* 140 */         argType.checkEqualTypes(predType.get(i));
/*     */       }
/* 142 */       return startEnv.intersect(myEnv);
/*     */     }
/*     */     catch (TypeModeError e) {
/* 145 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBExpression convertToMode(ModeCheckContext context, boolean rearrange) throws TypeModeError
/*     */   {
/* 151 */     ModeCheckContext resultContext = (ModeCheckContext)context.clone();
/* 152 */     BindingList bindings = Factory.makeBindingList();
/* 153 */     RuleBase bestRuleBase = 
/* 154 */       resultContext.getBestRuleBase(getPredId(), getArgs(), bindings);
/* 155 */     if (bestRuleBase == null) {
/* 156 */       return Factory.makeModedExpression(
/* 157 */         this, 
/* 158 */         new ErrorMode("there is no rulebase that allows " + getPredName() + 
/* 159 */         bindings), 
/* 160 */         resultContext);
/*     */     }
/* 162 */     Mode resultMode = bestRuleBase.getMode();
/* 163 */     Collection vars = getVariables();
/* 164 */     resultContext.removeAllBound(vars);
/* 165 */     if (vars.isEmpty())
/*     */     {
/*     */ 
/*     */ 
/* 169 */       resultMode = Mode.makeSemidet();
/*     */     } else {
/* 171 */       resultContext.bindVars(vars);
/* 172 */       resultMode.setPercentFree(bindings);
/*     */     }
/*     */     
/* 175 */     return Factory.makeModedExpression(this, resultMode, resultContext, bestRuleBase);
/*     */   }
/*     */   
/*     */   public Object accept(ExpressionVisitor v)
/*     */   {
/* 180 */     return v.visit(this);
/*     */   }
/*     */   
/*     */   public RBExpression makeModed(Mode resultMode, ModeCheckContext resultContext, RuleBase bestRuleBase) {
/* 184 */     RBPredicateExpression modedExp = (RBPredicateExpression)makeModed(resultMode, resultContext);
/* 185 */     modedExp.setRuleBase(bestRuleBase);
/* 186 */     return modedExp;
/*     */   }
/*     */   
/*     */   private void setRuleBase(RuleBase bestRuleBase) {
/* 190 */     Assert.assertNull(this.rules);
/* 191 */     this.rules = bestRuleBase;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBPredicateExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */