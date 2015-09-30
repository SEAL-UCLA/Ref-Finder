/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mutex
/*    */ {
/* 10 */   private int waiting = -1;
/*    */   
/*    */   public synchronized void obtain()
/*    */   {
/* 14 */     this.waiting += 1;
/* 15 */     if (this.waiting == 0)
/*    */     {
/* 17 */       return;
/*    */     }
/*    */     try {
/* 20 */       wait();
/*    */     } catch (InterruptedException localInterruptedException) {
/* 22 */       throw new Error("This should not happen!");
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized void release()
/*    */   {
/* 28 */     if (this.waiting > 0) {
/* 29 */       notify();
/*    */     }
/* 31 */     this.waiting -= 1;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/Mutex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */