/*    */ package tyRuBa.modes;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashMap;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import tyRuBa.engine.RBSubstitutable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TypeEnv
/*    */   extends Hashtable
/*    */ {
/*    */   public Type basicGet(RBSubstitutable v)
/*    */   {
/* 18 */     return (Type)super.get(v);
/*    */   }
/*    */   
/*    */   public Type get(RBSubstitutable v) {
/* 22 */     Type result = (Type)super.get(v);
/* 23 */     if (result == null) {
/* 24 */       result = Factory.makeTVar(v.name().substring(1));
/* 25 */       put(v, result);
/*    */     }
/* 27 */     return result;
/*    */   }
/*    */   
/*    */   public Object clone() {
/* 31 */     TypeEnv cl = new TypeEnv();
/* 32 */     HashMap varRenamings = new HashMap();
/* 33 */     for (Iterator iter = keySet().iterator(); iter.hasNext();) {
/* 34 */       RBSubstitutable element = (RBSubstitutable)iter.next();
/* 35 */       cl.put(element, get(element).clone(varRenamings));
/*    */     }
/* 37 */     return cl;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 41 */     StringBuffer result = new StringBuffer("TypeEnv(");
/* 42 */     Enumeration keys = keys();
/* 43 */     while (keys.hasMoreElements()) {
/* 44 */       RBSubstitutable key = (RBSubstitutable)keys.nextElement();
/* 45 */       result.append(" " + key + "= " + get(key));
/*    */     }
/* 47 */     result.append(" )");
/* 48 */     return result.toString();
/*    */   }
/*    */   
/*    */   public TypeEnv union(TypeEnv other) throws TypeModeError {
/* 52 */     TypeEnv result = new TypeEnv();
/* 53 */     for (Iterator iter = other.keySet().iterator(); iter.hasNext();) {
/* 54 */       RBSubstitutable var = (RBSubstitutable)iter.next();
/* 55 */       if (containsKey(var))
/* 56 */         result.put(var, get(var).union(other.get(var)));
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   public TypeEnv intersect(TypeEnv other) throws TypeModeError {
/* 62 */     TypeEnv result = (TypeEnv)clone();
/* 63 */     other = (TypeEnv)other.clone();
/* 64 */     for (Iterator iter = other.keySet().iterator(); iter.hasNext();) {
/* 65 */       RBSubstitutable var = (RBSubstitutable)iter.next();
/* 66 */       if (result.containsKey(var)) {
/* 67 */         result.put(var, result.get(var).intersect(other.get(var)));
/*    */       } else
/* 69 */         result.put(var, other.get(var));
/*    */     }
/* 71 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/TypeEnv.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */