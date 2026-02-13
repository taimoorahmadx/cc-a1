Git flow:
    git pull
    # edit files
    git add .
    git commit -m "Describe changes"
    git push

7.1 Folder Structure
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

7.2 README.md Must Include
    • Language name and file extension
    • Complete keyword list with meanings
    • Identifier rules and examples
    • Literal formats with examples
    • Operator list with precedence
    • Comment syntax
    • At least 3 sample programs
    • Compilation and execution instructions
    • Team members with roll numbers