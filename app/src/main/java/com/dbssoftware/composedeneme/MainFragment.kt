package com.dbssoftware.composedeneme

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import java.util.UUID

class MainFragment : Fragment() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                var showProgressBar by remember { mutableStateOf(false) }
                var shouldMakeApiCall by remember { mutableStateOf(false) }
                var endpoint by remember { mutableStateOf(TextFieldValue()) }
                var route by remember { mutableStateOf(TextFieldValue()) }
                var selectedMethod by remember { mutableStateOf("GET") }
                var selectedAsync by remember { mutableStateOf("Async") }
                var headerContentType by remember { mutableStateOf(TextFieldValue("application/json")) }
                var headerGuid by remember { mutableStateOf(TextFieldValue()) }
                var queryUnmasked by remember { mutableStateOf(TextFieldValue("false")) }
                var queryContactDetailType by remember { mutableStateOf(TextFieldValue("MOBILE")) }
                var response by remember { mutableStateOf("") }

                Scaffold(
                    topBar = {
                        TopBar(title = "API Request Screen")
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        if (showProgressBar) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        } else {
                            ApiRequestScreen(
                                onCallServiceClicked = {
                                    showProgressBar = true
                                    shouldMakeApiCall = true
                                },
                                endpoint = endpoint,
                                onEndpointChange = { endpoint = it },
                                route = route,
                                onRouteChange = { route = it },
                                selectedMethod = selectedMethod,
                                onMethodChange = { selectedMethod = it },
                                selectedAsync = selectedAsync,
                                onAsyncChange = { selectedAsync = it },
                                headerContentType = headerContentType,
                                onHeaderContentTypeChange = { headerContentType = it },
                                headerGuid = headerGuid,
                                onHeaderGuidChange = { headerGuid = it },
                                queryUnmasked = queryUnmasked,
                                onQueryUnmaskedChange = { queryUnmasked = it },
                                queryContactDetailType = queryContactDetailType,
                                onQueryContactDetailTypeChange = { queryContactDetailType = it }
                            )
                        }
                    }

                    if (shouldMakeApiCall) {
                        LaunchedEffect(Unit) {
                            delay(2000) // Ağ gecikmesini simüle edin

                            val requestHeader = getDefaultHeaders(
                                headerContentType.text,
                                headerGuid.text
                            )
                            val requestQuery = getQueryMap(
                                queryUnmasked.text,
                                queryContactDetailType.text
                            )

                            response = "Sample Response" // Bu, gerçek API yanıtı olacaktır
                            showProgressBar = false
                            shouldMakeApiCall = false
                            findNavController().navigate(
                                MainFragmentDirections.actionMainFragmentToResponseFragment(
                                    requestHeader.toString(),
                                    requestQuery.toString(),
                                    response
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getQueryMap(unmasked: String, contactDetailType: String): HashMap<String, String> {
        val queryMap = HashMap<String, String>()
        queryMap["unmasked"] = unmasked
        queryMap["contactDetailType"] = contactDetailType
        return queryMap
    }

    private fun getDefaultHeaders(contentType: String, guid: String): HashMap<String, String> {
        val expectedHeaders = HashMap<String, String>()
        expectedHeaders["Content-Type"] = contentType
        expectedHeaders["guid"] = if (guid.isNotEmpty()) guid else UUID.randomUUID().toString()
        expectedHeaders["dialect"] = "TR"
        expectedHeaders["ip"] = "127.0.0.1" // getIPAddress() ?: ""
        expectedHeaders["channel"] = "Mobile"
        expectedHeaders["Accept"] = "application/json"
        expectedHeaders["tenant-company-id"] = "GAR"
        expectedHeaders["tenant-geolocation"] = "TUR"
        expectedHeaders["client-id"] = "asdasdasdasdasd892438sadsa"
        expectedHeaders["Authorization"] = "Bearer dsakldjklasjklas"
        return expectedHeaders
    }

    @Composable
    fun ApiRequestScreen(
        onCallServiceClicked: () -> Unit,
        endpoint: TextFieldValue,
        onEndpointChange: (TextFieldValue) -> Unit,
        route: TextFieldValue,
        onRouteChange: (TextFieldValue) -> Unit,
        selectedMethod: String,
        onMethodChange: (String) -> Unit,
        selectedAsync: String,
        onAsyncChange: (String) -> Unit,
        headerContentType: TextFieldValue,
        onHeaderContentTypeChange: (TextFieldValue) -> Unit,
        headerGuid: TextFieldValue,
        onHeaderGuidChange: (TextFieldValue) -> Unit,
        queryUnmasked: TextFieldValue,
        onQueryUnmaskedChange: (TextFieldValue) -> Unit,
        queryContactDetailType: TextFieldValue,
        onQueryContactDetailTypeChange: (TextFieldValue) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Bu satırı ekledik
        ) {
            OutlinedTextField(
                value = endpoint,
                onValueChange = onEndpointChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Endpoint") }
            )

            OutlinedTextField(
                value = route,
                onValueChange = onRouteChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Route") }
            )

            CustomDropdownMenu(
                options = listOf("GET", "POST"),
                selectedOption = selectedMethod,
                onOptionSelected = onMethodChange,
                label = "Method"
            )

            CustomDropdownMenu(
                options = listOf("Async", "Sync"),
                selectedOption = selectedAsync,
                onOptionSelected = onAsyncChange,
                label = "Execution Type"
            )

            Text(text = "Headers", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = headerContentType,
                onValueChange = onHeaderContentTypeChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Content-Type") }
            )

            OutlinedTextField(
                value = headerGuid,
                onValueChange = onHeaderGuidChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("GUID") }
            )

            Text(text = "Query Parameters", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(
                value = queryUnmasked,
                onValueChange = onQueryUnmaskedChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Unmasked") }
            )

            OutlinedTextField(
                value = queryContactDetailType,
                onValueChange = onQueryContactDetailTypeChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Contact Detail Type") }
            )

            Button(
                onClick = onCallServiceClicked,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Call Service")
            }
        }
    }

    @Composable
    fun CustomDropdownMenu(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit, label: String) {
        var expanded by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Column {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(label) },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { expanded = true }
                        )
                    },
                    interactionSource = interactionSource,
                    visualTransformation = VisualTransformation.None
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }
        }
    }
}
