package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.ManageClientBaseAdapter;
import com.liberal.young.tuomanprivatecloud.bean.JsonResponse;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ManageClientActivity extends BaseActivity {


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

    private static final int ADD_WORKER = 1;
    private static final int ADD_CLIENT = 2;
    private ManageClientBaseAdapter adapter;
    private List<String> mClientNameList;
    private List<String> mClientPhoneList = new ArrayList<>();
    private int dataLength = 0;
    private String userLimit;

    private MyApplication application;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String url = "http://115.29.172.223:8080/machine/api";

    private boolean isScrollToBottom;
    private static final int PAGE_ITEM_NUMBER = 12;      //一页加载的item数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_client_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setImageResource(R.mipmap.add_title);
        application = (MyApplication) getApplication();
        userLimit = application.getUserLimits();
        lvManageClient = (ListView) findViewById(R.id.lv_manage_client);
        mClientNameList = new ArrayList<>();

        if (userLimit.equals("1")||userLimit.equals("2")){
            doHttpPageSearch("pageSearchCustomer");
            tvTitle.setText("客户列表");

        }else if (userLimit.equals("3")||userLimit.equals("4")){
            doHttpPageSearch("pageSearchUser");
            tvTitle.setText("操作工管理");
        }


        adapter = new ManageClientBaseAdapter(this,mClientNameList,mClientPhoneList);
        lvManageClient.setOnItemClickListener(itemClickListener);
        lvManageClient.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState==SCROLL_STATE_IDLE && view.getCount()-1==view.getLastVisiblePosition()&&view.getCount()%PAGE_ITEM_NUMBER==0){
                    //滑动到底部
                    isScrollToBottom = true;
                    int addpage = (view.getCount()/PAGE_ITEM_NUMBER)+1;
                    L.i("addpage"+addpage);
                    if (userLimit.equals("1")||userLimit.equals("2")){
                        addHttpPage(addpage,"pageSearchCustomer");
                    }else if (userLimit.equals("3")||userLimit.equals("4")){
                        addHttpPage(addpage,"pageSearchUser");
                    }

                    adapter.getDataNotify(mClientNameList,mClientPhoneList);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        lvManageClient.setAdapter(adapter);

    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                if (userLimit.equals("1")||userLimit.equals("2")){
                    Intent intent = new Intent(this,AddClientActivity.class);
                    startActivityForResult(intent,ADD_CLIENT);
                }else if (userLimit.equals("3")||userLimit.equals("4")){
                    //添加操作工
                    Intent intent = new Intent(this,AddClientActivity.class);
                    startActivityForResult(intent,ADD_WORKER);
                }

                break;
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (userLimit.equals("1")||userLimit.equals("2")){
                Intent intent = new Intent(ManageClientActivity.this,ManageClientDetailActivity.class);
                intent.putExtra("clientName",mClientNameList.get(position));
                startActivity(intent);
            }else if (userLimit.equals("3")||userLimit.equals("4")){
                Intent intent = new Intent(ManageClientActivity.this,WorkerInfoActivity.class);
                intent.putExtra("workerName",mClientNameList.get(position));
                intent.putExtra("workerPhone",mClientPhoneList.get(position));
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_WORKER:
                doHttpPageSearch("pageSearchUser");
                break;
            case ADD_CLIENT:
                doHttpPageSearch("pageSearchCustomer");
                break;
            default:
                break;
        }
        adapter.getDataNotify(mClientNameList,mClientPhoneList);
    }

    private void doHttpPageSearch(String methed){
        mClientNameList = new ArrayList<>();
        mClientPhoneList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JsonUtils.pageSearch(1,PAGE_ITEM_NUMBER,methed,application.getAccessToken()));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
                        JsonResponse jsonResponse = jsonParseUtil.parsePageSearchJson();
                        dataLength = jsonResponse.getResult().size();

                        for (int i = 0; i< dataLength; i++){
                            mClientNameList.add(i,jsonResponse.getResult().get(i).getUsername());
                            mClientPhoneList.add(i,jsonResponse.getResult().get(i).getPhone());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //上拉加载更多数据
    private void addHttpPage(int page,String methed){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JsonUtils.pageSearch(page,PAGE_ITEM_NUMBER,methed,application.getAccessToken()));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
                        JsonResponse jsonResponse = jsonParseUtil.parsePageSearchJson();
                        dataLength = jsonResponse.getResult().size();

                        for (int i = 0; i< dataLength; i++){
                            mClientNameList.add(jsonResponse.getResult().get(i).getUsername());
                            mClientPhoneList.add(jsonResponse.getResult().get(i).getPhone());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
