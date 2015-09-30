/*    */ package metapackage;
/*    */ 
/*    */ import java.io.File;
/*    */ import lsclipse.LSclipse;
/*    */ 
/*    */ public class MetaInfo
/*    */ {
/*    */   public static final int k = 1;
/*    */   public static final int minConcFact = 3;
/*    */   public static final int beamSize = 100;
/*    */   public static final double accuracy = 0.75D;
/*    */   public static final int maxException = 10;
/* 13 */   public static final String baseDir = LSclipse.getDefault().getStateLocation().toOSString();
/*    */   
/*    */ 
/*    */ 
/* 17 */   public static final File srcDir = new File(baseDir, "input");
/* 18 */   public static final File resDir = new File(baseDir, "output");
/* 19 */   public static final File fdbDir = new File(baseDir, "fdb");
/* 20 */   public static final File lsclipseDir = new File(srcDir, "lsclipse");
/*    */   
/* 22 */   public static final File included2kb = new File(srcDir, "2KB_lsdPred.rub");
/* 23 */   public static final File includedDelta = new File(srcDir, "deltaKB_lsdPred.rub");
/*    */   
/*    */ 
/* 26 */   public static final String winnowings = new File(srcDir, "winnowingRules.rub").getAbsolutePath();
/* 27 */   public static final String modifiedWinnowings = new File(srcDir, "convertedwinnowingRules.rub").getAbsolutePath();
/*    */   
/* 29 */   public static final String lsclipse2KB = new File(lsclipseDir, "2KB_lsclipsePred.rub").getAbsolutePath();
/* 30 */   public static final String lsclipseDelta = new File(lsclipseDir, "deltaKB_lsclipsePred.rub").getAbsolutePath();
/*    */   
/* 32 */   public static final String lsclipseRefactorPred = new File(lsclipseDir, "preds1.rub").getAbsolutePath();
/* 33 */   public static final String lsclipseRefactorDeltaPrimed = new File(lsclipseDir, "deltaKB_primed_lsdPred.rub").getAbsolutePath();
/*    */   
/* 35 */   public static final String resultsFile = new File(resDir, "Hierarchical_lsclipse_Temp.rub").getAbsolutePath();
/* 36 */   public static final String ongoingResultsFile = new File(resDir, "exmp.rub").getAbsolutePath();
/* 37 */   public static final String refsOnlyFile = new File(resDir, "output.rub").getAbsolutePath();
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/metapackage/MetaInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */