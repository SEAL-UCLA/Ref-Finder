/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import org.apache.regexp.RESyntaxException;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.JavaConstructorType;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.util.TwoLevelKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RBJavaObjectCompoundTerm
/*     */   extends RBCompoundTerm
/*     */ {
/*     */   public static RBTerm javaClass(String name)
/*     */   {
/*     */     try
/*     */     {
/*  27 */       return new RBJavaObjectCompoundTerm(Class.forName(name));
/*     */     } catch (ClassNotFoundException e) {
/*  29 */       throw new Error("Class not found:" + name);
/*     */     }
/*     */   }
/*     */   
/*     */   public static RBTerm regexp(String re)
/*     */   {
/*     */     try {
/*  36 */       return new RBJavaObjectCompoundTerm(new RBJavaObjectCompoundTerm.2(re, re));
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RESyntaxException e)
/*     */     {
/*     */ 
/*  43 */       throw new Error("Regular expression syntax error");
/*     */     }
/*     */   }
/*     */   
/*  47 */   public static final RBTerm theEmptyList = new RBJavaObjectCompoundTerm.1("[]");
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
/*     */   private Object arg;
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
/*     */   public RBJavaObjectCompoundTerm(Object arg)
/*     */   {
/*  77 */     if ((arg instanceof String)) {
/*  78 */       this.arg = ((String)arg).intern();
/*     */     } else {
/*  80 */       this.arg = arg;
/*     */     }
/*     */   }
/*     */   
/*     */   public ConstructorType getConstructorType() {
/*  85 */     return ConstructorType.makeJava(this.arg.getClass());
/*     */   }
/*     */   
/*     */   public RBTerm getArg() {
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public RBTerm getArg(int i) {
/*  93 */     if (i == 0) {
/*  94 */       return this;
/*     */     }
/*  96 */     throw new Error("RBJavaObjectCompoundTerms only have one argument");
/*     */   }
/*     */   
/*     */   public int getNumArgs() {
/* 100 */     return 1;
/*     */   }
/*     */   
/* 103 */   boolean freefor(RBVariable v) { return true; }
/*     */   
/*     */   public boolean isGround() {
/* 106 */     return true;
/*     */   }
/*     */   
/* 109 */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) { return equals(other); }
/*     */   
/*     */   protected Type getType(TypeEnv env) throws TypeModeError {
/* 112 */     return ((JavaConstructorType)getConstructorType()).getType();
/*     */   }
/*     */   
/*     */   public int formHashCode() {
/* 116 */     return 9595 + this.arg.hashCode();
/*     */   }
/*     */   
/* 119 */   public int hashCode() { return 9595 + this.arg.hashCode(); }
/*     */   
/*     */ 
/*     */ 
/* 123 */   public BindingMode getBindingMode(ModeCheckContext context) { return Factory.makeBound(); }
/*     */   
/*     */   public void makeAllBound(ModeCheckContext context) {}
/*     */   
/*     */   public boolean equals(Object x) {
/* 128 */     if (x.getClass().equals(getClass())) {
/* 129 */       return this.arg.equals(((RBJavaObjectCompoundTerm)x).arg);
/*     */     }
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public Object accept(TermVisitor v)
/*     */   {
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isOfType(TypeConstructor t) {
/* 140 */     return t.isSuperTypeOf(getTypeConstructor());
/*     */   }
/*     */   
/*     */   public Object up() {
/* 144 */     return this.arg;
/*     */   }
/*     */   
/*     */   public Frame unify(RBTerm other, Frame f) {
/* 148 */     if ((other instanceof RBVariable))
/* 149 */       return other.unify(this, f);
/* 150 */     if (equals(other)) {
/* 151 */       return f;
/*     */     }
/* 153 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFirst()
/*     */   {
/* 161 */     if ((this.arg instanceof String)) {
/* 162 */       String str = (String)this.arg;
/* 163 */       int firstindexofhash = str.indexOf('#');
/* 164 */       if (firstindexofhash == -1) {
/* 165 */         return " ";
/*     */       }
/* 167 */       return str.substring(0, firstindexofhash).intern();
/*     */     }
/* 169 */     if ((this.arg instanceof Number))
/* 170 */       return ((Number)this.arg).toString();
/* 171 */     if ((this.arg instanceof TwoLevelKey)) {
/* 172 */       return ((TwoLevelKey)this.arg).getFirst();
/*     */     }
/* 174 */     throw new Error("This object does not support TwoLevelKey indexing: " + this.arg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSecond()
/*     */   {
/* 182 */     if ((this.arg instanceof String)) {
/* 183 */       String str = (String)this.arg;
/* 184 */       int firstindexofhash = str.indexOf('#');
/* 185 */       if (firstindexofhash == -1) {
/* 186 */         return str;
/*     */       }
/* 188 */       return str.substring(firstindexofhash).intern();
/*     */     }
/* 190 */     if ((this.arg instanceof Number))
/* 191 */       return ((Number)this.arg).toString();
/* 192 */     if ((this.arg instanceof TwoLevelKey)) {
/* 193 */       return ((TwoLevelKey)this.arg).getSecond();
/*     */     }
/* 195 */     throw new Error("This object does not support TwoLevelKey indexing: " + this.arg);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 200 */     return this.arg.toString();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 204 */     in.defaultReadObject();
/* 205 */     if ((this.arg instanceof String)) {
/* 206 */       this.arg = ((String)this.arg).intern();
/*     */     }
/*     */   }
/*     */   
/*     */   public int intValue() {
/* 211 */     if ((this.arg instanceof Integer)) {
/* 212 */       return ((Integer)this.arg).intValue();
/*     */     }
/* 214 */     return super.intValue();
/*     */   }
/*     */   
/*     */   public Object getObject()
/*     */   {
/* 219 */     return this.arg;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBJavaObjectCompoundTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */