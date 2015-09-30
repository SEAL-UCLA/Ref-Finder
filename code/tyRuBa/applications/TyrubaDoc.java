/*     */ package tyRuBa.applications;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import tyRuBa.engine.FrontEnd;
/*     */ import tyRuBa.modes.TypeModeError;
/*     */ import tyRuBa.parser.ParseException;
/*     */ import tyRuBa.tdbc.Connection;
/*     */ import tyRuBa.tdbc.PreparedQuery;
/*     */ import tyRuBa.tdbc.ResultSet;
/*     */ import tyRuBa.tdbc.TyrubaException;
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
/*     */ public class TyrubaDoc
/*     */ {
/*     */   private static PrintWriter out;
/*  36 */   private static Set groups = new TreeSet();
/*  37 */   private static File header = null;
/*  38 */   private static File footer = null;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  42 */     if (args.length < 2) {
/*  43 */       usage();
/*  44 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/*  48 */     if (args.length > 2) {
/*  49 */       header = new File(args[2]);
/*     */     }
/*     */     
/*  52 */     if (args.length > 3) {
/*  53 */       footer = new File(args[3]);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  59 */       File fout = new File(args[1]);
/*     */       
/*  61 */       out = new PrintWriter(new FileOutputStream(fout));
/*     */       
/*     */ 
/*  64 */       FrontEnd frontEnd = new FrontEnd(true);
/*  65 */       File fin = new File(args[0]);
/*  66 */       frontEnd.load(fin.toString());
/*  67 */       Connection con = new Connection(frontEnd);
/*     */       
/*     */ 
/*  70 */       PreparedQuery query = con.prepareQuery("tyrubadocGroup(?Order, ?ID, ?Label, ?Description)");
/*  71 */       ResultSet rs = query.executeQuery();
/*     */       
/*  73 */       while (rs.next()) {
/*  74 */         String id = rs.getString("?ID");
/*  75 */         Integer order = new Integer(rs.getInt("?Order"));
/*  76 */         String label = rs.getString("?Label");
/*  77 */         String description = rs.getString("?Description");
/*  78 */         Group g = new Group(id, order, label, description);
/*  79 */         groups.add(g);
/*     */       }
/*     */       
/*     */ 
/*  83 */       query = con.prepareQuery("tyrubadoc(?Group, ?Pred, ?Description)");
/*  84 */       rs = query.executeQuery();
/*     */       
/*     */ 
/*     */ 
/*  88 */       printHeader();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  93 */       Map groupRules = new TreeMap();
/*     */       
/*  95 */       while (rs.next()) {
/*  96 */         String group = rs.getString("?Group");
/*  97 */         String rule = rs.getString("?Pred");
/*  98 */         String doc = rs.getString("?Description");
/*  99 */         Map rules = (Map)groupRules.get(group);
/* 100 */         if (rules == null) {
/* 101 */           rules = new TreeMap();
/* 102 */           groupRules.put(group, rules);
/*     */         }
/* 104 */         rules.put(rule, doc);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 109 */       Iterator iter = groups.iterator();
/* 110 */       while (iter.hasNext()) {
/* 111 */         Group group = (Group)iter.next();
/* 112 */         Map rules = (Map)groupRules.remove(group.id);
/* 113 */         if (rules == null) {
/* 114 */           rules = new TreeMap();
/*     */         }
/* 116 */         printGroup(group, rules);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 121 */       iter = groupRules.keySet().iterator();
/* 122 */       while (iter.hasNext()) {
/* 123 */         String id = (String)iter.next();
/* 124 */         Map rules = (Map)groupRules.get(id);
/* 125 */         Group g = new Group(id, new Integer(0), id, "");
/* 126 */         printGroup(g, rules);
/*     */       }
/*     */       
/* 129 */       printFooter();
/*     */       
/* 131 */       out.close();
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 134 */       e.printStackTrace();
/*     */     } catch (ParseException e) {
/* 136 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 138 */       e.printStackTrace();
/*     */     } catch (TypeModeError e) {
/* 140 */       e.printStackTrace();
/*     */     } catch (TyrubaException e) {
/* 142 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void printGroup(Group group, Map rules)
/*     */   {
/* 152 */     out.println("<h2><a name=\"" + group.id + "\">" + group.label + "</a></h2>");
/*     */     
/* 154 */     out.println("<p>" + group.description + "</p>");
/*     */     
/* 156 */     out.println("<table cellspacing=\"0\" cellpadding=\"3\" border=\"1\">");
/* 157 */     out.println("<tr>");
/* 158 */     out.println("<td class=\"TyrubaDocHeading\">Predicate</td><td class=\"TyrubaDocHeading\">Description</td>");
/* 159 */     out.println("</tr>");
/*     */     
/* 161 */     Iterator iter = rules.keySet().iterator();
/* 162 */     while (iter.hasNext()) {
/* 163 */       String rule = (String)iter.next();
/* 164 */       String doc = (String)rules.get(rule);
/* 165 */       printDoc(rule, doc);
/*     */     }
/*     */     
/* 168 */     out.println("</table>");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void printHeader()
/*     */     throws IOException
/*     */   {
/* 176 */     if ((header != null) && (header.exists())) {
/* 177 */       copyfile(header);
/*     */     }
/*     */     else {
/* 180 */       out.println("<html>");
/* 181 */       out.println("<head>");
/* 182 */       out.println("<title>TyRuBa Documentation</title>");
/* 183 */       out.println("<style>");
/* 184 */       out.println(".TyrubaDocHeading { background-color: #CCCCCC; font-weight: bold; }");
/* 185 */       out.println("</style>");
/* 186 */       out.println("</head>");
/* 187 */       out.println("<body>");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void printDoc(String rule, String doc)
/*     */   {
/* 197 */     out.println("<tr>");
/* 198 */     out.println("<td class=\"TyrubaDocPredicate\">" + rule + "</td>");
/* 199 */     out.println("<td class=\"TyrubaDocDescription\">" + doc + "</td>");
/* 200 */     out.println("</tr>");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void printFooter()
/*     */     throws IOException
/*     */   {
/* 209 */     if ((footer != null) && (footer.exists())) {
/* 210 */       copyfile(footer);
/*     */     }
/*     */     else {
/* 213 */       out.println("</body>");
/* 214 */       out.println("</html>");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void copyfile(File f) throws IOException {
/* 219 */     LineNumberReader reader = new LineNumberReader(new FileReader(f));
/* 220 */     String line = reader.readLine();
/* 221 */     while (line != null) {
/* 222 */       out.println(line);
/* 223 */       line = reader.readLine();
/*     */     }
/* 225 */     reader.close();
/*     */   }
/*     */   
/*     */   private static void usage()
/*     */   {
/* 230 */     System.out.println("usage: TyrubaDoc <input file> <output file> [header] [footer]");
/* 231 */     System.out.println("\t<input file> = the tyruba rules file containing documentation");
/* 232 */     System.out.println("\t<output file> = the file to which the html is to be written");
/* 233 */     System.out.println("\t[header] = optional arg indicating file to use as the header for the output");
/* 234 */     System.out.println("\t[footer] = optional arg indicating file to use as the footer for the output");
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/applications/TyrubaDoc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */