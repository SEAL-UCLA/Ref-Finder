/*     */ package lsd.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import lsd.rule.LSDBinding;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDLiteral;
/*     */ import lsd.rule.LSDPredicate;
/*     */ import lsd.rule.LSDRule;
/*     */ import lsd.rule.LSDVariable;
/*     */ import metapackage.MetaInfo;
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.RBExpression;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBVariable;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.util.ElementSource;
/*     */ 
/*     */ public class LSDTyrubaRuleChecker
/*     */ {
/*  30 */   private FrontEnd frontend = null;
/*     */   
/*  32 */   private File dbDir = null;
/*     */   
/*  34 */   private boolean backgroundPageCleaning = false;
/*     */   
/*  36 */   boolean loadInitFile = true;
/*     */   
/*  38 */   int cachesize = 5000;
/*     */   
/*     */   public LSDTyrubaRuleChecker()
/*     */   {
/*  42 */     if (this.frontend == null) {
/*  43 */       if (this.dbDir == null) {
/*  44 */         this.frontend = new FrontEnd(this.loadInitFile, MetaInfo.fdbDir, true, 
/*  45 */           null, true, this.backgroundPageCleaning);
/*     */       }
/*     */       else
/*  48 */         this.frontend = new FrontEnd(this.loadInitFile, this.dbDir, true, null, false, 
/*  49 */           this.backgroundPageCleaning);
/*     */     }
/*  51 */     this.frontend.setCacheSize(this.cachesize);
/*     */   }
/*     */   
/*     */ 
/*     */   public void loadAdditionalDB(File inputDBFile)
/*     */     throws ParseException, TypeModeError, IOException
/*     */   {
/*  58 */     this.frontend.load(inputDBFile.getAbsolutePath());
/*     */   }
/*     */   
/*     */   public void loadAdditionalDB(String input)
/*     */     throws ParseException, TypeModeError, IOException
/*     */   {
/*  64 */     this.frontend.load(input);
/*     */   }
/*     */   
/*     */   public void loadPrimedAdditionalDB(File inputDBFile)
/*     */     throws ParseException, TypeModeError, IOException
/*     */   {
/*  70 */     List<LSDFact> facts = null;
/*     */     try {
/*  72 */       facts = new LSDTyrubaFactReader(inputDBFile).getFacts();
/*     */     } catch (Exception localException) {}
/*  74 */     if (facts != null) {
/*  75 */       for (LSDFact fact : facts) {
/*  76 */         if (fact.getPredicate().isConclusionPredicate())
/*     */         {
/*  78 */           this.frontend.parse(fact.toString().replaceFirst("_", "_p_") + ".");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void loadFact(LSDFact fact)
/*     */     throws TypeModeError, ParseException
/*     */   {
/*  88 */     this.frontend.parse(fact.toString() + ".");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<java.util.Map<LSDVariable, String>> getCounterExamples(LSDRule rule)
/*     */   {
/*  97 */     return (List)invokeQuery(rule, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ArrayList<LSDRule> getTrueConclusions(LSDRule rule)
/*     */   {
/* 104 */     return (ArrayList)invokeQuery(rule, true);
/*     */   }
/*     */   
/*     */   private Object invokeQuery(LSDRule rule, boolean returnConclusions)
/*     */   {
/* 109 */     LSDRule substitute = returnConclusions ? rule.getConclusions() : rule;
/* 110 */     String query = (returnConclusions ? rule.convertAllToAntecedents() : rule).toTyrubaQuery(false);
/* 111 */     ArrayList<java.util.Map<LSDVariable, String>> exceptions = new ArrayList();
/* 112 */     ArrayList<LSDRule> newSubstitutedRules = new ArrayList();
/* 113 */     ArrayList<LSDVariable> freeVars = rule.getConclusions().getFreeVariables();
/*     */     
/* 115 */     Set<Set<String>> exceptionMatches = new HashSet();
/* 116 */     Set<String> foundConclusionMatches = new HashSet();
/*     */     
/*     */     try
/*     */     {
/* 120 */       RBExpression exp = this.frontend.makeExpression(query);
/* 121 */       ElementSource es = this.frontend.frameQuery(exp);
/*     */       try {
/* 123 */         if (es.status() == -1)
/*     */         {
/* 125 */           if (returnConclusions) {
/* 126 */             return newSubstitutedRules;
/*     */           }
/* 128 */           return exceptions;
/*     */         }
/*     */         
/*     */ 
/* 132 */         while (es.status() == 1) {
/* 133 */           Frame frame = (Frame)es.nextElement();
/* 134 */           Set<String> exceptionMatchStrings = new HashSet();
/* 135 */           LinkedHashMap<LSDVariable, String> exception = new LinkedHashMap();
/* 136 */           LSDRule newRule = null;
/* 137 */           for (RBVariable matchedVar : frame.keySet()) {
/* 138 */             RBTerm term = frame.get(matchedVar);
/* 139 */             String constant = "\"" + term.toString() + "\"";
/* 140 */             LSDVariable toReplace = null;
/*     */             
/*     */ 
/* 143 */             for (LSDVariable freeVar : new LinkedHashSet(freeVars)) {
/* 144 */               if (freeVar != null)
/*     */               {
/* 146 */                 if (freeVar.toString().equals(matchedVar.toString())) {
/* 147 */                   exceptionMatchStrings.add(freeVar.toString() + constant);
/* 148 */                   toReplace = freeVar;
/*     */                 } }
/*     */             }
/* 151 */             if (toReplace != null)
/*     */             {
/* 153 */               exception.put(toReplace, constant);
/* 154 */               newRule = (newRule == null ? substitute : newRule)
/* 155 */                 .substitute(toReplace, new LSDBinding(constant));
/*     */             }
/*     */           }
/*     */           
/* 159 */           if (!exceptionMatches.contains(exceptionMatchStrings)) {
/* 160 */             exceptions.add(exception);
/* 161 */             exceptionMatches.add(exceptionMatchStrings);
/*     */           }
/* 163 */           if ((newRule != null) && 
/* 164 */             (!foundConclusionMatches.contains(newRule.toString()))) {
/* 165 */             newSubstitutedRules.add(newRule);
/* 166 */             foundConclusionMatches.add(newRule.toString());
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 171 */         e.printStackTrace();
/*     */       } catch (Error e) {
/* 173 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 179 */       if (!returnConclusions) {
/*     */         break label484;
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 177 */       e.printStackTrace();
/*     */     }
/*     */     
/* 180 */     return newSubstitutedRules;
/*     */     label484:
/* 182 */     return exceptions;
/*     */   }
/*     */   
/*     */   public List<LSDFact> get2KBMatches(LSDRule rule) {
/* 186 */     LSDRule substitute = rule;
/* 187 */     String query = rule.convertAllToAntecedents().toTyrubaQuery(false);
/* 188 */     ArrayList<LSDRule> newSubstitutedRules = new ArrayList();
/* 189 */     ArrayList<LSDVariable> freeVars = rule.getFreeVariables();
/*     */     RBVariable matchedVar;
/*     */     try
/*     */     {
/* 193 */       RBExpression exp = this.frontend.makeExpression(query);
/* 194 */       ElementSource es = this.frontend.frameQuery(exp);
/*     */       try {
/* 196 */         if (es.status() == -1)
/*     */         {
/* 198 */           return new ArrayList();
/*     */         }
/*     */         
/*     */ 
/* 202 */         while (es.status() == 1) {
/* 203 */           Frame frame = (Frame)es.nextElement();
/* 204 */           LSDRule newRule = null;
/* 205 */           for (Iterator localIterator1 = frame.keySet().iterator(); localIterator1.hasNext();) { matchedVar = (RBVariable)localIterator1.next();
/* 206 */             RBTerm term = frame.get(matchedVar);
/* 207 */             String constant = "\"" + term.toString() + "\"";
/* 208 */             LSDVariable toReplace = null;
/*     */             
/* 210 */             for (LSDVariable freeVar : new LinkedHashSet(freeVars)) {
/* 211 */               if (freeVar != null)
/*     */               {
/* 213 */                 if (freeVar.toString().equals(matchedVar.toString()))
/* 214 */                   toReplace = freeVar;
/*     */               }
/*     */             }
/* 217 */             if (toReplace != null)
/*     */             {
/* 219 */               newRule = 
/* 220 */                 (newRule == null ? substitute : newRule).substitute(toReplace, new LSDBinding(constant));
/*     */             }
/*     */           }
/* 223 */           if (newRule != null) {
/* 224 */             newSubstitutedRules.add(newRule);
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 228 */         e.printStackTrace();
/*     */       } catch (Error e) {
/* 230 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 236 */       foundFacts = new ArrayList();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 234 */       e.printStackTrace();
/*     */     }
/*     */     List<LSDFact> foundFacts;
/* 237 */     for (e = newSubstitutedRules.iterator(); e.hasNext(); 
/* 238 */         matchedVar.hasNext())
/*     */     {
/* 237 */       LSDRule r = (LSDRule)e.next();
/* 238 */       matchedVar = r.getLiterals().iterator(); continue;LSDLiteral literal = (LSDLiteral)matchedVar.next();
/* 239 */       if (((literal instanceof LSDFact)) && (literal.getPredicate().is2KBPredicate()) && (!foundFacts.contains((LSDFact)literal))) {
/* 240 */         foundFacts.add((LSDFact)literal);
/*     */       }
/*     */     }
/* 243 */     return foundFacts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int countMatches(String query, List<LSDVariable> freeVars, int max)
/*     */   {
/* 250 */     Set<Set<String>> matches = new HashSet();
/*     */     try {
/* 252 */       RBExpression exp = this.frontend.makeExpression(query);
/* 253 */       ElementSource es = this.frontend.frameQuery(exp);
/* 254 */       if (es.status() == -1)
/* 255 */         return 0;
/*     */       do {
/* 257 */         Frame frame = (Frame)es.nextElement();
/* 258 */         Set<String> matchStrings = new HashSet();
/* 259 */         Iterator localIterator2; for (Iterator localIterator1 = frame.keySet().iterator(); localIterator1.hasNext(); 
/*     */             
/*     */ 
/*     */ 
/* 263 */             localIterator2.hasNext())
/*     */         {
/* 259 */           RBVariable matchedVar = (RBVariable)localIterator1.next();
/* 260 */           RBTerm term = frame.get(matchedVar);
/* 261 */           String constant = "\"" + term.toString() + "\"";
/*     */           
/* 263 */           localIterator2 = new LinkedHashSet(freeVars).iterator(); continue;LSDVariable freeVar = (LSDVariable)localIterator2.next();
/* 264 */           if (freeVar.toString().equals(matchedVar.toString())) {
/* 265 */             matchStrings.add(freeVar.toString() + constant);
/*     */           }
/*     */         }
/* 268 */         matches.add(matchStrings);
/* 256 */         if (es.status() != 1) break; } while ((max == 0) || (matches.size() < max));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 271 */       e.printStackTrace();
/*     */     }
/* 273 */     return matches.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 280 */   public int countTrueConclusions(LSDRule rule) { return countTrueConclusions(rule, 0); }
/*     */   
/*     */   public int countTrueConclusions(LSDRule rule, int max) {
/* 283 */     String query = rule.convertAllToAntecedents().toTyrubaQuery(false);
/* 284 */     List<LSDVariable> freeVars = rule.getConclusions().getFreeVariables();
/* 285 */     return countMatches(query, freeVars, max);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */   public int countCounterExamples(LSDRule rule) { return countCounterExamples(rule, 0); }
/*     */   
/*     */   public int countCounterExamples(LSDRule rule, int max) {
/* 295 */     String query = rule.toTyrubaQuery(false);
/* 296 */     List<LSDVariable> freeVars = rule.getConclusions().getFreeVariables();
/* 297 */     return countMatches(query, freeVars, max);
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> getReplacementConstants(LSDRule rule, LSDVariable match)
/*     */   {
/* 303 */     assert (rule.getFreeVariables().contains(match));
/* 304 */     String query = rule.convertAllToAntecedents().toTyrubaQuery(false);
/* 305 */     replacements = new LinkedHashSet();
/*     */     try
/*     */     {
/* 308 */       RBExpression exp = this.frontend.makeExpression(query);
/* 309 */       ElementSource es = this.frontend.frameQuery(exp);
/*     */       try {
/* 311 */         if (es.status() == -1) {
/* 312 */           return replacements;
/*     */         }
/*     */         
/*     */ 
/* 316 */         while (es.status() == 1) {
/* 317 */           Frame frame = (Frame)es.nextElement();
/* 318 */           for (RBVariable matchedVar : frame.keySet()) {
/* 319 */             if (matchedVar.toString().equals(match.toString())) {
/* 320 */               RBTerm term = frame.get(matchedVar);
/* 321 */               if (term == null) break;
/* 322 */               replacements.add("\"" + term.toString() + "\"");
/* 323 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception localException1) {}catch (Error localError) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 334 */       return replacements;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 332 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 339 */     this.frontend.shutdown();
/* 340 */     this.frontend.crash();
/*     */   }
/*     */   
/*     */   public void loadRelatedFacts(ArrayList<LSDFact> facts, ArrayList<String> typeNames) throws Exception {
/* 344 */     loadAdditionalDB(MetaInfo.included2kb);
/* 345 */     String line = null;
/* 346 */     Iterator localIterator2; for (Iterator localIterator1 = facts.iterator(); localIterator1.hasNext(); 
/*     */         
/* 348 */         localIterator2.hasNext())
/*     */     {
/* 346 */       LSDFact fact = (LSDFact)localIterator1.next();
/* 347 */       line = fact.toString() + ".";
/* 348 */       localIterator2 = typeNames.iterator(); continue;String str = (String)localIterator2.next();
/* 349 */       if (line.contains(str)) {
/* 350 */         loadFact(LSDTyrubaFactReader.parseTyrubaFact(line));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/io/LSDTyrubaRuleChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */