package com.common.mybatis.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.common.core.utils.BeanCopyUtils;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseMapperPlus<M, T, V> extends BaseMapper<T> {
    /* ========== 利用反射获取泛型的 class 对象 ========== */
    default Class<V> currentVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 2);
    }

    default Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 1);
    }

    default Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseMapperPlus.class, 0);
    }

    /**
     * 返回数据库中所有的实体对象
     */
    default List<T> selectAll() {
        return this.selectList(new QueryWrapper<>());
    }

    /* ========== 查询简单视图对象 ========== */
    /**
     * 根据条件，查询一条简单视图对象
     */
    default V selectSimpleVoOne(Wrapper<T> wrapper) {
        return selectSimpleVoOne(wrapper, this.currentVoClass());
    }

    /**
     * 根据ID查询简单视图对象
     */
    default V selectSimpleVoById(Serializable id) {
        return selectSimpleVoById(id, this.currentVoClass());
    }

    /**
     * 根据条件，查询符合条件的所有简单视图对象
     */
    default List<V> selectSimpleVoList(Wrapper<T> wrapper) {
        return selectSimpleVoList(wrapper, this.currentVoClass());
    }

    /**
     * 根据ID批量查询简单视图对象
     */
    default List<V> selectSimpleVoBatchIds(Collection<? extends Serializable> ids) {
        return selectSimpleVoBatchIds(ids, this.currentVoClass());
    }

    /**
     * 根据 columnMap 的条件查询简单视图对象
     */
    default List<V> selectSimpleVoByMap(Map<String, Object> map) {
        return selectSimpleVoByMap(map, this.currentVoClass());
    }

    /**
     * 分页查询简单视图对象
     */
    default <P extends IPage<V>> P selectSimpleVoPage(IPage<T> page, Wrapper<T> wrapper) {
        return selectSimpleVoPage(page, wrapper, this.currentVoClass());
    }

    /* ========== 批量查询或更新 ========== */
    /**
     * 批量插入
     */
    default boolean insertBatch(Collection<T> entityList) {
        return Db.saveBatch(entityList);
    }

    /**
     * 批量更新
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return Db.updateBatchById(entityList);
    }

    /**
     * 批量插入或更新
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList) {
        return Db.saveOrUpdateBatch(entityList);
    }

    /**
     * 批量插入(包含限制条数)
     */
    default boolean insertBatch(Collection<T> entityList, int batchSize) {
        return Db.saveBatch(entityList, batchSize);
    }

    /**
     * 批量更新(包含限制条数)
     */
    default boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return Db.updateBatchById(entityList, batchSize);
    }

    /**
     * 批量插入或更新(包含限制条数)
     */
    default boolean insertOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return Db.saveOrUpdateBatch(entityList, batchSize);
    }

    /**
     * 插入或更新(包含限制条数)
     */
    default boolean insertOrUpdate(T entity) {
        return Db.saveOrUpdate(entity);
    }

    /* ========== 查询简单视图对象的原始方法 ========== */
    default <C> C selectSimpleVoById(Serializable id, Class<C> voClass) {
        T obj = this.selectById(id);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanCopyUtils.copy(obj, voClass);
    }

    default <C> List<C> selectSimpleVoBatchIds(Collection<? extends Serializable> idList, Class<C> voClass) {
        List<T> list = this.selectBatchIds(idList);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }

    default <C> List<C> selectSimpleVoByMap(Map<String, Object> map, Class<C> voClass) {
        List<T> list = this.selectByMap(map);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }


    default <C> C selectSimpleVoOne(Wrapper<T> wrapper, Class<C> voClass) {
        T obj = this.selectOne(wrapper);
        if (ObjectUtil.isNull(obj)) {
            return null;
        }
        return BeanCopyUtils.copy(obj, voClass);
    }

    default <C> List<C> selectSimpleVoList(Wrapper<T> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return CollUtil.newArrayList();
        }
        return BeanCopyUtils.copyList(list, voClass);
    }

    default <C, P extends IPage<C>> P selectSimpleVoPage(IPage<T> page, Wrapper<T> wrapper, Class<C> voClass) {
        List<T> list = this.selectList(page, wrapper);
        IPage<C> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(list)) {
            return (P) voPage;
        }
        voPage.setRecords(BeanCopyUtils.copyList(list, voClass));
        return (P) voPage;
    }
}