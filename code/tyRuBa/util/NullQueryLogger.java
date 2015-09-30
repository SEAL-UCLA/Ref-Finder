/*    */ package tyRuBa.util;
/*    */ 
/*    */ import tyRuBa.engine.RBExpression;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NullQueryLogger
/*    */   extends QueryLogger
/*    */ {
/* 13 */   public static NullQueryLogger the = new NullQueryLogger();
/*    */   
/*    */   public final void close() {}
/*    */   
/*    */   public final void logQuery(RBExpression query) {}
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/NullQueryLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */