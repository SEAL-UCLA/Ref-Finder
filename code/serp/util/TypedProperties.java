/*     */ package serp.util;
/*     */ 
/*     */ import java.util.Properties;
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
/*     */ public class TypedProperties
/*     */   extends Properties
/*     */ {
/*     */   public TypedProperties() {}
/*     */   
/*     */   public TypedProperties(Properties defaults)
/*     */   {
/*  33 */     super(defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBooleanProperty(String key)
/*     */   {
/*  43 */     return getBooleanProperty(key, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getBooleanProperty(String key, boolean def)
/*     */   {
/*  53 */     String val = getProperty(key);
/*  54 */     return val == null ? def : Boolean.valueOf(val).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getFloatProperty(String key)
/*     */   {
/*  66 */     return getFloatProperty(key, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getFloatProperty(String key, float def)
/*     */   {
/*  78 */     String val = getProperty(key);
/*  79 */     return val == null ? def : Float.parseFloat(val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getDoubleProperty(String key)
/*     */   {
/*  91 */     return getDoubleProperty(key, 0.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getDoubleProperty(String key, double def)
/*     */   {
/* 103 */     String val = getProperty(key);
/* 104 */     return val == null ? def : Double.parseDouble(val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLongProperty(String key)
/*     */   {
/* 116 */     return getLongProperty(key, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLongProperty(String key, long def)
/*     */   {
/* 128 */     String val = getProperty(key);
/* 129 */     return val == null ? def : Long.parseLong(val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntProperty(String key)
/*     */   {
/* 141 */     return getIntProperty(key, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntProperty(String key, int def)
/*     */   {
/* 153 */     String val = getProperty(key);
/* 154 */     return val == null ? def : Integer.parseInt(val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object setProperty(String key, String val)
/*     */   {
/* 166 */     if (val == null)
/* 167 */       return remove(key);
/* 168 */     return super.setProperty(key, val);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String key, boolean val)
/*     */   {
/* 179 */     setProperty(key, String.valueOf(val));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String key, double val)
/*     */   {
/* 190 */     setProperty(key, String.valueOf(val));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String key, float val)
/*     */   {
/* 201 */     setProperty(key, String.valueOf(val));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String key, int val)
/*     */   {
/* 212 */     setProperty(key, String.valueOf(val));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperty(String key, long val)
/*     */   {
/* 223 */     setProperty(key, String.valueOf(val));
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/TypedProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */