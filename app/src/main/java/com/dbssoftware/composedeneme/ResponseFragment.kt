package com.dbssoftware.composedeneme

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

class ResponseFragment : Fragment() {

    private val args: ResponseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ResponseScreen(
                    requestHeader = beautifyJsonString(args.requestHeader),
                    requestQuery = beautifyJsonString(args.requestQuery),
                    response = args.response
                )
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun ResponseScreen( requestHeader: String, requestQuery: String, response: String) {
        Scaffold(
            topBar = {
                TopBar(
                    title = "API Response Screen",
                    navigationIcon = Icons.Filled.ArrowBack,
                    navigationIconClick = { findNavController().popBackStack() }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Body:", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
                Text(text = requestHeader, modifier = Modifier.padding(vertical = 8.dp))
                Text(text = requestQuery, modifier = Modifier.padding(vertical = 8.dp))

                Text(text = "Response:", style = MaterialTheme.typography.h6, modifier = Modifier.padding(vertical = 8.dp))
                Text(text = response, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }

    fun beautifyJsonString(jsonString: String): String {
        val stringBuilder = StringBuilder()
        var indentLevel = 0
        val indentString = "    " // 4 boşluklu girinti

        for (char in jsonString) {
            when (char) {
                '{', '[' -> {
                    stringBuilder.append(char).append("\n")
                    indentLevel++
                    stringBuilder.append(indentString.repeat(indentLevel))
                }
                '}', ']' -> {
                    stringBuilder.append("\n")
                    indentLevel--
                    stringBuilder.append(indentString.repeat(indentLevel)).append(char)
                }
                ',' -> {
                    stringBuilder.append(char).append("\n")
                    stringBuilder.append(indentString.repeat(indentLevel))
                }
                else -> stringBuilder.append(char)
            }
        }
        return stringBuilder.toString()
    }
}
