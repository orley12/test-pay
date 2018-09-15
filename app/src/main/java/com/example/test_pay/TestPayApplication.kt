package com.example.test_pay

import android.app.Application
import com.example.test_pay.di.AppComponent
import com.example.test_pay.di.DaggerAppComponent

open class TestPayApplication : Application() {
    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(applicationContext)
    }
}
