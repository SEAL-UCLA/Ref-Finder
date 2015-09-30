/*     */ package changetypes;
/*     */ 
/*     */ import java.util.Vector;
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
/*     */ public class Fact
/*     */ {
/*     */   public static final String PRIVATE = "private";
/*     */   public static final String PROTECTED = "protected";
/*     */   public static final String PUBLIC = "public";
/*     */   public static final String PACKAGE = "package";
/*     */   public static final String INTERFACE = "interface";
/*     */   public static final String CLASS = "class";
/*     */   public Fact.FactTypes type;
/*     */   public Vector<String> params;
/*     */   public int params_length;
/*  51 */   public String filename = "";
/*     */   public int startposition;
/*     */   public int length;
/*     */   
/*     */   private Fact(Fact.FactTypes mytype, Vector<String> myparams) {
/*  56 */     this.type = mytype;
/*  57 */     this.params = new Vector(myparams);
/*     */   }
/*     */   
/*     */   public Fact(Fact f) {
/*  61 */     this.type = f.type;
/*  62 */     this.params = f.params;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  66 */     return this.params.hashCode() + this.type.ordinal() * 1000;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  71 */     if (o.getClass() != getClass()) return false;
/*  72 */     Fact f = (Fact)o;
/*  73 */     if (!this.type.equals(f.type)) return false;
/*  74 */     for (int i = 0; i < this.params.size(); i++) {
/*  75 */       if ((!((String)this.params.get(i)).equals(f.params.get(i))) && 
/*  76 */         (!((String)this.params.get(i)).equals("*")) && (!((String)f.params.get(i)).equals("*")))
/*  77 */         return false;
/*     */     }
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  83 */     StringBuilder res = new StringBuilder();
/*  84 */     res.append(this.type.toString());
/*  85 */     res.append("(");
/*  86 */     boolean first = true;
/*  87 */     for (String arg : this.params) {
/*  88 */       if (!first) res.append(", ");
/*  89 */       res.append(arg);
/*  90 */       first = false;
/*     */     }
/*  92 */     res.append(")");
/*     */     
/*  94 */     return res.toString();
/*     */   }
/*     */   
/*     */   public static Fact makeModifierMethodFact(String mFullName, String modifier)
/*     */   {
/*  99 */     Vector<String> params = new Vector();
/* 100 */     params.add(mFullName);
/* 101 */     params.add(modifier);
/* 102 */     return new Fact(Fact.FactTypes.METHODMODIFIER, params);
/*     */   }
/*     */   
/* 105 */   public static Fact makeFieldModifierFact(String fFullName, String modifier) { Vector<String> params = new Vector();
/* 106 */     params.add(fFullName);
/* 107 */     params.add(modifier);
/* 108 */     return new Fact(Fact.FactTypes.FIELDMODIFIER, params);
/*     */   }
/*     */   
/*     */   public static Fact makeConditionalFact(String condition, String ifBlockName, String elseBlockName, String typeFullName)
/*     */   {
/* 113 */     Vector<String> params = new Vector();
/* 114 */     params.add(condition);
/* 115 */     params.add(ifBlockName);
/* 116 */     params.add(elseBlockName);
/* 117 */     params.add(typeFullName);
/* 118 */     return new Fact(Fact.FactTypes.CONDITIONAL, params);
/*     */   }
/*     */   
/*     */   public static Fact makeParameterFact(String methodFullName, String paramList, String paramChange) {
/* 122 */     Vector<String> params = new Vector();
/* 123 */     params.add(methodFullName);
/* 124 */     params.add(paramList);
/* 125 */     params.add(paramChange);
/* 126 */     return new Fact(Fact.FactTypes.PARAMETER, params);
/*     */   }
/*     */   
/*     */   public static Fact makePackageFact(String packageFullName) {
/* 130 */     Vector<String> params = new Vector();
/* 131 */     params.add(packageFullName);
/* 132 */     return new Fact(Fact.FactTypes.PACKAGE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeTypeFact(String typeFullName, String typeShortName, String packageFullName, String typeKind) {
/* 136 */     Vector<String> params = new Vector();
/* 137 */     params.add(typeFullName);
/* 138 */     params.add(typeShortName);
/* 139 */     params.add(packageFullName);
/* 140 */     params.add(typeKind);
/* 141 */     return new Fact(Fact.FactTypes.TYPE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeFieldFact(String fieldFullName, String fieldShortName, String typeFullName, String visibility) {
/* 145 */     Vector<String> params = new Vector();
/* 146 */     params.add(fieldFullName);
/* 147 */     params.add(fieldShortName);
/* 148 */     params.add(typeFullName);
/* 149 */     params.add(visibility);
/* 150 */     return new Fact(Fact.FactTypes.FIELD, params);
/*     */   }
/*     */   
/*     */   public static Fact makeMethodFact(String methodFullName, String methodShortName, String typeFullName, String visibility) {
/* 154 */     Vector<String> params = new Vector();
/* 155 */     params.add(methodFullName);
/* 156 */     params.add(methodShortName);
/* 157 */     params.add(typeFullName);
/* 158 */     params.add(visibility);
/* 159 */     return new Fact(Fact.FactTypes.METHOD, params);
/*     */   }
/*     */   
/*     */   public static Fact makeFieldTypeFact(String fieldFullName, String declaredTypeFullName) {
/* 163 */     Vector<String> params = new Vector();
/* 164 */     params.add(fieldFullName);
/* 165 */     params.add(declaredTypeFullName);
/* 166 */     return new Fact(Fact.FactTypes.FIELDOFTYPE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeTypeInTypeFact(String innerTypeFullName, String outerTypeFullName) {
/* 170 */     Vector<String> params = new Vector();
/* 171 */     params.add(innerTypeFullName);
/* 172 */     params.add(outerTypeFullName);
/* 173 */     return new Fact(Fact.FactTypes.TYPEINTYPE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeReturnsFact(String methodFullName, String returnTypeFullName) {
/* 177 */     Vector<String> params = new Vector();
/* 178 */     params.add(methodFullName);
/* 179 */     params.add(returnTypeFullName);
/* 180 */     return new Fact(Fact.FactTypes.RETURN, params);
/*     */   }
/*     */   
/*     */   public static Fact makeSubtypeFact(String superTypeFullName, String subTypeFullName) {
/* 184 */     Vector<String> params = new Vector();
/* 185 */     params.add(superTypeFullName);
/* 186 */     params.add(subTypeFullName);
/* 187 */     return new Fact(Fact.FactTypes.SUBTYPE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeImplementsFact(String superTypeFullName, String subTypeFullName) {
/* 191 */     Vector<String> params = new Vector();
/* 192 */     params.add(superTypeFullName);
/* 193 */     params.add(subTypeFullName);
/* 194 */     return new Fact(Fact.FactTypes.IMPLEMENTS, params);
/*     */   }
/*     */   
/*     */   public static Fact makeExtendsFact(String superTypeFullName, String subTypeFullName) {
/* 198 */     Vector<String> params = new Vector();
/* 199 */     params.add(superTypeFullName);
/* 200 */     params.add(subTypeFullName);
/* 201 */     return new Fact(Fact.FactTypes.EXTENDS, params);
/*     */   }
/*     */   
/*     */   public static Fact makeInheritedFieldFact(String fieldShortName, String superTypeFullName, String subTypeFullName) {
/* 205 */     Vector<String> params = new Vector();
/* 206 */     params.add(fieldShortName);
/* 207 */     params.add(superTypeFullName);
/* 208 */     params.add(subTypeFullName);
/* 209 */     return new Fact(Fact.FactTypes.INHERITEDFIELD, params);
/*     */   }
/*     */   
/*     */   public static Fact makeInheritedMethodFact(String methodShortName, String superTypeFullName, String subTypeFullName) {
/* 213 */     Vector<String> params = new Vector();
/* 214 */     params.add(methodShortName);
/* 215 */     params.add(superTypeFullName);
/* 216 */     params.add(subTypeFullName);
/* 217 */     return new Fact(Fact.FactTypes.INHERITEDMETHOD, params);
/*     */   }
/*     */   
/*     */   public static Fact makeMethodBodyFact(String methodFullName, String methodBody) {
/* 221 */     Vector<String> params = new Vector();
/* 222 */     params.add(methodFullName);
/* 223 */     params.add(methodBody);
/* 224 */     return new Fact(Fact.FactTypes.METHODBODY, params);
/*     */   }
/*     */   
/*     */   public static Fact makeMethodArgsFact(String methodFullName, String methodSignature) {
/* 228 */     Vector<String> params = new Vector();
/* 229 */     params.add(methodFullName);
/* 230 */     params.add(methodSignature);
/* 231 */     return new Fact(Fact.FactTypes.METHODSIGNATURE, params);
/*     */   }
/*     */   
/*     */   public static Fact makeCallsFact(String callerMethodFullName, String calleeMethodFullName) {
/* 235 */     Vector<String> params = new Vector();
/* 236 */     params.add(callerMethodFullName);
/* 237 */     params.add(calleeMethodFullName);
/* 238 */     return new Fact(Fact.FactTypes.CALLS, params);
/*     */   }
/*     */   
/*     */   public static Fact makeAccessesFact(String fieldFullName, String accessorMethodFullName) {
/* 242 */     Vector<String> params = new Vector();
/* 243 */     params.add(fieldFullName);
/* 244 */     params.add(accessorMethodFullName);
/* 245 */     return new Fact(Fact.FactTypes.ACCESSES, params);
/*     */   }
/*     */   
/*     */   public static Fact makeCastFact(String expression, String type, String methodName) {
/* 249 */     Vector<String> params = new Vector();
/* 250 */     params.add(expression);
/* 251 */     params.add(type);
/* 252 */     params.add(methodName);
/* 253 */     return new Fact(Fact.FactTypes.CAST, params);
/*     */   }
/*     */   
/*     */   public static Fact makeTryCatchFact(String tryBlock, String catchClauses, String finallyBlock, String methodName) {
/* 257 */     Vector<String> params = new Vector();
/* 258 */     params.add(tryBlock);
/* 259 */     params.add(catchClauses);
/* 260 */     params.add(finallyBlock);
/* 261 */     params.add(methodName);
/* 262 */     return new Fact(Fact.FactTypes.TRYCATCH, params);
/*     */   }
/*     */   
/*     */   public static Fact makeThrownExceptionFact(String methodQualifiedName, String exceptionQualifiedName)
/*     */   {
/* 267 */     Vector<String> params = new Vector();
/* 268 */     params.add(methodQualifiedName);
/* 269 */     params.add(exceptionQualifiedName);
/* 270 */     return new Fact(Fact.FactTypes.THROWN, params);
/*     */   }
/*     */   
/*     */   public static Fact makeGetterFact(String methodQualifiedName, String fieldQualifiedName)
/*     */   {
/* 275 */     Vector<String> params = new Vector();
/* 276 */     params.add(methodQualifiedName);
/* 277 */     params.add(fieldQualifiedName);
/* 278 */     return new Fact(Fact.FactTypes.GETTER, params);
/*     */   }
/*     */   
/*     */   public static Fact makeSetterFact(String methodQualifiedName, String fieldQualifiedName)
/*     */   {
/* 283 */     Vector<String> params = new Vector();
/* 284 */     params.add(methodQualifiedName);
/* 285 */     params.add(fieldQualifiedName);
/* 286 */     return new Fact(Fact.FactTypes.SETTER, params);
/*     */   }
/*     */   
/*     */   public static Fact makeLocalVarFact(String methodQualifiedName, String typeQualifiedName, String identifier, String expression)
/*     */   {
/* 291 */     Vector<String> params = new Vector();
/* 292 */     params.add(methodQualifiedName);
/* 293 */     params.add(typeQualifiedName);
/* 294 */     params.add(identifier);
/* 295 */     params.add(expression);
/* 296 */     return new Fact(Fact.FactTypes.LOCALVAR, params);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/changetypes/Fact.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */