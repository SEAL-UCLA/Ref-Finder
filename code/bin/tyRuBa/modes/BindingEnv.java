/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BindingEnv
/*    */   extends Hashtable
/*    */   implements Cloneable
/*    */ {
/*    */   public BindingMode getBindingMode(RBVariable var)
/*    */   {
/* 18 */     return (BindingMode)get(var);
/*    */   }
/*    */   
/*    */   public BindingMode putBindingMode(RBVariable var, BindingMode bindingmode) {
/* 22 */     return (BindingMode)put(var, bindingmode);
/*    */   }
/*    */   
/*    */   public Object clone() {
/* 26 */     BindingEnv cl = (BindingEnv)super.clone();
/* 27 */     return cl;
/*    */   }
/*    */   
/*    */ 
/*    */   public BindingEnv intersection(BindingEnv other)
/*    */   {
/* 33 */     BindingEnv result = (BindingEnv)clone();
/* 34 */     for (Iterator iter = result.keySet().iterator(); iter.hasNext();) {
/* 35 */       RBVariable var = (RBVariable)iter.next();
/* 36 */       if (!other.isBound(var)) {
/* 37 */         iter.remove();
/*    */       }
/*    */     }
/* 40 */     return result;
/*    */   }
/*    */   
/*    */   public boolean isBound(RBVariable var) {
/* 44 */     BindingMode result = getBindingMode(var);
/* 45 */     return result != null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/BindingEnv.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */