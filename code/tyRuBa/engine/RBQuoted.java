/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.engine.visitor.TermVisitor;
/*    */ import tyRuBa.modes.Factory;
/*    */ import tyRuBa.modes.Type;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ public class RBQuoted extends RBAbstractPair
/*    */ {
/* 11 */   private static final RBTerm quotedName = FrontEnd.makeName("{}");
/*    */   
/*    */   public RBQuoted(RBTerm listOfParts) {
/* 14 */     super(quotedName, listOfParts);
/*    */   }
/*    */   
/*    */   public Object up() {
/* 18 */     return quotedToString();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 22 */     return "{" + getQuotedParts().quotedToString() + "}";
/*    */   }
/*    */   
/*    */   public String quotedToString() {
/* 26 */     return getQuotedParts().quotedToString();
/*    */   }
/*    */   
/*    */   public RBTerm getQuotedParts() {
/* 30 */     return getCdr();
/*    */   }
/*    */   
/*    */   protected Type getType(TypeEnv env) throws TypeModeError {
/* 34 */     return Factory.makeSubAtomicType(Factory.makeTypeConstructor(String.class));
/*    */   }
/*    */   
/*    */   public Object accept(TermVisitor v) {
/* 38 */     return v.visit(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getFirst()
/*    */   {
/* 45 */     return getCdr().getFirst();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getSecond()
/*    */   {
/* 52 */     return getCdr().getSecond();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBQuoted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */