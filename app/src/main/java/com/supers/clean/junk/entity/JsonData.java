package com.supers.clean.junk.entity;


import java.util.List;

/**
 */

public class JsonData {

    public int full_main;
    public int full_start;
    public int full_exit;
    public int skip_time;
    public int full_manager;
    public int full_message;
    public int full_success;
    public int full_setting;
    public int full_unload;
    public int full_float;
    public int full_cool;
    public int full_shortcut;
    public int full_file;
    public int full_file_1;
    public int full_file_2;
    public int full_similar_photo;
    public int full_recyclebin;
    public int charging;
    public int inter_time;
    public int crossVersion;
    public String divide;
    public List<String> packageName;
    public List<CrossBean> cross;
    public List<CrossPromotionBean> crossPromotion;

    public static class CrossBean {
        /**
         * crossMark : list1
         * sequence : [1,2]
         */

        public String crossMark;
        public List<Integer> sequence;
    }

    public static class CrossPromotionBean {
        /**
         * mark : 1
         * crossType : zip
         * periodStart : 12:00
         * periodEnd : 18:00
         * click : 2
         * pkg : com.eosmobi.applock
         * label : Applock
         * url : com.eosmobi.applock&referrer=utm_source%3Deos_cleaner
         * appName : [{"lag":"en","content":"AppLock"},{"lag":"es","content":"AppLock"},{"lag":"pt","content":"AppLock"}]
         * iconUrl : https://68.media.tumblr.com/4260e0635c59261140e5581db0e5ffaf/tumblr_oij3kow4p31sxao59o1_250.png
         * topPicUrl : https://firebasestorage.googleapis.com/v0/b/theme-8ce26.appspot.com/o/APPLOCK.zip?alt=media&token=663095ec-c5d7-40fc-8e83-e8efb4f3d028
         * content : [{"lag":"en","content":["#1 Top Ranked Launcher","Small, Smart & Simple","Beautiful Wallpapers & Themes"]},{"lag":"es","content":["# 1 Lanzador mejor clasificado","Peque帽o, inteligente y simple","Fondos y temas hermosos"]},{"lag":"pt","content":["# 1 Lan莽ador com classifica莽茫o superior","Pequeno, inteligente e simples","Pap茅is de parede & temas bonitos"]}]
         */

        public int mark;
        public String crossType;
        public String periodStart;
        public String periodEnd;
        public String click;
        public String pkg;
        public String label;
        public String url;
        public String iconUrl;
        public String topPicUrl;
        public List<AppNameBean> appName;
        public List<ContentBean> content;

        public static class AppNameBean {
            /**
             * lag : en
             * content : AppLock
             */

            public String lag;
            public String content;
        }

        public static class ContentBean {
            /**
             * lag : en
             * content : ["#1 Top Ranked Launcher","Small, Smart & Simple","Beautiful Wallpapers & Themes"]
             */

            public String lag;
            public List<String> content;
        }
    }

    //0 native 1 全屏

//    public int full_main;
//    public int full_manager;
//    public int full_message;
//    public int full_success;
//    public int full_setting;
//    public int full_unload;
//    public int full_float;
//    public int full_cool;
//    public int full_shortcut;
//    public int full_file;
//    public int full_file_1;
//    public int full_file_2;
//    public int full_similar_photo;
//    public int full_recyclebin;
//    public int inter_time;
//    public int charging;
//    public List<Integer> tuiguang;
//    public List<String> packageName;


}
