/*     */ package serp.util;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LRUMap
/*     */   implements SortedMap
/*     */ {
/*  21 */   private Map _orders = new HashMap();
/*  22 */   private TreeMap _values = new TreeMap();
/*  23 */   private int _order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */   public Comparator comparator()
/*     */   {
/*  28 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object firstKey()
/*     */   {
/*  34 */     return ((OrderKey)this._values.firstKey()).key;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object lastKey()
/*     */   {
/*  40 */     return ((OrderKey)this._values.lastKey()).key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap headMap(Object toKey)
/*     */   {
/*  49 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap subMap(Object fromKey, Object toKey)
/*     */   {
/*  58 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortedMap tailMap(Object fromKey)
/*     */   {
/*  67 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/*  73 */     this._orders.clear();
/*  74 */     this._values.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/*  80 */     return this._orders.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/*  86 */     return this._values.containsValue(value);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set entrySet()
/*     */   {
/*  92 */     return new EntrySet(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  98 */     if (other == this)
/*  99 */       return true;
/* 100 */     if (!(other instanceof Map)) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     return new HashMap(this).equals(other);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 110 */     Object order = this._orders.remove(key);
/* 111 */     if (order == null) {
/* 112 */       return null;
/*     */     }
/*     */     
/* 115 */     Object value = this._values.remove(order);
/* 116 */     order = nextOrderKey(key);
/* 117 */     this._orders.put(key, order);
/* 118 */     this._values.put(order, value);
/*     */     
/* 120 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 126 */     return this._orders.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set keySet()
/*     */   {
/* 132 */     return new KeySet(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 138 */     Object order = nextOrderKey(key);
/* 139 */     Object oldOrder = this._orders.put(key, order);
/*     */     
/* 141 */     Object rem = null;
/* 142 */     if (oldOrder != null) {
/* 143 */       rem = this._values.remove(oldOrder);
/*     */     }
/* 145 */     this._values.put(order, value);
/* 146 */     return rem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void putAll(Map map)
/*     */   {
/* 153 */     for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
/*     */     {
/* 155 */       Map.Entry entry = (Map.Entry)itr.next();
/* 156 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 163 */     Object order = this._orders.remove(key);
/* 164 */     if (order != null)
/* 165 */       return this._values.remove(order);
/* 166 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 172 */     return this._orders.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection values()
/*     */   {
/* 178 */     return new ValueCollection(null);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 184 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized OrderKey nextOrderKey(Object key)
/*     */   {
/* 193 */     OrderKey ok = new OrderKey(null);
/* 194 */     ok.key = key;
/* 195 */     ok.order = (this._order--);
/* 196 */     return ok;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class OrderKey
/*     */     implements Comparable
/*     */   {
/* 206 */     public Object key = null;
/* 207 */     public int order = 0;
/*     */     
/*     */ 
/*     */     public int compareTo(Object other)
/*     */     {
/* 212 */       return this.order - ((OrderKey)other).order;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class MapEntry
/*     */     implements Map.Entry
/*     */   {
/* 223 */     private Map.Entry _valuesEntry = null;
/*     */     
/*     */ 
/*     */     public MapEntry(Map.Entry valuesEntry)
/*     */     {
/* 228 */       this._valuesEntry = valuesEntry;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 234 */       LRUMap.OrderKey ok = (LRUMap.OrderKey)this._valuesEntry.getKey();
/* 235 */       return ok.key;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/* 241 */       return this._valuesEntry.getValue();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object setValue(Object value)
/*     */     {
/* 247 */       return this._valuesEntry.setValue(value);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 253 */       if (other == this)
/* 254 */         return true;
/* 255 */       if (!(other instanceof Map.Entry)) {
/* 256 */         return false;
/*     */       }
/* 258 */       Object key = getKey();
/* 259 */       Object key2 = ((Map.Entry)other).getKey();
/* 260 */       if (((key == null) && (key2 != null)) || (
/* 261 */         (key != null) && (!key.equals(key2)))) {
/* 262 */         return false;
/*     */       }
/* 264 */       Object val = getValue();
/* 265 */       Object val2 = ((Map.Entry)other).getValue();
/*     */       
/* 267 */       return ((val == null) && (val2 == null)) || ((val != null) && (val2.equals(val2)));
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
/* 280 */       return LRUMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean add(Object o)
/*     */     {
/* 286 */       Map.Entry entry = (Map.Entry)o;
/* 287 */       LRUMap.this.put(entry.getKey(), entry.getValue());
/* 288 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 294 */       final Iterator valuesItr = LRUMap.this._values.entrySet().iterator();
/*     */       
/* 296 */       new Iterator()
/*     */       {
/* 298 */         private LRUMap.MapEntry _last = null;
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 303 */           return valuesItr.hasNext();
/*     */         }
/*     */         
/*     */ 
/*     */         public Object next()
/*     */         {
/* 309 */           this._last = new LRUMap.MapEntry((Map.Entry)valuesItr.next());
/* 310 */           return this._last;
/*     */         }
/*     */         
/*     */ 
/*     */         public void remove()
/*     */         {
/* 316 */           valuesItr.remove();
/* 317 */           LRUMap.this._orders.remove(this._last.getKey());
/*     */         }
/*     */       };
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
/* 332 */       return LRUMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 338 */       final Iterator keysItr = LRUMap.this._values.keySet().iterator();
/*     */       
/* 340 */       new Iterator()
/*     */       {
/* 342 */         private Object _last = null;
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 347 */           return keysItr.hasNext();
/*     */         }
/*     */         
/*     */ 
/*     */         public Object next()
/*     */         {
/* 353 */           this._last = ((LRUMap.OrderKey)keysItr.next()).key;
/* 354 */           return this._last;
/*     */         }
/*     */         
/*     */ 
/*     */         public void remove()
/*     */         {
/* 360 */           keysItr.remove();
/* 361 */           LRUMap.this._orders.remove(this._last);
/*     */         }
/*     */       };
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
/* 376 */       return LRUMap.this.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 382 */       final Iterator valuesItr = LRUMap.this._values.entrySet().iterator();
/*     */       
/* 384 */       new Iterator()
/*     */       {
/* 386 */         private Object _last = null;
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 391 */           return valuesItr.hasNext();
/*     */         }
/*     */         
/*     */ 
/*     */         public Object next()
/*     */         {
/* 397 */           Map.Entry entry = (Map.Entry)valuesItr.next();
/* 398 */           this._last = ((LRUMap.OrderKey)entry.getKey()).key;
/* 399 */           return entry.getValue();
/*     */         }
/*     */         
/*     */ 
/*     */         public void remove()
/*     */         {
/* 405 */           valuesItr.remove();
/* 406 */           LRUMap.this._orders.remove(this._last);
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/LRUMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */