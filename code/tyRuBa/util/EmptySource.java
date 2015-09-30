/*    */ package tyRuBa.util;
/*    */ 
/*    */ class EmptySource extends ElementSource
/*    */ {
/*  5 */   public static EmptySource the = new EmptySource();
/*    */   
/*    */   public int status() {
/*  8 */     return -1;
/*    */   }
/*    */   
/* 11 */   public Object nextElement() { throw new Error("TheEmpty ElementSource has no elements"); }
/*    */   
/*    */   public ElementSource append(ElementSource other)
/*    */   {
/* 15 */     return other;
/*    */   }
/*    */   
/*    */   public ElementSource map(Action what) {
/* 19 */     return theEmpty;
/*    */   }
/*    */   
/* 22 */   public void print(PrintingState p) { p.print("{*empty*}"); }
/*    */   
/*    */   public boolean isEmpty() {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public ElementSource first() {
/* 29 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/EmptySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */