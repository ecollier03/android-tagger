package com.example.imagetagger.repositories

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import com.example.imagetagger.models.FileEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(
    @ApplicationContext appContext: Context
) {

    /**
     * used to access the users media gallery
     */
    private val contentResolver: ContentResolver = appContext.contentResolver
//    private val loadedImages = getImages()

    /**
     * gets a single page of all user images
     */
    suspend fun getImages(
        pageSize: Int,
        offset: Int
    ): List<FileEntry> {
        return withContext(Dispatchers.IO) {
            val projection = arrayOf(
                Images.Media._ID,
                Images.Media.DISPLAY_NAME,
                Images.Media.SIZE,
                Images.Media.MIME_TYPE,
                Images.Media.DATE_ADDED,
            )

            val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                Images.Media.EXTERNAL_CONTENT_URI
            }

            val images = mutableListOf<FileEntry>()

            contentResolver.query(
                collectionUri, // Queried collection
                projection, // List of columns we want to fetch
                null, // Filtering parameters (in this case none)
                null, // Filtering values (in this case none)
                "${Images.Media.DATE_ADDED} DESC", // Sorting order (recent -> older files)
            )?.use { cursor ->
                if ( !cursor.moveToPosition(offset) ) {
                    return@withContext images
                }
                val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(Images.Media.SIZE)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(Images.Media.DATE_ADDED)
                var count = 0
                while (cursor.moveToNext() && count < pageSize) {
                    val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                    val name = cursor.getString(displayNameColumn)
                    val size = cursor.getLong(sizeColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)

                    images.add(FileEntry(uri, name, size, mimeType, dateAdded))
                    count++
                }
            }
            return@withContext images
        }
    }

}

