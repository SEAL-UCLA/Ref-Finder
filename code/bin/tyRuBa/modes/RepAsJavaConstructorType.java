/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import tyRuBa.engine.FunctorIdentifier;
/*    */ import tyRuBa.engine.RBCompoundTerm;
/*    */ import tyRuBa.engine.RBJavaObjectCompoundTerm;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RepAsJavaConstructorType
/*    */   extends ConstructorType
/*    */   implements Serializable
/*    */ {
/*    */   FunctorIdentifier functorId;
/*    */   CompositeType result;
/*    */   Type repAsType;
/*    */   
/*    */   public RepAsJavaConstructorType(FunctorIdentifier functorId, Type repAs, CompositeType result)
/*    */   {
/* 26 */     this.functorId = functorId;
/* 27 */     this.result = result;
/* 28 */     this.repAsType = repAs;
/*    */   }
/*    */   
/*    */   public FunctorIdentifier getFunctorId() {
/* 32 */     return this.functorId;
/*    */   }
/*    */   
/*    */   public TypeConstructor getTypeConst() {
/* 36 */     return this.result.getTypeConstructor();
/*    */   }
/*    */   
/*    */   public int getArity() {
/* 40 */     return 1;
/*    */   }
/*    */   
/*    */   public RBTerm apply(RBTerm term) {
/* 44 */     if ((term instanceof RBJavaObjectCompoundTerm)) {
/* 45 */       return RBCompoundTerm.makeRepAsJava(this, ((RBJavaObjectCompoundTerm)term).getObject());
/*    */     }
/*    */     
/* 48 */     return RBCompoundTerm.make(this, term);
/*    */   }
/*    */   
/*    */   public RBTerm apply(ArrayList terms)
/*    */   {
/* 53 */     throw new Error("RepAsJava Constructors can only be applied a single term");
/*    */   }
/*    */   
/*    */   public Type apply(Type argType) throws TypeModeError {
/* 57 */     Map renamings = new HashMap();
/* 58 */     Type iargs = this.repAsType.clone(renamings);
/* 59 */     CompositeType iresult = (CompositeType)this.result.clone(renamings);
/* 60 */     argType.checkEqualTypes(iargs);
/* 61 */     return iresult.getTypeConstructor().apply(iresult.getArgs(), true);
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 65 */     if (other.getClass() != getClass()) {
/* 66 */       return false;
/*    */     }
/* 68 */     RepAsJavaConstructorType ctOther = (RepAsJavaConstructorType)other;
/* 69 */     return (this.repAsType.equals(ctOther.repAsType)) && (this.functorId.equals(ctOther.functorId)) && (this.result.equals(ctOther.result));
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 74 */     return this.repAsType.hashCode() + 13 * this.functorId.hashCode() + 31 * this.result.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/RepAsJavaConstructorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */