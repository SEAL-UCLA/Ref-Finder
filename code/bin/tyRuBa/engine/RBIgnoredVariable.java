/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import tyRuBa.engine.visitor.TermVisitor;
/*    */ import tyRuBa.modes.Factory;
/*    */ import tyRuBa.modes.Type;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ 
/*    */ 
/*    */ public class RBIgnoredVariable
/*    */   extends RBVariable
/*    */ {
/* 13 */   public static final RBIgnoredVariable the = new RBIgnoredVariable();
/*    */   
/*    */   private RBIgnoredVariable() {
/* 16 */     super("?");
/*    */   }
/*    */   
/*    */   public Frame unify(RBTerm other, Frame f) {
/* 20 */     return f;
/*    */   }
/*    */   
/*    */   boolean freefor(RBVariable v) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   protected boolean sameForm(RBTerm other, Frame lr, Frame rl) {
/* 28 */     return this == other;
/*    */   }
/*    */   
/*    */   public int formHashCode() {
/* 32 */     return 1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 36 */     return obj == this;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 40 */     return 66727982;
/*    */   }
/*    */   
/*    */   public Object clone() {
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   protected Type getType(TypeEnv env) {
/* 48 */     return Factory.makeTVar("");
/*    */   }
/*    */   
/*    */   public Object accept(TermVisitor v) {
/* 52 */     return v.visit(this);
/*    */   }
/*    */   
/*    */   public Object readResolve() throws ObjectStreamException {
/* 56 */     return the;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBIgnoredVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */