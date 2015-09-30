/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.modes.ConstructorType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RBGenericCompoundTerm
/*    */   extends RBCompoundTerm
/*    */ {
/*    */   ConstructorType typeTag;
/*    */   RBTerm args;
/*    */   
/* 17 */   public RBTerm getArg() { return this.args; }
/*    */   
/*    */   public RBGenericCompoundTerm(ConstructorType constructorType, RBTerm args) {
/* 20 */     this.typeTag = constructorType;
/* 21 */     this.args = args;
/*    */   }
/*    */   
/*    */   public ConstructorType getConstructorType() {
/* 25 */     return this.typeTag;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBGenericCompoundTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */