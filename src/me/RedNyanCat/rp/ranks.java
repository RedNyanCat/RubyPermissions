package me.RedNyanCat.rp;

public class ranks {

	private String rank;
	private String suffix;
	private String playerStatRank;
	private String playerStatSuffix;
	
	public ranks(String rank, String suffix, String playerStatRank, String playerStatSuffix){
		
		this.rank = rank;
		this.suffix = suffix;
		this.playerStatRank = playerStatRank;
		this.playerStatSuffix = playerStatSuffix;
		
	}
	
	public void setRank(String rank){
		
		this.rank = rank;
		
	}
	
	public String getRank(){
		
		return this.rank;
		
	}
	
	public void setSuffix(String suffix){
		
		this.suffix = suffix;
		
	}
	
	public String getSuffix(){
		
		return this.suffix;
		
	}
	
	public void setPlayerStatRank(String playerStatRank){
		
		this.playerStatRank = playerStatRank;
		
	}
	
	public String getPlayerStatRank(){
		
		return this.playerStatRank;
		
	}
	
	public void setPlayerStatSuffix(String playerStatSuffix){
		
		this.playerStatSuffix = playerStatSuffix;
		
	}
	
	public String getPlayerStatSuffix(){
		
		return this.playerStatSuffix;
		
	}
	
}
