package com.kAvEh.essentialwords;

public class Excercise {
	
	private int _id;
	private int _lesson;
	private String _type;
	private String _question;
	private String _a;
	private String _b;
	private String _c;
	private String _d;
	private String _answer;
	private String _state;
	private int _star;

	public Excercise (int id, int lesson, String type, String question,
			String a, String b, String c, String d, String answer, String state, int star) {
		this._id = id;
		this._lesson = lesson;
		this._type = type;
		this._question = question;
		this._a = a;
		this._b = b;
		this._c = c;
		this._d = d;
		this._answer = answer;
		this._state = state;
		this._star = star;
	}

	public int getID() {
		return this._id;
	}

	public int getLesson() {
		return this._lesson;
	}

	public String getType() {
		return this._type;
	}

	public String getQuestion() {
		return this._question;
	}

	public String getA() {
		return this._a;
	}

	public String getB() {
		return this._b;
	}

	public String getC() {
		return this._c;
	}
	
	public String getD() {
		return this._d;
	}
	
	public String getAnswer() {
		return this._answer;
	}
	
	public void setState(String ans) {
		this._state = ans;
	}
	
	public String getState() {
		return this._state;
	}
	
	public int getStar() {
		return this._star;
	}
}
