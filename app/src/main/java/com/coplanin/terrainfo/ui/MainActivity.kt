package com.coplanin.terrainfo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coplanin.terrainfo.ui.login.LoginScreen
import com.coplanin.terrainfo.ui.login.LoginViewModel
import com.coplanin.terrainfo.ui.map.MapScreen
import com.coplanin.terrainfo.ui.theme.TerrainfoTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.coplanin.terrainfo.ui.predio.AddPredioScreen
import com.coplanin.terrainfo.ui.predio.PredioEditScreen
import com.coplanin.terrainfo.ui.predio.PredioScreen
import com.coplanin.terrainfo.ui.predio.PredioViewModel
import com.coplanin.terrainfo.ui.profile.ProfileScreen
import com.coplanin.terrainfo.ui.terreno.TerrenoEditScreen
import com.google.android.gms.maps.model.LatLng

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("terrainfo_prefs", Context.MODE_PRIVATE)
        val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
        val daysInMillis = 1 * 24 * 60 * 60 * 1000L
        val isLoggedIn = System.currentTimeMillis() - lastLoginTimestamp < daysInMillis

        setContent {
            TerrainfoTheme {
                val navController = rememberNavController()
                val viewModel: PredioViewModel = hiltViewModel()

                // Abrir el GeoPackage dentro de un LaunchedEffect
                LaunchedEffect(Unit) {
                    viewModel.openGeoPackage(this@MainActivity)
                }

                NavHost(navController = navController, startDestination = if (isLoggedIn) "map" else "login") {
                    composable("login") {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginSuccess = {
                                saveLoginTimestamp(sharedPreferences)
                                navController.navigate("map") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("map") {
                        MapScreen(navController = navController)
                    }

                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }

                    composable(
                        "predio/{visitId}",
                        arguments = listOf(navArgument("visitId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val visitId = backStackEntry.arguments?.getString("visitId") ?: ""
                        PredioScreen(navController = navController, visitId = visitId)
                    }

                    composable(
                        "predio_detail/{id}?codigoOrip={codigoOrip}&matricula={matricula}&areaTerreno={areaTerreno}&tipo={tipo}&condicion={condicion}&destino={destino}&areaRegistral={areaRegistral}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.StringType },
                            navArgument("codigoOrip") { type = NavType.StringType; defaultValue = "" },
                            navArgument("matricula") { type = NavType.StringType; defaultValue = "" },
                            navArgument("areaTerreno") { type = NavType.StringType; defaultValue = "" },
                            navArgument("tipo") { type = NavType.StringType; defaultValue = "" },
                            navArgument("condicion") { type = NavType.StringType; defaultValue = "" },
                            navArgument("destino") { type = NavType.StringType; defaultValue = "" },
                            navArgument("areaRegistral") { type = NavType.StringType; defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        PredioEditScreen(
                            id = backStackEntry.arguments?.getString("id") ?: "",
                            codigoOrip = backStackEntry.arguments?.getString("codigoOrip") ?: "",
                            matricula = backStackEntry.arguments?.getString("matricula") ?: "",
                            areaTerreno = backStackEntry.arguments?.getString("areaTerreno") ?: "",
                            tipo = backStackEntry.arguments?.getString("tipo") ?: "",
                            condicion = backStackEntry.arguments?.getString("condicion") ?: "",
                            destino = backStackEntry.arguments?.getString("destino") ?: "",
                            areaRegistral = backStackEntry.arguments?.getString("areaRegistral") ?: "",
                            tipoReferenciaFmiAntiguo = backStackEntry.arguments?.getString("tipoReferenciaFmiAntiguo") ?: "",
                            navController = navController
                        )
                    }

                    composable(
                        "terreno_detail/{id}?etiqueta={etiqueta}&ilcPredio={ilcPredio}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.StringType },
                            navArgument("etiqueta") { type = NavType.StringType; defaultValue = "" },
                            navArgument("ilcPredio") { type = NavType.StringType; defaultValue = "" }
                        )
                    ) { backStackEntry ->
                        TerrenoEditScreen(
                            id = backStackEntry.arguments?.getString("id") ?: "",
                            etiqueta = backStackEntry.arguments?.getString("etiqueta") ?: "",
                            ilcPredio = backStackEntry.arguments?.getString("ilcPredio") ?: "",
                            navController = navController
                        )
                    }

                    composable(
                        "add_predio?lat={lat}&lng={lng}",
                        arguments = listOf(
                            navArgument("lat") { type = NavType.FloatType },
                            navArgument("lng") { type = NavType.FloatType }
                        )
                    ) { backStackEntry ->
                        val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
                        val lng = backStackEntry.arguments?.getFloat("lng") ?: 0f
                        AddPredioScreen(
                            latLng = LatLng(lat.toDouble(), lng.toDouble()),
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    private fun saveLoginTimestamp(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putLong("last_login_timestamp", System.currentTimeMillis())
            .apply()
    }
}
