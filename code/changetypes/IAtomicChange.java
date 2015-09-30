/*    */ package changetypes;
/*    */ 
/*    */ public abstract interface IAtomicChange
/*    */ {
/*    */   public static enum ChangeTypes
/*    */   {
/*  7 */     ADD_PACKAGE, 
/*  8 */     ADD_TYPE, 
/*  9 */     ADD_METHOD, 
/* 10 */     ADD_FIELD, 
/* 11 */     ADD_RETURN, 
/* 12 */     ADD_FIELDOFTYPE, 
/* 13 */     ADD_ACCESSES, 
/* 14 */     ADD_CALLS, 
/* 15 */     ADD_SUBTYPE, 
/* 16 */     ADD_INHERITEDFIELD, 
/* 17 */     ADD_INHERITEDMETHOD, 
/* 18 */     ADD_PARAMETER, 
/*    */     
/* 20 */     DEL_PACKAGE, 
/* 21 */     DEL_TYPE, 
/* 22 */     DEL_METHOD, 
/* 23 */     DEL_FIELD, 
/* 24 */     DEL_RETURN, 
/* 25 */     DEL_FIELDOFTYPE, 
/* 26 */     DEL_ACCESSES, 
/* 27 */     DEL_CALLS, 
/* 28 */     DEL_SUBTYPE, 
/* 29 */     DEL_INHERITEDFIELD, 
/* 30 */     DEL_INHERITEDMETHOD, 
/*    */     
/* 32 */     CHANGE_METHODBODY, 
/* 33 */     CHANGE_METHODSIGNATURE, 
/* 34 */     ADD_CONDITIONAL, 
/* 35 */     DEL_CONDITIONAL, 
/* 36 */     DEL_PARAMETER;
/*    */   }
/*    */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/changetypes/IAtomicChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */