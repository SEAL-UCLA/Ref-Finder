/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
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
/* 137 */     return new EntrySet(null);
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
/* 163 */     return new KeySet(null);
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
/* 218 */     return new ValueCollection(null);
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
/*     */   protected abstract RefMapKey createRefMapKey(Object paramObject, ReferenceQueue paramReferenceQueue, boolean paramBoolean);
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
/*     */   private static final class MapEntry
/*     */     implements Map.Entry
/*     */   {
/* 284 */     Map.Entry _entry = null;
/*     */     
/*     */ 
/*     */     public MapEntry(Map.Entry entry)
/*     */     {
/* 289 */       this._entry = entry;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 295 */       Object key = this._entry.getKey();
/* 296 */       if (!(key instanceof RefKeyMap.RefMapKey))
/* 297 */         return key;
/* 298 */       return ((RefKeyMap.RefMapKey)key).getKey();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/* 304 */       return this._entry.getValue();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object setValue(Object value)
/*     */     {
/* 310 */       return this._entry.setValue(value);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 316 */       if (other == this)
/* 317 */         return true;
/* 318 */       if (!(other instanceof Map.Entry)) {
/* 319 */         return false;
/*     */       }
/* 321 */       Object key = getKey();
/* 322 */       Object key2 = ((Map.Entry)other).getKey();
/* 323 */       if (((key == null) && (key2 != null)) || (
/* 324 */         (key != null) && (!key.equals(key2)))) {
/* 325 */         return false;
/*     */       }
/* 327 */       Object val = getValue();
/* 328 */       Object val2 = ((Map.Entry)other).getValue();
/*     */       
/* 330 */       return ((val == null) && (val2 == null)) || ((val != null) && (val2.equals(val2)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class EntrySet
/*     */     extends AbstractSet
/*     */   {
/*     */     private EntrySet() {}
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 343 */       return RefKeyMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean add(Object o)
/*     */     {
/* 349 */       Map.Entry entry = (Map.Entry)o;
/* 350 */       RefKeyMap.this.put(entry.getKey(), entry.getValue());
/* 351 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 357 */       return new EntryIterator(null);
/*     */     }
/*     */     
/*     */ 
/*     */     private class EntryIterator
/*     */       extends LookaheadIterator
/*     */     {
/*     */       private EntryIterator() {}
/*     */       
/*     */ 
/*     */       protected Iterator newIterator()
/*     */       {
/* 369 */         return RefKeyMap.this._map.entrySet().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 375 */         Map.Entry entry = (Map.Entry)value.value;
/*     */         
/* 377 */         if ((entry.getKey() instanceof RefKeyMap.RefMapKey))
/*     */         {
/* 379 */           RefKeyMap.RefMapKey ref = (RefKeyMap.RefMapKey)entry.getKey();
/* 380 */           if (ref.getKey() == null)
/* 381 */             value.valid = false;
/*     */         }
/* 383 */         value.value = new RefKeyMap.MapEntry(entry);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class KeySet
/*     */     extends AbstractSet
/*     */   {
/*     */     private KeySet() {}
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 397 */       return RefKeyMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 403 */       return new KeyIterator(null);
/*     */     }
/*     */     
/*     */ 
/*     */     private class KeyIterator
/*     */       extends LookaheadIterator
/*     */     {
/*     */       private KeyIterator() {}
/*     */       
/*     */ 
/*     */       protected Iterator newIterator()
/*     */       {
/* 415 */         return RefKeyMap.this._map.keySet().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 421 */         if ((value.value instanceof RefKeyMap.RefMapKey))
/*     */         {
/* 423 */           RefKeyMap.RefMapKey ref = (RefKeyMap.RefMapKey)value.value;
/* 424 */           if (ref.getKey() == null) {
/* 425 */             value.valid = false;
/*     */           } else {
/* 427 */             value.value = ref.getKey();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class ValueCollection
/*     */     extends AbstractCollection
/*     */   {
/*     */     private ValueCollection() {}
/*     */     
/*     */     public int size()
/*     */     {
/* 442 */       return RefKeyMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 448 */       return new ValueIterator(null);
/*     */     }
/*     */     
/*     */ 
/*     */     private class ValueIterator
/*     */       extends LookaheadIterator
/*     */     {
/*     */       private ValueIterator() {}
/*     */       
/*     */ 
/*     */       protected Iterator newIterator()
/*     */       {
/* 460 */         return RefKeyMap.this._map.entrySet().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 466 */         Map.Entry entry = (Map.Entry)value.value;
/*     */         
/* 468 */         if ((entry.getKey() instanceof RefKeyMap.RefMapKey))
/*     */         {
/* 470 */           RefKeyMap.RefMapKey ref = (RefKeyMap.RefMapKey)entry.getKey();
/* 471 */           if (ref.getKey() == null)
/* 472 */             value.valid = false;
/*     */         }
/* 474 */         value.value = entry.getValue();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface RefMapKey
/*     */     extends Comparable
/*     */   {
/*     */     public abstract Object getKey();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/RefKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */