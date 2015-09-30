/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public abstract class Type
/*    */   implements Cloneable, Serializable
/*    */ {
/* 10 */   public static final Type integer = Factory.makeStrictAtomicType(Factory.makeTypeConstructor(Integer.class));
/*    */   
/* 12 */   public static final Type string = Factory.makeStrictAtomicType(Factory.makeTypeConstructor(String.class));
/*    */   
/* 14 */   public static final Type number = Factory.makeStrictAtomicType(Factory.makeTypeConstructor(Number.class));
/*    */   
/* 16 */   public static final Type object = Factory.makeStrictAtomicType(Factory.makeTypeConstructor(Object.class));
/*    */   
/*    */   public void checkEqualTypes(Type other) throws TypeModeError
/*    */   {
/* 20 */     checkEqualTypes(other, true);
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract void checkEqualTypes(Type paramType, boolean paramBoolean)
/*    */     throws TypeModeError;
/*    */   
/*    */ 
/*    */   public abstract boolean isFreeFor(TVar paramTVar);
/*    */   
/*    */ 
/*    */   public abstract Type clone(Map paramMap);
/*    */   
/*    */ 
/*    */   public abstract Type union(Type paramType)
/*    */     throws TypeModeError;
/*    */   
/*    */   public abstract Type intersect(Type paramType)
/*    */     throws TypeModeError;
/*    */   
/*    */   public static void check(boolean b, Type t1, Type t2)
/*    */     throws TypeModeError
/*    */   {
/* 43 */     if (!b) {
/* 44 */       throw new TypeModeError("Incompatible types: " + t1 + ", " + t2);
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract boolean isSubTypeOf(Type paramType, Map paramMap);
/*    */   
/*    */   public abstract Type copyStrictPart();
/*    */   
/*    */   public abstract boolean hasOverlapWith(Type paramType);
/*    */   
/*    */   public boolean hasOverlapWith(Vector types, boolean hasOverlap) {
/* 55 */     int size = types.size();
/* 56 */     boolean equalTypes = false;
/* 57 */     int counter = 0;
/* 58 */     while ((!equalTypes) && (counter < size)) {
/* 59 */       Type currType = (Type)types.elementAt(counter);
/* 60 */       if (currType.equals(this)) {
/* 61 */         hasOverlap = hasOverlap;
/* 62 */         equalTypes = true;
/* 63 */       } else if ((hasOverlap) && 
/* 64 */         (!currType.hasOverlapWith(this))) {
/* 65 */         hasOverlap = false;
/*    */       }
/*    */       
/* 68 */       counter++;
/*    */     }
/* 70 */     if (!equalTypes) {
/* 71 */       types.add(this);
/*    */     }
/* 73 */     return hasOverlap;
/*    */   }
/*    */   
/*    */   public abstract Type getParamType(String paramString, Type paramType);
/*    */   
/*    */   public Class javaEquivalent() throws TypeModeError {
/* 79 */     throw new TypeModeError("This type " + this + " has no defined mapping to a Java equivalent");
/*    */   }
/*    */   
/*    */   public boolean isJavaType() {
/* 83 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/Type.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */