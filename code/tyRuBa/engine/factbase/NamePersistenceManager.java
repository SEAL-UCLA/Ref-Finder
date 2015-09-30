/*     */ package tyRuBa.engine.factbase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class NamePersistenceManager
/*     */   implements Serializable
/*     */ {
/*     */   private Map nameMap;
/*     */   private String storagePath;
/*     */   
/*     */   public NamePersistenceManager(String storagePath)
/*     */   {
/*  36 */     File nameFile = new File(storagePath + "/names.data");
/*  37 */     this.storagePath = storagePath;
/*  38 */     if (nameFile.exists()) {
/*     */       try {
/*  40 */         FileInputStream fis = new FileInputStream(nameFile);
/*  41 */         ObjectInputStream ois = new ObjectInputStream(fis);
/*     */         
/*  43 */         this.nameMap = ((HashMap)ois.readObject());
/*     */         
/*  45 */         ois.close();
/*  46 */         fis.close();
/*     */       } catch (IOException localIOException) {
/*  48 */         throw new Error("Could not load names because of IOException");
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  50 */         throw new Error("Could not load names because of ClassNotFoundException");
/*     */       }
/*     */     } else {
/*  53 */       this.nameMap = new HashMap();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamePersistenceManager(URL storageLocation)
/*     */   {
/*  62 */     this.storagePath = storageLocation.toString();
/*     */     try {
/*  64 */       ObjectInputStream ois = new ObjectInputStream(storageLocation.openStream());
/*  65 */       this.nameMap = ((HashMap)ois.readObject());
/*     */       
/*  67 */       ois.close();
/*     */     } catch (IOException e) {
/*  69 */       System.err.println(this.storagePath);
/*  70 */       throw new Error("Could not load names because of IOException", e);
/*     */     } catch (ClassNotFoundException e) {
/*  72 */       throw new Error("Could not load names because of ClassNotFoundException", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPersistentName(String tyRuBaName)
/*     */   {
/*  81 */     String result = (String)this.nameMap.get(tyRuBaName);
/*  82 */     if (result == null) {
/*  83 */       int nextNum = this.nameMap.size();
/*  84 */       result = String.valueOf(nextNum);
/*  85 */       this.nameMap.put(tyRuBaName, result);
/*     */     }
/*  87 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void backup()
/*     */   {
/*  95 */     File nameFile = new File(this.storagePath + "/names.data");
/*     */     try {
/*  97 */       FileOutputStream fos = new FileOutputStream(nameFile, false);
/*  98 */       ObjectOutputStream oos = new ObjectOutputStream(fos);
/*     */       
/* 100 */       oos.writeObject(this.nameMap);
/*     */       
/* 102 */       oos.close();
/* 103 */       fos.close();
/*     */     } catch (IOException localIOException) {
/* 105 */       throw new Error("Could not save names because of IOException");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/factbase/NamePersistenceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */