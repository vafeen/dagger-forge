package com.example.samplehiltapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.samplehiltapp.ui.theme.DaggerForgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val viewModel: MainActivityViewModel = hiltViewModel()
			DaggerForgeTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(innerPadding),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.SpaceEvenly
					) {
						Text(text = viewModel.cat.sound)
						Text(text = viewModel.dog.sound)
					}
				}
			}
		}
	}
}

