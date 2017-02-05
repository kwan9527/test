package com.liyu.itester.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class GetPlatform {
	public static String getCPUname() {
    	String str = "", strCPU = "", cpuVendor = "";
    	try {
    	//��ȡCPU��Ϣ
    	Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
    	InputStreamReader ir = new InputStreamReader(pp.getInputStream());
    	LineNumberReader input = new LineNumberReader(ir);
    	//����CPU���к�
    	for (int i = 1; i < 100; i++) {
    	str = input.readLine();
    	if (str != null) {
    	//���ҵ����к�������
    	if (str.indexOf("Hardware") > -1) {
    	//��ȡ���к�
    	strCPU = str.substring(str.indexOf(":") + 1,
    	str.length());
    	//ȥ�ո�
    	cpuVendor = strCPU.trim();
    	break;
    	}
    	}else{
    	//�ļ���β
    	break;
    	}
    	}
    	} catch (IOException ex) {
    	//����Ĭ��ֵ
    	ex.printStackTrace();
    	}
    	
    	return cpuVendor;
    	}
}
