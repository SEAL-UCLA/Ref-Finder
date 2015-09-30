/*    */ package serp.util;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ public class WeakKeyMap
/*    */   extends RefKeyMap
/*    */ {
/*    */   public WeakKeyMap() {}
/*    */   
/*    */   public WeakKeyMap(Map map)
/*    */   {
/* 54 */     super(map);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected RefKeyMap.RefMapKey createRefMapKey(Object key, ReferenceQueue queue, boolean identity)
/*    */   {
/* 61 */     if (queue == null) {
/* 62 */       return new WeakKeyMap.WeakMapKey(key, identity);
/*    */     }
/* 64 */     return new WeakKeyMap.WeakMapKey(key, queue, identity);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/WeakKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */