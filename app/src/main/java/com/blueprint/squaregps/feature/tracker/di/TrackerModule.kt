package com.blueprint.squaregps.feature.tracker.di

import com.blueprint.squaregps.feature.tracker.data.api.TrackerApi
import com.blueprint.squaregps.feature.tracker.data.impl.TrackerCacheDataSourceImpl
import com.blueprint.squaregps.feature.tracker.data.impl.TrackerDataSourceImpl
import com.blueprint.squaregps.feature.tracker.domain.TrackerPoller
import com.blueprint.squaregps.feature.tracker.domain.TrackerRepository
import com.blueprint.squaregps.feature.tracker.domain.TrackerStateRepository
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateCacheDataSource
import com.blueprint.squaregps.feature.tracker.domain.datasource.TrackerStateDataSource
import com.blueprint.squaregps.feature.tracker.ui.details.TrackerDetailsViewModel
import com.blueprint.squaregps.feature.tracker.ui.list.TrackerListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

val trackerModule = module {

    single { TrackerApi(client = get(), sessionHashProvider = get()) }

    //tracker
    single { TrackerDataSourceImpl(trackerApi = get()) } binds arrayOf(
        TrackerDataSource::class,
        TrackerStateDataSource::class
    )
    single { TrackerCacheDataSourceImpl() } binds arrayOf(
        TrackerCacheDataSource::class,
        TrackerStateCacheDataSource::class
    )
    single { TrackerRepository(trackerDataSource = get(), trackerCacheDataSource = get()) }
    viewModelOf(::TrackerListViewModel)

    //details
    single {
        TrackerStateRepository(
            trackerStateDataSource = get(),
            trackerStateCacheDataSource = get()
        )
    }
    factory { TrackerPoller(trackerStateRepository = get()) }
    viewModelOf(::TrackerDetailsViewModel)

}
