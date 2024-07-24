package com.dbssoftware.composedeneme

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment

class CustomRequestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyApp() {
    var endpoint by remember { mutableStateOf("") }
    var route by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("GET") }
    var requestType by remember { mutableStateOf("Sync") }
    val headerParams = remember { mutableStateListOf("" to "") }
    val queryParams = remember { mutableStateListOf("" to "") }
    val bodyParams = remember { mutableStateListOf("" to "") }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Allow whole screen to scroll
        ) {
            Text(
                text = "Send Custom Request",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = endpoint,
                onValueChange = { endpoint = it },
                label = { Text("Endpoint") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = route,
                onValueChange = { route = it },
                label = { Text("Route") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Text(
                text = "Method",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = method == "GET",
                    onClick = { method = "GET" }
                )
                Text(text = "GET", modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = method == "POST",
                    onClick = { method = "POST" }
                )
                Text(text = "POST", modifier = Modifier.padding(start = 8.dp))
            }

            Text(
                text = "Execution Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = requestType == "Sync",
                    onClick = { requestType = "Sync" }
                )
                Text(text = "Sync", modifier = Modifier.padding(start = 8.dp))

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = requestType == "Async",
                    onClick = { requestType = "Async" }
                )
                Text(text = "Async", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Add Header Param",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            headerParams.forEachIndexed { index, keyValue ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            headerParams.add("" to "")
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    OutlinedTextField(
                        value = keyValue.first,
                        onValueChange = { newKey -> headerParams[index] = newKey to keyValue.second },
                        label = { Text("Key") },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    )
                    OutlinedTextField(
                        value = keyValue.second,
                        onValueChange = { newValue -> headerParams[index] = keyValue.first to newValue },
                        label = { Text("Value") },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Add Query Param",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            queryParams.forEachIndexed { index, keyValue ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            queryParams.add("" to "")
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                    OutlinedTextField(
                        value = keyValue.first,
                        onValueChange = { newKey -> queryParams[index] = newKey to keyValue.second },
                        label = { Text("Key") },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    )
                    OutlinedTextField(
                        value = keyValue.second,
                        onValueChange = { newValue -> queryParams[index] = keyValue.first to newValue },
                        label = { Text("Value") },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (method == "POST") {
                Text(
                    text = "Add Body Param",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                bodyParams.forEachIndexed { index, keyValue ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                bodyParams.add("" to "")
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                        OutlinedTextField(
                            value = keyValue.first,
                            onValueChange = { newKey -> bodyParams[index] = newKey to keyValue.second },
                            label = { Text("Key") },
                            modifier = Modifier.weight(1f).padding(4.dp)
                        )
                        OutlinedTextField(
                            value = keyValue.second,
                            onValueChange = { newValue -> bodyParams[index] = keyValue.first to newValue },
                            label = { Text("Value") },
                            modifier = Modifier.weight(1f).padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    val headerMap = headerParams.toMap()
                    val queryMap = queryParams.toMap()
                    val bodyMap = bodyParams.toMap()
                    // Use headerMap, queryMap, and bodyMap as needed
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Send Request")
            }
        }
    }
}
