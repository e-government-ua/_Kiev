package org.wf.dp.dniprorada.model;
public class Dimension {
	private String height;
	private String width;
	
	public Dimension() {
	}
	
	public Dimension(String height, String width){
		this.height = height;
		this.width = width;		
	}
	
	/**
	 * @return the {@linkplain #height}
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height the  {@linkplain #height} to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the {@linkplain #width}
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the  {@linkplain #width} to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
}


