package com.utils.xls.cell.regions;

import org.apache.poi.ss.usermodel.Sheet;

import com.utils.string.StrUtils;

public abstract class AbstractRegion implements Region {

	AbstractRegion() {
	}

	@Override
	public abstract void write(
			Sheet sheet,
			int firstRow,
			int firstCol) throws Exception;

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
