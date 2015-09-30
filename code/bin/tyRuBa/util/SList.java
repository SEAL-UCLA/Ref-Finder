/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SList
/*    */ {
/*    */   private Object object;
/*    */   
/*    */   private SList next;
/*    */   
/*    */ 
/*    */   public final Object object()
/*    */   {
/* 14 */     return this.object;
/*    */   }
/*    */   
/*    */   public final SList next() {
/* 18 */     return this.next;
/*    */   }
/*    */   
/*    */   public SList(Object object, SList next) {
/* 22 */     this.object = object;
/* 23 */     this.next = next;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/SList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */