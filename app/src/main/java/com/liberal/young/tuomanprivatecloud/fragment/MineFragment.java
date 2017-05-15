package com.liberal.young.tuomanprivatecloud.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.LoginActivity;
import com.liberal.young.tuomanprivatecloud.activity.ManageClientActivity;
import com.liberal.young.tuomanprivatecloud.activity.ModifiPwdActivity;
import com.liberal.young.tuomanprivatecloud.activity.SettingActivity;
import com.liberal.young.tuomanprivatecloud.activity.UserHelpActivity;
import com.liberal.young.tuomanprivatecloud.utils.CircleTransformPicasso;
import com.liberal.young.tuomanprivatecloud.utils.FileImageUploadUtil;
import com.liberal.young.tuomanprivatecloud.utils.ImgUtils;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * com.liberal.young.tuomanprivatecloud.fragment.MineFragment
 * Created by Administrator on 2017/3/10.
 */

public class MineFragment extends Fragment {


    @BindView(R.id.iv_mine_account)
    ImageView ivMineAccount;
    @BindView(R.id.tv_admin)
    TextView tvAdmin;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.rl_mine_account)
    RelativeLayout rlMineAccount;
    @BindView(R.id.tv_user_management)
    TextView tvUserManagement;
    @BindView(R.id.tv_user_modifi_pwd)
    TextView tvUserModifiPwd;
    @BindView(R.id.tv_user_help)
    TextView tvUserHelp;
    @BindView(R.id.tv_user_setting)
    TextView tvUserSetting;
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
    @BindView(R.id.tv_hehe)
    TextView tvHehe;

    private MyApplication application;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Intent openCameraIntent;
    private WaitingDialog dialog;

    private String username;
    private String userHeadUrl;
    private String userLimits;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment_layout, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }


    private void initView(View view) {
        application = (MyApplication) getActivity().getApplication();
        username = application.getUsername();
        userHeadUrl = application.getUserHeadUrl();
        userLimits = application.getUserLimits();
        dialog = new WaitingDialog(getActivity(),application,"",false);
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvUserManagement = (TextView) view.findViewById(R.id.tv_user_management);
        tvHehe = (TextView) view.findViewById(R.id.tv_hehe);
        tvAdmin = (TextView) view.findViewById(R.id.tv_admin);
        tvId = (TextView) view.findViewById(R.id.tv_id);
        ivMineAccount = (ImageView) view.findViewById(R.id.iv_mine_account);
        ivTitleLeft.setVisibility(View.GONE);
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText("我的");
        tvTitle.setTextColor(getResources().getColor(R.color.colorBlueShade));
        tvAdmin.setText(username);
        tvId.setText("ID:"+application.getCompanyId());

        if (!"".equals(userHeadUrl)){
            Picasso.with(getActivity()).load(userHeadUrl).transform(new CircleTransformPicasso())
                    .resize(200,200)
                    .placeholder(R.mipmap.head_big)
                    .error(R.mipmap.head_big).into(ivMineAccount);
        }


        if (userLimits==null){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginInformation",MODE_PRIVATE);
            userLimits = sharedPreferences.getString("userLimits","");
        }
        if (userLimits.equals("1")||userLimits.equals("2")) {     //管理员
            tvUserManagement.setText("客户列表");
        } else if (userLimits.equals("3")||userLimits.equals("4")) {     //客户
            tvUserManagement.setText("操作工管理");
        } else if (userLimits.equals("5")) {
            tvHehe.setVisibility(View.GONE);
            tvUserManagement.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right,R.id.rl_mine_account ,R.id.tv_user_management, R.id.tv_user_modifi_pwd, R.id.tv_user_help, R.id.tv_user_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.iv_title_right:
                break;
            case R.id.rl_mine_account:
                //选择头像
                //startActivity(new Intent(getActivity(), TestRoundHeadCrop.class));
                showChoosePicDialog();
                break;
            case R.id.tv_user_management:
                Intent intent = new Intent(getActivity(), ManageClientActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_user_modifi_pwd:
                Intent intent2 = new Intent(getActivity(), ModifiPwdActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_user_help:
                Intent in = new Intent(getActivity(), UserHelpActivity.class);
                startActivity(in);
                break;
            case R.id.tv_user_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent1,100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case 100:
                    Intent intentOut = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intentOut);
                    getActivity().finish();
                    System.gc();
                    break;
            }
        }

    }


    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = { "选择本地照片", "拍照" };
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        /*openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);*/

                        openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ContentValues contentValues = new ContentValues(1);
                        contentValues.put(MediaStore.Images.Media.DATA, new File(Environment.getExternalStorageDirectory(), "image.jpg").getAbsolutePath());
                        tempUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

                        //申请拍照的权限
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                            }else {
                                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                            }
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }


    //android 6.0 以上申请权限的返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

    private int MY_REQUEST_CODE = 55;





    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.i("The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImgUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            uploadPic(photo);
        }
    }

    private void uploadPic(final Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        dialog.waiting();

        //上传图片
        new Thread(){
            @Override
            public void run() {
                super.run();
                final String res = FileImageUploadUtil.uploadFile(saveBitmapFile(bitmap), MyConstant.UPLOAD_PHOTO_URL
                        ,application.getAccessToken(),application.getUsername());   //上传图片
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        L.i("res:::"+res);
                        dialog.stopWaiting();
                        if (res==null||res.equals("")){
                            Toast.makeText(application, "上传图片失败", Toast.LENGTH_SHORT).show();
                        }else {
                            if (JsonUtils.getCode(res)==0){
                                ivMineAccount.setImageBitmap(bitmap);
                            }else {
                                Toast.makeText(application, "上传图片失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        }.start();


        /*String imagePath = ImgUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        L.i(imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }*/
    }

    public File saveBitmapFile(Bitmap bitmap){
        File file = new File(Environment.getExternalStorageDirectory(), "userhead.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
