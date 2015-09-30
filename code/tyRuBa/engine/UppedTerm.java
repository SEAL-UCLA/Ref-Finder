/*    */ package tyRuBa.engine;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UppedTerm
/*    */ {
/*    */   RBTerm term;
/*    */   
/*    */ 
/*    */ 
/*    */   public UppedTerm(RBTerm t)
/*    */   {
/* 13 */     this.term = t;
/*    */   }
/*    */   
/*    */   public RBTerm down() {
/* 17 */     return this.term;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 21 */     return this.term.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/UppedTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */