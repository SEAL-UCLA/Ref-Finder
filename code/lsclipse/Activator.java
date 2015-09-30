/*    */ package lsclipse;
/*    */ 
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
/*    */ public class Activator
/*    */   extends AbstractUIPlugin
/*    */ {
/*    */   public static final String PLUGIN_ID = "lsclipse";
/*    */   private static Activator plugin;
/*    */   
/*    */   public void start(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 28 */     super.start(context);
/* 29 */     plugin = this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void stop(BundleContext context)
/*    */     throws Exception
/*    */   {
/* 37 */     plugin = null;
/* 38 */     super.stop(context);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Activator getDefault()
/*    */   {
/* 47 */     return plugin;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/Activator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */