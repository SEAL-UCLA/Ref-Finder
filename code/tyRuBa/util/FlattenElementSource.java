/*    */ package tyRuBa.util;
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
/*    */ public class FlattenElementSource
/*    */   extends ElementSource
/*    */ {
/* 16 */   Object e = null;
/*    */   
/*    */ 
/* 19 */   private ElementCollector sources = new ElementDuplicatesCollector();
/*    */   
/*    */   public FlattenElementSource(ElementSource metaSource)
/*    */   {
/* 23 */     this.sources.setSource(metaSource);
/*    */   }
/*    */   
/*    */   public int status() {
/* 27 */     if (this.e != null) {
/* 28 */       return 1;
/*    */     }
/* 30 */     RemovableElementSource remainingSources = this.sources.elements();
/*    */     
/* 32 */     while ((stat = remainingSources.status()) == 1) {
/* 33 */       ElementSource firstSource = 
/* 34 */         (ElementSource)remainingSources.peekNextElement();
/* 35 */       int stat = firstSource.status();
/* 36 */       switch (stat) {
/*    */       case 1: 
/* 38 */         this.e = firstSource.nextElement();
/* 39 */         return 1;
/*    */       case -1: 
/* 41 */         remainingSources.removeNextElement();
/*    */         
/*    */ 
/* 44 */         break;
/*    */       case 0: 
/* 46 */         remainingSources.nextElement();
/*    */       }
/*    */       
/*    */     }
/*    */     
/* 51 */     int stat = this.sources.elements().status();
/* 52 */     if (stat == -1) {
/* 53 */       return -1;
/*    */     }
/* 55 */     return 0;
/*    */   }
/*    */   
/*    */   public Object nextElement()
/*    */   {
/* 60 */     status();
/* 61 */     Object result = this.e;
/* 62 */     this.e = null;
/* 63 */     return result;
/*    */   }
/*    */   
/*    */   public void print(PrintingState p)
/*    */   {
/* 68 */     p.print("Flatten(");
/* 69 */     p.indent();p.newline();
/* 70 */     this.sources.print(p);
/* 71 */     p.outdent();
/* 72 */     p.print(")");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/FlattenElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */