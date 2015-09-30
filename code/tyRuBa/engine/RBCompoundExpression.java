/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Vector;
/*    */ import tyRuBa.util.ElementSource;
/*    */ 
/*    */ public abstract class RBCompoundExpression
/*    */   extends RBExpression
/*    */ {
/*    */   private ArrayList subexps;
/*    */   
/*    */   public RBCompoundExpression(Vector exps)
/*    */   {
/* 14 */     this.subexps = new ArrayList();
/* 15 */     for (int i = 0; i < exps.size(); i++) {
/* 16 */       addSubexp((RBExpression)exps.elementAt(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public RBCompoundExpression(Object[] exps)
/*    */   {
/* 22 */     this.subexps = new ArrayList();
/* 23 */     for (int i = 0; i < exps.length; i++) {
/* 24 */       addSubexp((RBExpression)exps[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public RBCompoundExpression()
/*    */   {
/* 30 */     this.subexps = new ArrayList();
/*    */   }
/*    */   
/*    */   public RBCompoundExpression(RBExpression e1, RBExpression e2) {
/* 34 */     this();
/* 35 */     addSubexp(e1);
/* 36 */     addSubexp(e2);
/*    */   }
/*    */   
/*    */   public int getNumSubexps() {
/* 40 */     return this.subexps.size();
/*    */   }
/*    */   
/*    */   public RBExpression getSubexp(int i) {
/* 44 */     return (RBExpression)this.subexps.get(i);
/*    */   }
/*    */   
/*    */   public ElementSource getSubexps() {
/* 48 */     return ElementSource.with(this.subexps);
/*    */   }
/*    */   
/*    */   public ArrayList getSubexpsArrayList() {
/* 52 */     return (ArrayList)this.subexps.clone();
/*    */   }
/*    */   
/*    */   public void addSubexp(RBExpression e) {
/* 56 */     if (getClass().isInstance(e)) {
/* 57 */       RBCompoundExpression ce = (RBCompoundExpression)e;
/* 58 */       for (int i = 0; i < ce.getNumSubexps(); i++) {
/* 59 */         addSubexp(ce.getSubexp(i));
/*    */       }
/*    */     } else {
/* 62 */       this.subexps.add(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public Object clone()
/*    */   {
/*    */     try
/*    */     {
/* 70 */       cl = (RBCompoundExpression)super.clone();
/*    */     } catch (CloneNotSupportedException localCloneNotSupportedException) { RBCompoundExpression cl;
/* 72 */       throw new Error("This should not happen"); }
/*    */     RBCompoundExpression cl;
/* 74 */     cl.subexps = ((ArrayList)this.subexps.clone());
/* 75 */     return cl;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 79 */     return toString(false);
/*    */   }
/*    */   
/*    */   public String toString(boolean brackets) {
/* 83 */     StringBuffer result = new StringBuffer();
/* 84 */     for (int i = 0; i < getNumSubexps(); i++) {
/* 85 */       RBExpression currExp = getSubexp(i);
/* 86 */       if (i > 0) {
/* 87 */         result.append(separator() + " ");
/*    */       }
/* 89 */       if ((currExp instanceof RBCompoundExpression)) {
/* 90 */         result.append(((RBCompoundExpression)currExp).toString(true));
/*    */       } else {
/* 92 */         result.append(getSubexp(i).toString());
/*    */       }
/*    */     }
/* 95 */     if (brackets) {
/* 96 */       return "(" + result.toString() + ")";
/*    */     }
/* 98 */     return result.toString();
/*    */   }
/*    */   
/*    */   protected abstract String separator();
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBCompoundExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */