package com.techiteasy.campusatlas.ui.mainscreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techiteasy.campusatlas.ui.components.Searchbar
import com.techiteasy.campusatlas.ui.panels.BookmarksScreen
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalInspectionMode
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.BoundingBox
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mapview(
    navController: NavController,
    isAdminMode: Boolean = false,
    isEditorMode: Boolean = true,
    onEditorModeChange: (Boolean) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var searchText by remember { mutableStateOf("") }
    
    // Sync sheet visibility with the current navigation route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isBookmarksRoute = currentRoute == "bookmarks"
    
    val scope = rememberCoroutineScope()
    
    // Initialize sheet state based on whether we are starting on the bookmarks route
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = if (isBookmarksRoute) SheetValue.Expanded else SheetValue.PartiallyExpanded
        )
    )

    // Handle updates when the route changes (e.g. clicking bottom nav)
    LaunchedEffect(currentRoute) {
        if (isBookmarksRoute) {
            scope.launch { sheetState.bottomSheetState.expand() }
        } else if (currentRoute == "map" || currentRoute == "admin_map") {
            scope.launch { sheetState.bottomSheetState.partialExpand() }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 0.dp,
            sheetDragHandle = { if (isBookmarksRoute) DragHandle() },
            sheetContainerColor = colorScheme.surface,
            sheetContent = {
                if (isBookmarksRoute) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.92f)
                    ) {
                        BookmarksScreen()
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Check if we are in preview mode to avoid loading the osmdroid MapView class,
                    // which can cause NoClassDefFoundError in the Android Studio Layoutlib.
                    if (LocalInspectionMode.current) {
                        Text("OpenStreetMap Map Component (Preview)")
                    } else {
                        OsmMapComponent()
                    }
                }

                if (isAdminMode) {
                    Box(modifier = Modifier.statusBarsPadding()) {
                        AdminMapOverlay(
                            navController = navController,
                            isEditorMode = isEditorMode,
                            onEditorModeChange = onEditorModeChange
                        )
                    }
                } else {
                    Searchbar(
                        searchText = searchText,
                        onSearchChanged = { searchText = it },
                        onSettingsClick = {
                            navController.navigate("settings") { launchSingleTop = true }
                        },
                        modifier = Modifier
                            .statusBarsPadding()
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

// Global object to persist map camera state across recompositions and NavHost changes
object MapCameraState {
    var zoom by mutableDoubleStateOf(20.40)
    var lat by mutableDoubleStateOf(11.110663)
    var lon by mutableDoubleStateOf(122.643741)
    var rotation by mutableFloatStateOf(9.7f)
}

/**
 * A wrapper for the OpenStreetMap MapView.
 */
@Composable
private fun OsmMapComponent() {
    var currentZoom by remember { mutableStateOf(MapCameraState.zoom) }
    var currentLat by remember { mutableStateOf(MapCameraState.lat) }
    var currentLon by remember { mutableStateOf(MapCameraState.lon) }
    var currentRotation by remember { mutableStateOf(MapCameraState.rotation) }
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapView = remember(context) {
        createMapView(context)
    }

    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)

        // Make sure the map is active when it first appears.
        mapView.onResume()

        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onPause()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            update = { },
            modifier = Modifier.fillMaxSize()
        )
        
        // Live Debug Overlay
        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 140.dp, end = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "LIVE DEBUG", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Zoom: ${kotlin.String.format("%.2f", currentZoom)}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Rot: ${kotlin.String.format("%.1f°", currentRotation)}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Lat: ${kotlin.String.format("%.6f", currentLat)}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Lon: ${kotlin.String.format("%.6f", currentLon)}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private fun createMapView(ctx: Context): MapView {
    return MapView(ctx).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        setBuiltInZoomControls(false) // removes + and - buttons
        minZoomLevel = 18.62
        maxZoomLevel = 20.90

        // Box the map to the school area (ISAT U Miagao vicinity).
        val schoolBounds = BoundingBox(11.1127, 122.6458, 11.1086, 122.6417)
        setScrollableAreaLimitDouble(schoolBounds)

        // Enable Rotation
        val rotationGestureOverlay = org.osmdroid.views.overlay.gestures.RotationGestureOverlay(this)
        rotationGestureOverlay.isEnabled = true
        overlays.add(rotationGestureOverlay)

        // Fast update GPS Provider powered by Google's Fused Provider Engine
        val gpsProvider = com.techiteasy.campusatlas.utils.GoogleFusedLocationProvider(ctx)

        // Show User Location with custom blue dot
        val locationOverlay = org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay(gpsProvider, this)

        // Create mimicking blue dot bitmap
        val size = 100 // Increased size to prevent shadow clipping
        val bitmap = android.graphics.Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)

        val center = size / 2f
        // Matching Google Maps exact visual weight: Thin white border, large blue center
        val outerRadius = 24f
        val innerRadius = 20f

        // Draw White Border with Drop Shadow
        paint.color = android.graphics.Color.WHITE
        // The shadow layer requires drawing onto a software bitmap
        paint.setShadowLayer(12f, 0f, 6f, android.graphics.Color.argb(130, 0, 0, 0))
        canvas.drawCircle(center, center, outerRadius, paint)

        // Clear shadow for the next draw
        paint.clearShadowLayer()

        // Draw Inner Blue Dot
        paint.color = android.graphics.Color.parseColor("#1A73E8") // Vibrant Google Maps Blue
        canvas.drawCircle(center, center, innerRadius, paint)

        // Create mimicking blue dot WITH CONE for direction
        val dirSize = 140 // Larger to accommodate the sweeping cone
        val dirBitmap = android.graphics.Bitmap.createBitmap(dirSize, dirSize, android.graphics.Bitmap.Config.ARGB_8888)
        val dirCanvas = android.graphics.Canvas(dirBitmap)
        val conePaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
        val dirCenter = dirSize / 2f

        // Draw semi-transparent fading cone pointing UP (-90 degrees)
        conePaint.shader = android.graphics.RadialGradient(
            dirCenter, dirCenter, dirCenter,
            intArrayOf(android.graphics.Color.argb(120, 26, 115, 232), android.graphics.Color.TRANSPARENT),
            null, android.graphics.Shader.TileMode.CLAMP
        )
        val rectF = android.graphics.RectF(0f, 0f, dirSize.toFloat(), dirSize.toFloat())
        dirCanvas.drawArc(rectF, -120f, 60f, true, conePaint) // Center at -90, sweep 60 degrees

        // Reset shader and draw the exact same blue dot in the center of the cone
        conePaint.shader = null
        conePaint.color = android.graphics.Color.WHITE
        conePaint.setShadowLayer(12f, 0f, 6f, android.graphics.Color.argb(130, 0, 0, 0))
        dirCanvas.drawCircle(dirCenter, dirCenter, outerRadius, conePaint)
        conePaint.clearShadowLayer()
        conePaint.color = android.graphics.Color.parseColor("#1A73E8")
        dirCanvas.drawCircle(dirCenter, dirCenter, innerRadius, conePaint)

        // Ensure the cone ALWAYS shows, whether moving or stationary
        locationOverlay.setPersonIcon(dirBitmap)
        locationOverlay.setDirectionArrow(dirBitmap, dirBitmap)

        locationOverlay.enableMyLocation()
        overlays.add(locationOverlay)

        val campusCenter = GeoPoint(MapCameraState.lat, MapCameraState.lon)
        controller.setZoom(MapCameraState.zoom)
        controller.setCenter(campusCenter)
        mapOrientation = MapCameraState.rotation

        // Listener to update debug state and global camera state
        addMapListener(object : org.osmdroid.events.MapListener {
            override fun onScroll(event: org.osmdroid.events.ScrollEvent?): Boolean {
                MapCameraState.lat = mapCenter.latitude
                MapCameraState.lon = mapCenter.longitude
                MapCameraState.rotation = mapOrientation
                return false
            }

            override fun onZoom(event: org.osmdroid.events.ZoomEvent?): Boolean {
                MapCameraState.zoom = zoomLevelDouble
                MapCameraState.rotation = mapOrientation
                return false
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun MapviewPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Mapview(navController = navController)
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MapviewDarkPreview() {
    CampusAtlasTheme {
        val navController = rememberNavController()
        Mapview(navController = navController, isAdminMode = true)
    }
}
