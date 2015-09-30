/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectTuple
/*     */   implements Serializable
/*     */ {
/*     */   private boolean isSingleton;
/*     */   private Object[] objects;
/*     */   private Object singletonObj;
/*  19 */   public static ObjectTuple theEmpty = new ObjectTuple(new Object[0]);
/*     */   
/*     */   private ObjectTuple(Object[] objects) {
/*  22 */     this.objects = objects;
/*     */   }
/*     */   
/*     */   private ObjectTuple(Object object, boolean isSingleton) {
/*  26 */     this.singletonObj = object;
/*  27 */     this.isSingleton = isSingleton;
/*  28 */     System.err.println("MAKING A SINGLETON ObjectTuple, something probably isn't right");
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  32 */     out.writeBoolean(this.isSingleton);
/*  33 */     if (this.isSingleton) {
/*  34 */       out.writeObject(this.singletonObj);
/*     */     } else {
/*  36 */       out.writeInt(this.objects.length);
/*  37 */       for (int i = 0; i < this.objects.length; i++) {
/*  38 */         out.writeObject(this.objects[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  44 */     this.isSingleton = in.readBoolean();
/*  45 */     if (this.isSingleton) {
/*  46 */       this.singletonObj = in.readObject();
/*  47 */       if ((this.singletonObj instanceof String)) {
/*  48 */         this.singletonObj = ((String)this.singletonObj).intern();
/*     */       }
/*     */     } else {
/*  51 */       this.objects = new Object[in.readInt()];
/*  52 */       for (int i = 0; i < this.objects.length; i++) {
/*  53 */         this.objects[i] = in.readObject();
/*  54 */         if ((this.objects[i] instanceof String)) {
/*  55 */           this.objects[i] = ((String)this.objects[i]).intern();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static ObjectTuple make(Object[] objs) {
/*  62 */     if (objs.length == 0)
/*  63 */       return theEmpty;
/*  64 */     if (objs.length == 1) {
/*  65 */       return new ObjectTuple(objs[0], true);
/*     */     }
/*     */     
/*  68 */     return new ObjectTuple(objs);
/*     */   }
/*     */   
/*     */   public static ObjectTuple makeSingleton(Object o)
/*     */   {
/*  73 */     return new ObjectTuple(o, true);
/*     */   }
/*     */   
/*     */   public int size() {
/*  77 */     if (this.isSingleton) {
/*  78 */       return 1;
/*     */     }
/*  80 */     return this.objects.length;
/*     */   }
/*     */   
/*     */   public Object get(int i)
/*     */   {
/*  85 */     if (this.isSingleton) {
/*  86 */       if (i != 0) {
/*  87 */         throw new Error("Index out of bounds");
/*     */       }
/*  89 */       return this.singletonObj;
/*     */     }
/*  91 */     return this.objects[i];
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  96 */     if (obj.getClass() == getClass()) {
/*  97 */       if (this == obj) {
/*  98 */         return true;
/*     */       }
/*     */       
/* 101 */       ObjectTuple other = (ObjectTuple)obj;
/*     */       
/* 103 */       if ((this.isSingleton) && (other.isSingleton))
/* 104 */         return this.singletonObj.equals(other.singletonObj);
/* 105 */       if (this.isSingleton != other.isSingleton) {
/* 106 */         return false;
/*     */       }
/* 108 */       for (int i = 0; i < this.objects.length; i++) {
/* 109 */         if (!this.objects[i].equals(other.objects[i])) {
/* 110 */           return false;
/*     */         }
/*     */       }
/* 113 */       return true;
/*     */     }
/*     */     
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   public static ObjectTuple append(ObjectTuple first, ObjectTuple second)
/*     */   {
/* 121 */     Object[] result = new Object[first.size() + second.size()];
/* 122 */     for (int i = 0; i < first.size(); i++) {
/* 123 */       result[i] = first.get(i);
/*     */     }
/* 125 */     for (int i = 0; i < second.size(); i++) {
/* 126 */       result[(first.size() + i)] = second.get(i);
/*     */     }
/* 128 */     return make(result);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 134 */     StringBuffer result = new StringBuffer();
/* 135 */     result.append("<<");
/* 136 */     if (this.isSingleton) {
/* 137 */       result.append(this.singletonObj);
/*     */     } else {
/* 139 */       for (int i = 0; i < this.objects.length; i++) {
/* 140 */         if (i > 0)
/* 141 */           result.append(", ");
/* 142 */         result.append(this.objects[i].toString());
/*     */       }
/*     */     }
/* 145 */     result.append(">>");
/* 146 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 153 */     if (this.isSingleton) {
/* 154 */       int hash = 1;
/* 155 */       return hash * 83 + this.singletonObj.hashCode();
/*     */     }
/* 157 */     int hash = this.objects.length;
/* 158 */     for (int i = 0; i < this.objects.length; i++)
/* 159 */       hash = hash * 83 + this.objects[i].hashCode();
/* 160 */     return hash;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/ObjectTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */