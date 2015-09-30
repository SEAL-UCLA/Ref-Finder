/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import tyRuBa.engine.FunctorIdentifier;
/*     */ import tyRuBa.engine.MetaBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeConstructor
/*     */ {
/*  14 */   public static TypeConstructor theAny = Factory.makeTypeConstructor(Object.class);
/*     */   
/*     */   public Type apply(TupleType args, boolean growable) {
/*  17 */     if (growable) {
/*  18 */       return new GrowableType(new CompositeType(this, false, args));
/*     */     }
/*  20 */     return new CompositeType(this, false, args);
/*     */   }
/*     */   
/*     */   public Type applyStrict(TupleType args, boolean growable)
/*     */   {
/*  25 */     if (growable) {
/*  26 */       return new GrowableType(new CompositeType(this, true, args));
/*     */     }
/*  28 */     return new CompositeType(this, true, args);
/*     */   }
/*     */   
/*     */   public boolean isSuperTypeOf(TypeConstructor other)
/*     */   {
/*  33 */     if (equals(other)) {
/*  34 */       return true;
/*     */     }
/*  36 */     TypeConstructor superTypeConst = other.getSuperTypeConstructor();
/*     */     
/*  38 */     return (superTypeConst != null) && (isSuperTypeOf(superTypeConst));
/*     */   }
/*     */   
/*     */   public abstract TypeConstructor getSuperTypeConstructor();
/*     */   
/*     */   public TypeConstructor getSuperestTypeConstructor()
/*     */   {
/*  45 */     TypeConstructor superConst = getSuperTypeConstructor();
/*  46 */     if (superConst == null) {
/*  47 */       return this;
/*     */     }
/*  49 */     return superConst.getSuperestTypeConstructor();
/*     */   }
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract int getTypeArity();
/*     */   
/*     */   public int getTermArity()
/*     */   {
/*     */     try {
/*  59 */       if (!hasRepresentation())
/*  60 */         throw new TypeModeError("The type constructor " + this + "is abstract and cannot be used as a term constructor");
/*  61 */       Type representedBy = getRepresentation();
/*  62 */       if ((representedBy instanceof TupleType))
/*  63 */         return ((TupleType)representedBy).size();
/*  64 */       if ((representedBy instanceof ListType)) {
/*  65 */         return 1;
/*     */       }
/*  67 */       if ((representedBy instanceof CompositeType)) {
/*  68 */         return 1;
/*     */       }
/*  70 */       throw new Error("This should not happen");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {
/*  73 */       throw new Error("This should not happen, unless the type system is broken");
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract String getParameterName(int paramInt);
/*     */   
/*     */   public TypeConstructor lowerBound(TypeConstructor otherTypeConst)
/*     */   {
/*  81 */     if (equals(otherTypeConst))
/*  82 */       return this;
/*  83 */     if (isSuperTypeOf(otherTypeConst))
/*  84 */       return this;
/*  85 */     if (otherTypeConst.isSuperTypeOf(this)) {
/*  86 */       return otherTypeConst;
/*     */     }
/*  88 */     return getSuperTypeConstructor().lowerBound(otherTypeConst);
/*     */   }
/*     */   
/*     */   public Type getRepresentation()
/*     */   {
/*  93 */     throw new Error("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public boolean hasRepresentation() {
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   public abstract ConstructorType getConstructorType();
/*     */   
/*     */   public FunctorIdentifier getFunctorId() {
/* 103 */     return new FunctorIdentifier(getName(), getTermArity());
/*     */   }
/*     */   
/*     */   public abstract boolean isInitialized();
/*     */   
/*     */   public void setParameter(TupleType args) {
/* 109 */     throw new Error("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public void setConstructorType(ConstructorType constrType) {
/* 113 */     throw new Error("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public void addSubTypeConst(TypeConstructor typeConstructor) throws TypeModeError {
/* 117 */     throw new TypeModeError("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public void addSuperTypeConst(TypeConstructor superConst) throws TypeModeError {
/* 121 */     throw new TypeModeError("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public void setRepresentationType(Type repBy) {
/* 125 */     throw new Error("This is not a user defined type: " + this);
/*     */   }
/*     */   
/*     */   public boolean isJavaTypeConstructor() {
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public void setMetaBase(MetaBase base) {}
/*     */   
/*     */   public abstract Class javaEquivalent();
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/TypeConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */