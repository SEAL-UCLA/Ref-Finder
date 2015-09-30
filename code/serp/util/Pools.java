/*     */ package serp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pools
/*     */ {
/*     */   public static Pool synchronizedPool(Pool pool)
/*     */   {
/*  20 */     if (pool == null)
/*  21 */       throw new NullPointerException();
/*  22 */     return new SynchronizedPool(pool);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SynchronizedPool
/*     */     implements Pool, Serializable
/*     */   {
/*  32 */     private Pool _pool = null;
/*     */     
/*     */ 
/*     */     public SynchronizedPool(Pool pool)
/*     */     {
/*  37 */       this._pool = pool;
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int getMaxPool()
/*     */     {
/*  43 */       return this._pool.getMaxPool();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void setMaxPool(int max)
/*     */     {
/*  49 */       this._pool.setMaxPool(max);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int getMinPool()
/*     */     {
/*  55 */       return this._pool.getMinPool();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void setMinPool(int min)
/*     */     {
/*  61 */       this._pool.setMinPool(min);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int getWait()
/*     */     {
/*  67 */       return this._pool.getWait();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void setWait(int millis)
/*     */     {
/*  73 */       this._pool.setWait(millis);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int getAutoReturn()
/*     */     {
/*  79 */       return this._pool.getAutoReturn();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void setAutoReturn(int millis)
/*     */     {
/*  85 */       this._pool.setAutoReturn(millis);
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/*  91 */       return this._pool.iterator();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int size()
/*     */     {
/*  97 */       return this._pool.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean isEmpty()
/*     */     {
/* 103 */       return this._pool.isEmpty();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean contains(Object obj)
/*     */     {
/* 109 */       return this._pool.contains(obj);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean containsAll(Collection c)
/*     */     {
/* 115 */       return this._pool.containsAll(c);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Object[] toArray()
/*     */     {
/* 121 */       return this._pool.toArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Object[] toArray(Object[] fill)
/*     */     {
/* 127 */       return this._pool.toArray(fill);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean add(Object obj)
/*     */     {
/* 133 */       return this._pool.add(obj);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean addAll(Collection c)
/*     */     {
/* 139 */       return this._pool.addAll(c);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean remove(Object obj)
/*     */     {
/* 145 */       return this._pool.remove(obj);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean removeAll(Collection c)
/*     */     {
/* 151 */       return this._pool.removeAll(c);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean retainAll(Collection c)
/*     */     {
/* 157 */       return this._pool.retainAll(c);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void clear()
/*     */     {
/* 163 */       this._pool.clear();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized boolean equals(Object obj)
/*     */     {
/* 169 */       return this._pool.equals(obj);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized int hashCode()
/*     */     {
/* 175 */       return this._pool.hashCode();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Object get()
/*     */     {
/* 181 */       return this._pool.get();
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Object get(Object match)
/*     */     {
/* 187 */       return this._pool.get(match);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Object get(Object match, Comparator comp)
/*     */     {
/* 193 */       return this._pool.get(match, comp);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized Set takenSet()
/*     */     {
/* 199 */       return this._pool.takenSet();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/Pools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */