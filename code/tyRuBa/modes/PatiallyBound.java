/*    */ package tyRuBa.modes;
/*    */ 
/*    */ public class PatiallyBound extends BindingMode
/*    */ {
/*  5 */   public static PatiallyBound the = new PatiallyBound();
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 10 */     return getClass().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 14 */     return other instanceof PatiallyBound;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 18 */     return "BF";
/*    */   }
/*    */   
/*    */   public boolean satisfyBinding(BindingMode mode)
/*    */   {
/* 23 */     return mode instanceof Free;
/*    */   }
/*    */   
/*    */   public boolean isBound() {
/* 27 */     return false;
/*    */   }
/*    */   
/* 30 */   public boolean isFree() { return false; }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/PatiallyBound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */