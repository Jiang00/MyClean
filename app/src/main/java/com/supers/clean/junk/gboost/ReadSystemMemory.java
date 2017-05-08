//package com.supers.clean.junk.gboost;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import android.app.ActivityManager;
//import android.app.ActivityManager.MemoryInfo;
//import android.content.Context;
//import android.text.format.Formatter;
//import android.util.Log;
//import android.view.Window;
//import android.widget.TextView;
//public class ReadSystemMemory extends Activity {
//	public static final int REFRESH = 0x000001;
//    public TextView tv = null;
//    long total = 0;
//    long idle = 0;
//    double usage = 0;
//
//    public void CPULoad( )
//    {
//        readUsage( );
//    }
//
//   public void readUsage( )
//    {
//        try
//        {
//            BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( "/proc/stat" ) ), 1000 );
//            String load = reader.readLine();
//            reader.close();
//
//            String[] toks = load.split(" ");
//
//            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
//            long currIdle = Long.parseLong(toks[5]);
//
//            this.usage =(currTotal - total) * 100.0f / (currTotal - total + currIdle - idle);
//            this.total = currTotal;
//            this.idle = currIdle;
//        }
//        catch( IOException ex )
//        {
//            ex.printStackTrace();
//        }
//
//    }
//
//  public    Handler  mHandler = new Handler() {
//
//       @Override
//       public void handleMessage(Message msg) {
//    	   tv = (TextView) findViewById(R.id.system_memory);
//       	if (msg.what == ReadSystemMemory.REFRESH) {
//       		if(tv!=null)
//       			tv.setText("mTotle: " + this.getTotalMemory() + ", " + "midle: "
//
//       				 + this.getAvailMemory()+","+"cusage:"+ Math.round(this.getUsage())+"%");
//
//	         }
//       }
//
//       public double getUsage( )
//       {
//           readUsage( );
//           return usage;
//       }
//
//	public String getAvailMemory() {
//		 ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//
//	       MemoryInfo mi = new MemoryInfo();
//
//	       am.getMemoryInfo(mi);
//
//	       //mi.availMem; ��ǰϵͳ�Ŀ����ڴ�
//
//	       return Formatter.formatFileSize(getBaseContext(), mi.availMem);// ����ȡ���ڴ��С���
//
//	}
//
//	public String getTotalMemory() {
//		 String str1 = "/proc/meminfo";// ϵͳ�ڴ���Ϣ�ļ�
//
//	       String str2;
//
//	       String[] arrayOfString;
//
//	       long initial_memory = 0;
//
//	       try {
//
//	           FileReader localFileReader = new FileReader(str1);
//
//	           BufferedReader localBufferedReader = new BufferedReader(
//
//	                  localFileReader, 8192);
//
//	           str2 = localBufferedReader.readLine();// ��ȡmeminfo��һ�У�ϵͳ���ڴ��С
//
//	           arrayOfString = str2.split("\\s+");
//
//	           for (String num : arrayOfString) {
//
//	              Log.i(str2, num + "\t");
//
//	           }
//
//	          initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// ���ϵͳ���ڴ棬��λ��KB������1024ת��ΪByte
//
//	           localBufferedReader.close();
//
//	       } catch (IOException e) {
//
//	       }
//
//	       return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byteת��ΪKB����MB���ڴ��С���
//
//	}
//};
//
//
//	@Override
//
//    public void onCreate(Bundle savedInstanceState) {
//
//       super.onCreate(savedInstanceState);
//       this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//
//       setContentView(R.layout.main);
//       getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main);
//
//
//       tv = (TextView) findViewById(R.id.system_memory);
//
//
//       new MyThread().start();
//
//
//
//
//
//    }
//	public class MyThread extends Thread {
//        public void run() {
//                while (true) {
//
//                        Message msg = new Message();
//                        msg.what = REFRESH;
//                        msg.obj = this;
//                        mHandler.sendMessage(msg);
//                        try {
//                            Thread.sleep(1000);
//                       } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                       }
//                        }
//                }
//        }
//}
//
//
//
//
//
