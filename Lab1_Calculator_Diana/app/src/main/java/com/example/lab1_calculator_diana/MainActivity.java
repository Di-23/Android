package com.example.lab1_calculator_diana;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import org.mariuszgromada.math.mxparser.Expression;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText display;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.textView);
        display.setShowSoftInputOnFocus(false);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getString(R.string.display).equals(display.getText().toString())) {
                    display.setText("");
                }
            }
        });
    }

    private void updateText(String strToAdd) {
        String oldString = display.getText().toString();
        int cursorPosition = display.getSelectionStart();
        String leftStr = oldString.substring(0, cursorPosition);
        String rightStr = oldString.substring(cursorPosition);

        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(strToAdd);
        }
        else {
            display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
        }

        display.setSelection(cursorPosition + strToAdd.length());
    }

    public void clearBTN(View view) {
        display.setText("");
    }

    public void parenthesesBTN(View view) {
        int cursorPosition = display.getSelectionStart();
        int openPar = 0;
        int closePar = 0;
        int textLen = display.getText().length();

        for (int i = 0; i < cursorPosition; i++) {
            if (display.getText().toString().substring(i, i + 1).equals("(")) {
                openPar ++;
            }
            if (display.getText().toString().substring(i, i + 1).equals(")")) {
                closePar ++;
            }
        }

        if (openPar == closePar || display.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText("(");
        }
        else if (closePar < openPar && !display.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText(")");
        }

        display.setSelection(cursorPosition + 1);
    }

    public void exponentBTN(View view) {
        updateText("^");
    }

    public void divideBTN(View view) {
        updateText("÷");
    }

    public void sevenBTN(View view) {
        updateText("7");
    }

    public void eightBTN(View view) {
        updateText("8");
    }

    public void nineBTN(View view) {
        updateText("9");
    }

    public void multiplyBTN(View view) {
        updateText("×");
    }

    public void fourBTN(View view) {
        updateText("4");
    }

    public void fiveBTN(View view) {
        updateText("5");
    }

    public void sixBTN(View view) {
        updateText("6");
    }

    public void subtractBTN(View view) {
        updateText("-");
    }

    public void oneBTN(View view) {
        updateText("1");
    }

    public void twoBTN(View view) {
        updateText("2");
    }

    public void threeBTN(View view) {
        updateText("3");
    }

    public void addBTN(View view) {
        updateText("+");
    }

    public void zeroBTN(View view) {
        updateText("0");
    }

    public void pointBTN(View view) {
        updateText(".");
    }

    public void rootBTN(View view) {
        updateText("√(");
    }

    public void percentageBTN(View view) {
        updateText("%");
    }

    public void sinBTN(View view) {
        updateText("sin(");
    }

    public void cosBTN(View view) {
        updateText("cos(");
    }

    public void tgBTN(View view) {
        updateText("tg(");
    }

    public void ctgBTN(View view) {
        updateText("ctg(");
    }

    public void equalsBTN(View view) {
        String userExpression = display.getText().toString();

        userExpression = userExpression.replaceAll("÷", "/");
        userExpression = userExpression.replaceAll("×", "*");
        userExpression = userExpression.replaceAll("√", "sqrt");
        userExpression = userExpression.replaceAll("%", "/100");

        Expression exp = new Expression(userExpression);

        String result = String.valueOf(exp.calculate());

        display.setText(result);
        display.setSelection(result.length());
    }

    public void backspaceBTN(View view) {
        int cursorPosition = display.getSelectionStart();
        int textLen = display.getText().length();

        if (cursorPosition != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPosition - 1, cursorPosition, "");
            display.setText(selection);
            display.setSelection(cursorPosition - 1);
        }
    }
}