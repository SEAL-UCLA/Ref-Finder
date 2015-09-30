/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CompositeType extends BoundaryType
/*     */ {
/*   8 */   private TypeConstructor typeConst = null;
/*   9 */   boolean strict = false;
/*     */   private TupleType args;
/*     */   
/*     */   public CompositeType(TypeConstructor typeConst, boolean strict, TupleType args) {
/*  13 */     this.typeConst = typeConst;
/*  14 */     this.strict = strict;
/*  15 */     this.args = args;
/*     */   }
/*     */   
/*     */   public TypeConstructor getTypeConstructor() {
/*  19 */     return this.typeConst;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  23 */     return getTypeConstructor().hashCode() + 13 * this.args.hashCode();
/*     */   }
/*     */   
/*     */   public void checkEqualTypes(Type other, boolean grow) throws TypeModeError {
/*  27 */     if (((other instanceof TVar)) || ((other instanceof GrowableType))) {
/*  28 */       other.checkEqualTypes(this, grow);
/*     */     } else {
/*  30 */       check(other instanceof CompositeType, this, other);
/*  31 */       CompositeType cother = (CompositeType)other;
/*  32 */       check(getTypeConstructor().equals(cother.getTypeConstructor()), this, other);
/*     */       try {
/*  34 */         this.args.checkEqualTypes(cother.args, grow);
/*     */       } catch (TypeModeError e) {
/*  36 */         throw new TypeModeError(e, this);
/*     */       }
/*  38 */       boolean newStrict = (this.strict) || (cother.strict);
/*  39 */       cother.strict = newStrict;
/*  40 */       this.strict = newStrict;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSubTypeOf(Type other, Map renamings) {
/*  45 */     if ((other instanceof TVar))
/*  46 */       other = ((TVar)other).getContents();
/*  47 */     if (other == null)
/*  48 */       return false;
/*  49 */     if (!(other instanceof CompositeType)) {
/*  50 */       return false;
/*     */     }
/*  52 */     CompositeType declared = (CompositeType)other;
/*  53 */     TypeConstructor declaredTypeConst = declared.getTypeConstructor();
/*  54 */     if (isStrict())
/*     */     {
/*     */ 
/*  57 */       return (this.typeConst.equals(declaredTypeConst)) && (declared.isStrict()) && (this.args.isSubTypeOf(declared.args, renamings)); }
/*  58 */     if (this.typeConst.equals(declaredTypeConst))
/*  59 */       return this.args.isSubTypeOf(declared.args, renamings);
/*  60 */     if (declaredTypeConst.isSuperTypeOf(this.typeConst)) {
/*  61 */       Map params = new HashMap();
/*  62 */       for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/*  63 */         String currName = this.typeConst.getParameterName(i);
/*  64 */         params.put(currName, this.args.getParamType(i, this.typeConst));
/*     */       }
/*  66 */       for (int i = 0; i < declaredTypeConst.getTypeArity(); i++) {
/*  67 */         String currName = declaredTypeConst.getParameterName(i);
/*  68 */         Type paramType = (Type)params.get(currName);
/*  69 */         if (paramType != null) {
/*  70 */           Type declaredType = declared.args.getParamType(i, declaredTypeConst);
/*  71 */           if (declaredType == null)
/*  72 */             return false;
/*  73 */           if (!paramType.isSubTypeOf(declaredType, renamings)) {
/*  74 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*  78 */       return true;
/*     */     }
/*  80 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  86 */     if (!(other instanceof CompositeType)) {
/*  87 */       return false;
/*     */     }
/*  89 */     CompositeType cother = (CompositeType)other;
/*     */     
/*     */ 
/*  92 */     return (getTypeConstructor().equals(cother.getTypeConstructor())) && (isStrict() == cother.isStrict()) && (this.args.equals(cother.args));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  97 */     String constName = this.typeConst.getName() + this.args;
/*  98 */     if (isStrict()) {
/*  99 */       return "=" + constName;
/*     */     }
/* 101 */     return constName;
/*     */   }
/*     */   
/*     */   public boolean isFreeFor(TVar var)
/*     */   {
/* 106 */     return this.args.isFreeFor(var);
/*     */   }
/*     */   
/*     */   public Type clone(Map tfact) {
/* 110 */     return new CompositeType(this.typeConst, this.strict, (TupleType)this.args.clone(tfact));
/*     */   }
/*     */   
/*     */   public String getName() {
/* 114 */     return getTypeConstructor().getName();
/*     */   }
/*     */   
/*     */   public TupleType getArgs() {
/* 118 */     return this.args;
/*     */   }
/*     */   
/*     */   public Type union(Type other) throws TypeModeError {
/* 122 */     if (((other instanceof TVar)) || ((other instanceof GrowableType))) {
/* 123 */       return other.union(this);
/*     */     }
/* 125 */     check(other instanceof CompositeType, this, other);
/* 126 */     CompositeType cother = (CompositeType)other;
/* 127 */     TypeConstructor otherTypeConst = cother.typeConst;
/* 128 */     if (equals(other))
/* 129 */       return this;
/* 130 */     if (this.typeConst.equals(otherTypeConst)) {
/* 131 */       TupleType resultArg = (TupleType)this.args.union(cother.args);
/* 132 */       if ((this.strict) || (cother.strict)) {
/* 133 */         return this.typeConst.applyStrict(resultArg, false);
/*     */       }
/* 135 */       return this.typeConst.apply(resultArg, false);
/*     */     }
/* 137 */     if (otherTypeConst.isSuperTypeOf(this.typeConst)) {
/* 138 */       check(!isStrict(), this, other);
/* 139 */       Map params = new HashMap();
/* 140 */       for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/* 141 */         params.put(this.typeConst.getParameterName(i), this.args.get(i));
/*     */       }
/* 143 */       TupleType resultArg = Factory.makeTupleType();
/* 144 */       for (int i = 0; i < cother.typeConst.getTypeArity(); i++) {
/* 145 */         String currName = cother.typeConst.getParameterName(i);
/* 146 */         Type paramValue = (Type)params.get(currName);
/* 147 */         if (paramValue != null) {
/* 148 */           resultArg.add(paramValue.union(cother.args.get(i)));
/*     */         } else {
/* 150 */           resultArg.add(cother.args.get(i));
/*     */         }
/*     */       }
/* 153 */       if (cother.strict) {
/* 154 */         return otherTypeConst.applyStrict(resultArg, false);
/*     */       }
/* 156 */       return otherTypeConst.apply(resultArg, false);
/*     */     }
/* 158 */     if (this.typeConst.isSuperTypeOf(otherTypeConst)) {
/* 159 */       return cother.intersect(this);
/*     */     }
/* 161 */     check(!isStrict(), this, other);
/* 162 */     check(!cother.isStrict(), this, other);
/* 163 */     TypeConstructor superTypeConst = this.typeConst.lowerBound(otherTypeConst);
/* 164 */     Map params = new HashMap();
/* 165 */     for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/* 166 */       params.put(this.typeConst.getParameterName(i), this.args.get(i));
/*     */     }
/* 168 */     for (int i = 0; i < otherTypeConst.getTypeArity(); i++) {
/* 169 */       String currName = otherTypeConst.getParameterName(i);
/* 170 */       Type paramValue = (Type)params.get(currName);
/* 171 */       if (paramValue == null) {
/* 172 */         params.put(currName, cother.args.get(i));
/*     */       } else {
/* 174 */         params.put(currName, paramValue.union(cother.args.get(i)));
/*     */       }
/*     */     }
/* 177 */     TupleType resultArg = Factory.makeTupleType();
/* 178 */     for (int i = 0; i < superTypeConst.getTypeArity(); i++) {
/* 179 */       String currName = superTypeConst.getParameterName(i);
/* 180 */       resultArg.add((Type)params.get(currName));
/*     */     }
/* 182 */     return superTypeConst.apply(resultArg, false);
/*     */   }
/*     */   
/*     */   public Type intersect(Type other)
/*     */     throws TypeModeError
/*     */   {
/* 188 */     if (((other instanceof TVar)) || ((other instanceof GrowableType))) {
/* 189 */       return other.intersect(this);
/*     */     }
/* 191 */     check(other instanceof CompositeType, this, other);
/* 192 */     CompositeType cother = (CompositeType)other;
/* 193 */     TypeConstructor otherTypeConst = cother.typeConst;
/* 194 */     if (equals(other))
/* 195 */       return this;
/* 196 */     if (this.typeConst.equals(otherTypeConst)) {
/* 197 */       TupleType resultArg = (TupleType)this.args.intersect(cother.args);
/* 198 */       if ((this.strict) || (cother.strict)) {
/* 199 */         return this.typeConst.applyStrict(resultArg, false);
/*     */       }
/* 201 */       return this.typeConst.apply(resultArg, false);
/*     */     }
/* 203 */     if (this.typeConst.isSuperTypeOf(otherTypeConst)) {
/* 204 */       check(!cother.isStrict(), this, other);
/* 205 */       Map params = new HashMap();
/* 206 */       for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/* 207 */         params.put(this.typeConst.getParameterName(i), this.args.get(i));
/*     */       }
/* 209 */       TupleType resultArg = Factory.makeTupleType();
/* 210 */       for (int i = 0; i < cother.typeConst.getTypeArity(); i++) {
/* 211 */         String currName = cother.typeConst.getParameterName(i);
/* 212 */         Type paramValue = (Type)params.get(currName);
/* 213 */         check(paramValue != null, this, other);
/* 214 */         resultArg.add(paramValue.intersect(cother.args.get(i)));
/*     */       }
/* 216 */       return cother.typeConst.apply(resultArg, false); }
/* 217 */     if (otherTypeConst.isSuperTypeOf(this.typeConst)) {
/* 218 */       return cother.intersect(this);
/*     */     }
/* 220 */     throw new TypeModeError("Incompatible types: " + this + ", " + other);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasOverlapWith(Type other)
/*     */   {
/* 226 */     if (((other instanceof TVar)) || ((other instanceof GrowableType)))
/* 227 */       return other.hasOverlapWith(this);
/* 228 */     if (!(other instanceof CompositeType)) {
/* 229 */       return false;
/*     */     }
/* 231 */     CompositeType cother = (CompositeType)other;
/* 232 */     TypeConstructor otherTypeConst = cother.getTypeConstructor();
/* 233 */     if (this.typeConst.equals(otherTypeConst))
/* 234 */       return this.args.hasOverlapWith(cother.args);
/* 235 */     if (this.typeConst.isSuperTypeOf(otherTypeConst)) {
/* 236 */       Map params = new HashMap();
/* 237 */       for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/* 238 */         params.put(this.typeConst.getParameterName(i), this.args.get(i));
/*     */       }
/* 240 */       for (int i = 0; i < otherTypeConst.getTypeArity(); i++) {
/* 241 */         Type paramValue = (Type)params.get(otherTypeConst.getParameterName(i));
/* 242 */         if ((paramValue != null) && 
/* 243 */           (cother.args.get(i).hasOverlapWith(paramValue))) {
/* 244 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 248 */       return false; }
/* 249 */     if (otherTypeConst.isSuperTypeOf(this.typeConst)) {
/* 250 */       return other.hasOverlapWith(this);
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean isStrict()
/*     */   {
/* 258 */     return this.strict;
/*     */   }
/*     */   
/*     */   public Type copyStrictPart() {
/* 262 */     if (isStrict()) {
/* 263 */       return this.typeConst.applyStrict((TupleType)this.args.copyStrictPart(), false);
/*     */     }
/* 265 */     TypeConstructor resultTypeConst = 
/* 266 */       this.typeConst.getSuperestTypeConstructor();
/* 267 */     Map params = new HashMap();
/* 268 */     for (int i = 0; i < this.typeConst.getTypeArity(); i++) {
/* 269 */       params.put(this.typeConst.getParameterName(i), this.args.get(i).copyStrictPart());
/*     */     }
/* 271 */     TupleType resultArg = Factory.makeTupleType();
/* 272 */     for (int i = 0; i < resultTypeConst.getTypeArity(); i++) {
/* 273 */       String currName = resultTypeConst.getParameterName(i);
/* 274 */       Type paramType = (Type)params.get(currName);
/* 275 */       if (paramType == null) {
/* 276 */         resultArg.add(Factory.makeTVar(currName));
/*     */       } else {
/* 278 */         resultArg.add(paramType);
/*     */       }
/*     */     }
/* 281 */     return resultTypeConst.apply(resultArg, false);
/*     */   }
/*     */   
/*     */   public void makeStrict()
/*     */   {
/* 286 */     this.strict = true;
/*     */   }
/*     */   
/*     */   public void addSubType(Type subType) throws TypeModeError {
/* 290 */     if (!(subType instanceof CompositeType)) {
/* 291 */       throw new TypeModeError(subType + " is an illegal subtype for " + this);
/*     */     }
/* 293 */     this.typeConst.addSubTypeConst(((CompositeType)subType).getTypeConstructor());
/*     */   }
/*     */   
/*     */   public void setRepresentationType(Type repBy)
/*     */   {
/* 298 */     this.typeConst.setRepresentationType(repBy);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getParamType(String currName, Type repAs)
/*     */   {
/* 370 */     if ((repAs instanceof TVar)) {
/* 371 */       if (currName.equals(((TVar)repAs).getName())) {
/* 372 */         return this;
/*     */       }
/* 374 */       return null;
/*     */     }
/* 376 */     if (!(repAs instanceof CompositeType)) {
/* 377 */       return null;
/*     */     }
/* 379 */     CompositeType compositeRepAs = (CompositeType)repAs;
/* 380 */     if (compositeRepAs.getTypeConstructor().equals(this.typeConst)) {
/* 381 */       return this.args.getParamType(currName, compositeRepAs.args);
/*     */     }
/* 383 */     return null;
/*     */   }
/*     */   
/*     */   public Class javaEquivalent()
/*     */     throws TypeModeError
/*     */   {
/* 389 */     return this.typeConst.javaEquivalent();
/*     */   }
/*     */   
/*     */   public boolean isJavaType() {
/* 393 */     return getTypeConstructor().isJavaTypeConstructor();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/CompositeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */