/*     */ package lsd.rule;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LSDConst
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  10 */     String field = "\"ca.ubc.jquery%.ActionsLog#append\"";
/*  11 */     System.out.println(getContainerPackage(field));
/*  12 */     System.out.println(getContainerType(field));
/*  13 */     System.out.println(getShortMethodOrFieldName(field));
/*     */     
/*  15 */     LSDFact mf = createModifiedField(field);
/*  16 */     System.out.println(mf.toString());
/*  17 */     String method = "\"ca.ubc.jquery%.ActionsLog#logGeneric(java.lang%.String)\"";
/*  18 */     LSDFact mm = createModifiedMethod(method);
/*  19 */     System.out.println(mm.toString());
/*     */     
/*  21 */     String type = "\"org.jfree.chart.util%.LineUtilities\"";
/*  22 */     LSDFact mt = createModifiedType(type);
/*  23 */     System.out.println(mt.toString());
/*     */     
/*  25 */     String shortName = "\"drawDomainMarker(java.awt%.Graphics2D,org.jfree.chart.plot%.CategoryPlot,org.jfree.chart.axis%.CategoryAxis,org.jfree.chart.plot%.CategoryMarker,java.awt.geom%.Rectangle2D)\"";
/*  26 */     String typeName = "\"org.jfree.chart.renderer.category%.IntervalBarRenderer\"";
/*  27 */     String fullName = createFullMethodOrFieldName(shortName, typeName);
/*  28 */     LSDFact mm2 = createModifiedMethod(fullName);
/*  29 */     System.out.println(mm2.toString());
/*     */   }
/*     */   
/*  32 */   private static String getContainerType(String methodOrField) { int beginIndex = methodOrField.indexOf("\"") + 1;
/*  33 */     int endIndex = methodOrField.indexOf("#");
/*  34 */     assert (beginIndex >= 1);
/*  35 */     assert (endIndex > beginIndex);
/*  36 */     return methodOrField.substring(beginIndex, endIndex);
/*     */   }
/*     */   
/*  39 */   private static String getShortMethodOrFieldName(String methodOrField) { int beginIndex = methodOrField.indexOf("#") + 1;
/*  40 */     int endIndex = methodOrField.length();
/*  41 */     assert (beginIndex >= 1);
/*  42 */     assert (endIndex > beginIndex);
/*  43 */     return methodOrField.substring(beginIndex, endIndex);
/*     */   }
/*     */   
/*     */   private static String getShortTypeName(String typeOrInnerType) {
/*  47 */     int beginIndex = typeOrInnerType.indexOf("%") + 2;
/*  48 */     int endIndex = typeOrInnerType.length();
/*  49 */     assert (beginIndex >= 1);
/*  50 */     assert (endIndex > beginIndex);
/*  51 */     return typeOrInnerType.substring(beginIndex, endIndex);
/*     */   }
/*     */   
/*  54 */   private static String getContainerPackage(String typeOrMethodOrField) { int beginIndex = typeOrMethodOrField.indexOf("\"") + 1;
/*  55 */     int endIndex = typeOrMethodOrField.indexOf("%");
/*  56 */     assert (beginIndex >= 2);
/*  57 */     assert (endIndex > beginIndex);
/*     */     try {
/*  59 */       return typeOrMethodOrField.substring(beginIndex, endIndex);
/*     */     } catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*  61 */       System.err.println(typeOrMethodOrField);
/*  62 */       System.exit(0);
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */   
/*     */ 
/*  68 */   private static String stripDoubleQuote(String s) { return s.replaceAll("\"", ""); }
/*     */   
/*     */   public static LSDFact createModifiedField(String fullFieldName) {
/*  71 */     String shortFieldName = getShortMethodOrFieldName(fullFieldName);
/*  72 */     String containerType = getContainerType(fullFieldName);
/*     */     
/*  74 */     LSDPredicate pred = LSDPredicate.getPredicate("modified_field");
/*  75 */     fullFieldName = stripDoubleQuote(fullFieldName);
/*  76 */     List<String> constants = new java.util.ArrayList();
/*  77 */     constants.add(fullFieldName);
/*  78 */     constants.add(shortFieldName);
/*  79 */     constants.add(containerType);
/*     */     
/*  81 */     LSDFact mf = LSDFact.createLSDFact(pred, constants, true);
/*  82 */     return mf;
/*     */   }
/*     */   
/*     */   public static LSDFact createModifiedMethod(String fullMethodName) {
/*  86 */     String shortMethodName = getShortMethodOrFieldName(fullMethodName);
/*  87 */     String containerType = getContainerType(fullMethodName);
/*  88 */     LSDPredicate pred = LSDPredicate.getPredicate("modified_method");
/*  89 */     List<String> constants = new java.util.ArrayList();
/*  90 */     constants.add(stripDoubleQuote(fullMethodName));
/*  91 */     constants.add(shortMethodName);
/*  92 */     constants.add(containerType);
/*     */     
/*  94 */     LSDFact mf = LSDFact.createLSDFact(pred, constants, true);
/*  95 */     return mf;
/*     */   }
/*     */   
/*     */   public static LSDFact createModifiedType(String fullTypeName)
/*     */   {
/* 100 */     String shortTypeName = getShortTypeName(fullTypeName);
/* 101 */     String containerPackage = getContainerPackage(fullTypeName);
/* 102 */     LSDPredicate pred = LSDPredicate.getPredicate("modified_type");
/* 103 */     List<String> constants = new java.util.ArrayList();
/* 104 */     constants.add(stripDoubleQuote(fullTypeName));
/* 105 */     constants.add(shortTypeName);
/* 106 */     constants.add(containerPackage);
/*     */     
/* 108 */     LSDFact mt = LSDFact.createLSDFact(pred, constants, true);
/* 109 */     return mt;
/*     */   }
/*     */   
/* 112 */   public static LSDFact createModifiedPackage(String fullPackageName) { LSDPredicate pred = LSDPredicate.getPredicate("modified_package");
/* 113 */     List<String> constants = new java.util.ArrayList();
/* 114 */     constants.add(fullPackageName);
/*     */     
/* 116 */     LSDFact mp = LSDFact.createLSDFact(pred, constants, true);
/* 117 */     return mp;
/*     */   }
/*     */   
/* 120 */   public static String createFullMethodOrFieldName(String shortName, String containerName) { return containerName.substring(0, containerName.length()) + "#" + shortName; }
/*     */   
/*     */   public static LSDFact convertModifiedToAdded(LSDFact mf) {
/* 123 */     LSDPredicate pred = LSDPredicate.getPredicate(mf.getPredicate().getName().replace("modified", "added"));
/*     */     
/* 125 */     return LSDFact.createLSDFact(pred, (java.util.ArrayList)mf.getBindings());
/*     */   }
/*     */   
/* 128 */   public static LSDFact convertModifiedToDeleted(LSDFact mf) { LSDPredicate pred = LSDPredicate.getPredicate(mf.getPredicate().getName().replace("modified", "deleted"));
/*     */     
/* 130 */     return LSDFact.createLSDFact(pred, (java.util.ArrayList)mf.getBindings());
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/rule/LSDConst.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */