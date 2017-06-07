package linchange.example.com.supergreattest.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import linchange.example.com.supergreattest.R;
import linchange.example.com.supergreattest.utils.LogUtils;

//我的位置界面
public class LocationActivity extends BaseActivity {
    private static final String TAG = "LocationActivity";

    public static final int SET_TEXT = 100041; //设置文字

    private MapView mMapView; //百度地图显示控件
    private BaiduMap mBaiduMap; //百度地图

    private TextView tv_location; //当前位置文字显示

    public LocationClient mLocationClient = null; //定位客户端
    public BDLocationListener myListener = new MyLocationListener(); //定位监听器

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_TEXT: //给位置显示控件设置文字
                    Bundle bundle = msg.getData(); //取出包裹
                    String text = bundle.getString("position", "无法获取当前位置"); //获取位置信息
                    tv_location.setText(text); //给显示位置组件设置文字
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initView(); //初始化View

        /**
         * 1.定位
         * 2.绘制图层
         */
    }

    //初始化View
    private void initView() {
        //绑定视图
        mMapView = (MapView) findViewById(R.id.mMapView);
        tv_location = (TextView) findViewById(R.id.tv_location);

        //初始化百度地图
        mBaiduMap = mMapView.getMap();

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener( myListener );

        initLocation(); //初始化百度地图的参数配置

        //开始定位
        LogUtils.i(TAG, "开始定位");
        mLocationClient.start();
    }

    //初始化百度地图的参数配置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");

        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span=3000;
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option); //为定位客户端设置选项
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    //百度地图回调监听器
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation){

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

                LogUtils.i(TAG, "location.getLocationDescribe()=" + location.getLocationDescribe());
                handlerToSetText(location); //地址定位成功时设置文字提示

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

                LogUtils.i(TAG, "location.getLocationDescribe()=" + location.getLocationDescribe());
                handlerToSetText(location); //地址定位成功时设置文字提示

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

                LogUtils.i(TAG, "location.getLocationDescribe()=" + location.getLocationDescribe());
                handlerToSetText(location); //地址定位成功时设置文字提示

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            //定位的结果
//            LogUtils.i(TAG, "BaiduLocationApiDem=" + sb.toString());
            LogUtils.i(TAG, "结束定位");

            //移动到我的位置
            //设置缩放比例，确保屏幕内有当前位置
            MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(18);
            //设置百度地图的缩放状态
            mBaiduMap.setMapStatus(mapUpdate);

            //开始移动（设置经纬度）
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory
                    .newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            //设置百度地图的经纬度状态
            mBaiduMap.setMapStatus(mapLatlng);

            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_map_locate);

            if (bitmap != null) { //当生成的bitmap
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
            }


        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 使用handler给显示位置的组件设置文字
     * @param location 当前位置
     */
    private void handlerToSetText(BDLocation location) {
        Message message = new Message(); //新建消息
        message.what = SET_TEXT; //设置消息的类型为设置文字

        Bundle bundle = new Bundle(); //新建包裹
        bundle.putString("position", "当前位置为：" + location.getLocationDescribe()); //设置包裹内容

        message.setData(bundle); //把包裹放入消息中

        mHandler.sendMessage(message); //发送消息
    }
}
