/*     */ package lsclipse.dialogs;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Set;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Control;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.ProgressBar;
/*     */ import org.eclipse.swt.widgets.Shell;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProgressBarDialog
/*     */ {
/*     */   private Shell shell;
/*     */   private Label lblStep;
/*     */   private Label lblMessage;
/*     */   private ProgressBar pbProg;
/*     */   private Text txtLog;
/*     */   
/*  31 */   public ProgressBarDialog(Shell parentShell, String baseproj, int fetchmethod, String proj2, String svnurl, int svnversion, Set<String> changedFiles) { createDialogArea(parentShell); }
/*     */   
/*     */   public ProgressBarDialog(Shell parentShell) {
/*  34 */     this.shell = new Shell(parentShell, 16777248);
/*  35 */     this.shell.setSize(520, 400);
/*  36 */     this.shell.setText("Running LSDiff...");
/*  37 */     createDialogArea(this.shell);
/*     */   }
/*     */   
/*  40 */   public synchronized void open() { this.shell.open(); }
/*     */   
/*     */ 
/*  43 */   public synchronized void dispose() { this.shell.dispose(); }
/*     */   
/*     */   public synchronized void setStep(int phaseid) {
/*  46 */     String[] phases = { "Preparation", 
/*  47 */       "Extract FB1: Base project facts", 
/*  48 */       "Extract FB2: New project facts", 
/*  49 */       "Compute Difference", 
/*  50 */       "Perform LSDiff", 
/*  51 */       "Cleanup" };
/*  52 */     if ((phaseid < 0) || (phaseid >= phases.length)) return;
/*  53 */     String step = "Step " + (phaseid + 1) + " / " + phases.length + " : " + phases[phaseid] + "\n";
/*     */     try {
/*  55 */       this.lblStep.setText(step);
/*     */     } catch (Throwable t) {
/*  57 */       System.out.println(t.getMessage());
/*     */     }
/*  59 */     this.txtLog.append(step);
/*     */   }
/*     */   
/*  62 */   public synchronized void setMessage(String msg) { this.lblMessage.setText(msg);
/*  63 */     this.txtLog.append(msg);
/*     */   }
/*     */   
/*  66 */   public synchronized void appendLog(String log) { if (this.txtLog.getLineCount() > 300) {
/*  67 */       this.txtLog.getText().substring(1000);
/*     */     }
/*  69 */     this.txtLog.append(log);
/*  70 */     System.out.print(log);
/*     */   }
/*     */   
/*  73 */   public void appendError(String err) { this.txtLog.append(err);
/*  74 */     System.out.print(err);
/*     */   }
/*     */   
/*  77 */   public void setProgressMaxValue(int maxvalue) { this.pbProg.setMaximum(maxvalue); }
/*     */   
/*     */ 
/*  80 */   public void setProgressCurrValue(int currvalue) { this.pbProg.setSelection(currvalue); }
/*     */   
/*     */   private Control createDialogArea(Composite shell) {
/*  83 */     GridLayout layout = new GridLayout();
/*  84 */     layout.numColumns = 1;
/*  85 */     shell.setLayout(layout);
/*     */     
/*  87 */     this.lblStep = new Label(shell, 0);
/*  88 */     this.lblStep.setLayoutData(new GridData(4, 2, true, false));
/*  89 */     this.lblStep.setText("Step 1 / 999");
/*     */     
/*  91 */     this.lblMessage = new Label(shell, 0);
/*  92 */     this.lblMessage.setLayoutData(new GridData(4, 2, true, false));
/*  93 */     this.lblMessage.setText("Idle");
/*     */     
/*  95 */     this.pbProg = new ProgressBar(shell, 65538);
/*  96 */     this.pbProg.setLayoutData(new GridData(4, 2, true, false));
/*  97 */     this.pbProg.setMaximum(1000);
/*  98 */     this.pbProg.setSelection(0);
/*  99 */     this.pbProg.setSelection(256);
/*     */     
/* 101 */     Label lblSeparator = new Label(shell, 258);
/* 102 */     lblSeparator.setLayoutData(new GridData(4, 2, true, false));
/*     */     
/* 104 */     this.txtLog = new Text(shell, 2818);
/*     */     
/*     */ 
/*     */ 
/* 108 */     this.txtLog.setLayoutData(new GridData(4, 4, true, true));
/* 109 */     this.txtLog.setEditable(false);
/* 110 */     this.txtLog.setBackground(new Color(shell.getDisplay(), 10, 10, 10));
/* 111 */     this.txtLog.setForeground(new Color(shell.getDisplay(), 200, 200, 200));
/*     */     
/* 113 */     shell.layout();
/*     */     
/* 115 */     return shell;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/dialogs/ProgressBarDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */