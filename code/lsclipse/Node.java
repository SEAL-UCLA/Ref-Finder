/*    */ package lsclipse;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Node
/*    */ {
/*    */   private List<Node> children;
/*    */   private boolean visited;
/*    */   private RefactoringQuery refQry;
/*    */   private int numFound_;
/*    */   
/*    */   public Node() {
/* 13 */     this.children = new java.util.ArrayList();
/* 14 */     this.visited = false;
/* 15 */     this.refQry = null;
/* 16 */     this.numFound_ = 0;
/*    */   }
/*    */   
/*    */   public Node(RefactoringQuery refQry) {
/* 20 */     this.children = new java.util.ArrayList();
/* 21 */     this.visited = false;
/* 22 */     this.refQry = refQry;
/* 23 */     this.numFound_ = 0;
/*    */   }
/*    */   
/*    */   public Node(List<Node> children, boolean visited, RefactoringQuery refQry)
/*    */   {
/* 28 */     this.children = children;
/* 29 */     this.visited = visited;
/* 30 */     this.refQry = refQry;
/*    */   }
/*    */   
/*    */   public void incrementNumFound() {
/* 34 */     this.numFound_ += 1;
/*    */   }
/*    */   
/*    */   public int numFound() {
/* 38 */     return this.numFound_;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 43 */     return this.refQry.getName() + "(" + this.numFound_ + ")";
/*    */   }
/*    */   
/*    */   public List<Node> getChildren() {
/* 47 */     return this.children;
/*    */   }
/*    */   
/*    */   public void addChild(Node child) {
/* 51 */     this.children.add(child);
/*    */   }
/*    */   
/*    */   public void setChildren(List<Node> children) {
/* 55 */     this.children = children;
/*    */   }
/*    */   
/*    */   public boolean isVisited() {
/* 59 */     return this.visited;
/*    */   }
/*    */   
/*    */   public void setVisited(boolean visited) {
/* 63 */     this.visited = visited;
/*    */   }
/*    */   
/*    */   public RefactoringQuery getRefQry() {
/* 67 */     return this.refQry;
/*    */   }
/*    */   
/*    */   public void setRefQry(RefactoringQuery refQry) {
/* 71 */     this.refQry = refQry;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */