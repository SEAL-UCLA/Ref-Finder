/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ abstract class RefValueCollection
/*     */   implements RefCollection
/*     */ {
/*  33 */   private Collection _coll = null;
/*  34 */   private ReferenceQueue _queue = new ReferenceQueue();
/*  35 */   private boolean _identity = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RefValueCollection()
/*     */   {
/*  43 */     this(new LinkedList());
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
/*     */   public RefValueCollection(Collection coll)
/*     */   {
/*  59 */     if (((coll instanceof MapSet)) && (((MapSet)coll).isIdentity()))
/*     */     {
/*  61 */       this._identity = true;
/*  62 */       this._coll = new HashSet();
/*     */     }
/*     */     else
/*     */     {
/*  66 */       this._coll = coll;
/*  67 */       this._coll.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeHard(Object obj)
/*     */   {
/*  74 */     removeExpired();
/*     */     
/*     */ 
/*  77 */     if ((this._coll instanceof List))
/*     */     {
/*     */ 
/*  80 */       for (ListIterator li = ((List)this._coll).listIterator(); li.hasNext();)
/*     */       {
/*  82 */         Object value = li.next();
/*  83 */         if (equal(obj, value))
/*     */         {
/*  85 */           li.set(obj);
/*  86 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  90 */     else if (remove(obj))
/*     */     {
/*  92 */       this._coll.add(obj);
/*  93 */       return true;
/*     */     }
/*     */     
/*  96 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean makeReference(Object obj)
/*     */   {
/* 102 */     removeExpired();
/*     */     
/*     */ 
/* 105 */     if (obj == null) {
/* 106 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 110 */     if ((this._coll instanceof List))
/*     */     {
/* 112 */       for (ListIterator li = ((List)this._coll).listIterator(); li.hasNext();)
/*     */       {
/* 114 */         Object value = li.next();
/* 115 */         if (equal(obj, value))
/*     */         {
/* 117 */           li.set(createRefValue(obj, this._queue, this._identity));
/* 118 */           return true;
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     } else {
/* 124 */       for (Iterator itr = this._coll.iterator(); itr.hasNext();)
/*     */       {
/* 126 */         Object value = itr.next();
/* 127 */         if (equal(obj, value))
/*     */         {
/* 129 */           itr.remove();
/* 130 */           add(obj);
/* 131 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean equal(Object obj, Object value)
/*     */   {
/* 145 */     if ((value instanceof RefValue)) {
/* 146 */       value = ((RefValue)value).getValue();
/*     */     }
/*     */     
/* 149 */     return ((this._identity) || (obj == null)) && ((obj == value) || ((!this._identity) && (obj != null) && (obj.equals(value))));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean add(Object obj)
/*     */   {
/* 155 */     removeExpired();
/* 156 */     return addFilter(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean addAll(Collection objs)
/*     */   {
/* 162 */     removeExpired();
/*     */     
/* 164 */     boolean added = false;
/* 165 */     for (Iterator itr = objs.iterator(); itr.hasNext();)
/* 166 */       added = (added) || (addFilter(itr.next()));
/* 167 */     return added;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean addFilter(Object obj)
/*     */   {
/* 173 */     if (obj == null)
/* 174 */       return this._coll.add(null);
/* 175 */     return this._coll.add(createRefValue(obj, this._queue, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/* 181 */     this._coll.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contains(Object obj)
/*     */   {
/* 187 */     if (obj == null)
/* 188 */       return this._coll.contains(null);
/* 189 */     return this._coll.contains(createRefValue(obj, null, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsAll(Collection objs)
/*     */   {
/* 195 */     boolean contains = true;
/* 196 */     for (Iterator itr = objs.iterator(); (contains) && (itr.hasNext());)
/* 197 */       contains = contains(itr.next());
/* 198 */     return contains;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 204 */     return this._coll.equals(other);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 210 */     return this._coll.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object obj)
/*     */   {
/* 216 */     removeExpired();
/* 217 */     return removeFilter(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeAll(Collection objs)
/*     */   {
/* 223 */     removeExpired();
/*     */     
/* 225 */     boolean removed = false;
/* 226 */     for (Iterator itr = objs.iterator(); itr.hasNext();)
/* 227 */       removed = (removed) || (removeFilter(itr.next()));
/* 228 */     return removed;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean retainAll(Collection objs)
/*     */   {
/* 234 */     removeExpired();
/*     */     
/* 236 */     boolean removed = false;
/* 237 */     for (Iterator itr = iterator(); itr.hasNext();)
/*     */     {
/* 239 */       if (!objs.contains(itr.next()))
/*     */       {
/* 241 */         itr.remove();
/* 242 */         removed = true;
/*     */       }
/*     */     }
/*     */     
/* 246 */     return removed;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean removeFilter(Object obj)
/*     */   {
/* 252 */     if (obj == null)
/* 253 */       return this._coll.remove(null);
/* 254 */     return this._coll.remove(createRefValue(obj, null, this._identity));
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 260 */     return this._coll.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 267 */     ArrayList list = new ArrayList(size());
/* 268 */     for (Iterator itr = iterator(); itr.hasNext();) {
/* 269 */       list.add(itr.next());
/*     */     }
/* 271 */     return list.toArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object[] toArray(Object[] a)
/*     */   {
/* 278 */     ArrayList list = new ArrayList(size());
/* 279 */     for (Iterator itr = iterator(); itr.hasNext();) {
/* 280 */       list.add(itr.next());
/*     */     }
/* 282 */     return list.toArray(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 288 */     return new ValuesIterator(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract RefValue createRefValue(Object paramObject, ReferenceQueue paramReferenceQueue, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeExpired()
/*     */   {
/*     */     Object value;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 313 */     while ((value = this._queue.poll()) != null)
/*     */     {
/*     */       Object value;
/*     */       try {
/* 317 */         this._queue.remove(1L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */       
/*     */ 
/* 322 */       this._coll.remove(value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract interface RefValue
/*     */   {
/*     */     public abstract Object getValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class ValuesIterator
/*     */     extends LookaheadIterator
/*     */   {
/*     */     private ValuesIterator() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Iterator newIterator()
/*     */     {
/* 349 */       return RefValueCollection.this._coll.iterator();
/*     */     }
/*     */     
/*     */ 
/*     */     protected void processValue(LookaheadIterator.ItrValue value)
/*     */     {
/* 355 */       if ((value.value instanceof RefValueCollection.RefValue))
/*     */       {
/* 357 */         RefValueCollection.RefValue ref = (RefValueCollection.RefValue)value.value;
/* 358 */         if (ref.getValue() == null) {
/* 359 */           value.valid = false;
/*     */         } else {
/* 361 */           value.value = ref.getValue();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/RefValueCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */