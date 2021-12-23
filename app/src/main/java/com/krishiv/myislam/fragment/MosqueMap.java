package com.krishiv.myislam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.Mosque;

public class MosqueMap extends Fragment implements OnMapReadyCallback {
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_map_info, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private GoogleMap mMap;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_event_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getActivity();

        if (Mosque.data.size() <= 0) {
            Mosque.getDataFromBase = new Mosque.GetDataFromBase() {
                @Override
                public void onDataGet() {

                }
            };
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(21.1702, 72.8311);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Education Event"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        setMarkerOnMap();
    }

    private void setMarkerOnMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (Mosque.data.size()>0){
            for (int i=0; i<Mosque.data.size(); i++){
                LatLng sydney = new LatLng(Mosque.data.get(i).getLat(), Mosque.data.get(i).getLng());
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(Mosque.data.get(i).getName())
                        .snippet(Mosque.data.get(i).getFormatted_address()));

                newMarker.setTitle(newMarker.getTitle());
                mMap.addMarker(new MarkerOptions().position(sydney).title(Mosque.data.get(i).getName()));
                builder.include(sydney);
            }
            LatLngBounds bounds = builder.build();
            int padding = 50; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
        }
    }
}
