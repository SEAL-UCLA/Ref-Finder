/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import tyRuBa.engine.RBComponent;
/*    */ import tyRuBa.engine.RBExpression;
/*    */ import tyRuBa.engine.RBTerm;
/*    */ 
/*    */ 
/*    */ public class TypeModeError
/*    */   extends Exception
/*    */ {
/*    */   public TypeModeError(String arg0)
/*    */   {
/* 13 */     super(arg0);
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, String str) {
/* 17 */     this(e.getMessage() + "\nin " + str);
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, RBComponent r) {
/* 21 */     this(e, r.toString());
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, RBTerm t) {
/* 25 */     this(e, t.toString());
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, RBExpression exp) {
/* 29 */     this(e, exp.toString());
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, Type t) {
/* 33 */     this(e, "Type: " + t.toString());
/*    */   }
/*    */   
/*    */   public TypeModeError(TypeModeError e, TupleType types) {
/* 37 */     this(e, types.toString());
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/TypeModeError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */