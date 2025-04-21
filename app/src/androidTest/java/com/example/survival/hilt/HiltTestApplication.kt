package com.example.survival.hilt

import com.example.survival.BaseApp
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(BaseApp::class)
interface HiltTestApplication