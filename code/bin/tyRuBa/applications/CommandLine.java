/*     */ package tyRuBa.applications;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.tests.PerformanceTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandLine
/*     */ {
/*  21 */   FrontEnd frontend = null;
/*  22 */   boolean loadInitFile = true;
/*  23 */   File dbDir = null;
/*  24 */   int cachesize = 5000;
/*  25 */   private boolean backgroundPageCleaning = false;
/*     */   
/*     */   void ensureFrontEnd() throws IOException, ParseException, TypeModeError
/*     */   {
/*  29 */     if (this.frontend == null) {
/*  30 */       if (this.dbDir == null) {
/*  31 */         this.frontend = new FrontEnd(this.loadInitFile, new File("./fdb/"), true, null, true, this.backgroundPageCleaning);
/*     */       } else
/*  33 */         this.frontend = new FrontEnd(this.loadInitFile, this.dbDir, true, null, false, this.backgroundPageCleaning);
/*     */     }
/*  35 */     this.frontend.setCacheSize(this.cachesize);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  39 */     new CommandLine().realmain(args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void realmain(String[] args)
/*     */   {
/*  46 */     if (args.length == 0) {
/*  47 */       System.err.println("ERROR: no commandline arguments where given");
/*  48 */       return;
/*     */     }
/*     */     try {
/*  51 */       int start = 0;
/*  52 */       for (int i = start; i < args.length; i++) {
/*  53 */         if (args[i].charAt(0) == '-') {
/*  54 */           if (args[i].equals("-noinit")) {
/*  55 */             System.err.println("Option -noinit seen...");
/*  56 */             if (this.frontend != null)
/*  57 */               throw new Error("The -noinit option must occur before any file names");
/*  58 */             this.loadInitFile = false;
/*  59 */           } else if (args[i].equals("-bgpager")) {
/*  60 */             if (this.frontend != null)
/*  61 */               throw new Error("The -bgpager option must occur before any file names");
/*  62 */             this.backgroundPageCleaning = true;
/*  63 */           } else if (args[i].equals("-cachesize")) {
/*  64 */             this.cachesize = Integer.parseInt(args[(++i)]);
/*  65 */             if (this.frontend != null)
/*  66 */               this.frontend.setCacheSize(this.cachesize);
/*  67 */           } else if (args[i].equals("-dbdir")) {
/*  68 */             if (this.frontend != null)
/*  69 */               throw new Error("The -dbdir option must occur before any file names");
/*  70 */             if (this.dbDir != null)
/*  71 */               throw new Error("The -dbdir option can only be set once");
/*  72 */             this.dbDir = new File(args[(++i)]);
/*  73 */           } else if (args[i].equals("-o")) {
/*  74 */             ensureFrontEnd();
/*  75 */             this.frontend.redirectOutput(
/*  76 */               new PrintStream(new FileOutputStream(args[(++i)])));
/*  77 */           } else if (args[i].equals("-i")) {
/*  78 */             System.err.println("Option -i seen...");
/*  79 */             ensureFrontEnd();
/*  80 */             boolean keepGoing = false;
/*     */             do {
/*     */               try {
/*  83 */                 System.err.println(
/*  84 */                   "\n--- Interactive mode... type queries!");
/*  85 */                 System.err.println("end with CTRL-D");
/*  86 */                 this.frontend.load(System.in);
/*  87 */                 keepGoing = false;
/*     */               } catch (ParseException e) {
/*  89 */                 keepGoing = true;
/*  90 */                 System.err.println(
/*  91 */                   "TyRuBaParser:" + e.getMessage());
/*     */               } catch (TypeModeError e) {
/*  93 */                 keepGoing = true;
/*  94 */                 System.err.println(
/*  95 */                   "Type or Mode Error: " + e.getMessage());
/*     */               }
/*  81 */             } while (
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
/*  97 */               keepGoing);
/*  98 */           } else if (args[i].equals("-silent")) {
/*  99 */             tyRuBa.engine.RuleBase.silent = true;
/* 100 */           } else if (args[i].equals("-nocache")) {
/* 101 */             if (this.frontend != null)
/* 102 */               throw new Error("The -nocache option must occur before any file names");
/* 103 */             tyRuBa.engine.RuleBase.useCache = false;
/* 104 */           } else if (args[i].equals("-classpath")) {
/* 105 */             ensureFrontEnd();
/* 106 */             this.frontend.parse("classpath(\"" + args[(++i)] + "\").");
/* 107 */           } else if (args[i].equals("-parse")) {
/* 108 */             String command = args[(++i)];
/* 109 */             while (!args[i].endsWith("."))
/* 110 */               command = command + " " + args[(++i)];
/* 111 */             System.err.println("-parse " + command);
/* 112 */             this.frontend.parse(command);
/* 113 */           } else if (args[i].equals("-benchmark")) {
/* 114 */             ensureFrontEnd();
/* 115 */             String queryfile = args[(++i)];
/* 116 */             PerformanceTest test = PerformanceTest.make(this.frontend, queryfile);
/* 117 */             this.frontend.output().println("----- results for tests in " + queryfile + " -------");
/* 118 */             this.frontend.output().println(test);
/*     */           }
/* 120 */           else if (args[i].equals("-metadata")) {
/* 121 */             ensureFrontEnd();
/* 122 */             this.frontend.enableMetaData();
/*     */           } else {
/* 124 */             System.err.println(
/* 125 */               "*** Error: unkown commandline option: " + args[i]);
/* 126 */             System.exit(-1);
/*     */           }
/*     */         } else {
/* 129 */           ensureFrontEnd();
/* 130 */           System.err.println("Loading file: " + args[i]);
/* 131 */           this.frontend.load(args[i]);
/*     */         }
/*     */       }
/*     */     } catch (FileNotFoundException e) {
/* 135 */       System.err.println("TyRuBaParser:" + e.getMessage());
/* 136 */       System.exit(-1);
/*     */     } catch (IOException e) {
/* 138 */       System.err.println("TyRuBaParser:" + e.getMessage());
/* 139 */       System.exit(-2);
/*     */     } catch (ParseException e) {
/* 141 */       System.err.println(e.getMessage());
/* 142 */       System.exit(-3);
/*     */     } catch (TypeModeError e) {
/* 144 */       e.printStackTrace();
/* 145 */       System.exit(-4);
/*     */     }
/* 147 */     this.frontend.shutdown();
/* 148 */     System.exit(0);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/applications/CommandLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */