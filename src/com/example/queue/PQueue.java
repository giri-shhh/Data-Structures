package com.example.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PQueue<T extends Comparable<T>> {

	private int heapSize = 0;
	private int heapCapacity = 0;
	private List<T> heap = null;

	private Map<T, TreeSet<Integer>> map = new HashMap<>();

	public PQueue() {
		this(1);
	}

	public PQueue(int size) {
		heap = new ArrayList<>(size);
	}

	public PQueue(T[] elems) {
		heapSize = heapCapacity = elems.length;
		heap = new ArrayList<>(heapCapacity);
		for (int i = 0; i < heapSize; i++) {
			mapAdd(elems[i], i);
			heap.add(elems[i]);
		}

		for (int i = Math.max(0, heapSize - 2 / 1); i >= 0; i--)
			sink(i);

	}

	public PQueue(Collection<T> elems) {
		this(elems.size());
		for (T elem : elems)
			add(elem);
	}

	public boolean isEmpty() {
		return heapSize == 0;
	}

	public void clear() {
		for (int i = 0; i < heapSize; i++)
			heap.set(i, null);
		heapSize = 0;
		map.clear();
	}

	public int size() {
		return heapSize;
	}

	public T peek() {
		if (isEmpty())
			return null;
		return heap.get(0);
	}

	public T poll() {
		return removeAt(0);
	}

	public boolean contains(T elem) {
		if (elem == null)
			return false;
		return map.containsKey(elem);
	}

	public void add(T elem) {
		if (elem == null)
			throw new IllegalArgumentException();
		if (heapSize < heapCapacity)
			heap.set(heapSize, elem);
		else {
			heap.add(elem);
			heapCapacity++;
		}
		mapAdd(elem, heapSize);
		swim(heapSize);
		heapSize++;
	}

	public boolean less(int i, int j) {
		return heap.get(i).compareTo(heap.get(j)) <= 0;
	}

	public void swim(int k) {
		int parent = (k - 1) / 2;
		while (k > 0 && less(k, parent)) {
			swap(parent, k);
			k = parent;
			parent = (k - 1) / 2;
		}
	}

	public void sink(int k) {
		while (true) {
			int left = 2 * k + 1;
			int right = 2 * k + 2;
			int smallest = left;
			if (right < heapSize && less(left, right))
				smallest = right;
			if (left >= heapSize || less(k, smallest))
				break;
			swap(smallest, k);
			k = smallest;
		}
	}

	private void swap(int i, int j) {
		T i_elem = heap.get(i);
		T j_elem = heap.get(j);
		heap.set(i, j_elem);
		heap.set(j, i_elem);
		mapSwap(i_elem, j_elem, i, j);

	}

	public boolean remove(T elem) {
		if (elem == null)
			return false;
		Integer index = mapGet(elem);
		if (index != null)
			removeAt(index);
		return index != null;
	}

	public T removeAt(int i) {
		if (isEmpty())
			return null;
		heapSize--;
		T removed_data = heap.get(i);
		swap(i, heapSize);
		heap.set(heapSize, null);
		mapRemove(removed_data, heapSize);
		if (i == heapSize)
			return removed_data;
		T elem = heap.get(i);
		sink(i);
		if (heap.get(i).equals(elem))
			swim(i);
		return removed_data;
	}

	public boolean isMinHeap(int k) {
		if (k >= heapSize)
			return true;
		int left = 2 * k + 1;
		int right = 2 * k + 2;
		if (left < heapSize && !less(k, left))
			return false;
		if (right < heapSize && !less(k, right))
			return false;
		return isMinHeap(left) && isMinHeap(right);
	}

	public void mapAdd(T value, int index) {
		TreeSet<Integer> set = map.get(value);
		if (set == null) {
			set = new TreeSet<>();
			set.add(index);
		} else
			set.add(index);
	}

	public void mapRemove(T value, int index) {
		TreeSet<Integer> set = map.get(value);
		set.remove(index);
		if (set.size() == 0)
			map.remove(value);
	}

	public Integer mapGet(T value) {
		TreeSet<Integer> set = map.get(value);
		if (set != null)
			return set.last();
		return null;
	}

	public void mapSwap(T val1, T val2, int val1Index, int val2Index) {
		Set<Integer> set1 = map.get(val1);
		Set<Integer> set2 = map.get(val2);
		set1.remove(val1Index);
		set2.remove(val2Index);
		set1.add(val2Index);
		set2.add(val2Index);
	}

	@Override
	public String toString() {
		return heap.toString();
	}
}
