/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.modes.TypeMapping;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.tdbc.PreparedInsert;
/*     */ 
/*     */ public class TypeTest extends TyrubaTest implements Serializable
/*     */ {
/*     */   public TypeTest(String arg0)
/*     */   {
/*  16 */     super(arg0);
/*     */   }
/*     */   
/*     */   protected void setUp() throws Exception {
/*  20 */     TyrubaTest.initfile = true;
/*  21 */     tyRuBa.engine.RuleBase.useCache = true;
/*  22 */     tyRuBa.engine.RuleBase.silent = true;
/*  23 */     super.setUp();
/*     */   }
/*     */   
/*     */   public void testIllegalCastToAbstractType() throws Exception {
/*  27 */     this.frontend.parse(
/*  28 */       "TYPE RefType AS String TYPE PrimType AS String TYPE Type = RefType | PrimType");
/*     */     
/*     */ 
/*     */ 
/*  32 */     this.frontend.parse(
/*  33 */       "type :: Type MODES (F) IS NONDET END ");
/*     */     
/*     */ 
/*  36 */     this.frontend.parse(
/*  37 */       "type(foo.Bar::RefType).");
/*     */     try
/*     */     {
/*  40 */       test_must_equal("type(?x),equals(?x,?s::Type)", "?s", "foo.Bar");
/*  41 */       fail("Should throw a TypeModeError");
/*     */     }
/*     */     catch (TypeModeError e) {
/*  44 */       assertTrue("Wrong error message:" + e.getMessage(), e.getMessage().indexOf("Illegal cast") >= 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public void testMixingCompositeAndAtomicTypes()
/*     */     throws Exception
/*     */   {
/*  51 */     this.frontend.parse(
/*  52 */       "TYPE Element AS String ");
/*     */     
/*  54 */     this.frontend.parse(
/*  55 */       "name :: Element, String MODES (B,F) IS SEMIDET END ");
/*     */     
/*     */ 
/*  58 */     this.frontend.parse(
/*  59 */       "re_name :: Element, org.apache.regexp.RE ");
/*     */     
/*  61 */     this.frontend.parse(
/*  62 */       "TYPE Pattern     = RegExpPat\t             | Name              \t| StarPat              \t| SubtypePat ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  67 */     this.frontend.parse(
/*  68 */       "TYPE RegExpPat AS RegExp TYPE Name AS String TYPE StarPat AS <> TYPE SubtypePat AS Pattern ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  73 */     this.frontend.parse(
/*  74 */       "match :: Pattern, Element match(?re::RegExpPat, ?X)   :- re_name(?X, ?re). match(?S::Name, ?X)         :- name(?X, ?S). match(StarPat<>, ?X)        :- Element(?X). ");
/*     */     
/*     */ 
/*     */ 
/*  78 */     this.frontend.parse(
/*  79 */       "match(?P::SubtypePat, ?X)   :- match(?P,?X). ");
/*     */   }
/*     */   
/*     */   public void testCompositeSubTypes() throws Exception
/*     */   {
/*  84 */     this.frontend.parse(
/*  85 */       "TYPE Element AS String name :: Element, String MODES (B,F) IS SEMIDET END re_name :: Element, org.apache.regexp.RE TYPE Pattern<>   = RegExpPat<>             | Name<>              | StarPat<>              | SubtypePat<> TYPE RegExpPat<> AS <org.apache.regexp.RE>TYPE Name<> AS <String> TYPE StarPat<> AS <> TYPE SubtypePat<> AS <Pattern<>> ");
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
/* 101 */     this.frontend.parse(
/* 102 */       "match :: Pattern<>, Element match(RegExpPat<?re>, ?X)   :- re_name(?X, ?re). match(Name<?S>, ?X)         :- name(?X, ?S). match(StarPat<>, ?X)        :- Element(?X). ");
/*     */     
/*     */ 
/*     */ 
/* 106 */     this.frontend.parse(
/* 107 */       "match(SubtypePat<?P>, ?X)   :- match(?P,?X). ");
/*     */   }
/*     */   
/*     */   public void testBadAppend1() throws ParseException, TypeModeError {
/*     */     try {
/* 112 */       this.frontend.parse("append(?x,abc,?x).");
/* 113 */       fail("This should have thrown a TypeModeError because \"abc\" does nothave type [?t]");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testBadAppend2() throws ParseException, TypeModeError
/*     */   {
/*     */     try {
/* 121 */       this.frontend.parse("append([?x|?xs],?ys,[?x|?zs]) :- append(?x,?ys,?zs).");
/* 122 */       fail("This should have thrown a TypeModeError because ?x is not a list");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testEmptyList() throws ParseException, TypeModeError {
/* 128 */     this.frontend.parse("list :: [?x]\nMODES (FREE) IS NONDET END");
/*     */     
/* 130 */     this.frontend.parse("list([]).");
/*     */   }
/*     */   
/*     */   public void testDeclarationArity() throws ParseException, TypeModeError {
/*     */     try {
/* 135 */       this.frontend.parse("planet :: String\nMODES\n(BOUND, BOUND, FREE) IS DET\n(FREE, FREE, BOUND) IS MULTI\nEND\n");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */       fail("This should have thrown a TypeModeError because planet has arity 1but the mode declarations have arities 3");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testUndefinedPredError() throws ParseException, TypeModeError
/*     */   {
/*     */     try {
/* 149 */       this.frontend.parse("plannet(Earth).");
/* 150 */       fail("This should have thrown a TypeModeError because plannet is not a declaredpredicate");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testStrictType() throws ParseException, TypeModeError
/*     */   {
/*     */     try {
/* 158 */       this.frontend.parse("isSum :: Integer, Integer, Integer");
/* 159 */       this.frontend.parse("isSum(?x,?y,?z) :-  sum(?x,?y,?z).");
/* 160 */       fail("This should fail because isSum must have strict(=) types");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     try
/*     */     {
/* 165 */       this.frontend.parse("foooo :: [Integer]");
/* 166 */       this.frontend.parse("foooo([?x,?y,?z]) :-  sum(?x,?y,?z).");
/* 167 */       fail("This should fail because foooo must have strict(=) types");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */     try
/*     */     {
/* 172 */       this.frontend.parse("sumLost :: [Integer], Integer");
/* 173 */       this.frontend.parse("sumLost([],0).");
/* 174 */       this.frontend.parse("sumLost([?x|?xs],?sum) :-  sumLost(?xs,?rest), sum(?x,?rest,?sum).");
/*     */       
/* 176 */       fail("This should fail because isSum must have strict(=) types");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError3) {}
/*     */     
/* 180 */     this.frontend.parse("sameObject :: =Object, =Object MODES (F,B) IS DET       (B,F) IS DET END");
/*     */     
/*     */ 
/*     */ 
/* 184 */     this.frontend.parse("sameObject(?x,?x).");
/* 185 */     test_must_succeed("equals(?x,foo),sameObject(?x,?x)");
/*     */   }
/*     */   
/*     */   public void testEmptyListRule1() throws ParseException, TypeModeError {
/* 189 */     this.frontend.parse("foo :: Integer, [Integer] \nMODES (B,F) IS NONDET END");
/*     */     
/* 191 */     this.frontend.parse("foo(?T,?X) :- equals(?X,[]), Integer(?T), NOT(equals(?T,1)); equals(?X,[1]), Integer(?T).");
/*     */   }
/*     */   
/*     */   public void testEmptyListRule2() throws ParseException, TypeModeError
/*     */   {
/* 196 */     this.frontend.parse("foo :: Integer, [Integer] \nMODES (B,F) IS MULTI END");
/*     */     
/* 198 */     this.frontend.parse("foo(?T,[]) :- Integer(?T), NOT(equals(?T,1)).");
/* 199 */     this.frontend.parse("foo(?T,[?T]) :- Integer(?T).");
/*     */   }
/*     */   
/*     */   public void testStrictTypesInRules() throws ParseException, TypeModeError {
/* 203 */     this.frontend.parse("inEither :: ?a,[?a],[?a]\nMODES (F,B,B) IS NONDET END");
/*     */     
/* 205 */     this.frontend.parse("inEither(?x,?l1,?l2) :- member(?x,?l1); member(?x,?l2).");
/*     */     
/* 207 */     this.frontend.parse("inEitherOne :: ?a,[?a],[?a]\nMODES (F,B,B) IS NONDET END\n");
/*     */     
/* 209 */     this.frontend.parse("inEitherOne(?x,?l1,?l2) :- member(?x,?l1).");
/* 210 */     this.frontend.parse("inEitherOne(?x,?l1,?l2) :- member(?x,?l2).");
/*     */     
/* 212 */     this.frontend.parse("inAny :: ?a,[[?a]]\nMODES (F,B) IS NONDET END");
/*     */     
/* 214 */     this.frontend.parse("inAny(?x,[?l1|?lmore]) :- member(?x,?l1); inAny(?x,?lmore).");
/*     */     
/* 216 */     test_must_findall("inEither(?x,[1,2,3],[4,5,6])", "?x", 
/* 217 */       new String[] { "1", "2", "3", "4", "5", "6" });
/* 218 */     test_must_findall("inEitherOne(?x,[1,2,3],[4,5,6])", "?x", 
/* 219 */       new String[] { "1", "2", "3", "4", "5", "6" });
/* 220 */     test_must_findall("inAny(?x,[[1],[2,3],[4,5,6]])", "?x", 
/* 221 */       new String[] { "1", "2", "3", "4", "5", "6" });
/*     */   }
/*     */   
/*     */   public void testStrictTypesWithShrinkingTypes() throws ParseException, TypeModeError {
/*     */     try {
/* 226 */       test_must_fail("sum(?x,?x,?x), member(?x,[0,1,a]), Integer(?x)");
/* 227 */       fail("This should have thrown a TypeModeError since sum requires ?x to be strictly Integer");
/*     */     }
/*     */     catch (TypeModeError e) {
/* 230 */       System.err.println(e.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 234 */       test_must_fail("member(?x,[0,1,a]), Integer(?x), sum(?x,?x,?x)");
/* 235 */       fail("This should have thrown a TypeModeError since sum requires ?x to be strictly Integer");
/*     */     }
/*     */     catch (TypeModeError e) {
/* 238 */       System.err.println(e.getMessage());
/*     */     }
/*     */     try
/*     */     {
/* 242 */       test_must_fail("Integer(?x), sum(?x,?x,?x), member(?x,[0,1,a])");
/* 243 */       fail("This should have thrown a TypeModeError since sum requires ?x to be strictly Integer");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */ 
/*     */ 
/* 249 */     test_must_succeed("Integer(?x), sum(?x,?x,?x), member(?x,[0,1,2])");
/*     */   }
/*     */   
/*     */   public void testTypeTests() throws ParseException, TypeModeError {
/* 253 */     test_must_succeed("Integer(1)");
/* 254 */     test_must_succeed("Number(1.1)");
/* 255 */     test_must_fail("list_ref(0,[0,a],?x), String(?x)");
/*     */     
/* 257 */     this.frontend.parse("TYPE Sno AS String");
/* 258 */     this.frontend.parse("TYPE Bol AS String");
/* 259 */     this.frontend.parse("TYPE Snobol = Sno | Bol");
/* 260 */     test_must_succeed("Sno(sno::Sno)");
/* 261 */     test_must_succeed("Snobol(bol::Bol)");
/* 262 */     test_must_succeed("Snobol(sno::Sno)");
/* 263 */     test_must_fail("list_ref(0,[sno::Sno,bol::Bol],?x), Bol(?x)");
/*     */     
/* 265 */     this.frontend.parse("TYPE Bar AS String");
/*     */   }
/*     */   
/*     */   public void testListType() throws ParseException, TypeModeError {
/* 269 */     this.frontend.parse(
/* 270 */       "descriptorImg :: [String], String MODES    (B,F) IS SEMIDET END ");
/*     */     
/*     */ 
/*     */ 
/* 274 */     this.frontend.parse(
/* 275 */       "descriptorImg([compilationUnit], DESC_OBJS_CUNIT).");
/*     */   }
/*     */   
/*     */ 
/*     */   public void testListType2()
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 282 */     this.frontend.parse(
/* 283 */       "abc :: [String] MODES    (F) IS MULTI END ");
/*     */     
/*     */ 
/*     */ 
/* 287 */     this.frontend.parse(
/* 288 */       "abc([a,b,?c]) :- equals(?c,c).");
/* 289 */     this.frontend.parse(
/* 290 */       "abc(?x) :- equals(?x,[a,b,?c]), equals(?c,c).");
/* 291 */     this.frontend.parse(
/* 292 */       "abc(?x) :- equals(?c,c), equals(?x,[a,b,?c]).");
/*     */   }
/*     */   
/*     */   public void testUserListType() throws ParseException, TypeModeError
/*     */   {
/* 297 */     this.frontend.parse("TYPE Package AS String ");
/* 298 */     this.frontend.parse("TYPE CU AS String ");
/* 299 */     this.frontend.parse("TYPE Element = Package | CU ");
/*     */     
/* 301 */     this.frontend.parse(
/* 302 */       "child :: Element, Element MODES   (F,F) IS NONDET   (B,F) IS NONDET   (F,B) IS SEMIDET END");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 309 */     this.frontend.parse(
/* 310 */       "viewFromHere :: Element, [Element] MODES \t(B,F) IS NONDET END");
/*     */     
/*     */ 
/*     */ 
/* 314 */     this.frontend.parse(
/* 315 */       "viewFromHere(?X,?ViewOfChild) :-     child(?X,?Child), viewFromHere(?Child,?ChildsView),     equals(?ViewOfChild, [?Child | ?ChildsView]). ");
/*     */     
/*     */ 
/* 318 */     this.frontend.parse(
/* 319 */       "viewFromHere(?X, []) :- Element(?X), NOT(child(?X,?)).");
/*     */   }
/*     */   
/*     */   public void testCompositeType() throws ParseException, TypeModeError {
/* 323 */     this.frontend.parse("TYPE Tree<?key,?value> = Node<?key,?value>| Leaf<?value>| EmptyTree<>| WeirdTree<?value>");
/*     */     
/*     */ 
/*     */ 
/* 327 */     this.frontend.parse(
/* 328 */       "TYPE Node<?key,?value> AS <?key, Tree<?key,?value>,Tree<?key,?value>>");
/* 329 */     this.frontend.parse("TYPE Leaf<?value> AS <?value>");
/* 330 */     this.frontend.parse("TYPE EmptyTree<> AS <>");
/* 331 */     this.frontend.parse("TYPE WeirdTree<?value> AS [?value]");
/*     */     
/* 333 */     this.frontend.parse("sumLeaf :: Tree<?x,=Integer>, =Integer \nMODES (B,F) IS SEMIDET END");
/*     */     
/* 335 */     this.frontend.parse("sumLeaf(Leaf<?value>,?value).");
/* 336 */     this.frontend.parse("sumLeaf(EmptyTree<>,0).");
/* 337 */     this.frontend.parse("sumLeaf(WeirdTree[],0).");
/* 338 */     this.frontend.parse("sumLeaf(WeirdTree[?l|?ist],?sum) :- sumList(?ist,?istSum), sum(?l,?istSum,?sum).");
/*     */     
/* 340 */     this.frontend.parse("sumLeaf(Node<?key,?left,?right>,?sum):- sum(?leftSum,?rightSum,?sum), sumLeaf(?left,?leftSum),sumLeaf(?right,?rightSum).");
/*     */     
/*     */ 
/*     */ 
/* 344 */     this.frontend.parse("fringe :: Tree<?k,?v>, [=Leaf<?v>]\nMODES (B,F) IS DET END");
/*     */     
/*     */ 
/* 347 */     this.frontend.parse("fringe(EmptyTree<>,[]).");
/* 348 */     this.frontend.parse("fringe(Leaf<?v>,[Leaf<?v>]).");
/* 349 */     this.frontend.parse("fringe(Node<?k,?t1,?t2>,?fringe) :-fringe(?t1,?f1),fringe(?t2,?f2),append(?f1,?f2,?fringe).");
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 354 */       test_must_fail("sumLeaf(Node<1,1>,0)");
/* 355 */       fail("This should have thrown a TypeModeError since Node<?key,?value> has constructor with arity 3");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError1) {}
/*     */     
/*     */ 
/* 360 */     test_must_succeed("sumLeaf(Leaf<1>,1)");
/* 361 */     test_must_fail("sumLeaf(Leaf<1>,2)");
/* 362 */     test_must_succeed("sumLeaf(EmptyTree<>,0)");
/* 363 */     test_must_fail("sumLeaf(EmptyTree<>,1)");
/* 364 */     test_must_succeed("sumLeaf(WeirdTree[],0)");
/* 365 */     test_must_succeed("sumLeaf(WeirdTree[1],1)");
/* 366 */     test_must_succeed("sumLeaf(WeirdTree[1,2,3],6)");
/* 367 */     test_must_fail("sumLeaf(WeirdTree[1,2,3],7)");
/* 368 */     test_must_succeed("sumLeaf(Node<1,EmptyTree<>,EmptyTree<>>,0)");
/* 369 */     test_must_succeed("sumLeaf(Node<1,EmptyTree<>,Leaf<2>>,2)");
/* 370 */     test_must_succeed("sumLeaf(Node<1,Leaf<3>,EmptyTree<>>,3)");
/* 371 */     test_must_succeed("sumLeaf(Node<1,Leaf<1>,Leaf<3>>,4)");
/*     */     try {
/* 373 */       test_must_fail("sumLeaf(Leaf<abc>,0)");
/* 374 */       fail("This should have thrown a TypeModeError since Leaf<abc> is of type Tree<?x,String> which is incompatible with Tree<Integer,Integer>");
/*     */     }
/*     */     catch (TypeModeError localTypeModeError2) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public void testUserDefinedListType2()
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 383 */     this.frontend.parse(
/* 384 */       "TYPE Type AS String TYPE Method AS String TYPE Element = Type | Method method :: Type, Method MODES(F,F) IS NONDET (B,F) IS NONDET (F,B) IS SEMIDET END");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 394 */     this.frontend.parse(
/* 395 */       "signature :: Method, String MODES (F,F) IS NONDET (B,F) IS SEMIDET (F,B) IS NONDET END ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 402 */     this.frontend.parse(
/* 403 */       "methodizeHierarchy :: String, [Type], [Element] MODES \t(B,B,F) REALLY IS DET END ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 408 */     this.frontend.parse(
/* 409 */       "methodizeHierarchy(?sig,[],[]) :- String(?sig). ");
/*     */     
/* 411 */     this.frontend.parse(
/* 412 */       "methodizeHierarchy(?sig,[?C1|?CH],?mH) :-    Type(?C1),   NOT( EXISTS ?m : method(?C1,?m), signature(?m,?sig) ),   methodizeHierarchy(?sig,?CH,?mH). ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 417 */     this.frontend.parse(
/* 418 */       "methodizeHierarchy(?sig,[?C1|?CH],[?C1,?m|?mH]) :-   method(?C1,?m), signature(?m,?sig),   methodizeHierarchy(?sig,?CH,?mH).");
/*     */   }
/*     */   
/*     */ 
/*     */   public void testUserDefinedListType()
/*     */     throws TypeModeError, ParseException
/*     */   {
/* 425 */     this.frontend.parse("TYPE List<?element> = NonEmptyList<?element> | EmptyList<>");
/* 426 */     this.frontend.parse("TYPE NonEmptyList<?element> AS <?element,List<?element>>");
/* 427 */     this.frontend.parse("TYPE EmptyList<> AS <>");
/*     */     
/* 429 */     this.frontend.parse("append1 :: List<?x>, List<?x>, List<?x>\nMODES\n(B,B,F) IS DET\n(F,F,B) IS NONDET\nEND");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 435 */     this.frontend.parse("append1(EmptyList<>,?x,?x).");
/* 436 */     this.frontend.parse("append1(NonEmptyList<?x,?xs>,?y,NonEmptyList<?x,?xys>):- append1(?xs,?y,?xys).");
/*     */     
/*     */ 
/* 439 */     test_must_succeed("append1(EmptyList<>,EmptyList<>,EmptyList<>)");
/* 440 */     test_must_succeed("append1(EmptyList<>,NonEmptyList<1,EmptyList<>>,NonEmptyList<1,EmptyList<>>)");
/*     */     
/* 442 */     test_must_succeed("append1(NonEmptyList<a,EmptyList<>>,NonEmptyList<1,EmptyList<>>,NonEmptyList<a,NonEmptyList<1,EmptyList<>>>)");
/*     */     
/* 444 */     test_must_findall("append1(?x,?y,NonEmptyList<1,NonEmptyList<2,EmptyList<>>>)", 
/* 445 */       "?x", new String[] {
/* 446 */       "EmptyList<>", 
/* 447 */       "NonEmptyList<1,EmptyList<>>", 
/* 448 */       "NonEmptyList<1,NonEmptyList<2,EmptyList<>>>" });
/*     */     
/*     */ 
/* 451 */     this.frontend.parse("sumList1 :: List<=Integer>, =Integer\nMODES (B,F) IS DET END");
/*     */     
/* 453 */     this.frontend.parse("sumList1(EmptyList<>,0).");
/* 454 */     this.frontend.parse("sumList1(NonEmptyList<?l,?ist>,?sum):- sumList1(?ist,?sumIst), sum(?l,?sumIst,?sum).");
/*     */     
/*     */ 
/* 457 */     test_must_equal("sumList1(EmptyList<>,?x)", "?x", "0");
/* 458 */     test_must_equal("sumList1(NonEmptyList<1,EmptyList<>>,?x)", "?x", "1");
/* 459 */     test_must_equal("append1(NonEmptyList<1,EmptyList<>>,NonEmptyList<2,EmptyList<>>,?list), sumList1(?list,?x)", 
/* 460 */       "?x", "3");
/*     */   }
/*     */   
/*     */   public static class SourceLocation implements Serializable { private static final long serialVersionUID = 1L;
/*     */     protected String f1;
/*     */     protected int f2;
/*     */     protected int f3;
/*     */     protected int f4;
/*     */     
/* 469 */     public SourceLocation(String parseFrom) { int lparAt = parseFrom.indexOf('(');
/* 470 */       int comma1 = parseFrom.indexOf(',', lparAt);
/* 471 */       int comma2 = parseFrom.indexOf(',', comma1 + 1);
/* 472 */       int rparAt = parseFrom.indexOf(')', comma2);
/*     */       
/* 474 */       this.f1 = parseFrom.substring(0, lparAt);
/* 475 */       this.f2 = Integer.parseInt(parseFrom.substring(lparAt + 1, comma1));
/* 476 */       this.f3 = Integer.parseInt(parseFrom.substring(comma1 + 1, comma2));
/* 477 */       this.f4 = Integer.parseInt(parseFrom.substring(comma2 + 1, rparAt));
/*     */     }
/*     */     
/*     */     public SourceLocation(String f1, int f2, int f3, int f4) {
/* 481 */       this.f1 = f1;
/* 482 */       this.f2 = f2;
/* 483 */       this.f3 = f3;
/* 484 */       this.f4 = f4;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean equals(Object _other)
/*     */     {
/* 493 */       if (!getClass().equals(_other.getClass()))
/* 494 */         return false;
/* 495 */       SourceLocation other = (SourceLocation)_other;
/*     */       
/*     */ 
/*     */ 
/* 499 */       return (this.f1.equals(other.f1)) && (this.f2 == other.f2) && (this.f3 == other.f3) && (this.f4 == other.f4);
/*     */     }
/*     */   }
/*     */   
/*     */   public void testMappedCompositeType() throws ParseException, TypeModeError, tyRuBa.tdbc.TyrubaException
/*     */   {
/* 505 */     this.frontend.parse("TYPE SourceLocation<> AS <String,Integer,Integer,Integer> TYPE SourceRange<>    AS <String,Integer,Integer,Integer,String> TYPE SourceLink<> = SourceLocation<> | SourceRange<>  sourceLoc :: String, SourceLocation<> PERSISTENT MODES (F,F) IS NONDET END backLoc :: Object, String MODES (F,F) IS NONDET END backLoc(?X,?Y) :- sourceLoc(?Y,?X). label :: Object, String");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 515 */     this.frontend.addTypeMapping(new tyRuBa.engine.FunctorIdentifier("SourceLocation", 0), new TypeMapping()
/*     */     {
/*     */       public Class getMappedClass() {
/* 518 */         return TypeTest.SourceLocation.class;
/*     */       }
/*     */       
/*     */       public Object toTyRuBa(Object obj) {
/* 522 */         TypeTest.SourceLocation sl_obj = (TypeTest.SourceLocation)obj;
/* 523 */         return new Object[] {
/* 524 */           sl_obj.f1, 
/* 525 */           new Integer(sl_obj.f2), 
/* 526 */           new Integer(sl_obj.f3), 
/* 527 */           new Integer(sl_obj.f4) };
/*     */       }
/*     */       
/*     */       public Object toJava(Object _parts)
/*     */       {
/* 532 */         Object[] parts = (Object[])_parts;
/* 533 */         return new TypeTest.SourceLocation(
/* 534 */           (String)parts[0], 
/* 535 */           ((Integer)parts[1]).intValue(), 
/* 536 */           ((Integer)parts[2]).intValue(), 
/* 537 */           ((Integer)parts[3]).intValue());
/*     */       }
/*     */       
/* 540 */     });
/* 541 */     this.frontend.parse("label(SourceLocation<?,?,?,?>,tisASourceLocation).");
/* 542 */     this.frontend.parse("label(SourceLocation<foo,1,2,3>,niceOne).");
/*     */     
/* 544 */     SourceLocation sl1 = new SourceLocation("foo", 1, 2, 3);
/* 545 */     SourceLocation sl2 = new SourceLocation("bar", 2, 3, 4);
/*     */     
/* 547 */     PreparedInsert insertIt = this.frontend.prepareForInsertion("sourceLoc(!name,!loc)");
/* 548 */     insertIt.put("!name", "foo");
/* 549 */     insertIt.put("!loc", sl1);
/* 550 */     insertIt.executeInsert();
/*     */     
/* 552 */     insertIt.put("!name", "bar");
/* 553 */     insertIt.put("!loc", sl2);
/* 554 */     insertIt.executeInsert();
/*     */     
/* 556 */     assertEquals(this.frontend.getProperty("foo", "sourceLoc").up(), sl1);
/* 557 */     assertEquals(this.frontend.getProperty("bar", "sourceLoc").up(), sl2);
/* 558 */     assertFalse(this.frontend.getProperty("foo", "sourceLoc").up().equals(sl2));
/* 559 */     assertFalse(this.frontend.getProperty("bar", "sourceLoc").up().equals(sl1));
/*     */     
/* 561 */     assertEquals(this.frontend.getProperty(sl1, "backLoc").up(), "foo");
/*     */   }
/*     */   
/*     */   public void testJavaTypeConstructor() throws Exception
/*     */   {
/* 566 */     this.frontend.parse(
/* 567 */       "sourceLoc :: String, tyRuBa.tests.TypeTest$SourceLocation PERSISTENT MODES (F,F) IS NONDET END ");
/*     */     
/*     */ 
/* 570 */     this.frontend.parse(
/* 571 */       "sourceLoc(foo,\"foo(1,2,3)\"::tyRuBa.tests.TypeTest$SourceLocation).");
/*     */     
/* 573 */     SourceLocation sl = new SourceLocation("foo", 1, 2, 3);
/*     */     
/* 575 */     assertEquals(this.frontend.getProperty("foo", "sourceLoc").up(), sl);
/*     */   }
/*     */   
/*     */   public void testSameStringUserDefinedTypes()
/*     */     throws Exception
/*     */   {
/* 581 */     String[] vehics = {
/* 582 */       "Bike", "Car", "Automobile", "Big#Automobile", "#Car", "Big#" };
/*     */     
/* 584 */     String typeDec = "TYPE Vehicle = ";
/* 585 */     for (int i = 0; i < vehics.length; i++) {
/* 586 */       this.frontend.parse("TYPE " + vehics[i] + " AS String");
/* 587 */       if (i != 0) typeDec = typeDec + " | ";
/* 588 */       typeDec = typeDec + vehics[i];
/*     */     }
/* 590 */     this.frontend.parse(typeDec);
/*     */     
/* 592 */     this.frontend.parse("vehicle :: Vehicle PERSISTENT MODES   (F) IS NONDET END ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 597 */     for (int i = 0; i < vehics.length; i++) {
/* 598 */       this.frontend.parse("vehicle(Foo::" + vehics[i] + ").");
/* 599 */       this.frontend.parse("vehicle(" + vehics[i] + "::" + vehics[i] + ").");
/*     */     }
/*     */     
/* 602 */     test_resultcount("vehicle(?x)", 2 * vehics.length);
/*     */     
/* 604 */     for (int i = 0; i < vehics.length; i++) {
/* 605 */       for (int j = 0; j < vehics.length; j++) {
/* 606 */         String query = "vehicle(" + vehics[i] + "::" + vehics[j] + ")";
/* 607 */         if (i == j) {
/* 608 */           test_must_succeed(query);
/*     */         } else {
/* 610 */           test_must_fail(query);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/TypeTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */