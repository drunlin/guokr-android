package com.github.drunlin.guokr.module.tool;

import com.github.drunlin.guokr.util.JavaUtil.Supplier;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单的缓存容器，无用的引用会自动删除。
 *
 * @author drunlin@outlook.com
 */
public class ReferenceMap<K, V> {
    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();
    private final Map<K, Reference<V>> referenceMap = new HashMap<>();
    private final Map<Reference<V>, K> keyMap = new HashMap<>();

    public V get(K key, Supplier<V> creator) {
        poll();

        V referent;
        Reference<V> reference = referenceMap.get(key);
        if (reference != null && (referent = reference.get()) != null) {
            return referent;
        } else {
            referent = creator.call();
            reference = new WeakReference<>(referent, referenceQueue);
            keyMap.put(reference, key);
            referenceMap.put(key, reference);
            return referent;
        }
    }

    private void poll() {
        Reference<V> reference;
        //noinspection unchecked
        while ((reference = (Reference<V>) referenceQueue.poll()) != null) {
            K key = keyMap.get(reference);
            if (reference == referenceMap.get(key)) {
                referenceMap.remove(key);
            }
        }
    }
}
