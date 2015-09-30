/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
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
/*     */ public class SoftKeyMap
/*     */   extends RefKeyMap
/*     */ {
/*     */   public SoftKeyMap() {}
/*     */   
/*     */   public SoftKeyMap(Map map)
/*     */   {
/*  54 */     super(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RefKeyMap.RefMapKey createRefMapKey(Object key, ReferenceQueue queue, boolean identity)
/*     */   {
/*  61 */     if (queue == null) {
/*  62 */       return new SoftMapKey(key, identity);
/*     */     }
/*  64 */     return new SoftMapKey(key, queue, identity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class SoftMapKey
/*     */     extends SoftReference
/*     */     implements RefKeyMap.RefMapKey
/*     */   {
/*  75 */     private boolean _identity = false;
/*     */     
/*     */ 
/*     */     public SoftMapKey(Object key, boolean identity)
/*     */     {
/*  80 */       super();
/*  81 */       this._identity = identity;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public SoftMapKey(Object key, ReferenceQueue queue, boolean identity)
/*     */     {
/*  88 */       super(queue);
/*  89 */       this._identity = identity;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/*  95 */       return get();
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 101 */       Object obj = get();
/* 102 */       if (obj == null) {
/* 103 */         return 0;
/*     */       }
/* 105 */       if (this._identity)
/* 106 */         return System.identityHashCode(obj);
/* 107 */       return obj.hashCode();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 113 */       if (this == other) {
/* 114 */         return true;
/*     */       }
/* 116 */       if ((other instanceof RefKeyMap.RefMapKey)) {
/* 117 */         other = ((RefKeyMap.RefMapKey)other).getKey();
/*     */       }
/* 119 */       Object obj = get();
/* 120 */       if (obj == null)
/* 121 */         return false;
/* 122 */       if (this._identity)
/* 123 */         return obj == other;
/* 124 */       return obj.equals(other);
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(Object other)
/*     */     {
/* 130 */       if (this == other) {
/* 131 */         return 0;
/*     */       }
/* 133 */       Object key = getKey();
/*     */       Object otherKey;
/* 135 */       Object otherKey; if ((other instanceof RefKeyMap.RefMapKey)) {
/* 136 */         otherKey = ((RefKeyMap.RefMapKey)other).getKey();
/*     */       } else {
/* 138 */         otherKey = other;
/*     */       }
/* 140 */       if ((key == null) && (otherKey == null))
/* 141 */         return 0;
/* 142 */       if ((key == null) && (otherKey != null))
/* 143 */         return -1;
/* 144 */       if (otherKey == null) {
/* 145 */         return 1;
/*     */       }
/* 147 */       if (!(key instanceof Comparable)) {
/* 148 */         return System.identityHashCode(otherKey) - 
/* 149 */           System.identityHashCode(key);
/*     */       }
/* 151 */       return ((Comparable)key).compareTo(otherKey);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/SoftKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */