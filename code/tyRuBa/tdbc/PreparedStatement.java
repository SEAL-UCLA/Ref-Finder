/*     */ package tyRuBa.tdbc;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBSubstitutable;
/*     */ import tyRuBa.engine.RBTemplateVar;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PreparedStatement
/*     */ {
/*     */   private TypeEnv tEnv;
/*  24 */   protected Frame putMap = new Frame();
/*     */   
/*     */ 
/*  27 */   private Set mustPut = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private QueryEngine engine;
/*     */   
/*     */ 
/*     */ 
/*     */   public PreparedStatement(QueryEngine engine, TypeEnv tEnv)
/*     */   {
/*  37 */     this.engine = engine;
/*  38 */     this.tEnv = tEnv;
/*  39 */     for (Iterator iter = tEnv.keySet().iterator(); iter.hasNext();) {
/*  40 */       RBSubstitutable var = (RBSubstitutable)iter.next();
/*  41 */       if ((var instanceof RBTemplateVar)) {
/*  42 */         if (this.mustPut == null)
/*  43 */           this.mustPut = new HashSet();
/*  44 */         this.mustPut.add(var.name());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkReadyToRun() throws TyrubaException {
/*  50 */     if (!readyToRun())
/*  51 */       throw new TyrubaException("Some input variables left unbound: " + this.mustPut);
/*     */   }
/*     */   
/*     */   public boolean readyToRun() {
/*  55 */     return this.mustPut == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(String templateVar, Object value)
/*     */     throws TyrubaException
/*     */   {
/*  64 */     checkVarType(templateVar, value);
/*  65 */     if (this.mustPut != null) {
/*  66 */       this.mustPut.remove(templateVar);
/*  67 */       if (this.mustPut.isEmpty())
/*  68 */         this.mustPut = null;
/*     */     }
/*  70 */     this.putMap.put(new RBTemplateVar(templateVar), this.engine.makeJavaObject(value));
/*     */   }
/*     */   
/*     */   private void checkVarType(String templateVarName, Object value) throws TyrubaException {
/*  74 */     RBTemplateVar var = new RBTemplateVar(templateVarName);
/*  75 */     Type expected = this.tEnv.basicGet(var);
/*  76 */     if (expected == null) {
/*  77 */       throw new TyrubaException("Trying to put an unknown variable: " + templateVarName);
/*     */     }
/*     */     try {
/*  80 */       Class expectedClass = expected.javaEquivalent();
/*  81 */       if (expectedClass == null) {
/*  82 */         throw new TyrubaException("There is no Java equivalent for tyRuBa type " + expected);
/*     */       }
/*  84 */       if (!expectedClass.isAssignableFrom(value.getClass())) {
/*  85 */         throw new TyrubaException("Value: " + value + " of class " + value.getClass().getName() + " expected " + expectedClass.getName());
/*     */       }
/*     */     } catch (TypeModeError e) {
/*  88 */       e.printStackTrace();
/*  89 */       throw new TyrubaException(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void put(String templateVar, int value)
/*     */     throws TyrubaException
/*     */   {
/*  98 */     put(templateVar, new Integer(value));
/*     */   }
/*     */   
/*     */   public void put(String templateVar, long value) throws TyrubaException {
/* 102 */     put(templateVar, new Long(value));
/*     */   }
/*     */   
/*     */   public void put(String templateVar, float value) throws TyrubaException {
/* 106 */     put(templateVar, new Float(value));
/*     */   }
/*     */   
/*     */   public void put(String templateVar, boolean value) throws TyrubaException {
/* 110 */     put(templateVar, new Boolean(value));
/*     */   }
/*     */   
/*     */   public QueryEngine getEngine() {
/* 114 */     return this.engine;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tdbc/PreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */