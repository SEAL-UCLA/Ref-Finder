/*    */ package changetypes;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChangeSet
/*    */   extends HashSet<AtomicChange>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 13 */   public final int[] changecount = new int[AtomicChange.ChangeTypes.values().length];
/*    */   
/*    */   public void print(PrintStream out) {
/* 16 */     if (size() > 0) {
/* 17 */       out.println("~~~Changes~~~");
/* 18 */       for (AtomicChange ac : this) {
/* 19 */         out.println(ac.toString());
/*    */       }
/*    */     } else {
/* 22 */       out.println("No changes");
/*    */     }
/*    */   }
/*    */   
/*    */   public void normalize() {}
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/changetypes/ChangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */