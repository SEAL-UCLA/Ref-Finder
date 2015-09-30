/*     */ package serp.util;
/*     */ 
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
/*  34 */     return ((LRUMap.OrderKey)this._values.firstKey()).key;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object lastKey()
/*     */   {
/*  40 */     return ((LRUMap.OrderKey)this._values.lastKey()).key;
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
/*  92 */     return new LRUMap.EntrySet(this, null);
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
/* 132 */     return new LRUMap.KeySet(this, null);
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
/* 178 */     return new LRUMap.ValueCollection(this, null);
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
/*     */   private synchronized LRUMap.OrderKey nextOrderKey(Object key)
/*     */   {
/* 193 */     LRUMap.OrderKey ok = new LRUMap.OrderKey(null);
/* 194 */     ok.key = key;
/* 195 */     ok.order = (this._order--);
/* 196 */     return ok;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/LRUMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */