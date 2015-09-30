/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import tyRuBa.modes.CompositeType;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeMapping;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.modes.UserDefinedTypeConstructor;
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
/*     */ public class TypeInfoBase
/*     */   implements PredInfoProvider
/*     */ {
/*     */   MetaBase metaBase;
/*  31 */   HashMap predicateMap = new HashMap();
/*  32 */   HashMap typeConstructorMap = new HashMap();
/*  33 */   HashMap functorMap = new HashMap();
/*  34 */   HashMap toTyRuBaMappingMap = new HashMap();
/*     */   
/*     */   public TypeInfoBase(String identifier)
/*     */   {
/*  38 */     addTypeConst(TypeConstructor.theAny);
/*  39 */     this.metaBase = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableMetaData(QueryEngine qe)
/*     */   {
/*  49 */     this.metaBase = new MetaBase(qe);
/*     */     
/*     */ 
/*  52 */     for (Iterator iter = this.typeConstructorMap.values().iterator(); iter.hasNext();) {
/*  53 */       this.metaBase.assertTypeConstructor((TypeConstructor)iter.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public void insert(PredInfo pInfo) throws TypeModeError {
/*  58 */     PredInfo result = (PredInfo)this.predicateMap.get(pInfo.getPredId());
/*  59 */     if (result != null)
/*  60 */       throw new TypeModeError("Duplicate mode/type entries for predicate " + 
/*  61 */         pInfo.getPredId());
/*  62 */     this.predicateMap.put(pInfo.getPredId(), pInfo);
/*     */   }
/*     */   
/*     */   public PredInfo getPredInfo(PredicateIdentifier predId) throws TypeModeError {
/*  66 */     PredInfo result = maybeGetPredInfo(predId);
/*  67 */     if (result == null) {
/*  68 */       throw new TypeModeError("Unknown predicate " + predId);
/*     */     }
/*  70 */     return result;
/*     */   }
/*     */   
/*     */   public PredInfo maybeGetPredInfo(PredicateIdentifier predId)
/*     */   {
/*  75 */     return (PredInfo)this.predicateMap.get(predId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addTypeConst(TypeConstructor t)
/*     */   {
/*  82 */     this.typeConstructorMap.put(t.getName() + "/" + t.getTypeArity(), t);
/*  83 */     if (this.metaBase != null) this.metaBase.assertTypeConstructor(t);
/*     */   }
/*     */   
/*     */   public void addFunctorConst(Type repAs, CompositeType type) {
/*  87 */     TypeConstructor tc = type.getTypeConstructor();
/*  88 */     FunctorIdentifier functorId = tc.getFunctorId();
/*     */     
/*  90 */     ConstructorType constrType = ConstructorType.makeUserDefined(functorId, repAs, type);
/*  91 */     this.functorMap.put(functorId, constrType);
/*  92 */     tc.setConstructorType(constrType);
/*     */   }
/*     */   
/*     */   public void addTypeMapping(FunctorIdentifier id, TypeMapping mapping) throws TypeModeError {
/*  96 */     TypeConstructor tc = findTypeConst(id.getName(), id.getArity());
/*  97 */     tc.getConstructorType();
/*  98 */     if ((tc instanceof UserDefinedTypeConstructor)) {
/*  99 */       ((UserDefinedTypeConstructor)tc).setMapping(mapping);
/*     */     } else {
/* 101 */       throw new Error("The tyRuBa type " + id + " is not a mappable type. Only Userdefined types can be mapped.");
/*     */     }
/*     */     
/* 104 */     if (tc.hasRepresentation()) {
/* 105 */       ConstructorType ct = tc.getConstructorType();
/* 106 */       mapping.setFunctor(ct);
/*     */     }
/* 108 */     this.toTyRuBaMappingMap.put(mapping.getMappedClass(), mapping);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 112 */     StringBuffer result = new StringBuffer(
/* 113 */       "/******** predicate info ********/\n");
/* 114 */     Iterator itr = this.predicateMap.values().iterator();
/* 115 */     while (itr.hasNext()) {
/* 116 */       PredInfo element = (PredInfo)itr.next();
/* 117 */       result.append(element.toString());
/*     */     }
/* 119 */     result.append("/******** user defined types ****/\n");
/* 120 */     itr = this.typeConstructorMap.values().iterator();
/* 121 */     while (itr.hasNext()) {
/* 122 */       result.append(itr.next() + "\n");
/*     */     }
/* 124 */     result.append("/********************************/\n");
/* 125 */     return result.toString();
/*     */   }
/*     */   
/*     */   public TypeConstructor findType(String typeName) {
/* 129 */     if ((typeName.equals("String")) || 
/* 130 */       (typeName.equals("Integer")) || 
/* 131 */       (typeName.equals("Number")) || 
/* 132 */       (typeName.equals("Float")))
/*     */     {
/* 134 */       typeName = "java.lang." + typeName; }
/* 135 */     if (typeName.equals("RegExp"))
/* 136 */       typeName = "org.apache.regexp.RE";
/* 137 */     TypeConstructor result = (TypeConstructor)this.typeConstructorMap.get(typeName + "/0");
/* 138 */     if ((result == null) && 
/* 139 */       (typeName.indexOf('.') >= 0)) {
/*     */       try {
/* 141 */         Class cl = Class.forName(typeName);
/* 142 */         result = Factory.makeTypeConstructor(cl);
/* 143 */         addTypeConst(result);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   public TypeConstructor findTypeConst(String typeName, int arity) {
/* 155 */     TypeConstructor result = (TypeConstructor)this.typeConstructorMap.get(typeName + "/" + arity);
/* 156 */     if (result == null) {
/* 157 */       result = Factory.makeTypeConstructor(typeName, arity);
/* 158 */       addTypeConst(result);
/*     */     }
/* 160 */     return result;
/*     */   }
/*     */   
/*     */   public ConstructorType findConstructorType(FunctorIdentifier id) {
/* 164 */     return (ConstructorType)this.functorMap.get(id);
/*     */   }
/*     */   
/*     */   public TypeMapping findTypeMapping(Class cls) {
/* 168 */     return (TypeMapping)this.toTyRuBaMappingMap.get(cls);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 172 */     this.predicateMap = new HashMap();
/* 173 */     this.typeConstructorMap = new HashMap();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/TypeInfoBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */