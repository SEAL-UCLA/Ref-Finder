# Ref-Finder

[Website for Ref-Finder Tool Demo](https://sites.google.com/site/reffindertool/)

RefFinder  identifies complex refactorings between two program versions using a template-based refactoring reconstruction approach---RefFinder expresses each refactoring type in terms of template logic rules and uses a logic programming engine to infer concrete refactoring instances. It currently supports sixty three refactoring types from Fowler's catalog, showing the most comprehensive coverage among existing techniques.

Reference:
Research Paper: [Template-based Reconstruction of Complex Refactorings, Kyle Prete, Napol Rachatasumrit, Nikita Sudan, and Miryung Kim, ICSM '10](http://web.cs.ucla.edu/~miryung/Publications/icsm10-reffinder.pdf)

Tool Demo: [Ref-Finder: a Refactoring Reconstruction Tool based on Logic Query Templates, Miryung Kim, Matthew Gee, Alex Loh, and Napol Rachatasumrit, FSE' 10](http://web.cs.ucla.edu/~miryung/Publications/fse10-reffindertool.pdf)

Here is an example on how to use Ref-Finder jar file to identify refactorings. 

```
import edu.utexas.seal.reffinder.Application; ...
public class YourRefFinderView extends ViewPart {
  public void createPartControl(Composite p) {
    Application refFinder = new Application();
    try {
					refFinder.myStart(projOrig, projDelta, null);
					String xmlFilePath = projDelta.getLocation().toOSString()+System.getProperty("file.separator") +"RefList.xml";
					XmlReader xml = new XmlReader(xmlFilePath);
					//please refer to XmlReader and XMLOutput for more details
				} catch (Exception e1) {
				}
```

## Usage

Step 1: Create your own Eclipse Plugin Project, import RefFinder_1.0.0.jar to classpath

Step 2: Create a Viewer and invoke myStart(IProject projectA, IProject projectB, String pathOutput)

Step 3: Parse the XML output from Ref-Finder.

## Extend Ref-Finder

You may want to start exploring from lsclipse/RefactoringQuery.java. Please check docs folder for syntax guide, etc.

## Dataset for ICSM'10

Please check inspect_dataset.

## Ref-Distiller
A extension of Ref-Finder that we are actively working on.

[Website for Ref-Distiller](https://sites.google.com/site/refdistiller/)

Manual refactoring edits are error prone, as refactoring requires developers to coordinate related transformations and understand the complex inter-relationship between affected files, variables, and methods. We propose RefDistiller, an approach for improving detection of manual refactoring anomalies by two combined strategies. First, it uses a predefined template to identify potential missed refactoring edits---omission anomalies. Second, it leverages an automated refactoring engine to separate behavior-preserving edits from behavior-modifying edits---commission anomalies. We evaluate its effectiveness on a data set with one hundred manual refactoring bugs. These bugs are hard to detect because they do not produce any compilation errors nor are caught by the pre- and post-condition checking of many existing refactoring engines. RefDistiller is able to identify 97% of the erroneous edits, of which 24% are not detected by the given test suites. 

Reference:
[Everton L. G. Alves , Myoungkyu Song , Miryung Kim, RefDistiller: a refactoring aware code review tool for inspecting manual refactoring edits, FSE'14](http://dl.acm.org/citation.cfm?id=2661674&CFID=717088503&CFTOKEN=96750876)

## LSDiff

Ref-Finder is built on LSDiff and we have open-sourced [LSDiff](https://github.com/SEAL-UCLA/lsdiff) and [LS-EclipsePlugin](https://github.com/SEAL-UCLA/ls-eclipse.git)

Software engineers often inspect program differences when reviewing others’ code changes, when writing check-in comments, or when determining why a program behaves differently from expected behavior. Program differencing tools that support these tasks are limited in their ability to group related code changes or to detect potential inconsistency in program changes. To overcome these limitations and to complement existing approaches, we built Logical Structural Diff (LSDiff) that infers systematic structural differences as logic rules, noting anomalies from systematic changes as exceptions to the logic rules. We conducted a focus group study with professional software engineers in a large E-commerce company and also compared LSDiff’s results with plain structural differences without rules and textual differences. Our evaluation suggests that LSDiff complements existing differencing tools by grouping code changes that form systematic change patterns regardless of their distribution throughout the code and that its ability to discover anomalies shows promise in detecting inconsistent changes.
