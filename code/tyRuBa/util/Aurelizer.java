/*     */ package tyRuBa.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.Clip;
/*     */ import javax.sound.sampled.DataLine.Info;
/*     */ import javax.sound.sampled.LineUnavailableException;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Aurelizer
/*     */ {
/*  24 */   public static final Aurelizer debug_sounds = null;
/*     */   
/*     */ 
/*     */ 
/*  28 */   Map clips = new HashMap();
/*     */   
/*     */   public Aurelizer() {
/*  31 */     defineClip("store", "police.au");
/*  32 */     defineClip("load", "dead.au");
/*  33 */     defineClip("backup", "FlyinOff.au");
/*  34 */     defineClip("error", "Buzz01.au");
/*  35 */     defineClip("ok", "Ding.au");
/*  36 */     defineClip("compact", "empty.au");
/*  37 */     defineClip("split", "cork.au");
/*  38 */     defineClip("zero_compact", "ding2.au");
/*  39 */     defineClip("temporizing", "alarmbell.au");
/*  40 */     defineClip("temporizing2", "gong.au");
/*     */   }
/*     */   
/*     */   private void defineClip(String eventName, String soundFileName) {
/*  44 */     Clip clip = null;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  50 */       URL url = getClass().getClassLoader().getResource("lib/au/" + soundFileName);
/*  51 */       AudioInputStream stream = AudioSystem.getAudioInputStream(url);
/*     */       
/*     */ 
/*     */ 
/*  55 */       AudioFormat format = stream.getFormat();
/*  56 */       if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
/*  57 */         format = 
/*  58 */           new AudioFormat(
/*  59 */           AudioFormat.Encoding.PCM_SIGNED, 
/*  60 */           format.getSampleRate(), 
/*  61 */           format.getSampleSizeInBits() * 2, 
/*  62 */           format.getChannels(), 
/*  63 */           format.getFrameSize() * 2, 
/*  64 */           format.getFrameRate(), 
/*  65 */           true);
/*     */         
/*  67 */         stream = AudioSystem.getAudioInputStream(format, stream);
/*     */       }
/*     */       
/*     */ 
/*  71 */       DataLine.Info info = 
/*  72 */         new DataLine.Info(
/*  73 */         Clip.class, 
/*  74 */         stream.getFormat(), 
/*  75 */         (int)stream.getFrameLength() * format.getFrameSize());
/*  76 */       clip = (Clip)AudioSystem.getLine(info);
/*     */       
/*     */ 
/*  79 */       clip.open(stream);
/*     */       
/*  81 */       this.clips.put(eventName, clip);
/*     */     }
/*     */     catch (MalformedURLException localMalformedURLException) {}catch (IOException localIOException) {}catch (LineUnavailableException localLineUnavailableException) {}catch (UnsupportedAudioFileException localUnsupportedAudioFileException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  91 */     new Aurelizer();
/*     */   }
/*     */   
/*     */   public synchronized void enter(String eventName) {
/*  95 */     Clip clip = (Clip)this.clips.get(eventName);
/*  96 */     if (clip.isActive()) clip.stop();
/*  97 */     clip.setFramePosition(0);
/*  98 */     clip.start();
/*     */   }
/*     */   
/*     */   public synchronized void exit(String eventName) {
/* 102 */     Clip clip = (Clip)this.clips.get(eventName);
/* 103 */     clip.stop();
/*     */   }
/*     */   
/*     */   public synchronized void enter_loop(String eventName) {
/* 107 */     Clip clip = (Clip)this.clips.get(eventName);
/* 108 */     if (clip.isActive()) clip.stop();
/* 109 */     clip.setFramePosition(0);
/* 110 */     clip.loop(-1);
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/Aurelizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */