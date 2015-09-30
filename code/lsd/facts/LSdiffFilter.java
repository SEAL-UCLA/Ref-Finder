/*    */ package lsd.facts;
/*    */ 
/*    */ public class LSdiffFilter
/*    */ {
/*    */   final boolean packageLevel;
/*    */   final boolean typeLevel;
/*    */   final boolean methodLevel;
/*    */   final boolean fieldLevel;
/*    */   final boolean bodyLevel;
/*    */   
/*    */   public LSdiffFilter(boolean packageL, boolean classL, boolean methodL, boolean fieldL, boolean bodyL) {
/* 12 */     this.packageLevel = packageL;
/* 13 */     this.typeLevel = classL;
/* 14 */     this.methodLevel = methodL;
/* 15 */     this.fieldLevel = fieldL;
/* 16 */     this.bodyLevel = bodyL;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/facts/LSdiffFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */