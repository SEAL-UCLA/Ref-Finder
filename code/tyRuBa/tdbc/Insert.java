/*    */ package tyRuBa.tdbc;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ import tyRuBa.parser.TyRuBaParser;
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
/*    */ public class Insert
/*    */ {
/*    */   private QueryEngine queryEngine;
/*    */   
/*    */   Insert(QueryEngine queryEngine)
/*    */   {
/* 27 */     this.queryEngine = queryEngine;
/*    */   }
/*    */   
/*    */   public void executeInsert(String insertString) throws TyrubaException {
/* 31 */     TyRuBaParser parser = new TyRuBaParser(new ByteArrayInputStream(insertString.getBytes()), System.err);
/*    */     try {
/* 33 */       parser.Rule(this.queryEngine);
/*    */     } catch (ParseException e) {
/* 35 */       throw new TyrubaException(e);
/*    */     } catch (TypeModeError e) {
/* 37 */       throw new TyrubaException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/Insert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */