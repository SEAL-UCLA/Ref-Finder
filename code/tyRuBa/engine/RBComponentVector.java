/*    */ package tyRuBa.engine;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import tyRuBa.engine.compilation.CompilationContext;
/*    */ import tyRuBa.engine.compilation.Compiled;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RBComponentVector
/*    */ {
/*    */   public ArrayList contents;
/*    */   
/*    */   public RBComponentVector()
/*    */   {
/* 21 */     this.contents = new ArrayList();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 25 */     this.contents = new ArrayList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public RBComponentVector(int predictedSize)
/*    */   {
/* 33 */     this.contents = new ArrayList(predictedSize);
/*    */   }
/*    */   
/*    */ 
/*    */   public RBComponentVector(ArrayList vect)
/*    */   {
/* 39 */     this.contents = vect;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void insert(RBComponent c)
/*    */   {
/* 71 */     if (c == null)
/* 72 */       throw new NullPointerException("Not allowed to insert null");
/* 73 */     this.contents.add(c);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 78 */     int len = this.contents.size();
/* 79 */     StringBuffer result = new StringBuffer();
/* 80 */     for (int i = 0; i < len; i++) {
/* 81 */       result.append(this.contents.get(i) + "\n");
/*    */     }
/* 83 */     return result.toString();
/*    */   }
/*    */   
/*    */   public Compiled compile(CompilationContext context) {
/* 87 */     Compiled result = Compiled.fail;
/* 88 */     for (Iterator iter = iterator(); iter.hasNext();) {
/* 89 */       RBComponent element = (RBComponent)iter.next();
/* 90 */       result = result.disjoin(element.compile(context));
/*    */     }
/* 92 */     return result;
/*    */   }
/*    */   
/*    */   private Iterator iterator() {
/* 96 */     new Iterator()
/*    */     {
/*    */       int pos;
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */       public boolean hasNext()
/*    */       {
/* :5 */         return this.pos < RBComponentVector.this.contents.size();
/*    */       }
/*    */       
/*    */       private void skipInvalids() {
/* :9 */         while ((this.pos < RBComponentVector.this.contents.size()) && (!
/* ;0 */           ((RBComponent)RBComponentVector.this.contents.get(this.pos)).isValid()))
/* ;1 */           this.pos += 1;
/*    */       }
/*    */       
/*    */       public Object next() {
/* ;5 */         Object result = RBComponentVector.this.contents.get(this.pos++);
/* ;6 */         skipInvalids();
/* ;7 */         return result;
/*    */       }
/*    */       
/*    */       public void remove() {
/* <1 */         throw new Error("This operation is not supported");
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/RBComponentVector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */