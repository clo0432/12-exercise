package p12.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private final HashMap<Q, Queue<T>> queues;

    public MultiQueueImpl() {
        queues = new HashMap<Q, Queue<T>>();
    }

    @Override
    public Set<Q> availableQueues() {
        return Set.copyOf(queues.keySet());
    }

    @Override
    public void openNewQueue(Q queue) throws IllegalArgumentException {
        if (queues.containsKey(queue)) {
            throw new IllegalArgumentException("Can't open a new queue with the same key");
        }

        queues.put(queue, new LinkedList<T>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) throws IllegalArgumentException {
        if (queues.get(queue) == null) {
            throw new IllegalArgumentException("The queue " + queue + " doesn't exist");
        }

        return queues.get(queue).isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) throws IllegalArgumentException {
        if (queues.get(queue) == null) {
            throw new IllegalArgumentException("The queue " + queue + " doesn't exist");
        }

        queues.get(queue).add(elem);
    }

    @Override
    public T dequeue(Q queue) throws IllegalArgumentException {
        if (queues.get(queue) == null) {
            throw new IllegalArgumentException("The queue " + queue + " doesn't exist");
        }

        return queues.get(queue).poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> elemsDequeued = new HashMap<>();
        for (final Q key : queues.keySet()) {
            elemsDequeued.put(key, dequeue(key));
        }
        return elemsDequeued;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> allEnqueuedElems = new HashSet<>();
        for (final Q key : queues.keySet()) {
            allEnqueuedElems.addAll(queues.get(key));
        }
        return allEnqueuedElems;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) throws IllegalArgumentException {
        if (queues.get(queue) == null) {
            throw new IllegalArgumentException("The queue " + queue + " doesn't exist");
        }

        List<T> elementList = new ArrayList<>();
        while(!queues.get(queue).isEmpty()) {
            elementList.add(dequeue(queue));
        }
        return elementList;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) throws IllegalArgumentException, IllegalStateException {
        if (queues.get(queue) == null) {
            throw new IllegalArgumentException("The queue " + queue + " doesn't exist");
        }
        if (queues.size() < 2) {
            throw new IllegalStateException("Can't close and reallocate a queue with less than 2 queues open");
        }
        
        List<T> elems = dequeueAllFromQueue(queue);
        queues.remove(queue);
        for (final Q key : queues.keySet()) {
            queues.get(key).addAll(elems);
            break;
        }
    }
}