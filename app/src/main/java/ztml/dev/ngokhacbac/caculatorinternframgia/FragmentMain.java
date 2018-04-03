package ztml.dev.ngokhacbac.caculatorinternframgia;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class FragmentMain extends Fragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    private TextView mResultTopText;
    private TextView mResultBottomText;
    private Button mZeroButton;
    private Button mOneButton;
    private Button mTwoButton;
    private Button mThreeButton;
    private Button mFourButton;
    private Button mFiveButton;
    private Button mSixButton;
    private Button mSevenButton;
    private Button mEightButton;
    private Button mNineButton;
    private Button mAcButton;
    private Button mHyphenButton;
    private Button mPercentButton;
    private Button mDivButton;
    private Button mMulButton;
    private Button mAddButton;
    private Button mSubButton;
    private Button mEqualButton;
    private Button mDotButton;
    private ImageButton mMenuImageButton;

    private String mShowDisplay = "";

    private SharedPreferences mSharedPreferences;
    public static final String PREF_NAME = "PREF_SAVE_LAST_RESULT";
    public static final String PREF_KEY_EXPRESSION = "PREF_EXPRESSION";
    public static final String PREF_KEY_RESULT = "PREF_RESULT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mResultTopText = view.findViewById(R.id.text_result_top);
        mResultBottomText = view.findViewById(R.id.text_result_bottom);
        mZeroButton = view.findViewById(R.id.button_0);
        mOneButton = view.findViewById(R.id.button_1);
        mTwoButton = view.findViewById(R.id.button_2);
        mThreeButton = view.findViewById(R.id.button_3);
        mFourButton = view.findViewById(R.id.button_4);
        mFiveButton = view.findViewById(R.id.button_5);
        mSixButton = view.findViewById(R.id.button_6);
        mSevenButton = view.findViewById(R.id.button_7);
        mEightButton = view.findViewById(R.id.button_8);
        mNineButton = view.findViewById(R.id.button_9);
        mAcButton = view.findViewById(R.id.button_ac);
        mHyphenButton = view.findViewById(R.id.button_hyphen);
        mPercentButton = view.findViewById(R.id.button_percent);
        mDivButton = view.findViewById(R.id.button_div);
        mMulButton = view.findViewById(R.id.button_multi);
        mSubButton = view.findViewById(R.id.button_sub);
        mAddButton = view.findViewById(R.id.button_add);
        mEqualButton = view.findViewById(R.id.button_equal);
        mDotButton = view.findViewById(R.id.button_dot);
        mMenuImageButton = view.findViewById(R.id.imagebutton_menu);

        mZeroButton.setOnClickListener(this);
        mOneButton.setOnClickListener(this);
        mTwoButton.setOnClickListener(this);
        mThreeButton.setOnClickListener(this);
        mFourButton.setOnClickListener(this);
        mFiveButton.setOnClickListener(this);
        mSixButton.setOnClickListener(this);
        mSevenButton.setOnClickListener(this);
        mEightButton.setOnClickListener(this);
        mNineButton.setOnClickListener(this);

        mAcButton.setOnClickListener(this);
        mHyphenButton.setOnClickListener(this);
        mPercentButton.setOnClickListener(this);
        mDivButton.setOnClickListener(this);
        mMulButton.setOnClickListener(this);
        mSubButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mEqualButton.setOnClickListener(this);
        mDotButton.setOnClickListener(this);
        mMenuImageButton.setOnClickListener(this);
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    private String calculatingResult(String postfix) {
        Stack<Float> s = new Stack<>();
        String number = "";
        for (int i = 0; i < postfix.length(); i++) {
            if (isOperator(postfix.charAt(i)) == false) {
                if (postfix.charAt(i) == ' ') {
                    if (number.length() != 0) {
                        float n = Float.parseFloat(number);
                        s.push(n);
                        number = "";
                    }
                } else {
                    number += String.valueOf(postfix.charAt(i));
                }
            } else {
                if (number.length() != 0) {
                    s.push(Float.parseFloat(number));
                    number = "";
                }
                float x, y;
                try {
                    x = s.pop();
                } catch (EmptyStackException e) {
                    x = 0;
                }
                try {
                    y = s.pop();
                } catch (EmptyStackException e) {
                    y = 0;
                }
                switch (postfix.charAt(i)) {
                    case '+':
                        y += x;
                        break;
                    case '-':
                        y -= x;
                        break;
                    case 'x':
                        y *= x;
                        break;
                    case '÷':
                        if (x == 0) {
                            mShowDisplay = "";
                            return getString(R.string.mess_infinity);
                        } else {
                            y /= x;
                            break;
                        }
                    case '%':
                        y = x / 100;
                        break;
                }
                s.push(y);
            }
        }
        return String.valueOf(s.pop());
    }

    private boolean isStringHasOperator(String result) {
        for (int i = 0; i < result.length(); i++)
            if (isOperator(result.charAt(i)) == true)
                return true;
        return false;

    }

    private String covertInfixToPostfix(String infix) {
        String infix_temp = "0" + infix;
        String postfix = "";
        Stack<Character> s = new Stack<>();
        for (int i = 0; i < infix_temp.length(); i++) {
            if (isOperator(infix_temp.charAt(i)) == true) {
                if (s.empty() == true) {
                    s.push(infix_temp.charAt(i));
                } else if (getPriority(s.peek()) >= getPriority(infix_temp.charAt(i))) {
                    postfix = postfix + s.pop();
                    s.push(infix_temp.charAt(i));
                } else s.push(infix_temp.charAt(i));
                postfix += " ";
            }
            if (isOperator(infix_temp.charAt(i)) == false) {
                postfix += infix_temp.charAt(i);
            }
        }
        while (!s.empty()) {
            postfix += s.pop();
        }
        return postfix;
    }

    private int getPriority(char c) {
        if (c == '+' || c == '-') return 1;
        else if (c == 'x' || c == '÷' || c == '%') return 2;
        else return 0;
    }

    private boolean isOperator(char c) {
        char operator[] = {'+', '-', 'x', ')', '(', '÷', '%'};
        Arrays.sort(operator);
        if (Arrays.binarySearch(operator, c) > -1)
            return true;
        else return false;
    }

    private void saveLastResult(String expression, String result) {
        mSharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREF_KEY_EXPRESSION, expression);
        editor.putString(PREF_KEY_RESULT, result);
        editor.commit();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mShowDisplay = "";
                mResultBottomText.setText(mShowDisplay);
                mResultTopText.setText(mShowDisplay);
                break;
            case R.id.menu_save_last_result:
                saveLastResult(mResultTopText.getText().toString(), mShowDisplay);
                break;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String expression = mSharedPreferences.getString(PREF_KEY_EXPRESSION, "");
        mShowDisplay = mSharedPreferences.getString(PREF_KEY_RESULT, "");
        mResultBottomText.setText(mShowDisplay);
        mResultTopText.setText(expression);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                mShowDisplay = mShowDisplay + getString(R.string.button_1);
                mResultBottomText.setText(mShowDisplay);
                //      displayResult();
                break;
            case R.id.button_2:
                mShowDisplay = mShowDisplay + getString(R.string.button_2);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_3:
                mShowDisplay = mShowDisplay + getString(R.string.button_3);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_4:
                mShowDisplay = mShowDisplay + getString(R.string.button_4);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_5:
                mShowDisplay = mShowDisplay + getString(R.string.button_5);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_6:
                mShowDisplay = mShowDisplay + getString(R.string.button_6);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_7:
                mShowDisplay = mShowDisplay + getString(R.string.button_7);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_8:
                mShowDisplay = mShowDisplay + getString(R.string.button_8);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_9:
                mShowDisplay = mShowDisplay + getString(R.string.button_9);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_0:
                mShowDisplay = mShowDisplay + getString(R.string.button_0);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_ac:
                mShowDisplay = "";
                mResultTopText.setText("");
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_hyphen:
                if (mShowDisplay.length() != 0) {
                    if (mShowDisplay.charAt(0) == '-') {
                        mShowDisplay = mShowDisplay.substring(1);
                    } else {
                        mShowDisplay = "-" + mShowDisplay;
                    }
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_percent:
                mShowDisplay = mShowDisplay + getString(R.string.button_percent);
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_div:
                if (mShowDisplay.length() == 0) {
                    mShowDisplay = getString(R.string.button_0) + getString(R.string.button_div);
                } else {
                    mShowDisplay = mShowDisplay + getString(R.string.button_div);
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_multi:
                if (mShowDisplay.length() == 0) {
                    mShowDisplay = getString(R.string.button_0) + getString(R.string.button_mul);
                } else {
                    mShowDisplay = mShowDisplay + getString(R.string.button_mul);
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_add:
                if (mShowDisplay.length() == 0) {
                    mShowDisplay = getString(R.string.button_0) + getString(R.string.button_add);
                } else {
                    mShowDisplay = mShowDisplay + getString(R.string.button_add);
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_sub:
                if (mShowDisplay.length() == 0) {
                    mShowDisplay = getString(R.string.button_0) + getString(R.string.button_sub);
                } else {
                    mShowDisplay = mShowDisplay + getString(R.string.button_sub);
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.button_equal:
                if (isStringHasOperator(mShowDisplay) == true) {
                    String result = calculatingResult(covertInfixToPostfix(mShowDisplay));
                    mResultTopText.setText(mShowDisplay);
                    mShowDisplay = result + "";
                    mResultBottomText.setText(mShowDisplay);
                    if (mShowDisplay.equals(getString(R.string.mess_infinity))) {
                        mShowDisplay = "";
                    }
                } else {
                    mResultTopText.setText("");
                    mResultBottomText.setText(mShowDisplay);
                }
                break;
            case R.id.button_dot:
                if (mShowDisplay.length() == 0) {
                    mShowDisplay = getString(R.string.button_0) + getString(R.string.button_dot);
                } else {
                    mShowDisplay = mShowDisplay + ".";
                }
                mResultBottomText.setText(mShowDisplay);
                break;
            case R.id.imagebutton_menu:
                showMenu(v);
                break;
        }
    }
}
