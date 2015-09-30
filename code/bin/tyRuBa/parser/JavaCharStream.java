/*     */ package tyRuBa.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public class JavaCharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   
/*     */   static final int hexval(char c) throws IOException
/*     */   {
/*  13 */     switch (c)
/*     */     {
/*     */     case '0': 
/*  16 */       return 0;
/*     */     case '1': 
/*  18 */       return 1;
/*     */     case '2': 
/*  20 */       return 2;
/*     */     case '3': 
/*  22 */       return 3;
/*     */     case '4': 
/*  24 */       return 4;
/*     */     case '5': 
/*  26 */       return 5;
/*     */     case '6': 
/*  28 */       return 6;
/*     */     case '7': 
/*  30 */       return 7;
/*     */     case '8': 
/*  32 */       return 8;
/*     */     case '9': 
/*  34 */       return 9;
/*     */     
/*     */     case 'A': 
/*     */     case 'a': 
/*  38 */       return 10;
/*     */     case 'B': 
/*     */     case 'b': 
/*  41 */       return 11;
/*     */     case 'C': 
/*     */     case 'c': 
/*  44 */       return 12;
/*     */     case 'D': 
/*     */     case 'd': 
/*  47 */       return 13;
/*     */     case 'E': 
/*     */     case 'e': 
/*  50 */       return 14;
/*     */     case 'F': 
/*     */     case 'f': 
/*  53 */       return 15;
/*     */     }
/*     */     
/*  56 */     throw new IOException();
/*     */   }
/*     */   
/*  59 */   public int bufpos = -1;
/*     */   
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*     */   protected int[] bufline;
/*     */   protected int[] bufcolumn;
/*  66 */   protected int column = 0;
/*  67 */   protected int line = 1;
/*     */   
/*  69 */   protected boolean prevCharIsCR = false;
/*  70 */   protected boolean prevCharIsLF = false;
/*     */   
/*     */   protected Reader inputStream;
/*     */   
/*     */   protected char[] nextCharBuf;
/*     */   protected char[] buffer;
/*  76 */   protected int maxNextCharInd = 0;
/*  77 */   protected int nextCharInd = -1;
/*  78 */   protected int inBuf = 0;
/*     */   
/*     */   protected void ExpandBuff(boolean wrapAround)
/*     */   {
/*  82 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  83 */     int[] newbufline = new int[this.bufsize + 2048];
/*  84 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */     
/*     */     try
/*     */     {
/*  88 */       if (wrapAround)
/*     */       {
/*  90 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  91 */         System.arraycopy(this.buffer, 0, newbuffer, 
/*  92 */           this.bufsize - this.tokenBegin, this.bufpos);
/*  93 */         this.buffer = newbuffer;
/*     */         
/*  95 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  96 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/*  97 */         this.bufline = newbufline;
/*     */         
/*  99 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 100 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/* 101 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 103 */         this.bufpos += this.bufsize - this.tokenBegin;
/*     */       }
/*     */       else
/*     */       {
/* 107 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 108 */         this.buffer = newbuffer;
/*     */         
/* 110 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 111 */         this.bufline = newbufline;
/*     */         
/* 113 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 114 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 116 */         this.bufpos -= this.tokenBegin;
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 121 */       throw new Error(t.getMessage());
/*     */     }
/*     */     
/* 124 */     this.available = (this.bufsize += 2048);
/* 125 */     this.tokenBegin = 0;
/*     */   }
/*     */   
/*     */   protected void FillBuff()
/*     */     throws IOException
/*     */   {
/* 131 */     if (this.maxNextCharInd == 4096)
/* 132 */       this.maxNextCharInd = (this.nextCharInd = 0);
/*     */     try {
/*     */       int i;
/* 135 */       if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 
/* 136 */         4096 - this.maxNextCharInd)) == -1)
/*     */       {
/* 138 */         this.inputStream.close();
/* 139 */         throw new IOException();
/*     */       }
/*     */       
/* 142 */       this.maxNextCharInd += i;
/* 143 */       return;
/*     */     }
/*     */     catch (IOException e) {
/* 146 */       if (this.bufpos != 0)
/*     */       {
/* 148 */         this.bufpos -= 1;
/* 149 */         backup(0);
/*     */       }
/*     */       else
/*     */       {
/* 153 */         this.bufline[this.bufpos] = this.line;
/* 154 */         this.bufcolumn[this.bufpos] = this.column;
/*     */       }
/* 156 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   protected char ReadByte() throws IOException
/*     */   {
/* 162 */     if (++this.nextCharInd >= this.maxNextCharInd) {
/* 163 */       FillBuff();
/*     */     }
/* 165 */     return this.nextCharBuf[this.nextCharInd];
/*     */   }
/*     */   
/*     */   public char BeginToken() throws IOException
/*     */   {
/* 170 */     if (this.inBuf > 0)
/*     */     {
/* 172 */       this.inBuf -= 1;
/*     */       
/* 174 */       if (++this.bufpos == this.bufsize) {
/* 175 */         this.bufpos = 0;
/*     */       }
/* 177 */       this.tokenBegin = this.bufpos;
/* 178 */       return this.buffer[this.bufpos];
/*     */     }
/*     */     
/* 181 */     this.tokenBegin = 0;
/* 182 */     this.bufpos = -1;
/*     */     
/* 184 */     return readChar();
/*     */   }
/*     */   
/*     */   protected void AdjustBuffSize()
/*     */   {
/* 189 */     if (this.available == this.bufsize)
/*     */     {
/* 191 */       if (this.tokenBegin > 2048)
/*     */       {
/* 193 */         this.bufpos = 0;
/* 194 */         this.available = this.tokenBegin;
/*     */       }
/*     */       else {
/* 197 */         ExpandBuff(false);
/*     */       }
/* 199 */     } else if (this.available > this.tokenBegin) {
/* 200 */       this.available = this.bufsize;
/* 201 */     } else if (this.tokenBegin - this.available < 2048) {
/* 202 */       ExpandBuff(true);
/*     */     } else {
/* 204 */       this.available = this.tokenBegin;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void UpdateLineColumn(char c) {
/* 209 */     this.column += 1;
/*     */     
/* 211 */     if (this.prevCharIsLF)
/*     */     {
/* 213 */       this.prevCharIsLF = false;
/* 214 */       this.line += (this.column = 1);
/*     */     }
/* 216 */     else if (this.prevCharIsCR)
/*     */     {
/* 218 */       this.prevCharIsCR = false;
/* 219 */       if (c == '\n')
/*     */       {
/* 221 */         this.prevCharIsLF = true;
/*     */       }
/*     */       else {
/* 224 */         this.line += (this.column = 1);
/*     */       }
/*     */     }
/* 227 */     switch (c)
/*     */     {
/*     */     case '\r': 
/* 230 */       this.prevCharIsCR = true;
/* 231 */       break;
/*     */     case '\n': 
/* 233 */       this.prevCharIsLF = true;
/* 234 */       break;
/*     */     case '\t': 
/* 236 */       this.column -= 1;
/* 237 */       this.column += 8 - (this.column & 0x7);
/* 238 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 243 */     this.bufline[this.bufpos] = this.line;
/* 244 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException
/*     */   {
/* 249 */     if (this.inBuf > 0)
/*     */     {
/* 251 */       this.inBuf -= 1;
/*     */       
/* 253 */       if (++this.bufpos == this.bufsize) {
/* 254 */         this.bufpos = 0;
/*     */       }
/* 256 */       return this.buffer[this.bufpos];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 261 */     if (++this.bufpos == this.available)
/* 262 */       AdjustBuffSize();
/*     */     char c;
/* 264 */     if ((this.buffer[this.bufpos] = c = ReadByte()) == '\\')
/*     */     {
/* 266 */       UpdateLineColumn(c);
/*     */       
/* 268 */       int backSlashCnt = 1;
/*     */       
/*     */       for (;;)
/*     */       {
/* 272 */         if (++this.bufpos == this.available) {
/* 273 */           AdjustBuffSize();
/*     */         }
/*     */         try
/*     */         {
/* 277 */           if ((this.buffer[this.bufpos] = c = ReadByte()) != '\\')
/*     */           {
/* 279 */             UpdateLineColumn(c);
/*     */             
/* 281 */             if ((c == 'u') && ((backSlashCnt & 0x1) == 1))
/*     */             {
/* 283 */               if (--this.bufpos >= 0) break;
/* 284 */               this.bufpos = (this.bufsize - 1);
/*     */               
/* 286 */               break;
/*     */             }
/*     */             
/* 289 */             backup(backSlashCnt);
/* 290 */             return '\\';
/*     */           }
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 295 */           if (backSlashCnt > 1) {
/* 296 */             backup(backSlashCnt);
/*     */           }
/* 298 */           return '\\';
/*     */         }
/*     */         
/* 301 */         UpdateLineColumn(c);
/* 302 */         backSlashCnt++;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*     */         do
/*     */         {
/* 309 */           this.column += 1;
/* 308 */         } while ((c = ReadByte()) == 'u');
/*     */         
/*     */ 
/* 311 */         this.buffer[this.bufpos] = 
/*     */         
/*     */ 
/* 314 */           (c = (char)(hexval(c) << 12 | hexval(ReadByte()) << 8 | hexval(ReadByte()) << 4 | hexval(ReadByte())));
/*     */         
/* 316 */         this.column += 4;
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 320 */         throw new Error("Invalid escape character at line " + this.line + 
/* 321 */           " column " + this.column + ".");
/*     */       }
/*     */       
/* 324 */       if (backSlashCnt == 1) {
/* 325 */         return c;
/*     */       }
/*     */       
/* 328 */       backup(backSlashCnt - 1);
/* 329 */       return '\\';
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 334 */     UpdateLineColumn(c);
/* 335 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getColumn()
/*     */   {
/* 345 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public int getLine()
/*     */   {
/* 354 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndColumn() {
/* 358 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndLine() {
/* 362 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getBeginColumn() {
/* 366 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public int getBeginLine() {
/* 370 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public void backup(int amount)
/*     */   {
/* 375 */     this.inBuf += amount;
/* 376 */     if (this.bufpos -= amount < 0) {
/* 377 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */   
/*     */   public JavaCharStream(Reader dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 383 */     this.inputStream = dstream;
/* 384 */     this.line = startline;
/* 385 */     this.column = (startcolumn - 1);
/*     */     
/* 387 */     this.available = (this.bufsize = buffersize);
/* 388 */     this.buffer = new char[buffersize];
/* 389 */     this.bufline = new int[buffersize];
/* 390 */     this.bufcolumn = new int[buffersize];
/* 391 */     this.nextCharBuf = new char['က'];
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaCharStream(Reader dstream, int startline, int startcolumn)
/*     */   {
/* 397 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public JavaCharStream(Reader dstream)
/*     */   {
/* 402 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 407 */     this.inputStream = dstream;
/* 408 */     this.line = startline;
/* 409 */     this.column = (startcolumn - 1);
/*     */     
/* 411 */     if ((this.buffer == null) || (buffersize != this.buffer.length))
/*     */     {
/* 413 */       this.available = (this.bufsize = buffersize);
/* 414 */       this.buffer = new char[buffersize];
/* 415 */       this.bufline = new int[buffersize];
/* 416 */       this.bufcolumn = new int[buffersize];
/* 417 */       this.nextCharBuf = new char['က'];
/*     */     }
/* 419 */     this.prevCharIsLF = (this.prevCharIsCR = 0);
/* 420 */     this.tokenBegin = (this.inBuf = this.maxNextCharInd = 0);
/* 421 */     this.nextCharInd = (this.bufpos = -1);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn)
/*     */   {
/* 427 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(Reader dstream)
/*     */   {
/* 432 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */   public JavaCharStream(InputStream dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 437 */     this(new java.io.InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaCharStream(InputStream dstream, int startline, int startcolumn)
/*     */   {
/* 443 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public JavaCharStream(InputStream dstream)
/*     */   {
/* 448 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */ 
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize)
/*     */   {
/* 454 */     ReInit(new java.io.InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn)
/*     */   {
/* 459 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */   
/*     */   public void ReInit(InputStream dstream) {
/* 463 */     ReInit(dstream, 1, 1, 4096);
/*     */   }
/*     */   
/*     */   public String GetImage()
/*     */   {
/* 468 */     if (this.bufpos >= this.tokenBegin) {
/* 469 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 471 */     return 
/* 472 */       new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */   
/*     */   public char[] GetSuffix(int len)
/*     */   {
/* 477 */     char[] ret = new char[len];
/*     */     
/* 479 */     if (this.bufpos + 1 >= len) {
/* 480 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     }
/*     */     else {
/* 483 */       System.arraycopy(this.buffer, this.bufsize - (len - this.bufpos - 1), ret, 0, 
/* 484 */         len - this.bufpos - 1);
/* 485 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     }
/*     */     
/* 488 */     return ret;
/*     */   }
/*     */   
/*     */   public void Done()
/*     */   {
/* 493 */     this.nextCharBuf = null;
/* 494 */     this.buffer = null;
/* 495 */     this.bufline = null;
/* 496 */     this.bufcolumn = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void adjustBeginLineColumn(int newLine, int newCol)
/*     */   {
/* 504 */     int start = this.tokenBegin;
/*     */     int len;
/*     */     int len;
/* 507 */     if (this.bufpos >= this.tokenBegin)
/*     */     {
/* 509 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else
/*     */     {
/* 513 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     }
/*     */     
/* 516 */     int i = 0;int j = 0;int k = 0;
/* 517 */     int nextColDiff = 0;int columnDiff = 0;
/*     */     
/* 519 */     while ((i < len) && 
/* 520 */       (this.bufline[(j = start % this.bufsize)] == this.bufline[(k = ++start % this.bufsize)]))
/*     */     {
/* 522 */       this.bufline[j] = newLine;
/* 523 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 524 */       this.bufcolumn[j] = (newCol + columnDiff);
/* 525 */       columnDiff = nextColDiff;
/* 526 */       i++;
/*     */     }
/*     */     
/* 529 */     if (i < len)
/*     */     {
/* 531 */       this.bufline[j] = (newLine++);
/* 532 */       this.bufcolumn[j] = (newCol + columnDiff);
/*     */       
/* 534 */       while (i++ < len)
/*     */       {
/* 536 */         if (this.bufline[(j = start % this.bufsize)] != this.bufline[(++start % this.bufsize)]) {
/* 537 */           this.bufline[j] = (newLine++);
/*     */         } else {
/* 539 */           this.bufline[j] = newLine;
/*     */         }
/*     */       }
/*     */     }
/* 543 */     this.line = this.bufline[j];
/* 544 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/parser/JavaCharStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */