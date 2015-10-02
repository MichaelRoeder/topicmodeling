/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Iterator;

import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.ObjectDoubleOpenHashMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;
import com.carrotsearch.hppc.cursors.ObjectDoubleCursor;

@Deprecated
public class OpenHashMapDeSerializer {
	
	private static final int DEFAULT_BUFFER_SIZE = 512; 

	public static void serializeObjectDoubleOpenHashMap(
			ObjectDoubleOpenHashMap<String> hashMap, OutputStream oStream)
			throws IOException {
		// at first the size of the current map
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer stringBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		int bufferedStringLength;
		// serialize the size of the Map
		intBuffer.putInt(hashMap.assigned);
		oStream.write(intBuffer.array());
		intBuffer.clear();
		// iterate through the HashMap
		Iterator<ObjectDoubleCursor<String>> iterator = hashMap.iterator();
		ObjectDoubleCursor<String> cursor;
		while (iterator.hasNext()) {
			cursor = iterator.next();
			// Serialize the length of the key
			intBuffer.putInt(cursor.key.length());
			oStream.write(intBuffer.array());
			intBuffer.clear();
			// Serialize the key
			bufferedStringLength = cursor.key.length() * 2;
			if(bufferedStringLength > stringBuffer.capacity()) {
				stringBuffer = ByteBuffer.allocate(bufferedStringLength);
			}
			for(int i = 0; i < cursor.key.length(); ++i) {
				stringBuffer.putChar(cursor.key.charAt(i));
			}
			oStream.write(stringBuffer.array(), 0, bufferedStringLength);
			stringBuffer.clear();
			// Serialize the value
			doubleBuffer.putDouble(cursor.value);
			oStream.write(doubleBuffer.array());
			doubleBuffer.clear();
		}
	}

	public static void serializeObjectDoubleOpenHashMap(
			ObjectDoubleOpenHashMap<String> hashMap, RandomAccessFile raf)
			throws IOException {
		// at first the size of the current map
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer stringBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		int bufferedStringLength;
		// serialize the size of the Map
		intBuffer.putInt(hashMap.assigned);
		raf.write(intBuffer.array());
//		oStream.write(intBuffer.array());
		intBuffer.clear();
		// iterate through the HashMap
		Iterator<ObjectDoubleCursor<String>> iterator = hashMap.iterator();
		ObjectDoubleCursor<String> cursor;
		while (iterator.hasNext()) {
			cursor = iterator.next();
			// Serialize the length of the key
			intBuffer.putInt(cursor.key.length());
			raf.write(intBuffer.array());
			intBuffer.clear();
			// Serialize the key
			bufferedStringLength = cursor.key.length() * 2;
			if(bufferedStringLength > stringBuffer.capacity()) {
				stringBuffer = ByteBuffer.allocate(bufferedStringLength);
			}
			for(int i = 0; i < cursor.key.length(); ++i) {
				stringBuffer.putChar(cursor.key.charAt(i));
			}
			raf.write(stringBuffer.array(), 0, bufferedStringLength);
			stringBuffer.clear();
			// Serialize the value
			doubleBuffer.putDouble(cursor.value);
			raf.write(doubleBuffer.array());
			doubleBuffer.clear();
		}
	}

	public static void serializeIntDoubleOpenHashMap(
			IntDoubleOpenHashMap hashMap, OutputStream oStream)
			throws IOException {
		// at first the size of the current map
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		// serialize the size of the Map
		intBuffer.putInt(hashMap.assigned);
		oStream.write(intBuffer.array());
		intBuffer.clear();
		// iterate through the HashMap
		Iterator<IntDoubleCursor> iterator = hashMap.iterator();
		IntDoubleCursor cursor;
		while (iterator.hasNext()) {
			cursor = iterator.next();
			// Serialize the key
			intBuffer.putInt(cursor.key);
			oStream.write(intBuffer.array());
			intBuffer.clear();
			// Serialize the value
			doubleBuffer.putDouble(cursor.value);
			oStream.write(doubleBuffer.array());
			doubleBuffer.clear();
		}
	}

