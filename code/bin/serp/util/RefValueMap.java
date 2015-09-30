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
/*     */ abstract class RefValueMap
/*     */   implements RefMap
/*     */ {
/*  36 */   private Map _map = null;
/*  37 */   private ReferenceQueue _queue = new ReferenceQueue();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RefValueMap()
/*     */   {
/*  45 */     this(new HashMap());
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
/*     */   public RefValueMap(Map map)
/*     */   {
/*  59 */     this._map = map;
/*  60 */     this._map.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeHard(Object key)
/*     */   {
/*  66 */     removeExpired();
/*     */     
/*  68 */     if (!containsKey(key)) {
/*  69 */       return false;
/*     */     }
/*  71 */     Object value = this._map.get(key);
/*  72 */     if ((value instanceof RefValueMap.RefMapValue))
/*     */     {
/*  74 */       RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)value;
/*  75 */       value = ref.getValue();
/*  76 */       if (value == null) {
/*  77 */         return false;
/*     */       }
/*  79 */       ref.invalidate();
/*  80 */       this._map.put(key, value);
/*     */     }
/*  82 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeReference(Object key)
/*     */   {
/*  88 */     removeExpired();
/*     */     
/*  90 */     Object value = this._map.get(key);
/*  91 */     if (value == null)
/*  92 */       return false;
/*  93 */     if (!(value instanceof RefValueMap.RefMapValue))
/*  94 */       put(key, value);
/*  95 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/* 101 */     Collection values = this._map.values();
/*     */     
/* 103 */     for (Iterator itr = values.iterator(); itr.hasNext();)
/*     */     {
/* 105 */       Object value = itr.next();
/* 106 */       if ((value instanceof RefValueMap.RefMapValue))
/* 107 */         ((RefValueMap.RefMapValue)value).invalidate();
/* 108 */       itr.remove();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 115 */     return this._map.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 121 */     return values().contains(value);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set entrySet()
/*     */   {
/* 127 */     return new RefValueMap.EntrySet(this, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 133 */     return this._map.equals(other);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 139 */     Object value = this._map.get(key);
/* 140 */     if (!(value instanceof RefValueMap.RefMapValue))
/* 141 */       return value;
/* 142 */     return ((RefValueMap.RefMapValue)value).getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 148 */     return this._map.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set keySet()
/*     */   {
/* 154 */     return new RefValueMap.KeySet(this, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 160 */     removeExpired();
/*     */     
/* 162 */     Object replaced = putFilter(key, value);
/* 163 */     if (!(replaced instanceof RefValueMap.RefMapValue))
/* 164 */       return replaced;
/* 165 */     return ((RefValueMap.RefMapValue)replaced).getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public void putAll(Map map)
/*     */   {
/* 171 */     removeExpired();
/*     */     
/*     */ 
/* 174 */     for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
/*     */     {
/* 176 */       Map.Entry entry = (Map.Entry)itr.next();
/* 177 */       putFilter(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private Object putFilter(Object key, Object value)
/*     */   {
/*     */     Object replaced;
/*     */     Object replaced;
/* 185 */     if (value == null) {
/* 186 */       replaced = this._map.put(key, null);
/*     */     } else {
/* 188 */       replaced = this._map.put(key, createRefMapValue(key, value, this._queue));
/*     */     }
/* 190 */     if ((replaced instanceof RefValueMap.RefMapValue))
/* 191 */       ((RefValueMap.RefMapValue)replaced).invalidate();
/* 192 */     return replaced;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 198 */     removeExpired();
/*     */     
/* 200 */     Object value = this._map.remove(key);
/* 201 */     if (!(value instanceof RefValueMap.RefMapValue)) {
/* 202 */       return value;
/*     */     }
/* 204 */     RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)value;
/* 205 */     ref.invalidate();
/* 206 */     return ref.getValue();
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
/* 218 */     return new RefValueMap.ValueCollection(this, null);
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
/*     */   protected abstract RefValueMap.RefMapValue createRefMapValue(Object paramObject1, Object paramObject2, ReferenceQueue paramReferenceQueue);
/*     */   
/*     */ 
/*     */ 
/*     */   private void removeExpired()
/*     */   {
/*     */     RefValueMap.RefMapValue ref;
/*     */     
/* 237 */     while ((ref = (RefValueMap.RefMapValue)this._queue.poll()) != null)
/*     */     {
/*     */       RefValueMap.RefMapValue ref;
/*     */       try {
/* 241 */         this._queue.remove(1L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */       
/*     */ 
/* 246 */       if (ref.isValid()) {
/* 247 */         this._map.remove(ref.getKey());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/RefValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */