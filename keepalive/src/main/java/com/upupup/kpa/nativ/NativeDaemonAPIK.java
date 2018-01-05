package com.upupup.kpa.nativ;

import com.upupup.kpa.NativeDaemonBase;

import android.content.Context;

/**
 * native code to watch each other when api under 20 (contains 20)
 * @author renqingyou
 *
 */
public class NativeDaemonAPIK extends NativeDaemonBase {
	
	public NativeDaemonAPIK(Context context) {
		super(context);
	}

	static{
		try {
			System.loadLibrary("daemon_apik");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public native void doDaemon(String pkgName, String svcName, String daemonPath);
	
}
