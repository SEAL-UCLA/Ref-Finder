/*      */ package lsd.facts;
/*      */ 
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeSet;
/*      */ import lsd.io.LSDAlchemyRuleReader;
/*      */ import lsd.io.LSDTyrubaFactReader;
/*      */ import lsd.io.LSDTyrubaRuleChecker;
/*      */ import lsd.rule.LSDBinding;
/*      */ import lsd.rule.LSDFact;
/*      */ import lsd.rule.LSDInvalidTypeException;
/*      */ import lsd.rule.LSDLiteral;
/*      */ import lsd.rule.LSDPredicate;
/*      */ import lsd.rule.LSDRule;
/*      */ import lsd.rule.LSDRule.LSDRuleComparator;
/*      */ import lsd.rule.LSDVariable;
/*      */ import metapackage.MetaInfo;
/*      */ import tyRuBa.modes.TypeModeError;
/*      */ import tyRuBa.parser.ParseException;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class LSDRuleEnumerator
/*      */ {
/*      */   private LSDTyrubaRuleChecker ruleChecker;
/*      */   private LSDTyrubaRuleChecker remainingRuleChecker;
/*   43 */   private int minMatches = 1;
/*      */   
/*   45 */   private int minMatchesPerLiteral = 0;
/*      */   
/*   47 */   private int maxExceptions = 10;
/*      */   
/*   49 */   private double minAccuracy = 0.0D;
/*      */   
/*   51 */   private int beamSize = 100;
/*      */   
/*   53 */   private ArrayList<LSDFact> read2kbFacts = new ArrayList();
/*      */   
/*   55 */   private ArrayList<LSDFact> readDeltaFacts = new ArrayList();
/*      */   
/*   57 */   private ArrayList<LSDRule> winnowingRules = new ArrayList();
/*      */   
/*   59 */   private ArrayList<LSDFact> workingSet2KB = new ArrayList();
/*      */   
/*   61 */   private ArrayList<LSDFact> workingSetDeltaKB = new ArrayList();
/*      */   
/*      */   public static ArrayList<LSDPredicate> getUniquePredicates(Collection<LSDFact> facts, boolean antedecedent)
/*      */   {
/*   65 */     TreeSet<String> predNames = new TreeSet();
/*   66 */     LSDPredicate p; for (LSDFact f : facts) {
/*   67 */       p = f.getPredicate();
/*   68 */       predNames.add(p.getName());
/*      */     }
/*   70 */     ArrayList<LSDPredicate> preds = new ArrayList();
/*      */     
/*   72 */     for (String s : predNames) {
/*   73 */       LSDPredicate p = LSDPredicate.getPredicate(s);
/*   74 */       if ((antedecedent) && (p.isAntecedentPredicate())) {
/*   75 */         preds.add(p);
/*      */       } else {
/*   77 */         preds.add(p);
/*      */       }
/*      */     }
/*      */     
/*   81 */     return preds; }
/*      */   
/*   83 */   private ArrayList<LSDRule> modifiedWinnowingRules = new ArrayList();
/*      */   
/*      */ 
/*      */ 
/*      */   final LSdiffDistanceFactBase onDemand2KB;
/*      */   
/*      */ 
/*      */   final LSdiffHierarchialDeltaKB onDemandDeltaKB;
/*      */   
/*      */ 
/*   93 */   public int statsGeneratedPartials = 0;
/*      */   
/*   95 */   public int statsEnqueuedPartials = 0;
/*      */   
/*   97 */   public int statsSavedPartials = 0;
/*      */   
/*   99 */   int statsGeneratedGroundings = 0;
/*      */   
/*  101 */   int statsEnqueuedGroundings = 0;
/*      */   
/*  103 */   int statsSavedGroundings = 0;
/*      */   
/*  105 */   int statsPartialValidQueryCount = 0;
/*      */   
/*  107 */   int statsGroundingConstantsQueryCount = 0;
/*      */   
/*  109 */   int statsGroundingValidQueryCount = 0;
/*      */   
/*  111 */   int statsGroundingExceptionsQueryCount = 0;
/*      */   
/*      */   double timeUngroundRuleGeneration;
/*      */   
/*      */   double timePartiallyGroundRuleGeneration;
/*      */   
/*      */   int numValidRules;
/*      */   
/*      */   int numRulesWithException;
/*      */   
/*      */   int num2KBSize;
/*      */   
/*      */   int numDeltaKBSize;
/*      */   
/*      */   int numWinnowDeltaKBSize;
/*      */   
/*      */   int numRemainingDeltaKBSize;
/*      */   
/*      */   int numFinalRules;
/*      */   
/*  131 */   private long enumerationTimestamp = 0L;
/*      */   
/*      */ 
/*      */   private LSDFactBase fb;
/*      */   
/*      */ 
/*      */   static int varNum;
/*      */   
/*      */   public BufferedWriter output;
/*      */   
/*      */   private String resString;
/*      */   
/*      */   private int antecedantSize;
/*      */   
/*      */   private static final boolean isConclusion = true;
/*      */   
/*      */   private static final boolean isAntecedent = false;
/*      */   
/*  149 */   long timer = 0L; long lastStart = 0L;
/*      */   
/*      */   static
/*      */   {
/*  137 */     varNum = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  152 */     tyRuBa.engine.RuleBase.silent = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public LSDRuleEnumerator(File twoKBFile, File deltaKBFile, File winnowingRulesFile, File resultsFile, int minConcFact, double accuracy, int k, int beamSize2, int maxException, File modifiedWinnowingRulesFile, BufferedWriter output)
/*      */     throws Exception
/*      */   {
/*  160 */     setMinMatchesPerLiteral(0);
/*  161 */     setMaxExceptions(maxException);
/*  162 */     setBeamSize(this.beamSize);
/*  163 */     setMinMatches(minConcFact);
/*  164 */     setMinAccuracy(accuracy);
/*  165 */     setAntecedentSize(k);
/*  166 */     this.output = output;
/*  167 */     this.fb = new LSDFactBase();
/*      */     
/*  169 */     startTimer();
/*  170 */     this.read2kbFacts = new LSDTyrubaFactReader(twoKBFile).getFacts();
/*  171 */     this.readDeltaFacts = new LSDTyrubaFactReader(deltaKBFile).getFacts();
/*  172 */     this.winnowingRules = new LSDAlchemyRuleReader(winnowingRulesFile)
/*  173 */       .getRules();
/*      */     
/*      */ 
/*  176 */     this.onDemand2KB = new LSdiffDistanceFactBase(this.read2kbFacts, this.readDeltaFacts);
/*  177 */     this.onDemandDeltaKB = new LSdiffHierarchialDeltaKB(this.readDeltaFacts);
/*      */     
/*      */ 
/*  180 */     this.modifiedWinnowingRules = new LSDAlchemyRuleReader(new File(
/*  181 */       MetaInfo.modifiedWinnowings)).getRules();
/*  182 */     stopTimer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public LSDRuleEnumerator(ArrayList<LSDFact> input2kbFacts, ArrayList<LSDFact> inputDeltaFacts, int minConcFact, double accuracy, int k, int beamSize2, int maxException, BufferedWriter output)
/*      */     throws Exception
/*      */   {
/*  190 */     setMinMatchesPerLiteral(0);
/*  191 */     setMaxExceptions(maxException);
/*  192 */     setBeamSize(this.beamSize);
/*  193 */     setMinMatches(minConcFact);
/*  194 */     setMinAccuracy(accuracy);
/*  195 */     setAntecedentSize(k);
/*  196 */     this.output = output;
/*  197 */     this.fb = new LSDFactBase();
/*      */     
/*  199 */     startTimer();
/*  200 */     this.read2kbFacts = input2kbFacts;
/*  201 */     this.readDeltaFacts = inputDeltaFacts;
/*      */     
/*      */ 
/*      */ 
/*  205 */     this.onDemand2KB = new LSdiffDistanceFactBase(this.read2kbFacts, this.readDeltaFacts);
/*  206 */     this.onDemandDeltaKB = new LSdiffHierarchialDeltaKB(this.readDeltaFacts);
/*      */     
/*      */ 
/*  209 */     this.modifiedWinnowingRules = new LSDAlchemyRuleReader(new File(
/*  210 */       MetaInfo.modifiedWinnowings)).getRules();
/*  211 */     stopTimer();
/*      */   }
/*      */   
/*      */   public void setAntecedentSize(int k) {
/*  215 */     this.antecedantSize = k;
/*      */   }
/*      */   
/*      */   public void setMinMatches(int minMatches) {
/*  219 */     this.minMatches = minMatches;
/*      */   }
/*      */   
/*      */   public void setMinMatchesPerLiteral(int minMatchesPerLiteral) {
/*  223 */     this.minMatchesPerLiteral = minMatchesPerLiteral;
/*      */   }
/*      */   
/*      */   public void setMaxExceptions(int maxExceptions) {
/*  227 */     this.maxExceptions = maxExceptions;
/*      */   }
/*      */   
/*      */   public void setMinAccuracy(double minAccuracy) {
/*  231 */     this.minAccuracy = minAccuracy;
/*      */   }
/*      */   
/*      */   public void setBeamSize(int beamSize) {
/*  235 */     this.beamSize = beamSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void loadFactBases(int hopDistance2KB, LSdiffFilter filter)
/*      */     throws Exception
/*      */   {
/*  243 */     this.onDemand2KB.expand(hopDistance2KB);
/*  244 */     this.workingSet2KB = this.onDemand2KB.getWorking2KBFacts();
/*      */     
/*  246 */     TreeSet<LSDFact> workingDelta = new TreeSet();
/*  247 */     this.onDemandDeltaKB.filterFacts(null, workingDelta, filter);
/*  248 */     this.workingSetDeltaKB = new ArrayList(workingDelta);
/*      */     
/*  250 */     this.fb = new LSDFactBase();
/*      */     
/*  252 */     this.fb.load2KBFactBase(this.workingSet2KB);
/*  253 */     this.fb.loadDeltaKBFactBase(this.workingSetDeltaKB);
/*  254 */     this.fb.loadWinnowingRules(this.modifiedWinnowingRules);
/*      */     
/*  256 */     List<LSDFact> afterWinnowing = this.fb.getRemainingFacts(true);
/*      */     
/*  258 */     this.num2KBSize = this.fb.num2KBFactSize();
/*  259 */     this.numDeltaKBSize = this.fb.numDeltaKBFactSize();
/*  260 */     this.numWinnowDeltaKBSize = afterWinnowing.size();
/*      */     
/*  262 */     this.ruleChecker = createRuleChecker();
/*  263 */     this.remainingRuleChecker = createReducedRuleChecker(new ArrayList());
/*      */     
/*  265 */     System.out.println("Number of 2kbFacts: " + this.num2KBSize);
/*  266 */     System.out.println("Number of deltaFacts: " + this.numDeltaKBSize);
/*      */   }
/*      */   
/*      */   private void swapFactBase(TreeSet<LSDFact> delta) throws Exception {
/*  270 */     LSDTyrubaRuleChecker newRuleChecker = new LSDTyrubaRuleChecker();
/*  271 */     ArrayList<LSDFact> twoKB = this.workingSet2KB;
/*  272 */     ArrayList<LSDFact> deltaKB = new ArrayList(delta);
/*  273 */     this.workingSetDeltaKB = deltaKB;
/*  274 */     newRuleChecker.loadAdditionalDB(MetaInfo.included2kb);
/*  275 */     for (LSDFact fact : twoKB)
/*  276 */       newRuleChecker.loadFact(fact);
/*  277 */     newRuleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  278 */     for (LSDFact fact : deltaKB)
/*  279 */       newRuleChecker.loadFact(fact);
/*  280 */     this.ruleChecker = newRuleChecker;
/*  281 */     this.remainingRuleChecker = createReducedRuleChecker(new ArrayList());
/*  282 */     System.out.println("[swapFactBase: Number of working 2kbFacts]\t: " + twoKB.size());
/*  283 */     System.out.println("[swapFactBase: Number of working deltaFacts]\t: " + delta.size());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LSDTyrubaRuleChecker createRuleChecker()
/*      */     throws ParseException, TypeModeError, IOException
/*      */   {
/*  292 */     LSDTyrubaRuleChecker newRuleChecker = new LSDTyrubaRuleChecker();
/*  293 */     ArrayList<LSDFact> twoKB = this.workingSet2KB;
/*  294 */     ArrayList<LSDFact> deltaKB = this.workingSetDeltaKB;
/*  295 */     newRuleChecker.loadAdditionalDB(MetaInfo.included2kb);
/*  296 */     for (LSDFact fact : twoKB)
/*  297 */       newRuleChecker.loadFact(fact);
/*  298 */     newRuleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  299 */     for (LSDFact fact : deltaKB)
/*  300 */       newRuleChecker.loadFact(fact);
/*  301 */     return newRuleChecker;
/*      */   }
/*      */   
/*      */   public LSDTyrubaRuleChecker createReducedRuleChecker(Collection<LSDRule> additionalRules)
/*      */     throws IOException, TypeModeError, ParseException
/*      */   {
/*  307 */     LSDTyrubaRuleChecker newRuleChecker = new LSDTyrubaRuleChecker();
/*  308 */     newRuleChecker.loadAdditionalDB(MetaInfo.included2kb);
/*  309 */     ArrayList<LSDFact> twoKB = this.workingSet2KB;
/*  310 */     ArrayList<LSDFact> deltaKB = this.workingSetDeltaKB;
/*  311 */     ArrayList<LSDRule> winnowing = this.modifiedWinnowingRules;
/*      */     
/*  313 */     for (LSDFact fact : twoKB)
/*  314 */       newRuleChecker.loadFact(fact);
/*  315 */     newRuleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  316 */     LSDFactBase localFB = new LSDFactBase();
/*  317 */     localFB.load2KBFactBase(twoKB);
/*  318 */     localFB.loadDeltaKBFactBase(deltaKB);
/*  319 */     localFB.loadWinnowingRules(winnowing);
/*  320 */     localFB.loadWinnowingRules(additionalRules);
/*      */     
/*  322 */     Object afterWinnowing = localFB.getRemainingFacts(true);
/*  323 */     this.fb = localFB;
/*  324 */     this.num2KBSize = this.fb.num2KBFactSize();
/*  325 */     this.numDeltaKBSize = this.fb.numDeltaKBFactSize();
/*  326 */     this.numWinnowDeltaKBSize = ((List)afterWinnowing).size();
/*      */     
/*  328 */     for (LSDFact fact : (List)afterWinnowing)
/*  329 */       newRuleChecker.loadFact(fact);
/*  330 */     return newRuleChecker;
/*      */   }
/*      */   
/*      */   public LSDTyrubaRuleChecker createRuleChecker(ArrayList<String> cluster)
/*      */     throws IOException, TypeModeError, ParseException
/*      */   {
/*  336 */     LSDTyrubaRuleChecker newRuleChecker = new LSDTyrubaRuleChecker();
/*  337 */     newRuleChecker.loadAdditionalDB(MetaInfo.included2kb);
/*  338 */     Iterator localIterator2; for (Iterator localIterator1 = this.read2kbFacts.iterator(); localIterator1.hasNext(); 
/*  339 */         localIterator2.hasNext())
/*      */     {
/*  338 */       LSDFact fact = (LSDFact)localIterator1.next();
/*  339 */       localIterator2 = cluster.iterator(); continue;String str = (String)localIterator2.next();
/*  340 */       if (fact.toString().contains(str)) {
/*  341 */         newRuleChecker.loadFact(fact);
/*      */       }
/*      */     }
/*  344 */     newRuleChecker.loadAdditionalDB(MetaInfo.includedDelta);
/*  345 */     for (localIterator1 = this.readDeltaFacts.iterator(); localIterator1.hasNext(); 
/*  346 */         localIterator2.hasNext())
/*      */     {
/*  345 */       LSDFact fact = (LSDFact)localIterator1.next();
/*  346 */       localIterator2 = cluster.iterator(); continue;String str = (String)localIterator2.next();
/*  347 */       if (fact.toString().contains(str))
/*  348 */         newRuleChecker.loadFact(fact);
/*      */     }
/*  350 */     return newRuleChecker;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void startTimer()
/*      */   {
/*  357 */     this.lastStart = new Date().getTime();
/*      */   }
/*      */   
/*      */   private void stopTimer() {
/*  361 */     long temp = new Date().getTime() - this.lastStart;
/*  362 */     this.timer += temp;
/*      */   }
/*      */   
/*      */   private LSDVariable newFreeVariable(Collection<LSDVariable> variables, char type)
/*      */   {
/*  367 */     Set<String> varNames = new HashSet();
/*  368 */     for (LSDVariable variable : variables) {
/*  369 */       varNames.add(variable.getName());
/*      */     }
/*  371 */     for (int i = 0; varNames.contains("x" + i); i++) {}
/*      */     
/*  373 */     return new LSDVariable("x" + i, type);
/*      */   }
/*      */   
/*      */   private double nextEnumerationTiming() {
/*  377 */     long nowTime = new Date().getTime();
/*  378 */     double delta = (nowTime - this.enumerationTimestamp) / 1000.0D;
/*  379 */     this.enumerationTimestamp = nowTime;
/*  380 */     return delta;
/*      */   }
/*      */   
/*      */ 
/*      */   private List<LSDRule> groundRule(LSDRule ungroundedRule)
/*      */   {
/*  386 */     ArrayList<LSDRule> rules = new ArrayList();
/*      */     
/*  388 */     Stack<Grounding> groundings = new Stack();
/*      */     
/*  390 */     groundings.add(new Grounding(ungroundedRule));
/*  391 */     this.statsEnqueuedGroundings += 1;
/*  392 */     this.statsGeneratedGroundings += 1;
/*      */     
/*  394 */     while (!groundings.isEmpty())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  400 */       Grounding grounding = (Grounding)groundings.pop();
/*  401 */       LSDVariable variable = 
/*  402 */         (LSDVariable)grounding.remainingVariables.iterator().next();
/*      */       
/*      */ 
/*  405 */       startTimer();
/*  406 */       Set<String> constants = this.ruleChecker.getReplacementConstants(
/*  407 */         grounding.rule, variable);
/*  408 */       this.statsGroundingConstantsQueryCount += 1;
/*  409 */       constants.add(null);
/*      */       
/*  411 */       for (String constant : constants)
/*      */       {
/*      */ 
/*      */ 
/*  415 */         if ((constant == null) || 
/*  416 */           (!grounding.usedConstants.contains(constant)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  421 */           if ((constant == null) || (constant.indexOf("java.") <= 0))
/*      */           {
/*      */ 
/*  424 */             Grounding newGrounding = grounding.addGrounding(variable, 
/*  425 */               constant);
/*  426 */             this.statsGeneratedGroundings += 1;
/*      */             
/*      */ 
/*  429 */             if (!newGrounding.rule.containsFacts())
/*      */             {
/*  431 */               int minMatchesByLength = this.minMatchesPerLiteral * (
/*  432 */                 newGrounding.rule.getLiterals().size() - 1);
/*  433 */               startTimer();
/*  434 */               int numMatches = countRemainingMatches(newGrounding.rule);
/*  435 */               this.statsGroundingValidQueryCount += 1;
/*  436 */               if ((numMatches >= this.minMatches) && (numMatches >= minMatchesByLength))
/*      */               {
/*      */ 
/*      */ 
/*  440 */                 if (newGrounding.remainingVariables.size() > 0) {
/*  441 */                   if (newGrounding.scanned) {
/*  442 */                     if ((newGrounding.isGrounded()) && (newGrounding.scanned) && 
/*  443 */                       (newGrounding.rule.isValid())) {
/*  444 */                       rules = addRule(rules, grounding, 
/*  445 */                         grounding.numMatches);
/*  446 */                     } else if (grounding.scanned) {
/*      */                       break;
/*      */                     }
/*      */                   }
/*      */                   else {
/*  451 */                     this.statsEnqueuedGroundings += 1;
/*  452 */                     newGrounding.scanned = true;
/*  453 */                     newGrounding.numMatches = numMatches;
/*  454 */                     groundings.add(newGrounding);
/*      */                   }
/*  456 */                 } else if ((newGrounding.rule.isValid()) && 
/*  457 */                   (newGrounding.isGrounded()))
/*  458 */                   addRule(rules, newGrounding, numMatches); }
/*      */             }
/*      */           } } }
/*      */     }
/*  462 */     return rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private List<LSDRule> groundRules(List<LSDRule> ungroundedRules)
/*      */   {
/*  469 */     ArrayList<LSDRule> rules = new ArrayList();
/*      */     
/*  471 */     int rulesGrounded = 0;
/*  472 */     for (LSDRule ungroundedRule : ungroundedRules) {
/*  473 */       if (rulesGrounded % 10 == 0)
/*      */       {
/*  475 */         System.err.println(rulesGrounded * 100 / ungroundedRules
/*  476 */           .size() + 
/*  477 */           "% done.");
/*  478 */         System.err.flush();
/*      */       }
/*  480 */       rules.addAll(groundRule(ungroundedRule));
/*  481 */       rulesGrounded++;
/*      */     }
/*  483 */     return rules;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<LSDRule> extendUngroundedRules(List<LSDRule> oldPartialRules, List<LSDRule> newPartialRules)
/*      */   {
/*  494 */     Set<LSDRule> ungroundedRules = new LinkedHashSet();
/*      */     
/*  496 */     List<LSDPredicate> predicates = getUniquePredicates(this.workingSet2KB, true);
/*  497 */     System.out.println("[extendUngroundRules: predicates to add]\t" + predicates);
/*      */     
/*  499 */     for (Iterator localIterator1 = oldPartialRules.iterator(); localIterator1.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  507 */         ???.hasNext())
/*      */     {
/*  499 */       LSDRule partialRule = (LSDRule)localIterator1.next();
/*  500 */       List<LSDLiteral> previousLiterals = partialRule.getLiterals();
/*  501 */       LSDPredicate conclusionPredicate = 
/*  502 */         ((LSDLiteral)partialRule.getConclusions().getLiterals().get(0)).getPredicate();
/*  503 */       Set<Character> currentTypes = new HashSet();
/*  504 */       for (LSDVariable variable : partialRule.getFreeVariables()) {
/*  505 */         currentTypes.add(Character.valueOf(variable.getType()));
/*      */       }
/*  507 */       ??? = predicates.iterator(); continue;LSDPredicate predicate = (LSDPredicate)???.next();
/*      */       
/*  509 */       LSDPredicate antecedant = null;
/*  510 */       if ((partialRule.getAntecedents() != null) && 
/*  511 */         (partialRule.getAntecedents().getLiterals().size() > 0)) {
/*  512 */         antecedant = 
/*  513 */           ((LSDLiteral)partialRule.getAntecedents().getLiterals().get(0)).getPredicate();
/*      */       }
/*  515 */       if (predicate.allowedInSameRule(conclusionPredicate, 
/*  516 */         antecedant))
/*      */       {
/*      */ 
/*      */ 
/*  520 */         if (predicate.typeMatches(currentTypes))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  526 */           List<List<LSDBinding>> bindingsList = enumerateUngroundedBindings(
/*  527 */             partialRule, predicate);
/*      */           
/*  529 */           for (List<LSDBinding> bindings : bindingsList) {
/*  530 */             this.statsGeneratedPartials += 1;
/*  531 */             LSDLiteral newLiteral = null;
/*      */             try {
/*  533 */               newLiteral = new LSDLiteral(predicate, bindings, 
/*  534 */                 false);
/*      */             }
/*      */             catch (LSDInvalidTypeException localLSDInvalidTypeException) {
/*  537 */               System.err.println("We're taking types directly from the predicates, so we should never have this type error.");
/*  538 */               System.exit(-7);
/*      */             }
/*      */             
/*      */ 
/*  542 */             for (LSDLiteral oldLiteral : previousLiterals)
/*      */             {
/*  544 */               if (oldLiteral.identifiesSameIgnoringNegation(newLiteral)) {
/*      */                 break;
/*      */               }
/*      */             }
/*  548 */             LSDRule newPartialRule = new LSDRule(partialRule);
/*  549 */             newPartialRule.addLiteral(newLiteral);
/*      */             
/*      */ 
/*      */ 
/*  553 */             if ((newPartialRule.literalsLinked()) && 
/*  554 */               (newPartialRule.hasValidLinks()))
/*      */             {
/*      */ 
/*  557 */               int minMatchesByLength = this.minMatchesPerLiteral * (
/*  558 */                 newPartialRule.getLiterals().size() - 1);
/*  559 */               startTimer();
/*  560 */               int numMatches = countRemainingMatches(newPartialRule, 
/*  561 */                 Math.max(this.minMatches, minMatchesByLength));
/*  562 */               this.statsPartialValidQueryCount += 1;
/*  563 */               if ((numMatches >= this.minMatches) && 
/*  564 */                 (numMatches >= minMatchesByLength))
/*      */               {
/*  566 */                 this.statsSavedPartials += 1;
/*  567 */                 ungroundedRules.add(newPartialRule);
/*  568 */                 this.statsEnqueuedPartials += 1;
/*  569 */                 newPartialRules.add(newPartialRule);
/*      */               }
/*      */             }
/*      */           }
/*      */         } }
/*      */     }
/*  575 */     return new ArrayList(ungroundedRules);
/*      */   }
/*      */   
/*      */ 
/*      */   private List<LSDRule> narrowSearch(List<LSDRule> partialRules, int currentLength)
/*      */   {
/*  581 */     ArrayList<LSDRule> chosenRules = new ArrayList();
/*  582 */     ArrayList<LSDRule> sortedRules = sortRules(partialRules);
/*  583 */     int max = Math.min(this.beamSize, sortedRules.size());
/*  584 */     for (int i = 0; i < max; i++)
/*  585 */       chosenRules.add((LSDRule)sortedRules.get(i));
/*  586 */     return chosenRules;
/*      */   }
/*      */   
/*      */   public List<LSDFact> getRelevantFacts(LSDRule rule) {
/*  590 */     return this.fb.getRelevantFacts(rule);
/*      */   }
/*      */   
/*  593 */   public List<Map<LSDVariable, String>> getExceptions(LSDRule rule) { return this.fb.getExceptions(rule); }
/*      */   
/*      */   public List<LSDRule> levelIncrementLearning(PrintStream result) {
/*  596 */     List<LSDRule> rules = null;
/*      */     try {
/*  598 */       for (int level = 0; level <= 3; level++)
/*      */       {
/*  600 */         if (level == 0)
/*  601 */           loadFactBases(1, new LSdiffFilter(true, false, false, false, false));
/*  602 */         TreeSet<LSDFact> workingDeltaKB = this.onDemandDeltaKB
/*  603 */           .expandCluster(null, level);
/*  604 */         swapFactBase(workingDeltaKB);
/*      */         
/*  606 */         rules = enumerateRules(1);
/*  607 */         if (rules != null) {
/*  608 */           this.fb.loadWinnowingRules(rules);
/*  609 */           this.fb.forceWinnowing();
/*      */         }
/*  611 */         int cnt = 0;
/*  612 */         Iterator localIterator2; for (Iterator localIterator1 = rules.iterator(); localIterator1.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  619 */             localIterator2.hasNext())
/*      */         {
/*  612 */           LSDRule r = (LSDRule)localIterator1.next();
/*  613 */           result.println(r.toString());
/*  614 */           int matches = countMatches(r);
/*  615 */           int exceptions = countExceptions(r);
/*  616 */           if (exceptions > 0) this.numRulesWithException += 1;
/*  617 */           result.println("#" + cnt++ + "\t(" + matches + "/" + (matches + exceptions) + ")");
/*  618 */           result.println(r);
/*  619 */           localIterator2 = this.fb.getRelevantFacts(r).iterator(); continue;LSDFact pfact = (LSDFact)localIterator2.next();
/*  620 */           result.println("#P:\t" + pfact);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  625 */       e.printStackTrace();
/*      */     }
/*  627 */     return rules;
/*      */   }
/*      */   
/*      */   public List<LSDRule> levelIncrementLearning2() {
/*  631 */     List<LSDRule> packageLevelRules = null;
/*  632 */     List<LSDRule> typeLevelRules = null;
/*  633 */     List<LSDRule> typeDependencyLevelRules = null;
/*  634 */     List<LSDRule> methodLevelRules = null;
/*  635 */     List<LSDRule> methodBodyLevelRules = null;
/*  636 */     List<LSDRule> fieldLevelRules = null;
/*      */     
/*      */     try
/*      */     {
/*  640 */       for (int level = 0; level <= 5; level++)
/*      */       {
/*  642 */         if (level == 0) {
/*  643 */           loadFactBases(1, new LSdiffFilter(true, false, false, false, false));
/*      */         }
/*  645 */         TreeSet<LSDFact> workingDeltaKB = this.onDemandDeltaKB
/*  646 */           .expandCluster(null, level);
/*      */         
/*  648 */         switch (level) {
/*      */         case 0: 
/*  650 */           System.out.println("**PACKAGE_LEVEL**");
/*  651 */           packageLevelRules = enumerateRules(1);
/*  652 */           this.fb.loadWinnowingRules(packageLevelRules);
/*  653 */           break;
/*      */         case 1: 
/*  655 */           System.out.println("**TYPE_LEVEL**");
/*  656 */           assert (packageLevelRules != null);
/*  657 */           typeLevelRules = extendPreviouslyLearnedRules(packageLevelRules);
/*  658 */           this.fb.loadWinnowingRules(typeLevelRules);
/*  659 */           break;
/*      */         case 2: 
/*  661 */           System.out.println("**TYPE_DEP_LEVEL**");
/*      */           
/*  663 */           assert (typeLevelRules != null);
/*  664 */           typeDependencyLevelRules = extendPreviouslyLearnedRules(typeLevelRules);
/*  665 */           this.fb.loadWinnowingRules(typeDependencyLevelRules);
/*  666 */           break;
/*      */         case 3: 
/*  668 */           System.out.println("**METHOD_LEVEL**");
/*      */           
/*  670 */           methodLevelRules = extendPreviouslyLearnedRules(typeLevelRules);
/*  671 */           this.fb.loadWinnowingRules(methodLevelRules);
/*  672 */           break;
/*      */         case 4: 
/*  674 */           System.out.println("**FIELD_LEVEL**");
/*      */           
/*  676 */           fieldLevelRules = extendPreviouslyLearnedRules(typeLevelRules);
/*  677 */           this.fb.loadWinnowingRules(fieldLevelRules);
/*  678 */           break;
/*      */         case 5: 
/*  680 */           System.out.println("**BODY_LEVEL**");
/*      */           
/*  682 */           methodBodyLevelRules = extendPreviouslyLearnedRules(methodLevelRules);
/*  683 */           this.fb.loadWinnowingRules(methodBodyLevelRules);
/*  684 */           break;
/*      */         default: 
/*  686 */           if (!$assertionsDisabled) throw new AssertionError();
/*      */           break; }
/*  688 */         this.fb.forceWinnowing();
/*      */         
/*  690 */         swapFactBase(workingDeltaKB);
/*      */       }
/*      */     } catch (Exception e) {
/*  693 */       e.printStackTrace();
/*      */     }
/*  695 */     return null;
/*      */   }
/*      */   
/*      */   public List<LSDRule> onDemandLearning(List<LSDFact> cluster, int level)
/*      */   {
/*      */     try
/*      */     {
/*  702 */       TreeSet<LSDFact> nextLevelWorkingDeltaKB = null;
/*      */       
/*  704 */       if (level == 0) {
/*  705 */         this.onDemandDeltaKB.expandCluster(null, level);
/*  706 */       } else { if (level > 5) {
/*  707 */           return null;
/*      */         }
/*  709 */         this.onDemandDeltaKB.expandCluster(cluster, level);
/*      */       }
/*      */       
/*  712 */       swapFactBase(nextLevelWorkingDeltaKB);
/*  713 */       List<LSDRule> rules = enumerateRules(1);
/*      */       
/*  715 */       if (rules != null) {
/*  716 */         this.fb.loadWinnowingRules(rules);
/*  717 */         this.fb.forceWinnowing();
/*      */       }
/*      */       
/*  720 */       System.err.println("Found Rules:" + rules.size());
/*  721 */       this.numValidRules = rules.size();
/*  722 */       List<LSDFact> factUncoveredByRules = this.fb.getRemainingFacts(true);
/*  723 */       this.numRemainingDeltaKBSize = factUncoveredByRules.size();
/*      */       
/*  725 */       int cnt = 0;
/*  726 */       Iterator localIterator2; for (Iterator localIterator1 = rules.iterator(); localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  733 */           localIterator2.hasNext())
/*      */       {
/*  726 */         LSDRule r = (LSDRule)localIterator1.next();
/*  727 */         System.err.println(r.toString());
/*  728 */         int matches = countMatches(r);
/*  729 */         int exceptions = countExceptions(r);
/*  730 */         if (exceptions > 0) this.numRulesWithException += 1;
/*  731 */         System.err.println("#" + cnt++ + "\t(" + matches + "/" + (matches + exceptions) + ")");
/*  732 */         System.err.println(r);
/*  733 */         localIterator2 = this.fb.getRelevantFacts(r).iterator(); continue;LSDFact pfact = (LSDFact)localIterator2.next();
/*  734 */         System.err.println("#P:\t" + pfact);
/*      */       }
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  739 */       e.printStackTrace();
/*  740 */       System.err.println(e.getMessage());
/*      */     }
/*  742 */     return null;
/*      */   }
/*      */   
/*      */   public void bruteForceLearning(int hopDistance2KB, LSdiffFilter filter)
/*      */   {
/*      */     try
/*      */     {
/*  749 */       long st = new Date().getTime();
/*      */       
/*  751 */       loadFactBases(hopDistance2KB, filter);
/*      */       
/*  753 */       List<LSDRule> rules = enumerateRules(this.antecedantSize);
/*  754 */       this.numValidRules = rules.size();
/*      */       
/*      */ 
/*  757 */       this.fb.loadWinnowingRules(rules);
/*  758 */       this.fb.forceWinnowing();
/*      */       
/*      */ 
/*  761 */       List<LSDFact> remainingFacts = this.fb.getRemainingFacts(true);
/*  762 */       this.numRemainingDeltaKBSize = remainingFacts.size();
/*      */       
/*  764 */       System.err.println("Found Rules:" + rules.size());
/*  765 */       int cnt = 1;
/*  766 */       int matches; Iterator localIterator2; for (Iterator localIterator1 = rules.iterator(); localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  774 */           localIterator2.hasNext())
/*      */       {
/*  766 */         LSDRule r = (LSDRule)localIterator1.next();
/*      */         
/*  768 */         System.err.println(r.toString());
/*  769 */         matches = countMatches(r);
/*  770 */         int exceptions = countExceptions(r);
/*  771 */         if (exceptions > 0) this.numRulesWithException += 1;
/*  772 */         System.err.println("#" + cnt++ + "\t(" + matches + "/" + (matches + exceptions) + ")");
/*  773 */         System.err.println(r);
/*  774 */         localIterator2 = this.fb.getRelevantFacts(r).iterator(); continue;LSDFact pfact = (LSDFact)localIterator2.next();
/*  775 */         System.err.println("#P:\t" + pfact);
/*      */       }
/*      */       
/*      */ 
/*  779 */       Collection<LSDRule> selectedSubset = coverSet(rules, true, null);
/*      */       
/*  781 */       System.err.println("Selected Rules:" + selectedSubset.size());
/*      */       
/*  783 */       for (LSDRule r : selectedSubset) {
/*  784 */         System.err.println(r.toString());
/*      */       }
/*      */       
/*  787 */       System.err.println("Remaining Facts:" + remainingFacts.size());
/*  788 */       for (LSDFact f : remainingFacts) {
/*  789 */         System.err.print(f);
/*      */       }
/*      */       
/*  792 */       int cInfo = counttextual(selectedSubset);
/*  793 */       long en = new Date().getTime();
/*  794 */       this.output.write(Double.valueOf(en - st).doubleValue() / 1000.0D + " \t " + 
/*  795 */         rules.size() + " \t " + selectedSubset.size() + " \t " + 
/*  796 */         this.numRemainingDeltaKBSize + " \t " + this.resString + cInfo);
/*  797 */       this.output.newLine();
/*      */       
/*  799 */       shutdown();
/*      */     } catch (Exception e) {
/*  801 */       e.printStackTrace();
/*  802 */       System.err.println(e.getMessage());
/*      */     }
/*  804 */     System.out.println("Done");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   Collection<LSDRule> coverSet(Collection<LSDRule> rules, boolean print, File rf)
/*      */     throws TypeModeError, FileNotFoundException, ParseException, IOException
/*      */   {
/*  812 */     List<LSDRule> chosenRules = new ArrayList();
/*  813 */     List<LSDRule> remainingRules = new ArrayList(rules);
/*      */     
/*  815 */     HashMap<LSDRule, Integer> alreadyFoundExceptionCounts = new HashMap();
/*  816 */     int startingNumFacts = -1;
/*      */     List<LSDFact> remainingFacts;
/*  818 */     do { LSDFactBase fb = new LSDFactBase();
/*  819 */       LSDRule bestRule = null;
/*  820 */       int bestCount = 0;
/*  821 */       fb.load2KBFactBase(this.workingSet2KB);
/*  822 */       fb.loadDeltaKBFactBase(this.workingSetDeltaKB);
/*  823 */       fb.loadWinnowingRules(this.modifiedWinnowingRules);
/*  824 */       fb.loadWinnowingRules(chosenRules);
/*  825 */       remainingFacts = fb.getRemainingFacts(true);
/*  826 */       if (startingNumFacts == -1)
/*  827 */         startingNumFacts = remainingFacts.size();
/*  828 */       fb.loadWinnowingRules(remainingRules);
/*  829 */       for (Iterator<LSDRule> i = remainingRules.iterator(); i.hasNext();) {
/*  830 */         LSDRule rule = (LSDRule)i.next();
/*  831 */         List<LSDFact> facts = fb.getRelevantFacts(rule);
/*  832 */         facts.retainAll(remainingFacts);
/*  833 */         int count = facts.size();
/*  834 */         if (count == 0) {
/*  835 */           i.remove();
/*      */ 
/*      */         }
/*  838 */         else if (count > bestCount) {
/*  839 */           bestCount = count;
/*  840 */           bestRule = rule;
/*  841 */         } else if (count == bestCount)
/*      */         {
/*      */ 
/*  844 */           Integer preFound = 
/*  845 */             (Integer)alreadyFoundExceptionCounts.get(bestRule);
/*      */           int exBestRule;
/*  847 */           if (preFound == null) {
/*  848 */             int exBestRule = countExceptions(bestRule);
/*  849 */             alreadyFoundExceptionCounts.put(bestRule, Integer.valueOf(exBestRule));
/*      */           } else {
/*  851 */             exBestRule = preFound.intValue();
/*      */           }
/*  853 */           preFound = (Integer)alreadyFoundExceptionCounts.get(bestRule);
/*      */           int exRule;
/*  855 */           if (preFound == null) {
/*  856 */             int exRule = countExceptions(rule);
/*  857 */             alreadyFoundExceptionCounts.put(rule, Integer.valueOf(exRule));
/*      */           } else {
/*  859 */             exRule = preFound.intValue();
/*      */           }
/*      */           
/*      */ 
/*  863 */           if (exBestRule > exRule) {
/*  864 */             bestRule = rule;
/*      */           }
/*  866 */           else if (rule.generalityCompare(bestRule) < 0)
/*  867 */             bestRule = rule;
/*      */         }
/*      */       }
/*  870 */       if (bestRule != null) {
/*  871 */         chosenRules.add(bestRule);
/*  872 */         remainingRules.remove(bestRule);
/*  873 */         remainingFacts.removeAll(fb.getRelevantFacts(bestRule));
/*      */       }
/*  875 */     } while ((!remainingFacts.isEmpty()) && (!remainingRules.isEmpty()));
/*      */     
/*  877 */     LSDFactBase fb = new LSDFactBase();
/*  878 */     if (print) {
/*  879 */       fb.load2KBFactBase(this.workingSet2KB);
/*  880 */       fb.loadDeltaKBFactBase(this.workingSetDeltaKB);
/*  881 */       fb.loadWinnowingRules(this.modifiedWinnowingRules);
/*  882 */       fb.loadWinnowingRules(chosenRules);
/*  883 */       fb.forceWinnowing();
/*      */     }
/*  885 */     double coverage = Double.valueOf(startingNumFacts - remainingFacts
/*  886 */       .size()).doubleValue() / 
/*  887 */       Double.valueOf(startingNumFacts).doubleValue();
/*  888 */     double conciseness = Double.valueOf(startingNumFacts).doubleValue() / 
/*  889 */       Double.valueOf(chosenRules.size() + remainingFacts.size()).doubleValue();
/*  890 */     this.resString = 
/*  891 */       (startingNumFacts + " \t " + coverage + " \t " + conciseness + " \t ");
/*      */     
/*  893 */     return chosenRules;
/*      */   }
/*      */   
/*      */   private ArrayList<LSDRule> addRule(ArrayList<LSDRule> rules, Grounding grounding, int numMatches)
/*      */   {
/*  898 */     double accuracy = measureAccuracy(grounding.rule, this.minAccuracy, 
/*  899 */       this.maxExceptions, numMatches);
/*  900 */     this.statsGroundingExceptionsQueryCount += 1;
/*  901 */     if (accuracy >= this.minAccuracy) {
/*  902 */       this.statsSavedGroundings += 1;
/*  903 */       grounding.rule.setAccuracy(accuracy);
/*  904 */       grounding.rule.setNumMatches(numMatches);
/*  905 */       grounding.rule.setScore();
/*  906 */       rules.add(grounding.rule);
/*      */     }
/*  908 */     return rules;
/*      */   }
/*      */   
/*      */   List<List<LSDBinding>> enumerateUngroundedBindings(LSDRule partialRule, LSDPredicate predicate)
/*      */   {
/*  913 */     List<List<LSDBinding>> bindingsList = new ArrayList();
/*  914 */     bindingsList.add(new ArrayList());
/*  915 */     Set<LSDVariable> ruleFreeVars = new HashSet(partialRule
/*  916 */       .getFreeVariables());
/*      */     char[] arrayOfChar;
/*  918 */     int j = (arrayOfChar = predicate.getTypes()).length; List<List<LSDBinding>> newBindingsList; for (int i = 0; i < j; i++) { char type = arrayOfChar[i];
/*  919 */       newBindingsList = new ArrayList();
/*      */       
/*  921 */       for (Iterator localIterator1 = bindingsList.iterator(); localIterator1.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  935 */           ???.hasNext())
/*      */       {
/*  921 */         List<LSDBinding> prevBindings = (List)localIterator1.next();
/*      */         
/*      */ 
/*  924 */         Set<LSDVariable> freeVariables = new HashSet(
/*  925 */           ruleFreeVars);
/*  926 */         for (LSDBinding b : prevBindings)
/*  927 */           freeVariables.add(b.getVariable());
/*  928 */         List<LSDVariable> variableChoices = new ArrayList();
/*  929 */         for (LSDVariable v : freeVariables) {
/*  930 */           if (v.getType() == type)
/*  931 */             variableChoices.add(v);
/*      */         }
/*  933 */         variableChoices.add(newFreeVariable(freeVariables, type));
/*      */         
/*  935 */         ??? = variableChoices.iterator(); continue;LSDVariable nextVariable = (LSDVariable)???.next();
/*      */         
/*  937 */         ArrayList<LSDBinding> newBindings = new ArrayList(
/*  938 */           prevBindings);
/*  939 */         newBindings.add(new LSDBinding(nextVariable));
/*  940 */         newBindingsList.add(newBindings);
/*      */       }
/*      */       
/*      */ 
/*  944 */       bindingsList = newBindingsList;
/*      */     }
/*  946 */     for (Iterator<List<LSDBinding>> i = bindingsList.iterator(); i
/*  947 */           .hasNext();) {
/*  948 */       Object bindings = (List)i.next();
/*  949 */       boolean linked = false;
/*  950 */       for (LSDBinding b : (List)bindings) {
/*  951 */         if (ruleFreeVars.contains(b.getVariable())) {
/*  952 */           linked = true;
/*  953 */           break;
/*      */         }
/*      */       }
/*  956 */       if (!linked)
/*  957 */         i.remove();
/*      */     }
/*  959 */     return bindingsList;
/*      */   }
/*      */   
/*      */   List<List<LSDBinding>> enumerateUngroundedBindings(LSDRule partialRule, LSDLiteral literal)
/*      */   {
/*  964 */     List<List<LSDBinding>> bindingsList = new ArrayList();
/*  965 */     bindingsList.add(new ArrayList());
/*  966 */     Set<LSDVariable> ruleFreeVars = new HashSet(partialRule
/*  967 */       .getFreeVariables());
/*      */     Iterator localIterator2;
/*  969 */     for (LSDBinding binding : literal.getBindings())
/*  970 */       if (!binding.isBound())
/*      */       {
/*  972 */         List<List<LSDBinding>> newBindingsList = new ArrayList();
/*      */         
/*  974 */         for (localIterator2 = bindingsList.iterator(); localIterator2.hasNext(); 
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  989 */             ???.hasNext())
/*      */         {
/*  974 */           List<LSDBinding> prevBindings = (List)localIterator2.next();
/*      */           
/*      */ 
/*  977 */           Set<LSDVariable> freeVariables = new HashSet(
/*  978 */             ruleFreeVars);
/*  979 */           for (LSDBinding b : prevBindings)
/*  980 */             freeVariables.add(b.getVariable());
/*  981 */           List<LSDVariable> variableChoices = new ArrayList();
/*  982 */           for (LSDVariable v : freeVariables) {
/*  983 */             if (v.getType() == binding.getType())
/*  984 */               variableChoices.add(v);
/*      */           }
/*  986 */           variableChoices.add(newFreeVariable(freeVariables, binding
/*  987 */             .getType()));
/*      */           
/*  989 */           ??? = variableChoices.iterator(); continue;LSDVariable nextVariable = (LSDVariable)???.next();
/*      */           
/*  991 */           ArrayList<LSDBinding> newBindings = new ArrayList(
/*  992 */             prevBindings);
/*  993 */           newBindings.add(new LSDBinding(nextVariable));
/*  994 */           newBindingsList.add(newBindings);
/*      */         }
/*      */         
/*      */ 
/*  998 */         bindingsList = newBindingsList;
/*      */       }
/* 1000 */     for (Iterator<List<LSDBinding>> i = bindingsList.iterator(); i
/* 1001 */           .hasNext();) {
/* 1002 */       Object bindings = (List)i.next();
/* 1003 */       boolean linked = false;
/* 1004 */       for (LSDBinding b : (List)bindings) {
/* 1005 */         if (ruleFreeVars.contains(b.getVariable())) {
/* 1006 */           linked = true;
/* 1007 */           break;
/*      */         }
/*      */       }
/* 1010 */       if (!linked)
/* 1011 */         i.remove();
/*      */     }
/* 1013 */     return bindingsList;
/*      */   }
/*      */   
/*      */   int countRemainingMatches(LSDRule rule)
/*      */   {
/* 1018 */     return this.remainingRuleChecker.countTrueConclusions(rule);
/*      */   }
/*      */   
/*      */   int countRemainingMatches(LSDRule rule, int i)
/*      */   {
/* 1023 */     return this.remainingRuleChecker.countTrueConclusions(rule, i);
/*      */   }
/*      */   
/*      */   public int countMatches(LSDRule rule)
/*      */   {
/* 1028 */     return this.ruleChecker.countTrueConclusions(rule);
/*      */   }
/*      */   
/*      */   public int countExceptions(LSDRule rule)
/*      */   {
/* 1033 */     return this.ruleChecker.countCounterExamples(rule);
/*      */   }
/*      */   
/*      */   int countExceptions(LSDRule rule, int max)
/*      */   {
/* 1038 */     return this.ruleChecker.countCounterExamples(rule, max);
/*      */   }
/*      */   
/*      */   double measureAccuracy(LSDRule rule, double min, int maxExceptions, double matches)
/*      */   {
/* 1043 */     int accuracyMaxExceptions = 
/* 1044 */       (int)Math.floor(matches / min - matches) + 1;
/* 1045 */     double exceptions = countExceptions(rule, Math.min(maxExceptions, 
/* 1046 */       accuracyMaxExceptions));
/* 1047 */     if (exceptions >= maxExceptions)
/* 1048 */       return 0.0D;
/* 1049 */     return matches / (matches + exceptions);
/*      */   }
/*      */   
/*      */   public void shutdown() {
/* 1053 */     this.ruleChecker.shutdown();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected class Grounding
/*      */   {
/*      */     public int numMatches;
/*      */     
/* 1062 */     public boolean scanned = false;
/*      */     
/*      */     public Set<LSDVariable> remainingVariables;
/*      */     
/* 1066 */     public Set<String> usedConstants = new HashSet();
/*      */     public LSDRule rule;
/*      */     
/*      */     public Grounding(LSDRule rule)
/*      */     {
/* 1071 */       this.remainingVariables = new LinkedHashSet(rule
/* 1072 */         .getFreeVariables());
/* 1073 */       this.rule = rule;
/*      */     }
/*      */     
/*      */     public boolean isGrounded()
/*      */     {
/* 1078 */       ArrayList<LSDLiteral> literalsList = this.rule.getLiterals();
/* 1079 */       Iterator localIterator2; for (Iterator localIterator1 = literalsList.iterator(); localIterator1.hasNext(); 
/*      */           
/* 1081 */           localIterator2.hasNext())
/*      */       {
/* 1079 */         LSDLiteral literal = (LSDLiteral)localIterator1.next();
/* 1080 */         List<LSDBinding> bindingsList = literal.getBindings();
/* 1081 */         localIterator2 = bindingsList.iterator(); continue;LSDBinding binding = (LSDBinding)localIterator2.next();
/* 1082 */         if (binding.getGroundConst() != null) {
/* 1083 */           return true;
/*      */         }
/*      */       }
/* 1086 */       return false;
/*      */     }
/*      */     
/*      */     public Grounding(Grounding oldGrounding) {
/* 1090 */       this.remainingVariables = new HashSet(
/* 1091 */         oldGrounding.remainingVariables);
/* 1092 */       this.usedConstants = new HashSet(oldGrounding.usedConstants);
/* 1093 */       this.rule = oldGrounding.rule;
/*      */     }
/*      */     
/*      */     public Grounding addGrounding(LSDVariable variable, String constant) {
/* 1097 */       Grounding newGrounding = new Grounding(LSDRuleEnumerator.this, this);
/*      */       
/* 1099 */       assert (this.remainingVariables.contains(variable)) : 
/* 1100 */         ("Error: " + this.remainingVariables + " doesn't contain " + variable);
/* 1101 */       newGrounding.remainingVariables.remove(variable);
/* 1102 */       if (constant != null) {
/* 1103 */         assert (!this.usedConstants.contains(constant));
/* 1104 */         newGrounding.remainingVariables.remove(variable);
/* 1105 */         newGrounding.usedConstants.add(constant);
/*      */         try {
/* 1107 */           newGrounding.rule = this.rule.substitute(variable, 
/* 1108 */             new LSDBinding(constant));
/*      */         }
/*      */         catch (LSDInvalidTypeException localLSDInvalidTypeException) {
/* 1111 */           System.err.println("We're dealing with consts, so why type mismatch?");
/* 1112 */           System.exit(-15);
/*      */         }
/*      */       }
/* 1115 */       return newGrounding;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void LogData(File rf, List<LSDFact> remainingFacts, List<LSDRule> chosenRules, int startingNumFacts)
/*      */     throws IOException
/*      */   {
/* 1123 */     BufferedWriter output = new BufferedWriter(new FileWriter(rf));
/* 1124 */     if (!remainingFacts.isEmpty()) {
/* 1125 */       output.write("The following facts (" + remainingFacts.size() + "/" + 
/* 1126 */         startingNumFacts + ")were not matched by any rule:");
/* 1127 */       output.newLine();
/* 1128 */       for (LSDFact fact : remainingFacts) {
/* 1129 */         output.write("\t" + fact);
/* 1130 */         output.newLine();
/*      */       }
/*      */     } else {
/* 1133 */       output.write("Complete coverage."); }
/* 1134 */     output.newLine();
/* 1135 */     for (LSDRule rule : chosenRules) {
/* 1136 */       int matches = rule.getNumMatches();
/* 1137 */       int exceptions = countExceptions(rule);
/* 1138 */       output.newLine();
/* 1139 */       output.write("\t" + rule + "\t(" + matches + "/" + (
/* 1140 */         matches + exceptions) + ")");
/* 1141 */       output.newLine();
/* 1142 */       for (LSDFact pfact : this.fb.getRelevantFacts(rule)) {
/* 1143 */         output.write("\t#P:\t" + pfact);
/* 1144 */         output.newLine();
/*      */       }
/*      */       
/* 1147 */       if (exceptions > 0) {
/* 1148 */         output.write("\t    Except:");
/* 1149 */         output.newLine();
/*      */         
/* 1151 */         ??? = this.fb.getExceptions(rule).iterator();
/* 1150 */         while (???.hasNext()) {
/* 1151 */           Map<LSDVariable, String> exception = (Map)???.next();
/* 1152 */           output.newLine();
/* 1153 */           output.write("\t\t(");
/* 1154 */           boolean first = true;
/* 1155 */           for (LSDVariable var : exception.keySet()) {
/* 1156 */             output.write((first ? "" : ", ") + var + "=" + 
/* 1157 */               (String)exception.get(var));
/* 1158 */             first = false;
/*      */           }
/* 1160 */           output.write(")");
/*      */         }
/* 1162 */         output.write("");
/* 1163 */         output.newLine();
/*      */       }
/*      */     }
/* 1166 */     output.close();
/*      */   }
/*      */   
/*      */ 
/*      */   private int counttextual(Collection<LSDRule> selectedSubset)
/*      */   {
/* 1172 */     int count = 0;
/* 1173 */     for (LSDRule rule : selectedSubset) { Iterator localIterator3;
/* 1174 */       for (Iterator localIterator2 = rule.getLiterals().iterator(); localIterator2.hasNext(); 
/* 1175 */           localIterator3.hasNext())
/*      */       {
/* 1174 */         LSDLiteral literal = (LSDLiteral)localIterator2.next();
/* 1175 */         localIterator3 = literal.getBindings().iterator(); continue;LSDBinding bind = (LSDBinding)localIterator3.next();
/* 1176 */         int i = 0;
/* 1177 */         if (bind.isBound())
/* 1178 */           for (LSDFact delta : this.readDeltaFacts)
/*      */           {
/* 1180 */             if (delta.toString().contains(bind.getGroundConst())) {
/*      */               break;
/*      */             }
/* 1183 */             i++;
/*      */           }
/* 1185 */         if (i == this.readDeltaFacts.size()) {
/* 1186 */           count++;
/* 1187 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1193 */     return count;
/*      */   }
/*      */   
/*      */   private ArrayList<LSDRule> sortRules(List<LSDRule> rules)
/*      */   {
/* 1198 */     LSDRule[] temp = new LSDRule[rules.size()];
/* 1199 */     for (int i = 0; i < temp.length; i++) {
/* 1200 */       temp[i] = ((LSDRule)rules.get(i));
/*      */     }
/* 1202 */     void tmp49_46 = new LSDRule();tmp49_46.getClass();Arrays.sort(temp, new LSDRule.LSDRuleComparator(tmp49_46));
/* 1203 */     ArrayList<LSDRule> sortedList = new ArrayList();
/* 1204 */     LSDRule[] arrayOfLSDRule1; int j = (arrayOfLSDRule1 = temp).length; for (int i = 0; i < j; i++) { LSDRule rule = arrayOfLSDRule1[i];
/* 1205 */       sortedList.add(rule);
/*      */     }
/* 1207 */     return sortedList;
/*      */   }
/*      */   
/*      */ 
/*      */   public List<LSDRule> enumerateRules(int maxLiterals)
/*      */   {
/* 1213 */     List<LSDRule> rules = new ArrayList();
/* 1214 */     List<LSDRule> partialRules = new ArrayList(
/* 1215 */       enumerateConclusions());
/* 1216 */     this.statsGeneratedPartials += partialRules.size();
/* 1217 */     this.statsEnqueuedPartials += partialRules.size();
/* 1218 */     for (int currentLength = 1; currentLength <= maxLiterals; currentLength++)
/*      */     {
/* 1220 */       System.out.println("Finding rules of length " + currentLength);
/* 1221 */       List<LSDRule> newPartialRules = new ArrayList();
/* 1222 */       nextEnumerationTiming();
/* 1223 */       List<LSDRule> ungroundedRules = extendUngroundedRules(partialRules, 
/* 1224 */         newPartialRules);
/* 1225 */       double iterationTimeUngroundRuleGeneration = nextEnumerationTiming();
/* 1226 */       this.timeUngroundRuleGeneration += iterationTimeUngroundRuleGeneration;
/*      */       
/* 1228 */       System.out.println("Ungrounded rules, length " + currentLength + 
/* 1229 */         ": " + iterationTimeUngroundRuleGeneration + " s");
/* 1230 */       System.out.println("Total ungrounded rules generated: " + ungroundedRules.size());
/* 1231 */       partialRules = newPartialRules;
/* 1232 */       rules.addAll(groundRules(ungroundedRules));
/*      */       
/* 1234 */       double iterationTimePartiallyGroundRuleGeneration = nextEnumerationTiming();
/* 1235 */       this.timePartiallyGroundRuleGeneration += iterationTimePartiallyGroundRuleGeneration;
/* 1236 */       System.out.println("Rule grounding, length " + currentLength + ": " + 
/* 1237 */         iterationTimePartiallyGroundRuleGeneration + " s");
/* 1238 */       System.out.println("Total grounded rules generated: " + 
/* 1239 */         rules.size() + " rules");
/* 1240 */       if (currentLength == maxLiterals)
/*      */         break;
/*      */       try {
/* 1243 */         this.remainingRuleChecker.shutdown();
/* 1244 */         this.remainingRuleChecker = createReducedRuleChecker(rules);
/*      */       } catch (Exception e) {
/* 1246 */         System.out.println(e);
/* 1247 */         e.printStackTrace();
/*      */       }
/* 1249 */       System.out.println("Creating new rule checker: " + 
/* 1250 */         nextEnumerationTiming() + " s");
/* 1251 */       System.out.println("Enqueued partial rules: " + partialRules.size() + 
/* 1252 */         " rules");
/*      */       
/* 1254 */       partialRules = narrowSearch(newPartialRules, currentLength);
/* 1255 */       System.out.println("Reduced enqueued partial rules: " + 
/* 1256 */         partialRules.size() + " rules");
/* 1257 */       System.out.println("Reducing partial rule set: " + 
/* 1258 */         nextEnumerationTiming() + " s");
/* 1259 */       if (partialRules.size() == 0)
/*      */         break;
/*      */     }
/* 1262 */     return rules;
/*      */   }
/*      */   
/*      */   public List<LSDRule> extendPreviouslyLearnedRules(List<LSDRule> previouslyLearnedRules) {
/* 1266 */     List<LSDRule> outcome = new ArrayList();
/* 1267 */     List<LSDRule> combineNewConsequentToPreviouslyLearnedRules = new ArrayList();
/*      */     
/* 1269 */     if (previouslyLearnedRules.size() > 0) {
/*      */       Iterator localIterator2;
/* 1271 */       for (Iterator localIterator1 = getUniquePredicates(this.workingSetDeltaKB, false).iterator(); localIterator1.hasNext(); 
/*      */           
/* 1273 */           localIterator2.hasNext())
/*      */       {
/* 1271 */         LSDPredicate newConsequentPred = (LSDPredicate)localIterator1.next();
/*      */         
/* 1273 */         localIterator2 = previouslyLearnedRules.iterator(); continue;LSDRule previousRule = (LSDRule)localIterator2.next();
/*      */         
/*      */ 
/* 1276 */         LSDRule previousAntecedents = previousRule.getAntecedents();
/*      */         
/* 1278 */         List<List<LSDBinding>> potentialBindingsForNewConsequentPredicate = enumerateUngroundedBindings(
/* 1279 */           previousAntecedents, newConsequentPred);
/*      */         
/*      */ 
/*      */ 
/* 1283 */         for (List<LSDBinding> bindingsForNewConsequent : potentialBindingsForNewConsequentPredicate) {
/* 1284 */           LSDLiteral newConsequentLiteral = null;
/*      */           try {
/* 1286 */             newConsequentLiteral = new LSDLiteral(newConsequentPred, bindingsForNewConsequent, 
/* 1287 */               true);
/*      */           }
/*      */           catch (LSDInvalidTypeException localLSDInvalidTypeException) {
/* 1290 */             System.err.println("We're taking types directly from the predicates, so we should never have this type error.");
/* 1291 */             System.exit(-7);
/*      */           }
/*      */           
/*      */ 
/* 1295 */           LSDRule previousAntecedentsNewConsequent = new LSDRule(previousAntecedents);
/* 1296 */           previousAntecedentsNewConsequent.addLiteral(newConsequentLiteral);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1301 */           combineNewConsequentToPreviouslyLearnedRules.add(previousAntecedentsNewConsequent);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1306 */       combineNewConsequentToPreviouslyLearnedRules = enumerateConclusions();
/*      */     }
/* 1308 */     System.out.println("[# combineNewConsequentToPreviouslyLearnedRules]:\t" + combineNewConsequentToPreviouslyLearnedRules.size());
/*      */     
/* 1310 */     List<LSDRule> newPartialRules = new ArrayList();
/* 1311 */     Object ungroundedRules = extendUngroundedRules(
/* 1312 */       combineNewConsequentToPreviouslyLearnedRules, newPartialRules);
/*      */     
/* 1314 */     System.out.println("[# ungroundedRules]:\t" + ((List)ungroundedRules).size());
/*      */     
/*      */ 
/*      */ 
/* 1318 */     List<LSDRule> groundRules = groundRules((List)ungroundedRules);
/* 1319 */     System.out.println("[# groundRules]:\t" + groundRules.size());
/*      */     
/*      */ 
/*      */ 
/* 1323 */     outcome.addAll(groundRules);
/*      */     try
/*      */     {
/* 1326 */       this.remainingRuleChecker.shutdown();
/* 1327 */       this.remainingRuleChecker = createReducedRuleChecker(outcome);
/*      */     } catch (Exception e) {
/* 1329 */       System.out.println(e);
/* 1330 */       e.printStackTrace();
/*      */     }
/* 1332 */     return outcome;
/*      */   }
/*      */   
/*      */   private List<LSDRule> enumerateConclusions()
/*      */   {
/* 1337 */     List<LSDRule> conclusions = new ArrayList();
/*      */     
/* 1339 */     System.out.println("[enumerateConclusion: getUniquePredicates]:\t" + getUniquePredicates(this.workingSetDeltaKB, false));
/*      */     
/* 1341 */     for (LSDPredicate predicate : getUniquePredicates(this.workingSetDeltaKB, false))
/*      */     {
/* 1343 */       ArrayList<LSDBinding> bindings = new ArrayList();
/* 1344 */       ArrayList<LSDVariable> variables = new ArrayList();
/* 1345 */       char[] arrayOfChar; int j = (arrayOfChar = predicate.getTypes()).length; for (int i = 0; i < j; i++) { char type = arrayOfChar[i];
/* 1346 */         LSDVariable nextVar = newFreeVariable(variables, type);
/* 1347 */         variables.add(nextVar);
/* 1348 */         bindings.add(new LSDBinding(nextVar));
/*      */       }
/*      */       
/* 1351 */       LSDRule rule = new LSDRule();
/*      */       try {
/* 1353 */         rule.addLiteral(new LSDLiteral(predicate, bindings, 
/* 1354 */           true));
/*      */       }
/*      */       catch (LSDInvalidTypeException localLSDInvalidTypeException) {
/* 1357 */         System.err.println("We're taking types directly from the predicates, so we should never have this type error.");
/* 1358 */         System.exit(-7);
/*      */       }
/* 1360 */       startTimer();
/* 1361 */       int numMatches = countRemainingMatches(rule, this.minMatches);
/* 1362 */       this.statsPartialValidQueryCount += 1;
/* 1363 */       if (numMatches >= this.minMatches)
/* 1364 */         conclusions.add(rule);
/*      */     }
/* 1366 */     return conclusions;
/*      */   }
/*      */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/facts/LSDRuleEnumerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */