/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class ArrayListSource extends ElementSource
/*    */ {
/*  7 */   int pos = 0;
/*    */   int sz;
/*    */   ArrayList els;
/*    */   
/*    */   public ArrayListSource(ArrayList els) {
/* 12 */     this.els = els;
/* 13 */     this.sz = els.size();
/*    */   }
/*    */   
/*    */   public int status() {
/* 17 */     return this.pos < this.sz ? 1 : -1;
/*    */   }
/*    */   
/* 20 */   public Object nextElement() { return this.els.get(this.pos++); }
/*    */   
/*    */   public void print(PrintingState p) {
/* 23 */     p.print("{");
/* 24 */     for (int i = this.pos; i < this.els.size(); i++) {
/* 25 */       if (i > 0)
/* 26 */         p.print(",");
/* 27 */       p.print(this.els.get(i).toString());
/*    */     }
/* 29 */     p.print("}");
/*    */   }
/*    */   
/*    */   public ElementSource first()
/*    */   {
/* 34 */     if (hasMoreElements()) {
/* 35 */       return ElementSource.singleton(nextElement());
/*    */     }
/* 37 */     return ElementSource.theEmpty;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/ArrayListSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */