package edu.cmu.rwsefe.vowl.model;


public class Character {
	
	private int id;
	private int unicodeValue;
	private int confidence;
	//private Stroke stroke;
	
	public Character(int newId, int newUnicodeValue, int newConfidence) {
		id = newId;
		unicodeValue = newUnicodeValue;
		confidence = newConfidence;
		//stroke = newStroke;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		id = newId;
	}
	
	public int getUnicodeValue() {
		return unicodeValue;
	}
	
	public void setUnicodealue(int newUnicodeValue) {
		unicodeValue = newUnicodeValue;
	}
	
	public int getConfidence() {
		return confidence;
	}
	
	public void setConfidence(int newConfidence) {
		confidence = newConfidence;
	}
	
	/*
	public Stroke getStroke() {
		return stroke;
	}
	
	public void setStroke(Stroke newStroke) {
		stroke = newStroke;
	}
	*/

}
