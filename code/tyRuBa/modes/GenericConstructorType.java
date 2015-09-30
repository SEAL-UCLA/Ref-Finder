/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import tyRuBa.engine.FunctorIdentifier;
/*    */ import tyRuBa.engine.RBCompoundTerm;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericConstructorType
/*    */   extends ConstructorType
/*    */   implements Serializable
/*    */ {
/*    */   FunctorIdentifier identifier;
/*    */   Type args;
/*    */   CompositeType result;
/*    */   
/*    */   public GenericConstructorType(FunctorIdentifier identifier, Type args, CompositeType result)
/*    */   {
/* 29 */     this.identifier = identifier;
/* 30 */     this.args = args;
/* 31 */     this.result = result;
/*    */   }
/*    */   
/*    */   public FunctorIdentifier getFunctorId() {
/* 35 */     return this.identifier;
/*    */   }
/*    */   
/*    */   public TypeConstructor getTypeConst() {
/* 39 */     return this.result.getTypeConstructor();
/*    */   }
/*    */   
/*    */   public int getArity() {
/* 43 */     if ((this.args instanceof TupleType)) {
/* 44 */       return ((TupleType)this.args).size();
/*    */     }
/* 46 */     return 1;
/*    */   }
/*    */   
/*    */   public RBTerm apply(RBTerm tuple) {
/* 50 */     return RBCompoundTerm.make(this, tuple);
/*    */   }
/*    */   
/*    */   public RBTerm apply(ArrayList terms) {
/* 54 */     return apply(RBTuple.make(terms));
/*    */   }
/*    */   
/*    */   public Type apply(Type argType) throws TypeModeError {
/* 58 */     Map renamings = new HashMap();
/* 59 */     Type iargs = this.args.clone(renamings);
/* 60 */     CompositeType iresult = (CompositeType)this.result.clone(renamings);
/*    */     
/* 62 */     argType.checkEqualTypes(iargs);
/* 63 */     return iresult.getTypeConstructor().apply(iresult.getArgs(), true);
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 67 */     if (other.getClass() != getClass()) {
/* 68 */       return false;
/*    */     }
/* 70 */     GenericConstructorType ctOther = (GenericConstructorType)other;
/* 71 */     return (this.args.equals(ctOther.args)) && (this.identifier.equals(ctOther.identifier)) && (this.result.equals(ctOther.result));
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 76 */     return this.args.hashCode() + this.identifier.hashCode() * 13 + this.result.hashCode() * 31;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/GenericConstructorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */