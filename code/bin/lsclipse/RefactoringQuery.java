/*    */ package lsclipse;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class RefactoringQuery {
/*    */   private String name;
/*    */   private String query;
/*    */   private ArrayList<String> types;
/*    */   
/* 10 */   public RefactoringQuery(String name, String query) { this.name = name;
/* 11 */     this.query = query;
/* 12 */     this.types = new ArrayList();
/*    */   }
/*    */   
/* 15 */   public ArrayList<String> getTypes() { return this.types; }
/*    */   
/*    */   public void setTypes(ArrayList<String> types) {
/* 18 */     this.types = types;
/*    */   }
/*    */   
/*    */   public void addType(String type) {
/* 22 */     this.types.add(type);
/*    */   }
/*    */   
/* 25 */   public String getName() { return this.name; }
/*    */   
/*    */   public void setName(String name) {
/* 28 */     this.name = name;
/*    */   }
/*    */   
/* 31 */   public String getQuery() { return this.query; }
/*    */   
/*    */   public void setQuery(String query) {
/* 34 */     this.query = query;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/RefactoringQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */