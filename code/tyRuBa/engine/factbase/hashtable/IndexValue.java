/*    */ package tyRuBa.engine.factbase.hashtable;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import tyRuBa.engine.RBTuple;
/*    */ import tyRuBa.engine.Validator;
/*    */ import tyRuBa.engine.factbase.ValidatorManager;
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
/*    */ public class IndexValue
/*    */   implements Serializable
/*    */ {
/*    */   private long validatorHandle;
/*    */   private RBTuple parts;
/*    */   
/*    */   public static IndexValue make(Validator v, RBTuple parts)
/*    */   {
/* 28 */     if (v == null) {
/* 29 */       return new IndexValue(0L, parts);
/*    */     }
/* 31 */     return new IndexValue(v.handle(), parts);
/*    */   }
/*    */   
/*    */ 
/*    */   public static IndexValue make(long validatorHandle, RBTuple parts)
/*    */   {
/* 37 */     return new IndexValue(validatorHandle, parts);
/*    */   }
/*    */   
/*    */   private IndexValue(long validatorHandle, RBTuple parts)
/*    */   {
/* 42 */     this.validatorHandle = validatorHandle;
/* 43 */     this.parts = parts;
/*    */   }
/*    */   
/*    */   public long getValidatorHandle()
/*    */   {
/* 48 */     return this.validatorHandle;
/*    */   }
/*    */   
/*    */   public RBTuple getParts()
/*    */   {
/* 53 */     return this.parts;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isValid(ValidatorManager vm)
/*    */   {
/* 61 */     if (this.validatorHandle == 0L) {
/* 62 */       return true;
/*    */     }
/* 64 */     Validator v = vm.get(this.validatorHandle);
/* 65 */     if ((v == null) || (!v.isValid())) {
/* 66 */       return false;
/*    */     }
/* 68 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IndexValue prepend(RBTuple tuple)
/*    */   {
/* 78 */     return new IndexValue(this.validatorHandle, tuple.append(this.parts));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 82 */     return this.parts.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/hashtable/IndexValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */