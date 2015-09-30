/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.factbase.FactLibraryManager;
/*     */ import tyRuBa.engine.factbase.NamePersistenceManager;
/*     */ import tyRuBa.engine.factbase.ValidatorManager;
/*     */ import tyRuBa.modes.CompositeType;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.ListType;
/*     */ import tyRuBa.modes.PredInfo;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeMapping;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.parser.TyRuBaParser;
/*     */ import tyRuBa.tdbc.PreparedInsert;
/*     */ import tyRuBa.tdbc.PreparedQuery;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.NullQueryLogger;
/*     */ import tyRuBa.util.QueryLogger;
/*     */ import tyRuBa.util.SynchPolicy;
/*     */ import tyRuBa.util.pager.Pager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class QueryEngine
/*     */ {
/*  40 */   private QueryLogger logger = NullQueryLogger.the;
/*     */   
/*     */   public void setLogger(QueryLogger logger) {
/*  43 */     if (logger == null)
/*  44 */       logger = NullQueryLogger.the;
/*  45 */     synchronized (frontend()) {
/*  46 */       this.logger.close();
/*  47 */       this.logger = logger;
/*     */     }
/*     */   }
/*     */   
/*     */   public void insert(RBComponent r) throws TypeModeError {
/*  52 */     frontend().updateCounter += 1L;
/*  53 */     rulebase().insert(r);
/*     */   }
/*     */   
/*     */   public void insertPredInfo(PredInfo p) throws TypeModeError {
/*  57 */     rulebase().insert(p);
/*     */   }
/*     */   
/*     */   public void insert(RBPredicateExpression exp) throws TypeModeError {
/*  61 */     insert(new RBFact(exp));
/*     */   }
/*     */   
/*     */   abstract FrontEnd frontend();
/*     */   
/*     */   abstract ModedRuleBaseIndex rulebase();
/*     */   
/*     */   public PrintStream output()
/*     */   {
/*  70 */     return frontend().os;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract String getStoragePath();
/*     */   
/*     */ 
/*     */   public abstract String getIdentifier();
/*     */   
/*     */   public int getFrontEndCacheSize()
/*     */   {
/*  81 */     return frontend().getCacheSize();
/*     */   }
/*     */   
/*     */   public ValidatorManager getFrontEndValidatorManager() {
/*  85 */     return frontend().getValidatorManager();
/*     */   }
/*     */   
/*     */   public NamePersistenceManager getFrontendNamePersistenceManager() {
/*  89 */     return frontend().getNamePersistenceManager();
/*     */   }
/*     */   
/*     */   public FactLibraryManager getFrontEndFactLibraryManager() {
/*  93 */     return frontend().getFactLibraryManager();
/*     */   }
/*     */   
/*     */   public long getFrontEndLastBackupTime() {
/*  97 */     return frontend().getLastBackupTime();
/*     */   }
/*     */   
/*     */   public Pager getFrontEndPager() {
/* 101 */     return frontend().getPager();
/*     */   }
/*     */   
/*     */ 
/*     */   public void loadLibrary(String fileName)
/*     */     throws ParseException, IOException, TypeModeError
/*     */   {
/* 108 */     URL initfile = ClassLoader.getSystemClassLoader().getResource(
/* 109 */       "lib/" + fileName);
/* 110 */     load(initfile);
/*     */   }
/*     */   
/*     */   public void load(String fileName)
/*     */     throws ParseException, IOException, TypeModeError
/*     */   {
/* 116 */     System.err.println("** loading : " + fileName);
/* 117 */     TyRuBaParser.parse(this, fileName, output());
/*     */   }
/*     */   
/*     */   public void load(InputStream input)
/*     */     throws IOException, ParseException, TypeModeError
/*     */   {
/* 123 */     TyRuBaParser.parse(this, input, output());
/*     */   }
/*     */   
/*     */   public void load(URL url) throws ParseException, IOException, TypeModeError
/*     */   {
/* 128 */     TyRuBaParser.parse(this, url, output());
/*     */   }
/*     */   
/*     */   public void parse(String input) throws ParseException, TypeModeError
/*     */   {
/* 133 */     parse(input, System.err);
/*     */   }
/*     */   
/*     */   public void parse(String input, PrintStream output)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 139 */     TyRuBaParser.parse(this, new ByteArrayInputStream(input.getBytes()), 
/* 140 */       output);
/*     */   }
/*     */   
/*     */ 
/*     */   public ElementSource frameQuery(RBExpression e)
/*     */     throws TypeModeError, ParseException
/*     */   {
/* 147 */     frontend().autoUpdateBuckets();
/* 148 */     PreparedQuery runable = e.prepareForRunning(this);
/*     */     
/*     */ 
/*     */ 
/* 152 */     this.logger.logQuery(e);
/* 153 */     synchronized (frontend())
/*     */     {
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
/* 165 */       frontend().getSynchPolicy().newSource();
/* 166 */       ElementSource result = new QueryEngine.1(this, runable);
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
/* 178 */       frontend().getSynchPolicy().sourceDone();
/* 179 */       ElementSource ret = result.synchronizeOn(frontend());
/*     */       
/* 181 */       return ret;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ElementSource frameQuery(String q)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 189 */     RBExpression exp = TyRuBaParser.parseExpression(
/* 190 */       new ByteArrayInputStream(q.getBytes()), System.err, this);
/* 191 */     return frameQuery(exp);
/*     */   }
/*     */   
/*     */   public ElementSource varQuery(RBExpression e, RBVariable v)
/*     */     throws TypeModeError, ParseException
/*     */   {
/* 197 */     return frameQuery(e).map(new QueryEngine.2(this, v));
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
/*     */   public RBTerm getProperty(Object obj, String propertyName)
/*     */   {
/*     */     try
/*     */     {
/* 214 */       RBVariable propval = FrontEnd.makeVar("?propval");
/* 215 */       RBTerm objTerm = (obj instanceof RBTerm) ? (RBTerm)obj : 
/* 216 */         makeJavaObject(obj);
/* 217 */       RBExpression query = FrontEnd.makePredicateExpression(propertyName, 
/* 218 */         new RBTerm[] { objTerm, propval });
/* 219 */       ElementSource result = varQuery(query, propval);
/* 220 */       if (!result.hasMoreElements()) {
/* 221 */         return null;
/*     */       }
/* 223 */       RBTerm val = (RBTerm)result.nextElement();
/* 224 */       result.release();
/* 225 */       return val;
/*     */     }
/*     */     catch (TypeModeError e) {
/* 228 */       e.printStackTrace();
/* 229 */       throw new Error("Problem calling predicate: " + e.getMessage());
/*     */     } catch (ParseException e) {
/* 231 */       e.printStackTrace();
/* 232 */       throw new Error("Problem calling predicate: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getStringProperty(Object obj, String propertyName)
/*     */   {
/* 240 */     RBTerm res = getProperty(obj, propertyName);
/* 241 */     String result = "null";
/* 242 */     if (res != null) {
/* 243 */       Object upped = res.up();
/* 244 */       if ((upped instanceof UppedTerm)) {
/* 245 */         result = res.toString();
/*     */       } else {
/* 247 */         result = upped.toString();
/*     */       }
/*     */     }
/* 250 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntProperty(Object obj, String propertyName)
/*     */   {
/* 258 */     RBTerm prop = getProperty(obj, propertyName);
/* 259 */     if (prop == null) {
/* 260 */       return 0;
/*     */     }
/* 262 */     return prop.intValue();
/*     */   }
/*     */   
/*     */   public ElementSource varQuery(String qs, String vs)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 268 */     RBVariable v = RBVariable.make(vs);
/* 269 */     RBExpression q = TyRuBaParser.parseExpression(new ByteArrayInputStream(
/* 270 */       qs.getBytes()), System.err, this);
/* 271 */     return varQuery(q, v);
/*     */   }
/*     */   
/*     */   public void dumpFacts(PrintStream out) {
/* 275 */     rulebase().dumpFacts(out);
/*     */   }
/*     */   
/*     */   public RBExpression makeExpression(String expression)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 281 */     TyRuBaParser parser = new TyRuBaParser(new ByteArrayInputStream(
/* 282 */       expression.getBytes()), System.err);
/* 283 */     return parser.Expression(this);
/*     */   }
/*     */   
/*     */   private RBPredicateExpression makePredExpression(String expression) throws ParseException, TypeModeError
/*     */   {
/* 288 */     TyRuBaParser parser = new TyRuBaParser(new ByteArrayInputStream(
/* 289 */       expression.getBytes()), System.err);
/* 290 */     return parser.SimplePredicate(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RBTerm makeTerm(String term)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 300 */     TyRuBaParser parser = new TyRuBaParser(new ByteArrayInputStream(term
/* 301 */       .getBytes()), System.err);
/* 302 */     return parser.Term(new FrontEnd(false));
/*     */   }
/*     */   
/*     */   public TypeConstructor findType(String typeName) throws TypeModeError {
/* 306 */     TypeConstructor result = rulebase().findType(typeName);
/* 307 */     if (result == null) {
/* 308 */       throw new TypeModeError("Unknown type: " + typeName);
/*     */     }
/* 310 */     return result;
/*     */   }
/*     */   
/*     */   public TypeConstructor findTypeConst(String typeName, int arity)
/*     */     throws TypeModeError
/*     */   {
/* 316 */     TypeConstructor typeConst = rulebase().findTypeConst(typeName, arity);
/* 317 */     if (typeConst == null) {
/* 318 */       throw new TypeModeError("Unknown composite type: " + typeName + 
/* 319 */         " with arity " + arity);
/*     */     }
/* 321 */     return typeConst;
/*     */   }
/*     */   
/*     */   public ConstructorType findConstructorType(FunctorIdentifier id)
/*     */     throws TypeModeError
/*     */   {
/* 327 */     ConstructorType typeConst = rulebase().findConstructorType(id);
/* 328 */     if (typeConst == null) {
/* 329 */       throw new TypeModeError("Unknown functor: " + id);
/*     */     }
/* 331 */     return typeConst;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addTypePredicate(TypeConstructor TypeConstructor, ArrayList subTypes)
/*     */   {
/* 337 */     rulebase().addTypePredicate(TypeConstructor, subTypes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeType addNewType(CompositeType type)
/*     */     throws TypeModeError
/*     */   {
/* 347 */     return rulebase().addType(type);
/*     */   }
/*     */   
/*     */   public void addFunctorConst(Type repAs, CompositeType type) {
/* 351 */     rulebase().addFunctorConst(repAs, type);
/*     */   }
/*     */   
/*     */   public PreparedQuery prepareForRunning(RBExpression e) throws TypeModeError {
/* 355 */     return e.prepareForRunning(this);
/*     */   }
/*     */   
/*     */   public PreparedQuery prepareForRunning(String queryTemplate) throws ParseException, TypeModeError
/*     */   {
/* 360 */     RBExpression exp = TyRuBaParser.parseExpression(
/* 361 */       new ByteArrayInputStream(queryTemplate.getBytes()), System.err, 
/* 362 */       this);
/* 363 */     return prepareForRunning(exp);
/*     */   }
/*     */   
/*     */   public PreparedInsert prepareForInsertion(String factStr) throws ParseException, TypeModeError
/*     */   {
/* 368 */     RBPredicateExpression fact = makePredExpression(factStr);
/* 369 */     TypeEnv tEnv = fact.typecheck(rulebase(), new TypeEnv());
/* 370 */     return new PreparedInsert(this, fact, tEnv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RBTerm makeTermFromString(String term)
/*     */     throws ParseException, TypeModeError
/*     */   {
/* 381 */     TyRuBaParser parser = new TyRuBaParser(new ByteArrayInputStream(term
/* 382 */       .getBytes()), System.err);
/* 383 */     return parser.Term(this);
/*     */   }
/*     */   
/*     */   public void addTypeMapping(FunctorIdentifier id, TypeMapping mapping) throws TypeModeError
/*     */   {
/* 388 */     rulebase().addTypeMapping(mapping, id);
/*     */   }
/*     */   
/*     */   public TypeMapping findTypeMapping(Class forWhat) {
/* 392 */     return rulebase().findTypeMapping(forWhat);
/*     */   }
/*     */   
/*     */   public RBTerm makeJavaObject(Object _o) {
/* 396 */     TypeMapping mapping = findTypeMapping(_o.getClass());
/* 397 */     if (mapping == null) {
/* 398 */       return RBCompoundTerm.makeJava(_o);
/*     */     }
/*     */     
/* 401 */     Object obj_rep = mapping.toTyRuBa(_o);
/* 402 */     RBTerm term_rep = null;
/* 403 */     if ((obj_rep instanceof Object[])) {
/* 404 */       Object[] obj_arr = (Object[])obj_rep;
/* 405 */       RBTerm[] term_arr = new RBTerm[obj_arr.length];
/* 406 */       for (int i = 0; i < term_arr.length; i++) {
/* 407 */         term_arr[i] = makeJavaObject(obj_arr[i]);
/*     */       }
/*     */       
/* 410 */       ConstructorType consType = mapping.getFunctor();
/* 411 */       Type repType = consType.getTypeConst().getRepresentation();
/* 412 */       if ((repType instanceof ListType)) {
/* 413 */         term_rep = FrontEnd.makeList(term_arr);
/*     */       }
/* 415 */       else if ((repType instanceof TupleType)) {
/* 416 */         term_rep = FrontEnd.makeTuple(term_arr);
/*     */       }
/*     */       else {
/* 419 */         throw new Error("Cannot convert java object " + term_rep + " to " + repType);
/*     */       }
/*     */     } else {
/* 422 */       term_rep = makeJavaObject(obj_rep);
/*     */     }
/*     */     
/* 425 */     return mapping.getFunctor().apply(term_rep);
/*     */   }
/*     */   
/*     */   public RBTerm makeTypeCast(TypeConstructor toType, Object value)
/*     */   {
/* 430 */     return 
/* 431 */       toType.getConstructorType().apply(RBCompoundTerm.makeJava(value));
/*     */   }
/*     */   
/*     */   public abstract void enableMetaData();
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/QueryEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */