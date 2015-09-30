/*    */ package lsd.io;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lsd.rule.LSDFact;
/*    */ import lsd.rule.LSDPredicate;
/*    */ 
/*    */ public class LSDTyrubaFactReader
/*    */ {
/* 14 */   private ArrayList<LSDFact> facts = null;
/*    */   
/*    */   public LSDTyrubaFactReader(File inputFile) {
/* 17 */     ArrayList<LSDFact> fs = new ArrayList();
/*    */     try {
/* 19 */       if (inputFile.exists()) {
/* 20 */         BufferedReader in = new BufferedReader(
/* 21 */           new java.io.FileReader(inputFile));
/* 22 */         String line = null;
/* 23 */         while ((line = in.readLine()) != null)
/* 24 */           if ((!line.trim().equals("")) && (line.trim().charAt(0) != '#') && 
/* 25 */             (!line.trim().startsWith("//")))
/*    */           {
/* 27 */             LSDFact fact = parseTyrubaFact(line);
/* 28 */             fs.add(fact);
/*    */           }
/* 30 */         in.close();
/*    */       }
/* 32 */       this.facts = fs;
/*    */     } catch (IOException e) {
/* 34 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static ArrayList<LSDFact> convertToClassLevel(ArrayList<LSDFact> readDeltaFacts)
/*    */   {
/* 42 */     ArrayList<LSDFact> facts = new ArrayList();
/* 43 */     for (LSDFact fact : readDeltaFacts)
/* 44 */       if (fact.getPredicate().isMethodLevel()) {
/* 45 */         LSDFact tempFact = fact.convertToClassLevel();
/* 46 */         if (tempFact != null)
/*    */         {
/*    */ 
/* 49 */           facts.add(tempFact); }
/*    */       } else {
/* 51 */         facts.add(fact);
/*    */       }
/* 53 */     return facts;
/*    */   }
/*    */   
/*    */   public ArrayList<LSDFact> getFacts() {
/* 57 */     return this.facts;
/*    */   }
/*    */   
/*    */   public static LSDFact parseTyrubaFact(String line)
/*    */   {
/* 62 */     String factString = line.trim();
/*    */     
/* 64 */     String predicateName = factString.substring(0, factString.indexOf('('))
/* 65 */       .trim();
/* 66 */     LSDPredicate predicate = LSDPredicate.getPredicate(predicateName);
/* 67 */     factString = factString.substring(factString.indexOf('(') + 1).trim();
/* 68 */     int endOfArgs = factString.lastIndexOf(')');
/* 69 */     String arguments = factString.substring(0, endOfArgs).trim();
/* 70 */     factString = factString.substring(endOfArgs + 1).trim();
/* 71 */     if (!factString.equals("."))
/*    */     {
/* 73 */       System.err.println("Facts must be in the form 'predicate(const, const, ...).'");
/* 74 */       System.err.println("Line: " + line);
/* 75 */       System.exit(-3);
/*    */     }
/*    */     
/* 78 */     if (predicate == null) {
/* 79 */       System.err.println("Predicate " + predicateName + 
/* 80 */         " is not defined.");
/* 81 */       System.err.println("Line: " + line);
/* 82 */       System.exit(-1);
/*    */     }
/* 84 */     String[] params = arguments.split("\", \"");
/* 85 */     List<String> binds = new ArrayList();
/* 86 */     String[] arrayOfString1; int j = (arrayOfString1 = params).length; for (int i = 0; i < j; i++) { String p = arrayOfString1[i];
/* 87 */       if (p.startsWith("\"")) {
/* 88 */         binds.add(p.substring(1));
/* 89 */       } else if (p.endsWith("\"")) {
/* 90 */         binds.add(p.substring(0, p.length() - 2));
/*    */       } else {
/* 92 */         binds.add(p);
/*    */       }
/*    */     }
/* 95 */     return LSDFact.createLSDFact(predicate, binds, true);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/io/LSDTyrubaFactReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */