/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashSet;
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
/*    */ public class ElementSetCollector
/*    */   extends ElementCollector
/*    */ {
/* 19 */   private Set seen = new HashSet();
/*    */   
/*    */   protected void addElement(Object e) {
/* 22 */     super.addElement(e);
/* 23 */     this.seen.add(e);
/*    */   }
/*    */   
/*    */   public ElementSetCollector(ElementSource s)
/*    */   {
/* 28 */     super(s);
/*    */   }
/*    */   
/*    */   public ElementSetCollector() {}
/*    */   
/*    */   private boolean isPresent(Object el)
/*    */   {
/* 35 */     return this.seen.contains(el);
/*    */   }
/*    */   
/*    */   protected int newElementFromSource()
/*    */   {
/*    */     int status;
/*    */     Object element;
/*    */     do
/*    */     {
/* 44 */       if (this.source == null)
/* 45 */         return -1;
/* 46 */       status = this.source.status();
/* 47 */       if (status != 1) break;
/* 48 */       element = this.source.nextElement();
/* 49 */     } while (isPresent(element));
/* 50 */     addElement(element);
/* 51 */     return 1;
/*    */     
/*    */ 
/* 54 */     if (status == -1) {
/* 55 */       this.source = null;
/* 56 */       this.seen = null;
/*    */     }
/*    */     
/* 59 */     return status;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 66 */     ElementSetCollector testSet = new ElementSetCollector();
/* 67 */     testSet.setSource(
/* 68 */       testSet.elements().map(new ElementSetCollector.1())
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 75 */       .append(
/* 76 */       ElementSource.singleton(new Integer(1))));
/*    */     
/* 78 */     RemovableElementSource testSetEls = new ElementSetCollector(testSet.elements()).elements();
/*    */     
/* 80 */     while (testSetEls.status() == 1) {
/* 81 */       System.out.println(testSetEls.peekNextElement());
/* 82 */       testSetEls.removeNextElement();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/ElementSetCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */