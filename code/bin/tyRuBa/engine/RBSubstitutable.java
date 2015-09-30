/*    */ package tyRuBa.engine;
/*    */ 
/*    */ 
/*    */ public abstract class RBSubstitutable
/*    */   extends RBTerm
/*    */ {
/*    */   protected String name;
/*    */   
/*    */   RBSubstitutable(String name)
/*    */   {
/* 11 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String name() {
/* 15 */     return this.name;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 19 */     return this.name.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 28 */     return ((obj instanceof RBSubstitutable)) && (((RBSubstitutable)obj).name == this.name);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBSubstitutable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */