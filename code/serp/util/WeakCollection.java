/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeakCollection
/*     */   extends RefValueCollection
/*     */ {
/*     */   public WeakCollection() {}
/*     */   
/*     */   public WeakCollection(Collection coll)
/*     */   {
/*  52 */     super(coll);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RefValueCollection.RefValue createRefValue(Object value, ReferenceQueue queue, boolean identity)
/*     */   {
/*  59 */     if (queue == null) {
/*  60 */       return new WeakValue(value, identity);
/*     */     }
/*  62 */     return new WeakValue(value, queue, identity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class WeakValue
/*     */     extends WeakReference
/*     */     implements RefValueCollection.RefValue
/*     */   {
/*  73 */     private boolean _identity = false;
/*     */     
/*     */ 
/*     */     public WeakValue(Object value, boolean identity)
/*     */     {
/*  78 */       super();
/*  79 */       this._identity = identity;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public WeakValue(Object value, ReferenceQueue queue, boolean identity)
/*     */     {
/*  86 */       super(queue);
/*  87 */       this._identity = identity;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/*  93 */       return get();
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/*  99 */       Object obj = get();
/* 100 */       if (obj == null) {
/* 101 */         return 0;
/*     */       }
/* 103 */       if (this._identity)
/* 104 */         return System.identityHashCode(obj);
/* 105 */       return obj.hashCode();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 111 */       if (this == other) {
/* 112 */         return true;
/*     */       }
/* 114 */       if ((other instanceof RefValueCollection.RefValue)) {
/* 115 */         other = ((RefValueCollection.RefValue)other).getValue();
/*     */       }
/* 117 */       Object obj = get();
/* 118 */       if (obj == null)
/* 119 */         return false;
/* 120 */       if (this._identity)
/* 121 */         return obj == other;
/* 122 */       return obj.equals(other);
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(Object other)
/*     */     {
/* 128 */       if (this == other) {
/* 129 */         return 0;
/*     */       }
/* 131 */       Object value = getValue();
/*     */       Object otherValue;
/* 133 */       Object otherValue; if ((other instanceof RefValueCollection.RefValue)) {
/* 134 */         otherValue = ((RefValueCollection.RefValue)other).getValue();
/*     */       } else {
/* 136 */         otherValue = other;
/*     */       }
/* 138 */       if ((value == null) && (otherValue == null))
/* 139 */         return 0;
/* 140 */       if ((value == null) && (otherValue != null))
/* 141 */         return -1;
/* 142 */       if (otherValue == null) {
/* 143 */         return 1;
/*     */       }
/* 145 */       if (!(value instanceof Comparable)) {
/* 146 */         return System.identityHashCode(otherValue) - 
/* 147 */           System.identityHashCode(value);
/*     */       }
/* 149 */       return ((Comparable)value).compareTo(otherValue);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/WeakCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */