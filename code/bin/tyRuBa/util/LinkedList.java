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
/*    */ 
/*    */ public class LinkedList
/*    */ {
/* 14 */   private LinkedList.Bucket head = new LinkedList.Bucket(this, "dummy");
/*    */   
/* 16 */   private LinkedList.Bucket tail = this.head;
/*    */   
/*    */   public void addElement(Object e)
/*    */   {
/* 20 */     this.tail.next = new LinkedList.Bucket(this, e);
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
/* 32 */     return new LinkedList.1(this);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/LinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */