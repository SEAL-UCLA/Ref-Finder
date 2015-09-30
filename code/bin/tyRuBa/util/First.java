/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class First
/*    */   extends ElementSource
/*    */ {
/*    */   private ElementSource source;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public First(ElementSource from)
/*    */   {
/* 20 */     this.source = from;
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 24 */     p.print("First(");
/* 25 */     this.source.print(p);
/* 26 */     p.outdent();
/* 27 */     p.print(")");
/*    */   }
/*    */   
/*    */   public int status() {
/* 31 */     if (this.source == null) {
/* 32 */       return -1;
/*    */     }
/* 34 */     int stat = this.source.status();
/* 35 */     if (this.source.status() == -1) {
/* 36 */       this.source = null;
/*    */     }
/* 38 */     return stat;
/*    */   }
/*    */   
/*    */   public Object nextElement()
/*    */   {
/* 43 */     ElementSource it = this.source;
/* 44 */     this.source = null;
/* 45 */     return it.nextElement();
/*    */   }
/*    */   
/*    */   public ElementSource first() {
/* 49 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/First.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */