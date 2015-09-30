/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.modes.CompositeType;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.PredInfo;
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
/*     */ public class BucketModedRuleBaseIndex
/*     */   extends ModedRuleBaseIndex
/*     */ {
/*     */   BasicModedRuleBaseIndex localRuleBase;
/*     */   BasicModedRuleBaseIndex globalRuleBase;
/*     */   
/*     */   public void enableMetaData()
/*     */   {
/*  30 */     this.localRuleBase.enableMetaData();
/*  31 */     this.globalRuleBase.enableMetaData();
/*     */   }
/*     */   
/*     */   public BucketModedRuleBaseIndex(QueryEngine qe, String identifier, BasicModedRuleBaseIndex globalRuleBase) {
/*  35 */     this.localRuleBase = new BasicModedRuleBaseIndex(qe, identifier);
/*  36 */     this.globalRuleBase = globalRuleBase;
/*     */   }
/*     */   
/*     */   protected ModedRuleBaseCollection getModedRuleBases(PredicateIdentifier predID) throws TypeModeError
/*     */   {
/*  41 */     ModedRuleBaseCollection result = this.localRuleBase.maybeGetModedRuleBases(predID);
/*  42 */     if (result == null) {
/*  43 */       return this.globalRuleBase.getModedRuleBases(predID);
/*     */     }
/*  45 */     return result;
/*     */   }
/*     */   
/*     */   public void insert(PredInfo p) throws TypeModeError
/*     */   {
/*  50 */     if (this.globalRuleBase.contains(p.getPredId()))
/*  51 */       throw new TypeModeError("Duplicate mode/type entries for predicate " + 
/*  52 */         p.getPredId());
/*  53 */     this.localRuleBase.insert(p);
/*     */   }
/*     */   
/*     */   public void dumpFacts(PrintStream out) {
/*  57 */     out.print("local facts: ");
/*  58 */     this.localRuleBase.dumpFacts(out);
/*  59 */     out.print("global facts: ");
/*  60 */     this.globalRuleBase.dumpFacts(out);
/*     */   }
/*     */   
/*     */   public PredInfo maybeGetPredInfo(PredicateIdentifier predId) {
/*  64 */     PredInfo result = this.localRuleBase.maybeGetPredInfo(predId);
/*  65 */     if (result == null) {
/*  66 */       return this.globalRuleBase.maybeGetPredInfo(predId);
/*     */     }
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   public void addTypePredicate(TypeConstructor TypeConstructor, ArrayList subtypes)
/*     */   {
/*  73 */     this.localRuleBase.addTypePredicate(TypeConstructor, subtypes);
/*     */   }
/*     */   
/*     */   protected void basicAddTypeConst(TypeConstructor t) {
/*  77 */     this.localRuleBase.basicAddTypeConst(t);
/*     */   }
/*     */   
/*     */   public void addFunctorConst(Type repAs, CompositeType type) {
/*  81 */     this.localRuleBase.addFunctorConst(repAs, type);
/*     */   }
/*     */   
/*     */   public void addTypeMapping(TypeMapping mapping, FunctorIdentifier id) throws TypeModeError {
/*  85 */     this.localRuleBase.addTypeMapping(mapping, id);
/*     */   }
/*     */   
/*     */   public TypeConstructor findType(String typeName)
/*     */   {
/*  90 */     TypeConstructor result = this.localRuleBase.findType(typeName);
/*  91 */     if (result == null) {
/*  92 */       result = this.globalRuleBase.findType(typeName);
/*     */     }
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   public TypeConstructor findTypeConst(String typeName, int arity)
/*     */   {
/*  99 */     TypeConstructor result = this.localRuleBase.typeInfoBase.findTypeConst(typeName, arity);
/* 100 */     if (result != null) {
/* 101 */       return result;
/*     */     }
/* 103 */     return this.globalRuleBase.typeInfoBase.findTypeConst(typeName, arity);
/*     */   }
/*     */   
/*     */ 
/*     */   public ConstructorType findConstructorType(FunctorIdentifier id)
/*     */   {
/* 109 */     ConstructorType result = this.localRuleBase.typeInfoBase.findConstructorType(id);
/* 110 */     if (result != null) {
/* 111 */       return result;
/*     */     }
/* 113 */     return this.globalRuleBase.typeInfoBase.findConstructorType(id);
/*     */   }
/*     */   
/*     */ 
/*     */   public TypeMapping findTypeMapping(Class forWhat)
/*     */   {
/* 119 */     TypeMapping result = this.localRuleBase.typeInfoBase.findTypeMapping(forWhat);
/* 120 */     if (result != null) {
/* 121 */       return result;
/*     */     }
/* 123 */     return this.globalRuleBase.typeInfoBase.findTypeMapping(forWhat);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 128 */     this.localRuleBase.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void backup()
/*     */   {
/* 135 */     this.globalRuleBase.backup();
/* 136 */     this.localRuleBase.backup();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/BucketModedRuleBaseIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */