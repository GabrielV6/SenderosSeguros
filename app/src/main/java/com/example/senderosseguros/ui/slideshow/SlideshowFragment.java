package com.example.senderosseguros.ui.slideshow;

import com.example.senderosseguros.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.senderosseguros.conexion.AccesoDatos;
import com.example.senderosseguros.databinding.FragmentSlideshowBinding;
import com.example.senderosseguros.entidad.Obstaculo;
import com.example.senderosseguros.entidad.ObstaculoMarcadores;
import com.example.senderosseguros.entidad.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Properties;

public class SlideshowFragment extends Fragment implements OnMapReadyCallback {

    private FragmentSlideshowBinding binding;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "Mis mapas";
    private RequestQueue requestQueue;
    private LatLng punto1, punto2; // Variables para guardar las coordenadas de dos puntos
    private boolean isFirstPoint = false; // Variable para alternar entre el primer y segundo punto

    private FloatingActionButton likeButton;
    private FloatingActionButton trashButton;


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

        trashButton = binding.trash;
        likeButton = binding.like;
        trashButton.setVisibility(View.INVISIBLE);
        likeButton.setVisibility(View.INVISIBLE);
        likeButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Botón de 'like' clickeado", Toast.LENGTH_SHORT).show();
            likeButton.setVisibility(View.INVISIBLE); // Oculta el botón después de hacer clic
        });
        trashButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Botón de 'trash' clickeado", Toast.LENGTH_SHORT).show();
            trashButton.setVisibility(View.INVISIBLE); // Oculta el botón después de hacer clic
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(requireActivity());


        // Configura los OnClickListeners para las opciones
        setUpOptionButtons();

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

    private void setUpOptionButtons() {
        binding.iconOption1.setOnClickListener(v -> handleOptionSelection("1"));
        binding.iconOption2.setOnClickListener(v -> handleOptionSelection("2"));
        binding.iconOption3.setOnClickListener(v -> handleOptionSelection("3"));
        binding.iconOption4.setOnClickListener(v -> handleOptionSelection("4"));
        binding.iconOption5.setOnClickListener(v -> handleOptionSelection("5"));
        binding.iconOption6.setOnClickListener(v -> handleOptionSelection("6"));
    }

    private void handleOptionSelection(String option) {
        Toast.makeText(getContext(), "Opción seleccionada: " + option, Toast.LENGTH_SHORT).show();
        binding.frameLayout.setVisibility(View.GONE);
        // TO DO: ACA VA LA LOGICA PARA PROCESAR EL OBSTACULO REPORTADO...
        getCurrentLocation();

        Bundle bundle = new Bundle();
        bundle.putString("selected_option", option); // Pasar la opción seleccionada
        bundle.putDouble("latitud", punto1.latitude);  // Latitud del punto1
        bundle.putDouble("longitud", punto1.longitude); // Longitud del punto1
        
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_reportar, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

        obstaculosMarcados();

        mMap.setOnMarkerClickListener(marker -> {
            AccesoDatos accesoDatos = new AccesoDatos(requireContext());
            String markerTitle = marker.getTitle();
            String latitudObstaculo = String.valueOf(marker.getPosition().latitude);
            String longitudObstaculo = String.valueOf(marker.getPosition().longitude);
            int id_user_login = SessionManager.getInstance().getID_User();
            accesoDatos.obtenerID_Punto(latitudObstaculo, longitudObstaculo, idPunto -> {
                // Aquí ya tienes el idPunto después de que se obtenga
                Toast.makeText(requireContext(), "ID Punto obtenido: " + idPunto, Toast.LENGTH_SHORT).show();

                // Ahora puedes usar el idPunto para obtener el obstáculo
                Obstaculo obstaculo = accesoDatos.obtenerObstaculo(idPunto);
                int id_obstaculo = obstaculo.getIdObstaculo();
                boolean estaPuntuado = accesoDatos.chequearCalificado(id_user_login, id_obstaculo);
                // Si necesitas hacer algo más con el idPunto o el obstáculo, lo puedes hacer aquí //si esta puntuado 1(true) sino 0,
                if (!estaPuntuado){
                    likeButton.setVisibility(View.VISIBLE);
                likeButton.setOnClickListener(v -> {

                    boolean calificado = accesoDatos.registrarPuntuacion(id_user_login, id_obstaculo); // Ejemplo de acción, registrar puntuación
                    if(calificado) {
                        Toast.makeText(requireContext(), "¡Has puntuado este obstáculo!", Toast.LENGTH_SHORT).show();
                        likeButton.setVisibility(View.INVISIBLE); // Ocultar el botón después de puntuar
                    }
                });}
                else
                    likeButton.setVisibility(View.INVISIBLE);
            });

            // Este Toast se ejecuta de inmediato y no tiene acceso a idPunto, porque el código que obtiene el idPunto es asincrónico.
            Toast.makeText(getContext(), "Obstáculo " + markerTitle, Toast.LENGTH_SHORT).show();


            trashButton.setVisibility(View.VISIBLE);


            return false;
        });

        // Habilita el listener para capturar clics en el mapa
        mMap.setOnMapClickListener(latLng -> {
            likeButton.setVisibility(View.INVISIBLE);
            trashButton.setVisibility(View.INVISIBLE);
            if (punto1!=null && punto2!=null) {
                mMap.clear();
                getCurrentLocation();
                obstaculosMarcados();
                punto2 = null;
                isFirstPoint = false;
            }
            // Guarda las coordenadas del clic
            if (isFirstPoint) {
                punto1 = latLng;
                Toast.makeText(getContext(), "Punto 1 seleccionado: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            } else {
                punto2 = latLng;
                Toast.makeText(getContext(), "Punto 2 seleccionado: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }

            // Alterna para que el siguiente clic guarde el segundo punto (o el primero, si ya están ambos guardados)
            isFirstPoint = !isFirstPoint;

            // Limpia el mapa y agrega ambos puntos como marcadores (si existen)
            //mMap.clear();
            if (punto1 != null) {
                mMap.addMarker(new MarkerOptions().position(punto1).title("Punto 1"));
            }
            if (punto2 != null) {
                mMap.addMarker(new MarkerOptions().position(punto2).title("Punto 2"));
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableLocation();
        } else {
            requestPermissionsLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (punto1 != null && punto2 != null) {
                    rutaEnMapa(mMap);
                }
            }
        });
        //rutaEnMapa(mMap);
    }

    private void obstaculosMarcados() {
        AccesoDatos accesoDatos = new AccesoDatos(requireContext());
        List<ObstaculoMarcadores> obstacles = accesoDatos.getObstaculos();

        for (ObstaculoMarcadores obstaculo : obstacles) {
            LatLng position = new LatLng(obstaculo.getLatitud(), obstaculo.getLongitud());
            String title = "Tipo: " + obstaculo.getDescripcionObstaculo();

            switch (obstaculo.getIdTipoObstaculo()) {
                case 1:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cascochica)));
                    break;
                case 2:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pisochica)));
                    break;
                case 3:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.rampachica)));
                    break;
                case 4:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.peligrochica)));
                    break;
                case 5:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vallachica)));
                    break;
                case 6:
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(title)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.conochica)));
                    break;
            }

        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origen de la ruta
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destino de la ruta
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Modo de viaje
        String mode = "mode=walking";
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
                                .width(8)
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
        LatLng origin = new LatLng(punto1.latitude, punto1.longitude);
        LatLng dest = new LatLng(punto2.latitude, punto2.longitude);
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
                            punto1 = currentLocation;
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Mi ubicacion"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                        }
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "Verifique los permisos de ubicacion: ", e);
        }
    }

}
