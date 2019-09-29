package com.example.queue;

import java.util.Iterator;

import com.example.linkedlist.DoublyLinkedList;

public class Queue<T> implements Iterable<T> {

	private DoublyLinkedList<T> list = new DoublyLinkedList<T>();

	public Queue() {

	}

	public Queue(T firstElem) {
		offer(firstElem);
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public T peek() {
		if (isEmpty())
			throw new RuntimeException("Queue is Empty");
		return list.peekFirst();
	}

	public T poll() {
		if (isEmpty())
			throw new RuntimeException("Queue is Empty");
		return list.removeFirst();
	}

	public void offer(T elem) {
		list.addLast(elem);
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

}
