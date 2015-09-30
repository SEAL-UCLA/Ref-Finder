/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DelayedElementSource
/*    */   extends ElementSource
/*    */ {
/* 14 */   private ElementSource delayed = null;
/*    */   
/*    */   private ElementSource delayed() {
/* 17 */     if (this.delayed == null) {
/* 18 */       this.delayed = produce();
/*    */     }
/* 20 */     return this.delayed;
/*    */   }
/*    */   
/*    */   public int status() {
/* 24 */     return delayed().status();
/*    */   }
/*    */   
/*    */ 
/*    */   protected abstract ElementSource produce();
/*    */   
/*    */ 
/*    */   public Object nextElement()
/*    */   {
/* 33 */     return delayed().nextElement();
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 37 */     p.print("Delayed(" + produceString());
/* 38 */     if (this.delayed != null) {
/* 39 */       p.indent();p.newline();
/* 40 */       this.delayed.print(p);
/* 41 */       p.outdent();
/*    */     }
/* 43 */     p.print(")");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected abstract String produceString();
/*    */   
/*    */ 
/*    */ 
/*    */   public void release()
/*    */   {
/* 54 */     if (this.delayed != null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 60 */       this.delayed.release();
/* 61 */       this.delayed = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/DelayedElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */