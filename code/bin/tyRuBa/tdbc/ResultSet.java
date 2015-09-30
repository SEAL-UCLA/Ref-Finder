/*     */ package tyRuBa.tdbc;
/*     */ 
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBCompoundTerm;
/*     */ import tyRuBa.engine.RBExpression;
/*     */ import tyRuBa.engine.RBRepAsJavaObjectCompoundTerm;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBVariable;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.util.ElementSource;
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
/*     */ public class ResultSet
/*     */ {
/*     */   private ElementSource eltSource;
/*  28 */   private Frame frame = null;
/*     */   
/*     */   public ResultSet(ElementSource eltSource) {
/*  31 */     this.eltSource = eltSource;
/*     */   }
/*     */   
/*     */   ResultSet(QueryEngine queryEngine, String query) throws TyrubaException {
/*     */     try {
/*  36 */       this.eltSource = queryEngine.frameQuery(query);
/*     */     } catch (ParseException e) {
/*  38 */       throw new TyrubaException(e);
/*     */     } catch (TypeModeError e) {
/*  40 */       throw new TyrubaException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ResultSet(QueryEngine queryEngine, RBExpression query) throws TyrubaException {
/*     */     try {
/*  46 */       this.eltSource = queryEngine.frameQuery(query);
/*     */     } catch (ParseException e) {
/*  48 */       throw new TyrubaException(e);
/*     */     } catch (TypeModeError e) {
/*  50 */       throw new TyrubaException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean next() throws TyrubaException {
/*  55 */     boolean more = this.eltSource.hasMoreElements();
/*  56 */     if (more) {
/*  57 */       this.frame = ((Frame)this.eltSource.nextElement());
/*     */     }
/*     */     else {
/*  60 */       this.frame = null;
/*     */     }
/*     */     
/*  63 */     return more;
/*     */   }
/*     */   
/*     */   public Object getObject(String variableName) throws TyrubaException {
/*  67 */     if (this.frame == null) {
/*  68 */       throw new TyrubaException("There are no more elements in the current result set.");
/*     */     }
/*  70 */     RBVariable var = FrontEnd.makeVar(variableName);
/*  71 */     RBTerm term = this.frame.get(var);
/*  72 */     if ((term instanceof RBCompoundTerm)) {
/*  73 */       if ((term instanceof RBRepAsJavaObjectCompoundTerm))
/*  74 */         return ((RBRepAsJavaObjectCompoundTerm)term).getValue();
/*  75 */       if (((RBCompoundTerm)term).getNumArgs() == 1) {
/*  76 */         if (((RBCompoundTerm)term).getArg() != null) {
/*  77 */           return ((RBCompoundTerm)term).getArg().up();
/*     */         }
/*  79 */         return term.up();
/*     */       }
/*     */       
/*  82 */       return term;
/*     */     }
/*     */     
/*     */ 
/*  86 */     return term;
/*     */   }
/*     */   
/*     */   public String getString(String variableName) throws TyrubaException
/*     */   {
/*  91 */     Object o = getObject(variableName);
/*  92 */     if (!(o instanceof String)) {
/*  93 */       throw wrongType(variableName, o, "String");
/*     */     }
/*  95 */     return (String)o;
/*     */   }
/*     */   
/*     */   public int getInt(String variableName)
/*     */     throws TyrubaException
/*     */   {
/* 101 */     Object o = getObject(variableName);
/* 102 */     if ((o instanceof Integer)) {
/* 103 */       return ((Integer)o).intValue();
/*     */     }
/* 105 */     throw wrongType(variableName, o, "int");
/*     */   }
/*     */   
/*     */   private TyrubaException wrongType(String varName, Object found, String expectedType) {
/* 109 */     return new TyrubaException("Variable " + varName + " is bound to an object of type " + found.getClass().getName() + " not " + expectedType + ".");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/tdbc/ResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */