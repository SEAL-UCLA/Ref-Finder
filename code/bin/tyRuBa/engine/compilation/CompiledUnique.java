/*    */ package tyRuBa.engine.compilation;
/*    */ 
/*    */ import tyRuBa.engine.Frame;
/*    */ import tyRuBa.engine.RBContext;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBVariable;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public class CompiledUnique extends SemiDetCompiled
/*    */ {
/*    */   RBVariable[] vars;
/*    */   Compiled exp;
/*    */   
/*    */   public CompiledUnique(RBVariable[] vars, Compiled exp)
/*    */   {
/* 16 */     this.vars = vars;
/* 17 */     this.exp = exp;
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context) {
/* 21 */     Frame f = (Frame)input;
/* 22 */     Frame newf = (Frame)f.clone();
/* 23 */     RBTerm[] vals = new RBTerm[this.vars.length];
/* 24 */     newf.removeVars(this.vars);
/* 25 */     for (int i = 0; i < this.vars.length; i++) {
/* 26 */       vals[i] = this.vars[i].substitute(f);
/*    */     }
/* 28 */     ElementSource result = this.exp.runNonDet(newf, context);
/* 29 */     if (!result.hasMoreElements())
/* 30 */       return null;
/*    */     int i;
/* 32 */     for (; result.hasMoreElements(); 
/*    */         
/* 34 */         i < vals.length)
/*    */     {
/* 33 */       Frame currentFrame = (Frame)result.nextElement();
/* 34 */       i = 0; continue;
/* 35 */       newf = vals[i].unify(this.vars[i].substitute(currentFrame), newf);
/* 36 */       if (newf == null) {
/* 37 */         return null;
/*    */       }
/* 34 */       i++;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 41 */     return newf;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 46 */     StringBuffer result = new StringBuffer("UNIQUE(");
/* 47 */     for (int i = 0; i < this.vars.length; i++) {
/* 48 */       if (i > 0) {
/* 49 */         result.append(",");
/*    */       }
/* 51 */       result.append(this.vars[i]);
/*    */     }
/* 53 */     result.append(": " + this.exp + ")");
/* 54 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/compilation/CompiledUnique.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */