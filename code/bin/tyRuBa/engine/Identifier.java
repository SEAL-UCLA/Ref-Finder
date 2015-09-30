/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Identifier
/*    */   implements Serializable
/*    */ {
/*    */   protected String name;
/*    */   protected int arity;
/*    */   
/*    */   public Identifier(String name, int arity)
/*    */   {
/* 19 */     this.name = name;
/* 20 */     this.arity = arity;
/*    */   }
/*    */   
/*    */   public boolean equals(Object arg) {
/* 24 */     if (arg.getClass().equals(getClass())) {
/* 25 */       Identifier other = (Identifier)arg;
/* 26 */       return (this.name.equals(other.name)) && (this.arity == other.arity);
/*    */     }
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 33 */     return getClass().hashCode() * this.arity + this.name.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 37 */     return this.name + "/" + this.arity;
/*    */   }
/*    */   
/*    */   public int getArity() {
/* 41 */     return this.arity;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/Identifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */