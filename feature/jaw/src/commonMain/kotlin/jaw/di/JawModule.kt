package jaw.di

import jaw.view.viewmodel.JawSelectionViewModel
import org.koin.dsl.module

val jawModule = module {
    single<JawSelectionViewModel> { JawSelectionViewModel() }
}