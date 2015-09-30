/*    */ package tyRuBa.tdbc;
/*    */ 
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ import tyRuBa.util.ElementSource;
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
/*    */ public class PreparedQuery
/*    */   extends PreparedStatement
/*    */ {
/*    */   private RBExpression preparedExp;
/*    */   private Compiled compiled;
/*    */   
/*    */   public PreparedQuery(QueryEngine engine, RBExpression preparedExp, TypeEnv tEnv)
/*    */   {
/* 33 */     super(engine, tEnv);
/* 34 */     this.preparedExp = preparedExp;
/* 35 */     this.compiled = preparedExp.compile(new CompilationContext());
/*    */   }
/*    */   
/*    */   public static PreparedQuery prepare(QueryEngine queryEngine, String queryTemplate) throws ParseException, TypeModeError
/*    */   {
/* 40 */     return queryEngine.prepareForRunning(queryTemplate);
/*    */   }
/*    */   
/*    */   public ResultSet executeQuery() throws TyrubaException {
/* 44 */     checkReadyToRun();
/* 45 */     return new ResultSet(this.compiled.start(this.putMap));
/*    */   }
/*    */   
/*    */   public ElementSource start() {
/* 49 */     return this.compiled.start(this.putMap);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/PreparedQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */