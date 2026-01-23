package jaw.di

import jaw.view.viewmodel.SelectionViewModel
import org.koin.dsl.module

val jawModule = module {
    single<SelectionViewModel> { SelectionViewModel() }
}