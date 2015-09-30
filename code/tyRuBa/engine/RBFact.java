/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TVar;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ public class RBFact extends RBComponent implements Cloneable
/*     */ {
/*     */   private RBTuple args;
/*     */   private PredicateIdentifier pred;
/*     */   
/*     */   public RBFact(RBPredicateExpression e)
/*     */   {
/*  29 */     this.pred = e.getPredId();
/*  30 */     this.args = e.getArgs();
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId() {
/*  34 */     return this.pred;
/*     */   }
/*     */   
/*     */   public RBTuple getArgs() {
/*  38 */     return this.args;
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     try {
/*  43 */       RBFact cl = (RBFact)super.clone();
/*  44 */       cl.args = ((RBTuple)this.args.clone());
/*  45 */       return cl;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  47 */       throw new Error("This shouldn't happen!");
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/*  52 */     return getPredName() + this.args + ".";
/*     */   }
/*     */   
/*     */   public boolean isGround() {
/*  56 */     return this.args.isGround();
/*     */   }
/*     */   
/*     */   public boolean isGroundFact() {
/*  60 */     return isGround();
/*     */   }
/*     */   
/*     */   public TupleType typecheck(PredInfoProvider predinfo) throws TypeModeError {
/*     */     try {
/*  65 */       TypeEnv startEnv = new TypeEnv();
/*  66 */       PredicateIdentifier pred = getPredId();
/*  67 */       PredInfo pInfo = predinfo.getPredInfo(pred);
/*  68 */       TupleType predTypes = pInfo.getTypeList();
/*     */       
/*  70 */       RBTuple args = getArgs();
/*  71 */       int numArgs = args.getNumSubterms();
/*  72 */       TupleType startArgTypes = Factory.makeTupleType();
/*  73 */       for (int i = 0; i < numArgs; i++) {
/*  74 */         Type currStrictPart = predTypes.get(i).copyStrictPart();
/*  75 */         Type argType = args.getSubterm(i).getType(startEnv);
/*  76 */         if (!(currStrictPart instanceof TVar)) {
/*  77 */           argType = argType.intersect(currStrictPart);
/*     */         }
/*  79 */         startArgTypes.add(argType);
/*     */       }
/*     */       
/*  82 */       Map varRenamings = new HashMap();
/*  83 */       if (!startArgTypes.isSubTypeOf(predTypes, varRenamings)) {
/*  84 */         throw new TypeModeError("Inferred types " + 
/*  85 */           startArgTypes + " incompatible with declared types " + predTypes);
/*     */       }
/*  87 */       return startArgTypes;
/*     */     }
/*     */     catch (TypeModeError e) {
/*  90 */       throw new TypeModeError(e, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public RBComponent convertToMode(PredicateMode mode, ModeCheckContext context) throws TypeModeError
/*     */   {
/*  96 */     BindingList paramModes = mode.getParamModes();
/*     */     
/*  98 */     RBTuple args = getArgs();
/*  99 */     for (int i = 0; i < args.getNumSubterms(); i++) {
/* 100 */       if (paramModes.get(i).isBound()) {
/* 101 */         args.getSubterm(i).makeAllBound(context);
/*     */       }
/*     */     }
/*     */     
/* 105 */     Collection vars = args.getVariables();
/* 106 */     context.removeAllBound(vars);
/* 107 */     if (!vars.isEmpty()) {
/* 108 */       throw new TypeModeError("Variables: " + vars + 
/* 109 */         " do not become bound in " + this);
/*     */     }
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Mode getMode()
/*     */   {
/* 116 */     return Mode.makeSemidet();
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/* 120 */     return new tyRuBa.engine.compilation.CompiledFact(this.args);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBFact.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */