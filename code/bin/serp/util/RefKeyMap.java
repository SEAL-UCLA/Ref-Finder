/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ abstract class RefKeyMap
/*     */   implements RefMap
/*     */ {
/*  42 */   private Map _map = null;
/*  43 */   private ReferenceQueue _queue = new ReferenceQueue();
/*  44 */   private boolean _identity = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RefKeyMap()
/*     */   {
/*  53 */     this(new HashMap());
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
/*     */   public RefKeyMap(Map map)
/*     */   {
/*  69 */     if ((map instanceof IdentityMap))
/*     */     {
/*  71 */       this._identity = true;
/*  72 */       this._map = new HashMap();
/*     */     }
/*     */     else
/*     */     {
/*  76 */       this._map = map;
/*  77 */       this._map.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeHard(Object key)
/*     */   {
/*  84 */     removeExpired();
/*     */     
/*     */ 
/*  87 */     if (!containsKey(key)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     Object value = remove(key);
/*  92 */     this._map.put(key, value);
/*  93 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeReference(Object key)
/*     */   {
/*  99 */     removeExpired();
/*     */     
/*     */ 
/* 102 */     if (key == null) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     if (!containsKey(key)) {
/* 107 */       return false;
/*     */     }
/* 109 */     Object value = remove(key);
/* 110 */     put(key, value);
/* 111 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/* 117 */     this._map.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 123 */     if (key == null)
/* 124 */       return this._map.containsKey(null);
/* 125 */     return this._map.containsKey(createRefMapKey(key, null, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 131 */     return this._map.containsValue(value);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set entrySet()
/*     */   {
/* 137 */     return new RefKeyMap.EntrySet(this, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 143 */     return this._map.equals(other);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 149 */     if (key == null)
/* 150 */       return this._map.get(null);
/* 151 */     return this._map.get(createRefMapKey(key, null, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 157 */     return this._map.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set keySet()
/*     */   {
/* 163 */     return new RefKeyMap.KeySet(this, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 169 */     removeExpired();
/* 170 */     return putFilter(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public void putAll(Map map)
/*     */   {
/* 176 */     removeExpired();
/*     */     
/*     */ 
/* 179 */     for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
/*     */     {
/* 181 */       Map.Entry entry = (Map.Entry)itr.next();
/* 182 */       putFilter(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Object putFilter(Object key, Object value)
/*     */   {
/* 189 */     if (key == null) {
/* 190 */       return this._map.put(null, value);
/*     */     }
/*     */     
/*     */ 
/* 194 */     key = createRefMapKey(key, this._queue, this._identity);
/* 195 */     Object ret = this._map.remove(key);
/* 196 */     this._map.put(key, value);
/* 197 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 203 */     removeExpired();
/* 204 */     if (key == null)
/* 205 */       return this._map.remove(null);
/* 206 */     return this._map.remove(createRefMapKey(key, null, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 212 */     return this._map.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection values()
/*     */   {
/* 218 */     return new RefKeyMap.ValueCollection(this, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 224 */     return this._map.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract RefKeyMap.RefMapKey createRefMapKey(Object paramObject, ReferenceQueue paramReferenceQueue, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeExpired()
/*     */   {
/*     */     Object key;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 249 */     while ((key = this._queue.poll()) != null)
/*     */     {
/*     */       Object key;
/*     */       try {
/* 253 */         this._queue.remove(1L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */       
/*     */ 
/* 258 */       this._map.remove(key);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/RefKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */