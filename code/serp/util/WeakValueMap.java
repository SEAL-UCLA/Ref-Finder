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
/*     */ public class WeakValueMap
/*     */   extends RefValueMap
/*     */ {
/*     */   public WeakValueMap() {}
/*     */   
/*     */   public WeakValueMap(Map map)
/*     */   {
/*  53 */     super(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RefValueMap.RefMapValue createRefMapValue(Object key, Object value, ReferenceQueue queue)
/*     */   {
/*  60 */     return new WeakMapValue(key, value, queue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class WeakMapValue
/*     */     extends WeakReference
/*     */     implements RefValueMap.RefMapValue
/*     */   {
/*  72 */     private Object _key = null;
/*  73 */     private boolean _valid = true;
/*     */     
/*     */ 
/*     */     public WeakMapValue(Object key, Object value, ReferenceQueue queue)
/*     */     {
/*  78 */       super(queue);
/*  79 */       this._key = key;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/*  85 */       return this._key;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/*  91 */       return get();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isValid()
/*     */     {
/*  97 */       return this._valid;
/*     */     }
/*     */     
/*     */ 
/*     */     public void invalidate()
/*     */     {
/* 103 */       this._valid = false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 109 */       if (this == other)
/* 110 */         return true;
/* 111 */       if (other == null) {
/* 112 */         return false;
/*     */       }
/* 114 */       if (!(other instanceof WeakMapValue)) {
/* 115 */         return (get() != null) && (get().equals(other));
/*     */       }
/* 117 */       return (get() != null) && (get().equals(
/* 118 */         ((WeakMapValue)other).get()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/WeakValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */