/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import serp.util.SoftValueMap;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*     */ import tyRuBa.util.ElementCollector;
/*     */ import tyRuBa.util.ElementSetCollector;
/*     */ import tyRuBa.util.ElementSource;
/*     */ import tyRuBa.util.RemovableElementSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedRuleBase
/*     */   extends Compiled
/*     */ {
/*     */   Compiled compiledContents;
/*  23 */   SemiDetCachedRuleBase mySemiDetCompanion = null;
/*     */   
/*     */ 
/*  26 */   private Map cache = null;
/*     */   
/*     */   public CachedRuleBase(Compiled compiledRuleBase) {
/*  29 */     super(compiledRuleBase.getMode());
/*  30 */     this.compiledContents = compiledRuleBase;
/*  31 */     initCache();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initCache()
/*     */   {
/*  40 */     this.cache = (RuleBase.softCache ? new SoftValueMap() : new HashMap());
/*     */   }
/*     */   
/*     */   public ElementSource runNonDet(Object input, RBContext context)
/*     */   {
/*  45 */     RBTuple other = (RBTuple)input;
/*  46 */     FormKey k = new FormKey(other);
/*  47 */     CachedRuleBase.CacheEntry entry = (CachedRuleBase.CacheEntry)this.cache.get(k);
/*     */     ElementCollector cachedResult;
/*  49 */     if ((entry == null) || 
/*  50 */       ((cachedResult = entry.getCachedResult()) == null))
/*     */     {
/*  52 */       ElementCollector result = new ElementSetCollector();
/*  53 */       if (!RuleBase.silent)
/*  54 */         if (entry == null) {
/*  55 */           System.err.print(".");
/*     */         } else
/*  57 */           System.err.print("@");
/*  58 */       entry = new CachedRuleBase.CacheEntry(this, k, result);
/*  59 */       this.cache.put(k, entry);
/*     */       
/*  61 */       result.setSource(new CachedRuleBase.1(this, other, context));
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
/*  72 */       return result.elements(); }
/*     */     ElementCollector cachedResult;
/*  74 */     if (!RuleBase.silent) {
/*  75 */       System.err.print("H");
/*     */     }
/*     */     
/*  78 */     Frame call = new Frame();
/*  79 */     if (other.sameForm(entry.key.theKey, call, new Frame()))
/*     */     {
/*     */ 
/*     */ 
/*  83 */       return cachedResult.elements().map(new CachedRuleBase.2(this, call));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */     throw new Error("Should never happen");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SemiDetCompiled first()
/*     */   {
/* 121 */     if (this.mySemiDetCompanion == null) {
/* 122 */       this.mySemiDetCompanion = new SemiDetCachedRuleBase(this.compiledContents.first());
/*     */     }
/* 124 */     return this.mySemiDetCompanion;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 128 */     return "CACHED RULEBASE(...)";
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/CachedRuleBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */