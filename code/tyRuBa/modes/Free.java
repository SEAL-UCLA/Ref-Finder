/*    */ package tyRuBa.modes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Free
/*    */   extends BindingMode
/*    */ {
/* 10 */   public static Free the = new Free();
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 15 */     return getClass().hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 19 */     return other instanceof Free;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 23 */     return "F";
/*    */   }
/*    */   
/*    */   public boolean satisfyBinding(BindingMode mode)
/*    */   {
/* 28 */     return equals(mode);
/*    */   }
/*    */   
/*    */   public boolean isBound() {
/* 32 */     return false;
/*    */   }
/*    */   
/* 35 */   public boolean isFree() { return true; }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/Free.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */