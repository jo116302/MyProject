package kr.co.myProject.common.BibleEntity;

public class BiblePrimitive extends BibleEntity {

	private String value;

	@Override
	public BibleEntity deepCopy() {
		// TODO Auto-generated method stub
		return this;
	}

	public BiblePrimitive(String String) {
		setValue(String);
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getAsString() {
		return value;
	}
}
