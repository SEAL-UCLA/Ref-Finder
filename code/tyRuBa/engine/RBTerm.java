/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import tyRuBa.engine.visitor.CollectVarsVisitor;
/*     */ import tyRuBa.engine.visitor.InstantiateVisitor;
/*     */ import tyRuBa.engine.visitor.SubstantiateVisitor;
/*     */ import tyRuBa.engine.visitor.SubstituteVisitor;
/*     */ import tyRuBa.engine.visitor.TermVisitor;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Type;
/*     */ import tyRuBa.modes.TypeConstructor;
/*     */ import tyRuBa.modes.TypeEnv;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.util.TwoLevelKey;
/*     */ 
/*     */ 
/*     */ public abstract class RBTerm
/*     */   implements Cloneable, Serializable, TwoLevelKey
/*     */ {
/*     */   public Object up()
/*     */   {
/*  26 */     return new UppedTerm(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract Frame unify(RBTerm paramRBTerm, Frame paramFrame);
/*     */   
/*     */ 
/*     */   abstract boolean freefor(RBVariable paramRBVariable);
/*     */   
/*     */ 
/*     */   abstract boolean isGround();
/*     */   
/*     */ 
/*     */   public abstract BindingMode getBindingMode(ModeCheckContext paramModeCheckContext);
/*     */   
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */   
/*     */ 
/*     */   public abstract int hashCode();
/*     */   
/*     */ 
/*     */   protected abstract boolean sameForm(RBTerm paramRBTerm, Frame paramFrame1, Frame paramFrame2);
/*     */   
/*     */   public final boolean sameForm(RBTerm other)
/*     */   {
/*  52 */     return sameForm(other, new Frame(), new Frame());
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int formHashCode();
/*     */   
/*     */   public String quotedToString()
/*     */   {
/*  60 */     return toString();
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     try {
/*  65 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  67 */       throw new Error("This should not happen");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int intValue()
/*     */   {
/*  74 */     throw new Error("This is not an integer");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Collection getVariables()
/*     */   {
/*  81 */     CollectVarsVisitor visitor = new CollectVarsVisitor();
/*  82 */     accept(visitor);
/*  83 */     Collection vars = visitor.getVars();
/*  84 */     vars.remove(RBIgnoredVariable.the);
/*  85 */     return vars;
/*     */   }
/*     */   
/*     */   protected abstract Type getType(TypeEnv paramTypeEnv) throws TypeModeError;
/*     */   
/*     */   public String functorTypeConstructor() throws TypeModeError
/*     */   {
/*  92 */     throw new TypeModeError(toString() + " cannot be used as a functor");
/*     */   }
/*     */   
/*     */   public final RBVariable[] varMap() {
/*  96 */     ArrayList varlist = new ArrayList();
/*  97 */     CollectVarsVisitor visitor = new CollectVarsVisitor(varlist);
/*  98 */     accept(visitor);
/*  99 */     return (RBVariable[])varlist.toArray(new RBVariable[varlist.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void makeAllBound(ModeCheckContext paramModeCheckContext);
/*     */   
/*     */ 
/*     */   public RBTerm addTypeCast(TypeConstructor typeCast)
/*     */     throws TypeModeError
/*     */   {
/* 109 */     ConstructorType constructorType = typeCast.getConstructorType();
/* 110 */     if (constructorType == null)
/* 111 */       throw new TypeModeError("Illegal cast: " + typeCast + " is not a concrete type");
/* 112 */     return constructorType.apply(this);
/*     */   }
/*     */   
/*     */   public abstract Object accept(TermVisitor paramTermVisitor);
/*     */   
/*     */   public RBTerm substitute(Frame frame) {
/* 118 */     SubstituteVisitor visitor = new SubstituteVisitor(frame);
/* 119 */     return (RBTerm)accept(visitor);
/*     */   }
/*     */   
/*     */   public RBTerm instantiate(Frame frame) {
/* 123 */     InstantiateVisitor visitor = new InstantiateVisitor(frame);
/* 124 */     return (RBTerm)accept(visitor);
/*     */   }
/*     */   
/*     */   public RBTerm substantiate(Frame subst, Frame inst) {
/* 128 */     SubstantiateVisitor visitor = new SubstantiateVisitor(subst, inst);
/* 129 */     return (RBTerm)accept(visitor);
/*     */   }
/*     */   
/*     */   public abstract String toString();
/*     */   
/*     */   public boolean isOfType(TypeConstructor t) {
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */