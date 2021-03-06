package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.AutoLineActivity;
import com.liberal.young.tuomanprivatecloud.activity.ChejianActivity;
import com.liberal.young.tuomanprivatecloud.activity.DetailMachineListActivity;
import com.liberal.young.tuomanprivatecloud.adapter.ChejianBaseAdapter;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusMachineFragment;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 陀曼的客户登录后加载的生产线界面
 * <p>
 * Created by Administrator on 2017/3/14.
 */

public class MachineFragment extends Fragment {
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_manage_client)
    ListView lvManageClient;
    @BindView(R.id.swipe_manage_client_activity)
    SwipeRefreshLayout swipeManageClientActivity;



    private MyApplication application;
    private int companyId = -1;
    private WaitingDialog waitingDialog;
    private String userLimit;
    private List<String> workShopList = new ArrayList<>();
    private ChejianBaseAdapter adapter;


    public MachineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_client_layout, container, false);

        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    private void initView(View view) {
        lvManageClient = (ListView) view.findViewById(R.id.lv_manage_client);
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        application = (MyApplication) getActivity().getApplication();
        swipeManageClientActivity = (SwipeRefreshLayout) view.findViewById(R.id.swipe_manage_client_activity);
        companyId = (int) application.getCompanyId();
        tvTitle.setText("车间列表");
        ivTitleLeft.setVisibility(View.GONE);
        ivTitleRight.setVisibility(View.GONE);
        ivTitleLeft.setImageResource(R.mipmap.back);
        waitingDialog = new WaitingDialog(getActivity(), application, "", false);
        userLimit = application.getUserLimits();
        lvManageClient.setOnItemClickListener(itemClickListener);
        doHttpSearchWorkShop();
        adapter = new ChejianBaseAdapter(getActivity(), workShopList);
        lvManageClient.setAdapter(adapter);
        swipeManageClientActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userLimit.equals("1") || userLimit.equals("2")) {
                    doHttpSearchWorkShop();
                } else if (userLimit.equals("3") || userLimit.equals("4")) {

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeManageClientActivity.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    //item点击事件
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (userLimit.equals("1") || userLimit.equals("2")) {       //管理端
                //跳转下一页面的
                Intent intent = new Intent(getActivity(), DetailMachineListActivity.class);
                intent.putExtra("companyId", companyId);
                intent.putExtra("workshop", workShopList.get(position));
                intent.putExtra("haha", 1);
                startActivity(intent);
            } else if (userLimit.equals("3") || userLimit.equals("4")) {      //用户端
                Intent intent = new Intent(getActivity(), AutoLineActivity.class);
                intent.putExtra("companyId", companyId);
                intent.putExtra("workshop", workShopList.get(position));
                intent.putExtra("haha", 1);
                startActivity(intent);
            }
        }
    };


    //------------查找公司下的车间---------------
    private void doHttpSearchWorkShop() {
        workShopList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.findWorkshops(companyId, application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: " + e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse查找车间：: " + res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JsonUtils.getCode(res) == 0) {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                JSONArray array = jsonObject.getJSONArray("result");
                                int size = array.length();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        workShopList.add(i, array.get(i).toString());
                                    }
                                } else {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.getDataNotify(workShopList);
                        }
                    }
                });
            }
        });
    }





}
