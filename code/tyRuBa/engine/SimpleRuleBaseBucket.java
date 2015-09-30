/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.parser.ParseException;
/*    */ 
/*    */ public class SimpleRuleBaseBucket extends RuleBaseBucket
/*    */ {
/*  8 */   StringBuffer mystuff = null;
/*    */   
/*    */   public SimpleRuleBaseBucket(FrontEnd frontEnd) {
/* 11 */     super(frontEnd, null);
/*    */   }
/*    */   
/*    */   public synchronized void addStuff(String toParse) {
/* 15 */     if (this.mystuff == null)
/* 16 */       this.mystuff = new StringBuffer();
/* 17 */     this.mystuff.append(toParse + "\n");
/* 18 */     setOutdated();
/*    */   }
/*    */   
/*    */   public synchronized void clearStuff() {
/* 22 */     this.mystuff = null;
/* 23 */     setOutdated();
/*    */   }
/*    */   
/*    */   public synchronized void update() throws ParseException, TypeModeError {
/* 27 */     if (this.mystuff != null) {
/* 28 */       parse(this.mystuff.toString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/SimpleRuleBaseBucket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */