package com.example.test_pay.di

import android.content.Context
import com.example.test_pay.ui.ConfirmationActivity
import com.example.test_pay.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class])
interface AppComponent{

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : AppComponent
    }

    // Types that can be retrieved from the graph
    fun mainActivityComponent(): MainActivityComponent.Factory
    fun inject(confirmationActivity: ConfirmationActivity)
}