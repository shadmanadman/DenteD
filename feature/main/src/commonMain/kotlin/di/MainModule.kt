package di


import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import view.viewmodel.MainViewModel

val mainModule = module {
    viewModelOf(::MainViewModel)
}