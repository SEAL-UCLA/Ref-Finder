/*     */ package lsd.rule;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class LSDFact extends LSDLiteral implements Comparable<LSDFact>
/*     */ {
/*     */   private LSDFact(LSDPredicate pred, ArrayList<LSDBinding> bindings, boolean nonNegated) throws LSDInvalidTypeException
/*     */   {
/*   9 */     super(pred, bindings, nonNegated);
/*     */   }
/*     */   
/*     */   public boolean contains(String filter) {
/*  13 */     return super.toString().contains(filter);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  17 */     if ((o instanceof LSDFact)) {
/*  18 */       LSDFact of = (LSDFact)o;
/*  19 */       if (of.toString().equals(toString())) {
/*  20 */         return true;
/*     */       }
/*     */     }
/*  23 */     return false;
/*     */   }
/*     */   
/*     */   public static LSDFact createLSDFact(LSDPredicate pred, ArrayList<LSDBinding> bindings) {
/*  27 */     LSDFact theFact = null;
/*     */     try {
/*  29 */       theFact = new LSDFact(pred, bindings, true);
/*     */     }
/*     */     catch (LSDInvalidTypeException e) {
/*  32 */       System.err.println("LSDFact cannot have an invalid type.");
/*  33 */       System.exit(1);
/*     */     }
/*  35 */     return theFact;
/*     */   }
/*     */   
/*     */   public int compareTo(LSDFact arg0) {
/*  39 */     return toString().compareTo(arg0.toString());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  43 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public static LSDFact createLSDFact(LSDPredicate pred, java.util.List<String> constants, boolean nonNegated)
/*     */   {
/*  48 */     ArrayList<LSDBinding> bindings = new ArrayList();
/*  49 */     for (String s : constants) {
/*  50 */       bindings.add(new LSDBinding("\"" + s + "\""));
/*     */     }
/*  52 */     LSDFact theFact = null;
/*     */     try
/*     */     {
/*  55 */       theFact = new LSDFact(pred, bindings, nonNegated);
/*     */     }
/*     */     catch (LSDInvalidTypeException e) {
/*  58 */       System.err.println("LSDFact cannot have an invalid type.");
/*  59 */       System.exit(1);
/*     */     }
/*  61 */     if (theFact.bindings == null)
/*  62 */       return null;
/*  63 */     return theFact;
/*     */   }
/*     */   
/*     */   public LSDFact nonNegatedCopy() {
/*     */     try {
/*  68 */       return new LSDFact(this.predicate, this.bindings, true);
/*     */     } catch (LSDInvalidTypeException e) {
/*  70 */       System.err.println("LSDFact cannot have an invalid type.");
/*  71 */       System.exit(1); }
/*  72 */     return null;
/*     */   }
/*     */   
/*     */   public LSDFact addedCopy()
/*     */   {
/*  77 */     LSDPredicate newPredicate = this.predicate.getPrefixPredicate("added");
/*  78 */     if (newPredicate == null)
/*     */     {
/*  80 */       System.err.println("All predicates should have an added/deleted version.");
/*  81 */       System.exit(1);
/*  82 */       return null;
/*     */     }
/*     */     try {
/*  85 */       return new LSDFact(newPredicate, this.bindings, true);
/*     */     } catch (LSDInvalidTypeException e) {
/*  87 */       System.err.println("LSDFact cannot have an invalid type.");
/*  88 */       System.exit(1); }
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   public LSDFact deletedCopy()
/*     */   {
/*  94 */     LSDPredicate newPredicate = this.predicate.getPrefixPredicate("deleted");
/*  95 */     if (newPredicate == null)
/*     */     {
/*  97 */       System.err.println("All predicates should have an added/deleted version.");
/*  98 */       System.exit(1);
/*  99 */       return null;
/*     */     }
/*     */     try {
/* 102 */       return new LSDFact(newPredicate, this.bindings, true);
/*     */     } catch (LSDInvalidTypeException e) {
/* 104 */       System.err.println("LSDFact cannot have an invalid type.");
/* 105 */       System.exit(1); }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getPrimaryConstants()
/*     */   {
/* 112 */     String name = getPredicate().getSuffix();
/* 113 */     if (name.equals("type")) {
/* 114 */       String[] s = { ((LSDBinding)this.bindings.get(0)).toString() };
/* 115 */       return s; }
/* 116 */     if (name.equals("field")) {
/* 117 */       String[] s = { ((LSDBinding)this.bindings.get(0)).toString() };
/* 118 */       return s; }
/* 119 */     if (name.equals("method")) {
/* 120 */       String[] s = { ((LSDBinding)this.bindings.get(0)).toString() };
/* 121 */       return s; }
/* 122 */     if (name.equals("typeintype")) {
/* 123 */       String[] s = { ((LSDBinding)this.bindings.get(0)).toString() };
/* 124 */       return s; }
/* 125 */     if (name.equals("inheritedmethod")) {
/* 126 */       String n = ((LSDBinding)this.bindings.get(0)).toString();
/* 127 */       String[] s = { ((LSDBinding)this.bindings.get(1)).toString() + "#" + n, 
/* 128 */         ((LSDBinding)this.bindings.get(2)).toString() };
/* 129 */       return s; }
/* 130 */     if (name.equals("inheritedfield")) {
/* 131 */       String n = ((LSDBinding)this.bindings.get(0)).toString();
/* 132 */       String[] s = { ((LSDBinding)this.bindings.get(1)).toString() + "#" + n, 
/* 133 */         ((LSDBinding)this.bindings.get(2)).toString() };
/* 134 */       return s; }
/* 135 */     if (name.equals("conditional")) {
/* 136 */       System.out.println("conditional within LSD Fact was called\n");
/*     */     }
/* 138 */     String[] s = new String[this.bindings.size()];
/* 139 */     for (int i = 0; i < s.length; i++) {
/* 140 */       s[i] = ((LSDBinding)this.bindings.get(i)).toString();
/*     */     }
/* 142 */     return s;
/*     */   }
/*     */   
/*     */   public String getReferenceConstant() {
/* 146 */     return 
/* 147 */       ((LSDBinding)this.bindings.get(this.predicate.getReferenceArgument())).toString();
/*     */   }
/*     */   
/*     */   public LSDFact convertToClassLevel()
/*     */   {
/* 152 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/rule/LSDFact.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */