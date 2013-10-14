/*
 *	AudioPlayStream.java
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

import	java.io.*;
import	javax.sound.sampled.*;

// OutputStream that writes its data to the soundcard output
public class AudioPlayStream extends OutputStream implements LineListener {

	private AudioFormat m_format;
	private SourceDataLine m_line;
	private int m_bufferSize;
	private boolean immediateStop=false;
	private LineListener m_listener;

	public AudioPlayStream(AudioFormat format) {
		super();
		m_format=format;
		if (Debug.TRACE) {
			Debug.println(this, "<init> with format="+format);
		}
	}
	
	public void setListener(LineListener ll) {
		m_listener=ll;
	}

	public void update(LineEvent event) {
		if (m_listener!=null) {
			m_listener.update(event);
		}
		if (Debug.TRACE) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				Debug.println("Play: Stop");
			} else if (event.getType().equals(LineEvent.Type.START)) {
				Debug.println("Play: Start");
			} else if (event.getType().equals(LineEvent.Type.OPEN)) {
				Debug.println("Play: Open");
			} else if (event.getType().equals(LineEvent.Type.CLOSE)) {
				Debug.println("Play: Close");
			}
		}
	}

	// opens the sound hardware
	public void open() throws Exception {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, m_format);

		// get and open the target data line for capture.
		m_line = null;
		try {
			m_line = (SourceDataLine) AudioSystem.getLine(info);
			m_line.addLineListener(this);
			if (Debug.DEBUG) {
				Debug.println(this, "open(): Got SourceDataLine "+m_line.getClass());
			}
			m_bufferSize=(int) (m_format.getSampleRate()/4); // 125ms
			// align buffer size to integral frames
			m_bufferSize-=m_bufferSize % m_format.getFrameSize();
			m_line.open(m_format, m_bufferSize);
			if (Debug.DEBUG) {
				Debug.println(this, "open(): opened SourceDataLine ");
			}
			m_bufferSize=m_line.getBufferSize();
		} catch (LineUnavailableException ex) {
			throw new Exception("Unable to open the line: "+ex.getMessage());
		}
		immediateStop=false;
	}


	public void setImmediateStop(boolean value) {
		immediateStop=value;
	}

	public void start() throws Exception {
		m_line.flush();
		m_line.start();
		immediateStop=false;
	}

	public void close() throws IOException {
		close(immediateStop);
	}

	public void close(boolean immediately) throws IOException {
		if (m_line!=null) {
			if (Debug.TRACE) {
				Debug.println(this, "close(immediate="+immediately+")");
			}
			if (!immediately) {
				drain();
			} else {
				m_line.stop();
				flush();
			}
			if (m_line!=null) {
				m_line.close();
				m_line=null;
			}
		}
		m_bufferSize=0;
		immediateStop=false;
	}

	public void write(int b) throws IOException {
		byte[] hack=new byte[1];
		hack[0]=(byte) b;
		write(hack);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		if (m_line==null) {
			throw new IOException("Output line is closed.");
		}
		int res=m_line.write(b, off, len);
		if (Debug.TRACE_READWRITE) {
			Debug.println(this, "write: "+res+" bytes.");
		}
	}

	public void flush() throws IOException {
		if (m_line!=null) {
			if (Debug.TRACE) {
				Debug.println(this, "flush()");
			}
			immediateStop=true;
			m_line.flush();
			synchronized (this) {
				notifyAll(); // interrupt any drain going on
			}

		}
	}

	public synchronized void drain() {
		if (m_line!=null && m_line.isActive()) {
			long t=0;
			long waitTime=AMAudioFormat.bytes2Ms(m_bufferSize, m_format)+50;
			if (Debug.DEBUG || Debug.TRACE) {
				Debug.println(this, "drain() simulation ("+waitTime+"ms...");
				t=System.currentTimeMillis();
			}
			try {
				wait(waitTime);
			} catch (InterruptedException ie) {}

			if (Debug.TRACE) {
				Debug.println(this, "drain() exit: "+(System.currentTimeMillis()-t)+"ms");
			}
		}
	}

	public int getBufferSize() {
		return m_bufferSize;
	}
}

/*** AudioPlayStream.java ***/
