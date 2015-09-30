/*    */ package serp.util;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapSet
/*    */   extends AbstractSet
/*    */ {
/* 21 */   Map _map = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MapSet()
/*    */   {
/* 31 */     this(new HashMap());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public MapSet(Map map)
/*    */   {
/* 45 */     this._map = map;
/* 46 */     this._map.clear();
/*    */   }
/*    */   
/*    */ 
/*    */   public int size()
/*    */   {
/* 52 */     return this._map.size();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean add(Object obj)
/*    */   {
/* 58 */     if (this._map.containsKey(obj)) {
/* 59 */       return false;
/*    */     }
/* 61 */     this._map.put(obj, null);
/* 62 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean remove(Object obj)
/*    */   {
/* 68 */     boolean contained = this._map.containsKey(obj);
/* 69 */     this._map.remove(obj);
/* 70 */     return contained;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean contains(Object obj)
/*    */   {
/* 76 */     return this._map.containsKey(obj);
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator iterator()
/*    */   {
/* 82 */     return this._map.keySet().iterator();
/*    */   }
/*    */   
/*    */ 
/*    */   boolean isIdentity()
/*    */   {
/* 88 */     return this._map instanceof IdentityMap;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/MapSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */