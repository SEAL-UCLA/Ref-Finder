/*     */ package serp.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPool
/*     */   implements Pool
/*     */ {
/*  20 */   private static Comparator COMP_TRUE = new Comparator()
/*     */   {
/*     */     public int compare(Object o1, Object o2)
/*     */     {
/*  24 */       return 0;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  30 */   private static Comparator COMP_EQUAL = new Comparator()
/*     */   {
/*     */     public int compare(Object o1, Object o2)
/*     */     {
/*  34 */       if ((o1 == o2) || ((o1 != null) && (o2 != null) && (o1.equals(o2))))
/*  35 */         return 0;
/*  36 */       return System.identityHashCode(o1) < System.identityHashCode(o2) ? 
/*  37 */         -1 : 1;
/*     */     }
/*     */   };
/*     */   
/*  41 */   private int _min = 0;
/*  42 */   private int _max = 0;
/*  43 */   private int _wait = 0;
/*  44 */   private int _autoReturn = 0;
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
/*     */   public AbstractPool() {}
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
/*     */   public AbstractPool(int min, int max, int wait, int autoReturn)
/*     */   {
/*  68 */     setMinPool(min);
/*  69 */     setMaxPool(max);
/*  70 */     setWait(wait);
/*  71 */     setAutoReturn(autoReturn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractPool(Collection c)
/*     */   {
/*  81 */     addAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxPool()
/*     */   {
/*  87 */     return this._max;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMaxPool(int max)
/*     */   {
/*  93 */     if ((max < 0) || (max < this._min))
/*  94 */       throw new IllegalArgumentException(String.valueOf(max));
/*  95 */     this._max = max;
/*     */     
/*     */ 
/*  98 */     if (this._max > 0)
/*     */     {
/* 100 */       int trim = size() + takenMap().size() - this._max;
/* 101 */       if (trim > 0)
/*     */       {
/* 103 */         Iterator itr = freeSet().iterator();
/* 104 */         for (int i = 0; (i < trim) && (itr.hasNext()); i++)
/*     */         {
/* 106 */           itr.next();
/* 107 */           itr.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMinPool()
/*     */   {
/* 116 */     return this._min;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMinPool(int min)
/*     */   {
/* 122 */     if ((min < 0) || ((this._max > 0) && (min > this._max)))
/* 123 */       throw new IllegalArgumentException(String.valueOf(min));
/* 124 */     this._min = min;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getWait()
/*     */   {
/* 130 */     return this._wait;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setWait(int millis)
/*     */   {
/* 136 */     if (millis < 0)
/* 137 */       throw new IllegalArgumentException(String.valueOf(millis));
/* 138 */     this._wait = millis;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getAutoReturn()
/*     */   {
/* 144 */     return this._autoReturn;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAutoReturn(int millis)
/*     */   {
/* 150 */     if (millis < 0)
/* 151 */       throw new IllegalArgumentException(String.valueOf(millis));
/* 152 */     this._autoReturn = millis;
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 158 */     new Iterator()
/*     */     {
/* 160 */       private Iterator _itr = AbstractPool.this.freeSet().iterator();
/*     */       
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 165 */         return this._itr.hasNext();
/*     */       }
/*     */       
/*     */ 
/*     */       public Object next()
/*     */       {
/* 171 */         return this._itr.next();
/*     */       }
/*     */       
/*     */ 
/*     */       public void remove()
/*     */       {
/* 177 */         if (AbstractPool.this.size() + AbstractPool.this.takenMap().size() <= AbstractPool.this._min) {
/* 178 */           throw new IllegalStateException();
/*     */         }
/* 180 */         this._itr.remove();
/* 181 */         synchronized (AbstractPool.this)
/*     */         {
/* 183 */           AbstractPool.this.notifyAll();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 192 */     return freeSet().size();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 198 */     return size() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contains(Object obj)
/*     */   {
/* 204 */     return freeSet().contains(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsAll(Collection c)
/*     */   {
/* 210 */     return freeSet().containsAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 216 */     return freeSet().toArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object[] toArray(Object[] fill)
/*     */   {
/* 222 */     return freeSet().toArray(fill);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean add(Object obj)
/*     */   {
/* 228 */     if (obj == null) {
/* 229 */       return false;
/*     */     }
/* 231 */     Map taken = takenMap();
/* 232 */     boolean removed = takenMap().remove(obj) != null;
/* 233 */     boolean added = ((this._max == 0) || (size() + taken.size() < this._max)) && 
/* 234 */       (freeSet().add(obj));
/*     */     
/* 236 */     if ((removed) || (added))
/*     */     {
/* 238 */       synchronized (this)
/*     */       {
/* 240 */         notifyAll();
/*     */       }
/*     */     }
/*     */     
/* 244 */     return added;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean addAll(Collection c)
/*     */   {
/* 250 */     boolean ret = false;
/* 251 */     for (Iterator itr = c.iterator(); itr.hasNext();) {
/* 252 */       ret = (add(itr.next())) || (ret);
/*     */     }
/* 254 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object obj)
/*     */   {
/* 260 */     if (size() + takenMap().size() <= this._min) {
/* 261 */       return false;
/*     */     }
/* 263 */     if (freeSet().remove(obj))
/*     */     {
/* 265 */       synchronized (this)
/*     */       {
/* 267 */         notifyAll();
/*     */       }
/* 269 */       return true;
/*     */     }
/* 271 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeAll(Collection c)
/*     */   {
/* 277 */     boolean ret = false;
/* 278 */     for (Iterator itr = c.iterator(); itr.hasNext();) {
/* 279 */       ret = (remove(itr.next())) || (ret);
/*     */     }
/* 281 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean retainAll(Collection c)
/*     */   {
/* 288 */     Collection remove = new LinkedList();
/* 289 */     for (Iterator itr = freeSet().iterator(); itr.hasNext();)
/*     */     {
/* 291 */       Object next = itr.next();
/* 292 */       if (!c.contains(next))
/* 293 */         remove.add(next);
/*     */     }
/* 295 */     return removeAll(remove);
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/* 301 */     freeSet().clear();
/* 302 */     takenMap().clear();
/* 303 */     synchronized (this)
/*     */     {
/* 305 */       notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 312 */     if (obj == this)
/* 313 */       return true;
/* 314 */     if (!(obj instanceof Pool)) {
/* 315 */       return false;
/*     */     }
/* 317 */     Pool p = (Pool)obj;
/* 318 */     return (p.size() == size()) && (p.containsAll(this));
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 324 */     int sum = 0;
/*     */     
/* 326 */     for (Iterator itr = freeSet().iterator(); itr.hasNext();)
/*     */     {
/* 328 */       Object next = itr.next();
/* 329 */       sum += (next == null ? 0 : next.hashCode());
/*     */     }
/*     */     
/* 332 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get()
/*     */   {
/* 343 */     return get(null, COMP_TRUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(Object match)
/*     */   {
/* 355 */     return get(match, COMP_EQUAL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(Object match, Comparator comp)
/*     */   {
/* 375 */     if ((comp == null) && (match == null)) {
/* 376 */       comp = COMP_TRUE;
/* 377 */     } else if (comp == null) {
/* 378 */       comp = COMP_EQUAL;
/*     */     }
/* 380 */     Object obj = find(match, comp);
/* 381 */     if (obj != null) {
/* 382 */       return obj;
/*     */     }
/*     */     
/* 385 */     long now = System.currentTimeMillis();
/* 386 */     long end = now + this._wait;
/* 387 */     while (now < end)
/*     */     {
/*     */ 
/* 390 */       synchronized (this) {
/*     */         try {
/* 392 */           wait(end - now);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */       }
/* 396 */       obj = find(match, comp);
/* 397 */       if (obj != null) {
/* 398 */         return obj;
/*     */       }
/* 400 */       now = System.currentTimeMillis();
/*     */     }
/*     */     
/* 403 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set takenSet()
/*     */   {
/* 409 */     return Collections.unmodifiableSet(takenMap().keySet());
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
/*     */ 
/*     */   protected Object find(Object match, Comparator comp)
/*     */   {
/* 426 */     clean();
/*     */     
/*     */ 
/* 429 */     if (size() == 0) {
/* 430 */       return null;
/*     */     }
/*     */     
/* 433 */     Map taken = takenMap();
/* 434 */     Object next = null;
/* 435 */     for (Iterator itr = freeSet().iterator(); itr.hasNext();)
/*     */     {
/* 437 */       next = itr.next();
/* 438 */       if (comp.compare(match, next) == 0)
/*     */       {
/* 440 */         itr.remove();
/* 441 */         taken.put(next, new Long(System.currentTimeMillis()));
/* 442 */         return next;
/*     */       }
/*     */     }
/*     */     
/* 446 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clean()
/*     */   {
/* 458 */     if (this._autoReturn > 0)
/*     */     {
/* 460 */       Collection back = null;
/* 461 */       long now = System.currentTimeMillis();
/*     */       
/* 463 */       for (Iterator itr = takenMap().entrySet().iterator(); 
/* 464 */             itr.hasNext();)
/*     */       {
/* 466 */         Map.Entry entry = (Map.Entry)itr.next();
/*     */         
/* 468 */         if (entry.getKey() == null) {
/* 469 */           itr.remove();
/*     */         }
/* 471 */         else if (((Long)entry.getValue()).longValue() + this._autoReturn < now)
/*     */         {
/* 473 */           if (back == null)
/* 474 */             back = new LinkedList();
/* 475 */           back.add(entry.getKey());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 480 */       if (back != null) {
/* 481 */         addAll(back);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract Set freeSet();
/*     */   
/*     */   protected abstract Map takenMap();
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/AbstractPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */