/*    */ package lsclipse;
/*    */ 
/*    */ import org.eclipse.jface.resource.ImageDescriptor;
/*    */ import org.eclipse.ui.plugin.AbstractUIPlugin;
/*    */ import org.osgi.framework.BundleContext;
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
/*    */ public class LSclipse
/*    */   extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "LSclipse";
/*    */   private static LSclipse plugin;
/*    */   
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 30 */     super.start(context);
/* 31 */     plugin = this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 39 */     plugin = null;
/* 40 */     super.stop(context);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static LSclipse getDefault()
/*    */   {
/* 49 */     return plugin;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ImageDescriptor getImageDescriptor(String path)
/*    */   {
/* 60 */     return imageDescriptorFromPlugin("LSclipse", path);
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/LSclipse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */