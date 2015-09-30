/*     */ package lsd.rule;
/*     */ 
/*     */ 
/*     */ public class LSDBinding
/*     */   implements Comparable<LSDBinding>
/*     */ {
/*     */   private char type;
/*     */   
/*     */   private LSDVariable variable;
/*  10 */   private String groundConst = null;
/*     */   
/*     */   public boolean isBound() {
/*  13 */     return this.groundConst != null;
/*     */   }
/*     */   
/*     */   public String getGroundConst() {
/*  17 */     String temp = this.groundConst;
/*  18 */     if (temp != null)
/*  19 */       temp = temp.substring(1, temp.length() - 1);
/*  20 */     return temp;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  24 */     if (!(other instanceof LSDBinding))
/*  25 */       return false;
/*  26 */     LSDBinding otherBinding = (LSDBinding)other;
/*  27 */     if ((this.groundConst == null) || (otherBinding.groundConst == null)) {
/*  28 */       if (this.groundConst != otherBinding.groundConst) {
/*  29 */         return false;
/*     */       }
/*  31 */     } else if (!this.groundConst.equals(otherBinding.groundConst)) {
/*  32 */       return false;
/*     */     }
/*  34 */     if ((this.variable == null) || (otherBinding.variable == null)) {
/*  35 */       if (this.variable != otherBinding.variable) {
/*  36 */         return false;
/*     */       }
/*  38 */     } else if (!this.variable.equals(otherBinding.variable)) {
/*  39 */       return false;
/*     */     }
/*  41 */     return true;
/*     */   }
/*     */   
/*     */   public LSDBinding(LSDVariable var) {
/*  45 */     this.variable = var;
/*  46 */     this.type = var.getType();
/*     */   }
/*     */   
/*     */   public LSDBinding(String cst)
/*     */   {
/*  51 */     this.variable = null;
/*  52 */     this.groundConst = cst;
/*     */   }
/*     */   
/*     */   public void ground(String cst) {
/*  56 */     this.type = this.variable.getType();
/*  57 */     this.variable = null;
/*  58 */     this.groundConst = cst;
/*     */   }
/*     */   
/*     */   private LSDBinding(LSDBinding toCopyFrom) {
/*  62 */     this.variable = toCopyFrom.variable;
/*  63 */     this.type = toCopyFrom.type;
/*  64 */     this.groundConst = toCopyFrom.groundConst;
/*     */   }
/*     */   
/*     */   public boolean typeChecks(char type)
/*     */   {
/*  69 */     return (this.variable == null) || (this.variable.typeChecks(type));
/*     */   }
/*     */   
/*     */   public LSDBinding substitute(LSDVariable toReplace, LSDBinding replacement)
/*     */     throws LSDInvalidTypeException
/*     */   {
/*  75 */     LSDBinding nb = new LSDBinding(this);
/*  76 */     if (this.variable == null)
/*  77 */       return nb;
/*  78 */     if (this.variable.equals(toReplace)) {
/*  79 */       if (!this.variable.typeChecks(toReplace))
/*  80 */         throw new LSDInvalidTypeException();
/*  81 */       if ((replacement.variable != null) && 
/*  82 */         (!this.variable.typeChecks(replacement.variable)))
/*  83 */         throw new LSDInvalidTypeException();
/*  84 */       nb.variable = replacement.variable;
/*  85 */       nb.type = this.variable.getType();
/*  86 */       nb.groundConst = replacement.groundConst;
/*     */     }
/*  88 */     return nb;
/*     */   }
/*     */   
/*     */   public LSDVariable getVariable() {
/*  92 */     return this.variable;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  96 */     if (this.variable != null) {
/*  97 */       return this.variable.toString();
/*     */     }
/*  99 */     return this.groundConst.toString();
/*     */   }
/*     */   
/*     */   public void replaceVar(LSDVariable newVar) {
/* 103 */     this.variable = newVar;
/* 104 */     this.type = newVar.getType();
/*     */   }
/*     */   
/*     */   public char getType() {
/* 108 */     return this.type;
/*     */   }
/*     */   
/*     */   public int compareTo(LSDBinding o)
/*     */   {
/* 113 */     String os = o.getType() + ":" + o.groundConst;
/* 114 */     String ts = this.type + ":" + this.groundConst;
/* 115 */     return os.compareTo(ts);
/*     */   }
/*     */   
/* 118 */   public void setType(char c) { this.type = c; }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/rule/LSDBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */