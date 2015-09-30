/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmptyIterator
/*    */   implements Iterator
/*    */ {
/* 13 */   public static final EmptyIterator the = new EmptyIterator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public Object next() {
/* 24 */     throw new NoSuchElementException();
/*    */   }
/*    */   
/*    */   public void remove() {
/* 28 */     throw new IllegalStateException();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/EmptyIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */