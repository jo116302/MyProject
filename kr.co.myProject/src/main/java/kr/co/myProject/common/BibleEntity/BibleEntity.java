package kr.co.myProject.common.BibleEntity;

public abstract class BibleEntity {
	/**
	 * 참고 : package com.google.gson.JsonElement.class
	 */

	public abstract BibleEntity deepCopy();

	public boolean isBibleMap() {
		return this instanceof BibleMap;
	}
	
	public boolean isBiblePrimitive() {
		return this instanceof BiblePrimitive;
	}

	public BibleMap getAsBibleMap() {
		if (isBibleMap()) {
			return (BibleMap) this;
		}
		throw new IllegalStateException("Not a BibleMap Object: " + this);
	}

	public String getAsString() {
		throw new UnsupportedOperationException(getClass().getSimpleName());
	}
	
	public BiblePrimitive getBiblePrimitive() {
		if (isBiblePrimitive()) {
			return (BiblePrimitive) this;
		}
		throw new IllegalStateException("Not a BiblePrimitive Object: " + this);
	}

	@Override
	public String toString() {
		System.out.println(this.getClass());
		System.out.println(this instanceof BiblePrimitive);
		
		if (this instanceof BiblePrimitive) {
			return this.getBiblePrimitive().getAsString();
		}
		return new String();
	}
}
