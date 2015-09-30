/*    */ package tyRuBa.util.pager;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class FileLocation
/*    */   extends Location
/*    */ {
/* 18 */   File base = null;
/*    */   
/*    */   int myHashCode;
/*    */   
/*    */ 
/*    */   public FileLocation(File theBasePath)
/*    */   {
/* 25 */     this.base = theBasePath;
/* 26 */     this.myHashCode = this.base.hashCode();
/*    */   }
/*    */   
/*    */   public FileLocation(String filename)
/*    */   {
/* 31 */     this(new File(filename));
/*    */   }
/*    */   
/*    */   public File getBase()
/*    */   {
/* 36 */     return this.base;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 43 */     if ((other instanceof FileLocation)) {
/* 44 */       FileLocation flOther = (FileLocation)other;
/* 45 */       return flOther.base.equals(this.base);
/*    */     }
/* 47 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 55 */     return this.myHashCode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Pager.ResourceId getResourceID(String relativeID)
/*    */   {
/* 62 */     return new FileResourceID(this, relativeID);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     return this.base.toString();
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/pager/FileLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */