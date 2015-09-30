/*    */ package tyRuBa.tests;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import tyRuBa.engine.FrontEnd;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MetaTypeTest
/*    */   extends TyrubaTest
/*    */   implements Serializable
/*    */ {
/*    */   public MetaTypeTest(String arg0)
/*    */   {
/* 14 */     super(arg0);
/*    */   }
/*    */   
/*    */   protected void setUp() throws Exception {
/* 18 */     TyrubaTest.initfile = true;
/* 19 */     tyRuBa.engine.RuleBase.useCache = true;
/* 20 */     tyRuBa.engine.RuleBase.silent = true;
/* 21 */     super.setUp();
/* 22 */     this.frontend.enableMetaData();
/*    */   }
/*    */   
/*    */   public void testRepAsJavaTypes() throws Exception {
/* 26 */     this.frontend.parse(
/* 27 */       "TYPE Element = Type | Method | Number TYPE Type AS String TYPE Method AS String TYPE Number AS Integer ");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 32 */     test_must_equal(
/* 33 */       "meta.name(?x,Type),meta.typeConstructor(?x)", 
/* 34 */       "?x", this.frontend.findType("Type"));
/* 35 */     test_must_findall(
/* 36 */       "meta.name(?type,Element),meta.subtype(?type,?sub),meta.name(?sub,?subname)", 
/*    */       
/* 38 */       "?subname", 
/* 39 */       new String[] {
/* 40 */       "Method", "Number", "Type" });
/*    */     
/* 42 */     test_must_equal(
/* 43 */       "meta.name(?Type,Type),meta.subtype(?Element,?Type)", 
/* 44 */       "?Element", 
/* 45 */       this.frontend.findType("Element"));
/*    */   }
/*    */   
/*    */   public void testRepAsTupleTypes() throws Exception
/*    */   {
/* 50 */     this.frontend.parse(
/* 51 */       "TYPE Tree = Empty | Leaf | Node TYPE Empty AS <> TYPE Leaf AS String TYPE Node AS <Tree,Tree> ");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 57 */     test_must_equal(
/* 58 */       "meta.name(?x,Tree),meta.typeConstructor(?x)", 
/* 59 */       "?x", this.frontend.findType("Tree"));
/* 60 */     test_must_findall(
/* 61 */       "meta.name(?type,Tree),meta.subtype(?type,?sub),meta.name(?sub,?subname)", 
/*    */       
/* 63 */       "?subname", 
/* 64 */       new String[] {
/* 65 */       "Empty", "Leaf", "Node" });
/*    */     
/*    */ 
/* 68 */     test_must_succeed(
/* 69 */       "meta.name(?x,Node),meta.typeConstructor(?x),meta.representation(?x,?rep::meta.TupleType)");
/*    */     
/*    */ 
/* 72 */     test_must_equal(
/* 73 */       "meta.name(?x,Node),meta.typeConstructor(?x),meta.representation(?x,?rep::meta.TupleType),length(?rep,?ct)", 
/*    */       
/*    */ 
/* 76 */       "?ct", 
/* 77 */       "2");
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/tests/MetaTypeTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */