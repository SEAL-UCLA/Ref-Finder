/*     */ package lsclipse.views;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   private String nodeName;
/*     */   private Vector<Node> children;
/*     */   private Node parent;
/*     */   private String message;
/*     */   private String fileName;
/*     */   private String projectName;
/*     */   private String baseProjectName;
/*     */   private String newProjectName;
/*     */   private String basePath;
/*     */   private String newPath;
/*     */   private String basePackageName;
/*     */   private String newPackageName;
/*     */   private String refactoring;
/*     */   private boolean isParent;
/*     */   private List<String> dependents_;
/*     */   public String params;
/*  29 */   public Map<String, IJavaElement> oldFacts = new HashMap();
/*  30 */   public Map<String, IJavaElement> newFacts = new HashMap();
/*     */   
/*     */   public Node(String name, Node p)
/*     */   {
/*  34 */     this.children = new Vector();
/*  35 */     this.nodeName = name;
/*  36 */     this.isParent = false;
/*  37 */     this.basePackageName = "";
/*  38 */     this.newPackageName = "";
/*  39 */     if (p != null)
/*  40 */       p.addChild(this);
/*     */   }
/*     */   
/*     */   public void setDependents(List<String> dependents_) {
/*  44 */     this.dependents_ = dependents_;
/*     */   }
/*     */   
/*     */   public List<String> getDependents() {
/*  48 */     return this.dependents_;
/*     */   }
/*     */   
/*     */   public void setParentStatus(boolean item)
/*     */   {
/*  53 */     this.isParent = true;
/*     */   }
/*     */   
/*     */   public boolean isParent()
/*     */   {
/*  58 */     return this.isParent;
/*     */   }
/*     */   
/*     */   public void setProjectName(String item) {
/*  62 */     this.projectName = item;
/*     */   }
/*     */   
/*  65 */   public String getProjectName() { return this.projectName; }
/*     */   
/*     */   public void setBasePackageName(String item) {
/*  68 */     this.basePackageName = item;
/*     */   }
/*     */   
/*  71 */   public String getBasePackageName() { return this.basePackageName; }
/*     */   
/*     */   public void setRefactoring(String item) {
/*  74 */     this.refactoring = item;
/*     */   }
/*     */   
/*  77 */   public String getRefactoring() { return this.refactoring; }
/*     */   
/*     */   public void setNewPackageName(String item) {
/*  80 */     this.newPackageName = item;
/*     */   }
/*     */   
/*  83 */   public String getNewPackageName() { return this.newPackageName; }
/*     */   
/*     */   public void setBaseProjectName(String item) {
/*  86 */     this.baseProjectName = item;
/*     */   }
/*     */   
/*  89 */   public String getBaseProjectName() { return this.baseProjectName; }
/*     */   
/*     */   public void setNewProjectName(String item) {
/*  92 */     this.newProjectName = item;
/*     */   }
/*     */   
/*  95 */   public String getNewProjectName() { return this.newProjectName; }
/*     */   
/*     */   public void setBasePath(String basePath1)
/*     */   {
/*  99 */     this.basePath = basePath1;
/*     */   }
/*     */   
/*     */   public String getBasePath()
/*     */   {
/* 104 */     return this.basePath;
/*     */   }
/*     */   
/*     */   public void setNewPath(String newPath1)
/*     */   {
/* 109 */     this.newPath = newPath1;
/*     */   }
/*     */   
/*     */   public String getNewPath()
/*     */   {
/* 114 */     return this.newPath;
/*     */   }
/*     */   
/*     */   public void setFile(String item) {
/* 118 */     this.fileName = item;
/*     */   }
/*     */   
/* 121 */   public String getFile() { return this.fileName; }
/*     */   
/*     */   public String getName() {
/* 124 */     return this.nodeName;
/*     */   }
/*     */   
/* 127 */   public void setMessage(String s) { this.message = s; }
/*     */   
/*     */   public String getMessage() {
/* 130 */     return this.message;
/*     */   }
/*     */   
/* 133 */   public String toString() { return getName(); }
/*     */   
/*     */   public void setParent(Node p) {
/* 136 */     this.parent = p;
/*     */   }
/*     */   
/* 139 */   public Node getParent() { return this.parent; }
/*     */   
/*     */   public void addChild(Node child)
/*     */   {
/* 143 */     this.children.add(child);
/* 144 */     if (child.getParent() == null)
/*     */     {
/* 146 */       child.setParent(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public Node getChild(int index) {
/* 151 */     return (Node)this.children.get(index);
/*     */   }
/*     */   
/* 154 */   public Vector<Node> getChildren() { return this.children; }
/*     */   
/*     */   public boolean hasChildren() {
/* 157 */     return !this.children.isEmpty();
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/views/Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */