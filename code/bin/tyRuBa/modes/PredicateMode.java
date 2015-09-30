/*    */ package tyRuBa.modes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PredicateMode
/*    */ {
/*    */   private BindingList paramModes;
/*    */   
/*    */ 
/*    */ 
/*    */   private Mode mode;
/*    */   
/*    */ 
/*    */ 
/*    */   private boolean toBeCheck;
/*    */   
/*    */ 
/*    */ 
/*    */   public PredicateMode(BindingList paramModes, Mode mode, boolean toBeCheck)
/*    */   {
/* 22 */     this.paramModes = paramModes;
/* 23 */     this.mode = mode;
/* 24 */     this.toBeCheck = toBeCheck;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 28 */     return this.paramModes.hashCode() + 
/* 29 */       13 * (this.mode.hashCode() + 13 * getClass().hashCode());
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 33 */     if ((other instanceof PredicateMode)) {
/* 34 */       PredicateMode cother = (PredicateMode)other;
/*    */       
/* 36 */       return (this.paramModes.equals(cother.paramModes)) && (this.mode.equals(cother.mode));
/*    */     }
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 43 */     return this.paramModes + " IS " + this.mode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BindingList getParamModes()
/*    */   {
/* 51 */     return this.paramModes;
/*    */   }
/*    */   
/*    */   public Mode getMode() {
/* 55 */     return (Mode)this.mode.clone();
/*    */   }
/*    */   
/*    */   public boolean toBeCheck() {
/* 59 */     return this.toBeCheck;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/PredicateMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */