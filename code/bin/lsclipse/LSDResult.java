/*    */ package lsclipse;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import lsd.rule.LSDFact;
/*    */ import lsd.rule.LSDVariable;
/*    */ 
/*    */ public class LSDResult
/*    */ {
/*    */   public int num_matches;
/*    */   public int num_counter;
/*    */   public String desc;
/*    */   public java.util.List<LSDFact> examples;
/*    */   public java.util.List<Map<LSDVariable, String>> exceptions;
/* 16 */   private ArrayList<String> examplesString = null;
/* 17 */   private ArrayList<String> exceptionsString = null;
/*    */   
/* 19 */   public ArrayList<String> getExampleStr() { if (this.examplesString == null) {
/* 20 */       this.examplesString = new ArrayList();
/* 21 */       for (LSDFact fact : this.examples) {
/* 22 */         this.examplesString.add(fact.toString());
/*    */       }
/*    */     }
/* 25 */     return this.examplesString;
/*    */   }
/*    */   
/* 28 */   public ArrayList<String> getExceptionsString() { if (this.exceptionsString == null) {
/* 29 */       this.exceptionsString = new ArrayList();
/* 30 */       for (Map<LSDVariable, String> exception : this.exceptions) {
/* 31 */         StringBuilder s = new StringBuilder();
/* 32 */         s.append("[ ");
/* 33 */         for (Map.Entry<LSDVariable, String> entry : exception.entrySet()) {
/* 34 */           s.append(entry.getKey());
/* 35 */           s.append("=\"");
/* 36 */           s.append((String)entry.getValue());
/* 37 */           s.append("\" ");
/*    */         }
/* 39 */         s.append("]");
/* 40 */         this.exceptionsString.add(s.toString());
/*    */       }
/*    */     }
/* 43 */     return this.exceptionsString;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/LSDResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */