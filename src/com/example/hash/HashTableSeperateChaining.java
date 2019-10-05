package com.example.hash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.example.linkedlist.DoublyLinkedList;

class Entry<K, V> {
	int hash;
	K key;
	V value;

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
		hash = key.hashCode();
	}

	public boolean equals(Entry<K, V> other) {
		if (hash != other.hash)
			return false;
		else
			return key.equals(other.key);
	}

	@Override
	public String toString() {
		return key + "=>" + value;
	}
}

@SuppressWarnings("unchecked")
public class HashTableSeperateChaining<K, V> implements Iterable<K> {

	private static final int DEFAULT_CAPACITY = 3;
	private static final double DEFAULT_LOAD_FACTOR = 0.75;

	private double maxLoadFactor;
	private int capacity, threshhold, size = 0;
	private DoublyLinkedList<Entry<K, V>>[] table;

	public HashTableSeperateChaining() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public HashTableSeperateChaining(int capacity) {
		this(capacity, DEFAULT_LOAD_FACTOR);
	}

	public HashTableSeperateChaining(int capacity, double maxLoadFactor) {
		if (capacity < 0)
			throw new IllegalArgumentException("Illegal Capacity");
		if (maxLoadFactor <= 0 || Double.isNaN(maxLoadFactor) || Double.isInfinite(maxLoadFactor))
			throw new IllegalArgumentException("Illegal MaxLoadFactor");
		this.maxLoadFactor = maxLoadFactor;
		this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
		threshhold = (int) (this.capacity * maxLoadFactor);
		table = new DoublyLinkedList[this.capacity];
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private int normalizeIndex(int keyHash) {
		return (keyHash & 0x7FFFFFFF) % capacity;
	}

	public void clear() {
		Arrays.fill(table, null);
		size = 0;
	}

	public boolean containsKey(K key) {
		return hasKey(key);
	}

	public boolean hasKey(K key) {
		int bucketIndex = normalizeIndex(key.hashCode());
		return bucketSeekEntry(bucketIndex, key) != null;
	}

	public V put(K key, V value) {
		return insert(key, value);
	}

	public V add(K key, V value) {
		return insert(key, value);
	}

	public V insert(K key, V value) {
		if (key == null)
			throw new IllegalArgumentException("Null Key");
		Entry<K, V> newEntry = new Entry<>(key, value);
		int bucketIndex = normalizeIndex(newEntry.hash);
		return bucketInsertEntry(bucketIndex, newEntry);
	}

	public V get(K key) {
		if (key == null)
			throw new IllegalArgumentException();
		int bucketIndex = normalizeIndex(key.hashCode());
		Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
		if (entry != null)
			return entry.value;
		return null;
	}

	public V remove(K key) {
		if (key == null)
			return null;
		int bucketIndex = normalizeIndex(key.hashCode());
		return bucketRemoveEntry(bucketIndex, key);
	}

	private V bucketRemoveEntry(int bucketIndex, K key) {
		Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);
		if (entry != null) {
			DoublyLinkedList<Entry<K, V>> links = table[bucketIndex];
			links.remove(entry);
			--size;
			return entry.value;
		} else
			return null;
	}

	private V bucketInsertEntry(int bucketIndex, Entry<K, V> entry) {
		DoublyLinkedList<Entry<K, V>> bucket = table[bucketIndex];
		if (bucket == null)
			table[bucketIndex] = new DoublyLinkedList<>();
		Entry<K, V> existentEntry = bucketSeekEntry(bucketIndex, entry.key);
		if (existentEntry == null) {
			bucket.add(entry);
			if (++size > threshhold)
				resizeTable();
			return null;
		} else {
			V oldVal = existentEntry.value;
			existentEntry.value = entry.value;
			return oldVal;
		}
	}

	private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {
		if (key == null)
			return null;
		DoublyLinkedList<Entry<K, V>> bucket = table[bucketIndex];
		if (bucket == null)
			return null;
		for (Entry<K, V> entry : bucket)
			if (entry.key.equals(key))
				return entry;
		return null;
	}

	private void resizeTable() {
		capacity *= 2;
		threshhold = (int) (capacity * maxLoadFactor);
		DoublyLinkedList<Entry<K, V>>[] newTable = new DoublyLinkedList[capacity];
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				for (Entry<K, V> entry : table[i]) {
					int bucketIndex = normalizeIndex(entry.hash);
					DoublyLinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
					if (bucket == null)
						newTable[bucketIndex] = bucket = new DoublyLinkedList<>();
					bucket.add(entry);
				}
				table[i].clear();
				table[i] = null;
			}
		}
		table = newTable;
	}

	public List<K> keys() {
		List<K> keys = new ArrayList<>(size());
		for (DoublyLinkedList<Entry<K, V>> bucket : table)
			if (bucket != null)
				for (Entry<K, V> entry : bucket)
					keys.add(entry.key);
		return keys;
	}

	public List<V> values() {
		List<V> values = new ArrayList<>(size());
		for (DoublyLinkedList<Entry<K, V>> bucket : table)
			if (bucket != null)
				for (Entry<K, V> entry : bucket)
					values.add(entry.value);
		return values;
	}

	@Override
	public Iterator<K> iterator() {
		return null;
	}

}
