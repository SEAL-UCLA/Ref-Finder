/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.util.ObjectTuple;
/*     */ import tyRuBa.util.TwoLevelKey;
/*     */ 
/*     */ public class RBTuple extends RBTerm implements TwoLevelKey
/*     */ {
/*     */   RBTerm[] subterms;
/*     */   
/*     */   public RBTuple(RBTerm t1)
/*     */   {
/*  21 */     this.subterms = new RBTerm[1];
/*  22 */     this.subterms[0] = t1;
/*     */   }
/*     */   
/*     */   public RBTuple(RBTerm t1, RBTerm t2) {
/*  26 */     this.subterms = new RBTerm[] { t1, t2 };
/*     */   }
/*     */   
/*     */   public RBTuple(RBTerm t1, RBTerm t2, RBTerm t3) {
/*  30 */     this.subterms = new RBTerm[] { t1, t2, t3 };
/*     */   }
/*     */   
/*     */   public RBTuple(RBTerm t1, RBTerm t2, RBTerm t3, RBTerm t4) {
/*  34 */     this.subterms = new RBTerm[] { t1, t2, t3, t4 };
/*     */   }
/*     */   
/*     */   public RBTuple(RBTerm t1, RBTerm t2, RBTerm t3, RBTerm t4, RBTerm t5) {
/*  38 */     this.subterms = new RBTerm[] { t1, t2, t3, t4, t5 };
/*     */   }
/*     */   
/*  41 */   public static RBTuple theEmpty = new RBTuple(new RBTerm[0]);
/*     */   
/*     */   public static RBTuple make(ArrayList terms) {
/*  44 */     return make((RBTerm[])terms.toArray(new RBTerm[terms.size()]));
/*     */   }
/*     */   
/*     */   public static RBTuple make(RBTerm[] terms) {
/*  48 */     if (terms.length == 0) {
/*  49 */       return theEmpty;
/*     */     }
/*  51 */     return new RBTuple(terms);
/*     */   }
/*     */   
/*     */   private RBTuple(RBTerm[] terms) {
/*  55 */     this.subterms = ((RBTerm[])terms.clone());
/*     */   }
/*     */   
/*     */   public static RBTuple makeSingleton(RBTerm term) {
/*  59 */     return new RBTuple(new RBTerm[] { term });
/*     */   }
/*     */   
/*     */   public int getNumSubterms() {
/*  63 */     return this.subterms.length;
/*     */   }
/*     */   
/*     */   public RBTerm getSubterm(int i) {
/*  67 */     return this.subterms[i];
/*     */   }
/*     */   
/*     */   public boolean equals(Object x) {
/*  71 */     if (x.getClass() != getClass())
/*  72 */       return false;
/*  73 */     RBTuple cx = (RBTuple)x;
/*  74 */     if (cx.subterms.length != this.subterms.length)
/*  75 */       return false;
/*  76 */     for (int i = 0; i < this.subterms.length; i++) {
/*  77 */       if (!this.subterms[i].equals(cx.subterms[i]))
/*  78 */         return false;
/*     */     }
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public int formHashCode() {
/*  84 */     int hash = this.subterms.length;
/*  85 */     for (int i = 0; i < this.subterms.length; i++)
/*  86 */       hash = hash * 19 + this.subterms[i].formHashCode();
/*  87 */     return hash;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  91 */     int hash = this.subterms.length;
/*  92 */     for (int i = 0; i < this.subterms.length; i++)
/*  93 */       hash = hash * 19 + this.subterms[i].hashCode();
/*  94 */     return hash;
/*     */   }
/*     */   
/*     */   boolean freefor(RBVariable v) {
/*  98 */     for (int i = 0; i < this.subterms.length; i++) {
/*  99 */       if (!this.subterms[i].freefor(v))
/* 100 */         return false;
/*     */     }
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public BindingMode getBindingMode(ModeCheckContext context) {
/* 106 */     for (int i = 0; i < getNumSubterms(); i++) {
/* 107 */       if (!getSubterm(i).getBindingMode(context).isBound())
/* 108 */         return Factory.makePartiallyBound();
/*     */     }
/* 110 */     return Factory.makeBound();
/*     */   }
/*     */   
/*     */   public boolean isGround() {
/* 114 */     for (int i = 0; i < getNumSubterms(); i++) {
/* 115 */       if (!getSubterm(i).isGround()) {
/* 116 */         return false;
/*     */       }
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */   
/*     */   public boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/* 123 */     if (other.getClass() != getClass())
/* 124 */       return false;
/* 125 */     RBTuple cother = (RBTuple)other;
/* 126 */     if (getNumSubterms() != cother.getNumSubterms())
/* 127 */       return false;
/* 128 */     for (int i = 0; i < this.subterms.length; i++) {
/* 129 */       if (!this.subterms[i].sameForm(cother.subterms[i], lr, rl))
/* 130 */         return false;
/*     */     }
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public Frame unify(RBTerm other, Frame f) {
/* 136 */     if (!(other instanceof RBTuple)) {
/* 137 */       if ((other instanceof RBVariable)) {
/* 138 */         return other.unify(this, f);
/*     */       }
/* 140 */       return null; }
/* 141 */     RBTuple cother = (RBTuple)other;
/* 142 */     int sz = getNumSubterms();
/* 143 */     if (sz != cother.getNumSubterms())
/* 144 */       return null;
/* 145 */     for (int i = 0; i < sz; i++) {
/* 146 */       f = this.subterms[i].unify(cother.subterms[i], f);
/* 147 */       if (f == null)
/* 148 */         return null;
/*     */     }
/* 150 */     return f;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 154 */     StringBuffer result = new StringBuffer("<");
/* 155 */     for (int i = 0; i < this.subterms.length; i++) {
/* 156 */       if (i > 0)
/* 157 */         result.append(",");
/* 158 */       result.append(this.subterms[i].toString());
/*     */     }
/* 160 */     result.append(">");
/* 161 */     return result.toString();
/*     */   }
/*     */   
/*     */   protected Type getType(TypeEnv env) throws TypeModeError {
/* 165 */     TupleType tlst = Factory.makeTupleType();
/* 166 */     for (int i = 0; i < this.subterms.length; i++) {
/* 167 */       tlst.add(this.subterms[i].getType(env));
/*     */     }
/* 169 */     return tlst;
/*     */   }
/*     */   
/*     */   public void makeAllBound(ModeCheckContext context) {
/* 173 */     for (int i = 0; i < getNumSubterms(); i++) {
/* 174 */       getSubterm(i).makeAllBound(context);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object accept(TermVisitor v) {
/* 179 */     return v.visit(this);
/*     */   }
/*     */   
/*     */   public RBTuple append(RBTuple other) {
/* 183 */     RBTerm[] parts = new RBTerm[getNumSubterms() + other.getNumSubterms()];
/* 184 */     for (int i = 0; i < getNumSubterms(); i++) {
/* 185 */       parts[i] = getSubterm(i);
/*     */     }
/* 187 */     for (int i = 0; i < other.getNumSubterms(); i++) {
/* 188 */       parts[(i + getNumSubterms())] = getSubterm(i);
/*     */     }
/* 190 */     return FrontEnd.makeTuple(parts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFirst()
/*     */   {
/* 197 */     if (this.subterms.length > 0) {
/* 198 */       return this.subterms[0].getFirst();
/*     */     }
/* 200 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSecond()
/*     */   {
/* 208 */     if (this.subterms.length > 1)
/*     */     {
/* 210 */       Object second = this.subterms[0].getSecond();
/* 211 */       Object[] objs = new Object[this.subterms.length];
/* 212 */       objs[0] = second;
/* 213 */       for (int i = 1; i < this.subterms.length; i++) {
/* 214 */         if ((this.subterms[i] instanceof RBRepAsJavaObjectCompoundTerm)) {
/* 215 */           objs[i] = ((RBRepAsJavaObjectCompoundTerm)this.subterms[i]).getValue();
/* 216 */         } else if ((this.subterms[i] instanceof RBJavaObjectCompoundTerm)) {
/* 217 */           objs[i] = ((RBJavaObjectCompoundTerm)this.subterms[i]).getObject();
/*     */         } else {
/* 219 */           objs[i] = this.subterms[i];
/*     */         }
/*     */       }
/* 222 */       return ObjectTuple.make(objs); }
/* 223 */     if (this.subterms.length > 0) {
/* 224 */       return this.subterms[0].getSecond();
/*     */     }
/* 226 */     return ObjectTuple.theEmpty;
/*     */   }
/*     */   
/*     */   public Object up()
/*     */   {
/* 231 */     Object[] objs = new Object[this.subterms.length];
/* 232 */     for (int i = 0; i < objs.length; i++) {
/* 233 */       objs[i] = this.subterms[i].up();
/*     */     }
/* 235 */     return objs;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */