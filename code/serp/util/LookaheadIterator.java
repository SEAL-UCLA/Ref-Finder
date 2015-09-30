/*     */ package serp.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LookaheadIterator
/*     */   implements Iterator
/*     */ {
/*  25 */   private Iterator _itr = null;
/*  26 */   private ItrValue _next = new ItrValue();
/*     */   
/*     */ 
/*  29 */   private int _last = -1;
/*  30 */   private int _index = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator getIterator()
/*     */   {
/*  39 */     initialize();
/*  40 */     return this._itr;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  46 */     initialize();
/*  47 */     return this._next.valid;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object next()
/*     */   {
/*  53 */     initialize();
/*  54 */     if (!this._next.valid) {
/*  55 */       throw new NoSuchElementException();
/*     */     }
/*  57 */     Object next = this._next.value;
/*  58 */     setNext();
/*  59 */     return next;
/*     */   }
/*     */   
/*     */ 
/*     */   public void remove()
/*     */   {
/*  65 */     initialize();
/*     */     
/*     */ 
/*  68 */     Iterator itr = newIterator();
/*  69 */     for (int i = 0; i <= this._last; i++) {
/*  70 */       itr.next();
/*     */     }
/*     */     
/*  73 */     itr.remove();
/*     */     
/*     */ 
/*  76 */     this._index = (this._last - 1);
/*  77 */     this._itr = itr;
/*  78 */     setNext();
/*     */     
/*     */ 
/*  81 */     this._last = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Iterator newIterator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void processValue(ItrValue paramItrValue);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initialize()
/*     */   {
/* 113 */     if (this._itr == null)
/*     */     {
/* 115 */       this._itr = newIterator();
/* 116 */       setNext();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setNext()
/*     */   {
/* 126 */     this._next.value = null;
/* 127 */     this._next.valid = false;
/*     */     
/*     */ 
/* 130 */     int index = this._index;
/* 131 */     while (this._itr.hasNext())
/*     */     {
/* 133 */       this._next.value = this._itr.next();
/* 134 */       this._next.valid = true;
/* 135 */       index++;
/*     */       
/* 137 */       processValue(this._next);
/* 138 */       if (this._next.valid)
/*     */         break;
/*     */     }
/* 141 */     this._last = this._index;
/* 142 */     this._index = index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ItrValue
/*     */   {
/* 158 */     public Object value = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     public boolean valid = false;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/LookaheadIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */