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
/*  28 */   public static final Multiplicity zero = new Multiplicity.1(0);
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
/*  40 */   public static final Multiplicity one = new Multiplicity.2(1);
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
/*  58 */   public static final Multiplicity many = new Multiplicity.3(2);
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
/*  80 */   public static final Multiplicity infinite = new Multiplicity.4(999);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int compareInt;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Multiplicity(int compareInt)
/*     */   {
/*  97 */     this.compareInt = compareInt;
/*     */   }
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


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/Multiplicity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */