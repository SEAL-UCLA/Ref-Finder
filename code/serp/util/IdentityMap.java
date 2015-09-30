/*     */ package serp.util;
/*     */ 
/*     */ import java.util.AbstractSet;
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
/*  67 */     return new EntrySet(super.entrySet());
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
/*  79 */     return new KeySet(super.keySet());
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
/* 114 */     return new IdentityKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class IdentityKey
/*     */   {
/* 123 */     private Object _key = null;
/*     */     
/*     */ 
/*     */     public IdentityKey(Object key)
/*     */     {
/* 128 */       this._key = key;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 134 */       return this._key;
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 140 */       return System.identityHashCode(this._key);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 146 */       if (this == other)
/* 147 */         return true;
/* 148 */       if (!(other instanceof IdentityKey)) {
/* 149 */         return false;
/*     */       }
/* 151 */       return ((IdentityKey)other).getKey() == this._key;
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
/* 162 */     Map.Entry _entry = null;
/*     */     
/*     */ 
/*     */     public MapEntry(Map.Entry entry)
/*     */     {
/* 167 */       this._entry = entry;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getKey()
/*     */     {
/* 173 */       IdentityMap.IdentityKey key = (IdentityMap.IdentityKey)this._entry.getKey();
/* 174 */       if (key == null) {
/* 175 */         return null;
/*     */       }
/* 177 */       return key.getKey();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/* 183 */       return this._entry.getValue();
/*     */     }
/*     */     
/*     */ 
/*     */     public Object setValue(Object value)
/*     */     {
/* 189 */       return this._entry.setValue(value);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object other)
/*     */     {
/* 195 */       if (other == this)
/* 196 */         return true;
/* 197 */       if (!(other instanceof Map.Entry)) {
/* 198 */         return false;
/*     */       }
/* 200 */       Object key = getKey();
/* 201 */       Object key2 = ((Map.Entry)other).getKey();
/* 202 */       if (((key == null) && (key2 != null)) || (
/* 203 */         (key != null) && (!key.equals(key2)))) {
/* 204 */         return false;
/*     */       }
/* 206 */       Object val = getValue();
/* 207 */       Object val2 = ((Map.Entry)other).getValue();
/*     */       
/* 209 */       return ((val == null) && (val2 == null)) || ((val != null) && (val2.equals(val2)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class EntrySet
/*     */     extends AbstractSet
/*     */   {
/* 220 */     private Set _entrySet = null;
/*     */     
/*     */ 
/*     */     public EntrySet(Set entrySet)
/*     */     {
/* 225 */       this._entrySet = entrySet;
/*     */     }
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 231 */       return this._entrySet.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean add(Object o)
/*     */     {
/* 237 */       Map.Entry entry = (Map.Entry)o;
/* 238 */       return this._entrySet.add(new IdentityMap.MapEntry(entry));
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 244 */       new Iterator()
/*     */       {
/* 246 */         Iterator _itr = IdentityMap.EntrySet.this._entrySet.iterator();
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 251 */           return this._itr.hasNext();
/*     */         }
/*     */         
/*     */ 
/*     */         public Object next()
/*     */         {
/* 257 */           return new IdentityMap.MapEntry((Map.Entry)this._itr.next());
/*     */         }
/*     */         
/*     */ 
/*     */         public void remove()
/*     */         {
/* 263 */           this._itr.remove();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class KeySet
/*     */     extends AbstractSet
/*     */   {
/* 276 */     private Set _keySet = null;
/*     */     
/*     */ 
/*     */     public KeySet(Set keySet)
/*     */     {
/* 281 */       this._keySet = keySet;
/*     */     }
/*     */     
/*     */ 
/*     */     public int size()
/*     */     {
/* 287 */       return this._keySet.size();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean remove(Object rem)
/*     */     {
/* 293 */       for (Iterator itr = this._keySet.iterator(); itr.hasNext();)
/*     */       {
/* 295 */         if (((IdentityMap.IdentityKey)itr.next()).getKey() == rem)
/*     */         {
/* 297 */           itr.remove();
/* 298 */           return true;
/*     */         }
/*     */       }
/* 301 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public Iterator iterator()
/*     */     {
/* 307 */       new Iterator()
/*     */       {
/* 309 */         private Iterator _itr = IdentityMap.KeySet.this._keySet.iterator();
/*     */         
/*     */ 
/*     */         public boolean hasNext()
/*     */         {
/* 314 */           return this._itr.hasNext();
/*     */         }
/*     */         
/*     */ 
/*     */         public Object next()
/*     */         {
/* 320 */           IdentityMap.IdentityKey key = (IdentityMap.IdentityKey)this._itr.next();
/* 321 */           if (key == null) {
/* 322 */             return null;
/*     */           }
/* 324 */           return key.getKey();
/*     */         }
/*     */         
/*     */ 
/*     */         public void remove()
/*     */         {
/* 330 */           this._itr.remove();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/IdentityMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */