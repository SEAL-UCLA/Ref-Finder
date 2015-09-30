/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GrowableType
/*     */   extends Type
/*     */ {
/*     */   private BoundaryType lowerBound;
/*     */   private BoundaryType upperBound;
/*     */   
/*     */   public GrowableType(BoundaryType lowerBound)
/*     */   {
/*  16 */     this.lowerBound = lowerBound;
/*  17 */     this.upperBound = lowerBound;
/*     */   }
/*     */   
/*     */   private GrowableType(BoundaryType lowerBound, BoundaryType upperBound) {
/*  21 */     this.lowerBound = lowerBound;
/*  22 */     this.upperBound = upperBound;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  26 */     return this.lowerBound.hashCode() + 13 * this.upperBound.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  30 */     if (!(other instanceof GrowableType)) {
/*  31 */       return false;
/*     */     }
/*  33 */     GrowableType sother = (GrowableType)other;
/*     */     
/*  35 */     return (this.lowerBound.equals(sother.lowerBound)) && (this.upperBound.equals(sother.upperBound));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  40 */     return this.upperBound.toString();
/*     */   }
/*     */   
/*     */   public void checkEqualTypes(Type other, boolean grow) throws TypeModeError {
/*  44 */     if ((other instanceof TVar)) {
/*  45 */       other.checkEqualTypes(this, grow);
/*  46 */     } else { if (equals(other))
/*  47 */         return;
/*  48 */       if ((other instanceof GrowableType)) {
/*  49 */         GrowableType sother = (GrowableType)other;
/*  50 */         this.lowerBound = ((BoundaryType)this.lowerBound.union(sother.lowerBound));
/*  51 */         this.upperBound = this.lowerBound;
/*  52 */         sother.lowerBound = this.lowerBound;
/*  53 */         sother.upperBound = this.lowerBound;
/*     */       } else {
/*  55 */         check(other instanceof BoundaryType, this, other);
/*  56 */         BoundaryType b_other = (BoundaryType)other;
/*  57 */         BoundaryType new_lowerBound = (BoundaryType)this.lowerBound.union(b_other);
/*  58 */         if (grow) {
/*  59 */           this.lowerBound = new_lowerBound;
/*  60 */           this.upperBound = this.lowerBound;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*  66 */   public boolean isSubTypeOf(Type other, Map renamings) { return this.lowerBound.isSubTypeOf(other, renamings); }
/*     */   
/*     */   public Type intersect(Type other) throws TypeModeError
/*     */   {
/*  70 */     if ((other instanceof GrowableType)) {
/*  71 */       GrowableType sother = (GrowableType)other;
/*  72 */       BoundaryType max = 
/*  73 */         (BoundaryType)this.upperBound.union(sother.upperBound);
/*  74 */       BoundaryType min = 
/*  75 */         (BoundaryType)this.lowerBound.intersect(sother.lowerBound);
/*  76 */       if (max.equals(min)) {
/*  77 */         return min;
/*     */       }
/*  79 */       return new GrowableType(min, max);
/*     */     }
/*  81 */     if ((other instanceof BoundaryType)) {
/*  82 */       BoundaryType cother = (BoundaryType)other;
/*  83 */       BoundaryType result = (BoundaryType)this.lowerBound.intersect(other);
/*  84 */       if (cother.isStrict()) {
/*  85 */         check(cother.isSuperTypeOf(this.upperBound), this, other);
/*  86 */         return result;
/*     */       }
/*  88 */       if (!result.isSuperTypeOf(this.upperBound)) {
/*  89 */         return new GrowableType(result, this.upperBound);
/*     */       }
/*  91 */       return result;
/*     */     }
/*     */     
/*     */ 
/*  95 */     return this.lowerBound.intersect(other);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFreeFor(TVar var)
/*     */   {
/* 104 */     return this.upperBound.isFreeFor(var);
/*     */   }
/*     */   
/*     */   public Type clone(Map tfact) {
/* 108 */     return new GrowableType((BoundaryType)this.lowerBound.clone(tfact), 
/* 109 */       (BoundaryType)this.upperBound.clone(tfact));
/*     */   }
/*     */   
/*     */   public Type union(Type other) throws TypeModeError {
/* 113 */     if ((other instanceof TVar))
/* 114 */       return other.union(this);
/* 115 */     if ((other instanceof BoundaryType)) {
/* 116 */       BoundaryType b_other = (BoundaryType)other;
/* 117 */       return new GrowableType((BoundaryType)this.lowerBound.union(b_other), 
/* 118 */         (BoundaryType)this.upperBound.union(b_other));
/*     */     }
/*     */     
/* 121 */     check(other instanceof GrowableType, this, other);
/* 122 */     BoundaryType otherLower = ((GrowableType)other).lowerBound;
/* 123 */     BoundaryType otherUpper = ((GrowableType)other).upperBound;
/* 124 */     return new GrowableType(
/* 125 */       (BoundaryType)this.lowerBound.union(otherLower), 
/* 126 */       (BoundaryType)this.upperBound.union(otherUpper));
/*     */   }
/*     */   
/*     */   public Type copyStrictPart()
/*     */   {
/* 131 */     throw new Error("This should not be called!");
/*     */   }
/*     */   
/*     */   public boolean hasOverlapWith(Type other) {
/* 135 */     return this.lowerBound.hasOverlapWith(other);
/*     */   }
/*     */   
/*     */   public Type getParamType(String currName, Type repAs) {
/* 139 */     if ((repAs instanceof TVar)) {
/* 140 */       if (currName.equals(((TVar)repAs).getName())) {
/* 141 */         return this;
/*     */       }
/* 143 */       return null;
/*     */     }
/*     */     
/* 146 */     return this.lowerBound.getParamType(currName, repAs);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/GrowableType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */