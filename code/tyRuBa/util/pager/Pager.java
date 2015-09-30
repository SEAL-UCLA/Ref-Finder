/*     */ package tyRuBa.util.pager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import junit.framework.Assert;
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
/*     */ public class Pager
/*     */ {
/*     */   public static abstract class Task
/*     */   {
/*  69 */     private boolean isChangedResource = false;
/*     */     
/*     */ 
/*  72 */     private Pager.Resource updatedResource = null;
/*     */     
/*     */     private final boolean mayChangeResource;
/*     */     
/*     */     public Task(boolean mayChangeResource)
/*     */     {
/*  78 */       this.mayChangeResource = mayChangeResource;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Object doIt(Pager.Resource paramResource);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final void changedResource(Pager.Resource changedResource)
/*     */     {
/*  98 */       Assert.assertTrue(this.mayChangeResource);
/*  99 */       this.isChangedResource = true;
/* 100 */       this.updatedResource = changedResource;
/*     */     }
/*     */     
/*     */     final boolean resourceIsChanged()
/*     */     {
/* 105 */       return this.isChangedResource;
/*     */     }
/*     */     
/*     */     public Pager.Resource getChangedResource()
/*     */     {
/* 110 */       return this.updatedResource;
/*     */     }
/*     */     
/*     */     public boolean mayChangeResource() {
/* 114 */       return this.mayChangeResource;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final class ResourceReferenceInfo
/*     */     extends DoubleLinkedList.Entry
/*     */   {
/*     */     private Pager.ResourceId resId;
/*     */     
/*     */ 
/*     */     private Pager.Resource resource;
/*     */     
/*     */ 
/*     */     private boolean referenced;
/*     */     
/*     */ 
/*     */     private long timeLastReferenced;
/*     */     
/*     */ 
/*     */     private int numReferences;
/*     */     
/*     */ 
/*     */     private boolean dirty;
/*     */     
/*     */ 
/*     */ 
/*     */     public ResourceReferenceInfo(Pager.ResourceId resId, Pager.Resource resource)
/*     */     {
/* 144 */       this.resId = resId;
/* 145 */       this.resource = resource;
/* 146 */       this.referenced = false;
/* 147 */       this.timeLastReferenced = 0L;
/* 148 */       this.dirty = false;
/* 149 */       this.numReferences = 0;
/*     */     }
/*     */     
/*     */     public Pager.ResourceId getResourceID() {
/* 153 */       return this.resId;
/*     */     }
/*     */     
/*     */     public Pager.Resource getResource() {
/* 157 */       return this.resource;
/*     */     }
/*     */     
/*     */     public void updateResource(Pager.Resource newResource)
/*     */     {
/* 162 */       this.resource = newResource;
/*     */     }
/*     */     
/*     */     public boolean isReferenced() {
/* 166 */       return this.referenced;
/*     */     }
/*     */     
/*     */     public void setReferenced(boolean referenced) {
/* 170 */       if (referenced) {
/* 171 */         incrementReferenceCounter();
/* 172 */         setTimeLastReferenced(System.currentTimeMillis());
/*     */       }
/* 174 */       this.referenced = referenced;
/*     */     }
/*     */     
/*     */     private void setTimeLastReferenced(long timeLastReferenced) {
/* 178 */       this.timeLastReferenced = timeLastReferenced;
/*     */     }
/*     */     
/*     */     private void incrementReferenceCounter() {
/* 182 */       this.numReferences += 1;
/*     */     }
/*     */     
/*     */     public void resetReferenceCounter() {
/* 186 */       this.numReferences = 0;
/*     */     }
/*     */     
/*     */     public boolean isDirty() {
/* 190 */       return this.dirty;
/*     */     }
/*     */     
/*     */     public void setDirty(boolean dirty) {
/* 194 */       synchronized (Pager.this.dirtyResources) {
/* 195 */         if (this.dirty == dirty) {
/* 196 */           return;
/*     */         }
/* 198 */         this.dirty = dirty;
/* 199 */         if (dirty) {
/* 200 */           Pager.this.dirtyResources.add(this);
/*     */         } else {
/* 202 */           Pager.this.dirtyResources.remove(this);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int compareTo(Object o) {
/* 208 */       ResourceReferenceInfo other = (ResourceReferenceInfo)o;
/*     */       
/* 210 */       long myCompareNumber = this.timeLastReferenced + this.numReferences;
/* 211 */       long otherCompareNumber = other.timeLastReferenced + other.numReferences;
/*     */       
/* 213 */       if (otherCompareNumber > myCompareNumber)
/* 214 */         return -1;
/* 215 */       if (otherCompareNumber < myCompareNumber) {
/* 216 */         return 1;
/*     */       }
/* 218 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 223 */     public String toString() { return "Rsrc(" + this.resId + (this.dirty ? "=DIRTY" : "") + ")"; } }
/*     */   public static abstract class ResourceId { public abstract boolean equals(Object paramObject);
/*     */     public abstract int hashCode();
/*     */     public abstract InputStream readResource() throws IOException;
/*     */     public abstract OutputStream writeResource() throws IOException;
/*     */     public abstract void removeResource();
/* 229 */     public abstract boolean resourceExists(); } private long lastTaskTime = System.currentTimeMillis();
/*     */   
/*     */ 
/*     */   private int cacheSize;
/*     */   
/*     */ 
/* 235 */   private Map inMemory = new HashMap();
/*     */   
/*     */ 
/* 238 */   private Set dirtyResources = new HashSet();
/*     */   
/*     */ 
/*     */   private DiskManager diskMan;
/*     */   
/*     */ 
/*     */   private DoubleLinkedList lruQueue;
/*     */   
/*     */   private boolean needToCallBackup;
/*     */   
/*     */ 
/* 249 */   private PageCleaner pageCleaner = null;
/*     */   
/*     */   public static abstract interface Resource
/*     */     extends Serializable
/*     */   {}
/*     */   
/*     */   class PageCleaner extends Thread
/*     */   {
/*     */     public PageCleaner()
/*     */     {
/* 259 */       super();
/* 260 */       setPriority(1);
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       for (;;) {
/* 266 */         Pager.ResourceReferenceInfo toClean = null;
/* 267 */         if (!Pager.this.diskMan.isAlive())
/* 268 */           return;
/* 269 */         synchronized (Pager.this.dirtyResources) {
/* 270 */           boolean cleaningTime = (!Pager.this.dirtyResources.isEmpty()) && (Pager.this.diskMan.isIdle()) && (isIdle());
/* 271 */           if (cleaningTime)
/* 272 */             toClean = (Pager.ResourceReferenceInfo)Pager.this.dirtyResources.iterator().next(); }
/*     */         boolean cleaningTime;
/* 274 */         if (cleaningTime)
/*     */         {
/*     */ 
/* 277 */           Pager.this.writeResourceToDisk(toClean);
/*     */         }
/*     */         try
/*     */         {
/* 281 */           sleep(300L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isIdle() {
/* 288 */       long idletime = System.currentTimeMillis() - Pager.this.lastTaskTime;
/*     */       
/* 290 */       return idletime > 4000L;
/*     */     }
/*     */   }
/*     */   
/*     */   public Pager(int cacheSize, int queueSize, long lastBackupTime, boolean backgrounCleaning)
/*     */   {
/* 296 */     this.cacheSize = cacheSize;
/* 297 */     this.needToCallBackup = false;
/* 298 */     this.lruQueue = new DoubleLinkedList();
/* 299 */     this.diskMan = new DiskManager(queueSize);
/* 300 */     this.diskMan.setPriority(10);
/* 301 */     this.diskMan.start();
/* 302 */     if (backgrounCleaning) {
/* 303 */       this.pageCleaner = new PageCleaner();
/* 304 */       this.pageCleaner.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void enableBackGroundPaging() {}
/*     */   
/*     */   /* Error */
/*     */   public Object synchDoTask(ResourceId rsrcID, Task task)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore_3
/*     */     //   2: aload_0
/*     */     //   3: invokestatic 27	java/lang/System:currentTimeMillis	()J
/*     */     //   6: putfield 33	tyRuBa/util/pager/Pager:lastTaskTime	J
/*     */     //   9: aload_0
/*     */     //   10: aload_1
/*     */     //   11: invokespecial 86	tyRuBa/util/pager/Pager:getResource	(LtyRuBa/util/pager/Pager$ResourceId;)LtyRuBa/util/pager/Pager$Resource;
/*     */     //   14: astore 4
/*     */     //   16: aload_2
/*     */     //   17: invokevirtual 90	tyRuBa/util/pager/Pager$Task:mayChangeResource	()Z
/*     */     //   20: ifeq +33 -> 53
/*     */     //   23: aload_0
/*     */     //   24: getfield 61	tyRuBa/util/pager/Pager:diskMan	LtyRuBa/util/pager/DiskManager;
/*     */     //   27: aload_1
/*     */     //   28: invokevirtual 96	tyRuBa/util/pager/DiskManager:getResourceLock	(LtyRuBa/util/pager/Pager$ResourceId;)V
/*     */     //   31: iconst_1
/*     */     //   32: istore_3
/*     */     //   33: goto +20 -> 53
/*     */     //   36: astore 5
/*     */     //   38: iload_3
/*     */     //   39: ifeq +11 -> 50
/*     */     //   42: aload_0
/*     */     //   43: getfield 61	tyRuBa/util/pager/Pager:diskMan	LtyRuBa/util/pager/DiskManager;
/*     */     //   46: aload_1
/*     */     //   47: invokevirtual 100	tyRuBa/util/pager/DiskManager:releaseResourceLock	(LtyRuBa/util/pager/Pager$ResourceId;)V
/*     */     //   50: aload 5
/*     */     //   52: athrow
/*     */     //   53: iload_3
/*     */     //   54: ifeq +11 -> 65
/*     */     //   57: aload_0
/*     */     //   58: getfield 61	tyRuBa/util/pager/Pager:diskMan	LtyRuBa/util/pager/DiskManager;
/*     */     //   61: aload_1
/*     */     //   62: invokevirtual 100	tyRuBa/util/pager/DiskManager:releaseResourceLock	(LtyRuBa/util/pager/Pager$ResourceId;)V
/*     */     //   65: aload_2
/*     */     //   66: aload 4
/*     */     //   68: invokevirtual 103	tyRuBa/util/pager/Pager$Task:doIt	(LtyRuBa/util/pager/Pager$Resource;)Ljava/lang/Object;
/*     */     //   71: astore 5
/*     */     //   73: aload_2
/*     */     //   74: invokevirtual 107	tyRuBa/util/pager/Pager$Task:resourceIsChanged	()Z
/*     */     //   77: ifeq +15 -> 92
/*     */     //   80: aload_0
/*     */     //   81: aload_1
/*     */     //   82: aload_2
/*     */     //   83: invokevirtual 110	tyRuBa/util/pager/Pager$Task:getChangedResource	()LtyRuBa/util/pager/Pager$Resource;
/*     */     //   86: invokespecial 114	tyRuBa/util/pager/Pager:changeResource	(LtyRuBa/util/pager/Pager$ResourceId;LtyRuBa/util/pager/Pager$Resource;)V
/*     */     //   89: goto +8 -> 97
/*     */     //   92: aload_0
/*     */     //   93: aload_1
/*     */     //   94: invokespecial 118	tyRuBa/util/pager/Pager:referenceResource	(LtyRuBa/util/pager/Pager$ResourceId;)V
/*     */     //   97: aload 5
/*     */     //   99: areturn
/*     */     // Line number table:
/*     */     //   Java source line #317	-> byte code offset #0
/*     */     //   Java source line #318	-> byte code offset #2
/*     */     //   Java source line #319	-> byte code offset #9
/*     */     //   Java source line #321	-> byte code offset #16
/*     */     //   Java source line #322	-> byte code offset #23
/*     */     //   Java source line #323	-> byte code offset #31
/*     */     //   Java source line #326	-> byte code offset #36
/*     */     //   Java source line #327	-> byte code offset #38
/*     */     //   Java source line #328	-> byte code offset #42
/*     */     //   Java source line #329	-> byte code offset #50
/*     */     //   Java source line #327	-> byte code offset #53
/*     */     //   Java source line #328	-> byte code offset #57
/*     */     //   Java source line #330	-> byte code offset #65
/*     */     //   Java source line #331	-> byte code offset #73
/*     */     //   Java source line #332	-> byte code offset #80
/*     */     //   Java source line #334	-> byte code offset #92
/*     */     //   Java source line #336	-> byte code offset #97
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	100	0	this	Pager
/*     */     //   0	100	1	rsrcID	ResourceId
/*     */     //   0	100	2	task	Task
/*     */     //   1	53	3	lockHeld	boolean
/*     */     //   14	53	4	rsrc	Resource
/*     */     //   36	15	5	localObject1	Object
/*     */     //   71	27	5	result	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	36	36	finally
/*     */   }
/*     */   
/*     */   public void asynchDoTask(ResourceId rsrcID, Task task)
/*     */   {
/* 344 */     this.lastTaskTime = System.currentTimeMillis();
/* 345 */     synchDoTask(rsrcID, task);
/*     */   }
/*     */   
/*     */   private void referenceResource(ResourceId rsrcID)
/*     */   {
/* 350 */     ResourceReferenceInfo rsrc_ref = (ResourceReferenceInfo)this.inMemory.get(rsrcID);
/* 351 */     if (rsrc_ref != null) {
/* 352 */       rsrc_ref.setReferenced(true);
/* 353 */       rsrc_ref.incrementReferenceCounter();
/* 354 */       this.lruQueue.remove(rsrc_ref);
/* 355 */       this.lruQueue.enqueue(rsrc_ref);
/*     */     }
/*     */   }
/*     */   
/*     */   private void changeResource(ResourceId rsrcID, Resource newResource)
/*     */   {
/* 361 */     this.needToCallBackup = true;
/* 362 */     ResourceReferenceInfo rsrc_ref = (ResourceReferenceInfo)this.inMemory.get(rsrcID);
/* 363 */     if (rsrc_ref == null)
/*     */     {
/* 365 */       ResourceReferenceInfo newInfo = new ResourceReferenceInfo(rsrcID, newResource);
/* 366 */       newInfo.setDirty(true);
/* 367 */       newInfo.setReferenced(true);
/* 368 */       if (needToPageOutIfOneMoreAdded()) {
/* 369 */         pageOutOne();
/*     */       }
/* 371 */       this.inMemory.put(rsrcID, newInfo);
/* 372 */       this.lruQueue.enqueue(newInfo);
/*     */     }
/* 374 */     else if (newResource == null)
/*     */     {
/* 376 */       rsrcID.removeResource();
/* 377 */       this.inMemory.remove(rsrcID);
/* 378 */       this.lruQueue.remove(rsrc_ref);
/*     */     }
/*     */     else {
/* 381 */       rsrc_ref.updateResource(newResource);
/* 382 */       rsrc_ref.setDirty(true);
/* 383 */       rsrc_ref.setReferenced(true);
/* 384 */       this.lruQueue.remove(rsrc_ref);
/* 385 */       this.lruQueue.enqueue(rsrc_ref);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Resource getResource(ResourceId rsrcID)
/*     */   {
/* 392 */     ResourceReferenceInfo rsrc_ref = (ResourceReferenceInfo)this.inMemory.get(rsrcID);
/* 393 */     if (rsrc_ref == null) {
/* 394 */       Resource result = getResourceFromDisk(rsrcID);
/* 395 */       return result;
/*     */     }
/* 397 */     Resource result = rsrc_ref.getResource();
/* 398 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void backup()
/*     */   {
/* 405 */     if (this.needToCallBackup) {
/* 406 */       for (Iterator iter = this.inMemory.values().iterator(); iter.hasNext();) {
/* 407 */         ResourceReferenceInfo refInfo = (ResourceReferenceInfo)iter.next();
/* 408 */         if (refInfo.isDirty()) {
/* 409 */           writeResourceToDisk(refInfo);
/*     */         }
/*     */       }
/* 412 */       this.needToCallBackup = false;
/*     */     }
/* 414 */     this.diskMan.flush();
/*     */   }
/*     */   
/*     */   public void shutdown()
/*     */   {
/* 419 */     backup();
/* 420 */     this.diskMan.killMe();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void crash()
/*     */   {
/* 428 */     this.diskMan.crash();
/*     */   }
/*     */   
/*     */   public void setCacheSize(int cacheSize)
/*     */   {
/* 433 */     this.cacheSize = cacheSize;
/* 434 */     pageUntilNonNeeded();
/*     */   }
/*     */   
/*     */   public int getCacheSize()
/*     */   {
/* 439 */     return this.cacheSize;
/*     */   }
/*     */   
/*     */   private boolean needToPageOut()
/*     */   {
/* 444 */     return this.inMemory.size() > this.cacheSize;
/*     */   }
/*     */   
/*     */   private boolean needToPageOutIfOneMoreAdded()
/*     */   {
/* 449 */     return this.inMemory.size() + 1 > this.cacheSize;
/*     */   }
/*     */   
/*     */   private void pageOutOne()
/*     */   {
/* 454 */     if (this.inMemory.size() > 0) {
/* 455 */       ResourceReferenceInfo victim = null;
/*     */       
/*     */ 
/* 458 */       victim = (ResourceReferenceInfo)this.lruQueue.dequeue();
/* 459 */       this.inMemory.remove(victim.getResourceID());
/*     */       
/*     */ 
/* 462 */       if (victim.isDirty()) {
/* 463 */         writeResourceToDisk(victim);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void pageUntilNonNeeded()
/*     */   {
/* 473 */     while (needToPageOut()) {
/* 474 */       pageOutOne();
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeResourceToDisk(ResourceReferenceInfo victim)
/*     */   {
/* 480 */     this.diskMan.writeOut(victim.getResourceID(), victim.getResource());
/* 481 */     victim.setDirty(false);
/*     */   }
/*     */   
/*     */   private Resource getResourceFromDisk(ResourceId rsrcID)
/*     */   {
/* 486 */     if (this.diskMan.resourceExists(rsrcID)) {
/* 487 */       if (needToPageOutIfOneMoreAdded()) {
/* 488 */         pageOutOne();
/*     */       }
/*     */       
/* 491 */       Resource resource = this.diskMan.readIn(rsrcID);
/* 492 */       changeResource(rsrcID, resource);
/* 493 */       return resource;
/*     */     }
/* 495 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void printStats()
/*     */   {
/* 501 */     this.diskMan.printStats();
/*     */   }
/*     */   
/*     */   public boolean isDirty() {
/* 505 */     synchronized (this.dirtyResources) {
/* 506 */       return !this.dirtyResources.isEmpty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/Pager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */