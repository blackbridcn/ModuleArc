package org.pswdlevel;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断密码等级
 * 默认[密码为空]：0
 * 1级：密码中只有数字、小写字母、大写字母、特殊符号中的一种类型，或者任意两种的但是长度小于
 */
public class CheckPasswordStrongPersenter {

    // 密码等级
    public static final int LEVEL_DEF = 0;
    public static final int LEVEL_LOWER = 1;
    public static final int LEVEL_MIDDLE = 2;
    public static final int LEVEL_SENIOR = 3;
    public static final int LEVEL_TOP = 4;

    //正则匹配 Z = 数字 S = 小写字母  H = 大小字母 T = 特殊字符
    public static final String regexZ = "[0-9]+";
    public static final String regexS = "[a-z]+";
    public static final String regexH = "[A-Z]+";
    public static final String regexT = "[\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\{\\}\\|\\:\\\"\\<\\>\\?\\`\\-\\=\\[\\]\\\\\\;\\\'\\,\\.\\/]+";
    //密码正则 6-16位包含
    public static final String REGEX = "(?=^.{6,16}$)(((?=.*[0-9])(?=.*[a-zA-Z]))|((?=.*[0-9])(?=.*[\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\{\\}\\|\\:\\\"\\<\\>\\?\\`\\-\\=\\[\\]\\\\\\;\\\'\\,\\.\\/]))|((?=.*[a-zA-Z])(?=.*[\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\{\\}\\|\\:\\\"\\<\\>\\?\\`\\-\\=\\[\\]\\\\\\;\\\'\\,\\.\\/]))|((?=.*[a-z])(?=.*[A-Z])))(?!.*\\n)(?!.*\\r\\n)(?!.*[\\u4e00-\\u9fa5])(?!.*\\s)(?!.*[^A-Za-z0-9\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\{\\}\\|\\:\\\"\\<\\>\\?\\`\\-\\=\\[\\]\\\\\\;\\\'\\,\\.\\/]).*$";


    private Activity mActivity;
    private EditText editText;
    private PasswordStrongLevelView passwordStrongLevelView;

    private Handler myHandler = new Handler() {
        // 接收到消息后处理
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 调用方法
                    passwordStrongLevelView.setPasswordLevel((int) msg.obj);
                    break;
            }
        }
    };

    public CheckPasswordStrongPersenter(Activity mActivity, EditText editText, PasswordStrongLevelView passwordStrongLevelView) {
        this.mActivity = mActivity;
        this.editText = editText;
        this.passwordStrongLevelView = passwordStrongLevelView;
        pwdWatcherListener(editText);
    }

    /**
     * Edit添加监听
     *
     * @param pwdEditText
     */
    private void pwdWatcherListener(final EditText pwdEditText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwdText = pwdEditText.getText().toString().trim();
                judgePwdStrongLeave(pwdText);
            }
        });

    }

    /**
     * 判断密码强度
     *
     * @param pwdText
     */
    private void judgePwdStrongLeave(String pwdText) {
        switch (checkPasswordStrong(pwdText)) {
            case LEVEL_DEF:
                senMessageLevel(LEVEL_DEF);
                break;
            case LEVEL_LOWER:
                senMessageLevel(LEVEL_LOWER);
                break;
            case LEVEL_MIDDLE:
                senMessageLevel(LEVEL_MIDDLE);
                break;
            case LEVEL_SENIOR:
                senMessageLevel(LEVEL_SENIOR);
                break;
            case LEVEL_TOP:
                senMessageLevel(LEVEL_TOP);
                break;
        }
    }


    /**
     * 发送消息强度等级
     *
     * @param level
     */
    private void senMessageLevel(int level) {
        Message message = new Message();
        message.what = 1;
        message.obj = level;
        myHandler.sendMessage(message);
    }

    /**
     * 检查密码强度
     *
     * @param passwordStr
     * @return
     */
    private int checkPasswordStrong(String passwordStr) {
        int level = 0;
        int len = passwordStr.length();
        /*空为默认为最低等级*/
        if (TextUtils.isEmpty(passwordStr) || len == 0) {
            return LEVEL_DEF;
        }
        /*不符合正则返回默认值*/
        if (!checkPass(passwordStr)) {
            return LEVEL_LOWER;
        }
        if (stringFind(passwordStr, regexZ)) {
            ++level;
        }
        if (stringFind(passwordStr, regexS)) {
            ++level;
        }
        if (stringFind(passwordStr, regexH)) {
            ++level;
        }
        if (stringFind(passwordStr, regexT)) {
            ++level;
        }
        switch (level) {
            case LEVEL_DEF:
                return LEVEL_DEF;
            case LEVEL_LOWER:
                return LEVEL_LOWER;
            case LEVEL_MIDDLE:
                //满足大于6位任意两种组合密码强度为低或者满足两种组合（不能仅为字母）大于9位密码强度为中
                return len >= 9 && !isAcronym(passwordStr) ? LEVEL_SENIOR : LEVEL_MIDDLE;
            case LEVEL_SENIOR:
                //满足大于11位三种组合（其中必须包含字母、数字、特殊符号）强度为高或者满足小于11位任意三种组合强度为中
                if (len >= 11) {
                    return len >= 11 && stringFind(passwordStr, regexS) && stringFind(passwordStr, regexH) ? LEVEL_SENIOR : LEVEL_TOP;
                } else {
                    return LEVEL_SENIOR;
                }
            case LEVEL_TOP:
                //满足9位任意四种组合(大于9位强度为高或者小于9位密码强度为中)
                return len >= 9 ? LEVEL_TOP : LEVEL_SENIOR;
        }
        return LEVEL_DEF;

    }

    /**
     * 检查密码是否符合正则
     *
     * @param passwordStr 密码
     * @return
     */
    private boolean checkPass(String passwordStr) {
        Matcher matcher = Pattern.compile(REGEX).matcher(passwordStr);
        return matcher.matches();
    }

    /**
     * 搜索字符str中是否包含有regex指定的正则表达式匹配的子串
     *
     * @param str
     * @param regex 指定的正则表达式
     * @return 包含指定的正则为true ,不包括返回false;
     */
    private boolean stringFind(String str, String regex) {
        return Pattern.compile(regex).matcher(str).find();
    }

    /**
     * 判断是否包含大小写字母
     *
     * @param word
     * @return
     */
    public boolean isAcronym(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!Character.isLowerCase(c) && !Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }
}
