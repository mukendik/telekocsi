package com.alma.telekocsi.dao;

import java.util.List;

public interface IAdapter <T> {
	
	static final int DIALOG_ERROR 	= 0;
	static final int DIALOG_LOADING = 1;
	
	T add(T fiche);
	T remove(T fiche);
	void clear(String nb);
	T update(T fiche);
	void setFiche(T fiche);
	void login(T fiche);
	void setList(List<T> list);
	void showDialog(int DialogType);
	void dismissDialog(int DialogType);
	
}
