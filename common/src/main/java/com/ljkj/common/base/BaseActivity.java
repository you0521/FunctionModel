package com.ljkj.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.ljkj.common.View.loding.LoadingView;
import com.ljkj.common.utils.ActivityUtils;
import com.ljkj.common.utils.ToastUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public  abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    private Unbinder mBind;

    protected T presenter;

    private LoadingView loadingView;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //setStatusBarColor();
        //绑定ButterKnife
        mBind = ButterKnife.bind(this);
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
        //初始化数据
        initView();
        //view与数据绑定
        initLoad();
        //设置监听
        setListener();

    }



    private T getPresenter() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass.equals(BaseActivity.class))
            return null;
        Type[] arguments = ((ParameterizedType) superclass).getActualTypeArguments();
        if (arguments.length == 0) {
            return null;
        }
        Class<T> argument = (Class<T>) arguments[0];
        try {
            //实例化对象
            T type = argument.newInstance();
            return type;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁ButterKnife
        if (mBind != null) {
            mBind.unbind();
            mBind = null;
        }
        //将沉浸式销毁   防止内存泄漏
        if (mImmersionBar != null){
            mImmersionBar.destroy(this,null);

        }
        //销毁view
        if (presenter != null){
            presenter.detachView();
        }
        //ActivityUtils.fixSoftInputLeaks(this.getWindow());
    }

    //统一获取布局文件
    public abstract int getLayoutId();

    //统一初始化
    public abstract void initView();

    //统一请求数据
    public abstract void initLoad();

    //创建监听事件
    protected void setListener() {
    }

    BaseFragment lastFragment;

    //统一管理Fragment的替换
    public BaseFragment setCreateView(Class<? extends BaseFragment> classFragment, int layoutId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String simpleName = classFragment.getSimpleName();
        BaseFragment fragmentByTag = (BaseFragment) manager.findFragmentByTag(simpleName);
        try {
            if (fragmentByTag == null) {
                //创建一个对象实例
                fragmentByTag = classFragment.newInstance();
                transaction.add(layoutId, fragmentByTag, simpleName);
            }
            if (lastFragment != null) {
                //隐藏
                transaction.hide(lastFragment);
            }
            //展示
            transaction.show(fragmentByTag);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        lastFragment = fragmentByTag;
        transaction.commit();
        return fragmentByTag;
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
    public void errorMessage(String msg) {
        showErrorMsg(msg);

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
        LoadingView.showLoadingDiaLog(this);

    }

    /**
     * 获取状态栏颜色值
     *
     * @return
     */
   // public abstract int getStatusBarColor();

    /**
     * 状态栏图标是否是浅色
     *
     * @return
     */
   // public abstract boolean statusBarIsLightMode();

  /*  *//**
     * 设置浸入式标题栏
     *//*
    private void setStatusBarColor() {
        // 未设置标题栏颜色，默认禁用浸入式标题栏
        if (getStatusBarColor() == 0) {
            return;
        }
        if (null == getWindow()) {
            return;
        }
        // 设置状态栏透明
        StatusBarUtils.transparentStatusBar(getWindow());
        // 设置标题栏颜色为应用主题颜色
        StatusBarUtils.setStatusBarColor(getWindow(), getResources().getColor(getStatusBarColor()), 0);
        if (statusBarIsLightMode()) {
            // 设置状态栏图标为白色
            StatusBarUtils.setLightMode(getWindow());
        } else {
            StatusBarUtils.setDarkMode(getWindow());
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

