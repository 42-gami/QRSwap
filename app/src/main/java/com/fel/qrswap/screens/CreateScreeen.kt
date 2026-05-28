package com.fel.qrswap.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fel.qrswap.data.Card
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.data.Element
import com.fel.qrswap.weather.RetrofitInstance

enum class CreateStep {
    ELEMENT,
    DRAW,
    DETAILS
}

@Composable
fun CreateScreen(
    viewModel: CardViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {

        val weather =
            RetrofitInstance.api.getCurrentWeather(
                latitude = 52.23,
                longitude = 21.01
            )

        println(weather.current.temperature_2m)
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

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (step) {

                CreateStep.ELEMENT -> {
                    ElementSelectScreen(
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
    onSelected: (Element) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choose Element")

            Element.values().forEach { element ->
                Button(onClick = { onSelected(element) }) {
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