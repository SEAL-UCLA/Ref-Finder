/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.util.ElementSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerformanceTest
/*     */ {
/*     */   private boolean isScenario;
/*     */   private String[] queries;
/*     */   FrontEnd frontend;
/*     */   Test[] tests;
/*     */   
/*     */   public PerformanceTest(FrontEnd frontend, String[] queries, boolean isScenario)
/*     */     throws ParseException, IOException, TypeModeError
/*     */   {
/*  28 */     this.frontend = frontend;
/*  29 */     this.queries = queries;
/*  30 */     this.isScenario = isScenario;
/*  31 */     run();
/*     */   }
/*     */   
/*     */   private void run() {
/*  35 */     if (this.isScenario) {
/*  36 */       runScenario();
/*     */     } else {
/*  38 */       runOneByOne();
/*     */     }
/*     */   }
/*     */   
/*     */   public static PerformanceTest make(FrontEnd frontend, String queryfile) throws ParseException, IOException, TypeModeError {
/*  43 */     ArrayList queries = new ArrayList();
/*  44 */     BufferedReader qf = new BufferedReader(new FileReader(queryfile));
/*     */     
/*  46 */     boolean isScenario = false;
/*  47 */     String query; while ((query = qf.readLine()) != null) { String query;
/*  48 */       if (!query.startsWith("//")) {
/*  49 */         queries.add(query);
/*  50 */       } else if (query.startsWith("//SCENARIO")) {
/*  51 */         isScenario = true;
/*     */       }
/*     */     }
/*  54 */     return new PerformanceTest(frontend, (String[])queries.toArray(new String[queries.size()]), isScenario);
/*     */   }
/*     */   
/*     */   public class Test
/*     */   {
/*     */     String query;
/*  60 */     long runtime = 0L;
/*  61 */     long numresults = 0L;
/*  62 */     private Throwable error = null;
/*     */     
/*     */     public Test(String query) {
/*  65 */       this.query = query;
/*     */     }
/*     */     
/*     */     void run() throws ParseException, TypeModeError {
/*  69 */       ElementSource result = PerformanceTest.this.frontend.frameQuery(this.query);
/*  70 */       while (result.hasMoreElements()) {
/*  71 */         this.numresults += 1L;
/*  72 */         result.nextElement();
/*     */       }
/*     */     }
/*     */     
/*     */     void timedRun() {
/*  77 */       timedScenarioStepRun(System.currentTimeMillis());
/*     */     }
/*     */     
/*     */     public long timedScenarioStepRun(long startTime) {
/*  81 */       this.numresults = 0L;
/*     */       try {
/*  83 */         run();
/*     */       }
/*     */       catch (Throwable e) {
/*  86 */         this.error = e;
/*     */       }
/*  88 */       long endtime = System.currentTimeMillis();
/*  89 */       this.runtime = (endtime - startTime);
/*  90 */       return endtime;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  94 */       if (this.error != null) {
/*  95 */         return this.query + "#CRASHED: " + this.error.getMessage();
/*     */       }
/*  97 */       return 
/*  98 */         this.query + "  #results = " + this.numresults + "  seconds = " + this.runtime / 1000.0D;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void runOneByOne()
/*     */   {
/* 112 */     this.tests = new Test[this.queries.length];
/* 113 */     for (int i = 0; i < this.queries.length; i++) {
/* 114 */       this.tests[i] = new Test(this.queries[i]);
/* 115 */       System.gc();
/*     */       
/* 117 */       this.tests[i].timedRun();
/* 118 */       System.err.println(this.tests[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void runScenario()
/*     */   {
/* 129 */     this.tests = new Test[this.queries.length];
/* 130 */     for (int i = 0; i < this.queries.length; i++) {
/* 131 */       this.tests[i] = new Test(this.queries[i]);
/*     */     }
/* 133 */     System.gc();
/*     */     
/* 135 */     long startTime = System.currentTimeMillis();
/* 136 */     for (int i = 0; i < this.queries.length; i++) {
/* 137 */       startTime = this.tests[i].timedScenarioStepRun(startTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 142 */     StringBuffer out = new StringBuffer();
/* 143 */     for (int i = 0; i < this.tests.length; i++) {
/* 144 */       out.append(this.tests[i] + "\n");
/*     */     }
/* 146 */     out.append("TOTAL : " + totalTime() + "\n");
/* 147 */     return out.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double totalTime()
/*     */   {
/* 190 */     long total = 0L;
/* 191 */     for (int i = 0; i < this.tests.length; i++) {
/* 192 */       total += this.tests[i].runtime;
/*     */     }
/* 194 */     return total / 1000.0D;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/PerformanceTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */