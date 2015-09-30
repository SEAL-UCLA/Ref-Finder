/*    */ package tyRuBa.tdbc;
/*    */ 
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ import tyRuBa.engine.RBPredicateExpression;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PreparedInsert
/*    */   extends PreparedStatement
/*    */ {
/*    */   RBPredicateExpression fact;
/*    */   
/*    */   public PreparedInsert(QueryEngine engine, RBPredicateExpression fact, TypeEnv tEnv)
/*    */   {
/* 22 */     super(engine, tEnv);
/* 23 */     this.fact = fact;
/*    */   }
/*    */   
/*    */   public void executeInsert() throws TyrubaException {
/* 27 */     checkReadyToRun();
/*    */     try {
/* 29 */       getEngine().insert((RBPredicateExpression)this.fact.substitute(this.putMap));
/*    */     } catch (TypeModeError e) {
/* 31 */       throw new TyrubaException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return "PrepIns(" + this.fact + ", " + this.putMap + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/PreparedInsert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */