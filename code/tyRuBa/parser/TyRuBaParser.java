/*      */ package tyRuBa.parser;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import tyRuBa.engine.QueryEngine;
/*      */ import tyRuBa.engine.RBExpression;
/*      */ import tyRuBa.engine.RBTerm;
/*      */ import tyRuBa.modes.TVarFactory;
/*      */ import tyRuBa.modes.Type;
/*      */ import tyRuBa.modes.TypeModeError;
/*      */ 
/*      */ public class TyRuBaParser implements TyRuBaParserConstants
/*      */ {
/*      */   private PrintStream outputStream;
/*   15 */   private java.net.URL baseURL = null;
/*      */   private boolean interactive;
/*      */   public TyRuBaParserTokenManager token_source;
/*      */   
/*   19 */   public TyRuBaParser(java.io.InputStream is, PrintStream os) { this(is, os, null); }
/*      */   
/*      */   public TyRuBaParser(java.io.InputStream is, PrintStream os, java.net.URL base)
/*      */   {
/*   23 */     this(is);
/*   24 */     this.outputStream = os;
/*      */     
/*   26 */     this.interactive = (is == System.in);
/*      */   }
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
/*      */   JavaCharStream jj_input_stream;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Token token;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void parse(QueryEngine rules, String fileName, PrintStream os)
/*      */     throws ParseException, java.io.IOException, TypeModeError
/*      */   {
/*   61 */     parse(rules, new java.io.File(fileName).toURL(), os);
/*      */   }
/*      */   
/*      */   public static void parse(QueryEngine rules, java.io.InputStream is, PrintStream os)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*   67 */     TyRuBaParser parser = new TyRuBaParser(is, os);
/*   68 */     parser.CompilationUnit(rules);
/*      */   }
/*      */   
/*      */   public static RBExpression parseExpression(java.io.InputStream is, PrintStream os, QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*   73 */     TyRuBaParser parser = new TyRuBaParser(is, os);
/*   74 */     return parser.ExpressionAndEOF(rules);
/*      */   }
/*      */   
/*      */ 
/*      */   static String internalStringLiteral(String src)
/*      */   {
/*   80 */     StringBuffer trg = new StringBuffer(src.length());
/*   81 */     for (int i = 0; i < src.length(); i++) {
/*   82 */       if (src.charAt(i) == '\\') {
/*   83 */         i++;
/*      */         
/*      */ 
/*      */ 
/*   87 */         trg.append(src.charAt(i));
/*      */       }
/*      */       else {
/*   90 */         trg.append(src.charAt(i));
/*      */       }
/*      */     }
/*   93 */     return trg.toString();
/*      */   }
/*      */   
/*      */   static String stringLiteral(String src)
/*      */   {
/*   98 */     return internalStringLiteral(stripQuotes(src));
/*      */   }
/*      */   
/*      */   static String stripQuotes(String src)
/*      */   {
/*  103 */     return src.substring(1, src.length() - 1);
/*      */   }
/*      */   
/*      */   static String javaClassName(String classToken)
/*      */   {
/*  108 */     if (classToken.endsWith("[]")) {
/*  109 */       return "[L" + classToken.substring(1, classToken.length() - 2) + ";";
/*      */     }
/*  111 */     return classToken.substring(1);
/*      */   }
/*      */   
/*      */   private static String undoubleQuestionMarks(String src)
/*      */   {
/*  116 */     StringBuffer trg = new StringBuffer(src.length());
/*  117 */     for (int i = 0; i < src.length(); i++) {
/*  118 */       if ((src.charAt(i) == '?') && (i + 1 < src.length()) && 
/*  119 */         (src.charAt(i + 1) == '?'))
/*      */       {
/*  121 */         trg.append(src.charAt(i++));
/*      */       }
/*      */       else
/*  124 */         trg.append(src.charAt(i));
/*      */     }
/*  126 */     return trg.toString();
/*      */   }
/*      */   
/*      */ 
/*      */   private static RBTerm makeQuotedCodeName(String s, int startName)
/*      */   {
/*  132 */     int startVar = s.indexOf('?', startName);
/*  133 */     while ((startVar < s.length()) && (s.charAt(startVar + 1) == '?'))
/*      */     {
/*  135 */       startVar = s.indexOf('?', startVar + 2);
/*      */     }
/*  137 */     if (startVar == -1) {
/*  138 */       if (s.length() - startName > 1) {
/*  139 */         return tyRuBa.engine.FrontEnd.makeName(
/*  140 */           undoubleQuestionMarks(s.substring(startName, s.length() - 1)));
/*      */       }
/*  142 */       return tyRuBa.engine.FrontEnd.theEmptyList;
/*      */     }
/*      */     
/*      */ 
/*  146 */     if (startVar == startName)
/*      */     {
/*  148 */       return makeQuotedCodeVar(s, startVar);
/*      */     }
/*      */     
/*  151 */     RBTerm car = tyRuBa.engine.FrontEnd.makeName(
/*  152 */       undoubleQuestionMarks(s.substring(startName, startVar)));
/*  153 */     return new tyRuBa.engine.RBPair(car, makeQuotedCodeVar(s, startVar));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static RBTerm makeQuotedCodeVar(String s, int startVar)
/*      */   {
/*  161 */     int startName = startVar + 1;
/*  162 */     while ((startName < s.length() - 1) && (
/*  163 */       Character.isJavaIdentifierPart(s.charAt(startName))))
/*  164 */       startName++;
/*  165 */     tyRuBa.engine.RBVariable car = tyRuBa.engine.FrontEnd.makeVar(s.substring(startVar, startName));
/*  166 */     return new tyRuBa.engine.RBPair(car, makeQuotedCodeName(s, startName));
/*      */   }
/*      */   
/*      */   static RBTerm makeQuotedCode(String s)
/*      */   {
/*  171 */     return new tyRuBa.engine.RBQuoted(makeQuotedCodeName(s, 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void CompilationUnit(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*      */     for (;;)
/*      */     {
/*  184 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */       {
/*      */       case 13: 
/*      */       case 14: 
/*      */       case 26: 
/*      */       case 54: 
/*      */       case 60: 
/*      */         break;
/*      */       default: 
/*  193 */         this.jj_la1[0] = this.jj_gen;
/*  194 */         break;
/*      */       }
/*  196 */       if (jj_2_1(2)) {
/*  197 */         PredInfoRules(rules);
/*  198 */         if (this.interactive)
/*  199 */           System.err.println("** predicate info added to db **");
/*      */       } else {
/*  201 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */         case 26: 
/*  203 */           UserDefinedTypeDeclaration(rules);
/*  204 */           if (this.interactive)
/*  205 */             System.err.println("** I have defined your type! **");
/*  206 */           break;
/*      */         case 60: 
/*  208 */           Rule(rules);
/*  209 */           if (this.interactive)
/*  210 */             System.err.println("** assertion added to rulebase **");
/*  211 */           break;
/*      */         case 54: 
/*  213 */           Query(rules);
/*  214 */           break;
/*      */         case 13: 
/*  216 */           IncludeDirective(rules);
/*  217 */           break;
/*      */         case 14: 
/*  219 */           LibraryDirective(rules); }
/*      */       }
/*      */     }
/*  222 */     this.jj_la1[1] = this.jj_gen;
/*  223 */     jj_consume_token(-1);
/*  224 */     throw new ParseException();
/*      */     
/*      */ 
/*      */ 
/*  228 */     jj_consume_token(0);
/*      */   }
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
/*      */   public final void LibraryDirective(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  243 */     jj_consume_token(14);
/*  244 */     Token t = jj_consume_token(12);
/*      */     try {
/*  246 */       String fileName = stringLiteral(t.image);
/*  247 */       System.err.println("LOADING LIBRARY " + fileName);
/*  248 */       rules.loadLibrary(fileName);
/*  249 */       System.err.println("LOADING LIBRARY " + fileName + " Done");
/*      */     }
/*      */     catch (java.net.MalformedURLException e) {
/*  252 */       System.err.println("Warning: MalformedURL in #library");
/*  253 */       System.err.println(e.getMessage());
/*      */     }
/*      */     catch (java.io.IOException e) {
/*  256 */       System.err.println("Warning: IOException in #library");
/*  257 */       System.err.println(e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public final void IncludeDirective(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  265 */     jj_consume_token(13);
/*  266 */     Token t = jj_consume_token(12);
/*      */     try {
/*  268 */       String fileName = stringLiteral(t.image);
/*  269 */       java.net.URL url; java.net.URL url; if (this.baseURL != null) {
/*  270 */         url = new java.net.URL(this.baseURL, fileName);
/*      */       }
/*      */       else
/*  273 */         url = new java.net.URL(fileName);
/*  274 */       System.err.println("INCLUDING " + url.toString());
/*  275 */       parse(rules, url, this.outputStream);
/*  276 */       System.err.println("INCLUDING " + url.toString() + " Done");
/*      */     }
/*      */     catch (java.net.MalformedURLException e) {
/*  279 */       System.err.println("Warning: MalformedURL in #include");
/*  280 */       System.err.println(e.getMessage());
/*      */     }
/*      */     catch (java.io.IOException e) {
/*  283 */       System.err.println("Warning: IOException in #include");
/*  284 */       System.err.println(e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */   public final tyRuBa.engine.RBFact Fact(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*  290 */     tyRuBa.engine.RBPredicateExpression pred = SimplePredicate(rules);
/*  291 */     return new tyRuBa.engine.RBFact(pred);
/*      */   }
/*      */   
/*      */   public final void Rule(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  297 */     RBExpression exp = null;
/*  298 */     tyRuBa.engine.RBPredicateExpression pred = SimplePredicate(rules);
/*  299 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 54: 
/*  301 */       jj_consume_token(54);
/*  302 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 16: 
/*      */       case 17: 
/*      */       case 19: 
/*      */       case 20: 
/*      */       case 21: 
/*      */       case 22: 
/*      */       case 41: 
/*      */       case 60: 
/*  311 */         exp = Expression(rules);
/*  312 */         break;
/*      */       case 33: 
/*  314 */         exp = ModeSwitchExpression(rules);
/*  315 */         break;
/*      */       default: 
/*  317 */         this.jj_la1[2] = this.jj_gen;
/*  318 */         jj_consume_token(-1);
/*  319 */         throw new ParseException();
/*      */       }
/*      */       break;
/*      */     default: 
/*  323 */       this.jj_la1[3] = this.jj_gen;
/*      */     }
/*      */     
/*  326 */     jj_consume_token(49);
/*  327 */     if (exp == null) {
/*  328 */       rules.insert(pred);
/*      */     } else {
/*  330 */       rules.insert(new tyRuBa.engine.RBRule(pred, exp));
/*      */     }
/*      */   }
/*      */   
/*      */   public final tyRuBa.engine.RBPredicateExpression SimplePredicate(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  337 */     Token t = jj_consume_token(60);
/*  338 */     jj_consume_token(41);
/*  339 */     ArrayList terms = new ArrayList();
/*  340 */     TermList(terms, rules);
/*  341 */     tyRuBa.engine.RBPredicateExpression e = new tyRuBa.engine.RBPredicateExpression(t.image, terms);
/*  342 */     jj_consume_token(42);
/*  343 */     return e;
/*      */   }
/*      */   
/*      */   public final void predNameList(ArrayList names)
/*      */     throws ParseException
/*      */   {
/*  349 */     Token t = jj_consume_token(60);
/*  350 */     names.add(t.image);
/*  351 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 48: 
/*  353 */       jj_consume_token(48);
/*  354 */       predNameList(names);
/*  355 */       break;
/*      */     default: 
/*  357 */       this.jj_la1[4] = this.jj_gen;
/*      */     }
/*      */   }
/*      */   
/*      */   public final void PredInfoRules(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*  363 */     ArrayList names = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*  367 */     ArrayList predModes = new ArrayList();
/*  368 */     boolean isPersistent = false;
/*  369 */     predNameList(names);
/*  370 */     jj_consume_token(40);
/*  371 */     tyRuBa.modes.TupleType types = tyRuBa.modes.Factory.makeTupleType();
/*  372 */     TVarFactory tfact = new TVarFactory();
/*  373 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 43: 
/*      */     case 45: 
/*      */     case 57: 
/*      */     case 60: 
/*      */     case 61: 
/*  379 */       TypeList(types, tfact, rules);
/*  380 */       break;
/*      */     case 41: 
/*  382 */       jj_consume_token(41);
/*  383 */       jj_consume_token(42);
/*  384 */       break;
/*      */     default: 
/*  386 */       this.jj_la1[5] = this.jj_gen;
/*  387 */       jj_consume_token(-1);
/*  388 */       throw new ParseException();
/*      */     }
/*  390 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 37: 
/*  392 */       jj_consume_token(37);
/*  393 */       isPersistent = true;
/*  394 */       break;
/*      */     default: 
/*  396 */       this.jj_la1[6] = this.jj_gen;
/*      */     }
/*      */     
/*  399 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 23: 
/*  401 */       jj_consume_token(23);
/*      */       for (;;)
/*      */       {
/*  404 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 41: 
/*      */           break;
/*      */         default: 
/*  409 */           this.jj_la1[7] = this.jj_gen;
/*  410 */           break;
/*      */         }
/*  412 */         ModeRule(predModes, types.size(), names);
/*      */       }
/*  414 */       jj_consume_token(28);
/*  415 */       break;
/*      */     default: 
/*  417 */       this.jj_la1[8] = this.jj_gen;
/*      */     }
/*      */     
/*  420 */     for (int i = 0; i < names.size(); i++) {
/*  421 */       tyRuBa.modes.PredInfo p = tyRuBa.modes.Factory.makePredInfo(rules, (String)names.get(i), types, predModes, isPersistent);
/*  422 */       rules.insertPredInfo(p);
/*      */     }
/*      */   }
/*      */   
/*      */   public final void TypeList(tyRuBa.modes.TupleType types, TVarFactory tfact, QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*  428 */     Type t = Type(tfact, rules);
/*  429 */     types.add(t);
/*  430 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 48: 
/*  432 */       jj_consume_token(48);
/*  433 */       TypeList(types, tfact, rules);
/*  434 */       break;
/*      */     default: 
/*  436 */       this.jj_la1[9] = this.jj_gen;
/*      */     }
/*      */   }
/*      */   
/*      */   public final Type Type(TVarFactory tfact, QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*      */     Type t;
/*  443 */     if (jj_2_2(3)) {
/*  444 */       t = CompositeType(tfact, rules); } else { Type t;
/*      */       Type t;
/*  446 */       Type t; Type t; switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 57: 
/*      */       case 60: 
/*  449 */         t = AtomicType(rules);
/*  450 */         break;
/*      */       case 61: 
/*  452 */         t = TypeVariable(tfact);
/*  453 */         break;
/*      */       case 45: 
/*  455 */         t = TupleType(tfact, rules);
/*  456 */         break;
/*      */       case 43: 
/*  458 */         t = ListType(tfact, rules);
/*  459 */         break;
/*      */       default: 
/*  461 */         this.jj_la1[10] = this.jj_gen;
/*  462 */         jj_consume_token(-1);
/*  463 */         throw new ParseException(); }
/*      */     }
/*      */     Type t;
/*  466 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */   public final Type AtomicType(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  473 */     boolean strict = false;
/*  474 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 57: 
/*  476 */       jj_consume_token(57);
/*  477 */       strict = true;
/*  478 */       break;
/*      */     default: 
/*  480 */       this.jj_la1[11] = this.jj_gen;
/*      */     }
/*      */     
/*  483 */     Token t = jj_consume_token(60);
/*  484 */     Type type; Type type; if (strict) {
/*  485 */       type = tyRuBa.modes.Factory.makeStrictAtomicType(rules.findType(t.image));
/*      */     } else
/*  487 */       type = tyRuBa.modes.Factory.makeAtomicType(rules.findType(t.image));
/*  488 */     return type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final Type CompositeType(TVarFactory tfact, QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  496 */     boolean strict = false;
/*  497 */     int arity = -1;
/*  498 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 57: 
/*  500 */       jj_consume_token(57);
/*  501 */       strict = true;
/*  502 */       break;
/*      */     default: 
/*  504 */       this.jj_la1[12] = this.jj_gen;
/*      */     }
/*      */     
/*  507 */     Token t = jj_consume_token(60);
/*      */     
/*  509 */     tyRuBa.modes.TupleType args = TupleType(tfact, rules);
/*  510 */     arity = args.size();
/*  511 */     Type type; Type type; if (strict) {
/*  512 */       type = rules.findTypeConst(t.image, arity).applyStrict(args, false);
/*      */     } else
/*  514 */       type = rules.findTypeConst(t.image, arity).apply(args, false);
/*  515 */     return type;
/*      */   }
/*      */   
/*      */   public final Type ListType(TVarFactory tfact, QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  521 */     jj_consume_token(43);
/*  522 */     Type t = Type(tfact, rules);
/*  523 */     jj_consume_token(44);
/*  524 */     return tyRuBa.modes.Factory.makeListType(t);
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.TupleType TupleType(TVarFactory tfact, QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*  529 */     tyRuBa.modes.TupleType types = tyRuBa.modes.Factory.makeTupleType();
/*  530 */     jj_consume_token(45);
/*  531 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 43: 
/*      */     case 45: 
/*      */     case 57: 
/*      */     case 60: 
/*      */     case 61: 
/*  537 */       TypeList(types, tfact, rules);
/*  538 */       break;
/*      */     default: 
/*  540 */       this.jj_la1[13] = this.jj_gen;
/*      */     }
/*      */     
/*  543 */     jj_consume_token(46);
/*  544 */     return types;
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.TVar TypeVariable(TVarFactory tfact)
/*      */     throws ParseException
/*      */   {
/*  550 */     Token t = jj_consume_token(61);
/*  551 */     return tfact.makeTVar(t.image.substring(1));
/*      */   }
/*      */   
/*      */   public final void ModeRule(ArrayList predModes, int numArgs, ArrayList names) throws ParseException, TypeModeError
/*      */   {
/*  556 */     tyRuBa.modes.BindingList bList = tyRuBa.modes.Factory.makeBindingList();
/*      */     
/*  558 */     boolean toBeCheck = true;
/*  559 */     jj_consume_token(41);
/*  560 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 33: 
/*      */     case 60: 
/*  563 */       bList = ModeElem(bList);
/*      */       for (;;)
/*      */       {
/*  566 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 48: 
/*      */           break;
/*      */         default: 
/*  571 */           this.jj_la1[14] = this.jj_gen;
/*  572 */           break;
/*      */         }
/*  574 */         jj_consume_token(48);
/*  575 */         bList = ModeElem(bList);
/*      */       }
/*      */     }
/*      */     
/*  579 */     this.jj_la1[15] = this.jj_gen;
/*      */     
/*      */ 
/*  582 */     jj_consume_token(42);
/*  583 */     if (bList.size() != numArgs) {
/*  584 */       throw new TypeModeError(
/*  585 */         "Number of arguments in mode declaration is different from type declaration in predicate(s) " + 
/*  586 */         names);
/*      */     }
/*  588 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 25: 
/*  590 */       jj_consume_token(25);
/*  591 */       toBeCheck = false;
/*  592 */       break;
/*      */     default: 
/*  594 */       this.jj_la1[16] = this.jj_gen;
/*      */     }
/*      */     
/*  597 */     jj_consume_token(24);
/*  598 */     tyRuBa.modes.Mode mode = PredMode();
/*  599 */     if ((bList.size() == 0) && (
/*  600 */       (mode.isMulti()) || (mode.isNondet()))) {
/*  601 */       throw new TypeModeError(
/*  602 */         "Predicate with no argument can never return more than one result in the predicate(s)" + 
/*  603 */         names);
/*      */     }
/*      */     
/*  606 */     predModes.add(tyRuBa.modes.Factory.makePredicateMode(bList, mode, toBeCheck));
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.BindingList ModeElem(tyRuBa.modes.BindingList bList) throws ParseException
/*      */   {
/*  611 */     tyRuBa.modes.BindingMode bm = Mode();
/*  612 */     bList.add(bm);
/*  613 */     return bList;
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.BindingMode Mode()
/*      */     throws ParseException
/*      */   {
/*  619 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 60: 
/*  621 */       Token t = jj_consume_token(60);
/*  622 */       if (t.image.equals("B"))
/*  623 */         return tyRuBa.modes.Factory.makeBound();
/*  624 */       if ((t.image.equals("F")) || (t.image.equals("FREE"))) {
/*  625 */         return tyRuBa.modes.Factory.makeFree();
/*      */       }
/*  627 */       throw new ParseException("Unknow binding mode " + t.image);
/*      */     
/*      */     case 33: 
/*  630 */       jj_consume_token(33);
/*  631 */       return tyRuBa.modes.Factory.makeBound();
/*      */     }
/*      */     
/*  634 */     this.jj_la1[17] = this.jj_gen;
/*  635 */     jj_consume_token(-1);
/*  636 */     throw new ParseException();
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.Mode PredMode() throws ParseException { tyRuBa.modes.Mode m;
/*      */     tyRuBa.modes.Mode m;
/*      */     tyRuBa.modes.Mode m;
/*      */     tyRuBa.modes.Mode m;
/*  643 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 29: 
/*  645 */       m = Det();
/*  646 */       break;
/*      */     case 30: 
/*  648 */       m = SemiDet();
/*  649 */       break;
/*      */     case 31: 
/*  651 */       m = Multi();
/*  652 */       break;
/*      */     case 32: 
/*  654 */       m = NonDet();
/*  655 */       break;
/*      */     default: 
/*  657 */       this.jj_la1[18] = this.jj_gen;
/*  658 */       jj_consume_token(-1);
/*  659 */       throw new ParseException(); }
/*      */     tyRuBa.modes.Mode m;
/*  661 */     return m;
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.Mode Det() throws ParseException
/*      */   {
/*  666 */     jj_consume_token(29);
/*  667 */     return tyRuBa.modes.Mode.makeDet();
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.Mode SemiDet() throws ParseException
/*      */   {
/*  672 */     jj_consume_token(30);
/*  673 */     return tyRuBa.modes.Mode.makeSemidet();
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.Mode Multi() throws ParseException
/*      */   {
/*  678 */     jj_consume_token(31);
/*  679 */     return tyRuBa.modes.Mode.makeMulti();
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.Mode NonDet() throws ParseException
/*      */   {
/*  684 */     jj_consume_token(32);
/*  685 */     return tyRuBa.modes.Mode.makeNondet();
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.TypeConstructor ExistingTypeAtomName(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  691 */     Token t = jj_consume_token(60);
/*  692 */     return rules.findType(t.image);
/*      */   }
/*      */   
/*      */ 
/*      */   public final void UserDefinedTypeDeclaration(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  699 */     TVarFactory tfact = new TVarFactory();
/*  700 */     jj_consume_token(26);
/*  701 */     tyRuBa.modes.CompositeType t1 = NewCompositeType(rules, tfact);
/*  702 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 57: 
/*  704 */       jj_consume_token(57);
/*  705 */       Type t2 = ExistingType(rules, tfact);
/*  706 */       t1.addSubType(t2);
/*      */       for (;;)
/*      */       {
/*  709 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 52: 
/*      */           break;
/*      */         default: 
/*  714 */           this.jj_la1[19] = this.jj_gen;
/*  715 */           break;
/*      */         }
/*  717 */         jj_consume_token(52);
/*  718 */         t2 = ExistingType(rules, tfact);
/*  719 */         t1.addSubType(t2);
/*      */       }
/*      */     
/*      */     case 27: 
/*  723 */       jj_consume_token(27);
/*  724 */       Type representedBy = Type(tfact, rules);
/*  725 */       t1.setRepresentationType(representedBy);
/*  726 */       rules.addFunctorConst(representedBy, t1);
/*  727 */       break;
/*      */     default: 
/*  729 */       this.jj_la1[20] = this.jj_gen;
/*  730 */       jj_consume_token(-1);
/*  731 */       throw new ParseException();
/*      */     }
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.CompositeType NewCompositeType(QueryEngine rules, TVarFactory tfact)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  738 */     tyRuBa.modes.TupleType tuple = tyRuBa.modes.Factory.makeTupleType();
/*  739 */     Token t = jj_consume_token(60);
/*  740 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 45: 
/*  742 */       tuple = tupleOfTVars(tfact);
/*  743 */       break;
/*      */     default: 
/*  745 */       this.jj_la1[21] = this.jj_gen;
/*      */     }
/*      */     
/*  748 */     return rules.addNewType(
/*  749 */       (tyRuBa.modes.CompositeType)tyRuBa.modes.Factory.makeTypeConstructor(t.image, tuple.size()).apply(tuple, false));
/*      */   }
/*      */   
/*      */   public final Type ExistingType(QueryEngine rules, TVarFactory tfact)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  755 */     tyRuBa.modes.TupleType tuple = tyRuBa.modes.Factory.makeTupleType();
/*  756 */     Token t = jj_consume_token(60);
/*  757 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 45: 
/*  759 */       tuple = tupleOfTVars(tfact);
/*  760 */       break;
/*      */     default: 
/*  762 */       this.jj_la1[22] = this.jj_gen;
/*      */     }
/*      */     
/*  765 */     return rules.findTypeConst(t.image, tuple.size()).apply(tuple, false);
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.TupleType tupleOfTVars(TVarFactory tfact)
/*      */     throws ParseException
/*      */   {
/*  771 */     tyRuBa.modes.TupleType tuple = tyRuBa.modes.Factory.makeTupleType();
/*  772 */     jj_consume_token(45);
/*  773 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 61: 
/*  775 */       Type arg = TypeVariable(tfact);
/*  776 */       tuple.add(arg);
/*      */       for (;;)
/*      */       {
/*  779 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */         {
/*      */         case 48: 
/*      */           break;
/*      */         default: 
/*  784 */           this.jj_la1[23] = this.jj_gen;
/*  785 */           break;
/*      */         }
/*  787 */         jj_consume_token(48);
/*  788 */         arg = TypeVariable(tfact);
/*  789 */         tuple.add(arg);
/*      */       }
/*      */     }
/*      */     
/*  793 */     this.jj_la1[24] = this.jj_gen;
/*      */     
/*      */ 
/*  796 */     jj_consume_token(46);
/*  797 */     return tuple;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final void Query(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  805 */     jj_consume_token(54);
/*  806 */     RBExpression q = Expression(rules);
/*  807 */     jj_consume_token(49);
/*  808 */     System.err.println("##QUERY : " + q);
/*  809 */     tyRuBa.util.ElementSource solutions = rules.frameQuery(q);
/*  810 */     if (!solutions.hasMoreElements()) {
/*  811 */       System.err.println();
/*  812 */       System.err.println("FAILURE");
/*      */     }
/*      */     else {
/*  815 */       while (solutions.hasMoreElements()) {
/*  816 */         System.err.println();
/*  817 */         tyRuBa.engine.Frame solution = (tyRuBa.engine.Frame)solutions.nextElement();
/*      */         
/*      */ 
/*  820 */         System.err.print(solution.toString());
/*      */       }
/*  822 */       System.err.println();
/*      */     }
/*  824 */     System.err.println("##END QUERY");
/*      */   }
/*      */   
/*      */   public final tyRuBa.modes.ModeCase ModeCase(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*  829 */     java.util.Collection boundVars = new java.util.HashSet();
/*      */     
/*  831 */     jj_consume_token(33);
/*      */     for (;;)
/*      */     {
/*  834 */       tyRuBa.engine.RBVariable var = Variable();
/*  835 */       boundVars.add(var);
/*  836 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */       {
/*      */       }
/*      */       
/*      */     }
/*  841 */     this.jj_la1[25] = this.jj_gen;
/*      */     
/*      */ 
/*      */ 
/*  845 */     jj_consume_token(74);
/*  846 */     RBExpression exp = Expression(rules);
/*  847 */     return new tyRuBa.modes.ModeCase(boundVars, exp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final RBExpression ModeSwitchExpression(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  855 */     tyRuBa.modes.ModeCase mc = ModeCase(rules);
/*  856 */     tyRuBa.engine.RBModeSwitchExpression msExp = new tyRuBa.engine.RBModeSwitchExpression(mc);
/*      */     
/*      */ 
/*  859 */     while (jj_2_3(2))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  864 */       jj_consume_token(52);
/*  865 */       mc = ModeCase(rules);
/*  866 */       msExp.addModeCase(mc);
/*      */     }
/*  868 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 52: 
/*  870 */       jj_consume_token(52);
/*  871 */       jj_consume_token(34);
/*  872 */       jj_consume_token(74);
/*  873 */       RBExpression defaultExp = Expression(rules);
/*  874 */       msExp.addDefaultCase(defaultExp);
/*  875 */       break;
/*      */     default: 
/*  877 */       this.jj_la1[26] = this.jj_gen;
/*      */     }
/*      */     
/*  880 */     return msExp;
/*      */   }
/*      */   
/*      */   public final RBExpression ExpressionAndEOF(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  886 */     RBExpression e = Expression(rules);
/*  887 */     jj_consume_token(0);
/*  888 */     return e;
/*      */   }
/*      */   
/*      */   public final RBExpression Expression(QueryEngine rules) throws ParseException, TypeModeError {
/*      */     RBExpression e;
/*      */     RBExpression e;
/*  894 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 17: 
/*      */     case 21: 
/*  897 */       e = Quantifier(rules);
/*  898 */       break;
/*      */     case 16: 
/*      */     case 19: 
/*      */     case 20: 
/*      */     case 22: 
/*      */     case 41: 
/*      */     case 60: 
/*  905 */       e = Disjunction(rules);
/*  906 */       break;
/*      */     default: 
/*  908 */       this.jj_la1[27] = this.jj_gen;
/*  909 */       jj_consume_token(-1);
/*  910 */       throw new ParseException(); }
/*      */     RBExpression e;
/*  912 */     return e;
/*      */   }
/*      */   
/*      */   public final RBExpression Disjunction(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  918 */     tyRuBa.engine.RBCompoundExpression ce = null;
/*  919 */     RBExpression e1 = Conjunction(rules);
/*      */     for (;;)
/*      */     {
/*  922 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */       {
/*      */       case 47: 
/*      */         break;
/*      */       default: 
/*  927 */         this.jj_la1[28] = this.jj_gen;
/*  928 */         break;
/*      */       }
/*  930 */       jj_consume_token(47);
/*  931 */       RBExpression e2 = Conjunction(rules);
/*  932 */       if (ce == null) {
/*  933 */         ce = new tyRuBa.engine.RBDisjunction(e1, e2);
/*      */       } else
/*  935 */         ce.addSubexp(e2);
/*      */     }
/*  937 */     if (ce == null) return e1; return ce;
/*      */   }
/*      */   
/*      */   public final RBExpression Conjunction(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/*  943 */     tyRuBa.engine.RBCompoundExpression ce = null;
/*  944 */     RBExpression e1 = Predicate(rules);
/*      */     for (;;)
/*      */     {
/*  947 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */       {
/*      */       case 48: 
/*      */         break;
/*      */       default: 
/*  952 */         this.jj_la1[29] = this.jj_gen;
/*  953 */         break;
/*      */       }
/*  955 */       jj_consume_token(48);
/*  956 */       RBExpression e2 = Predicate(rules);
/*  957 */       if (ce == null) {
/*  958 */         ce = tyRuBa.engine.FrontEnd.makeAnd(e1, e2);
/*      */       } else {
/*  960 */         ce.addSubexp(e2);
/*      */       }
/*      */     }
/*  963 */     if (ce == null) return e1; return ce; }
/*      */   
/*      */   public final RBExpression Predicate(QueryEngine rules) throws ParseException, TypeModeError { RBExpression e;
/*      */     RBExpression e;
/*      */     RBExpression e;
/*      */     RBExpression e;
/*  969 */     RBExpression e; switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 60: 
/*  971 */       e = PredicateExpression(rules);
/*  972 */       break;
/*      */     case 16: 
/*  974 */       e = NotFilter(rules);
/*  975 */       break;
/*      */     case 22: 
/*  977 */       e = TestFilter(rules);
/*  978 */       break;
/*      */     case 19: 
/*  980 */       e = FindAll(rules);
/*  981 */       break;
/*      */     case 20: 
/*  983 */       e = CountAll(rules);
/*  984 */       break;
/*      */     case 41: 
/*  986 */       jj_consume_token(41);
/*  987 */       RBExpression e = Expression(rules);
/*  988 */       jj_consume_token(42);
/*  989 */       break;
/*      */     default: 
/*  991 */       this.jj_la1[30] = this.jj_gen;
/*  992 */       jj_consume_token(-1);
/*  993 */       throw new ParseException(); }
/*      */     RBExpression e;
/*  995 */     return e;
/*      */   }
/*      */   
/*      */   public final RBExpression PredicateExpression(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1001 */     ArrayList terms = new ArrayList();
/*      */     
/*      */ 
/* 1004 */     Token t = jj_consume_token(60);
/* 1005 */     jj_consume_token(41);
/* 1006 */     TermList(terms, rules);
/* 1007 */     jj_consume_token(42);
/* 1008 */     return new tyRuBa.engine.RBPredicateExpression(t.image, terms);
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final RBExpression FindAll(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1035 */     jj_consume_token(19);
/* 1036 */     jj_consume_token(41);
/* 1037 */     RBExpression p = Predicate(rules);
/* 1038 */     jj_consume_token(48);
/* 1039 */     RBTerm t1 = Term(rules);
/* 1040 */     jj_consume_token(48);
/* 1041 */     RBTerm t2 = Term(rules);
/* 1042 */     jj_consume_token(42);
/* 1043 */     return new tyRuBa.engine.RBFindAll(p, t1, t2);
/*      */   }
/*      */   
/*      */ 
/*      */   public final RBExpression CountAll(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1050 */     jj_consume_token(20);
/* 1051 */     jj_consume_token(41);
/* 1052 */     RBExpression p = Predicate(rules);
/* 1053 */     jj_consume_token(48);
/* 1054 */     RBTerm t1 = Term(rules);
/* 1055 */     jj_consume_token(48);
/* 1056 */     RBTerm t2 = Term(rules);
/* 1057 */     jj_consume_token(42);
/* 1058 */     return new tyRuBa.engine.RBCountAll(p, t1, t2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Token jj_nt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int jj_ntk;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Token jj_scanpos;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Token jj_lastpos;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int jj_la;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final RBExpression NotFilter(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1097 */     jj_consume_token(16);
/* 1098 */     jj_consume_token(41);
/* 1099 */     RBExpression e = Expression(rules);
/* 1100 */     jj_consume_token(42);
/* 1101 */     return new tyRuBa.engine.RBNotFilter(e);
/*      */   }
/*      */   
/*      */   public final RBExpression TestFilter(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1107 */     jj_consume_token(22);
/* 1108 */     jj_consume_token(41);
/* 1109 */     RBExpression e = Expression(rules);
/* 1110 */     jj_consume_token(42);
/* 1111 */     return new tyRuBa.engine.RBTestFilter(e);
/*      */   }
/*      */   
/*      */ 
/*      */   public final RBExpression Quantifier(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1118 */     ArrayList vars = new ArrayList();
/* 1119 */     Token t; Token t; switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 17: 
/* 1121 */       t = jj_consume_token(17);
/* 1122 */       break;
/*      */     case 21: 
/* 1124 */       t = jj_consume_token(21);
/* 1125 */       break;
/*      */     case 18: case 19: case 20: default: 
/* 1127 */       this.jj_la1[31] = this.jj_gen;
/* 1128 */       jj_consume_token(-1);
/* 1129 */       throw new ParseException(); }
/*      */     Token t;
/* 1131 */     varList(vars);
/* 1132 */     jj_consume_token(74);
/* 1133 */     RBExpression e = Expression(rules);
/* 1134 */     if (t.image == "EXISTS")
/* 1135 */       return new tyRuBa.engine.RBExistsQuantifier(vars, e);
/* 1136 */     if (t.image == "UNIQUE")
/* 1137 */       return new tyRuBa.engine.RBUniqueQuantifier(vars, e);
/* 1138 */     throw new Error("Missing return statement in function");
/*      */   }
/*      */   
/*      */   public final void varList(ArrayList v) throws ParseException
/*      */   {
/* 1143 */     tyRuBa.engine.RBVariable var = Variable();
/* 1144 */     v.add(var);
/*      */     for (;;)
/*      */     {
/* 1147 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk)
/*      */       {
/*      */       case 48: 
/*      */         break;
/*      */       default: 
/* 1152 */         this.jj_la1[32] = this.jj_gen;
/* 1153 */         break;
/*      */       }
/* 1155 */       jj_consume_token(48);
/* 1156 */       var = Variable();
/* 1157 */       v.add(var);
/*      */     }
/*      */   }
/*      */   
/*      */   public final RBTerm Term(QueryEngine rules) throws ParseException, TypeModeError {
/*      */     RBTerm t;
/* 1163 */     if (jj_2_4(2)) {
/* 1164 */       t = CompoundTerm(rules); } else { RBTerm t;
/*      */       RBTerm t;
/* 1166 */       RBTerm t; RBTerm t; switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 6: 
/*      */       case 10: 
/*      */       case 12: 
/*      */       case 60: 
/*      */       case 61: 
/*      */       case 62: 
/*      */       case 63: 
/*      */       case 64: 
/* 1175 */         t = SimpleTerm(rules);
/* 1176 */         break;
/*      */       case 45: 
/* 1178 */         t = Tuple(rules);
/* 1179 */         break;
/*      */       case 73: 
/* 1181 */         t = QuotedCode();
/* 1182 */         break;
/*      */       case 43: 
/* 1184 */         t = List(rules);
/* 1185 */         break;
/*      */       default: 
/* 1187 */         this.jj_la1[33] = this.jj_gen;
/* 1188 */         jj_consume_token(-1);
/* 1189 */         throw new ParseException(); }
/*      */     }
/*      */     RBTerm t;
/* 1192 */     return t;
/*      */   }
/*      */   
/*      */   public final RBTerm Tuple(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/* 1197 */     ArrayList terms = new ArrayList();
/* 1198 */     jj_consume_token(45);
/* 1199 */     TermList(terms, rules);
/* 1200 */     jj_consume_token(46);
/* 1201 */     return tyRuBa.engine.RBTuple.make(terms);
/*      */   }
/*      */   
/*      */ 
/*      */   public final RBTerm CompoundTerm(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1208 */     ArrayList terms = new ArrayList();
/* 1209 */     if (jj_2_5(2)) {
/* 1210 */       Token typeName = jj_consume_token(60);
/* 1211 */       RBTerm t; switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 45: 
/* 1213 */         jj_consume_token(45);
/* 1214 */         TermList(terms, rules);
/* 1215 */         RBTerm t = rules.findConstructorType(new tyRuBa.engine.FunctorIdentifier(typeName.image, terms.size()))
/* 1216 */           .apply(terms);
/* 1217 */         jj_consume_token(46);
/* 1218 */         break;
/*      */       case 43: 
/* 1220 */         jj_consume_token(43);
/* 1221 */         RBTerm t = RealTermList(rules);
/* 1222 */         t = rules.findConstructorType(new tyRuBa.engine.FunctorIdentifier(typeName.image, 1))
/* 1223 */           .apply(t);
/* 1224 */         jj_consume_token(44);
/* 1225 */         break;
/*      */       case 44: default: 
/* 1227 */         this.jj_la1[34] = this.jj_gen;
/* 1228 */         jj_consume_token(-1);
/* 1229 */         throw new ParseException();break; }
/*      */     } else { RBTerm t;
/* 1231 */       if (jj_2_6(2)) {
/* 1232 */         t = SimpleTerm(rules);
/*      */       } else {
/* 1234 */         jj_consume_token(-1);
/* 1235 */         throw new ParseException(); } }
/*      */     RBTerm t;
/* 1237 */     return t;
/*      */   }
/*      */   
/*      */   public final RBTerm SimpleTerm(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/*      */     RBTerm t;
/*      */     RBTerm t;
/* 1244 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 6: 
/*      */     case 10: 
/*      */     case 12: 
/*      */     case 60: 
/*      */     case 62: 
/*      */     case 63: 
/*      */     case 64: 
/* 1252 */       t = Constant();
/* 1253 */       break;
/*      */     case 61: 
/* 1255 */       t = Variable();
/* 1256 */       break;
/*      */     default: 
/* 1258 */       this.jj_la1[35] = this.jj_gen;
/* 1259 */       jj_consume_token(-1);
/* 1260 */       throw new ParseException(); }
/*      */     RBTerm t;
/* 1262 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 40: 
/* 1264 */       jj_consume_token(40);
/* 1265 */       tyRuBa.modes.TypeConstructor typeAtom = ExistingTypeAtomName(rules);
/* 1266 */       t = t.addTypeCast(typeAtom);
/* 1267 */       break;
/*      */     default: 
/* 1269 */       this.jj_la1[36] = this.jj_gen;
/*      */     }
/*      */     
/* 1272 */     return t;
/*      */   }
/*      */   
/*      */   public final RBTerm List(QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1278 */     jj_consume_token(43);
/* 1279 */     RBTerm r = RealTermList(rules);
/* 1280 */     jj_consume_token(44);
/* 1281 */     return r;
/*      */   }
/*      */   
/*      */   public final RBTerm Constant()
/*      */     throws ParseException
/*      */   {
/* 1287 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 6: 
/* 1289 */       Token t = jj_consume_token(6);
/* 1290 */       return tyRuBa.engine.FrontEnd.makeInteger(t.image);
/*      */     
/*      */     case 10: 
/* 1293 */       Token t = jj_consume_token(10);
/* 1294 */       return tyRuBa.engine.FrontEnd.makeReal(t.image);
/*      */     
/*      */     case 12: 
/* 1297 */       Token t = jj_consume_token(12);
/* 1298 */       return tyRuBa.engine.FrontEnd.makeName(stringLiteral(t.image));
/*      */     
/*      */     case 60: 
/* 1301 */       Token t = jj_consume_token(60);
/* 1302 */       return tyRuBa.engine.FrontEnd.makeName(t.image);
/*      */     
/*      */     case 62: 
/* 1305 */       Token t = jj_consume_token(62);
/* 1306 */       return tyRuBa.engine.FrontEnd.makeTemplateVar(t.image);
/*      */     
/*      */     case 63: 
/* 1309 */       Token t = jj_consume_token(63);
/* 1310 */       return tyRuBa.engine.RBJavaObjectCompoundTerm.javaClass(javaClassName(t.image));
/*      */     
/*      */     case 64: 
/* 1313 */       Token t = jj_consume_token(64);
/* 1314 */       return tyRuBa.engine.RBJavaObjectCompoundTerm.regexp(stripQuotes(t.image));
/*      */     }
/*      */     
/* 1317 */     this.jj_la1[37] = this.jj_gen;
/* 1318 */     jj_consume_token(-1);
/* 1319 */     throw new ParseException();
/*      */   }
/*      */   
/*      */ 
/*      */   public final void TermList(ArrayList terms, QueryEngine rules)
/*      */     throws ParseException, TypeModeError
/*      */   {
/* 1326 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 6: 
/*      */     case 10: 
/*      */     case 12: 
/*      */     case 43: 
/*      */     case 45: 
/*      */     case 60: 
/*      */     case 61: 
/*      */     case 62: 
/*      */     case 63: 
/*      */     case 64: 
/*      */     case 73: 
/* 1338 */       RBTerm t = Term(rules);
/* 1339 */       terms.add(t);
/* 1340 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 48: 
/* 1342 */         jj_consume_token(48);
/* 1343 */         TermList(terms, rules);
/* 1344 */         break;
/*      */       default: 
/* 1346 */         this.jj_la1[38] = this.jj_gen;
/*      */       }
/*      */       
/* 1349 */       break;
/*      */     default: 
/* 1351 */       this.jj_la1[39] = this.jj_gen;
/*      */     }
/*      */   }
/*      */   
/*      */   public final RBTerm RealTermList(QueryEngine rules) throws ParseException, TypeModeError
/*      */   {
/* 1357 */     RBTerm t2 = tyRuBa.engine.FrontEnd.theEmptyList;
/* 1358 */     switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */     case 6: 
/*      */     case 10: 
/*      */     case 12: 
/*      */     case 43: 
/*      */     case 45: 
/*      */     case 60: 
/*      */     case 61: 
/*      */     case 62: 
/*      */     case 63: 
/*      */     case 64: 
/*      */     case 73: 
/* 1370 */       RBTerm t1 = Term(rules);
/* 1371 */       switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */       case 48: 
/*      */       case 52: 
/* 1374 */         switch (this.jj_ntk == -1 ? jj_ntk() : this.jj_ntk) {
/*      */         case 48: 
/* 1376 */           jj_consume_token(48);
/* 1377 */           t2 = RealTermList(rules);
/* 1378 */           break;
/*      */         case 52: 
/* 1380 */           jj_consume_token(52);
/* 1381 */           t2 = Term(rules);
/* 1382 */           break;
/*      */         case 49: case 50: case 51: default: 
/* 1384 */           this.jj_la1[40] = this.jj_gen;
/* 1385 */           jj_consume_token(-1);
/* 1386 */           throw new ParseException();
/*      */         }
/*      */         break;
/*      */       case 49: case 50: case 51: default: 
/* 1390 */         this.jj_la1[41] = this.jj_gen;
/*      */       }
/*      */       
/* 1393 */       return new tyRuBa.engine.RBPair(t1, t2);
/*      */     }
/*      */     
/* 1396 */     this.jj_la1[42] = this.jj_gen;
/* 1397 */     return tyRuBa.engine.FrontEnd.theEmptyList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final tyRuBa.engine.RBVariable Variable()
/*      */     throws ParseException
/*      */   {
/* 1405 */     Token t = jj_consume_token(61);
/* 1406 */     tyRuBa.engine.RBVariable var; tyRuBa.engine.RBVariable var; if (t.image.length() == 1) {
/* 1407 */       var = tyRuBa.engine.FrontEnd.makeIgnoredVar();
/*      */     } else
/* 1409 */       var = tyRuBa.engine.FrontEnd.makeVar(t.image);
/* 1410 */     return var;
/*      */   }
/*      */   
/*      */   public final RBTerm QuotedCode()
/*      */     throws ParseException
/*      */   {
/* 1416 */     Token t = jj_consume_token(73);
/* 1417 */     return makeQuotedCode(t.image);
/*      */   }
/*      */   
/*      */   private final boolean jj_2_1(int xla)
/*      */   {
/* 1422 */     this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1423 */     try { return !jj_3_1();
/* 1424 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1425 */     } finally { jj_save(0, xla);
/*      */     }
/*      */   }
/*      */   
/* 1429 */   private final boolean jj_2_2(int xla) { this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1430 */     try { return !jj_3_2();
/* 1431 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1432 */     } finally { jj_save(1, xla);
/*      */     }
/*      */   }
/*      */   
/* 1436 */   private final boolean jj_2_3(int xla) { this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1437 */     try { return !jj_3_3();
/* 1438 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1439 */     } finally { jj_save(2, xla);
/*      */     }
/*      */   }
/*      */   
/* 1443 */   private final boolean jj_2_4(int xla) { this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1444 */     try { return !jj_3_4();
/* 1445 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1446 */     } finally { jj_save(3, xla);
/*      */     }
/*      */   }
/*      */   
/* 1450 */   private final boolean jj_2_5(int xla) { this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1451 */     try { return !jj_3_5();
/* 1452 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1453 */     } finally { jj_save(4, xla);
/*      */     }
/*      */   }
/*      */   
/* 1457 */   private final boolean jj_2_6(int xla) { this.jj_la = xla;this.jj_lastpos = (this.jj_scanpos = this.token);
/* 1458 */     try { return !jj_3_6();
/* 1459 */     } catch (LookaheadSuccess localLookaheadSuccess) { return true;
/* 1460 */     } finally { jj_save(5, xla);
/*      */     }
/*      */   }
/*      */   
/* 1464 */   private final boolean jj_3R_11() { if (jj_3R_18()) return true;
/* 1465 */     if (jj_scan_token(40)) return true;
/* 1466 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_42() {
/* 1470 */     if (jj_scan_token(61)) return true;
/* 1471 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_25() {
/* 1475 */     if (jj_3R_28()) return true;
/* 1476 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_24() {
/* 1480 */     if (jj_scan_token(48)) return true;
/* 1481 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_20() {
/* 1485 */     if (jj_scan_token(45)) { return true;
/*      */     }
/* 1487 */     Token xsp = this.jj_scanpos;
/* 1488 */     if (jj_3R_25()) this.jj_scanpos = xsp;
/* 1489 */     if (jj_scan_token(46)) return true;
/* 1490 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_22() {
/* 1494 */     if (jj_3R_27()) return true;
/* 1495 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_35() {
/* 1499 */     if (jj_scan_token(64)) return true;
/* 1500 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_13() {
/* 1504 */     if (jj_scan_token(33)) return true;
/* 1505 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_34() {
/* 1509 */     if (jj_scan_token(63)) return true;
/* 1510 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_18() {
/* 1514 */     if (jj_scan_token(60)) { return true;
/*      */     }
/* 1516 */     Token xsp = this.jj_scanpos;
/* 1517 */     if (jj_3R_24()) this.jj_scanpos = xsp;
/* 1518 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_26()
/*      */   {
/* 1523 */     Token xsp = this.jj_scanpos;
/* 1524 */     if (jj_3R_29()) {
/* 1525 */       this.jj_scanpos = xsp;
/* 1526 */       if (jj_3R_30()) {
/* 1527 */         this.jj_scanpos = xsp;
/* 1528 */         if (jj_3R_31()) {
/* 1529 */           this.jj_scanpos = xsp;
/* 1530 */           if (jj_3R_32()) {
/* 1531 */             this.jj_scanpos = xsp;
/* 1532 */             if (jj_3R_33()) {
/* 1533 */               this.jj_scanpos = xsp;
/* 1534 */               if (jj_3R_34()) {
/* 1535 */                 this.jj_scanpos = xsp;
/* 1536 */                 if (jj_3R_35()) return true;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1543 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_29() {
/* 1547 */     if (jj_scan_token(6)) return true;
/* 1548 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_33() {
/* 1552 */     if (jj_scan_token(62)) return true;
/* 1553 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_32() {
/* 1557 */     if (jj_scan_token(60)) return true;
/* 1558 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_31() {
/* 1562 */     if (jj_scan_token(12)) return true;
/* 1563 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_30() {
/* 1567 */     if (jj_scan_token(10)) return true;
/* 1568 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_43() {
/* 1572 */     if (jj_scan_token(43)) return true;
/* 1573 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_21() {
/* 1577 */     if (jj_3R_26()) return true;
/* 1578 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_17()
/*      */   {
/* 1583 */     Token xsp = this.jj_scanpos;
/* 1584 */     if (jj_3R_21()) {
/* 1585 */       this.jj_scanpos = xsp;
/* 1586 */       if (jj_3R_22()) return true;
/*      */     }
/* 1588 */     xsp = this.jj_scanpos;
/* 1589 */     if (jj_3R_23()) this.jj_scanpos = xsp;
/* 1590 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_6() {
/* 1594 */     if (jj_3R_17()) return true;
/* 1595 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_23() {
/* 1599 */     if (jj_scan_token(40)) return true;
/* 1600 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_19() {
/* 1604 */     if (jj_scan_token(57)) return true;
/* 1605 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_12()
/*      */   {
/* 1610 */     Token xsp = this.jj_scanpos;
/* 1611 */     if (jj_3R_19()) this.jj_scanpos = xsp;
/* 1612 */     if (jj_scan_token(60)) return true;
/* 1613 */     if (jj_3R_20()) return true;
/* 1614 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_16() {
/* 1618 */     if (jj_scan_token(43)) return true;
/* 1619 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_15() {
/* 1623 */     if (jj_scan_token(45)) return true;
/* 1624 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_5() {
/* 1628 */     if (jj_scan_token(60)) { return true;
/*      */     }
/* 1630 */     Token xsp = this.jj_scanpos;
/* 1631 */     if (jj_3R_15()) {
/* 1632 */       this.jj_scanpos = xsp;
/* 1633 */       if (jj_3R_16()) return true;
/*      */     }
/* 1635 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_1() {
/* 1639 */     if (jj_3R_11()) return true;
/* 1640 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_44() {
/* 1644 */     if (jj_scan_token(57)) return true;
/* 1645 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_41()
/*      */   {
/* 1650 */     Token xsp = this.jj_scanpos;
/* 1651 */     if (jj_3R_44()) this.jj_scanpos = xsp;
/* 1652 */     if (jj_scan_token(60)) return true;
/* 1653 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_14()
/*      */   {
/* 1658 */     Token xsp = this.jj_scanpos;
/* 1659 */     if (jj_3_5()) {
/* 1660 */       this.jj_scanpos = xsp;
/* 1661 */       if (jj_3_6()) return true;
/*      */     }
/* 1663 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_40() {
/* 1667 */     if (jj_3R_43()) return true;
/* 1668 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_39() {
/* 1672 */     if (jj_3R_20()) return true;
/* 1673 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_38() {
/* 1677 */     if (jj_3R_42()) return true;
/* 1678 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_37() {
/* 1682 */     if (jj_3R_41()) return true;
/* 1683 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_2() {
/* 1687 */     if (jj_3R_12()) return true;
/* 1688 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_36()
/*      */   {
/* 1693 */     Token xsp = this.jj_scanpos;
/* 1694 */     if (jj_3_2()) {
/* 1695 */       this.jj_scanpos = xsp;
/* 1696 */       if (jj_3R_37()) {
/* 1697 */         this.jj_scanpos = xsp;
/* 1698 */         if (jj_3R_38()) {
/* 1699 */           this.jj_scanpos = xsp;
/* 1700 */           if (jj_3R_39()) {
/* 1701 */             this.jj_scanpos = xsp;
/* 1702 */             if (jj_3R_40()) return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1707 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_28() {
/* 1711 */     if (jj_3R_36()) return true;
/* 1712 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_4() {
/* 1716 */     if (jj_3R_14()) return true;
/* 1717 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3R_27() {
/* 1721 */     if (jj_scan_token(61)) return true;
/* 1722 */     return false;
/*      */   }
/*      */   
/*      */   private final boolean jj_3_3() {
/* 1726 */     if (jj_scan_token(52)) return true;
/* 1727 */     if (jj_3R_13()) return true;
/* 1728 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1737 */   public boolean lookingAhead = false;
/*      */   private boolean jj_semLA;
/*      */   private int jj_gen;
/* 1740 */   private final int[] jj_la1 = new int[43];
/*      */   private static int[] jj_la1_0;
/*      */   private static int[] jj_la1_1;
/*      */   private static int[] jj_la1_2;
/*      */   
/* 1745 */   static { jj_la1_0();
/* 1746 */     jj_la1_1();
/* 1747 */     jj_la1_2();
/*      */   }
/*      */   
/* 1750 */   private static void jj_la1_0() { jj_la1_0 = new int[] { 67133440, 67133440, 8060928, 0, 0, 0, 0, 0, 8388608, 0, 0, 0, 0, 0, 0, 0, 33554432, 0, -536870912, 0, 134217728, 0, 0, 0, 0, 0, 0, 8060928, 0, 0, 5832704, 2228224, 0, 5184, 0, 5184, 0, 5184, 0, 5184, 0, 0, 5184 }; }
/*      */   
/*      */   private static void jj_la1_1() {
/* 1753 */     jj_la1_1 = new int[] { 272629760, 272629760, 268435970, 4194304, 65536, 838871552, 32, 512, 0, 65536, 838871040, 33554432, 33554432, 838871040, 65536, 268435458, 0, 268435458, 1, 1048576, 33554432, 8192, 8192, 65536, 536870912, 536870912, 1048576, 268435968, 32768, 65536, 268435968, 0, 65536, -268425216, 10240, -268435456, 256, -805306368, 65536, -268425216, 1114112, 1114112, -268425216 };
/*      */   }
/*      */   
/* 1756 */   private static void jj_la1_2() { jj_la1_2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 513, 0, 1, 0, 1, 0, 513, 0, 0, 513 }; }
/*      */   
/* 1758 */   private final JJCalls[] jj_2_rtns = new JJCalls[6];
/* 1759 */   private boolean jj_rescan = false;
/* 1760 */   private int jj_gc = 0;
/*      */   
/*      */   public TyRuBaParser(java.io.InputStream stream) {
/* 1763 */     this.jj_input_stream = new JavaCharStream(stream, 1, 1);
/* 1764 */     this.token_source = new TyRuBaParserTokenManager(this.jj_input_stream);
/* 1765 */     this.token = new Token();
/* 1766 */     this.jj_ntk = -1;
/* 1767 */     this.jj_gen = 0;
/* 1768 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1769 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   public void ReInit(java.io.InputStream stream) {
/* 1773 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 1774 */     this.token_source.ReInit(this.jj_input_stream);
/* 1775 */     this.token = new Token();
/* 1776 */     this.jj_ntk = -1;
/* 1777 */     this.jj_gen = 0;
/* 1778 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1779 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   public TyRuBaParser(java.io.Reader stream) {
/* 1783 */     this.jj_input_stream = new JavaCharStream(stream, 1, 1);
/* 1784 */     this.token_source = new TyRuBaParserTokenManager(this.jj_input_stream);
/* 1785 */     this.token = new Token();
/* 1786 */     this.jj_ntk = -1;
/* 1787 */     this.jj_gen = 0;
/* 1788 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1789 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   public void ReInit(java.io.Reader stream) {
/* 1793 */     this.jj_input_stream.ReInit(stream, 1, 1);
/* 1794 */     this.token_source.ReInit(this.jj_input_stream);
/* 1795 */     this.token = new Token();
/* 1796 */     this.jj_ntk = -1;
/* 1797 */     this.jj_gen = 0;
/* 1798 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1799 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   public TyRuBaParser(TyRuBaParserTokenManager tm) {
/* 1803 */     this.token_source = tm;
/* 1804 */     this.token = new Token();
/* 1805 */     this.jj_ntk = -1;
/* 1806 */     this.jj_gen = 0;
/* 1807 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1808 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   public void ReInit(TyRuBaParserTokenManager tm) {
/* 1812 */     this.token_source = tm;
/* 1813 */     this.token = new Token();
/* 1814 */     this.jj_ntk = -1;
/* 1815 */     this.jj_gen = 0;
/* 1816 */     for (int i = 0; i < 43; i++) this.jj_la1[i] = -1;
/* 1817 */     for (int i = 0; i < this.jj_2_rtns.length; i++) this.jj_2_rtns[i] = new JJCalls();
/*      */   }
/*      */   
/*      */   private final Token jj_consume_token(int kind) throws ParseException {
/*      */     Token oldToken;
/* 1822 */     if ((oldToken = this.token).next != null) this.token = this.token.next; else
/* 1823 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 1824 */     this.jj_ntk = -1;
/* 1825 */     if (this.token.kind == kind) {
/* 1826 */       this.jj_gen += 1;
/* 1827 */       if (++this.jj_gc > 100) {
/* 1828 */         this.jj_gc = 0;
/* 1829 */         for (int i = 0; i < this.jj_2_rtns.length; i++) {
/* 1830 */           JJCalls c = this.jj_2_rtns[i];
/* 1831 */           while (c != null) {
/* 1832 */             if (c.gen < this.jj_gen) c.first = null;
/* 1833 */             c = c.next;
/*      */           }
/*      */         }
/*      */       }
/* 1837 */       return this.token;
/*      */     }
/* 1839 */     this.token = oldToken;
/* 1840 */     this.jj_kind = kind;
/* 1841 */     throw generateParseException();
/*      */   }
/*      */   
/*      */ 
/* 1845 */   private final LookaheadSuccess jj_ls = new LookaheadSuccess(null);
/*      */   
/* 1847 */   private final boolean jj_scan_token(int kind) { if (this.jj_scanpos == this.jj_lastpos) {
/* 1848 */       this.jj_la -= 1;
/* 1849 */       if (this.jj_scanpos.next == null) {
/* 1850 */         this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken());
/*      */       } else {
/* 1852 */         this.jj_lastpos = (this.jj_scanpos = this.jj_scanpos.next);
/*      */       }
/*      */     } else {
/* 1855 */       this.jj_scanpos = this.jj_scanpos.next;
/*      */     }
/* 1857 */     if (this.jj_rescan) {
/* 1858 */       int i = 0; for (Token tok = this.token; 
/* 1859 */           (tok != null) && (tok != this.jj_scanpos); tok = tok.next) i++;
/* 1860 */       if (tok != null) jj_add_error_token(kind, i);
/*      */     }
/* 1862 */     if (this.jj_scanpos.kind != kind) return true;
/* 1863 */     if ((this.jj_la == 0) && (this.jj_scanpos == this.jj_lastpos)) throw this.jj_ls;
/* 1864 */     return false;
/*      */   }
/*      */   
/*      */   public final Token getNextToken() {
/* 1868 */     if (this.token.next != null) this.token = this.token.next; else
/* 1869 */       this.token = (this.token.next = this.token_source.getNextToken());
/* 1870 */     this.jj_ntk = -1;
/* 1871 */     this.jj_gen += 1;
/* 1872 */     return this.token;
/*      */   }
/*      */   
/*      */   public final Token getToken(int index) {
/* 1876 */     Token t = this.lookingAhead ? this.jj_scanpos : this.token;
/* 1877 */     for (int i = 0; i < index; i++) {
/* 1878 */       if (t.next != null) t = t.next; else
/* 1879 */         t = t.next = this.token_source.getNextToken();
/*      */     }
/* 1881 */     return t;
/*      */   }
/*      */   
/*      */   private final int jj_ntk() {
/* 1885 */     if ((this.jj_nt = this.token.next) == null) {
/* 1886 */       return this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind;
/*      */     }
/* 1888 */     return this.jj_ntk = this.jj_nt.kind;
/*      */   }
/*      */   
/* 1891 */   private java.util.Vector jj_expentries = new java.util.Vector();
/*      */   private int[] jj_expentry;
/* 1893 */   private int jj_kind = -1;
/* 1894 */   private int[] jj_lasttokens = new int[100];
/*      */   private int jj_endpos;
/*      */   
/*      */   private void jj_add_error_token(int kind, int pos) {
/* 1898 */     if (pos >= 100) return;
/* 1899 */     if (pos == this.jj_endpos + 1) {
/* 1900 */       this.jj_lasttokens[(this.jj_endpos++)] = kind;
/* 1901 */     } else if (this.jj_endpos != 0) {
/* 1902 */       this.jj_expentry = new int[this.jj_endpos];
/* 1903 */       for (int i = 0; i < this.jj_endpos; i++) {
/* 1904 */         this.jj_expentry[i] = this.jj_lasttokens[i];
/*      */       }
/* 1906 */       boolean exists = false;
/* 1907 */       for (java.util.Enumeration e = this.jj_expentries.elements(); e.hasMoreElements();) {
/* 1908 */         int[] oldentry = (int[])e.nextElement();
/* 1909 */         if (oldentry.length == this.jj_expentry.length) {
/* 1910 */           exists = true;
/* 1911 */           for (int i = 0; i < this.jj_expentry.length; i++) {
/* 1912 */             if (oldentry[i] != this.jj_expentry[i]) {
/* 1913 */               exists = false;
/* 1914 */               break;
/*      */             }
/*      */           }
/* 1917 */           if (exists) break;
/*      */         }
/*      */       }
/* 1920 */       if (!exists) this.jj_expentries.addElement(this.jj_expentry);
/* 1921 */       if (pos != 0) this.jj_lasttokens[((this.jj_endpos = pos) - 1)] = kind;
/*      */     }
/*      */   }
/*      */   
/*      */   public ParseException generateParseException() {
/* 1926 */     this.jj_expentries.removeAllElements();
/* 1927 */     boolean[] la1tokens = new boolean[75];
/* 1928 */     for (int i = 0; i < 75; i++) {
/* 1929 */       la1tokens[i] = false;
/*      */     }
/* 1931 */     if (this.jj_kind >= 0) {
/* 1932 */       la1tokens[this.jj_kind] = true;
/* 1933 */       this.jj_kind = -1;
/*      */     }
/* 1935 */     for (int i = 0; i < 43; i++) {
/* 1936 */       if (this.jj_la1[i] == this.jj_gen) {
/* 1937 */         for (int j = 0; j < 32; j++) {
/* 1938 */           if ((jj_la1_0[i] & 1 << j) != 0) {
/* 1939 */             la1tokens[j] = true;
/*      */           }
/* 1941 */           if ((jj_la1_1[i] & 1 << j) != 0) {
/* 1942 */             la1tokens[(32 + j)] = true;
/*      */           }
/* 1944 */           if ((jj_la1_2[i] & 1 << j) != 0) {
/* 1945 */             la1tokens[(64 + j)] = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1950 */     for (int i = 0; i < 75; i++) {
/* 1951 */       if (la1tokens[i] != 0) {
/* 1952 */         this.jj_expentry = new int[1];
/* 1953 */         this.jj_expentry[0] = i;
/* 1954 */         this.jj_expentries.addElement(this.jj_expentry);
/*      */       }
/*      */     }
/* 1957 */     this.jj_endpos = 0;
/* 1958 */     jj_rescan_token();
/* 1959 */     jj_add_error_token(0, 0);
/* 1960 */     int[][] exptokseq = new int[this.jj_expentries.size()][];
/* 1961 */     for (int i = 0; i < this.jj_expentries.size(); i++) {
/* 1962 */       exptokseq[i] = ((int[])this.jj_expentries.elementAt(i));
/*      */     }
/* 1964 */     return new ParseException(this.token, exptokseq, tokenImage);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void jj_rescan_token()
/*      */   {
/* 1974 */     this.jj_rescan = true;
/* 1975 */     for (int i = 0; i < 6; i++) {
/* 1976 */       JJCalls p = this.jj_2_rtns[i];
/*      */       do {
/* 1978 */         if (p.gen > this.jj_gen) {
/* 1979 */           this.jj_la = p.arg;this.jj_lastpos = (this.jj_scanpos = p.first);
/* 1980 */           switch (i) {
/* 1981 */           case 0:  jj_3_1(); break;
/* 1982 */           case 1:  jj_3_2(); break;
/* 1983 */           case 2:  jj_3_3(); break;
/* 1984 */           case 3:  jj_3_4(); break;
/* 1985 */           case 4:  jj_3_5(); break;
/* 1986 */           case 5:  jj_3_6();
/*      */           }
/*      */         }
/* 1989 */         p = p.next;
/* 1990 */       } while (p != null);
/*      */     }
/* 1992 */     this.jj_rescan = false;
/*      */   }
/*      */   
/*      */   private final void jj_save(int index, int xla) {
/* 1996 */     JJCalls p = this.jj_2_rtns[index];
/* 1997 */     while (p.gen > this.jj_gen) {
/* 1998 */       if (p.next == null) { p = p.next = new JJCalls(); break; }
/* 1999 */       p = p.next;
/*      */     }
/* 2001 */     p.gen = (this.jj_gen + xla - this.jj_la);p.first = this.token;p.arg = xla;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void parse(QueryEngine rules, java.net.URL url, PrintStream os)
/*      */     throws ParseException, java.io.IOException, TypeModeError
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual 97	java/net/URL:openStream	()Ljava/io/InputStream;
/*      */     //   4: astore_3
/*      */     //   5: new 1	tyRuBa/parser/TyRuBaParser
/*      */     //   8: dup
/*      */     //   9: aload_3
/*      */     //   10: aload_2
/*      */     //   11: aload_1
/*      */     //   12: invokespecial 58	tyRuBa/parser/TyRuBaParser:<init>	(Ljava/io/InputStream;Ljava/io/PrintStream;Ljava/net/URL;)V
/*      */     //   15: astore 4
/*      */     //   17: aload 4
/*      */     //   19: aload_0
/*      */     //   20: invokevirtual 101	tyRuBa/parser/TyRuBaParser:CompilationUnit	(LtyRuBa/engine/QueryEngine;)V
/*      */     //   23: goto +12 -> 35
/*      */     //   26: astore 5
/*      */     //   28: aload_3
/*      */     //   29: invokevirtual 105	java/io/InputStream:close	()V
/*      */     //   32: aload 5
/*      */     //   34: athrow
/*      */     //   35: aload_3
/*      */     //   36: invokevirtual 105	java/io/InputStream:close	()V
/*      */     //   39: return
/*      */     // Line number table:
/*      */     //   Java source line #40	-> byte code offset #0
/*      */     //   Java source line #42	-> byte code offset #5
/*      */     //   Java source line #43	-> byte code offset #17
/*      */     //   Java source line #45	-> byte code offset #26
/*      */     //   Java source line #46	-> byte code offset #28
/*      */     //   Java source line #47	-> byte code offset #32
/*      */     //   Java source line #46	-> byte code offset #35
/*      */     //   Java source line #48	-> byte code offset #39
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	40	0	rules	QueryEngine
/*      */     //   0	40	1	url	java.net.URL
/*      */     //   0	40	2	os	PrintStream
/*      */     //   4	32	3	is	java.io.InputStream
/*      */     //   15	3	4	parser	TyRuBaParser
/*      */     //   26	7	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   5	26	26	finally
/*      */   }
/*      */   
/*      */   public final void enable_tracing() {}
/*      */   
/*      */   public final void disable_tracing() {}
/*      */   
/*      */   static final class JJCalls
/*      */   {
/*      */     int gen;
/*      */     Token first;
/*      */     int arg;
/*      */     JJCalls next;
/*      */   }
/*      */   
/*      */   private static final class LookaheadSuccess
/*      */     extends Error
/*      */   {}
/*      */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/parser/TyRuBaParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */