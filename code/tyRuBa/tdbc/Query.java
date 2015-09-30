/*    */ package tyRuBa.tdbc;
/*    */ 
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Query
/*    */ {
/*    */   private QueryEngine queryEngine;
/*    */   
/*    */   Query(QueryEngine queryEngine)
/*    */   {
/* 19 */     this.queryEngine = queryEngine;
/*    */   }
/*    */   
/*    */   public ResultSet executeQuery(String queryString) throws TyrubaException {
/* 23 */     return new ResultSet(this.queryEngine, queryString);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/Query.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */