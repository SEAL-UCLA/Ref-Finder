/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ 
/*     */ public class StringNativePredicateTest extends TyrubaTest
/*     */ {
/*     */   public StringNativePredicateTest(String arg0)
/*     */   {
/*  11 */     super(arg0);
/*     */   }
/*     */   
/*     */   public void setUp() throws Exception {
/*  15 */     TyrubaTest.initfile = true;
/*  16 */     super.setUp();
/*     */   }
/*     */   
/*     */   public void testEqual() throws ParseException, TypeModeError {
/*  20 */     test_must_succeed("equals(abc,\"abc\")");
/*     */   }
/*     */   
/*     */   public void testStringAppend() throws ParseException, TypeModeError {
/*  24 */     test_must_succeed("string_append(abc,def,abcdef)");
/*  25 */     test_must_fail("string_append(abc,def,quiwe)");
/*  26 */     test_must_equal("string_append(abc,def,?x)", "?x", "abcdef");
/*  27 */     test_must_equal("string_append(abc,?x,abcdef)", "?x", "def");
/*  28 */     test_must_fail("string_append(abc,?x,abdef)");
/*  29 */     test_must_equal("string_append(?x,def,abcdef)", "?x", "abc");
/*  30 */     test_must_fail("string_append(?x,def,abcddf)");
/*  31 */     test_must_findall("string_append(?x,?y,abcdef)", "?x", 
/*  32 */       new String[] { "\"\"", "a", "ab", "abc", "abcd", "abcde", "abcdef" });
/*  33 */     test_must_equal("string_append(?x,?x,abcabc)", "?x", "abc");
/*     */     
/*  35 */     test_must_equal("string_append(ab,cd,ef,?x)", "?x", "abcdef");
/*     */   }
/*     */   
/*     */   public void testStringAppendTypeCheck() throws ParseException, TypeModeError {
/*  39 */     FrontEnd frontend = new FrontEnd(false);
/*     */     try {
/*  41 */       tyRuBa.util.ElementSource results = 
/*  42 */         frontend.varQuery("string_append(1,2,?x)", "?x");
/*  43 */       fail("this should have thrown type error");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testStringLength() throws ParseException, TypeModeError
/*     */   {
/*  50 */     test_must_succeed("string_length(abc,3)");
/*  51 */     test_must_equal("string_length(abc,?x)", "?x", "3");
/*     */   }
/*     */   
/*     */   public void testStringIndexSplit() throws ParseException, TypeModeError {
/*  55 */     test_must_equal("string_index_split(0,abcdef,?x,?y)", 
/*  56 */       new String[] { "?x", "?y" }, 
/*  57 */       new String[] { "\"\"", "abcdef" });
/*     */     
/*  59 */     test_must_equal("string_index_split(6,abcdef,?x,?y)", 
/*  60 */       new String[] { "?x", "?y" }, 
/*  61 */       new String[] { "abcdef", "\"\"" });
/*     */     
/*  63 */     test_must_equal("string_index_split(3,abcdef,?x,?y)", 
/*  64 */       new String[] { "?x", "?y" }, 
/*  65 */       new String[] { "abc", "def" });
/*     */     
/*  67 */     test_must_succeed("string_index_split(0,abcdef,\"\",abcdef)");
/*  68 */     test_must_succeed("string_index_split(6,abcdef,abcdef,\"\")");
/*  69 */     test_must_succeed("string_index_split(3,abcdef,abc,def)");
/*  70 */     test_must_fail("string_index_split(2,abcdef,abc,def)");
/*     */     
/*  72 */     test_must_fail("string_index_split(7,abcdef,?x,?y)");
/*     */   }
/*     */   
/*     */   public void testStringReplace() throws ParseException, TypeModeError {
/*  76 */     test_must_succeed("string_replace(\".\",\"/\",abc.def.ghi,\"abc/def/ghi\")");
/*  77 */     test_must_succeed("string_replace(\".\",\"/\",abc.def.ghi.,\"abc/def/ghi/\")");
/*  78 */     test_must_succeed(
/*  79 */       "string_replace(\".\",\"/\",\".abc.def.ghi.\",\"/abc/def/ghi/\")");
/*  80 */     test_must_succeed(
/*  81 */       "string_replace(\".\",\"/\",\".abc.def.ghi\",\"/abc/def/ghi\")");
/*     */     
/*  83 */     test_must_equal("string_replace(\".\",\"/\",\".abc.def.ghi\",?x)", 
/*  84 */       "?x", "\"/abc/def/ghi\"");
/*     */     
/*  86 */     test_must_equal("string_replace(\".\",\"/\",?x,\"/abc/def/ghi\")", 
/*  87 */       "?x", "\".abc.def.ghi\"");
/*     */   }
/*     */   
/*     */   public void testStringSplitAtLast() throws ParseException, TypeModeError {
/*  91 */     test_must_succeed("string_split_at_last(l,hello,hel,o)");
/*  92 */     test_must_succeed("string_split_at_last(ll,hello,he,o)");
/*     */     
/*  94 */     test_must_equal("string_split_at_last(l,hello,?start,?end)", "?start", "hel");
/*  95 */     test_must_equal("string_split_at_last(l,hello,?start,?end)", "?end", "o");
/*  96 */     test_must_equal("string_split_at_last(l,?x,hel,o)", "?x", "hello");
/*  97 */     test_must_equal("string_split_at_last(hel,?x,\"\",lo)", "?x", "hello");
/*  98 */     test_must_equal("string_split_at_last(ll,?x,he,o)", "?x", "hello");
/*  99 */     test_must_fail("string_split_at_last(\".\",?x,foo,faa.Foo)");
/* 100 */     test_must_equal("string_split_at_last(\".\",?x,foo.faa,Foo)", "?x", "foo.faa.Foo");
/*     */   }
/*     */   
/*     */   public void testToUpperCase() throws ParseException, TypeModeError {
/* 104 */     test_must_equal("to_upper_case(abcde,?x)", "?x", "ABCDE");
/* 105 */     test_must_succeed("to_upper_case(abcde,ABCDE)");
/* 106 */     test_must_fail("to_upper_case(abcde,AbcDE)");
/*     */   }
/*     */   
/*     */   public void testToLowerCase() throws ParseException, TypeModeError {
/* 110 */     test_must_equal("to_lower_case(abCde,?x)", "?x", "abcde");
/* 111 */     test_must_succeed("to_lower_case(abcde,abcde)");
/* 112 */     test_must_fail("to_lower_case(abCDe,AbCDE)");
/*     */   }
/*     */   
/*     */   public void testCapitalize() throws ParseException, TypeModeError {
/* 116 */     test_must_equal("capitalize(abcd,?x)", "?x", "Abcd");
/* 117 */     test_must_succeed("capitalize(abcd,Abcd)");
/* 118 */     test_must_fail("capitalize(abcd,abcd)");
/* 119 */     test_must_fail("capitalize(\"\",?)");
/*     */   }
/*     */   
/*     */   public void testDecapitalize() throws ParseException, TypeModeError {
/* 123 */     test_must_equal("decapitalize(ABCD,?x)", "?x", "aBCD");
/* 124 */     test_must_succeed("decapitalize(abcd,abcd)");
/* 125 */     test_must_fail("decapitalize(ABCD,ABCD)");
/* 126 */     test_must_fail("decapitalize(\"\",?)");
/*     */   }
/*     */   
/*     */   public void testStringRepeat() throws ParseException, TypeModeError {
/* 130 */     test_must_equal("string_repeat(3,foo,?x)", "?x", "foofoofoo");
/* 131 */     test_must_equal("string_repeat(0,foo,?x)", "?x", "\"\"");
/* 132 */     test_must_fail("string_repeat(-1,foo,?x)");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/StringNativePredicateTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */