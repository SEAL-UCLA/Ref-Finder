/*     */ package tyRuBa.engine.factbase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import tyRuBa.engine.FunctorIdentifier;
/*     */ import tyRuBa.engine.PredicateIdentifier;
/*     */ import tyRuBa.engine.RBComponent;
/*     */ import tyRuBa.engine.RBCompoundTerm;
/*     */ import tyRuBa.engine.RBTerm;
/*     */ import tyRuBa.engine.RBTuple;
/*     */ import tyRuBa.engine.compilation.CompilationContext;
/*     */ import tyRuBa.engine.compilation.Compiled;
/*     */ import tyRuBa.modes.ConstructorType;
/*     */ import tyRuBa.modes.PredicateMode;
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
/*     */ 
/*     */ public class FileWriterFactBase
/*     */   extends FactBase
/*     */ {
/*     */   private String predicateName;
/*     */   private FactBase containedFactBase;
/*     */   private static PrintWriter pw;
/*     */   
/*     */   public FileWriterFactBase(PredicateIdentifier pid, FactBase fb, File f)
/*     */   {
/*  43 */     this.predicateName = pid.getName();
/*  44 */     this.containedFactBase = fb;
/*     */     try {
/*  46 */       if (pw == null) {
/*  47 */         pw = new PrintWriter(new FileOutputStream(f));
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/*  51 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  60 */     return this.containedFactBase.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isPersistent()
/*     */   {
/*  67 */     return this.containedFactBase.isPersistent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized void insert(RBComponent f)
/*     */   {
/*  74 */     if (f.isGroundFact()) {
/*  75 */       pw.print(this.predicateName + "(");
/*  76 */       RBTuple args = f.getArgs();
/*  77 */       printTuple(args);
/*  78 */       pw.println(").");
/*  79 */       this.containedFactBase.insert(f);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void printTuple(RBTuple args)
/*     */   {
/*  88 */     for (int i = 0; i < args.getNumSubterms(); i++) {
/*  89 */       RBTerm subterm = args.getSubterm(i);
/*  90 */       if (i > 0) {
/*  91 */         pw.print(", ");
/*     */       }
/*  93 */       if ((subterm instanceof RBCompoundTerm)) {
/*  94 */         RBCompoundTerm compterm = (RBCompoundTerm)subterm;
/*  95 */         pw.print(compterm.getConstructorType().getFunctorId().getName() + "<");
/*  96 */         RBTerm[] terms = new RBTerm[compterm.getNumArgs()];
/*  97 */         for (int j = 0; j < compterm.getNumArgs(); j++) {
/*  98 */           terms[j] = compterm.getArg(j);
/*     */         }
/* 100 */         printTuple(RBTuple.make(terms));
/* 101 */         pw.print(">");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Compiled basicCompile(PredicateMode mode, CompilationContext context)
/*     */   {
/* 111 */     pw.flush();
/* 112 */     return this.containedFactBase.compile(mode, context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void backup()
/*     */   {
/* 120 */     this.containedFactBase.backup();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/FileWriterFactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */