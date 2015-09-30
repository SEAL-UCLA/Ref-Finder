/*    */ package tyRuBa.util.pager;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
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
/*    */ public class URLResourceID
/*    */   extends Pager.ResourceId
/*    */ {
/*    */   private URL actualLocation;
/*    */   
/*    */   public URLResourceID(URLLocation location, String relativeID)
/*    */   {
/*    */     try
/*    */     {
/* 27 */       this.actualLocation = new URL(location.getBase(), relativeID);
/*    */     } catch (MalformedURLException localMalformedURLException) {
/* 29 */       throw new Error("Malformed URL for URLResource: " + location + " ::: " + relativeID);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean equals(Object other)
/*    */   {
/* 37 */     if ((other instanceof URLResourceID)) {
/* 38 */       URLResourceID id_other = (URLResourceID)other;
/* 39 */       return id_other.actualLocation.equals(this.actualLocation);
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 49 */     return 29 * this.actualLocation.hashCode();
/*    */   }
/*    */   
/*    */   public InputStream readResource() throws IOException
/*    */   {
/* 54 */     return this.actualLocation.openStream();
/*    */   }
/*    */   
/*    */   public OutputStream writeResource()
/*    */     throws IOException
/*    */   {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void removeResource() {}
/*    */   
/*    */ 
/*    */   public boolean resourceExists()
/*    */   {
/*    */     try
/*    */     {
/* 71 */       this.actualLocation.openStream().close();
/* 72 */       return true;
/*    */     } catch (IOException localIOException) {}
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/URLResourceID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */