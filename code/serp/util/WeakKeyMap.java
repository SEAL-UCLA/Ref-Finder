/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Map;
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
/*     */ public class WeakKeyMap
/*     */   extends RefKeyMap
/*     */ {
/*     */   public WeakKeyMap() {}
/*     */   
/*     */   public WeakKeyMap(Map map)
/*     */   {
/*  54 */     super(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RefKeyMap.RefMapKey createRefMapKey(Object key, ReferenceQueue queue, boolean identity)
/*     */   {
/*  61 */     if (queue == null) {
/*  62 */       return new WeakMapKey(key, identity);
/*     */     }
/*  64 */     return new WeakMapKey(key, queue, identity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class WeakMapKey
/*     */     extends WeakReference
/*     */     implements RefKeyMap.RefMapKey
/*     */   {
/*  75 */     private boolean _identity = false;
/*  76 */     private int _hash = 0;
/*     */     
/*     */ 
/*     */     public WeakMapKey(Object key, boolean identity)
/*     */     {
/*  81 */       super();
/*  82 */       this._identity = identity;
/*  83 */       this._hash = (identity ? System.identityHashCode(key) : key.hashCode());
/*     */     }
/*     */     
/*     */ 
/*     */     public WeakMapKey(Object key, ReferenceQueue queue, boolean identity)
/*     */     {
/*  89 */       super(queue);
/*  90 */       this._identity = identity;
/*  91 */       this._hash = (identity ? System.identityHashCode(key) : key.hashCode());
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/*  97 */       return get();
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 103 */       return this._hash;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 109 */       if (this == other) {
/* 110 */         return true;
/*     */       }
/* 112 */       if ((other instanceof RefKeyMap.RefMapKey)) {
/* 113 */         other = ((RefKeyMap.RefMapKey)other).getKey();
/*     */       }
/* 115 */       Object obj = get();
/* 116 */       if (obj == null)
/* 117 */         return false;
/* 118 */       if (this._identity)
/* 119 */         return obj == other;
/* 120 */       return obj.equals(other);
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(Object other)
/*     */     {
/* 126 */       if (this == other) {
/* 127 */         return 0;
/*     */       }
/* 129 */       Object key = getKey();
/*     */       Object otherKey;
/* 131 */       Object otherKey; if ((other instanceof RefKeyMap.RefMapKey)) {
/* 132 */         otherKey = ((RefKeyMap.RefMapKey)other).getKey();
/*     */       } else {
/* 134 */         otherKey = other;
/*     */       }
/* 136 */       if (key == null)
/* 137 */         return -1;
/* 138 */       if (otherKey == null) {
/* 139 */         return 1;
/*     */       }
/* 141 */       if (!(key instanceof Comparable)) {
/* 142 */         return System.identityHashCode(otherKey) - 
/* 143 */           System.identityHashCode(key);
/*     */       }
/* 145 */       return ((Comparable)key).compareTo(otherKey);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/WeakKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */