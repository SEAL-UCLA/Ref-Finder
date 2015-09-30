/*     */ package lsclipse;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import lsclipse.rules.ChangeBidirectionalAssociationToUni;
/*     */ import lsclipse.rules.ChangeUnidirectionalAssociationToBi;
/*     */ import lsclipse.rules.ConsolidateConditionalExpression;
/*     */ import lsclipse.rules.ConsolidateDuplicateConditionalFragment;
/*     */ import lsclipse.rules.DecomposeConditional;
/*     */ import lsclipse.rules.EncapsulateCollection;
/*     */ import lsclipse.rules.ExtractMethod;
/*     */ import lsclipse.rules.FormTemplateMethod;
/*     */ import lsclipse.rules.InlineMethod;
/*     */ import lsclipse.rules.InlineTemp;
/*     */ import lsclipse.rules.IntroduceAssertion;
/*     */ import lsclipse.rules.IntroduceExplainingVariable;
/*     */ import lsclipse.rules.IntroduceNullObject;
/*     */ import lsclipse.rules.IntroduceParamObject;
/*     */ import lsclipse.rules.MoveMethod;
/*     */ import lsclipse.rules.ParameterizeMethod;
/*     */ import lsclipse.rules.PreserveWholeObject;
/*     */ import lsclipse.rules.RemoveAssignmentToParameters;
/*     */ import lsclipse.rules.RemoveControlFlag;
/*     */ import lsclipse.rules.RenameMethod;
/*     */ import lsclipse.rules.ReplaceArrayWithObject;
/*     */ import lsclipse.rules.ReplaceConditionalWithPolymorphism;
/*     */ import lsclipse.rules.ReplaceDataValueWithObject;
/*     */ import lsclipse.rules.ReplaceExceptionWithTest;
/*     */ import lsclipse.rules.ReplaceMethodWithMethodObject;
/*     */ import lsclipse.rules.ReplaceNestedCondWithGuardClauses;
/*     */ import lsclipse.rules.ReplaceParameterWithExplicitMethods;
/*     */ import lsclipse.rules.ReplaceSubclassWithField;
/*     */ import lsclipse.rules.ReplaceTypeCodeWithState;
/*     */ import lsclipse.rules.ReplaceTypeCodeWithSubclasses;
/*     */ import lsclipse.rules.Rule;
/*     */ import lsclipse.rules.RuleFactory;
/*     */ import lsclipse.rules.SeparateQueryFromModifier;
/*     */ import metapackage.MetaInfo;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.tdbc.Connection;
/*     */ import tyRuBa.tdbc.PreparedQuery;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
/*     */ 
/*     */ public class TopologicalSort
/*     */ {
/*  60 */   FrontEnd frontend = null;
/*  61 */   private RuleFactory ruleFactory = new RuleFactory();
/*  62 */   boolean loadInitFile = true;
/*  63 */   File dbDir = null;
/*  64 */   int cachesize = 5000;
/*  65 */   private boolean backgroundPageCleaning = false;
/*     */   
/*  67 */   public Map<String, List<String>> dependents = new HashMap();
/*     */   private Set<Node> graph;
/*     */   
/*  70 */   public Set<Node> getGraph() { return Collections.unmodifiableSet(this.graph); }
/*     */   
/*     */ 
/*     */   private ArrayList<String> written_strs;
/*     */   
/*     */   public TopologicalSort()
/*     */   {
/*  77 */     this.graph = new java.util.HashSet();
/*  78 */     this.written_strs = new ArrayList();
/*     */     
/*  80 */     RefactoringQuery move_field = new RefactoringQuery(
/*  81 */       "move_field", 
/*  82 */       "deleted_field(?fFullName, ?fShortName, ?tFullName), added_field(?f1FullName, ?fShortName, ?t1FullName), deleted_accesses(?fFullName, ?mFullName), added_accesses(?f1FullName, ?nFullName), before_type(?tFullName, ?, ?package), after_type(?t1FullName, ?, ?package), NOT(equals(?tFullName, ?t1FullName))");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     move_field.addType("?fShortName");
/*  90 */     move_field.addType("?tFullName");
/*  91 */     move_field.addType("?t1FullName");
/*  92 */     Node move_field_node = new Node(move_field);
/*  93 */     this.graph.add(move_field_node);
/*     */     
/*  95 */     Node move_method_node = new Node(
/*  96 */       new MoveMethod().getRefactoringQuery());
/*  97 */     this.graph.add(move_method_node);
/*     */     
/*  99 */     Node rename_method_node = new Node(
/* 100 */       new RenameMethod().getRefactoringQuery());
/* 101 */     this.graph.add(rename_method_node);
/*     */     
/* 103 */     String push_down_field_query = "move_field(?fShortName, ?tParentFullName, ?tChildFullName), before_subtype(?tParentFullName, ?tChildFullName)";
/*     */     
/* 105 */     RefactoringQuery push_down_field = new RefactoringQuery(
/* 106 */       "push_down_field", push_down_field_query);
/* 107 */     push_down_field.addType("?fShortName");
/* 108 */     push_down_field.addType("?tParentFullName");
/* 109 */     push_down_field.addType("?tChildFullName");
/* 110 */     Node push_down_field_node = new Node(push_down_field);
/* 111 */     push_down_field_node.addChild(move_field_node);
/* 112 */     this.graph.add(push_down_field_node);
/*     */     
/* 114 */     String push_down_method_query = "move_method(?mShortName, ?tParentFullName, ?tChildFullName), before_subtype(?tParentFullName, ?tChildFullName)";
/*     */     
/* 116 */     RefactoringQuery push_down_method = new RefactoringQuery(
/* 117 */       "push_down_method", push_down_method_query);
/* 118 */     push_down_method.addType("?mShortName");
/* 119 */     push_down_method.addType("?tParentFullName");
/* 120 */     push_down_method.addType("?tChildFullName");
/* 121 */     Node push_down_method_node = new Node(push_down_method);
/* 122 */     push_down_method_node.addChild(move_method_node);
/* 123 */     this.graph.add(push_down_method_node);
/*     */     
/* 125 */     Node extract_method_node = new Node(
/* 126 */       new ExtractMethod().getRefactoringQuery());
/* 127 */     this.graph.add(extract_method_node);
/*     */     
/* 129 */     Node decompose_conditional_node = new Node(
/* 130 */       new DecomposeConditional().getRefactoringQuery());
/* 131 */     decompose_conditional_node.addChild(extract_method_node);
/* 132 */     this.graph.add(decompose_conditional_node);
/*     */     
/* 134 */     String pull_up_field_query = "move_field(?fShortName, ?tChildFullName, ?tParentFullName), before_subtype(?tParentFullName, ?tChildFullName)";
/*     */     
/* 136 */     RefactoringQuery pull_up_field = new RefactoringQuery("pull_up_field", 
/* 137 */       pull_up_field_query);
/* 138 */     pull_up_field.addType("?fShortName");
/* 139 */     pull_up_field.addType("?tChildFullName");
/* 140 */     pull_up_field.addType("?tParentFullName");
/* 141 */     Node pull_up_field_node = new Node(pull_up_field);
/* 142 */     pull_up_field_node.addChild(move_field_node);
/* 143 */     this.graph.add(pull_up_field_node);
/*     */     
/* 145 */     String pull_up_method_query = "move_method(?mShortName, ?tChildFullName, ?tParentFullName), before_subtype(?tParentFullName, ?tChildFullName)";
/*     */     
/* 147 */     RefactoringQuery pull_up_method = new RefactoringQuery(
/* 148 */       "pull_up_method", pull_up_method_query);
/* 149 */     pull_up_method.addType("?mShortName");
/* 150 */     pull_up_method.addType("?tChildFullName");
/* 151 */     pull_up_method.addType("?tParentFullName");
/* 152 */     Node pull_up_method_node = new Node(pull_up_method);
/* 153 */     pull_up_method_node.addChild(move_method_node);
/* 154 */     this.graph.add(pull_up_method_node);
/*     */     
/*     */ 
/*     */ 
/* 158 */     String collapse_hierarchy_query = "(deleted_subtype(?tParentFullName, ?tChildFullName), (pull_up_field(?fuShortName, ?tChildFullName, ?tParentFullName);pull_up_method(?muShortName, ?tChildFullName, ?tParentFullName)));(before_subtype(?tParentFullName, ?tChildFullName),deleted_type(?tParentFullName, ?tParentShortName, ?),(push_down_field(?fdShortName, ?tParentFullName, ?tChildFullName);push_down_method(?mdShortName, ?tParentFullName, ?tChildFullName)))";
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
/* 171 */     RefactoringQuery collapse_hierarchy = new RefactoringQuery(
/* 172 */       "collapse_hierarchy", collapse_hierarchy_query);
/* 173 */     collapse_hierarchy.addType("?tParentFullName");
/* 174 */     collapse_hierarchy.addType("?tChildFullName");
/* 175 */     Node collapse_hierarchy_node = new Node(collapse_hierarchy);
/* 176 */     collapse_hierarchy_node.addChild(push_down_method_node);
/* 177 */     collapse_hierarchy_node.addChild(push_down_field_node);
/* 178 */     collapse_hierarchy_node.addChild(pull_up_method_node);
/* 179 */     collapse_hierarchy_node.addChild(pull_up_field_node);
/* 180 */     this.graph.add(collapse_hierarchy_node);
/*     */     
/* 182 */     String remove_parameter_query = "deleted_parameter(?mFullName, ?paramList, ?delParam)";
/* 183 */     RefactoringQuery remove_parameter = new RefactoringQuery(
/* 184 */       "remove_parameter", remove_parameter_query);
/* 185 */     remove_parameter.addType("?mFullName");
/* 186 */     remove_parameter.addType("?delParam");
/* 187 */     Node remove_parameter_node = new Node(remove_parameter);
/* 188 */     this.graph.add(remove_parameter_node);
/*     */     
/* 190 */     Node inline_method_node = new Node(
/* 191 */       new InlineMethod().getRefactoringQuery());
/* 192 */     this.graph.add(inline_method_node);
/*     */     
/* 194 */     String add_parameter_query = "added_parameter(?mFullName, ?paramList, ?addParam)";
/* 195 */     RefactoringQuery add_parameter = new RefactoringQuery("add_parameter", 
/* 196 */       add_parameter_query);
/* 197 */     add_parameter.addType("?mFullName");
/* 198 */     add_parameter.addType("?addParam");
/* 199 */     Node add_parameter_node = new Node(add_parameter);
/* 200 */     this.graph.add(add_parameter_node);
/*     */     
/* 202 */     Node replace_method_with_method_object_node = new Node(
/* 203 */       new ReplaceMethodWithMethodObject().getRefactoringQuery());
/* 204 */     this.graph.add(replace_method_with_method_object_node);
/*     */     
/* 206 */     String extract_class_query = "added_type(?newtFullName, ?newtShortName, ?),before_type(?tFullName, ?tShortName, ?pkg),after_type(?tFullName, ?tShortName, ?pkg),added_field(?fFullName, ?, ?tFullName),added_fieldoftype(?fFullName, ?newtFullName),(move_field(?fShortName, ?tFullName, ?newtFullName);move_method(?mShortName, ?tFullName, ?newtFullName))";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 214 */     RefactoringQuery extract_class = new RefactoringQuery("extract_class", 
/* 215 */       extract_class_query);
/* 216 */     extract_class.addType("?newtFullName");
/* 217 */     extract_class.addType("?tFullName");
/*     */     
/* 219 */     String inline_class_query = "deleted_type(?oldtFullName, ?oldtShortName, ?),before_type(?tFullName, ?tShortName, ?pkg),after_type(?tFullName, ?tShortName, ?pkg),deleted_field(?fFullName, ?, ?tFullName),deleted_fieldoftype(?fFullName, ?oldtFullName),(move_field(?fShortName, ?oldtFullName, ?tFullName);move_method(?mShortname, ?oldtFullName, ?tFullName))";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */     RefactoringQuery inline_class = new RefactoringQuery("inline_class", 
/* 228 */       inline_class_query);
/* 229 */     inline_class.addType("?oldtFullName");
/* 230 */     inline_class.addType("?tFullName");
/* 231 */     Node inline_class_node = new Node(inline_class);
/* 232 */     inline_class_node.addChild(move_method_node);
/* 233 */     inline_class_node.addChild(move_field_node);
/* 234 */     this.graph.add(inline_class_node);
/*     */     
/* 236 */     String hide_delegate_query = "after_method(?clientmFullName, ?, ?clienttFullName),deleted_calls(?clientmFullName, ?delegatemFullName),added_calls(?clientmFullName, ?servermFullName),added_method(?servermFullName, ?, ?servertFullName),added_calls(?servermFullName, ?delegatemFullName),before_field(?delegatefFullName, ?, ?servertFullName),after_field(?delegatefFullName, ?, ?servertFullName),before_fieldoftype(?delegatefFullName, ?delegatetFullName),after_fieldoftype(?delegatefFullName, ?delegatetFullName),after_method(?delegatemFullName, ?, ?delegatetFullName),NOT(equals(?clienttFullName, ?servertFullName)),NOT(equals(?delegatetFullName, ?servertFullName))";
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
/* 248 */     RefactoringQuery hide_delegate = new RefactoringQuery("hide_delegate", 
/* 249 */       hide_delegate_query);
/* 250 */     hide_delegate.addType("?delegatetFullName");
/* 251 */     hide_delegate.addType("?servertFullName");
/* 252 */     hide_delegate.addType("?clienttFullName");
/* 253 */     Node hide_delegate_node = new Node(hide_delegate);
/* 254 */     this.graph.add(hide_delegate_node);
/*     */     
/* 256 */     String remove_middle_man_query = "after_method(?clientmFullName, ?, ?clienttFullName),added_calls(?clientmFullName, ?delegatemFullName),deleted_calls(?clientmFullName, ?servermFullName),deleted_method(?servermFullName, ?, ?servertFullName),deleted_calls(?servermFullName, ?delegatemFullName),before_field(?delegatefFullName, ?, ?servertFullName),after_field(?delegatefFullName, ?, ?servertFullName),before_fieldoftype(?delegatefFullName, ?delegatetFullName),after_fieldoftype(?delegatefFullName, ?delegatetFullName),after_method(?delegatemFullName, ?, ?delegatetFullName),NOT(equals(?clienttFullName, ?servertFullName)),NOT(equals(?delegatetFullName, ?servertFullName))";
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
/* 268 */     RefactoringQuery remove_middle_man = new RefactoringQuery(
/* 269 */       "remove_middle_man", remove_middle_man_query);
/* 270 */     remove_middle_man.addType("?delegatetFullName");
/* 271 */     remove_middle_man.addType("?servertFullName");
/* 272 */     remove_middle_man.addType("?clienttFullName");
/* 273 */     Node remove_middle_man_node = new Node(remove_middle_man);
/* 274 */     this.graph.add(remove_middle_man_node);
/*     */     
/* 276 */     String introduce_local_extension_query = "(added_subtype(?tSuperFullName,?tSubFullName);(added_type(?tSubFullName, ?, ?),added_field(?superInstanceFieldFullName, ?, ?tSubFullName),added_fieldoftype(?superInstanceFieldFullName, ?tSuperFullName))),added_method(?subCtorFullName, \"<init>()\", ?tSubFullName),move_method(?mShortName, ?tClientFullName, ?tSubFullName),before_method(?mBeforeFullName, ?mShortName, ?tClientFullName),after_method(?mAfterFullName, ?mShortName, ?tSubFullName),deleted_calls(?mClientFullName, ?mBeforeFullName),after_type(?tClientFullName, ?, ?),added_calls(?mClientFullName, ?mAfterFullName)";
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
/* 294 */     RefactoringQuery introduce_local_extension = new RefactoringQuery(
/* 295 */       "introduce_local_extension", introduce_local_extension_query);
/* 296 */     introduce_local_extension.addType("?tSubFullName");
/* 297 */     introduce_local_extension.addType("?tSuperFullName");
/* 298 */     Node introduce_local_extension_node = new Node(
/* 299 */       introduce_local_extension);
/* 300 */     introduce_local_extension_node.addChild(move_method_node);
/* 301 */     this.graph.add(introduce_local_extension_node);
/*     */     
/* 303 */     String replace_ctor_with_factory_method_query = "added_method(?mFactFullName, ?, ?tFullName),after_method(?mCtorFullName,\"<init>()\",?tFullName),added_calls(?mFactFullName, ?mCtorFullName),added_methodmodifier(?mCtorFullName,\"private\")";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 309 */     RefactoringQuery replace_ctor_with_factory_method = new RefactoringQuery(
/* 310 */       "replace_constructor_with_factory_method", 
/* 311 */       replace_ctor_with_factory_method_query);
/* 312 */     replace_ctor_with_factory_method.addType("?mCtorFullName");
/* 313 */     replace_ctor_with_factory_method.addType("?mFactFullName");
/* 314 */     Node replace_ctor_with_factory_method_node = new Node(
/* 315 */       replace_ctor_with_factory_method);
/* 316 */     this.graph.add(replace_ctor_with_factory_method_node);
/*     */     
/* 318 */     Node replace_data_value_with_object_node = new Node(
/* 319 */       new ReplaceDataValueWithObject().getRefactoringQuery());
/* 320 */     this.graph.add(replace_data_value_with_object_node);
/*     */     
/* 322 */     RefactoringQuery replace_magic_number_with_const = new RefactoringQuery(
/* 323 */       "replace_magic_number_with_constant", 
/* 324 */       "added_field(?fFullName, ?, ?tFullName),added_fieldmodifier(?fFullName, \"final\"),before_method(?mFullName, ?, ?tFullName), added_accesses(?fFullName, ?mFullName)");
/*     */     
/*     */ 
/*     */ 
/* 328 */     replace_magic_number_with_const.addType("?mFullName");
/* 329 */     replace_magic_number_with_const.addType("?fFullName");
/* 330 */     Node replace_magic_number_with_const_node = new Node(
/* 331 */       replace_magic_number_with_const);
/* 332 */     this.graph.add(replace_magic_number_with_const_node);
/*     */     
/* 334 */     Node replace_array_with_object_node = new Node(
/* 335 */       new ReplaceArrayWithObject().getRefactoringQuery());
/* 336 */     this.graph.add(replace_array_with_object_node);
/*     */     
/* 338 */     Node change_uni_to_bi_node = new Node(
/* 339 */       new ChangeUnidirectionalAssociationToBi()
/* 340 */       .getRefactoringQuery());
/* 341 */     this.graph.add(change_uni_to_bi_node);
/*     */     
/* 343 */     Node change_bi_to_uni_node = new Node(
/* 344 */       new ChangeBidirectionalAssociationToUni()
/* 345 */       .getRefactoringQuery());
/* 346 */     this.graph.add(change_bi_to_uni_node);
/*     */     
/* 348 */     Node encapsulate_collection = new Node(
/* 349 */       new EncapsulateCollection().getRefactoringQuery());
/* 350 */     this.graph.add(encapsulate_collection);
/*     */     
/* 352 */     Node replace_nested_cond_guard_clauses = new Node(
/* 353 */       new ReplaceNestedCondWithGuardClauses().getRefactoringQuery());
/* 354 */     this.graph.add(replace_nested_cond_guard_clauses);
/*     */     
/* 356 */     Node separate_query_from_modifier_node = new Node(
/* 357 */       new SeparateQueryFromModifier().getRefactoringQuery());
/* 358 */     this.graph.add(separate_query_from_modifier_node);
/*     */     
/* 360 */     Node parameterize_method_node = new Node(
/* 361 */       new ParameterizeMethod().getRefactoringQuery());
/* 362 */     this.graph.add(parameterize_method_node);
/*     */     
/* 364 */     Node replace_parameter_with_methods_node = new Node(
/* 365 */       new ReplaceParameterWithExplicitMethods()
/* 366 */       .getRefactoringQuery());
/* 367 */     this.graph.add(replace_parameter_with_methods_node);
/*     */     
/* 369 */     Node preserve_whole_object_node = new Node(
/* 370 */       new PreserveWholeObject().getRefactoringQuery());
/* 371 */     this.graph.add(preserve_whole_object_node);
/*     */     
/* 373 */     RefactoringQuery replace_param_with_method = new RefactoringQuery(
/* 374 */       "replace_param_with_method", 
/* 375 */       "deleted_calls(?clientmFullName, ?othermFullName),before_calls(?clientmFullName, ?mFullName),after_calls(?clientmFullName, ?mFullName),added_calls(?mFullName, ?othermFullName),deleted_parameter(?mFullName, ?, ?paramName),NOT(added_parameter(?mFullName, ?, ?))");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 381 */     replace_param_with_method.addType("?mFullName");
/* 382 */     replace_param_with_method.addType("?paramName");
/* 383 */     replace_param_with_method.addType("?othermFullName");
/* 384 */     Node replace_param_with_method_node = new Node(
/* 385 */       replace_param_with_method);
/* 386 */     this.graph.add(replace_param_with_method_node);
/*     */     
/* 388 */     RefactoringQuery hide_method = new RefactoringQuery("hide_method", 
/* 389 */       "deleted_methodmodifier(?mFullName, \"public\"),added_methodmodifier(?mFullName, \"private\")");
/*     */     
/* 391 */     hide_method.addType("?mFullName");
/* 392 */     Node hide_method_node = new Node(hide_method);
/* 393 */     this.graph.add(hide_method_node);
/*     */     
/* 395 */     RefactoringQuery pull_up_ctor_body = new RefactoringQuery(
/* 396 */       "pull_up_constructor_body", 
/* 397 */       "added_method(?supermFullName, \"<init>()\", ?supertFullName),before_subtype(?supertFullName, ?subtFullName),after_subtype(?supertFullName, ?subtFullName),before_method(?submFullName, \"<init>()\", ?subtFullName),after_method(?submFullName, \"<init>()\", ?subtFullName),added_calls(?submFullName, ?supermFullName)");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 403 */     pull_up_ctor_body.addType("?supertFullName");
/* 404 */     Node pull_up_ctor_body_node = new Node(pull_up_ctor_body);
/* 405 */     this.graph.add(pull_up_ctor_body_node);
/*     */     
/* 407 */     RefactoringQuery extract_subclass = new RefactoringQuery(
/* 408 */       "extract_subclass", 
/* 409 */       "added_subtype(?superTFullName, ?tFullName),NOT(before_type(?tFullName, ?, ?)),(move_field(?fShortName, ?superTFullName, ?tFullName);move_method(?mShortName, ?superTFullName, ?tFullName))");
/*     */     
/*     */ 
/*     */ 
/* 413 */     extract_subclass.addType("?superTFullName");
/* 414 */     extract_subclass.addType("?tFullName");
/* 415 */     Node extract_subclass_node = new Node(extract_subclass);
/* 416 */     extract_subclass_node.addChild(move_method_node);
/* 417 */     extract_subclass_node.addChild(move_field_node);
/* 418 */     this.graph.add(extract_subclass_node);
/*     */     
/* 420 */     RefactoringQuery extract_superclass = new RefactoringQuery(
/* 421 */       "extract_superclass", 
/* 422 */       "added_subtype(?tFullName, ?subtFullName),NOT(before_type(?tFullName, ?, ?)),(move_field(?fShortName, ?subtFullName, ?tFullName);move_method(?mShortName, ?subtFullName, ?tFullName))");
/*     */     
/*     */ 
/*     */ 
/* 426 */     extract_superclass.addType("?subtFullName");
/* 427 */     extract_superclass.addType("?tFullName");
/* 428 */     Node extract_superclass_node = new Node(extract_superclass);
/* 429 */     extract_superclass_node.addChild(move_method_node);
/* 430 */     extract_superclass_node.addChild(move_field_node);
/* 431 */     this.graph.add(extract_superclass_node);
/*     */     
/* 433 */     Node consolidate_cond_expression_node = new Node(
/* 434 */       new ConsolidateConditionalExpression().getRefactoringQuery());
/* 435 */     consolidate_cond_expression_node.addChild(extract_method_node);
/* 436 */     this.graph.add(consolidate_cond_expression_node);
/*     */     
/* 438 */     RefactoringQuery extract_interface = new RefactoringQuery(
/* 439 */       "extract_interface", 
/* 440 */       "added_type(?interfacetFullName, ?,?),added_implements(?interfacetFullName, ?othertFullName),before_type(?othertFullName, ?, ?)");
/*     */     
/*     */ 
/* 443 */     extract_interface.addType("?interfacetFullName");
/* 444 */     extract_interface.addType("?othertFullName");
/* 445 */     Node extract_interface_node = new Node(extract_interface);
/* 446 */     this.graph.add(extract_interface_node);
/*     */     
/* 448 */     Node replace_exception_with_test_node = new Node(
/* 449 */       new ReplaceExceptionWithTest().getRefactoringQuery());
/* 450 */     this.graph.add(replace_exception_with_test_node);
/*     */     
/* 452 */     RefactoringQuery change_value_to_reference = new RefactoringQuery(
/* 453 */       "change_value_to_reference", 
/* 454 */       "replace_constructor_with_factory_method(?mCtorFullName, ?mFactFullName),added_method(?mFactFullName, ?, ?tFullName),before_fieldoftype(?, ?tFullName),after_fieldoftype(?, ?tFullName),NOT(after_method(?, \"equals()\", ?tFullName);after_method(?, \"hashCode()\", ?tFullName))");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 460 */     change_value_to_reference.addType("?tFullName");
/* 461 */     Node change_value_to_reference_node = new Node(
/* 462 */       change_value_to_reference);
/* 463 */     change_value_to_reference_node
/* 464 */       .addChild(replace_ctor_with_factory_method_node);
/* 465 */     this.graph.add(change_value_to_reference_node);
/*     */     
/* 467 */     RefactoringQuery change_reference_to_value = new RefactoringQuery(
/* 468 */       "change_reference_to_value", 
/* 469 */       "deleted_method(?mFactFullName, ?, ?tFullName),before_method(?mCtorFullName,\"<init>()\",?tFullName),deleted_calls(?mFactFullName, ?mCtorFullName),added_calls(?mClientFullName, ?mCtorFullName),deleted_calls(?mClientFullName, ?mFactFullName),deleted_methodmodifier(?mCtorFullName,\"private\"),after_fieldoftype(?, ?tFullName),before_fieldoftype(?, ?tFullName),after_method(?, \"equals()\", ?tFullName),after_method(?, \"hashCode()\", ?tFullName)");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 479 */     change_reference_to_value.addType("?tFullName");
/* 480 */     Node change_reference_to_value_node = new Node(
/* 481 */       change_reference_to_value);
/* 482 */     change_reference_to_value_node
/* 483 */       .addChild(replace_ctor_with_factory_method_node);
/* 484 */     this.graph.add(change_reference_to_value_node);
/*     */     
/* 486 */     Node consolidate_duplicate_cond_fragments_node = new Node(
/* 487 */       new ConsolidateDuplicateConditionalFragment()
/* 488 */       .getRefactoringQuery());
/* 489 */     this.graph.add(consolidate_duplicate_cond_fragments_node);
/*     */     
/* 491 */     RefactoringQuery encapsulate_downcast = new RefactoringQuery(
/* 492 */       "encapsulate_downcast", 
/* 493 */       "added_cast(?, ?tFullName, ?mFullName),added_return(?mFullName, ?tFullName),deleted_return(?mFullName, ?oldtFullName),(after_subtype(?oldtFullName, ?tFullName);(after_subtype(?oldtFullName, ?othertFullName),after_subtype(?othertFullName, ?tFullName)))");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 499 */     encapsulate_downcast.addType("?mFullName");
/* 500 */     encapsulate_downcast.addType("?tFullName");
/* 501 */     Node encapsulate_downcast_node = new Node(encapsulate_downcast);
/* 502 */     this.graph.add(encapsulate_downcast_node);
/*     */     
/* 504 */     Node introduce_assertion_node = new Node(
/* 505 */       new IntroduceAssertion().getRefactoringQuery());
/* 506 */     this.graph.add(introduce_assertion_node);
/*     */     
/* 508 */     RefactoringQuery tease_apart_inheritance = new RefactoringQuery(
/* 509 */       "tease_apart_inheritance", "before_type(?gptFullName, ?,?),after_type(?gptFullName, ?,?),before_subtype(?gptFullName, ?p1tFullName),after_subtype(?gptFullName, ?p1tFullName),before_subtype(?gptFullName, ?p2tFullName),after_subtype(?gptFullName, ?p2tFullName),NOT(equals(?p1tFullName, ?p2tFullName)),deleted_subtype(?p1tFullName, ?t1FullName),deleted_subtype(?p2tFullName, ?t2FullName),added_field(?fFullName, ?, ?gptFullName),added_fieldoftype(?fFullName, ?newptFullName),added_type(?newptFullName, ?, ?),added_subtype(?newptFullName, ?newt1FullName),added_subtype(?newptFullName, ?newt2FullName),(move_field(?, ?t1FullName, ?newt1FullName);move_method(?, ?t1FullName, ?newt1FullName);equals(?t1FullName, ?newt1FullName)),(move_field(?, ?t2FullName, ?newt2FullName);move_method(?, ?t2FullName, ?newt2FullName);equals(?t2FullName, ?newt2FullName))");
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
/* 529 */     tease_apart_inheritance.addType("?gptFullName");
/* 530 */     Node tease_apart_inheritance_node = new Node(tease_apart_inheritance);
/* 531 */     tease_apart_inheritance_node.addChild(move_method_node);
/* 532 */     tease_apart_inheritance_node.addChild(move_field_node);
/* 533 */     this.graph.add(tease_apart_inheritance_node);
/*     */     
/* 535 */     Node introduce_null_object_node = new Node(
/* 536 */       new IntroduceNullObject().getRefactoringQuery());
/* 537 */     this.graph.add(introduce_null_object_node);
/*     */     
/* 539 */     RefactoringQuery replace_inheritance_with_delegation = new RefactoringQuery(
/* 540 */       "replace_inheritance_with_delegation", 
/* 541 */       "deleted_subtype(?delegate, ?delegatingObj),added_fieldoftype(?fFullName, ?delegate),added_field(?fFullName, ?, ?delegatingObj)");
/*     */     
/*     */ 
/* 544 */     replace_inheritance_with_delegation.addType("?delegate");
/* 545 */     replace_inheritance_with_delegation.addType("?delegatingObj");
/* 546 */     Node replace_inheritance_with_delegation_node = new Node(
/* 547 */       replace_inheritance_with_delegation);
/* 548 */     this.graph.add(replace_inheritance_with_delegation_node);
/*     */     
/* 550 */     RefactoringQuery replace_delegation_with_inheritance = new RefactoringQuery(
/* 551 */       "replace_delegation_with_inheritance", 
/* 552 */       "added_subtype(?delegate, ?delegatingObj), deleted_fieldoftype(?fFullName, ?delegate), deleted_field(?fFullName, ?, ?delegatingObj)");
/*     */     
/*     */ 
/* 555 */     replace_delegation_with_inheritance.addType("?delegate");
/* 556 */     replace_delegation_with_inheritance.addType("?delegatingObj");
/* 557 */     Node replace_delegation_with_inheritance_node = new Node(
/* 558 */       replace_delegation_with_inheritance);
/* 559 */     this.graph.add(replace_delegation_with_inheritance_node);
/*     */     
/* 561 */     RefactoringQuery replace_type_code_with_class_query = new RefactoringQuery(
/* 562 */       "replace_type_code_with_class", 
/* 563 */       "deleted_field(?old_fFullName1, ?fShortName1, ?tFullName), deleted_field(?old_fFullName2, ?fShortName2, ?tFullName), NOT(equals(?fShortName1, ?fShortName2)), added_field(?new_fFullName1, ?fShortName1, ?tCodeFullName), added_field(?new_fFullName2, ?fShortName2, ?tCodeFullName), added_type(?tCodeFullName, ?, ?), added_fieldmodifier(?new_fFullName1, \"static\"), added_fieldmodifier(?new_fFullName2, \"static\"), deleted_fieldmodifier(?old_fFullName1, \"static\"), deleted_fieldmodifier(?old_fFullName2, \"static\"), added_fieldoftype(?new_fFullName1, ?tCodeFullName), added_fieldoftype(?new_fFullName2, ?tCodeFullName), deleted_fieldoftype(?fFullName, ?), added_fieldoftype(?fFullName, ?tCodeFullName)");
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
/* 577 */     replace_type_code_with_class_query.addType("?tFullName");
/* 578 */     replace_type_code_with_class_query.addType("?tCodeFullName");
/* 579 */     new Node(
/* 580 */       replace_type_code_with_class_query);
/*     */     
/*     */ 
/* 583 */     Node remove_assignment_to_parameters_node = new Node(
/* 584 */       new RemoveAssignmentToParameters().getRefactoringQuery());
/* 585 */     this.graph.add(remove_assignment_to_parameters_node);
/*     */     
/* 587 */     RefactoringQuery encapsulate_field = new RefactoringQuery(
/* 588 */       "encapsulate_field", 
/* 589 */       "deleted_fieldmodifier(?fFullName,\"public\"),added_fieldmodifier(?fFullName,\"private\"),added_getter(?mGetFullName, ?fFullName),added_setter(?mSetFullName, ?fFullName)");
/*     */     
/*     */ 
/*     */ 
/* 593 */     encapsulate_field.addType("?fFullName");
/* 594 */     Node encapsulate_field_node = new Node(encapsulate_field);
/* 595 */     this.graph.add(encapsulate_field_node);
/*     */     
/* 597 */     RefactoringQuery remove_setting_method = new RefactoringQuery(
/* 598 */       "remove_setting_method", 
/* 599 */       "added_fieldmodifier(?fFullName,\"final\"),deleted_setter(?mFullName,?fFullName)");
/*     */     
/* 601 */     remove_setting_method.addType("?mFullName");
/* 602 */     remove_setting_method.addType("?fFullName");
/* 603 */     Node remove_setting_method_node = new Node(remove_setting_method);
/* 604 */     this.graph.add(remove_setting_method_node);
/*     */     
/* 606 */     RefactoringQuery replace_error_code_with_exception = new RefactoringQuery(
/* 607 */       "replace_error_code_with_exception", 
/* 608 */       "deleted_return(?mFullName, ?oldReturnType),added_return(?mFullName, \"void\"),added_throws(?mFullName, ?tFullName)");
/*     */     
/*     */ 
/* 611 */     replace_error_code_with_exception.addType("?mFullName");
/* 612 */     replace_error_code_with_exception.addType("?oldReturnType");
/* 613 */     replace_error_code_with_exception.addType("?tFullName");
/* 614 */     Node replace_error_code_with_exception_node = new Node(
/* 615 */       replace_error_code_with_exception);
/* 616 */     this.graph.add(replace_error_code_with_exception_node);
/*     */     
/* 618 */     new Node(
/* 619 */       new ReplaceTypeCodeWithSubclasses().getRefactoringQuery());
/*     */     
/*     */ 
/* 622 */     Node introduce_parameter_object_node = new Node(
/* 623 */       new IntroduceParamObject().getRefactoringQuery());
/* 624 */     introduce_parameter_object_node.addChild(remove_parameter_node);
/* 625 */     introduce_parameter_object_node.addChild(add_parameter_node);
/* 626 */     this.graph.add(introduce_parameter_object_node);
/*     */     
/* 628 */     new Node(
/* 629 */       new ReplaceTypeCodeWithState().getRefactoringQuery());
/*     */     
/*     */ 
/* 632 */     Node replace_conditional_with_polymorphism_node = new Node(
/* 633 */       new ReplaceConditionalWithPolymorphism()
/* 634 */       .getRefactoringQuery());
/* 635 */     this.graph.add(replace_conditional_with_polymorphism_node);
/*     */     
/* 637 */     RefactoringQuery self_encapsulate_field = new RefactoringQuery(
/* 638 */       "self_encapsulate_field", 
/* 639 */       "added_getter(?mGetFullName, ?fFullName),before_field(?fFullName, ?, ?), added_setter(?mSetFullName, ?fFullName)");
/*     */     
/*     */ 
/* 642 */     self_encapsulate_field.addType("?fFullName");
/* 643 */     Node self_encapsulate_field_node = new Node(self_encapsulate_field);
/* 644 */     self_encapsulate_field_node.addChild(encapsulate_field_node);
/* 645 */     this.graph.add(self_encapsulate_field_node);
/*     */     
/* 647 */     RefactoringQuery extract_hierarchy = new RefactoringQuery(
/* 648 */       "extract_hierarchy", 
/* 649 */       "replace_type_code_with_subclasses(?tFullName);replace_type_code_with_state(?tFullName, ?);(replace_conditional_with_polymorphism(?mFullName, ?),before_method(?mFullName, ?, ?tFullName))");
/*     */     
/*     */ 
/*     */ 
/* 653 */     extract_hierarchy.addType("?tFullName");
/* 654 */     Node extract_hierarchy_node = new Node(extract_hierarchy);
/* 655 */     extract_hierarchy_node
/* 656 */       .addChild(replace_conditional_with_polymorphism_node);
/*     */     
/*     */ 
/* 659 */     this.graph.add(extract_hierarchy_node);
/*     */     
/* 661 */     Node form_template_method_node = new Node(
/* 662 */       new FormTemplateMethod().getRefactoringQuery());
/* 663 */     this.graph.add(form_template_method_node);
/*     */     
/* 665 */     Node replace_subclass_with_field_node = new Node(
/* 666 */       new ReplaceSubclassWithField().getRefactoringQuery());
/* 667 */     replace_subclass_with_field_node
/* 668 */       .addChild(replace_ctor_with_factory_method_node);
/* 669 */     this.graph.add(replace_subclass_with_field_node);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 674 */     RefactoringQuery replace_temp_with_query = new RefactoringQuery(
/* 675 */       "replace_temp_with_query", 
/* 676 */       new ExtractMethod().getRefactoringString() + 
/* 677 */       ", added_calls(?mFullName, ?newmFullName)," + 
/* 678 */       "deleted_localvar(?mFullName, ?type, ?, ?newmBody)," + 
/* 679 */       "added_return(?newmFullName, ?type)");
/* 680 */     replace_temp_with_query.addType("?mFullName");
/* 681 */     replace_temp_with_query.addType("?newmFullName");
/* 682 */     Node replace_temp_with_query_node = new Node(replace_temp_with_query);
/* 683 */     replace_temp_with_query_node.addChild(extract_method_node);
/* 684 */     this.graph.add(replace_temp_with_query_node);
/*     */     
/* 686 */     Node inline_temp_node = new Node(
/* 687 */       new InlineTemp().getRefactoringQuery());
/* 688 */     this.graph.add(inline_temp_node);
/*     */     
/* 690 */     Node introduce_explaining_variable_node = new Node(
/* 691 */       new IntroduceExplainingVariable().getRefactoringQuery());
/* 692 */     this.graph.add(introduce_explaining_variable_node);
/*     */     
/* 694 */     Node remove_control_flag_node = new Node(
/* 695 */       new RemoveControlFlag().getRefactoringQuery());
/* 696 */     this.graph.add(remove_control_flag_node);
/*     */   }
/*     */   
/*     */ 
/*     */   public void inferRefactoring(File fileName, Node curr)
/*     */   {
/* 702 */     if (curr.isVisited()) {
/* 703 */       return;
/*     */     }
/* 705 */     curr.setVisited(true);
/* 706 */     for (Node child : curr.getChildren()) {
/* 707 */       inferRefactoring(fileName, child);
/*     */     }
/*     */     try
/*     */     {
/* 711 */       Connection con = new Connection(this.frontend);
/* 712 */       RefactoringQuery refactoring = curr.getRefQry();
/* 713 */       System.out.println("REF BEING CHECKED: " + refactoring.getName());
/* 714 */       PreparedQuery query = con.prepareQuery(refactoring.getQuery());
/* 715 */       ResultSet rs = query.executeQuery();
/*     */       
/* 717 */       while (rs.next()) {
/* 718 */         Rule currentRule = this.ruleFactory.returnRuleByName(refactoring
/* 719 */           .getName());
/* 720 */         if (currentRule != null) {
/* 721 */           String result = currentRule.checkAdherence(rs);
/* 722 */           storeResult(fileName, curr, rs, result);
/* 723 */         } else if (refactoring.getName().equals(
/* 724 */           "self_encapsulate_field")) {
/* 725 */           String fieldName = rs.getString("?fFullName");
/* 726 */           String checkForStragglers = "before_accesses(\"" + 
/* 727 */             fieldName + "\", ?mFullName), after_accesses(\"" + 
/* 728 */             fieldName + "\", ?mFullName)";
/* 729 */           PreparedQuery query2 = con.prepareQuery(checkForStragglers);
/* 730 */           ResultSet rs2 = query2.executeQuery();
/* 731 */           if (!rs2.next())
/*     */           {
/*     */ 
/* 734 */             checkForStragglers = 
/*     */             
/*     */ 
/*     */ 
/* 738 */               "added_accesses(\"" + fieldName + "\", ?mFullName), NOT(added_getter(?mFullName, \"" + fieldName + "\");added_setter(?mFullName, \"" + fieldName + "\"))";
/* 739 */             query2 = con.prepareQuery(checkForStragglers);
/* 740 */             rs2 = query2.executeQuery();
/* 741 */             if (!rs2.next())
/*     */             {
/*     */ 
/* 744 */               String writeTo = buildResult(refactoring, rs);
/* 745 */               storeResult(fileName, curr, rs, writeTo);
/*     */             }
/* 747 */           } } else { String writeTo = buildResult(refactoring, rs);
/* 748 */           storeResult(fileName, curr, rs, writeTo);
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/* 752 */       System.out.println("IO Exception: " + e.getMessage());
/*     */     } catch (Exception e) {
/* 754 */       System.out.println("Got exception: " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private String buildResult(RefactoringQuery rq, ResultSet rs) throws TyrubaException
/*     */   {
/* 760 */     String result = rq.getName() + "(";
/* 761 */     Iterator<String> types_itr = rq.getTypes().iterator();
/* 762 */     int types_size = rq.getTypes().size();
/* 763 */     int types_count = 0;
/* 764 */     while (types_itr.hasNext()) {
/* 765 */       types_count++;
/* 766 */       String type = (String)types_itr.next();
/* 767 */       String type_binding = rs.getString(type);
/* 768 */       result = result + "\"" + type_binding + "\"";
/* 769 */       if (types_count < types_size)
/* 770 */         result = result + ",";
/*     */     }
/* 772 */     return result + ")";
/*     */   }
/*     */   
/*     */   private void storeResult(File fileName, Node curr, ResultSet rs, String result) throws IOException, ParseException, TypeModeError
/*     */   {
/* 777 */     if ((result != null) && (!this.written_strs.contains(result))) {
/* 778 */       this.written_strs.add(result);
/* 779 */       curr.incrementNumFound();
/* 780 */       String pred = "\n" + result + ".";
/* 781 */       List<String> vars1 = substituteInQueryString(curr.getRefQry()
/* 782 */         .getQuery(), rs);
/* 783 */       this.dependents.put(result, vars1);
/*     */       
/* 785 */       FileWriter fileWriter = new FileWriter(fileName, true);
/* 786 */       fileWriter.write(pred);
/* 787 */       fileWriter.flush();
/* 788 */       fileWriter.close();
/*     */       
/* 790 */       this.frontend.parse(pred);
/*     */     }
/*     */   }
/*     */   
/*     */   void ensureFrontEnd() throws IOException, ParseException, TypeModeError
/*     */   {
/* 796 */     if (this.frontend == null) {
/* 797 */       if (this.dbDir == null) {
/* 798 */         this.frontend = new FrontEnd(this.loadInitFile, MetaInfo.fdbDir, 
/* 799 */           false, null, true, this.backgroundPageCleaning);
/*     */       } else
/* 801 */         this.frontend = new FrontEnd(this.loadInitFile, this.dbDir, true, null, false, 
/* 802 */           this.backgroundPageCleaning);
/*     */     }
/* 804 */     this.frontend.setCacheSize(this.cachesize);
/*     */   }
/*     */   
/*     */   public void sort(String outputFile) {
/*     */     try {
/* 809 */       this.frontend = null;
/* 810 */       ensureFrontEnd();
/* 811 */       this.frontend.load(MetaInfo.lsclipseRefactorPred);
/* 812 */       this.frontend.load(MetaInfo.lsclipse2KB);
/* 813 */       this.frontend.load(MetaInfo.lsclipseDelta);
/*     */       
/* 815 */       File f = new File(outputFile);
/* 816 */       if (f.exists())
/*     */       {
/* 818 */         f.delete();
/*     */       }
/* 820 */       for (Node query : this.graph) {
/* 821 */         inferRefactoring(f, query);
/*     */       }
/* 823 */       this.frontend.shutdown();
/*     */       
/* 825 */       this.frontend = null;
/* 826 */       this.written_strs = null;
/*     */     } catch (Exception e) {
/* 828 */       System.out.println("Something REALLY BAD has happened!");
/* 829 */       e.printStackTrace();
/*     */     }
/*     */     catch (Throwable e) {
/* 832 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void printTree() {
/* 837 */     for (Node query : this.graph)
/* 838 */       printTree(query, 0);
/*     */   }
/*     */   
/*     */   public void printTree(Node curr, int indentCount) {
/* 842 */     for (int i = 0; i < indentCount; i++) {
/* 843 */       System.out.print(" ");
/*     */     }
/* 845 */     System.out.println(curr.getRefQry().getName());
/*     */     
/* 847 */     if (curr.getChildren() != null) {
/* 848 */       for (Node child : curr.getChildren()) {
/* 849 */         if (child != null)
/* 850 */           printTree(child, indentCount + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String findIntInString(String str) {
/* 856 */     Pattern intsOnly = Pattern.compile("\\d+");
/* 857 */     Matcher makeMatch = intsOnly.matcher(str);
/* 858 */     if (makeMatch.find()) {
/* 859 */       String inputInt = makeMatch.group();
/* 860 */       System.out.println(inputInt);
/* 861 */       return inputInt;
/*     */     }
/* 863 */     return "";
/*     */   }
/*     */   
/*     */   public String findDecimalInString(String str) {
/* 867 */     Pattern intsOnly = Pattern.compile("[\\d]+\\.[\\d]+");
/* 868 */     Matcher makeMatch = intsOnly.matcher(str);
/* 869 */     if (makeMatch.find()) {
/* 870 */       String inputInt = makeMatch.group();
/* 871 */       System.out.println(inputInt);
/* 872 */       return inputInt;
/*     */     }
/* 874 */     return "";
/*     */   }
/*     */   
/*     */   private static List<String> substituteInQueryString(String queryString, ResultSet rs)
/*     */   {
/* 879 */     List<String> res = new ArrayList();
/*     */     
/* 881 */     List<String> queries = new ArrayList();
/*     */     
/* 883 */     queryString = queryString.replace(" ", "");
/* 884 */     int current = 0;
/*     */     
/* 886 */     while (queryString.charAt(current) == '(') {
/* 887 */       queries.add("(");
/* 888 */       current++;
/*     */     }
/* 890 */     while (current < queryString.length()) {
/* 891 */       if (queryString.startsWith("NOT(", current)) {
/* 892 */         queries.add("NOT");
/* 893 */         queries.add("(");
/* 894 */         current += 4;
/*     */       }
/*     */       else {
/* 897 */         int start = current;
/*     */         
/* 899 */         while (queryString.charAt(current) != ')')
/*     */         {
/* 901 */           if (queryString.charAt(current) == '"') {
/* 902 */             current++;
/* 903 */             while (queryString.charAt(current) != '"') {
/* 904 */               current++;
/*     */             }
/*     */           }
/* 907 */           current++;
/*     */         }
/* 909 */         current++;
/* 910 */         queries.add(queryString.substring(start, current));
/*     */         
/* 912 */         while ((current < queryString.length()) && (!
/* 913 */           Character.isLetter(queryString.charAt(current)))) {
/* 914 */           queries.add(String.valueOf(queryString.charAt(current)));
/* 915 */           current++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 920 */     for (String q : queries)
/*     */     {
/* 922 */       if (q.length() < 4) {
/* 923 */         if (q.equals(",")) {
/* 924 */           res.add("AND");
/* 925 */         } else if (q.equals(";")) {
/* 926 */           res.add("OR");
/*     */         } else {
/* 928 */           res.add(q);
/*     */         }
/*     */       } else {
/* 931 */         String newq = new String(q);
/*     */         
/* 933 */         int begin = 0;
/* 934 */         int end = 0;
/* 935 */         boolean addNewQ = true;
/*     */         for (;;) {
/* 937 */           char endchar = ',';
/* 938 */           begin = q.indexOf('?', end);
/* 939 */           if (begin >= 0)
/*     */           {
/* 941 */             end = q.indexOf(endchar, begin);
/* 942 */             if (end < 0) {
/* 943 */               endchar = ')';
/* 944 */               end = q.indexOf(endchar, begin);
/*     */             }
/* 946 */             if (end >= 0)
/*     */             {
/* 948 */               String var = q.substring(begin, end);
/* 949 */               if (var.equals("?")) {
/*     */                 continue;
/*     */               }
/*     */               
/*     */ 
/*     */               try
/*     */               {
/* 956 */                 String type_binding = rs.getString(var);
/* 957 */                 newq = newq.replace(var, "\"" + type_binding + "\"");
/*     */               } catch (TyrubaException localTyrubaException) {
/* 959 */                 addNewQ = false;
/*     */ 
/*     */               }
/*     */               catch (NullPointerException localNullPointerException)
/*     */               {
/* 964 */                 addNewQ = false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 971 */         if (addNewQ) {
/* 972 */           res.add(newq);
/*     */         } else
/* 974 */           res.add(q);
/*     */       } }
/* 976 */     return res;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/TopologicalSort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */