/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Validator
/*    */   implements Serializable
/*    */ {
/*  8 */   private boolean isOutdated = true;
/*  9 */   private boolean isValid = true;
/*    */   
/* 11 */   private long handle = -1L;
/*    */   
/*    */ 
/*    */   public long handle()
/*    */   {
/* 16 */     return this.handle;
/*    */   }
/*    */   
/*    */   public void setHandle(long handle) {
/* 20 */     this.handle = handle;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isValid()
/*    */   {
/* 28 */     return this.isValid;
/*    */   }
/*    */   
/*    */   public void invalidate() {
/* 32 */     this.isValid = false;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return 
/*    */     
/* 38 */       "Validator(" + this.handle + "," + (this.isOutdated ? "OUTDATED" : "UPTODATE") + "," + (this.isValid ? "VALID" : "INALIDATED") + ")";
/*    */   }
/*    */   
/*    */   public boolean isOutdated() {
/* 42 */     return this.isOutdated;
/*    */   }
/*    */   
/*    */   public void setOutdated(boolean flag) {
/* 46 */     this.isOutdated = flag;
/*    */   }
/*    */   
/* 49 */   private boolean hasAssociatedFacts = false;
/*    */   
/*    */   public boolean hasAssociatedFacts() {
/* 52 */     return this.hasAssociatedFacts;
/*    */   }
/*    */   
/*    */   public void setHasAssociatedFacts(boolean flag) {
/* 56 */     this.hasAssociatedFacts = flag;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/Validator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */