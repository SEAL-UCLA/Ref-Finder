/*    */ package tyRuBa.util;
/*    */ 
/*    */ import junit.framework.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SynchPolicy
/*    */ {
/*    */   SynchResource resource;
/*    */   
/*    */   public SynchPolicy(SynchResource res)
/*    */   {
/* 14 */     this.resource = res;
/*    */   }
/*    */   
/*    */ 
/* 18 */   int stopSources = 0;
/* 19 */   int busySources = 0;
/*    */   
/*    */   public void sourceDone() {
/* 22 */     synchronized (this.resource) {
/* 23 */       this.busySources -= 1;
/* 24 */       debug_message("--");
/* 25 */       Assert.assertTrue(this.busySources >= 0);
/*    */       
/*    */ 
/* 28 */       if (this.busySources == 0)
/* 29 */         this.resource.notifyAll();
/*    */     }
/*    */   }
/*    */   
/*    */   public void newSource() {
/* 34 */     while (this.stopSources > 0)
/*    */       try {
/* 36 */         this.resource.wait();
/*    */       } catch (InterruptedException e) {
/* 38 */         e.printStackTrace();
/*    */       }
/* 40 */     this.busySources += 1;
/* 41 */     debug_message("++");
/*    */   }
/*    */   
/*    */   public void stopSources()
/*    */   {
/* 46 */     long waitTime = 100L;
/* 47 */     if (Aurelizer.debug_sounds != null)
/* 48 */       Aurelizer.debug_sounds.enter_loop("temporizing");
/*    */     try {
/* 50 */       synchronized (this.resource) {
/* 51 */         this.stopSources += 1;
/* 52 */         debug_message("stop");
/* 53 */         while (this.busySources > 0) {
/*    */           try {
/* 55 */             this.resource.wait(waitTime);
/* 56 */             if (waitTime > 100L) {
/* 57 */               System.gc();
/*    */             }
/* 59 */             waitTime *= 2L;
/*    */           } catch (InterruptedException e) {
/* 61 */             e.printStackTrace();
/*    */           }
/* 63 */           if ((this.busySources > 0) && (waitTime > 33000L)) {
/* 64 */             this.stopSources -= 1;
/* 65 */             if (Aurelizer.debug_sounds != null)
/* 66 */               Aurelizer.debug_sounds.enter("error");
/* 67 */             throw new Error("I've lost my patience waiting for all queries to be released");
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */     finally {
/* 73 */       if (Aurelizer.debug_sounds != null) {
/* 74 */         Aurelizer.debug_sounds.exit("temporizing");
/*    */       }
/*    */     }
/* 73 */     if (Aurelizer.debug_sounds != null) {
/* 74 */       Aurelizer.debug_sounds.exit("temporizing");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private void debug_message(String msg) {}
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     return "SynchPolicy(busy=" + this.busySources + ",stop=" + this.stopSources + ")";
/*    */   }
/*    */   
/*    */   public void allowSources() {
/* 88 */     synchronized (this.resource) {
/* 89 */       Assert.assertTrue(this.stopSources > 0);
/* 90 */       this.stopSources -= 1;
/* 91 */       debug_message("allow");
/* 92 */       this.resource.notifyAll();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/SynchPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */