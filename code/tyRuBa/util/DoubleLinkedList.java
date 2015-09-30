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
/*     */ public class DoubleLinkedList
/*     */ {
/*     */   public static class Entry
/*     */   {
/*     */     private Entry prev;
/*     */     private Entry next;
/*     */     
/*     */     public Entry()
/*     */     {
/*  31 */       this.prev = null;
/*  32 */       this.next = null;
/*     */     }
/*     */   }
/*     */   
/*  36 */   private Entry head = null;
/*  37 */   private Entry tail = null;
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
/*     */   public Entry head() {
/*  52 */     return this.head;
/*     */   }
/*     */   
/*     */   public Entry tail() {
/*  56 */     return this.tail;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  60 */     this.head = null;
/*  61 */     this.tail = null;
/*  62 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void enqueue(Entry entry) {
/*  66 */     if (this.head != null) {
/*  67 */       entry.prev = null;
/*  68 */       entry.next = this.head;
/*     */       
/*  70 */       this.head.prev = entry;
/*  71 */       this.head = entry;
/*     */     } else {
/*  73 */       entry.prev = null;
/*  74 */       entry.next = null;
/*     */       
/*  76 */       this.head = entry;
/*  77 */       this.tail = this.head;
/*     */     }
/*     */     
/*  80 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void addLast(Entry entry) {
/*  84 */     if (this.tail != null) {
/*  85 */       entry.prev = this.tail;
/*  86 */       entry.next = null;
/*     */       
/*  88 */       this.tail.next = entry;
/*  89 */       this.tail = entry;
/*     */     } else {
/*  91 */       entry.prev = null;
/*  92 */       entry.next = null;
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
/* 107 */         this.head.prev = list.tail;
/* 108 */         list.tail.next = this.head;
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
/*     */   public void addAfter(DoubleLinkedList list, Entry entry) {
/* 122 */     if (list.head != null) {
/* 123 */       if (entry != this.tail) {
/* 124 */         list.tail.next = entry.next;
/* 125 */         entry.next.prev = list.tail;
/* 126 */         entry.next = list.head;
/* 127 */         list.head.prev = entry;
/*     */       } else {
/* 129 */         this.tail = list.tail;
/* 130 */         entry.next = list.head;
/* 131 */         list.head.prev = entry;
/*     */       }
/*     */       
/* 134 */       this.size += list.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAfter(Entry after, Entry entry)
/*     */   {
/* 142 */     if (entry != this.tail) {
/* 143 */       after.next = entry.next;
/* 144 */       entry.next.prev = after;
/* 145 */       entry.next = after;
/* 146 */       after.prev = entry;
/*     */     } else {
/* 148 */       after.next = null;
/* 149 */       this.tail = after;
/* 150 */       entry.next = after;
/* 151 */       after.prev = entry;
/*     */     }
/*     */     
/* 154 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void addBefore(DoubleLinkedList list, Entry entry) {
/* 158 */     if (list.head != null) {
/* 159 */       if (entry != this.head) {
/* 160 */         list.head.prev = entry.prev;
/* 161 */         entry.prev.next = list.head;
/* 162 */         entry.prev = list.tail;
/* 163 */         list.tail.next = entry;
/*     */       } else {
/* 165 */         this.head = list.head;
/* 166 */         entry.prev = list.tail;
/* 167 */         list.tail.next = entry;
/*     */       }
/*     */       
/* 170 */       this.size += list.size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBefore(Entry before, Entry entry)
/*     */   {
/* 179 */     if (entry != this.head) {
/* 180 */       before.prev = entry.prev;
/* 181 */       entry.prev.next = before;
/* 182 */       entry.prev = before;
/* 183 */       before.next = entry;
/*     */     } else {
/* 185 */       before.prev = null;
/* 186 */       this.head = before;
/* 187 */       entry.prev = before;
/* 188 */       before.next = entry;
/*     */     }
/*     */     
/* 191 */     this.size += 1;
/*     */   }
/*     */   
/*     */   public void remove(Entry entry) {
/* 195 */     if (entry != this.head) {
/* 196 */       if (entry != this.tail) {
/* 197 */         entry.prev.next = entry.next;
/* 198 */         entry.next.prev = entry.prev;
/*     */       } else {
/* 200 */         entry.prev.next = null;
/* 201 */         this.tail = entry.prev;
/*     */       }
/*     */     }
/* 204 */     else if (entry != this.tail) {
/* 205 */       entry.next.prev = null;
/* 206 */       this.head = entry.next;
/*     */     } else {
/* 208 */       this.head = null;
/* 209 */       this.tail = null;
/*     */     }
/*     */     
/*     */ 
/* 213 */     entry.prev = null;
/* 214 */     entry.next = null;
/*     */     
/* 216 */     this.size -= 1;
/*     */   }
/*     */   
/*     */   public Entry dequeue() {
/* 220 */     Entry result = this.tail;
/* 221 */     remove(result);
/* 222 */     return result;
/*     */   }
/*     */   
/*     */   public Entry peek() {
/* 226 */     return this.tail;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 230 */     String result = "DoubleLL( ";
/* 231 */     Entry current = this.head;
/* 232 */     while (current != null) {
/* 233 */       result = result + current + " ";
/* 234 */       current = current.next;
/*     */     }
/* 236 */     return result + ")";
/*     */   }
/*     */   
/*     */   public Iterator iterator() {
/* 240 */     new Iterator()
/*     */     {
/* 242 */       private DoubleLinkedList.Entry current = DoubleLinkedList.this.head;
/*     */       
/*     */       public boolean hasNext() {
/* 245 */         return this.current != null;
/*     */       }
/*     */       
/*     */       public Object next() {
/* 249 */         DoubleLinkedList.Entry result = this.current;
/* 250 */         this.current = DoubleLinkedList.Entry.access$2(this.current);
/* 251 */         return result;
/*     */       }
/*     */       
/*     */       public void remove() {
/* 255 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/DoubleLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */