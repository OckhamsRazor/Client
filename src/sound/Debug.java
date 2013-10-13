/*
 *	Debug.java
 */

/*
 * Copyright (c) 2001 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package sound;

public class Debug {

	public static boolean ERROR=true;
	public static boolean DEBUG=true;
	public static boolean TRACE=false;
	public static boolean TRACE_READWRITE=false;
	public static boolean TRACE_INOUT=false;
	public static boolean SHOW_ALL_EXCEPTIONS=false;

	// for net.Server
	public static boolean SLOW_NET_UPLOAD=false;
	public static boolean SLOW_NET_DOWNLOAD=false;

	// show the time of a debug message
	private static final boolean SHOW_TIMES=false;
	private static long START_TIME=System.currentTimeMillis();

	public static synchronized void println(String sMessage) {
		if (SHOW_TIMES) {
			sMessage=""+(System.currentTimeMillis()-START_TIME)+": "+sMessage;
		}
		System.out.println(sMessage);
	}

	public static void println(Object obj, String sMessage) {
		String cn=obj.getClass().getName();
		int i=cn.lastIndexOf('.');
		if (i>=0 && i<cn.length()-1) {
			cn=cn.substring(i+1);
		}
		println(cn+": "+sMessage);
	}

	public static synchronized void println(Throwable t) {
		t.printStackTrace();
	}

	public static synchronized void printStackTrace() {
		Thread.dumpStack();
	}
}

/*** Debug.java ***/
