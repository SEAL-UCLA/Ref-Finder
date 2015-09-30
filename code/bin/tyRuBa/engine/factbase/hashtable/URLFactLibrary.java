/*    */ package tyRuBa.engine.factbase.hashtable;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import junit.framework.Assert;
/*    */ import tyRuBa.engine.QueryEngine;
/*    */ import tyRuBa.engine.factbase.FileBasedValidatorManager;
/*    */ import tyRuBa.engine.factbase.NamePersistenceManager;
/*    */ import tyRuBa.engine.factbase.ValidatorManager;
/*    */ import tyRuBa.modes.BindingList;
/*    */ import tyRuBa.modes.Factory;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ import tyRuBa.util.pager.URLLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class URLFactLibrary
/*    */ {
/*    */   private String baseURL;
/*    */   private HashMap indexes;
/*    */   private NamePersistenceManager nameManager;
/*    */   private ValidatorManager validatorManager;
/*    */   private QueryEngine engine;
/*    */   
/*    */   public URLFactLibrary(String baseURL, QueryEngine qe)
/*    */   {
/* 48 */     this.indexes = new HashMap();
/* 49 */     this.baseURL = baseURL;
/* 50 */     this.engine = qe;
/*    */     try {
/* 52 */       this.nameManager = new NamePersistenceManager(new URL(baseURL + "names.data"));
/* 53 */       Assert.assertNotNull(this.nameManager);
/* 54 */       this.validatorManager = new FileBasedValidatorManager(new URL(baseURL + "validators.data"));
/* 55 */       Assert.assertNotNull(this.validatorManager);
/*    */     } catch (MalformedURLException e) {
/* 57 */       throw new Error("Library file does not exist");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Index getIndex(String predicateName, int arity, PredicateMode mode)
/*    */   {
/* 70 */     BindingList bl = mode.getParamModes();
/*    */     
/* 72 */     if (bl.getNumFree() == 0) {
/* 73 */       bl = Factory.makeBindingList(arity, Factory.makeFree());
/*    */     }
/*    */     
/* 76 */     Index result = (Index)this.indexes.get(predicateName + arity + bl.getBFString());
/*    */     try
/*    */     {
/* 79 */       if (result == null) {
/* 80 */         result = new Index(mode, new URLLocation(this.baseURL + this.nameManager.getPersistentName(predicateName) + "/" + 
/* 81 */           arity + "/" + bl.getBFString() + "/"), this.engine, predicateName + "/" + arity + "/" + 
/* 82 */           bl.getBFString(), this.nameManager, this.validatorManager);
/*    */       }
/*    */     } catch (MalformedURLException e) {
/* 85 */       throw new Error("Malformed URL for Fact Library index: " + predicateName + "/" + arity + "/" + 
/* 86 */         bl.getBFString());
/*    */     }
/* 88 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/hashtable/URLFactLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */