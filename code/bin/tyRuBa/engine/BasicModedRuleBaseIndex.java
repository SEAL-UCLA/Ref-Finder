/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import tyRuBa.modes.CompositeType;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Free;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeMapping;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicModedRuleBaseIndex
/*     */   extends ModedRuleBaseIndex
/*     */ {
/*     */   QueryEngine engine;
/*     */   String identifier;
/*     */   TypeInfoBase typeInfoBase;
/*  35 */   HashMap index = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public void enableMetaData()
/*     */   {
/*  41 */     this.typeInfoBase.enableMetaData(this.engine);
/*     */   }
/*     */   
/*     */   public BasicModedRuleBaseIndex(QueryEngine qe, String identifier) {
/*  45 */     this.engine = qe;
/*  46 */     this.identifier = identifier;
/*  47 */     this.typeInfoBase = new TypeInfoBase(identifier);
/*     */   }
/*     */   
/*     */   protected ModedRuleBaseCollection getModedRuleBases(PredicateIdentifier predID)
/*     */     throws TypeModeError
/*     */   {
/*  53 */     ModedRuleBaseCollection result = maybeGetModedRuleBases(predID);
/*  54 */     if (result == null) {
/*  55 */       throw new TypeModeError("Unknown predicate " + predID);
/*     */     }
/*  57 */     return result;
/*     */   }
/*     */   
/*     */   public ModedRuleBaseCollection maybeGetModedRuleBases(PredicateIdentifier predID)
/*     */   {
/*  62 */     PredInfo pInfo = this.typeInfoBase.maybeGetPredInfo(predID);
/*  63 */     if (pInfo == null) {
/*  64 */       return null;
/*     */     }
/*     */     
/*  67 */     int numPredicateMode = pInfo.getNumPredicateMode();
/*  68 */     if (numPredicateMode == 0) {
/*  69 */       throw new Error("there are no mode declarations for " + predID);
/*     */     }
/*  71 */     return (ModedRuleBaseCollection)this.index.get(predID);
/*     */   }
/*     */   
/*     */   public void dumpFacts(PrintStream out) {
/*  75 */     for (Iterator iter = this.index.values().iterator(); iter.hasNext();) {
/*  76 */       ModedRuleBaseCollection element = (ModedRuleBaseCollection)iter.next();
/*  77 */       element.dumpFacts(out);
/*     */     }
/*     */   }
/*     */   
/*     */   public void insert(PredInfo p) throws TypeModeError {
/*  82 */     this.typeInfoBase.insert(p);
/*  83 */     ModedRuleBaseCollection rulebases = new ModedRuleBaseCollection(this.engine, p, this.identifier);
/*  84 */     this.index.put(p.getPredId(), rulebases);
/*     */   }
/*     */   
/*     */   public void addTypePredicate(TypeConstructor TypeConstructor, ArrayList subtypes) {
/*  88 */     RBRule typeRule = null;
/*  89 */     PredicateIdentifier typePred = new PredicateIdentifier(TypeConstructor.getName(), 1);
/*  90 */     RBTuple args = new RBTuple(RBVariable.make("?arg"));
/*  91 */     RBDisjunction cond = new RBDisjunction();
/*  92 */     PredInfo typePredInfo = Factory.makePredInfo(this.engine, TypeConstructor.getName(), 
/*  93 */       Factory.makeTupleType(Factory.makeAtomicType(TypeConstructor)));
/*  94 */     typePredInfo.addPredicateMode(Factory.makeAllBoundMode(1));
/*  95 */     PredicateMode freeMode = Factory.makePredicateMode(
/*  96 */       Factory.makeBindingList(Free.the), Mode.makeNondet());
/*     */     
/*  98 */     boolean hasFreeMode = true;
/*  99 */     for (int i = 0; i < subtypes.size(); i++) {
/* 100 */       String currTypeConstructorName = ((TypeConstructor)subtypes.get(i)).getName();
/*     */       try {
/* 102 */         PredInfo currSubTypePredInfo = 
/* 103 */           getPredInfo(new PredicateIdentifier(currTypeConstructorName, 1));
/* 104 */         hasFreeMode = (hasFreeMode) && (currSubTypePredInfo.getNumPredicateMode() > 1);
/*     */       } catch (TypeModeError e) {
/* 106 */         throw new Error("This should not happen");
/*     */       }
/* 108 */       RBExpression currExp = new RBPredicateExpression(
/* 109 */         new PredicateIdentifier(currTypeConstructorName, 1), 
/* 110 */         new RBTuple(RBVariable.make("?arg")));
/* 111 */       cond.addSubexp(currExp);
/*     */     }
/* 113 */     if (hasFreeMode) {
/* 114 */       typePredInfo.addPredicateMode(freeMode);
/*     */     }
/*     */     
/* 117 */     if (subtypes.size() == 1) {
/* 118 */       typeRule = new RBRule(typePred, args, cond.getSubexp(0));
/* 119 */     } else if (subtypes.size() > 1) {
/* 120 */       typeRule = new RBRule(typePred, args, cond);
/*     */     }
/*     */     try
/*     */     {
/* 124 */       insert(typePredInfo);
/* 125 */       if (typeRule != null) {
/* 126 */         insert(typeRule);
/*     */       }
/*     */     } catch (TypeModeError e) {
/* 129 */       throw new Error("This should not happen", e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void basicAddTypeConst(TypeConstructor t)
/*     */   {
/* 135 */     this.typeInfoBase.addTypeConst(t);
/*     */   }
/*     */   
/*     */   public void addFunctorConst(Type repAs, CompositeType type) {
/* 139 */     this.typeInfoBase.addFunctorConst(repAs, type);
/*     */   }
/*     */   
/*     */   public void addTypeMapping(TypeMapping mapping, FunctorIdentifier id) throws TypeModeError {
/* 143 */     this.typeInfoBase.addTypeMapping(id, mapping);
/*     */   }
/*     */   
/*     */   public TypeMapping findTypeMapping(Class forWhat) {
/* 147 */     return this.typeInfoBase.findTypeMapping(forWhat);
/*     */   }
/*     */   
/*     */   public PredInfo maybeGetPredInfo(PredicateIdentifier predId) {
/* 151 */     return this.typeInfoBase.maybeGetPredInfo(predId);
/*     */   }
/*     */   
/*     */   public boolean contains(PredicateIdentifier p) {
/* 155 */     PredInfo result = this.typeInfoBase.maybeGetPredInfo(p);
/* 156 */     return result != null;
/*     */   }
/*     */   
/*     */   public TypeConstructor findType(String typeName) {
/* 160 */     return this.typeInfoBase.findType(typeName);
/*     */   }
/*     */   
/*     */   public TypeConstructor findTypeConst(String typeName, int arity) {
/* 164 */     return this.typeInfoBase.findTypeConst(typeName, arity);
/*     */   }
/*     */   
/*     */   public ConstructorType findConstructorType(FunctorIdentifier id) {
/* 168 */     return this.typeInfoBase.findConstructorType(id);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 172 */     this.typeInfoBase.clear();
/* 173 */     this.index = new HashMap();
/*     */   }
/*     */   
/*     */   public void backup() {
/* 177 */     for (Iterator iter = this.index.values().iterator(); iter.hasNext();) {
/* 178 */       ModedRuleBaseCollection coll = (ModedRuleBaseCollection)iter.next();
/* 179 */       coll.backup();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 184 */     return getClass().getName() + "(" + this.identifier + ")";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/BasicModedRuleBaseIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */