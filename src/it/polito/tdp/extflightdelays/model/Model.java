package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	Graph<String, DefaultWeightedEdge> grafo;
	ExtFlightDelaysDAO dao=new ExtFlightDelaysDAO();
	Map<String, String> mappaStati;
	
	public void creaGrafo() {
		grafo=new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		mappaStati=new TreeMap<String, String>();
		List<String> stati=dao.loadAllStates();
		
		for(String s: stati) {
			mappaStati.put(s, s);
		}
		
		
		
		Graphs.addAllVertices(grafo, stati);
		int peso=0;
		
		for(String s1: stati) {
			for(String s2: stati) {
				peso=dao.getPeso(mappaStati,s1, s2);
				if(peso>0) {
					Graphs.addEdgeWithVertices(grafo, s1, s2, peso);
				}
			}
		}
		
		
		System.out.println("GRAFO CREATO "+grafo.vertexSet().size()+" vertici "+grafo.edgeSet().size());
		
		
	}

	public Collection<String> getStati() {
		return mappaStati.values();
	}
	
	
	
	public String elecoVoli(String stato) {
		
		String s="";
		List<DefaultWeightedEdge> archiUscenti= new LinkedList<DefaultWeightedEdge>();
		archiUscenti.addAll(grafo.outgoingEdgesOf(stato));
		
		Collections.sort(archiUscenti, new Comparator<DefaultWeightedEdge>(){

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				return (int)grafo.getEdgeWeight(o2) - (int)grafo.getEdgeWeight(o1);
			}
			
			});
		
		
		for(DefaultWeightedEdge e: archiUscenti) {
			s=s+grafo.getEdgeTarget(e)+" "+grafo.getEdgeWeight(e)+"\n";
		}
		
		
		return s;
	}
	
	

	public Graph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<String, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	
	
	
	
}
