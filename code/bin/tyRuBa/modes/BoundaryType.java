/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ public abstract class BoundaryType
/*    */   extends Type
/*    */ {
/*    */   abstract boolean isStrict();
/*    */   
/*    */   public boolean isSuperTypeOf(Type other)
/*    */   {
/* 13 */     return other.isSubTypeOf(this, new HashMap());
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/BoundaryType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */