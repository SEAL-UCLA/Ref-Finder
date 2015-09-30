/*     */ package tyRuBa.util;
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
/*     */ class ElementCollectorSource
/*     */   extends RemovableElementSource
/*     */ {
/*     */   private ElementCollector myCollector;
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
/*     */   private RemovableElementSource pos;
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
/*     */   ElementCollectorSource(ElementCollector aCollector)
/*     */   {
/* 106 */     this.myCollector = aCollector;
/* 107 */     this.pos = this.myCollector.elementStore.elements();
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
/*     */   public int status()
/*     */   {
/* 120 */     if (this.pos.hasMoreElements())
/*     */     {
/* 122 */       return 1; }
/* 123 */     if (this.myCollector != null)
/*     */     {
/* 125 */       int st = this.myCollector.kick();
/*     */       
/*     */ 
/* 128 */       if (st == -1) {
/* 129 */         this.myCollector = null;
/*     */       }
/* 131 */       return st;
/*     */     }
/* 133 */     return -1;
/*     */   }
/*     */   
/*     */   public void removeNextElement() {
/* 137 */     status();
/* 138 */     this.pos.removeNextElement();
/*     */   }
/*     */   
/*     */   public Object peekNextElement() {
/* 142 */     return this.pos.peekNextElement();
/*     */   }
/*     */   
/*     */   public Object nextElement() {
/* 146 */     status();
/* 147 */     return this.pos.nextElement();
/*     */   }
/*     */   
/*     */   public void print(PrintingState p) {
/* 151 */     p.print("CollectorSource(");
/* 152 */     p.indent();p.newline();
/* 153 */     p.print("pos= ");
/* 154 */     p.indent();
/* 155 */     this.pos.print(p);
/* 156 */     p.outdent();p.newline();
/* 157 */     p.print("on = ");
/* 158 */     p.indent();
/* 159 */     if (this.myCollector == null) {
/* 160 */       p.print("null");
/*     */     } else
/* 162 */       this.myCollector.print(p);
/* 163 */     p.outdent();
/* 164 */     p.outdent();
/* 165 */     p.print(")");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/ElementCollectorSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */