/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
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
/*     */ public class CountedHashMap
/*     */   implements Serializable
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_SIZE = 32;
/*     */   private static final int MAX_CONCURRENCY = 32;
/*     */   private static final double DEFAULT_LOAD_FACTOR = 0.75D;
/*  66 */   private transient Object[] mutex = new Object[32];
/*     */   
/*     */   private CountedHashMap.Entry[] table;
/*     */   
/*     */   private int size;
/*     */   
/*     */   private double loadFactor;
/*     */   
/*     */   private int threshold;
/*     */   
/*     */   private void rehash(boolean grow)
/*     */   {
/*  78 */     rehash(0, grow);
/*     */   }
/*     */   
/*     */   private void rehash(int obtained, boolean grow) {
/*  82 */     if (obtained == 32) {
/*  83 */       CountedHashMap.Entry[] ntable = grow ? new CountedHashMap.Entry[this.table.length * 2] : new CountedHashMap.Entry[this.table.length / 2];
/*  84 */       int location = 0;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  89 */       while (location < this.table.length) {
/*  90 */         CountedHashMap.Entry bucket = this.table[location];
/*     */         
/*  92 */         while (bucket != null) {
/*  93 */           CountedHashMap.Entry next = bucket.next;
/*     */           
/*  95 */           int nlocation = (bucket.key % ntable.length + ntable.length) % ntable.length;
/*     */           
/*  97 */           bucket.next = ntable[nlocation];
/*  98 */           ntable[nlocation] = bucket;
/*     */           
/* 100 */           bucket = next;
/*     */         }
/*     */         
/* 103 */         location++;
/*     */       }
/*     */       
/* 106 */       this.table = ntable;
/*     */       
/* 108 */       this.threshold = ((int)(this.table.length * this.loadFactor));
/*     */     } else {
/* 110 */       synchronized (this.mutex[(obtained++)]) { rehash(obtained, grow);
/*     */       }
/*     */     } }
/*     */   
/* 114 */   public CountedHashMap() { this(32, 0.75D); }
/*     */   
/*     */   public CountedHashMap(int initialSize)
/*     */   {
/* 118 */     this(initialSize, 0.75D);
/*     */   }
/*     */   
/*     */   public CountedHashMap(double loadFactor) {
/* 122 */     this(32, loadFactor);
/*     */   }
/*     */   
/*     */   public CountedHashMap(int initialSize, double loadFactor)
/*     */   {
/*  67 */     int i = 32;
/*  68 */     while (i-- != 0) {
/*  69 */       this.mutex[i] = new Object();
/*     */     }
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
/* 127 */     this.table = new CountedHashMap.Entry[initialSize];
/* 128 */     this.size = 0;
/* 129 */     this.loadFactor = loadFactor;
/* 130 */     this.threshold = ((int)(this.table.length * loadFactor));
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
/*     */   public boolean containsKey(int key)
/*     */   {
/* 146 */     int length = this.table.length;
/* 147 */     int location = (key % length + length) % length;
/*     */     
/* 149 */     synchronized (this.mutex[(location % 32)]) {
/* 150 */       if (length == this.table.length) {
/* 151 */         CountedHashMap.Entry bucket = this.table[location];
/* 152 */         while (bucket != null) {
/* 153 */           if (key == bucket.key) {
/* 154 */             return true;
/*     */           }
/* 156 */           bucket = bucket.next;
/*     */         }
/*     */         
/* 159 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 163 */     return containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Object get(int key)
/*     */   {
/* 172 */     int length = this.table.length;
/* 173 */     int location = (key % length + length) % length;
/*     */     
/* 175 */     synchronized (this.mutex[(location % 32)]) {
/* 176 */       if (length == this.table.length) {
/* 177 */         CountedHashMap.Entry bucket = this.table[location];
/* 178 */         while (bucket != null) {
/* 179 */           if (key == bucket.key) {
/* 180 */             return bucket.value;
/*     */           }
/* 182 */           bucket = bucket.next;
/*     */         }
/*     */         
/* 185 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 189 */     return get(key);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 193 */     return this.size == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object put(int key, Object value)
/*     */   {
/* 200 */     int length = this.table.length;
/* 201 */     int location = (key % length + length) % length;
/*     */     
/* 203 */     synchronized (this.mutex[(location % 32)]) {
/* 204 */       if (length == this.table.length)
/*     */       {
/*     */ 
/* 207 */         CountedHashMap.Entry bucket = this.table[location];
/*     */         
/* 209 */         while (bucket != null) {
/* 210 */           if (key == bucket.key) {
/* 211 */             Object previous = bucket.value;
/* 212 */             bucket.value = value;
/* 213 */             bucket.preIncrementCount();
/* 214 */             return previous;
/*     */           }
/* 216 */           bucket = bucket.next;
/*     */         }
/*     */         
/* 219 */         this.table[location] = new CountedHashMap.Entry(key, value, this.table[location]);
/*     */         
/* 221 */         synchronized (this.mutex) {
/* 222 */           if (++this.size > this.threshold) {
/* 223 */             rehash(true);
/*     */           }
/*     */         }
/* 226 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 230 */     return put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(int key)
/*     */   {
/* 236 */     int length = this.table.length;
/* 237 */     int location = (key % length + length) % length;
/*     */     
/* 239 */     synchronized (this.mutex[(location % 32)]) {
/* 240 */       if (length == this.table.length) {
/* 241 */         CountedHashMap.Entry previous = null;
/* 242 */         CountedHashMap.Entry bucket = this.table[location];
/*     */         
/* 244 */         while (bucket != null) {
/* 245 */           if (key == bucket.key) {
/* 246 */             if (bucket.preDecrementCount() == 0) {
/* 247 */               if (previous == null) {
/* 248 */                 this.table[location] = bucket.next;
/*     */               } else {
/* 250 */                 previous.next = bucket.next;
/*     */               }
/* 252 */               synchronized (this.mutex) {
/* 253 */                 if (--this.size < this.threshold / 3) {
/* 254 */                   rehash(false);
/*     */                 }
/*     */               }
/*     */             }
/* 258 */             return bucket.value;
/*     */           }
/* 260 */           previous = bucket;
/* 261 */           bucket = bucket.next;
/*     */         }
/*     */         
/*     */ 
/* 265 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 269 */     return remove(key);
/*     */   }
/*     */   
/*     */   public boolean setFlags(int key, int flags) {
/* 273 */     int length = this.table.length;
/* 274 */     int location = (key % length + length) % length;
/*     */     
/* 276 */     synchronized (this.mutex[(location % 32)]) {
/* 277 */       if (length == this.table.length) {
/* 278 */         CountedHashMap.Entry bucket = this.table[location];
/*     */         
/* 280 */         while (bucket != null) {
/* 281 */           if (key == bucket.key) {
/* 282 */             bucket.setFlags(flags);
/* 283 */             return true;
/*     */           }
/* 285 */           bucket = bucket.next;
/*     */         }
/*     */         
/* 288 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 292 */     return setFlags(key, flags);
/*     */   }
/*     */   
/*     */   public int getFlags(int key) {
/* 296 */     int length = this.table.length;
/* 297 */     int location = (key % length + length) % length;
/*     */     
/* 299 */     synchronized (this.mutex[(location % 32)]) {
/* 300 */       if (length == this.table.length) {
/* 301 */         CountedHashMap.Entry bucket = this.table[location];
/*     */         
/* 303 */         while (bucket != null) {
/* 304 */           if (key == bucket.key) {
/* 305 */             return bucket.getFlags();
/*     */           }
/* 307 */           bucket = bucket.next;
/*     */         }
/*     */         
/* 310 */         return 0;
/*     */       }
/*     */     }
/*     */     
/* 314 */     return getFlags(key);
/*     */   }
/*     */   
/*     */   public int size() {
/* 318 */     return this.size;
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 322 */     in.defaultReadObject();
/*     */     
/* 324 */     this.mutex = new Object[32];
/* 325 */     int i = 32;
/* 326 */     while (i-- != 0) {
/* 327 */       this.mutex[i] = new Object();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/CountedHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */