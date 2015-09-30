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
/*    */ public class SoftValueMap
/*    */   extends RefValueMap
/*    */ {
/*    */   public SoftValueMap() {}
/*    */   
/*    */   public SoftValueMap(Map map)
/*    */   {
/* 53 */     super(map);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected RefValueMap.RefMapValue createRefMapValue(Object key, Object value, ReferenceQueue queue)
/*    */   {
/* 60 */     return new SoftValueMap.SoftMapValue(key, value, queue);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/SoftValueMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */