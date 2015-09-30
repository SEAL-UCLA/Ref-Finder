/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import tyRuBa.engine.FunctorIdentifier;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ConstructorType
/*    */ {
/*    */   public abstract FunctorIdentifier getFunctorId();
/*    */   
/*    */   public abstract TypeConstructor getTypeConst();
/*    */   
/*    */   public abstract int getArity();
/*    */   
/*    */   public abstract RBTerm apply(RBTerm paramRBTerm);
/*    */   
/*    */   public abstract RBTerm apply(ArrayList paramArrayList);
/*    */   
/*    */   public abstract Type apply(Type paramType)
/*    */     throws TypeModeError;
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   public static ConstructorType makeUserDefined(FunctorIdentifier functorId, Type repAs, CompositeType type)
/*    */   {
/* 32 */     if (repAs.isJavaType()) {
/* 33 */       return new RepAsJavaConstructorType(functorId, repAs, type);
/*    */     }
/*    */     
/* 36 */     return new GenericConstructorType(functorId, repAs, type);
/*    */   }
/*    */   
/*    */   public static ConstructorType makeJava(Class javaClass)
/*    */   {
/* 41 */     return new JavaConstructorType(javaClass);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/ConstructorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */