/*     */ package tyRuBa.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenMgrError
/*     */   extends Error
/*     */ {
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int LOOP_DETECTED = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int errorCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String addEscapes(String str)
/*     */   {
/*  41 */     StringBuffer retval = new StringBuffer();
/*     */     
/*  43 */     for (int i = 0; i < str.length(); i++) {
/*  44 */       switch (str.charAt(i))
/*     */       {
/*     */       case '\000': 
/*     */         break;
/*     */       case '\b': 
/*  49 */         retval.append("\\b");
/*  50 */         break;
/*     */       case '\t': 
/*  52 */         retval.append("\\t");
/*  53 */         break;
/*     */       case '\n': 
/*  55 */         retval.append("\\n");
/*  56 */         break;
/*     */       case '\f': 
/*  58 */         retval.append("\\f");
/*  59 */         break;
/*     */       case '\r': 
/*  61 */         retval.append("\\r");
/*  62 */         break;
/*     */       case '"': 
/*  64 */         retval.append("\\\"");
/*  65 */         break;
/*     */       case '\'': 
/*  67 */         retval.append("\\'");
/*  68 */         break;
/*     */       case '\\': 
/*  70 */         retval.append("\\\\");
/*  71 */         break;
/*     */       default:  char ch;
/*  73 */         if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
/*  74 */           String s = "0000" + Integer.toString(ch, 16);
/*  75 */           retval.append("\\u" + s.substring(s.length() - 4, s.length()));
/*     */         } else {
/*  77 */           retval.append(ch);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  82 */     return retval.toString();
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
/*     */   protected static String LexicalError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar)
/*     */   {
/*  98 */     return 
/*     */     
/*     */ 
/*     */ 
/* 102 */       "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : new StringBuilder("\"").append(addEscapes(String.valueOf(curChar))).append("\"").append(" (").append(curChar).append("), ").toString()) + "after : \"" + addEscapes(errorAfter) + "\"";
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
/*     */   public String getMessage()
/*     */   {
/* 115 */     return super.getMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TokenMgrError() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public TokenMgrError(String message, int reason)
/*     */   {
/* 126 */     super(message);
/* 127 */     this.errorCode = reason;
/*     */   }
/*     */   
/*     */   public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
/* 131 */     this(LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/parser/TokenMgrError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */