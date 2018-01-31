package com.wwish.ganalytics.model;

import java.util.LinkedList;

/**
 * Created by gaocaili on 2017/8/16.
 */
public class LimitQueue<E>{

    private int limit; // 队列长度

    private LinkedList<E> queue = new LinkedList<E>();

    public LimitQueue(int limit){
        this.limit = limit;
    }

    /**
     * 入列：当队列大小已满时，把队头的元素poll掉
     */
    public boolean offer(E e){
        if(queue.size() >= limit){
            queue.poll();
        }
        return queue.offer(e);
    }

    public E get(int position) {
        return queue.get(position);
    }

    public E getLast() {
        return queue.getLast();
    }

    public E getFirst() {
        return queue.getFirst();
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return queue.size();
    }

    public E poll(){
        return queue.poll();
    }

}
