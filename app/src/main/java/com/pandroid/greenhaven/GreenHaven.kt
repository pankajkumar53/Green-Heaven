package com.pandroid.greenhaven

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.pandroid.greenhaven.data.di.authModule
import com.pandroid.greenhaven.data.di.bookingModule
import com.pandroid.greenhaven.data.di.cartModule
import com.pandroid.greenhaven.data.di.favModule
import com.pandroid.greenhaven.data.di.fireBaseModule
import com.pandroid.greenhaven.data.di.homeModule
import com.pandroid.greenhaven.data.di.plantModule
import com.pandroid.greenhaven.data.di.userPrefModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GreenHaven : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GreenHaven)
            modules(userPrefModule)
            modules(authModule)
            modules(homeModule)
            modules(favModule)
            modules(cartModule)
            modules(bookingModule)
            modules(fireBaseModule)
            modules(plantModule)
        }
        FirebaseApp.initializeApp(this)

        // Initialize Firebase App Check with the Play Integrity provider
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }

}