package org.wikicrimes.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class MapaChaveDupla<K1, K2, V> implements Serializable {
	
	Map<K1, Map<K2, V>> mapao;
	
	public MapaChaveDupla(){
		mapao = new HashMap<K1, Map<K2, V>>();
	}
	
	public void put(K1 chave1, K2 chave2, V valor){
		Map<K2, V> mapinha = mapao.get(chave1);
		if(mapinha == null){
			mapinha = new HashMap<K2, V>();
			mapao.put(chave1, mapinha);
		}
		mapinha.put(chave2, valor);
	}

	public V get(K1 chave1, K2 chave2){
		Map<K2, V> mapinha = mapao.get(chave1);
		if(mapinha == null)
			return null;
		else
			return mapinha.get(chave2);
	}
	
}
