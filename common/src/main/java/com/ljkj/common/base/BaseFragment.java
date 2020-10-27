package com.ljkj.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.ljkj.common.R;
import com.ljkj.common.View.loding.LoadingView;
import com.ljkj.common.utils.StatusBarUtils;
import com.ljkj.common.utils.ToastUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 *
 * @author zhangbiao
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    Unbinder unbinder;
    protected T presenter;
    private LoadingView loadingView;
    protected ImmersionBar mImmersionBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        if (presenter != null) {
            presenter.attachView(this);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
        init();
        loadData();
    }

    //这是统一加载布局的
    protected abstract int getLayoutId();

    //这是统一加载初始化组件的方法
    protected abstract void init();

    //这是统一加载数据的
    protected abstract void loadData();

    private T getPresenter() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass.equals(BaseFragment.class))
            return null;
        Type[] arguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (arguments.length == 0) {
            return null;
        }
        Class<T> aClass = (Class<T>) arguments[0];
        try {
            T t = aClass.newInstance();
            return t;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy(this);

    }

    @Override
    public void showProgressDialog() {
        showLoadingDialog();

    }

    @Override
    public void dimissProgressDialog() {
        closeLoadingDialog();
    }

    @Override
    public void errorMessage(String errorMsg) {
        showErrorMsg(errorMsg);

    }

    private void showErrorMsg(String msg) {
        if (msg.equals("网络异常，请检查您的网络状态") || msg.equals("网络连接超时") || msg.equals("请求超时")) {
            ToastUtils.show(msg);
        } else {
            ToastUtils.show(msg);
        }

    }

    private void closeLoadingDialog() {
       LoadingView.dimissLoadingDialog();
    }

    private void showLoadingDialog() {
       LoadingView.showLoadingDiaLog(getContext());

    }
  /*  *//**
     * 获取状态栏颜色值
     * @return
     *//*
    public abstract int getStatusBarColor();

    *//**
     * 状态栏图标是否是浅色
     * @return
     *//*
    public abstract boolean statusBarIsLightMode();
    *//**
     * 设置浸入式标题栏
     *//*
    private void setStatusBarColor() {
        // 未设置标题栏颜色，默认禁用浸入式标题栏
        if(getStatusBarColor() == 0) {
            return;
        }
        if(null == getActivity().getWindow()) {
            return;
        }
        // 设置状态栏透明
        StatusBarUtils.transparentStatusBar(getActivity().getWindow());
        // 设置标题栏颜色为应用主题颜色
        StatusBarUtils.setStatusBarColor(getActivity().getWindow(), getResources().getColor(getStatusBarColor()), 0);
        if(statusBarIsLightMode()) {
            // 设置状态栏图标为白色
            StatusBarUtils.setLightMode(getActivity().getWindow());
        } else {
            StatusBarUtils.setDarkMode(getActivity().getWindow());
        }
    }*/
    /**
     * 初始化沉浸式状态栏
     */
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this).fullScreen(false) ;    //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏

        //设置状态栏字体颜色
        //  mImmersionBar.statusBarColor(R.color.colorPrimary);
        mImmersionBar.init();
    }
    /**
     * 是否可以使用沉浸式
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }
}

