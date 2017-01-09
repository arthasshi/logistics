package com.ds.arthas.logistics.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.utils.AMapUtil;
import com.ds.arthas.logistics.utils.Logger;
import com.ds.arthas.logistics.utils.ToastUtil;

import java.util.List;


/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MapActivity extends BaseActivity implements LocationSource, AMapLocationListener,CompoundButton.OnCheckedChangeListener ,SearchView.OnQueryTextListener,PoiSearch.OnPoiSearchListener,AMap.OnMarkerClickListener,RouteSearch.OnRouteSearchListener{
    private MapView map;
    private AMap aMap;
    private AMapLocationClientOption locaOpt;
    private AMapLocationClient locaC;
    private OnLocationChangedListener mListener;
    private SearchView sv;
    private LatLng ll;
    private Toolbar title;
    private RouteSearch mRouteSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        DsApplication.getInstance().getActivitys().add(this);
        locaC=new AMapLocationClient(this);
        locaOpt=new AMapLocationClientOption();
        locaOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locaOpt.setInterval(2000);
        locaC.setLocationOption(locaOpt);
        initView(savedInstanceState);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.map_search_meanu,menu);

        SearchManager sm= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv= (SearchView) menu.findItem(R.id.search_map).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(this);

        changeTextViewColor(sv);
        return true;
    }
    public void changeTextViewColor(View view){
        if(view!=null){
            if(view instanceof TextView){
                ((TextView)view).setTextColor(Color.WHITE);
            }
            if(view instanceof ViewGroup){
                ViewGroup vg= (ViewGroup) view;
                for (int i=0;i<vg.getChildCount();i++){
                    changeTextViewColor(vg.getChildAt(i));
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        if(ll!=null){
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,22));
        }
        initData();
        setListener();

    }

    @Override
    protected void onPause() {
        super.onPause();

        map.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    private void setListener() {
        locaC.setLocationListener(this);
        locaC.startLocation();
        aMap.setOnMarkerClickListener(this);

    }

    private void initData() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        MyLocationStyle style=new MyLocationStyle();
        style.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_room_white_36dp));
        style.strokeColor(Color.parseColor("#666666"));
        style.strokeWidth(4);
        aMap.setMyLocationStyle(style);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(22));
        searchAddress(getIntent().getStringExtra("searchStr"));
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

    }

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    public void searchAddress(String str){

        if(str!=null&&!"".equals(str)){
            title.setTitle(str);
            showProgressDialog(str);// 显示进度框
            currentPage = 0;
            query = new PoiSearch.Query(str, "", "廊坊");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);// 设置查第一页
            query.setCityLimit(true);

            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
    }


    private void initView(@Nullable Bundle savedInstanceState) {
        title= (Toolbar) findViewById(R.id.tb_map_title);
        title.setTitleTextColor(getResources().getColor(R.color.write));
        title.setTitle("地图导航");
        setSupportActionBar(title);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        map= (MapView) findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        if(aMap==null){
            aMap=map.getMap();
        }
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(locaC!=null&&mListener!=null){
            if(aMapLocation!=null&&aMapLocation.getErrorCode()==0){
                mListener.onLocationChanged(aMapLocation);
                ll=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
            }else{
                String err= "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",err);
            }

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener=onLocationChangedListener;
        if(locaC==null){
            locaC = new AMapLocationClient(this);
            locaOpt = new AMapLocationClientOption();
            //设置定位监听
            locaC.setLocationListener(this);
            //设置为高精度定位模式
            locaOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            locaC.setLocationOption(locaOpt);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            locaC.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locaC != null) {
            locaC.stopLocation();
            locaC.onDestroy();
        }
        locaC = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchAddress(query);
        Toast.makeText(this,query,Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
                    } else {
//                        ToastUtil.show(PoiKeywordSearchActivity.this,
//                                R.string.no_result);
                    }
                }
            } else {
//                ToastUtil.show(PoiKeywordSearchActivity.this,
//                        R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(PoiKeywordSearchActivity.this, rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
    /**
     * 显示进度框
     */
    private ProgressDialog progDialog = null;// 搜索时进度条
    private void showProgressDialog(String keyWord) {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(MapActivity.this, "mark click", Toast.LENGTH_SHORT).show();
        showProgressDialog("路径规划中");
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo( new LatLonPoint(ll.latitude,ll.longitude),new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude) );
//        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo( new LatLonPoint(39.923271, 116.396034),new LatLonPoint(39.984947, 116.494689) );
        Logger.d("route","slat:"+ll.latitude);
        Logger.d("route","slon:"+ll.longitude);
        Logger.d("route","elat:"+marker.getPosition().latitude);
        Logger.d("route","elat:"+marker.getPosition().longitude);

        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        return false;
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult() {
//        if (mStartPoint == null) {
//            ToastUtil.show(mContext, "定位中，稍后再试...");
//            return;
//        }
//        if (mEndPoint == null) {
//            ToastUtil.show(mContext, "终点未设置");
//        }
//        showProgressDialog("路径规划中");
//        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
//                mStartPoint, mEndPoint);
//        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
//            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
//                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
//            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
//        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
//            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
//                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
//        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
//            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
//            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
//        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    private WalkRouteResult mWalkRouteResult;
    private RelativeLayout mBottomLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+ AMapUtil.getFriendlyLength(dis)+")";
                    Toast.makeText(MapActivity.this, des, Toast.LENGTH_SHORT).show();
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
//                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(MapActivity.this,WalkRouteDetailActivity.class);
//                            intent.putExtra("walk_path", walkPath);
//                            intent.putExtra("walk_result",
//                                    mWalkRouteResult);
//                            startActivity(intent);
//                        }
//                    });
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(MapActivity.this, "对不起，没有搜索到相关数据！");
                }

            } else {
                ToastUtil.show(MapActivity.this,"对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }
}
