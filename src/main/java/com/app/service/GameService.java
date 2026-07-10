package com.app.service;

import com.app.domain.vo.CardInfoVo;
import com.common.core.domain.RemoteMethod;
import com.common.core.utils.CardHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    // 缓存方法映射
    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    // 对象类型转换
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @PostConstruct
    public void init() {
        // 扫描所有标注 @RemoteMethod 的方法
        for (Method method : this.getClass().getDeclaredMethods()) {
            RemoteMethod annotation = method.getAnnotation(RemoteMethod.class);
            if (annotation != null) {
                String methodName = annotation.value().isEmpty()
                        ? method.getName()
                        : annotation.value();
                methodCache.put(methodName, method);
            }
        }
    }

    /**
     * 通用调用方法
     */
    public Object invokeMethod(
            String methodName,
            Map<String, Object> params,
            CardHandler cardHandler
    ) {
        Method method = methodCache.get(methodName);
        if (method == null) {
            throw new IllegalArgumentException("Method not found: " + methodName);
        }

        try {
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];

                // 特殊处理 cardHandler 参数
                if (param.getType() == CardHandler.class) {
                    args[i] = cardHandler;
                } else {
                    // 从 map 中获取参数值
                    Object value = params.get(param.getName());
                    if (value != null) {
                        args[i] = OBJECT_MAPPER.convertValue(value, param.getType());
                    }
                }
            }

            return method.invoke(this, args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }

    /**
     * 从卡区移动到手牌区
     */
    @RemoteMethod
    public void MoveFromAreaToHand(boolean isOpponent, int cardId, int areaId, CardHandler cardHandler) {
        List<Integer> handList = cardHandler.getHandList(isOpponent);
        cardHandler.removeAreaCard(areaId, cardId);
        handList.add(cardId);
    }

    /**
     * 将卡牌从卡区移动到卡区
     */
    @RemoteMethod
    public void MoveFromAreaToArea(int cardId, int areaId, int targetAreaId, CardHandler cardHandler) {
        cardHandler.removeAreaCard(areaId, cardId);
        List<Integer> areaList = cardHandler.getAreaList(targetAreaId);
        areaList.add(cardId);
    }

    /**
     * 将手牌移动到卡框处
     */
    @RemoteMethod
    public void MoveToFrame(int cardId, int frameId, CardHandler cardHandler) {
        boolean isSelf = cardHandler.removeCard(cardId);
        cardHandler.setCardPosition(isSelf, cardId, frameId);
    }

    /**
     * 将手牌移动到卡区中
     */
    @RemoteMethod
    public void MoveToArea(int cardId, int areaId, CardHandler cardHandler) {
        cardHandler.removeCard(cardId);
        List<Integer> areaList = cardHandler.getAreaList(areaId);
        areaList.add(cardId);
    }

    /**
     * 将手牌移动到手牌区中
     */
    @RemoteMethod
    public void MoveToHand(boolean isOpponent, int cardId, CardHandler cardHandler) {
        cardHandler.removeCard(cardId);
        List<Integer> handList = cardHandler.getHandList(isOpponent);
        handList.add(cardId);
    }

    /**
     * 创建贴纸
     */
    @RemoteMethod
    public void CreateSticker(int stickerId, int typeId, int cardId, CardHandler cardHandler) {
        CardInfoVo cardInfo = cardHandler.getCard(cardId);
        cardInfo.stickers[typeId] = stickerId;
    }

    /**
     * 拖拽贴纸
     */
    @RemoteMethod
    public void MoveSticker(int typeId, int cardId, int targetCardId, CardHandler cardHandler) {
        CardInfoVo card = cardHandler.getCard(cardId);
        CardInfoVo targetCard = cardHandler.getCard(targetCardId);
        targetCard.stickers[typeId] = card.stickers[typeId];
        card.stickers[typeId] = -1;
    }

    /**
     * 移除贴纸
     */
    @RemoteMethod
    public void RemoveSticker(int typeId, int cardId, CardHandler cardHandler) {
        CardInfoVo cardInfo = cardHandler.getCard(cardId);
        cardInfo.stickers[typeId] = -1;
    }
}
