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
/*  72 */     if ((value instanceof RefMapValue))
/*     */     {
/*  74 */       RefMapValue ref = (RefMapValue)value;
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
/*  93 */     if (!(value instanceof RefMapValue))
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
/* 106 */       if ((value instanceof RefMapValue))
/* 107 */         ((RefMapValue)value).invalidate();
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
/* 127 */     return new EntrySet(null);
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
/* 140 */     if (!(value instanceof RefMapValue))
/* 141 */       return value;
/* 142 */     return ((RefMapValue)value).getValue();
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
/* 154 */     return new KeySet(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 160 */     removeExpired();
/*     */     
/* 162 */     Object replaced = putFilter(key, value);
/* 163 */     if (!(replaced instanceof RefMapValue))
/* 164 */       return replaced;
/* 165 */     return ((RefMapValue)replaced).getValue();
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
/* 190 */     if ((replaced instanceof RefMapValue))
/* 191 */       ((RefMapValue)replaced).invalidate();
/* 192 */     return replaced;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 198 */     removeExpired();
/*     */     
/* 200 */     Object value = this._map.remove(key);
/* 201 */     if (!(value instanceof RefMapValue)) {
/* 202 */       return value;
/*     */     }
/* 204 */     RefMapValue ref = (RefMapValue)value;
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
/*     */   protected abstract RefMapValue createRefMapValue(Object paramObject1, Object paramObject2, ReferenceQueue paramReferenceQueue);
/*     */   
/*     */ 
/*     */ 
/*     */   private void removeExpired()
/*     */   {
/*     */     RefMapValue ref;
/*     */     
/* 237 */     while ((ref = (RefMapValue)this._queue.poll()) != null)
/*     */     {
/*     */       RefMapValue ref;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class MapEntry
/*     */     implements Map.Entry
/*     */   {
/* 292 */     Map.Entry _entry = null;
/*     */     
/*     */ 
/*     */     public MapEntry(Map.Entry entry)
/*     */     {
/* 297 */       this._entry = entry;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 303 */       return this._entry.getKey();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/* 309 */       Object value = this._entry.getValue();
/* 310 */       if (!(value instanceof RefValueMap.RefMapValue))
/* 311 */         return value;
/* 312 */       return ((RefValueMap.RefMapValue)value).getValue();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object setValue(Object value)
/*     */     {
/* 318 */       Object ret = this._entry.getValue();
/* 319 */       if (value == null) {
/* 320 */         this._entry.setValue(null);
/*     */       } else {
/* 322 */         this._entry.setValue(RefValueMap.this.createRefMapValue(this._entry.getKey(), 
/* 323 */           value, RefValueMap.this._queue));
/*     */       }
/* 325 */       if (!(ret instanceof RefValueMap.RefMapValue)) {
/* 326 */         return ret;
/*     */       }
/* 328 */       RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)ret;
/* 329 */       ref.invalidate();
/* 330 */       return ref.getValue();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 336 */       if (other == this)
/* 337 */         return true;
/* 338 */       if (!(other instanceof Map.Entry)) {
/* 339 */         return false;
/*     */       }
/* 341 */       Object key = getKey();
/* 342 */       Object key2 = ((Map.Entry)other).getKey();
/* 343 */       if (((key == null) && (key2 != null)) || (
/* 344 */         (key != null) && (!key.equals(key2)))) {
/* 345 */         return false;
/*     */       }
/* 347 */       Object val = getValue();
/* 348 */       Object val2 = ((Map.Entry)other).getValue();
/*     */       
/* 350 */       return ((val == null) && (val2 == null)) || ((val != null) && (val2.equals(val2)));
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
/* 363 */       return RefValueMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean add(Object o)
/*     */     {
/* 369 */       Map.Entry entry = (Map.Entry)o;
/* 370 */       RefValueMap.this.put(entry.getKey(), entry.getValue());
/* 371 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 377 */       return new EntryIterator(null);
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
/* 389 */         return RefValueMap.this._map.entrySet().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 395 */         Map.Entry entry = (Map.Entry)value.value;
/*     */         
/* 397 */         if ((entry.getValue() instanceof RefValueMap.RefMapValue))
/*     */         {
/* 399 */           RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)entry.getValue();
/* 400 */           if (ref.getValue() == null)
/* 401 */             value.valid = false;
/*     */         }
/* 403 */         value.value = new RefValueMap.MapEntry(RefValueMap.this, entry);
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
/*     */ 
/*     */     public int size()
/*     */     {
/* 417 */       return RefValueMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 423 */       return new ValueIterator(null);
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
/* 435 */         return RefValueMap.this._map.values().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 441 */         if ((value.value instanceof RefValueMap.RefMapValue))
/*     */         {
/* 443 */           RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)value.value;
/* 444 */           if (ref.getValue() == null) {
/* 445 */             value.valid = false;
/*     */           } else {
/* 447 */             value.value = ref.getValue();
/*     */           }
/*     */         }
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
/*     */     public int size()
/*     */     {
/* 462 */       return RefValueMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 468 */       return new KeyIterator(null);
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
/* 480 */         return RefValueMap.this._map.entrySet().iterator();
/*     */       }
/*     */       
/*     */ 
/*     */       protected void processValue(LookaheadIterator.ItrValue value)
/*     */       {
/* 486 */         Map.Entry entry = (Map.Entry)value.value;
/*     */         
/* 488 */         if ((entry.getValue() instanceof RefValueMap.RefMapValue))
/*     */         {
/* 490 */           RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)entry.getValue();
/* 491 */           if (ref.getValue() == null)
/* 492 */             value.valid = false;
/*     */         }
/* 494 */         value.value = entry.getKey();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface RefMapValue
/*     */   {
/*     */     public abstract Object getKey();
/*     */     
/*     */     public abstract Object getValue();
/*     */     
/*     */     public abstract boolean isValid();
/*     */     
/*     */     public abstract void invalidate();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/RefValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */