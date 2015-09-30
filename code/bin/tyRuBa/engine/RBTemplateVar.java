/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.engine.visitor.TermVisitor;
/*    */ import tyRuBa.modes.BindingMode;
/*    */ import tyRuBa.modes.Bound;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ import tyRuBa.modes.Type;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RBTemplateVar
/*    */   extends RBSubstitutable
/*    */ {
/*    */   public RBTemplateVar(String name)
/*    */   {
/* 32 */     super(name.intern());
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame unify(RBTerm other, Frame f)
/*    */   {
/* 38 */     throw new Error("Unsupported operation");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean freefor(RBVariable v)
/*    */   {
/* 47 */     throw new Error("Unsupported operation");
/*    */   }
/*    */   
/*    */   boolean isGround()
/*    */   {
/* 52 */     return true;
/*    */   }
/*    */   
/*    */   public BindingMode getBindingMode(ModeCheckContext context)
/*    */   {
/* 57 */     return Bound.the;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean sameForm(RBTerm other, Frame leftToRight, Frame rightToLeft)
/*    */   {
/* 66 */     throw new Error("Unsupported operation");
/*    */   }
/*    */   
/*    */ 
/*    */   public int formHashCode()
/*    */   {
/* 72 */     throw new Error("Unsupported operation");
/*    */   }
/*    */   
/*    */   protected Type getType(TypeEnv env) throws TypeModeError
/*    */   {
/* 77 */     return env.get(this);
/*    */   }
/*    */   
/*    */ 
/*    */   public void makeAllBound(ModeCheckContext context) {}
/*    */   
/*    */   public Object accept(TermVisitor v)
/*    */   {
/* 85 */     return v.visit(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getFirst()
/*    */   {
/* 92 */     throw new Error("Variables cannot be two level keys");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getSecond()
/*    */   {
/* 99 */     throw new Error("Variables cannot be two level keys");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBTemplateVar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */