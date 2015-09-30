/*     */ package lsdSimplified;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ import lsd.rule.LSDPredicate;
/*     */ import metapackage.MetaInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Converter
/*     */ {
/*  20 */   static String tempRes = MetaInfo.srcDir + "temp.rub";
/*     */   
/*     */   public static File convertDeltaFacts(File src) {
/*  23 */     File res = new File(tempRes);
/*  24 */     String line = null;
/*  25 */     BufferedWriter writer = null;
/*     */     try {
/*  27 */       BufferedReader input = new BufferedReader(new FileReader(src));
/*  28 */       writer = new BufferedWriter(new FileWriter(res));
/*  29 */       while ((line = input.readLine()) != null) {
/*  30 */         if ((line.contains("//")) || (line.contains("include")) || (line.length() == 0))
/*     */         {
/*  32 */           writer.write(line);
/*  33 */           writer.newLine();
/*     */         }
/*     */         else {
/*  36 */           String pred = line.substring(0, line.indexOf("("));
/*  37 */           String arg = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
/*  38 */           if (shouldChange(pred))
/*     */           {
/*  40 */             writer.write(changeArguments(pred, arg));
/*  41 */             writer.newLine();
/*     */           }
/*     */           else
/*     */           {
/*  45 */             writer.write(line);
/*  46 */             writer.newLine();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  51 */       writer.close();
/*  52 */       return res;
/*     */     } catch (Exception localException) {
/*  54 */       System.err.println("error" + line);
/*     */       try {
/*  56 */         writer.close();
/*     */       }
/*     */       catch (IOException e1) {
/*  59 */         e1.printStackTrace();
/*     */       } }
/*  61 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean shouldChange(String predicateName)
/*     */   {
/*  66 */     LSDPredicate predicate = LSDPredicate.getPredicate(predicateName);
/*  67 */     if (predicate.isMethodLevel())
/*  68 */       return true;
/*  69 */     return false;
/*     */   }
/*     */   
/*     */   public static String changeArguments(String predicateName, String arguments)
/*     */   {
/*  74 */     StringTokenizer tokenizer = new StringTokenizer(arguments, ",", false);
/*  75 */     String arg0 = tokenizer.nextToken();
/*     */     try {
/*  77 */       if ((predicateName.contains("typeintype")) || (predicateName.contains("accesses")))
/*  78 */         arg0 = tokenizer.nextToken();
/*  79 */       if (predicateName.contains("inherited"))
/*     */       {
/*  81 */         arg0 = tokenizer.nextToken();
/*  82 */         arg0 = tokenizer.nextToken();
/*     */       }
/*  84 */       if (arg0.indexOf("#") != -1) {
/*  85 */         arg0 = arg0.substring(1, arg0.indexOf("#"));
/*  86 */       } else if (arg0.indexOf("\"") != -1)
/*  87 */         arg0 = arg0.substring(1, arg0.lastIndexOf("\""));
/*  88 */       String arg1 = arg0.substring(arg0.indexOf("%.") + 2);
/*  89 */       String arg2; String arg2; if (arg0.indexOf("%") == 0) {
/*  90 */         arg2 = "null";
/*     */       } else
/*  92 */         arg2 = arg0.substring(0, arg0.indexOf("%."));
/*  93 */       if ((arg2 == null) || (arg2.length() == 0))
/*  94 */         arg2 = "null";
/*  95 */       return "changed_type(\"" + arg0 + "\",\"" + arg1 + "\",\"" + arg2 + "\").";
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*  99 */       arg0 = predicateName + "(\"" + arguments + ").";
/* 100 */       System.out.println(arg0); }
/* 101 */     return arg0;
/*     */   }
/*     */   
/*     */   public static void clear()
/*     */   {
/* 106 */     File res = new File(tempRes);
/*     */     
/* 108 */     res.delete();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsdSimplified/Converter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */