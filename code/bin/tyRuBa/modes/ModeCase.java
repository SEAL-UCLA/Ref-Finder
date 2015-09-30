/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModeCase
/*    */ {
/*    */   private Collection boundVars;
/*    */   private RBExpression exp;
/*    */   
/*    */   public ModeCase(Collection boundVars, RBExpression exp)
/*    */   {
/* 15 */     this.boundVars = boundVars;
/* 16 */     this.exp = exp;
/*    */   }
/*    */   
/*    */   public RBExpression getExp() {
/* 20 */     return this.exp;
/*    */   }
/*    */   
/*    */   public Collection getBoundVars() {
/* 24 */     return this.boundVars;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     String varString = getBoundVars().toString();
/* 29 */     return "BOUND " + varString.substring(1, varString.length() - 1) + 
/* 30 */       " : " + getExp();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/ModeCase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */