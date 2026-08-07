package com.example.eduapp.di

import androidx.room.Room
import com.example.eduapp.data.PuzzleCatalog
import com.example.eduapp.data.PuzzleRepository
import com.example.eduapp.data.PuzzleRepositoryImpl
import com.example.eduapp.data.ResultRepository
import com.example.eduapp.data.ResultRepositoryImpl
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * The dependency graph. Every object the app needs is declared once here,
 * and Koin wires them together.
 */
val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "picquiz_db"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<AppDatabase>().appDao() }

    single { PuzzleCatalog(androidContext().assets) }

    single<PuzzleRepository> { PuzzleRepositoryImpl(get()) }
    single<ResultRepository> { ResultRepositoryImpl(get()) }

    viewModel { AppViewModel(get(), get()) }
}
