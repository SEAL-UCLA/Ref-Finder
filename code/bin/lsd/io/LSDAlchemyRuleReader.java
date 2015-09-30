/*     */ package lsd.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import lsd.rule.LSDBinding;
/*     */ import lsd.rule.LSDRule;
/*     */ 
/*     */ public class LSDAlchemyRuleReader
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  13 */     LSDRule r = parseAlchemyRule("before_type(y) ^ before_fieldtype(y, X) => before_fieldtype(y, \"z\")");
/*  14 */     if (r != null) {
/*  15 */       System.out.println("LSDRule\n" + r.toString());
/*  16 */       System.out.println("Tyruba Query\n" + r.toTyrubaQuery(true));
/*     */     }
/*  18 */     r = parseAlchemyRule("before_type(x) ^ before_fieldtype( \"foo()\", x) => before_fieldtype(y, x)");
/*  19 */     if (r != null) {
/*  20 */       System.out.println("LSDRule\n" + r.toString());
/*  21 */       System.out.println("Tyruba Query\n" + r.toTyrubaQuery(true));
/*     */     }
/*  23 */     System.out.println("Parser tests completed.");
/*     */   }
/*     */   
/*  26 */   private ArrayList<LSDRule> rules = null;
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
/*     */   public LSDAlchemyRuleReader(java.io.File inputFile)
/*     */   {
/*  50 */     ArrayList<LSDRule> rs = new ArrayList();
/*     */     try {
/*  52 */       if (inputFile.exists()) {
/*  53 */         BufferedReader in = new BufferedReader(
/*  54 */           new java.io.FileReader(inputFile));
/*  55 */         String line = null;
/*  56 */         while ((line = in.readLine()) != null)
/*     */         {
/*  58 */           if ((!line.trim().equals("")) && (line.trim().charAt(0) != '#'))
/*     */           {
/*  60 */             LSDRule rule = parseAlchemyRule(line);
/*  61 */             rs.add(rule);
/*     */           } }
/*  63 */         in.close();
/*     */       }
/*  65 */       this.rules = rs;
/*     */     } catch (java.io.IOException e) {
/*  67 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  72 */   public ArrayList<LSDRule> getRules() { return this.rules; }
/*     */   
/*     */   static int quoteCount(String s) {
/*  75 */     int quoteCount = 0;
/*  76 */     char[] arrayOfChar; int j = (arrayOfChar = s.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/*  77 */       if (c == '"')
/*  78 */         quoteCount++;
/*     */     }
/*  80 */     return quoteCount;
/*     */   }
/*     */   
/*     */   public static LSDRule parseAlchemyRule(String line) {
/*  84 */     line = line.replace("?", "");
/*  85 */     if (line.lastIndexOf("\t") > 0) {
/*  86 */       line = line.substring(0, line.lastIndexOf("\t"));
/*     */     }
/*  88 */     LSDRule rule = new LSDRule();
/*     */     
/*     */ 
/*     */ 
/*  92 */     String ruleString = line.substring(line.indexOf('\t') + 1).trim();
/*     */     
/*  94 */     while (!ruleString.equals(""))
/*     */     {
/*     */ 
/*  97 */       boolean negated = false;
/*  98 */       if (ruleString.charAt(0) == '!')
/*     */       {
/* 100 */         negated = true;
/* 101 */         ruleString = ruleString.substring(1);
/*     */       }
/* 103 */       String predicateName = ruleString.substring(0, ruleString.indexOf('(')).trim();
/* 104 */       ruleString = ruleString.substring(ruleString.indexOf('(') + 1).trim();
/* 105 */       int endOfArgs = ruleString.indexOf(')');
/* 106 */       int firstQuote = ruleString.indexOf('"');
/* 107 */       int secondQuote = ruleString.indexOf('"', firstQuote + 1);
/* 108 */       if ((secondQuote == -1) && (firstQuote != -1))
/*     */       {
/* 110 */         System.err.println("Mismatched quotes in the rule");
/* 111 */         System.err.println("Line: " + line);
/* 112 */         System.exit(-1);
/*     */       }
/* 114 */       while (quoteCount(ruleString.substring(0, endOfArgs)) % 2 != 0)
/*     */       {
/* 116 */         endOfArgs = ruleString.indexOf(')', endOfArgs + 1);
/* 117 */         assert (endOfArgs != -1);
/*     */       }
/* 119 */       String arguments = ruleString.substring(0, endOfArgs).trim();
/* 120 */       ruleString = ruleString.substring(endOfArgs + 1).trim();
/* 121 */       if (!ruleString.equals(""))
/*     */       {
/*     */ 
/*     */ 
/* 125 */         if (ruleString.charAt(0) == 'v')
/*     */         {
/* 127 */           ruleString = ruleString.substring(1).trim();
/*     */         }
/* 129 */         else if (ruleString.charAt(0) == '^')
/*     */         {
/* 131 */           ruleString = ruleString.substring(1).trim();
/* 132 */           negated = !negated;
/*     */         }
/* 134 */         else if (ruleString.charAt(0) == '=')
/*     */         {
/* 136 */           assert (ruleString.charAt(1) == '>');
/* 137 */           ruleString = ruleString.substring(2).trim();
/* 138 */           negated = !negated;
/*     */         }
/*     */         else
/*     */         {
/* 142 */           System.err.println("Rule ill defined...");
/* 143 */           System.err.println("Line: " + line);
/* 144 */           System.err.println("Remaining: " + ruleString);
/* 145 */           System.exit(-1);
/*     */         } }
/* 147 */       lsd.rule.LSDPredicate predicate = lsd.rule.LSDPredicate.getPredicate(predicateName);
/* 148 */       if (predicate == null)
/*     */       {
/* 150 */         System.err.println("Predicate " + predicateName + " is not defined.");
/* 151 */         System.err.println("Line: " + line);
/* 152 */         System.exit(-1);
/*     */       }
/* 154 */       ArrayList<LSDBinding> bindings = new ArrayList();
/* 155 */       char[] types = predicate.getTypes();
/* 156 */       for (int i = 0; i < types.length; i++)
/*     */       {
/* 158 */         if (arguments.charAt(0) == '"')
/*     */         {
/* 160 */           String constant = arguments.substring(0, arguments.indexOf('"', 1) + 1);
/* 161 */           arguments = arguments.substring(arguments.indexOf('"', 1) + 1).trim();
/* 162 */           if (i != types.length - 1)
/*     */           {
/* 164 */             assert (arguments.charAt(0) == ',');
/* 165 */             arguments = arguments.substring(1).trim();
/*     */           }
/* 167 */           bindings.add(new LSDBinding(constant));
/*     */         }
/* 169 */         else if (Character.isUpperCase(arguments.charAt(0)))
/*     */         {
/* 171 */           String constant = "";
/* 172 */           if (i != types.length - 1)
/*     */           {
/* 174 */             assert (arguments.contains(","));
/* 175 */             constant = arguments.substring(0, arguments.indexOf(',', 1));
/* 176 */             arguments = arguments.substring(arguments.indexOf(',') + 1).trim();
/*     */           }
/*     */           else
/*     */           {
/* 180 */             assert (!arguments.contains(","));
/* 181 */             constant = arguments;
/*     */           }
/* 183 */           bindings.add(new LSDBinding(constant));
/*     */         }
/*     */         else
/*     */         {
/* 187 */           String varName = "";
/* 188 */           if (i != types.length - 1)
/*     */           {
/* 190 */             assert (arguments.contains(","));
/* 191 */             varName = arguments.substring(0, arguments.indexOf(',', 1));
/* 192 */             arguments = arguments.substring(arguments.indexOf(',') + 1).trim();
/*     */           }
/*     */           else
/*     */           {
/* 196 */             if (arguments.contains(","))
/*     */             {
/* 198 */               System.err.println("Error: we think '" + arguments + "' shouldn't contain a comma.");
/* 199 */               System.err.println("Line: " + line);
/* 200 */               System.exit(-1);
/*     */             }
/* 202 */             varName = arguments;
/*     */           }
/* 204 */           bindings.add(new LSDBinding(new lsd.rule.LSDVariable(varName, types[i])));
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 209 */         boolean success = rule.addLiteral(new lsd.rule.LSDLiteral(predicate, bindings, !negated));
/* 210 */         if (!success)
/*     */         {
/* 212 */           System.err.println("Error, rules cannot contain facts.");
/* 213 */           System.err.println("Line: " + line);
/* 214 */           System.exit(-1);
/*     */         }
/*     */       }
/*     */       catch (lsd.rule.LSDInvalidTypeException e)
/*     */       {
/* 219 */         e.printStackTrace();
/* 220 */         System.err.println("Line: " + line);
/* 221 */         System.exit(-1);
/*     */       }
/*     */     }
/* 224 */     if (!rule.isValid())
/*     */     {
/*     */ 
/* 227 */       System.err.println("Rule skipped because it's not valid: isHornClause " + 
/* 228 */         rule.isHornClause() + 
/* 229 */         "\tdoesTypeChecks " + 
/* 230 */         rule.typeChecks() + 
/* 231 */         "\tMight also not be properly interrelated.");
/*     */       
/* 233 */       System.err.println("Rule parsed as: " + rule);
/* 234 */       System.err.println("Line: " + line);
/* 235 */       return null;
/*     */     }
/* 237 */     return rule;
/*     */   }
/*     */ }


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsd/io/LSDAlchemyRuleReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */