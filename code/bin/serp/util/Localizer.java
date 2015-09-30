/*     */ package serp.util;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
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
/*     */ public class Localizer
/*     */ {
/*  26 */   private static Map _bundles = new HashMap();
/*     */   
/*     */ 
/*  29 */   private String _file = null;
/*     */   
/*     */ 
/*  32 */   private Locale _locale = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Localizer forPackage(Class cls)
/*     */   {
/*  43 */     return forPackage(cls, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Localizer forPackage(Class cls, Locale locale)
/*     */   {
/*  59 */     Localizer loc = new Localizer();
/*     */     
/*  61 */     int dot = cls == null ? -1 : cls.getName().lastIndexOf('.');
/*  62 */     if (dot == -1) {
/*  63 */       loc._file = "localizer";
/*     */     } else
/*  65 */       loc._file = (cls.getName().substring(0, dot + 1) + "localizer");
/*  66 */     loc._locale = locale;
/*     */     
/*  68 */     return loc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String key)
/*     */   {
/*  79 */     return get(key, this._locale);
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
/*     */ 
/*     */   public String get(String key, Object sub)
/*     */   {
/*  93 */     return get(key, new Object[] { sub }, this._locale);
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
/*     */ 
/*     */   public String get(String key, Object[] subs)
/*     */   {
/* 107 */     return get(key, subs, this._locale);
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
/*     */ 
/*     */   public String get(String key, Object sub, Locale locale)
/*     */   {
/* 121 */     return get(key, new Object[] { sub }, locale);
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
/*     */ 
/*     */   public String get(String key, Object[] subs, Locale locale)
/*     */   {
/* 135 */     String str = get(key, locale);
/* 136 */     return MessageFormat.format(str, subs);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String key, Locale locale)
/*     */   {
/* 152 */     String cacheKey = this._file;
/* 153 */     if (locale != null)
/* 154 */       cacheKey = cacheKey + locale.toString();
/* 155 */     ResourceBundle bundle = (ResourceBundle)_bundles.get(cacheKey);
/*     */     
/* 157 */     if (bundle == null)
/*     */     {
/* 159 */       if (locale != null) {
/* 160 */         bundle = ResourceBundle.getBundle(this._file, locale);
/*     */       } else {
/* 162 */         bundle = ResourceBundle.getBundle(this._file);
/*     */       }
/*     */       
/* 165 */       _bundles.put(cacheKey, bundle);
/*     */     }
/*     */     
/* 168 */     return bundle.getString(key);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/Localizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */