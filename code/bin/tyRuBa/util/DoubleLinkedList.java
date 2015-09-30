/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ public class DoubleLinkedList
/*     */ {
/*  36 */   private DoubleLinkedList.Entry head = null;
/*  37 */   private DoubleLinkedList.Entry tail = null;
/*  38 */   private int size = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  44 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   public int size() {
/*  48 */     return this.size;
/*     */   }
/*     */   
/*     */   public DoubleLinkedList.Entry head() {
/*  52 */     return this.head;
/*     */   }
/*     */   
/*     */   public DoubleLinkedList.Entry tail() {
/*  56 */     return this.tail;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  60 */     this.head = null;
/*  61 */     this.tail = null;
/*  62 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void enqueue(DoubleLinkedList.Entry entry) {
/*  66 */     if (this.head != null) {
/*  67 */       DoubleLinkedList.Entry.access$0(entry, null);
/*  68 */       DoubleLinkedList.Entry.access$1(entry, this.head);
/*     */       
/*  70 */       DoubleLinkedList.Entry.access$0(this.head, entry);
/*  71 */       this.head = entry;
/*     */     } else {
/*  73 */       DoubleLinkedList.Entry.access$0(entry, null);
/*  74 */       DoubleLinkedList.Entry.access$1(entry, null);
/*     */       
/*  76 */       this.head = entry;
/*  77 */       this.tail = this.head;
/*     */     }
/*     */     
/*  80 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void addLast(DoubleLinkedList.Entry entry) {
/*  84 */     if (this.tail != null) {
/*  85 */       DoubleLinkedList.Entry.access$0(entry, this.tail);
/*  86 */       DoubleLinkedList.Entry.access$1(entry, null);
/*     */       
/*  88 */       DoubleLinkedList.Entry.access$1(this.tail, entry);
/*  89 */       this.tail = entry;
/*     */     } else {
/*  91 */       DoubleLinkedList.Entry.access$0(entry, null);
/*  92 */       DoubleLinkedList.Entry.access$1(entry, null);
/*     */       
/*  94 */       this.tail = entry;
/*  95 */       this.head = this.tail;
/*     */     }
/*     */     
/*  98 */     this.size += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAll(DoubleLinkedList list)
/*     */   {
/* 105 */     if (list.head != null) {
/* 106 */       if (this.head != null) {
/* 107 */         DoubleLinkedList.Entry.access$0(this.head, list.tail);
/* 108 */         DoubleLinkedList.Entry.access$1(list.tail, this.head);
/* 109 */         this.head = list.head;
/*     */         
/* 111 */         this.size += list.size;
/*     */       } else {
/* 113 */         this.head = list.head;
/* 114 */         this.tail = list.tail;
/*     */         
/* 116 */         this.size = list.size;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAfter(DoubleLinkedList list, DoubleLinkedList.Entry entry) {
/* 122 */     if (list.head != null) {
/* 123 */       if (entry != this.tail) {
/* 124 */         DoubleLinkedList.Entry.access$1(list.tail, DoubleLinkedList.Entry.access$2(entry));
/* 125 */         DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), list.tail);
/* 126 */         DoubleLinkedList.Entry.access$1(entry, list.head);
/* 127 */         DoubleLinkedList.Entry.access$0(list.head, entry);
/*     */       } else {
/* 129 */         this.tail = list.tail;
/* 130 */         DoubleLinkedList.Entry.access$1(entry, list.head);
/* 131 */         DoubleLinkedList.Entry.access$0(list.head, entry);
/*     */       }
/*     */       
/* 134 */       this.size += list.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAfter(DoubleLinkedList.Entry after, DoubleLinkedList.Entry entry)
/*     */   {
/* 142 */     if (entry != this.tail) {
/* 143 */       DoubleLinkedList.Entry.access$1(after, DoubleLinkedList.Entry.access$2(entry));
/* 144 */       DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), after);
/* 145 */       DoubleLinkedList.Entry.access$1(entry, after);
/* 146 */       DoubleLinkedList.Entry.access$0(after, entry);
/*     */     } else {
/* 148 */       DoubleLinkedList.Entry.access$1(after, null);
/* 149 */       this.tail = after;
/* 150 */       DoubleLinkedList.Entry.access$1(entry, after);
/* 151 */       DoubleLinkedList.Entry.access$0(after, entry);
/*     */     }
/*     */     
/* 154 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void addBefore(DoubleLinkedList list, DoubleLinkedList.Entry entry) {
/* 158 */     if (list.head != null) {
/* 159 */       if (entry != this.head) {
/* 160 */         DoubleLinkedList.Entry.access$0(list.head, DoubleLinkedList.Entry.access$3(entry));
/* 161 */         DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), list.head);
/* 162 */         DoubleLinkedList.Entry.access$0(entry, list.tail);
/* 163 */         DoubleLinkedList.Entry.access$1(list.tail, entry);
/*     */       } else {
/* 165 */         this.head = list.head;
/* 166 */         DoubleLinkedList.Entry.access$0(entry, list.tail);
/* 167 */         DoubleLinkedList.Entry.access$1(list.tail, entry);
/*     */       }
/*     */       
/* 170 */       this.size += list.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBefore(DoubleLinkedList.Entry before, DoubleLinkedList.Entry entry)
/*     */   {
/* 179 */     if (entry != this.head) {
/* 180 */       DoubleLinkedList.Entry.access$0(before, DoubleLinkedList.Entry.access$3(entry));
/* 181 */       DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), before);
/* 182 */       DoubleLinkedList.Entry.access$0(entry, before);
/* 183 */       DoubleLinkedList.Entry.access$1(before, entry);
/*     */     } else {
/* 185 */       DoubleLinkedList.Entry.access$0(before, null);
/* 186 */       this.head = before;
/* 187 */       DoubleLinkedList.Entry.access$0(entry, before);
/* 188 */       DoubleLinkedList.Entry.access$1(before, entry);
/*     */     }
/*     */     
/* 191 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void remove(DoubleLinkedList.Entry entry) {
/* 195 */     if (entry != this.head) {
/* 196 */       if (entry != this.tail) {
/* 197 */         DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), DoubleLinkedList.Entry.access$2(entry));
/* 198 */         DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), DoubleLinkedList.Entry.access$3(entry));
/*     */       } else {
/* 200 */         DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), null);
/* 201 */         this.tail = DoubleLinkedList.Entry.access$3(entry);
/*     */       }
/*     */     }
/* 204 */     else if (entry != this.tail) {
/* 205 */       DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), null);
/* 206 */       this.head = DoubleLinkedList.Entry.access$2(entry);
/*     */     } else {
/* 208 */       this.head = null;
/* 209 */       this.tail = null;
/*     */     }
/*     */     
/*     */ 
/* 213 */     DoubleLinkedList.Entry.access$0(entry, null);
/* 214 */     DoubleLinkedList.Entry.access$1(entry, null);
/*     */     
/* 216 */     this.size -= 1;
/*     */   }
/*     */   
/*     */   public DoubleLinkedList.Entry dequeue() {
/* 220 */     DoubleLinkedList.Entry result = this.tail;
/* 221 */     remove(result);
/* 222 */     return result;
/*     */   }
/*     */   
/*     */   public DoubleLinkedList.Entry peek() {
/* 226 */     return this.tail;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 230 */     String result = "DoubleLL( ";
/* 231 */     DoubleLinkedList.Entry current = this.head;
/* 232 */     while (current != null) {
/* 233 */       result = result + current + " ";
/* 234 */       current = DoubleLinkedList.Entry.access$2(current);
/*     */     }
/* 236 */     return result + ")";
/*     */   }
/*     */   
/*     */   public Iterator iterator() {
/* 240 */     return new DoubleLinkedList.1(this);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/DoubleLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */