/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PrintingState
/*    */ {
/* 17 */   Set visited = new HashSet();
/*    */   
/* 19 */   int indentationLevel = 0;
/* 20 */   int column = 0;
/*    */   PrintStream out;
/*    */   
/*    */   public PrintingState(PrintStream s)
/*    */   {
/* 25 */     this.out = s;
/*    */   }
/*    */   
/*    */   public void print(String o) {
/* 29 */     String s = o.toString();
/* 30 */     this.column += s.length();
/* 31 */     this.out.print(s);
/*    */   }
/*    */   
/*    */   void println(String o) {
/* 35 */     print(o);
/* 36 */     newline();
/*    */   }
/*    */   
/*    */   void newline() {
/* 40 */     this.out.println();
/* 41 */     for (this.column = 0; this.column < this.indentationLevel; this.column += 1) {
/* 42 */       this.out.print(" ");
/*    */     }
/*    */   }
/*    */   
/*    */   void indent() {
/* 47 */     this.indentationLevel += 2;
/*    */   }
/*    */   
/*    */   void outdent() {
/* 51 */     this.indentationLevel -= 2;
/*    */   }
/*    */   
/*    */   protected void printObj(Object object) {
/* 55 */     if ((object instanceof ElementSource)) {
/* 56 */       ((ElementSource)object).print(this);
/*    */     }
/*    */     else {
/* 59 */       print(object.toString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/PrintingState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */