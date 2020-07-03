package com.icandothisallday2020.ex68kakaomap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {
    //카카오 지도는 AVD(애뮬레이터)에서는 동작하지 않음
    //카카오 지도 뷰
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //키해시 얻어오는 메소드 호출
        String keyHash=getKeyHash(this);
        //Log 기록을 남겨 Logcat 창에 키해시 기록
        Log.i("MyKeyHash",/*msg:*/keyHash);

        mapView=new MapView(this);//맵뷰 객체 생성
        RelativeLayout mapContainer=findViewById(R.id.map_container);//맵 뷰가 놓여질 ViewGroup 찾아오기
        mapContainer.addView(mapView);//맵뷰 추가

        //지도 초점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5,126.97),true);

        //줌레벨 변경
        mapView.setZoomLevel(12,true);
        //줌인, 줌아웃
        mapView.zoomIn(true);       mapView.zoomOut(true);
        //마커(POI:Point Of Interest)표시
        MapPOIItem marker=new MapPOIItem();
        marker.setItemName("미래능력개발교육원");
        marker.setTag(0);//식별자
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.5,127.6));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);//선택한 마커 색변경(select marker 표시)
        mapView.addPOIItem(marker);
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
                Toast.makeText(MainActivity.this, ""+mapPOIItem.getItemName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });
    }
    //키해드 자바코드로 얻어오는 메소드[ 라이브러리를 추가한 후에 사용 가능한 코드 ]
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
