/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SynchronizedElementSource
/*    */   extends ElementSource
/*    */ {
/*    */   private SynchResource resource;
/*    */   
/*    */ 
/*    */ 
/*    */   private ElementSource src;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SynchronizedElementSource(SynchResource resource, ElementSource src)
/*    */   {
/* 20 */     synchronized (resource) {
/* 21 */       resource.getSynchPolicy().newSource();
/* 22 */       this.resource = resource;
/* 23 */       this.src = src;
/*    */     }
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 28 */     p.print("Synchronized(");
/* 29 */     this.src.print(p);
/* 30 */     p.print(")");
/*    */   }
/*    */   
/*    */   public int status() {
/* 34 */     if (this.resource == null) {
/* 35 */       return -1;
/*    */     }
/* 37 */     synchronized (this.resource) {
/* 38 */       int result = this.src.status();
/* 39 */       if (result == -1)
/*    */       {
/* 41 */         release();
/*    */       }
/* 43 */       return result;
/*    */     }
/*    */   }
/*    */   
/*    */   public void release() {
/* 48 */     if (this.resource != null)
/*    */     {
/* 50 */       if (this.src != null)
/*    */       {
/* 52 */         this.src.release();
/*    */       }
/* 54 */       this.src = null;
/* 55 */       this.resource.getSynchPolicy().sourceDone();
/* 56 */       this.resource = null;
/*    */     }
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public Object nextElement()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 26	tyRuBa/util/SynchronizedElementSource:resource	LtyRuBa/util/SynchResource;
/*    */     //   4: dup
/*    */     //   5: astore_1
/*    */     //   6: monitorenter
/*    */     //   7: aload_0
/*    */     //   8: getfield 28	tyRuBa/util/SynchronizedElementSource:src	LtyRuBa/util/ElementSource;
/*    */     //   11: invokevirtual 67	tyRuBa/util/ElementSource:nextElement	()Ljava/lang/Object;
/*    */     //   14: aload_1
/*    */     //   15: monitorexit
/*    */     //   16: areturn
/*    */     //   17: aload_1
/*    */     //   18: monitorexit
/*    */     //   19: athrow
/*    */     // Line number table:
/*    */     //   Java source line #68	-> byte code offset #0
/*    */     //   Java source line #69	-> byte code offset #7
/*    */     //   Java source line #68	-> byte code offset #17
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	20	0	this	SynchronizedElementSource
/*    */     //   5	13	1	Ljava/lang/Object;	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	16	17	finally
/*    */     //   17	19	17	finally
/*    */   }
/*    */   
/*    */   protected void finalize()
/*    */     throws Throwable
/*    */   {
/*    */     try
/*    */     {
/* 76 */       if (this.resource != null)
/*    */       {
/*    */ 
/* 79 */         if (Aurelizer.debug_sounds != null)
/* 80 */           Aurelizer.debug_sounds.enter("ok");
/*    */       }
/* 82 */       release();
/* 83 */       super.finalize();
/*    */     } catch (Throwable e) {
/* 85 */       e.printStackTrace();
/* 86 */       if (Aurelizer.debug_sounds != null) {
/* 87 */         Aurelizer.debug_sounds.enter("error");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/SynchronizedElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */