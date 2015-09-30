/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTwoLevelKey
/*    */   implements TwoLevelKey, Serializable
/*    */ {
/*    */   String first;
/*    */   Object second;
/*    */   
/*    */   public SimpleTwoLevelKey(String first, Object second)
/*    */   {
/* 17 */     this.first = first;
/* 18 */     if ((this.first instanceof String)) {
/* 19 */       this.first = this.first.intern();
/*    */     }
/* 21 */     this.second = second;
/* 22 */     if ((this.second instanceof String)) {
/* 23 */       this.second = ((String)this.second).intern();
/*    */     }
/*    */   }
/*    */   
/*    */   public String getFirst() {
/* 28 */     return this.first;
/*    */   }
/*    */   
/*    */   public Object getSecond() {
/* 32 */     return this.second;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/SimpleTwoLevelKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */