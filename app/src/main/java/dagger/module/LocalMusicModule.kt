package dagger.module

import com.joxad.zikobot.data.module.localmusic.manager.LocalMusicManager
import com.startogamu.zikobot.ZikobotApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



/**
 * Created by blackbird-linux on 15/10/17.
 */
@Module
class LocalMusicModule {
    @Provides
    @Singleton
    internal fun provideLocalMusicManager( app:ZikobotApp): LocalMusicManager {
        return LocalMusicManager(app)
    }
}