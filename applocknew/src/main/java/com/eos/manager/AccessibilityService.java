package com.eos.manager;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.client.AndroidSdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by song on 16/4/6.
 */
public class AccessibilityService extends android.accessibilityservice.AccessibilityService {

    private static final CharSequence PACKAGE = "com.android.settings";
    private static final CharSequence NAME_APP_DETAILS = "com.android.settings.applications.InstalledAppDetailsTop";
    private static final CharSequence NAME_ALERT_DIALOG = "Dialog";
    private String[] force_stop;
    private String[] delete;

    private boolean isAppDetail;
    private boolean isDis = true;
    private boolean clickKillProcess = false;

    private static String model = Build.MODEL;
    private Locale locale;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (null == event || null == event.getSource()) {
            return;
        }
        if (isDis) {
            return;
        }
        Locale localeF = getResources().getConfiguration().locale;
        String language = localeF.getLanguage();
        if (language != locale.getLanguage()) {
            locale = localeF;
            languageYtpe(language);
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && TextUtils.equals(PACKAGE, event.getPackageName())) {
            final CharSequence className = event.getClassName();

            if ((className.toString().endsWith("InstalledAppDetailsTop")) || ("com.android.settings.applications.InstalledAppDetailsActivity".equals(className.toString()))) {
                if (!clickKillProcess) {
                    if (!simulationClick(event, force_stop)) {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                } else {
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    clickKillProcess = false;
                }
                isAppDetail = true;
            } else if (isAppDetail && className.toString().contains(NAME_ALERT_DIALOG)) {
                simulationClick(event, delete);
                clickKillProcess = true;
                performGlobalAction(GLOBAL_ACTION_BACK);
                isAppDetail = false;
            }
        }

        if (event.getPackageName() != null) {
            String packageName = event.getPackageName().toString();
            if (!packageName.equals(getPackageName())) {
                AppLockEosService.startLock(this, packageName);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean simulationClick(AccessibilityEvent event, String[] text) {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            nodeInfoList.addAll(event.getSource().findAccessibilityNodeInfosByText(text[i]));
            Log.e("rqy", "--language--text=" + text[i]);
        }
        Log.e("rqy", "--language--text=" + text.toString() + "--nodeInfoList.size=" + nodeInfoList.size());
        for (AccessibilityNodeInfo node : nodeInfoList) {
            if (node.isClickable() && node.isEnabled()) {
                Log.e("rqy", "--simulationClick--" + node.getText() + node.performAction(AccessibilityNodeInfo.ACTION_CLICK));
                return true;
            }
        }
        AndroidSdk.track("强度清理", "清理失败:" + model, "", 1);
        return false;
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        languageYtpe(language);
    }

    private void languageYtpe(String language) {
        if (language.equals("en")) {
            force_stop = new String[]{"FORCE STOP", "STOP", "Force stop"};
            delete = new String[]{"OK", "ok", "yes", "ALLOW", "FORCE STOP"};
        } else if (language.equals("ar")) {
            force_stop = new String[]{"فرض الإيقاف", "إيقاف قسري", "إيقاف إجباري", "‏ايقاف اجباري"};
            delete = new String[]{"OK", "ok", "yes", "ALLOW", "FORCE STOP"};
        } else if (language.equals("de")) {
            force_stop = new String[]{"Beenden erzwingen", "Stopp erzwingen", "Stoppen erzwingen", "STOPP ERZWINGEN"};
            delete = new String[]{"Beenden erzwingen", "ZULASSEN"};
        } else if (language.equals("es")) {
            force_stop = new String[]{"Forzar detención", "Termina", "Forzar cierre", "Forzar la detención", "Almacenamiento", "Provocar la detención"};
            delete = new String[]{"Forzar detención", "Sí", "Aceptar", "Termina", "Ha", "PERMITIR"};
        } else if (language.equals("fr")) {
            force_stop = new String[]{"Forcer l'arrêt"};
            delete = new String[]{"Oui", "Forcer l'arrêt", "AUTORISE", "CONSENTI"};
        } else if (language.equals("hi")) {
            force_stop = new String[]{"बलपूर्वक रोकें"};
            delete = new String[]{"ठीक छ", "हो", "हाँ"};
        } else if (language.equals("in")) {
            force_stop = new String[]{"Paksa berhenti"};
            delete = new String[]{"Oke", "CONSENTI"};
        } else if (language.equals("it")) {
            force_stop = new String[]{"Arresto forzato"};
            delete = new String[]{"Si", "ok"};
        } else if (language.equals("ja")) {
            force_stop = new String[]{"強制停止"};
            delete = new String[]{"はい"};
        } else if (language.equals("ko")) {
            force_stop = new String[]{"강제 중지", "강제 종료"};
            delete = new String[]{"예"};
        } else if (language.equals("pt")) {
            force_stop = new String[]{"Forçar interrupção", "Forçar parada", "Forçar paragem"};
            delete = new String[]{"Forçar parada", "Ano", "PERMITIR"};
        } else if (language.equals("ru")) {
            force_stop = new String[]{"Остановить", "Закрыть", "Остановить принудительно"};
            delete = new String[]{"Так", "PA3PEШИТЬ", "ОК", "Иә"};
        } else if (language.equals("th")) {
            force_stop = new String[]{"บังคับ​หยุดการใช้งาน", "บังคับให้หยุด", "บังคับหยุดการใช้งาน"};
            delete = new String[]{"ใช่"};
        } else if (language.equals("tr")) {
            force_stop = new String[]{"Durmaya zorla"};
            delete = new String[]{"Tamam", "Zorla durdur", "Oldu", "Evet", "Durmaya zorla", "İZİN VER"};
        } else if (language.equals("vi")) {
            force_stop = new String[]{"Buộc dừng"};
            delete = new String[]{"Có"};
        } else if (language.equals("zh")) {
            force_stop = new String[]{"结束运行", "强行停止", "强制停止", "強制停止", "強行停止", "強制終了", "結束操作"};
            delete = new String[]{"是", "确定", "结束运行", "强行停止", "強制停止", "强制停止", "允許", "允许", "許可", "確定"};
        } else {
            force_stop = new String[]{"FORCE STOP", "Force stop", "Məcburi dayanma", "Forçar aturada", "Vynucené zastavení",
                    "Gennemtving stop", "Sunnitud peatamine", "Behartu etetera", "Fórsáil stad", "Prisilno zaustavi",
                    "Þvinga stöðvun", "Piespiedu apturēšana", "Priverst. sustabdyti", "Kényszerleállítás", "Gedwongen stoppen",
                    "Tving stopp", "Majburiy to'xtashish", "Wymuś zatrzymanie", "Oprire forţată", "Vynútiť zastavenie", "Vsili ustavitev",
                    "Prinudno zaustavi", "Pakota lopetus", "Tvinga stopp", "Επιβολή διακοπής", "Принудит. спиране",
                    "Күшпен тоқтату", "Принудно запирање", "Примусово закрити",
                    "გაჩერება", "Հարկադիր կանգ", "Dwing stop", "Henti paksa", "Força l'aturada", "Vynutit ukončení",
                    "Tving til at standse", "Sunni peatuma", "Sapilitang pagtigil", "Phoqelela ukuma", "Komesha kwa lazima",
                    "Forsēt apturēšanu", "Sustabdyti", "Kényszerített leállítás", "Nu stoppen", "Opriți forțat", "Prisilna ustavitev",
                    "Tvingad avslutning", "Αναγκαστική διακοπή", "Принудително спиране",
                    "Хүчээр зогсоох", "Принудно заустави", "Зупинити", "ძალით შეჩერება",
                    "Ստիպողաբար դադարեցնել", "אלץ עצירה", "توقف اجباری", "በኃይል ማቆም", "បង្ខំ​ឲ្យ​បញ្ឈប", "ບັງ​ຄັບ​ປິດ", "Forţare oprire",
                    "Paksa henti", "Υποχρεωτική διακοπή", "‏כפה עצירה", "Forzar cierre", "PRISILNO ZAUSTAVI",
                    "Buộc dừng", "บังคับให้หยุด", "FORCER L'ARRÊT", "فرض الإيقاف", "Paksa berhenti", "強制停止", "Forçar parada",
                    "Forzar detención", "Остановить", "Termina", "强行停止", "강제 중지", "إيقاف إجباري", "Durmaya zorla"};
            delete = new String[]{"ok", "yes", "ALLOW", "FORCE STOP", "হ্যাঁ", "Ναι", "Kyllä", "હા", "አዎ", "კარგი", "დიახ", "Так",
                    "យល់ព្រម", "ಹೌದು", "Jā", "ਹਾਂ", "ඔව්", "ΘĶ", "Ndiyo", "Во ред", "ஆம்", "Áno", "Да",
                    "Այո", "Ja", "Ya", "Si", "Sim", "Bəli", "Da", "Bai", "Taip", "Igen", "Tak", "Onartu", "موافق", "‏بەڵێ",
                    "‏بله", "ОК"};
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            isDis = intent.getBooleanExtra("isDis", true);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AppLockEosService.stopWorking(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        AppLockEosService.startWorking(this);
        return super.onUnbind(intent);
    }
}
