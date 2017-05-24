package com.eos.manager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;


import com.privacy.lock.R;
import com.eos.manager.meta.SecurityMyPref;
import com.eos.manager.page.LockPatternUtils;
import com.eos.manager.page.SecurityPatternView;
import com.eos.manager.page.NumberDot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by superjoy on 2014/10/24.
 */
public class AppLockSetPattern extends ClientActivitySecurity implements View.OnClickListener {
    public EditText email_address;
    CharSequence lastPasswd;
    public static final byte SET_EMPTY = 0;
    public static final byte SET_NORMAL_PASSWD = 1;
    public static final byte SET_GRAPH_PASSWD = 2;
    public static final byte SET_EMAIL = 3;
    boolean firsetShowf = false;

    @Override
    protected boolean hasHelp() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.RGBA_8888);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setupView() {
        byte setting = getIntent().getByteExtra("set", SET_EMPTY);
        switch (setting) {
            case SET_EMAIL:
                setEmail();
                break;

            case SET_NORMAL_PASSWD:
                setPasswdView();
                break;

            case SET_GRAPH_PASSWD:
                setGraphView();
                break;
        }
    }

    public void randomNumpadIfPossible() {
        if (!App.getSharedPreferences().getBoolean("random", false)) {
            return;
        }

        int[] buttons = new int[]{
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9
        };
        ArrayList<Integer> idx = new ArrayList<Integer>();
        for (int i = 0; i < 10; ++i)
            idx.add(i);

        for (int button : buttons) {
            int i = getRandomInt(0, idx.size() - 1);
            Integer v = idx.remove(i);
            ((Button) findViewById(button)).setText(v.toString());
        }
    }

    // 返回a到b之間(包括a,b)的任意一個自然数,如果a > b || liteA < 0，返回-1
    public static int getRandomInt(int a, int b) {
        if (a > b || a < 0)
            return -1;
        // 下面两种形式等价
        // return liteA + (int) (new Random().nextDouble() * (b - liteA + 1));
        return a + (int) (Math.random() * (b - a + 1));
    }

    public void setEmail() {
        startListApp();
    }


    NumberDot passdot;
    int size = 0;

    @Override
    public void onClick(View view) {
        Button v = (Button) view;
        passdot.setNumber(v.getText().charAt(0));
        if (setProgress == 0 && size < 6) {
            size++;
        }
        if (togglePattern) {
            togglePattern = false;
            Button ok = (Button) findViewById(R.id.ok);
            ok.setText(R.string.security_btn_next);
            ok.setTextColor(getResources().getColor(R.color.security_numpad_font_color));
//            ok.setBackgroundResource(R.drawable.button_bg);

        }
    }

    public void passwdIsEmpty() {
//
    }

    //
    public Drawable getIcon() {
        String packageName = getIntent().getStringExtra("pkg");
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return pi.applicationInfo.loadIcon(getPackageManager());
        } catch (Exception e) {
            return super.getResources().getDrawable(R.drawable.icon);
        }
    }


    public SecurityPatternView securityPatternView;
    public boolean confirmMode = false;

    Button cancel;

    TextView tip;

    public void enterGraphNormal() {
        confirmMode = false;
        tip.setTextColor(getResources().getColor(R.color.security_white_87));
        tip.setText(R.string.security_draw_pattern);
        tip.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        cancel.setText(R.string.security_use_normal);
        cancel.setTextColor(getResources().getColor(R.color.leadr_color));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPasswdView();
                Tracker.sendEvent(Tracker.CATE_SETTING, Tracker.ACT_LEADER_SETTINGPASS_PASSWORD, Tracker.ACT_LEADER_SETTINGPASS_PASSWORD, 1L);
            }
        });
    }

    public void enterConfirmMode() {
        confirmMode = true;
        tip.setText(R.string.security_draw_pattern_next);
        cancel.setVisibility(View.VISIBLE);
        cancel.setText(R.string.security_reset_passwd_2_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterGraphNormal();
            }
        });
    }

    void setGraphView() {
        if (SecurityMyPref.getFirstLeader()) {
            SecurityMyPref.setFirstLeader(false);
            Tracker.sendEvent(Tracker.ACT_LEADER, Tracker.ACT_LEADER_SETTINGPASS, Tracker.ACT_LEADER_SETTINGPASS, 1L);
        } else {
            Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_LEADER_SETTINGPASS, Tracker.ACT_LEADER_SETTINGPASS, 1L);

        }
        setContentView(R.layout.security_pattern_view_set);
        cancel = (Button) this.findViewById(R.id.number_cancel);
        tip = (TextView) this.findViewById(R.id.tip);
        View back = findViewById(R.id.back);
        if (firstSetup) {
            back.setVisibility(View.GONE);
            firsetShowf = true;
        }
        securityPatternView = (SecurityPatternView) findViewById(R.id.lpv_lock);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        securityPatternView.setOnPatternListener(new SecurityPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {
            }

            @Override
            public void onPatternCellAdded(List<SecurityPatternView.Cell> pattern) {

            }

            @Override
            public void onPatternDetected(List<SecurityPatternView.Cell> pattern) {
                if (confirmMode) {
                    if (!LockPatternUtils.checkPattern(pattern, pattern1)) {
                        securityPatternView.setDisplayMode(SecurityPatternView.DisplayMode.Wrong);
                        tip.setTextColor(0xffcc0000);
                        tip.setText(R.string.security_password_not_match);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                securityPatternView.clearPattern();
                                tip.setTextColor(getResources().getColor(R.color.security_body_text_1_inverse));
                                tip.setText(R.string.security_draw_pattern_next);
                            }
                        }, 700);
                    } else {
                        try {
                            SecurityMyPref.begin().setPasswd(LockPatternUtils.patternToString(pattern1), false).useNormalPasswd(false).commit();
                            Toast.makeText(context, R.string.security_password_setsuccessful, Toast.LENGTH_SHORT).show();
                            setResult(1);
                            SecurityMyPref.setClickOK(true);


                            if (firstSetup) {
//                                finish();
                                setEmail();
//
//                                Intent intent=new Intent(App.getContext(),AppLock.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
                                firstSetup = false;
                                int min = (int) (System.currentTimeMillis() / 1000 / 60);
                                SecurityMyPref.setMainFirstFullCountDown(min);
                            } else {
                                startListApp();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, R.string.security_set_password_successful, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (pattern.size() < 3) {
                        securityPatternView.setDisplayMode(SecurityPatternView.DisplayMode.Wrong);
                        tip.setTextColor(0xffcc0000);
                        tip.setText(R.string.security_pattern_short);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                securityPatternView.clearPattern();
                                tip.setTextColor(getResources().getColor(R.color.security_body_text_1_inverse));
                                tip.setText(R.string.security_draw_pattern_next);
                            }
                        }, 700);
                    } else {
                        if (pattern1 == null)
                            pattern1 = new ArrayList<>(pattern);
                        else {
                            pattern1.clear();
                            pattern1.addAll(pattern);
                        }
                        securityPatternView.clearPattern();
                        enterConfirmMode();
                    }
                }
            }
        });
        enterGraphNormal();
    }

    public boolean firstSetup = false;
    public boolean togglePattern = true;

    public void setPasswdView() {

        setContentView(R.layout.security_password_setting);
        View back = findViewById(R.id.back);
        if (firstSetup) {
            back.setVisibility(View.GONE);
            firsetShowf = true;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((ImageView) findViewById(R.id.backspace)).setColorFilter(getResources().getColor(R.color.security_numpad_color));
        passdot = (NumberDot) findViewById(R.id.passwd_dot_id);
        passdot.setFlag(true);
        passdot.init(new NumberDot.ICheckListener() {
            @Override
            public void match(String pass) {
                SecurityMyPref.begin().setPasswd(pass, true).useNormalPasswd(true).commit();
                setResult(1);
                Toast.makeText(context, R.string.security_password_setsuccessful, Toast.LENGTH_SHORT).show();
                SecurityMyPref.setClickOK(true);

                if (firstSetup) {
                    setEmail();
//                    finish();
//                    Intent intent=new Intent(App.getContext(),AppLock.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    firstSetup = false;
                    int min = (int) (System.currentTimeMillis() / 1000 / 60);
                    SecurityMyPref.setMainFirstFullCountDown(min);

                } else {
                    startListApp();

                }
            }
        });
        final Button okBtn = (Button) findViewById(R.id.ok);
        okBtn.setText(R.string.security_use_pattern);
        okBtn.setVisibility(View.VISIBLE);
        okBtn.setTextColor(getResources().getColor(R.color.leadr_color));
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (togglePattern) {
                    setGraphView();
                    Tracker.sendEvent(Tracker.CATE_SETTING, Tracker.ACT_LEADER_SETTINGPASS, Tracker.ACT_LEADER_SETTINGPASS, 1L);

                    return;
                }
                if (setProgress == 0) {
                    if (size < 6) {
                        size = 0;
                        passdot.reset();
                        Toast.makeText(AppLockSetPattern.this, R.string.security_password_short, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (passdot.empty()) {
                        passwdIsEmpty();
                        return;
                    }
                    ++setProgress;
                    passdot.setFlag(false);
                    passdot.reset();

//                    okBtn.setVisibility(View.INVISIBLE);
                    okBtn.setText(R.string.security_reset_passwd_2_btn);
                    okBtn.setTextColor(getResources().getColor(R.color.leadr_color));
                    ((TextView) findViewById(R.id.title)).setText(R.string.security_set_confirm_password);
                    ((TextView) findViewById(R.id.tip)).setText(R.string.security_confirm_passwd_tip);
                } else if (setProgress == 1) {
                    setProgress = 0;
                    passdot.setFlag(false);
                    passdot.reset();
                    togglePattern = true;
                    okBtn.setText(R.string.security_use_pattern);
                    ((TextView) findViewById(R.id.title)).setText(R.string.security_set_password);
                    ((TextView) findViewById(R.id.tip)).setText(R.string.security_set_passwd_tip);
                }
            }
        });
        findViewById(R.id.backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passdot.backSpace();
                try {
                    if (size > 0) {
                        size--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (passdot.empty() && !togglePattern) {
                    togglePattern = true;
                    okBtn.setText(R.string.security_use_pattern);
//                    okBtn.setBackgroundDrawable(null);
                    okBtn.setTextColor(getResources().getColor(R.color.leadr_color));
                }
            }
        });

        //ignore
        findViewById(R.id.number_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setProgress == 0) {
                    onBackPressed();
                } else {
                    --setProgress;
                    passdot.reset();
                    ((TextView) findViewById(R.id.title)).setText(R.string.security_set_password);
                    okBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (firsetShowf == true) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                finish();

            }

        }

        return super.onKeyDown(keyCode, event);
    }


    public void startListApp() {
        finish();
    }

    public List<SecurityPatternView.Cell> pattern1, pattern2;
    public byte setProgress = 0;
}
