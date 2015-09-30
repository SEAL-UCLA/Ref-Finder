/*     */ package lsd.rule;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class LSDLiteral
/*     */ {
/*     */   private boolean nonNegated;
/*     */   protected final LSDPredicate predicate;
/*     */   protected final ArrayList<LSDBinding> bindings;
/*     */   
/*     */   public LSDLiteral(LSDPredicate pred, List<LSDBinding> bindings, boolean nonNegated)
/*     */     throws LSDInvalidTypeException
/*     */   {
/*  18 */     this.nonNegated = nonNegated;
/*     */     
/*     */ 
/*  21 */     if (pred.arity() != bindings.size()) {
/*  22 */       this.predicate = null;
/*  23 */       this.bindings = null; return;
/*     */     }
/*     */     
/*     */     ArrayList<LSDBinding> ALBindings;
/*     */     
/*     */     ArrayList<LSDBinding> ALBindings;
/*     */     
/*  30 */     if ((bindings instanceof ArrayList)) {
/*  31 */       ALBindings = (ArrayList)bindings;
/*     */     } else
/*  33 */       ALBindings = new ArrayList(bindings);
/*  34 */     if (!pred.typeChecks(ALBindings)) {
/*  35 */       throw new LSDInvalidTypeException();
/*     */     }
/*  37 */     this.predicate = pred;
/*  38 */     this.bindings = ALBindings;
/*     */   }
/*     */   
/*     */   public LSDLiteral nonNegatedCopy() {
/*     */     try {
/*  43 */       return new LSDLiteral(this.predicate, this.bindings, true);
/*     */     } catch (LSDInvalidTypeException e) {
/*  45 */       System.err.println("We're creating a non-negated copy of our valid self.  This can't happen..");
/*  46 */       System.exit(1); }
/*  47 */     return null;
/*     */   }
/*     */   
/*     */   public LSDLiteral negatedCopy() {
/*     */     try {
/*  52 */       return new LSDLiteral(this.predicate, this.bindings, false);
/*     */     } catch (LSDInvalidTypeException e) {
/*  54 */       System.err.println("We're creating a negated copy of our valid self.  This can't happen..");
/*  55 */       System.exit(1); }
/*  56 */     return null;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  61 */     StringBuilder bs = new StringBuilder();
/*  62 */     for (int i = 0; i < this.bindings.size(); i++) {
/*  63 */       if (i >= 1) {
/*  64 */         bs.append(",");
/*     */       }
/*  66 */       bs.append(((LSDBinding)this.bindings.get(i)).toString());
/*     */     }
/*     */     
/*  69 */     return 
/*  70 */       (this.nonNegated ? "" : "!") + this.predicate.getDisplayName() + "(" + bs.toString() + ")";
/*     */   }
/*     */   
/*     */   public String toTyrubaString(Hashtable<LSDVariable, Integer> freeVarCount) {
/*  74 */     String output = "";
/*  75 */     for (int i = 0; i < this.bindings.size(); i++) {
/*  76 */       if (i >= 1) {
/*  77 */         output = output + ",";
/*     */       }
/*  79 */       output = output + ((LSDBinding)this.bindings.get(i)).toString();
/*     */     }
/*  81 */     output = this.predicate.getName() + "(" + output + ")";
/*     */     
/*     */ 
/*  84 */     if (this.nonNegated) {
/*  85 */       boolean quantified = false;
/*  86 */       for (int i = 0; i < this.bindings.size(); i++) {
/*  87 */         LSDVariable var = ((LSDBinding)this.bindings.get(i)).getVariable();
/*  88 */         if ((var != null) && (((Integer)freeVarCount.get(var)).intValue() == 1)) {
/*  89 */           output = 
/*  90 */             var.toString() + (output.charAt(0) == '?' ? ", " : " : ") + output;
/*  91 */           quantified = true;
/*     */         }
/*     */       }
/*  94 */       if (quantified)
/*  95 */         output = "EXISTS " + output;
/*  96 */       output = "NOT(" + output + ")";
/*     */     }
/*  98 */     return output;
/*     */   }
/*     */   
/*     */   public LSDLiteral substitute(LSDVariable toReplace, LSDBinding replacement)
/*     */     throws LSDInvalidTypeException
/*     */   {
/* 104 */     ArrayList<LSDBinding> newbs = new ArrayList();
/* 105 */     boolean freeVariables = false;
/* 106 */     LSDBinding nb; for (LSDBinding oldBinding : this.bindings) {
/* 107 */       nb = oldBinding.substitute(toReplace, 
/* 108 */         replacement);
/* 109 */       newbs.add(nb);
/* 110 */       if (!nb.isBound())
/* 111 */         freeVariables = true;
/*     */     }
/* 113 */     if (freeVariables) {
/* 114 */       return new LSDLiteral(this.predicate, newbs, this.nonNegated);
/*     */     }
/* 116 */     List<String> binds = new ArrayList();
/* 117 */     for (LSDBinding binding : newbs) {
/* 118 */       binds.add(binding.toString());
/*     */     }
/* 120 */     return LSDFact.createLSDFact(this.predicate, binds, this.nonNegated);
/*     */   }
/*     */   
/*     */   public ArrayList<LSDVariable> freeVars()
/*     */   {
/* 125 */     ArrayList<LSDVariable> freeVars = new ArrayList();
/* 126 */     for (int i = 0; i < this.bindings.size(); i++) {
/* 127 */       if (!((LSDBinding)this.bindings.get(i)).isBound())
/* 128 */         freeVars.add(((LSDBinding)this.bindings.get(i)).getVariable());
/*     */     }
/* 130 */     return freeVars;
/*     */   }
/*     */   
/*     */   public boolean isNegated()
/*     */   {
/* 135 */     return !this.nonNegated;
/*     */   }
/*     */   
/*     */   public LSDPredicate getPredicate() {
/* 139 */     return this.predicate;
/*     */   }
/*     */   
/*     */   public List<LSDBinding> getBindings() {
/* 143 */     return new ArrayList(this.bindings);
/*     */   }
/*     */   
/*     */   public boolean equalsIgnoringNegation(Object other) {
/* 147 */     if (!(other instanceof LSDLiteral))
/* 148 */       return false;
/* 149 */     LSDLiteral otherLit = (LSDLiteral)other;
/* 150 */     if (!this.predicate.equalsIgnoringPrimes(otherLit.predicate))
/* 151 */       return false;
/* 152 */     if (this.bindings.size() != otherLit.bindings.size())
/* 153 */       return false;
/* 154 */     for (int i = 0; i < this.bindings.size(); i++) {
/* 155 */       if (!((LSDBinding)this.bindings.get(i)).equals(otherLit.bindings.get(i)))
/* 156 */         return false;
/*     */     }
/* 158 */     return true;
/*     */   }
/*     */   
/*     */   public boolean identifiesSameIgnoringNegation(Object other) {
/* 162 */     if (!(other instanceof LSDLiteral))
/* 163 */       return false;
/* 164 */     LSDLiteral otherLit = (LSDLiteral)other;
/* 165 */     if (!this.predicate.equalsIgnoringPrimes(otherLit.predicate))
/* 166 */       return false;
/* 167 */     List<List<LSDBinding>> thisBindingsLists = getPrimaryBindings();
/* 168 */     List<List<LSDBinding>> otherBindingsLists = otherLit.getPrimaryBindings();
/*     */     
/* 170 */     if (thisBindingsLists.size() != otherBindingsLists.size())
/* 171 */       return false;
/* 172 */     boolean anyMatch = false;
/* 173 */     for (int i = 0; i < thisBindingsLists.size(); i++) {
/* 174 */       boolean thisMatches = true;
/* 175 */       List<LSDBinding> thisBindings = (List)thisBindingsLists.get(i);
/* 176 */       List<LSDBinding> otherBindings = (List)otherBindingsLists.get(i);
/* 177 */       if (thisBindings.size() == otherBindings.size())
/*     */       {
/* 179 */         for (int j = 0; j < thisBindings.size(); j++) {
/* 180 */           if (!((LSDBinding)thisBindings.get(j)).equals(otherBindings.get(j)))
/* 181 */             thisMatches = false;
/*     */         }
/* 183 */         if (thisMatches)
/* 184 */           anyMatch = true;
/*     */       }
/*     */     }
/* 187 */     return anyMatch;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 191 */     if (!(other instanceof LSDLiteral))
/* 192 */       return false;
/* 193 */     LSDLiteral otherLit = (LSDLiteral)other;
/* 194 */     if (this.nonNegated != otherLit.nonNegated)
/* 195 */       return false;
/* 196 */     return equalsIgnoringNegation(otherLit);
/*     */   }
/*     */   
/*     */   public static LSDLiteral createDefaultLiteral(LSDPredicate predicate, boolean nonNegated) {
/* 200 */     ArrayList<LSDBinding> bindings = new ArrayList();
/* 201 */     char[] types = predicate.getTypes();
/*     */     
/* 203 */     for (int i = 0; i < types.length; i++) {
/* 204 */       String fvName = types[i] + i;
/* 205 */       LSDVariable fv = new LSDVariable(fvName, types[i]);
/* 206 */       LSDBinding binding = new LSDBinding(fv);
/*     */       
/* 208 */       bindings.add(binding);
/*     */     }
/*     */     try {
/* 211 */       return new LSDLiteral(predicate, bindings, nonNegated);
/*     */     }
/*     */     catch (LSDInvalidTypeException e) {}
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasSamePred(LSDLiteral other)
/*     */   {
/* 221 */     return this.predicate.getSuffix().equals(other.predicate.getSuffix());
/*     */   }
/*     */   
/*     */ 
/*     */   public List<List<LSDBinding>> getPrimaryBindings()
/*     */   {
/* 227 */     int[][] primaryArguments = this.predicate.getPrimaryArguments();
/* 228 */     List<List<LSDBinding>> primaryBindings = new ArrayList();
/* 229 */     int[][] arrayOfInt1; int j = (arrayOfInt1 = primaryArguments).length; for (int i = 0; i < j; i++) { int[] argumentSet = arrayOfInt1[i];
/* 230 */       List<LSDBinding> primaryBindingSet = new ArrayList();
/* 231 */       int[] arrayOfInt2; int m = (arrayOfInt2 = argumentSet).length; for (int k = 0; k < m; k++) { int argument = arrayOfInt2[k];
/* 232 */         assert (argument < this.bindings.size());
/* 233 */         primaryBindingSet.add((LSDBinding)this.bindings.get(argument));
/*     */       }
/* 235 */       primaryBindings.add(primaryBindingSet);
/*     */     }
/* 237 */     return primaryBindings;
/*     */   }
/*     */   
/* 240 */   public char[] getPrimaryTypes() { return getPredicate().getPrimaryTypes(); }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 245 */     LSDPredicate foo = LSDPredicate.getPredicate("added_inheritedMethod");
/* 246 */     ArrayList<LSDBinding> bindings = new ArrayList();
/* 247 */     LSDBinding binding = new LSDBinding(new LSDVariable("a", 'm'));
/* 248 */     bindings.add(binding);
/* 249 */     binding = new LSDBinding(new LSDVariable("b", 't'));
/* 250 */     bindings.add(binding);
/* 251 */     binding = new LSDBinding(new LSDVariable("c", 't'));
/* 252 */     bindings.add(binding);
/*     */     try {
/* 254 */       LSDLiteral bar = new LSDLiteral(foo, bindings, false);
/* 255 */       System.out.println(bar);
/* 256 */       bar = bar
/* 257 */         .substitute(new LSDVariable("a", 'm'), new LSDBinding("X"));
/* 258 */       bar = bar
/* 259 */         .substitute(new LSDVariable("b", 't'), new LSDBinding("X"));
/* 260 */       bar = bar
/* 261 */         .substitute(new LSDVariable("c", 't'), new LSDBinding("X"));
/* 262 */       bar.nonNegated = false;
/* 263 */       assert ((bar instanceof LSDFact));
/* 264 */       System.out.println(bar);
/*     */     } catch (Exception e) {
/* 266 */       System.out.println(e.toString());
/*     */     }
/*     */     
/* 269 */     System.out.println("Literal tests succeeded.");
/*     */   }
/*     */   
/*     */   public int getBindingScore(LSDBinding binding)
/*     */   {
/* 274 */     int i = 0;
/* 275 */     if (this.predicate.getSuffix().equalsIgnoreCase("type")) {
/* 276 */       if (getLocation(binding) == 3) {
/* 277 */         i++;
/*     */       }
/* 279 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("field")) {
/* 280 */       if (getLocation(binding) == 3) {
/* 281 */         i++;
/*     */       }
/* 283 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("method")) {
/* 284 */       if (getLocation(binding) == 3) {
/* 285 */         i++;
/*     */       }
/* 287 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("subtype"))
/*     */     {
/* 289 */       i += 2;
/* 290 */       if (getLocation(binding) == 1) {
/* 291 */         i++;
/*     */       }
/* 293 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("accesses")) {
/* 294 */       i++;
/* 295 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("calls")) {
/* 296 */       i++;
/* 297 */     } else if (this.predicate.getSuffix().equalsIgnoreCase("dependency")) {
/* 298 */       i += 2; }
/* 299 */     return i;
/*     */   }
/*     */   
/* 302 */   private int getLocation(LSDBinding binding) { String literal = toString();
/* 303 */     literal = literal.substring(literal.indexOf("(") + 1, literal.length() - 1);
/* 304 */     StringTokenizer tokenizer = new StringTokenizer(literal, ",");
/* 305 */     int i = 1;
/* 306 */     String temp = null;
/* 307 */     while (tokenizer.hasMoreTokens()) {
/* 308 */       temp = tokenizer.nextToken();
/* 309 */       if (!temp.startsWith("?"))
/*     */       {
/* 311 */         temp = temp.substring(1, temp.length() - 1);
/* 312 */         if (binding.getGroundConst().equalsIgnoreCase(temp))
/* 313 */           return i;
/*     */       }
/* 315 */       i++;
/*     */     }
/* 317 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isConclusion() {
/* 321 */     return this.predicate.isConclusionPredicate();
/*     */   }
/*     */   
/*     */   public boolean isDependency() {
/* 325 */     return this.predicate.isDependencyPredicate();
/*     */   }
/*     */   
/*     */   public List<LSDLiteral> getCompatibles() {
/* 329 */     ArrayList<LSDLiteral> newliterals = new ArrayList();
/* 330 */     for (LSDPredicate pred : this.predicate.getMethodLevelDependency()) {
/* 331 */       ArrayList<LSDBinding> bindings = new ArrayList();
/* 332 */       int i = 0;
/* 333 */       char[] types = pred.getTypes();
/* 334 */       for (LSDBinding oldBinding : getBindings()) {
/* 335 */         bindings.add(new LSDBinding(new LSDVariable("t" + i, types[i])));
/* 336 */         i++;
/*     */       }
/* 338 */       pred.updateBindings(bindings);
/*     */       try {
/* 340 */         newliterals.add(new LSDLiteral(pred, bindings, !isNegated()));
/*     */       }
/*     */       catch (LSDInvalidTypeException e) {
/* 343 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 346 */     return newliterals;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/rule/LSDLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */