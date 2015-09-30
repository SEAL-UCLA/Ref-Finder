/*    */ package lsclipse;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class LCS {
/*    */   public static String getLCS(String x, String y) {
/*  7 */     int M = x.length();
/*  8 */     int N = y.length();
/*    */     
/*    */ 
/* 11 */     int[][] opt = new int[M + 1][N + 1];
/*    */     
/*    */ 
/* 14 */     for (int i = M - 1; i >= 0; i--) {
/* 15 */       for (int j = N - 1; j >= 0; j--) {
/* 16 */         if (x.charAt(i) == y.charAt(j)) {
/* 17 */           opt[i][j] = (opt[(i + 1)][(j + 1)] + 1);
/*    */         } else {
/* 19 */           opt[i][j] = Math.max(opt[(i + 1)][j], opt[i][(j + 1)]);
/*    */         }
/*    */       }
/*    */     }
/* 23 */     String output = "";
/* 24 */     int i = 0;int j = 0;
/* 25 */     while ((i < M) && (j < N))
/* 26 */       if (x.charAt(i) == y.charAt(j)) {
/* 27 */         output = output + x.charAt(i);
/* 28 */         i++;
/* 29 */         j++;
/*    */       }
/* 31 */       else if (opt[(i + 1)][j] >= opt[i][(j + 1)]) { i++;
/* 32 */       } else { j++;
/*    */       }
/* 34 */     return output;
/*    */   }
/*    */   
/*    */ 
/*    */   public static void allSubSequences(String x, String y) {}
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 42 */     String y = "int output=super.getSpeed() + 200;   return output; } ";
/* 43 */     String x = "";
/* 44 */     System.out.println("The output is: " + getLCS(x, y));
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/LCS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */