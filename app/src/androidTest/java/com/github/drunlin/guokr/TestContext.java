package com.github.drunlin.guokr;

import android.os.Bundle;

import com.github.drunlin.guokr.mock.MockNetworkModel;
import com.github.drunlin.guokr.model.NetworkModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.module.tool.ReferenceMap;
import com.github.drunlin.guokr.module.tool.ViewLifecycle;
import com.github.drunlin.guokr.util.JavaUtil.Converter;

import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * @author drunlin@outlook.com
 */
public class TestContext {
    static {
        App.viewLifecycleConverter = view -> new MockLifecycle();
    }

    private static final Injector injector = new MockInjector();

    /**
     * 获取依赖注入器。
     * @return
     */
    public static Injector getInjector() {
        return injector;
    }

    /**
     * 对入对象所需的依赖对象。
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> T inject(T instance) {
        return injector.inject(instance);
    }

    /**
     * 重置注入的对象。
     * @see org.mockito.Mockito#reset(Object[])
     * @param target
     */
    public static void resetMocks(Object target) {
        foreachInject(target, field -> reset(field.get(target)));
    }

    /**
     * 遍对象{@link Inject}注解上的属性。
     * @param target
     * @param creator
     */
    private static void foreachInject(Object target, FieldHolder creator) {
        foreachInject(target.getClass(), creator);
    }

    /**
     * 遍历类{@link Inject}注解上的属性。
     * @param clazz
     * @param creator
     */
    private static void foreachInject(Class clazz, FieldHolder creator) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                try {
                    creator.set(field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            foreachInject(superClass, creator);
        }
    }

    interface FieldHolder {
        void set(Field field) throws IllegalAccessException;
    }

    static class MockInjector implements Injector {
        private final ReferenceMap<Class, Object> objectMap = new ReferenceMap<>();

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Class<T> clazz) {
            if (clazz.getSimpleName().endsWith("Factory")) {
                String className = clazz.getName().replaceAll("\\$Factory$", "");
                try {
                    Class cls = Class.forName(className);
                    Object instance = objectMap.get(cls, () -> mock(cls));
                    return (T) objectMap.get(clazz, () -> mock(clazz, (Answer) i -> instance));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (clazz == NetworkModel.class) {
                return (T) objectMap.get(clazz, MockNetworkModel::getInstance);
            }
            return (T) objectMap.get(clazz, () -> mock(clazz));
        }

        @Override
        public <T> T inject(T instance) {
            foreachInject(instance, field -> field.set(instance, get(field.getType())));
            return instance;
        }
    }

    static class MockLifecycle implements ViewLifecycle {
        @Override
        public <P> P bind(Class<? extends P> presenterClass) {
            return injector.get(presenterClass);
        }

        @Override
        public <P, F> P bind(Class<F> factoryClass, Converter<F, P> delegate) {
            return delegate.convert(injector.get(factoryClass));
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {}

        @Override
        public void onSaveInstanceState() {}

        @Override
        public void onDestroy() {}
    }
}
