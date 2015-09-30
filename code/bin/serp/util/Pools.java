/*    */ package serp.util;
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
/*    */ public class Pools
/*    */ {
/*    */   public static Pool synchronizedPool(Pool pool)
/*    */   {
/* 20 */     if (pool == null)
/* 21 */       throw new NullPointerException();
/* 22 */     return new Pools.SynchronizedPool(pool);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/Pools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */