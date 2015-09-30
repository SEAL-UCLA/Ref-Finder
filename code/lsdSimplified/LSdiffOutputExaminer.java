/*    */ package lsdSimplified;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.LinkedHashSet;
/*    */ import lsd.facts.LSDFactBase;
/*    */ import lsd.io.LSDAlchemyRuleReader;
/*    */ import lsd.io.LSDTyrubaFactReader;
/*    */ import lsd.rule.LSDFact;
/*    */ import lsd.rule.LSDPredicate;
/*    */ import lsd.rule.LSDRule;
/*    */ 
/*    */ 
/*    */ public class LSdiffOutputExaminer
/*    */ {
/* 18 */   LSDFactBase localFB = new LSDFactBase();
/* 19 */   ArrayList<LSDRule> lsdiffRules = new ArrayList();
/* 20 */   File winnowingRulesFile = new File("input/winnowingRules.rub");
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 24 */     String oldVersion = "0.9.10";
/* 25 */     String newVersion = "0.9.11";
/* 26 */     File twoKBFile = new File(
/* 27 */       "/Volumes/gorillaHD2/LSdiff/Tyruba/lsd/jfreechart/" + oldVersion + "_" + newVersion + "2KB.rub");
/* 28 */     File deltaKBFile = new File("/Volumes/gorillaHD2/LSdiff/Tyruba/lsd/jfreechart/" + oldVersion + "_" + newVersion + "delta.rub");
/*    */     
/* 30 */     LSdiffOutputExaminer lsdiffExaminer = new LSdiffOutputExaminer(twoKBFile, deltaKBFile, null);
/* 31 */     lsdiffExaminer.compute_LSdiff_FACTTYPE();
/* 32 */     lsdiffExaminer.print(System.out);
/*    */   }
/*    */   
/*    */   public LSdiffOutputExaminer(File twoKBFile, File deltaKBFile, File lsdiffRuleFile)
/*    */   {
/*    */     try {
/* 38 */       if (twoKBFile != null) {
/* 39 */         ArrayList<LSDFact> twoKB = new LSDTyrubaFactReader(twoKBFile).getFacts();
/* 40 */         this.localFB.load2KBFactBase(twoKB);
/*    */       }
/* 42 */       if (deltaKBFile != null) {
/* 43 */         ArrayList<LSDFact> deltaKB = new LSDTyrubaFactReader(deltaKBFile).getFacts();
/* 44 */         this.localFB.loadDeltaKBFactBase(deltaKB);
/*    */       }
/*    */     } catch (Exception e) {
/* 47 */       e.printStackTrace();
/*    */     }
/*    */     
/* 50 */     ArrayList<LSDRule> winnowingRules = new LSDAlchemyRuleReader(
/* 51 */       this.winnowingRulesFile).getRules();
/* 52 */     if ((twoKBFile != null) && (deltaKBFile != null)) this.localFB.loadWinnowingRules(winnowingRules);
/* 53 */     this.localFB.getRemainingFacts(true);
/* 54 */     if (lsdiffRuleFile != null) {
/* 55 */       this.lsdiffRules = new LSDAlchemyRuleReader(lsdiffRuleFile).getRules();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 61 */   private HashMap<String, ArrayList<LSDFact>> predicateToFacts = new HashMap();
/*    */   
/*    */   public void compute_LSdiff_FACTTYPE() {
/* 64 */     LinkedHashSet<LSDFact> deltaKBFacts = this.localFB.getDeltaKBFact();
/* 65 */     for (LSDFact fact : deltaKBFacts) {
/* 66 */       LSDPredicate predicate = fact.getPredicate();
/* 67 */       String predicateType = predicate.getName();
/*    */       
/* 69 */       ArrayList<LSDFact> facts = (ArrayList)this.predicateToFacts.get(predicateType);
/* 70 */       if (facts == null) {
/* 71 */         facts = new ArrayList();
/* 72 */         this.predicateToFacts.put(predicateType, facts);
/*    */       }
/* 74 */       facts.add(fact);
/*    */     }
/*    */   }
/*    */   
/* 78 */   public void print(PrintStream p) { p.println("2KB Size:\t" + this.localFB.num2KBFactSize());
/* 79 */     p.println("DeltaKB Size:\t" + this.localFB.numDeltaKBFactSize());
/* 80 */     p.println("Categorization of Delta KB Facts");
/*    */     
/* 82 */     for (String predicateType : this.predicateToFacts.keySet()) {
/* 83 */       ArrayList<LSDFact> facts = (ArrayList)this.predicateToFacts.get(predicateType);
/* 84 */       p.println(predicateType);
/* 85 */       p.println("# Facts:\t" + facts.size());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsdSimplified/LSdiffOutputExaminer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */