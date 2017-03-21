package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.ImgUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

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
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

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
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
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
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
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
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     *
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImgUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ImgUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }
    }
}
