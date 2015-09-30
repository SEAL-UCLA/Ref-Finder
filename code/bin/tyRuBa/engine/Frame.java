/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Frame
/*     */   extends Hashtable
/*     */ {
/*     */   public RBTerm get(RBSubstitutable v)
/*     */   {
/*  22 */     return (RBTerm)super.get(v);
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  26 */     Frame cl = (Frame)super.clone();
/*     */     
/*  28 */     return cl;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  32 */     StringBuffer result = new StringBuffer("|");
/*  33 */     Enumeration keys = keys();
/*  34 */     if (!keys.hasMoreElements()) {
/*  35 */       result.append(" SUCCESS");
/*     */     } else {
/*  37 */       while (keys.hasMoreElements()) {
/*  38 */         result.append(" ");
/*  39 */         RBSubstitutable key = (RBSubstitutable)keys.nextElement();
/*  40 */         result.append(key + "=" + get(key));
/*     */       }
/*     */     }
/*  43 */     result.append(" |");
/*  44 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public Frame callResult(Frame body)
/*     */   {
/*  50 */     Frame result = new Frame();
/*  51 */     Enumeration keys = keys();
/*  52 */     Frame instAux = new Frame();
/*  53 */     while (keys.hasMoreElements()) {
/*  54 */       RBSubstitutable key = (RBSubstitutable)keys.nextElement();
/*  55 */       RBTerm value = get(key);
/*  56 */       result.put(key, value.substantiate(body, instAux));
/*     */     }
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   public Frame append(Frame other)
/*     */   {
/*  63 */     Frame f = (Frame)clone();
/*  64 */     Enumeration others = other.keys();
/*  65 */     while (others.hasMoreElements()) {
/*  66 */       Object key = others.nextElement();
/*  67 */       Object val = other.get(key);
/*  68 */       f.put(key, val);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */     return f;
/*     */   }
/*     */   
/*     */   public boolean equals(Object x) {
/*  81 */     if (x.getClass() != getClass())
/*  82 */       return false;
/*  83 */     boolean equal = true;
/*  84 */     Frame other = (Frame)x;
/*  85 */     Frame l = new Frame();
/*  86 */     Frame r = new Frame();
/*  87 */     Enumeration keys = keys();
/*  88 */     while ((equal) && (keys.hasMoreElements())) {
/*  89 */       RBSubstitutable key = (RBSubstitutable)keys.nextElement();
/*  90 */       RBTerm value = get(key);
/*  91 */       equal = value.sameForm(other.get(key), l, r);
/*     */     }
/*  93 */     return equal;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  97 */     int hash = 0;
/*  98 */     Enumeration keys = keys();
/*  99 */     while (keys.hasMoreElements()) {
/* 100 */       RBSubstitutable key = (RBSubstitutable)keys.nextElement();
/* 101 */       RBTerm value = get(key);
/* 102 */       hash += key.hashCode() * value.formHashCode();
/*     */     }
/* 104 */     return hash;
/*     */   }
/*     */   
/*     */   public Frame removeVars(RBSubstitutable[] vars) {
/* 108 */     Frame result = (Frame)clone();
/* 109 */     for (int i = 0; i < vars.length; i++) {
/* 110 */       result.remove(vars[i]);
/*     */     }
/* 112 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/Frame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */