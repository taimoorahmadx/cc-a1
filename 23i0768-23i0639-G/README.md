# NovaLang Compiler - Assignment 01

## Team Members
* RollNo1: 23I-0639
* RollNo2: 23I-0768
* Section: G

## Language Overview
* **Language Name:** NovaLang
* **File Extension:** `.lang`
*(For the complete language specifications, keywords, syntax rules, and sample programs, please see `docs/README.md`)*

## Git Flow
git pull
# edit files
git add .
git commit -m "Describe changes"
git push

# Folder Structure
22i1234-22i5678-A/
    src/
        ManualScanner.java, Token.java, TokenType.java
        SymbolTable.java, ErrorHandler.java
        Scanner.flex, Yylex.java
    docs/
        Automata_Design.pdf
        README.md
        LanguageGrammar.txt
        Comparison.pdf
    tests/
        test1.lang (all valid tokens)
        test2.lang (complex expressions)
        test3.lang (string/char with escapes)
        test4.lang (lexical errors)
        test5.lang (comments)
        TestResults.txt
    README.md

------------------------
# How to Run?
**Part 1: Manual Scanner**
* Compile: javac src/*.java
* Execute: java -cp src ManualScanner tests/test1.lang
**Part 2: JFlex Scanner**
* Generate: jflex src/Scanner.flex
* Compile: javac src/*.java
* Execute: java -cp src JFlexRunner tests/test1.lang

Note: Change `test1.lang` to `test2.lang` - `test5.lang` to run other test cases.