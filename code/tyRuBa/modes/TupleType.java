/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TupleType
/*     */   extends Type
/*     */ {
/*     */   private ArrayList parts;
/*     */   
/*     */   public TupleType()
/*     */   {
/*  17 */     this.parts = new ArrayList();
/*     */   }
/*     */   
/*     */   public TupleType(Type[] types)
/*     */   {
/*  22 */     this.parts = new ArrayList(types.length);
/*  23 */     for (int i = 0; i < types.length; i++) {
/*  24 */       this.parts.add(types[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Type[] getTypes() {
/*  29 */     return (Type[])this.parts.toArray(new Type[this.parts.size()]);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  33 */     int size = size();
/*  34 */     int hash = getClass().hashCode();
/*  35 */     for (int i = 0; i < size; i++)
/*  36 */       hash = hash * 19 + get(i).hashCode();
/*  37 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  41 */     if (other == null)
/*  42 */       return false;
/*  43 */     if (!(other instanceof TupleType)) {
/*  44 */       return false;
/*     */     }
/*  46 */     TupleType cother = (TupleType)other;
/*  47 */     int size = size();
/*  48 */     if (size != cother.size()) {
/*  49 */       return false;
/*     */     }
/*  51 */     for (int i = 0; i < size(); i++) {
/*  52 */       Type t1 = get(i);
/*  53 */       Type t2 = cother.get(i);
/*  54 */       if ((t1 == null) && (t2 != null))
/*  55 */         return false;
/*  56 */       if ((t1 != null) && (t2 == null))
/*  57 */         return false;
/*  58 */       if ((t1 != null) && (!get(i).equals(cother.get(i)))) {
/*  59 */         return false;
/*     */       }
/*     */     }
/*  62 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  68 */     StringBuffer result = new StringBuffer("<");
/*  69 */     for (int i = 0; i < size(); i++) {
/*  70 */       if (i != 0) {
/*  71 */         result.append(", ");
/*     */       }
/*  73 */       result.append(get(i));
/*     */     }
/*  75 */     result.append(">");
/*  76 */     return result.toString();
/*     */   }
/*     */   
/*     */   public void add(Type newPart) {
/*  80 */     this.parts.add(newPart);
/*     */   }
/*     */   
/*     */   public Type get(int i) {
/*  84 */     return (Type)this.parts.get(i);
/*     */   }
/*     */   
/*     */   public int size() {
/*  88 */     return this.parts.size();
/*     */   }
/*     */   
/*     */   public void checkEqualTypes(Type tother, boolean grow) throws TypeModeError {
/*  92 */     if ((tother instanceof TVar)) {
/*  93 */       tother.checkEqualTypes(this, grow);
/*     */     } else {
/*  95 */       check(tother instanceof TupleType, this, tother);
/*  96 */       TupleType other = (TupleType)tother;
/*  97 */       check(other.size() == size(), this, other);
/*  98 */       for (int i = 0; i < size(); i++) {
/*  99 */         Type t1 = get(i);
/* 100 */         Type t2 = other.get(i);
/* 101 */         t1.checkEqualTypes(t2, grow);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSubTypeOf(Type tother, Map renamings) {
/* 107 */     if (!(tother instanceof TupleType)) {
/* 108 */       return false;
/*     */     }
/* 110 */     TupleType other = (TupleType)tother;
/* 111 */     if (size() != other.size()) {
/* 112 */       return false;
/*     */     }
/* 114 */     for (int i = 0; i < size(); i++) {
/* 115 */       if (!get(i).isSubTypeOf(other.get(i), renamings)) {
/* 116 */         return false;
/*     */       }
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Type clone(Map tfact)
/*     */   {
/* 125 */     TupleType result = Factory.makeTupleType();
/* 126 */     for (int i = 0; i < size(); i++) {
/* 127 */       if (get(i) == null) {
/* 128 */         result.add(null);
/*     */       } else {
/* 130 */         result.add(get(i).clone(tfact));
/*     */       }
/*     */     }
/* 133 */     return result;
/*     */   }
/*     */   
/*     */   public Type union(Type tother) throws TypeModeError {
/* 137 */     if ((tother instanceof TVar)) {
/* 138 */       return tother.union(this);
/*     */     }
/* 140 */     check(tother instanceof TupleType, this, tother);
/* 141 */     TupleType other = (TupleType)tother;
/* 142 */     check(other.size() == size(), this, other);
/* 143 */     TupleType result = Factory.makeTupleType();
/* 144 */     for (int i = 0; i < size(); i++) {
/* 145 */       if (get(i) == null) {
/* 146 */         result.add(other.get(i));
/*     */       } else {
/* 148 */         result.add(get(i).union(other.get(i)));
/*     */       }
/*     */     }
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   public Type intersect(Type tother) throws TypeModeError
/*     */   {
/* 156 */     if ((tother instanceof TVar)) {
/* 157 */       return tother.intersect(this);
/*     */     }
/* 159 */     check(tother instanceof TupleType, this, tother);
/* 160 */     TupleType other = (TupleType)tother;
/* 161 */     check(other.size() == size(), this, other);
/* 162 */     TupleType result = Factory.makeTupleType();
/* 163 */     for (int i = 0; i < size(); i++) {
/* 164 */       if (get(i) == null) {
/* 165 */         result.add(other.get(i));
/*     */       } else {
/* 167 */         result.add(get(i).intersect(other.get(i)));
/*     */       }
/*     */     }
/* 170 */     return result;
/*     */   }
/*     */   
/*     */   public boolean hasOverlapWith(Type tother) {
/* 174 */     if (tother == null)
/* 175 */       return false;
/* 176 */     if ((tother instanceof TVar))
/* 177 */       return tother.hasOverlapWith(tother);
/* 178 */     if (!(tother instanceof TupleType)) {
/* 179 */       return false;
/*     */     }
/* 181 */     TupleType other = (TupleType)tother;
/* 182 */     int size = size();
/* 183 */     if ((size == 0) || (size != other.size())) {
/* 184 */       return false;
/*     */     }
/* 186 */     for (int i = 0; i < size(); i++) {
/* 187 */       if ((get(i) == null) && (other.get(i) != null))
/* 188 */         return false;
/* 189 */       if ((get(i) != null) && 
/* 190 */         (!get(i).hasOverlapWith(other.get(i)))) {
/* 191 */         return false;
/*     */       }
/*     */     }
/* 194 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFreeFor(TVar var)
/*     */   {
/* 200 */     for (int i = 0; i < size(); i++) {
/* 201 */       if ((get(i) != null) && (!get(i).isFreeFor(var))) {
/* 202 */         return false;
/*     */       }
/*     */     }
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public Type copyStrictPart() {
/* 209 */     TupleType result = new TupleType();
/* 210 */     for (int i = 0; i < size(); i++) {
/* 211 */       result.add(get(i).copyStrictPart());
/*     */     }
/* 213 */     return result;
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
/*     */   public Type getParamType(int pos, TypeConstructor typeConst)
/*     */   {
/* 241 */     if (!typeConst.hasRepresentation()) {
/* 242 */       return get(pos);
/*     */     }
/* 244 */     return getParamType(typeConst.getParameterName(pos), 
/* 245 */       typeConst.getRepresentation());
/*     */   }
/*     */   
/*     */   public Type getParamType(String currName, Type repAs)
/*     */   {
/* 250 */     if ((repAs instanceof TVar)) {
/* 251 */       if (currName.equals(((TVar)repAs).getName())) {
/* 252 */         return this;
/*     */       }
/* 254 */       return null;
/*     */     }
/* 256 */     if ((repAs instanceof ListType)) {
/* 257 */       if (size() == 1) {
/* 258 */         return get(0).getParamType(currName, repAs);
/*     */       }
/* 260 */       return null;
/*     */     }
/* 262 */     if (!(repAs instanceof TupleType)) {
/* 263 */       return null;
/*     */     }
/* 265 */     Type result = null;
/* 266 */     TupleType repAsTuple = (TupleType)repAs;
/* 267 */     if (size() != repAsTuple.size()) {
/* 268 */       return null;
/*     */     }
/* 270 */     for (int i = 0; i < size(); i++) {
/* 271 */       Type currParamType = get(i).getParamType(currName, repAsTuple.get(i));
/* 272 */       if ((result != null) && (currParamType != null)) {
/*     */         try {
/* 274 */           result.checkEqualTypes(currParamType);
/*     */         } catch (TypeModeError localTypeModeError) {
/* 276 */           return null;
/*     */         }
/* 278 */       } else if (result == null) {
/* 279 */         result = currParamType;
/*     */       }
/*     */     }
/*     */     
/* 283 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/TupleType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */