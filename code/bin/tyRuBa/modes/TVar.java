/*     */ package tyRuBa.modes;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TVar
/*     */   extends Type
/*     */ {
/*     */   private Type content;
/*  17 */   private static int ctr = 1;
/*  18 */   private int id = ++ctr;
/*     */   private String name;
/*     */   
/*     */   public TVar(String name)
/*     */   {
/*  23 */     this.name = name;
/*  24 */     this.content = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Type getContents()
/*     */   {
/*  31 */     TVar me = derefTVar();
/*  32 */     if (me.content == null) {
/*  33 */       return null;
/*     */     }
/*  35 */     return this.content;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  39 */     TVar me = derefTVar();
/*  40 */     if (me.isFree()) {
/*  41 */       return "?" + me.getName() + "_" + me.id;
/*     */     }
/*  43 */     return me.getContents().toString();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  48 */     TVar me = derefTVar();
/*  49 */     return me.name;
/*     */   }
/*     */   
/*     */   private void setContents(Type other) {
/*  53 */     this.content = other;
/*     */   }
/*     */   
/*     */   public void checkEqualTypes(Type other, boolean grow) throws TypeModeError {
/*  57 */     TVar me = derefTVar();
/*  58 */     if (me.equals(other))
/*  59 */       return;
/*  60 */     if ((other instanceof TVar)) {
/*  61 */       TVar otherVar = ((TVar)other).derefTVar();
/*  62 */       if (me.isFree()) {
/*  63 */         if (!otherVar.isFreeFor(this)) {
/*  64 */           throw new TypeModeError("Recursion in inferred type " + this + " & " + 
/*  65 */             otherVar);
/*     */         }
/*     */         
/*  68 */         me.setContents(otherVar);
/*     */       }
/*  70 */       else if (otherVar.isFree()) {
/*  71 */         if (!me.isFreeFor(otherVar)) {
/*  72 */           throw new TypeModeError("Recursion in inferred type " + this + " & " + 
/*  73 */             otherVar);
/*     */         }
/*     */         
/*  76 */         otherVar.setContents(me);
/*     */       }
/*     */       else
/*     */       {
/*  80 */         me.content.checkEqualTypes(otherVar.content);
/*  81 */         me.setContents(otherVar);
/*     */       }
/*     */     }
/*  84 */     else if (me.isFree()) {
/*  85 */       if (!other.isFreeFor(this)) {
/*  86 */         throw new TypeModeError("Recursion in inferred type " + this + " & " + 
/*  87 */           other);
/*     */       }
/*     */       
/*  90 */       me.setContents(other);
/*     */     } else {
/*  92 */       me.content.checkEqualTypes(other);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSubTypeOf(Type declared, Map renamings) {
/*  97 */     TVar me = derefTVar();
/*  98 */     if (!me.isFree())
/*  99 */       return me.getContents().isSubTypeOf(declared, renamings);
/* 100 */     if (!(declared instanceof TVar)) {
/* 101 */       return false;
/*     */     }
/* 103 */     TVar vdeclared = ((TVar)declared).derefTVar();
/*     */     
/* 105 */     if (!vdeclared.isFree()) {
/* 106 */       return false;
/*     */     }
/* 108 */     TVar renamed = (TVar)renamings.get(me);
/* 109 */     if (renamed == null) {
/* 110 */       renamings.put(me, vdeclared);
/* 111 */       return true;
/*     */     }
/* 113 */     return vdeclared.equals(renamed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private TVar derefTVar()
/*     */   {
/* 120 */     if ((this.content != null) && ((this.content instanceof TVar))) {
/* 121 */       return ((TVar)this.content).derefTVar();
/*     */     }
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isFree()
/*     */   {
/* 128 */     return getContents() == null;
/*     */   }
/*     */   
/*     */   public boolean isFreeFor(TVar var) {
/* 132 */     TVar me = derefTVar();
/* 133 */     if (!me.isFree()) {
/* 134 */       return me.content.isFreeFor(var);
/*     */     }
/* 136 */     return var != me;
/*     */   }
/*     */   
/*     */   public Type clone(Map varRenamings)
/*     */   {
/* 141 */     TVar me = derefTVar();
/* 142 */     TVar clone = (TVar)varRenamings.get(me);
/* 143 */     if (clone != null) {
/* 144 */       return clone;
/*     */     }
/* 146 */     clone = new TVar(me.getName());
/* 147 */     clone.setContents(me.content == null ? 
/* 148 */       null : me.content.clone(varRenamings));
/* 149 */     varRenamings.put(me, clone);
/* 150 */     return clone;
/*     */   }
/*     */   
/*     */   public Type union(Type other) throws TypeModeError
/*     */   {
/* 155 */     TVar me = derefTVar();
/* 156 */     if (!me.isFree())
/* 157 */       return me.getContents().union(other);
/* 158 */     if (me.equals(other)) {
/* 159 */       return me;
/*     */     }
/* 161 */     check(other.isFreeFor(me), me, other);
/* 162 */     me.setContents(other);
/* 163 */     return me.content;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 179 */     if (!(other instanceof TVar)) {
/* 180 */       return false;
/*     */     }
/* 182 */     return derefTVar() == ((TVar)other).derefTVar();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 187 */     TVar aliasOfMe = derefTVar();
/* 188 */     if (aliasOfMe == this) {
/* 189 */       return super.hashCode();
/*     */     }
/* 191 */     return aliasOfMe.hashCode();
/*     */   }
/*     */   
/*     */   public Type intersect(Type other) throws TypeModeError {
/* 195 */     TVar me = derefTVar();
/* 196 */     if (me.equals(other))
/* 197 */       return me;
/* 198 */     if (!me.isFree()) {
/* 199 */       me.setContents(me.content.intersect(other));
/* 200 */       return me.content.intersect(other);
/*     */     }
/* 202 */     check(other.isFreeFor(me), this, other);
/* 203 */     me.setContents(other);
/* 204 */     return other;
/*     */   }
/*     */   
/*     */   public Type copyStrictPart()
/*     */   {
/* 209 */     if (isFree()) {
/* 210 */       return Factory.makeTVar(getName());
/*     */     }
/* 212 */     return getContents().copyStrictPart();
/*     */   }
/*     */   
/*     */   public boolean hasOverlapWith(Type other)
/*     */   {
/* 217 */     TVar me = derefTVar();
/* 218 */     if (!me.isFree()) {
/* 219 */       return me.content.hasOverlapWith(other);
/*     */     }
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   public Type getParamType(String currName, Type repAs)
/*     */   {
/* 226 */     if ((repAs instanceof TVar)) {
/* 227 */       if (currName.equals(((TVar)repAs).getName())) {
/* 228 */         return this;
/*     */       }
/* 230 */       return null;
/*     */     }
/* 232 */     if (!isFree()) {
/* 233 */       return getContents().getParamType(currName, repAs);
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class javaEquivalent()
/*     */     throws TypeModeError
/*     */   {
/* 246 */     Type contents = getContents();
/* 247 */     if (contents != null) {
/* 248 */       return contents.javaEquivalent();
/*     */     }
/* 250 */     throw new TypeModeError("This type variable is empty, and therefore has no java equivalent");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/TVar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */