package com.example.currency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currency.data.MainViewModel
import com.example.currency.data.UIEvent
import com.example.currency.data.UIState
import com.example.currency.ui.theme.CurrencyTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<MainViewModel>()

                    CurrencyConverterScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = viewModel.uiState.collectAsState().value,
                        onEvent = viewModel::onEvent
                    )

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyTheme {
        //Greeting("Android")

        CurrencyConverterScreen(uiState = UIState(), onEvent = {})
    }
}

@Composable
fun CurrencyConverterScreen(
    modifier: Modifier = Modifier,
    uiState: UIState,
    onEvent: (UIEvent) -> Unit,
) = with(uiState) {


    @Composable
    fun CurrencyDropdownMenu(
        selectedCurrency: String = "EGP",
        modifier: Modifier = Modifier,
        onCurrencySelected: (String) -> Unit,
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = modifier) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedCurrency)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                symbols.forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            onCurrencySelected(currency)
                            expanded = false
                        },
                        text = {
                            Text(text = currency)
                        }
                    )
                }
            }
        }
    }


    @Composable
    fun CurrencyTextField(
        amount: String,
        onValueChange: (String) -> Unit,
        label: @Composable (() -> Unit),
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
    ) {
        OutlinedTextField(
            value = amount,
            label = label,
            onValueChange = onValueChange,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = enabled,
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Currency Converter", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CurrencyDropdownMenu(selectedCurrency = form, modifier = Modifier.weight(1f)) {
                onEvent.invoke(UIEvent.From(it))
            }

            TextButton(onClick = {
                onEvent.invoke(UIEvent.Swipe)
            }) {
                Text(text = "Swap")
            }
            CurrencyDropdownMenu(selectedCurrency = to, modifier = Modifier.weight(1f)) {
                onEvent.invoke(UIEvent.To(it))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            ) {
            CurrencyTextField(
                amount = amount,
                onValueChange = {
                    onEvent(UIEvent.Amount(it))
                },
                modifier = Modifier
                    .weight(0.5f),

                label = { Text(text = "amount") },
            )
            Text(
                text = "=",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(5.dp)
            )
            CurrencyTextField(
                amount = result,
                enabled = false,
                onValueChange = {

                },
                modifier = Modifier.weight(0.5f)
                   ,
                label = { Text(text = "result") },
            )
        }

        AnimatedVisibility(visible = isLoading) {
            Text(text = "loading ...")
        }
        AnimatedVisibility(visible = error!=null) {
            Text(text = error.toString())
        }


    }
}
