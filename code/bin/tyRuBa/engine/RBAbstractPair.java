/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ 
/*     */ public abstract class RBAbstractPair extends RBTerm
/*     */ {
/*     */   private RBTerm car;
/*     */   private RBTerm cdr;
/*     */   
/*     */   public RBAbstractPair(RBTerm aCar, RBTerm aCdr)
/*     */   {
/*  13 */     this.car = aCar;
/*  14 */     this.cdr = aCdr;
/*     */   }
/*     */   
/*     */   public RBTerm getCar() {
/*  18 */     return this.car;
/*     */   }
/*     */   
/*     */   public RBTerm getCdr() {
/*  22 */     return this.cdr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCdr(RBTerm aCdr)
/*     */   {
/*  29 */     if (this.cdr == null) {
/*  30 */       if (aCdr != null) {
/*  31 */         this.cdr = aCdr;
/*     */       } else
/*  33 */         throw new IllegalArgumentException("Cannot set cdr to null");
/*     */     } else
/*  35 */       throw new IllegalStateException("Cannot set cdr more than once");
/*     */   }
/*     */   
/*     */   public RBTerm getCddr() {
/*  39 */     return ((RBAbstractPair)this.cdr).getCdr();
/*     */   }
/*     */   
/*     */   public int getNumSubterms() throws ImproperListException {
/*  43 */     RBTerm cdr = getCdr();
/*  44 */     if ((cdr instanceof RBAbstractPair))
/*  45 */       return ((RBAbstractPair)cdr).getNumSubterms() + 1;
/*  46 */     if (cdr.equals(FrontEnd.theEmptyList)) {
/*  47 */       return 1;
/*     */     }
/*  49 */     throw new ImproperListException();
/*     */   }
/*     */   
/*     */   public RBTerm getSubterm(int i)
/*     */   {
/*  54 */     if (i == 0) {
/*  55 */       return getCar();
/*     */     }
/*     */     try {
/*  58 */       return ((RBAbstractPair)getCdr()).getSubterm(i - 1);
/*     */     } catch (ClassCastException e) {
/*  60 */       throw new java.util.NoSuchElementException();
/*     */     }
/*     */   }
/*     */   
/*     */   public RBTerm[] getSubterms() throws ImproperListException
/*     */   {
/*  66 */     int sz = getNumSubterms();
/*  67 */     RBTerm[] result = new RBTerm[sz];
/*  68 */     RBTerm current = this;
/*  69 */     for (int i = 0; (current instanceof RBAbstractPair); i++) {
/*  70 */       result[i] = ((RBAbstractPair)current).car;
/*  71 */       current = ((RBAbstractPair)current).cdr;
/*     */     }
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   protected final String cdrToString(boolean begin, RBTerm cdr) {
/*  77 */     String result = "";
/*  78 */     if (cdr.getClass() == getClass()) {
/*  79 */       RBAbstractPair pcdr = (RBAbstractPair)cdr;
/*  80 */       if (!begin)
/*  81 */         result = result + ",";
/*  82 */       result = result + pcdr.getCar() + cdrToString(false, pcdr.getCdr());
/*  83 */     } else if (!cdr.equals(FrontEnd.theEmptyList)) {
/*  84 */       result = result + "|" + cdr;
/*     */     }
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   public Frame unify(RBTerm other, Frame f)
/*     */   {
/*  91 */     if (other.getClass() == getClass()) {
/*  92 */       RBAbstractPair cother = (RBAbstractPair)other;
/*  93 */       f = getCar().unify(cother.getCar(), f);
/*  94 */       if (f != null) {
/*  95 */         f = getCdr().unify(cother.getCdr(), f);
/*     */       }
/*  97 */       return f; }
/*  98 */     if ((other instanceof RBVariable)) {
/*  99 */       return other.unify(this, f);
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/* 105 */     if (other.getClass() == getClass()) {
/* 106 */       RBAbstractPair cother = (RBAbstractPair)other;
/*     */       
/* 108 */       return (getCar().sameForm(cother.getCar(), lr, rl)) && (getCdr().sameForm(cother.getCdr(), lr, rl));
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   boolean freefor(RBVariable v) {
/* 114 */     return (this.car.freefor(v)) && (this.cdr.freefor(v));
/*     */   }
/*     */   
/*     */   public boolean equals(Object x) {
/* 118 */     if (x == null)
/* 119 */       return false;
/* 120 */     if (x.getClass() != getClass())
/* 121 */       return false;
/* 122 */     RBAbstractPair cx = (RBAbstractPair)x;
/* 123 */     return (getCar().equals(cx.getCar())) && (getCdr().equals(cx.getCdr()));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 127 */     return this.car.hashCode() + 11 * this.cdr.hashCode();
/*     */   }
/*     */   
/*     */   public int formHashCode() {
/* 131 */     return this.car.formHashCode() + 11 * this.cdr.formHashCode();
/*     */   }
/*     */   
/*     */   public void makeAllBound(ModeCheckContext context) {
/* 135 */     getCar().makeAllBound(context);
/* 136 */     getCdr().makeAllBound(context);
/*     */   }
/*     */   
/*     */   public BindingMode getBindingMode(ModeCheckContext context) {
/* 140 */     BindingMode carMode = getCar().getBindingMode(context);
/* 141 */     BindingMode cdrMode = getCdr().getBindingMode(context);
/* 142 */     if ((carMode.isBound()) && (cdrMode.isBound())) {
/* 143 */       return carMode;
/*     */     }
/* 145 */     return tyRuBa.modes.Factory.makePartiallyBound();
/*     */   }
/*     */   
/*     */   public boolean isGround()
/*     */   {
/* 150 */     return (getCar().isGround()) && (getCdr().isGround());
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBAbstractPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */