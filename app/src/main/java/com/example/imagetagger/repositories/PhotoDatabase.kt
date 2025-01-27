package com.example.imagetagger.repositories

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "Uris")
data class UrisEntity(
//    (autoGenerate = true) val id: Int = 0,
    @PrimaryKey val uri: String
)

@Entity(tableName = "Tags")
data class TagsEntity(
//    (autoGenerate = true) val id: Int = 0,
    @PrimaryKey val tag: String
)

@Entity(
    tableName = "Tagged",
    primaryKeys = ["urisId", "photosId"],
    foreignKeys = [
        ForeignKey(
            entity = UrisEntity::class,
            parentColumns = ["uri"],
            childColumns = ["urisId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagsEntity::class,
            parentColumns = ["tag"],
            childColumns = ["photosId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["urisId"]),
        Index(value = ["photosId"])
    ]
)
data class Tagged(
    val urisId: String,
    val photosId: String
)

@Dao
interface UrisDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: UrisEntity)

//    @Query("Select * FROM Uris")
//    suspend fun getAll(): List<String>
}


@Dao
interface TagsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TagsEntity)

    @Query("Select * FROM Tags")
    fun getAllTags(): Flow<List<TagsEntity>>
}

@Dao
interface TaggedDAO {
//    @Insert
//    suspend fun insert(relation: String)
//
//    @Query(
//        """
//        SELECT table_b.* FROM table_b
//        INNER JOIN table_c ON table_b.id = table_c.tableBId
//        WHERE table_c.tableAId = :tableAId
//        """
//    )
//    suspend fun getRelatedBItems(tableAId: Int): List<TableB>
//
//    @Query(
//        """
//        SELECT table_a.* FROM table_a
//        INNER JOIN table_c ON table_a.id = table_c.tableAId
//        WHERE table_c.tableBId = :tableBId
//        """
//    )
//    suspend fun getRelatedAItems(tableBId: Int): List<TableA>
}


@Database(
    entities = [UrisEntity::class, TagsEntity::class, Tagged::class],
    version = 2,
    exportSchema = false
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun UrisDAO(): UrisDAO
    abstract fun TagsDAO(): TagsDAO
    abstract fun TaggedDAO(): TaggedDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        //When we want a DB we call this (basically static) method
        //val theDB = WeatherDatabase.getDatabase(myContext)
        fun getDatabase(context: Context): PhotoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                //if another thread initialized this before we got the lock
                //return the object they created
                if (INSTANCE != null) return INSTANCE!!
                //otherwise we're the first thread here, so create the DB
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "artwork_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}