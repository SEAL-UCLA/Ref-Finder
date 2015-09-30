/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IteratorPair
/*    */   implements Iterator
/*    */ {
/*    */   Iterator[] iterators;
/*    */   int which;
/*    */   
/*    */   public IteratorPair(Iterator carIt, Iterator cdrIt)
/*    */   {
/* 18 */     this.iterators = new Iterator[] { carIt, cdrIt };
/*    */     
/* 20 */     this.which = 0;
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 24 */     if (this.which < 2) {
/* 25 */       if ((this.iterators[this.which] != null) && (this.iterators[this.which].hasNext())) {
/* 26 */         return true;
/*    */       }
/* 28 */       this.which += 1;
/* 29 */       return hasNext();
/*    */     }
/*    */     
/* 32 */     return false;
/*    */   }
/*    */   
/*    */   public Object next()
/*    */   {
/* 37 */     if (hasNext()) {
/* 38 */       return this.iterators[this.which].next();
/*    */     }
/* 40 */     throw new NoSuchElementException();
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 45 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/IteratorPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */