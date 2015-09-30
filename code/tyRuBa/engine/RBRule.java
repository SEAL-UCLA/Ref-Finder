/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ErrorMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TVar;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ public class RBRule extends RBComponent implements Cloneable
/*     */ {
/*     */   private PredicateIdentifier pred;
/*     */   private RBTuple args;
/*     */   private RBExpression cond;
/*  29 */   Mode mode = null;
/*     */   
/*     */   public Mode getMode() {
/*  32 */     return this.mode;
/*     */   }
/*     */   
/*     */   public RBRule(RBPredicateExpression pred, RBExpression exp) {
/*  36 */     this.pred = pred.getPredId();
/*  37 */     this.args = pred.getArgs();
/*  38 */     this.cond = exp;
/*     */   }
/*     */   
/*     */   RBRule(PredicateIdentifier pred, RBTuple args, RBExpression cond) {
/*  42 */     this.pred = pred;
/*  43 */     this.args = args;
/*  44 */     this.cond = cond;
/*     */   }
/*     */   
/*     */   RBRule(PredicateIdentifier pred, RBTuple args, RBExpression cond, Mode resultMode) {
/*  48 */     this(pred, args, cond);
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId()
/*     */   {
/*  53 */     return this.pred;
/*     */   }
/*     */   
/*     */   public RBTuple getArgs() {
/*  57 */     return this.args;
/*     */   }
/*     */   
/*     */   public final RBExpression getCondition() {
/*  61 */     return this.cond;
/*     */   }
/*     */   
/*     */   public RBComponent addCondition(RBExpression e) {
/*  65 */     return new RBRule(this.pred, this.args, FrontEnd.makeAnd(e, this.cond));
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     try {
/*  70 */       return (RBRule)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  73 */       throw new Error("This shouldn't happen!");
/*     */     }
/*     */   }
/*     */   
/*     */   public RBRule substitute(Frame frame) {
/*  78 */     RBRule r = (RBRule)clone();
/*  79 */     r.args = ((RBTuple)this.args.substitute(frame));
/*  80 */     r.cond = this.cond.substitute(frame);
/*  81 */     return r;
/*     */   }
/*     */   
/*     */   public String conclusionToString() {
/*  85 */     return getPredName() + getArgs();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  89 */     StringBuffer result = new StringBuffer(conclusionToString());
/*  90 */     if (this.cond != null) {
/*  91 */       result.append(" :- " + this.cond);
/*     */     }
/*  93 */     result.append(".");
/*  94 */     return result.toString();
/*     */   }
/*     */   
/*     */   public TupleType typecheck(PredInfoProvider predinfo) throws TypeModeError
/*     */   {
/*     */     try {
/* 100 */       TypeEnv startEnv = new TypeEnv();
/* 101 */       PredicateIdentifier pred = getPredId();
/* 102 */       PredInfo pInfo = predinfo.getPredInfo(pred);
/* 103 */       if (pInfo == null) {
/* 104 */         throw new TypeModeError("Unknown predicate " + pred);
/*     */       }
/* 106 */       TupleType predTypes = pInfo.getTypeList();
/* 107 */       RBTuple args = getArgs();
/* 108 */       int numArgs = args.getNumSubterms();
/* 109 */       TupleType startArgTypes = Factory.makeTupleType();
/* 110 */       for (int i = 0; i < numArgs; i++) {
/* 111 */         Type currStrictPart = predTypes.get(i).copyStrictPart();
/* 112 */         Type argType = args.getSubterm(i).getType(startEnv);
/* 113 */         if (!(currStrictPart instanceof TVar)) {
/* 114 */           argType.checkEqualTypes(currStrictPart, false);
/*     */         }
/* 116 */         startArgTypes.add(argType);
/*     */       }
/*     */       
/* 119 */       TypeEnv inferredTypeEnv = getCondition().typecheck(predinfo, startEnv);
/*     */       
/* 121 */       TupleType argTypes = Factory.makeTupleType();
/* 122 */       java.util.Map varRenamings = new HashMap();
/* 123 */       for (int i = 0; i < numArgs; i++) {
/* 124 */         argTypes.add(args.getSubterm(i).getType(inferredTypeEnv));
/*     */       }
/* 126 */       if (!argTypes.isSubTypeOf(predTypes, varRenamings)) {
/* 127 */         throw new TypeModeError("Inferred types " + 
/* 128 */           argTypes + " incompatible with declared types " + predTypes);
/*     */       }
/* 130 */       return argTypes;
/*     */     }
/*     */     catch (TypeModeError e) {
/* 133 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBComponent convertToNormalForm() {
/* 138 */     return new RBRule(this.pred, this.args, this.cond.convertToNormalForm());
/*     */   }
/*     */   
/*     */   public RBComponent convertToMode(PredicateMode predMode, ModeCheckContext context) throws TypeModeError
/*     */   {
/* 143 */     BindingList paramModes = predMode.getParamModes();
/* 144 */     boolean toBeCheck = predMode.toBeCheck();
/* 145 */     int numArgs = this.args.getNumSubterms();
/*     */     
/* 147 */     for (int i = 0; i < numArgs; i++) {
/* 148 */       RBTerm currArg = this.args.getSubterm(i);
/* 149 */       if (paramModes.get(i).isBound()) {
/* 150 */         currArg.makeAllBound(context);
/*     */       }
/*     */     }
/*     */     
/* 154 */     RBExpression converted = this.cond.convertToMode(context);
/*     */     
/* 156 */     if ((converted.getMode() instanceof ErrorMode)) {
/* 157 */       throw new TypeModeError("cannot convert " + conclusionToString() + 
/* 158 */         ":-" + converted + 
/* 159 */         " to any legal mode\n" + 
/* 160 */         "    " + converted.getMode());
/*     */     }
/* 162 */     Mode resultMode = converted.getMode();
/* 163 */     Collection vars = this.args.getVariables();
/* 164 */     context.removeAllBound(vars);
/* 165 */     if (vars.isEmpty()) {
/* 166 */       resultMode = resultMode.first();
/* 167 */     } else if (toBeCheck) {
/* 168 */       if (!resultMode.compatibleWith(predMode.getMode())) {
/* 169 */         throw new TypeModeError("cannot convert " + conclusionToString() + 
/* 170 */           ":-" + converted + 
/* 171 */           " to the declared mode " + predMode.getMode() + ".\n" + 
/* 172 */           "inferred mode was " + converted.getMode());
/*     */       }
/* 174 */       vars = this.args.getVariables();
/* 175 */       converted.getNewContext().removeAllBound(vars);
/* 176 */       if (!vars.isEmpty()) {
/* 177 */         throw new TypeModeError("Variables " + vars + 
/* 178 */           " do not become bound in " + this);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 183 */       resultMode = resultMode.restrictedBy(predMode.getMode());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 188 */     RBRule convertedRule = new RBRule(this.pred, this.args, converted, resultMode);
/*     */     
/* 190 */     if (!RuleBase.silent) {
/* 191 */       System.err.println(predMode + " ==> " + convertedRule);
/*     */     }
/* 193 */     return convertedRule;
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c)
/*     */   {
/* 198 */     Compiled compiledCond = this.cond.compile(c);
/* 199 */     if (compiledCond.getMode().hi.compareTo(this.mode.hi) > 0) {
/* 200 */       compiledCond = compiledCond.first();
/*     */     }
/* 202 */     return tyRuBa.engine.compilation.CompiledRule.make(this, this.args, compiledCond);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */