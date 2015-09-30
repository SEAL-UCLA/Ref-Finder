/*    */ package tyRuBa.util.pager;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileResourceID
/*    */   extends Pager.ResourceId
/*    */ {
/*    */   private String relativeId;
/*    */   private FileLocation base;
/* 29 */   private File lazyActualFile = null;
/*    */   
/*    */   public FileResourceID(FileLocation location, String relativeID)
/*    */   {
/* 33 */     this.base = location;
/* 34 */     this.relativeId = relativeID;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return "FileResourceID(" + this.base + "/" + this.relativeId + ")";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 45 */     if ((other instanceof FileResourceID)) {
/* 46 */       FileResourceID id_other = (FileResourceID)other;
/* 47 */       return (id_other.relativeId.equals(this.relativeId)) && (id_other.base.equals(this.base));
/*    */     }
/* 49 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 57 */     return 23 * this.base.hashCode() + 47 * this.relativeId.hashCode();
/*    */   }
/*    */   
/*    */   public InputStream readResource() throws IOException
/*    */   {
/* 62 */     if (this.lazyActualFile == null) {
/* 63 */       this.lazyActualFile = new File(this.base.getBase(), this.relativeId);
/*    */     }
/* 65 */     return new FileInputStream(this.lazyActualFile);
/*    */   }
/*    */   
/*    */   public OutputStream writeResource() throws IOException
/*    */   {
/* 70 */     File baseFile = this.base.getBase();
/* 71 */     if (!baseFile.exists()) {
/* 72 */       baseFile.mkdirs();
/*    */     }
/* 74 */     if (this.lazyActualFile == null) {
/* 75 */       this.lazyActualFile = new File(this.base.getBase(), this.relativeId);
/*    */     }
/* 77 */     return new FileOutputStream(this.lazyActualFile);
/*    */   }
/*    */   
/*    */   public void removeResource()
/*    */   {
/* 82 */     if (this.lazyActualFile == null) {
/* 83 */       this.lazyActualFile = new File(this.base.getBase(), this.relativeId);
/*    */     }
/* 85 */     this.lazyActualFile.delete();
/*    */   }
/*    */   
/*    */   public boolean resourceExists()
/*    */   {
/* 90 */     if (this.lazyActualFile == null) {
/* 91 */       this.lazyActualFile = new File(this.base.getBase(), this.relativeId);
/*    */     }
/* 93 */     return this.lazyActualFile.exists();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/FileResourceID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */