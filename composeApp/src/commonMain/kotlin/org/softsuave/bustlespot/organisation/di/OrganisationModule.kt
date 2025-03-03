package org.softsuave.bustlespot.organisation.di


import org.softsuave.bustlespot.organisation.data.OrganisationRepository
import org.softsuave.bustlespot.organisation.data.OrganisationRepositoryImpl
import org.softsuave.bustlespot.organisation.ui.OrganisationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val organisationModule = module {
    single<OrganisationRepository> {
        OrganisationRepositoryImpl(get(),get())
    }

    viewModelOf(::OrganisationViewModel)
}
