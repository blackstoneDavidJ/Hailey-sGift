package src;

import java.io.File;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sound implements Serializable
{
	private String name;
	private String desc;
	private String creator;
	private String date;
	private File soundFile;
	
	public Sound(String name, String desc, String creator, String date, File soundFile)
	{
		this.name = name;
		this.desc = desc;
		this.creator = creator;
		this.date = date;
		this.soundFile = soundFile;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDesc() 
	{
		return desc;
	}

	public void setDesc(String desc) 
	{
		this.desc = desc;
	}

	public String getCreator() 
	{
		return creator;
	}

	public void setCreator(String creator) 
	{
		this.creator = creator;
	}

	public String getDate() 
	{
		return date;
	}

	public void setDate(String date) 
	{
		this.date = date;
	}

	public File getSoundFile() 
	{
		return soundFile;
	}

	public void setSoundFile(File soundFile) 
	{
		this.soundFile = soundFile;
	}
}
