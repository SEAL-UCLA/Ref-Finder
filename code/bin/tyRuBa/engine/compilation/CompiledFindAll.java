/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompiledFindAll
/*    */   extends SemiDetCompiled
/*    */ {
/*    */   private final Compiled query;
/*    */   private final RBTerm extract;
/*    */   private final RBTerm result;
/*    */   
/*    */   public CompiledFindAll(Compiled query, RBTerm extract, RBTerm result)
/*    */   {
/* 21 */     super(Mode.makeDet());
/* 22 */     this.query = query;
/* 23 */     this.extract = extract;
/* 24 */     this.result = result;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 28 */     ElementSource res = this.query.runNonDet(((Frame)input).clone(), context);
/* 29 */     res = res.map(new CompiledFindAll.1(this));
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 34 */     RBTerm resultList = FrontEnd.makeList(res);
/* 35 */     return this.result.unify(resultList, (Frame)input);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 39 */     return "COMPILED FINDALL(" + this.query + "," + this.extract + "," + this.result + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledFindAll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */