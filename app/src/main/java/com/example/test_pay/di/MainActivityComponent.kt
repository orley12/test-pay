package com.example.test_pay.di

import android.app.Activity
import com.example.test_pay.annotation.ActivityScope
import com.example.test_pay.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance mainActivity: Activity) : MainActivityComponent
    }

    fun inject(mainActivity: MainActivity)

}