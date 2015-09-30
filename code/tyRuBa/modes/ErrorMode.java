/*    */ package tyRuBa.modes;
/*    */ 
/*    */ public class ErrorMode extends Mode
/*    */ {
/*    */   String msg;
/*    */   
/*    */   public ErrorMode(String msg) {
/*  8 */     super(Multiplicity.zero, Multiplicity.infinite);
/*  9 */     this.msg = msg;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 13 */     return "ERROR: " + this.msg;
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 17 */     if ((other instanceof ErrorMode)) {
/* 18 */       return this.msg.equals(((ErrorMode)other).msg);
/*    */     }
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 25 */     return 122 + this.msg.hashCode();
/*    */   }
/*    */   
/*    */   public Mode add(Mode other) {
/* 29 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/ErrorMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */