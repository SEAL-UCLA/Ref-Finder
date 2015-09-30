/*    */ package lsd.facts;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import lsd.io.LSDTyrubaFactReader;
/*    */ import lsd.io.LSDTyrubaRuleChecker;
/*    */ import lsd.rule.LSDFact;
/*    */ import lsd.rule.LSDRule;
/*    */ 
/*    */ public class LSD2KBMatches
/*    */ {
/*    */   PrintStream p;
/*    */   String outputDir;
/*    */   
/*    */   static
/*    */   {
/* 23 */     tyRuBa.engine.RuleBase.silent = true;
/*    */   }
/*    */   
/*    */   public LSD2KBMatches(PrintStream p, String outputDir) {
/* 27 */     this.p = p;
/* 28 */     this.outputDir = outputDir;
/*    */   }
/*    */   
/*    */   public static String matchesInRuleFile(File ruleFile, File twoKBFile, File deltaKBFile)
/*    */   {
/* 33 */     LSDTyrubaRuleChecker ruleChecker = null;
/*    */     try {
/* 35 */       ruleChecker = new LSDTyrubaRuleChecker();
/*    */       
/* 37 */       ruleChecker.loadAdditionalDB(twoKBFile);
/* 38 */       ruleChecker.loadAdditionalDB(deltaKBFile);
/*    */     }
/*    */     catch (Throwable e) {
/* 41 */       e.printStackTrace();
/* 42 */       System.exit(-1);
/*    */     }
/* 44 */     Set<LSDFact> factsDeltaKB = new HashSet();
/* 45 */     ArrayList<LSDFact> factList = new LSDTyrubaFactReader(
/* 46 */       deltaKBFile).getFacts();
/* 47 */     for (LSDFact f : factList) {
/* 48 */       factsDeltaKB.add(f);
/*    */     }
/* 50 */     Set<LSDFact> facts = new HashSet();
/* 51 */     int numberOfRules = 0;
/* 52 */     int outsideReferences = 0;
/* 53 */     int outsideReferencingRules = 0;
/*    */     try {
/* 55 */       if (ruleFile.exists()) {
/* 56 */         BufferedReader in = new BufferedReader(
/* 57 */           new FileReader(ruleFile));
/* 58 */         String line = null;
/* 59 */         while ((line = in.readLine()) != null)
/*    */         {
/* 61 */           if (!line.trim().equals(""))
/*    */           {
/* 63 */             if (line.trim().charAt(0) != '#')
/*    */             {
/* 65 */               if (line.contains("=>"))
/*    */               {
/* 67 */                 LSDRule rule = lsd.io.LSDAlchemyRuleReader.parseAlchemyRule(line);
/* 68 */                 numberOfRules++;
/* 69 */                 int factsThisRule = 0;
/* 70 */                 for (LSDFact fact : ruleChecker.get2KBMatches(rule))
/* 71 */                   if ((!factsDeltaKB.contains(fact.addedCopy())) && 
/* 72 */                     (!factsDeltaKB.contains(fact.deletedCopy())))
/*    */                   {
/*    */ 
/* 75 */                     factsThisRule++;
/* 76 */                     facts.add(fact);
/*    */                   }
/* 78 */                 if (factsThisRule > 0) {
/* 79 */                   outsideReferences += factsThisRule;
/* 80 */                   outsideReferencingRules++;
/*    */                 }
/*    */               } } }
/*    */         }
/* 84 */         in.close();
/*    */       }
/* 86 */       ruleChecker.shutdown();
/*    */     } catch (IOException e) {
/* 88 */       e.printStackTrace();
/*    */     }
/*    */     
/* 91 */     return numberOfRules + "\t" + outsideReferencingRules + "\t" + outsideReferences + "\t" + facts.size();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/facts/LSD2KBMatches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */