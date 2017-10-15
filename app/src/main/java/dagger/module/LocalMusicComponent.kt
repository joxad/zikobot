package dagger.module

import com.startogamu.zikobot.splash.SplashActivityVM
import dagger.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by blackbird-linux on 15/10/17.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, LocalMusicModule::class))
interface LocalMusicComponent {
    fun inject(splashActivityVM: SplashActivityVM)
}