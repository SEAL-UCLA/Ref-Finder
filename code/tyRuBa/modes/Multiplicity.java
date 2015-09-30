/*     */ package tyRuBa.modes;
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
/*     */ public abstract class Multiplicity
/*     */   implements Comparable
/*     */ {
/*     */   public static Multiplicity fromInt(int i)
/*     */   {
/*  17 */     if (i < 0)
/*  18 */       throw new Error("Only works for positive integers");
/*  19 */     if (i == 0)
/*  20 */       return zero;
/*  21 */     if (i == 1) {
/*  22 */       return one;
/*     */     }
/*  24 */     return many;
/*     */   }
/*     */   
/*     */ 
/*  28 */   public static final Multiplicity zero = new Multiplicity(0) {
/*     */     public String toString() {
/*  30 */       return "Mult(0)";
/*     */     }
/*     */     
/*  33 */     public Multiplicity multiply(Multiplicity other) { return this; }
/*     */     
/*     */     public Multiplicity add(Multiplicity other) {
/*  36 */       return other;
/*     */     }
/*     */   };
/*     */   
/*  40 */   public static final Multiplicity one = new Multiplicity(1) {
/*     */     public String toString() {
/*  42 */       return "Mult(1)";
/*     */     }
/*     */     
/*  45 */     public Multiplicity multiply(Multiplicity other) { return other; }
/*     */     
/*     */     public Multiplicity add(Multiplicity other) {
/*  48 */       if (other.equals(zero))
/*  49 */         return this;
/*  50 */       if (other.equals(infinite)) {
/*  51 */         return other;
/*     */       }
/*  53 */       return many;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*  58 */   public static final Multiplicity many = new Multiplicity(2)
/*     */   {
/*  60 */     public String toString() { return "Mult(>1)"; }
/*     */     
/*     */     public Multiplicity multiply(Multiplicity other) {
/*  63 */       if (other.equals(zero))
/*  64 */         return other;
/*  65 */       if (other.equals(infinite)) {
/*  66 */         return other;
/*     */       }
/*  68 */       return this;
/*     */     }
/*     */     
/*     */     public Multiplicity add(Multiplicity other) {
/*  72 */       if (other.equals(infinite)) {
/*  73 */         return other;
/*     */       }
/*  75 */       return this;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*  80 */   public static final Multiplicity infinite = new Multiplicity(999)
/*     */   {
/*  82 */     public String toString() { return "Mult(INFINITE)"; }
/*     */     
/*     */     public Multiplicity multiply(Multiplicity other) {
/*  85 */       if (other.equals(zero)) {
/*  86 */         return other;
/*     */       }
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public Multiplicity add(Multiplicity other) {
/*  92 */       return this;
/*     */     }
/*     */   };
/*     */   int compareInt;
/*     */   
/*  97 */   protected Multiplicity(int compareInt) { this.compareInt = compareInt; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Multiplicity multiply(Multiplicity paramMultiplicity);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Multiplicity add(Multiplicity paramMultiplicity);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(Object o)
/*     */   {
/* 115 */     if (this.compareInt < ((Multiplicity)o).compareInt)
/* 116 */       return -1;
/* 117 */     if (this.compareInt > ((Multiplicity)o).compareInt) {
/* 118 */       return 1;
/*     */     }
/* 120 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object arg0)
/*     */   {
/* 125 */     return compareTo(arg0) == 0;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 129 */     return (this.compareInt + 13) * 113;
/*     */   }
/*     */   
/*     */   public Multiplicity min(Multiplicity other) {
/* 133 */     if (compareTo(other) == 1) {
/* 134 */       return other;
/*     */     }
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public Multiplicity max(Multiplicity other)
/*     */   {
/* 141 */     if (compareTo(other) == -1) {
/* 142 */       return other;
/*     */     }
/* 144 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/Multiplicity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */