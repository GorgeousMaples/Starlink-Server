package com.common.core.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步任务上下文辅助类
 *
 */
@Slf4j
public class ContextHolder {

    // TransmittableThreadLocal 用于解决跨线程传递线程本地变量的问题
    private static final TransmittableThreadLocal<ConcurrentHashMap<String, Object>> context = new TransmittableThreadLocal<>();

    /**
     * 获取上下文
     * ConcurrentHashMap 是线程安全的哈希映射类
     */
    public static ConcurrentHashMap<String, Object> getContext() {
        return context.get();
    }

    /**
     * 设置 context
     *
     * @param value 值
     */
    public static void setContext(ConcurrentHashMap<String, Object> value) {
        context.set(value);
    }

    /**
     * 获取当前 context 中的 Web 请求参数
     */
    public static RequestAttributes getRequestAttributes() {
        ConcurrentHashMap<String, Object> currentContext = getContext();
        return currentContext != null && currentContext.containsKey("requestAttributes")
                ? (RequestAttributes) currentContext.get("requestAttributes")
                : null;
    }

    /**
     * 将当前线程的 Web 请求参数添加到 context 中
     * 键为 requestAttributes
     */
    public static void addCurrentRequestAttributes() {
        RequestAttributes attributes = null;
        try {
            attributes = RequestContextHolder.getRequestAttributes();
        } catch (Exception e) {
            log.error("获取requestAttributes失败", e);
        }
        if (attributes == null) {
            return;
        }
        put("requestAttributes", attributes);
    }

    /**
     * 将当前 context 中的 RequestAttributes 添加到当前线程的 Web 请求参数中
     */
    public static void setCurrentRequestAttributes() {
        RequestContextHolder.setRequestAttributes(getRequestAttributes());
    }

    /**
     * 设置键值对到 context 中
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, Object value) {
        ConcurrentHashMap<String, Object> map = context.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.put(key, value);
        context.set(map);
    }

    /**
     * 清理 context
     * 避免线程之间数据污染
     */
    public static void clear() {
        context.remove();
    }
}

