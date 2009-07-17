package org.wikicrimes.view;

import java.util.ArrayList;
import java.util.List;

import org.wikicrimes.business.RelatoBusiness;
import org.wikicrimes.model.Crime;
import org.wikicrimes.model.Relato;

public class RelatoBean {

	private RelatoBusiness relatoBusiness;

	private List<Relato> relatos;
	private String stringRelatos;
	private List<Relato> relatosSemEnderecoEncontrado;
	private Relato relato;

	public Relato getRelato() {
		return relato;
	}

	public void setRelato(Relato relato) {
		this.relato = relato;
	}

	public String irEnderecoRelatos() {
		relatos = relatoBusiness.getRelatosSemEndereco();

		return "enderecoRelatos";
	}
	
	/*
	 * Trata uma string recebida com o(s) relato(s) e guarda os novos endereços no banco.
	 * Caso haja relatos sem endereços encontrados redireciona para uma nova página
	 */
	public String atualizarEnderecos(){
		Relato relatoAux;
		boolean alterado;
		relatosSemEnderecoEncontrado = new ArrayList<Relato>();
		String[] stringsRelatos = stringRelatos.split(";");
		
		for(int i = 0; i<stringsRelatos.length; i=i+6)
		{
			long id = Integer.parseInt(stringsRelatos[i]);
			alterado = false;
			
			relatoAux = relatoBusiness.getRelatoById(id);
			
			if(!stringsRelatos[i+1].equalsIgnoreCase("undefined")){
				if(!stringsRelatos[i+1].equals("null")){
					relatoAux.setPais(stringsRelatos[i+1]);
					alterado = true;
				}
				if(!stringsRelatos[i+2].equals("null")){
					relatoAux.setEstado(stringsRelatos[i+2]);
					alterado = true;
				}
				if(!stringsRelatos[i+3].equals("null")){
					relatoAux.setCidade(stringsRelatos[i+3]);
					alterado = true;
				}
				if(!stringsRelatos[i+4].equals("null")){
					relatoAux.setEndereco(stringsRelatos[i+4]);
					alterado = true;
				}
				if(!stringsRelatos[i+5].equals("null")){
					relatoAux.setCep(stringsRelatos[i+5]);
					alterado = true;
				}
			}
			//Retira o crime gravado da lista dos crimes;
			if(	alterado ){
				relatoBusiness.update(relatoAux);
				
				for(Relato r : relatos)
					if( r.getIdRelato() == id) {
						relatos.remove(r);
						break;
					}
			} else {
				for(Relato r : relatos)
					if( r.getIdRelato() == id) {
						relatosSemEnderecoEncontrado.add(r);
						break;
					}
			}
		}
		
		if(relatosSemEnderecoEncontrado != null && relatosSemEnderecoEncontrado.size() != 0){
			relato = new Relato();
			return "relatosNaoEncontrados";
		}
		else if (relatos.size()!=0)
			return null;
		else
			return "index";
	}
	
	public String atualizarEndereco() {
		Relato relatoAux = null;

		for (Relato r : relatosSemEnderecoEncontrado) {
			if (r.getIdRelato().longValue() == relato.getIdRelato().longValue()) {
				relatoAux = r;
				relatosSemEnderecoEncontrado.remove(r);
				relatos.remove(r);
				break;
			}
		}

		if (relato != null && relatoAux != null) {
			if (relato.getPais() != null && relato.getPais().length() > 0
					&& !relato.getPais().equals("null")
					&& !relato.getPais().equals("undefined"))
				relatoAux.setPais(relato.getPais());

			if (relato.getEstado() != null && relato.getEstado().length() > 0
					&& !relato.getEstado().equals("null")
					&& !relato.getEstado().equals("undefined"))
				relatoAux.setEstado(relato.getEstado());

			if (relato.getEndereco() != null && relato.getEndereco().length() > 0
					&& !relato.getEndereco().equals("null")
					&& !relato.getEndereco().equals("undefined"))
				relatoAux.setEndereco(relato.getEndereco());

			if (relato.getCidade() != null && relato.getCidade().length() > 0
					&& !relato.getCidade().equals("null")
					&& !relato.getCidade().equals("undefined"))
				relatoAux.setCidade(relato.getCidade());

			if (relato.getCep() != null && relato.getCep().length() > 0
					&& !relato.getCep().equals("null")
					&& !relato.getCep().equals("undefined"))
				relatoAux.setCep(relato.getCep());

			relatoAux.setLatitude(relato.getLatitude());
			relatoAux.setLongitude(relato.getLongitude());

			relatoBusiness.update(relatoAux);
		}

		if (relatosSemEnderecoEncontrado.size() != 0)
			return null;
		else if (relatos.size()!=0)
			return "enderecoRelatos";
		else
			return "index";
	}

	public String getStringRelatos() {
		return stringRelatos;
	}

	public void setStringRelatos(String stringRelatos) {
		this.stringRelatos = stringRelatos;
	}
	
	public List<Relato> getRelatos() {
		return relatos;
	}

	public void setRelatos(List<Relato> relatos) {
		this.relatos = relatos;
	}

	public int getQtdRelatos() {
		return  relatos.size();
	}

	public RelatoBusiness getRelatoBusiness() {
		return relatoBusiness;
	}

	public void setRelatoBusiness(RelatoBusiness relatoBusiness) {
		this.relatoBusiness = relatoBusiness;
	}
	
	public List<Relato> getRelatosSemEnderecoEncontrado() {
		return relatosSemEnderecoEncontrado;
	}

	public void setRelatosSemEnderecoEncontrado(
			List<Relato> relatosSemEnderecoEncontrado) {
		this.relatosSemEnderecoEncontrado = relatosSemEnderecoEncontrado;
	}

}
