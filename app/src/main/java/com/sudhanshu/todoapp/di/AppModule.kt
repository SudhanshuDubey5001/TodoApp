package com.sudhanshu.todoapp.di

import android.app.Application
import androidx.room.Room
import com.sudhanshu.todoapp.data.TodoDatabase
import com.sudhanshu.todoapp.data.TodoRepository
import com.sudhanshu.todoapp.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//This is appModule class which is one place for all the dependencies you will need for your app. There could be
//more modules dependent on complexity of app. It also defines the lifetime of dependency as it is not needed forever
// Like if some dependency is needed for only one activity, you can make the dependency Singleton so that it is alive
//till the lifetime of that activity

@Module //to tell dagger this is module and take the dependencies from here
@InstallIn(SingletonComponent::class) //defines "how long" the dependency will live
object AppModule {

    //now here define the methods how the dependencies will be created
    @Provides //meaning it will provide the dependency to whoever repquests it
    @Singleton //meaning it will only have single instance
    fun provideTodoRepository(app: Application): TodoRepository {
        val db = Room.databaseBuilder(app, TodoDatabase::class.java, "todo_db").build()
        return TodoRepositoryImpl(db.todoDao)
    }

//    //now we got the database instance "db", but we also need repository instance, so we will simply pass in this
//    // below method and get the repo instance-->
//    @Provides
//    @Singleton
//    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
//        return TodoRepositoryImpl(db.todoDao)
//    }
}