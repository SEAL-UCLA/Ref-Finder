/*     */ package lsclipse.views;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ import metapackage.MetaInfo;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ import org.eclipse.jface.action.Action;
/*     */ import org.eclipse.jface.action.IMenuManager;
/*     */ import org.eclipse.jface.action.IToolBarManager;
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
/*  85 */   ArrayList<TreeView.EditorInput> listDiffs = new ArrayList();
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
/*     */   private void fillLocalPullDown(IMenuManager manager) {}
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
/*     */   private void fillLocalToolBar(IToolBarManager manager) {}
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
/*     */   private void makeActions()
/*     */   {
/* 235 */     this.doubleClickListAction = new TreeView.1(this);
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
/* 249 */     this.doubleClickTreeAction = new TreeView.2(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 322 */     this.selectAction = new TreeView.3(this, "Select version...");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 493 */     this.viewer.addMouseListener(new TreeView.4(this));
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
/* 505 */     this.list.addMouseListener(new TreeView.5(this));
/*     */   }
/*     */   
/*     */   public void setFocus() {}
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/views/TreeView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */