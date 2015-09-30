/*    */ package tyRuBa.util.pager;
/*    */ 
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
/*    */ 
/*    */ public class URLLocation
/*    */   extends Location
/*    */ {
/* 19 */   URL base = null;
/*    */   
/*    */   public URLLocation(String theBaseURL) throws MalformedURLException
/*    */   {
/* 23 */     this(new URL(theBaseURL));
/*    */   }
/*    */   
/*    */   public URLLocation(URL theBaseURL)
/*    */   {
/* 28 */     this.base = theBaseURL;
/*    */   }
/*    */   
/*    */   public URL getBase()
/*    */   {
/* 33 */     return this.base;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Pager.ResourceId getResourceID(String relativeID)
/*    */   {
/* 40 */     return new URLResourceID(this, relativeID);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/URLLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */