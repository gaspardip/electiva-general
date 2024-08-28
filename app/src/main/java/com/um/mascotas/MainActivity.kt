package com.um.mascotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        MyMinimalScaffold()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMinimalScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hello, Material 3") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to Jetpack Compose with Material 3!")

                // Button
                Button(onClick = { /* Handle button click */ }) {
                    Text("Click Me")
                }

                // Outlined Button
                OutlinedButton(onClick = { /* Handle button click */ }) {
                    Text("Outlined Button")
                }

                // Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Card Title", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "This is a sample card content.")
                    }
                }

                // Switch
                var isChecked by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                    Text(text = if (isChecked) "Switch ON" else "Switch OFF")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MyMinimalScaffoldPreview() {
    MyApp()
}
