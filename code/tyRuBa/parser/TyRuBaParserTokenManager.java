/*      */ package tyRuBa.parser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ 
/*      */ public class TyRuBaParserTokenManager implements TyRuBaParserConstants {
/*    6 */   int nestedBraces = 0;
/*    7 */   public java.io.PrintStream debugStream = System.out;
/*    8 */   public void setDebugStream(java.io.PrintStream ds) { this.debugStream = ds; }
/*      */   
/*      */   private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1) {
/*   11 */     switch (pos)
/*      */     {
/*      */     case 0: 
/*   14 */       if ((active0 & 0x20000000000000) != 0L)
/*   15 */         return 65;
/*   16 */       if ((active0 & 0x390004000000000) != 0L)
/*   17 */         return 28;
/*   18 */       if ((active0 & 0x3FFFFF0000) != 0L)
/*      */       {
/*   20 */         this.jjmatchedKind = 60;
/*   21 */         return 30;
/*      */       }
/*   23 */       if ((active0 & 0x400000000E000) != 0L)
/*   24 */         return 36;
/*   25 */       if ((active0 & 0x8000000000) != 0L)
/*      */       {
/*   27 */         this.jjmatchedKind = 58;
/*   28 */         return 28;
/*      */       }
/*   30 */       if ((active0 & 0x2000000000000) != 0L)
/*   31 */         return 5;
/*   32 */       return -1;
/*      */     case 1: 
/*   34 */       if ((active0 & 0xC000000000) != 0L)
/*   35 */         return 28;
/*   36 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*   38 */         this.jjmatchedKind = 60;
/*   39 */         this.jjmatchedPos = 1;
/*   40 */         return 101;
/*      */       }
/*   42 */       if ((active0 & 0x2FF6FF0000) != 0L)
/*      */       {
/*   44 */         this.jjmatchedKind = 60;
/*   45 */         this.jjmatchedPos = 1;
/*   46 */         return 30;
/*      */       }
/*   48 */       if ((active0 & 0x1009000000) != 0L)
/*   49 */         return 30;
/*   50 */       return -1;
/*      */     case 2: 
/*   52 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*   54 */         this.jjmatchedKind = 60;
/*   55 */         this.jjmatchedPos = 2;
/*   56 */         return 101;
/*      */       }
/*   58 */       if ((active0 & 0x2FC6FE0000) != 0L)
/*      */       {
/*   60 */         this.jjmatchedKind = 60;
/*   61 */         this.jjmatchedPos = 2;
/*   62 */         return 30;
/*      */       }
/*   64 */       if ((active0 & 0x30010000) != 0L)
/*   65 */         return 30;
/*   66 */       return -1;
/*      */     case 3: 
/*   68 */       if ((active0 & 0x27C2BE0000) != 0L)
/*      */       {
/*   70 */         this.jjmatchedKind = 60;
/*   71 */         this.jjmatchedPos = 3;
/*   72 */         return 30;
/*      */       }
/*   74 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*   76 */         this.jjmatchedKind = 60;
/*   77 */         this.jjmatchedPos = 3;
/*   78 */         return 101;
/*      */       }
/*   80 */       if ((active0 & 0x804400000) != 0L)
/*   81 */         return 30;
/*   82 */       return -1;
/*      */     case 4: 
/*   84 */       if ((active0 & 0x25423A0000) != 0L)
/*      */       {
/*   86 */         this.jjmatchedKind = 60;
/*   87 */         this.jjmatchedPos = 4;
/*   88 */         return 30;
/*      */       }
/*   90 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*   92 */         this.jjmatchedKind = 60;
/*   93 */         this.jjmatchedPos = 4;
/*   94 */         return 101;
/*      */       }
/*   96 */       if ((active0 & 0x280840000) != 0L)
/*   97 */         return 30;
/*   98 */       return -1;
/*      */     case 5: 
/*  100 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*  102 */         this.jjmatchedKind = 60;
/*  103 */         this.jjmatchedPos = 5;
/*  104 */         return 101;
/*      */       }
/*  106 */       if ((active0 & 0x2440180000) != 0L)
/*      */       {
/*  108 */         this.jjmatchedKind = 60;
/*  109 */         this.jjmatchedPos = 5;
/*  110 */         return 30;
/*      */       }
/*  112 */       if ((active0 & 0x102220000) != 0L)
/*  113 */         return 30;
/*  114 */       return -1;
/*      */     case 6: 
/*  116 */       if ((active0 & 0x2000100000) != 0L)
/*      */       {
/*  118 */         this.jjmatchedKind = 60;
/*  119 */         this.jjmatchedPos = 6;
/*  120 */         return 30;
/*      */       }
/*  122 */       if ((active0 & 0xE000) != 0L)
/*      */       {
/*  124 */         this.jjmatchedKind = 60;
/*  125 */         this.jjmatchedPos = 6;
/*  126 */         return 101;
/*      */       }
/*  128 */       if ((active0 & 0x440080000) != 0L)
/*  129 */         return 30;
/*  130 */       return -1;
/*      */     case 7: 
/*  132 */       if ((active0 & 0x2000000000) != 0L)
/*      */       {
/*  134 */         this.jjmatchedKind = 60;
/*  135 */         this.jjmatchedPos = 7;
/*  136 */         return 30;
/*      */       }
/*  138 */       if ((active0 & 0x8000) != 0L)
/*      */       {
/*  140 */         this.jjmatchedKind = 60;
/*  141 */         this.jjmatchedPos = 7;
/*  142 */         return 101;
/*      */       }
/*  144 */       if ((active0 & 0x6000) != 0L)
/*  145 */         return 101;
/*  146 */       if ((active0 & 0x100000) != 0L)
/*  147 */         return 30;
/*  148 */       return -1;
/*      */     case 8: 
/*  150 */       if ((active0 & 0x8000) != 0L)
/*  151 */         return 101;
/*  152 */       if ((active0 & 0x2000000000) != 0L)
/*      */       {
/*  154 */         this.jjmatchedKind = 60;
/*  155 */         this.jjmatchedPos = 8;
/*  156 */         return 30;
/*      */       }
/*  158 */       return -1;
/*      */     }
/*  160 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_0(int pos, long active0, long active1)
/*      */   {
/*  165 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStopAtPos(int pos, int kind) {
/*  169 */     this.jjmatchedKind = kind;
/*  170 */     this.jjmatchedPos = pos;
/*  171 */     return pos + 1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  175 */     this.jjmatchedKind = kind;
/*  176 */     this.jjmatchedPos = pos;
/*  177 */     try { this.curChar = this.input_stream.readChar();
/*  178 */     } catch (IOException localIOException) { return pos + 1; }
/*  179 */     return jjMoveNfa_0(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_0() {
/*  183 */     switch (this.curChar)
/*      */     {
/*      */     case '#': 
/*  186 */       this.jjmatchedKind = 50;
/*  187 */       return jjMoveStringLiteralDfa1_0(57344L);
/*      */     case '&': 
/*  189 */       return jjMoveStringLiteralDfa1_0(549755813888L);
/*      */     case '(': 
/*  191 */       return jjStopAtPos(0, 41);
/*      */     case ')': 
/*  193 */       return jjStopAtPos(0, 42);
/*      */     case '*': 
/*  195 */       return jjStartNfaWithStates_0(0, 56, 28);
/*      */     case '+': 
/*  197 */       return jjStartNfaWithStates_0(0, 55, 28);
/*      */     case ',': 
/*  199 */       return jjStopAtPos(0, 48);
/*      */     case '.': 
/*  201 */       return jjStartNfaWithStates_0(0, 49, 5);
/*      */     case '/': 
/*  203 */       return jjStartNfaWithStates_0(0, 53, 65);
/*      */     case ':': 
/*  205 */       this.jjmatchedKind = 74;
/*  206 */       return jjMoveStringLiteralDfa1_0(18015498021109760L);
/*      */     case ';': 
/*  208 */       return jjStopAtPos(0, 47);
/*      */     case '<': 
/*  210 */       return jjStopAtPos(0, 45);
/*      */     case '=': 
/*  212 */       return jjStartNfaWithStates_0(0, 57, 28);
/*      */     case '>': 
/*  214 */       return jjStopAtPos(0, 46);
/*      */     case '@': 
/*  216 */       return jjStopAtPos(0, 51);
/*      */     case 'A': 
/*  218 */       return jjMoveStringLiteralDfa1_0(134217728L);
/*      */     case 'B': 
/*  220 */       return jjMoveStringLiteralDfa1_0(8589934592L);
/*      */     case 'C': 
/*  222 */       return jjMoveStringLiteralDfa1_0(1048576L);
/*      */     case 'D': 
/*  224 */       return jjMoveStringLiteralDfa1_0(17716740096L);
/*      */     case 'E': 
/*  226 */       return jjMoveStringLiteralDfa1_0(268566528L);
/*      */     case 'F': 
/*  228 */       return jjMoveStringLiteralDfa1_0(34360262656L);
/*      */     case 'I': 
/*  230 */       return jjMoveStringLiteralDfa1_0(16777216L);
/*      */     case 'M': 
/*  232 */       return jjMoveStringLiteralDfa1_0(2155872256L);
/*      */     case 'N': 
/*  234 */       return jjMoveStringLiteralDfa1_0(4295294976L);
/*      */     case 'O': 
/*  236 */       return jjMoveStringLiteralDfa1_0(68719476736L);
/*      */     case 'P': 
/*  238 */       return jjMoveStringLiteralDfa1_0(137438953472L);
/*      */     case 'R': 
/*  240 */       return jjMoveStringLiteralDfa1_0(33554432L);
/*      */     case 'S': 
/*  242 */       return jjMoveStringLiteralDfa1_0(1073741824L);
/*      */     case 'T': 
/*  244 */       return jjMoveStringLiteralDfa1_0(71303168L);
/*      */     case 'U': 
/*  246 */       return jjMoveStringLiteralDfa1_0(2097152L);
/*      */     case '[': 
/*  248 */       return jjStopAtPos(0, 43);
/*      */     case ']': 
/*  250 */       return jjStopAtPos(0, 44);
/*      */     case '{': 
/*  252 */       return jjStopAtPos(0, 69);
/*      */     case '|': 
/*  254 */       this.jjmatchedKind = 52;
/*  255 */       return jjMoveStringLiteralDfa1_0(274877906944L);
/*      */     }
/*  257 */     return jjMoveNfa_0(0, 0);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa1_0(long active0) {
/*      */     try {
/*  262 */       this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  264 */       jjStopStringLiteralDfa_0(0, active0, 0L);
/*  265 */       return 1;
/*      */     }
/*  267 */     switch (this.curChar)
/*      */     {
/*      */     case '&': 
/*  270 */       if ((active0 & 0x8000000000) != 0L)
/*  271 */         return jjStartNfaWithStates_0(1, 39, 28);
/*      */       break;
/*      */     case '-': 
/*  274 */       if ((active0 & 0x40000000000000) != 0L)
/*  275 */         return jjStopAtPos(1, 54);
/*      */       break;
/*      */     case ':': 
/*  278 */       if ((active0 & 0x10000000000) != 0L)
/*  279 */         return jjStopAtPos(1, 40);
/*      */       break;
/*      */     case 'A': 
/*  282 */       return jjMoveStringLiteralDfa2_0(active0, 34359738368L);
/*      */     case 'E': 
/*  284 */       return jjMoveStringLiteralDfa2_0(active0, 156267184128L);
/*      */     case 'F': 
/*  286 */       if ((active0 & 0x1000000000) != 0L)
/*  287 */         return jjStartNfaWithStates_0(1, 36, 30);
/*      */       break;
/*      */     case 'I': 
/*  290 */       return jjMoveStringLiteralDfa2_0(active0, 524288L);
/*      */     case 'N': 
/*  292 */       return jjMoveStringLiteralDfa2_0(active0, 270532608L);
/*      */     case 'O': 
/*  294 */       return jjMoveStringLiteralDfa2_0(active0, 12894666752L);
/*      */     case 'S': 
/*  296 */       if ((active0 & 0x1000000) != 0L)
/*  297 */         return jjStartNfaWithStates_0(1, 24, 30);
/*  298 */       if ((active0 & 0x8000000) != 0L)
/*  299 */         return jjStartNfaWithStates_0(1, 27, 30);
/*      */       break;
/*      */     case 'U': 
/*  302 */       return jjMoveStringLiteralDfa2_0(active0, 2147483648L);
/*      */     case 'X': 
/*  304 */       return jjMoveStringLiteralDfa2_0(active0, 131072L);
/*      */     case 'Y': 
/*  306 */       return jjMoveStringLiteralDfa2_0(active0, 67108864L);
/*      */     case 'g': 
/*  308 */       return jjMoveStringLiteralDfa2_0(active0, 32768L);
/*      */     case 'i': 
/*  310 */       return jjMoveStringLiteralDfa2_0(active0, 8192L);
/*      */     case 'l': 
/*  312 */       return jjMoveStringLiteralDfa2_0(active0, 16384L);
/*      */     case '|': 
/*  314 */       if ((active0 & 0x4000000000) != 0L) {
/*  315 */         return jjStartNfaWithStates_0(1, 38, 28);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  320 */     return jjStartNfa_0(0, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/*  324 */     if ((active0 &= old0) == 0L)
/*  325 */       return jjStartNfa_0(0, old0, 0L);
/*  326 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  328 */       jjStopStringLiteralDfa_0(1, active0, 0L);
/*  329 */       return 2;
/*      */     }
/*  331 */     switch (this.curChar)
/*      */     {
/*      */     case 'A': 
/*  334 */       return jjMoveStringLiteralDfa3_0(active0, 33554432L);
/*      */     case 'C': 
/*  336 */       return jjMoveStringLiteralDfa3_0(active0, 34359738368L);
/*      */     case 'D': 
/*  338 */       if ((active0 & 0x10000000) != 0L)
/*  339 */         return jjStartNfaWithStates_0(2, 28, 30);
/*  340 */       return jjMoveStringLiteralDfa3_0(active0, 8650752L);
/*      */     case 'F': 
/*  342 */       return jjMoveStringLiteralDfa3_0(active0, 17179869184L);
/*      */     case 'I': 
/*  344 */       return jjMoveStringLiteralDfa3_0(active0, 2228224L);
/*      */     case 'L': 
/*  346 */       return jjMoveStringLiteralDfa3_0(active0, 2147483648L);
/*      */     case 'M': 
/*  348 */       return jjMoveStringLiteralDfa3_0(active0, 1073741824L);
/*      */     case 'N': 
/*  350 */       return jjMoveStringLiteralDfa3_0(active0, 4295491584L);
/*      */     case 'P': 
/*  352 */       return jjMoveStringLiteralDfa3_0(active0, 67108864L);
/*      */     case 'R': 
/*  354 */       return jjMoveStringLiteralDfa3_0(active0, 137438953472L);
/*      */     case 'S': 
/*  356 */       return jjMoveStringLiteralDfa3_0(active0, 4194304L);
/*      */     case 'T': 
/*  358 */       if ((active0 & 0x10000) != 0L)
/*  359 */         return jjStartNfaWithStates_0(2, 16, 30);
/*  360 */       if ((active0 & 0x20000000) != 0L)
/*  361 */         return jjStartNfaWithStates_0(2, 29, 30);
/*      */       break;
/*      */     case 'U': 
/*  364 */       return jjMoveStringLiteralDfa3_0(active0, 8590983168L);
/*      */     case 'e': 
/*  366 */       return jjMoveStringLiteralDfa3_0(active0, 32768L);
/*      */     case 'i': 
/*  368 */       return jjMoveStringLiteralDfa3_0(active0, 16384L);
/*      */     case 'n': 
/*  370 */       return jjMoveStringLiteralDfa3_0(active0, 8192L);
/*      */     }
/*      */     
/*      */     
/*  374 */     return jjStartNfa_0(1, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/*  378 */     if ((active0 &= old0) == 0L)
/*  379 */       return jjStartNfa_0(1, old0, 0L);
/*  380 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  382 */       jjStopStringLiteralDfa_0(2, active0, 0L);
/*  383 */       return 3;
/*      */     }
/*  385 */     switch (this.curChar)
/*      */     {
/*      */     case 'A': 
/*  388 */       return jjMoveStringLiteralDfa4_0(active0, 17179869184L);
/*      */     case 'D': 
/*  390 */       return jjMoveStringLiteralDfa4_0(active0, 4295491584L);
/*      */     case 'E': 
/*  392 */       if ((active0 & 0x4000000) != 0L)
/*  393 */         return jjStartNfaWithStates_0(3, 26, 30);
/*  394 */       return jjMoveStringLiteralDfa4_0(active0, 8388608L);
/*      */     case 'I': 
/*  396 */       return jjMoveStringLiteralDfa4_0(active0, 1073741824L);
/*      */     case 'L': 
/*  398 */       return jjMoveStringLiteralDfa4_0(active0, 33554432L);
/*      */     case 'N': 
/*  400 */       return jjMoveStringLiteralDfa4_0(active0, 8590983168L);
/*      */     case 'Q': 
/*  402 */       return jjMoveStringLiteralDfa4_0(active0, 2097152L);
/*      */     case 'S': 
/*  404 */       return jjMoveStringLiteralDfa4_0(active0, 137439084544L);
/*      */     case 'T': 
/*  406 */       if ((active0 & 0x400000) != 0L)
/*  407 */         return jjStartNfaWithStates_0(3, 22, 30);
/*  408 */       if ((active0 & 0x800000000) != 0L)
/*  409 */         return jjStartNfaWithStates_0(3, 35, 30);
/*  410 */       return jjMoveStringLiteralDfa4_0(active0, 2147483648L);
/*      */     case 'U': 
/*  412 */       return jjMoveStringLiteralDfa4_0(active0, 262144L);
/*      */     case 'b': 
/*  414 */       return jjMoveStringLiteralDfa4_0(active0, 16384L);
/*      */     case 'c': 
/*  416 */       return jjMoveStringLiteralDfa4_0(active0, 8192L);
/*      */     case 'n': 
/*  418 */       return jjMoveStringLiteralDfa4_0(active0, 32768L);
/*      */     }
/*      */     
/*      */     
/*  422 */     return jjStartNfa_0(2, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/*  426 */     if ((active0 &= old0) == 0L)
/*  427 */       return jjStartNfa_0(2, old0, 0L);
/*  428 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  430 */       jjStopStringLiteralDfa_0(3, active0, 0L);
/*  431 */       return 4;
/*      */     }
/*  433 */     switch (this.curChar)
/*      */     {
/*      */     case 'A': 
/*  436 */       return jjMoveStringLiteralDfa5_0(active0, 524288L);
/*      */     case 'D': 
/*  438 */       if ((active0 & 0x200000000) != 0L)
/*  439 */         return jjStartNfaWithStates_0(4, 33, 30);
/*  440 */       return jjMoveStringLiteralDfa5_0(active0, 1073741824L);
/*      */     case 'E': 
/*  442 */       return jjMoveStringLiteralDfa5_0(active0, 4294967296L);
/*      */     case 'I': 
/*  444 */       if ((active0 & 0x80000000) != 0L)
/*  445 */         return jjStartNfaWithStates_0(4, 31, 30);
/*  446 */       return jjMoveStringLiteralDfa5_0(active0, 137438953472L);
/*      */     case 'L': 
/*  448 */       return jjMoveStringLiteralDfa5_0(active0, 33554432L);
/*      */     case 'P': 
/*  450 */       if ((active0 & 0x40000) != 0L)
/*  451 */         return jjStartNfaWithStates_0(4, 18, 30);
/*      */       break;
/*      */     case 'S': 
/*  454 */       if ((active0 & 0x800000) != 0L)
/*  455 */         return jjStartNfaWithStates_0(4, 23, 30);
/*      */       break;
/*      */     case 'T': 
/*  458 */       return jjMoveStringLiteralDfa5_0(active0, 1179648L);
/*      */     case 'U': 
/*  460 */       return jjMoveStringLiteralDfa5_0(active0, 17181966336L);
/*      */     case 'e': 
/*  462 */       return jjMoveStringLiteralDfa5_0(active0, 32768L);
/*      */     case 'l': 
/*  464 */       return jjMoveStringLiteralDfa5_0(active0, 8192L);
/*      */     case 'r': 
/*  466 */       return jjMoveStringLiteralDfa5_0(active0, 16384L);
/*      */     }
/*      */     
/*      */     
/*  470 */     return jjStartNfa_0(3, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/*  474 */     if ((active0 &= old0) == 0L)
/*  475 */       return jjStartNfa_0(3, old0, 0L);
/*  476 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  478 */       jjStopStringLiteralDfa_0(4, active0, 0L);
/*  479 */       return 5;
/*      */     }
/*  481 */     switch (this.curChar)
/*      */     {
/*      */     case 'A': 
/*  484 */       return jjMoveStringLiteralDfa6_0(active0, 1048576L);
/*      */     case 'E': 
/*  486 */       if ((active0 & 0x200000) != 0L)
/*  487 */         return jjStartNfaWithStates_0(5, 21, 30);
/*  488 */       return jjMoveStringLiteralDfa6_0(active0, 1073741824L);
/*      */     case 'L': 
/*  490 */       return jjMoveStringLiteralDfa6_0(active0, 17180393472L);
/*      */     case 'S': 
/*  492 */       if ((active0 & 0x20000) != 0L)
/*  493 */         return jjStartNfaWithStates_0(5, 17, 30);
/*  494 */       return jjMoveStringLiteralDfa6_0(active0, 137438953472L);
/*      */     case 'T': 
/*  496 */       if ((active0 & 0x100000000) != 0L)
/*  497 */         return jjStartNfaWithStates_0(5, 32, 30);
/*      */       break;
/*      */     case 'Y': 
/*  500 */       if ((active0 & 0x2000000) != 0L)
/*  501 */         return jjStartNfaWithStates_0(5, 25, 30);
/*      */       break;
/*      */     case 'a': 
/*  504 */       return jjMoveStringLiteralDfa6_0(active0, 16384L);
/*      */     case 'r': 
/*  506 */       return jjMoveStringLiteralDfa6_0(active0, 32768L);
/*      */     case 'u': 
/*  508 */       return jjMoveStringLiteralDfa6_0(active0, 8192L);
/*      */     }
/*      */     
/*      */     
/*  512 */     return jjStartNfa_0(4, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/*  516 */     if ((active0 &= old0) == 0L)
/*  517 */       return jjStartNfa_0(4, old0, 0L);
/*  518 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  520 */       jjStopStringLiteralDfa_0(5, active0, 0L);
/*  521 */       return 6;
/*      */     }
/*  523 */     switch (this.curChar)
/*      */     {
/*      */     case 'L': 
/*  526 */       if ((active0 & 0x80000) != 0L)
/*  527 */         return jjStartNfaWithStates_0(6, 19, 30);
/*  528 */       return jjMoveStringLiteralDfa7_0(active0, 1048576L);
/*      */     case 'T': 
/*  530 */       if ((active0 & 0x40000000) != 0L)
/*  531 */         return jjStartNfaWithStates_0(6, 30, 30);
/*  532 */       if ((active0 & 0x400000000) != 0L)
/*  533 */         return jjStartNfaWithStates_0(6, 34, 30);
/*  534 */       return jjMoveStringLiteralDfa7_0(active0, 137438953472L);
/*      */     case 'a': 
/*  536 */       return jjMoveStringLiteralDfa7_0(active0, 32768L);
/*      */     case 'd': 
/*  538 */       return jjMoveStringLiteralDfa7_0(active0, 8192L);
/*      */     case 'r': 
/*  540 */       return jjMoveStringLiteralDfa7_0(active0, 16384L);
/*      */     }
/*      */     
/*      */     
/*  544 */     return jjStartNfa_0(5, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/*  548 */     if ((active0 &= old0) == 0L)
/*  549 */       return jjStartNfa_0(5, old0, 0L);
/*  550 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  552 */       jjStopStringLiteralDfa_0(6, active0, 0L);
/*  553 */       return 7;
/*      */     }
/*  555 */     switch (this.curChar)
/*      */     {
/*      */     case 'E': 
/*  558 */       return jjMoveStringLiteralDfa8_0(active0, 137438953472L);
/*      */     case 'L': 
/*  560 */       if ((active0 & 0x100000) != 0L)
/*  561 */         return jjStartNfaWithStates_0(7, 20, 30);
/*      */       break;
/*      */     case 'e': 
/*  564 */       if ((active0 & 0x2000) != 0L)
/*  565 */         return jjStartNfaWithStates_0(7, 13, 101);
/*      */       break;
/*      */     case 't': 
/*  568 */       return jjMoveStringLiteralDfa8_0(active0, 32768L);
/*      */     case 'y': 
/*  570 */       if ((active0 & 0x4000) != 0L) {
/*  571 */         return jjStartNfaWithStates_0(7, 14, 101);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  576 */     return jjStartNfa_0(6, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/*  580 */     if ((active0 &= old0) == 0L)
/*  581 */       return jjStartNfa_0(6, old0, 0L);
/*  582 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  584 */       jjStopStringLiteralDfa_0(7, active0, 0L);
/*  585 */       return 8;
/*      */     }
/*  587 */     switch (this.curChar)
/*      */     {
/*      */     case 'N': 
/*  590 */       return jjMoveStringLiteralDfa9_0(active0, 137438953472L);
/*      */     case 'e': 
/*  592 */       if ((active0 & 0x8000) != 0L) {
/*  593 */         return jjStartNfaWithStates_0(8, 15, 101);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  598 */     return jjStartNfa_0(7, active0, 0L);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/*  602 */     if ((active0 &= old0) == 0L)
/*  603 */       return jjStartNfa_0(7, old0, 0L);
/*  604 */     try { this.curChar = this.input_stream.readChar();
/*      */     } catch (IOException localIOException) {
/*  606 */       jjStopStringLiteralDfa_0(8, active0, 0L);
/*  607 */       return 9;
/*      */     }
/*  609 */     switch (this.curChar)
/*      */     {
/*      */     case 'T': 
/*  612 */       if ((active0 & 0x2000000000) != 0L) {
/*  613 */         return jjStartNfaWithStates_0(9, 37, 30);
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  618 */     return jjStartNfa_0(8, active0, 0L);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAdd(int state) {
/*  622 */     if (this.jjrounds[state] != this.jjround)
/*      */     {
/*  624 */       this.jjstateSet[(this.jjnewStateCnt++)] = state;
/*  625 */       this.jjrounds[state] = this.jjround;
/*      */     }
/*      */   }
/*      */   
/*      */   private final void jjAddStates(int start, int end) {
/*      */     do {
/*  631 */       this.jjstateSet[(this.jjnewStateCnt++)] = jjnextStates[start];
/*  632 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddTwoStates(int state1, int state2) {
/*  636 */     jjCheckNAdd(state1);
/*  637 */     jjCheckNAdd(state2);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start, int end) {
/*      */     do {
/*  642 */       jjCheckNAdd(jjnextStates[start]);
/*  643 */     } while (start++ != end);
/*      */   }
/*      */   
/*      */   private final void jjCheckNAddStates(int start) {
/*  647 */     jjCheckNAdd(jjnextStates[start]);
/*  648 */     jjCheckNAdd(jjnextStates[(start + 1)]); }
/*      */   
/*  650 */   static final long[] jjbitVec0 = {
/*  651 */     -2L, -1L, -1L, -1L };
/*      */   
/*  653 */   static final long[] jjbitVec2 = {
/*  654 */     0, 0, -1L, -1L };
/*      */   
/*  656 */   static final long[] jjbitVec3 = {
/*  657 */     2301339413881290750L, -16384L, 4294967295L, 432345564227567616L };
/*      */   
/*  659 */   static final long[] jjbitVec4 = {
/*  660 */     000-36028797027352577L };
/*      */   
/*  662 */   static final long[] jjbitVec5 = {
/*  663 */     0, -1L, -1L, -1L };
/*      */   
/*  665 */   static final long[] jjbitVec6 = {
/*  666 */     -1L, -1L, 65535L };
/*      */   
/*  668 */   static final long[] jjbitVec7 = {
/*  669 */     -1L, -1L };
/*      */   
/*  671 */   static final long[] jjbitVec8 = {
/*  672 */     70368744177663L };
/*      */   
/*      */ 
/*      */   private final int jjMoveNfa_0(int startState, int curPos)
/*      */   {
/*  677 */     int startsAt = 0;
/*  678 */     this.jjnewStateCnt = 101;
/*  679 */     int i = 1;
/*  680 */     this.jjstateSet[0] = startState;
/*  681 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/*  684 */       if (++this.jjround == Integer.MAX_VALUE)
/*  685 */         ReInitRounds();
/*  686 */       if (this.curChar < '@')
/*      */       {
/*  688 */         long l = 1L << this.curChar;
/*      */         do
/*      */         {
/*  691 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 36: 
/*  694 */             if ((0x3FF4C1800000000 & l) != 0L)
/*      */             {
/*  696 */               if (kind > 60)
/*  697 */                 kind = 60;
/*  698 */               jjCheckNAdd(30);
/*      */             }
/*  700 */             if ((0x20002C7A00000000 & l) != 0L)
/*      */             {
/*  702 */               if (kind > 58)
/*  703 */                 kind = 58;
/*  704 */               jjCheckNAdd(28);
/*      */             }
/*  706 */             if ((0x1800000000 & l) != 0L)
/*      */             {
/*  708 */               if (kind > 63)
/*  709 */                 kind = 63;
/*  710 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/*  712 */             break;
/*      */           case 101: 
/*  714 */             if ((0x3FF4C1800000000 & l) != 0L)
/*      */             {
/*  716 */               if (kind > 63)
/*  717 */                 kind = 63;
/*  718 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/*  720 */             if ((0x3FF4C1800000000 & l) != 0L)
/*      */             {
/*  722 */               if (kind > 60)
/*  723 */                 kind = 60;
/*  724 */               jjCheckNAdd(30);
/*      */             }
/*  726 */             break;
/*      */           case 65: 
/*  728 */             if ((0xFFFF7FFFFFFFFFFF & l) != 0L) {
/*  729 */               jjCheckNAddStates(0, 2);
/*  730 */             } else if (this.curChar == '/')
/*      */             {
/*  732 */               if (kind > 64)
/*  733 */                 kind = 64;
/*      */             }
/*  735 */             if (this.curChar == '*') {
/*  736 */               jjCheckNAddTwoStates(95, 96);
/*  737 */             } else if (this.curChar == '/')
/*  738 */               jjCheckNAddStates(3, 5);
/*  739 */             if (this.curChar == '*') {
/*  740 */               this.jjstateSet[(this.jjnewStateCnt++)] = 87;
/*  741 */             } else if (this.curChar == '/')
/*  742 */               jjCheckNAddStates(6, 8);
/*  743 */             if (this.curChar == '*')
/*  744 */               jjCheckNAddTwoStates(78, 79);
/*  745 */             if (this.curChar == '*')
/*  746 */               this.jjstateSet[(this.jjnewStateCnt++)] = 70;
/*  747 */             break;
/*      */           case 0: 
/*  749 */             if ((0x3FF000000000000 & l) != 0L) {
/*  750 */               jjCheckNAddStates(9, 15);
/*  751 */             } else if ((0x20002C7A00000000 & l) != 0L)
/*      */             {
/*  753 */               if (kind > 58)
/*  754 */                 kind = 58;
/*  755 */               jjCheckNAdd(28);
/*      */             }
/*  757 */             else if ((0x100003600 & l) != 0L)
/*      */             {
/*  759 */               if (kind > 1) {
/*  760 */                 kind = 1;
/*      */               }
/*  762 */             } else if (this.curChar == '/') {
/*  763 */               jjAddStates(16, 21);
/*  764 */             } else if (this.curChar == '\'') {
/*  765 */               jjCheckNAddStates(22, 24);
/*  766 */             } else if (this.curChar == '"') {
/*  767 */               jjCheckNAddStates(25, 27);
/*  768 */             } else if (this.curChar == '.') {
/*  769 */               jjCheckNAdd(5);
/*  770 */             } else if (this.curChar == '?')
/*      */             {
/*  772 */               if (kind > 61)
/*  773 */                 kind = 61;
/*  774 */               jjCheckNAdd(32);
/*      */             }
/*  776 */             if ((0x3FE000000000000 & l) != 0L)
/*      */             {
/*  778 */               if (kind > 6)
/*  779 */                 kind = 6;
/*  780 */               jjCheckNAddTwoStates(2, 3);
/*      */             }
/*  782 */             else if ((0x1800000000 & l) != 0L)
/*      */             {
/*  784 */               if (kind > 60)
/*  785 */                 kind = 60;
/*  786 */               jjCheckNAdd(30);
/*      */             }
/*  788 */             else if (this.curChar == '-') {
/*  789 */               jjAddStates(28, 30);
/*  790 */             } else if (this.curChar == '0')
/*      */             {
/*  792 */               if (kind > 6)
/*  793 */                 kind = 6;
/*  794 */               jjCheckNAddStates(31, 33);
/*      */             }
/*  796 */             else if (this.curChar == '/') {
/*  797 */               jjCheckNAddStates(0, 2);
/*  798 */             } else if (this.curChar == '!')
/*      */             {
/*  800 */               if (kind > 62)
/*  801 */                 kind = 62;
/*  802 */               jjCheckNAdd(34);
/*      */             }
/*  804 */             if (this.curChar == '#')
/*  805 */               this.jjstateSet[(this.jjnewStateCnt++)] = 36;
/*  806 */             break;
/*      */           case 1: 
/*  808 */             if ((0x3FE000000000000 & l) != 0L)
/*      */             {
/*  810 */               if (kind > 6)
/*  811 */                 kind = 6;
/*  812 */               jjCheckNAddTwoStates(2, 3); }
/*  813 */             break;
/*      */           case 2: 
/*  815 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  817 */               if (kind > 6)
/*  818 */                 kind = 6;
/*  819 */               jjCheckNAddTwoStates(2, 3); }
/*  820 */             break;
/*      */           case 4: 
/*  822 */             if (this.curChar == '.')
/*  823 */               jjCheckNAdd(5);
/*  824 */             break;
/*      */           case 5: 
/*  826 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  828 */               if (kind > 10)
/*  829 */                 kind = 10;
/*  830 */               jjCheckNAddStates(34, 36); }
/*  831 */             break;
/*      */           case 7: 
/*  833 */             if ((0x280000000000 & l) != 0L)
/*  834 */               jjCheckNAdd(8);
/*  835 */             break;
/*      */           case 8: 
/*  837 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  839 */               if (kind > 10)
/*  840 */                 kind = 10;
/*  841 */               jjCheckNAddTwoStates(8, 9); }
/*  842 */             break;
/*      */           case 10: 
/*  844 */             if (this.curChar == '"')
/*  845 */               jjCheckNAddStates(25, 27);
/*  846 */             break;
/*      */           case 11: 
/*  848 */             if ((0xFFFFFFFBFFFFDBFF & l) != 0L)
/*  849 */               jjCheckNAddStates(25, 27);
/*  850 */             break;
/*      */           case 13: 
/*  852 */             if ((0x8400000000 & l) != 0L)
/*  853 */               jjCheckNAddStates(25, 27);
/*  854 */             break;
/*      */           case 14: 
/*  856 */             if ((this.curChar == '"') && (kind > 12))
/*  857 */               kind = 12;
/*  858 */             break;
/*      */           case 15: 
/*  860 */             if ((0xFF000000000000 & l) != 0L)
/*  861 */               jjCheckNAddStates(37, 40);
/*  862 */             break;
/*      */           case 16: 
/*  864 */             if ((0xFF000000000000 & l) != 0L)
/*  865 */               jjCheckNAddStates(25, 27);
/*  866 */             break;
/*      */           case 17: 
/*  868 */             if ((0xF000000000000 & l) != 0L)
/*  869 */               this.jjstateSet[(this.jjnewStateCnt++)] = 18;
/*  870 */             break;
/*      */           case 18: 
/*  872 */             if ((0xFF000000000000 & l) != 0L)
/*  873 */               jjCheckNAdd(16);
/*  874 */             break;
/*      */           case 19: 
/*  876 */             if (this.curChar == '\'')
/*  877 */               jjCheckNAddStates(22, 24);
/*  878 */             break;
/*      */           case 20: 
/*  880 */             if ((0xFFFFFF7FFFFFDBFF & l) != 0L)
/*  881 */               jjCheckNAddStates(22, 24);
/*  882 */             break;
/*      */           case 22: 
/*  884 */             if ((0x8400000000 & l) != 0L)
/*  885 */               jjCheckNAddStates(22, 24);
/*  886 */             break;
/*      */           case 23: 
/*  888 */             if ((this.curChar == '\'') && (kind > 12))
/*  889 */               kind = 12;
/*  890 */             break;
/*      */           case 24: 
/*  892 */             if ((0xFF000000000000 & l) != 0L)
/*  893 */               jjCheckNAddStates(41, 44);
/*  894 */             break;
/*      */           case 25: 
/*  896 */             if ((0xFF000000000000 & l) != 0L)
/*  897 */               jjCheckNAddStates(22, 24);
/*  898 */             break;
/*      */           case 26: 
/*  900 */             if ((0xF000000000000 & l) != 0L)
/*  901 */               this.jjstateSet[(this.jjnewStateCnt++)] = 27;
/*  902 */             break;
/*      */           case 27: 
/*  904 */             if ((0xFF000000000000 & l) != 0L)
/*  905 */               jjCheckNAdd(25);
/*  906 */             break;
/*      */           case 28: 
/*  908 */             if ((0x20002C7A00000000 & l) != 0L)
/*      */             {
/*  910 */               if (kind > 58)
/*  911 */                 kind = 58;
/*  912 */               jjCheckNAdd(28); }
/*  913 */             break;
/*      */           case 29: 
/*  915 */             if ((0x1800000000 & l) != 0L)
/*      */             {
/*  917 */               if (kind > 60)
/*  918 */                 kind = 60;
/*  919 */               jjCheckNAdd(30); }
/*  920 */             break;
/*      */           case 30: 
/*  922 */             if ((0x3FF4C1800000000 & l) != 0L)
/*      */             {
/*  924 */               if (kind > 60)
/*  925 */                 kind = 60;
/*  926 */               jjCheckNAdd(30); }
/*  927 */             break;
/*      */           case 31: 
/*  929 */             if (this.curChar == '?')
/*      */             {
/*  931 */               if (kind > 61)
/*  932 */                 kind = 61;
/*  933 */               jjCheckNAdd(32); }
/*  934 */             break;
/*      */           case 32: 
/*  936 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  938 */               if (kind > 61)
/*  939 */                 kind = 61;
/*  940 */               jjCheckNAdd(32); }
/*  941 */             break;
/*      */           case 33: 
/*  943 */             if (this.curChar == '!')
/*      */             {
/*  945 */               if (kind > 62)
/*  946 */                 kind = 62;
/*  947 */               jjCheckNAdd(34); }
/*  948 */             break;
/*      */           case 34: 
/*  950 */             if ((0x3FF001000000000 & l) != 0L)
/*      */             {
/*  952 */               if (kind > 62)
/*  953 */                 kind = 62;
/*  954 */               jjCheckNAdd(34); }
/*  955 */             break;
/*      */           case 35: 
/*  957 */             if (this.curChar == '#')
/*  958 */               this.jjstateSet[(this.jjnewStateCnt++)] = 36;
/*  959 */             break;
/*      */           case 37: 
/*  961 */             if ((0x3FF4C1800000000 & l) != 0L)
/*      */             {
/*  963 */               if (kind > 63)
/*  964 */                 kind = 63;
/*  965 */               jjCheckNAddTwoStates(37, 39); }
/*  966 */             break;
/*      */           case 40: 
/*      */           case 42: 
/*  969 */             if (this.curChar == '/')
/*  970 */               jjCheckNAddStates(0, 2);
/*  971 */             break;
/*      */           case 41: 
/*  973 */             if ((0xFFFF7FFFFFFFFFFF & l) != 0L)
/*  974 */               jjCheckNAddStates(0, 2);
/*  975 */             break;
/*      */           case 44: 
/*  977 */             if ((this.curChar == '/') && (kind > 64))
/*  978 */               kind = 64;
/*  979 */             break;
/*      */           case 45: 
/*  981 */             if ((0x3FF000000000000 & l) != 0L)
/*  982 */               jjCheckNAddStates(9, 15);
/*  983 */             break;
/*      */           case 46: 
/*  985 */             if ((0x3FF000000000000 & l) != 0L)
/*  986 */               jjCheckNAddTwoStates(46, 47);
/*  987 */             break;
/*      */           case 47: 
/*  989 */             if (this.curChar == '.')
/*      */             {
/*  991 */               if (kind > 10)
/*  992 */                 kind = 10;
/*  993 */               jjCheckNAddStates(45, 47); }
/*  994 */             break;
/*      */           case 48: 
/*  996 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/*  998 */               if (kind > 10)
/*  999 */                 kind = 10;
/* 1000 */               jjCheckNAddStates(45, 47); }
/* 1001 */             break;
/*      */           case 50: 
/* 1003 */             if ((0x280000000000 & l) != 0L)
/* 1004 */               jjCheckNAdd(51);
/* 1005 */             break;
/*      */           case 51: 
/* 1007 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1009 */               if (kind > 10)
/* 1010 */                 kind = 10;
/* 1011 */               jjCheckNAddTwoStates(51, 9); }
/* 1012 */             break;
/*      */           case 52: 
/* 1014 */             if ((0x3FF000000000000 & l) != 0L)
/* 1015 */               jjCheckNAddTwoStates(52, 53);
/* 1016 */             break;
/*      */           case 54: 
/* 1018 */             if ((0x280000000000 & l) != 0L)
/* 1019 */               jjCheckNAdd(55);
/* 1020 */             break;
/*      */           case 55: 
/* 1022 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1024 */               if (kind > 10)
/* 1025 */                 kind = 10;
/* 1026 */               jjCheckNAddTwoStates(55, 9); }
/* 1027 */             break;
/*      */           case 56: 
/* 1029 */             if ((0x3FF000000000000 & l) != 0L)
/* 1030 */               jjCheckNAddStates(48, 50);
/* 1031 */             break;
/*      */           case 58: 
/* 1033 */             if ((0x280000000000 & l) != 0L)
/* 1034 */               jjCheckNAdd(59);
/* 1035 */             break;
/*      */           case 59: 
/* 1037 */             if ((0x3FF000000000000 & l) != 0L)
/* 1038 */               jjCheckNAddTwoStates(59, 9);
/* 1039 */             break;
/*      */           case 60: 
/* 1041 */             if (this.curChar == '0')
/*      */             {
/* 1043 */               if (kind > 6)
/* 1044 */                 kind = 6;
/* 1045 */               jjCheckNAddStates(31, 33); }
/* 1046 */             break;
/*      */           case 62: 
/* 1048 */             if ((0x3FF000000000000 & l) != 0L)
/*      */             {
/* 1050 */               if (kind > 6)
/* 1051 */                 kind = 6;
/* 1052 */               jjCheckNAddTwoStates(62, 3); }
/* 1053 */             break;
/*      */           case 63: 
/* 1055 */             if ((0xFF000000000000 & l) != 0L)
/*      */             {
/* 1057 */               if (kind > 6)
/* 1058 */                 kind = 6;
/* 1059 */               jjCheckNAddTwoStates(63, 3); }
/* 1060 */             break;
/*      */           case 64: 
/* 1062 */             if (this.curChar == '/')
/* 1063 */               jjAddStates(16, 21);
/* 1064 */             break;
/*      */           case 66: 
/* 1066 */             if ((0xFFFFFFFFFFFFDBFF & l) != 0L)
/* 1067 */               jjCheckNAddStates(6, 8);
/* 1068 */             break;
/*      */           case 67: 
/* 1070 */             if (((0x2400 & l) != 0L) && (kind > 2))
/* 1071 */               kind = 2;
/* 1072 */             break;
/*      */           case 68: 
/* 1074 */             if ((this.curChar == '\n') && (kind > 2))
/* 1075 */               kind = 2;
/* 1076 */             break;
/*      */           case 69: 
/* 1078 */             if (this.curChar == '\r')
/* 1079 */               this.jjstateSet[(this.jjnewStateCnt++)] = 68;
/* 1080 */             break;
/*      */           case 70: 
/* 1082 */             if (this.curChar == '*')
/* 1083 */               jjCheckNAddTwoStates(71, 72);
/* 1084 */             break;
/*      */           case 71: 
/* 1086 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1087 */               jjCheckNAddTwoStates(71, 72);
/* 1088 */             break;
/*      */           case 72: 
/* 1090 */             if (this.curChar == '*')
/* 1091 */               jjCheckNAddStates(51, 53);
/* 1092 */             break;
/*      */           case 73: 
/* 1094 */             if ((0xFFFF7BFFFFFFFFFF & l) != 0L)
/* 1095 */               jjCheckNAddTwoStates(74, 72);
/* 1096 */             break;
/*      */           case 74: 
/* 1098 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1099 */               jjCheckNAddTwoStates(74, 72);
/* 1100 */             break;
/*      */           case 75: 
/* 1102 */             if ((this.curChar == '/') && (kind > 2))
/* 1103 */               kind = 2;
/* 1104 */             break;
/*      */           case 76: 
/* 1106 */             if (this.curChar == '*')
/* 1107 */               this.jjstateSet[(this.jjnewStateCnt++)] = 70;
/* 1108 */             break;
/*      */           case 77: 
/* 1110 */             if (this.curChar == '*')
/* 1111 */               jjCheckNAddTwoStates(78, 79);
/* 1112 */             break;
/*      */           case 78: 
/* 1114 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1115 */               jjCheckNAddTwoStates(78, 79);
/* 1116 */             break;
/*      */           case 79: 
/* 1118 */             if (this.curChar == '*')
/* 1119 */               jjCheckNAddStates(54, 56);
/* 1120 */             break;
/*      */           case 80: 
/* 1122 */             if ((0xFFFF7BFFFFFFFFFF & l) != 0L)
/* 1123 */               jjCheckNAddTwoStates(81, 79);
/* 1124 */             break;
/*      */           case 81: 
/* 1126 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1127 */               jjCheckNAddTwoStates(81, 79);
/* 1128 */             break;
/*      */           case 82: 
/* 1130 */             if (this.curChar == '/')
/* 1131 */               jjCheckNAddStates(3, 5);
/* 1132 */             break;
/*      */           case 83: 
/* 1134 */             if ((0xFFFFFFFFFFFFDBFF & l) != 0L)
/* 1135 */               jjCheckNAddStates(3, 5);
/* 1136 */             break;
/*      */           case 84: 
/* 1138 */             if (((0x2400 & l) != 0L) && (kind > 3))
/* 1139 */               kind = 3;
/* 1140 */             break;
/*      */           case 85: 
/* 1142 */             if ((this.curChar == '\n') && (kind > 3))
/* 1143 */               kind = 3;
/* 1144 */             break;
/*      */           case 86: 
/* 1146 */             if (this.curChar == '\r')
/* 1147 */               this.jjstateSet[(this.jjnewStateCnt++)] = 85;
/* 1148 */             break;
/*      */           case 87: 
/* 1150 */             if (this.curChar == '*')
/* 1151 */               jjCheckNAddTwoStates(88, 89);
/* 1152 */             break;
/*      */           case 88: 
/* 1154 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1155 */               jjCheckNAddTwoStates(88, 89);
/* 1156 */             break;
/*      */           case 89: 
/* 1158 */             if (this.curChar == '*')
/* 1159 */               jjCheckNAddStates(57, 59);
/* 1160 */             break;
/*      */           case 90: 
/* 1162 */             if ((0xFFFF7BFFFFFFFFFF & l) != 0L)
/* 1163 */               jjCheckNAddTwoStates(91, 89);
/* 1164 */             break;
/*      */           case 91: 
/* 1166 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1167 */               jjCheckNAddTwoStates(91, 89);
/* 1168 */             break;
/*      */           case 92: 
/* 1170 */             if ((this.curChar == '/') && (kind > 4))
/* 1171 */               kind = 4;
/* 1172 */             break;
/*      */           case 93: 
/* 1174 */             if (this.curChar == '*')
/* 1175 */               this.jjstateSet[(this.jjnewStateCnt++)] = 87;
/* 1176 */             break;
/*      */           case 94: 
/* 1178 */             if (this.curChar == '*')
/* 1179 */               jjCheckNAddTwoStates(95, 96);
/* 1180 */             break;
/*      */           case 95: 
/* 1182 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1183 */               jjCheckNAddTwoStates(95, 96);
/* 1184 */             break;
/*      */           case 96: 
/* 1186 */             if (this.curChar == '*')
/* 1187 */               jjCheckNAddStates(60, 62);
/* 1188 */             break;
/*      */           case 97: 
/* 1190 */             if ((0xFFFF7BFFFFFFFFFF & l) != 0L)
/* 1191 */               jjCheckNAddTwoStates(98, 96);
/* 1192 */             break;
/*      */           case 98: 
/* 1194 */             if ((0xFFFFFBFFFFFFFFFF & l) != 0L)
/* 1195 */               jjCheckNAddTwoStates(98, 96);
/* 1196 */             break;
/*      */           case 99: 
/* 1198 */             if ((this.curChar == '/') && (kind > 5))
/* 1199 */               kind = 5;
/* 1200 */             break;
/*      */           case 100: 
/* 1202 */             if (this.curChar == '-') {
/* 1203 */               jjAddStates(28, 30);
/*      */             }
/*      */             break;
/*      */           }
/* 1207 */         } while (i != startsAt);
/*      */       }
/* 1209 */       else if (this.curChar < '')
/*      */       {
/* 1211 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1214 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 36: 
/* 1217 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1219 */               if (kind > 63)
/* 1220 */                 kind = 63;
/* 1221 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/* 1223 */             else if ((0x1000000040000000 & l) != 0L)
/*      */             {
/* 1225 */               if (kind > 58)
/* 1226 */                 kind = 58;
/* 1227 */               jjCheckNAdd(28);
/*      */             }
/* 1229 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1231 */               if (kind > 60)
/* 1232 */                 kind = 60;
/* 1233 */               jjCheckNAdd(30);
/*      */             }
/* 1235 */             break;
/*      */           case 101: 
/* 1237 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1239 */               if (kind > 63)
/* 1240 */                 kind = 63;
/* 1241 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/* 1243 */             else if (this.curChar == '[') {
/* 1244 */               this.jjstateSet[(this.jjnewStateCnt++)] = 38; }
/* 1245 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1247 */               if (kind > 60)
/* 1248 */                 kind = 60;
/* 1249 */               jjCheckNAdd(30);
/*      */             }
/* 1251 */             break;
/*      */           case 65: 
/* 1253 */             jjCheckNAddStates(0, 2);
/* 1254 */             if (this.curChar == '\\')
/* 1255 */               this.jjstateSet[(this.jjnewStateCnt++)] = 42;
/* 1256 */             break;
/*      */           case 0: 
/* 1258 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1260 */               if (kind > 60)
/* 1261 */                 kind = 60;
/* 1262 */               jjCheckNAdd(30);
/*      */             }
/* 1264 */             else if ((0x1000000040000000 & l) != 0L)
/*      */             {
/* 1266 */               if (kind > 58)
/* 1267 */                 kind = 58;
/* 1268 */               jjCheckNAdd(28);
/*      */             }
/* 1270 */             break;
/*      */           case 3: 
/* 1272 */             if (((0x100000001000 & l) != 0L) && (kind > 6))
/* 1273 */               kind = 6;
/* 1274 */             break;
/*      */           case 6: 
/* 1276 */             if ((0x2000000020 & l) != 0L)
/* 1277 */               jjAddStates(63, 64);
/* 1278 */             break;
/*      */           case 9: 
/* 1280 */             if (((0x5000000050 & l) != 0L) && (kind > 10))
/* 1281 */               kind = 10;
/* 1282 */             break;
/*      */           case 11: 
/* 1284 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/* 1285 */               jjCheckNAddStates(25, 27);
/* 1286 */             break;
/*      */           case 12: 
/* 1288 */             if (this.curChar == '\\')
/* 1289 */               jjAddStates(65, 67);
/* 1290 */             break;
/*      */           case 13: 
/* 1292 */             if ((0x14404410000000 & l) != 0L)
/* 1293 */               jjCheckNAddStates(25, 27);
/* 1294 */             break;
/*      */           case 20: 
/* 1296 */             if ((0xFFFFFFFFEFFFFFFF & l) != 0L)
/* 1297 */               jjCheckNAddStates(22, 24);
/* 1298 */             break;
/*      */           case 21: 
/* 1300 */             if (this.curChar == '\\')
/* 1301 */               jjAddStates(68, 70);
/* 1302 */             break;
/*      */           case 22: 
/* 1304 */             if ((0x14404410000000 & l) != 0L)
/* 1305 */               jjCheckNAddStates(22, 24);
/* 1306 */             break;
/*      */           case 28: 
/* 1308 */             if ((0x1000000040000000 & l) != 0L)
/*      */             {
/* 1310 */               if (kind > 58)
/* 1311 */                 kind = 58;
/* 1312 */               jjCheckNAdd(28); }
/* 1313 */             break;
/*      */           case 29: 
/* 1315 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1317 */               if (kind > 60)
/* 1318 */                 kind = 60;
/* 1319 */               jjCheckNAdd(30); }
/* 1320 */             break;
/*      */           case 30: 
/* 1322 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1324 */               if (kind > 60)
/* 1325 */                 kind = 60;
/* 1326 */               jjCheckNAdd(30); }
/* 1327 */             break;
/*      */           case 32: 
/* 1329 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1331 */               if (kind > 61)
/* 1332 */                 kind = 61;
/* 1333 */               this.jjstateSet[(this.jjnewStateCnt++)] = 32; }
/* 1334 */             break;
/*      */           case 34: 
/* 1336 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1338 */               if (kind > 62)
/* 1339 */                 kind = 62;
/* 1340 */               this.jjstateSet[(this.jjnewStateCnt++)] = 34; }
/* 1341 */             break;
/*      */           case 37: 
/* 1343 */             if ((0x7FFFFFE87FFFFFE & l) != 0L)
/*      */             {
/* 1345 */               if (kind > 63)
/* 1346 */                 kind = 63;
/* 1347 */               jjCheckNAddTwoStates(37, 39); }
/* 1348 */             break;
/*      */           case 38: 
/* 1350 */             if ((this.curChar == ']') && (kind > 63))
/* 1351 */               kind = 63;
/* 1352 */             break;
/*      */           case 39: 
/* 1354 */             if (this.curChar == '[')
/* 1355 */               this.jjstateSet[(this.jjnewStateCnt++)] = 38;
/* 1356 */             break;
/*      */           case 41: 
/* 1358 */             jjCheckNAddStates(0, 2);
/* 1359 */             break;
/*      */           case 43: 
/* 1361 */             if (this.curChar == '\\')
/* 1362 */               this.jjstateSet[(this.jjnewStateCnt++)] = 42;
/* 1363 */             break;
/*      */           case 49: 
/* 1365 */             if ((0x2000000020 & l) != 0L)
/* 1366 */               jjAddStates(71, 72);
/* 1367 */             break;
/*      */           case 53: 
/* 1369 */             if ((0x2000000020 & l) != 0L)
/* 1370 */               jjAddStates(73, 74);
/* 1371 */             break;
/*      */           case 57: 
/* 1373 */             if ((0x2000000020 & l) != 0L)
/* 1374 */               jjAddStates(75, 76);
/* 1375 */             break;
/*      */           case 61: 
/* 1377 */             if ((0x100000001000000 & l) != 0L)
/* 1378 */               jjCheckNAdd(62);
/* 1379 */             break;
/*      */           case 62: 
/* 1381 */             if ((0x7E0000007E & l) != 0L)
/*      */             {
/* 1383 */               if (kind > 6)
/* 1384 */                 kind = 6;
/* 1385 */               jjCheckNAddTwoStates(62, 3); }
/* 1386 */             break;
/*      */           case 66: 
/* 1388 */             jjAddStates(6, 8);
/* 1389 */             break;
/*      */           case 71: 
/* 1391 */             jjCheckNAddTwoStates(71, 72);
/* 1392 */             break;
/*      */           case 73: 
/*      */           case 74: 
/* 1395 */             jjCheckNAddTwoStates(74, 72);
/* 1396 */             break;
/*      */           case 78: 
/* 1398 */             jjCheckNAddTwoStates(78, 79);
/* 1399 */             break;
/*      */           case 80: 
/*      */           case 81: 
/* 1402 */             jjCheckNAddTwoStates(81, 79);
/* 1403 */             break;
/*      */           case 83: 
/* 1405 */             jjAddStates(3, 5);
/* 1406 */             break;
/*      */           case 88: 
/* 1408 */             jjCheckNAddTwoStates(88, 89);
/* 1409 */             break;
/*      */           case 90: 
/*      */           case 91: 
/* 1412 */             jjCheckNAddTwoStates(91, 89);
/* 1413 */             break;
/*      */           case 95: 
/* 1415 */             jjCheckNAddTwoStates(95, 96);
/* 1416 */             break;
/*      */           case 97: 
/*      */           case 98: 
/* 1419 */             jjCheckNAddTwoStates(98, 96);
/*      */           
/*      */           }
/*      */           
/* 1423 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/* 1427 */         int hiByte = this.curChar >> '\b';
/* 1428 */         int i1 = hiByte >> 6;
/* 1429 */         long l1 = 1L << (hiByte & 0x3F);
/* 1430 */         int i2 = (this.curChar & 0xFF) >> '\006';
/* 1431 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1434 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 36: 
/* 1437 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1439 */               if (kind > 60)
/* 1440 */                 kind = 60;
/* 1441 */               jjCheckNAdd(30);
/*      */             }
/* 1443 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1445 */               if (kind > 63)
/* 1446 */                 kind = 63;
/* 1447 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/* 1449 */             break;
/*      */           case 101: 
/* 1451 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1453 */               if (kind > 60)
/* 1454 */                 kind = 60;
/* 1455 */               jjCheckNAdd(30);
/*      */             }
/* 1457 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1459 */               if (kind > 63)
/* 1460 */                 kind = 63;
/* 1461 */               jjCheckNAddTwoStates(37, 39);
/*      */             }
/* 1463 */             break;
/*      */           case 41: 
/*      */           case 65: 
/* 1466 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1467 */               jjCheckNAddStates(0, 2);
/* 1468 */             break;
/*      */           case 0: 
/* 1470 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1472 */               if (kind > 60)
/* 1473 */                 kind = 60;
/* 1474 */               jjCheckNAdd(30); }
/* 1475 */             break;
/*      */           case 11: 
/* 1477 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1478 */               jjAddStates(25, 27);
/* 1479 */             break;
/*      */           case 20: 
/* 1481 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1482 */               jjAddStates(22, 24);
/* 1483 */             break;
/*      */           case 30: 
/* 1485 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1487 */               if (kind > 60)
/* 1488 */                 kind = 60;
/* 1489 */               jjCheckNAdd(30); }
/* 1490 */             break;
/*      */           case 32: 
/* 1492 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1494 */               if (kind > 61)
/* 1495 */                 kind = 61;
/* 1496 */               this.jjstateSet[(this.jjnewStateCnt++)] = 32; }
/* 1497 */             break;
/*      */           case 34: 
/* 1499 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1501 */               if (kind > 62)
/* 1502 */                 kind = 62;
/* 1503 */               this.jjstateSet[(this.jjnewStateCnt++)] = 34; }
/* 1504 */             break;
/*      */           case 37: 
/* 1506 */             if (jjCanMove_1(hiByte, i1, i2, l1, l2))
/*      */             {
/* 1508 */               if (kind > 63)
/* 1509 */                 kind = 63;
/* 1510 */               jjCheckNAddTwoStates(37, 39); }
/* 1511 */             break;
/*      */           case 66: 
/* 1513 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1514 */               jjAddStates(6, 8);
/* 1515 */             break;
/*      */           case 71: 
/* 1517 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1518 */               jjCheckNAddTwoStates(71, 72);
/* 1519 */             break;
/*      */           case 73: 
/*      */           case 74: 
/* 1522 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1523 */               jjCheckNAddTwoStates(74, 72);
/* 1524 */             break;
/*      */           case 78: 
/* 1526 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1527 */               jjCheckNAddTwoStates(78, 79);
/* 1528 */             break;
/*      */           case 80: 
/*      */           case 81: 
/* 1531 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1532 */               jjCheckNAddTwoStates(81, 79);
/* 1533 */             break;
/*      */           case 83: 
/* 1535 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1536 */               jjAddStates(3, 5);
/* 1537 */             break;
/*      */           case 88: 
/* 1539 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1540 */               jjCheckNAddTwoStates(88, 89);
/* 1541 */             break;
/*      */           case 90: 
/*      */           case 91: 
/* 1544 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1545 */               jjCheckNAddTwoStates(91, 89);
/* 1546 */             break;
/*      */           case 95: 
/* 1548 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/* 1549 */               jjCheckNAddTwoStates(95, 96);
/* 1550 */             break;
/*      */           case 97: 
/*      */           case 98: 
/* 1553 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
/* 1554 */               jjCheckNAddTwoStates(98, 96);
/*      */             }
/*      */             break;
/*      */           }
/* 1558 */         } while (i != startsAt);
/*      */       }
/* 1560 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/* 1562 */         this.jjmatchedKind = kind;
/* 1563 */         this.jjmatchedPos = curPos;
/* 1564 */         kind = Integer.MAX_VALUE;
/*      */       }
/* 1566 */       curPos++;
/* 1567 */       if ((i = this.jjnewStateCnt) == (startsAt = 101 - (this.jjnewStateCnt = startsAt)))
/* 1568 */         return curPos;
/* 1569 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {} }
/* 1570 */     return curPos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int jjStopStringLiteralDfa_1(int pos, long active0, long active1)
/*      */   {
/* 1578 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_1(int pos, long active0, long active1)
/*      */   {
/* 1583 */     return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0, active1), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_1(int pos, int kind, int state) {
/* 1587 */     this.jjmatchedKind = kind;
/* 1588 */     this.jjmatchedPos = pos;
/* 1589 */     try { this.curChar = this.input_stream.readChar();
/* 1590 */     } catch (IOException localIOException) { return pos + 1; }
/* 1591 */     return jjMoveNfa_1(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_1() {
/* 1595 */     switch (this.curChar)
/*      */     {
/*      */     case '{': 
/* 1598 */       return jjStopAtPos(0, 71);
/*      */     case '}': 
/* 1600 */       return jjStopAtPos(0, 73);
/*      */     }
/* 1602 */     return jjMoveNfa_1(0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   private final int jjMoveNfa_1(int startState, int curPos)
/*      */   {
/* 1608 */     int startsAt = 0;
/* 1609 */     this.jjnewStateCnt = 1;
/* 1610 */     int i = 1;
/* 1611 */     this.jjstateSet[0] = startState;
/* 1612 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/* 1615 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1616 */         ReInitRounds();
/* 1617 */       if (this.curChar < '@')
/*      */       {
/*      */ 
/*      */         do
/*      */         {
/* 1622 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1625 */             kind = 70;
/*      */           
/*      */           }
/*      */           
/* 1629 */         } while (i != startsAt);
/*      */       }
/* 1631 */       else if (this.curChar < '')
/*      */       {
/* 1633 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1636 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1639 */             if ((0xD7FFFFFFFFFFFFFF & l) != 0L) {
/* 1640 */               kind = 70;
/*      */             }
/*      */             break;
/*      */           }
/* 1644 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/* 1648 */         int hiByte = this.curChar >> '\b';
/* 1649 */         int i1 = hiByte >> 6;
/* 1650 */         long l1 = 1L << (hiByte & 0x3F);
/* 1651 */         int i2 = (this.curChar & 0xFF) >> '\006';
/* 1652 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1655 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1658 */             if ((jjCanMove_0(hiByte, i1, i2, l1, l2)) && (kind > 70)) {
/* 1659 */               kind = 70;
/*      */             }
/*      */             break;
/*      */           }
/* 1663 */         } while (i != startsAt);
/*      */       }
/* 1665 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/* 1667 */         this.jjmatchedKind = kind;
/* 1668 */         this.jjmatchedPos = curPos;
/* 1669 */         kind = Integer.MAX_VALUE;
/*      */       }
/* 1671 */       curPos++;
/* 1672 */       if ((i = this.jjnewStateCnt) == (startsAt = 1 - (this.jjnewStateCnt = startsAt)))
/* 1673 */         return curPos;
/* 1674 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {} }
/* 1675 */     return curPos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private final int jjStopStringLiteralDfa_2(int pos, long active0, long active1)
/*      */   {
/* 1683 */     return -1;
/*      */   }
/*      */   
/*      */   private final int jjStartNfa_2(int pos, long active0, long active1)
/*      */   {
/* 1688 */     return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0, active1), pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
/* 1692 */     this.jjmatchedKind = kind;
/* 1693 */     this.jjmatchedPos = pos;
/* 1694 */     try { this.curChar = this.input_stream.readChar();
/* 1695 */     } catch (IOException localIOException) { return pos + 1; }
/* 1696 */     return jjMoveNfa_2(state, pos + 1);
/*      */   }
/*      */   
/*      */   private final int jjMoveStringLiteralDfa0_2() {
/* 1700 */     switch (this.curChar)
/*      */     {
/*      */     case '{': 
/* 1703 */       return jjStopAtPos(0, 71);
/*      */     case '}': 
/* 1705 */       return jjStopAtPos(0, 72);
/*      */     }
/* 1707 */     return jjMoveNfa_2(0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   private final int jjMoveNfa_2(int startState, int curPos)
/*      */   {
/* 1713 */     int startsAt = 0;
/* 1714 */     this.jjnewStateCnt = 1;
/* 1715 */     int i = 1;
/* 1716 */     this.jjstateSet[0] = startState;
/* 1717 */     int kind = Integer.MAX_VALUE;
/*      */     for (;;)
/*      */     {
/* 1720 */       if (++this.jjround == Integer.MAX_VALUE)
/* 1721 */         ReInitRounds();
/* 1722 */       if (this.curChar < '@')
/*      */       {
/*      */ 
/*      */         do
/*      */         {
/* 1727 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1730 */             kind = 70;
/*      */           
/*      */           }
/*      */           
/* 1734 */         } while (i != startsAt);
/*      */       }
/* 1736 */       else if (this.curChar < '')
/*      */       {
/* 1738 */         long l = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1741 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1744 */             if ((0xD7FFFFFFFFFFFFFF & l) != 0L) {
/* 1745 */               kind = 70;
/*      */             }
/*      */             break;
/*      */           }
/* 1749 */         } while (i != startsAt);
/*      */       }
/*      */       else
/*      */       {
/* 1753 */         int hiByte = this.curChar >> '\b';
/* 1754 */         int i1 = hiByte >> 6;
/* 1755 */         long l1 = 1L << (hiByte & 0x3F);
/* 1756 */         int i2 = (this.curChar & 0xFF) >> '\006';
/* 1757 */         long l2 = 1L << (this.curChar & 0x3F);
/*      */         do
/*      */         {
/* 1760 */           switch (this.jjstateSet[(--i)])
/*      */           {
/*      */           case 0: 
/* 1763 */             if ((jjCanMove_0(hiByte, i1, i2, l1, l2)) && (kind > 70)) {
/* 1764 */               kind = 70;
/*      */             }
/*      */             break;
/*      */           }
/* 1768 */         } while (i != startsAt);
/*      */       }
/* 1770 */       if (kind != Integer.MAX_VALUE)
/*      */       {
/* 1772 */         this.jjmatchedKind = kind;
/* 1773 */         this.jjmatchedPos = curPos;
/* 1774 */         kind = Integer.MAX_VALUE;
/*      */       }
/* 1776 */       curPos++;
/* 1777 */       if ((i = this.jjnewStateCnt) == (startsAt = 1 - (this.jjnewStateCnt = startsAt)))
/* 1778 */         return curPos;
/* 1779 */       try { this.curChar = this.input_stream.readChar(); } catch (IOException localIOException) {} }
/* 1780 */     return curPos;
/*      */   }
/*      */   
/* 1783 */   static final int[] jjnextStates = {
/* 1784 */     41, 43, 44, 83, 84, 86, 66, 67, 69, 46, 47, 52, 53, 56, 57, 9, 
/* 1785 */     65, 76, 77, 82, 93, 94, 20, 21, 23, 11, 12, 14, 1, 4, 45, 61, 
/* 1786 */     63, 3, 5, 6, 9, 11, 12, 16, 14, 20, 21, 25, 23, 48, 49, 9, 
/* 1787 */     56, 57, 9, 72, 73, 75, 79, 80, 75, 89, 90, 92, 96, 97, 99, 7, 
/* 1788 */     8, 13, 15, 17, 22, 24, 26, 50, 51, 54, 55, 58, 59 };
/*      */   
/*      */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
/*      */   {
/* 1792 */     switch (hiByte)
/*      */     {
/*      */     case 0: 
/* 1795 */       return (jjbitVec2[i2] & l2) != 0L;
/*      */     }
/* 1797 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 1798 */       return true;
/* 1799 */     return false;
/*      */   }
/*      */   
/*      */   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
/*      */   {
/* 1804 */     switch (hiByte)
/*      */     {
/*      */     case 0: 
/* 1807 */       return (jjbitVec4[i2] & l2) != 0L;
/*      */     case 48: 
/* 1809 */       return (jjbitVec5[i2] & l2) != 0L;
/*      */     case 49: 
/* 1811 */       return (jjbitVec6[i2] & l2) != 0L;
/*      */     case 51: 
/* 1813 */       return (jjbitVec7[i2] & l2) != 0L;
/*      */     case 61: 
/* 1815 */       return (jjbitVec8[i2] & l2) != 0L;
/*      */     }
/* 1817 */     if ((jjbitVec3[i1] & l1) != 0L)
/* 1818 */       return true;
/* 1819 */     return false;
/*      */   }
/*      */   
/* 1822 */   public static final String[] jjstrLiteralImages = {
/* 1823 */     "", 
/* 1824 */     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "#include", "#library", "#generate", 
/* 1825 */     "NOT", "EXISTS", "NODUP", 
/* 1826 */     "FINDALL", "COUNTALL", "UNIQUE", 
/* 1827 */     "TEST", "MODES", "IS", "REALLY", 
/* 1828 */     "TYPE", "AS", "END", "DET", "SEMIDET", 
/* 1829 */     "MULTI", "NONDET", "BOUND", 
/* 1830 */     "DEFAULT", "FACT", "OF", "PERSISTENT", 
/* 1831 */     "||", "&&", "::", "(", ")", "[", "]", "<", ">", ";", ",", 
/* 1832 */     ".", "#", "@", "|", "/", ":-", "+", "*", "=", 
/* 1833 */     0000000000000000":" };
/* 1834 */   public static final String[] lexStateNames = {
/* 1835 */     "DEFAULT", 
/* 1836 */     "inBraces", 
/* 1837 */     "inNestedBraces" };
/*      */   
/* 1839 */   public static final int[] jjnewLexState = {
/* 1840 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/* 1841 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/* 1842 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 2, -1, 0-1 };
/*      */   
/* 1844 */   static final long[] jjtoToken = {
/* 1845 */     -576460752303426495L, 1537L };
/*      */   
/* 1847 */   static final long[] jjtoSkip = {
/* 1848 */     62L };
/*      */   
/* 1850 */   static final long[] jjtoMore = {
/* 1851 */     0480L };
/*      */   
/*      */   protected JavaCharStream input_stream;
/* 1854 */   private final int[] jjrounds = new int[101];
/* 1855 */   private final int[] jjstateSet = new int['Ê'];
/*      */   
/*      */   StringBuffer image;
/*      */   int jjimageLen;
/*      */   int lengthOfMatch;
/*      */   protected char curChar;
/*      */   
/*      */   public TyRuBaParserTokenManager(JavaCharStream stream)
/*      */   {
/* 1864 */     this.input_stream = stream;
/*      */   }
/*      */   
/*      */   public TyRuBaParserTokenManager(JavaCharStream stream, int lexState) {
/* 1868 */     this(stream);
/* 1869 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream) {
/* 1873 */     this.jjmatchedPos = (this.jjnewStateCnt = 0);
/* 1874 */     this.curLexState = this.defaultLexState;
/* 1875 */     this.input_stream = stream;
/* 1876 */     ReInitRounds();
/*      */   }
/*      */   
/*      */   private final void ReInitRounds()
/*      */   {
/* 1881 */     this.jjround = -2147483647;
/* 1882 */     for (int i = 101; i-- > 0;)
/* 1883 */       this.jjrounds[i] = Integer.MIN_VALUE;
/*      */   }
/*      */   
/*      */   public void ReInit(JavaCharStream stream, int lexState) {
/* 1887 */     ReInit(stream);
/* 1888 */     SwitchTo(lexState);
/*      */   }
/*      */   
/*      */   public void SwitchTo(int lexState) {
/* 1892 */     if ((lexState >= 3) || (lexState < 0)) {
/* 1893 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*      */     }
/* 1895 */     this.curLexState = lexState;
/*      */   }
/*      */   
/*      */   protected Token jjFillToken()
/*      */   {
/* 1900 */     Token t = Token.newToken(this.jjmatchedKind);
/* 1901 */     t.kind = this.jjmatchedKind;
/* 1902 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 1903 */     t.image = (im == null ? this.input_stream.GetImage() : im);
/* 1904 */     t.beginLine = this.input_stream.getBeginLine();
/* 1905 */     t.beginColumn = this.input_stream.getBeginColumn();
/* 1906 */     t.endLine = this.input_stream.getEndLine();
/* 1907 */     t.endColumn = this.input_stream.getEndColumn();
/* 1908 */     return t;
/*      */   }
/*      */   
/* 1911 */   int curLexState = 0;
/* 1912 */   int defaultLexState = 0;
/*      */   
/*      */   int jjnewStateCnt;
/*      */   
/*      */   int jjround;
/*      */   
/*      */   int jjmatchedPos;
/*      */   int jjmatchedKind;
/*      */   
/*      */   public Token getNextToken()
/*      */   {
/* 1923 */     int curPos = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1930 */       this.curChar = this.input_stream.BeginToken();
/*      */     }
/*      */     catch (IOException localIOException1)
/*      */     {
/* 1934 */       this.jjmatchedKind = 0;
/* 1935 */       return jjFillToken();
/*      */     }
/*      */     
/* 1938 */     this.image = null;
/* 1939 */     this.jjimageLen = 0;
/*      */     
/*      */     for (;;)
/*      */     {
/* 1943 */       switch (this.curLexState)
/*      */       {
/*      */       case 0: 
/* 1946 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 1947 */         this.jjmatchedPos = 0;
/* 1948 */         curPos = jjMoveStringLiteralDfa0_0();
/* 1949 */         break;
/*      */       case 1: 
/* 1951 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 1952 */         this.jjmatchedPos = 0;
/* 1953 */         curPos = jjMoveStringLiteralDfa0_1();
/* 1954 */         break;
/*      */       case 2: 
/* 1956 */         this.jjmatchedKind = Integer.MAX_VALUE;
/* 1957 */         this.jjmatchedPos = 0;
/* 1958 */         curPos = jjMoveStringLiteralDfa0_2();
/*      */       }
/*      */       
/* 1961 */       if (this.jjmatchedKind != Integer.MAX_VALUE)
/*      */       {
/* 1963 */         if (this.jjmatchedPos + 1 < curPos)
/* 1964 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1);
/* 1965 */         if ((jjtoToken[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
/*      */         {
/* 1967 */           Token matchedToken = jjFillToken();
/* 1968 */           if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1969 */             this.curLexState = jjnewLexState[this.jjmatchedKind];
/* 1970 */           return matchedToken;
/*      */         }
/* 1972 */         if ((jjtoSkip[(this.jjmatchedKind >> 6)] & 1L << (this.jjmatchedKind & 0x3F)) != 0L)
/*      */         {
/* 1974 */           if (jjnewLexState[this.jjmatchedKind] == -1) break;
/* 1975 */           this.curLexState = jjnewLexState[this.jjmatchedKind];
/* 1976 */           break;
/*      */         }
/* 1978 */         MoreLexicalActions();
/* 1979 */         if (jjnewLexState[this.jjmatchedKind] != -1)
/* 1980 */           this.curLexState = jjnewLexState[this.jjmatchedKind];
/* 1981 */         curPos = 0;
/* 1982 */         this.jjmatchedKind = Integer.MAX_VALUE;
/*      */         try {
/* 1984 */           this.curChar = this.input_stream.readChar();
/*      */         }
/*      */         catch (IOException localIOException2) {}
/*      */       }
/*      */     }
/* 1989 */     int error_line = this.input_stream.getEndLine();
/* 1990 */     int error_column = this.input_stream.getEndColumn();
/* 1991 */     String error_after = null;
/* 1992 */     boolean EOFSeen = false;
/* 1993 */     try { this.input_stream.readChar();this.input_stream.backup(1);
/*      */     } catch (IOException localIOException3) {
/* 1995 */       EOFSeen = true;
/* 1996 */       error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
/* 1997 */       if ((this.curChar == '\n') || (this.curChar == '\r')) {
/* 1998 */         error_line++;
/* 1999 */         error_column = 0;
/*      */       }
/*      */       else {
/* 2002 */         error_column++;
/*      */       } }
/* 2004 */     if (!EOFSeen) {
/* 2005 */       this.input_stream.backup(1);
/* 2006 */       error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
/*      */     }
/* 2008 */     throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void MoreLexicalActions()
/*      */   {
/* 2015 */     this.jjimageLen += (this.lengthOfMatch = this.jjmatchedPos + 1);
/* 2016 */     switch (this.jjmatchedKind)
/*      */     {
/*      */     case 71: 
/* 2019 */       if (this.image == null) {
/* 2020 */         this.image = new StringBuffer(new String(this.input_stream.GetSuffix(this.jjimageLen)));
/*      */       } else
/* 2022 */         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
/* 2023 */       this.jjimageLen = 0;
/* 2024 */       this.nestedBraces += 1;
/* 2025 */       break;
/*      */     case 72: 
/* 2027 */       if (this.image == null) {
/* 2028 */         this.image = new StringBuffer(new String(this.input_stream.GetSuffix(this.jjimageLen)));
/*      */       } else
/* 2030 */         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
/* 2031 */       this.jjimageLen = 0;
/* 2032 */       this.nestedBraces -= 1;
/* 2033 */       if (this.nestedBraces == 0) SwitchTo(1);
/* 2034 */       break;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/parser/TyRuBaParserTokenManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */