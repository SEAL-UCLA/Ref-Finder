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
/*    */ public abstract class ConsList
/*    */ {
/* 33 */   public static final ConsList theEmpty = new ConsList.1();
/*    */   
/*    */ 
/*    */   public abstract boolean isEmpty();
/*    */   
/*    */ 
/*    */   public abstract Object car();
/*    */   
/*    */ 
/*    */   public abstract ConsList cdr();
/*    */   
/*    */ 
/*    */   public static ConsList cons(Object acar, ConsList acdr)
/*    */   {
/* 47 */     return new ConsList.ConsCel(acar, acdr);
/*    */   }
/*    */   
/*    */   public int length() {
/* 51 */     if (isEmpty()) {
/* 52 */       return 0;
/*    */     }
/* 54 */     return 1 + cdr().length();
/*    */   }
/*    */   
/*    */   public Object[] asArray() {
/* 58 */     ConsList rest = this;
/* 59 */     int len = length();
/* 60 */     Object[] result = new Object[len];
/* 61 */     for (int i = 0; i < len; i++) {
/* 62 */       result[i] = rest.car();
/* 63 */       rest = rest.cdr();
/*    */     }
/* 65 */     return result;
/*    */   }
/*    */   
/*    */   public Object[] reverseArray() {
/* 69 */     ConsList rest = this;
/* 70 */     int len = length();
/* 71 */     Object[] result = new Object[len];
/* 72 */     for (int i = len - 1; i >= 0; i--) {
/* 73 */       result[i] = rest.car();
/* 74 */       rest = rest.cdr();
/*    */     }
/* 76 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 80 */     Object[] els = asArray();
/* 81 */     StringBuffer text = new StringBuffer();
/* 82 */     for (int i = 0; i < els.length; i++) {
/* 83 */       if (i > 0) {
/* 84 */         text.append("/");
/*    */       }
/* 86 */       text.append(els[i].toString());
/*    */     }
/* 88 */     return text.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/ConsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */