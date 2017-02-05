package com.liyu.itester;

import java.io.File;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class CameraActivity extends Activity implements OnClickListener{
    private static final int CAPTURE_PIC = 10;  
    private String imageFilePath;  
    private Uri imageFileUri;
    private int mCameraId = 0;
    private static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		initUI();
		openCamera();
		//showBox();
		
	}
	private void initUI()
	{
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.imageView_camera).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		Display display = getWindowManager().getDefaultDisplay();  
	    imageFilePath = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() + "/test.jpg" : null;  
	    imageFileUri = Uri.fromFile(new File(imageFilePath)); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (resultCode == RESULT_OK && requestCode == CAPTURE_PIC) {  
//            Options options = new Options();  
//            options.inJustDecodeBounds = true;//���ý���ֻ��Ϊ�˻�ȡͼƬ��width��heightֵ,������������ȡͼƬ  
//            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);//��������options.outWidth��options.outHeight����ȡͼƬ�ĳߴ�  
//  
//            int widthRatio = (int) Math.ceil(options.outWidth / width);//��ȡ��ȵ�ѹ������  
//            int heightRatio = (int) Math.ceil(options.outHeight / height);//��ȡ�߶ȵ�ѹ������  
//  
//            if (widthRatio > 1 || heightRatio > 1) {//ֻҪ����һ���ı��ʴ���1,˵����Ҫѹ��  
//                if(widthRatio>=heightRatio){//ȡoptions.inSampleSizeΪ��߱����е����ֵ  
//                    options.inSampleSize = widthRatio;  
//                }else{  
//                    options.inSampleSize = heightRatio;  
//                }  
//            }  
//              
//            options.inJustDecodeBounds = false;//����Ϊ�����Ľ���ͼƬ  
//            bitmap = BitmapFactory.decodeFile(imageFilePath, options);//����ͼƬ  
//  
//           imageView.setImageBitmap(bitmap);
			if(Camera.getNumberOfCameras()==2&&mCameraId==0)
			{
				mCameraId = 1;
				openCamera();
			}
			else
            findViewById(R.id.button_pass).setEnabled(true);
        }  
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(jumpUtils.isFastDoubleClick())
			return;
		Intent intent = new Intent();
		switch(v.getId())
		{
		case R.id.button_pass:
			intent.putExtra("pass", true);
			this.setResult(R.id.button_camera,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_camera), CameraActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_camera,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_camera), CameraActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		case R.id.imageView_camera:
			mCameraId = 0;
			openCamera();
			break;
		}
	}
	
	private void openCamera()
	{
		if(Camera.getNumberOfCameras()==0)
		{
			showBox();
			if(FunctionApplication.isAutoMode)
			{
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_camera), CameraActivity.this, XmlUtils.activityClasses.get(0), false);
				this.finish();
			}	
			return;
		}
			
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//�����׽ͼƬ����ͼ  
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);//ָ��ϵͳ������ձ�����imageFileUri��ָ��λ��  
        intent.putExtra(EXTRAS_CAMERA_FACING, mCameraId);
        startActivityForResult(intent, CAPTURE_PIC);//����ϵͳ���,�ȴ�����
	}
	
	private void showBox()
	{
		AlertDialog.Builder dialog=new AlertDialog.Builder(getApplicationContext());
		dialog.setTitle(R.string.title_tips);
		dialog.setIcon(R.drawable.icon);
		dialog.setMessage(getResources().getString(R.string.label_camera_none));
		dialog.setPositiveButton(getResources().getString(R.string.label_iknow),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		AlertDialog mDialog=dialog.create();
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mDialog.show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(CameraActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    CameraActivity.this.setResult(R.id.button_camera,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_camera),CameraActivity.this,XmlUtils.activityClasses.get(0), false);
	    				CameraActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
