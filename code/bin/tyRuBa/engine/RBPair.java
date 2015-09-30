/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import tyRuBa.engine.visitor.TermVisitor;
/*    */ import tyRuBa.modes.Factory;
/*    */ import tyRuBa.modes.Type;
/*    */ import tyRuBa.modes.TypeEnv;
/*    */ import tyRuBa.modes.TypeModeError;
/*    */ import tyRuBa.util.ObjectTuple;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RBPair
/*    */   extends RBAbstractPair
/*    */ {
/*    */   public RBPair(RBTerm aCar)
/*    */   {
/* 19 */     super(aCar, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RBPair(RBTerm aCar, RBTerm aCdr)
/*    */   {
/* 26 */     super(aCar, aCdr);
/*    */   }
/*    */   
/*    */   public static RBTerm make(RBTerm[] terms) {
/* 30 */     RBTerm t = FrontEnd.theEmptyList;
/* 31 */     for (int i = terms.length - 1; i >= 0; i--) {
/* 32 */       t = new RBPair(terms[i], t);
/*    */     }
/* 34 */     return t;
/*    */   }
/*    */   
/*    */   public Object up()
/*    */   {
/*    */     try
/*    */     {
/* 41 */       int size = getNumSubterms();
/* 42 */       Object[] array = new Object[size];
/* 43 */       for (int i = 0; i < size; i++) {
/* 44 */         array[i] = getSubterm(i).up();
/*    */       }
/* 46 */       return array;
/*    */     } catch (ImproperListException e) {}
/* 48 */     return super.up();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 53 */     return "[" + cdrToString(true, this) + "]";
/*    */   }
/*    */   
/*    */   public String quotedToString() {
/* 57 */     return getCar().quotedToString() + getCdr().quotedToString();
/*    */   }
/*    */   
/*    */   protected Type getType(TypeEnv env) throws TypeModeError
/*    */   {
/*    */     try {
/* 63 */       car = getCar().getType(env);
/*    */     } catch (TypeModeError e) { Type car;
/* 65 */       throw new TypeModeError(e, getCar());
/*    */     }
/*    */     Type car;
/* 68 */     try { cdr = getCdr().getType(env);
/*    */     } catch (TypeModeError e) { Type cdr;
/* 70 */       throw new TypeModeError(e, getCdr());
/*    */     }
/*    */     try { Type cdr;
/* 73 */       result = Factory.makeListType(car).union(cdr);
/*    */     } catch (TypeModeError e) { Type result;
/* 75 */       throw new TypeModeError(e, this); }
/*    */     Type result;
/* 77 */     return result;
/*    */   }
/*    */   
/*    */   public Object accept(TermVisitor v) {
/* 81 */     return v.visit(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getFirst()
/*    */   {
/* 88 */     return getCar().getFirst();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getSecond()
/*    */   {
/* 95 */     Object[] result = new Object[2];
/* 96 */     result[0] = getCar().getSecond();
/* 97 */     result[1] = getCdr();
/* 98 */     return ObjectTuple.make(result);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/RBPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */