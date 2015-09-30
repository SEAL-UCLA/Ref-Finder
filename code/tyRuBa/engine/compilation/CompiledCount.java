/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompiledCount
/*    */   extends SemiDetCompiled
/*    */ {
/*    */   private final Compiled query;
/*    */   private final RBTerm extract;
/*    */   private final RBTerm result;
/*    */   
/*    */   public CompiledCount(Compiled query, RBTerm extract, RBTerm result)
/*    */   {
/* 23 */     super(Mode.makeDet());
/* 24 */     this.query = query;
/* 25 */     this.extract = extract;
/* 26 */     this.result = result;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 30 */     ElementSource res = this.query.runNonDet(((Frame)input).clone(), context);
/* 31 */     Set results = new HashSet();
/* 32 */     while (res.hasMoreElements()) {
/* 33 */       Frame frame = (Frame)res.nextElement();
/* 34 */       results.add(this.extract.substitute(frame));
/*    */     }
/* 36 */     RBTerm resultCount = FrontEnd.makeInteger(results.size());
/* 37 */     return this.result.unify(resultCount, (Frame)input);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 41 */     return "COMPILED FINDALL(" + this.query + "," + this.result + ")";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/compilation/CompiledCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */