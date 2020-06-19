package com.linkknown.ilearning.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linkknown.ilearning.BuildConfig;
import com.linkknown.ilearning.MainActivity;
import com.linkknown.ilearning.R;
import com.linkknown.ilearning.util.LoginUtil;
import com.linkknown.ilearning.util.SecurityUtil;
import com.linkknown.ilearning.util.StringUtilEx;
import com.linkknown.ilearning.util.ui.ToastUtil;
import com.linkknown.ilearning.util.ui.UIUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SetSmallIconActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.small_icon)
    public ImageView small_icon;

    @BindView(R.id.setSmallIconBtn)
    public Button setSmallIconBtn;

    private Bitmap selectedImg;//选择的头像
    private File headFile;
    private String headFileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_small_icon);
        mContext = this;
        ButterKnife.bind(this);

        //初始化参数为空
        selectedImg = null;
        headFile = null;
        headFileUri = null;

        initView();
    }


    public void initView(){
        initToolBar(toolbar,true,"点击图片更换头像");
        small_icon.setOnClickListener(this);
        setSmallIconBtn.setOnClickListener(this);
        //获取传过来的参数，展示当前头像
        UIUtils.setImage(mContext, small_icon, LoginUtil.getHeaderIcon(mContext));
    };


    //提交按钮
    public void submit(){
        if (headFile != null && selectedImg!=null && headFile.length()>0){
            //带参数返回
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("headFileUri",headFileUri);
            intent.putExtra("bundle", bundle);
            setResult(200,intent);
            finish();
        }else{
            ToastUtil.showText(mContext,"请选择图片或拍照");
        }
    };


    @Override
    public void onClick(View v) {
        if (SecurityUtil.isFastClick()) {
            ToastUtil.showText(this, "您点击的太频繁");
            return;
        }
        switch (v.getId()) {
            case R.id.small_icon:
                showTypeDialog();
                break;
            case R.id.setSmallIconBtn:
                submit();
                break;
            default:
                break;
        }

    }


    //弹框提示选择图片来源， 相册选择 or 拍照
    private void showTypeDialog() {
        initImgParam();//弹框的时候，初始化图片路径参数
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        // 在相册中选取
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        // 调用照相机
        tv_select_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext,  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 权限还没有授予，进行申请
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300); // 申请的 requestCode 为 300
                } else {
                    //手机摄像
                    phoneCamera();
                    dialog.dismiss();
                }

            }
        });
        dialog.setView(view);
        dialog.show();
    }


    //头像获取的方式 --  1:相册  2:拍照
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://相册
                if (resultCode == RESULT_OK) {
                    cropPhoto_album(data.getData());// 调用系统的裁剪功能: 相册选图专用
                }
                break;
            case 2://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto_camera(); // 调用系统的裁剪功能: 拍照专用
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    selectedImg = extras.getParcelable("data");
                    if (selectedImg != null) {
                        // 用ImageView显示出来
                        small_icon.setImageBitmap(selectedImg);
                        headFile = getFileFromBitMap(selectedImg);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //处理权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phoneCamera();
                }
                break;
        }
    }


    //手机拍照
    private void phoneCamera() {
        Intent intent;
        Uri pictureUri;
        // 判断当前系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pictureUri = FileProvider.getUriForFile(mContext, "linkknown.fileProvider", headFile);
        } else {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureUri = Uri.fromFile(headFile);
        }
        // 去拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, 2);
    }


    //调用系统的裁剪功能: 相册选图专用
    public void cropPhoto_album(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }


    //调用系统的裁剪功能: 拍照专用
    public void cropPhoto_camera(){
        // 开始切割
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(FileProvider.getUriForFile(mContext,"linkknown.fileProvider", headFile), "image/*");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, headFileUri); // 返回一个文件
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    };


    //必要的地方才调用此方法来初始化参数
    public void initImgParam(){
        headFile = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
        headFileUri = Environment.getExternalStorageDirectory() + "/head.jpg";
    };

    //BitMap转为文件
    public File getFileFromBitMap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        try {
            headFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(headFile);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headFile;
    }


}
