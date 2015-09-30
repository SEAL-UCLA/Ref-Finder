/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ public abstract class ElementCollector
/*    */ {
/* 17 */   LinkedList elementStore = new LinkedList();
/*    */   
/*    */   protected void addElement(Object e)
/*    */   {
/* 21 */     this.elementStore.addElement(e);
/*    */   }
/*    */   
/*    */   public ElementCollector(ElementSource s)
/*    */   {
/* 26 */     setSource(s);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ElementCollector() {}
/*    */   
/*    */ 
/*    */   public RemovableElementSource elements()
/*    */   {
/* 36 */     return new ElementCollectorSource(this);
/*    */   }
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
/* 62 */   private boolean hurting = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */   protected ElementSource source = ElementSource.theEmpty;
/*    */   
/*    */   /* Error */
/*    */   protected final int kick()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 32	tyRuBa/util/ElementCollector:hurting	Z
/*    */     //   4: ifne +31 -> 35
/*    */     //   7: aload_0
/*    */     //   8: iconst_1
/*    */     //   9: putfield 32	tyRuBa/util/ElementCollector:hurting	Z
/*    */     //   12: aload_0
/*    */     //   13: invokevirtual 54	tyRuBa/util/ElementCollector:newElementFromSource	()I
/*    */     //   16: istore_1
/*    */     //   17: goto +11 -> 28
/*    */     //   20: astore_2
/*    */     //   21: aload_0
/*    */     //   22: iconst_0
/*    */     //   23: putfield 32	tyRuBa/util/ElementCollector:hurting	Z
/*    */     //   26: aload_2
/*    */     //   27: athrow
/*    */     //   28: aload_0
/*    */     //   29: iconst_0
/*    */     //   30: putfield 32	tyRuBa/util/ElementCollector:hurting	Z
/*    */     //   33: iload_1
/*    */     //   34: ireturn
/*    */     //   35: iconst_0
/*    */     //   36: ireturn
/*    */     // Line number table:
/*    */     //   Java source line #42	-> byte code offset #0
/*    */     //   Java source line #45	-> byte code offset #7
/*    */     //   Java source line #46	-> byte code offset #12
/*    */     //   Java source line #47	-> byte code offset #20
/*    */     //   Java source line #50	-> byte code offset #21
/*    */     //   Java source line #51	-> byte code offset #26
/*    */     //   Java source line #50	-> byte code offset #28
/*    */     //   Java source line #52	-> byte code offset #33
/*    */     //   Java source line #55	-> byte code offset #35
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	37	0	this	ElementCollector
/*    */     //   16	2	1	foundElement	int
/*    */     //   28	6	1	foundElement	int
/*    */     //   20	7	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	20	20	finally
/*    */   }
/*    */   
/*    */   protected abstract int newElementFromSource();
/*    */   
/*    */   public void setSource(ElementSource source)
/*    */   {
/* 74 */     this.source = source;
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 78 */     p.print(toString());
/* 79 */     if (!p.visited.contains(this)) {
/* 80 */       p.visited.add(this);
/* 81 */       p.print("(");
/* 82 */       p.indent();
/* 83 */       p.newline();
/* 84 */       p.print("collected= ");
/* 85 */       p.indent();
/* 86 */       this.elementStore.elements().print(p);
/* 87 */       p.outdent();
/* 88 */       p.newline();
/* 89 */       p.print("source= ");
/* 90 */       p.indent();
/* 91 */       if (this.source == null) {
/* 92 */         p.print("null");
/*    */       } else
/* 94 */         this.source.print(p);
/* 95 */       p.outdent();
/* 96 */       p.outdent();
/* 97 */       p.print(")");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/ElementCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */