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
/*     */ public class ThreadLock
/*     */   implements Serializable
/*     */ {
/*  25 */   private transient int _count = 0;
/*  26 */   private transient Thread _owner = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void lock()
/*     */   {
/*  34 */     Thread thread = Thread.currentThread();
/*  35 */     if (thread != this._owner)
/*  36 */       while (this._count > 0)
/*  37 */         try { wait();
/*     */         } catch (InterruptedException localInterruptedException) {}
/*  39 */     this._count += 1;
/*  40 */     this._owner = thread;
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
/*     */   public synchronized boolean lock(long timeout)
/*     */   {
/*  54 */     if (timeout == 0L)
/*     */     {
/*  56 */       lock();
/*  57 */       return true;
/*     */     }
/*     */     
/*  60 */     Thread thread = Thread.currentThread();
/*  61 */     if ((thread != this._owner) && (this._count > 0))
/*     */     {
/*  63 */       long time = System.currentTimeMillis();
/*  64 */       long end = time + timeout;
/*  65 */       while ((this._count > 0) && (time < end))
/*     */       {
/*     */         try
/*     */         {
/*  69 */           wait(end - time);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/*     */ 
/*  74 */         time = System.currentTimeMillis();
/*     */       }
/*     */     }
/*     */     
/*  78 */     if ((thread != this._owner) && (this._count > 0)) {
/*  79 */       return false;
/*     */     }
/*  81 */     this._count += 1;
/*  82 */     this._owner = thread;
/*  83 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void unlock()
/*     */   {
/*  95 */     Thread thread = Thread.currentThread();
/*  96 */     if (thread != this._owner) {
/*  97 */       throw new IllegalStateException();
/*     */     }
/*  99 */     this._count -= 1;
/* 100 */     if (this._count == 0) {
/* 101 */       notify();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLocked()
/*     */   {
/* 110 */     return this._count > 0;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/ThreadLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */