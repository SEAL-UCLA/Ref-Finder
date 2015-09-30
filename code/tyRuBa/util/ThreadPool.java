/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.util.LinkedList;
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
/*     */ public class ThreadPool
/*     */ {
/*     */   private LinkedList jobs;
/*     */   private Object mutex;
/*     */   private int workers;
/*  22 */   private boolean halt = false;
/*  23 */   private boolean exit = false;
/*     */   
/*     */   private class Worker implements Runnable
/*     */   {
/*     */     private Worker() {}
/*     */     
/*     */     public void run() {
/*     */       for (;;) {
/*  31 */         synchronized (ThreadPool.this.mutex) {
/*  32 */           if (ThreadPool.this.halt) { cleanUp();return;
/*     */             
/*     */ 
/*  35 */             if (ThreadPool.this.exit) { cleanUp();return;
/*     */             }
/*     */             try {
/*  38 */               ThreadPool.this.mutex.wait();
/*     */             }
/*     */             catch (InterruptedException localInterruptedException) {}
/*  41 */             if (ThreadPool.this.halt) { cleanUp();return;
/*     */             }
/*     */           }
/*  34 */           if (ThreadPool.this.jobs.size() == 0) {
/*     */             continue;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */           Runnable job = (Runnable)ThreadPool.this.jobs.removeFirst();
/*     */           try
/*     */           {
/*  47 */             job.run();
/*     */           }
/*     */           catch (Throwable localThrowable) {}
/*     */         }
/*  51 */         Runnable job = null;
/*     */       }
/*     */     }
/*     */     
/*     */     private void cleanUp()
/*     */     {
/*  57 */       ThreadPool.this.workers -= 1;
/*  58 */       ThreadPool.this.mutex.notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadPool(int workers)
/*     */   {
/*  68 */     if (workers <= 0) {
/*  69 */       throw new IllegalArgumentException("There must be at least 1 worker thread");
/*     */     }
/*  71 */     this.workers = workers;
/*     */     
/*  73 */     this.jobs = new LinkedList();
/*     */     
/*  75 */     this.mutex = this.jobs;
/*     */     
/*  77 */     int i = workers;
/*  78 */     while (i-- != 0) {
/*  79 */       new Thread(new Worker(null)).start();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delegate(Runnable job)
/*     */   {
/*  88 */     synchronized (this.mutex) {
/*  89 */       if ((this.halt) || (this.exit)) { throw new IllegalStateException("ThreadPool is being shutdown");
/*     */       }
/*  91 */       this.jobs.addLast(job);
/*  92 */       this.mutex.notify();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int waiting()
/*     */   {
/* 101 */     return this.jobs.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void halt()
/*     */   {
/* 108 */     synchronized (this.mutex) {
/* 109 */       this.halt = true;
/* 110 */       this.mutex.notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void exit()
/*     */   {
/* 118 */     synchronized (this.mutex) {
/* 119 */       this.exit = true;
/* 120 */       this.mutex.notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void waitExit()
/*     */   {
/* 128 */     synchronized (this.mutex) {
/* 129 */       this.exit = true;
/* 130 */       this.mutex.notifyAll();
/*     */       
/* 132 */       while (this.workers != 0) {
/*     */         try {
/* 134 */           this.mutex.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void waitHalt()
/*     */   {
/* 144 */     synchronized (this.mutex) {
/* 145 */       this.halt = true;
/* 146 */       this.mutex.notifyAll();
/*     */       
/* 148 */       while (this.workers != 0) {
/*     */         try {
/* 150 */           this.mutex.wait();
/*     */         } catch (InterruptedException localInterruptedException) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void finalize() {
/* 157 */     exit();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/ThreadPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */