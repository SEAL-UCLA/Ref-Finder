/*    */ package tyRuBa.engine;
/*    */ 
/*    */ 
/*    */ 
/*    */ class FormKey
/*    */ {
/*    */   RBTerm theKey;
/*    */   
/*    */ 
/* 10 */   FormKey(RBTerm t) { this.theKey = t; }
/*    */   
/*    */   public boolean equals(Object other) {
/* 13 */     if ((other instanceof FormKey)) {
/* 14 */       return this.theKey.sameForm(((FormKey)other).theKey);
/*    */     }
/*    */     
/* 17 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 21 */     return this.theKey.formHashCode();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/FormKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */