/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class JavaTypeConstructor extends TypeConstructor implements Serializable
/*    */ {
/*    */   private final Class javaClass;
/*    */   
/*    */   public JavaTypeConstructor(Class javaclass)
/*    */   {
/* 11 */     if ((javaclass.isInterface()) || (javaclass.isPrimitive())) {
/* 12 */       throw new Error("no interfaces or primitives types are allowed");
/*    */     }
/* 14 */     this.javaClass = javaclass;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 19 */     String name = this.javaClass.getName();
/* 20 */     if (name.startsWith("java.lang.")) {
/* 21 */       return name.substring("java.lang.".length());
/*    */     }
/* 23 */     return name;
/*    */   }
/*    */   
/*    */   public boolean equals(Object other)
/*    */   {
/* 28 */     if ((other != null) && (getClass().equals(other.getClass()))) {
/* 29 */       return getName().equals(((TypeConstructor)other).getName());
/*    */     }
/* 31 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 36 */     return getName().hashCode();
/*    */   }
/*    */   
/*    */   public void addSuperType(TypeConstructor superType) throws TypeModeError {
/* 40 */     throw new TypeModeError("Can not add super type for java types " + this);
/*    */   }
/*    */   
/*    */   public TypeConstructor getSuperTypeConstructor() {
/* 44 */     if (this.javaClass.getSuperclass() == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     return new JavaTypeConstructor(this.javaClass.getSuperclass());
/*    */   }
/*    */   
/*    */   public int getTypeArity() {
/* 51 */     return 0;
/*    */   }
/*    */   
/*    */   public String getParameterName(int i) {
/* 55 */     throw new Error("This is not a user defined type");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isInitialized()
/*    */   {
/* 62 */     throw new Error("This is not a user defined type");
/*    */   }
/*    */   
/*    */   public ConstructorType getConstructorType() {
/* 66 */     return ConstructorType.makeJava(this.javaClass);
/*    */   }
/*    */   
/*    */   public boolean isJavaTypeConstructor() {
/* 70 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 74 */     return "JavaTypeConstructor(" + this.javaClass + ")";
/*    */   }
/*    */   
/*    */   public Class javaEquivalent() {
/* 78 */     return this.javaClass;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/JavaTypeConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */