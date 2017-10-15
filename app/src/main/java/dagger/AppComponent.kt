package dagger;

import com.startogamu.zikobot.ZikobotApp
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: ZikobotApp)
}
