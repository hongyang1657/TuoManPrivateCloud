package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.DataTitleRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.adapter.ProducLineRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.adapter.TotalHomeAdapter;
import com.liberal.young.tuomanprivatecloud.adapter.WorkShopBaseAdapter;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.view.MyFormScrollView;
import com.liberal.young.tuomanprivatecloud.view.ScrollViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public class QueryFragment extends Fragment implements ScrollViewListener {


    @BindView(R.id.tv_switch_month)
    TextView tvSwitchMonth;
    @BindView(R.id.tv_switch_workshop)
    TextView tvSwitchWorkshop;
    @BindView(R.id.iv_rank)
    ImageView ivRank;
    @BindView(R.id.ll_title_forms)
    LinearLayout llTitleForms;
    @BindView(R.id.tv_corner_title)
    TextView tvCornerTitle;
    @BindView(R.id.rv_forms_title)
    RecyclerView rvFormsTitle;
    @BindView(R.id.rv_forms_production_line)
    RecyclerView rvFormsProductionLine;
    @BindView(R.id.rv_recycler)
    RecyclerView rvRecycler;
    @BindView(R.id.rv_recycler_total)
    RecyclerView rvRecyclerTotal;
    private MyFormScrollView svForms;

    private HomeAdapter mAdapter;
    private TotalHomeAdapter mAdapterTotal;
    private List<Integer> mTotalDatas;
    private List<Integer> mDatas;      //填充的数据数组
    private List<String> lineList;   //生产线数组
    private List<Integer> lineTotalList = new ArrayList<>();     //月总产量
    private List<Integer> lineForecast = new ArrayList<>();
    private List<String> dateList;   //日期数组
    private int linesNum = 1;    //生产线数量
    private int dayDate = 31;    //dayOfMonth
    private MyApplication application;
    private List<String> workShopList = new ArrayList<>();
    private Calendar calendar;
    private String currentSelectMonth;
    private String currentSelectWorkshop;

    private ProducLineRecyclerAdapter lineAdapter;
    private DataTitleRecyclerAdapter adapter;
    private int dayOfMonth;
    private int[][] a = {
            {1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10}

    };

    public QueryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_forms_layout, container, false);
        initData();
        initView(view);
        ButterKnife.bind(this, view);
        syncScroll(rvFormsTitle, rvRecycler,rvRecyclerTotal);  //两个横向的RecyclerView联动
        return view;
    }

    private void initData() {
        lineForecast = new ArrayList<>();
        mDatas = new ArrayList<>();
        for (int i = 0; i < (linesNum) * dayDate; i++) {
            mDatas.add(0);
        }
        mTotalDatas = new ArrayList<>();
        for (int i = 0; i < dayDate; i++) {
            mTotalDatas.add(0);
        }
        lineList = new ArrayList<>();
        for (int i = 0; i < linesNum+1; i++) {
            if (i == 0) {
                lineList.add("全线");
                lineTotalList.add(0);
                lineForecast.add(100);
            } else {
                lineList.add(i + "号线");
                lineTotalList.add(i);
                lineForecast.add(100);
            }
        }
        dateList = new ArrayList<>();
        for (int i = 1; i <= dayDate; i++) {
            dateList.add(i + "日");
        }


        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        //L.i("当前日期"+dayOfMonth);
    }

    private void initView(View view) {
        calendar = Calendar.getInstance();
        application = (MyApplication) getActivity().getApplication();
        svForms = (MyFormScrollView) view.findViewById(R.id.sv_forms);
        tvSwitchWorkshop = (TextView) view.findViewById(R.id.tv_switch_workshop);
        tvSwitchMonth = (TextView) view.findViewById(R.id.tv_switch_month);
        svForms.setScrollViewListener(this);
        currentSelectMonth = getYearMonth();

        tvSwitchMonth.setText(getMonth());
        if (!"1".equals(application.getUserLimits())) {
            doHttpSearchWorkShop();
        }

        //全线数据
        mAdapterTotal = new TotalHomeAdapter(getActivity(),mTotalDatas,dayOfMonth,mDatas,lineForecast);
        rvRecyclerTotal = (RecyclerView) view.findViewById(R.id.rv_recycler_total);
        rvRecyclerTotal.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvRecyclerTotal.setAdapter(mAdapterTotal);

        //各条生产线数据
        mAdapter = new HomeAdapter();
        rvRecycler = (RecyclerView) view.findViewById(R.id.rv_recycler);
        rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(linesNum, StaggeredGridLayoutManager.HORIZONTAL));
        rvRecycler.setAdapter(mAdapter);

        //月产量数据
        rvFormsProductionLine = (RecyclerView) view.findViewById(R.id.rv_forms_production_line);
        rvFormsProductionLine.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        lineAdapter = new ProducLineRecyclerAdapter(getActivity(), lineList,lineTotalList,mDatas,lineForecast);
        lineAdapter.setOnItemClickListener(new ProducLineRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

            }
        });
        rvFormsProductionLine.setAdapter(lineAdapter);


        rvFormsTitle = (RecyclerView) view.findViewById(R.id.rv_forms_title);
        rvFormsTitle.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        adapter = new DataTitleRecyclerAdapter(getActivity(), dateList);
        adapter.setOnItemClickListener(new DataTitleRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

            }
        });
        rvFormsTitle.setAdapter(adapter);
    }

    @OnClick({R.id.tv_switch_month, R.id.tv_switch_workshop, R.id.iv_rank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_switch_month:
                //切换月份
                initMonthPopWindow();
                break;
            case R.id.tv_switch_workshop:
                //切换车间
                initWorkshopPopWindow();
                break;
            case R.id.iv_rank:

                break;
        }
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.FormsViewHolder> {

        @Override
        public FormsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FormsViewHolder holder = new FormsViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.report_forms_item, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(FormsViewHolder holder, int position) {
            holder.tvYield.setText(mDatas.get(position)+"");
            double a = (double)(mDatas.get(position))/(double)(lineForecast.get(position%linesNum));
            holder.tvPercent.setText((int) (a*100)+"%");
            /*if (position + 1 > linesNum * (dayDate - 1)) {
                holder.rlItemBack.setBackground(getResources().getDrawable(R.drawable.round_rect_back_red));
            } else {
                holder.rlItemBack.setBackground(getResources().getDrawable(R.drawable.round_rect_back));
            }*/

                if (position+1<=(dayOfMonth-1)*linesNum+linesNum&&position+1>(dayOfMonth-1)*linesNum){
                    holder.rlItemBack.setBackground(getResources().getDrawable(R.drawable.round_rect_back_red));
                }else {
                    holder.rlItemBack.setBackground(getResources().getDrawable(R.drawable.round_rect_back));
                }
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class FormsViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout rlItemBack;
            TextView tvYield;
            TextView tvPercent;

            public FormsViewHolder(View view) {
                super(view);
                rlItemBack = (RelativeLayout) view.findViewById(R.id.rl_report_form_item_back);
                tvYield = (TextView) view.findViewById(R.id.tv_yield_numb);
                tvPercent = (TextView) view.findViewById(R.id.tv_yield_percent);
            }
        }

        public void notifyMDate(){

        }
    }


    private void syncScroll(final RecyclerView topRecycler, final RecyclerView bottomRecycler,final RecyclerView totalRecycler) {
        topRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    bottomRecycler.scrollBy(dx, dy);
                    totalRecycler.scrollBy(dx,dy);
                }
            }
        });

        bottomRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    topRecycler.scrollBy(dx, dy);
                    totalRecycler.scrollBy(dx,dy);
                }
            }
        });

        totalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    bottomRecycler.scrollBy(dx, dy);
                    topRecycler.scrollBy(dx, dy);
                }
            }
        });
    }

    //自定义的滑动监听接口
    @Override
    public void onScrollChanged(MyFormScrollView scrollView, int x, int y, int oldx, int oldy) {
        //Log.i("result", "onScrollChanged。。。ScrollView的监听: " + "----y:" + y + "  oldy:" + oldy);
        rvFormsProductionLine.scrollBy(x, y - oldy);
    }


    private PopupWindow popWnd = null;

    private void initMonthPopWindow() {
        if (popWnd == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.month_pop_layout, null);
            TextView tv1 = (TextView) contentView.findViewById(R.id.tv_pop_1);
            TextView tv2 = (TextView) contentView.findViewById(R.id.tv_pop_2);
            TextView tv3 = (TextView) contentView.findViewById(R.id.tv_pop_3);
            TextView tv4 = (TextView) contentView.findViewById(R.id.tv_pop_4);
            TextView tv5 = (TextView) contentView.findViewById(R.id.tv_pop_5);
            TextView tv6 = (TextView) contentView.findViewById(R.id.tv_pop_6);
            TextView tv7 = (TextView) contentView.findViewById(R.id.tv_pop_7);
            TextView tv8 = (TextView) contentView.findViewById(R.id.tv_pop_8);
            TextView tv9 = (TextView) contentView.findViewById(R.id.tv_pop_9);
            TextView tv10 = (TextView) contentView.findViewById(R.id.tv_pop_10);
            TextView tv11 = (TextView) contentView.findViewById(R.id.tv_pop_11);
            TextView tv12 = (TextView) contentView.findViewById(R.id.tv_pop_12);
            tv1.setOnClickListener(clickListener);
            tv2.setOnClickListener(clickListener);
            tv3.setOnClickListener(clickListener);
            tv4.setOnClickListener(clickListener);
            tv5.setOnClickListener(clickListener);
            tv6.setOnClickListener(clickListener);
            tv7.setOnClickListener(clickListener);
            tv8.setOnClickListener(clickListener);
            tv9.setOnClickListener(clickListener);
            tv10.setOnClickListener(clickListener);
            tv11.setOnClickListener(clickListener);
            tv12.setOnClickListener(clickListener);

            popWnd = new PopupWindow(getActivity());
            popWnd.setOutsideTouchable(true);
            popWnd.setContentView(contentView);
            popWnd.setWidth(application.getWidth() / 5);
            popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        popWnd.showAsDropDown(tvSwitchMonth, 0, 0, Gravity.LEFT);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popWnd.dismiss();
            switch (v.getId()) {
                case R.id.tv_pop_1:
                    tvSwitchMonth.setText("1月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-01";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_2:
                    tvSwitchMonth.setText("2月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-02";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_3:
                    tvSwitchMonth.setText("3月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-03";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_4:
                    tvSwitchMonth.setText("4月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-04";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_5:
                    tvSwitchMonth.setText("5月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-05";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_6:
                    tvSwitchMonth.setText("6月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-06";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_7:
                    tvSwitchMonth.setText("7月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-07";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_8:
                    tvSwitchMonth.setText("8月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-08";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_9:
                    tvSwitchMonth.setText("9月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-09";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_10:
                    tvSwitchMonth.setText("10月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-10";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_11:
                    tvSwitchMonth.setText("11月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-11";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
                case R.id.tv_pop_12:
                    tvSwitchMonth.setText("12月");
                    currentSelectMonth = calendar.get(Calendar.YEAR) + "-12";
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                    break;
            }
        }
    };

    //选择车间
    private PopupWindow workshopPopWnd = null;

    private void initWorkshopPopWindow() {
        if (workshopPopWnd == null) {
            WorkShopBaseAdapter adapter = new WorkShopBaseAdapter(workShopList, getActivity());
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.work_shop_pop_layout, null);
            ListView listView = (ListView) contentView.findViewById(R.id.lv_workshop);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentSelectWorkshop = workShopList.get(position);
                    tvSwitchWorkshop.setText(currentSelectWorkshop);
                    workshopPopWnd.dismiss();
                    doHttpGetCompanyData(currentSelectMonth, currentSelectWorkshop);
                }
            });


            workshopPopWnd = new PopupWindow(getActivity());
            workshopPopWnd.setOutsideTouchable(true);
            workshopPopWnd.setContentView(contentView);
            workshopPopWnd.setWidth(application.getWidth() / 4);
            workshopPopWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        workshopPopWnd.showAsDropDown(tvSwitchWorkshop, 0, 0, Gravity.NO_GRAVITY);
    }

    //------------查找公司下的车间---------------
    private void doHttpSearchWorkShop() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.findWorkshops(application.getCompanyId(), application.getAccessToken()));
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
                                    currentSelectWorkshop = workShopList.get(0);
                                    tvSwitchWorkshop.setText(workShopList.get(0));
                                    doHttpGetCompanyData(getYearMonth(),workShopList.get(0));   //初始化表格数据
                                }else {
                                    rvFormsProductionLine.setVisibility(View.GONE);
                                    svForms.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    //--------------获取表格数据-----------------
    private void doHttpGetCompanyData(String month, String workshop) {
        lineList = new ArrayList<>();  //生产线名数组
        //dateList = new ArrayList<>();  //日期数组
        mDatas = new ArrayList<>();    //数据数组
        lineForecast = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.getCompanyData(application.getCompanyId()
                , month, workshop, application.getAccessToken()));
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
                Log.i("hy_debug_message", "onResponse表格数据: " + res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JsonUtils.getCode(res) == 0) {
                            try {
                                JSONObject object = new JSONObject(res);
                                JSONArray array = object.getJSONArray("result");
                                linesNum = array.length();      //生产线的条数
                                if(linesNum!=0){
                                    lineList.add("全线");
                                    for (int i = 0; i < linesNum; i++) {
                                        JSONObject obj = array.getJSONObject(i);
                                        lineList.add(obj.getString("NAME"));
                                        lineForecast.add(obj.getInt("FORECAST"));
                                    }
                                    for (int i=0;i<dayDate;i++){
                                        for (int n = 0; n < linesNum; n++) {
                                            JSONObject obj = array.getJSONObject(n);
                                            if (i<9){
                                                mDatas.add(obj.getInt("/0"+(i+1)));
                                            }else {
                                                mDatas.add(obj.getInt("/"+(i+1)));
                                            }
                                        }
                                    }


                                    rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(linesNum, StaggeredGridLayoutManager.HORIZONTAL));
                                    rvRecycler.setAdapter(mAdapter);
                                    rvRecycler.setVisibility(View.VISIBLE);
                                    mAdapterTotal.notifyDate(mTotalDatas,mDatas,lineForecast);
                                }else {
                                    lineList.add("全线");
                                    rvRecycler.setVisibility(View.GONE);
                                }
                                //L.i("sdasd"+mDatas.size());

                                lineAdapter.notifyLineDate(lineList,lineTotalList,mDatas,lineForecast);
                                adapter.notifyDataSetChanged();
                                rvFormsTitle.scrollToPosition(0);
                                rvRecyclerTotal.scrollToPosition(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {      //没有数据
                            rvRecycler.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }


    //获取当月
    private String getMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        String mon;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfMon;

        mon = String.valueOf(month + 1);

        if (day < 10) {
            dayOfMon = "0" + day;
        } else {
            dayOfMon = String.valueOf(day);
        }
        return mon + "月";
    }

    private String getYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        String mon;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayOfMon;

        if (month < 10) {
            mon = "0" + (month + 1);
        } else {
            mon = String.valueOf(month + 1);
        }


        if (day < 10) {
            dayOfMon = "0" + day;
        } else {
            dayOfMon = String.valueOf(day);
        }
        return calendar.get(Calendar.YEAR) + "-" + mon;
    }
}
