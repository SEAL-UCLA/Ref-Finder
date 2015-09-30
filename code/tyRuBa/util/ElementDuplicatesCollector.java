/*    */ package tyRuBa.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElementDuplicatesCollector
/*    */   extends ElementCollector
/*    */ {
/*    */   public ElementDuplicatesCollector(ElementSource s)
/*    */   {
/* 12 */     super(s);
/*    */   }
/*    */   
/*    */ 
/*    */   public ElementDuplicatesCollector() {}
/*    */   
/*    */   protected int newElementFromSource()
/*    */   {
/*    */     int stat;
/* 21 */     if ((stat = this.source.status()) == 1) {
/* 22 */       Object element = this.source.nextElement();
/* 23 */       addElement(element);
/*    */     }
/* 25 */     return stat;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/ElementDuplicatesCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */