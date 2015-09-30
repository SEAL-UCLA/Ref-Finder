/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.ArrayList;
/*    */ import tyRuBa.engine.FunctorIdentifier;
/*    */ import tyRuBa.engine.RBCompoundTerm;
/*    */ import tyRuBa.engine.RBJavaObjectCompoundTerm;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaConstructorType
/*    */   extends ConstructorType
/*    */ {
/*    */   Class javaClass;
/*    */   
/*    */   public JavaConstructorType(Class javaClass)
/*    */   {
/* 22 */     this.javaClass = javaClass;
/*    */   }
/*    */   
/*    */   public RBTerm apply(ArrayList terms) {
/* 26 */     throw new Error("Java Constructors can only be applied a single term");
/*    */   }
/*    */   
/*    */   public RBTerm apply(RBTerm term) {
/* 30 */     if ((term instanceof RBJavaObjectCompoundTerm)) {
/* 31 */       RBJavaObjectCompoundTerm java_term = (RBJavaObjectCompoundTerm)term;
/* 32 */       if (getTypeConst().isSuperTypeOf(java_term.getTypeConstructor())) {
/* 33 */         return java_term;
/*    */       }
/* 35 */       Object obj = java_term.getObject();
/*    */       try {
/* 37 */         Constructor ctor = this.javaClass.getConstructor(new Class[] { obj.getClass() });
/* 38 */         return RBCompoundTerm.makeJava(ctor.newInstance(new Object[] { obj }));
/*    */       } catch (Exception e) {
/* 40 */         throw new Error("Illegal TyRuBa to Java Type Cast: " + java_term + "::" + this);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 45 */     return RBCompoundTerm.make(this, term);
/*    */   }
/*    */   
/*    */   public Type apply(Type argType) throws TypeModeError {
/* 49 */     Type iresult = getType();
/* 50 */     argType.checkEqualTypes(iresult);
/* 51 */     return iresult;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 55 */     return Factory.makeSubAtomicType(getTypeConst());
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 59 */     if (getClass() != other.getClass()) {
/* 60 */       return false;
/*    */     }
/* 62 */     return this.javaClass.equals(((JavaConstructorType)other).javaClass);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 66 */     return this.javaClass.hashCode();
/*    */   }
/*    */   
/*    */   public int getArity() {
/* 70 */     return 1;
/*    */   }
/*    */   
/*    */   public FunctorIdentifier getFunctorId() {
/* 74 */     return new FunctorIdentifier(this.javaClass.getName(), 1);
/*    */   }
/*    */   
/* 77 */   public TypeConstructor getTypeConst() { return Factory.makeTypeConstructor(this.javaClass); }
/*    */   
/*    */   public String toString()
/*    */   {
/* 81 */     return "JavaConstructorType(" + this.javaClass + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/JavaConstructorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */