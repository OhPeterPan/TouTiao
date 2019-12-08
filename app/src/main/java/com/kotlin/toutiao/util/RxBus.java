package com.kotlin.toutiao.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private ConcurrentHashMap<Object, List<Subject>> subMap = new ConcurrentHashMap<>();

    private RxBus() {

    }

    public static RxBus getInstance() {
        return Holder.rxBus;
    }

    public Observable register(Object object) {

        List<Subject> subjects = subMap.get(object);
        if (subjects == null) {
            subjects = new ArrayList<>();
            subMap.put(object, subjects);
        }

        PublishSubject<Object> subject = PublishSubject.create();
        subjects.add(subject);

        return subject;
    }


    public void unRegister(Object object, Observable observable) {

        if (!subMap.containsKey(object)) return;

        List<Subject> subjects = subMap.get(object);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) subMap.remove(object);
        }
    }

    public void post(Object object, Object content) {
        if (!subMap.containsKey(object)) return;
        List<Subject> subjects = subMap.get(object);
        if (null != subjects || !subjects.isEmpty()) {
            for (Subject sub :
                    subjects) {
                sub.onNext(content);
            }
        }
    }

    private static class Holder {
        public static RxBus rxBus = new RxBus();
    }
}
