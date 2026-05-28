package com.fel.qrswap.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.fel.qrswap.data.Card
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.data.Element
import com.fel.qrswap.ui.theme.toColor
import com.fel.qrswap.weather.RetrofitInstance
import com.fel.qrswap.weather.WeatherResponse
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.fel.qrswap.location.getCurrentLocation

enum class CreateStep {
    ELEMENT,
    DRAW,
    DETAILS
}

private const val USE_MOCK_WEATHER = false
@Composable
fun CreateScreen(
    viewModel: CardViewModel,
    navController: NavController
) {
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var weatherError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var latitude by remember { mutableStateOf(52.23) }
    var longitude by remember { mutableStateOf(21.01) }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                getCurrentLocation(context) { lat, lon ->

                    latitude = lat
                    longitude = lon

                    android.util.Log.d(
                        "LOCATION",
                        "lat=$lat lon=$lon"
                    )
                }
            }
        }

    fun mockWeather(): WeatherResponse {
        return WeatherResponse(
            current = com.fel.qrswap.weather.Current(
                time = "MOCK",
                interval = 900,
                temperature_2m = 8.0,
                is_day = 0,
                rain = 1.0,
                wind_speed_10m = 30.0
            )
        )
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    LaunchedEffect(latitude, longitude){
        try {
            val result = if (USE_MOCK_WEATHER) {
                mockWeather()
            } else {
                RetrofitInstance.api.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude
                )
            }

            weather = result
            weatherError = false

            android.util.Log.d(
                "WEATHER",
                "temp=${result.current.temperature_2m}, " +
                        "rain=${result.current.rain}, " +
                        "wind=${result.current.wind_speed_10m}, " +
                        "isDay=${result.current.is_day}"
            )

        } catch (e: Exception) {
            weather = null
            weatherError = true

            android.util.Log.e("WEATHER", "FAILED REQUEST", e)
        }
    }
    var step by remember { mutableStateOf(CreateStep.ELEMENT) }

    var element by remember { mutableStateOf<Element?>(null) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var hp by remember { mutableStateOf(0) }
    var dmg by remember { mutableStateOf(0) }
    var mana by remember { mutableStateOf(0) }
    var isSpell by remember { mutableStateOf(false) }

    val isFormValid = remember(name, description, element) {
        name.isNotBlank() &&
                description.isNotBlank() &&
                element != null
    }



    fun allowedElements(): List<Element> {

        val w = weather

        if (w == null) {
            return listOf(Element.EARTH)
        }

        val temp = w.current.temperature_2m
        val rain = w.current.rain
        val wind = w.current.wind_speed_10m
        val isDay = w.current.is_day == 1

        val list = mutableListOf<Element>()

        list += Element.EARTH

        if (temp > 10) list += Element.FIRE
        else list += Element.ICE

        if (!isDay) list += Element.DARK

        if (rain > 0.2) list += Element.WATER

        if (wind > 20) list += Element.AIR

        return list
    }

    fun nextStep(step: CreateStep): CreateStep? {
        return when (step) {
            CreateStep.ELEMENT -> CreateStep.DRAW
            CreateStep.DRAW -> CreateStep.DETAILS
            CreateStep.DETAILS -> null
        }
    }

    fun previousStep(step: CreateStep): CreateStep? {
        return when (step) {
            CreateStep.ELEMENT -> null
            CreateStep.DRAW -> CreateStep.ELEMENT
            CreateStep.DETAILS -> CreateStep.DRAW
        }
    }

    fun finishCardCreation() {
        val safeElement = element ?: return

        viewModel.insert(
            Card(
                name = name,
                description = description,
                element = safeElement,
                isSpell = isSpell,
                hp = if (isSpell) null else hp,
                dmg = if (isSpell) null else dmg,
                cost = mana,
                portrait = ByteArray(0)
            )
        )

        navController.navigate("collection") {
            popUpTo("create") { inclusive = true }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (step) {

                CreateStep.ELEMENT -> {
                    if (weatherError) {
                        Text(
                            text = "No internet connection.\nOnly EARTH element is available.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    ElementSelectScreen(
                        allowedElements = allowedElements(),
                        onSelected = {
                            element = it
                            step = CreateStep.DRAW
                        }
                    )
                }

                CreateStep.DRAW -> {
                    DrawScreen(onNext = {
                        step = CreateStep.DETAILS
                    })
                }

                CreateStep.DETAILS -> {
                    CardDetailsForm(
                        name = name,
                        onNameChange = { name = it.take(20) },

                        description = description,
                        onDescriptionChange = { description = it.take(100) },

                        hp = hp,
                        onHpChange = { hp = it.coerceIn(0, 255) },

                        dmg = dmg,
                        onDmgChange = { dmg = it.coerceIn(0, 255) },

                        mana = mana,
                        onManaChange = { mana = it.coerceIn(0, 255) },

                        isSpell = isSpell,
                        onSpellChange = { isSpell = it }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {

            val prev = previousStep(step)
            val next = nextStep(step)

            if (prev != null) {
                Button(onClick = { step = prev }) {
                    Text("Back")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (next != null) {
                Button(onClick = { step = next }) {
                    Text("Next")
                }
            } else {
                Button(
                    onClick = { finishCardCreation() },
                    enabled = isFormValid
                ) {
                    Text("Finish")
                }
            }
        }
    }
}

@Composable
fun ElementSelectScreen(
    allowedElements: List<Element>,
    onSelected: (Element) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Today's Elements:")

            if (allowedElements.isEmpty()) {
                Text("Loading weather...")
                return@Column
            }

            Element.values().forEach { element ->
                val enabled = element in allowedElements

                Button(
                    onClick = { onSelected(element) },
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = element.toColor(),
                        contentColor = Color.White
                    )
                ) {
                    Text(element.name)
                }
            }
        }
    }
}

@Composable
fun DrawScreen(
    onNext: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Drawing screen placeholder (32x32 editor later)")
        }

        Button(
            onClick = onNext,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Next")
        }
    }
}

@Composable
fun CardDetailsForm(
    name: String,
    onNameChange: (String) -> Unit,

    description: String,
    onDescriptionChange: (String) -> Unit,

    hp: Int,
    onHpChange: (Int) -> Unit,

    dmg: Int,
    onDmgChange: (Int) -> Unit,

    mana: Int,
    onManaChange: (Int) -> Unit,

    isSpell: Boolean,
    onSpellChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            TextField(
                value = name,
                onValueChange = { onNameChange(it.take(20)) },
                label = { Text("Name") }
            )

            TextField(
                value = description,
                onValueChange = { onDescriptionChange(it.take(100)) },
                label = { Text("Description") }
            )

            if (!isSpell) {

                TextField(
                    value = hp.toString(),
                    onValueChange = {
                        onHpChange(it.toIntOrNull()?.coerceIn(0, 255) ?: 0)
                    },
                    label = { Text("HP (0–255)") }
                )

                TextField(
                    value = dmg.toString(),
                    onValueChange = {
                        onDmgChange(it.toIntOrNull()?.coerceIn(0, 255) ?: 0)
                    },
                    label = { Text("DMG (0–255)") }
                )
            }

            TextField(
                value = mana.toString(),
                onValueChange = {
                    onManaChange(it.toIntOrNull()?.coerceIn(0, 255) ?: 0)
                },
                label = { Text("Mana (0–255)") }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Creature")

                Switch(
                    checked = isSpell,
                    onCheckedChange = onSpellChange
                )

                Text("Spell")
            }
        }
    }
}