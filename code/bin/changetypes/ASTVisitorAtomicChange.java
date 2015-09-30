/*     */ package changetypes;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ import org.eclipse.jdt.core.dom.ASTVisitor;
/*     */ import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Block;
/*     */ import org.eclipse.jdt.core.dom.CastExpression;
/*     */ import org.eclipse.jdt.core.dom.CatchClause;
/*     */ import org.eclipse.jdt.core.dom.ClassInstanceCreation;
/*     */ import org.eclipse.jdt.core.dom.CompilationUnit;
/*     */ import org.eclipse.jdt.core.dom.ConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.Expression;
/*     */ import org.eclipse.jdt.core.dom.FieldAccess;
/*     */ import org.eclipse.jdt.core.dom.IBinding;
/*     */ import org.eclipse.jdt.core.dom.IMethodBinding;
/*     */ import org.eclipse.jdt.core.dom.IPackageBinding;
/*     */ import org.eclipse.jdt.core.dom.ITypeBinding;
/*     */ import org.eclipse.jdt.core.dom.IVariableBinding;
/*     */ import org.eclipse.jdt.core.dom.IfStatement;
/*     */ import org.eclipse.jdt.core.dom.MethodDeclaration;
/*     */ import org.eclipse.jdt.core.dom.MethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.Name;
/*     */ import org.eclipse.jdt.core.dom.PackageDeclaration;
/*     */ import org.eclipse.jdt.core.dom.QualifiedName;
/*     */ import org.eclipse.jdt.core.dom.SimpleName;
/*     */ import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
/*     */ import org.eclipse.jdt.core.dom.Statement;
/*     */ import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
/*     */ import org.eclipse.jdt.core.dom.SuperMethodInvocation;
/*     */ import org.eclipse.jdt.core.dom.TryStatement;
/*     */ import org.eclipse.jdt.core.dom.Type;
/*     */ import org.eclipse.jdt.core.dom.TypeDeclaration;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/*     */ import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ASTVisitorAtomicChange
/*     */   extends ASTVisitor
/*     */ {
/*     */   public FactBase facts;
/*  55 */   private Map<String, IJavaElement> typeToFileMap_ = new HashMap();
/*     */   
/*     */   public Map<String, IJavaElement> getTypeToFileMap() {
/*  58 */     return Collections.unmodifiableMap(this.typeToFileMap_);
/*     */   }
/*     */   
/*     */ 
/*  62 */   private List<String> allowedFieldMods_ = Arrays.asList(new String[] {"public", "private", "protected", "static", "final" });
/*     */   
/*     */ 
/*  65 */   private Stack<IMethodBinding> mtbStack = new Stack();
/*  66 */   private Stack<ITypeBinding> itbStack = new Stack();
/*     */   
/*     */   public ASTVisitorAtomicChange() {
/*  69 */     this.facts = new FactBase();
/*     */   }
/*     */   
/*     */   public void printFacts(PrintStream out) {
/*  73 */     this.facts.print(out);
/*     */   }
/*     */   
/*     */   public boolean visit(PackageDeclaration node) {
/*     */     try {
/*  78 */       this.facts.add(Fact.makePackageFact(node.getName().toString()));
/*     */     } catch (Exception e) {
/*  80 */       System.err.println("Cannot resolve bindings for package " + 
/*  81 */         node.getName().toString());
/*     */     }
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   private static String removeParameters(String name) {
/*  87 */     int index = name.indexOf('<');
/*  88 */     if (index <= 0) {
/*  89 */       return name;
/*     */     }
/*  91 */     return name.substring(0, index);
/*     */   }
/*     */   
/*     */   private static String getModifier(IBinding ib)
/*     */   {
/*  96 */     if ((ib.getModifiers() & 0x1) > 0)
/*  97 */       return "public";
/*  98 */     if ((ib.getModifiers() & 0x4) > 0)
/*  99 */       return "protected";
/* 100 */     if ((ib.getModifiers() & 0x2) > 0) {
/* 101 */       return "private";
/*     */     }
/* 103 */     return "package";
/*     */   }
/*     */   
/*     */   private String edit_str(String str)
/*     */   {
/* 108 */     String newmBody_str = str.replace("{", "");
/* 109 */     newmBody_str = newmBody_str.replace("}", "");
/* 110 */     newmBody_str = newmBody_str.replace(" ", "");
/* 111 */     newmBody_str = newmBody_str.replace(";", "");
/* 112 */     return newmBody_str;
/*     */   }
/*     */   
/*     */   public boolean visit(IfStatement node)
/*     */   {
/* 117 */     Statement thenStmt = node.getThenStatement();
/* 118 */     Statement elseStmt = node.getElseStatement();
/* 119 */     Expression condExpr = node.getExpression();
/*     */     
/* 121 */     String thenStr = thenStmt.toString().replace('\n', ' ');
/* 122 */     String elseStr = "";
/*     */     
/* 124 */     if (elseStmt != null) {
/* 125 */       elseStr = elseStmt.toString().replace('\n', ' ');
/*     */     }
/*     */     
/* 128 */     if (this.mtbStack.isEmpty()) {
/* 129 */       return true;
/*     */     }
/* 131 */     IMethodBinding mtb = (IMethodBinding)this.mtbStack.peek();
/* 132 */     String methodStr = getQualifiedName(mtb);
/* 133 */     String condStr = condExpr.toString();
/*     */     
/* 135 */     condStr = edit_str(condStr);
/* 136 */     thenStr = edit_str(thenStr);
/* 137 */     elseStr = edit_str(elseStr);
/*     */     
/*     */     try
/*     */     {
/* 141 */       this.facts.add(Fact.makeConditionalFact(condStr, thenStr, elseStr, 
/* 142 */         methodStr));
/*     */     } catch (Exception e) {
/* 144 */       System.err.println("Cannot resolve conditional \"" + 
/* 145 */         condExpr.toString() + "\"");
/* 146 */       System.out.println("ifStmt: " + thenStr);
/* 147 */       System.out.println("elseStmt: " + elseStr);
/* 148 */       System.err.println(e.getMessage());
/*     */     }
/* 150 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void endVisit(IfStatement node) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean visit(CastExpression node)
/*     */   {
/* 162 */     if (this.mtbStack.isEmpty()) {
/* 163 */       return true;
/*     */     }
/* 165 */     Expression expression = node.getExpression();
/* 166 */     ITypeBinding type = node.getType().resolveBinding();
/* 167 */     IMethodBinding mtb = (IMethodBinding)this.mtbStack.peek();
/* 168 */     String exprStr = expression.toString();
/* 169 */     String typeStr = getQualifiedName(type);
/* 170 */     String methodStr = getQualifiedName(mtb);
/*     */     
/* 172 */     exprStr = edit_str(exprStr);
/*     */     
/* 174 */     this.facts.add(Fact.makeCastFact(exprStr, typeStr, methodStr));
/*     */     
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(TryStatement node) {
/* 180 */     if (this.mtbStack.isEmpty()) {
/* 181 */       return true;
/*     */     }
/* 183 */     String bodyStr = node.getBody() != null ? node.getBody().toString() : 
/* 184 */       "";
/* 185 */     bodyStr = edit_str(bodyStr);
/* 186 */     StringBuilder catchClauses = new StringBuilder();
/* 187 */     for (Object o : node.catchClauses()) {
/* 188 */       if (catchClauses.length() > 0)
/* 189 */         catchClauses.append(",");
/* 190 */       CatchClause c = (CatchClause)o;
/* 191 */       catchClauses.append(getQualifiedName(c.getException().getType()
/* 192 */         .resolveBinding()));
/* 193 */       catchClauses.append(":");
/* 194 */       if (c.getBody() != null)
/* 195 */         catchClauses.append(edit_str(c.getBody().toString()));
/*     */     }
/* 197 */     String finallyStr = node.getFinally() != null ? node.getFinally()
/* 198 */       .toString() : "";
/* 199 */     finallyStr = edit_str(finallyStr);
/*     */     
/* 201 */     IMethodBinding mtb = (IMethodBinding)this.mtbStack.peek();
/* 202 */     String methodStr = getQualifiedName(mtb);
/*     */     
/* 204 */     this.facts.add(Fact.makeTryCatchFact(bodyStr, catchClauses.toString(), 
/* 205 */       finallyStr, methodStr));
/*     */     
/* 207 */     return true;
/*     */   }
/*     */   
/*     */   private static String getQualifiedName(ITypeBinding itb) {
/* 211 */     if (itb.isPrimitive())
/* 212 */       return itb.getName();
/* 213 */     if (itb.isArray())
/*     */       try {
/* 215 */         StringBuilder suffix = new StringBuilder();
/* 216 */         for (int i = 0; i < itb.getDimensions(); i++) {
/* 217 */           suffix.append("[]");
/*     */         }
/* 219 */         return 
/* 220 */           getQualifiedName(itb.getElementType()) + suffix.toString();
/*     */       } catch (NullPointerException e) {
/* 222 */         return null;
/*     */       }
/* 224 */     if (itb.isNullType())
/* 225 */       return "null";
/* 226 */     if ((itb.isClass()) || (itb.isInterface())) {
/* 227 */       if (itb.isNested()) {
/* 228 */         String name = itb.getName();
/* 229 */         if (itb.isAnonymous())
/*     */         {
/* 231 */           String binname = itb.getBinaryName();
/* 232 */           int index = binname.indexOf('$');
/* 233 */           name = binname.substring(index + 1, binname.length());
/*     */         }
/* 235 */         return getQualifiedName(itb.getDeclaringClass()) + "#" + name;
/*     */       }
/*     */       try {
/* 238 */         String pkg = itb.getPackage().getName();
/* 239 */         String name = itb.getName();
/*     */         
/* 241 */         name = removeParameters(itb.getName());
/* 242 */         return pkg + "%." + name;
/*     */       } catch (NullPointerException e) {
/* 244 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 248 */     return "java.lang%.Object";
/*     */   }
/*     */   
/*     */   private static String getQualifiedName(IVariableBinding ivb)
/*     */   {
/*     */     try {
/* 254 */       String name = ivb.getName();
/* 255 */       return getQualifiedName(ivb.getDeclaringClass()) + "#" + name;
/*     */     } catch (NullPointerException e) {}
/* 257 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getQualifiedName(IMethodBinding imb)
/*     */   {
/* 265 */     return 
/* 266 */       getQualifiedName(imb.getDeclaringClass()) + "#" + getSimpleName(imb);
/*     */   }
/*     */   
/*     */   private static String getSimpleName(ITypeBinding itb) {
/* 270 */     if (itb.isNested()) {
/* 271 */       if (itb.isAnonymous()) {
/* 272 */         String binname = itb.getBinaryName();
/* 273 */         int index = binname.indexOf('$');
/* 274 */         String name = binname.substring(index + 1, binname.length());
/* 275 */         return getSimpleName(itb.getDeclaringClass()) + "#" + name;
/*     */       }
/* 277 */       return 
/* 278 */         getSimpleName(itb.getDeclaringClass()) + "#" + itb.getName();
/*     */     }
/*     */     
/* 281 */     return itb.getName();
/*     */   }
/*     */   
/*     */   private static String getSimpleName(IMethodBinding imb)
/*     */   {
/*     */     try
/*     */     {
/* 288 */       String name = imb.getName();
/* 289 */       if (imb.isConstructor())
/* 290 */         name = "<init>";
/* 291 */       String args = "";
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 296 */       args = "(" + args + ")";
/* 297 */       return name + args;
/*     */     } catch (NullPointerException e) {}
/* 299 */     return "";
/*     */   }
/*     */   
/*     */   public boolean visit(CompilationUnit node)
/*     */   {
/* 304 */     IJavaElement thisFile = node.getJavaElement();
/*     */     
/* 306 */     for (Object abstractTypeDeclaration : node.types()) {
/* 307 */       if ((abstractTypeDeclaration instanceof TypeDeclaration)) {
/* 308 */         TypeDeclaration td = (TypeDeclaration)abstractTypeDeclaration;
/* 309 */         this.typeToFileMap_.put(getQualifiedName(td.resolveBinding()), 
/* 310 */           thisFile);
/*     */       }
/*     */     }
/*     */     
/* 314 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(TypeDeclaration node) {
/* 318 */     ITypeBinding itb = node.resolveBinding();
/* 319 */     this.itbStack.push(itb);
/*     */     try
/*     */     {
/* 322 */       this.facts.add(Fact.makeTypeFact(getQualifiedName(itb), 
/* 323 */         getSimpleName(itb), itb.getPackage().getName(), 
/* 324 */         itb.isInterface() ? "interface" : "class"));
/*     */     } catch (Exception e) {
/* 326 */       System.err.println("Cannot resolve bindings for class " + 
/* 327 */         node.getName().toString());
/*     */     }
/*     */     ITypeBinding localITypeBinding1;
/*     */     ITypeBinding i2;
/*     */     try
/*     */     {
/* 333 */       ITypeBinding itb2 = itb.getSuperclass();
/* 334 */       if (itb.getSuperclass() != null) {
/* 335 */         this.facts.add(Fact.makeSubtypeFact(getQualifiedName(itb2), 
/* 336 */           getQualifiedName(itb)));
/* 337 */         this.facts.add(Fact.makeExtendsFact(getQualifiedName(itb2), 
/* 338 */           getQualifiedName(itb))); }
/*     */       ITypeBinding[] arrayOfITypeBinding;
/* 340 */       int i; if (node.isInterface()) {
/* 341 */         i = (arrayOfITypeBinding = itb.getInterfaces()).length; for (localITypeBinding1 = 0; localITypeBinding1 < i; localITypeBinding1++) { ITypeBinding i2 = arrayOfITypeBinding[localITypeBinding1];
/* 342 */           this.facts.add(Fact.makeSubtypeFact(getQualifiedName(i2), 
/* 343 */             getQualifiedName(itb)));
/* 344 */           this.facts.add(Fact.makeExtendsFact(getQualifiedName(i2), 
/* 345 */             getQualifiedName(itb)));
/*     */         }
/*     */       } else {
/* 348 */         i = (arrayOfITypeBinding = itb.getInterfaces()).length; for (localITypeBinding1 = 0; localITypeBinding1 < i; localITypeBinding1++) { i2 = arrayOfITypeBinding[localITypeBinding1];
/* 349 */           this.facts.add(Fact.makeSubtypeFact(getQualifiedName(i2), 
/* 350 */             getQualifiedName(itb)));
/* 351 */           this.facts.add(Fact.makeImplementsFact(getQualifiedName(i2), 
/* 352 */             getQualifiedName(itb)));
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 356 */       System.err.println("Cannot resolve super class bindings for class " + 
/* 357 */         node.getName().toString());
/*     */     }
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 362 */       localITypeBinding1 = (localObject = itb.getDeclaredFields()).length; for (i2 = 0; i2 < localITypeBinding1; i2++) { IVariableBinding ivb = localObject[i2];
/* 363 */         String visibility = getModifier(ivb);
/* 364 */         String fieldStr = ivb.toString();
/*     */         
/* 366 */         String[] tokens = fieldStr.split(" ");
/* 367 */         String[] arrayOfString1; int k = (arrayOfString1 = tokens).length; for (int j = 0; j < k; j++) { String token = arrayOfString1[j];
/* 368 */           if (this.allowedFieldMods_.contains(token)) {
/* 369 */             this.facts.add(Fact.makeFieldModifierFact(
/* 370 */               getQualifiedName(ivb), token));
/*     */           }
/*     */         }
/* 373 */         this.facts.add(Fact.makeFieldFact(getQualifiedName(ivb), 
/* 374 */           ivb.getName(), getQualifiedName(itb), visibility));
/* 375 */         if (!ivb.getType().isParameterizedType()) {
/* 376 */           this.facts.add(Fact.makeFieldTypeFact(getQualifiedName(ivb), 
/* 377 */             getQualifiedName(ivb.getType())));
/*     */         } else {
/* 379 */           this.facts.add(Fact.makeFieldTypeFact(getQualifiedName(ivb), 
/* 380 */             makeParameterizedName(ivb)));
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 384 */       System.err.println("Cannot resolve field bindings for class " + 
/* 385 */         node.getName().toString());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 390 */       ITypeBinding localITypeBinding2 = (localObject = node.getTypes()).length; for (i2 = 0; i2 < localITypeBinding2; i2++) { TypeDeclaration t = localObject[i2];
/* 391 */         ITypeBinding intb = t.resolveBinding();
/* 392 */         this.facts.add(Fact.makeTypeInTypeFact(getQualifiedName(intb), 
/* 393 */           getQualifiedName(itb)));
/*     */       }
/*     */     } catch (Exception e) {
/* 396 */       System.err.println("Cannot resolve inner type bindings for class " + 
/* 397 */         node.getName().toString());
/*     */     }
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
/* 411 */     return true;
/*     */   }
/*     */   
/*     */   private String makeParameterizedName(IVariableBinding ivb) {
/* 415 */     StringBuilder sb = new StringBuilder();
/* 416 */     sb.append(getQualifiedName(ivb.getType()) + "<");
/* 417 */     boolean comma = false;
/* 418 */     ITypeBinding[] arrayOfITypeBinding; int j = (arrayOfITypeBinding = ivb.getType().getTypeArguments()).length; for (int i = 0; i < j; i++) { ITypeBinding itb = arrayOfITypeBinding[i];
/* 419 */       if (comma)
/* 420 */         sb.append(",");
/* 421 */       sb.append(getQualifiedName(itb));
/* 422 */       comma = true;
/*     */     }
/* 424 */     sb.append(">");
/* 425 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void endVisit(TypeDeclaration node) {
/* 429 */     this.itbStack.pop();
/*     */   }
/*     */   
/*     */   public boolean visit(AnonymousClassDeclaration node) {
/* 433 */     ITypeBinding itb = node.resolveBinding();
/* 434 */     this.itbStack.push(itb);
/*     */     
/*     */     try
/*     */     {
/* 438 */       this.facts.add(Fact.makeTypeFact(getQualifiedName(itb), 
/* 439 */         getSimpleName(itb), itb.getPackage().getName(), 
/* 440 */         itb.isInterface() ? "interface" : "class"));
/*     */     } catch (Exception e) {
/* 442 */       System.err.println("Cannot resolve bindings for anonymous class " + 
/* 443 */         itb.getName());
/*     */     }
/*     */     Object localObject;
/*     */     int j;
/*     */     int i;
/*     */     try {
/* 449 */       try { this.facts.add(Fact.makeSubtypeFact(
/* 450 */           getQualifiedName(itb.getSuperclass()), 
/* 451 */           getQualifiedName(itb)));
/* 452 */         this.facts.add(Fact.makeExtendsFact(
/* 453 */           getQualifiedName(itb.getSuperclass()), 
/* 454 */           getQualifiedName(itb)));
/*     */       } catch (NullPointerException e) {
/* 456 */         return false;
/*     */       }
/* 458 */       j = (localObject = itb.getInterfaces()).length; for (i = 0; i < j; i++) { ITypeBinding i2 = localObject[i];
/*     */         try {
/* 460 */           this.facts.add(Fact.makeSubtypeFact(getQualifiedName(i2), 
/* 461 */             getQualifiedName(itb)));
/* 462 */           this.facts.add(Fact.makeImplementsFact(getQualifiedName(i2), 
/* 463 */             getQualifiedName(itb)));
/*     */         } catch (NullPointerException e) {
/* 465 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 470 */       System.err.println("Cannot resolve super class bindings for anonymous class " + 
/* 471 */         itb.getName());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 476 */       j = (localObject = itb.getDeclaredFields()).length; for (i = 0; i < j; i++) { IVariableBinding ivb = localObject[i];
/* 477 */         String visibility = getModifier(ivb);
/* 478 */         this.facts.add(Fact.makeFieldFact(getQualifiedName(ivb), 
/* 479 */           ivb.getName(), getQualifiedName(itb), visibility));
/* 480 */         this.facts.add(Fact.makeFieldTypeFact(getQualifiedName(ivb), 
/* 481 */           getQualifiedName(ivb.getType())));
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 485 */       System.err.println("Cannot resolve field bindings for anonymous class " + 
/* 486 */         itb.getName());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 491 */       if (itb.isNested()) {
/* 492 */         this.facts.add(Fact.makeTypeInTypeFact(getQualifiedName(itb), 
/* 493 */           getQualifiedName(itb.getDeclaringClass())));
/*     */       }
/*     */     } catch (Exception e) {
/* 496 */       System.err.println("Cannot resolve inner type for anonymous class " + 
/* 497 */         itb.getName());
/*     */     }
/*     */     
/* 500 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(AnonymousClassDeclaration node) {
/* 504 */     this.itbStack.pop();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean visit(MethodDeclaration node)
/*     */   {
/* 510 */     IMethodBinding mtb = node.resolveBinding();
/* 511 */     this.mtbStack.push(mtb);
/* 512 */     String nodeStr = node.toString();
/*     */     
/*     */ 
/* 515 */     String modifier = "protected";
/* 516 */     int dex = nodeStr.indexOf(' ');
/* 517 */     if (dex >= 0) {
/* 518 */       String temp = nodeStr.substring(0, dex);
/* 519 */       if (temp.equals("public")) {
/* 520 */         modifier = "public";
/* 521 */       } else if (temp.equals("private")) {
/* 522 */         modifier = "private";
/*     */       }
/*     */     }
/*     */     try {
/* 526 */       String visibility = getModifier(mtb);
/* 527 */       this.facts.add(Fact.makeMethodFact(getQualifiedName(mtb), 
/* 528 */         getSimpleName(mtb), 
/* 529 */         getQualifiedName(mtb.getDeclaringClass()), visibility));
/*     */     }
/*     */     catch (Exception e) {
/* 532 */       System.err.println("Cannot resolve return method bindings for method " + 
/* 533 */         node.getName().toString());
/*     */     }
/*     */     try
/*     */     {
/* 537 */       String returntype = getQualifiedName(mtb.getReturnType());
/* 538 */       this.facts.add(Fact.makeReturnsFact(getQualifiedName(mtb), returntype));
/*     */     }
/*     */     catch (Exception e) {
/* 541 */       System.err.println("Cannot resolve return type bindings for method " + 
/* 542 */         node.getName().toString());
/*     */     }
/*     */     try
/*     */     {
/* 546 */       this.facts.add(Fact.makeModifierMethodFact(getQualifiedName(mtb), 
/* 547 */         modifier));
/*     */     }
/*     */     catch (Exception e) {
/* 550 */       System.err.println("Cannot resolve return type bindings for method modifier " + 
/* 551 */         node.getName().toString());
/*     */     }
/*     */     try {
/* 554 */       String bodystring = node.getBody() != null ? node.getBody()
/* 555 */         .toString() : "";
/* 556 */       bodystring = bodystring.replace('\n', ' ');
/*     */       
/* 558 */       bodystring = bodystring.replace('"', ' ');
/* 559 */       bodystring = bodystring.replace('"', ' ');
/* 560 */       bodystring = bodystring.replace('\\', ' ');
/*     */       
/*     */ 
/*     */ 
/* 564 */       this.facts.add(
/* 565 */         Fact.makeMethodBodyFact(getQualifiedName(mtb), bodystring));
/*     */     } catch (Exception e) {
/* 567 */       System.err.println("Cannot resolve bindings for body");
/*     */     }
/*     */     SingleVariableDeclaration param;
/* 570 */     try { List<SingleVariableDeclaration> parameters = node.parameters();
/*     */       
/* 572 */       StringBuilder sb = new StringBuilder();
/* 573 */       for (Iterator localIterator = parameters.iterator(); localIterator.hasNext();) { param = (SingleVariableDeclaration)localIterator.next();
/* 574 */         if (sb.length() != 0)
/* 575 */           sb.append(", ");
/* 576 */         sb.append(param.getType().toString());
/* 577 */         sb.append(":");
/* 578 */         sb.append(param.getName().toString());
/*     */       }
/*     */       
/*     */ 
/* 582 */       this.facts.add(Fact.makeParameterFact(getQualifiedName(mtb), 
/* 583 */         sb.toString(), ""));
/*     */     } catch (Exception e) {
/* 585 */       System.err.println("Cannot resolve bindings for parameters");
/*     */     }
/*     */     try
/*     */     {
/* 589 */       List<Name> thrownTypes = node.thrownExceptions();
/* 590 */       for (Name n : thrownTypes) {
/* 591 */         this.facts.add(Fact.makeThrownExceptionFact(getQualifiedName(mtb), 
/* 592 */           getQualifiedName(n.resolveTypeBinding())));
/*     */       }
/*     */     } catch (Exception e) {
/* 595 */       System.err.println("Cannot resolve bindings for exceptions");
/*     */     }
/*     */     
/* 598 */     return true;
/*     */   }
/*     */   
/*     */   public void endVisit(MethodDeclaration node) {
/* 602 */     this.mtbStack.pop();
/*     */   }
/*     */   
/*     */   public boolean visit(FieldAccess node) {
/* 606 */     IVariableBinding ivb = node.resolveFieldBinding();
/*     */     
/* 608 */     if (this.mtbStack.isEmpty()) {
/* 609 */       return true;
/*     */     }
/* 611 */     IMethodBinding mtb = (IMethodBinding)this.mtbStack.peek();
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 617 */       if ((!node.getName().toString().equals("length")) || 
/* 618 */         (ivb.getDeclaringClass() != null))
/*     */       {
/*     */ 
/* 621 */         this.facts.add(Fact.makeAccessesFact(
/* 622 */           getQualifiedName(node.resolveFieldBinding()), 
/* 623 */           getQualifiedName(mtb)));
/*     */       }
/*     */     } catch (Exception e) {
/* 626 */       System.err.println("Cannot resolve field access \"" + 
/* 627 */         node.getName().toString() + "\"");
/*     */     }
/*     */     try
/*     */     {
/* 631 */       String simpleMethodName = getSimpleName(mtb);
/* 632 */       if (simpleMethodName.toLowerCase().startsWith("get")) {
/* 633 */         this.facts.add(Fact.makeGetterFact(getQualifiedName(mtb), 
/* 634 */           getQualifiedName(node.resolveFieldBinding())));
/* 635 */       } else if (simpleMethodName.toLowerCase().startsWith("set")) {
/* 636 */         this.facts.add(Fact.makeSetterFact(getQualifiedName(mtb), 
/* 637 */           getQualifiedName(node.resolveFieldBinding())));
/*     */       }
/*     */     } catch (Exception e) {
/* 640 */       System.err.println("Cannot resolve bindings for exceptions");
/*     */     }
/* 642 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(SimpleName node) {
/* 646 */     if ((this.mtbStack.isEmpty()) && (!this.itbStack.isEmpty())) {
/* 647 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 655 */     if (!this.mtbStack.isEmpty()) {
/* 656 */       if (node.getIdentifier().equals("length"))
/* 657 */         return false;
/*     */       try {
/* 659 */         return visitName(node.resolveBinding(), (IMethodBinding)this.mtbStack.peek());
/*     */       } catch (Exception e) {
/* 661 */         System.err.println("Cannot resolve simple name \"" + 
/* 662 */           node.getFullyQualifiedName().toString() + "\"");
/* 663 */         return false;
/*     */       }
/*     */     }
/* 666 */     return false;
/*     */   }
/*     */   
/*     */   public boolean visit(QualifiedName node) {
/* 670 */     if ((this.mtbStack.isEmpty()) && (!this.itbStack.isEmpty()))
/* 671 */       return false;
/* 672 */     if (!this.mtbStack.isEmpty()) {
/* 673 */       if (node.getName().getIdentifier().equals("length")) {
/* 674 */         return true;
/*     */       }
/*     */       try {
/* 677 */         return visitName(node.resolveBinding(), (IMethodBinding)this.mtbStack.peek());
/*     */       } catch (Exception e) {
/* 679 */         System.err.println("Cannot resolve qualified name \"" + 
/* 680 */           node.getFullyQualifiedName().toString() + "\"");
/* 681 */         return false;
/*     */       }
/*     */     }
/* 684 */     return false;
/*     */   }
/*     */   
/*     */   private boolean visitName(IBinding ib, IMethodBinding iMethodBinding) throws Exception
/*     */   {
/* 689 */     switch (ib.getKind()) {
/*     */     case 3: 
/* 691 */       IVariableBinding ivb = (IVariableBinding)ib;
/* 692 */       if (ivb.isField()) {
/* 693 */         this.facts.add(Fact.makeAccessesFact(getQualifiedName(ivb), 
/* 694 */           getQualifiedName(iMethodBinding)));
/*     */         try
/*     */         {
/* 697 */           String simpleMethodName = getSimpleName(iMethodBinding);
/* 698 */           if (simpleMethodName.toLowerCase().startsWith("get")) {
/* 699 */             this.facts.add(Fact.makeGetterFact(
/* 700 */               getQualifiedName(iMethodBinding), 
/* 701 */               getQualifiedName(ivb)));
/* 702 */           } else if (simpleMethodName.toLowerCase().startsWith("set")) {
/* 703 */             this.facts.add(Fact.makeSetterFact(
/* 704 */               getQualifiedName(iMethodBinding), 
/* 705 */               getQualifiedName(ivb)));
/*     */           }
/*     */         }
/*     */         catch (Exception e) {
/* 709 */           System.err.println("Cannot resolve bindings for exceptions");
/*     */         }
/*     */       }
/*     */       
/*     */       break;
/*     */     }
/*     */     
/* 716 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean visit(MethodInvocation node)
/*     */   {
/* 735 */     IMethodBinding mmtb = node.resolveMethodBinding();
/*     */     
/* 737 */     if (this.mtbStack.isEmpty()) {
/* 738 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 744 */       if (node.getExpression() != null)
/*     */       {
/* 746 */         if (mmtb.getDeclaringClass().getQualifiedName().startsWith("java.awt.geom.Path2D"))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 753 */           Expression e = node.getExpression();
/* 754 */           ITypeBinding itb = e.resolveTypeBinding();
/* 755 */           this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 756 */             getQualifiedName(itb) + "#" + getSimpleName(mmtb)));
/*     */           break label179; } }
/* 758 */       this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 759 */         getQualifiedName(mmtb)));
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 765 */       System.err.println("Cannot resolve method invocation \"" + 
/* 766 */         node.getName().toString() + "\""); }
/*     */     label179:
/* 768 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(SuperMethodInvocation node) {
/* 772 */     IMethodBinding mmtb = node.resolveMethodBinding();
/*     */     
/* 774 */     if (this.mtbStack.isEmpty()) {
/* 775 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 779 */       this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 780 */         getQualifiedName(mmtb)));
/*     */     } catch (Exception e) {
/* 782 */       System.err.println("Cannot resolve method invocation \"" + 
/* 783 */         node.getName().toString() + "\"");
/*     */     }
/* 785 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(ClassInstanceCreation node) {
/* 789 */     IMethodBinding mmtb = node.resolveConstructorBinding();
/*     */     
/* 791 */     if (this.mtbStack.isEmpty()) {
/* 792 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 796 */       this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 797 */         getQualifiedName(mmtb)));
/*     */     } catch (Exception e) {
/* 799 */       System.err.println("Cannot resolve class instance creation \"" + 
/* 800 */         node.getType().toString() + "\"");
/*     */     }
/* 802 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(ConstructorInvocation node) {
/* 806 */     IMethodBinding mmtb = node.resolveConstructorBinding();
/*     */     
/* 808 */     if (this.mtbStack.isEmpty()) {
/* 809 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 813 */       this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 814 */         getQualifiedName(mmtb)));
/*     */     } catch (Exception e) {
/* 816 */       System.err.println("Cannot resolve constructor invocation in \"\"");
/*     */     }
/*     */     
/* 819 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(SuperConstructorInvocation node) {
/* 823 */     IMethodBinding mmtb = node.resolveConstructorBinding();
/*     */     
/* 825 */     if (this.mtbStack.isEmpty()) {
/* 826 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 830 */       this.facts.add(Fact.makeCallsFact(getQualifiedName((IMethodBinding)this.mtbStack.peek()), 
/* 831 */         getQualifiedName(mmtb)));
/*     */     }
/*     */     catch (Exception e) {
/* 834 */       System.err.println("Cannot resolve super constructor invocation in \"\"");
/*     */     }
/*     */     
/* 837 */     return true;
/*     */   }
/*     */   
/*     */   public boolean visit(VariableDeclarationStatement vds) {
/* 841 */     if (this.mtbStack.isEmpty()) {
/* 842 */       return true;
/*     */     }
/*     */     
/* 845 */     for (Object ovdf : vds.fragments()) {
/* 846 */       VariableDeclarationFragment vdf = (VariableDeclarationFragment)ovdf;
/*     */       try {
/* 848 */         this.facts.add(Fact.makeLocalVarFact(getQualifiedName(
/* 849 */           (IMethodBinding)this.mtbStack.peek()), getQualifiedName(vds.getType()
/* 850 */           .resolveBinding()), vdf.getName().getIdentifier(), vdf
/* 851 */           .getInitializer().toString()));
/*     */       } catch (Exception e) {
/* 853 */         System.err.println("Cannot resolve variable declaration \"" + 
/* 854 */           vdf.getName().toString() + "\"");
/*     */       }
/*     */     }
/* 857 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/changetypes/ASTVisitorAtomicChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */