/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ public class HandleTable
/*    */   implements Serializable
/*    */ {
/*    */   private static final int DEFAULTSIZE = 32;
/*    */   private static final double GROWTHFACTOR = 1.33D;
/*    */   private int freeHead;
/*    */   private int[] free;
/*    */   private int[] update;
/*    */   private Object[] references;
/*    */   
/*    */   public HandleTable()
/*    */   {
/* 43 */     this.freeHead = 1;
/*    */     
/* 45 */     this.free = new int[32];
/* 46 */     this.update = new int[32];
/* 47 */     this.references = new Object[32];
/*    */     
/* 49 */     for (int i = 1; i < 32; i++)
/* 50 */       this.free[i] = (i + 1);
/*    */   }
/*    */   
/*    */   public long add(Object reference) {
/* 54 */     if (this.freeHead != this.references.length) {
/* 55 */       long indirection = (this.update[this.freeHead] << 32) + this.freeHead;
/*    */       
/* 57 */       this.references[this.freeHead] = reference;
/*    */       
/* 59 */       this.freeHead = this.free[this.freeHead];
/*    */       
/* 61 */       return indirection;
/*    */     }
/* 63 */     int[] tmpFree = new int[(int)(this.free.length * 1.33D)];
/* 64 */     int[] tmpUpdate = new int[(int)(this.update.length * 1.33D)];
/* 65 */     Object[] tmpReferences = new Object[(int)(this.references.length * 1.33D)];
/*    */     
/* 67 */     System.arraycopy(this.free, 0, tmpFree, 0, this.free.length);
/* 68 */     System.arraycopy(this.update, 0, tmpUpdate, 0, this.update.length);
/* 69 */     System.arraycopy(this.references, 0, tmpReferences, 0, this.references.length);
/*    */     
/* 71 */     for (int i = this.free.length; i < tmpFree.length; i++) {
/* 72 */       tmpFree[i] = (i + 1);
/*    */     }
/* 74 */     this.free = tmpFree;
/* 75 */     this.update = tmpUpdate;
/* 76 */     this.references = tmpReferences;
/*    */     
/* 78 */     return add(reference);
/*    */   }
/*    */   
/*    */   public Object get(long handle)
/*    */   {
/* 83 */     int index = (int)(handle & 0xFFFFFFFFFFFFFFFF);
/* 84 */     if (this.update[index] == handle >> 32) {
/* 85 */       return this.references[index];
/*    */     }
/* 87 */     return null;
/*    */   }
/*    */   
/*    */   public void remove(long handle) {
/* 91 */     int index = (int)(handle & 0xFFFFFFFFFFFFFFFF);
/* 92 */     if (this.update[index] == handle >> 32) {
/* 93 */       this.free[index] = this.freeHead;
/* 94 */       this.update[index] += 1;
/* 95 */       this.references[index] = null;
/* 96 */       this.freeHead = index;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/HandleTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */