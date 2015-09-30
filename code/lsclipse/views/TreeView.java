/*     */ package lsclipse.views;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import lsclipse.LSDiffRunner;
/*     */ import lsclipse.TopologicalSort;
/*     */ import lsclipse.dialogs.ProgressBarDialog;
/*     */ import lsclipse.dialogs.SelectProjectDialog;
/*     */ import metapackage.MetaInfo;
/*     */ import org.eclipse.compare.CompareConfiguration;
/*     */ import org.eclipse.compare.CompareEditorInput;
/*     */ import org.eclipse.compare.CompareUI;
/*     */ import org.eclipse.compare.IStreamContentAccessor;
/*     */ import org.eclipse.compare.ITypedElement;
/*     */ import org.eclipse.compare.structuremergeviewer.Differencer;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.NullProgressMonitor;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.action.IToolBarManager;
/*     */ import org.eclipse.swt.events.MouseEvent;
/*     */ import org.eclipse.swt.events.MouseListener;
/*     */ import org.eclipse.swt.graphics.Image;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.ui.IActionBars;
/*     */ import org.eclipse.ui.ISharedImages;
/*     */ import org.eclipse.ui.IViewSite;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.part.ViewPart;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeView
/*     */   extends ViewPart
/*     */ {
/*     */   public static final String ID = "lsclipse.views.TreeView";
/*     */   private org.eclipse.swt.widgets.List viewer;
/*     */   private org.eclipse.swt.widgets.List list;
/*     */   private Action doubleClickTreeAction;
/*     */   private Action doubleClickListAction;
/*     */   private Action selectAction;
/*     */   private Composite parent;
/*     */   private Vector<Node> nodeList;
/*     */   private Map<String, Node> allNodes;
/*     */   private HashMap<String, Node> hashNode;
/*     */   HashMap<String, Node> strNodeRelation;
/*     */   GridData layoutData1;
/*  85 */   ArrayList<EditorInput> listDiffs = new ArrayList();
/*  86 */   IProject baseproj = null;
/*  87 */   IProject newproj = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public TreeView()
/*     */   {
/*  93 */     this.nodeList = new Vector();
/*  94 */     this.hashNode = new HashMap();
/*  95 */     this.allNodes = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createPartControl(Composite parent)
/*     */   {
/* 103 */     GridLayout layout = new GridLayout();
/* 104 */     layout.numColumns = 2;
/* 105 */     parent.setLayout(layout);
/*     */     
/* 107 */     this.layoutData1 = new GridData(2);
/* 108 */     this.layoutData1.grabExcessHorizontalSpace = true;
/* 109 */     this.layoutData1.grabExcessVerticalSpace = true;
/* 110 */     this.layoutData1.horizontalAlignment = 4;
/* 111 */     this.layoutData1.verticalAlignment = 4;
/* 112 */     this.layoutData1.exclude = false;
/*     */     
/* 114 */     this.parent = parent;
/* 115 */     this.viewer = new org.eclipse.swt.widgets.List(this.parent, 516);
/* 116 */     this.list = new org.eclipse.swt.widgets.List(this.parent, 516);
/* 117 */     this.viewer.setLayoutData(this.layoutData1);
/* 118 */     this.list.setLayoutData(this.layoutData1);
/*     */     
/* 120 */     parent.layout();
/*     */     
/* 122 */     makeActions();
/* 123 */     hookDoubleClickAction();
/* 124 */     contributeToActionBars();
/*     */     
/* 126 */     IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
/* 127 */     mgr.add(this.selectAction);
/*     */   }
/*     */   
/*     */   private void contributeToActionBars() {
/* 131 */     IActionBars bars = getViewSite().getActionBars();
/* 132 */     fillLocalPullDown(bars.getMenuManager());
/* 133 */     fillLocalToolBar(bars.getToolBarManager());
/*     */   }
/*     */   
/*     */   private void fillLocalPullDown(IMenuManager manager) {}
/*     */   
/*     */   private void fillLocalToolBar(IToolBarManager manager) {}
/*     */   
/*     */   class EditorInput extends CompareEditorInput
/*     */   {
/*     */     String base_;
/*     */     String mod_;
/*     */     
/*     */     public EditorInput(CompareConfiguration configuration) {
/* 146 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public void setBase(InputStream inputStream)
/*     */     {
/*     */       try
/*     */       {
/* 154 */         this.base_ = convertToString(inputStream);
/*     */       }
/*     */       catch (IOException e) {
/* 157 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public void setBase(String s) {
/* 162 */       this.base_ = s;
/*     */     }
/*     */     
/*     */     private String convertToString(InputStream is) throws IOException {
/* 166 */       char[] buffer = new char[65536];
/* 167 */       StringBuilder out = new StringBuilder();
/* 168 */       Reader in = new InputStreamReader(is, "UTF-8");
/*     */       int read;
/* 170 */       while ((read = in.read(buffer, 0, buffer.length)) >= 0) { int read;
/* 171 */         if (read > 0) {
/* 172 */           out.append(buffer, 0, read);
/*     */         }
/*     */       }
/* 175 */       return out.toString();
/*     */     }
/*     */     
/*     */     public void setMod(InputStream inputStream) {
/*     */       try {
/* 180 */         this.mod_ = convertToString(inputStream);
/*     */       }
/*     */       catch (IOException e) {
/* 183 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public void setMod(String s) {
/* 188 */       this.mod_ = s;
/*     */     }
/*     */     
/*     */     public Object prepareInput(IProgressMonitor monitor)
/*     */       throws InvocationTargetException, InterruptedException
/*     */     {
/* 194 */       Differencer d = new Differencer();
/* 195 */       Object diff = d.findDifferences(false, new NullProgressMonitor(), 
/* 196 */         null, null, new Input(this.base_), new Input(this.mod_));
/* 197 */       return diff;
/*     */     }
/*     */     
/*     */     class Input implements ITypedElement, IStreamContentAccessor {
/*     */       String fContent;
/*     */       
/*     */       public Input(String s) {
/* 204 */         this.fContent = s;
/*     */       }
/*     */       
/*     */ 
/*     */       public String getName()
/*     */       {
/* 210 */         return "name";
/*     */       }
/*     */       
/*     */ 
/*     */       public String getType()
/*     */       {
/* 216 */         return "java";
/*     */       }
/*     */       
/*     */       public InputStream getContents()
/*     */         throws CoreException
/*     */       {
/* 222 */         return new ByteArrayInputStream(this.fContent.getBytes());
/*     */       }
/*     */       
/*     */ 
/*     */       public Image getImage()
/*     */       {
/* 228 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void makeActions()
/*     */   {
/* 235 */     this.doubleClickListAction = new Action() {
/*     */       public void run() {
/* 237 */         int selind = TreeView.this.list.getSelectionIndex();
/* 238 */         if (selind >= TreeView.this.listDiffs.size())
/* 239 */           return;
/* 240 */         TreeView.EditorInput file = (TreeView.EditorInput)TreeView.this.listDiffs.get(selind);
/* 241 */         if (file == null) {
/* 242 */           return;
/*     */         }
/* 244 */         CompareUI.openCompareEditor(file);
/*     */       }
/*     */       
/*     */ 
/* 248 */     };
/* 249 */     this.doubleClickTreeAction = new Action()
/*     */     {
/*     */       public void run()
/*     */       {
/* 253 */         TreeView.this.list.removeAll();
/* 254 */         TreeView.this.listDiffs.clear();
/*     */         
/*     */ 
/*     */ 
/* 258 */         int index = TreeView.this.viewer.getSelectionIndex();
/* 259 */         if ((index < 0) || (index >= TreeView.this.nodeList.size())) {
/* 260 */           return;
/*     */         }
/* 262 */         Node node = (Node)TreeView.this.nodeList.get(index);
/*     */         
/* 264 */         TreeView.this.list.add(node.getName());
/* 265 */         TreeView.this.listDiffs.add(null);
/* 266 */         TreeView.this.list.add(node.params);
/* 267 */         TreeView.this.listDiffs.add(null);
/*     */         
/* 269 */         TreeView.this.list.add(" ");
/* 270 */         TreeView.this.listDiffs.add(null);
/*     */         
/* 272 */         int numtabs = 0;
/*     */         
/* 274 */         for (String statement : node.getDependents()) {
/* 275 */           StringBuilder output = new StringBuilder();
/* 276 */           if (statement.equals(")")) {
/* 277 */             numtabs--;
/*     */           }
/* 279 */           for (int i = 0; i < numtabs; i++) {
/* 280 */             output.append("\t");
/*     */           }
/* 282 */           if (statement.equals("(")) {
/* 283 */             output.append("(");
/* 284 */             numtabs++;
/*     */           } else {
/* 286 */             output.append(statement);
/*     */           }
/* 288 */           TreeView.this.list.add(output.toString());
/* 289 */           TreeView.this.listDiffs.add(null);
/*     */         }
/*     */         
/* 292 */         if (!node.oldFacts.isEmpty()) {
/* 293 */           TreeView.this.list.add("");
/* 294 */           TreeView.this.listDiffs.add(null);
/* 295 */           TreeView.this.list.add("Compare:");
/* 296 */           TreeView.this.listDiffs.add(null);
/* 297 */           for (String display : node.oldFacts.keySet()) {
/* 298 */             IJavaElement filenameBase = (IJavaElement)node.oldFacts.get(display);
/* 299 */             IJavaElement filenameMod = (IJavaElement)node.newFacts.get(display);
/* 300 */             TreeView.this.list.add(display);
/* 301 */             TreeView.EditorInput ei = new TreeView.EditorInput(TreeView.this, 
/* 302 */               new CompareConfiguration());
/*     */             try {
/* 304 */               ei.setBase(
/* 305 */                 ((IFile)filenameBase.getCorrespondingResource()).getContents());
/*     */             } catch (Exception localException1) {
/* 307 */               ei.setBase("");
/*     */             }
/*     */             try {
/* 310 */               ei.setMod(
/* 311 */                 ((IFile)filenameMod.getCorrespondingResource()).getContents());
/*     */             } catch (Exception localException2) {
/* 313 */               ei.setMod("");
/*     */             }
/* 315 */             TreeView.this.listDiffs.add(ei);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 321 */     };
/* 322 */     this.selectAction = new Action("Select version...")
/*     */     {
/*     */       public void run() {
/* 325 */         SelectProjectDialog seldiag = new SelectProjectDialog(
/* 326 */           TreeView.this.parent.getShell());
/* 327 */         int returncode = seldiag.open();
/* 328 */         if (returncode > 0) {
/* 329 */           return;
/*     */         }
/* 331 */         long start = System.currentTimeMillis();
/*     */         
/*     */ 
/* 334 */         TreeView.this.baseproj = ResourcesPlugin.getWorkspace().getRoot()
/* 335 */           .getProject(seldiag.getProj1());
/* 336 */         TreeView.this.newproj = ResourcesPlugin.getWorkspace().getRoot()
/* 337 */           .getProject(seldiag.getProj2());
/*     */         
/*     */ 
/* 340 */         ProgressBarDialog pbdiag = new ProgressBarDialog(
/* 341 */           TreeView.this.parent.getShell());
/* 342 */         pbdiag.open();
/* 343 */         pbdiag.setStep(0);
/*     */         
/*     */ 
/* 346 */         if (new LSDiffRunner().doFactExtractionForRefFinder(
/* 347 */           seldiag.getProj1(), seldiag.getProj2(), pbdiag)) {
/* 348 */           TreeView.this.refreshTree();
/*     */         }
/*     */         else {
/* 351 */           System.out.println("Something went wrong - fact extraction failed");
/*     */         }
/* 353 */         pbdiag.setStep(5);
/* 354 */         pbdiag.setMessage("Cleaning up... ");
/* 355 */         pbdiag.appendLog("OK\n");
/*     */         
/* 357 */         pbdiag.dispose();
/* 358 */         long end = System.currentTimeMillis();
/* 359 */         System.out.println("Total time: " + (end - start));
/*     */       }
/* 361 */     };
/* 362 */     this.selectAction.setImageDescriptor(PlatformUI.getWorkbench()
/* 363 */       .getSharedImages()
/* 364 */       .getImageDescriptor("IMG_OBJ_FOLDER"));
/*     */   }
/*     */   
/*     */   public void refreshTree() {
/* 368 */     this.list.removeAll();
/* 369 */     this.nodeList.clear();
/* 370 */     this.viewer.removeAll();
/* 371 */     System.out.println(this.baseproj.toString());
/* 372 */     System.out.println(this.newproj.toString());
/*     */     
/* 374 */     TopologicalSort tSort = new TopologicalSort();
/*     */     
/* 376 */     long beforetime = System.currentTimeMillis();
/* 377 */     tSort.sort(MetaInfo.refsOnlyFile);
/* 378 */     long aftertime = System.currentTimeMillis();
/*     */     
/* 380 */     Map<String, java.util.List<String>> dependentMap = tSort.dependents;
/*     */     
/* 382 */     Set<String> allchildren = new HashSet();
/*     */     
/* 384 */     for (java.util.List<String> s : dependentMap.values()) {
/* 385 */       allchildren.addAll(s);
/*     */     }
/*     */     
/* 388 */     this.strNodeRelation = new HashMap();
/* 389 */     Set<String> parents = new HashSet();
/*     */     
/*     */ 
/* 392 */     Iterator localIterator2 = dependentMap.entrySet().iterator();
/*     */     Node temp;
/* 391 */     while (localIterator2.hasNext()) {
/* 392 */       Object queryEntry = (Map.Entry)localIterator2.next();
/* 393 */       String filledQuery = (String)((Map.Entry)queryEntry).getKey();
/* 394 */       temp = makeNode(filledQuery, (java.util.List)((Map.Entry)queryEntry).getValue(), this.baseproj, 
/* 395 */         this.newproj);
/* 396 */       this.hashNode.put("[" + temp.getName() + "]", temp);
/* 397 */       this.allNodes.put(filledQuery, temp);
/* 398 */       System.out.println(filledQuery);
/* 399 */       this.strNodeRelation.put(filledQuery, temp);
/*     */       
/* 401 */       this.nodeList.add(temp);
/* 402 */       this.viewer.add(temp.getName());
/* 403 */       parents.add(filledQuery);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 408 */     System.out.println("\nTotal time for inference(ms): " + (
/* 409 */       aftertime - beforetime));
/* 410 */     Object nodes = tSort.getGraph();
/* 411 */     int totalCount = 0;
/* 412 */     for (lsclipse.Node node : (Set)nodes) {
/* 413 */       if (node.numFound() > 0) {
/* 414 */         totalCount += node.numFound();
/* 415 */         System.out.print(node.toString() + ", ");
/*     */       }
/*     */     }
/* 418 */     System.out.println("\nFor a total of " + totalCount + 
/* 419 */       " refactorings found.");
/*     */   }
/*     */   
/*     */   private String getName(String filledQuery) {
/* 423 */     int parenthIndex = filledQuery.indexOf('(');
/* 424 */     return filledQuery.substring(0, parenthIndex);
/*     */   }
/*     */   
/*     */   private Node makeNode(String filledQuery, java.util.List<String> children, IProject baseProject, IProject newProject)
/*     */   {
/* 429 */     String name = getName(filledQuery);
/* 430 */     String nicename = name.replace('_', ' ');
/* 431 */     nicename = nicename.substring(0, 1).toUpperCase() + 
/* 432 */       nicename.substring(1);
/* 433 */     int nameIndex = filledQuery.indexOf(name);
/*     */     
/* 435 */     Node temp = new Node(nicename, null);
/* 436 */     temp.setDependents(children);
/* 437 */     temp.setFile("test.java.txt");
/* 438 */     temp.setProjectName("LSclipse");
/* 439 */     temp.params = filledQuery.substring(nameIndex + name.length());
/* 440 */     if (temp.params.length() > 4) {
/* 441 */       String[] params = temp.params
/* 442 */         .substring(2, temp.params.length() - 2).split("\",\"");
/* 443 */       ArrayList<String> fields = new ArrayList();
/* 444 */       ArrayList<String> methods = new ArrayList();
/* 445 */       ArrayList<String> classes = new ArrayList();
/*     */       String[] arrayOfString1;
/* 447 */       int j = (arrayOfString1 = params).length; for (int i = 0; i < j; i++) { String s = arrayOfString1[i];
/* 448 */         if ((!s.contains("{")) && 
/* 449 */           (!s.contains("}")) && 
/* 450 */           (!s.contains(";")) && 
/* 451 */           (!s.contains("=")) && 
/* 452 */           (s.contains("%")) && (
/* 453 */           (!s.contains("(")) || (s.indexOf("(") >= s.indexOf("%"))))
/*     */         {
/*     */ 
/*     */ 
/* 457 */           if ((s.contains("(")) && (s.contains(")")))
/*     */           {
/* 459 */             methods.add(s);
/* 460 */           } else if (s.contains("#")) {
/* 461 */             fields.add(s);
/* 462 */           } else if (s.contains("%.")) {
/* 463 */             classes.add(s);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 468 */       for (String s : methods) {
/* 469 */         int indhex = s.indexOf("#");
/* 470 */         String qualifiedClassName = s.substring(0, indhex);
/* 471 */         temp.oldFacts.put(qualifiedClassName, 
/* 472 */           (IJavaElement)LSDiffRunner.getOldTypeToFileMap().get(qualifiedClassName));
/* 473 */         temp.newFacts.put(qualifiedClassName, 
/* 474 */           (IJavaElement)LSDiffRunner.getNewTypeToFileMap().get(qualifiedClassName));
/*     */       }
/* 476 */       for (String s : fields) {
/* 477 */         int indhex = s.indexOf("#");
/* 478 */         String qualifiedClassName = s.substring(0, indhex);
/* 479 */         temp.oldFacts.put(qualifiedClassName, 
/* 480 */           (IJavaElement)LSDiffRunner.getOldTypeToFileMap().get(qualifiedClassName));
/* 481 */         temp.newFacts.put(qualifiedClassName, 
/* 482 */           (IJavaElement)LSDiffRunner.getNewTypeToFileMap().get(qualifiedClassName));
/*     */       }
/* 484 */       for (String s : classes) {
/* 485 */         temp.oldFacts.put(s, (IJavaElement)LSDiffRunner.getOldTypeToFileMap().get(s));
/* 486 */         temp.newFacts.put(s, (IJavaElement)LSDiffRunner.getNewTypeToFileMap().get(s));
/*     */       }
/*     */     }
/* 489 */     return temp;
/*     */   }
/*     */   
/*     */   private void hookDoubleClickAction() {
/* 493 */     this.viewer.addMouseListener(new MouseListener() {
/*     */       public void mouseDoubleClick(MouseEvent arg0) {
/* 495 */         TreeView.this.doubleClickTreeAction.run();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       public void mouseDown(MouseEvent arg0) {}
/*     */       
/*     */ 
/*     */       public void mouseUp(MouseEvent arg0) {}
/* 504 */     });
/* 505 */     this.list.addMouseListener(new MouseListener() {
/*     */       public void mouseDoubleClick(MouseEvent arg0) {
/* 507 */         TreeView.this.doubleClickListAction.run();
/*     */       }
/*     */       
/*     */       public void mouseDown(MouseEvent arg0) {}
/*     */       
/*     */       public void mouseUp(MouseEvent arg0) {}
/*     */     });
/*     */   }
/*     */   
/*     */   public void setFocus() {}
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/views/TreeView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */