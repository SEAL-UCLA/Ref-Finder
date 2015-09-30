/*     */ package serp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Semaphore
/*     */   implements Serializable
/*     */ {
/*  15 */   private int _available = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Semaphore() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Semaphore(int available)
/*     */   {
/*  32 */     if (available < 0)
/*  33 */       throw new IllegalArgumentException("available = " + available);
/*  34 */     this._available = available;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getAvailable()
/*     */   {
/*  43 */     return this._available;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void down()
/*     */   {
/*  53 */     while (this._available == 0)
/*  54 */       try { wait(); } catch (InterruptedException localInterruptedException) {}
/*  55 */     this._available -= 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean down(long timeout)
/*     */   {
/*  70 */     if (timeout == 0L)
/*     */     {
/*  72 */       down();
/*  73 */       return true;
/*     */     }
/*     */     
/*  76 */     if (this._available == 0)
/*     */     {
/*  78 */       long time = System.currentTimeMillis();
/*  79 */       long end = time + timeout;
/*  80 */       while ((this._available == 0) && (time < end))
/*     */       {
/*     */         try
/*     */         {
/*  84 */           wait(end - time);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/*     */ 
/*  89 */         time = System.currentTimeMillis();
/*     */       }
/*     */     }
/*     */     
/*  93 */     if (this._available == 0) {
/*  94 */       return false;
/*     */     }
/*  96 */     this._available -= 1;
/*  97 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void up()
/*     */   {
/* 106 */     this._available += 1;
/* 107 */     if (this._available == 1) {
/* 108 */       notify();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/Semaphore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */