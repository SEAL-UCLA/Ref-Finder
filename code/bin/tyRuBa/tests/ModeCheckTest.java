/*     */ package tyRuBa.tests;
/*     */ 
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ 
/*     */ public class ModeCheckTest extends TyrubaTest
/*     */ {
/*     */   public ModeCheckTest(String arg0)
/*     */   {
/*   9 */     super(arg0);
/*     */   }
/*     */   
/*     */   protected void setUp() throws Exception {
/*  13 */     TyrubaTest.initfile = true;
/*  14 */     super.setUp();
/*     */   }
/*     */   
/*     */   public void testBadRule() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/*     */     try {
/*  19 */       this.frontend.parse("foo :: ?x, [?x]\nMODES (F,B) IS NONDET END");
/*     */       
/*  21 */       this.frontend.parse("foo(?x,?lst) :- member(?xx,?lst).");
/*  22 */       fail("This should have thrown a TypeModeError because ?xx never becomes bound");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testBadFact() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/*     */     try {
/*  29 */       this.frontend.parse("append(?x,[],?y).");
/*  30 */       fail("This should have thrown a TypeModeError because in BBF, ?y never becomes bound");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testDisjunction() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError
/*     */   {
/*  37 */     this.frontend.parse("studiesIn, worksIn :: String, String, String\nMODES\n(F,B,B) IS NONDET\n(B,F,F) IS SEMIDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  42 */     this.frontend.parse("staffOrStudent :: String, String, String\nMODES\n(F,B,B) IS NONDET\n(B,F,F) IS NONDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.frontend.parse("studiesIn(Terry,UBC,CPSC).");
/*  49 */     this.frontend.parse("worksIn(Kris,UBC,CPSC).");
/*  50 */     this.frontend.parse("staffOrStudent(?name,?sch,?dept) :- studiesIn(?name,?sch,?dept); worksIn(?name,?sch,?dept).");
/*     */   }
/*     */   
/*     */   public void testBadDisjunction() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError
/*     */   {
/*  55 */     this.frontend.parse("studiesIn, worksIn :: String, String, String\nMODES\n(F,B,B) IS NONDET\n(B,F,F) IS NONDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  60 */     this.frontend.parse("staffOrStudent :: String, String, String\nMODES\n(F,B,B) IS NONDET\n(B,F,F) IS NONDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */     this.frontend.parse("studiesIn(Terry,UBC,CPSC).");
/*  67 */     this.frontend.parse("worksIn(Kris,UBC,CPSC).");
/*     */     try
/*     */     {
/*  70 */       this.frontend.parse("staffOrStudent(?name,?sch,?dept) :- studiesIn(?name,UBC,CPSC); worksIn(?name,?sch,?dept).");
/*     */       
/*  72 */       fail("This should have thrown a TypeModeError: only ?name becomes bound, but ?sch and ?dept remain unbound");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testConjunction() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError
/*     */   {
/*  79 */     this.frontend.parse("friends, friendOfFriend :: String, String\nMODES\n(F,B) IS NONDET\n(B,F) IS NONDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */     this.frontend.parse("friends(Terry,Edith).");
/*  86 */     this.frontend.parse("friends(Edith,Rick).");
/*     */     
/*  88 */     this.frontend.parse("friendOfFriend(?x,?z) :- friends(?x,?y), friends(?y,?z).");
/*     */   }
/*     */   
/*     */   public void testBadConjunction() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/*  92 */     this.frontend.parse("friends, friendOfFriend :: String, String\nMODES\n(F,B) IS NONDET\n(B,F) IS NONDET\nEND\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  99 */       this.frontend.parse("friendOfFriend(?x,?z) :- friends(?xx,?y), friends(?y,?z).");
/* 100 */       fail("This should have thrown a TypeModeError becuase ?x never becomes bound in FB");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */   }
/*     */   
/*     */   public void testWhyWeNeedCovertToNormalForm() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 106 */     test_must_succeed("(sum(1,2,?x); sum(1,2,?y)), sum(?x,?y,5)");
/*     */   }
/*     */   
/*     */   public void testRuleWithNoArgument() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/*     */     try {
/* 111 */       this.frontend.parse("foo :: ()MODES () IS NONDET END");
/*     */       
/* 113 */       fail("This should have thrown a TypeModeError since only FAILURE or SUCCESS can be returned");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */     
/*     */ 
/* 118 */     this.frontend.parse("foo :: ()");
/* 119 */     this.frontend.parse("foo() :- append(?x,?y,[1,2,3]).");
/*     */   }
/*     */   
/*     */   public void testInsertion() throws tyRuBa.parser.ParseException, tyRuBa.modes.TypeModeError {
/* 123 */     this.frontend.parse("foo :: Object \n MODES (F) IS SEMIDET END");
/*     */     
/* 125 */     this.frontend.parse("foo(?x) :- equals(?x,1).");
/*     */     try {
/* 127 */       this.frontend.parse("foo(?x) :- equals(?x,1.1).");
/* 128 */       fail("This should have thrown a TypeModeError since foo(?x) would returnmultiple answers (i.e. ?x=1, ?x=1.1).");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError) {}
/*     */     
/*     */ 
/* 133 */     this.frontend.parse("foo :: Object, Object \n MODES (F,B) IS SEMIDET END");
/*     */     
/* 135 */     this.frontend.parse("foo(?x,?y) :- equals(?x,?y), equals(?y,foo).");
/* 136 */     this.frontend.parse("foo(?x,?y) :- equals(?x,?y), equals(?y,1).");
/* 137 */     this.frontend.parse("foo(?x,?y) :- equals(?x,?y), equals(?y,1.1).");
/*     */     
/* 139 */     this.frontend.parse("foo1 :: Object, Object \n MODES (F,B) IS SEMIDET END");
/*     */     
/* 141 */     this.frontend.parse("foo1(?x,?y) :- equals(?x,?y), equals(?y,1.1).");
/* 142 */     this.frontend.parse("foo1(?x,?y) :- equals(?x,?y), equals(?y,foo).");
/* 143 */     this.frontend.parse("foo1(?x,?y) :- equals(?x,?y), equals(?y,1).");
/*     */     
/* 145 */     this.frontend.parse("foo2 :: Object, Object \n MODES (F,B) IS SEMIDET END");
/*     */     
/* 147 */     this.frontend.parse("foo2(?x,?y) :- equals(?x,?y), equals(?y,foo).");
/*     */     try {
/* 149 */       this.frontend.parse("foo2(?x,?y) :- equals(?x,?y), equals(?y,bar).");
/* 150 */       fail("This should have thrown a TypeModeError since there is already a rule with inferred type String that returns mode SEMIDET");
/*     */     }
/*     */     catch (tyRuBa.modes.TypeModeError localTypeModeError1) {}
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tests/ModeCheckTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */