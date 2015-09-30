/*     */ package tyRuBa.engine.factbase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import tyRuBa.engine.Frame;
/*     */ import tyRuBa.engine.PredicateIdentifier;
/*     */ import tyRuBa.engine.QueryEngine;
/*     */ import tyRuBa.engine.RBContext;
/*     */ import tyRuBa.engine.RBTuple;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*     */ import tyRuBa.engine.factbase.hashtable.Index;
/*     */ import tyRuBa.engine.factbase.hashtable.URLFactLibrary;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.util.Action;
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
/*     */ public class FactLibraryManager
/*     */ {
/*     */   private HashMap libraries;
/*     */   private QueryEngine qe;
/*     */   
/*     */   public FactLibraryManager(QueryEngine qe)
/*     */   {
/*  41 */     this.qe = qe;
/*  42 */     removeAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLibraryJarFile(String jarFileLocation)
/*     */   {
/*  50 */     if (!this.libraries.containsKey(jarFileLocation)) {
/*  51 */       File jarFile = new File(jarFileLocation);
/*     */       try {
/*  53 */         this.libraries.put(jarFileLocation, new URLFactLibrary("jar:" + jarFile.toURL() + "!/", this.qe));
/*     */       } catch (MalformedURLException localMalformedURLException) {
/*  55 */         throw new Error("Jar file location is not a valid location (it can't be turned into a URL)");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLibraryURLLocation(String baseURL)
/*     */   {
/*  65 */     if (!this.libraries.containsKey(baseURL)) {
/*  66 */       this.libraries.put(baseURL, new URLFactLibrary(baseURL, this.qe));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public URLFactLibrary getLibrary(String location)
/*     */   {
/*  75 */     return (URLFactLibrary)this.libraries.get(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLibrary(String location)
/*     */   {
/*  83 */     this.libraries.remove(location);
/*     */   }
/*     */   
/*     */   public void removeAll()
/*     */   {
/*  88 */     this.libraries = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Compiled compile(PredicateMode pm, PredicateIdentifier predId, CompilationContext context)
/*     */   {
/* 100 */     if (this.libraries.size() == 0) {
/* 101 */       return Compiled.fail;
/*     */     }
/*     */     
/* 104 */     final Index[] indexes = new Index[this.libraries.size()];
/* 105 */     int i = 0;
/* 106 */     for (Iterator iter = this.libraries.values().iterator(); iter.hasNext();) {
/* 107 */       URLFactLibrary element = (URLFactLibrary)iter.next();
/* 108 */       indexes[i] = element.getIndex(predId.getName(), predId.getArity(), pm);
/* 109 */       i++;
/*     */     }
/*     */     
/* 112 */     if (pm.getMode().hi.compareTo(Multiplicity.one) <= 0) {
/* 113 */       if (pm.getParamModes().getNumFree() != 0)
/*     */       {
/* 115 */         new SemiDetCompiled(pm.getMode())
/*     */         {
/*     */           public Frame runSemiDet(Object input, RBContext context) {
/* 118 */             RBTuple goal = (RBTuple)input;
/*     */             
/*     */ 
/* 121 */             RBTuple inputPars = indexes[0].extractBound(goal);
/* 122 */             RBTuple outputPars = indexes[0].extractFree(goal);
/* 123 */             for (int i = 0; i < indexes.length; i++) {
/* 124 */               RBTuple retrieved = indexes[i].getMatchSingle(inputPars);
/* 125 */               if (retrieved != null) {
/* 126 */                 return retrieved.unify(outputPars, new Frame());
/*     */               }
/*     */             }
/* 129 */             return null;
/*     */           }
/*     */         };
/*     */       }
/*     */       
/* 134 */       new SemiDetCompiled(pm.getMode())
/*     */       {
/*     */         public Frame runSemiDet(Object input, RBContext context) {
/* 137 */           RBTuple goal = (RBTuple)input;
/* 138 */           for (int i = 0; i < indexes.length; i++) {
/* 139 */             RBTuple retrieved = indexes[i].getMatchSingle(goal);
/* 140 */             if (retrieved != null) {
/* 141 */               return new Frame();
/*     */             }
/*     */           }
/* 144 */           return null;
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 149 */     if (pm.getParamModes().getNumFree() != 0)
/*     */     {
/* 151 */       new Compiled(pm.getMode())
/*     */       {
/*     */         public ElementSource runNonDet(Object input, RBContext context) {
/* 154 */           RBTuple goal = (RBTuple)input;
/*     */           
/*     */ 
/* 157 */           RBTuple inputPars = indexes[0].extractBound(goal);
/* 158 */           final RBTuple outputPars = indexes[0].extractFree(goal);
/*     */           
/* 160 */           ElementSource matches = indexes[0].getMatchElementSource(inputPars);
/* 161 */           for (int i = 1; i < indexes.length; i++) {
/* 162 */             matches = matches.append(indexes[i].getMatchElementSource(inputPars));
/*     */           }
/* 164 */           matches.map(new Action()
/*     */           {
/*     */             public Object compute(Object arg) {
/* 167 */               RBTuple retrieved = (RBTuple)arg;
/* 168 */               return retrieved.unify(outputPars, new Frame());
/*     */             }
/*     */           });
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 175 */     throw new Error("This case should not happen");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/FactLibraryManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */