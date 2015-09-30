/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import tyRuBa.engine.ModedRuleBaseIndex;
/*     */ import tyRuBa.engine.PredicateIdentifier;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBTuple;
/*     */ import tyRuBa.engine.RBVariable;
/*     */ import tyRuBa.engine.RuleBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModeCheckContext
/*     */   implements Cloneable
/*     */ {
/*     */   private BindingEnv bindings;
/*     */   private ModedRuleBaseIndex rulebases;
/*     */   
/*     */   public ModeCheckContext(BindingEnv initialBindings, ModedRuleBaseIndex rulebases)
/*     */   {
/*  27 */     this.bindings = initialBindings;
/*  28 */     this.rulebases = rulebases;
/*     */   }
/*     */   
/*     */ 
/*     */   public RuleBase getBestRuleBase(PredicateIdentifier predId, RBTuple args, BindingList bindings)
/*     */   {
/*  34 */     for (int i = 0; i < args.getNumSubterms(); i++) {
/*  35 */       bindings.add(args.getSubterm(i).getBindingMode(this));
/*     */     }
/*  37 */     RuleBase result = this.rulebases.getBest(predId, bindings);
/*  38 */     return result;
/*     */   }
/*     */   
/*     */   public ModedRuleBaseIndex getModedRuleBaseIndex() {
/*  42 */     return this.rulebases;
/*     */   }
/*     */   
/*     */   public BindingEnv getBindingEnv() {
/*  46 */     return this.bindings;
/*     */   }
/*     */   
/*     */   public boolean isBound(RBVariable var)
/*     */   {
/*  51 */     return getBindingEnv().isBound(var);
/*     */   }
/*     */   
/*     */   public void removeAllBound(Collection vars) {
/*  55 */     Iterator itr = vars.iterator();
/*  56 */     while (itr.hasNext()) {
/*  57 */       RBVariable curr = (RBVariable)itr.next();
/*  58 */       if (isBound(curr)) {
/*  59 */         itr.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void makeBound(RBVariable variable)
/*     */   {
/*  66 */     this.bindings.putBindingMode(variable, Factory.makeBound());
/*     */   }
/*     */   
/*     */   public void bindVars(Collection vars)
/*     */   {
/*  71 */     Iterator itr = vars.iterator();
/*  72 */     while (itr.hasNext()) {
/*  73 */       makeBound((RBVariable)itr.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  78 */     ModeCheckContext cl = new ModeCheckContext(this.bindings, 
/*  79 */       getModedRuleBaseIndex());
/*  80 */     cl.bindings = ((BindingEnv)getBindingEnv().clone());
/*  81 */     return cl;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  85 */     return 
/*  86 */       "---------ModeCheckContext---------\nBindings: " + this.bindings;
/*     */   }
/*     */   
/*     */ 
/*     */   public ModeCheckContext intersection(ModeCheckContext other)
/*     */   {
/*  92 */     return new ModeCheckContext(getBindingEnv().intersection(
/*  93 */       other.getBindingEnv()), getModedRuleBaseIndex());
/*     */   }
/*     */   
/*     */   public boolean checkIfAllBound(Collection boundVars) {
/*  97 */     for (Iterator iter = boundVars.iterator(); iter.hasNext();) {
/*  98 */       RBVariable element = (RBVariable)iter.next();
/*  99 */       if (!isBound(element)) {
/* 100 */         return false;
/*     */       }
/*     */     }
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/ModeCheckContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */