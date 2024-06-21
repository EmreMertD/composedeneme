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
                var headerDialect by remember { mutableStateOf(TextFieldValue("TR")) }
                var headerIp by remember { mutableStateOf(TextFieldValue("127.0.0.1")) }
                var headerChannel by remember { mutableStateOf(TextFieldValue("Mobile")) }
                var headerAccept by remember { mutableStateOf(TextFieldValue("application/json")) }
                var headerTenantCompanyId by remember { mutableStateOf(TextFieldValue("GAR")) }
                var headerTenantGeolocation by remember { mutableStateOf(TextFieldValue("TUR")) }
                var headerClientId by remember { mutableStateOf(TextFieldValue("asdasdasdasdasd892438sadsa")) }
                var headerAuthorization by remember { mutableStateOf(TextFieldValue("Bearer dsakldjklasjklas")) }
                var queryUnmasked by remember { mutableStateOf("false") }
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
                                headerDialect = headerDialect,
                                onHeaderDialectChange = { headerDialect = it },
                                headerIp = headerIp,
                                onHeaderIpChange = { headerIp = it },
                                headerChannel = headerChannel,
                                onHeaderChannelChange = { headerChannel = it },
                                headerAccept = headerAccept,
                                onHeaderAcceptChange = { headerAccept = it },
                                headerTenantCompanyId = headerTenantCompanyId,
                                onHeaderTenantCompanyIdChange = { headerTenantCompanyId = it },
                                headerTenantGeolocation = headerTenantGeolocation,
                                onHeaderTenantGeolocationChange = { headerTenantGeolocation = it },
                                headerClientId = headerClientId,
                                onHeaderClientIdChange = { headerClientId = it },
                                headerAuthorization = headerAuthorization,
                                onHeaderAuthorizationChange = { headerAuthorization = it },
                                queryUnmasked = queryUnmasked,
                                onQueryUnmaskedChange = { queryUnmasked = it },
                                queryContactDetailType = queryContactDetailType,
                                onQueryContactDetailTypeChange = { queryContactDetailType = it }
                            )
                        }
                    }

                    if (shouldMakeApiCall) {
                        LaunchedEffect(Unit) {
                            delay(2000) // Simulate network delay

                            val requestHeader = getDefaultHeaders(
                                headerContentType.text,
                                headerGuid.text,
                                headerDialect.text,
                                headerIp.text,
                                headerChannel.text,
                                headerAccept.text,
                                headerTenantCompanyId.text,
                                headerTenantGeolocation.text,
                                headerClientId.text,
                                headerAuthorization.text
                            )
                            val requestQuery = getQueryMap(
                                queryUnmasked,
                                queryContactDetailType.text
                            )

                            response = "Sample Response" // This would be the actual API response
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

    private fun getDefaultHeaders(
        contentType: String,
        guid: String,
        dialect: String,
        ip: String,
        channel: String,
        accept: String,
        tenantCompanyId: String,
        tenantGeolocation: String,
        clientId: String,
        authorization: String
    ): HashMap<String, String> {
        val expectedHeaders = HashMap<String, String>()
        expectedHeaders["Content-Type"] = contentType
        expectedHeaders["guid"] = if (guid.isNotEmpty()) guid else UUID.randomUUID().toString()
        expectedHeaders["dialect"] = dialect
        expectedHeaders["ip"] = ip
        expectedHeaders["channel"] = channel
        expectedHeaders["Accept"] = accept
        expectedHeaders["tenant-company-id"] = tenantCompanyId
        expectedHeaders["tenant-geolocation"] = tenantGeolocation
        expectedHeaders["client-id"] = clientId
        expectedHeaders["Authorization"] = authorization
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
        headerDialect: TextFieldValue,
        onHeaderDialectChange: (TextFieldValue) -> Unit,
        headerIp: TextFieldValue,
        onHeaderIpChange: (TextFieldValue) -> Unit,
        headerChannel: TextFieldValue,
        onHeaderChannelChange: (TextFieldValue) -> Unit,
        headerAccept: TextFieldValue,
        onHeaderAcceptChange: (TextFieldValue) -> Unit,
        headerTenantCompanyId: TextFieldValue,
        onHeaderTenantCompanyIdChange: (TextFieldValue) -> Unit,
        headerTenantGeolocation: TextFieldValue,
        onHeaderTenantGeolocationChange: (TextFieldValue) -> Unit,
        headerClientId: TextFieldValue,
        onHeaderClientIdChange: (TextFieldValue) -> Unit,
        headerAuthorization: TextFieldValue,
        onHeaderAuthorizationChange: (TextFieldValue) -> Unit,
        queryUnmasked: String,
        onQueryUnmaskedChange: (String) -> Unit,
        queryContactDetailType: TextFieldValue,
        onQueryContactDetailTypeChange: (TextFieldValue) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
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

            OutlinedTextField(
                value = headerDialect,
                onValueChange = onHeaderDialectChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Dialect") }
            )

            OutlinedTextField(
                value = headerIp,
                onValueChange = onHeaderIpChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("IP") }
            )

            OutlinedTextField(
                value = headerChannel,
                onValueChange = onHeaderChannelChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Channel") }
            )

            OutlinedTextField(
                value = headerAccept,
                onValueChange = onHeaderAcceptChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Accept") }
            )

            OutlinedTextField(
                value = headerTenantCompanyId,
                onValueChange = onHeaderTenantCompanyIdChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Tenant Company ID") }
            )

            OutlinedTextField(
                value = headerTenantGeolocation,
                onValueChange = onHeaderTenantGeolocationChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Tenant Geolocation") }
            )

            OutlinedTextField(
                value = headerClientId,
                onValueChange = onHeaderClientIdChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Client ID") }
            )

            OutlinedTextField(
                value = headerAuthorization,
                onValueChange = onHeaderAuthorizationChange,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                label = { Text("Authorization") }
            )

            Text(text = "Query Parameters", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))

            CustomDropdownMenu(
                options = listOf("false", "true"),
                selectedOption = queryUnmasked,
                onOptionSelected = onQueryUnmaskedChange,
                label = "Unmasked"
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
