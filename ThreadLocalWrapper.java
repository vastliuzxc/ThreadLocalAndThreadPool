/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2022. All rights reserved
 */

package com.huawei.it.oem.po.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * 本地缓存包装器
 * </p>
 *
 * @author l30008807
 * @since 2022-01-13 19:10
 */
public class ThreadLocalWrapper {
    private static final List<ThreadLocalWrapper> THREAD_LOCALS = new ArrayList<>();

    /**
     * 请求结束时是否清除缓存
     */
    private boolean isClear;

    private ThreadLocal threadLocal;

    private ThreadLocalWrapper(boolean isClear, ThreadLocal threadLocal) {
        this.isClear = isClear;
        this.threadLocal = threadLocal;
    }

    /**
     * 构建一个ThreadLocal实例
     * 默认请求结束后清除线程缓存
     *
     * @return 实例
     */
    public static ThreadLocal build() {
        return build(true);
    }

    /**
     * 构建一个ThreadLocal实例
     * 指定请求结束后是否清除线程缓存
     *
     * @param isClear 是否清除缓存
     * @return 实例
     */
    public static ThreadLocal build(boolean isClear) {
        ThreadLocal threadLocal = new ThreadLocal<>();
        THREAD_LOCALS.add(new ThreadLocalWrapper(isClear, threadLocal));
        return threadLocal;
    }

    /**
     * 构建一个拥有初始值的ThreadLocal
     * 默认会在线程结束后清除
     *
     * @param supplier 初始值函数
     * @param <S> 泛型
     * @return ThreadLocal实例
     */
    public static <S> ThreadLocal<S> withInitial(Supplier< ? extends S> supplier) {
        return withInitial(supplier, true);
    }

    /**
     * 构建一个拥有初始值的ThreadLocal
     * 需要指定是否会在线程结束后清除
     *
     * @param supplier 初始值函数
     * @param isClear 是否清除标志
     * @param <S> 泛型
     * @return ThreadLocal实例
     */
    public static <S> ThreadLocal<S> withInitial(Supplier< ? extends S> supplier, boolean isClear) {
        ThreadLocal threadLocal = ThreadLocal.withInitial(supplier);
        THREAD_LOCALS.add(new ThreadLocalWrapper(isClear, threadLocal));
        return threadLocal;
    }

    /**
     * 清除threadLocal中的缓存
     *
     */
    public static void clear() {
        THREAD_LOCALS.stream().filter(wrapper -> wrapper.isClear).forEach(wrapper -> wrapper.threadLocal.remove());
    }
}
