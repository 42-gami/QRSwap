package com.fel.qrswap.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fel.qrswap.data.Countries
import com.fel.qrswap.data.Country
import com.fel.qrswap.data.UserProfile

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    var initials by remember { mutableStateOf(UserProfile.initials) }
    var selectedCountry by remember { mutableStateOf(UserProfile.country) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(searchQuery) {
        if (searchQuery.isBlank()) Countries.list
        else Countries.list.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${selectedCountry.flag} $initials",
            fontSize = 48.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = initials,
            onValueChange = {
                initials = it.take(3)
                UserProfile.save(context, initials, selectedCountry)
            },
            label = { Text("Initials (3 characters)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search country") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(filteredCountries) { country ->
                CountryRow(
                    country = country,
                    selected = country == selectedCountry,
                    onClick = {
                        selectedCountry = country
                        searchQuery = ""
                        UserProfile.save(context, initials, selectedCountry)
                    }
                )
            }
        }
    }
}

@Composable
fun CountryRow(
    country: Country,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    Surface(
        color = bg,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${country.flag}  ${country.name}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}