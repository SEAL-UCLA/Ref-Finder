/*    */ package serp.util;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class MultiIterator
/*    */   implements Iterator
/*    */ {
/*    */   private static final int PAST_END = -1;
/* 18 */   private Iterator _itr = Collections.EMPTY_LIST.iterator();
/* 19 */   private Iterator _last = null;
/* 20 */   private int _index = 0;
/*    */   
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 25 */     setIterator();
/* 26 */     return this._itr.hasNext();
/*    */   }
/*    */   
/*    */ 
/*    */   public Object next()
/*    */   {
/* 32 */     setIterator();
/* 33 */     return this._itr.next();
/*    */   }
/*    */   
/*    */ 
/*    */   public void remove()
/*    */   {
/* 39 */     setIterator();
/* 40 */     this._last.remove();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract Iterator newIterator(int paramInt);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void setIterator()
/*    */   {
/* 61 */     if (this._index == -1) {
/* 62 */       return;
/*    */     }
/*    */     
/* 65 */     this._last = this._itr;
/*    */     
/*    */ 
/* 68 */     Iterator newItr = this._itr;
/* 69 */     while ((newItr != null) && (!newItr.hasNext())) {
/* 70 */       newItr = newIterator(this._index++);
/*    */     }
/*    */     
/* 73 */     if ((newItr != null) && (this._itr != newItr)) {
/* 74 */       this._itr = newItr;
/* 75 */     } else if (newItr == null) {
/* 76 */       this._index = -1;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/MultiIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */