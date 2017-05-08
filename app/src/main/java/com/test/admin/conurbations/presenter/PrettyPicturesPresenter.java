package com.test.admin.conurbations.presenter;


import android.util.Log;

import com.test.admin.conurbations.activitys.IPrettyPictureListView;
import com.test.admin.conurbations.model.api.ACache;
import com.test.admin.conurbations.model.api.GankService;
import com.test.admin.conurbations.model.response.NetImage360;
import com.test.admin.conurbations.retrofit.ApiCallback;
import com.test.admin.conurbations.retrofit.AppClient;
import com.test.admin.conurbations.utils.AppUtils;

/**
 * Created by zhouqiong on 2017/1/18.
 */
public class PrettyPicturesPresenter extends BasePresenter {

    private IPrettyPictureListView iPrettyPictureView;

    public PrettyPicturesPresenter(IPrettyPictureListView iTestList) {
        this.iPrettyPictureView = iTestList;
    }

    public void getPrettyPictureLisData(String cid, final int pager) {
        final String key = "getPrettyPictureLisData" + "cid";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        addSubscription(AppClient.retrofit().create(GankService.class)
                        .get360ImageItemList(cid),
                new ApiCallback<NetImage360>() {
                    @Override
                    public void onSuccess(NetImage360 model) {
                        if (pager == 1) {
                            cache.put(key, model);
                        }
                        iPrettyPictureView.setPrettyPictureData(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Object obj = cache.getAsObject(key);
                        if (obj != null) {
                            NetImage360 model = (NetImage360) obj;
                            iPrettyPictureView.setPrettyPictureData(model);
                            return;
                        }
                        Log.d("msd", msg);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }
}
