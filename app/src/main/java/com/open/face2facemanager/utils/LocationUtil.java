package com.open.face2facemanager.utils;

public class LocationUtil {
   /* public static LocationClient locationClient = null;

    public static void getLocation(final Action1<BDLocation> action1) {
        locationClient = new LocationClient(TApplication.getInstance());

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                Log.i("onion",
                        "onUtil" + bdLocation.getAddrStr() + "\n" + bdLocation.getLatitude()
                                + "/" + bdLocation.getLongitude());
                Message msg = Message.obtain();
                msg.obj = bdLocation;
                Observable.just(bdLocation).subscribe(action1);
                locationClient.stop();
            }

        }); // 注册监听函数
        setLocationOption(); // 设置定位参数
        Log.i("onion", "locationClient start");
        locationClient.start();
    }

    *//**
     * 设置定位参数
     *//*
    private static void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }*/
}
