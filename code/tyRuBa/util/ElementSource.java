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
/*  50 */     new ElementSource()
/*     */     {
/*     */       public int status() {
/*  53 */         if (ElementSource.this == null) {
/*  54 */           return -1;
/*     */         }
/*  56 */         return 1;
/*     */       }
/*     */       
/*  59 */       public Object nextElement() { Object el = ElementSource.this;
/*  60 */         this.myElement = null;
/*  61 */         return el;
/*     */       }
/*     */       
/*  64 */       public void print(PrintingState p) { p.print("{");
/*  65 */         if (ElementSource.this == null) {
/*  66 */           p.print("null");
/*     */         } else
/*  68 */           p.print(ElementSource.this.toString());
/*  69 */         p.print("}");
/*     */       }
/*     */       
/*  72 */       public boolean isEmpty() { return ElementSource.this == null; }
/*     */       
/*     */       public ElementSource first() {
/*  75 */         return this;
/*     */       }
/*     */     };
/*     */   }
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
/* 115 */     new ElementSource() {
/* 116 */       int pos = 0;
/*     */       
/* 118 */       public int status() { return this.pos < ElementSource.this.length ? 1 : -1; }
/*     */       
/*     */ 
/* 121 */       public Object nextElement() { return ElementSource.this[(this.pos++)]; }
/*     */       
/*     */       public void print(PrintingState p) {
/* 124 */         p.print("{");
/* 125 */         for (int i = 0; i < ElementSource.this.length; i++) {
/* 126 */           if (i > 0)
/* 127 */             p.print(",");
/* 128 */           p.print(ElementSource.this[i].toString());
/*     */         }
/* 130 */         p.print("}");
/*     */       }
/*     */       
/*     */       public ElementSource first() {
/* 134 */         if (hasMoreElements()) {
/* 135 */           return ElementSource.singleton(nextElement());
/*     */         }
/* 137 */         return ElementSource.theEmpty;
/*     */       }
/*     */     };
/*     */   }
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
/* 152 */     new ElementSource()
/*     */     {
/*     */       public int status() {
/* 155 */         return ElementSource.this.hasNext() ? 1 : -1;
/*     */       }
/*     */       
/*     */       public Object nextElement() {
/* 159 */         return ElementSource.this.next();
/*     */       }
/*     */       
/*     */       public void print(PrintingState p) {
/* 163 */         p.print("{");
/* 164 */         p.print("NOT CURRENTLY SUPPORTED");
/* 165 */         p.print("}");
/*     */       }
/*     */     };
/*     */   }
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


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/ElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */