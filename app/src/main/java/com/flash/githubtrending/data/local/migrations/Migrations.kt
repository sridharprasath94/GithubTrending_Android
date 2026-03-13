package com.flash.githubtrending.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS favorites (
                repoId INTEGER NOT NULL,
                PRIMARY KEY(repoId)
            )
        """.trimIndent()
        )

    }
}