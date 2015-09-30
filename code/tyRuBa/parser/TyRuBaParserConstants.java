/*     */ package tyRuBa.parser;
/*     */ 
/*     */ 
/*     */ public abstract interface TyRuBaParserConstants
/*     */ {
/*     */   public static final int EOF = 0;
/*     */   
/*     */   public static final int WHITECHAR = 1;
/*     */   
/*     */   public static final int COMMENT = 2;
/*     */   public static final int SINGLE_LINE_COMMENT = 3;
/*     */   public static final int FORMAL_COMMENT = 4;
/*     */   public static final int MULTI_LINE_COMMENT = 5;
/*     */   public static final int INTEGER_LITERAL = 6;
/*     */   public static final int DECIMAL_LITERAL = 7;
/*     */   public static final int HEX_LITERAL = 8;
/*     */   public static final int OCTAL_LITERAL = 9;
/*     */   public static final int FLOATING_POINT_LITERAL = 10;
/*     */   public static final int EXPONENT = 11;
/*     */   public static final int STRING_LITERAL = 12;
/*     */   public static final int INCLUDE = 13;
/*     */   public static final int LIBRARY = 14;
/*     */   public static final int GENERATE = 15;
/*     */   public static final int NOT = 16;
/*     */   public static final int EXISTS = 17;
/*     */   public static final int NODUP = 18;
/*     */   public static final int FINDALL = 19;
/*     */   public static final int COUNTALL = 20;
/*     */   public static final int UNIQUE = 21;
/*     */   public static final int TEST = 22;
/*     */   public static final int MODES = 23;
/*     */   public static final int IS = 24;
/*     */   public static final int REALLY = 25;
/*     */   public static final int TYPEDEF = 26;
/*     */   public static final int REPRESENTED_AS = 27;
/*     */   public static final int MODEEND = 28;
/*     */   public static final int DET = 29;
/*     */   public static final int SEMIDET = 30;
/*     */   public static final int MULTI = 31;
/*     */   public static final int NONDET = 32;
/*     */   public static final int BOUND = 33;
/*     */   public static final int DEF = 34;
/*     */   public static final int FACT = 35;
/*     */   public static final int OF = 36;
/*     */   public static final int PERSISTENT = 37;
/*     */   public static final int OR = 38;
/*     */   public static final int AND = 39;
/*     */   public static final int TYPE = 40;
/*     */   public static final int LPAREN = 41;
/*     */   public static final int RPAREN = 42;
/*     */   public static final int LBRACKET = 43;
/*     */   public static final int RBRACKET = 44;
/*     */   public static final int LANGLE = 45;
/*     */   public static final int RANGLE = 46;
/*     */   public static final int SEMICOLON = 47;
/*     */   public static final int COMMA = 48;
/*     */   public static final int DOT = 49;
/*     */   public static final int HASH = 50;
/*     */   public static final int UNQUOTE = 51;
/*     */   public static final int VERTSLASH = 52;
/*     */   public static final int SLASH = 53;
/*     */   public static final int WHEN = 54;
/*     */   public static final int PLUS = 55;
/*     */   public static final int STAR = 56;
/*     */   public static final int STRICT = 57;
/*     */   public static final int SPECIAL = 58;
/*     */   public static final int SPECIAL_CHAR = 59;
/*     */   public static final int IDENTIFIER = 60;
/*     */   public static final int VARIABLE = 61;
/*     */   public static final int TEMPLATE_VAR = 62;
/*     */   public static final int JAVA_CLASS = 63;
/*     */   public static final int REGEXP = 64;
/*     */   public static final int LOWCASE = 65;
/*     */   public static final int UPCASE = 66;
/*     */   public static final int LETTER = 67;
/*     */   public static final int DIGIT = 68;
/*     */   public static final int QUOTEDCODE = 73;
/*     */   public static final int DEFAULT = 0;
/*     */   public static final int inBraces = 1;
/*     */   public static final int inNestedBraces = 2;
/*  81 */   public static final String[] tokenImage = {
/*  82 */     "<EOF>", 
/*  83 */     "<WHITECHAR>", 
/*  84 */     "<COMMENT>", 
/*  85 */     "<SINGLE_LINE_COMMENT>", 
/*  86 */     "<FORMAL_COMMENT>", 
/*  87 */     "<MULTI_LINE_COMMENT>", 
/*  88 */     "<INTEGER_LITERAL>", 
/*  89 */     "<DECIMAL_LITERAL>", 
/*  90 */     "<HEX_LITERAL>", 
/*  91 */     "<OCTAL_LITERAL>", 
/*  92 */     "<FLOATING_POINT_LITERAL>", 
/*  93 */     "<EXPONENT>", 
/*  94 */     "<STRING_LITERAL>", 
/*  95 */     "\"#include\"", 
/*  96 */     "\"#library\"", 
/*  97 */     "\"#generate\"", 
/*  98 */     "\"NOT\"", 
/*  99 */     "\"EXISTS\"", 
/* 100 */     "\"NODUP\"", 
/* 101 */     "\"FINDALL\"", 
/* 102 */     "\"COUNTALL\"", 
/* 103 */     "\"UNIQUE\"", 
/* 104 */     "\"TEST\"", 
/* 105 */     "\"MODES\"", 
/* 106 */     "\"IS\"", 
/* 107 */     "\"REALLY\"", 
/* 108 */     "\"TYPE\"", 
/* 109 */     "\"AS\"", 
/* 110 */     "\"END\"", 
/* 111 */     "\"DET\"", 
/* 112 */     "\"SEMIDET\"", 
/* 113 */     "\"MULTI\"", 
/* 114 */     "\"NONDET\"", 
/* 115 */     "\"BOUND\"", 
/* 116 */     "\"DEFAULT\"", 
/* 117 */     "\"FACT\"", 
/* 118 */     "\"OF\"", 
/* 119 */     "\"PERSISTENT\"", 
/* 120 */     "\"||\"", 
/* 121 */     "\"&&\"", 
/* 122 */     "\"::\"", 
/* 123 */     "\"(\"", 
/* 124 */     "\")\"", 
/* 125 */     "\"[\"", 
/* 126 */     "\"]\"", 
/* 127 */     "\"<\"", 
/* 128 */     "\">\"", 
/* 129 */     "\";\"", 
/* 130 */     "\",\"", 
/* 131 */     "\".\"", 
/* 132 */     "\"#\"", 
/* 133 */     "\"@\"", 
/* 134 */     "\"|\"", 
/* 135 */     "\"/\"", 
/* 136 */     "\":-\"", 
/* 137 */     "\"+\"", 
/* 138 */     "\"*\"", 
/* 139 */     "\"=\"", 
/* 140 */     "<SPECIAL>", 
/* 141 */     "<SPECIAL_CHAR>", 
/* 142 */     "<IDENTIFIER>", 
/* 143 */     "<VARIABLE>", 
/* 144 */     "<TEMPLATE_VAR>", 
/* 145 */     "<JAVA_CLASS>", 
/* 146 */     "<REGEXP>", 
/* 147 */     "<LOWCASE>", 
/* 148 */     "<UPCASE>", 
/* 149 */     "<LETTER>", 
/* 150 */     "<DIGIT>", 
/* 151 */     "\"{\"", 
/* 152 */     "<token of kind 70>", 
/* 153 */     "\"{\"", 
/* 154 */     "\"}\"", 
/* 155 */     "\"}\"", 
/* 156 */     "\":\"" };
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/parser/TyRuBaParserConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */