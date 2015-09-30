/*     */ package tyRuBa.engine;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.engine.compilation.SemiDetCompiled;
/*     */ import tyRuBa.modes.BindingList;
/*     */ import tyRuBa.modes.BindingMode;
/*     */ import tyRuBa.modes.Factory;
/*     */ import tyRuBa.modes.Mode;
/*     */ import tyRuBa.modes.ModeCheckContext;
/*     */ import tyRuBa.modes.Multiplicity;
/*     */ import tyRuBa.modes.PredInfoProvider;
/*     */ import tyRuBa.modes.PredicateMode;
/*     */ import tyRuBa.modes.TupleType;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.util.ElementSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Implementation
/*     */   extends RBComponent
/*     */ {
/*     */   private PredicateMode mode;
/*  27 */   private ArrayList solutions = null;
/*     */   private ArrayList arguments;
/*     */   private ArrayList results;
/*     */   private RBTuple argsAndResults;
/*     */   
/*     */   public abstract void doit(RBTerm[] paramArrayOfRBTerm);
/*     */   
/*     */   public Implementation(String paramModesString, String modeString)
/*     */   {
/*  36 */     this.mode = Factory.makePredicateMode(paramModesString, modeString);
/*  37 */     BindingList bindings = this.mode.getParamModes();
/*  38 */     ArrayList argsAndRes = new ArrayList();
/*  39 */     this.arguments = new ArrayList();
/*  40 */     this.results = new ArrayList();
/*  41 */     for (int i = 0; i < bindings.size(); i++) {
/*  42 */       RBTerm curr = FrontEnd.makeUniqueVar("arg" + i);
/*  43 */       argsAndRes.add(curr);
/*  44 */       if (bindings.get(i).isBound()) {
/*  45 */         this.arguments.add(curr);
/*     */       } else {
/*  47 */         this.results.add(curr);
/*     */       }
/*     */     }
/*  50 */     this.argsAndResults = RBTuple.make(argsAndRes);
/*     */   }
/*     */   
/*     */   public RBTerm getArgAt(int i) {
/*  54 */     return (RBTerm)this.arguments.get(i);
/*     */   }
/*     */   
/*     */   public int getNumArgs() {
/*  58 */     return this.arguments.size();
/*     */   }
/*     */   
/*     */   public RBTerm getResultAt(int i) {
/*  62 */     return (RBTerm)this.results.get(i);
/*     */   }
/*     */   
/*     */   public PredicateMode getPredicateMode() {
/*  66 */     return this.mode;
/*     */   }
/*     */   
/*     */   public Mode getMode() {
/*  70 */     return getPredicateMode().getMode();
/*     */   }
/*     */   
/*     */   public BindingList getBindingList() {
/*  74 */     return getPredicateMode().getParamModes();
/*     */   }
/*     */   
/*     */   public PredicateIdentifier getPredId() {
/*  78 */     throw new Error("This should not happen");
/*     */   }
/*     */   
/*     */   public RBTuple getArgs() {
/*  82 */     return this.argsAndResults;
/*     */   }
/*     */   
/*     */   public void addSolution() {
/*  86 */     this.solutions.add(new RBTerm[0]);
/*     */   }
/*     */   
/*     */   public void addSolution(Object o) {
/*  90 */     this.solutions.add(new RBTerm[] { RBCompoundTerm.makeJava(o) });
/*     */   }
/*     */   
/*     */   public void addSolution(Object o1, Object o2) {
/*  94 */     this.solutions.add(new RBTerm[] { RBCompoundTerm.makeJava(o1), RBCompoundTerm.makeJava(o2) });
/*     */   }
/*     */   
/*     */   public void addTermSolution(RBTerm t) {
/*  98 */     this.solutions.add(new RBTerm[] { t });
/*     */   }
/*     */   
/*     */   public void addTermSolution(RBTerm t1, RBTerm t2) {
/* 102 */     this.solutions.add(new RBTerm[] { t1, t2 });
/*     */   }
/*     */   
/*     */   public TupleType typecheck(PredInfoProvider predinfo) throws TypeModeError {
/* 106 */     throw new Error("This should not happen");
/*     */   }
/*     */   
/*     */   public RBComponent convertToMode(PredicateMode mode, ModeCheckContext context) throws TypeModeError
/*     */   {
/* 111 */     if (mode.equals(getPredicateMode())) {
/* 112 */       return this;
/*     */     }
/* 114 */     throw new Error("This should not happen");
/*     */   }
/*     */   
/*     */   public ArrayList eval(RBContext rb, Frame f, Frame callFrame)
/*     */   {
/* 119 */     this.solutions = new ArrayList();
/* 120 */     RBTerm[] args = new RBTerm[getNumArgs()];
/* 121 */     for (int i = 0; i < getNumArgs(); i++) {
/* 122 */       args[i] = getArgAt(i).substitute(f);
/*     */     }
/* 124 */     doit(args);
/* 125 */     ArrayList results = new ArrayList();
/* 126 */     for (int i = 0; i < this.solutions.size(); i++) {
/* 127 */       Frame result = (Frame)f.clone();
/* 128 */       RBTerm[] sols = (RBTerm[])this.solutions.get(i);
/* 129 */       for (int j = 0; j < sols.length; j++) {
/* 130 */         result = getResultAt(j).substitute(result).unify(sols[j], result);
/* 131 */         if (result == null) {
/* 132 */           j = sols.length;
/*     */         }
/*     */       }
/* 135 */       if (result != null) {
/* 136 */         results.add(callFrame.callResult(result));
/*     */       }
/*     */     }
/* 139 */     return results;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 143 */     return "Implementation in mode: " + this.mode;
/*     */   }
/*     */   
/*     */   public Compiled compile(CompilationContext c) {
/* 147 */     if (getMode().hi.compareTo(Multiplicity.one) <= 0) {
/* 148 */       new SemiDetCompiled() {
/*     */         public Frame runSemiDet(Object input, RBContext context) {
/* 150 */           Frame callFrame = new Frame();
/* 151 */           RBTuple goal = 
/* 152 */             (RBTuple)((RBTuple)input).instantiate(callFrame);
/* 153 */           Frame fc = goal.unify(Implementation.this.getArgs(), new Frame());
/* 154 */           ArrayList results = Implementation.this.eval(context, fc, callFrame);
/* 155 */           if (results.size() == 0) {
/* 156 */             return null;
/*     */           }
/* 158 */           return (Frame)results.get(0);
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 163 */     new Compiled(getMode()) {
/*     */       public ElementSource runNonDet(Object input, RBContext context) {
/* 165 */         Frame callFrame = new Frame();
/* 166 */         RBTuple goal = 
/* 167 */           (RBTuple)((RBTuple)input).instantiate(callFrame);
/* 168 */         Frame fc = goal.unify(Implementation.this.getArgs(), new Frame());
/* 169 */         ArrayList results = Implementation.this.eval(context, fc, callFrame);
/* 170 */         if (results == null) {
/* 171 */           return ElementSource.theEmpty;
/*     */         }
/* 173 */         return ElementSource.with(results);
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/Implementation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */