/*     */ package tyRuBa.util.pager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import serp.util.Semaphore;
/*     */ import tyRuBa.util.Aurelizer;
/*     */ import tyRuBa.util.DoubleLinkedList;
/*     */ import tyRuBa.util.DoubleLinkedList.Entry;
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
/*     */ public class DiskManager
/*     */   extends Thread
/*     */ {
/*     */   private int maxSize;
/*     */   Semaphore queueMutex;
/*     */   Semaphore queueAvailable;
/*     */   Semaphore queueSize;
/*     */   Semaphore resourceLocksMutex;
/*     */   boolean alive;
/*  56 */   DoubleLinkedList taskQueue = new DoubleLinkedList();
/*     */   
/*     */ 
/*  59 */   Map resourceLocks = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int couldHaveCanceledPageout;
/*     */   
/*     */ 
/*     */ 
/*  68 */   private int highWaterMark = 0;
/*     */   
/*     */ 
/*  71 */   private int pageOutRequests = 0;
/*     */   
/*     */ 
/*  74 */   private int pageInRequests = 0;
/*     */   
/*     */ 
/*     */   private static class Task
/*     */     extends DoubleLinkedList.Entry
/*     */   {
/*     */     Pager.Resource rsrc;
/*     */     
/*     */     Pager.ResourceId resourceID;
/*     */     
/*     */     Task(Pager.ResourceId resourceID, Pager.Resource rsrc)
/*     */     {
/*  86 */       this.resourceID = resourceID;
/*  87 */       this.rsrc = rsrc;
/*     */     }
/*     */     
/*     */     void doIt()
/*     */     {
/*     */       try {
/*  93 */         OutputStream os = this.resourceID.writeResource();
/*  94 */         if (os != null) {
/*  95 */           ObjectOutputStream oos = new ObjectOutputStream(os);
/*  96 */           oos.writeObject(this.rsrc);
/*  97 */           oos.close();
/*     */         }
/*     */       } catch (IOException e) {
/* 100 */         throw new Error("Could not page because of an IOException: " + e.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskManager(int maxQueueSize)
/*     */   {
/* 110 */     this.maxSize = maxQueueSize;
/* 111 */     this.resourceLocksMutex = new Semaphore();
/* 112 */     this.queueMutex = new Semaphore();
/* 113 */     this.queueAvailable = new Semaphore(this.maxSize);
/* 114 */     this.queueSize = new Semaphore(0);
/* 115 */     this.alive = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean isIdle()
/*     */   {
/* 127 */     return this.taskQueue.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 135 */     while (this.alive)
/*     */     {
/* 137 */       this.queueSize.down();
/* 138 */       if (!this.alive) {
/*     */         break;
/*     */       }
/* 141 */       this.queueMutex.down();
/* 142 */       Task nextTask = (Task)this.taskQueue.dequeue();
/* 143 */       this.queueMutex.up();
/*     */       
/*     */ 
/* 146 */       this.queueAvailable.up();
/*     */       
/* 148 */       nextTask.doIt();
/* 149 */       releaseResourceLock(nextTask.resourceID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void getResourceLock(Pager.ResourceId resID)
/*     */   {
/* 158 */     this.resourceLocksMutex.down();
/* 159 */     Semaphore lock = (Semaphore)this.resourceLocks.get(resID);
/* 160 */     if (lock == null) {
/* 161 */       lock = new Semaphore(1);
/* 162 */       this.resourceLocks.put(resID, lock);
/*     */     }
/* 164 */     this.resourceLocksMutex.up();
/* 165 */     lock.down();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void releaseResourceLock(Pager.ResourceId resID)
/*     */   {
/* 173 */     this.resourceLocksMutex.down();
/* 174 */     Semaphore lock = (Semaphore)this.resourceLocks.get(resID);
/* 175 */     if (lock != null) {
/* 176 */       this.resourceLocks.remove(resID);
/* 177 */       lock.up();
/*     */     }
/* 179 */     this.resourceLocksMutex.up();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void writeOut(Pager.ResourceId resourceID, Pager.Resource rsrc)
/*     */   {
/* 187 */     Task task = new Task(resourceID, rsrc);
/* 188 */     getResourceLock(resourceID);
/* 189 */     this.queueAvailable.down();
/* 190 */     this.queueMutex.down();
/* 191 */     this.taskQueue.enqueue(task);
/* 192 */     this.queueMutex.up();
/* 193 */     this.queueSize.up();
/*     */     int waterLevel;
/* 195 */     if ((waterLevel = this.queueSize.getAvailable()) > this.highWaterMark)
/* 196 */       this.highWaterMark = waterLevel;
/* 197 */     this.pageOutRequests += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean resourceExists(Pager.ResourceId rsrcID)
/*     */   {
/* 208 */     return (this.resourceLocks.get(rsrcID) != null) || (rsrcID.resourceExists());
/*     */   }
/*     */   
/*     */   public synchronized Pager.Resource readIn(Pager.ResourceId rsrcID)
/*     */   {
/* 213 */     this.pageInRequests += 1;
/*     */     
/* 215 */     if (this.resourceLocks.get(rsrcID) != null) {
/* 216 */       this.couldHaveCanceledPageout += 1;
/*     */     }
/* 218 */     getResourceLock(rsrcID);
/*     */     try {
/* 220 */       ObjectInputStream ois = new ObjectInputStream(rsrcID.readResource());
/* 221 */       resource = (Pager.Resource)ois.readObject();
/*     */     } catch (IOException e) { Pager.Resource resource;
/* 223 */       throw new Error("Could not page in because of IOException: " + e.getMessage());
/*     */     } catch (ClassNotFoundException e) {
/* 225 */       throw new Error("Could not page in because of ClassNotFoundException: " + e.getMessage()); }
/*     */     Pager.Resource resource;
/* 227 */     releaseResourceLock(rsrcID);
/* 228 */     return resource;
/*     */   }
/*     */   
/*     */   public synchronized void killMe()
/*     */   {
/* 233 */     this.alive = false;
/* 234 */     this.queueSize.up();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void crash()
/*     */   {
/* 241 */     stop();
/* 242 */     this.taskQueue = null;
/* 243 */     this.resourceLocks = null;
/* 244 */     this.resourceLocksMutex = null;
/* 245 */     this.queueSize = null;
/* 246 */     this.queueMutex = null;
/* 247 */     this.queueAvailable = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 255 */     while (this.queueSize.getAvailable() > 0) {
/*     */       try {
/* 257 */         sleep(500L);
/*     */       } catch (InterruptedException localInterruptedException) {
/* 259 */         if (Aurelizer.debug_sounds != null)
/* 260 */           Aurelizer.debug_sounds.enter("error");
/* 261 */         throw new Error("Don't interrupt me!!!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printStats()
/*     */   {
/* 268 */     System.err.println("Diskman.couldHaveCanceledPageout = " + this.couldHaveCanceledPageout);
/* 269 */     this.couldHaveCanceledPageout = 0;
/* 270 */     System.err.println("Diskman.biggestQueueSize = " + this.highWaterMark);
/* 271 */     this.highWaterMark = 0;
/* 272 */     System.err.println("Diskman.pageOutRequests = " + this.pageOutRequests);
/* 273 */     this.pageOutRequests = 0;
/* 274 */     System.err.println("Diskman.pageInRequests = " + this.pageInRequests);
/* 275 */     this.pageInRequests = 0;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/DiskManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */