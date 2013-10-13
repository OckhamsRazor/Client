/*
 *	AudioCapture.java
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

import	sound.Debug;
import	java.io.*;
import	javax.sound.sampled.*;

// class that provides an AudioInputStream that reads its data from the soundcard input
public class AudioCapture implements LineListener {

	private AudioInputStream m_stream;
	private AudioFormat m_format;
	private int m_formatCode;
	private long m_startTime;

	private int bufferSizeDefault;
	private int bufferSize;
	private TargetDataLine m_line;

	public AudioCapture(int formatCode) {
		super();
		m_format = AMAudioFormat.getLineAudioFormat(formatCode);
		m_formatCode=formatCode;
	    bufferSizeDefault = (int) m_format.getSampleRate(); // 500ms
	}

	public void update(LineEvent event) {
		if (Debug.DEBUG) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				Debug.println("Record: Stop");
			} else if (event.getType().equals(LineEvent.Type.START)) {
				Debug.println("Record: Start");
			} else if (event.getType().equals(LineEvent.Type.OPEN)) {
				Debug.println("Record: Open");
			} else if (event.getType().equals(LineEvent.Type.CLOSE)) {
				Debug.println("Record: Close");
			}
		}
		if (event.getType().equals(LineEvent.Type.START)) {
	    	m_startTime=System.currentTimeMillis();
	    } else
	    if (event.getType().equals(LineEvent.Type.STOP) 
	    	|| event.getType().equals(LineEvent.Type.CLOSE)) {
		    m_startTime=0;
		}
	}

	// opens the sound hardware
	public void open() throws Exception {
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, m_format);

		// get and open the target data line for capture.
		m_line = null;
		try {
			m_line = (TargetDataLine) AudioSystem.getLine(info);
			m_line.addLineListener(this);
			if (Debug.DEBUG) {
				Debug.println("Got TargetDataLine "+m_line.getClass());
			}
			m_line.open(m_format, bufferSizeDefault);
		} catch (LineUnavailableException ex) {
			throw new Exception("Unable to open the line: "+ex.getMessage());
		}
		bufferSize = m_line.getBufferSize();
		if (Debug.TRACE) {
			Debug.println("Recording buffersize="+bufferSize+" bytes.");
		}
		
		//see below
		//m_stream = new AudioInputStream(m_line);
		m_stream = new AudioInputStream(new TargetDataLineIS(m_line), m_line.getFormat(), AudioSystem.NOT_SPECIFIED);
	}

	public AudioInputStream getAudioInputStream() {
		return m_stream;
	}

	public int getBufferSizeInBytes() {
		return bufferSize;
	}

	public int getFormatCode() {
		return m_formatCode;
	}

	public void start() throws Exception {
		m_line.flush();
		m_line.start();
	}

	public void close() {
		if (m_stream!=null) {
			if (Debug.TRACE) {
				Debug.println("AudioCapture.close(): begin");
			}
			try {
				m_stream.close();
                                m_line.stop();
                                m_line.close();
                                m_line=null;
				// hmmm... the next lines cause a deadlock...
				// but the above doesn't close the line !
				//if (m_line!=null) {
				//	m_line.close();
				//}
			}
			catch(IOException ioe) {}

			if (Debug.TRACE) {
				Debug.println("AudioCapture.close(): end");
			}
		}
	}

	public long getTime() {
		if (m_startTime!=0) {
			return System.currentTimeMillis()-m_startTime;
		} else {
			return -1;
		}
	}

	//////////////////////////////////////////////////////////////////////

	// Bug: the implementation of "new AudioInputStream(TargetDataLine)" does
	// not forward close() to the line !
	// so this is a blunt copy of Sun's implementation !
	private class TargetDataLineIS extends InputStream {
		TargetDataLine line;

		TargetDataLineIS(TargetDataLine line) {
			super();
			this.line = line;
		}

		public int available() throws IOException {
			return line.available();
		}

		public int read() throws IOException {
			byte[] b = new byte[1];

			int value = read (b, 0, 1);

			if (value == -1) {
				return -1;
			}
			return (int)b[0];
		}


		public int read(byte[] b, int off, int len) throws IOException {
			try {
				return line.read(b, off, len);
			} catch (IllegalArgumentException e) {
				throw new IOException(e.getMessage());
			}
		}

		public void close() throws IOException {
			// SunBug: strangely enough, the line needs to be flushed and
			// stopped to avoid a dead lock...
			if (line.isActive()) {
				line.flush();
				line.stop();
			}
			line.close();
		}
	} // TargetDataLineIS

}


/*** AudioCapture.java ***/

