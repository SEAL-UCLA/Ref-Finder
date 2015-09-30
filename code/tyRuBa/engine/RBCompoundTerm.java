/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.RepAsJavaConstructorType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeMapping;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.modes.UserDefinedTypeConstructor;
/*     */ import tyRuBa.util.ObjectTuple;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RBCompoundTerm
/*     */   extends RBTerm
/*     */ {
/*     */   public static RBCompoundTerm make(ConstructorType constructorType, RBTerm term)
/*     */   {
/*  26 */     return new RBGenericCompoundTerm(constructorType, term);
/*     */   }
/*     */   
/*     */   public static RBTerm makeRepAsJava(RepAsJavaConstructorType type, Object obj) {
/*  30 */     return new RBRepAsJavaObjectCompoundTerm(type, obj);
/*     */   }
/*     */   
/*     */   public static RBTerm makeJava(Object o) {
/*  34 */     if ((o instanceof Object[]))
/*     */     {
/*  36 */       Object[] array = (Object[])o;
/*  37 */       RBTerm[] terms = new RBTerm[array.length];
/*  38 */       for (int i = 0; i < array.length; i++)
/*  39 */         terms[i] = makeJava(array[i]);
/*  40 */       return FrontEnd.makeList(terms);
/*     */     }
/*  42 */     if ((o instanceof UppedTerm)) {
/*  43 */       return ((UppedTerm)o).down();
/*     */     }
/*  45 */     return new RBJavaObjectCompoundTerm(o);
/*     */   }
/*     */   
/*     */   public int getNumArgs()
/*     */   {
/*  50 */     return getConstructorType().getArity();
/*     */   }
/*     */   
/*     */   public RBTerm getArg(int i) {
/*  54 */     if ((getArg() instanceof RBTuple))
/*  55 */       return ((RBTuple)getArg()).getSubterm(i);
/*  56 */     if (i == 0) {
/*  57 */       return getArg();
/*     */     }
/*  59 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract RBTerm getArg();
/*     */   
/*     */   public abstract ConstructorType getConstructorType();
/*     */   
/*     */   public boolean equals(Object x)
/*     */   {
/*  69 */     if (!(x instanceof RBCompoundTerm)) {
/*  70 */       return false;
/*     */     }
/*  72 */     RBCompoundTerm cx = (RBCompoundTerm)x;
/*  73 */     if (cx.getConstructorType().equals(getConstructorType())) {
/*  74 */       return cx.getArg().equals(getArg());
/*     */     }
/*  76 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int formHashCode()
/*     */   {
/*  82 */     return getConstructorType().hashCode() * 19 + getArg().formHashCode();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  86 */     return getConstructorType().hashCode() * 19 + getArg().hashCode();
/*     */   }
/*     */   
/*     */   boolean freefor(RBVariable v) {
/*  90 */     return getArg().freefor(v);
/*     */   }
/*     */   
/*     */   public BindingMode getBindingMode(ModeCheckContext context) {
/*  94 */     BindingMode bm = getArg().getBindingMode(context);
/*  95 */     if (bm.isBound()) {
/*  96 */       return bm;
/*     */     }
/*  98 */     return Factory.makePartiallyBound();
/*     */   }
/*     */   
/*     */   public boolean isGround()
/*     */   {
/* 103 */     return getArg().isGround();
/*     */   }
/*     */   
/*     */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/* 107 */     if (!(other instanceof RBCompoundTerm)) {
/* 108 */       return false;
/*     */     }
/* 110 */     RBCompoundTerm cother = (RBCompoundTerm)other;
/* 111 */     if (getConstructorType().equals(cother.getConstructorType())) {
/* 112 */       return getArg().sameForm(cother.getArg(), lr, rl);
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Frame unify(RBTerm other, Frame f)
/*     */   {
/* 120 */     if (!(other instanceof RBCompoundTerm)) {
/* 121 */       if ((other instanceof RBVariable)) {
/* 122 */         return other.unify(this, f);
/*     */       }
/* 124 */       return null;
/*     */     }
/*     */     
/* 127 */     RBCompoundTerm cother = (RBCompoundTerm)other;
/* 128 */     if (!cother.getConstructorType().equals(getConstructorType())) {
/* 129 */       return null;
/*     */     }
/* 131 */     return getArg().unify(cother.getArg(), f);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     return getConstructorType().getFunctorId().toString() + getArg();
/*     */   }
/*     */   
/*     */   protected Type getType(TypeEnv env) throws TypeModeError {
/* 141 */     Type argType = getArg().getType(env);
/* 142 */     return getConstructorType().apply(argType);
/*     */   }
/*     */   
/*     */   public void makeAllBound(ModeCheckContext context) {
/* 146 */     getArg().makeAllBound(context);
/*     */   }
/*     */   
/*     */   public Object up() {
/* 150 */     TypeConstructor tc = getTypeConstructor();
/* 151 */     if ((tc instanceof UserDefinedTypeConstructor)) {
/* 152 */       TypeMapping mapping = ((UserDefinedTypeConstructor)tc).getMapping();
/* 153 */       if (mapping != null) {
/* 154 */         return mapping.toJava(getArg().up());
/*     */       }
/*     */     }
/* 157 */     return super.up();
/*     */   }
/*     */   
/*     */   public final TypeConstructor getTypeConstructor() {
/* 161 */     return getConstructorType().getTypeConst();
/*     */   }
/*     */   
/*     */   public Object accept(TermVisitor v) {
/* 165 */     return v.visit(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFirst()
/*     */   {
/* 172 */     if (getNumArgs() > 0) {
/* 173 */       return getConstructorType().getFunctorId().getName() + getArg(0).getFirst();
/*     */     }
/* 175 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSecond()
/*     */   {
/* 183 */     int numArgs = getNumArgs();
/* 184 */     if (numArgs > 1)
/*     */     {
/* 186 */       Object second = getArg(0).getSecond();
/* 187 */       Object[] objs = new Object[numArgs];
/* 188 */       objs[0] = second;
/* 189 */       for (int i = 1; i < numArgs; i++) {
/* 190 */         RBTerm arg = getArg(i);
/* 191 */         if ((arg instanceof RBRepAsJavaObjectCompoundTerm)) {
/* 192 */           objs[i] = ((RBRepAsJavaObjectCompoundTerm)arg).getValue();
/* 193 */         } else if ((arg instanceof RBJavaObjectCompoundTerm)) {
/* 194 */           objs[i] = ((RBJavaObjectCompoundTerm)arg).getObject();
/*     */         } else {
/* 196 */           objs[i] = arg;
/*     */         }
/*     */       }
/* 199 */       return ObjectTuple.make(objs); }
/* 200 */     if (numArgs > 0) {
/* 201 */       return getArg(0).getSecond();
/*     */     }
/* 203 */     return ObjectTuple.theEmpty;
/*     */   }
/*     */   
/*     */   public boolean isOfType(TypeConstructor t)
/*     */   {
/* 208 */     return t.isSuperTypeOf(getConstructorType().getTypeConst());
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBCompoundTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */