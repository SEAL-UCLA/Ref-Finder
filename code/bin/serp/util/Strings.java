/*     */ package serp.util;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Strings
/*     */ {
/*  14 */   private static final Object[][] _codes = {
/*  15 */     { Byte.TYPE, "byte" }, 
/*  16 */     { Character.TYPE, "char" }, 
/*  17 */     { Double.TYPE, "double" }, 
/*  18 */     { Float.TYPE, "float" }, 
/*  19 */     { Integer.TYPE, "int" }, 
/*  20 */     { Long.TYPE, "long" }, 
/*  21 */     { Short.TYPE, "short" }, 
/*  22 */     { Boolean.TYPE, "boolean" }, 
/*  23 */     { Void.TYPE, "void" } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] split(String str, String token, int max)
/*     */   {
/*  34 */     if ((str == null) || (str.length() == 0))
/*  35 */       return new String[0];
/*  36 */     if ((token == null) || (token.length() == 0)) {
/*  37 */       throw new IllegalArgumentException("token: [" + token + "]");
/*     */     }
/*     */     
/*  40 */     LinkedList ret = new LinkedList();
/*  41 */     int start = 0;
/*  42 */     for (int split = 0; split != -1;)
/*     */     {
/*  44 */       split = str.indexOf(token, start);
/*  45 */       if ((split == -1) && (start >= str.length())) {
/*  46 */         ret.add("");
/*  47 */       } else if (split == -1) {
/*  48 */         ret.add(str.substring(start));
/*     */       }
/*     */       else {
/*  51 */         ret.add(str.substring(start, split));
/*  52 */         start = split + token.length();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  59 */     if (max == 0)
/*     */     {
/*     */ 
/*  62 */       while (ret.getLast().equals("")) {
/*  63 */         ret.removeLast();
/*     */       }
/*  65 */     } else if ((max > 0) && (ret.size() > max))
/*     */     {
/*     */ 
/*  68 */       StringBuffer buf = new StringBuffer(ret.removeLast().toString());
/*  69 */       while (ret.size() >= max)
/*     */       {
/*  71 */         buf.insert(0, token);
/*  72 */         buf.insert(0, ret.removeLast());
/*     */       }
/*  74 */       ret.add(buf.toString());
/*     */     }
/*     */     
/*  77 */     return (String[])ret.toArray(new String[ret.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String join(Object[] strings, String token)
/*     */   {
/*  86 */     if (strings == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     StringBuffer buf = new StringBuffer(20 * strings.length);
/*  90 */     for (int i = 0; i < strings.length; i++)
/*     */     {
/*  92 */       if (i > 0)
/*  93 */         buf.append(token);
/*  94 */       if (strings[i] != null)
/*  95 */         buf.append(strings[i]);
/*     */     }
/*  97 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String capitalize(String str)
/*     */   {
/* 107 */     if ((str == null) || (str.length() == 0)) {
/* 108 */       return str;
/*     */     }
/* 110 */     char first = Character.toUpperCase(str.charAt(0));
/* 111 */     if (str.length() == 1) {
/* 112 */       return String.valueOf(first);
/*     */     }
/* 114 */     return first + str.substring(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class toClass(String str, ClassLoader loader)
/*     */   {
/* 127 */     if (str == null) {
/* 128 */       throw new NullPointerException("str = null");
/*     */     }
/*     */     
/* 131 */     for (int i = 0; i < _codes.length; i++) {
/* 132 */       if (_codes[i][1].toString().equals(str))
/* 133 */         return (Class)_codes[i][0];
/*     */     }
/* 135 */     if (loader == null) {
/* 136 */       loader = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */     try
/*     */     {
/* 140 */       return Class.forName(str, true, loader);
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 144 */       throw new IllegalArgumentException(t.getClass().getName() + ": " + 
/* 145 */         t.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/Strings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */