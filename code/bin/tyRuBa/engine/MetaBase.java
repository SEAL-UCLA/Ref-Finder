/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.tdbc.PreparedInsert;
/*     */ import tyRuBa.tdbc.TyrubaException;
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
/*     */ public class MetaBase
/*     */ {
/*     */   private QueryEngine engine;
/*  27 */   private PreparedInsert typeConstructorFact = null;
/*  28 */   private PreparedInsert nameFact = null;
/*  29 */   private PreparedInsert subtypeFact = null;
/*  30 */   private PreparedInsert representationFact = null;
/*  31 */   private PreparedInsert arityFact = null;
/*     */   
/*     */ 
/*  34 */   public static String declarations = "TYPE meta.Type \t= meta.TupleType \t| meta.ListType \t| meta.CompositeType TYPE meta.TupleType AS [meta.Type] TYPE meta.ListType AS meta.Type TYPE meta.CompositeType AS <tyRuBa.modes.TypeConstructor,meta.Type> meta.typeConstructor :: tyRuBa.modes.TypeConstructor MODES (F) IS NONDET END meta.name :: Object, String MODES (F,F) IS NONDET END meta.arity :: tyRuBa.modes.TypeConstructor, Integer MODES (B,F) IS DET       (F,F) IS NONDET END meta.subtype :: tyRuBa.modes.TypeConstructor, tyRuBa.modes.TypeConstructor MODES (F,F) IS NONDET       (B,F) IS NONDET      (F,B) IS SEMIDET END meta.representation :: tyRuBa.modes.TypeConstructor, meta.Type MODES (B,F) IS SEMIDET       (F,F) IS NONDET END ";
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
/*     */   MetaBase(QueryEngine engine)
/*     */   {
/*  65 */     this.engine = engine;
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
/*     */   private void lazyInitialize()
/*     */   {
/*  85 */     if (this.typeConstructorFact == null)
/*     */       try {
/*  87 */         this.typeConstructorFact = this.engine.prepareForInsertion(
/*  88 */           "meta.typeConstructor(!t)");
/*  89 */         this.arityFact = this.engine.prepareForInsertion(
/*  90 */           "meta.arity(!t,!n)");
/*  91 */         this.nameFact = this.engine.prepareForInsertion(
/*  92 */           "meta.name(!t,!n)");
/*  93 */         this.subtypeFact = this.engine.prepareForInsertion(
/*  94 */           "meta.subtype(!super,!sub)");
/*  95 */         this.representationFact = this.engine.prepareForInsertion(
/*  96 */           "meta.representation(!type,!repType)");
/*     */       } catch (ParseException e) {
/*  98 */         e.printStackTrace();
/*  99 */         throw new Error(e);
/*     */       } catch (TypeModeError e) {
/* 101 */         e.printStackTrace();
/* 102 */         throw new Error(e);
/*     */       }
/*     */   }
/*     */   
/*     */   public void assertTypeConstructor(TypeConstructor type) {
/* 107 */     lazyInitialize();
/*     */     try {
/* 109 */       this.typeConstructorFact.put("!t", type);
/* 110 */       this.typeConstructorFact.executeInsert();
/*     */       
/* 112 */       this.nameFact.put("!t", type);
/* 113 */       this.nameFact.put("!n", type.getName());
/* 114 */       this.nameFact.executeInsert();
/*     */       
/* 116 */       this.arityFact.put("!t", type);
/* 117 */       this.arityFact.put("!n", type.getTypeArity());
/* 118 */       this.arityFact.executeInsert();
/*     */       
/* 120 */       type.setMetaBase(this);
/*     */     } catch (TyrubaException e) {
/* 122 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void assertSubtype(TypeConstructor superConst, TypeConstructor subConst) {
/* 127 */     lazyInitialize();
/*     */     try {
/* 129 */       this.subtypeFact.put("!super", superConst);
/* 130 */       this.subtypeFact.put("!sub", subConst);
/* 131 */       this.subtypeFact.executeInsert();
/*     */     } catch (TyrubaException e) {
/* 133 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void assertRepresentation(TypeConstructor constructor, Type repType) {
/* 138 */     lazyInitialize();
/*     */     try {
/* 140 */       this.representationFact.put("!type", constructor);
/* 141 */       this.representationFact.put("!repType", repType);
/* 142 */       this.representationFact.executeInsert();
/*     */     } catch (TyrubaException e) {
/* 144 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void addTypeMappings(FrontEnd frontend)
/*     */     throws TypeModeError
/*     */   {
/* 154 */     frontend.addTypeMapping(
/* 155 */       new FunctorIdentifier("meta.Type", 0), 
/* 156 */       new MetaBase.1());
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
/* 170 */     frontend.addTypeMapping(
/* 171 */       new FunctorIdentifier("meta.CompositeType", 0), 
/* 172 */       new MetaBase.2());
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
/* 193 */     frontend.addTypeMapping(
/* 194 */       new FunctorIdentifier("meta.TupleType", 0), 
/* 195 */       new MetaBase.3());
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/MetaBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */