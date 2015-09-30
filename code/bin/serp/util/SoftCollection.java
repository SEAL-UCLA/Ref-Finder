/*    */ package serp.util;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.util.Collection;
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
/*    */ public class SoftCollection
/*    */   extends RefValueCollection
/*    */ {
/*    */   public SoftCollection() {}
/*    */   
/*    */   public SoftCollection(Collection coll)
/*    */   {
/* 52 */     super(coll);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected RefValueCollection.RefValue createRefValue(Object value, ReferenceQueue queue, boolean identity)
/*    */   {
/* 59 */     if (queue == null) {
/* 60 */       return new SoftCollection.SoftValue(value, identity);
/*    */     }
/* 62 */     return new SoftCollection.SoftValue(value, queue, identity);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/SoftCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */