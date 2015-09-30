/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.CompositeType;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.TupleType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ModedRuleBaseIndex
/*     */   implements PredInfoProvider
/*     */ {
/*     */   public abstract void enableMetaData();
/*     */   
/*     */   protected abstract ModedRuleBaseCollection getModedRuleBases(PredicateIdentifier paramPredicateIdentifier)
/*     */     throws TypeModeError;
/*     */   
/*     */   public RuleBase getBest(PredicateIdentifier predID, BindingList bindings)
/*     */   {
/*     */     try
/*     */     {
/*  40 */       return getModedRuleBases(predID).getBest(bindings);
/*     */     } catch (TypeModeError e) {
/*  42 */       throw new Error("this should never happen");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void insert(PredInfo paramPredInfo)
/*     */     throws TypeModeError;
/*     */   
/*     */ 
/*     */   public final void insert(RBComponent r)
/*     */     throws TypeModeError
/*     */   {
/*  54 */     RBComponent converted = r.convertToNormalForm();
/*  55 */     TupleType resultTypes = converted.typecheck(this);
/*  56 */     ModedRuleBaseCollection rulebases = getModedRuleBases(converted.getPredId());
/*  57 */     rulebases.insertInEach(converted, this, resultTypes);
/*     */   }
/*     */   
/*     */   public abstract void dumpFacts(PrintStream paramPrintStream);
/*     */   
/*     */   public CompositeType addType(CompositeType type) throws TypeModeError {
/*  63 */     TypeConstructor newTypeConst = type.getTypeConstructor();
/*  64 */     TypeConstructor oldTypeConst = 
/*  65 */       findTypeConst(newTypeConst.getName(), newTypeConst.getTypeArity());
/*  66 */     if (oldTypeConst != null) {
/*  67 */       if (oldTypeConst.isInitialized()) {
/*  68 */         throw new TypeModeError("Duplicate declaration for type " + type);
/*     */       }
/*  70 */       oldTypeConst.setParameter(type.getArgs());
/*  71 */       return (CompositeType)oldTypeConst.apply(type.getArgs(), false);
/*     */     }
/*     */     
/*  74 */     newTypeConst.setParameter(type.getArgs());
/*  75 */     basicAddTypeConst(newTypeConst);
/*  76 */     return type; }
/*     */   
/*     */   public abstract void addFunctorConst(Type paramType, CompositeType paramCompositeType);
/*     */   
/*     */   public abstract ConstructorType findConstructorType(FunctorIdentifier paramFunctorIdentifier);
/*     */   
/*     */   public abstract TypeConstructor findType(String paramString);
/*     */   
/*     */   public abstract TypeConstructor findTypeConst(String paramString, int paramInt);
/*     */   
/*     */   protected abstract void basicAddTypeConst(TypeConstructor paramTypeConstructor);
/*     */   
/*     */   public abstract void addTypePredicate(TypeConstructor paramTypeConstructor, ArrayList paramArrayList);
/*     */   
/*  90 */   public final PredInfo getPredInfo(PredicateIdentifier predId) throws TypeModeError { PredInfo result = maybeGetPredInfo(predId);
/*  91 */     if (result == null) {
/*  92 */       if (predId.getArity() == 1) {
/*  93 */         TypeConstructor t = findType(predId.getName());
/*  94 */         if (t != null) {
/*  95 */           NativePredicate.defineTypeTest(this, predId, t);
/*  96 */           return maybeGetPredInfo(predId);
/*     */         }
/*  98 */       } else if (predId.getArity() == 2) {
/*  99 */         String name = predId.getName();
/* 100 */         if (name.startsWith("convertTo")) {
/* 101 */           TypeConstructor t = findType(name.substring("convertTo".length()));
/* 102 */           if (t != null) {
/* 103 */             NativePredicate.defineConvertTo(this, t);
/* 104 */             return maybeGetPredInfo(predId);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */       throw new TypeModeError("Unknown predicate " + predId);
/*     */     }
/* 117 */     return result;
/*     */   }
/*     */   
/*     */   public abstract void addTypeMapping(TypeMapping paramTypeMapping, FunctorIdentifier paramFunctorIdentifier)
/*     */     throws TypeModeError;
/*     */   
/*     */   public abstract TypeMapping findTypeMapping(Class paramClass);
/*     */   
/*     */   public abstract void backup();
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/ModedRuleBaseIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */