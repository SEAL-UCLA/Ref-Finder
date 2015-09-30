/*     */ package lsd.facts;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lsd.io.LSDTyrubaRuleChecker;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDLiteral;
/*     */ import lsd.rule.LSDRule;
/*     */ import lsd.rule.LSDVariable;
/*     */ import metapackage.MetaInfo;
/*     */ import tyRuBa.parser.ParseException;
/*     */ 
/*     */ public class LSDFactBase
/*     */ {
/*     */   public static final boolean deltaKB = true;
/*     */   public static final boolean twoKB = false;
/*  25 */   private LSDTyrubaRuleChecker ruleChecker = new LSDTyrubaRuleChecker();
/*  26 */   private LinkedHashSet<LSDFact> factsDeltaKB = new LinkedHashSet();
/*  27 */   private LinkedHashSet<LSDFact> facts2KB = new LinkedHashSet();
/*  28 */   private ArrayList<LSDRule> winnowingRules = new ArrayList();
/*  29 */   private boolean winnowed = true;
/*  30 */   private HashSet<LSDFact> matched = new HashSet();
/*  31 */   private HashMap<LSDRule, List<LSDFact>> ruleMatches = new HashMap();
/*     */   
/*  33 */   private HashMap<LSDRule, List<Map<LSDVariable, String>>> ruleExceptions = new HashMap();
/*     */   
/*     */   public LinkedHashSet<LSDFact> get2KBFact() {
/*  36 */     return this.facts2KB;
/*     */   }
/*     */   
/*  39 */   public LinkedHashSet<LSDFact> getDeltaKBFact() { return this.factsDeltaKB; }
/*     */   
/*     */   public List<LSDFact> getRemainingFacts(boolean deltaKB) {
/*  42 */     if (!this.winnowed)
/*  43 */       winnow();
/*  44 */     ArrayList<LSDFact> remainingFacts = new ArrayList();
/*  45 */     for (LSDFact f : deltaKB ? this.factsDeltaKB : this.facts2KB) {
/*  46 */       if (!this.matched.contains(f))
/*  47 */         remainingFacts.add(f);
/*     */     }
/*  49 */     return remainingFacts;
/*     */   }
/*     */   
/*     */   public List<LSDFact> getRelevantFacts(LSDRule rule) {
/*  53 */     if (!this.winnowed)
/*  54 */       winnow();
/*  55 */     if (!this.ruleMatches.containsKey(rule)) {
/*  56 */       System.err.println("The requested rule (" + rule + 
/*  57 */         ") is not in the list.");
/*  58 */       return null;
/*     */     }
/*  60 */     ArrayList<LSDFact> relevantFacts = new ArrayList();
/*  61 */     for (LSDFact f : (List)this.ruleMatches.get(rule)) {
/*  62 */       if ((this.factsDeltaKB.contains(f)) || (this.facts2KB.contains(f)))
/*  63 */         relevantFacts.add(f);
/*     */     }
/*  65 */     return relevantFacts;
/*     */   }
/*     */   
/*     */   public List<Map<LSDVariable, String>> getExceptions(LSDRule rule) {
/*  69 */     if (!this.winnowed)
/*  70 */       winnow();
/*  71 */     if (!this.ruleExceptions.containsKey(rule)) {
/*  72 */       System.err.println("The requested rule (" + rule + 
/*  73 */         ") is not in the list.");
/*  74 */       return null;
/*     */     }
/*  76 */     return (List)this.ruleExceptions.get(rule);
/*     */   }
/*     */   
/*     */   public void loadDeltaKBFactBase(ArrayList<LSDFact> facts) throws ParseException, tyRuBa.modes.TypeModeError, IOException
/*     */   {
/*  81 */     this.ruleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  82 */     for (LSDFact fact : facts) {
/*  83 */       this.ruleChecker.loadFact(fact);
/*  84 */       this.factsDeltaKB.add(fact);
/*     */     }
/*  86 */     resetWinnowing();
/*     */   }
/*     */   
/*     */   public void loadFilteredDeltaFactBase(ArrayList<LSDFact> facts, ArrayList<String> typeNames) throws Exception
/*     */   {
/*  91 */     this.ruleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  92 */     String line = null;
/*  93 */     Iterator localIterator2; for (Iterator localIterator1 = facts.iterator(); localIterator1.hasNext(); 
/*     */         
/*  95 */         localIterator2.hasNext())
/*     */     {
/*  93 */       LSDFact fact = (LSDFact)localIterator1.next();
/*  94 */       line = fact.toString();
/*  95 */       localIterator2 = typeNames.iterator(); continue;String str = (String)localIterator2.next();
/*  96 */       if (line.contains(str))
/*     */       {
/*  98 */         this.ruleChecker.loadFact(fact);
/*  99 */         this.factsDeltaKB.add(fact);
/*     */       }
/*     */     }
/*     */     
/* 103 */     resetWinnowing();
/*     */   }
/*     */   
/*     */   public void loadFiltered2KBFactBase(ArrayList<LSDFact> facts, ArrayList<String> typeNames) throws Exception {
/* 107 */     this.ruleChecker.loadAdditionalDB(MetaInfo.included2kb);
/* 108 */     String line = null;
/* 109 */     Iterator localIterator2; for (Iterator localIterator1 = facts.iterator(); localIterator1.hasNext(); 
/*     */         
/* 111 */         localIterator2.hasNext())
/*     */     {
/* 109 */       LSDFact fact = (LSDFact)localIterator1.next();
/* 110 */       line = fact.toString();
/* 111 */       localIterator2 = typeNames.iterator(); continue;String str = (String)localIterator2.next();
/* 112 */       if (line.contains(str))
/*     */       {
/* 114 */         this.ruleChecker.loadFact(fact);
/* 115 */         this.facts2KB.add(fact);
/*     */       }
/*     */     }
/*     */     
/* 119 */     resetWinnowing();
/*     */   }
/*     */   
/*     */   public void load2KBFactBase(ArrayList<LSDFact> facts) throws ParseException, tyRuBa.modes.TypeModeError, IOException {
/* 123 */     this.ruleChecker.loadAdditionalDB(MetaInfo.included2kb);
/* 124 */     for (LSDFact fact : facts) {
/* 125 */       this.ruleChecker.loadFact(fact);
/* 126 */       this.facts2KB.add(fact);
/*     */     }
/* 128 */     resetWinnowing();
/*     */   }
/*     */   
/*     */   public void loadWinnowingRules(Collection<LSDRule> rules)
/*     */   {
/* 133 */     this.winnowingRules.addAll(rules);
/* 134 */     resetWinnowing();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void winnow()
/*     */   {
/* 141 */     this.ruleMatches = new HashMap();
/* 142 */     for (LSDRule rule : this.winnowingRules) {
/* 143 */       ArrayList<LSDFact> thisRuleMatches = new ArrayList();
/* 144 */       List<Map<LSDVariable, String>> counterExamples = this.ruleChecker.getCounterExamples(rule);
/* 145 */       this.ruleExceptions.put(rule, counterExamples);
/* 146 */       ArrayList<LSDRule> resultingConclusions = this.ruleChecker.getTrueConclusions(rule);
/* 147 */       Iterator localIterator3; for (Iterator localIterator2 = resultingConclusions.iterator(); localIterator2.hasNext(); 
/* 148 */           localIterator3.hasNext())
/*     */       {
/* 147 */         LSDRule matchedRule = (LSDRule)localIterator2.next();
/* 148 */         localIterator3 = matchedRule.getLiterals().iterator(); continue;LSDLiteral generatedLiteral = (LSDLiteral)localIterator3.next();
/* 149 */         if (!(generatedLiteral instanceof LSDFact))
/*     */         {
/* 151 */           System.out.println("Not a fact:" + generatedLiteral);
/*     */         }
/*     */         else {
/* 154 */           LSDFact fact = ((LSDFact)generatedLiteral)
/* 155 */             .nonNegatedCopy();
/* 156 */           if (this.factsDeltaKB.contains(fact)) {
/* 157 */             thisRuleMatches.add(fact);
/* 158 */             this.matched.add(fact);
/*     */           }
/* 160 */           if (this.facts2KB.contains(fact)) {
/* 161 */             thisRuleMatches.add(fact);
/* 162 */             this.matched.add(fact);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 174 */       this.ruleMatches.put(rule, thisRuleMatches);
/*     */     }
/* 176 */     this.winnowed = true;
/*     */   }
/*     */   
/*     */   void forceWinnowing() {
/* 180 */     winnow();
/*     */   }
/*     */   
/*     */   private void resetWinnowing() {
/* 184 */     this.winnowed = false;
/*     */   }
/*     */   
/* 187 */   public int num2KBFactSize() { return this.facts2KB.size(); }
/*     */   
/*     */   public int numDeltaKBFactSize() {
/* 190 */     return this.factsDeltaKB.size();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/facts/LSDFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */