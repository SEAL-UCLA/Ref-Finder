/*    */ package tyRuBa.util;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeElementSource
/*    */   extends ElementSource
/*    */ {
/* 10 */   Vector children = new Vector();
/*    */   
/*    */   public void add(ElementSource child)
/*    */   {
/* 14 */     this.children.addElement(child);
/*    */   }
/*    */   
/*    */   public ElementSource get(int i)
/*    */   {
/* 19 */     return (ElementSource)this.children.elementAt(i);
/*    */   }
/*    */   
/*    */   public int numberOfChildren()
/*    */   {
/* 24 */     return this.children.size();
/*    */   }
/*    */   
/* 27 */   public int i = -1;
/*    */   
/*    */   public int status() {
/* 30 */     for (this.i = 0; this.i < numberOfChildren();) {
/* 31 */       int stat = get(this.i).status();
/* 32 */       if (stat == 1)
/* 33 */         return stat;
/* 34 */       if (stat == -1) {
/* 35 */         this.children.removeElementAt(this.i);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       }
/*    */       else
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 46 */         this.i += 1; }
/*    */     }
/* 48 */     if (numberOfChildren() == 0) {
/* 49 */       return -1;
/*    */     }
/* 51 */     return 0;
/*    */   }
/*    */   
/*    */   public Object nextElement() { int stat;
/*    */     int stat;
/* 56 */     if ((this.i < 0) || (this.i >= numberOfChildren())) {
/* 57 */       stat = status();
/*    */     } else
/* 59 */       stat = 1;
/* 60 */     if (stat == 1) {
/* 61 */       return get(this.i).nextElement();
/*    */     }
/*    */     
/* 64 */     throw new Error("No nextElement found in CompositeElementSource");
/*    */   }
/*    */   
/*    */   public void print(PrintingState p) {
/* 68 */     p.print("Composite(");
/* 69 */     p.indent();p.newline();
/* 70 */     for (int i = 0; i < numberOfChildren(); i++) {
/* 71 */       get(i).print(p);
/*    */     }
/* 73 */     p.outdent();
/* 74 */     p.print(")");
/*    */   }
/*    */   
/*    */   public ElementSource simplify() {
/* 78 */     if (this.children.size() == 0)
/* 79 */       return ElementSource.theEmpty;
/* 80 */     if (this.children.size() == 1)
/* 81 */       return get(0);
/* 82 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/CompositeElementSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */