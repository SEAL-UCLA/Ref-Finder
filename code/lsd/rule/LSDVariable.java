/*    */ package lsd.rule;
/*    */ 
/*    */ 
/*    */ public class LSDVariable
/*    */ {
/*    */   private char type;
/*    */   private String variableName;
/*    */   
/*    */   public static boolean isValidType(char type)
/*    */   {
/* 11 */     return "hieptmfabc".contains(Character.toString(type));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LSDVariable(String variableName, char type)
/*    */   {
/* 20 */     this.variableName = variableName;
/* 21 */     this.type = type;
/*    */   }
/*    */   
/* 24 */   public String getName() { return this.variableName; }
/*    */   
/*    */   public boolean typeChecks(char type) {
/* 27 */     return this.type == type;
/*    */   }
/*    */   
/* 30 */   public boolean typeChecks(LSDVariable match) { return this.type == match.type; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 35 */   public boolean typeConflicts(LSDVariable toBeMatched) { return (this.variableName.equals(toBeMatched.variableName)) && (!typeChecks(toBeMatched)); }
/*    */   
/* 37 */   public String toString() { return "?" + this.variableName; }
/*    */   
/* 39 */   public boolean equals(LSDVariable other) { return (this.variableName.equals(other.variableName)) && (this.type == other.type); }
/*    */   
/* 41 */   public boolean equals(Object other) { if ((other instanceof LSDVariable)) {
/* 42 */       return equals((LSDVariable)other);
/*    */     }
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 48 */     String identity = this.variableName + this.type;
/* 49 */     return identity.hashCode();
/*    */   }
/*    */   
/*    */   public int compareTo(Object o) {
/* 53 */     return o.hashCode() - hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */   public static void main(String[] args) {}
/*    */   
/*    */   public char getType()
/*    */   {
/* 61 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsd/rule/LSDVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */