/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ListType extends Type
/*     */ {
/*     */   private Type element;
/*     */   
/*     */   public ListType() {
/*  10 */     this.element = null;
/*     */   }
/*     */   
/*     */   public ListType(Type element) {
/*  14 */     this.element = element;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  18 */     return getElementType() == null ? 23423 : getElementType().hashCode() + 5774;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  22 */     if ((other instanceof ListType)) {
/*  23 */       ListType cother = (ListType)other;
/*  24 */       if (getElementType() == null) {
/*  25 */         return cother.getElementType() == null;
/*     */       }
/*     */       
/*  28 */       return (cother.getElementType() != null) && (getElementType().equals(cother.getElementType()));
/*     */     }
/*     */     
/*  31 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  35 */     if (getElementType() == null) {
/*  36 */       return "[]";
/*     */     }
/*  38 */     return "[" + getElementType() + "]";
/*     */   }
/*     */   
/*     */   public Type getElementType()
/*     */   {
/*  43 */     return this.element;
/*     */   }
/*     */   
/*     */   public boolean isFreeFor(TVar var) {
/*  47 */     return (getElementType() == null) || (getElementType().isFreeFor(var));
/*     */   }
/*     */   
/*     */   public Type clone(Map tfact) {
/*  51 */     return new ListType(getElementType() == null ? null : getElementType().clone(tfact));
/*     */   }
/*     */   
/*     */   public Type intersect(Type other) throws TypeModeError {
/*  55 */     if ((other instanceof TVar))
/*  56 */       return other.intersect(this);
/*  57 */     if (equals(other)) {
/*  58 */       return this;
/*     */     }
/*  60 */     check(other instanceof ListType, this, other);
/*  61 */     ListType lother = (ListType)other;
/*  62 */     if (getElementType() == null)
/*  63 */       return this;
/*  64 */     if (lother.getElementType() == null) {
/*  65 */       return lother;
/*     */     }
/*  67 */     return new ListType(
/*  68 */       getElementType().intersect(lother.getElementType()));
/*     */   }
/*     */   
/*     */   public void checkEqualTypes(Type other, boolean grow)
/*     */     throws TypeModeError
/*     */   {
/*  74 */     if ((other instanceof TVar)) {
/*  75 */       other.checkEqualTypes(this, grow);
/*     */     } else {
/*  77 */       check(other instanceof ListType, this, other);
/*  78 */       ListType lother = (ListType)other;
/*  79 */       if (getElementType() != null)
/*     */       {
/*  81 */         if (lother.getElementType() == null) {
/*  82 */           lother.element = getElementType();
/*     */         } else
/*     */           try {
/*  85 */             getElementType().checkEqualTypes(lother.getElementType(), grow);
/*     */           } catch (TypeModeError e) {
/*  87 */             throw new TypeModeError(e, this);
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSubTypeOf(Type declared, Map renamings) {
/*  94 */     if ((declared instanceof TVar))
/*  95 */       declared = ((TVar)declared).getContents();
/*  96 */     if (declared == null)
/*  97 */       return false;
/*  98 */     if ((declared instanceof ListType)) {
/*  99 */       ListType ldeclared = (ListType)declared;
/* 100 */       if (getElementType() == null)
/* 101 */         return true;
/* 102 */       if (ldeclared.getElementType() == null) {
/* 103 */         return false;
/*     */       }
/* 105 */       return getElementType().isSubTypeOf(ldeclared.getElementType(), renamings);
/*     */     }
/*     */     
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasOverlapWith(Type other)
/*     */   {
/* 113 */     if ((other instanceof TVar))
/* 114 */       return other.hasOverlapWith(this);
/* 115 */     if ((other instanceof ListType)) {
/* 116 */       Type otherElement = ((ListType)other).element;
/* 117 */       if ((this.element == null) && (otherElement == null))
/* 118 */         return true;
/* 119 */       if ((this.element == null) || (otherElement == null)) {
/* 120 */         return false;
/*     */       }
/* 122 */       return this.element.hasOverlapWith(otherElement);
/*     */     }
/*     */     
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public Type copyStrictPart()
/*     */   {
/* 130 */     if (this.element == null) {
/* 131 */       return new ListType();
/*     */     }
/* 133 */     return new ListType(this.element.copyStrictPart());
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
/*     */   public Type union(Type other)
/*     */     throws TypeModeError
/*     */   {
/* 147 */     if ((other instanceof TVar)) {
/* 148 */       return other.union(this);
/*     */     }
/* 150 */     check(other instanceof ListType, this, other);
/* 151 */     ListType lother = (ListType)other;
/* 152 */     if (getElementType() == null)
/* 153 */       return other;
/* 154 */     if (lother.getElementType() == null) {
/* 155 */       return this;
/*     */     }
/* 157 */     return new ListType(
/* 158 */       getElementType().union(lother.getElementType()));
/*     */   }
/*     */   
/*     */ 
/*     */   public Type getParamType(String currName, Type repAs)
/*     */   {
/* 164 */     if ((repAs instanceof TVar)) {
/* 165 */       if (currName.equals(((TVar)repAs).getName())) {
/* 166 */         return this;
/*     */       }
/* 168 */       return null;
/*     */     }
/* 170 */     if (!(repAs instanceof ListType))
/* 171 */       return null;
/* 172 */     if (this.element == null) {
/* 173 */       return null;
/*     */     }
/* 175 */     return this.element.getParamType(currName, ((ListType)repAs).element);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/modes/ListType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */