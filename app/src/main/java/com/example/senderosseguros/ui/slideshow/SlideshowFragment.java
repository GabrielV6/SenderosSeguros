package com.example.senderosseguros.ui.slideshow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.BuildConfig;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentSlideshowBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class SlideshowFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSlideshowBinding binding;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "Mis mapas";
    private RequestQueue requestQueue;

    // Register the permissions launcher
    private final ActivityResultLauncher<String[]> requestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if (fineLocationGranted != null && fineLocationGranted) {
                    enableLocation();
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    enableLocation();
                } else {
                    Log.i(TAG, "Permisos de ubicacion denegados.");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(requireActivity());
        //Botón para reportar obstáculos
        //Botón para reportar obstáculos
        //Botón para reportar obstáculos
        // Botón para reportar obstáculos
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtén la referencia al FrameLayout usando binding
                FrameLayout frameLayout = binding.frameLayout;

                // Alterna la visibilidad del FrameLayout
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE); // Ocultar si está visible
                } else {
                    frameLayout.setVisibility(View.VISIBLE); // Mostrar si está oculto
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
        rutaEnMapa(mMap);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origen de la ruta
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destino de la ruta
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Modo de viaje
        String mode = "mode=driving";
        // Construye los parámetros para el web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Formato de salida
        String output = "json";
        // URL de la solicitud
        Properties properties = new Properties();
        String mapsapikey = getResources().getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + mapsapikey;
        return url;
    }

    private void drawRoute(LatLng origin, LatLng dest) {
        String url = getDirectionsUrl(origin, dest);

        // Crea una solicitud JsonObjectRequest
        com.android.volley.toolbox.JsonObjectRequest jsonObjectRequest = new com.android.volley.toolbox.JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extrae la polilínea de la respuesta JSON
                        JSONArray routes = response.getJSONArray("routes");
                        JSONObject route = routes.getJSONObject(0);
                        JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                        String encodedPolyline = overviewPolyline.getString("points");

                        // Decodifica la polilínea
                        List<LatLng> points = PolyUtil.decode(encodedPolyline);

                        // Crea la polilínea y agrégala al mapa
                        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                .addAll(points)
                                .width(5)
                                .color(Color.BLUE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Maneja el error
            error.printStackTrace();
        });

        // Agrega la solicitud a la cola de Volley
        requestQueue.add(jsonObjectRequest);
    }

    private void rutaEnMapa(GoogleMap mMap) {
        /*PolylineOptions polylineOptions = new PolylineOptions().color(Color.BLUE).width(5f);
        polylineOptions.add(new LatLng(40.7128, -74.0060));
        polylineOptions.add(new LatLng(34.0522, -118.2437));
        mMap.addPolyline(polylineOptions);*/
        LatLng origin = new LatLng(40.7128, -74.0060);
        LatLng dest = new LatLng(34.0522, -118.2437);
        drawRoute(origin, dest);
    }

    private void enableLocation() {
        try {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } catch (SecurityException e) {
            Log.e(TAG, "Verifique los permisos de ubicacion: ", e);
        }
    }

    private void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi ubicacion"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                        }
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "Verifique los permisos de ubicacion: ", e);
        }
    }
}
