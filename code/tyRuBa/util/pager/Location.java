/*    */ package tyRuBa.util.pager;
/*    */ 
/*    */ import java.io.File;
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
/*    */ 
/*    */ public abstract class Location
/*    */ {
/*    */   public abstract Pager.ResourceId getResourceID(String paramString);
/*    */   
/*    */   public Location make(URL theBaseURL)
/*    */   {
/* 23 */     return new URLLocation(theBaseURL);
/*    */   }
/*    */   
/*    */   public Location make(File theBasePath)
/*    */   {
/* 28 */     return new FileLocation(theBasePath);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/pager/Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */