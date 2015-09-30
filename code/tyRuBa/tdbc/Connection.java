/*    */ package tyRuBa.tdbc;
/*    */ 
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
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
/*    */ public class Connection
/*    */ {
/*    */   private QueryEngine queryEngine;
/*    */   
/*    */   public Connection(QueryEngine queryEngine)
/*    */   {
/* 22 */     this.queryEngine = queryEngine;
/*    */   }
/*    */   
/*    */   public Query createQuery() {
/* 26 */     return new Query(this.queryEngine);
/*    */   }
/*    */   
/*    */   public Insert createInsert() {
/* 30 */     return new Insert(this.queryEngine);
/*    */   }
/*    */   
/*    */   public PreparedQuery prepareQuery(String qry) throws TyrubaException {
/*    */     try {
/* 35 */       return this.queryEngine.prepareForRunning(qry);
/*    */     } catch (ParseException e) {
/* 37 */       throw new TyrubaException(e);
/*    */     } catch (TypeModeError e) {
/* 39 */       throw new TyrubaException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public PreparedInsert prepareInsert(String fact) throws ParseException, TypeModeError {
/* 44 */     return this.queryEngine.prepareForInsertion(fact);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */