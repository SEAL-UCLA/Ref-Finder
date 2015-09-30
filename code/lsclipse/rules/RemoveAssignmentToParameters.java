/*     */ package lsclipse.rules;
/*     */ 
/*     */ import lsclipse.RefactoringQuery;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RemoveAssignmentToParameters
/*     */   implements Rule
/*     */ {
/*     */   private static final String NEWM_BODY = "?newmBody";
/*     */   private static final String OLDM_BODY = "?oldmBody";
/*     */   private static final String PARAM_LIST = "?paramList";
/*     */   private static final String M_FULL_NAME = "?mFullName";
/*     */   private String name_;
/*     */   
/*     */   public RemoveAssignmentToParameters()
/*     */   {
/*  23 */     this.name_ = "remove_assignment_to_parameters";
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  28 */     return this.name_;
/*     */   }
/*     */   
/*     */   public String getRefactoringString()
/*     */   {
/*  33 */     return getName() + "(" + "?mFullName" + ",?paramName)";
/*     */   }
/*     */   
/*     */   public RefactoringQuery getRefactoringQuery()
/*     */   {
/*  38 */     return new RefactoringQuery(getName(), getQueryString());
/*     */   }
/*     */   
/*     */   private String getQueryString() {
/*  42 */     return "before_parameter(?mFullName, ?paramList, ?),after_parameter(?mFullName, ?paramList, ?),deleted_methodbody(?mFullName, ?oldmBody),added_methodbody(?mFullName, ?newmBody)";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String checkAdherence(ResultSet rs)
/*     */     throws TyrubaException
/*     */   {
/*  51 */     String[] params = rs.getString("?paramList").split(",");
/*  52 */     String oldmBody = rs.getString("?oldmBody");
/*  53 */     String newmBody = rs.getString("?newmBody");
/*     */     
/*     */ 
/*  56 */     if (params[0].length() == 0)
/*  57 */       return null;
/*     */     String[] arrayOfString1;
/*  59 */     int j = (arrayOfString1 = params).length; for (int i = 0; i < j; i++) { String param = arrayOfString1[i];
/*  60 */       String name = param.substring(param.indexOf(':') + 1, 
/*  61 */         param.length());
/*  62 */       int oldAssignments = countNumAssignments(name, oldmBody);
/*  63 */       int newAssignments = countNumAssignments(name, newmBody);
/*  64 */       if (newAssignments < oldAssignments) {
/*  65 */         return 
/*  66 */           getName() + "(\"" + rs.getString("?mFullName") + "\",\"" + param + "\")";
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  71 */     return null;
/*     */   }
/*     */   
/*     */   private int countNumAssignments(String name, String body) {
/*  75 */     int result = 0;
/*  76 */     int nameAssignmentIndex = -1;
/*  77 */     while ((nameAssignmentIndex + 1 < body.length()) && 
/*  78 */       ((nameAssignmentIndex = findAssignmentIndex(name, body, 
/*  79 */       nameAssignmentIndex + 1)) > 0)) {
/*  80 */       result++;
/*     */     }
/*  82 */     return result;
/*     */   }
/*     */   
/*     */   private int findAssignmentIndex(String name, String body, int startindex)
/*     */   {
/*  87 */     int index = body.indexOf(name + "=", startindex);
/*  88 */     if ((index < 0) || (index == body.indexOf(name + "==", startindex))) {
/*  89 */       index = body.indexOf(name + "+=", startindex);
/*     */     }
/*  91 */     if (index < 0) {
/*  92 */       index = body.indexOf(name + "-=", startindex);
/*     */     }
/*  94 */     if (index < 0) {
/*  95 */       index = body.indexOf(name + "*=", startindex);
/*     */     }
/*  97 */     if (index < 0) {
/*  98 */       index = body.indexOf(name + "/=", startindex);
/*     */     }
/* 100 */     if (index < 0) {
/* 101 */       index = body.indexOf(name + "%=", startindex);
/*     */     }
/* 103 */     if (index < 0) {
/* 104 */       index = body.indexOf(name + "&=", startindex);
/*     */     }
/* 106 */     if (index < 0) {
/* 107 */       index = body.indexOf(name + "|=", startindex);
/*     */     }
/* 109 */     if (index < 0) {
/* 110 */       index = body.indexOf(name + "^=", startindex);
/*     */     }
/* 112 */     if (index < 0) {
/* 113 */       index = body.indexOf(name + "<<=", startindex);
/*     */     }
/* 115 */     if (index < 0) {
/* 116 */       index = body.indexOf(name + ">>=", startindex);
/*     */     }
/* 118 */     if (index < 0) {
/* 119 */       index = body.indexOf(name + ">>>=", startindex);
/*     */     }
/* 121 */     return index;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/RemoveAssignmentToParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */