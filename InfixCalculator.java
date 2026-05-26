package com.example.data_structure;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Stack;

public class InfixCalculator extends Application {

    // ─── colours ──────────────────────────────────────────────────────────────
    private static final String BG       = "#1e1e2e";
    private static final String CARD     = "#2a2a3e";
    private static final String ACCENT   = "#7c6af7";
    private static final String ACCENT2  = "#e06c75";
    private static final String TEXT     = "#cdd6f4";
    private static final String MUTED    = "#6c7086";
    private static final String SUCCESS  = "#a6e3a1";

    @Override
    public void start(Stage stage) {

        // ── title bar ──────────────────────────────────────────────────────────
        Label title = label("Infix Calculator", 22, FontWeight.BOLD, TEXT);
        Label sub   = label("Converts infix → postfix, then evaluates", 12, FontWeight.NORMAL, MUTED);

        VBox header = new VBox(4, title, sub);
        header.setAlignment(Pos.CENTER);

        // ── input ──────────────────────────────────────────────────────────────
        Label inLbl = label("Enter an infix expression:", 13, FontWeight.SEMI_BOLD, TEXT);

        TextField inputField = new TextField(); // gives a text bar
        inputField.setPromptText("e.g.  3 + 4 * 6 / 2 ^ 3 - ( 1 + 5 )"); // what's inside the code
        styleInput(inputField); // what it's going to get with it's style

        // ── buttons ────────────────────────────────────────────────────────────
        Button calcBtn  = styledButton("⚡  Calculate", ACCENT,  "#ffffff"); // types of buttons
        Button clearBtn = styledButton("✕  Clear",      "#3a3a4e", MUTED); // buttons and codes

        HBox buttons = new HBox(10, calcBtn, clearBtn); // horizontal box to place them next to eachother
        buttons.setAlignment(Pos.CENTER_LEFT);

        // ── results ────────────────────────────────────────────────────────────
        Label postfixLbl  = label("Postfix Expression", 11, FontWeight.SEMI_BOLD, MUTED); // the answer of the infix
        Label postfixOut  = label("—", 16, FontWeight.BOLD, ACCENT); // if it dosn't have an answer

        Label resultLbl   = label("Result", 11, FontWeight.SEMI_BOLD, MUTED); // same
        Label resultOut   = label("—", 28, FontWeight.BOLD, SUCCESS);

        Label stepsTitle  = label("Step-by-step trace", 11, FontWeight.SEMI_BOLD, MUTED); // the label is used to display
        TextArea stepsArea = new TextArea();
        stepsArea.setEditable(false);
        stepsArea.setPrefRowCount(8);
        styleTextArea(stepsArea);

        Label errorOut = label("", 12, FontWeight.NORMAL, ACCENT2);
        errorOut.setWrapText(true);

        // ── card layout ────────────────────────────────────────────────────────
        VBox postfixCard = card("📤  Postfix", postfixLbl, postfixOut);
        VBox resultCard  = card("🎯  Result",  resultLbl,  resultOut);

        HBox resultRow = new HBox(12, postfixCard, resultCard);
        HBox.setHgrow(postfixCard, Priority.ALWAYS);
        HBox.setHgrow(resultCard,  Priority.ALWAYS);

        // ── main container ─────────────────────────────────────────────────────
        VBox root = new VBox(18,
                header,
                separator(),
                inLbl, inputField,
                buttons,
                errorOut,
                resultRow,
                stepsTitle, stepsArea
        );
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color:" + BG + ";");
        root.setPrefWidth(520);

        // ── logic ──────────────────────────────────────────────────────────────
        calcBtn.setOnAction(e -> {
            errorOut.setText("");
            String raw = inputField.getText().trim();
            if (raw.isEmpty()) { errorOut.setText("⚠  Please enter an expression first."); return; }
            try {
                StringBuilder trace = new StringBuilder();
                String postfix = toPostfix(raw, trace);
                double result  = evalPostfix(postfix);

                postfixOut.setText(postfix);
                resultOut.setText(isWhole(result) ? String.valueOf((long) result)
                        : String.format("%.4f", result));
                stepsArea.setText(trace.toString());
            } catch (Exception ex) {
                errorOut.setText("⚠  " + ex.getMessage());
                postfixOut.setText("—");
                resultOut.setText("—");
                stepsArea.clear();
            }
        });

        clearBtn.setOnAction(e -> {
            inputField.clear();
            postfixOut.setText("—");
            resultOut.setText("—");
            stepsArea.clear();
            errorOut.setText("");
        });

        // ── enter key shortcut ─────────────────────────────────────────────────
        inputField.setOnAction(e -> calcBtn.fire());

        stage.setScene(new Scene(root));
        stage.setTitle("Infix Calculator");
        stage.setResizable(false);
        stage.show();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ALGORITHM 1 – Infix  ➜  Postfix  (Shunting-yard)
    // ══════════════════════════════════════════════════════════════════════════
    private String toPostfix(String infix, StringBuilder trace) {
        StringBuilder output = new StringBuilder();
        Stack<Character> ops  = new Stack<>();
        String[]         tokens = tokenise(infix);

        trace.append(String.format("%-20s %-30s %s%n", "Token", "Stack", "Output"));
        trace.append("─".repeat(70)).append("\n");

        for (String tok : tokens) {
            if (tok.isEmpty()) continue;

            if (isNumber(tok)) {                         // operand
                output.append(tok).append(" ");
            } else if (tok.equals("(")) {
                ops.push('(');
            } else if (tok.equals(")")) {
                while (!ops.isEmpty() && ops.peek() != '(')
                    output.append(ops.pop()).append(" ");
                if (ops.isEmpty()) throw new IllegalArgumentException("Mismatched parentheses.");
                ops.pop();                               // discard '('
            } else if (isOperator(tok.charAt(0))) {
                while (!ops.isEmpty() && ops.peek() != '('
                        && prec(ops.peek()) >= prec(tok.charAt(0)))
                    output.append(ops.pop()).append(" ");
                ops.push(tok.charAt(0));
            } else {
                throw new IllegalArgumentException("Unknown token: " + tok);
            }

            trace.append(String.format("%-20s %-30s %s%n",
                    tok, ops.toString(), output.toString().trim()));
        }

        while (!ops.isEmpty()) {
            char op = ops.pop();
            if (op == '(' || op == ')') throw new IllegalArgumentException("Mismatched parentheses.");
            output.append(op).append(" ");
        }

        trace.append("─".repeat(70)).append("\n");
        trace.append("Postfix: ").append(output.toString().trim());
        return output.toString().trim();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ALGORITHM 2 – Evaluate Postfix
    // ══════════════════════════════════════════════════════════════════════════
    private double evalPostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        for (String tok : postfix.split("\\s+")) {
            if (tok.isEmpty()) continue;
            if (isNumber(tok)) {
                stack.push(Double.parseDouble(tok));
            } else if (isOperator(tok.charAt(0)) && tok.length() == 1) {
                if (stack.size() < 2) throw new IllegalArgumentException("Invalid expression.");
                double b = stack.pop(), a = stack.pop();
                switch (tok.charAt(0)) {
                    case '+' -> stack.push(a + b);
                    case '-' -> stack.push(a - b);
                    case '*' -> stack.push(a * b);
                    case '/' -> { if (b == 0) throw new ArithmeticException("Division by zero."); stack.push(a / b); }
                    case '^' -> stack.push(Math.pow(a, b));
                }
            }
        }
        if (stack.size() != 1) throw new IllegalArgumentException("Invalid expression.");
        return stack.pop();
    }

    // ── helpers ────────────────────────────────────────────────────────────────
    private int    prec(char op)      { return switch(op){case '+','-'->1; case '*','/'->2; case '^'->3; default->0;}; }
    private boolean isOperator(char c){ return "+-*/^".indexOf(c) >= 0; }
    private boolean isNumber(String s){ try{ Double.parseDouble(s); return true; }catch(Exception e){ return false; } }
    private boolean isWhole(double d) { return d == Math.floor(d) && !Double.isInfinite(d); }

    private String[] tokenise(String expr) {
        // insert spaces around operators/parens so we can split cleanly
        return expr.replaceAll("([+\\-*/^()])", " $1 ").trim().split("\\s+");
    }

    // ── style helpers ──────────────────────────────────────────────────────────
    private Label label(String t, int size, FontWeight w, String color) {
        Label l = new Label(t);
        l.setFont(Font.font("System", w, size));
        l.setTextFill(Color.web(color));
        return l;
    }

    private void styleInput(TextField f) {
        f.setStyle("-fx-background-color:" + CARD + ";"
                + "-fx-text-fill:" + TEXT + ";"
                + "-fx-prompt-text-fill:" + MUTED + ";"
                + "-fx-border-color:" + ACCENT + ";"
                + "-fx-border-radius:8;"
                + "-fx-background-radius:8;"
                + "-fx-padding:10 14;"
                + "-fx-font-size:14;");
    }

    private void styleTextArea(TextArea a) {
        a.setStyle("-fx-control-inner-background:" + CARD + ";"
                + "-fx-text-fill:" + TEXT + ";"
                + "-fx-border-color:" + MUTED + ";"
                + "-fx-border-radius:8;"
                + "-fx-background-radius:8;"
                + "-fx-font-family:monospace;"
                + "-fx-font-size:12;");
    }

    private Button styledButton(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setFont(Font.font("System", FontWeight.SEMI_BOLD, 13));
        b.setStyle("-fx-background-color:" + bg + ";"
                + "-fx-text-fill:" + fg + ";"
                + "-fx-background-radius:8;"
                + "-fx-padding:9 20;"
                + "-fx-cursor:hand;");
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited (e -> b.setOpacity(1.0));
        return b;
    }

    private VBox card(String heading, Label sublabel, Label value) {
        Label h = label(heading, 11, FontWeight.SEMI_BOLD, MUTED);
        VBox box = new VBox(6, h, sublabel, value);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color:" + CARD + ";"
                + "-fx-background-radius:10;"
                + "-fx-border-color:" + MUTED + "33;"
                + "-fx-border-radius:10;");
        return box;
    }

    private Separator separator() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color:" + MUTED + "44;");
        return s;
    }

    public static void main(String[] args) { launch(args); }
}
