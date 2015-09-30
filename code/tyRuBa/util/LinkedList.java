/*    */ package tyRuBa.util;
/*    */ 
/*    */ public class LinkedList {
/*    */   private class Bucket {
/*    */     Object el;
/*    */     
/*  7 */     Bucket(Object e, Bucket r) { this.el = e;this.next = r; } Bucket(Object e) { this.el = e; } Bucket next = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 14 */   private Bucket head = new Bucket("dummy");
/*    */   
/* 16 */   private Bucket tail = this.head;
/*    */   
/*    */   public void addElement(Object e)
/*    */   {
/* 20 */     this.tail.next = new Bucket(e);
/* 21 */     this.tail = this.tail.next;
/*    */   }
/*    */   
/*    */   public boolean isEmpty()
/*    */   {
/* 26 */     return this.head.next == null;
/*    */   }
/*    */   
/*    */ 
/*    */   public RemovableElementSource elements()
/*    */   {
/* 32 */     new RemovableElementSource() {
/* 33 */       private LinkedList.Bucket pos = LinkedList.this.head;
/*    */       
/*    */       public int status() {
/* 36 */         if (this.pos.next != null) {
/* 37 */           return 1;
/*    */         }
/* 39 */         return 0;
/*    */       }
/*    */       
/*    */       public Object peekNextElement() {
/* 43 */         return this.pos.next.el;
/*    */       }
/*    */       
/*    */       public void removeNextElement() {
/* 47 */         if ((this.pos.next = this.pos.next.next) == null)
/* 48 */           LinkedList.this.tail = this.pos;
/*    */       }
/*    */       
/*    */       public Object nextElement() {
/* 52 */         this.pos = this.pos.next;
/* 53 */         return this.pos.el;
/*    */       }
/*    */       
/*    */       public void print(PrintingState p) {
/* 57 */         p.print("Linked[");
/* 58 */         for (LinkedList.Bucket current = this.pos.next; current != null; current = current.next) {
/* 59 */           p.printObj(current.el);
/* 60 */           if (current.next != null)
/* 61 */             p.print(",");
/*    */         }
/* 63 */         p.print("]");
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/LinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */