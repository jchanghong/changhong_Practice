/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package changhong_Practice.image_loader.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides I/O operations
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class IoUtils {

	/** {@value} */
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
	/** {@value} */

	private IoUtils() {
	}

	/**
	 * Copies stream, fires progress events by listener, can be interrupted by listener. Uses buffer size =
	 * {@value #DEFAULT_BUFFER_SIZE} bytes.
	 *
	 * @param is       Input stream
	 * @param os       Output stream
	 * @param listener Listener of copying progress and controller of copying interrupting
	 * @return <b>true</b> - if stream copied successfully; <b>false</b> - if copying was interrupted by listener
	 * @throws java.io.IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os) throws IOException {
		return copyStream(is, os, DEFAULT_BUFFER_SIZE);
	}
    public static int copyStreamint(InputStream is, OutputStream os) throws IOException {
		return copyStreamint(is, os, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Copies stream, fires progress events by listener, can be interrupted by listener.
	 *
	 * @param is         Input stream
	 * @param os         Output stream
	 * @param listener   Listener of copying progress and controller of copying interrupting
	 * @param bufferSize Buffer size for copying, also represents a step for firing progress listener callback, i.e.
	 *                   progress event will be fired after every copied <b>bufferSize</b> bytes
	 * @return <b>true</b> - if stream copied successfully; <b>false</b> - if copying was interrupted by listener
	 * @throws java.io.IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os, int bufferSize)
			throws IOException {
		final byte[] bytes = new byte[bufferSize];
		int count;
		while ((count = is.read(bytes, 0, bufferSize)) != -1) {
			os.write(bytes, 0, count);
		}
		return true;
	}public static int copyStreamint(InputStream is, OutputStream os, int bufferSize)
			throws IOException {
		final byte[] bytes = new byte[bufferSize];
        int sum=0;
		int count;
		while ((count = is.read(bytes, 0, bufferSize)) != -1) {
			os.write(bytes, 0, count);
            sum+=count;
		}
		return sum;
	}


	/**
	 * Reads all data from stream and close it silently
	 *
	 * @param is Input stream
	 */
	public static void readAndCloseStream(InputStream is) {
		final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1) {
			}
		} catch (IOException e) {
			// Do nothing
		} finally {
			closeSilently(is);
		}
	}

	public static void closeSilently(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// Do nothing
		}
	}
}
