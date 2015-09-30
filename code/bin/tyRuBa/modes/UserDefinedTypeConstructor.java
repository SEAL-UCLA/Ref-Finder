/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import tyRuBa.engine.MetaBase;
/*     */ 
/*     */ public class UserDefinedTypeConstructor
/*     */   extends TypeConstructor implements Serializable
/*     */ {
/*     */   private String name;
/*  10 */   private TypeConstructor superConst = null;
/*     */   private Type representedBy;
/*     */   private TVar[] parameters;
/*  13 */   boolean initialized = false;
/*     */   
/*     */ 
/*     */   private ConstructorType constructorType;
/*     */   
/*     */   private TypeMapping mapping;
/*     */   
/*  20 */   private transient MetaBase metaBase = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMetaBase(MetaBase metaBase)
/*     */   {
/*  26 */     this.metaBase = metaBase;
/*  27 */     if (this.superConst != null) {
/*  28 */       metaBase.assertSubtype(this.superConst, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public UserDefinedTypeConstructor(String name, int arity)
/*     */   {
/*  34 */     this.name = name;
/*  35 */     this.parameters = new TVar[arity];
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  39 */     if (!(other instanceof UserDefinedTypeConstructor)) {
/*  40 */       return false;
/*     */     }
/*  42 */     UserDefinedTypeConstructor tother = (UserDefinedTypeConstructor)other;
/*     */     
/*  44 */     return (this.name.equals(tother.name)) && (this.parameters.length == tother.parameters.length);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  49 */     return this.name.hashCode() * 13 + this.parameters.length;
/*     */   }
/*     */   
/*     */   public void addSubTypeConst(TypeConstructor subConst) throws TypeModeError {
/*  53 */     subConst.addSuperTypeConst(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addSuperTypeConst(TypeConstructor superConst)
/*     */     throws TypeModeError
/*     */   {
/*  60 */     if (equals(superConst)) {
/*  61 */       throw new TypeModeError(
/*  62 */         "Recursion in type inheritance: " + this + " depends on itself");
/*     */     }
/*  64 */     if (this.superConst == null) {
/*  65 */       this.superConst = superConst;
/*  66 */       if (this.metaBase != null) this.metaBase.assertSubtype(superConst, this);
/*     */     } else {
/*  68 */       throw new TypeModeError("Multiple inheritance not supported: " + this + 
/*  69 */         " inherits from " + this.superConst + " and " + superConst);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRepresentationType(Type repType)
/*     */   {
/*  78 */     this.representedBy = repType;
/*  79 */     if (this.metaBase != null) this.metaBase.assertRepresentation(this, repType);
/*     */   }
/*     */   
/*     */   public TypeConstructor getSuperTypeConstructor() {
/*  83 */     if (this.superConst != null) {
/*  84 */       return this.superConst;
/*     */     }
/*  86 */     return TypeConstructor.theAny;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  90 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getTypeArity() {
/*  94 */     return this.parameters.length;
/*     */   }
/*     */   
/*     */   public void setParameter(TupleType type)
/*     */   {
/*  99 */     if (type.size() != getTypeArity())
/* 100 */       throw new Error("This should not happen");
/* 101 */     for (int i = 0; i < this.parameters.length; i++) {
/* 102 */       this.parameters[i] = ((TVar)type.get(i));
/*     */     }
/* 104 */     this.initialized = true;
/*     */   }
/*     */   
/*     */   public String getParameterName(int i) {
/* 108 */     if (i < getTypeArity()) {
/* 109 */       return this.parameters[i].getName();
/*     */     }
/* 111 */     throw new Error("This should not happen");
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 116 */     StringBuffer result = new StringBuffer(this.name + "(");
/* 117 */     for (int i = 0; i < getTypeArity(); i++) {
/* 118 */       if (i > 0) {
/* 119 */         result.append(",");
/*     */       }
/* 121 */       result.append(this.parameters[i]);
/*     */     }
/* 123 */     result.append(")");
/* 124 */     if (this.representedBy != null) {
/* 125 */       result.append(" AS " + this.representedBy);
/*     */     }
/* 127 */     return result.toString();
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 131 */     return this.initialized;
/*     */   }
/*     */   
/*     */   public Type getRepresentation() {
/* 135 */     return this.representedBy;
/*     */   }
/*     */   
/*     */   public boolean hasRepresentation() {
/* 139 */     return this.representedBy != null;
/*     */   }
/*     */   
/*     */   public void setConstructorType(ConstructorType constrType) {
/* 143 */     if (this.constructorType != null)
/* 144 */       throw new Error("Should not set twice!");
/* 145 */     if (!hasRepresentation())
/* 146 */       throw new Error("Only concrete composite types can have a constructorType");
/* 147 */     this.constructorType = constrType;
/*     */   }
/*     */   
/*     */   public ConstructorType getConstructorType() {
/* 151 */     return this.constructorType;
/*     */   }
/*     */   
/*     */   public TypeConstructor getSuperestTypeConstructor() {
/* 155 */     TypeConstructor result = super.getSuperestTypeConstructor();
/* 156 */     if (TypeConstructor.theAny.equals(result)) {
/* 157 */       return this;
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */   
/*     */   public TypeMapping getMapping() {
/* 163 */     return this.mapping;
/*     */   }
/*     */   
/*     */   public void setMapping(TypeMapping mapping) {
/* 167 */     if (this.mapping != null) {
/* 168 */       throw new Error("Can only define a single Java type mapping per tyRuBa type");
/*     */     }
/* 170 */     this.mapping = mapping;
/*     */   }
/*     */   
/*     */   public Class javaEquivalent() {
/* 174 */     if (getMapping() == null) {
/* 175 */       return null;
/*     */     }
/* 177 */     return getMapping().getMappedClass();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/UserDefinedTypeConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */