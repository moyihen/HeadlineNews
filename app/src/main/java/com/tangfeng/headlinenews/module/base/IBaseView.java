package com.tangfeng.headlinenews.module.base;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * Date :2018/7/12
 * Time :13:18
 * author:moyihen
 */

public interface IBaseView<T> {
    /**
     * 显示加载动画
     */
    void onShowLoading();

    /**
     * 隐藏加载
     */
    void onHideLoading();

    /**
     * 显示网络错误
     */
    void onShowNetError();

    /**
     * 设置 presenter
     */
    void setPresenter(T presenter);

    /**
     * 绑定生命周期
     */
    <X> AutoDisposeConverter<X> bindAutoDispose();
}
