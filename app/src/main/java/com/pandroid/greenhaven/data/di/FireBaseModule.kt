package com.pandroid.greenhaven.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val fireBaseModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseStorage> { FirebaseStorage.getInstance() }
    single<FirebaseDatabase> { FirebaseDatabase.getInstance() }

}