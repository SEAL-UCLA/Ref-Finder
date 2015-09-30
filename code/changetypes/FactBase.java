/*     */ package changetypes;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ public class FactBase
/*     */   extends HashSet<Fact>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public FactBase() {}
/*     */   
/*     */   public FactBase(FactBase f)
/*     */   {
/*  21 */     super(f);
/*     */   }
/*     */   
/*     */   public void print(PrintStream out) {
/*  25 */     if (size() > 0) {
/*  26 */       out.println("~~~Facts~~~");
/*  27 */       for (Fact f : this) {
/*  28 */         out.println(f.toString());
/*     */       }
/*     */     } else {
/*  31 */       out.println("No facts");
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getParentFromFullName(String name)
/*     */   {
/*  37 */     int lastdot = name.lastIndexOf('.');
/*  38 */     if (lastdot == -1)
/*  39 */       return "";
/*  40 */     return name.substring(0, lastdot);
/*     */   }
/*     */   
/*     */   private static String getChildFromFullName(String name) {
/*  44 */     int lastdot = name.lastIndexOf('.');
/*  45 */     return name.substring(lastdot + 1);
/*     */   }
/*     */   
/*     */   private void makeChangeFromFact(ChangeSet res, Fact f, char typ) {
/*  49 */     switch (f.type) {
/*     */     case ACCESSES: 
/*  51 */       res.add(AtomicChange.makePackageChange(typ, (String)f.params.get(0)));
/*  52 */       res.changecount[AtomicChange.ChangeTypes.ADD_PACKAGE.ordinal()] += 1;
/*  53 */       break;
/*     */     case CALLS: 
/*  55 */       res.add(AtomicChange.makeTypeChange(typ, (String)f.params.get(0), 
/*  56 */         (String)f.params.get(1), (String)f.params.get(2)));
/*  57 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*     */       
/*  59 */       res.add(AtomicChange.makePackageChange('M', (String)f.params.get(2)));
/*  60 */       res.changecount[AtomicChange.ChangeTypes.MOD_PACKAGE.ordinal()] += 1;
/*  61 */       break;
/*     */     case CAST: 
/*  63 */       res.add(AtomicChange.makeMethodChange(typ, (String)f.params.get(0), 
/*  64 */         (String)f.params.get(1), (String)f.params.get(2)));
/*  65 */       res.changecount[AtomicChange.ChangeTypes.ADD_METHOD.ordinal()] += 1;
/*     */       
/*  67 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(2), 
/*  68 */         getChildFromFullName((String)f.params.get(2)), 
/*  69 */         getParentFromFullName((String)f.params.get(2))));
/*  70 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*  71 */       break;
/*     */     case CONDITIONAL: 
/*  73 */       res.add(AtomicChange.makeFieldChange(typ, (String)f.params.get(0), 
/*  74 */         (String)f.params.get(1), (String)f.params.get(2)));
/*  75 */       res.changecount[AtomicChange.ChangeTypes.ADD_FIELD.ordinal()] += 1;
/*     */       
/*  77 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(2), 
/*  78 */         getChildFromFullName((String)f.params.get(2)), 
/*  79 */         getParentFromFullName((String)f.params.get(2))));
/*  80 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*  81 */       break;
/*     */     case EXTENDS: 
/*  83 */       res.add(AtomicChange.makeReturnsChange(typ, (String)f.params.get(0), 
/*  84 */         (String)f.params.get(1)));
/*  85 */       res.changecount[AtomicChange.ChangeTypes.ADD_RETURN.ordinal()] += 1;
/*     */       
/*  87 */       res.add(AtomicChange.makeMethodChange('M', (String)f.params.get(0), 
/*  88 */         getChildFromFullName((String)f.params.get(0)), 
/*  89 */         getParentFromFullName((String)f.params.get(0))));
/*  90 */       res.changecount[AtomicChange.ChangeTypes.ADD_METHOD.ordinal()] += 1;
/*     */       
/*  92 */       res.add(AtomicChange.makeTypeChange(
/*  93 */         'M', 
/*  94 */         getParentFromFullName((String)f.params.get(0)), 
/*  95 */         getChildFromFullName(getParentFromFullName((String)f.params.get(0))), 
/*  96 */         getParentFromFullName(getParentFromFullName((String)f.params.get(0)))));
/*  97 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*  98 */       break;
/*     */     case FIELD: 
/* 100 */       res.add(AtomicChange.makeFieldTypeChange(typ, (String)f.params.get(0), 
/* 101 */         (String)f.params.get(1)));
/* 102 */       res.changecount[AtomicChange.ChangeTypes.ADD_FIELDOFTYPE.ordinal()] += 1;
/*     */       
/* 104 */       res.add(AtomicChange.makeFieldChange('M', (String)f.params.get(0), 
/* 105 */         getChildFromFullName((String)f.params.get(0)), 
/* 106 */         getParentFromFullName((String)f.params.get(0))));
/* 107 */       res.changecount[AtomicChange.ChangeTypes.ADD_FIELD.ordinal()] += 1;
/*     */       
/* 109 */       res.add(AtomicChange.makeTypeChange(
/* 110 */         'M', 
/* 111 */         getParentFromFullName((String)f.params.get(0)), 
/* 112 */         getChildFromFullName(getParentFromFullName((String)f.params.get(0))), 
/* 113 */         getParentFromFullName(getParentFromFullName((String)f.params.get(0)))));
/* 114 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 115 */       break;
/*     */     case METHOD: 
/* 117 */       res.add(AtomicChange.makeTypeInTypeChange(typ, (String)f.params.get(0), 
/* 118 */         (String)f.params.get(1)));
/* 119 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPEINTYPE.ordinal()] += 1;
/*     */       
/* 121 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(0), 
/* 122 */         getChildFromFullName((String)f.params.get(0)), 
/* 123 */         getParentFromFullName((String)f.params.get(0))));
/* 124 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*     */       
/* 126 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(1), 
/* 127 */         getChildFromFullName((String)f.params.get(1)), 
/* 128 */         getParentFromFullName((String)f.params.get(1))));
/* 129 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 130 */       break;
/*     */     case GETTER: 
/* 132 */       res.add(AtomicChange.makeSubtypeChange(typ, (String)f.params.get(0), 
/* 133 */         (String)f.params.get(1)));
/* 134 */       res.changecount[AtomicChange.ChangeTypes.ADD_SUBTYPE.ordinal()] += 1;
/*     */       
/* 136 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(0), 
/* 137 */         getChildFromFullName((String)f.params.get(0)), 
/* 138 */         getParentFromFullName((String)f.params.get(0))));
/* 139 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*     */       
/* 141 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(1), 
/* 142 */         getChildFromFullName((String)f.params.get(1)), 
/* 143 */         getParentFromFullName((String)f.params.get(1))));
/* 144 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 145 */       break;
/*     */     case INHERITEDFIELD: 
/* 147 */       res.add(AtomicChange.makeImplementsChange(typ, (String)f.params.get(0), 
/* 148 */         (String)f.params.get(1)));
/* 149 */       res.changecount[AtomicChange.ChangeTypes.ADD_IMPLEMENTS.ordinal()] += 1;
/*     */       
/* 151 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(0), 
/* 152 */         getChildFromFullName((String)f.params.get(0)), 
/* 153 */         getParentFromFullName((String)f.params.get(0))));
/* 154 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*     */       
/* 156 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(1), 
/* 157 */         getChildFromFullName((String)f.params.get(1)), 
/* 158 */         getParentFromFullName((String)f.params.get(1))));
/* 159 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 160 */       break;
/*     */     case IMPLEMENTS: 
/* 162 */       res.add(AtomicChange.makeExtendsChange(typ, (String)f.params.get(0), 
/* 163 */         (String)f.params.get(1)));
/* 164 */       res.changecount[AtomicChange.ChangeTypes.ADD_EXTENDS.ordinal()] += 1;
/*     */       
/* 166 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(0), 
/* 167 */         getChildFromFullName((String)f.params.get(0)), 
/* 168 */         getParentFromFullName((String)f.params.get(0))));
/* 169 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/*     */       
/* 171 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(1), 
/* 172 */         getChildFromFullName((String)f.params.get(1)), 
/* 173 */         getParentFromFullName((String)f.params.get(1))));
/* 174 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 175 */       break;
/*     */     case INHERITEDMETHOD: 
/* 177 */       res.add(AtomicChange.makeInheritedFieldChange(typ, (String)f.params.get(0), 
/* 178 */         (String)f.params.get(1), (String)f.params.get(2)));
/* 179 */       res.changecount[AtomicChange.ChangeTypes.ADD_INHERITEDFIELD
/* 180 */         .ordinal()] += 1;
/*     */       
/* 182 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(2), 
/* 183 */         getChildFromFullName((String)f.params.get(2)), 
/* 184 */         getParentFromFullName((String)f.params.get(2))));
/* 185 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 186 */       break;
/*     */     case LOCALVAR: 
/* 188 */       res.add(AtomicChange.makeInheritedMethodChange(typ, 
/* 189 */         (String)f.params.get(0), (String)f.params.get(1), (String)f.params.get(2)));
/* 190 */       res.changecount[AtomicChange.ChangeTypes.ADD_INHERITEDMETHOD
/* 191 */         .ordinal()] += 1;
/*     */       
/* 193 */       res.add(AtomicChange.makeTypeChange('M', (String)f.params.get(2), 
/* 194 */         getChildFromFullName((String)f.params.get(2)), 
/* 195 */         getParentFromFullName((String)f.params.get(2))));
/* 196 */       res.changecount[AtomicChange.ChangeTypes.ADD_TYPE.ordinal()] += 1;
/* 197 */       break;
/*     */     case FIELDMODIFIER: 
/* 199 */       res.add(AtomicChange.makeAccessesChange(typ, (String)f.params.get(0), 
/* 200 */         (String)f.params.get(1)));
/* 201 */       res.changecount[AtomicChange.ChangeTypes.ADD_ACCESSES.ordinal()] += 1;
/* 202 */       break;
/*     */     case FIELDOFTYPE: 
/* 204 */       res.add(AtomicChange.makeCallsChange(typ, (String)f.params.get(0), 
/* 205 */         (String)f.params.get(1)));
/* 206 */       res.changecount[AtomicChange.ChangeTypes.ADD_CALLS.ordinal()] += 1;
/* 207 */       break;
/*     */     
/*     */     case METHODBODY: 
/* 210 */       res.add(AtomicChange.makeMethodBodyChange(typ, (String)f.params.get(0), 
/* 211 */         (String)f.params.get(1)));
/* 212 */       res.changecount[AtomicChange.ChangeTypes.ADD_METHODBODY.ordinal()] += 1;
/*     */       
/*     */ 
/* 215 */       break;
/*     */     case METHODMODIFIER: 
/* 217 */       res.add(AtomicChange.makeMethodArgsChange((String)f.params.get(0), 
/* 218 */         (String)f.params.get(1)));
/* 219 */       res.changecount[AtomicChange.ChangeTypes.CHANGE_METHODSIGNATURE
/* 220 */         .ordinal()] += 1;
/*     */       
/* 222 */       break;
/*     */     case METHODSIGNATURE: 
/* 224 */       res.add(AtomicChange.makeConditionalChange(typ, (String)f.params.get(0), 
/* 225 */         (String)f.params.get(1), (String)f.params.get(2), (String)f.params.get(3)));
/* 226 */       res.changecount[AtomicChange.ChangeTypes.ADD_CONDITIONAL.ordinal()] += 1;
/* 227 */       break;
/*     */     case PACKAGE: 
/* 229 */       res.add(AtomicChange.makeParameterChange(typ, (String)f.params.get(0), 
/* 230 */         (String)f.params.get(1), (String)f.params.get(2)));
/* 231 */       res.changecount[AtomicChange.ChangeTypes.ADD_PARAMETER.ordinal()] += 1;
/* 232 */       break;
/*     */     case THROWN: 
/* 234 */       res.add(AtomicChange.makeThrownExceptionChange(typ, 
/* 235 */         (String)f.params.get(0), (String)f.params.get(1)));
/* 236 */       res.changecount[AtomicChange.ChangeTypes.ADD_THROWN.ordinal()] += 1;
/* 237 */       break;
/*     */     case TRYCATCH: 
/* 239 */       res.add(AtomicChange.makeGetterChange(typ, (String)f.params.get(0), 
/* 240 */         (String)f.params.get(1)));
/* 241 */       res.changecount[AtomicChange.ChangeTypes.ADD_GETTER.ordinal()] += 1;
/* 242 */       break;
/*     */     case TYPE: 
/* 244 */       res.add(AtomicChange.makeSetterChange(typ, (String)f.params.get(0), 
/* 245 */         (String)f.params.get(1)));
/* 246 */       res.changecount[AtomicChange.ChangeTypes.ADD_SETTER.ordinal()] += 1;
/* 247 */       break;
/*     */     case PARAMETER: 
/* 249 */       res.add(AtomicChange.makeMethodModifierChange(typ, (String)f.params.get(0), 
/* 250 */         (String)f.params.get(1)));
/* 251 */       res.changecount[AtomicChange.ChangeTypes.ADD_METHODMODIFIER
/* 252 */         .ordinal()] += 1;
/* 253 */       break;
/*     */     case RETURN: 
/* 255 */       res.add(AtomicChange.makeFieldModifierChange(typ, (String)f.params.get(0), 
/* 256 */         (String)f.params.get(1)));
/* 257 */       res.changecount[AtomicChange.ChangeTypes.ADD_FIELDMODIFIER
/* 258 */         .ordinal()] += 1;
/* 259 */       break;
/*     */     case SETTER: 
/* 261 */       res.add(AtomicChange.makeCastChange(typ, (String)f.params.get(0), 
/* 262 */         (String)f.params.get(1), (String)f.params.get(2)));
/* 263 */       res.changecount[AtomicChange.ChangeTypes.ADD_CAST.ordinal()] += 1;
/* 264 */       break;
/*     */     case SUBTYPE: 
/* 266 */       res.add(AtomicChange.makeTryCatchChange(typ, (String)f.params.get(0), 
/* 267 */         (String)f.params.get(1), (String)f.params.get(2), (String)f.params.get(3)));
/* 268 */       res.changecount[AtomicChange.ChangeTypes.ADD_TRYCATCH.ordinal()] += 1;
/* 269 */       break;
/*     */     case TYPEINTYPE: 
/* 271 */       res.add(AtomicChange.makeLocalVarChange(typ, (String)f.params.get(0), 
/* 272 */         (String)f.params.get(1), (String)f.params.get(2), (String)f.params.get(3)));
/* 273 */       res.changecount[AtomicChange.ChangeTypes.ADD_LOCALVAR.ordinal()] += 1;
/* 274 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChangeSet diff(FactBase oldfacts)
/*     */   {
/* 284 */     ChangeSet res = new ChangeSet();
/* 285 */     FactBase added = new FactBase(this);
/* 286 */     added.removeAll(oldfacts);
/* 287 */     FactBase deleted = new FactBase(oldfacts);
/* 288 */     deleted.removeAll(this);
/*     */     
/* 290 */     Set<Fact> before_parameters = new HashSet();
/* 291 */     Set<Fact> after_parameters = new HashSet();
/*     */     
/*     */ 
/* 294 */     for (Fact f : added) {
/* 295 */       if (f.type == Fact.FactTypes.PARAMETER) {
/* 296 */         before_parameters.add(f);
/*     */       } else {
/* 298 */         makeChangeFromFact(res, f, 'A');
/*     */       }
/*     */     }
/*     */     
/* 302 */     for (Fact f : deleted) {
/* 303 */       if (f.type == Fact.FactTypes.PARAMETER) {
/* 304 */         after_parameters.add(f);
/*     */       } else {
/* 306 */         makeChangeFromFact(res, f, 'D');
/*     */       }
/*     */     }
/*     */     
/* 310 */     for (Fact f : before_parameters)
/*     */     {
/*     */ 
/* 313 */       for (Fact fd : after_parameters)
/*     */       {
/* 315 */         if (((String)fd.params.get(0)).equals(f.params.get(0)))
/*     */         {
/*     */ 
/*     */ 
/* 319 */           String[] new_params = ((String)f.params.get(1)).split(",");
/* 320 */           String[] old_params = ((String)fd.params.get(1)).split(",");
/*     */           
/* 322 */           Set<String> tempNew = new HashSet();
/* 323 */           Set<String> tempOld = new HashSet();
/*     */           
/* 325 */           int j = (localObject1 = new_params).length; for (int i = 0; i < j; i++) { String s = localObject1[i];
/* 326 */             if (!s.equals(""))
/* 327 */               tempNew.add(s); }
/* 328 */           j = (localObject1 = old_params).length; for (i = 0; i < j; i++) { String s = localObject1[i];
/* 329 */             if (!s.equals(""))
/* 330 */               tempOld.add(s);
/*     */           }
/* 332 */           if (tempNew.equals(tempOld)) {
/*     */             break;
/*     */           }
/* 335 */           Set<String> addedParams = new HashSet(tempNew);
/* 336 */           Object deletedParams = new HashSet(tempOld);
/*     */           
/* 338 */           addedParams.removeAll(tempOld);
/* 339 */           ((Set)deletedParams).removeAll(tempNew);
/*     */           
/* 341 */           for (Object localObject1 = addedParams.iterator(); ((Iterator)localObject1).hasNext();) { String add = (String)((Iterator)localObject1).next();
/* 342 */             Fact fNew = Fact.makeParameterFact((String)f.params.get(0), 
/* 343 */               (String)f.params.get(1), add);
/* 344 */             makeChangeFromFact(res, fNew, 'A');
/*     */           }
/*     */           
/* 347 */           for (localObject1 = ((Set)deletedParams).iterator(); ((Iterator)localObject1).hasNext();) { String rem = (String)((Iterator)localObject1).next();
/* 348 */             Fact fNew = Fact.makeParameterFact((String)f.params.get(0), 
/* 349 */               (String)f.params.get(1), rem);
/* 350 */             makeChangeFromFact(res, fNew, 'D');
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 355 */     res.normalize();
/* 356 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public void deriveFacts()
/*     */   {
/* 362 */     deriveInheritedMembers();
/* 363 */     deriveDefaultConstructors();
/*     */   }
/*     */   
/*     */   public void deriveRemoveExternalMethodsCalls() {
/* 367 */     System.out.print("Deriving remove external methods... ");
/*     */     
/*     */ 
/* 370 */     FactBase badMethodCalls = new FactBase();
/* 371 */     for (Fact f : this) {
/* 372 */       if (f.type == Fact.FactTypes.CALLS)
/*     */       {
/* 374 */         if (((String)f.params.get(1)).startsWith("junit.framework"))
/* 375 */           badMethodCalls.add(f);
/*     */       }
/*     */     }
/* 378 */     removeAll(badMethodCalls);
/*     */     
/* 380 */     System.out.println("OK");
/*     */   }
/*     */   
/*     */   public void deriveDefaultConstructors() {
/* 384 */     System.out.print("Deriving default constructors... ");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 390 */     Set<Fact> typefacts = new HashSet();
/* 391 */     Set<Fact> methodfacts = new HashSet();
/* 392 */     for (Fact f : this) {
/* 393 */       if (f.type == Fact.FactTypes.TYPE) {
/* 394 */         typefacts.add(f);
/* 395 */       } else if (f.type == Fact.FactTypes.METHOD) {
/* 396 */         methodfacts.add(f);
/*     */       }
/*     */     }
/*     */     
/* 400 */     for (Fact f : typefacts) {
/* 401 */       boolean found = false;
/* 402 */       if (!((String)f.params.get(3)).equals("interface"))
/*     */       {
/* 404 */         for (Fact f2 : methodfacts) {
/* 405 */           if ((((String)f2.params.get(1)).startsWith("<init>(")) && 
/* 406 */             (((String)f2.params.get(2)).equals(f.params.get(0)))) {
/* 407 */             found = true;
/* 408 */             break;
/*     */           }
/*     */         }
/* 411 */         if (!found) {
/* 412 */           Fact constfact = 
/* 413 */             Fact.makeMethodFact((String)f.params.get(0) + "#<init>()", 
/* 414 */             "<init>()", (String)f.params.get(0), "public");
/* 415 */           Fact returnfact = Fact.makeReturnsFact((String)f.params.get(0) + 
/* 416 */             "#<init>()", "void");
/* 417 */           add(constfact);
/* 418 */           add(returnfact);
/*     */         }
/*     */       } }
/* 421 */     System.out.println("OK");
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
/*     */   public void deriveInheritedMembers()
/*     */   {
/* 435 */     System.out.println("Deriving inheritance members... ");
/*     */     
/*     */ 
/* 438 */     Set<Fact> subtypefacts = new HashSet();
/* 439 */     Set<Fact> methodfacts = new HashSet();
/* 440 */     Set<Fact> fieldfacts = new HashSet();
/* 441 */     Set<Fact> inheritedmethodfacts = new HashSet();
/* 442 */     Set<Fact> inheritedfieldfacts = new HashSet();
/* 443 */     for (Fact f : this) {
/* 444 */       if (f.type == Fact.FactTypes.SUBTYPE) {
/* 445 */         subtypefacts.add(f);
/* 446 */       } else if (f.type == Fact.FactTypes.METHOD) {
/* 447 */         methodfacts.add(f);
/* 448 */       } else if (f.type == Fact.FactTypes.FIELD) {
/* 449 */         fieldfacts.add(f);
/*     */       }
/*     */     }
/* 452 */     Queue<Fact> worklist = new LinkedList();
/*     */     
/*     */ 
/* 455 */     System.out.print("  Checking for directly inherited methods... ");
/* 456 */     for (Fact a1 : methodfacts)
/* 457 */       if (a1.params.get(3) != "private")
/*     */       {
/* 459 */         if (!((String)a1.params.get(1)).startsWith("<init>("))
/*     */         {
/* 461 */           for (Fact a2 : subtypefacts)
/* 462 */             if (((String)a2.params.get(0)).equals(a1.params.get(2)))
/*     */             {
/* 464 */               Fact b1 = Fact.makeMethodFact("*", (String)a1.params.get(1), 
/* 465 */                 (String)a2.params.get(1), "*");
/* 466 */               if (!methodfacts.contains(b1))
/*     */               {
/*     */ 
/* 469 */                 Fact newfact = Fact.makeInheritedMethodFact((String)a1.params.get(1), 
/* 470 */                   (String)a2.params.get(0), (String)a2.params.get(1));
/* 471 */                 inheritedmethodfacts.add(newfact);
/*     */               }
/*     */             } } }
/* 474 */     System.out.println("OK");
/*     */     
/*     */ 
/* 477 */     System.out.print("  Checking for directly inherited fields... ");
/* 478 */     Fact a2; for (Fact a1 : fieldfacts)
/* 479 */       if (a1.params.get(3) != "private")
/*     */       {
/* 481 */         for (??? = subtypefacts.iterator(); ???.hasNext();) { a2 = (Fact)???.next();
/* 482 */           if (((String)a2.params.get(0)).equals(a1.params.get(2)))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 488 */             Fact newfact = Fact.makeInheritedFieldFact((String)a1.params.get(1), 
/* 489 */               (String)a2.params.get(0), (String)a2.params.get(1));
/* 490 */             inheritedfieldfacts.add(newfact);
/*     */           }
/*     */         } }
/* 493 */     System.out.println("OK");
/*     */     
/*     */ 
/*     */ 
/* 497 */     System.out.print("  Checking for indirectly inherited methods... ");
/* 498 */     worklist.clear();
/* 499 */     for (Fact f : inheritedmethodfacts) {
/* 500 */       worklist.add(f);
/*     */     }
/* 502 */     for (; worklist.size() > 0; 
/*     */         
/* 504 */         a2.hasNext())
/*     */     {
/* 503 */       Fact a1 = (Fact)worklist.poll();
/* 504 */       a2 = subtypefacts.iterator(); continue;a2 = (Fact)a2.next();
/* 505 */       if (((String)((Fact)a2).params.get(0)).equals(a1.params.get(2)))
/*     */       {
/* 507 */         Fact b1 = Fact.makeMethodFact("*", (String)a1.params.get(0), 
/* 508 */           (String)((Fact)a2).params.get(1), "*");
/* 509 */         if (!methodfacts.contains(b1))
/*     */         {
/* 511 */           Fact b2 = Fact.makeInheritedMethodFact((String)a1.params.get(0), 
/* 512 */             (String)a1.params.get(1), (String)((Fact)a2).params.get(1));
/* 513 */           if (!inheritedmethodfacts.contains(b2))
/*     */           {
/*     */ 
/* 516 */             Fact newfact = Fact.makeInheritedMethodFact((String)a1.params.get(0), 
/* 517 */               (String)a1.params.get(1), (String)((Fact)a2).params.get(1));
/* 518 */             worklist.add(newfact);
/* 519 */             inheritedmethodfacts.add(newfact);
/*     */           }
/*     */         } } }
/* 522 */     addAll(inheritedmethodfacts);
/* 523 */     System.out.println("OK");
/*     */     
/*     */ 
/* 526 */     System.out.print("  Checking for indirectly inherited fields... ");
/* 527 */     worklist.clear();
/* 528 */     for (Object a2 = inheritedfieldfacts.iterator(); ((Iterator)a2).hasNext();) { Fact f = (Fact)((Iterator)a2).next();
/* 529 */       worklist.add(f);
/*     */     }
/* 531 */     for (; worklist.size() > 0; 
/*     */         
/* 533 */         a2.hasNext())
/*     */     {
/* 532 */       Fact a1 = (Fact)worklist.poll();
/* 533 */       a2 = subtypefacts.iterator(); continue;Fact a2 = (Fact)a2.next();
/* 534 */       if (((String)a2.params.get(0)).equals(a1.params.get(2)))
/*     */       {
/* 536 */         Fact b1 = Fact.makeMethodFact("*", (String)a1.params.get(0), 
/* 537 */           (String)a2.params.get(1), "*");
/* 538 */         if (!methodfacts.contains(b1))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 544 */           Fact newfact = Fact.makeInheritedFieldFact((String)a1.params.get(0), 
/* 545 */             (String)a1.params.get(1), (String)a2.params.get(1));
/* 546 */           worklist.add(newfact);
/* 547 */           inheritedfieldfacts.add(newfact);
/*     */         }
/*     */       } }
/* 550 */     addAll(inheritedfieldfacts);
/* 551 */     System.out.println("OK");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/changetypes/FactBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */