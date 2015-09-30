/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.ModedRuleBaseIndex;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBExpression;
/*     */ import tyRuBa.engine.RBPredicateExpression;
/*     */ import tyRuBa.engine.RuleBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Factory
/*     */ {
/*     */   public static Bound makeBound()
/*     */   {
/*  19 */     return Bound.the;
/*     */   }
/*     */   
/*     */   public static Free makeFree() {
/*  23 */     return Free.the;
/*     */   }
/*     */   
/*     */   public static BindingMode makePartiallyBound() {
/*  27 */     return PatiallyBound.the;
/*     */   }
/*     */   
/*     */   public static Type makeAtomicType(TypeConstructor typeConstructor) {
/*  31 */     return typeConstructor.apply(makeTupleType(), false);
/*     */   }
/*     */   
/*     */   public static Type makeStrictAtomicType(TypeConstructor typeConstructor) {
/*  35 */     return typeConstructor.applyStrict(makeTupleType(), false);
/*     */   }
/*     */   
/*     */   public static Type makeSubAtomicType(TypeConstructor typeConstructor) {
/*  39 */     return typeConstructor.apply(makeTupleType(), true);
/*     */   }
/*     */   
/*     */   public static TVar makeTVar(String name) {
/*  43 */     return new TVar(name);
/*     */   }
/*     */   
/*     */   public static PredicateMode makePredicateMode(BindingList paramModes, Mode mode)
/*     */   {
/*  48 */     return makePredicateMode(paramModes, mode, true);
/*     */   }
/*     */   
/*     */   public static PredicateMode makePredicateMode(BindingList paramModes, Mode mode, boolean toBeChecked)
/*     */   {
/*  53 */     if (paramModes.isAllBound()) {
/*  54 */       return new PredicateMode(paramModes, Mode.makeSemidet(), false);
/*     */     }
/*  56 */     return new PredicateMode(paramModes, mode, toBeChecked);
/*     */   }
/*     */   
/*     */   public static PredicateMode makeAllBoundMode(int size)
/*     */   {
/*  61 */     BindingList bounds = new BindingList();
/*  62 */     for (int i = 0; i < size; i++) {
/*  63 */       bounds.add(makeBound());
/*     */     }
/*  65 */     return new PredicateMode(bounds, Mode.makeSemidet(), false);
/*     */   }
/*     */   
/*     */   public static PredicateMode makePredicateMode(String paramModesString, String modeString)
/*     */   {
/*  70 */     BindingList paramModes = makeBindingList();
/*     */     
/*  72 */     for (int i = 0; i < paramModesString.length(); i++) {
/*  73 */       char currChar = paramModesString.charAt(i);
/*  74 */       if ((currChar == 'b') || (currChar == 'B')) {
/*  75 */         paramModes.add(makeBound());
/*  76 */       } else if ((currChar == 'f') || (currChar == 'F')) {
/*  77 */         paramModes.add(makeFree());
/*     */       }
/*     */       else {
/*  80 */         throw new Error("unknown binding mode " + currChar);
/*     */       }
/*     */     }
/*     */     
/*  84 */     Mode mode = Mode.convertFromString(modeString);
/*     */     
/*  86 */     return makePredicateMode(paramModes, mode);
/*     */   }
/*     */   
/*     */   public static TupleType makeTupleType() {
/*  90 */     return new TupleType();
/*     */   }
/*     */   
/*     */   public static TupleType makeTupleType(Type t)
/*     */   {
/*  95 */     return new TupleType(new Type[] { t });
/*     */   }
/*     */   
/*     */   public static TupleType makeTupleType(Type t1, Type t2) {
/*  99 */     return new TupleType(new Type[] { t1, t2 });
/*     */   }
/*     */   
/*     */   public static TupleType makeTupleType(Type t1, Type t2, Type t3) {
/* 103 */     return new TupleType(new Type[] { t1, t2, t3 });
/*     */   }
/*     */   
/*     */   public static TupleType makeTupleType(Type t1, Type t2, Type t3, Type t4) {
/* 107 */     return new TupleType(new Type[] { t1, t2, t3, t4 });
/*     */   }
/*     */   
/*     */   public static BindingList makeBindingList() {
/* 111 */     return new BindingList();
/*     */   }
/*     */   
/*     */   public static BindingList makeBindingList(BindingMode bm) {
/* 115 */     return new BindingList(bm);
/*     */   }
/*     */   
/*     */   public static BindingList makeBindingList(int repeat, BindingMode bm) {
/* 119 */     BindingList result = makeBindingList();
/* 120 */     for (int i = 0; i < repeat; i++) {
/* 121 */       result.add(bm);
/*     */     }
/* 123 */     return result;
/*     */   }
/*     */   
/*     */   public static PredInfo makePredInfo(QueryEngine engine, String predName, TupleType tList) {
/* 127 */     return new PredInfo(engine, predName, tList);
/*     */   }
/*     */   
/*     */   public static PredInfo makePredInfo(QueryEngine engine, String predName, TupleType tList, ArrayList predModes, boolean isPersistent)
/*     */   {
/* 132 */     if (predModes.isEmpty()) {
/* 133 */       predModes.add(makeAllBoundMode(tList.size()));
/*     */     }
/* 135 */     return new PredInfo(engine, predName, tList, predModes, isPersistent);
/*     */   }
/*     */   
/*     */   public static PredInfo makePredInfo(QueryEngine engine, String predName, TupleType tList, ArrayList predModes)
/*     */   {
/* 140 */     return makePredInfo(engine, predName, tList, predModes, false);
/*     */   }
/*     */   
/*     */   public static Type makeListType(Type et) {
/* 144 */     return new ListType(et);
/*     */   }
/*     */   
/* 147 */   public static ListType makeEmptyListType() { return new ListType(); }
/*     */   
/*     */   public static ModeCheckContext makeModeCheckContext(ModedRuleBaseIndex rulebases)
/*     */   {
/* 151 */     return new ModeCheckContext(new BindingEnv(), rulebases);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeEnv makeTypeEnv()
/*     */   {
/* 160 */     return new TypeEnv();
/*     */   }
/*     */   
/*     */   public static RBExpression makeModedExpression(RBExpression exp, Mode mode, ModeCheckContext context)
/*     */   {
/* 165 */     return exp.makeModed(mode, context);
/*     */   }
/*     */   
/*     */   public static RBExpression makeModedExpression(RBPredicateExpression exp, Mode resultMode, ModeCheckContext resultContext, RuleBase bestRuleBase) {
/* 169 */     return exp.makeModed(resultMode, resultContext, bestRuleBase);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TypeConstructor makeTypeConstructor(Class javaclass)
/*     */   {
/* 179 */     return new JavaTypeConstructor(javaclass);
/*     */   }
/*     */   
/*     */   public static TypeConstructor makeTypeConstructor(String name, int arity) {
/* 183 */     return new UserDefinedTypeConstructor(name, arity);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/Factory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */