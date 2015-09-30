/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Mode
/*     */   implements Cloneable
/*     */ {
/*     */   public final Multiplicity lo;
/*     */   public final Multiplicity hi;
/*     */   private final String printString;
/*  19 */   private int numFree = 0;
/*  20 */   private int numBound = 0;
/*     */   
/*     */   public Mode(Multiplicity lo, Multiplicity hi) {
/*  23 */     this(lo, hi, 0, 0);
/*     */   }
/*     */   
/*     */   public Mode(Multiplicity lo, Multiplicity hi, int numFree, int numBound) {
/*  27 */     this.lo = lo;
/*  28 */     this.hi = hi;
/*  29 */     this.numFree = numFree;
/*  30 */     this.numBound = numBound;
/*  31 */     if (lo.equals(Multiplicity.zero)) {
/*  32 */       if (hi.equals(Multiplicity.zero)) {
/*  33 */         this.printString = "FAIL";
/*  34 */       } else if (hi.equals(Multiplicity.one)) {
/*  35 */         this.printString = "SEMIDET";
/*  36 */       } else if (hi.equals(Multiplicity.many)) {
/*  37 */         this.printString = "NONDET";
/*  38 */       } else if (hi.equals(Multiplicity.infinite)) {
/*  39 */         this.printString = "INFINITE";
/*     */       } else {
/*  41 */         throw new Error("this should not happen");
/*     */       }
/*  43 */     } else if (lo.equals(Multiplicity.one)) {
/*  44 */       if (hi.equals(Multiplicity.one)) {
/*  45 */         this.printString = "DET";
/*  46 */       } else if (hi.equals(Multiplicity.many)) {
/*  47 */         this.printString = "MULTI";
/*  48 */       } else if (hi.equals(Multiplicity.infinite)) {
/*  49 */         this.printString = "INFINITE";
/*     */       } else {
/*  51 */         throw new Error("this should not happen");
/*     */       }
/*     */     } else {
/*  54 */       System.err.println("lo = " + lo + "\nhi = " + hi);
/*  55 */       throw new Error("this should not happen");
/*     */     }
/*     */   }
/*     */   
/*     */   public static Mode makeFail() {
/*  60 */     return new Mode(Multiplicity.zero, Multiplicity.zero, 0, 0);
/*     */   }
/*     */   
/*     */   public static Mode makeSemidet() {
/*  64 */     return new Mode(Multiplicity.zero, Multiplicity.one, 0, 0);
/*     */   }
/*     */   
/*     */   public static Mode makeDet() {
/*  68 */     return new Mode(Multiplicity.one, Multiplicity.one, 0, 0);
/*     */   }
/*     */   
/*     */   public static Mode makeNondet() {
/*  72 */     return new Mode(Multiplicity.zero, Multiplicity.many, 0, 0);
/*     */   }
/*     */   
/*     */   public static Mode makeMulti() {
/*  76 */     return new Mode(Multiplicity.one, Multiplicity.many, 0, 0);
/*     */   }
/*     */   
/*     */   public boolean isFail() {
/*  80 */     return (this.lo.equals(Multiplicity.zero)) && (this.hi.equals(Multiplicity.zero));
/*     */   }
/*     */   
/*     */   public boolean isSemiDet() {
/*  84 */     return (this.lo.equals(Multiplicity.zero)) && (this.hi.equals(Multiplicity.one));
/*     */   }
/*     */   
/*     */   public boolean isDet() {
/*  88 */     return (this.lo.equals(Multiplicity.one)) && (this.hi.equals(Multiplicity.one));
/*     */   }
/*     */   
/*     */   public boolean isNondet() {
/*  92 */     return (this.lo.equals(Multiplicity.zero)) && (this.hi.equals(Multiplicity.many));
/*     */   }
/*     */   
/*     */   public boolean isMulti() {
/*  96 */     return (this.lo.equals(Multiplicity.one)) && (this.hi.equals(Multiplicity.many));
/*     */   }
/*     */   
/*     */   public static Mode makeConvertTo() {
/* 100 */     return new Mode(Multiplicity.zero, Multiplicity.one, 1, 1);
/*     */   }
/*     */   
/*     */   public static Mode convertFromString(String modeString) {
/* 104 */     if (modeString.equals("DET"))
/* 105 */       return makeDet();
/* 106 */     if (modeString.equals("SEMIDET"))
/* 107 */       return makeSemidet();
/* 108 */     if (modeString.equals("NONDET"))
/* 109 */       return makeNondet();
/* 110 */     if (modeString.equals("MULTI"))
/* 111 */       return makeMulti();
/* 112 */     if (modeString.equals("FAIL"))
/* 113 */       return makeFail();
/* 114 */     if (modeString.equals("ERROR")) {
/* 115 */       return new ErrorMode("");
/*     */     }
/* 117 */     throw new Error("unknown mode " + modeString);
/*     */   }
/*     */   
/*     */   public Mode add(Mode other)
/*     */   {
/* 122 */     if (other == null)
/* 123 */       return this;
/* 124 */     if ((other instanceof ErrorMode)) {
/* 125 */       return other.add(this);
/*     */     }
/* 127 */     return new Mode(this.lo.max(other.lo), this.hi.add(other.hi), 
/* 128 */       this.numFree + other.numFree, this.numBound + other.numBound);
/*     */   }
/*     */   
/*     */   public Mode multiply(Mode other)
/*     */   {
/* 133 */     if ((other instanceof ErrorMode)) {
/* 134 */       return other;
/*     */     }
/* 136 */     return new Mode(this.lo.multiply(other.lo), this.hi.multiply(other.hi), 
/* 137 */       this.numFree + other.numFree, this.numBound + other.numBound);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 142 */     return this.printString;
/*     */   }
/*     */   
/*     */   public double getPercentFree() {
/* 146 */     if (this.numFree == 0) {
/* 147 */       return 0.0D;
/*     */     }
/* 149 */     return this.numFree / (this.numFree + this.numBound);
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 154 */     if ((other instanceof Mode)) {
/* 155 */       Mode om = (Mode)other;
/* 156 */       return (this.hi.equals(om.hi)) && (this.lo.equals(om.lo));
/*     */     }
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 163 */     return this.hi.hashCode() + 13 * this.lo.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int compareTo(Mode other)
/*     */   {
/* 170 */     int result = this.hi.compareTo(other.hi);
/* 171 */     if (result == 0) {
/* 172 */       result = this.lo.compareTo(other.lo);
/* 173 */       if (result == 0) {
/* 174 */         double thisPercentFree = getPercentFree();
/* 175 */         double otherPercentFree = other.getPercentFree();
/* 176 */         if (thisPercentFree < otherPercentFree)
/* 177 */           return -1;
/* 178 */         if (thisPercentFree > otherPercentFree) {
/* 179 */           return 1;
/*     */         }
/* 181 */         return 0;
/*     */       }
/*     */     }
/*     */     
/* 185 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isBetterThan(Mode other) {
/* 189 */     return compareTo(other) < 0;
/*     */   }
/*     */   
/*     */   public boolean compatibleWith(Mode declared) {
/* 193 */     return declared.hi.compareTo(this.hi) >= 0;
/*     */   }
/*     */   
/*     */   public Mode first() {
/* 197 */     if ((this instanceof ErrorMode)) {
/* 198 */       return this;
/*     */     }
/* 200 */     return new Mode(this.lo.min(Multiplicity.one), this.hi.min(Multiplicity.one), 
/* 201 */       this.numFree, this.numBound);
/*     */   }
/*     */   
/*     */   public Mode negate()
/*     */   {
/* 206 */     if (isFail())
/* 207 */       return new Mode(Multiplicity.one, Multiplicity.one, this.numFree, this.numBound);
/* 208 */     if ((this instanceof ErrorMode))
/* 209 */       return this;
/* 210 */     if ((isDet()) || (isMulti())) {
/* 211 */       return new Mode(Multiplicity.zero, Multiplicity.zero, this.numFree, this.numBound);
/*     */     }
/* 213 */     return new Mode(Multiplicity.zero, Multiplicity.one, this.numFree, this.numBound);
/*     */   }
/*     */   
/*     */   public Mode unique()
/*     */   {
/* 218 */     if ((isDet()) || (isFail()))
/* 219 */       return new Mode(this.lo, this.hi, this.numFree, this.numBound);
/* 220 */     if ((this instanceof ErrorMode)) {
/* 221 */       return this;
/*     */     }
/* 223 */     return new Mode(Multiplicity.zero, Multiplicity.one, this.numFree, this.numBound);
/*     */   }
/*     */   
/*     */   public Mode restrictedBy(Mode upperBound)
/*     */   {
/* 228 */     if (this.hi.compareTo(upperBound.hi) > 0) {
/* 229 */       return new Mode(this.lo, upperBound.hi, this.numFree, this.numBound);
/*     */     }
/* 231 */     return this;
/*     */   }
/*     */   
/*     */   public Mode findAll() {
/* 235 */     return new Mode(Multiplicity.one, Multiplicity.one, this.numFree, this.numBound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Mode moreBound()
/*     */   {
/* 243 */     return new Mode(Multiplicity.zero, this.hi, this.numFree, this.numBound);
/*     */   }
/*     */   
/*     */   public void setPercentFree(BindingList bindings) {
/* 247 */     for (int i = 0; i < bindings.size(); i++) {
/* 248 */       if (bindings.get(i).isBound()) {
/* 249 */         this.numBound += 1;
/*     */       } else {
/* 251 */         this.numFree += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 258 */       return super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 260 */       throw new Error("This should not happen");
/*     */     }
/*     */   }
/*     */   
/*     */   public Mode noOverlapWith(Mode other) {
/* 265 */     if (other == null) {
/* 266 */       return this;
/*     */     }
/* 268 */     return new Mode(this.lo.min(other.lo), this.hi.max(other.hi));
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/Mode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */