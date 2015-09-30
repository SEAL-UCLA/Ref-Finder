/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import serp.util.SoftValueMap;
/*    */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*    */ 
/*    */ 
/*    */ public class SemiDetCachedRuleBase
/*    */   extends SemiDetCompiled
/*    */ {
/*    */   SemiDetCompiled compiledContents;
/* 14 */   private Map cache = null;
/*    */   
/*    */   public SemiDetCachedRuleBase(SemiDetCompiled compiledRuleBase) {
/* 17 */     super(compiledRuleBase.getMode());
/* 18 */     this.compiledContents = compiledRuleBase;
/* 19 */     initCache();
/*    */   }
/*    */   
/*    */   private void initCache() {
/* 23 */     this.cache = (RuleBase.softCache ? new SoftValueMap() : new HashMap());
/*    */   }
/*    */   
/*    */   public Frame runSemiDet(Object input, RBContext context)
/*    */   {
/* 28 */     RBTuple other = (RBTuple)input;
/* 29 */     FormKey k = new FormKey(other);
/* 30 */     CacheEntry entry = (CacheEntry)this.cache.get(k);
/*    */     Frame cachedResult;
/* 32 */     if ((entry == null) || 
/* 33 */       ((cachedResult = entry.getCachedResult()) == null))
/*    */     {
/* 35 */       if (!RuleBase.silent)
/* 36 */         if (entry == null) {
/* 37 */           System.err.print(".");
/*    */         } else
/* 39 */           System.err.print("@");
/* 40 */       Frame result = this.compiledContents.runSemiDet(input, context);
/* 41 */       entry = new CacheEntry(k, result);
/* 42 */       this.cache.put(k, entry);
/* 43 */       return result; }
/*    */     Frame cachedResult;
/* 45 */     if (!RuleBase.silent)
/* 46 */       System.err.print("H");
/* 47 */     Frame call = new Frame();
/* 48 */     if (other.sameForm(entry.key.theKey, call, new Frame())) {
/* 49 */       return call.callResult(cachedResult);
/*    */     }
/* 51 */     throw new Error("Should never happen");
/*    */   }
/*    */   
/*    */   private class CacheEntry
/*    */   {
/*    */     FormKey key;
/*    */     Frame result;
/*    */     
/*    */     CacheEntry(FormKey k, Frame r) {
/* 60 */       this.result = r;
/* 61 */       this.key = k;
/*    */     }
/*    */     
/* 64 */     Frame getCachedResult() { return this.result; }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 69 */     return "SEMIDET CACHED RULEBASE(...)";
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/SemiDetCachedRuleBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */