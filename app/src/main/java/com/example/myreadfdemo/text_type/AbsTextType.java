package com.example.myreadfdemo.text_type;

/**
 * Created by lixinyu on 2017/5/8.
 */

public abstract class AbsTextType implements IBaseTextType {

	protected int decideSize(int size) {

		if (size >= 1 && size <= 8) {
			return 1;
		}
		if (size >= 9 && size <= 11) {
			return 2;
		}
		if (size >= 12 && size <= 14) {
			return 3;
		}
		if (size >= 15 && size <= 19) {
			return 4;
		}
		if (size >= 20 && size <= 29) {
			return 5;
		}
		if (size >= 30 && size <= 39) {
			return 6;
		}
		if (size >= 40) {
			return 7;
		}
		return 3;
	}
}
