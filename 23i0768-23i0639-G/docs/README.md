## Language Specification
* Language Name: NovaLang
* File Extension: `.lang`

### Team Members
* RollNo1: 23I-0639
* RollNo2: 23I-0768
* Section: G

### 1. Keywords
`start`, `finish`, `loop`, `condition`, `declare`, `output`, `input`, `function`, `return`, `break`, `continue`, `else`
*All keywords are case-sensitive and match exactly.*

### 2. Identifier Rules
* Must start with an uppercase letter (`A-Z`).
* Followed by lowercase letters, digits, or underscores (`a-z`, `0-9`, `_`).
* Maximum length of 31 characters.
* Examples: `Count`, `Variable_name`, `X12`

### 3. Literal Formats
* **Integer:** Optional `+` or `-`, followed by digits. Example: `42`, `-567`
* **Float:** Optional sign, digits, decimal point, 1-6 digits, optional exponent. Example: `3.14`, `-2.5e10`
* **String:** Enclosed in `""`, supports escapes `\n, \t, \r, \", \\`. Example: `"Hello\nWorld"`
* **Char:** Enclosed in `''`, supports escapes. Example: `'a'`, `'\n'`
* **Boolean:** `true`, `false`

### 4. Operators & Precedence
* **Arithmetic:** `**`, `*`, `/`, `%`, `+`, `-`
* **Relational:** `<`, `>`, `<=`, `>=`, `==`, `!=`
* **Logical:** `!`, `&&`, `||`
* **Assignment:** `=`, `+=`, `-=`, `*=`, `/=`
* **Unary:** `++`, `--`

### 5. Comments
* **Single-line:** Starts with `##`
* **Multi-line:** Enclosed between `#*` and `*#`

----

## Sample Programs

### Sample 1: Basic Structure
start
    declare Count = 0;
    ## This is a simple counter
    loop (Count < 10) {
        output("Value: ", Count);
        Count++;
    }
finish

### Sample 2: Functions & Conditionals
function Compute_max(A, B) {
    condition (A >= B) {
        return A;
    } else {
        return B;
    }
}

### Sample 3: Mathematical Operations & Multi-line Comments
start
    #* Calculating complex
       floating point operations
    *#
    declare Result = +3.14 * (1.5e10 / -0.123);
    declare Flag = true;
finish


### Sample 4: Lexical Errors
start
    ## Error 1: Invalid identifier (Starts with lowercase)
    declare count = 10;
    
    ## Error 2: Invalid characters (@ and $)
    declare Price$ = 50 @ 2;
    
    ## Error 3: Malformed literal (Multiple decimals)
    declare Num = 12.34.56;
    
    ## Error 4: Invalid identifier (Exceeds 31 characters)
    declare ThisIdentifierIsWayTooLongToBeValid = 100;
    
    ## Error 5: Malformed literal (Unterminated string)
    output("This string has no closing quote
finish

### Sample 5: Comments
start
    ## This is a standard single-line comment
    declare A = 10; ## Inline single-line comment
    
    #* This is a multi-line comment.
       It can span multiple lines.
       Keywords like start or loop inside here should be ignored.
       Operators like +-/* should also be ignored.
    *#
    declare B = 20;
    
    #* Error: Unclosed multi-line comment.
       This tests the specific error for comments that lack the closing tag.
       Because it never closes, the scanner should flag it as an error 
       when it reaches the EOF.
       
    declare C = 30;
finish