/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindingList
/*     */ {
/*     */   private ArrayList parts;
/*     */   
/*     */   public BindingList()
/*     */   {
/*  19 */     this.parts = new ArrayList();
/*     */   }
/*     */   
/*     */   public BindingList(BindingMode bm) {
/*  23 */     this();
/*  24 */     this.parts.add(bm);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  28 */     int size = size();
/*  29 */     int hash = getClass().hashCode();
/*  30 */     for (int i = 0; i < size; i++)
/*  31 */       hash = hash * 19 + get(i).hashCode();
/*  32 */     return hash;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  36 */     if ((other instanceof BindingList)) {
/*  37 */       BindingList cother = (BindingList)other;
/*  38 */       if (size() != cother.size()) {
/*  39 */         return false;
/*     */       }
/*  41 */       for (int i = 0; i < size(); i++) {
/*  42 */         if (!get(i).equals(cother.get(i)))
/*  43 */           return false;
/*     */       }
/*  45 */       return true;
/*     */     }
/*     */     
/*  48 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  53 */     StringBuffer result = new StringBuffer("(");
/*  54 */     int size = size();
/*  55 */     for (int i = 0; i < size; i++) {
/*  56 */       if (i > 0) {
/*  57 */         result.append(",");
/*     */       }
/*  59 */       result.append(get(i).toString());
/*     */     }
/*  61 */     result.append(")");
/*  62 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String getBFString() {
/*  66 */     StringBuffer result = new StringBuffer();
/*  67 */     int size = size();
/*  68 */     for (int i = 0; i < size; i++) {
/*  69 */       if (get(i).isBound()) {
/*  70 */         result.append("B");
/*     */       } else {
/*  72 */         result.append("F");
/*     */       }
/*     */     }
/*  75 */     return result.toString();
/*     */   }
/*     */   
/*     */   public void add(BindingMode newPart)
/*     */   {
/*  80 */     this.parts.add(newPart);
/*     */   }
/*     */   
/*     */   public BindingMode get(int i) {
/*  84 */     return (BindingMode)this.parts.get(i);
/*     */   }
/*     */   
/*     */   public int size() {
/*  88 */     return this.parts.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean satisfyBinding(BindingList other)
/*     */   {
/*  94 */     for (int i = 0; i < size(); i++) {
/*  95 */       if (!get(i).satisfyBinding(other.get(i))) {
/*  96 */         return false;
/*     */       }
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasFree() {
/* 103 */     for (int i = 0; i < size(); i++)
/* 104 */       if (!get(i).isBound()) return true;
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAllBound() {
/* 109 */     for (int i = 0; i < size(); i++) {
/* 110 */       if (!get(i).isBound()) {
/* 111 */         return false;
/*     */       }
/*     */     }
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public int getNumBound() {
/* 118 */     int result = 0;
/* 119 */     for (int i = 0; i < size(); i++) {
/* 120 */       if (get(i).isBound()) {
/* 121 */         result++;
/*     */       }
/*     */     }
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   public int getNumFree() {
/* 128 */     return size() - getNumBound();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/BindingList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */