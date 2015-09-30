/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class MapElementSource
/*    */   extends ElementSource
/*    */ {
/* 11 */   private Object next = null;
/*    */   private ElementSource remaining;
/*    */   private Action action;
/*    */   
/*    */   public MapElementSource(ElementSource on, Action what) {
/* 16 */     this.action = what;
/* 17 */     this.remaining = on;
/*    */   }
/*    */   
/*    */   public int status()
/*    */   {
/* 22 */     if (this.next == null) {
/* 23 */       advance();
/*    */     }
/* 25 */     if (this.next == null) {
/* 26 */       if (this.remaining == null) {
/* 27 */         return -1;
/*    */       }
/* 29 */       int result = this.remaining.status();
/* 30 */       return result;
/*    */     }
/*    */     
/*    */ 
/* 34 */     return 1;
/*    */   }
/*    */   
/*    */   public Object nextElement() {
/* 38 */     if (status() == 1) {
/* 39 */       Object theNext = this.next;
/* 40 */       this.next = null;
/* 41 */       return theNext;
/*    */     }
/*    */     
/* 44 */     throw new NoSuchElementException("MapElementSource");
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 48 */     p.print("Map(");
/* 49 */     p.indent();
/* 50 */     if (this.next != null) {
/* 51 */       p.print("ready=" + this.next + " ");
/*    */     }
/* 53 */     p.print("Action= " + this.action.toString());p.newline();
/* 54 */     p.print("on =");
/* 55 */     p.indent();
/* 56 */     if (this.remaining == null) {
/* 57 */       p.print("null");
/*    */     } else
/* 59 */       this.remaining.print(p);
/* 60 */     p.outdent();
/* 61 */     p.outdent();
/* 62 */     p.print(")");
/*    */   }
/*    */   
/*    */   private int advance() {
/* 66 */     int result = 1;
/* 67 */     this.next = null;
/*    */     
/* 69 */     while ((this.next == null) && ((result = this.remaining.status()) == 1))
/*    */     {
/* 71 */       this.next = this.action.compute(this.remaining.nextElement());
/*    */     }
/*    */     
/* 74 */     if (result == -1) {
/* 75 */       this.remaining = ElementSource.theEmpty;
/*    */     }
/* 77 */     return result;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 81 */     return (this.next == null) && (this.remaining.isEmpty());
/*    */   }
/*    */   
/*    */   public ElementSource first() {
/* 85 */     if (this.next != null) {
/* 86 */       return ElementSource.singleton(this.next);
/*    */     }
/*    */     
/* 89 */     return this.remaining.first().map(this.action);
/*    */   }
/*    */   
/*    */   public void release() {
/* 93 */     super.release();
/* 94 */     this.next = null;
/* 95 */     this.action = null;
/* 96 */     if (this.remaining != null) {
/* 97 */       this.remaining.release();
/* 98 */       this.remaining = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/MapElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */