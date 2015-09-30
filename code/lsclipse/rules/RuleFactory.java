/*    */ package lsclipse.rules;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuleFactory
/*    */ {
/* 15 */   Set<Rule> rules = new HashSet();
/*    */   
/*    */   public RuleFactory() {
/* 18 */     this.rules.add(new ChangeBidirectionalAssociationToUni());
/* 19 */     this.rules.add(new ChangeUnidirectionalAssociationToBi());
/* 20 */     this.rules.add(new DecomposeConditional());
/* 21 */     this.rules.add(new EncapsulateCollection());
/* 22 */     this.rules.add(new ExtractMethod());
/* 23 */     this.rules.add(new InlineMethod());
/* 24 */     this.rules.add(new InlineTemp());
/* 25 */     this.rules.add(new IntroduceAssertion());
/* 26 */     this.rules.add(new IntroduceExplainingVariable());
/* 27 */     this.rules.add(new IntroduceNullObject());
/* 28 */     this.rules.add(new MoveMethod());
/* 29 */     this.rules.add(new ParameterizeMethod());
/* 30 */     this.rules.add(new PreserveWholeObject());
/* 31 */     this.rules.add(new RemoveAssignmentToParameters());
/* 32 */     this.rules.add(new RemoveControlFlag());
/* 33 */     this.rules.add(new RenameMethod());
/* 34 */     this.rules.add(new ReplaceArrayWithObject());
/* 35 */     this.rules.add(new ReplaceConditionalWithPolymorphism());
/* 36 */     this.rules.add(new ReplaceDataValueWithObject());
/* 37 */     this.rules.add(new ReplaceExceptionWithTest());
/* 38 */     this.rules.add(new ReplaceMethodWithMethodObject());
/* 39 */     this.rules.add(new ReplaceNestedCondWithGuardClauses());
/* 40 */     this.rules.add(new ReplaceParameterWithExplicitMethods());
/* 41 */     this.rules.add(new ReplaceSubclassWithField());
/* 42 */     this.rules.add(new SeparateQueryFromModifier());
/* 43 */     this.rules.add(new ConsolidateConditionalExpression());
/* 44 */     this.rules.add(new ConsolidateDuplicateConditionalFragment());
/* 45 */     this.rules.add(new ReplaceTypeCodeWithSubclasses());
/* 46 */     this.rules.add(new IntroduceParamObject());
/* 47 */     this.rules.add(new ReplaceTypeCodeWithState());
/* 48 */     this.rules.add(new FormTemplateMethod());
/*    */   }
/*    */   
/*    */   public Rule returnRuleByName(String name) {
/* 52 */     for (Rule r : this.rules) {
/* 53 */       if (r.getName().equals(name)) {
/* 54 */         return r;
/*    */       }
/*    */     }
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/lsclipse/rules/RuleFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */