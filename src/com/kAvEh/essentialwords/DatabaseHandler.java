package com.kAvEh.essentialwords;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHandler extends SQLiteAssetHelper {

	// All Static variables
	// Database Version
	// TODO
	private static final int DATABASE_VERSION = 8;

	// Database Name
	// TODO
	private static final String DATABASE_NAME = "WordsDBV8";

	// Location table name
	private static final String TABLE_LESSON = "lesson";
	private static final String TABLE_LESSONS = "lessons";
	private static final String TABLE_EXCERCISE = "excercise";
	private static final String TABLE_LEITNER = "leitner";

	// Items Fields
	private static final String KEY_ID = "Id";
	private static final String KEY_LESSON = "lesson";
	private static final String KEY_WORD = "word";
	private static final String KEY_PART1 = "part1";
	private static final String KEY_PART2 = "part2";
	private static final String KEY_EXAMPLE = "example";
	private static final String KEY_TRANSLATION = "trans";
	private static final String KEY_TYPE = "type";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_A = "a";
	private static final String KEY_B = "b";
	private static final String KEY_C = "c";
	private static final String KEY_D = "d";
	private static final String KEY_ANSWER = "answer";
	private static final String KEY_STATE = "state";
	private static final String KEY_STAR = "star";
	private static final String KEY_COUNT = "count";
	private static final String KEY_L_STAGE = "leitner_stage";
	private static final String KEY_L_PART = "leitner_part";
	private static final String KEY_LAST_NUM_ADDED = "last_num_added";
	private static final String KEY_FINAL_COUNT = "final_count";
	private static final String KEY_STAGE1_COUNT = "stage1_count";
	private static final String KEY_STAGE2_COUNT = "stage2_count";
	private static final String KEY_STAGE3_COUNT = "stage3_count";
	private static final String KEY_STAGE4_COUNT = "stage4_count";
	private static final String KEY_STAGE5_COUNT = "stage5_count";
	private static final String KEY_LAST_VISIT = "last_visit";
	private static final String KEY_VISIT_STAGE5 = "visit_stage5";
	private static final String KEY_VISIT_STAGE4 = "visit_stage4";
	private static final String KEY_VISIT_STAGE3 = "visit_stage3";
	private static final String KEY_VISIT_STAGE2 = "visit_stage2";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		setForcedUpgradeVersion(DATABASE_VERSION);
	}

	// ---Item-------------------------------

	public void starLesson(int id, int like) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LESSON + " SET " + KEY_STAR + "='"
				+ like + "' WHERE " + KEY_ID + "='" + id + "';";
		db.execSQL(sql);
		db.close();

	}

	public void starExcercise(int id, int like) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_EXCERCISE + " SET " + KEY_STAR + "='"
				+ like + "' WHERE " + KEY_ID + "='" + id + "';";
		db.execSQL(sql);
		db.close();

	}

	public void starWord(int id, int like) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_STAR + "='"
				+ like + "' WHERE " + KEY_ID + "='" + id + "';";
		db.execSQL(sql);
		db.close();

	}

	public void answerQuestion(int id, String answer) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_EXCERCISE + " SET " + KEY_STATE + "='"
				+ answer + "' WHERE " + KEY_ID + "='" + id + "';";
		db.execSQL(sql);
		db.close();

	}

	public void showknowWord(int id, String state) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_STATE + "='"
				+ state + "' WHERE " + KEY_ID + "='" + id + "';";
		db.execSQL(sql);
		db.close();

	}

	public void addWord(Word w) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WORD, w.getWord());
		values.put(KEY_PART2, w.getpart1());
		values.put(KEY_TRANSLATION, w.getTrans());
		values.put(KEY_L_STAGE, w.getLeitnerStage());
		values.put(KEY_L_PART, w.getLeitnerPart());
		values.put(KEY_LESSON, w.getLesson());

		db.insert(TABLE_LESSONS, null, values);
		String sql = "UPDATE " + TABLE_LESSON + " SET " + KEY_COUNT + " = "
				+ KEY_COUNT + " + 1" + " WHERE " + KEY_ID + " = "
				+ w.getLesson() + ";";
		db.execSQL(sql);
		db.close();
	}

	public void editWord(Word w) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_WORD + " = '"
				+ w.getWord() + "' , " + KEY_PART2 + " = '" + w.getPart2()
				+ "' ,  " + KEY_TRANSLATION + " = '" + w.getTrans()
				+ "' WHERE " + KEY_ID + " = " + w.getID() + ";";
		db.execSQL(sql);
		db.close();
	}

	public int getLessonNum() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor mCount = db.rawQuery("SELECT * FROM " + TABLE_LESSON, null);
		mCount.moveToFirst();
		int count = mCount.getCount();
		mCount.close();
		db.close();
		return count;
	}

	public int getLessonToAdd() {
		String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_LESSON
				+ " WHERE " + KEY_COUNT + " < 15;";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		int ret;
		if (cursor.moveToFirst()) {
			ret = cursor.getInt(cursor.getColumnIndex(KEY_ID));
		} else {
			ContentValues values = new ContentValues();
			values.put(KEY_COUNT, 0);

			ret = (int) db.insert(TABLE_LESSON, null, values);
		}

		cursor.close();
		db.close();

		return ret;
	}

	public int[] getCorrects(int lesson) {
		int[] starList = new int[2];
		starList[0] = 0;
		starList[1] = 0;
		// Select All Query
		String selectQuery = "SELECT state FROM " + TABLE_LESSONS
				+ " WHERE lesson = '" + lesson + "';";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		String state;
		if (cursor.moveToFirst()) {
			do {
				state = cursor.getString(cursor.getColumnIndex(KEY_STATE));
				if (state.equals("yes"))
					starList[0]++;
				else if (state.equals("no"))
					starList[1]++;
			} while (cursor.moveToNext());
		}
		// return contact list
		cursor.close();
		db.close();
		return starList;
	}

	public int[] getExcerCorrects(int lesson) {
		int[] starList = new int[2];
		starList[0] = 0;
		starList[1] = 0;
		// Select All Query
		String selectQuery = "SELECT state, answer FROM " + TABLE_EXCERCISE
				+ " WHERE lesson = '" + lesson + "';";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		String state;
		String answer;
		if (cursor.moveToFirst()) {
			do {
				state = cursor.getString(cursor.getColumnIndex(KEY_STATE));
				answer = cursor.getString(cursor.getColumnIndex(KEY_ANSWER));
				if (!state.equals("n")) {
					if (state.equals(answer))
						starList[0]++;
					else
						starList[1]++;
				}
			} while (cursor.moveToNext());
		}
		// return contact list
		cursor.close();
		db.close();
		return starList;
	}

	public ArrayList<Word> getLesson(int num) {
		ArrayList<Word> lesson = new ArrayList<Word>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_LESSONS, new String[] { KEY_ID,
				KEY_LESSON, KEY_WORD, KEY_PART1, KEY_PART2, KEY_EXAMPLE,
				KEY_TRANSLATION, KEY_STAR, KEY_L_STAGE, KEY_L_PART },
				KEY_LESSON + "=?", new String[] { String.valueOf(num) }, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Word word = new Word(cursor.getInt(cursor
						.getColumnIndex(KEY_ID)), cursor.getInt(cursor
						.getColumnIndex(KEY_LESSON)), cursor.getString(cursor
						.getColumnIndex(KEY_WORD)), cursor.getString(cursor
						.getColumnIndex(KEY_PART1)), cursor.getString(cursor
						.getColumnIndex(KEY_PART2)), cursor.getString(cursor
						.getColumnIndex(KEY_EXAMPLE)), cursor.getString(cursor
						.getColumnIndex(KEY_TRANSLATION)), cursor.getInt(cursor
						.getColumnIndex(KEY_STAR)), cursor.getInt(cursor
						.getColumnIndex(KEY_L_STAGE)), cursor.getInt(cursor
						.getColumnIndex(KEY_L_PART)));
				lesson.add(word);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lesson;
	}
	
	public ArrayList<Word> getLearnedWords() {
		ArrayList<Word> lesson = new ArrayList<Word>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_LESSONS, new String[] { KEY_ID,
				KEY_LESSON, KEY_WORD, KEY_PART1, KEY_PART2, KEY_EXAMPLE,
				KEY_TRANSLATION, KEY_STAR, KEY_L_STAGE, KEY_L_PART },
				KEY_L_STAGE + "=?", new String[] { String.valueOf(6) }, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Word word = new Word(cursor.getInt(cursor
						.getColumnIndex(KEY_ID)), cursor.getInt(cursor
						.getColumnIndex(KEY_LESSON)), cursor.getString(cursor
						.getColumnIndex(KEY_WORD)), cursor.getString(cursor
						.getColumnIndex(KEY_PART1)), cursor.getString(cursor
						.getColumnIndex(KEY_PART2)), cursor.getString(cursor
						.getColumnIndex(KEY_EXAMPLE)), cursor.getString(cursor
						.getColumnIndex(KEY_TRANSLATION)), cursor.getInt(cursor
						.getColumnIndex(KEY_STAR)), cursor.getInt(cursor
						.getColumnIndex(KEY_L_STAGE)), cursor.getInt(cursor
						.getColumnIndex(KEY_L_PART)));
				lesson.add(word);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lesson;
	}

	public ArrayList<Excercise> getExcercise(int num) {
		ArrayList<Excercise> excer = new ArrayList<Excercise>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_EXCERCISE, new String[] { KEY_ID,
				KEY_LESSON, KEY_TYPE, KEY_QUESTION, KEY_A, KEY_B, KEY_C, KEY_D,
				KEY_ANSWER, KEY_STATE, KEY_STAR }, KEY_LESSON + "=?",
				new String[] { String.valueOf(num) }, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Excercise excercise = new Excercise(cursor.getInt(cursor
						.getColumnIndex(KEY_ID)), cursor.getInt(cursor
						.getColumnIndex(KEY_LESSON)), cursor.getString(cursor
						.getColumnIndex(KEY_TYPE)), cursor.getString(cursor
						.getColumnIndex(KEY_QUESTION)), cursor.getString(cursor
						.getColumnIndex(KEY_A)), cursor.getString(cursor
						.getColumnIndex(KEY_B)), cursor.getString(cursor
						.getColumnIndex(KEY_C)), cursor.getString(cursor
						.getColumnIndex(KEY_D)), cursor.getString(cursor
						.getColumnIndex(KEY_ANSWER)), cursor.getString(cursor
						.getColumnIndex(KEY_STATE)), cursor.getInt(cursor
						.getColumnIndex(KEY_STAR)));
				excer.add(excercise);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return excer;
	}

	public void setLeitner(int id, int stage, int part) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_STAGE + "="
				+ stage + ", " + KEY_L_PART + "=" + part + " WHERE " + KEY_ID
				+ "='" + id + "';";
		db.execSQL(sql);
		db.close();
	}

	public void generateLeitner() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_LEITNER + ";";
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			int num = cursor.getInt(cursor.getColumnIndex(KEY_STAGE1_COUNT));
			if (num < 15) {
				String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_STAGE
						+ "=1 , " + KEY_L_PART + "=1 WHERE " + KEY_ID + " IN ("
						+ "SELECT " + KEY_ID + " FROM " + TABLE_LESSONS
						+ "  WHERE " + KEY_L_STAGE + "=0 LIMIT " + (15 - num)
						+ ");";
				db.execSQL(sql);
			}
		}
		cursor.close();
		db.close();
	}

	public void addFinalCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_FINAL_COUNT
				+ "=" + KEY_FINAL_COUNT + " + 1" + ";";
		db.execSQL(sql);
		db.close();
	}

	public void minusFinalCount() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_FINAL_COUNT
				+ "=" + KEY_FINAL_COUNT + " - 1" + ";";
		db.execSQL(sql);
		db.close();
	}

	@SuppressLint("SimpleDateFormat")
	public void updateLeitner() {
		String selectQuery = "SELECT * FROM " + TABLE_LEITNER + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());

		if (cursor.moveToFirst()) {
			boolean flag = false;

			// Stage 2 Update
			try {
				flag = sdf.parse(
						cursor.getString(cursor
								.getColumnIndex(KEY_VISIT_STAGE2))).before(
						sdf.parse(today));
			} catch (Exception e) {
				flag = false;
			}
			if (cursor.getInt(cursor.getColumnIndex(KEY_STAGE2_COUNT)) == 0
					&& flag) {
				String sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_PART
						+ "=1 WHERE " + KEY_L_PART + "=2 AND " + KEY_L_STAGE
						+ "=2;";
				db.execSQL(sql);
			}
			// Stage 3 Update
			try {
				flag = sdf.parse(
						cursor.getString(cursor
								.getColumnIndex(KEY_VISIT_STAGE3))).before(
						sdf.parse(today));
			} catch (Exception e) {
				flag = false;
			}
			if (cursor.getInt(cursor.getColumnIndex(KEY_STAGE3_COUNT)) == 0
					&& flag) {
				String sql;
				for (int i = 1; i < 4; i++) {
					sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_PART
							+ "=" + i + " WHERE " + KEY_L_PART + "=" + (i + 1)
							+ " AND " + KEY_L_STAGE + "=3;";
					db.execSQL(sql);
				}
			}
			// Stage 4 Update
			try {
				flag = sdf.parse(
						cursor.getString(cursor
								.getColumnIndex(KEY_VISIT_STAGE4))).before(
						sdf.parse(today));
			} catch (Exception e) {
				flag = false;
			}
			if (cursor.getInt(cursor.getColumnIndex(KEY_STAGE4_COUNT)) == 0
					&& flag) {
				String sql;
				for (int i = 1; i < 8; i++) {
					sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_PART
							+ "=" + i + " WHERE " + KEY_L_PART + "=" + (i + 1)
							+ " AND " + KEY_L_STAGE + "=4;";
					db.execSQL(sql);
				}
			}
			// Stage 5 Update
			try {
				flag = sdf.parse(
						cursor.getString(cursor
								.getColumnIndex(KEY_VISIT_STAGE5))).before(
						sdf.parse(today));
			} catch (Exception e) {
				flag = false;
			}
			if (cursor.getInt(cursor.getColumnIndex(KEY_STAGE5_COUNT)) == 0
					&& flag) {
				String sql;
				for (int i = 1; i < 16; i++) {
					sql = "UPDATE " + TABLE_LESSONS + " SET " + KEY_L_PART
							+ "=" + i + " WHERE " + KEY_L_PART + "=" + (i + 1)
							+ " AND " + KEY_L_STAGE + "=5;";
					db.execSQL(sql);
				}
			}
		}
		cursor.close();
		String sql2 = "UPDATE " + TABLE_LEITNER + " SET " + KEY_VISIT_STAGE2
				+ "='" + today + "', " + KEY_VISIT_STAGE3 + "='" + today
				+ "', " + KEY_VISIT_STAGE4 + "='" + today + "', "
				+ KEY_VISIT_STAGE5 + "='" + today + "' ;";
		db.execSQL(sql2);
		db.close();
		updateCounter();
	}

	public void updateCounter() {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql;
		// update counter stage 1
		sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_STAGE1_COUNT
				+ "=(SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=1 AND " + KEY_L_PART + "=1)";
		db.execSQL(sql);
		// update counter stage 2
		sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_STAGE2_COUNT
				+ "=(SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=2 AND " + KEY_L_PART + "=1)";
		db.execSQL(sql);
		// update counter stage 3
		sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_STAGE3_COUNT
				+ "=(SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=3 AND " + KEY_L_PART + "=1)";
		db.execSQL(sql);
		// update counter stage 4
		sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_STAGE4_COUNT
				+ "=(SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=4 AND " + KEY_L_PART + "=1)";
		db.execSQL(sql);
		// update counter stage 5
		sql = "UPDATE " + TABLE_LEITNER + " SET " + KEY_STAGE5_COUNT
				+ "=(SELECT COUNT(*) FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=5 AND " + KEY_L_PART + "=1)";
		db.execSQL(sql);
		db.close();
	}

	public HashMap<String, String> getLeitnerData() {
		String selectQuery = "SELECT * FROM " + TABLE_LEITNER + ";";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		HashMap<String, String> ret = new HashMap<String, String>();
		if (cursor.moveToFirst()) {
			ret.put("last_visit_2",
					cursor.getString(cursor.getColumnIndex(KEY_VISIT_STAGE2)));
			ret.put("last_visit_3",
					cursor.getString(cursor.getColumnIndex(KEY_VISIT_STAGE3)));
			ret.put("last_visit_4",
					cursor.getString(cursor.getColumnIndex(KEY_VISIT_STAGE4)));
			ret.put("last_visit_5",
					cursor.getString(cursor.getColumnIndex(KEY_VISIT_STAGE5)));
			ret.put("last_visit",
					cursor.getString(cursor.getColumnIndex(KEY_LAST_VISIT)));
			ret.put("last_num_added",
					cursor.getString(cursor.getColumnIndex(KEY_LAST_NUM_ADDED)));
			ret.put("final_count",
					cursor.getString(cursor.getColumnIndex(KEY_FINAL_COUNT)));
			ret.put("stage1",
					cursor.getString(cursor.getColumnIndex(KEY_STAGE1_COUNT)));
			ret.put("stage2",
					cursor.getString(cursor.getColumnIndex(KEY_STAGE2_COUNT)));
			ret.put("stage3",
					cursor.getString(cursor.getColumnIndex(KEY_STAGE3_COUNT)));
			ret.put("stage4",
					cursor.getString(cursor.getColumnIndex(KEY_STAGE4_COUNT)));
			ret.put("stage5",
					cursor.getString(cursor.getColumnIndex(KEY_STAGE5_COUNT)));
		}
		cursor.close();
		db.close();
		return ret;
	}

	public ArrayList<Word> getLeitnerWords(int stage) {
		String selectQuery = "SELECT * FROM " + TABLE_LESSONS + " WHERE "
				+ KEY_L_STAGE + "=" + stage + " AND " + KEY_L_PART + "=1;";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<Word> ret = new ArrayList<Word>();
		Word w;
		if (cursor.moveToFirst()) {
			do {
				w = new Word(
						cursor.getInt(cursor.getColumnIndex(KEY_ID)),
						cursor.getInt(cursor.getColumnIndex(KEY_LESSON)),
						cursor.getString(cursor.getColumnIndex(KEY_WORD)),
						cursor.getString(cursor.getColumnIndex(KEY_PART1)),
						cursor.getString(cursor.getColumnIndex(KEY_PART2)),
						cursor.getString(cursor.getColumnIndex(KEY_EXAMPLE)),
						cursor.getString(cursor.getColumnIndex(KEY_TRANSLATION)),
						cursor.getInt(cursor.getColumnIndex(KEY_STAR)), cursor
								.getInt(cursor.getColumnIndex(KEY_L_STAGE)),
						cursor.getInt(cursor.getColumnIndex(KEY_L_PART)));
				ret.add(w);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return ret;
	}
}