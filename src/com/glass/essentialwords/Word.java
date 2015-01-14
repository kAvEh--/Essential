package com.glass.essentialwords;

public class Word {
	private int _id;
	private int _lesson;
	private String _word;
	private String _part1;
	private String _part2;
	private String _example;
	private String _trans;
	private int _star;
	private int _l_stage;
	private int _l_part;

	public Word(int id, int lesson, String word, String part1,
			String part2, String example, String trans, int star, int l_stage, int l_part) {
		this._id = id;
		this._lesson = lesson;
		this._word = word;
		this._part1 = part1;
		this._part2 = part2;
		this._example = example;
		this._trans = trans;
		this._star = star;
		this._l_stage = l_stage;
		this._l_part = l_part;
	}

	public int getID() {
		return this._id;
	}

	public int getLesson() {
		return this._lesson;
	}

	public String getWord() {
		return this._word;
	}

	public String getpart1() {
		return this._part1;
	}

	public String getPart2() {
		return this._part2;
	}

	public String getExample() {
		return this._example;
	}

	public String getTrans() {
		return this._trans;
	}
	
	public void setStar(int str) {
		this._star = str;
	}
	
	public int getStar() {
		return this._star;
	}
	
	public int getLeitnerStage() {
		return this._l_stage;
	}
	
	public int getLeitnerPart() {
		return this._l_part;
	}
	
	public void setLeitnerStage(int stage) {
		this._l_stage = stage;
	}
	
	public void setLeitnerPart(int part) {
		this._l_part = part;
	}
}