/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeMapping
/*    */   implements Serializable
/*    */ {
/*    */   private ConstructorType functor;
/*    */   
/*    */   public abstract Class getMappedClass();
/*    */   
/*    */   public abstract Object toTyRuBa(Object paramObject);
/*    */   
/*    */   public abstract Object toJava(Object paramObject);
/*    */   
/*    */   public ConstructorType getFunctor()
/*    */   {
/* 46 */     return this.functor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setFunctor(ConstructorType functor)
/*    */   {
/* 54 */     if (this.functor != null) {
/* 55 */       throw new Error("Double mapping for " + this + ": " + this.functor + " and " + functor);
/*    */     }
/* 57 */     this.functor = functor;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/TypeMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */