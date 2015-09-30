/*     */ package lsd.rule;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lsd.io.LSDAlchemyRuleReader;
/*     */ 
/*     */ public class LSDRule
/*     */ {
/*  17 */   private double score = 0.0D;
/*  18 */   private int numMatches = 0;
/*  19 */   private double accuracy = 0.0D;
/*  20 */   double numDeltaFacts = 0.0D;
/*     */   
/*  22 */   private ArrayList<LSDLiteral> literals = new ArrayList();
/*     */   
/*  24 */   private Set<LSDVariable> freeVars = new HashSet();
/*     */   
/*  26 */   static { penaltyLookup = new HashMap();
/*     */     
/*     */ 
/*  29 */     penaltyLookup.put(Character.valueOf('p'), Integer.valueOf(1));
/*  30 */     penaltyLookup.put(Character.valueOf('t'), Integer.valueOf(2));
/*  31 */     penaltyLookup.put(Character.valueOf('m'), Integer.valueOf(3));
/*  32 */     penaltyLookup.put(Character.valueOf('f'), Integer.valueOf(3));
/*  33 */     penaltyLookup.put(Character.valueOf('a'), Integer.valueOf(4));
/*  34 */     penaltyLookup.put(Character.valueOf('b'), Integer.valueOf(4));
/*  35 */     penaltyLookup.put(Character.valueOf('c'), Integer.valueOf(4));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static HashMap<Character, Integer> penaltyLookup;
/*     */   
/*     */ 
/*     */ 
/*     */   public LSDRule() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LSDRule(LSDRule oldRule)
/*     */   {
/*  53 */     for (LSDLiteral literal : oldRule.literals) {
/*  54 */       addLiteral(literal);
/*     */     }
/*     */   }
/*     */   
/*     */   public LSDRule(LSDRule rule, boolean b)
/*     */   {
/*  60 */     for (LSDLiteral literal : rule.literals) {
/*  61 */       ArrayList<LSDBinding> newBindings = new ArrayList();
/*  62 */       for (LSDBinding binding : literal.getBindings()) {
/*  63 */         LSDBinding newBinding = new LSDBinding(new LSDVariable(binding.getVariable().getName(), binding.getType()));
/*  64 */         newBindings.add(newBinding);
/*     */       }
/*     */       try {
/*  67 */         LSDLiteral newLiteral = new LSDLiteral(literal.predicate, newBindings, !literal.isNegated());
/*  68 */         addLiteral(newLiteral);
/*     */       } catch (LSDInvalidTypeException e) {
/*  70 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<LSDVariable> getFreeVariables()
/*     */   {
/*  77 */     ArrayList<LSDVariable> fv = new ArrayList(this.freeVars);
/*  78 */     return fv;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean addLiteral(LSDLiteral newLiteral)
/*     */   {
/*  84 */     Collection<LSDVariable> newFreeVars = newLiteral.freeVars();
/*  85 */     this.literals.add(newLiteral);
/*  86 */     this.freeVars.addAll(newFreeVars);
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public ArrayList<LSDLiteral> getLiterals() {
/*  91 */     return new ArrayList(this.literals);
/*     */   }
/*     */   
/*     */   public LSDRule convertAllToAntecedents() {
/*  95 */     LSDRule antecedents = new LSDRule();
/*  96 */     for (LSDLiteral literal : this.literals) {
/*  97 */       if (literal.isNegated()) {
/*  98 */         antecedents.addLiteral(literal);
/*     */       } else
/* 100 */         antecedents.addLiteral(literal.negatedCopy());
/*     */     }
/* 102 */     return antecedents;
/*     */   }
/*     */   
/*     */   public LSDRule getAntecedents() {
/* 106 */     LSDRule antecedents = new LSDRule();
/* 107 */     for (LSDLiteral literal : this.literals) {
/* 108 */       if (literal.isNegated())
/* 109 */         antecedents.addLiteral(literal);
/*     */     }
/* 111 */     return antecedents;
/*     */   }
/*     */   
/*     */   public LSDRule getConclusions() {
/* 115 */     LSDRule conclusions = new LSDRule();
/* 116 */     for (LSDLiteral literal : this.literals) {
/* 117 */       if (!literal.isNegated())
/* 118 */         conclusions.addLiteral(literal);
/*     */     }
/* 120 */     return conclusions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean literalsLinked()
/*     */   {
/* 127 */     Map<LSDVariable, Integer> freeVarCount = new HashMap();
/* 128 */     Iterator localIterator2; for (Iterator localIterator1 = this.literals.iterator(); localIterator1.hasNext(); 
/* 129 */         localIterator2.hasNext())
/*     */     {
/* 128 */       LSDLiteral l = (LSDLiteral)localIterator1.next();
/* 129 */       localIterator2 = l.freeVars().iterator(); continue;LSDVariable v = (LSDVariable)localIterator2.next();
/* 130 */       if (freeVarCount.get(v) == null) {
/* 131 */         freeVarCount.put(v, Integer.valueOf(1));
/*     */       } else {
/* 133 */         freeVarCount.put(v, Integer.valueOf(((Integer)freeVarCount.get(v)).intValue() + 1));
/*     */       }
/*     */     }
/* 136 */     for (LSDLiteral l : this.literals) {
/* 137 */       boolean invalid = true;
/* 138 */       for (LSDVariable v : l.freeVars()) {
/* 139 */         if (((Integer)freeVarCount.get(v)).intValue() > 1)
/*     */         {
/*     */ 
/* 142 */           invalid = false;
/*     */           
/* 144 */           break;
/*     */         }
/*     */       }
/* 147 */       if (invalid)
/* 148 */         return false;
/*     */     }
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasValidLinks() { LSDLiteral l;
/*     */     int i;
/* 155 */     for (Iterator localIterator = this.literals.iterator(); localIterator.hasNext(); 
/* 156 */         i < l.bindings.size())
/*     */     {
/* 155 */       l = (LSDLiteral)localIterator.next();
/* 156 */       i = 0; continue;
/* 157 */       LSDBinding temp = (LSDBinding)l.bindings.get(i);
/* 158 */       if (!temp.isBound()) {
/* 159 */         for (int j = i + 1; j < l.bindings.size(); j++) {
/* 160 */           if (temp.getVariable() == ((LSDBinding)l.bindings.get(j)).getVariable()) {
/* 161 */             return false;
/*     */           }
/*     */         }
/*     */       }
/* 156 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSamePreds()
/*     */   {
/* 171 */     ArrayList<LSDLiteral> conclusions = getConclusions().getLiterals();
/* 172 */     for (LSDLiteral conc : conclusions) {
/* 173 */       boolean allDuplicate = true;
/* 174 */       for (LSDLiteral literal : getAntecedents().getLiterals()) {
/* 175 */         if (!literal.getPredicate().getSuffix().equalsIgnoreCase(conc.getPredicate().getSuffix()))
/* 176 */           allDuplicate = false;
/*     */       }
/* 178 */       if (allDuplicate)
/* 179 */         return true;
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isValid()
/*     */   {
/* 187 */     if ((!isHornClause()) || (!typeChecks())) {
/* 188 */       return false;
/*     */     }
/* 190 */     if (!literalsLinked()) {
/* 191 */       return false;
/*     */     }
/* 193 */     Set<LSDVariable> antecedentVars = new HashSet(getAntecedents().getFreeVariables());
/* 194 */     for (LSDLiteral literal : getConclusions().getLiterals()) {
/* 195 */       boolean primaryMatched = false;
/* 196 */       for (List<LSDBinding> bindingSet : literal.getPrimaryBindings()) {
/* 197 */         boolean anyUnmatched = false;
/* 198 */         for (LSDBinding binding : bindingSet) {
/* 199 */           if ((!binding.isBound()) && (!antecedentVars.contains(binding.getVariable()))) {
/* 200 */             anyUnmatched = true;
/* 201 */             break;
/*     */           }
/*     */         }
/* 204 */         if (!anyUnmatched) {
/* 205 */           primaryMatched = true;
/* 206 */           break;
/*     */         }
/*     */       }
/* 209 */       if (!primaryMatched) {
/* 210 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */     return true;
/*     */   }
/*     */   
/*     */   public boolean containsFacts()
/*     */   {
/* 223 */     for (LSDLiteral literal : this.literals) {
/* 224 */       if ((literal instanceof LSDFact))
/* 225 */         return true;
/*     */     }
/* 227 */     return false;
/*     */   }
/*     */   
/*     */   public LSDRule substitute(LSDVariable toReplace, LSDBinding replacement)
/*     */     throws LSDInvalidTypeException
/*     */   {
/* 233 */     LSDRule newRule = new LSDRule();
/* 234 */     for (LSDLiteral literal : this.literals) {
/* 235 */       newRule.addLiteral(literal.substitute(toReplace, replacement));
/*     */     }
/* 237 */     return newRule;
/*     */   }
/*     */   
/*     */   public boolean typeChecks() {
/*     */     Iterator localIterator2;
/* 242 */     for (Iterator localIterator1 = this.freeVars.iterator(); localIterator1.hasNext(); 
/* 243 */         localIterator2.hasNext())
/*     */     {
/* 242 */       LSDVariable fv_i = (LSDVariable)localIterator1.next();
/* 243 */       localIterator2 = this.freeVars.iterator(); continue;LSDVariable fv_j = (LSDVariable)localIterator2.next();
/* 244 */       if (fv_i.typeConflicts(fv_j)) {
/* 245 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isHornClause() {
/* 253 */     int nonNegatedLiterals = 0;
/* 254 */     for (LSDLiteral literal : this.literals) {
/* 255 */       if (!literal.isNegated())
/* 256 */         nonNegatedLiterals++;
/*     */     }
/* 258 */     return nonNegatedLiterals == 1;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 262 */     StringBuilder output = new StringBuilder();
/*     */     
/* 264 */     for (LSDLiteral literal : this.literals)
/* 265 */       if (literal.isNegated())
/*     */       {
/* 267 */         if (output.length() != 0)
/* 268 */           output.append(" ^ ");
/* 269 */         output.append(literal.nonNegatedCopy().toString());
/*     */       }
/* 271 */     output.append(" => ");
/* 272 */     boolean first = true;
/*     */     
/* 274 */     for (LSDLiteral literal : this.literals)
/* 275 */       if (!literal.isNegated())
/*     */       {
/* 277 */         if (!first)
/* 278 */           output.append(" ^ ");
/* 279 */         output.append(literal.nonNegatedCopy().toString());
/* 280 */         first = false;
/*     */       }
/* 282 */     return output.toString();
/*     */   }
/*     */   
/*     */   public String toTyrubaQuery(boolean commandLine) {
/* 286 */     StringBuilder output = new StringBuilder();
/* 287 */     if (commandLine)
/* 288 */       output.append(":-");
/* 289 */     Hashtable<LSDVariable, Integer> freeVarCount = new Hashtable();
/* 290 */     Iterator localIterator2; for (Iterator localIterator1 = this.literals.iterator(); localIterator1.hasNext(); 
/* 291 */         localIterator2.hasNext())
/*     */     {
/* 290 */       LSDLiteral l = (LSDLiteral)localIterator1.next();
/* 291 */       localIterator2 = l.freeVars().iterator(); continue;LSDVariable v = (LSDVariable)localIterator2.next();
/* 292 */       if (freeVarCount.get(v) == null) {
/* 293 */         freeVarCount.put(v, Integer.valueOf(1));
/*     */       } else {
/* 295 */         freeVarCount.put(v, Integer.valueOf(((Integer)freeVarCount.get(v)).intValue() + 1));
/*     */       }
/*     */     }
/* 298 */     for (int i = 0; i < this.literals.size(); i++) {
/* 299 */       if (i > 0) {
/* 300 */         output.append(",");
/*     */       }
/* 302 */       output.append(((LSDLiteral)this.literals.get(i)).toTyrubaString(freeVarCount));
/*     */     }
/* 304 */     return output.toString() + (commandLine ? "." : "");
/*     */   }
/*     */   
/*     */   private String canonicalRepresentation() {
/* 308 */     return canonicalRepresentation(getLiterals(), new HashMap(), 0);
/*     */   }
/*     */   
/*     */   private String canonicalRepresentation(List<LSDLiteral> literals, Map<LSDVariable, String> varMap, int nextVarNum) {
/* 312 */     if (literals.size() == 0)
/* 313 */       return "";
/* 314 */     List<LSDPredicate> predicates = LSDPredicate.getPredicates();
/* 315 */     int firstPredicateIndex = predicates.size();
/* 316 */     List<Integer> firstPredicateList = null;
/* 317 */     for (int i = 0; i < literals.size(); i++) {
/* 318 */       LSDLiteral literal = (LSDLiteral)literals.get(i);
/* 319 */       thisIndex = predicates.indexOf(literal.getPredicate());
/* 320 */       if ((thisIndex < firstPredicateIndex) && (thisIndex >= 0)) {
/* 321 */         firstPredicateIndex = thisIndex;
/* 322 */         firstPredicateList = new ArrayList();
/* 323 */         firstPredicateList.add(Integer.valueOf(i));
/*     */       }
/* 325 */       else if (thisIndex == firstPredicateIndex) {
/* 326 */         firstPredicateList.add(Integer.valueOf(i));
/*     */       } }
/* 328 */     String repr = null;
/* 329 */     for (int thisIndex = firstPredicateList.iterator(); thisIndex.hasNext();) { int index = ((Integer)thisIndex.next()).intValue();
/*     */       
/* 331 */       StringBuilder thisRepr = new StringBuilder();
/* 332 */       Map<LSDVariable, String> thisVarMap = new HashMap(varMap);
/* 333 */       int thisNextVarNum = nextVarNum;
/* 334 */       LSDLiteral literal = (LSDLiteral)literals.get(index);
/* 335 */       if (literal.isNegated())
/* 336 */         thisRepr.append("!");
/* 337 */       thisRepr.append(literal.getPredicate().getName());
/* 338 */       thisRepr.append("(");
/* 339 */       for (LSDBinding binding : literal.getBindings()) {
/* 340 */         if (binding.isBound()) {
/* 341 */           thisRepr.append(binding.toString());
/*     */         } else {
/* 343 */           LSDVariable variable = binding.getVariable();
/* 344 */           if (!thisVarMap.containsKey(variable)) {
/* 345 */             thisVarMap.put(variable, "?x" + thisNextVarNum);
/* 346 */             thisNextVarNum++;
/*     */           }
/* 348 */           thisRepr.append((String)thisVarMap.get(variable));
/*     */         }
/* 350 */         thisRepr.append(",");
/*     */       }
/* 352 */       thisRepr.append(")");
/* 353 */       List<LSDLiteral> newLiterals = new ArrayList(literals);
/* 354 */       newLiterals.remove(index);
/* 355 */       thisRepr.append(canonicalRepresentation(newLiterals, thisVarMap, thisNextVarNum));
/* 356 */       if ((repr == null) || (thisRepr.toString().compareTo(repr) < 0))
/* 357 */         repr = thisRepr.toString();
/*     */     }
/* 359 */     return repr;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 363 */     if (!(o instanceof LSDRule))
/* 364 */       return false;
/* 365 */     return canonicalRepresentation().equals(((LSDRule)o).canonicalRepresentation());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 369 */     return canonicalRepresentation().hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int generalityCompare(LSDRule r2)
/*     */   {
/* 377 */     int penalty1 = 0;
/* 378 */     int penalty2 = 0;
/* 379 */     for (LSDVariable var : getFreeVariables())
/* 380 */       penalty1 += ((Integer)penaltyLookup.get(Character.valueOf(var.getType()))).intValue();
/* 381 */     for (LSDVariable var : r2.getFreeVariables())
/* 382 */       penalty2 += ((Integer)penaltyLookup.get(Character.valueOf(var.getType()))).intValue();
/* 383 */     return penalty1 - penalty2;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 388 */     LSDPredicate foo = LSDPredicate.getPredicate("added_inheritedmethod");
/* 389 */     ArrayList<LSDBinding> bindings = new ArrayList();
/* 390 */     LSDVariable a = new LSDVariable("a", 'm');
/* 391 */     LSDBinding binding = new LSDBinding(a);
/* 392 */     bindings.add(binding);
/* 393 */     binding = new LSDBinding(new LSDVariable("b", 't'));
/* 394 */     bindings.add(binding);
/* 395 */     binding = new LSDBinding(new LSDVariable("c", 't'));
/* 396 */     bindings.add(binding);
/*     */     try {
/* 398 */       LSDLiteral bar = new LSDLiteral(foo, bindings, false);
/* 399 */       System.out.println(bar.freeVars());
/*     */       
/* 401 */       foo = LSDPredicate.getPredicate("deleted_accesses");
/* 402 */       bindings = new ArrayList();
/* 403 */       binding = new LSDBinding(new LSDVariable("d", 'f'));
/* 404 */       bindings.add(binding);
/* 405 */       binding = new LSDBinding(new LSDVariable("a", 'm'));
/* 406 */       bindings.add(binding);
/* 407 */       LSDLiteral baz = new LSDLiteral(foo, bindings, true);
/*     */       
/* 409 */       foo = LSDPredicate.getPredicate("deleted_accesses");
/* 410 */       bindings = new ArrayList();
/* 411 */       binding = new LSDBinding(new LSDVariable("r", 'f'));
/* 412 */       bindings.add(binding);
/* 413 */       binding = new LSDBinding(new LSDVariable("s", 'm'));
/* 414 */       bindings.add(binding);
/* 415 */       LSDLiteral quxx = new LSDLiteral(foo, bindings, true);
/*     */       
/* 417 */       LSDRule r = new LSDRule();
/* 418 */       assert (r.addLiteral(bar));
/* 419 */       System.out.println(r);
/* 420 */       assert (!r.isHornClause());
/* 421 */       assert (r.freeVars.contains(a));
/* 422 */       LSDVariable b = new LSDVariable("a", 'm');
/* 423 */       assert (a.hashCode() == b.hashCode());
/* 424 */       assert (a.equals(b));
/* 425 */       assert (a != b);
/* 426 */       assert (r.freeVars.contains(b));
/* 427 */       assert (a.equals(b));
/* 428 */       assert (r.addLiteral(baz));
/* 429 */       System.out.println(r);
/* 430 */       assert (r.isHornClause());
/* 431 */       assert (r.isValid());
/* 432 */       assert (r.addLiteral(quxx));
/* 433 */       System.out.println(r);
/* 434 */       if ((!$assertionsDisabled) && (r.isValid())) throw new AssertionError();
/*     */     } catch (Exception e) {
/* 436 */       System.out.println(e.toString());
/* 437 */       e.printStackTrace();
/*     */       
/* 439 */       System.out.println(LSDAlchemyRuleReader.parseAlchemyRule("before_typeintype(z, x) ^ before_typeintype(x, z) => added_type(x)").canonicalRepresentation());
/*     */       
/* 441 */       System.out.println("Rule tests succeeded.");
/*     */     }
/*     */   }
/*     */   
/*     */   public String[] getClassLevelGrounding() {
/* 446 */     HashSet<String> res = new HashSet();
/* 447 */     ArrayList<LSDLiteral> temp = getLiterals();
/*     */     Iterator localIterator2;
/* 449 */     for (Iterator localIterator1 = temp.iterator(); localIterator1.hasNext(); 
/*     */         
/* 451 */         localIterator2.hasNext())
/*     */     {
/* 449 */       LSDLiteral literal = (LSDLiteral)localIterator1.next();
/* 450 */       List<LSDBinding> bindings = literal.getBindings();
/* 451 */       localIterator2 = bindings.iterator(); continue;LSDBinding binding = (LSDBinding)localIterator2.next();
/* 452 */       if (binding.isBound()) {
/* 453 */         res.add(binding.getGroundConst());
/*     */       }
/*     */     }
/* 456 */     String[] results = new String[res.size()];
/* 457 */     int i = 0;
/* 458 */     for (String str : res) {
/* 459 */       results[(i++)] = str;
/*     */     }
/* 461 */     return results;
/*     */   }
/*     */   
/*     */   public double getScore() {
/* 465 */     return this.score;
/*     */   }
/*     */   
/*     */   public void setScore() {
/* 469 */     boolean hasLanguageBinding = false;
/* 470 */     int bindingScore = 0;
/* 471 */     Iterator localIterator2; for (Iterator localIterator1 = this.literals.iterator(); localIterator1.hasNext(); 
/* 472 */         localIterator2.hasNext())
/*     */     {
/* 471 */       LSDLiteral literal = (LSDLiteral)localIterator1.next();
/* 472 */       localIterator2 = literal.getBindings().iterator(); continue;LSDBinding binding = (LSDBinding)localIterator2.next();
/* 473 */       if (binding.getGroundConst() != null) {
/* 474 */         if ((binding.getGroundConst().startsWith("java")) && (!hasLanguageBinding))
/* 475 */           hasLanguageBinding = true;
/* 476 */         bindingScore += literal.getBindingScore(binding);
/*     */       }
/*     */     }
/*     */     
/* 480 */     this.score = (2 * bindingScore);
/* 481 */     this.score += this.accuracy;
/* 482 */     this.score += 2.0D * (this.numMatches / 250.0D);
/* 483 */     if (hasLanguageBinding) {
/* 484 */       this.score -= 2.0D;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNumMatches(int numMatches)
/*     */   {
/* 490 */     this.numMatches = numMatches;
/*     */   }
/*     */   
/*     */   public int getNumMatches() {
/* 494 */     return this.numMatches;
/*     */   }
/*     */   
/*     */   public void setAccuracy(double a) {
/* 498 */     this.accuracy = a;
/*     */   }
/*     */   
/*     */   public void removeFreeVar(LSDVariable variable) {
/* 502 */     this.freeVars.remove(variable);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/rule/LSDRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */