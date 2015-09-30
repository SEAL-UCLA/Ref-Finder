/*    */ package serp.util;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class IdentityPool
/*    */   extends AbstractPool
/*    */ {
/* 18 */   private Set _free = new MapSet(new IdentityMap());
/* 19 */   private Map _taken = new WeakKeyMap(new IdentityMap());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IdentityPool() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IdentityPool(int min, int max, int wait, int autoReturn)
/*    */   {
/* 36 */     super(min, max, wait, autoReturn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IdentityPool(Collection c)
/*    */   {
/* 45 */     super(c);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Set freeSet()
/*    */   {
/* 51 */     return this._free;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Map takenMap()
/*    */   {
/* 57 */     return this._taken;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/IdentityPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */