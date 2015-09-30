/*    */ package tyRuBa.modes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bound
/*    */   extends BindingMode
/*    */ {
/* 10 */   public static Bound the = new Bound();
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 15 */     return getClass().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 19 */     return other instanceof Bound;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 23 */     return "B";
/*    */   }
/*    */   
/*    */   public boolean satisfyBinding(BindingMode mode) {
/* 27 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isBound() {
/* 31 */     return true;
/*    */   }
/*    */   
/* 34 */   public boolean isFree() { return false; }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/Bound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */