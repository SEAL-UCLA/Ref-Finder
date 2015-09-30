/*     */ package lsclipse.rules;
/*     */ 
/*     */ import lsclipse.RefactoringQuery;
/*     */ import lsclipse.utils.CodeCompare;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecomposeConditional
/*     */   implements Rule
/*     */ {
/*  17 */   private ExtractMethod conditionExtractMethod = null;
/*  18 */   private ExtractMethod ifBlockExtractMethod = null;
/*  19 */   private ExtractMethod elseBlockExtractMethod = null;
/*     */   
/*     */   private String name_;
/*     */   
/*     */   public static final String ELSE_BLOCK_FULL_NAME = "?m3FullName";
/*     */   public static final String IF_BLOCK_FULL_NAME = "?m2FullName";
/*     */   public static final String CONDITION_M_FULL_NAME = "?m1FullName";
/*     */   public static final String ELSE_BLOCK_2 = "?elseBlockB";
/*     */   public static final String IF_BLOCK_2 = "?ifBlockB";
/*     */   public static final String CONDITION_BLOCK_2 = "?conditionB";
/*     */   public static final String M_FULL_NAME = "?mFullName";
/*     */   public static final String ELSE_BLOCK = "?elseBlock";
/*     */   public static final String IF_BLOCK = "?ifBlock";
/*     */   public static final String CONDITION_BLOCK = "?condition";
/*     */   
/*     */   public DecomposeConditional()
/*     */   {
/*  36 */     this.name_ = "decompose_conditional";
/*     */     
/*  38 */     this.conditionExtractMethod = new ExtractMethod("?mFullName", 
/*  39 */       "?m1FullName", "?conditionB", "?t1FullName");
/*  40 */     this.ifBlockExtractMethod = new ExtractMethod("?mFullName", 
/*  41 */       "?m2FullName", "?ifBlockB", "?t2FullName");
/*  42 */     this.elseBlockExtractMethod = new ExtractMethod("?mFullName", 
/*  43 */       "?m3FullName", "?elseBlockB", "?t3FullName");
/*     */   }
/*     */   
/*     */   private String getQueryString()
/*     */   {
/*  48 */     return 
/*     */     
/*     */ 
/*     */ 
/*  52 */       "deleted_conditional(?condition, ?ifBlock, ?elseBlock, ?mFullName), " + this.conditionExtractMethod.getRefactoringString() + ", " + this.ifBlockExtractMethod.getRefactoringString() + ", " + this.elseBlockExtractMethod.getRefactoringString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RefactoringQuery getRefactoringQuery()
/*     */   {
/*  62 */     RefactoringQuery decompose_conditional = new RefactoringQuery(
/*  63 */       getName(), getQueryString());
/*  64 */     return decompose_conditional;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String checkAdherence(ResultSet rs)
/*     */     throws TyrubaException
/*     */   {
/*  75 */     String m1FullName = rs.getString("?m1FullName");
/*  76 */     String m2FullName = rs.getString("?m2FullName");
/*  77 */     String m3FullName = rs.getString("?m3FullName");
/*     */     
/*  79 */     String ifBlock = rs.getString("?ifBlock");
/*  80 */     String ifBlockB = rs.getString("?ifBlockB");
/*  81 */     String elseBlock = rs.getString("?elseBlock");
/*  82 */     String elseBlockB = rs.getString("?elseBlockB");
/*  83 */     String condition = rs.getString("?condition");
/*  84 */     String conditionB = rs.getString("?conditionB");
/*  85 */     if ((!m1FullName.equals(m2FullName)) && (!m1FullName.equals(m3FullName)) && 
/*  86 */       (!m2FullName.equals(m3FullName)) && 
/*  87 */       (CodeCompare.compare(ifBlock, ifBlockB)) && 
/*  88 */       (CodeCompare.compare(elseBlock, elseBlockB)) && 
/*  89 */       (CodeCompare.compare(condition, conditionB))) {
/*  90 */       return 
/*     */       
/*  92 */         getName() + "(\"" + condition + "\",\"" + ifBlock + "\",\"" + elseBlock + "\",\"" + rs.getString("?mFullName") + "\")";
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  99 */     return this.name_;
/*     */   }
/*     */   
/*     */   public String getRefactoringString()
/*     */   {
/* 104 */     return 
/* 105 */       getName() + "(" + "?condition" + "," + "?ifBlock" + "," + "?elseBlock" + "," + "?mFullName" + ")";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/DecomposeConditional.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */