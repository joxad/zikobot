package dagger

import com.startogamu.zikobot.ZikobotApp
import javax.inject.Singleton

/**
 * Created by blackbird-linux on 15/10/17.
 */
@Module class AppModule(val app: ZikobotApp) {
    @Provides @Singleton
    fun provideApp() = app
}