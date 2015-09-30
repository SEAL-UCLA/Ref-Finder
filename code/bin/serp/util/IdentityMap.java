/*     */ package serp.util;
/*     */ 
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
/*     */ public class IdentityMap
/*     */   extends HashMap
/*     */ {
/*     */   public IdentityMap() {}
/*     */   
/*     */   public IdentityMap(int initialCapacity)
/*     */   {
/*  30 */     super(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdentityMap(int initialCapacity, float loadFactor)
/*     */   {
/*  39 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdentityMap(Map map)
/*     */   {
/*  49 */     putAll(map);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  55 */     return new IdentityMap(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/*  61 */     return super.containsKey(createKey(key));
/*     */   }
/*     */   
/*     */ 
/*     */   public Set entrySet()
/*     */   {
/*  67 */     return new IdentityMap.EntrySet(this, super.entrySet());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/*  73 */     return super.get(createKey(key));
/*     */   }
/*     */   
/*     */ 
/*     */   public Set keySet()
/*     */   {
/*  79 */     return new IdentityMap.KeySet(this, super.keySet());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/*  85 */     return super.put(createKey(key), value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void putAll(Map map)
/*     */   {
/*  92 */     for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
/*     */     {
/*  94 */       Map.Entry entry = (Map.Entry)itr.next();
/*  95 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 102 */     return super.remove(createKey(key));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Object createKey(Object key)
/*     */   {
/* 111 */     if (key == null) {
/* 112 */       return key;
/*     */     }
/* 114 */     return new IdentityMap.IdentityKey(key);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/IdentityMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */