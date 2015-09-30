/*     */ package lsd.rule;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class LSDPredicate { public static int DELETED;
/*     */   public static int ADDED;
/*     */   public static int BEFORE;
/*     */   public static int AFTER;
/*     */   public static int MODIFIED;
/*     */   public static int DELETED_P; public static int ADDED_P;
/*  11 */   static { DELETED = 1;
/*  12 */     ADDED = 2;
/*  13 */     BEFORE = 3;
/*  14 */     AFTER = 4;
/*  15 */     MODIFIED = 5;
/*  16 */     DELETED_P = 6;
/*  17 */     ADDED_P = 7;
/*  18 */     MODIFIED_P = 8;
/*  19 */     UNDEFINED = 9;
/*     */     
/*  21 */     PACKAGELEVEL = 0;
/*  22 */     CLASSLEVEL = 1;
/*  23 */     METHODLEVEL = 2;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  29 */     newPredicates(); }
/*     */   
/*     */   public static int MODIFIED_P;
/*     */   public static int UNDEFINED; public static int PACKAGELEVEL;
/*  33 */   private static void newPredicates() { allowedPredicate = new java.util.HashMap();
/*     */     
/*  35 */     addAllowedPredicate("package", "p");
/*     */     
/*  37 */     addAllowedPredicate("type", "tap");
/*     */     
/*  39 */     addAllowedPredicate("field", "fbt");
/*     */     
/*  41 */     addAllowedPredicate("method", "mct");
/*     */     
/*  43 */     addAllowedPredicate("return", "mt");
/*  44 */     addAllowedPredicate("subtype", "tt");
/*     */     
/*  46 */     addAllowedPredicate("accesses", "fm");
/*  47 */     addAllowedPredicate("calls", "mm");
/*     */     
/*  49 */     addAllowedPredicate("inheritedfield", "btt");
/*     */     
/*  51 */     addAllowedPredicate("inheritedmethod", "ctt");
/*  52 */     addAllowedPredicate("fieldoftype", "ft");
/*     */     
/*     */ 
/*  55 */     addAllowedPredicate("typeintype", "tt");
/*     */     
/*     */ 
/*     */ 
/*  59 */     addAllowedPredicate("extends", "tt");
/*  60 */     addAllowedPredicate("implements", "tt");
/*     */     
/*  62 */     addAllowedPredicate("before_package", "p");
/*  63 */     addAllowedPredicate("before_type", "tap");
/*  64 */     addAllowedPredicate("before_field", "fbt");
/*  65 */     addAllowedPredicate("before_method", "mct");
/*     */     
/*  67 */     addAllowedPredicate("before_return", "mt");
/*  68 */     addAllowedPredicate("before_subtype", "tt");
/*  69 */     addAllowedPredicate("before_accesses", "fm");
/*  70 */     addAllowedPredicate("before_calls", "mm");
/*     */     
/*  72 */     addAllowedPredicate("before_inheritedfield", "btt");
/*  73 */     addAllowedPredicate("before_inheritedmethod", "ctt");
/*  74 */     addAllowedPredicate("before_fieldoftype", "ft");
/*  75 */     addAllowedPredicate("before_typeintype", "tt");
/*     */     
/*     */ 
/*  78 */     addAllowedPredicate("before_extends", "tt");
/*  79 */     addAllowedPredicate("before_implements", "tt");
/*     */     
/*  81 */     addAllowedPredicate("after_package", "p");
/*  82 */     addAllowedPredicate("after_type", "tap");
/*  83 */     addAllowedPredicate("after_field", "fbt");
/*  84 */     addAllowedPredicate("after_method", "mct");
/*     */     
/*  86 */     addAllowedPredicate("after_return", "mt");
/*  87 */     addAllowedPredicate("after_subtype", "tt");
/*  88 */     addAllowedPredicate("after_accesses", "fm");
/*  89 */     addAllowedPredicate("after_calls", "mm");
/*     */     
/*  91 */     addAllowedPredicate("after_inheritedfield", "btt");
/*  92 */     addAllowedPredicate("after_inheritedmethod", "ctt");
/*  93 */     addAllowedPredicate("after_fieldoftype", "ft");
/*  94 */     addAllowedPredicate("after_typeintype", "tt");
/*     */     
/*     */ 
/*  97 */     addAllowedPredicate("after_extends", "tt");
/*  98 */     addAllowedPredicate("after_implements", "tt");
/*     */     
/* 100 */     addAllowedPredicate("deleted_package", "p");
/* 101 */     addAllowedPredicate("deleted_type", "tap");
/* 102 */     addAllowedPredicate("deleted_field", "fbt");
/* 103 */     addAllowedPredicate("deleted_method", "mct");
/*     */     
/*     */ 
/* 106 */     addAllowedPredicate("deleted_return", "mt");
/* 107 */     addAllowedPredicate("deleted_subtype", "tt");
/* 108 */     addAllowedPredicate("deleted_accesses", "fm");
/* 109 */     addAllowedPredicate("deleted_calls", "mm");
/* 110 */     addAllowedPredicate("deleted_inheritedfield", "btt");
/* 111 */     addAllowedPredicate("deleted_inheritedmethod", "ctt");
/* 112 */     addAllowedPredicate("deleted_fieldoftype", "ft");
/* 113 */     addAllowedPredicate("deleted_typeintype", "tt");
/*     */     
/* 115 */     addAllowedPredicate("deleted_extends", "tt");
/* 116 */     addAllowedPredicate("deleted_implements", "tt");
/*     */     
/*     */ 
/* 119 */     addAllowedPredicate("added_package", "p");
/* 120 */     addAllowedPredicate("added_type", "tap");
/* 121 */     addAllowedPredicate("added_field", "fbt");
/* 122 */     addAllowedPredicate("added_method", "mct");
/*     */     
/* 124 */     addAllowedPredicate("added_return", "mt");
/* 125 */     addAllowedPredicate("added_subtype", "tt");
/* 126 */     addAllowedPredicate("added_accesses", "fm");
/* 127 */     addAllowedPredicate("added_calls", "mm");
/* 128 */     addAllowedPredicate("added_inheritedfield", "btt");
/* 129 */     addAllowedPredicate("added_inheritedmethod", "ctt");
/* 130 */     addAllowedPredicate("added_fieldoftype", "ft");
/* 131 */     addAllowedPredicate("added_typeintype", "tt");
/*     */     
/*     */ 
/* 134 */     addAllowedPredicate("added_extends", "tt");
/* 135 */     addAllowedPredicate("added_implements", "tt");
/*     */     
/* 137 */     addAllowedPredicate("modified_package", "p");
/* 138 */     addAllowedPredicate("modified_type", "tap");
/* 139 */     addAllowedPredicate("modified_method", "mct");
/* 140 */     addAllowedPredicate("modified_field", "fbt");
/*     */     
/*     */ 
/* 143 */     addAllowedPredicate("before_conditional", "hiet");
/* 144 */     addAllowedPredicate("after_conditional", "hiet");
/* 145 */     addAllowedPredicate("added_conditional", "hiet");
/* 146 */     addAllowedPredicate("deleted_conditional", "hiet");
/* 147 */     addAllowedPredicate("modified_conditional", "hiet");
/* 148 */     addAllowedPredicate("before_methodbody", "ti");
/* 149 */     addAllowedPredicate("after_methodbody", "ti");
/* 150 */     addAllowedPredicate("added_methodbody", "ti");
/* 151 */     addAllowedPredicate("deleted_methodbody", "ti");
/* 152 */     addAllowedPredicate("before_parameter", "cmi");
/* 153 */     addAllowedPredicate("after_parameter", "cmi");
/* 154 */     addAllowedPredicate("added_parameter", "cmi");
/* 155 */     addAllowedPredicate("deleted_parameter", "cmi");
/* 156 */     addAllowedPredicate("before_methodmodifier", "mi");
/* 157 */     addAllowedPredicate("after_methodmodifier", "mi");
/* 158 */     addAllowedPredicate("added_methodmodifier", "mi");
/* 159 */     addAllowedPredicate("deleted_methodmodifier", "mi");
/* 160 */     addAllowedPredicate("before_fieldmodifier", "mi");
/* 161 */     addAllowedPredicate("after_fieldmodifier", "mi");
/* 162 */     addAllowedPredicate("added_fieldmodifier", "mi");
/* 163 */     addAllowedPredicate("deleted_fieldmodifier", "mi");
/*     */     
/*     */ 
/*     */ 
/* 167 */     addAllowedPredicate("before_cast", "etm");
/* 168 */     addAllowedPredicate("after_cast", "etm");
/* 169 */     addAllowedPredicate("added_cast", "etm");
/* 170 */     addAllowedPredicate("deleted_cast", "etm");
/* 171 */     addAllowedPredicate("before_trycatch", "abcm");
/* 172 */     addAllowedPredicate("after_trycatch", "abcm");
/* 173 */     addAllowedPredicate("added_trycatch", "abcm");
/* 174 */     addAllowedPredicate("deleted_trycatch", "abcm");
/* 175 */     addAllowedPredicate("before_throws", "mt");
/* 176 */     addAllowedPredicate("after_throws", "mt");
/* 177 */     addAllowedPredicate("added_throws", "mt");
/* 178 */     addAllowedPredicate("deleted_throws", "mt");
/* 179 */     addAllowedPredicate("before_getter", "mf");
/* 180 */     addAllowedPredicate("after_getter", "mf");
/* 181 */     addAllowedPredicate("added_getter", "mf");
/* 182 */     addAllowedPredicate("deleted_getter", "mf");
/* 183 */     addAllowedPredicate("before_setter", "mf");
/* 184 */     addAllowedPredicate("after_setter", "mf");
/* 185 */     addAllowedPredicate("added_setter", "mf");
/* 186 */     addAllowedPredicate("deleted_setter", "mf");
/*     */     
/* 188 */     addAllowedPredicate("before_localvar", "mtab");
/* 189 */     addAllowedPredicate("after_localvar", "mtab");
/* 190 */     addAllowedPredicate("added_localvar", "mtab");
/* 191 */     addAllowedPredicate("deleted_localvar", "mtab");
/*     */   }
/*     */   
/*     */   public static int CLASSLEVEL;
/*     */   public static int METHODLEVEL;
/*     */   private static java.util.HashMap<String, LSDPredicate> allowedPredicate;
/* 197 */   private int kind = UNDEFINED;
/*     */   private final String predName;
/*     */   private final char[] types;
/*     */   private int level;
/*     */   
/* 202 */   public int arity() { return this.types.length; }
/*     */   
/*     */   private LSDPredicate(String pred, char[] types) throws LSDInvalidTypeException {
/* 205 */     this.types = types;
/*     */     
/*     */ 
/*     */ 
/* 209 */     for (int i = 0; i < types.length; i++) {
/* 210 */       if (!LSDVariable.isValidType(types[i]))
/*     */       {
/*     */ 
/* 213 */         throw new LSDInvalidTypeException();
/*     */       }
/*     */     }
/* 216 */     this.predName = pred;
/* 217 */     if (pred.indexOf("before_") != -1) {
/* 218 */       this.kind = BEFORE;
/* 219 */     } else if (pred.indexOf("after_") != -1) {
/* 220 */       this.kind = AFTER;
/* 221 */     } else if (pred.indexOf("deleted_") != -1) {
/* 222 */       this.kind = DELETED;
/* 223 */     } else if (pred.indexOf("added_") != -1) {
/* 224 */       this.kind = ADDED;
/* 225 */     } else if (pred.indexOf("modified_") != -1) {
/* 226 */       this.kind = MODIFIED;
/*     */     } else {
/* 228 */       this.kind = UNDEFINED;
/*     */     }
/* 230 */     this.level = setLevel(pred, types);
/*     */   }
/*     */   
/*     */   private int setLevel(String pred, char[] types) {
/* 234 */     if (types[0] == 'p')
/* 235 */       return PACKAGELEVEL;
/* 236 */     if (types[0] == 't')
/* 237 */       return CLASSLEVEL;
/* 238 */     return METHODLEVEL;
/*     */   }
/*     */   
/*     */ 
/* 242 */   public boolean isElement() { return arity() == 1; }
/* 243 */   public String getName() { return this.predName; }
/* 244 */   public String getDisplayName() { return is_pPredicate() ? this.predName.replaceFirst("_p_", "_") : this.predName; }
/* 245 */   public char[] getTypes() { return this.types; }
/*     */   
/* 247 */   public String toString() { StringBuilder typeString = new StringBuilder();
/* 248 */     for (int i = 0; i < this.types.length; i++) {
/* 249 */       if (i >= 1) {
/* 250 */         typeString.append(",");
/*     */       }
/* 252 */       typeString.append(this.types[i]);
/*     */     }
/* 254 */     return this.predName + "(" + typeString + ")";
/*     */   }
/*     */   
/*     */   private static void addAllowedPredicate(String predName, String types) {
/*     */     try {
/* 259 */       allowedPredicate.put(predName, new LSDPredicate(predName, types.toCharArray()));
/*     */ 
/*     */     }
/*     */     catch (LSDInvalidTypeException e)
/*     */     {
/* 264 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static LSDPredicate getPredicate(String predName) {
/* 269 */     return (LSDPredicate)allowedPredicate.get(predName);
/*     */   }
/*     */   
/*     */   public LSDPredicate getPrefixPredicate(String prefix) {
/* 273 */     return getPredicate(prefix + "_" + getSuffix());
/*     */   }
/*     */   
/*     */   public static java.util.List<LSDPredicate> getPredicates()
/*     */   {
/* 278 */     java.util.List<LSDPredicate> predicates = new ArrayList();
/* 279 */     for (LSDPredicate predicate : allowedPredicate.values()) {
/* 280 */       if (predicate.kind != UNDEFINED)
/* 281 */         predicates.add(predicate);
/*     */     }
/* 283 */     return predicates;
/*     */   }
/*     */   
/*     */   public boolean isKBBeforePredicate()
/*     */   {
/* 288 */     return (this.kind == DELETED) || (this.kind == BEFORE) || (this.kind == DELETED_P);
/*     */   }
/*     */   
/* 291 */   public boolean isKBAfterPredicate() { return (this.kind == ADDED) || (this.kind == AFTER) || (this.kind == ADDED_P); }
/*     */   
/*     */   public boolean isConclusionPredicate() {
/* 294 */     return (this.kind == DELETED) || (this.kind == ADDED) || (this.kind == MODIFIED);
/*     */   }
/*     */   
/* 297 */   public boolean is2KBPredicate() { return (this.kind == BEFORE) || (this.kind == AFTER) || (is_pPredicate()); }
/*     */   
/*     */   public boolean isAntecedentPredicate() {
/* 300 */     return this.kind == BEFORE;
/*     */   }
/*     */   
/*     */   public boolean is_pPredicate() {
/* 304 */     return (this.kind == ADDED_P) || (this.kind == DELETED_P) || (this.kind == MODIFIED_P);
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
/*     */   public boolean allowedInSameRule(LSDPredicate conclusion, LSDPredicate antecedant)
/*     */   {
/* 319 */     if (antecedant != null) {
/* 320 */       boolean x = this.kind == antecedant.kind;
/* 321 */       return x;
/*     */     }
/*     */     
/* 324 */     if (conclusion.kind == DELETED) {
/* 325 */       return this.kind == BEFORE;
/*     */     }
/* 327 */     if (conclusion.kind == ADDED) {
/* 328 */       return (this.kind == DELETED) || (this.kind == BEFORE) || (this.kind == AFTER);
/*     */     }
/* 330 */     if (conclusion.kind == MODIFIED) {
/* 331 */       return this.kind == BEFORE;
/*     */     }
/* 333 */     return false;
/*     */   }
/*     */   
/*     */   public boolean typeChecks(ArrayList<LSDBinding> bindings) {
/* 337 */     if (bindings.size() != this.types.length) return false;
/* 338 */     for (int i = 0; i < bindings.size(); i++) {
/* 339 */       char type = this.types[i];
/* 340 */       LSDBinding binding = (LSDBinding)bindings.get(i);
/* 341 */       if (!binding.typeChecks(type)) return false;
/*     */     }
/* 343 */     return true;
/*     */   }
/*     */   
/*     */   public boolean typeMatches(java.util.Collection<Character> types) { char[] arrayOfChar;
/* 347 */     int j = (arrayOfChar = this.types).length; for (int i = 0; i < j; i++) { char type = arrayOfChar[i];
/* 348 */       if (types.contains(Character.valueOf(type)))
/* 349 */         return true; }
/* 350 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equalsIgnoringPrimes(Object other)
/*     */   {
/* 355 */     if (!(other instanceof LSDPredicate))
/* 356 */       return false;
/* 357 */     return getDisplayName().equals(((LSDPredicate)other).getDisplayName());
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 362 */     if (!(other instanceof LSDPredicate))
/* 363 */       return false;
/* 364 */     return toString().equals(((LSDPredicate)other).toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 372 */     LSDPredicate foo = getPredicate("deleted_field");
/* 373 */     assert (foo.getName() == "deleted_field");
/* 374 */     assert (foo.arity() == 1);
/* 375 */     assert (getPredicate("added_bogusMethod") == null);
/* 376 */     foo = getPredicate("added_inheritedmethod");
/* 377 */     assert (foo.getName() == "added_inheritedmethod");
/* 378 */     assert (foo.arity() == 3);
/* 379 */     ArrayList<LSDBinding> bindings = new ArrayList();
/* 380 */     LSDBinding binding = new LSDBinding(new LSDVariable("a", 'm'));
/* 381 */     bindings.add(binding);
/* 382 */     binding = new LSDBinding(new LSDVariable("c", 't'));
/* 383 */     bindings.add(binding);
/* 384 */     assert (!foo.typeChecks(bindings));
/* 385 */     binding = new LSDBinding(new LSDVariable("b", 't'));
/* 386 */     bindings.add(binding);
/* 387 */     assert (foo.typeChecks(bindings));
/* 388 */     bindings = new ArrayList();
/* 389 */     binding = new LSDBinding(new LSDVariable("a", 'f'));
/* 390 */     bindings.add(binding);
/* 391 */     binding = new LSDBinding(new LSDVariable("c", 't'));
/* 392 */     bindings.add(binding);
/* 393 */     binding = new LSDBinding(new LSDVariable("b", 't'));
/* 394 */     bindings.add(binding);
/* 395 */     assert (!foo.typeChecks(bindings));
/* 396 */     assert (foo.toString().equals("added_inheritedmethod(m,t,t)"));
/* 397 */     System.out.println("Predicate tests succeeded.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ArrayList<LSDPredicate> getPredicates(int kind, int arity)
/*     */   {
/* 404 */     java.util.Collection<LSDPredicate> predicates = allowedPredicate.values();
/* 405 */     ArrayList<LSDPredicate> results = new ArrayList();
/* 406 */     for (LSDPredicate pred : predicates) {
/* 407 */       if ((pred.kind == kind) && (pred.arity() == arity)) {
/* 408 */         results.add(pred);
/*     */       }
/*     */     }
/* 411 */     if (results.size() == 0) return null;
/* 412 */     return results;
/*     */   }
/*     */   
/*     */   public static ArrayList<LSDPredicate> getPredicates(int kind, char type) {
/* 416 */     java.util.Collection<LSDPredicate> predicates = allowedPredicate.values();
/* 417 */     ArrayList<LSDPredicate> results = new ArrayList();
/* 418 */     for (LSDPredicate pred : predicates)
/*     */     {
/* 420 */       boolean includeType = false;
/* 421 */       for (int i = 0; i < pred.types.length; i++) {
/* 422 */         if (pred.types[i] == type) {
/* 423 */           includeType = true;
/*     */         }
/*     */       }
/* 426 */       if ((pred.kind == kind) && (includeType)) {
/* 427 */         results.add(pred);
/*     */       }
/*     */     }
/* 430 */     if (results.size() == 0) return null;
/* 431 */     return results;
/*     */   }
/*     */   
/*     */   public char[] getPrimaryTypes()
/*     */   {
/* 436 */     String name = getName();
/* 437 */     if (name.indexOf("_") > 0)
/* 438 */       name = name.substring(name.indexOf("_") + 1);
/* 439 */     if (name.equals("type"))
/* 440 */       return "t".toCharArray();
/* 441 */     if (name.equals("dependency"))
/* 442 */       return "t".toCharArray();
/* 443 */     if (name.equals("field"))
/* 444 */       return "f".toCharArray();
/* 445 */     if (name.equals("method"))
/* 446 */       return "m".toCharArray();
/* 447 */     if (name.equals("typeintype"))
/* 448 */       return "t".toCharArray();
/* 449 */     if (name.equals("inheritedmethod"))
/* 450 */       return "mt".toCharArray();
/* 451 */     if (name.equals("inheritedfield")) {
/* 452 */       return "ft".toCharArray();
/*     */     }
/* 454 */     return getTypes();
/*     */   }
/*     */   
/*     */   protected String getSuffix() {
/* 458 */     String name = getName();
/* 459 */     if (name.lastIndexOf("_") > 0) name = name.substring(name.lastIndexOf("_") + 1);
/* 460 */     return name;
/*     */   }
/*     */   
/*     */   protected String getPrefix() {
/* 464 */     String name = getName();
/* 465 */     if (name.lastIndexOf("_") > 0) name = name.substring(0, name.lastIndexOf("_"));
/* 466 */     return name;
/*     */   }
/*     */   
/*     */   public int[][] getPrimaryArguments()
/*     */   {
/* 471 */     String name = getSuffix();
/* 472 */     if (name.equals("type")) {
/* 473 */       int[][] s = { new int[1] };
/* 474 */       return s; }
/* 475 */     if (name.equals("field")) {
/* 476 */       int[][] s = { new int[1], { 1, 2 } };
/* 477 */       return s; }
/* 478 */     if (name.equals("method")) {
/* 479 */       int[][] s = { new int[1], { 1, 2 } };
/* 480 */       return s; }
/* 481 */     if (name.equals("typeintype")) {
/* 482 */       int[][] s = { new int[1] };
/* 483 */       return s;
/*     */     }
/* 485 */     int[] s = new int[getTypes().length];
/* 486 */     for (int i = 0; i < s.length; i++) {
/* 487 */       s[i] = i;
/*     */     }
/* 489 */     int[][] ss = { s };
/* 490 */     return ss;
/*     */   }
/*     */   
/*     */   public int getReferenceArgument()
/*     */   {
/* 495 */     String name = getSuffix();
/* 496 */     if (name.equals("subtype"))
/* 497 */       return 1;
/* 498 */     if (name.equals("accesses"))
/* 499 */       return 1;
/* 500 */     if (name.equals("inheritedfield"))
/* 501 */       return 2;
/* 502 */     if (name.equals("inheritedmethod")) {
/* 503 */       return 2;
/*     */     }
/* 505 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isMethodLevel() {
/* 509 */     if (this.level == METHODLEVEL)
/* 510 */       return true;
/* 511 */     return false;
/*     */   }
/*     */   
/*     */   public String getConvertedArgs(String arg)
/*     */   {
/* 516 */     java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(arg, ",", false);
/* 517 */     if (tokenizer.countTokens() != 2)
/*     */     {
/* 519 */       while (arg.contains("(")) {
/* 520 */         String temp = arg.substring(0, arg.indexOf("("));
/* 521 */         arg = arg.substring(arg.indexOf("(") + 1);
/* 522 */         arg = temp + arg.substring(arg.indexOf(")") + 1);
/*     */       }
/* 524 */       while (arg.contains("<")) {
/* 525 */         String temp = arg.substring(0, arg.indexOf("<"));
/* 526 */         arg = arg.substring(arg.indexOf("<") + 1);
/* 527 */         arg = temp + arg.substring(arg.indexOf(">") + 1);
/*     */       }
/*     */     }
/* 530 */     tokenizer = new java.util.StringTokenizer(arg, ",", false);
/* 531 */     if (isConclusionPredicate()) {
/* 532 */       String arg0 = tokenizer.nextToken();
/* 533 */       if ((getSuffix().equalsIgnoreCase("typeintype")) || (getSuffix().equalsIgnoreCase("accesses")))
/* 534 */         arg0 = tokenizer.nextToken();
/* 535 */       if (getSuffix().contains("inherited"))
/*     */       {
/* 537 */         arg0 = tokenizer.nextToken();
/* 538 */         arg0 = tokenizer.nextToken();
/*     */       }
/* 540 */       if (arg0.indexOf("#") != -1) {
/* 541 */         arg0 = arg0.substring(1, arg0.indexOf("#"));
/* 542 */       } else if (arg0.indexOf("\"") != -1)
/* 543 */         arg0 = arg0.substring(1, arg0.lastIndexOf("\""));
/* 544 */       String arg1 = arg0.substring(arg0.indexOf("%.") + 2);
/* 545 */       String arg2; String arg2; if (arg0.indexOf("%") == 0) {
/* 546 */         arg2 = "null";
/*     */       } else
/* 548 */         arg2 = arg0.substring(0, arg0.indexOf("%."));
/* 549 */       if ((arg2 == null) || (arg2.length() == 0))
/* 550 */         arg2 = "null";
/* 551 */       arg0 = "\"" + arg0 + "\",\"" + arg1 + "\",\"" + arg2 + "\"";
/* 552 */       return arg0;
/*     */     }
/*     */     
/*     */ 
/* 556 */     String arg0 = tokenizer.nextToken();
/* 557 */     String arg1 = tokenizer.nextToken();
/* 558 */     if (getSuffix().equalsIgnoreCase("accesses"))
/*     */     {
/* 560 */       String temp = arg1;
/* 561 */       arg1 = arg0;
/* 562 */       arg0 = temp;
/*     */     }
/* 564 */     if (arg0.indexOf("#") != -1) {
/* 565 */       arg0 = arg0.substring(1, arg0.indexOf("#"));
/* 566 */     } else if (arg0.indexOf("\"") != -1)
/* 567 */       arg0 = arg0.substring(1, arg0.lastIndexOf("\""));
/* 568 */     if (arg1.indexOf("#") != -1) {
/* 569 */       arg1 = arg1.substring(1, arg1.indexOf("#"));
/* 570 */     } else if (arg1.indexOf("\"") != -1) {
/* 571 */       arg1 = arg1.substring(1, arg1.lastIndexOf("\""));
/*     */     }
/* 573 */     arg0 = "\"" + arg0 + "\",\"" + arg1 + "\"";
/* 574 */     return arg0;
/*     */   }
/*     */   
/*     */ 
/*     */   public LSDPredicate toClassLevel()
/*     */   {
/* 580 */     if (isConclusionPredicate()) {
/* 581 */       return getPredicate("changed_type");
/*     */     }
/* 583 */     if ((this.predName.contains("accesses")) || (this.predName.contains("calls"))) {
/* 584 */       String newPred = this.predName.substring(0, this.predName.indexOf('_')) + "_dependency";
/* 585 */       return getPredicate(newPred);
/*     */     }
/*     */     
/* 588 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isDependencyPredicate() {
/* 592 */     if ((this.predName.contains("accesses")) || (this.predName.contains("calls")))
/* 593 */       return true;
/* 594 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCompatibleMethodLevel() {
/* 598 */     if (getSuffix().equalsIgnoreCase("dependency"))
/* 599 */       return false;
/* 600 */     return true;
/*     */   }
/*     */   
/*     */   public ArrayList<LSDPredicate> getMethodLevelDependency() {
/* 604 */     String prefix = getPrefix();
/* 605 */     ArrayList<LSDPredicate> preds = new ArrayList();
/* 606 */     preds.add(getPredicate(prefix + "_calls"));
/* 607 */     preds.add(getPredicate(prefix + "_accesses"));
/* 608 */     return preds;
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateBindings(ArrayList<LSDBinding> bindings)
/*     */   {
/* 614 */     if (getSuffix() == "accesses") {
/* 615 */       ArrayList<LSDBinding> temp = new ArrayList();
/* 616 */       temp.add((LSDBinding)bindings.get(1));
/* 617 */       temp.add((LSDBinding)bindings.get(0));
/* 618 */       bindings = temp;
/*     */     }
/*     */   }
/*     */   
/*     */   public static String combineArguments(String arg0, String arg1, String arg2)
/*     */   {
/* 624 */     return "\"" + arg0 + "\", \"" + arg1 + "\" ,\"" + arg2 + "\"";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/rule/LSDPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */