/*    */ package tyRuBa.util;
/*    */ 
/*    */ class AppendSource extends ElementSource { private ElementSource s1;
/*    */   private ElementSource s2;
/*    */   
/*  6 */   AppendSource(ElementSource s1, ElementSource s2) { this.s1 = s1;
/*  7 */     this.s2 = s2;
/*    */   }
/*    */   
/* 10 */   public int status() { int stat = this.s1.status();
/* 11 */     if (stat == 1)
/* 12 */       return stat;
/* 13 */     if (stat == -1) {
/* 14 */       this.s1 = this.s2;
/* 15 */       this.s2 = theEmpty;
/* 16 */       return this.s1.status();
/*    */     }
/* 18 */     return this.s2.status();
/*    */   }
/*    */   
/* 21 */   public Object nextElement() { if (this.s1.status() == 1) {
/* 22 */       return this.s1.nextElement();
/*    */     }
/* 24 */     return this.s2.nextElement();
/*    */   }
/*    */   
/* 27 */   public void print(PrintingState p) { p.print("Append(");
/* 28 */     p.indent();p.newline();
/* 29 */     this.s1.print(p);
/* 30 */     p.newline();p.print("++");
/* 31 */     this.s2.print(p);
/* 32 */     p.outdent();
/* 33 */     p.print(")");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/AppendSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */