package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MobileNetActivity extends Activity implements OnClickListener{
	
	 //SIM��״̬����   
    private static final String SIM_ABSENT = "No SIM card"; //�ֻ�����SIM��   
    private static final String SIM_READY = "OK"; //SIM����׼����   
    private static final String SIM_PIN_REQUIRED = "PIN required"; //��ҪSIM����PIN����   
    private static final String SIM_PUK_REQUIRED = "PUK required"; //��ҪSIM����PUK����    
    private static final String SIM_NETWORK_LOCKED = "Network locked"; //��ҪNetwork PIN����   
    private static final String SIM_UNKNOWN = "Unknown"; //״̬δ֪   
      
    //�������ͳ���   
    private static final String NETWORK_CDMA = "CDMA: Either IS95A or IS95B (2G)";  
    private static final String NETWORK_EDGE = "EDGE (2.75G)";  
    private static final String NETWORK_GPRS = "GPRS (2.5G)";  
    private static final String NETWORK_UMTS = "UMTS (3G)";  
    private static final String NETWORK_EVDO_0 = "EVDO revision 0 (3G)";  
    private static final String NETWORK_EVDO_A = "EVDO revision A (3G)";  
    private static final String NETWORK_EVDO_B = "EVDO revision B (3G)";  
    private static final String NETWORK_1X_RTT = "1xRTT  (2G)";  
    private static final String NETWORK_HSDPA = "HSDPA (3G)";  
    private static final String NETWORK_HSUPA = "HSUPA (3G)";  
    private static final String NETWORK_HSPA = "HSPA (3G)";  
    private static final String NETWORK_IDEN = "iDen (2G)";  
    private static final String NETWORK_UNKOWN = "Unknown";  
      
    //�ֻ���ʽ���ͳ���   
    private static final String PHONE_CDMA = "CDMA";  
    private static final String PHONE_GSM = "GSM";  
    private static final String PHONE_NONE = "No radio";  
	final String TAG = "MobileActivity";
	TextView tv_mobile;
	TelephonyManager tm;
	private String PhoneType,Offer,SIM,Stren,NetType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_net);
		 initUI();
    }
    
	private void initUI()
	{
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);  
		tv_mobile = (TextView)findViewById(R.id.textView_mobile);
		tv_mobile.setTextColor(Color.RED);
		tv_mobile.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		
		   updateViews(tm);  
	        //����SIM��״̬��������Ϣ��ʱ�����ܷ����仯�������Ҫע��PhoneStateListener   
	        //��ʵʱ���½�����ʾ����Ϣ��������������¼���LISTEN_SERVICE_STATE��LISTEN_DATA_CONNECTION_STATE   
	        //���Ƿֱ��Ӧ�ص�����onServiceStateChanged��onDataConnectionStateChanged   
	        tm.listen(new PhoneStateListener() {  
	  
	            @Override  
	            public void onDataConnectionStateChanged(int state, int networkType) {  
	                //��������״̬�ı���ܵ����������͵ĸı�   
	                updateViews(tm);  
	            }  
	  
	            @Override  
	            public void onServiceStateChanged(ServiceState serviceState) {  
	                updateViews(tm);  
	            }

				@Override
				public void onSignalStrengthsChanged(SignalStrength signalStrength) {
					// TODO Auto-generated method stub
					Stren=String.valueOf(-113+2*signalStrength.getGsmSignalStrength())+" dBm  "+String.valueOf(signalStrength.getGsmSignalStrength())+" asu";
					updateViews(tm);
					super.onSignalStrengthsChanged(signalStrength);
				} 
	            
	              
	        }, PhoneStateListener.LISTEN_SERVICE_STATE   
	                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE|PhoneStateListener.LISTEN_SIGNAL_STRENGTHS); 
	 }

	
	/** 
     * ����SIM��״̬��������Ϣ 
     * @param tm 
     */  
    private final void updateViews(TelephonyManager tm) {  
        
            if(PhoneType==null){
    	    PhoneType=mapDeviceTypeToName(tm.getPhoneType());
            }
	    	
	    	Offer=tm.getNetworkOperatorName();
	    	
	    	SIM=mapSimStateToName(tm.getSimState());
	    	
	    	NetType=mapNetworkTypeToName(tm.getNetworkType());  
	    	
	    //	if(PhoneType.equals(PHONE_NONE))
	    //	{
	    //		tv_mobile.setText("��֧���ƶ�����!");
	    //		return;
	    //	}
	    	if(NetType.equals(NETWORK_UNKOWN))
	    	{
	    		Stren = "";
	    	}
	    	tv_mobile.setText(getResources().getString(R.string.label_phone_type)+PhoneType);
	    	tv_mobile.append(getResources().getString(R.string.label_phone_carrier)+Offer);
	    	//tv_mobile.append("\nSIM��״̬: "+SIM);
	    	tv_mobile.append(getResources().getString(R.string.label_phone_net_type)+NetType);
	    	tv_mobile.append(getResources().getString(R.string.label_phone_signalstrength)+Stren);
	    	if(Offer!=null&&SIM.equals(SIM_READY)&&!NetType.equals(NETWORK_UNKOWN))
	    	{
	    		findViewById(R.id.button_pass).setEnabled(true);
	    		tv_mobile.setTextColor(Color.GREEN);
	    		if(PhoneType.equals(PHONE_NONE))
	    		{
	    			PhoneType = getResources().getString(R.string.label_phone_dongle);
	    		}
	    	}
	    	else
	    	{
	    		PhoneType=mapDeviceTypeToName(tm.getPhoneType());
	    	}
	    	
          
    }  
    /** 
     * ���ֻ���ʽֵ���ַ�����ʽ���� 
     * @param phoneType 
     * @return 
     */  
    private String mapDeviceTypeToName(int phoneType) {  
        switch (phoneType) {  
        case TelephonyManager.PHONE_TYPE_CDMA:  
            return PHONE_CDMA;  
        case TelephonyManager.PHONE_TYPE_GSM:  
            return PHONE_GSM;  
        case TelephonyManager.PHONE_TYPE_NONE:  
            return PHONE_NONE;  
        default: 
            //��Ӧ���ߵ������֧   
            return null;  
        }  
    }  
    /** 
     * ����������ֵ���ַ�����ʽ���� 
     * @param networkType 
     * @return 
     */  
    private String mapNetworkTypeToName(int networkType) {  
        switch (networkType) {  
        case TelephonyManager.NETWORK_TYPE_CDMA:  
            return NETWORK_CDMA;  
        case TelephonyManager.NETWORK_TYPE_EDGE:  
            return NETWORK_EDGE;  
        case TelephonyManager.NETWORK_TYPE_GPRS:  
            return NETWORK_GPRS;  
        case TelephonyManager.NETWORK_TYPE_UMTS:  
            return NETWORK_UMTS;  
        case TelephonyManager.NETWORK_TYPE_EVDO_0:  
            return NETWORK_EVDO_0;  
        case TelephonyManager.NETWORK_TYPE_EVDO_A:  
            return NETWORK_EVDO_A;  
        case TelephonyManager.NETWORK_TYPE_EVDO_B:  
            return NETWORK_EVDO_B;  
        case TelephonyManager.NETWORK_TYPE_1xRTT:  
            return NETWORK_1X_RTT;  
        case TelephonyManager.NETWORK_TYPE_HSDPA:  
            return NETWORK_HSDPA;  
        case TelephonyManager.NETWORK_TYPE_HSPA:  
            return NETWORK_HSPA;  
        case TelephonyManager.NETWORK_TYPE_HSUPA:  
            return NETWORK_HSUPA;  
        case TelephonyManager.NETWORK_TYPE_IDEN:  
            return NETWORK_IDEN;  
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:  
        default:  
            return NETWORK_UNKOWN;  
        }  
    }  
  
    /** 
     * ��SIM��״ֵ̬���ַ�����ʽ���� 
     * @param simState 
     * @return 
     */  
    private static String mapSimStateToName(int simState) {  
        switch (simState) {  
        case TelephonyManager.SIM_STATE_ABSENT:  
            return SIM_ABSENT;  
        case TelephonyManager.SIM_STATE_READY:  
            return SIM_READY;  
        case TelephonyManager.SIM_STATE_PIN_REQUIRED:  
            return SIM_PIN_REQUIRED;  
        case TelephonyManager.SIM_STATE_PUK_REQUIRED:  
            return SIM_PUK_REQUIRED;  
        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:  
            return SIM_NETWORK_LOCKED;  
        case TelephonyManager.SIM_STATE_UNKNOWN:  
            return SIM_UNKNOWN;  
        default:  
            //��Ӧ���ߵ������֧   
            return null;  
        }  
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
			this.setResult(R.id.button_mobile,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mobile_net), MobileNetActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_mobile,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mobile_net), MobileNetActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(MobileNetActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    MobileNetActivity.this.setResult(R.id.button_mobile,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mobile_net),MobileNetActivity.this,XmlUtils.activityClasses.get(0), false);
	    				MobileNetActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}