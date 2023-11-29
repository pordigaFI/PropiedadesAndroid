/*package com.porfirio.androidprojectpdg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.util.Constants

@Database(
    entities = [PropiedadDetailDto::class],
    version = 1,    //versión de la base de datos
    exportSchema = true
)
abstract class RegistroDatabase: RoomDatabase(){
    abstract fun registroDao(): RegistroDao

    companion object{
        private var INSTANCE: RegistroDatabase? = null

        fun getDatabase(context: Context): RegistroDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RegistroDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration()  //se permite a Room recrear las tablas de la bd
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
*/