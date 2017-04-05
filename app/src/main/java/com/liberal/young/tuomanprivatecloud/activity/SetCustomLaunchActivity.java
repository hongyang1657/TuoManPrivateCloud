package com.liberal.young.tuomanprivatecloud.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.ImgUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/21.
 */

public class SetCustomLaunchActivity extends BaseActivity {


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
    @BindView(R.id.ll_select_picture_from_album)
    LinearLayout llSelectPictureFromAlbum;
    @BindView(R.id.tv_setting_auto_close)
    TextView tvSettingAutoClose;
    @BindView(R.id.ll_take_picture)
    LinearLayout llTakePicture;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_BIG_PICTURE = 2;
    protected static Uri tempUri;
    @BindView(R.id.iv_iviv)
    ImageView ivIviv;

    private Intent openCameraIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_launch_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("自定义启动页");
        ivTitleRight.setVisibility(View.GONE);
        ivTitleLeft.setImageResource(R.mipmap.back);
    }

    @OnClick({R.id.iv_title_left, R.id.ll_select_picture_from_album, R.id.ll_take_picture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_select_picture_from_album:
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                break;
            case R.id.ll_take_picture:
                openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                tempUri = FileProvider.getUriForFile(this, this.getApplicationContext()
                        .getPackageName() + ".provider", new File(Environment.getExternalStorageDirectory(), "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

                openTakePhoto();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    Log.i("result", "onActivityResult111: "+data.getData());
                    Log.i("result", "onActivityResult111: "+tempUri);
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    Log.i("result", "onActivityResult: "+data.getData().toString());
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_BIG_PICTURE:

                    if (data != null) {
                        Log.i("result", "uri........222");
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        Bitmap bitmap = decodeUriAsBitmap(data.getData());
                        // 把解析到的位图显示出来
                        ivIviv.setImageBitmap(bitmap);
                    }
            }
        }
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

    private int MY_REQUEST_CODE = 555;
    private void openTakePhoto(){
        /**
         * 在启动拍照之前最好先判断一下sdcard是否可用
         */
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用
            //申请拍照的权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
                }else {
                    startActivityForResult(openCameraIntent, TAKE_PICTURE);
                }
            }

        }else {
            Toast.makeText(this,"sdcard不可用",Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("result", "The uri is not exist.");
        }
        Log.i("result", "uri........");
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 9);
        intent.putExtra("aspectY", 16);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",900);
        intent.putExtra("outputY", 1600);
        // 设置为true直接返回bitmap
        intent.putExtra("return-data", false);
        // 上面设为false的时候将MediaStore.EXTRA_OUTPUT关联一个Uri
        intent.putExtra("output", getTempUri());
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        Log.i("result", "uri........111");
        startActivityForResult(intent, CROP_BIG_PICTURE);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        if (isSDCARDMounted()) {

            File f = new File(Environment.getExternalStorageDirectory(), "bbb.jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "hehe", Toast.LENGTH_LONG).show();
            }
            return f;
        } else {
            return null;
        }
    }

    private boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bitmap bitmap = decodeUriAsBitmap(data.getData());
        saveBitmap(bitmap);
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了




        String imagePath = ImgUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        if (imagePath != null) {
            // 拿着imagePath上传了
            // ...
        }
    }

    private void saveBitmap(Bitmap bm) {
        File f = new File(Environment.getExternalStorageDirectory(),"abc.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i("result", "已经保存");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
