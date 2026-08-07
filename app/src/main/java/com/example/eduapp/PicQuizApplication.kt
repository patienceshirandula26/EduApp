package com.example.eduapp

import android.app.Application
import com.example.eduapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PicQuizApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PicQuizApplication)
            modules(appModule)
        }
    }
}
