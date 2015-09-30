/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.RepAsJavaConstructorType;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.util.TwoLevelKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBRepAsJavaObjectCompoundTerm
/*     */   extends RBCompoundTerm
/*     */ {
/*     */   Object javaObject;
/*     */   RepAsJavaConstructorType typeTag;
/*     */   
/*     */   public RBRepAsJavaObjectCompoundTerm(RepAsJavaConstructorType type, Object obj)
/*     */   {
/*  29 */     this.typeTag = type;
/*  30 */     this.javaObject = obj;
/*     */   }
/*     */   
/*     */   public RBTerm getArg() {
/*  34 */     return RBCompoundTerm.makeJava(this.javaObject);
/*     */   }
/*     */   
/*     */   public RBTerm getArg(int i) {
/*  38 */     if (i == 0) {
/*  39 */       return this;
/*     */     }
/*  41 */     throw new Error("Argument not found " + i);
/*     */   }
/*     */   
/*     */   public int getNumArgs() {
/*  45 */     return 1;
/*     */   }
/*     */   
/*     */   boolean freefor(RBVariable v) {
/*  49 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isGround() {
/*  53 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/*  57 */     return equals(other);
/*     */   }
/*     */   
/*     */   protected Type getType(TypeEnv env) throws TypeModeError {
/*  61 */     return this.typeTag.apply(Factory.makeSubAtomicType(this.typeTag.getTypeConst()));
/*     */   }
/*     */   
/*     */   public int formHashCode() {
/*  65 */     return 17 * this.typeTag.hashCode() + this.javaObject.hashCode();
/*     */   }
/*     */   
/*  68 */   public int hashCode() { return 17 * this.typeTag.hashCode() + this.javaObject.hashCode(); }
/*     */   
/*     */   public BindingMode getBindingMode(ModeCheckContext context)
/*     */   {
/*  72 */     return Factory.makeBound();
/*     */   }
/*     */   
/*     */ 
/*     */   public void makeAllBound(ModeCheckContext context) {}
/*     */   
/*     */   public boolean equals(Object x)
/*     */   {
/*  80 */     if (x.getClass().equals(getClass())) {
/*  81 */       RBRepAsJavaObjectCompoundTerm cx = (RBRepAsJavaObjectCompoundTerm)x;
/*  82 */       return (this.javaObject.equals(cx.javaObject)) && (this.typeTag.equals(cx.typeTag));
/*     */     }
/*  84 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object accept(TermVisitor v)
/*     */   {
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isOfType(TypeConstructor t) {
/*  94 */     return t.isSuperTypeOf(getTypeConstructor());
/*     */   }
/*     */   
/*     */   public Frame unify(RBTerm other, Frame f) {
/*  98 */     if (((other instanceof RBVariable)) || ((other instanceof RBGenericCompoundTerm)))
/*  99 */       return other.unify(this, f);
/* 100 */     if (equals(other)) {
/* 101 */       return f;
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public ConstructorType getConstructorType() {
/* 107 */     return this.typeTag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFirst()
/*     */   {
/* 114 */     if ((this.javaObject instanceof String)) {
/* 115 */       String str = (String)this.javaObject;
/* 116 */       int firstindexofhash = str.indexOf('#');
/* 117 */       if (firstindexofhash == -1) {
/* 118 */         return " ";
/*     */       }
/* 120 */       return str.substring(0, firstindexofhash).intern();
/*     */     }
/* 122 */     if ((this.javaObject instanceof Number))
/* 123 */       return ((Number)this.javaObject).toString();
/* 124 */     if ((this.javaObject instanceof TwoLevelKey)) {
/* 125 */       return ((TwoLevelKey)this.javaObject).getFirst();
/*     */     }
/* 127 */     throw new Error("This object does not support TwoLevelKey indexing: " + this.javaObject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSecond()
/*     */   {
/* 135 */     if ((this.javaObject instanceof String)) {
/* 136 */       String str = (String)this.javaObject;
/* 137 */       int firstindexofhash = str.indexOf('#');
/* 138 */       if (firstindexofhash == -1) {
/* 139 */         return this.typeTag.getFunctorId().toString() + str;
/*     */       }
/* 141 */       return this.typeTag.getFunctorId().toString() + str.substring(firstindexofhash).intern();
/*     */     }
/* 143 */     if ((this.javaObject instanceof Number))
/* 144 */       return ((Number)this.javaObject).toString();
/* 145 */     if ((this.javaObject instanceof TwoLevelKey)) {
/* 146 */       return ((TwoLevelKey)this.javaObject).getSecond();
/*     */     }
/* 148 */     throw new Error("This object does not support TwoLevelKey indexing: " + this.javaObject);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 153 */     if ((this.javaObject instanceof String)) {
/* 154 */       String javaString = (String)this.javaObject;
/* 155 */       return "\"" + javaString + "\"" + 
/* 156 */         "::" + this.typeTag.getFunctorId().getName();
/*     */     }
/*     */     
/* 159 */     return this.javaObject.toString() + "::" + this.typeTag.getFunctorId().getName();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 163 */     in.defaultReadObject();
/* 164 */     if ((this.javaObject instanceof String)) {
/* 165 */       this.javaObject = ((String)this.javaObject).intern();
/*     */     }
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 170 */     if ((this.javaObject instanceof Integer)) {
/* 171 */       return ((Integer)this.javaObject).intValue();
/*     */     }
/* 173 */     return super.intValue();
/*     */   }
/*     */   
/*     */   public Object getValue()
/*     */   {
/* 178 */     return this.javaObject;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBRepAsJavaObjectCompoundTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */