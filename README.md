# Infix Expression Calculator

A sleek, feature-rich **JavaFX desktop application** that converts infix expressions to postfix notation and evaluates them using the **Shunting-yard algorithm** and **stack data structures**.

![Java](https://img.shields.io/badge/Java-100%25-blue)
![License](https://img.shields.io/badge/license-MIT-green)

---

## 🎯 Features

- **Infix → Postfix Conversion**: Uses the classic Shunting-yard algorithm to convert human-readable infix expressions to postfix (RPN)
- **Expression Evaluation**: Accurately evaluates postfix expressions using a stack-based approach
- **Operator Support**: Handles `+`, `-`, `*`, `/`, and `^` (exponentiation) with correct precedence
- **Parentheses Support**: Properly handles nested parentheses
- **Step-by-Step Trace**: Real-time visualization of the algorithm execution with token-by-token breakdown
- **Modern UI**: Dark-themed JavaFX interface with intuitive controls
- **Error Handling**: Comprehensive validation with user-friendly error messages
- **Keyboard Shortcut**: Press Enter to calculate

---

## 🧮 How It Works

### Algorithm Overview

The calculator uses two main algorithms:

#### 1. **Shunting-yard Algorithm** (Infix → Postfix)
Converts infix notation (e.g., `3 + 4 * 2`) to postfix notation (e.g., `3 4 2 * +`):
- Maintains an operator stack
- Follows operator precedence rules
- Handles parentheses by pausing operations
- Outputs tokens in postfix order

#### 2. **Postfix Evaluation**
Evaluates the postfix expression using a stack:
- Pushes operands onto the stack
- Pops two operands when encountering an operator
- Performs the operation and pushes the result back
- Returns the final value when complete

---

## 🚀 Getting Started

### Prerequisites
- **Java 11+** (tested with Java 17+)
- **JavaFX SDK** (11 or later)
- **IDE** (IntelliJ IDEA, Eclipse, or NetBeans)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YoussefShaher/Infix-Expression-Calculator.git
   cd Infix-Expression-Calculator
   ```

2. **Configure JavaFX in your IDE**
   - Download [JavaFX SDK](https://gluonhq.com/products/javafx/)
   - Add the JavaFX library to your project's classpath
   - Add VM options: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls`

3. **Run the application**
   ```bash
   javac com/example/data_structure/InfixCalculator.java
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls com.example.data_structure.InfixCalculator
   ```

---

## 💡 Usage

1. **Enter an infix expression** in the input field
   - Example: `3 + 4 * 6 / 2 ^ 3 - ( 1 + 5 )`
   - Spaces are optional

2. **Click "⚡ Calculate"** or press **Enter**

3. **View the results**:
   - **Postfix Expression**: The converted RPN form
   - **Result**: The calculated final value
   - **Step-by-step trace**: Detailed breakdown of each token processing

4. **Click "✕ Clear"** to reset all fields

---

## 📊 Example

### Input
```
3 + 4 * 6 / 2 ^ 3 - ( 1 + 5 )
```

### Output
```
Postfix: 3 4 6 * 2 3 ^ / + 1 5 + -
Result: -2.75
```

### Step-by-step Trace
```
Token                 Stack                          Output
─────────────────────────────────────────────────────────────
3                     []                             3
+                     [+]                            3
4                     [+]                            3 4
*                     [+, *]                         3 4
6                     [+, *]                         3 4 6
/                     [+, /]                         3 4 6 *
2                     [+, /]                         3 4 6 * 2
^                     [+, /, ^]                      3 4 6 * 2
3                     [+, /, ^]                      3 4 6 * 2 3
)                     [+]                            3 4 6 * 2 3 ^ /
-                     [-, (]                         3 4 6 * 2 3 ^ / 1
(                     [-]                            3 4 6 * 2 3 ^ / 1 5 +
1                     [-]                            (final result)
5
```

---

## 🏗️ Architecture

### Core Methods

| Method | Purpose |
|--------|---------|
| `toPostfix(String infix, StringBuilder trace)` | Converts infix to postfix using Shunting-yard algorithm |
| `evalPostfix(String postfix)` | Evaluates postfix expression and returns result |
| `tokenise(String expr)` | Splits input into tokens (numbers, operators, parentheses) |
| `isOperator(char c)` | Checks if character is an operator |
| `prec(char op)` | Returns operator precedence (1: +/-, 2: */÷, 3: ^) |
| `isNumber(String s)` | Validates if token is a number |

### UI Components

- **Header**: Title and description
- **Input Field**: Expression entry area
- **Buttons**: Calculate and Clear actions
- **Result Cards**: Display postfix and final result
- **Trace Area**: Step-by-step algorithm execution log
- **Error Display**: User-friendly error messages

---

## ⚠️ Error Handling

The calculator validates and handles:
- ✅ Empty expressions
- ✅ Mismatched parentheses
- ✅ Division by zero
- ✅ Invalid tokens
- ✅ Incomplete expressions

---

## 🎨 UI Design

- **Dark Theme**: Easy on the eyes
- **Color Scheme**: Purple accent (`#7c6af7`), red errors (`#e06c75`), green success (`#a6e3a1`)
- **Rounded Corners**: Modern, polished appearance
- **Hover Effects**: Interactive button feedback

---

## 📈 Operator Precedence

The calculator respects standard mathematical operator precedence:

| Operator | Precedence | Associativity |
|----------|-----------|---------------|
| `^` (Power) | 3 (Highest) | Right |
| `*`, `/` | 2 | Left |
| `+`, `-` | 1 (Lowest) | Left |

---

## 🧪 Testing

Try these test cases:

```
2 + 3          → 5
10 - 4         → 6
2 * 3 + 4      → 10
10 / 2 + 3     → 8
2 ^ 3          → 8
( 2 + 3 ) * 4  → 20
10 - 5 - 2     → 3
2 ^ 3 ^ 2      → 512 (right-associative)
```

---

## 📚 Learning Resources

- **Shunting-yard Algorithm**: [Wikipedia](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
- **Postfix Notation (RPN)**: [Khan Academy](https://www.khanacademy.org/)
- **JavaFX Documentation**: [Oracle Docs](https://openjfx.io/)
- **Stack Data Structure**: [GeeksforGeeks](https://www.geeksforgeeks.org/stack-data-structure/)

---

## 📝 License

This project is open source and available under the MIT License.

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest improvements
- Submit pull requests

---

## 👨‍💻 Author

**Youssef Shaher**  
GitHub: [@YoussefShaher](https://github.com/YoussefShaher)

---

## 🎓 Key Concepts Demonstrated

- **Shunting-yard Algorithm**: Converting infix to postfix
- **Stack Data Structure**: Core to both algorithms
- **Operator Precedence**: Mathematical evaluation rules
- **JavaFX GUI**: Modern desktop UI development
- **Exception Handling**: Robust error management
- **String Tokenization**: Input parsing

Enjoy calculating! 🚀
