/*     */ package tyRuBa.engine;
/*     */ 
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
/* 143 */     string_append.addMode(new NativePredicate.1("BBF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */     string_append.addMode(new NativePredicate.2("FFB", "MULTI"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     string_append.addMode(new NativePredicate.3("BFB", "SEMIDET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 170 */     string_append.addMode(new NativePredicate.4("FBB", "SEMIDET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */     string_append.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringLength(ModedRuleBaseIndex rb) throws TypeModeError {
/* 183 */     NativePredicate string_length = new NativePredicate("string_length", 
/* 184 */       Type.string, Type.integer);
/*     */     
/* 186 */     string_length.addMode(new NativePredicate.5("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */     string_length.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringIndexSplit(ModedRuleBaseIndex rb) throws TypeModeError {
/* 197 */     NativePredicate string_index_split = new NativePredicate("string_index_split", 
/* 198 */       Type.integer, Type.string, Type.string, Type.string);
/*     */     
/* 200 */     string_index_split.addMode(new NativePredicate.6("BBFF", "SEMIDET"));
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
/* 211 */     string_index_split.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringSplitAtLast(ModedRuleBaseIndex rb) throws TypeModeError {
/* 215 */     NativePredicate string_split_at_last = new NativePredicate("string_split_at_last", 
/* 216 */       Type.string, Type.string, Type.string, Type.string);
/*     */     
/* 218 */     string_split_at_last.addMode(new NativePredicate.7("BBFF", "DET"));
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
/* 232 */     string_split_at_last.addMode(new NativePredicate.8("BFBB", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 266 */     string_replace.addMode(new NativePredicate.9("BBBF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 275 */     string_replace.addMode(new NativePredicate.10("BBFB", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 284 */     string_replace.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineToLowerCase(ModedRuleBaseIndex rb) throws TypeModeError {
/* 288 */     NativePredicate to_lower_case = new NativePredicate("to_lower_case", 
/* 289 */       Type.string, Type.string);
/*     */     
/* 291 */     to_lower_case.addMode(new NativePredicate.11("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 298 */     to_lower_case.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineToUpperCase(ModedRuleBaseIndex rb) throws TypeModeError {
/* 302 */     NativePredicate to_upper_case = new NativePredicate("to_upper_case", 
/* 303 */       Type.string, Type.string);
/*     */     
/* 305 */     to_upper_case.addMode(new NativePredicate.12("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */     to_upper_case.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineCapitalize(ModedRuleBaseIndex rb) throws TypeModeError {
/* 316 */     NativePredicate capitalize = new NativePredicate("capitalize", 
/* 317 */       Type.string, Type.string);
/*     */     
/* 319 */     capitalize.addMode(new NativePredicate.13("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */     capitalize.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineDecapitalize(ModedRuleBaseIndex rb) throws TypeModeError {
/* 331 */     NativePredicate decapitalize = new NativePredicate("decapitalize", 
/* 332 */       Type.string, Type.string);
/*     */     
/* 334 */     decapitalize.addMode(new NativePredicate.14("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 342 */     decapitalize.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineStringRepeat(ModedRuleBaseIndex rb) throws TypeModeError {
/* 346 */     NativePredicate string_repeat = new NativePredicate("string_repeat", 
/* 347 */       Type.integer, Type.string, Type.string);
/*     */     
/* 349 */     string_repeat.addMode(new NativePredicate.15("BBF", "DET"));
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
/* 362 */     string_repeat.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineTypeTest(ModedRuleBaseIndex rb, PredicateIdentifier id, TypeConstructor t) throws TypeModeError
/*     */   {
/* 367 */     Assert.assertEquals(1, id.getArity());
/* 368 */     String javaName = t.getName();
/* 369 */     NativePredicate type_test = new NativePredicate(id.getName(), Factory.makeAtomicType(t));
/*     */     
/* 371 */     type_test.addMode(new NativePredicate.16("B", "SEMIDET", t));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */     type_test.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineRange(ModedRuleBaseIndex rb) throws TypeModeError {
/* 384 */     NativePredicate range = new NativePredicate("range", 
/* 385 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 387 */     range.addMode(new NativePredicate.17("BBF", "NONDET"));
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
/* 398 */     range.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineGreater(ModedRuleBaseIndex rb) throws TypeModeError {
/* 402 */     NativePredicate greater = new NativePredicate("greater", Type.integer, Type.integer);
/*     */     
/* 404 */     greater.addMode(new NativePredicate.18("BB", "SEMIDET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 413 */     greater.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineSum(ModedRuleBaseIndex rb) throws TypeModeError {
/* 417 */     NativePredicate sum = new NativePredicate("sum", 
/* 418 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 420 */     sum.addMode(new NativePredicate.19("BBF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 428 */     sum.addMode(new NativePredicate.20("BFB", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 436 */     sum.addMode(new NativePredicate.21("FBB", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 444 */     sum.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineMul(ModedRuleBaseIndex rb) throws TypeModeError {
/* 448 */     NativePredicate mul = new NativePredicate("mul", 
/* 449 */       Type.integer, Type.integer, Type.integer);
/*     */     
/* 451 */     mul.addMode(new NativePredicate.22("BBF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 459 */     mul.addMode(new NativePredicate.23("FBB", "SEMIDET"));
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
/* 473 */     mul.addMode(new NativePredicate.24("BFB", "SEMIDET"));
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
/* 497 */     debug_print.addMode(new NativePredicate.25("B", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 505 */     debug_print.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineThrowError(ModedRuleBaseIndex rb) throws TypeModeError {
/* 509 */     NativePredicate throw_error = new NativePredicate("throw_error", Type.string);
/*     */     
/* 511 */     throw_error.addMode(new NativePredicate.26("B", "FAIL"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 518 */     throw_error.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineWriteFile(ModedRuleBaseIndex rb) throws TypeModeError {
/* 522 */     NativePredicate write_file = new NativePredicate("write_file", 
/* 523 */       Type.string, Type.string);
/*     */     
/* 525 */     write_file.addMode(new NativePredicate.27("BB", "SEMIDET"));
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
/* 546 */     write_file.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineWriteOutput(QueryEngine qe, ModedRuleBaseIndex rb) throws TypeModeError {
/* 550 */     NativePredicate write_output = new NativePredicate("write_output", Type.string);
/*     */     
/* 552 */     write_output.addMode(new NativePredicate.28("B", "DET", qe));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 560 */     write_output.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineFileseparator(ModedRuleBaseIndex rb) throws TypeModeError {
/* 564 */     NativePredicate fileseparator = new NativePredicate("fileseparator", Type.string);
/*     */     
/* 566 */     fileseparator.addMode(new NativePredicate.29("F", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 572 */     fileseparator.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineHashValue(ModedRuleBaseIndex rb) throws TypeModeError {
/* 576 */     NativePredicate hash_value = new NativePredicate("hash_value", 
/* 577 */       Type.object, Type.integer);
/*     */     
/* 579 */     hash_value.addMode(new NativePredicate.30("BF", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 585 */     hash_value.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineLength(ModedRuleBaseIndex rb) throws TypeModeError {
/* 589 */     NativePredicate length = new NativePredicate("length", 
/* 590 */       Factory.makeListType(Factory.makeTVar("element")), 
/* 591 */       Type.integer);
/*     */     
/* 593 */     length.addMode(new NativePredicate.31("BF", "DET"));
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
/* 626 */     re_match.addMode(new NativePredicate.32("BB", "SEMIDET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 635 */     re_match.addToRuleBase(rb);
/*     */   }
/*     */   
/*     */   public static void defineConvertTo(ModedRuleBaseIndex rb, TypeConstructor t) throws TypeModeError
/*     */   {
/* 640 */     NativePredicate convertTo = new NativePredicate("convertTo" + t.getName(), 
/* 641 */       Factory.makeAtomicType(t), Factory.makeStrictAtomicType(t));
/*     */     
/* 643 */     convertTo.addMode(new NativePredicate.33("BF", "SEMIDET", t));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 652 */     convertTo.addMode(new NativePredicate.34("FB", "DET"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/NativePredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */