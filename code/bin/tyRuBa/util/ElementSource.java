/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ElementSource
/*     */ {
/*     */   public static final int ELEMENT_READY = 1;
/*     */   public static final int NO_ELEMENTS_READY = 0;
/*     */   public static final int NO_MORE_ELEMENTS = -1;
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/*  26 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void print(PrintingState paramPrintingState);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int status();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract Object nextElement();
/*     */   
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*  44 */     return status() == 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public static ElementSource singleton(Object e)
/*     */   {
/*  50 */     return new ElementSource.1(e);
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
/*  82 */   public static final ElementSource theEmpty = EmptySource.the;
/*     */   
/*     */ 
/*     */   public ElementSource append(ElementSource other)
/*     */   {
/*  87 */     if (other.isEmpty()) {
/*  88 */       return this;
/*     */     }
/*  90 */     return new AppendSource(this, other);
/*     */   }
/*     */   
/*     */   public ElementSource map(Action what)
/*     */   {
/*  95 */     return new MapElementSource(this, what);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ElementSource with(Object[] els)
/*     */   {
/* 115 */     return new ElementSource.2(els);
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
/*     */   public static ElementSource with(ArrayList els)
/*     */   {
/* 144 */     if (els.isEmpty()) {
/* 145 */       return theEmpty;
/*     */     }
/* 147 */     return new ArrayListSource(els);
/*     */   }
/*     */   
/*     */   public static ElementSource with(Iterator it)
/*     */   {
/* 152 */     return new ElementSource.3(it);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void forceAll()
/*     */   {
/* 176 */     while (hasMoreElements())
/* 177 */       nextElement();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 181 */     ByteArrayOutputStream result = new ByteArrayOutputStream();
/* 182 */     print(new PrintingState(new PrintStream(result)));
/* 183 */     return result.toString();
/*     */   }
/*     */   
/*     */   public ElementSource first() {
/* 187 */     return new First(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ElementSource immediateFirst()
/*     */   {
/* 194 */     int stat = status();
/* 195 */     if (stat == 1)
/* 196 */       return singleton(nextElement());
/* 197 */     if (stat == -1) {
/* 198 */       return theEmpty;
/*     */     }
/* 200 */     return first();
/*     */   }
/*     */   
/*     */   public ElementSource flatten() {
/* 204 */     return new FlattenElementSource(this);
/*     */   }
/*     */   
/*     */   public SynchronizedElementSource synchronizeOn(SynchResource resource) {
/* 208 */     return new SynchronizedElementSource(resource, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int countElements()
/*     */   {
/* 218 */     int result = 0;
/* 219 */     while (hasMoreElements()) {
/* 220 */       nextElement();
/* 221 */       result++;
/*     */     }
/* 223 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object firstElementOrNull()
/*     */   {
/* 239 */     if (hasMoreElements()) {
/* 240 */       Object result = nextElement();
/* 241 */       release();
/* 242 */       return result;
/*     */     }
/*     */     
/* 245 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/ElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */