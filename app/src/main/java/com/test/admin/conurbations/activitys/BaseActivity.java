package com.test.admin.conurbations.activitys;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.test.admin.conurbations.R;
import com.test.admin.conurbations.annotations.SetLayout;
import com.test.admin.conurbations.utils.CommonUtil;
import com.test.admin.conurbations.utils.DialogUtils;
import com.test.admin.conurbations.utils.InjectUtil;
import com.test.admin.conurbations.utils.SaveBitmapUtils;
import com.test.admin.conurbations.utils.ToastUtils;
import com.test.admin.conurbations.utils.WrapperUtils;
import com.yalantis.contextmenu.lib.MenuObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhouqiong on 2017/2/27.
 */
@SetLayout
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private static final String TAG = "BaseActivity";

    public static final String TRANSLATE_VIEW = "translate_view";
    public static final String TRANSLATE_WEB_VIEW_BG_IMG = "translate_web_view_bg_img";
    public static final String TRANSLATE_WEB_VIEW_TITLE = "translate_web_view_title";
    public static final String EXTRA_URL = "URL";
    public static final String EXTRA_TITLE = "TITLE";
    public Map<String, View> views;

    protected abstract void initData(Bundle bundle);

    protected abstract void initPresenter();

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    protected void initToolbar(Toolbar toolbar, String toolbarTitle, String toolbarSubtitle) {

        if (!TextUtils.isEmpty(toolbarTitle)){
            toolbar.setTitle(toolbarTitle);
        }
        if (!TextUtils.isEmpty(toolbarSubtitle)){
            toolbar.setSubtitle(toolbarSubtitle);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finishActivity();
                return;
            }
        }
        //1
        views = new HashMap<>();
        InjectUtil.inject(this);
        initPresenter();
        initData(savedInstanceState);

        //2
       /* setContentView(setLayoutResourceID());
        ButterKnife.bind(this);
        initData(savedInstanceState);
        initPresenter();*/
    }

    @Override
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivityAndClear(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
        CommonUtil.exit();
    }

    @Override
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivityAndFinish(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityAndFinish(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finishAfterTransition();
        } else {
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void startActivityAndFinishWithOutObservable(final Class<?> cls) {
        final Intent intent = new Intent();
        intent.setClass(this, cls);
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(intent);
                        overridePendingTransition(0, android.R.anim.fade_out);
                        finish();
                    }
                });
    }

    @Override
    public void startActivityAndFinishWithoutTransition(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls) {
        startActivityForResultWithoutTransition(cls, 0);
    }

    @Override
    public void startActivityForResultWithoutTransition(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> cls) {
        startActivityForResult(cls, 0);
    }

    @Override
    public void startActivityForResult(Class<?> cls, Bundle bundle) {
        startActivityForResult(cls, 0, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    @Override
    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public Activity getRootActivity() {
        Activity activity = this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        return activity;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= 21) {
            this.finishAfterTransition();
        } else {
            this.finish();
        }
    }

    public static List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.more_ic_close);

        MenuObject addFr = new MenuObject("下载图片");
        addFr.setResource(R.mipmap.more_ic_download);
        MenuObject addShare = new MenuObject("分享图片");
        addShare.setResource(R.mipmap.more_ic_share);
        MenuObject addSave = new MenuObject("收藏图片");
        addSave.setResource(R.mipmap.more_ic_collection);
        MenuObject block = new MenuObject("设为桌面背景");
        block.setResource(R.mipmap.more_ic_backage);
        MenuObject block2 = new MenuObject("设为锁屏背景");
        block2.setResource(R.mipmap.more_ic_loakclose);

        menuObjects.add(close);
        menuObjects.add(addFr);
        menuObjects.add(addShare);
        menuObjects.add(addSave);
        menuObjects.add(block);
        menuObjects.add(block2);
        return menuObjects;
    }

    public static void downloadBitmap(final Context context, String url, final int state) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                if (state == 1) {
                    //保存图片
                    SaveBitmapUtils.getSaveBitmapObservable(bitmap).subscribe(SaveBitmapUtils.saveSubscriber);
                } else if (state == 2) {
                    //分享图片
                    SaveBitmapUtils.getSaveBitmapObservable(bitmap).subscribe(SaveBitmapUtils.getShareSubscriber(context));
                } else if (state == 4) {
                    //设置桌面壁纸
                    WrapperUtils.getSetWallWrapperObservable(bitmap, context).subscribe(WrapperUtils.callbackSubscriber);
                } else if (state == 5) {
                    //设置锁屏壁纸
                    WrapperUtils.getSetLockWrapperObservable(bitmap, context).subscribe(WrapperUtils.callbackSubscriber);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                ToastUtils.getInstance().showToast("操作失败！");
                DialogUtils.hideProgressDialog();
            }
        });
    }
}
