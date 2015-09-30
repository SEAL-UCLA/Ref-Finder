/*     */ package tyRuBa.engine.factbase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import tyRuBa.engine.Validator;
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
/*     */ public class FileBasedValidatorManager
/*     */   implements ValidatorManager
/*     */ {
/*     */   private String storagePath;
/*     */   private Map validators;
/*     */   private Map identifiers;
/*     */   private Map handles;
/*     */   private long lastInvalidateTime;
/*     */   private long validatorCounter;
/*     */   
/*     */   public FileBasedValidatorManager(String storagePath)
/*     */   {
/*  49 */     this.storagePath = storagePath;
/*  50 */     this.validators = new HashMap();
/*  51 */     this.identifiers = new HashMap();
/*  52 */     this.handles = new HashMap();
/*  53 */     this.lastInvalidateTime = -1L;
/*  54 */     this.validatorCounter = 0L;
/*     */     
/*  56 */     File validatorFile = new File(storagePath + "/validators.data");
/*  57 */     if (validatorFile.exists()) {
/*     */       try {
/*  59 */         FileInputStream fis = new FileInputStream(validatorFile);
/*  60 */         ObjectInputStream ois = new ObjectInputStream(fis);
/*     */         
/*  62 */         int size = ois.readInt();
/*  63 */         for (int i = 0; i < size; i++) {
/*  64 */           String id = (String)ois.readObject();
/*  65 */           Validator validator = (Validator)ois.readObject();
/*     */           
/*  67 */           Long handle = new Long(validator.handle());
/*  68 */           this.handles.put(handle, id);
/*  69 */           this.identifiers.put(id, handle);
/*  70 */           this.validators.put(handle, validator);
/*     */         }
/*  72 */         this.lastInvalidateTime = ois.readLong();
/*  73 */         this.validatorCounter = ois.readLong();
/*     */         
/*  75 */         ois.close();
/*  76 */         fis.close();
/*     */       } catch (IOException localIOException) {
/*  78 */         throw new Error("Could not load validators because of IOException");
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  80 */         throw new Error("Could not load validators because of ClassNotFoundException");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileBasedValidatorManager(URL url)
/*     */   {
/*  91 */     this.storagePath = url.toString();
/*  92 */     this.validators = new HashMap();
/*  93 */     this.identifiers = new HashMap();
/*  94 */     this.handles = new HashMap();
/*     */     try {
/*  96 */       ObjectInputStream ois = new ObjectInputStream(url.openStream());
/*  97 */       int size = ois.readInt();
/*  98 */       for (int i = 0; i < size; i++) {
/*  99 */         String id = (String)ois.readObject();
/* 100 */         Validator validator = (Validator)ois.readObject();
/* 101 */         Long handle = new Long(validator.handle());
/* 102 */         this.handles.put(handle, id);
/* 103 */         this.identifiers.put(id, handle);
/* 104 */         this.validators.put(handle, validator);
/*     */       }
/* 106 */       this.lastInvalidateTime = ois.readLong();
/* 107 */       this.validatorCounter = ois.readLong();
/* 108 */       ois.close();
/*     */     } catch (IOException localIOException) {
/* 110 */       throw new Error("Could not load validators because of IOException");
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 112 */       throw new Error("Could not load validators because of ClassNotFoundException");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Validator v, String identifier)
/*     */   {
/* 121 */     v.setHandle(this.validatorCounter++);
/* 122 */     Long handle = new Long(v.handle());
/* 123 */     if (!this.validators.containsKey(handle)) {
/* 124 */       this.validators.put(handle, v);
/* 125 */       this.identifiers.put(identifier, handle);
/* 126 */       this.handles.put(handle, identifier);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(long validatorHandle, Boolean outdated, Boolean hasFacts)
/*     */   {
/* 135 */     Validator v = (Validator)this.validators.get(new Long(validatorHandle));
/* 136 */     if (v != null) {
/* 137 */       if (outdated != null) {
/* 138 */         v.setOutdated(outdated.booleanValue());
/*     */       }
/* 140 */       if (hasFacts != null) {
/* 141 */         v.setHasAssociatedFacts(hasFacts.booleanValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void remove(long validatorHandle)
/*     */   {
/* 150 */     Long handle = new Long(validatorHandle);
/* 151 */     ((Validator)this.validators.get(handle));
/* 152 */     this.validators.remove(handle);
/* 153 */     String identifier = (String)this.handles.get(handle);
/* 154 */     this.identifiers.remove(identifier);
/* 155 */     this.handles.remove(handle);
/* 156 */     this.lastInvalidateTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void remove(String identifier)
/*     */   {
/* 163 */     Long handle = (Long)this.identifiers.get(identifier);
/* 164 */     if (handle != null) {
/* 165 */       this.identifiers.remove(identifier);
/* 166 */       remove(handle.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Validator get(long validatorHandle)
/*     */   {
/* 174 */     Long handle = new Long(validatorHandle);
/* 175 */     Validator result = (Validator)this.validators.get(handle);
/* 176 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Validator get(String identifier)
/*     */   {
/* 183 */     Long handle = (Long)this.identifiers.get(identifier);
/* 184 */     if (handle != null) {
/* 185 */       return get(handle.longValue());
/*     */     }
/* 187 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIdentifier(long validatorHandle)
/*     */   {
/* 195 */     return (String)this.handles.get(new Long(validatorHandle));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void printOutValidators()
/*     */   {
/* 202 */     Iterator it = this.validators.values().iterator();
/* 203 */     while (it.hasNext()) {
/* 204 */       Validator v = (Validator)it.next();
/* 205 */       System.err.println("[Validator] " + v);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void backup()
/*     */   {
/*     */     try
/*     */     {
/* 214 */       FileOutputStream fos = new FileOutputStream(new File(this.storagePath + "/validators.data"), false);
/* 215 */       ObjectOutputStream oos = new ObjectOutputStream(fos);
/* 216 */       int size = 0;
/* 217 */       for (Iterator iter = this.identifiers.keySet().iterator(); iter.hasNext();) {
/* 218 */         String element = (String)iter.next();
/* 219 */         if (!element.startsWith("TMP")) {
/* 220 */           size++;
/*     */         }
/*     */       }
/* 223 */       oos.writeInt(size);
/* 224 */       for (Iterator iter = this.identifiers.entrySet().iterator(); iter.hasNext();) {
/* 225 */         Map.Entry element = (Map.Entry)iter.next();
/* 226 */         String identifier = (String)element.getKey();
/* 227 */         if (!identifier.startsWith("TMP")) {
/* 228 */           oos.writeObject(identifier);
/* 229 */           oos.writeObject(this.validators.get(element.getValue()));
/*     */         }
/*     */       }
/* 232 */       oos.writeLong(this.lastInvalidateTime);
/* 233 */       oos.writeLong(this.validatorCounter);
/* 234 */       oos.close();
/* 235 */       fos.close();
/*     */     } catch (IOException localIOException) {
/* 237 */       throw new Error("Could not backup validator manager because of IOException");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLastInvalidatedTime()
/*     */   {
/* 246 */     return this.lastInvalidateTime;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/FileBasedValidatorManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */