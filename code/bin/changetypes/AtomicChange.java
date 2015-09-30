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
/*     */ public class AtomicChange
/*     */ {
/*  71 */   public int[] changecount = new int[AtomicChange.ChangeTypes.values().length];
/*     */   public AtomicChange.ChangeTypes type;
/*     */   public Vector<String> params;
/*     */   
/*     */   private AtomicChange(AtomicChange.ChangeTypes mytype, Vector<String> myparams)
/*     */   {
/*  77 */     this.type = mytype;
/*  78 */     this.params = new Vector(myparams);
/*     */   }
/*     */   
/*     */   public AtomicChange(AtomicChange f) {
/*  82 */     this.type = f.type;
/*  83 */     this.params = f.params;
/*     */   }
/*     */   
/*     */ 
/*  87 */   public int hashCode() { return this.type.ordinal(); }
/*     */   
/*     */   public boolean equals(Object o) {
/*  90 */     if (o.getClass() != getClass()) return false;
/*  91 */     AtomicChange f = (AtomicChange)o;
/*  92 */     if (!this.type.equals(f.type)) return false;
/*  93 */     for (int i = 0; i < this.params.size(); i++) {
/*  94 */       if ((!((String)this.params.get(i)).equals(f.params.get(i))) && 
/*  95 */         (!((String)this.params.get(i)).equals("*")) && (!((String)f.params.get(i)).equals("*")))
/*  96 */         return false;
/*     */     }
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     StringBuilder res = new StringBuilder();
/* 103 */     if ((this.type.ordinal() >= AtomicChange.ChangeTypes.ADD_PACKAGE.ordinal()) && 
/* 104 */       (this.type.ordinal() <= AtomicChange.ChangeTypes.ADD_TYPEINTYPE.ordinal())) {
/* 105 */       res.append("+");
/* 106 */     } else if ((this.type.ordinal() >= AtomicChange.ChangeTypes.DEL_PACKAGE.ordinal()) && 
/* 107 */       (this.type.ordinal() <= AtomicChange.ChangeTypes.DEL_TYPEINTYPE.ordinal())) {
/* 108 */       res.append("-");
/*     */     } else {
/* 110 */       res.append("*");
/*     */     }
/* 112 */     res.append(this.type.toString());
/* 113 */     res.append("(");
/* 114 */     boolean first = true;
/* 115 */     for (String arg : this.params) {
/* 116 */       if (!first) res.append(", ");
/* 117 */       res.append(arg);
/* 118 */       first = false;
/*     */     }
/* 120 */     res.append(")");
/*     */     
/* 122 */     return res.toString();
/*     */   }
/*     */   
/*     */   public static AtomicChange makePackageChange(char typ, String packageFullName) {
/* 126 */     Vector<String> params = new Vector();
/* 127 */     params.add(packageFullName);
/* 128 */     return new AtomicChange(
/* 129 */       typ == 'D' ? AtomicChange.ChangeTypes.DEL_PACKAGE : typ == 'A' ? AtomicChange.ChangeTypes.ADD_PACKAGE : 
/* 130 */       AtomicChange.ChangeTypes.MOD_PACKAGE, params);
/*     */   }
/*     */   
/*     */ 
/*     */   public static AtomicChange makeConditionalChange(char typ, String condition, String ifBlockName, String elseBlockName, String typeFullName)
/*     */   {
/* 136 */     Vector<String> params = new Vector();
/* 137 */     params.add(condition);
/* 138 */     params.add(ifBlockName);
/* 139 */     params.add(elseBlockName);
/* 140 */     params.add(typeFullName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_CONDITIONAL : 
/* 147 */       AtomicChange.ChangeTypes.DEL_CONDITIONAL, params);
/*     */   }
/*     */   
/* 150 */   public static AtomicChange makeParameterChange(char typ, String methodShortName, String methodFullName, String paramList) { Vector<String> params = new Vector();
/* 151 */     params.add(methodShortName);
/* 152 */     params.add(methodFullName);
/* 153 */     params.add(paramList);
/* 154 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_PARAMETER : 
/* 155 */       AtomicChange.ChangeTypes.DEL_PARAMETER, params);
/*     */   }
/*     */   
/* 158 */   public static AtomicChange makeThrownExceptionChange(char typ, String methodQualifiedName, String exceptionQualifiedName) { Vector<String> params = new Vector();
/* 159 */     params.add(methodQualifiedName);
/* 160 */     params.add(exceptionQualifiedName);
/* 161 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_THROWN : 
/* 162 */       AtomicChange.ChangeTypes.DEL_THROWN, params);
/*     */   }
/*     */   
/* 165 */   public static AtomicChange makeGetterChange(char typ, String methodQualifiedName, String fieldQualifiedName) { Vector<String> params = new Vector();
/* 166 */     params.add(methodQualifiedName);
/* 167 */     params.add(fieldQualifiedName);
/* 168 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_GETTER : 
/* 169 */       AtomicChange.ChangeTypes.DEL_GETTER, params);
/*     */   }
/*     */   
/* 172 */   public static AtomicChange makeSetterChange(char typ, String methodQualifiedName, String fieldQualifiedName) { Vector<String> params = new Vector();
/* 173 */     params.add(methodQualifiedName);
/* 174 */     params.add(fieldQualifiedName);
/* 175 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_SETTER : 
/* 176 */       AtomicChange.ChangeTypes.DEL_SETTER, params);
/*     */   }
/*     */   
/* 179 */   public static AtomicChange makeMethodModifierChange(char typ, String methodFullName, String modifier) { Vector<String> params = new Vector();
/* 180 */     params.add(methodFullName);
/* 181 */     params.add(modifier);
/* 182 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_METHODMODIFIER : 
/* 183 */       AtomicChange.ChangeTypes.DEL_METHODMODIFIER, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeFieldModifierChange(char typ, String fFullName, String modifier) {
/* 187 */     Vector<String> params = new Vector();
/* 188 */     params.add(fFullName);
/* 189 */     params.add(modifier);
/* 190 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_FIELDMODIFIER : 
/* 191 */       AtomicChange.ChangeTypes.DEL_FIELDMODIFIER, params);
/*     */   }
/*     */   
/*     */ 
/*     */   public static AtomicChange makeTypeChange(char typ, String typeFullName, String typeShortName, String packageFullName)
/*     */   {
/* 197 */     Vector<String> params = new Vector();
/* 198 */     params.add(typeFullName);
/* 199 */     params.add(typeShortName);
/* 200 */     params.add(packageFullName);
/* 201 */     return new AtomicChange(
/* 202 */       typ == 'D' ? AtomicChange.ChangeTypes.DEL_TYPE : typ == 'A' ? AtomicChange.ChangeTypes.ADD_TYPE : 
/* 203 */       AtomicChange.ChangeTypes.MOD_TYPE, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeFieldChange(char typ, String fieldFullName, String fieldShortName, String typeFullName) {
/* 207 */     Vector<String> params = new Vector();
/* 208 */     params.add(fieldFullName);
/* 209 */     params.add(fieldShortName);
/* 210 */     params.add(typeFullName);
/* 211 */     return new AtomicChange(
/* 212 */       typ == 'D' ? AtomicChange.ChangeTypes.DEL_FIELD : typ == 'A' ? AtomicChange.ChangeTypes.ADD_FIELD : 
/* 213 */       AtomicChange.ChangeTypes.MOD_FIELD, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeMethodChange(char typ, String methodFullName, String methodShortName, String typeFullName) {
/* 217 */     Vector<String> params = new Vector();
/* 218 */     params.add(methodFullName);
/* 219 */     params.add(methodShortName);
/* 220 */     params.add(typeFullName);
/* 221 */     return new AtomicChange(
/* 222 */       typ == 'D' ? AtomicChange.ChangeTypes.DEL_METHOD : typ == 'A' ? AtomicChange.ChangeTypes.ADD_METHOD : 
/* 223 */       AtomicChange.ChangeTypes.MOD_METHOD, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeFieldTypeChange(char typ, String fieldFullName, String declaredTypeFullName) {
/* 227 */     Vector<String> params = new Vector();
/* 228 */     params.add(fieldFullName);
/* 229 */     params.add(declaredTypeFullName);
/* 230 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_FIELDOFTYPE : 
/* 231 */       AtomicChange.ChangeTypes.DEL_FIELDOFTYPE, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeTypeInTypeChange(char typ, String innerTypeFullName, String outerTypeFullName) {
/* 235 */     Vector<String> params = new Vector();
/* 236 */     params.add(innerTypeFullName);
/* 237 */     params.add(outerTypeFullName);
/* 238 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_TYPEINTYPE : 
/* 239 */       AtomicChange.ChangeTypes.DEL_TYPEINTYPE, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeReturnsChange(char typ, String methodFullName, String returnTypeFullName) {
/* 243 */     Vector<String> params = new Vector();
/* 244 */     params.add(methodFullName);
/* 245 */     params.add(returnTypeFullName);
/* 246 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_RETURN : 
/* 247 */       AtomicChange.ChangeTypes.DEL_RETURN, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeSubtypeChange(char typ, String superTypeFullName, String subTypeFullName) {
/* 251 */     Vector<String> params = new Vector();
/* 252 */     params.add(superTypeFullName);
/* 253 */     params.add(subTypeFullName);
/* 254 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_SUBTYPE : 
/* 255 */       AtomicChange.ChangeTypes.DEL_SUBTYPE, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeImplementsChange(char typ, String superTypeFullName, String subTypeFullName) {
/* 259 */     Vector<String> params = new Vector();
/* 260 */     params.add(superTypeFullName);
/* 261 */     params.add(subTypeFullName);
/* 262 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_IMPLEMENTS : 
/* 263 */       AtomicChange.ChangeTypes.DEL_IMPLEMENTS, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeExtendsChange(char typ, String superTypeFullName, String subTypeFullName) {
/* 267 */     Vector<String> params = new Vector();
/* 268 */     params.add(superTypeFullName);
/* 269 */     params.add(subTypeFullName);
/* 270 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_EXTENDS : 
/* 271 */       AtomicChange.ChangeTypes.DEL_EXTENDS, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeInheritedFieldChange(char typ, String fieldShortName, String superTypeFullName, String subTypeFullName) {
/* 275 */     Vector<String> params = new Vector();
/* 276 */     params.add(fieldShortName);
/* 277 */     params.add(superTypeFullName);
/* 278 */     params.add(subTypeFullName);
/* 279 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_INHERITEDFIELD : 
/* 280 */       AtomicChange.ChangeTypes.DEL_INHERITEDFIELD, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeInheritedMethodChange(char typ, String methodShortName, String superTypeFullName, String subTypeFullName) {
/* 284 */     Vector<String> params = new Vector();
/* 285 */     params.add(methodShortName);
/* 286 */     params.add(superTypeFullName);
/* 287 */     params.add(subTypeFullName);
/* 288 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_INHERITEDMETHOD : 
/* 289 */       AtomicChange.ChangeTypes.DEL_INHERITEDMETHOD, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeMethodBodyChange(char typ, String methodFullName, String methodBody)
/*     */   {
/* 294 */     Vector<String> params = new Vector();
/* 295 */     params.add(methodFullName);
/* 296 */     params.add(methodBody);
/* 297 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_METHODBODY : 
/* 298 */       AtomicChange.ChangeTypes.DEL_METHODBODY, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeMethodArgsChange(String methodFullName, String methodSignature)
/*     */   {
/* 303 */     Vector<String> params = new Vector();
/* 304 */     params.add(methodFullName);
/* 305 */     params.add(methodSignature);
/* 306 */     return new AtomicChange(AtomicChange.ChangeTypes.CHANGE_METHODSIGNATURE, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeCallsChange(char typ, String callerMethodFullName, String calleeMethodFullName) {
/* 310 */     Vector<String> params = new Vector();
/* 311 */     params.add(callerMethodFullName);
/* 312 */     params.add(calleeMethodFullName);
/* 313 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_CALLS : 
/* 314 */       AtomicChange.ChangeTypes.DEL_CALLS, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeAccessesChange(char typ, String fieldFullName, String accessorMethodFullName) {
/* 318 */     Vector<String> params = new Vector();
/* 319 */     params.add(fieldFullName);
/* 320 */     params.add(accessorMethodFullName);
/* 321 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_ACCESSES : 
/* 322 */       AtomicChange.ChangeTypes.DEL_ACCESSES, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeCastChange(char typ, String expression, String type, String methodName) {
/* 326 */     Vector<String> params = new Vector();
/* 327 */     params.add(expression);
/* 328 */     params.add(type);
/* 329 */     params.add(methodName);
/* 330 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_CAST : 
/* 331 */       AtomicChange.ChangeTypes.DEL_CAST, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeTryCatchChange(char typ, String tryBlock, String catchClauses, String finallyBlock, String methodName) {
/* 335 */     Vector<String> params = new Vector();
/* 336 */     params.add(tryBlock);
/* 337 */     params.add(catchClauses);
/* 338 */     params.add(finallyBlock);
/* 339 */     params.add(methodName);
/* 340 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_TRYCATCH : 
/* 341 */       AtomicChange.ChangeTypes.DEL_TRYCATCH, params);
/*     */   }
/*     */   
/*     */   public static AtomicChange makeLocalVarChange(char typ, String methodQualifiedName, String typeQualifiedName, String identifier, String expression)
/*     */   {
/* 346 */     Vector<String> params = new Vector();
/* 347 */     params.add(methodQualifiedName);
/* 348 */     params.add(typeQualifiedName);
/* 349 */     params.add(identifier);
/* 350 */     params.add(expression);
/* 351 */     return new AtomicChange(typ == 'A' ? AtomicChange.ChangeTypes.ADD_LOCALVAR : 
/* 352 */       AtomicChange.ChangeTypes.DEL_LOCALVAR, params);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/changetypes/AtomicChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */