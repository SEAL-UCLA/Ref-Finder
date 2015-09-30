/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TVarFactory
/*    */ {
/* 14 */   private HashMap hm = new HashMap();
/*    */   
/*    */   public TVar makeTVar(String name) {
/* 17 */     TVar result = (TVar)this.hm.get(name);
/* 18 */     if (result == null) {
/* 19 */       result = Factory.makeTVar(name);
/* 20 */       this.hm.put(name, result);
/*    */     }
/* 22 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/TVarFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */