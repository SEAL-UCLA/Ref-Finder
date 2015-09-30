/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ import tyRuBa.modes.Mode;
/*    */ import tyRuBa.modes.ModeCheckContext;
/*    */ import tyRuBa.modes.PredInfoProvider;
/*    */ import tyRuBa.modes.PredicateMode;
/*    */ import tyRuBa.modes.TupleType;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidatorComponent
/*    */   extends RBComponent
/*    */ {
/*    */   private Validator validator;
/*    */   private RBComponent comp;
/*    */   
/*    */   public ValidatorComponent(RBComponent c, Validator validator)
/*    */   {
/* 27 */     this.validator = validator;
/* 28 */     this.comp = c;
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 32 */     return (this.validator != null) && (this.validator.isValid());
/*    */   }
/*    */   
/*    */   void checkValid() {
/* 36 */     if (!isValid()) {
/* 37 */       throw new Error("Internal Error: Using an invalidated component: " + 
/* 38 */         this);
/*    */     }
/*    */   }
/*    */   
/*    */   public Validator getValidator() {
/* 43 */     return this.validator;
/*    */   }
/*    */   
/*    */   public RBTuple getArgs() {
/* 47 */     checkValid();
/* 48 */     return this.comp.getArgs();
/*    */   }
/*    */   
/*    */   public PredicateIdentifier getPredId() {
/* 52 */     checkValid();
/* 53 */     return this.comp.getPredId();
/*    */   }
/*    */   
/*    */   public TupleType typecheck(PredInfoProvider predinfos) throws TypeModeError {
/* 57 */     checkValid();
/* 58 */     return this.comp.typecheck(predinfos);
/*    */   }
/*    */   
/*    */   public RBComponent convertToNormalForm() {
/* 62 */     checkValid();
/* 63 */     return new ValidatorComponent(this.comp.convertToNormalForm(), this.validator);
/*    */   }
/*    */   
/*    */   public boolean isGroundFact() {
/* 67 */     checkValid();
/* 68 */     return this.comp.isGroundFact();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 72 */     if (isValid()) {
/* 73 */       return this.comp.toString();
/*    */     }
/* 75 */     return "ValidatorComponent(INVALIDATED," + this.comp + ")";
/*    */   }
/*    */   
/*    */   public RBComponent convertToMode(PredicateMode mode, ModeCheckContext context) throws TypeModeError {
/* 79 */     checkValid();
/* 80 */     RBComponent converted = this.comp.convertToMode(mode, context);
/* 81 */     return new ValidatorComponent(converted, this.validator);
/*    */   }
/*    */   
/*    */   public Mode getMode() {
/* 85 */     checkValid();
/* 86 */     return this.comp.getMode();
/*    */   }
/*    */   
/*    */   public Compiled compile(CompilationContext c) {
/* 90 */     checkValid();
/* 91 */     return this.comp.compile(c);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/ValidatorComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */