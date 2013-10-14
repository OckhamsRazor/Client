/*
 *	AMAudioFormat.java
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

//TODO: enable dynamic change of audio format - especially for owner client...

package sound;

import	javax.sound.sampled.*;

public class AMAudioFormat {

	public static final int FORMAT_CODE_CD=1;
	public static final int FORMAT_CODE_FM=2;
	public static final int FORMAT_CODE_TELEPHONE=3;
	public static final int FORMAT_CODE_GSM=4;
	
	public static final String[] FORMAT_NAMES={
		"Cell phone GSM (13.2KBit/s - Modem)",
		"Telephone ulaw (64KBit/s - ISDN)",
		"FM quality mono (352.8KBit/s - ADSL)",
		"CD quality mono (705.6KBit/s - LAN)"
	};

	public static final int[] FORMAT_CODES={
		FORMAT_CODE_GSM,
		FORMAT_CODE_TELEPHONE,
		FORMAT_CODE_FM,
		FORMAT_CODE_CD
	};

	public static final int FORMAT_CODE_DEFAULT=FORMAT_CODE_GSM;

	private static int SAMPLE_RATE;
	private static float LINE_FRAME_SIZE=2.0f; // always using lines with 16 bit
	private static float NET_BYTES_PER_SAMPLE;
	
	// quick look-up tables
	private static final float[] netFrameSize={
		1, // nothing
		2, // CD
		2, // FM
		1, // Telephone
		33.0f // GSM
	};

	private static final float[] netSampleRate={
		1.0f,  // nothing
		44100.0f, // CD
		22050.0f, // FM
		8000.0f,  // Telephone
		8000.0f   // GSM
	};

	private static final float[] netFrameRate={
		1.0f,  // nothing
		44100.0f, // CD
		22050.0f, // FM
		8000.0f,  // Telephone
		50.0f   // GSM
	};

	public static long lineBytes2Ms(long bytes) {
		return (long) (bytes/LINE_FRAME_SIZE*1000/SAMPLE_RATE);
	}

	public static long netBytes2Ms(long bytes, int formatCode) {
		return (long) (bytes/netFrameRate[formatCode]*1000/netFrameSize[formatCode]);
	}
	
	public static long bytes2Ms(long bytes, AudioFormat format) {
		return (long) (bytes/format.getFrameRate()*1000/format.getFrameSize());
	}

	public static long ms2Bytes(long ms, AudioFormat format) {
		return (long) (ms*format.getFrameRate()/1000*format.getFrameSize());
	}

	public static AudioFormat getLineAudioFormat(int formatCode) {
		return getLineAudioFormat(netSampleRate[formatCode]);
	}

	public static AudioFormat getLineAudioFormat(float sampleRate) {
		return new AudioFormat(
                 AudioFormat.Encoding.PCM_SIGNED,
                 sampleRate,    // sampleRate
                 16,            // sampleSizeInBits
                 1,             // channels
                 2,             // frameSize
                 sampleRate,    // frameRate
                 false);        // bigEndian
                 // ulaw conversion does not work with Sun's ulaw provider if big endian...
	}
	
	public static AudioFormat getNetAudioFormat(int nformat) throws UnsupportedAudioFileException {
		if (nformat==FORMAT_CODE_CD) {
			return new AudioFormat(
                 AudioFormat.Encoding.PCM_SIGNED,
                 44100.0f,   // sampleRate
                 16,            // sampleSizeInBits
                 1,             // channels
                 2,             // frameSize
                 44100.0f,      // frameRate
                 true);         // bigEndian
		} 
		else if (nformat==FORMAT_CODE_FM) {
			return new AudioFormat(
                 AudioFormat.Encoding.PCM_SIGNED,
                 22050.0f,   // sampleRate
                 16,            // sampleSizeInBits
                 1,             // channels
                 2,             // frameSize
                 22050.0f,      // frameRate
                 true);         // bigEndian
		} 
		else if (nformat==FORMAT_CODE_TELEPHONE) {
			return new AudioFormat(
                 AudioFormat.Encoding.ULAW,
                 8000.0f,       // sampleRate
                 8,             // sampleSizeInBits
                 1,             // channels
                 1,             // frameSize
                 8000.0f,       // frameRate
                 false);        // bigEndian
		}
		else if (nformat==FORMAT_CODE_GSM) {
			try {
				Class.forName("org.tritonus.share.sampled.Encodings");
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("Tritonus shared classes not properly installed!");
			}
			return new AudioFormat(
				org.tritonus.share.sampled.Encodings.getEncoding("GSM0610"), 
				8000.0F,        // sampleRate
				-1,             // sampleSizeInBits
				1,              // channels
				33,             // frameSize
				50.0F,          // frameRate
				false);         // bigEndian
		}
		throw new RuntimeException("Wrong format code!");
	}
	
	public static int getFormatCode(AudioFormat format) {
		AudioFormat.Encoding encoding = format.getEncoding();
		// very simple check...
		if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
			if (format.getSampleRate()==44100.0f) {
				return FORMAT_CODE_CD;
			} else {
				return FORMAT_CODE_FM;
			}
		}
		if (encoding.equals(AudioFormat.Encoding.ULAW)) {
			return FORMAT_CODE_TELEPHONE;
		}
		if (encoding.equals(org.tritonus.share.sampled.Encodings.getEncoding("GSM0610"))) {
			return FORMAT_CODE_GSM;
		}
		throw new RuntimeException("Wrong Format");
	}
	
}

/*** AMAudioFormat.java ***/