	public static ObjectDoubleOpenHashMap<String> deserializeObjectDoubleOpenHashMap(
			InputStream iStream) throws IOException, ClassNotFoundException {
		
		int size, tempLength;
		String key;
		char tempChars[] = new char[(DEFAULT_BUFFER_SIZE / 2) + 1];
		double value;
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer stringBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		// read the size of the Map
		iStream.read(intBuffer.array(), 0, 4);
		size = intBuffer.getInt();
		intBuffer.clear();
		ObjectDoubleOpenHashMap<String> deserializedMap = new ObjectDoubleOpenHashMap<String>(
				size);
		for(int i = 0; i < size; ++i) {
			// read the length of the key (String)
			iStream.read(intBuffer.array(), 0, 4);
			tempLength = intBuffer.getInt();
			intBuffer.clear();
			if(tempLength > tempChars.length) {
				stringBuffer = ByteBuffer.allocate(tempLength * 2);
				tempChars = new char[tempLength];
			}
			iStream.read(stringBuffer.array(), 0, tempLength * 2);
			stringBuffer.asCharBuffer().get(tempChars, 0, tempLength);
			key = new String(tempChars, 0, tempLength);
//			key = stringBuffer.asCharBuffer().toString();
			stringBuffer.clear();
			// read the value (double)
			iStream.read(doubleBuffer.array(), 0, 8);
			value = doubleBuffer.getDouble();
			doubleBuffer.clear();
			deserializedMap.put(key, value);
		}

		return deserializedMap;
	}


	public static ObjectDoubleOpenHashMap<String> deserializeObjectDoubleOpenHashMap(
			RandomAccessFile raf) throws IOException, ClassNotFoundException {
		
		int size, tempLength;
		String key;
		char tempChars[] = new char[(DEFAULT_BUFFER_SIZE / 2) + 1];
		double value;
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer stringBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		// read the size of the Map
		raf.read(intBuffer.array(), 0, 4);
		size = intBuffer.getInt();
		intBuffer.clear();
		ObjectDoubleOpenHashMap<String> deserializedMap = new ObjectDoubleOpenHashMap<String>(
				size);
		for(int i = 0; i < size; ++i) {
			// read the length of the key (String)
			raf.read(intBuffer.array(), 0, 4);
			tempLength = intBuffer.getInt();
			intBuffer.clear();
			if(tempLength > tempChars.length) {
				stringBuffer = ByteBuffer.allocate(tempLength * 2);
				tempChars = new char[tempLength];
			}
			raf.read(stringBuffer.array(), 0, tempLength * 2);
			stringBuffer.asCharBuffer().get(tempChars, 0, tempLength);
			key = new String(tempChars, 0, tempLength);
//			key = stringBuffer.asCharBuffer().toString();
			stringBuffer.clear();
			// read the value (double)
			raf.read(doubleBuffer.array(), 0, 8);
			value = doubleBuffer.getDouble();
			doubleBuffer.clear();
			deserializedMap.put(key, value);
		}

		return deserializedMap;
	}
	
	public static IntDoubleOpenHashMap deserializeIntDoubleOpenHashMap(
			InputStream iStream) throws IOException, ClassNotFoundException {
		int size;
		int key;
		double value;
		ByteBuffer intBuffer = ByteBuffer.allocate(4);
		ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
		// read the size of the Map
		iStream.read(intBuffer.array(), 0, 4);
		size = intBuffer.getInt();
		intBuffer.clear();
		IntDoubleOpenHashMap deserializedMap = new IntDoubleOpenHashMap(
				size);
		for(int i = 0; i < size; ++i) {
			// read the key (int)
			iStream.read(intBuffer.array(), 0, 4);
			key = intBuffer.getInt();
			intBuffer.clear();
			// read the value (double)
			iStream.read(doubleBuffer.array(), 0, 8);
			value = doubleBuffer.getDouble();
			doubleBuffer.clear();
			deserializedMap.put(key, value);
		}

		return deserializedMap;
	}
}
