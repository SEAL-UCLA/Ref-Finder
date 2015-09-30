/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import junit.framework.Assert;
/*     */ import org.apache.regexp.RE;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeModeError;
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
/*     */ public class NativePredicate
/*     */   extends RBComponent
/*     */ {
/*     */   private PredInfo predinfo;
/*  38 */   private ArrayList implementations = new ArrayList();
/*     */   private PredicateIdentifier predId;
/*     */   
/*     */   public Mode getMode() {
/*  42 */     return null;
/*     */   }
/*     */   
/*     */   public NativePredicate(String name, TupleType argtypes)
/*     */   {
/*  47 */     this.predId = new PredicateIdentifier(name, argtypes.size());
/*  48 */     this.predinfo = Factory.makePredInfo(null, name, argtypes);
/*     */   }
/*     */   
/*     */   public NativePredicate(String name, Type t) {
/*  52 */     this(name, Factory.makeTupleType(t));
/*     */   }
/*     */   
/*     */   public NativePredicate(String name, Type t1, Type t2) {
/*  56 */     this(name, Factory.makeTupleType(t1, t2));
/*     */   }
/*     */   
/*     */   public NativePredicate(String name, Type t1, Type t2, Type t3) {
/*  60 */     this(name, Factory.makeTupleType(t1, t2, t3));
/*     */   }
/*     */   
/*     */   public NativePredicate(String name, Type t1, Type t2, Type t3, Type t4) {
/*  64 */     this(name, Factory.makeTupleType(t1, t2, t3, t4));
/*     */   }
/*     */   
/*     */ 
/*     */   public void addMode(Implementation imp)
/*     */   {
/*  70 */     this.predinfo.addPredicateMode(imp.getPredicateMode());
/*  71 */     this.implementations.add(imp);
/*     */   }
/*     */   
/*     */   private void addToRuleBase(ModedRuleBaseIndex rb) throws TypeModeError
/*     */   {
/*  76 */     rb.insert(this.predinfo);
/*  77 */     rb.insert(this);
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/*  81 */     throw new Error("Compilation only works after this has been converted to a proper mode.");
/*     */   }
/*     */   
/*     */   public TupleType typecheck(PredInfoProvider predinfo) throws TypeModeError
/*     */   {
/*  86 */     return getPredType();
/*     */   }
/*     */   
/*     */   public int getNumImplementation() {
/*  90 */     return this.implementations.size();
/*     */   }
/*     */   
/*     */   public Implementation getImplementationAt(int pos) {
/*  94 */     return (Implementation)this.implementations.get(pos);
/*     */   }
/*     */   
/*     */   public TupleType getPredType() {
/*  98 */     return this.predinfo.getTypeList();
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId() {
/* 102 */     return this.predId;
/*     */   }
/*     */   
/*     */   public RBTuple getArgs() {
/* 106 */     throw new Error("getArgs cannot be called until an implementation has been selected");
/*     */   }
/*     */   
/*     */   public RBComponent convertToMode(PredicateMode mode, ModeCheckContext context) throws TypeModeError
/*     */   {
/* 111 */     BindingList targetBindingList = mode.getParamModes();
/* 112 */     Implementation result = null;
/* 113 */     for (int i = 0; i < getNumImplementation(); i++) {
/* 114 */       Implementation candidate = getImplementationAt(i);
/* 115 */       if ((targetBindingList.satisfyBinding(candidate.getBindingList())) && (
/* 116 */         (result == null) || 
/* 117 */         (candidate.getMode().isBetterThan(result.getMode())))) {
/* 118 */         result = candidate;
/*     */       }
/*     */     }
/*     */     
/* 122 */     if (result == null) {
/* 123 */       throw new TypeModeError("Cannot find an implementation for " + 
/* 124 */         getPredName() + " :: " + mode);
/*     */     }
/* 126 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 131 */     StringBuffer result = new StringBuffer(this.predinfo.getPredId().toString());
/* 132 */     for (Iterator iter = this.implementations.iterator(); iter.hasNext();) {
/* 133 */       Implementation element = (Implementation)iter.next();
/* 134 */       result.append("\n" + element);
/*     */     }
/* 136 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static void defineStringAppend(ModedRuleBaseIndex rb) throws TypeModeError {
/* 140 */     NativePredicate string_append = new NativePredicate("string_append", 
/* 141 */       Type.string, Type.string, Type.string);
/*     */     
/* 143 */     string_append.addMode(new Implementation("BBF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 145 */         String s1 = (String)args[0].up();
/* 146 */         String s2 = (String)args[1].up();
/* 147 */         addSolution(s1.concat(s2));
/*     */       }
/*     */       
/* 150 */     });
/* 151 */     string_append.addMode(new Implementation("FFB", "MULTI") {
/*     */       public void doit(RBTerm[] args) {
/* 153 */         String s = (String)args[0].up();
/* 154 */         int len = s.length();
/* 155 */         for (int i = 0; i <= len; i++) {
/* 156 */           addSolution(s.substring(0, i), s.substring(i));
/*     */         }
/*     */         
/*     */       }
/* 160 */     });
/* 161 */     string_append.addMode(new Implementation("BFB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 163 */         String s1 = (String)args[0].up();
/* 164 */         String s3 = (String)args[1].up();
/* 165 */         if (s3.startsWith(s1)) {
/* 166 */           addSolution(s3.substring(s1.length()));
/*     */         }
/*     */       }
/* 169 */     });
/* 170 */     string_append.addMode(new Implementation("FBB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 172 */         String s2 = (String)args[0].up();
/* 173 */         String s3 = (String)args[1].up();
/* 174 */         if (s3.endsWith(s2)) {
/* 175 */           addSolution(s3.substring(0, s3.length() - s2.length()));
/*     */         }
/*     */       }
/* 178 */     });
/* 179 */     string_append.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringLength(ModedRuleBaseIndex rb) throws TypeModeError {
/* 183 */     NativePredicate string_length = new NativePredicate("string_length", 
/* 184 */       Type.string, Type.integer);
/*     */     
/* 186 */     string_length.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 188 */         String s = (String)args[0].up();
/* 189 */         addSolution(new Integer(s.length()));
/*     */       }
/*     */       
/* 192 */     });
/* 193 */     string_length.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringIndexSplit(ModedRuleBaseIndex rb) throws TypeModeError {
/* 197 */     NativePredicate string_index_split = new NativePredicate("string_index_split", 
/* 198 */       Type.integer, Type.string, Type.string, Type.string);
/*     */     
/* 200 */     string_index_split.addMode(new Implementation("BBFF", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 202 */         int where = ((Integer)args[0].up()).intValue();
/* 203 */         String to_split = (String)args[1].up();
/* 204 */         if ((where >= 0) && (where <= to_split.length())) {
/* 205 */           addSolution(to_split.substring(0, where), 
/* 206 */             to_split.substring(where));
/*     */         }
/*     */         
/*     */       }
/* 210 */     });
/* 211 */     string_index_split.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringSplitAtLast(ModedRuleBaseIndex rb) throws TypeModeError {
/* 215 */     NativePredicate string_split_at_last = new NativePredicate("string_split_at_last", 
/* 216 */       Type.string, Type.string, Type.string, Type.string);
/*     */     
/* 218 */     string_split_at_last.addMode(new Implementation("BBFF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 220 */         String separator = (String)args[0].up();
/* 221 */         String to_split = (String)args[1].up();
/* 222 */         int where = to_split.lastIndexOf(separator);
/* 223 */         if (where >= 0) {
/* 224 */           addSolution(to_split.substring(0, where), 
/* 225 */             to_split.substring(where + separator.length()));
/*     */         } else {
/* 227 */           addSolution("", to_split);
/*     */         }
/*     */         
/*     */       }
/* 231 */     });
/* 232 */     string_split_at_last.addMode(new Implementation("BFBB", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 234 */         String separator = (String)args[0].up();
/* 235 */         String start = (String)args[1].up();
/* 236 */         String end = (String)args[2].up();
/* 237 */         if (end.indexOf(separator) < 0) {
/* 238 */           addSolution(start.concat(separator.concat(end)));
/*     */         }
/*     */       }
/* 241 */     });
/* 242 */     string_split_at_last.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static String stringReplace(String r1, String r2, String s) {
/* 246 */     StringBuffer buf = new StringBuffer();
/* 247 */     int start = 0;
/*     */     do {
/* 249 */       int end = s.indexOf(r1, start);
/* 250 */       if (end == -1)
/*     */       {
/* 252 */         buf.append(s.substring(start));
/* 253 */         return buf.toString();
/*     */       }
/* 255 */       buf.append(s.substring(start, end));
/* 256 */       buf.append(r2);
/* 257 */       start = end + r1.length();
/* 258 */     } while (start < s.length());
/* 259 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static void defineStringReplace(ModedRuleBaseIndex rb) throws TypeModeError {
/* 263 */     NativePredicate string_replace = new NativePredicate("string_replace", 
/* 264 */       Type.string, Type.string, Type.string, Type.string);
/*     */     
/* 266 */     string_replace.addMode(new Implementation("BBBF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 268 */         String r1 = (String)args[0].up();
/* 269 */         String r2 = (String)args[1].up();
/* 270 */         String s = (String)args[2].up();
/* 271 */         addSolution(NativePredicate.stringReplace(r1, r2, s));
/*     */       }
/*     */       
/* 274 */     });
/* 275 */     string_replace.addMode(new Implementation("BBFB", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 277 */         String r1 = (String)args[0].up();
/* 278 */         String r2 = (String)args[1].up();
/* 279 */         String s = (String)args[2].up();
/* 280 */         addSolution(NativePredicate.stringReplace(r2, r1, s));
/*     */       }
/*     */       
/* 283 */     });
/* 284 */     string_replace.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineToLowerCase(ModedRuleBaseIndex rb) throws TypeModeError {
/* 288 */     NativePredicate to_lower_case = new NativePredicate("to_lower_case", 
/* 289 */       Type.string, Type.string);
/*     */     
/* 291 */     to_lower_case.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 293 */         String s = (String)args[0].up();
/* 294 */         addSolution(s.toLowerCase());
/*     */       }
/*     */       
/* 297 */     });
/* 298 */     to_lower_case.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineToUpperCase(ModedRuleBaseIndex rb) throws TypeModeError {
/* 302 */     NativePredicate to_upper_case = new NativePredicate("to_upper_case", 
/* 303 */       Type.string, Type.string);
/*     */     
/* 305 */     to_upper_case.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 307 */         String s = (String)args[0].up();
/* 308 */         addSolution(s.toUpperCase());
/*     */       }
/*     */       
/* 311 */     });
/* 312 */     to_upper_case.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineCapitalize(ModedRuleBaseIndex rb) throws TypeModeError {
/* 316 */     NativePredicate capitalize = new NativePredicate("capitalize", 
/* 317 */       Type.string, Type.string);
/*     */     
/* 319 */     capitalize.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 321 */         String s = (String)args[0].up();
/* 322 */         if (s.length() > 0) {
/* 323 */           addSolution(s.substring(0, 1).toUpperCase().concat(s.substring(1)));
/*     */         }
/*     */       }
/* 326 */     });
/* 327 */     capitalize.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineDecapitalize(ModedRuleBaseIndex rb) throws TypeModeError {
/* 331 */     NativePredicate decapitalize = new NativePredicate("decapitalize", 
/* 332 */       Type.string, Type.string);
/*     */     
/* 334 */     decapitalize.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 336 */         String s = (String)args[0].up();
/* 337 */         if (s.length() > 0) {
/* 338 */           addSolution(s.substring(0, 1).toLowerCase().concat(s.substring(1)));
/*     */         }
/*     */       }
/* 341 */     });
/* 342 */     decapitalize.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringRepeat(ModedRuleBaseIndex rb) throws TypeModeError {
/* 346 */     NativePredicate string_repeat = new NativePredicate("string_repeat", 
/* 347 */       Type.integer, Type.string, Type.string);
/*     */     
/* 349 */     string_repeat.addMode(new Implementation("BBF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 351 */         int num = ((Integer)args[0].up()).intValue();
/* 352 */         String rep = (String)args[1].up();
/* 353 */         if (num > -1) {
/* 354 */           String result = "";
/* 355 */           for (int i = 0; i < num; i++)
/* 356 */             result = result + rep;
/* 357 */           addSolution(result);
/*     */         }
/*     */         
/*     */       }
/* 361 */     });
/* 362 */     string_repeat.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineTypeTest(ModedRuleBaseIndex rb, PredicateIdentifier id, final TypeConstructor t) throws TypeModeError
/*     */   {
/* 367 */     Assert.assertEquals(1, id.getArity());
/* 368 */     t.getName();
/* 369 */     NativePredicate type_test = new NativePredicate(id.getName(), Factory.makeAtomicType(t));
/*     */     
/* 371 */     type_test.addMode(new Implementation("B", "SEMIDET")
/*     */     {
/*     */       public void doit(RBTerm[] args) {
/* 374 */         if (args[0].isOfType(t)) {
/* 375 */           addSolution();
/*     */         }
/*     */         
/*     */       }
/* 379 */     });
/* 380 */     type_test.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineRange(ModedRuleBaseIndex rb) throws TypeModeError {
/* 384 */     NativePredicate range = new NativePredicate("range", 
/* 385 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 387 */     range.addMode(new Implementation("BBF", "NONDET") {
/*     */       public void doit(RBTerm[] args) {
/* 389 */         int lo = ((Integer)args[0].up()).intValue();
/* 390 */         int high = ((Integer)args[1].up()).intValue();
/* 391 */         if (high > lo) {
/* 392 */           for (int i = lo; i < high; i++) {
/* 393 */             addSolution(new Integer(i));
/*     */           }
/*     */         }
/*     */       }
/* 397 */     });
/* 398 */     range.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineGreater(ModedRuleBaseIndex rb) throws TypeModeError {
/* 402 */     NativePredicate greater = new NativePredicate("greater", Type.integer, Type.integer);
/*     */     
/* 404 */     greater.addMode(new Implementation("BB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 406 */         int high = ((Integer)args[0].up()).intValue();
/* 407 */         int lo = ((Integer)args[1].up()).intValue();
/* 408 */         if (high > lo) {
/* 409 */           addSolution();
/*     */         }
/*     */       }
/* 412 */     });
/* 413 */     greater.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineSum(ModedRuleBaseIndex rb) throws TypeModeError {
/* 417 */     NativePredicate sum = new NativePredicate("sum", 
/* 418 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 420 */     sum.addMode(new Implementation("BBF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 422 */         int v1 = ((Integer)args[0].up()).intValue();
/* 423 */         int v2 = ((Integer)args[1].up()).intValue();
/* 424 */         addSolution(new Integer(v1 + v2));
/*     */       }
/*     */       
/* 427 */     });
/* 428 */     sum.addMode(new Implementation("BFB", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 430 */         int v1 = ((Integer)args[0].up()).intValue();
/* 431 */         int v2 = ((Integer)args[1].up()).intValue();
/* 432 */         addSolution(new Integer(v2 - v1));
/*     */       }
/*     */       
/* 435 */     });
/* 436 */     sum.addMode(new Implementation("FBB", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 438 */         int v1 = ((Integer)args[0].up()).intValue();
/* 439 */         int v2 = ((Integer)args[1].up()).intValue();
/* 440 */         addSolution(new Integer(v2 - v1));
/*     */       }
/*     */       
/* 443 */     });
/* 444 */     sum.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineMul(ModedRuleBaseIndex rb) throws TypeModeError {
/* 448 */     NativePredicate mul = new NativePredicate("mul", 
/* 449 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 451 */     mul.addMode(new Implementation("BBF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 453 */         int v1 = ((Integer)args[0].up()).intValue();
/* 454 */         int v2 = ((Integer)args[1].up()).intValue();
/* 455 */         addSolution(new Integer(v1 * v2));
/*     */       }
/*     */       
/* 458 */     });
/* 459 */     mul.addMode(new Implementation("FBB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 461 */         int v2 = ((Integer)args[0].up()).intValue();
/* 462 */         int v3 = ((Integer)args[1].up()).intValue();
/* 463 */         if (v2 == 0) {
/* 464 */           if (v3 == 0) {
/* 465 */             addSolution(new Integer(0));
/*     */           }
/* 467 */         } else if (v3 % v2 == 0) {
/* 468 */           addSolution(new Integer(v3 / v2));
/*     */         }
/*     */         
/*     */       }
/* 472 */     });
/* 473 */     mul.addMode(new Implementation("BFB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 475 */         int v1 = ((Integer)args[0].up()).intValue();
/* 476 */         int v3 = ((Integer)args[1].up()).intValue();
/* 477 */         if (v1 == 0) {
/* 478 */           if (v3 == 0) {
/* 479 */             addSolution(new Integer(0));
/*     */           }
/* 481 */         } else if (v3 % v1 == 0) {
/* 482 */           addSolution(new Integer(v3 / v1));
/*     */         }
/*     */         
/*     */       }
/* 486 */     });
/* 487 */     mul.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void debug_print(Object ob) {
/* 491 */     System.err.println(ob.toString());
/*     */   }
/*     */   
/*     */   public static void defineDebugPrint(ModedRuleBaseIndex rb) throws TypeModeError {
/* 495 */     NativePredicate debug_print = new NativePredicate("debug_print", Type.object);
/*     */     
/* 497 */     debug_print.addMode(new Implementation("B", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 499 */         String msg = args[0].up().toString();
/* 500 */         NativePredicate.debug_print(msg);
/* 501 */         addSolution();
/*     */       }
/*     */       
/* 504 */     });
/* 505 */     debug_print.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineThrowError(ModedRuleBaseIndex rb) throws TypeModeError {
/* 509 */     NativePredicate throw_error = new NativePredicate("throw_error", Type.string);
/*     */     
/* 511 */     throw_error.addMode(new Implementation("B", "FAIL") {
/*     */       public void doit(RBTerm[] args) {
/* 513 */         String msg = (String)args[0].up();
/* 514 */         throw new Error(msg);
/*     */       }
/*     */       
/* 517 */     });
/* 518 */     throw_error.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineWriteFile(ModedRuleBaseIndex rb) throws TypeModeError {
/* 522 */     NativePredicate write_file = new NativePredicate("write_file", 
/* 523 */       Type.string, Type.string);
/*     */     
/* 525 */     write_file.addMode(new Implementation("BB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 527 */         String filename = (String)args[0].up();
/* 528 */         String contents = (String)args[1].up();
/* 529 */         NativePredicate.debug_print("writing file: " + filename);
/*     */         try {
/* 531 */           File f = new File(filename);
/* 532 */           File p = f.getParentFile();
/* 533 */           if (p != null) {
/* 534 */             p.mkdirs();
/*     */           }
/* 536 */           PrintStream os = new PrintStream(new FileOutputStream(f));
/* 537 */           os.print(contents);
/* 538 */           os.close();
/* 539 */           addSolution();
/*     */         } catch (IOException e) {
/* 541 */           System.err.println(e.toString());
/*     */         }
/*     */         
/*     */       }
/* 545 */     });
/* 546 */     write_file.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineWriteOutput(final QueryEngine qe, ModedRuleBaseIndex rb) throws TypeModeError {
/* 550 */     NativePredicate write_output = new NativePredicate("write_output", Type.string);
/*     */     
/* 552 */     write_output.addMode(new Implementation("B", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 554 */         String contents = (String)args[0].up();
/* 555 */         qe.output().print(contents);
/* 556 */         addSolution();
/*     */       }
/*     */       
/* 559 */     });
/* 560 */     write_output.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineFileseparator(ModedRuleBaseIndex rb) throws TypeModeError {
/* 564 */     NativePredicate fileseparator = new NativePredicate("fileseparator", Type.string);
/*     */     
/* 566 */     fileseparator.addMode(new Implementation("F", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 568 */         addSolution(System.getProperty("file.separator"));
/*     */       }
/*     */       
/* 571 */     });
/* 572 */     fileseparator.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineHashValue(ModedRuleBaseIndex rb) throws TypeModeError {
/* 576 */     NativePredicate hash_value = new NativePredicate("hash_value", 
/* 577 */       Type.object, Type.integer);
/*     */     
/* 579 */     hash_value.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 581 */         addSolution(new Integer(args[0].up().hashCode()));
/*     */       }
/*     */       
/* 584 */     });
/* 585 */     hash_value.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineLength(ModedRuleBaseIndex rb) throws TypeModeError {
/* 589 */     NativePredicate length = new NativePredicate("length", 
/* 590 */       Factory.makeListType(Factory.makeTVar("element")), 
/* 591 */       Type.integer);
/*     */     
/* 593 */     length.addMode(new Implementation("BF", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 595 */         Object arg = args[0].up();
/* 596 */         if (((arg instanceof String)) && (arg.equals("[]"))) {
/* 597 */           addSolution(new Integer(0));
/*     */         } else {
/* 599 */           addSolution(new Integer(((Object[])arg).length));
/*     */         }
/*     */         
/*     */       }
/* 603 */     });
/* 604 */     length.addToRuleBase(rb);
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
/*     */   public static void defineReMatch(ModedRuleBaseIndex rb)
/*     */     throws TypeModeError
/*     */   {
/* 622 */     NativePredicate re_match = new NativePredicate("re_match", 
/* 623 */       Factory.makeStrictAtomicType(Factory.makeTypeConstructor(RE.class)), 
/* 624 */       Type.string);
/*     */     
/* 626 */     re_match.addMode(new Implementation("BB", "SEMIDET") {
/*     */       public void doit(RBTerm[] args) {
/* 628 */         RE re = (RE)args[0].up();
/* 629 */         String s = (String)args[1].up();
/* 630 */         if (re.match(s)) {
/* 631 */           addSolution();
/*     */         }
/*     */       }
/* 634 */     });
/* 635 */     re_match.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineConvertTo(ModedRuleBaseIndex rb, final TypeConstructor t) throws TypeModeError
/*     */   {
/* 640 */     NativePredicate convertTo = new NativePredicate("convertTo" + t.getName(), 
/* 641 */       Factory.makeAtomicType(t), Factory.makeStrictAtomicType(t));
/*     */     
/* 643 */     convertTo.addMode(new Implementation("BF", "SEMIDET")
/*     */     {
/*     */       public void doit(RBTerm[] args) {
/* 646 */         if (args[0].isOfType(t)) {
/* 647 */           addSolution(args[0].up());
/*     */         }
/*     */         
/*     */       }
/* 651 */     });
/* 652 */     convertTo.addMode(new Implementation("FB", "DET") {
/*     */       public void doit(RBTerm[] args) {
/* 654 */         addSolution(args[0].up());
/*     */       }
/*     */       
/* 657 */     });
/* 658 */     convertTo.addToRuleBase(rb);
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
/*     */   public static void defineNativePredicates(QueryEngine qe)
/*     */     throws TypeModeError
/*     */   {
/* 695 */     ModedRuleBaseIndex rules = qe.rulebase();
/*     */     
/*     */ 
/* 698 */     defineStringAppend(rules);
/* 699 */     defineStringLength(rules);
/* 700 */     defineStringSplitAtLast(rules);
/* 701 */     defineStringIndexSplit(rules);
/* 702 */     defineStringReplace(rules);
/* 703 */     defineToLowerCase(rules);
/* 704 */     defineToUpperCase(rules);
/* 705 */     defineCapitalize(rules);
/* 706 */     defineDecapitalize(rules);
/* 707 */     defineStringRepeat(rules);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 713 */     defineRange(rules);
/* 714 */     defineGreater(rules);
/* 715 */     defineSum(rules);
/* 716 */     defineMul(rules);
/* 717 */     defineDebugPrint(rules);
/* 718 */     defineThrowError(rules);
/* 719 */     defineWriteFile(rules);
/* 720 */     defineWriteOutput(qe, rules);
/* 721 */     defineHashValue(rules);
/* 722 */     defineLength(rules);
/* 723 */     defineFileseparator(rules);
/*     */     
/*     */ 
/*     */ 
/* 727 */     defineReMatch(rules);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/NativePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */