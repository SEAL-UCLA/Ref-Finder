/*    */ package lsclipse.dialogs;
/*    */ 
/*    */ import org.eclipse.core.resources.IProject;
/*    */ import org.eclipse.core.resources.IWorkspace;
/*    */ import org.eclipse.core.resources.IWorkspaceRoot;
/*    */ import org.eclipse.core.resources.ResourcesPlugin;
/*    */ import org.eclipse.jface.dialogs.Dialog;
/*    */ import org.eclipse.swt.layout.GridData;
/*    */ import org.eclipse.swt.layout.GridLayout;
/*    */ import org.eclipse.swt.widgets.Combo;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Label;
/*    */ import org.eclipse.swt.widgets.Shell;
/*    */ 
/*    */ public class SelectProjectDialog extends Dialog
/*    */ {
/* 17 */   private String proj1 = "";
/* 18 */   private String proj2 = "";
/*    */   private Combo cmbProj1;
/*    */   private Combo cmbProj2;
/*    */   
/*    */   public SelectProjectDialog(Shell parentShell)
/*    */   {
/* 24 */     super(parentShell);
/*    */   }
/*    */   
/*    */   public String getProj1() {
/* 28 */     return this.proj1;
/*    */   }
/*    */   
/*    */   public String getProj2() {
/* 32 */     return this.proj2;
/*    */   }
/*    */   
/*    */   public void okPressed() {
/* 36 */     this.proj1 = this.cmbProj1.getText();
/* 37 */     this.proj2 = this.cmbProj2.getText();
/*    */     
/* 39 */     super.okPressed();
/*    */   }
/*    */   
/*    */   protected org.eclipse.swt.widgets.Control createDialogArea(Composite parent) {
/* 43 */     getShell().setText("Select Versions");
/*    */     
/*    */ 
/* 46 */     GridLayout layout = new GridLayout();
/* 47 */     layout.numColumns = 1;
/* 48 */     parent.setLayout(layout);
/*    */     
/*    */ 
/* 51 */     GridData ldtDefault = new GridData(2);
/* 52 */     ldtDefault.grabExcessHorizontalSpace = true;
/* 53 */     ldtDefault.grabExcessVerticalSpace = true;
/* 54 */     ldtDefault.horizontalAlignment = 4;
/* 55 */     ldtDefault.verticalAlignment = 4;
/* 56 */     ldtDefault.exclude = false;
/*    */     
/* 58 */     GridLayout panelLayout = new GridLayout();
/* 59 */     panelLayout.numColumns = 1;
/*    */     
/* 61 */     Composite leftPanel = new Composite(parent, 0);
/* 62 */     leftPanel.setLayoutData(new GridData(1808));
/*    */     
/*    */ 
/* 65 */     leftPanel.setLayout(panelLayout);
/*    */     
/* 67 */     Label base = new Label(leftPanel, 0);
/* 68 */     base.setText("Base Version:");
/*    */     
/*    */ 
/* 71 */     this.cmbProj1 = new Combo(leftPanel, 8);
/* 72 */     this.cmbProj1.setLayoutData(ldtDefault);
/*    */     
/* 74 */     Label changed = new Label(leftPanel, 0);
/* 75 */     changed.setText("Changed Version:");
/*    */     
/*    */ 
/* 78 */     this.cmbProj2 = new Combo(leftPanel, 8);
/* 79 */     this.cmbProj2.setLayoutData(ldtDefault);
/*    */     
/*    */     IProject[] arrayOfIProject;
/*    */     
/* 83 */     int j = (arrayOfIProject = ResourcesPlugin.getWorkspace().getRoot().getProjects()).length; for (int i = 0; i < j; i++) {
/* 83 */       IProject proj = arrayOfIProject[i];
/* 84 */       this.cmbProj1.add(proj.getName());
/* 85 */       this.cmbProj2.add(proj.getName());
/*    */     }
/*    */     
/* 88 */     return parent;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/dialogs/SelectProjectDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */