/*     */ package lsdSimplified;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import lsd.facts.LSDFactBase;
/*     */ import lsd.rule.LSDBinding;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDLiteral;
/*     */ import lsd.rule.LSDRule;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ 
/*     */ public class FactClassifier
/*     */   implements Iterator<ArrayList<String>>
/*     */ {
/*     */   private ArrayList<LSDFact> t_twoKbFacts;
/*     */   private ArrayList<LSDFact> t_deltaKbFacts;
/*     */   private List<LSDRule> typeLevelRules;
/*     */   private List<LSDRule> winnowingRules;
/*  22 */   private ArrayList<ClusterInfo> clusters = new ArrayList();
/*     */   private Iterator<ClusterInfo> clusterIterator;
/*  24 */   private ClusterInfo currentCluster = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FactClassifier(ArrayList<LSDFact> t2kb, ArrayList<LSDFact> tdelta, List<LSDRule> twinnoing, List<LSDRule> rules)
/*     */     throws ParseException, TypeModeError, IOException
/*     */   {
/*  35 */     this.t_deltaKbFacts = tdelta;
/*  36 */     this.t_twoKbFacts = t2kb;
/*  37 */     this.typeLevelRules = rules;
/*  38 */     this.winnowingRules = twinnoing;
/*  39 */     this.currentCluster = null;
/*  40 */     classifier();
/*  41 */     this.clusterIterator = this.clusters.iterator();
/*     */   }
/*     */   
/*     */   private void classifier()
/*     */     throws ParseException, TypeModeError, IOException
/*     */   {
/*  47 */     LSDFactBase fb = new LSDFactBase();
/*  48 */     fb.load2KBFactBase(this.t_twoKbFacts);
/*  49 */     fb.loadDeltaKBFactBase(this.t_deltaKbFacts);
/*  50 */     fb.loadWinnowingRules(this.winnowingRules);
/*  51 */     fb.loadWinnowingRules(this.typeLevelRules);
/*  52 */     List<LSDFact> remainingFacts = fb.getRemainingFacts(true);
/*  53 */     for (LSDRule rule : this.typeLevelRules) {
/*  54 */       ArrayList<String> cluster = new ArrayList();
/*  55 */       List<LSDFact> facts = fb.getRelevantFacts(rule);
/*     */       
/*  57 */       facts.addAll(remainingFacts);
/*  58 */       Iterator localIterator3; for (Iterator localIterator2 = facts.iterator(); localIterator2.hasNext(); 
/*     */           
/*  60 */           localIterator3.hasNext())
/*     */       {
/*  58 */         LSDFact fact = (LSDFact)localIterator2.next();
/*  59 */         List<LSDBinding> bindings = fact.getBindings();
/*  60 */         localIterator3 = bindings.iterator(); continue;LSDBinding binding = (LSDBinding)localIterator3.next();
/*  61 */         if (!cluster.contains(binding.getGroundConst())) {
/*  62 */           cluster.add(binding.getGroundConst());
/*     */         }
/*     */       }
/*  65 */       for (localIterator2 = rule.getLiterals().iterator(); localIterator2.hasNext(); 
/*     */           
/*  67 */           localIterator3.hasNext())
/*     */       {
/*  65 */         LSDLiteral literal = (LSDLiteral)localIterator2.next();
/*  66 */         List<LSDBinding> bindings = literal.getBindings();
/*  67 */         localIterator3 = bindings.iterator(); continue;LSDBinding binding = (LSDBinding)localIterator3.next();
/*  68 */         if ((!cluster.contains(binding.getGroundConst())) && (binding.getGroundConst() != null)) {
/*  69 */           cluster.add(binding.getGroundConst());
/*     */         }
/*     */       }
/*  72 */       ClusterInfo info = new ClusterInfo(null);
/*  73 */       info.rule = rule;
/*  74 */       info.cluster = cluster;
/*  75 */       this.clusters.add(info);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasNext()
/*     */   {
/*  81 */     return this.clusterIterator.hasNext();
/*     */   }
/*     */   
/*     */   public ArrayList<String> next()
/*     */   {
/*  86 */     this.currentCluster = ((ClusterInfo)this.clusterIterator.next());
/*  87 */     return this.currentCluster.cluster;
/*     */   }
/*     */   
/*     */   public LSDRule getRule() {
/*  91 */     return this.currentCluster.rule;
/*     */   }
/*     */   
/*     */   public void remove()
/*     */   {
/*  96 */     this.clusterIterator.remove();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 101 */     return this.clusters.size();
/*     */   }
/*     */   
/*     */   private class ClusterInfo
/*     */   {
/*     */     LSDRule rule;
/*     */     ArrayList<String> cluster;
/*     */     
/*     */     private ClusterInfo() {}
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsdSimplified/FactClassifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */