package com.example.stack;

import java.util.EmptyStackException;
import java.util.Iterator;

import com.example.linkedlist.DoublyLinkedList;

public class Stack<T> implements Iterable<T> {

	private DoublyLinkedList<T> list = new DoublyLinkedList<T>();

	public Stack() {
	}

	public Stack(T firstElem) {
		push(firstElem);
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void push(T elem) {
		list.addLast(elem);
	}

	public T pop() {
		if (isEmpty())
			throw new EmptyStackException();
		return list.removeLast();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

}
