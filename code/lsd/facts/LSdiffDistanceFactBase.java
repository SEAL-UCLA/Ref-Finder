/*     */ package lsd.facts;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import lsd.io.LSDTyrubaFactReader;
/*     */ import lsd.rule.LSDBinding;
/*     */ import lsd.rule.LSDFact;
/*     */ import lsd.rule.LSDPredicate;
/*     */ 
/*     */ public class LSdiffDistanceFactBase
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  18 */     String[] v = { "0.9.9_0.9.10", "0.9.10_0.9.11" };
/*  19 */     String[] arrayOfString1; int j = (arrayOfString1 = v).length; for (int i = 0; i < j; i++) { String v_i = arrayOfString1[i];
/*     */       
/*  21 */       java.io.File twoKBFile = new java.io.File(
/*  22 */         "input/jfreechart/" + v_i + "2KB.rub");
/*  23 */       ArrayList<LSDFact> twoKB = new LSDTyrubaFactReader(twoKBFile)
/*  24 */         .getFacts();
/*  25 */       java.io.File deltaKBFile = new java.io.File(
/*  26 */         "input/jfreechart/" + v_i + "delta.rub");
/*  27 */       ArrayList<LSDFact> deltaKB = new LSDTyrubaFactReader(deltaKBFile)
/*  28 */         .getFacts();
/*     */       
/*  30 */       System.out.println("Original 2KB Size:\t" + twoKB.size());
/*  31 */       System.out.println("Original Delta KB Size:\t" + deltaKB.size());
/*     */       
/*  33 */       LSdiffDistanceFactBase filter2KB = new LSdiffDistanceFactBase(twoKB, 
/*  34 */         deltaKB);
/*  35 */       filter2KB.expand(1);
/*  36 */       System.out.println("Working Set Binding Size:" + filter2KB.workingSetBinding.size());
/*  37 */       System.out.println("Working 2KB Size:" + filter2KB.working2KB.size());
/*     */       
/*  39 */       System.out.println("\n");
/*     */     }
/*     */   }
/*     */   
/*  43 */   private TreeSet<LSDFact> working2KB = new TreeSet();
/*     */   
/*  45 */   private TreeSet<LSDBinding> workingSetBinding = new TreeSet();
/*     */   
/*     */   private final ArrayList<LSDFact> original2KB;
/*     */   
/*     */   private final ArrayList<LSDFact> originalDeltaKB;
/*     */   
/*     */   private final LSdiffHierarchialDeltaKB hdelta;
/*     */   
/*     */   public LSdiffDistanceFactBase(ArrayList<LSDFact> twoKB, ArrayList<LSDFact> deltaKB)
/*     */   {
/*  55 */     this.original2KB = twoKB;
/*  56 */     this.originalDeltaKB = deltaKB;
/*  57 */     this.hdelta = new LSdiffHierarchialDeltaKB(deltaKB);
/*     */   }
/*     */   
/*     */   public void expand(int depth) {
/*  61 */     initializedFromDirtyCodeElements();
/*  62 */     for (int i = 1; i <= depth; i++) {
/*  63 */       System.out.println("Iteration " + i);
/*  64 */       System.out.println("Working Set Binding Size:\t" + this.workingSetBinding.size());
/*     */       
/*  66 */       boolean stop = expandOneHopViaDependencies();
/*  67 */       System.out.println("Working 2KB Size:\t" + this.working2KB.size());
/*     */       
/*  69 */       if (stop) break;
/*     */     }
/*     */   }
/*     */   
/*  73 */   public ArrayList<LSDFact> getWorking2KBFacts() { ArrayList<LSDFact> facts = new ArrayList(this.working2KB);
/*  74 */     return facts;
/*     */   }
/*     */   
/*     */   private void printWorkingSetBinding(PrintStream p) {
/*  78 */     for (LSDBinding b : this.workingSetBinding) {
/*  79 */       char c = b.getType();
/*  80 */       p.println(c + "\t:\t" + b.getGroundConst());
/*     */     }
/*     */   }
/*     */   
/*     */   private void printWorking2KBFact(PrintStream p)
/*     */   {
/*  86 */     for (LSDFact f : this.working2KB) {
/*  87 */       p.println(f);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void initializeFromDeltaKB()
/*     */   {
/*  94 */     for (LSDFact fact : this.originalDeltaKB) {
/*  95 */       addBindingsFromFact(this.workingSetBinding, fact);
/*     */     }
/*  97 */     System.out.println("Initial Working Set Binding Size:\t" + this.workingSetBinding.size());
/*     */   }
/*     */   
/*     */   private void initializedFromDirtyCodeElements() { Iterator localIterator2;
/* 101 */     for (Iterator localIterator1 = this.hdelta.packageLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 102 */         localIterator2.hasNext())
/*     */     {
/* 101 */       String kind = (String)localIterator1.next();
/* 102 */       localIterator2 = ((TreeSet)this.hdelta.packageLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)localIterator2.next();
/* 103 */       addBindingsFromFact(this.workingSetBinding, fact);
/*     */     }
/*     */     
/* 106 */     System.out.println("Initial Working Set Binding Size:\t" + 
/* 107 */       this.workingSetBinding.size());
/* 108 */     for (localIterator1 = this.hdelta.typeLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 109 */         localIterator2.hasNext())
/*     */     {
/* 108 */       String kind = (String)localIterator1.next();
/* 109 */       localIterator2 = ((TreeSet)this.hdelta.typeLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)localIterator2.next();
/* 110 */       addBindingsFromFact(this.workingSetBinding, fact);
/*     */     }
/*     */     
/* 113 */     System.out.println("After Type Level: Working Set Binding Size:\t" + 
/* 114 */       this.workingSetBinding.size());
/*     */     
/* 116 */     for (localIterator1 = this.hdelta.methodLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 117 */         localIterator2.hasNext())
/*     */     {
/* 116 */       String kind = (String)localIterator1.next();
/* 117 */       localIterator2 = ((TreeSet)this.hdelta.methodLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)localIterator2.next();
/* 118 */       addBindingsFromFact(this.workingSetBinding, fact);
/*     */     }
/*     */     
/* 121 */     System.out.println("After Method Level: Working Set Binding Size:\t" + 
/* 122 */       this.workingSetBinding.size());
/* 123 */     for (localIterator1 = this.hdelta.fieldLevel.keySet().iterator(); localIterator1.hasNext(); 
/* 124 */         localIterator2.hasNext())
/*     */     {
/* 123 */       String kind = (String)localIterator1.next();
/* 124 */       localIterator2 = ((TreeSet)this.hdelta.fieldLevel.get(kind)).iterator(); continue;LSDFact fact = (LSDFact)localIterator2.next();
/* 125 */       addBindingsFromFact(this.workingSetBinding, fact);
/*     */     }
/*     */     
/* 128 */     System.out.println("After Field Level: Working Set Binding Size:\t" + 
/* 129 */       this.workingSetBinding.size());
/*     */   }
/*     */   
/*     */   private void addBindingsFromFact(TreeSet<LSDBinding> storage, LSDFact fact) {
/* 133 */     List<LSDBinding> bindings = fact.getBindings();
/* 134 */     char[] types = fact.getPredicate().getTypes();
/* 135 */     for (int i = 0; i < types.length; i++) {
/* 136 */       LSDBinding b = (LSDBinding)bindings.get(i);
/* 137 */       b.setType(types[i]);
/* 138 */       storage.add(b);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean expandOneHopViaDependencies()
/*     */   {
/* 145 */     TreeSet<LSDBinding> temp = new TreeSet();
/* 146 */     for (LSDFact twoKBfact : this.original2KB) {
/* 147 */       LSDPredicate tk_pred = twoKBfact.getPredicate();
/* 148 */       String tk_predName = tk_pred.getName();
/* 149 */       if ((tk_predName.endsWith("_accesses")) || 
/* 150 */         (tk_predName.endsWith("_calls")) || 
/* 151 */         (tk_predName.endsWith("_implements")) || 
/* 152 */         (tk_predName.endsWith("_extends"))) {
/* 153 */         char[] types = tk_pred.getTypes();
/* 154 */         List<LSDBinding> bindings = twoKBfact.getBindings();
/* 155 */         for (int i = 0; i < types.length; i++)
/*     */         {
/* 157 */           LSDBinding tk_binding = (LSDBinding)bindings.get(i);
/* 158 */           tk_binding.setType(types[i]);
/*     */           
/*     */ 
/*     */ 
/* 162 */           if (this.workingSetBinding.contains(tk_binding))
/*     */           {
/*     */ 
/* 165 */             this.working2KB.add(twoKBfact);
/* 166 */             addBindingsFromFact(temp, twoKBfact);
/*     */           }
/*     */         }
/*     */       }
/* 170 */       else if (tk_predName.endsWith("_inheritedfield"))
/*     */       {
/*     */ 
/* 173 */         List<LSDBinding> bindings = twoKBfact.getBindings();
/*     */         
/* 175 */         LSDBinding tk_binding_A = (LSDBinding)bindings.get(0);
/* 176 */         LSDBinding tk_binding_B = (LSDBinding)bindings.get(2);
/* 177 */         String fullName = lsd.rule.LSDConst.createFullMethodOrFieldName(
/* 178 */           tk_binding_A.getGroundConst(), tk_binding_B
/* 179 */           .getGroundConst());
/* 180 */         LSDBinding tk_binding = 
/* 181 */           (LSDBinding)lsd.rule.LSDConst.createModifiedField(fullName).getBindings().get(0);
/* 182 */         char[] types = tk_pred.getTypes();
/* 183 */         tk_binding.setType(types[0]);
/*     */         
/*     */ 
/* 186 */         if (this.workingSetBinding.contains(tk_binding))
/*     */         {
/*     */ 
/* 189 */           this.working2KB.add(twoKBfact);
/* 190 */           addBindingsFromFact(temp, twoKBfact);
/*     */         }
/*     */       }
/* 193 */       else if (tk_predName.endsWith("_inheritedmethod"))
/*     */       {
/*     */ 
/* 196 */         List<LSDBinding> bindings = twoKBfact.getBindings();
/* 197 */         LSDBinding tk_binding_A = (LSDBinding)bindings.get(0);
/* 198 */         LSDBinding tk_binding_B = (LSDBinding)bindings.get(2);
/* 199 */         String fullName = lsd.rule.LSDConst.createFullMethodOrFieldName(
/* 200 */           tk_binding_A.getGroundConst(), tk_binding_B
/* 201 */           .getGroundConst());
/* 202 */         LSDBinding tk_binding = 
/* 203 */           (LSDBinding)lsd.rule.LSDConst.createModifiedMethod(fullName).getBindings().get(0);
/* 204 */         char[] types = tk_pred.getTypes();
/* 205 */         tk_binding.setType(types[0]);
/*     */         
/*     */ 
/* 208 */         if (this.workingSetBinding.contains(tk_binding))
/*     */         {
/*     */ 
/* 211 */           this.working2KB.add(twoKBfact);
/* 212 */           addBindingsFromFact(temp, twoKBfact);
/*     */         }
/*     */       }
/* 215 */       else if ((tk_predName.endsWith("_typeintype")) || 
/* 216 */         (tk_predName.endsWith("_fieldoftype")) || 
/* 217 */         (tk_predName.endsWith("_return")))
/*     */       {
/* 219 */         List<LSDBinding> bindings = twoKBfact.getBindings();
/* 220 */         LSDBinding tk_binding_A = (LSDBinding)bindings.get(0);
/* 221 */         char[] types = tk_pred.getTypes();
/* 222 */         tk_binding_A.setType(types[0]);
/*     */         
/* 224 */         if (this.workingSetBinding.contains(tk_binding_A))
/*     */         {
/*     */ 
/* 227 */           this.working2KB.add(twoKBfact);
/* 228 */           addBindingsFromFact(temp, twoKBfact);
/*     */         }
/*     */       }
/* 231 */       else if ((!tk_predName.endsWith("_package")) && 
/* 232 */         (!tk_predName.endsWith("_type")) && 
/* 233 */         (!tk_predName.endsWith("_method"))) {
/* 234 */         tk_predName.endsWith("_field");
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 253 */     System.err.println("temp Size: " + temp.size());
/* 254 */     this.workingSetBinding.addAll(temp);
/* 255 */     System.err.println("workingSetBinding: " + this.workingSetBinding.size());
/* 256 */     if (temp.size() == 0)
/* 257 */       return true;
/* 258 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/facts/LSdiffDistanceFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */