/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class OneMap
/*     */   implements Map, Serializable
/*     */ {
/*  25 */   private Object key = null;
/*  26 */   private Object value = null;
/*     */   
/*     */   public void clear() {
/*  29 */     this.key = null;
/*  30 */     this.value = null;
/*     */   }
/*     */   
/*     */   public int size() {
/*  34 */     return (this.key == null) && (this.value == null) ? 0 : 1;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  38 */     return size() == 0;
/*     */   }
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  42 */     return ((key == null) && (this.key == null)) || ((key != null) && (key.equals(this.key)));
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  46 */     return ((value == null) && (this.value == null)) || ((value != null) && (value.equals(this.value)));
/*     */   }
/*     */   
/*     */   public Object get(Object key) {
/*  50 */     return ((key == null) && (this.key == null)) || (
/*  51 */       (key != null) && (key.equals(this.key))) ? this.value : null;
/*     */   }
/*     */   
/*     */   public Object put(Object key, Object value) {
/*  55 */     Object previous = get(key);
/*     */     
/*  57 */     this.key = key;
/*  58 */     this.value = value;
/*     */     
/*  60 */     return previous;
/*     */   }
/*     */   
/*     */   public Object remove(Object key) {
/*  64 */     Object previous = get(key);
/*     */     
/*  66 */     if (containsKey(key)) {
/*  67 */       this.key = null;
/*  68 */       this.value = null;
/*     */     }
/*     */     
/*  71 */     return previous;
/*     */   }
/*     */   
/*     */   public void putAll(Map t) {
/*  75 */     Iterator m = t.entrySet().iterator();
/*     */     
/*  77 */     if (m.hasNext()) {
/*  78 */       Map.Entry e = (Map.Entry)m.next();
/*  79 */       put(e.getKey(), e.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public Set keySet() {
/*  84 */     return new KeySet(null);
/*     */   }
/*     */   
/*     */   private class Entry implements Map.Entry {
/*     */     private Object key;
/*     */     private Object value;
/*     */     
/*     */     public Entry(Object key, Object value) {
/*  92 */       this.key = key;
/*  93 */       this.value = value;
/*     */     }
/*     */     
/*  96 */     public Object getKey() { return this.key; }
/*     */     
/*  98 */     public Object getValue() { return this.value; }
/*     */     
/*     */     public Object setValue(Object value) {
/* 101 */       Object previous = this.value;
/* 102 */       this.value = value;
/* 103 */       OneMap.this.value = value;
/*     */       
/* 105 */       return previous;
/*     */     }
/*     */   }
/*     */   
/*     */   private class KeySet implements Set {
/*     */     private KeySet() {}
/*     */     
/* 112 */     public boolean add(Object object) { throw new UnsupportedOperationException(); }
/*     */     
/*     */     public boolean addAll(Collection c)
/*     */     {
/* 116 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 120 */       return OneMap.this.remove(object) != null;
/*     */     }
/*     */     
/*     */     public void removeAll() {
/* 124 */       clear();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection collection) {
/* 128 */       Iterator iterator = collection.iterator();
/*     */       
/* 130 */       while (iterator.hasNext()) {
/* 131 */         if (OneMap.this.containsKey(iterator.next()))
/* 132 */           return false;
/*     */       }
/* 134 */       clear();
/* 135 */       return true;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection collection) {
/* 139 */       Iterator iterator = collection.iterator();
/*     */       
/* 141 */       while (iterator.hasNext()) {
/* 142 */         if (remove(iterator.next()))
/* 143 */           return true;
/*     */       }
/* 145 */       return false;
/*     */     }
/*     */     
/*     */     public int size() {
/* 149 */       return OneMap.this.size();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 153 */       return OneMap.this.isEmpty();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 157 */       OneMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 161 */       return new SetIterator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private class SetIterator
/*     */       implements Iterator
/*     */     {
/* 169 */       private boolean exhausted = OneMap.KeySet.this.isEmpty();
/* 170 */       private boolean cleared = this.exhausted;
/*     */       
/*     */       public SetIterator() {}
/*     */       
/* 174 */       public boolean hasNext() { return !this.exhausted; }
/*     */       
/*     */       public Object next()
/*     */       {
/* 178 */         if (hasNext()) {
/* 179 */           this.exhausted = true;
/* 180 */           return OneMap.this.key;
/*     */         }
/* 182 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 186 */         if ((this.exhausted) && (!this.cleared)) {
/* 187 */           this.cleared = true;
/* 188 */           OneMap.this.clear();
/*     */         } else {
/* 190 */           throw new IllegalStateException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 195 */     public boolean contains(Object object) { if (OneMap.this.key == object) {
/* 196 */         return true;
/*     */       }
/* 198 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean containsAll(Collection collection)
/*     */     {
/* 204 */       return (collection.size() == 0) || ((collection.size() == 1) && (OneMap.this.size() == 1) && (contains(collection.iterator().next())));
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 208 */       if (OneMap.this.size() == 0) {
/* 209 */         return new Object[0];
/*     */       }
/* 211 */       return new Object[] { OneMap.this.key };
/*     */     }
/*     */     
/*     */     public Object[] toArray(Object[] a) {
/* 215 */       if (OneMap.this.size() == 0) {
/* 216 */         if (a.length > 0)
/* 217 */           a[0] = null;
/* 218 */         return a;
/*     */       }
/* 220 */       if (a.length >= 1) {
/* 221 */         a[0] = OneMap.this.key;
/* 222 */         if (a.length > 1) {
/* 223 */           a[1] = null;
/*     */         }
/* 225 */         return a;
/*     */       }
/* 227 */       Object[] r = (Object[])Array.newInstance(a.getClass(), 1);
/* 228 */       r[0] = OneMap.this.key;
/* 229 */       return r;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 236 */   public Set entrySet() { return new EntrySet(); }
/*     */   
/*     */   public class EntrySet implements Set {
/*     */     public EntrySet() {}
/*     */     
/* 241 */     public boolean add(Object object) { throw new UnsupportedOperationException(); }
/*     */     
/*     */     public boolean addAll(Collection c)
/*     */     {
/* 245 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 249 */       if (contains(object)) {
/* 250 */         clear();
/* 251 */         return true;
/*     */       }
/* 253 */       return false;
/*     */     }
/*     */     
/*     */     public void removeAll() {
/* 257 */       clear();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection collection) {
/* 261 */       Iterator iterator = collection.iterator();
/*     */       
/* 263 */       while (iterator.hasNext()) {
/* 264 */         if (contains(iterator.next()))
/* 265 */           return false;
/*     */       }
/* 267 */       clear();
/* 268 */       return true;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection collection) {
/* 272 */       Iterator iterator = collection.iterator();
/*     */       
/* 274 */       while (iterator.hasNext()) {
/* 275 */         if (remove(iterator.next())) {
/* 276 */           return true;
/*     */         }
/*     */       }
/* 279 */       return false;
/*     */     }
/*     */     
/*     */     public int size() {
/* 283 */       return OneMap.this.size();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 287 */       return OneMap.this.isEmpty();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 291 */       OneMap.this.clear();
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 295 */       return new SetIterator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private class SetIterator
/*     */       implements Iterator
/*     */     {
/* 303 */       private boolean exhausted = OneMap.EntrySet.this.isEmpty();
/* 304 */       private boolean cleared = this.exhausted;
/*     */       
/*     */       public SetIterator() {}
/*     */       
/* 308 */       public boolean hasNext() { return !this.exhausted; }
/*     */       
/*     */       public Object next()
/*     */       {
/* 312 */         if (hasNext()) {
/* 313 */           this.exhausted = true;
/* 314 */           return new OneMap.Entry(OneMap.this, OneMap.this.key, OneMap.this.value);
/*     */         }
/* 316 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 320 */         if ((this.exhausted) && (!this.cleared)) {
/* 321 */           this.cleared = true;
/* 322 */           OneMap.this.clear();
/*     */         } else {
/* 324 */           throw new IllegalStateException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 329 */     public boolean contains(Object object) { if ((object instanceof OneMap.Entry)) {
/* 330 */         OneMap.Entry entry = (OneMap.Entry)object;
/* 331 */         if ((OneMap.this.key == entry.getKey()) && (OneMap.this.value == entry.getValue())) {
/* 332 */           return true;
/*     */         }
/*     */       }
/* 335 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean containsAll(Collection collection)
/*     */     {
/* 341 */       return (collection.size() == 0) || ((collection.size() == 1) && (OneMap.this.size() == 1) && (contains(collection.iterator().next())));
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 345 */       if (isEmpty()) {
/* 346 */         return new Object[0];
/*     */       }
/* 348 */       return new Object[] { new OneMap.Entry(OneMap.this, OneMap.this.key, OneMap.this.value) };
/*     */     }
/*     */     
/*     */     public Object[] toArray(Object[] a) {
/* 352 */       if (OneMap.this.size() == 0) {
/* 353 */         if (a.length > 0)
/* 354 */           a[0] = null;
/* 355 */         return a;
/*     */       }
/* 357 */       if (a.length >= 1) {
/* 358 */         a[0] = new OneMap.Entry(OneMap.this, OneMap.this.key, OneMap.this.value);
/* 359 */         if (a.length > 1) {
/* 360 */           a[1] = null;
/*     */         }
/* 362 */         return a;
/*     */       }
/* 364 */       Object[] r = (Object[])Array.newInstance(a.getClass(), 1);
/* 365 */       r[0] = new OneMap.Entry(OneMap.this, OneMap.this.key, OneMap.this.value);
/* 366 */       return r;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 373 */   public Collection values() { return new Values(null); }
/*     */   
/*     */   private class Values implements Collection {
/*     */     private Values() {}
/*     */     
/* 378 */     public void clear() { OneMap.this.clear(); }
/*     */     
/*     */     public boolean add(Object object)
/*     */     {
/* 382 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection collection) {
/* 386 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object object) {
/* 390 */       if (contains(object)) {
/* 391 */         clear();
/* 392 */         return true;
/*     */       }
/* 394 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean containsAll(Collection collection)
/*     */     {
/* 400 */       return (collection.size() == 0) || ((collection.size() == 1) && (OneMap.this.size() == 1) && (contains(collection.iterator().next())));
/*     */     }
/*     */     
/*     */     public boolean contains(Object object) {
/* 404 */       return OneMap.this.containsValue(object);
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 408 */       return OneMap.this.isEmpty();
/*     */     }
/*     */     
/*     */     public int size() {
/* 412 */       return OneMap.this.size();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection collection) {
/* 416 */       Iterator iterator = collection.iterator();
/*     */       
/* 418 */       while (iterator.hasNext()) {
/* 419 */         if (contains(iterator.next()))
/* 420 */           return false;
/*     */       }
/* 422 */       clear();
/* 423 */       return true;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection collection) {
/* 427 */       Iterator iterator = collection.iterator();
/*     */       
/* 429 */       while (iterator.hasNext()) {
/* 430 */         if (remove(iterator.next()))
/* 431 */           return true;
/*     */       }
/* 433 */       return false;
/*     */     }
/*     */     
/*     */     public Iterator iterator() {
/* 437 */       return new ValueIterator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private class ValueIterator
/*     */       implements Iterator
/*     */     {
/* 445 */       private boolean exhausted = OneMap.Values.this.isEmpty();
/* 446 */       private boolean cleared = this.exhausted;
/*     */       
/*     */       public ValueIterator() {}
/*     */       
/* 450 */       public boolean hasNext() { return !this.exhausted; }
/*     */       
/*     */       public Object next()
/*     */       {
/* 454 */         if (hasNext()) {
/* 455 */           this.exhausted = true;
/* 456 */           return OneMap.this.value;
/*     */         }
/* 458 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 462 */         if ((this.exhausted) && (!this.cleared)) {
/* 463 */           this.cleared = true;
/* 464 */           OneMap.this.clear();
/*     */         } else {
/* 466 */           throw new IllegalStateException();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 471 */     public Object[] toArray() { if (isEmpty()) {
/* 472 */         return new Object[0];
/*     */       }
/* 474 */       return new Object[] { OneMap.this.value };
/*     */     }
/*     */     
/*     */     public Object[] toArray(Object[] a) {
/* 478 */       if (OneMap.this.size() == 0) {
/* 479 */         if (a.length > 0)
/* 480 */           a[0] = null;
/* 481 */         return a;
/*     */       }
/* 483 */       if (a.length >= 1) {
/* 484 */         a[0] = OneMap.this.value;
/* 485 */         if (a.length > 1) {
/* 486 */           a[1] = null;
/*     */         }
/* 488 */         return a;
/*     */       }
/* 490 */       Object[] r = (Object[])Array.newInstance(a.getClass(), 1);
/* 491 */       r[0] = OneMap.this.value;
/* 492 */       return r;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/OneMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */