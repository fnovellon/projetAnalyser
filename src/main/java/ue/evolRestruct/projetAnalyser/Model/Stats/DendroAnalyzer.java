package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.util.ArrayList;

import javax.swing.JFrame;

import ue.evolRestruct.projetAnalyser.Model.DendroPackage.DendroPaint;

public class DendroAnalyzer{
	
	public ArrayList<NodeCluster> clusters = new ArrayList<NodeCluster>();
	
	public void add(String s) {
		clusters.add(new NodeCluster(null, null, s));
	}
	
	public void merge(String s1, String s2, double value) {
		NodeCluster n1 = null;
		NodeCluster n2 = null;
		
		for (NodeCluster node : clusters) {
			if(node.value.equals(s1)) {
				n1 = node;
			}
			else if (node.value.equals(s2) ) {
				n2 = node;
			}
		}
		if(n1 == null || n2 == null) {
			System.out.println(s1 + " / " + s2);
			System.out.println("DENDRO : \n" +this);
		}
		NodeCluster nNodeCluster = new NodeCluster(n1, n2, "(" + s1 + " + " + s2 + ")");
		nNodeCluster.mergeValue = value;
		clusters.add(nNodeCluster);
		
		clusters.remove(n1);
		clusters.remove(n2);
	}
	
	public ArrayList<JFrame> buildDendro() {
		ArrayList<JFrame> frames = new ArrayList<JFrame>();
		
		for (NodeCluster node : clusters) {
			frames.add(openDendro(node));
		}
		return frames;
	}
	
	public static JFrame openDendro(NodeCluster node) {
		JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        DendroPaint dendro = new DendroPaint(DendroPaint.buildDendro(node));
        f.getContentPane().add(dendro);

        f.setSize(800,800);
        f.setLocationRelativeTo(null);
        return f;
	}
	
	public String toString() {
		String buffer = "";
		for (NodeCluster node : clusters) {
			buffer += node.toString() + "\n";
		}
		return buffer;
	}

	public ArrayList<NodeCluster> getClusters() {
		return clusters;
	}
	
	
	
    public static class NodeCluster{
		
		public String value;
		public NodeCluster left;
		public NodeCluster right;
		public double mergeValue = 0;
		
		public NodeCluster(NodeCluster l, NodeCluster r, String v) {
			this.left = l;
			this.right = r;
			this.value = v;
		}
		
		public String toString() {
			String buffer = value;
			buffer += " -->";
			buffer += (left == null) ? "(null" : "("+left.toString();
			buffer += ", ";
			buffer += (right == null) ? "null)" : right.toString()+")";
			return buffer;
		}
		
		public double coupleValue() {
			double sum = mergeValue;
			if(left != null && right != null) {
				sum += left.coupleValue() + right.coupleValue();
			}
			return sum;
		}
		
		
	}
	
}
