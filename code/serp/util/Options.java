/*     */ package serp.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public class Options
/*     */   extends TypedProperties
/*     */ {
/*  28 */   private static Object[][] _primWrappers = {
/*  29 */     { Boolean.TYPE, Boolean.class, Boolean.FALSE }, 
/*  30 */     { Byte.TYPE, Byte.class, new Byte(0) }, 
/*  31 */     { Character.TYPE, Character.class, new Character('\000') }, 
/*  32 */     { Double.TYPE, Double.class, new Double(0.0D) }, 
/*  33 */     { Float.TYPE, Float.class, new Float(0.0F) }, 
/*  34 */     { Integer.TYPE, Integer.class, new Integer(0) }, 
/*  35 */     { Long.TYPE, Long.class, new Long(0L) }, 
/*  36 */     { Short.TYPE, Short.class, new Short(0) } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Options() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Options(Properties defaults)
/*     */   {
/*  56 */     super(defaults);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] setFromCmdLine(String[] args)
/*     */   {
/*  76 */     if ((args == null) || (args.length == 0)) {
/*  77 */       return args;
/*     */     }
/*  79 */     String key = null;
/*  80 */     String value = null;
/*  81 */     List remainder = new LinkedList();
/*  82 */     for (int i = 0; i < args.length + 1; i++)
/*     */     {
/*  84 */       if ((i == args.length) || (args[i].startsWith("-")))
/*     */       {
/*  86 */         key = trimQuote(key);
/*  87 */         if (key != null)
/*     */         {
/*  89 */           if ((value != null) && (value.length() > 0)) {
/*  90 */             setProperty(key, trimQuote(value));
/*     */           } else {
/*  92 */             setProperty(key, "true");
/*     */           }
/*     */         }
/*  95 */         if (i == args.length) {
/*     */           break;
/*     */         }
/*     */         
/*  99 */         key = args[i].substring(1);
/* 100 */         value = null;
/*     */ 
/*     */       }
/* 103 */       else if (key != null)
/*     */       {
/* 105 */         setProperty(key, trimQuote(args[i]));
/* 106 */         key = null;
/*     */       }
/*     */       else {
/* 109 */         remainder.add(args[i]);
/*     */       }
/*     */     }
/* 112 */     return (String[])remainder.toArray(new String[remainder.size()]);
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
/*     */   public void setInto(Object obj)
/*     */   {
/* 155 */     Map.Entry entry = null;
/* 156 */     if (this.defaults != null)
/*     */     {
/* 158 */       for (Iterator itr = this.defaults.entrySet().iterator(); itr.hasNext();)
/*     */       {
/* 160 */         entry = (Map.Entry)itr.next();
/* 161 */         if (!containsKey(entry.getKey())) {
/* 162 */           setInto(obj, entry);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 167 */     for (Iterator itr = entrySet().iterator(); itr.hasNext();) {
/* 168 */       setInto(obj, (Map.Entry)itr.next());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setInto(Object obj, Map.Entry entry)
/*     */   {
/* 178 */     if (entry.getKey() == null) {
/* 179 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 184 */       Object[] match = { obj };
/* 185 */       if (!matchOptionToSetter(entry.getKey().toString(), match)) {
/* 186 */         return;
/*     */       }
/* 188 */       Method setter = (Method)match[1];
/* 189 */       Class[] paramTypes = setter.getParameterTypes();
/* 190 */       Object[] values = new Object[paramTypes.length];
/* 191 */       String[] strValues = entry.getValue() == null ? new String[1] : 
/* 192 */         Strings.split(entry.getValue().toString(), ",", -1);
/*     */       
/*     */ 
/*     */ 
/* 196 */       for (int i = 0; i < strValues.length; i++)
/* 197 */         values[i] = stringToObject(strValues[i], paramTypes[i]);
/* 198 */       for (int i = strValues.length; i < values.length; i++) {
/* 199 */         values[i] = getDefaultValue(paramTypes[i]);
/*     */       }
/*     */       
/* 202 */       setter.invoke(match[0], values);
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 206 */       throw new IllegalArgumentException(obj + "." + entry.getKey() + 
/* 207 */         " = " + entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String trimQuote(String val)
/*     */   {
/* 218 */     if ((val != null) && (val.startsWith("'")) && (val.endsWith("'")))
/* 219 */       return val.substring(1, val.length() - 1);
/* 220 */     return val;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean matchOptionToSetter(String key, Object[] match)
/*     */     throws Exception
/*     */   {
/* 241 */     if ((key == null) || (key.length() == 0)) {
/* 242 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 247 */     String[] find = Strings.split(key, ".", 2);
/* 248 */     String base = Strings.capitalize(find[0]);
/* 249 */     String set = "set" + base;
/* 250 */     String get = "get" + base;
/*     */     
/*     */ 
/* 253 */     Class type = match[0].getClass();
/* 254 */     Method[] meths = type.getMethods();
/* 255 */     Method setter = null;
/* 256 */     Method getter = null;
/* 257 */     for (int i = 0; i < meths.length; i++)
/*     */     {
/* 259 */       if (meths[i].getName().equals(set)) {
/* 260 */         setter = meths[i];
/* 261 */       } else if (meths[i].getName().equals(get))
/* 262 */         getter = meths[i];
/*     */     }
/* 264 */     if ((setter == null) && (getter == null)) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     if (find.length > 1)
/*     */     {
/* 270 */       Object inner = null;
/* 271 */       if (getter != null) {
/* 272 */         inner = getter.invoke(match[0], null);
/*     */       }
/*     */       
/*     */ 
/* 276 */       if (inner == null)
/*     */       {
/* 278 */         Class innerType = setter.getParameterTypes()[0];
/* 279 */         inner = innerType.newInstance();
/* 280 */         setter.invoke(match[0], new Object[] { inner });
/*     */       }
/* 282 */       match[0] = inner;
/* 283 */       return matchOptionToSetter(find[1], match);
/*     */     }
/*     */     
/*     */ 
/* 287 */     match[1] = setter;
/* 288 */     return match[1] != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Object stringToObject(String str, Class type)
/*     */     throws Exception
/*     */   {
/* 300 */     if ((str == null) || (type == String.class)) {
/* 301 */       return str;
/*     */     }
/*     */     
/* 304 */     if (type == Class.class) {
/* 305 */       return Class.forName(str);
/*     */     }
/*     */     
/*     */ 
/* 309 */     if (((type.isPrimitive()) || (Number.class.isAssignableFrom(type))) && 
/* 310 */       (str.length() > 2) && (str.endsWith(".0"))) {
/* 311 */       str = str.substring(0, str.length() - 2);
/*     */     }
/*     */     
/* 314 */     if (type.isPrimitive()) {
/* 315 */       for (int i = 0; i < _primWrappers.length; i++) {
/* 316 */         if (type == _primWrappers[i][0])
/* 317 */           return stringToObject(str, (Class)_primWrappers[i][1]);
/*     */       }
/*     */     }
/* 320 */     Exception err = null;
/*     */     try
/*     */     {
/* 323 */       Constructor cons = type.getConstructor(
/* 324 */         new Class[] { String.class });
/* 325 */       return cons.newInstance(new Object[] { str });
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 329 */       err = e;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 334 */       Class subType = null;
/*     */       try
/*     */       {
/* 337 */         subType = Class.forName(str);
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/* 341 */         throw err;
/*     */       }
/* 343 */       if (!type.isAssignableFrom(subType))
/* 344 */         throw err;
/* 345 */       return subType.newInstance();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object getDefaultValue(Class type)
/*     */   {
/* 354 */     for (int i = 0; i < _primWrappers.length; i++) {
/* 355 */       if (_primWrappers[i][0] == type)
/* 356 */         return _primWrappers[i][2];
/*     */     }
/* 358 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/Options.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */