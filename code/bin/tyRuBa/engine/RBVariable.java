/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ 
/*     */ public class RBVariable extends RBSubstitutable
/*     */ {
/*  14 */   protected static int gensymctr = 1;
/*     */   
/*     */   public static RBVariable makeUnique(String id) {
/*  17 */     return new RBVariable(new String(id));
/*     */   }
/*     */   
/*     */   public static RBVariable make(String id) {
/*  21 */     return new RBVariable(id.intern());
/*     */   }
/*     */   
/*     */ 
/*     */   protected RBVariable(String id)
/*     */   {
/*  27 */     super(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Frame bind(RBTerm val, Frame f)
/*     */   {
/*  34 */     f.put(this, val);
/*  35 */     return f;
/*     */   }
/*     */   
/*     */   public Frame unify(RBTerm other, Frame f)
/*     */   {
/*  40 */     if ((other instanceof RBIgnoredVariable))
/*  41 */       return f;
/*  42 */     RBTerm val = f.get(this);
/*  43 */     if (val == null) {
/*  44 */       other = other.substitute(f);
/*  45 */       if (equals(other))
/*  46 */         return f;
/*  47 */       if (other.freefor(this)) {
/*  48 */         return bind(other, f);
/*     */       }
/*  50 */       return null;
/*     */     }
/*  52 */     return val.unify(other, f);
/*     */   }
/*     */   
/*     */   boolean freefor(RBVariable v) {
/*  56 */     return !equals(v);
/*     */   }
/*     */   
/*     */   public BindingMode getBindingMode(ModeCheckContext context) {
/*  60 */     if (context.isBound(this)) {
/*  61 */       return Factory.makeBound();
/*     */     }
/*  63 */     return Factory.makeFree();
/*     */   }
/*     */   
/*     */   public boolean isGround()
/*     */   {
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/*  72 */     if (other.getClass() != getClass()) {
/*  73 */       return false;
/*     */     }
/*  75 */     RBVariable binding = (RBVariable)lr.get(this);
/*  76 */     if (binding == null) {
/*  77 */       lr.put(this, other);
/*  78 */     } else if (!binding.equals(other)) {
/*  79 */       return false;
/*     */     }
/*  81 */     binding = (RBVariable)rl.get(other);
/*  82 */     if (binding == null) {
/*  83 */       rl.put(other, this);
/*  84 */       return true;
/*     */     }
/*  86 */     return equals(binding);
/*     */   }
/*     */   
/*     */ 
/*     */   public int formHashCode()
/*     */   {
/*  92 */     return 1;
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  96 */     return makeUnique(this.name);
/*     */   }
/*     */   
/*     */   protected Type getType(TypeEnv env) {
/* 100 */     return env.get(this);
/*     */   }
/*     */   
/*     */   public void makeAllBound(ModeCheckContext context) {
/* 104 */     context.makeBound(this);
/*     */   }
/*     */   
/*     */   public Object accept(TermVisitor v) {
/* 108 */     return v.visit(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream in)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 116 */     in.defaultReadObject();
/* 117 */     this.name = this.name.intern();
/*     */   }
/*     */   
/*     */   public String getFirst()
/*     */   {
/* 122 */     throw new Error("Variables cannot be two level keys");
/*     */   }
/*     */   
/*     */   public Object getSecond() {
/* 126 */     throw new Error("Variables cannot be two level keys");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */