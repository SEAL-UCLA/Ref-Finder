/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RBAvoidRecursion
/*    */   extends RBContext
/*    */ {
/*    */   protected RBContext guarded;
/*    */   protected RBRule rule;
/*    */   private int depth;
/* 16 */   private static int maxDepth = 0;
/*    */   
/* 18 */   public static int depthLimit = 250;
/*    */   
/*    */   public RBAvoidRecursion(RBContext aContext, RBRule r) {
/* 21 */     this.rule = r;
/* 22 */     this.guarded = aContext;
/* 23 */     this.depth = (aContext.depth() + 1);
/* 24 */     if (this.depth > maxDepth)
/*    */     {
/* 26 */       maxDepth = this.depth;
/* 27 */       if (this.depth == depthLimit) {
/* 28 */         System.err.print(this);
/* 29 */         throw new Error("To deep recursion in rule application");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   int depth() {
/* 35 */     return this.depth;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 39 */     StringBuffer result = new StringBuffer(this.rule + "\n");
/* 40 */     if ((this.guarded instanceof RBAvoidRecursion)) {
/* 41 */       result.append(this.guarded.toString());
/*    */     } else
/* 43 */       result.append("--------------------");
/* 44 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBAvoidRecursion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */